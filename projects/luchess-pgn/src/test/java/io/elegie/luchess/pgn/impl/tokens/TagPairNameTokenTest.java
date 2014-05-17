package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TagPairNameTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new TagPairNameToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        final int[] acceptableValues = new int[52];
        for (int c = 'a', ii = 0; c <= 'z'; c++, ii++) {
            acceptableValues[ii] = c;
        }
        for (int c = 'A', ii = 26; c <= 'Z'; c++, ii++) {
            acceptableValues[ii] = c;
        }
        return acceptableValues;
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { '1', '_', '|' };
    }

    @Test
    public void testTagPairNameSuccess() throws ParseException {
        testAcceptableValues(TokenType.TAG_PAIR_NAME);
        testMatch(TokenType.TAG_PAIR_NAME, "tagName");
    }

    @Test
    public void testTruncation() throws ParseException {
        testMatch(TokenType.TAG_PAIR_NAME, "tagName42", "tagName");
    }

}
