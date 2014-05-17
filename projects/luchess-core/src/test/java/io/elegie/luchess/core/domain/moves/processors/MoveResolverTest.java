package io.elegie.luchess.core.domain.moves.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.moves.AbstractMove;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests that the proper move is resolved when the resolver has been provided
 * with a position and valid board coordinates.
 */
@SuppressWarnings("javadoc")
public class MoveResolverTest {

    private static final Color color = Color.WHITE;
    private static final Vertex disambiguation = new Vertex('e', 1);

    private MoveResolver resolver;
    private Position position;

    @Before
    public void setUp() {
        position = new Position();
        resolver = new MoveResolver();
        resolver.setPosition(position);
        resolver.setDisambiguation(disambiguation);
    }

    @Test
    public void testInvalidInputs() {
        resolver.setPosition(null);
        resolver.setDisambiguation(null);
        assertNull(resolver.resolve());

        resolver.setPosition(position);
        assertNull(resolver.resolve());

        resolver.setTarget(new Vertex('z', 9));
        assertNull(resolver.resolve());
        resolver.setTarget(new Vertex('e', 4));
        assertNull(resolver.resolve());

        resolver.setDisambiguation(new Vertex('z', 9));
        assertNull(resolver.resolve());
        resolver.setDisambiguation(new Vertex('e', 4));
        assertNull(resolver.resolve());
    }

    @Test
    public void testBishopMove() throws IllegalMoveException {
        testResolve(Figurine.BISHOP, disambiguation.go(Direction.NORTH_EAST));
    }

    @Test
    public void testSmallCastleMove() throws IllegalMoveException {
        Vertex rookStart = new Vertex('h', 1);
        Vertex rookEnd = new Vertex('f', 1);
        position.setPiece(rookStart, new Piece(Figurine.ROOK, color));
        testResolve(Figurine.KING, disambiguation.go(Direction.EAST).go(Direction.EAST));
        assertNull(position.getPiece(rookStart));
        assertEquals(Figurine.ROOK, position.getPiece(rookEnd).getFigurine());
    }

    @Test
    public void testLargeCastleMove() throws IllegalMoveException {
        Vertex rookStart = new Vertex('a', 1);
        Vertex rookEnd = new Vertex('d', 1);
        position.setPiece(rookStart, new Piece(Figurine.ROOK, color));
        testResolve(Figurine.KING, disambiguation.go(Direction.WEST).go(Direction.WEST));
        assertNull(position.getPiece(rookStart));
        assertEquals(Figurine.ROOK, position.getPiece(rookEnd).getFigurine());
    }

    @Test
    public void testKingMove() throws IllegalMoveException {
        testResolve(Figurine.KING, disambiguation.go(Direction.NORTH_EAST));
    }

    @Test
    public void testKnightMove() throws IllegalMoveException {
        testResolve(Figurine.KNIGHT, disambiguation.go(Direction.NORTH_EAST).go(Direction.NORTH));
    }

    @Test
    public void testCapturePawnMove() throws IllegalMoveException {
        Vertex target = disambiguation.go(Direction.NORTH_EAST);
        position.setPiece(target, new Piece(Figurine.PAWN, color.invert()));
        testResolve(Figurine.PAWN, target);
    }

    @Test
    public void testCapturePawnMovePromotion() throws IllegalMoveException {
        Vertex origin = new Vertex('e', 7);
        Vertex target = origin.go(Direction.NORTH_EAST);
        position.setPiece(target, new Piece(Figurine.BISHOP, color.invert()));
        resolver.setDisambiguation(origin);
        resolver.setPromotedFigurine(Figurine.QUEEN);
        testResolve(Figurine.PAWN, Figurine.QUEEN, target);
    }

    @Test
    public void testCapturePawnMoveEnPassant() throws IllegalMoveException {
        Vertex origin = new Vertex('e', 5);
        Vertex target = origin.go(Direction.NORTH_EAST);
        position.setPiece(target.go(Direction.SOUTH), new Piece(Figurine.PAWN, color.invert()));
        resolver.setDisambiguation(origin);
        testResolve(Figurine.PAWN, target);
    }

    @Test
    public void testForwardPawnMove() throws IllegalMoveException {
        testResolve(Figurine.PAWN, disambiguation.go(Direction.NORTH));
    }

    @Test
    public void testForwardPawnMovePromotion() throws IllegalMoveException {
        Vertex origin = new Vertex('e', 7);
        resolver.setDisambiguation(origin);
        resolver.setPromotedFigurine(Figurine.QUEEN);
        testResolve(Figurine.PAWN, Figurine.QUEEN, origin.go(Direction.NORTH));
    }

    @Test
    public void testQueenMove() throws IllegalMoveException {
        testResolve(Figurine.QUEEN, disambiguation.go(Direction.NORTH_EAST));
    }

    @Test
    public void testRookMove() throws IllegalMoveException {
        testResolve(Figurine.ROOK, disambiguation.go(Direction.NORTH));
    }

    private void testResolve(Figurine figurine, Vertex target) throws IllegalMoveException {
        testResolve(figurine, figurine, target);
    }

    private void testResolve(Figurine startFigurine, Figurine endFigurine, Vertex target) throws IllegalMoveException {
        resolver.setTarget(target);
        position.setPiece(resolver.getDisambiguation(), new Piece(startFigurine, color));
        AbstractMove move = resolver.resolve();
        assertNotNull(move);
        move.apply(color, position);
        assertNull(position.getPiece(resolver.getDisambiguation()));
        assertEquals(endFigurine, position.getPiece(target).getFigurine());
    }

    // --- Other --------------------------------------------------------------

    @Test
    public void testMutators() {
        Vertex target = new Vertex('f', 5);
        Figurine promotedFigurine = Figurine.QUEEN;
        position.init();

        resolver.setPromotedFigurine(promotedFigurine);
        resolver.setTarget(target);

        assertEquals(disambiguation, resolver.getDisambiguation());
        assertEquals(position, resolver.getPosition());
        assertEquals(promotedFigurine, resolver.getPromotedFigurine());
        assertEquals(target, resolver.getTarget());
    }

}
