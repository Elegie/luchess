package io.elegie.luchess.pgn.impl.tokens.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.elegie.luchess.pgn.api.OpenTokenVisitor;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.Token;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

/**
 * This class is used to automate token testing. It verifies that all acceptable
 * values are accepted, that non-acceptable values are rejected, and also
 * provides "testMatch" methods to let subclasses verify that a given string is
 * successfully matched by the target token.
 */
@SuppressWarnings("javadoc")
public abstract class AbstractTokenTest {

    /**
     * @return A new token, of the token type being tested.
     */
    protected abstract Token createToken();

    /**
     * @return List of values which the token accepts to process.
     */
    protected abstract int[] getAcceptableValues();

    /**
     * @return List of values which the token does not accept to process.
     */
    protected abstract int[] getNonAcceptableValues();

    // --- Tests --------------------------------------------------------------

    @Test
    public void testAccept() {
        final Token token = createToken();
        for (int acceptableValue : getAcceptableValues()) {
            assertTrue(token.accepts(acceptableValue));
        }
        for (int nonAcceptableValue : getNonAcceptableValues()) {
            assertFalse(token.accepts(nonAcceptableValue));
        }
    }

    protected void testAcceptableValues(TokenType type) throws ParseException {
        for (int acceptable : getAcceptableValues()) {
            testMatch(type, Character.toString((char) acceptable));
        }
    }

    protected Token testMatch(TokenType tokenType, String testValue) throws ParseException {
        return testMatch(tokenType, testValue, testValue);
    }

    protected Token testMatch(TokenType tokenType, String testValue, String expectedValue) throws ParseException {
        final Token token = createToken();
        final Reader reader = new StringReader(testValue);
        final OpenTokenVisitor visitor = new OpenTokenVisitor();
        try {
            token.interpret(reader.read(), reader, visitor);
        } catch (IOException e) {
            String message = "Cannot read token (%s).";
            message = String.format(message, e.getMessage());
            fail(message);
        }
        if (tokenType != null) {
            assertEquals(expectedValue, visitor.getValue(tokenType));
        }
        return token;
    }

    // --- toString -----------------------------------------------------------

    @Override
    public String toString() {
        String tokenClass = this.getClass().getSimpleName();
        String value = "Token {type: %s}";
        return String.format(value, tokenClass);
    }

}
