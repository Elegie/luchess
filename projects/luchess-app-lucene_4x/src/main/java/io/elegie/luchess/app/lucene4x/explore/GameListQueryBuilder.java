package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.app.lucene4x.index.analysis.MoveTextTokenizer;
import io.elegie.luchess.app.lucene4x.index.mapping.FieldMapper;
import io.elegie.luchess.core.domain.entities.MoveText;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.payloads.PayloadTermQuery;
import org.apache.lucene.util.Version;

/**
 * This class is used to create a
 * {@link io.elegie.luchess.core.api.explore.GameListQuery}, combining the
 * different filled search criteria into the target query. Internally, we build
 * a big BooleanQuery, articulating clauses for all criteria.
 */
public class GameListQueryBuilder {

    private Version version;
    private Analyzer analyzer;
    private int analysisDepth;

    /**
     * @param version
     *            The Lucene engine.
     * @param analyzer
     *            The analyzer used for indexing (to be used with a query
     *            parser).
     * @param analysisDepth
     *            The opening moves depth.
     */
    public GameListQueryBuilder(Version version, Analyzer analyzer, int analysisDepth) {
        this.version = version;
        this.analyzer = analyzer;
        this.analysisDepth = analysisDepth;
    }

    /**
     * Creates the query, combining all parameters. Combinations are made with a
     * boolean AND (Lucene's BooleanClause.Occur.MUST), empty values not being
     * taken into account.
     * 
     * @param white
     *            The name for White (case-insensitive). Many names can be
     *            specified, if separated by a comma. Exact match can be
     *            requested by enclosing the name within double quotes.
     * @param black
     *            The name for Black. Same rules as for the name for White.
     * @param elo
     *            The minimum elo.
     * @param moves
     *            The starting moves of the searched games. Will only consider
     *            moves up to the first null or empty entry in the list.
     * @return A query to be executed against the index.
     */
    public Query createQuery(String white, String black, int elo, List<String> moves) {
        BooleanQuery query = new BooleanQuery();
        try {
            addPlayers(query, white, black);
            addElo(query, elo);
            addMoves(query, moves);
        } catch (ParseException e) {
            String message = "Cannot parse some values provided to the query.";
            throw new IllegalArgumentException(message, e);
        }
        return new GameListQuerySorter(query);
    }

    // --- Helpers for Players ------------------------------------------------

    private void addPlayers(BooleanQuery query, String white, String black) throws ParseException {
        boolean whiteSet = white != null && !white.isEmpty();
        boolean blackSet = black != null && !black.isEmpty();
        if (whiteSet && blackSet && white.equals(black)) {
            BooleanQuery anyColor = new BooleanQuery();
            anyColor.add(createPlayerQuery(white, FieldMapper.WHITE), BooleanClause.Occur.SHOULD);
            anyColor.add(createPlayerQuery(black, FieldMapper.BLACK), BooleanClause.Occur.SHOULD);
            query.add(anyColor, BooleanClause.Occur.MUST);
        } else {
            if (whiteSet) {
                query.add(createPlayerQuery(white, FieldMapper.WHITE), BooleanClause.Occur.MUST);
            }
            if (blackSet) {
                query.add(createPlayerQuery(black, FieldMapper.BLACK), BooleanClause.Occur.MUST);
            }
        }
    }

    private Query createPlayerQuery(String player, FieldMapper color) throws ParseException {
        String[] names = player.split(",");
        StringBuilder builder = new StringBuilder();
        String quotedStringPattern = "^\".+\"$";
        boolean first = true;
        for (String name : names) {
            if (!first) {
                builder.append(" ");
            }
            String fuzzyQualifier = "~2";
            if (name.matches(quotedStringPattern)) {
                fuzzyQualifier = "";
                name = name.substring(1, name.length() - 1);
            }
            builder.append(color.toString()).append(":").append(queryEscape(name.trim())).append(fuzzyQualifier);
            first = false;
        }
        QueryParser parser = new QueryParser(version, color.toString(), analyzer);
        return parser.parse("(" + builder.toString() + ")");
    }

    // --- Helpers for elo ----------------------------------------------------

    /**
     * Beyond this unreasonable value ({@value #MAX_ELO}), the query will not be
     * built.
     */
    public static final int MAX_ELO = 5000;

    private void addElo(BooleanQuery query, int elo) {
        if (elo > 0) {
            query.add(createEloQuery(elo), BooleanClause.Occur.MUST);
        }
    }

    private Query createEloQuery(int elo) {
        if (elo > MAX_ELO) {
            String message = "Specified elo is too high (elo=%s, max=%s)";
            message = String.format(message, elo, MAX_ELO);
            throw new IllegalArgumentException(message);
        }

        BooleanQuery query = new BooleanQuery();
        Query whiteRange = NumericRangeQuery.newIntRange(FieldMapper.WHITE_ELO.toString(), elo, MAX_ELO, true, true);
        Query blackRange = NumericRangeQuery.newIntRange(FieldMapper.BLACK_ELO.toString(), elo, MAX_ELO, true, true);
        query.add(whiteRange, BooleanClause.Occur.SHOULD);
        query.add(blackRange, BooleanClause.Occur.SHOULD);
        return query;
    }

    // --- Helpers for Moves --------------------------------------------------

    private void addMoves(BooleanQuery query, List<String> moves) {
        // Create the moves list, and calculate the actual depth
        StringBuilder builder = new StringBuilder().append(MoveTextTokenizer.MOVE_ZERO);
        int actualDepth = 0;
        for (String move : moves) {
            String value = "";
            if (move != null) {
                value = move.trim();
            }
            if (value.isEmpty()) {
                break;
            }
            builder.append(MoveText.SEPARATOR);
            value = MoveTextTokenizer.filter(value);
            value = queryEscape(value);
            builder.append(value);
            actualDepth++;
        }

        // Create the query
        String result = builder.toString();
        Term moveText = new Term(FieldMapper.MOVE_TEXT.toString(), result);
        if (actualDepth < analysisDepth) {
            query.add(new PayloadTermQuery(moveText, new GameListQueryPayloadFunction()), BooleanClause.Occur.MUST);
        } else {
            query.add(new PrefixQuery(moveText), BooleanClause.Occur.MUST);
        }
    }

    // --- Common Helpers -----------------------------------------------------

    private static final char[] CHARS_TO_ESCAPE = new char[] {
            '+', '-', '&', '|', '!', '(', ')', '{', '}', '[', ']',
            '^', '"', '~', '*', '?', ':', '\\', '/'
    };

    private String queryEscape(String source) {
        StringBuilder regexp = new StringBuilder();
        regexp.append("([");
        for (char token : CHARS_TO_ESCAPE) {
            regexp.append("\\").append(token);
        }
        regexp.append("])");
        return source.replaceAll(regexp.toString(), "$1");
    }

}
