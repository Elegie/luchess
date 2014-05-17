package io.elegie.luchess.app.lucene4x.index.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.BytesRef;

/**
 * This tokenizer will receive a sequence of moves, separated by a character
 * passed to the constructor, and will tokenize them, respecting the following
 * rules:
 * <ul>
 * <li>The whole sequence will be made into a single token, so as to allow
 * PrefixQueries.</li>
 * <li>Opening moves sequences will be made into tokens as well, up to the
 * analysis depth, so as to allow for fast search, using TermQueries. For
 * instance, tokens such as "e4", "e4 e5", "e4 e5 Nf3" will be generated.</li>
 * <li>All tokens for opening moves will have a payload containing the next
 * move, so that continuations may be calculated in an efficient way. The token
 * containing the whole sequence of moves will have no payload, because it
 * already has all the moves. In practice, this means that continuations will be
 * available only for sequences of moves of a length inferior to the analysis
 * depth.</li>
 * </ul>
 */
public class MoveTextTokenizer extends Tokenizer {

    /**
     * This is the first token for all move texts, i.e. "move zero".
     */
    public static final char MOVE_ZERO = 'X';

    /**
     * Since we have an internal "move zero", we need to increase the actual
     * analysis depth by one.
     */
    private static final int MOVE_ZERO_ANALYSIS_OFFSET = 1;

    /**
     * The maximal PGN move length (e.g. Nf3xg1, exd8=Q), minus the check sign,
     * used to initialize capacities of our subsequent buffers.
     */
    private static final int MAX_MOVE_LENGTH = 6;

    private final int analysisDepth;
    private final char moveSeparator;
    private List<Byte> previousMove;
    private List<List<Byte>> allMoves;
    private boolean finished;

    private CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private PositionIncrementAttribute positionAtt = addAttribute(PositionIncrementAttribute.class);
    private PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);

    protected MoveTextTokenizer(Reader input, int analysisDepth, char moveSeparator) {
        super(input);
        this.analysisDepth = analysisDepth + MOVE_ZERO_ANALYSIS_OFFSET;
        this.moveSeparator = moveSeparator;
    }

    @Override
    public final void reset() throws IOException {
        super.reset();
        previousMove = null;
        allMoves = new ArrayList<>();
        finished = false;
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (finished) {
            return false;
        }
        initialize();
        return process();
    }

    private void initialize() {
        clearAttributes();
        if (previousMove == null) {
            previousMove = Arrays.asList((byte) MOVE_ZERO);
        } else {
            positionAtt.setPositionIncrement(0);
        }
    }

    private boolean process() throws IOException {
        allMoves.add(previousMove);
        List<Byte> currentMove = readNextMove();
        if (allMoves.size() > analysisDepth) {
            while (!currentMove.isEmpty()) {
                allMoves.add(currentMove);
                currentMove = readNextMove();
            }
        }
        termAtt.append(new String(getAllMoves(), "UTF-8"));
        if (currentMove.isEmpty()) {
            finished = true;
        } else {
            payloadAtt.setPayload(new BytesRef(getBytes(currentMove)));
            previousMove = currentMove;
        }
        return true;
    }

    private List<Byte> readNextMove() throws IOException {
        List<Byte> result = new ArrayList<>(MAX_MOVE_LENGTH);
        int data = input.read();
        while (data != moveSeparator && data != -1) {
            if (accept(data)) {
                result.add((byte) data);
            }
            data = input.read();
        }
        return result;
    }

    private byte[] getAllMoves() {
        List<Byte> result = new ArrayList<>();
        int size = allMoves.size();
        for (int ii = 0; ii < size; ii++) {
            result.addAll(allMoves.get(ii));
            if (ii < size - 1) {
                result.add((byte) moveSeparator);
            }
        }
        return getBytes(result);
    }

    private byte[] getBytes(List<Byte> source) {
        byte[] target = new byte[source.size()];
        for (int j = 0; j < source.size(); j++) {
            target[j] = source.get(j);
        }
        return target;
    }

    // --- Filtering check marks out ------------------------------------------

    /**
     * The chars to be excluded from the produced tokens. These are the chess
     * check marks, and we remove them because they are optional, and could
     * therefore be omitted.
     */
    private static final List<Character> EXCLUDE_CHARS = Arrays.asList('+', '#');

    /**
     * @param value
     *            The char to be tested
     * @return True if the char should be accepted in the final indexed
     *         representation of the move.
     */
    private static boolean accept(int value) {
        return !EXCLUDE_CHARS.contains((char) value);
    }

    /**
     * @param value
     *            The raw text representation of the move.
     * @return The final representation, with optional parts excluded.
     */
    public static String filter(String value) {
        StringBuilder result = new StringBuilder();
        for (int ii = 0; ii < value.length(); ii++) {
            char part = value.charAt(ii);
            if (accept(part)) {
                result.append(part);
            }
        }
        return result.toString();
    }

}
