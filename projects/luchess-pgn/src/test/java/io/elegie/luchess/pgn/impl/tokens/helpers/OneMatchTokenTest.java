package io.elegie.luchess.pgn.impl.tokens.helpers;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.Token;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class automates some tests for tokens made of only one character.
 * Namely, it helps checking that only the one character is accepted, and not
 * others.
 */
@SuppressWarnings("javadoc")
public abstract class OneMatchTokenTest extends AbstractTokenTest {

    private static final Logger LOG = LoggerFactory.getLogger(OneMatchTokenTest.class);

    private Class<? extends Token> tokenClass;
    private TokenType tokenType;
    private int acceptableValue;
    private String testValue;

    protected OneMatchTokenTest(Class<? extends Token> tokenClass, TokenType tokenType, int acceptableValue) {
        this(tokenClass, tokenType, acceptableValue, Character.toString((char) acceptableValue));
    }

    protected OneMatchTokenTest(Class<? extends Token> tokenClass, TokenType tokenType, int acceptableValue,
            String testValue) {
        this.tokenClass = tokenClass;
        this.tokenType = tokenType;
        this.acceptableValue = acceptableValue;
        this.testValue = testValue;
    }

    @Override
    protected Token createToken() {
        try {
            return tokenClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "Cannot instantiate the token (%s).";
            message = String.format(message, e.getClass().getSimpleName());
            LOG.error(message, e);
            return null;
        }
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { acceptableValue };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'Z' };
    }

    @Test
    public void testSuccess() throws ParseException {
        testMatch(tokenType, testValue);
    }

}
