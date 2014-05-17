package io.elegie.luchess.app.lucene4x.index.mapping;

import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.MoveText;
import io.elegie.luchess.core.domain.entities.Result;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;

/**
 * Provides methods to convert a game to a Lucene document, and a Lucene
 * document to a game.
 */
public final class IndexMapper {

    private IndexMapper() {

    }

    /**
     * @param doc
     *            A Lucene document representing a game.
     * @return A game built from the document.
     */
    public static Game createGame(Document doc) {
        Game game = new Game();
        game.setId(getStringValue(doc, FieldMapper.ID));
        game.setWhite(getStringValue(doc, FieldMapper.WHITE));
        game.setBlack(getStringValue(doc, FieldMapper.BLACK));
        game.setWhiteElo(getIntValue(doc, FieldMapper.WHITE_ELO));
        game.setBlackElo(getIntValue(doc, FieldMapper.BLACK_ELO));
        game.setResult(Result.parse(getIntValue(doc, FieldMapper.RESULT)));

        String moveTextValue = getStringValue(doc, FieldMapper.MOVE_TEXT);
        String separator = Character.toString(MoveText.SEPARATOR);
        MoveText moveText = game.getMoveText();
        for (String move : moveTextValue.split(separator)) {
            moveText.addValue(move);
        }
        return game;
    }

    /**
     * @param game
     *            A game.
     * @return A Lucene document filled with the game.
     */
    public static Document createDocument(Game game) {
        Document doc = new Document();
        mapTextField(doc, FieldMapper.ID, game.getId());
        mapTextField(doc, FieldMapper.WHITE, game.getWhite());
        mapTextField(doc, FieldMapper.BLACK, game.getBlack());
        mapPositiveIntField(doc, FieldMapper.WHITE_ELO, game.getWhiteElo());
        mapPositiveIntField(doc, FieldMapper.BLACK_ELO, game.getBlackElo());
        mapIntField(doc, FieldMapper.RESULT, game.getResult().getIntValue());
        mapTextField(doc, FieldMapper.MOVE_TEXT, game.getMoveText().getValue());
        return doc;
    }

    // --- Helpers Doc to Game ------------------------------------------------

    private static String getStringValue(Document doc, FieldMapper gameField) {
        IndexableField field = doc.getField(gameField.toString());
        String result = "";
        if (field != null) {
            result = field.stringValue();
        }
        return result;
    }

    private static int getIntValue(Document doc, FieldMapper gameField) {
        IndexableField field = doc.getField(gameField.toString());
        int result = 0;
        if (field != null) {
            result = field.numericValue().intValue();
        }
        return result;
    }

    // --- Helpers Game to Doc ------------------------------------------------

    private static void mapTextField(Document doc, FieldMapper gameField, String value) {
        if (value != null) {
            TextField field = new TextField(gameField.toString(), value, Field.Store.YES);
            doc.add(field);
        }
    }

    private static void mapIntField(Document doc, FieldMapper gameField, int value) {
        IntField field = new IntField(gameField.toString(), value, IntField.Store.YES);
        doc.add(field);
    }

    private static void mapPositiveIntField(Document doc, FieldMapper gameField, int value) {
        if (value > 0) {
            mapIntField(doc, gameField, value);
        }
    }

}
