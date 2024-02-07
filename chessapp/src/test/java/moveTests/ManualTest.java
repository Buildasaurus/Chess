package moveTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Piece;
import chess.Models.Point;
import chess.Models.Piece.PieceType;

public class ManualTest
{
    Board board;

    public ManualTest()
    {
        board = new Board();
    }

    boolean compare(Move[] moves, Move[] correctMoves)
    {
        ArrayList<Move> generatedMovesList = new ArrayList<>(Arrays.asList(moves));
        ArrayList<Move> missingMovesList = new ArrayList<>(Arrays.asList(correctMoves));

        boolean testSucced = true;
        int counter = 0;
        for (Move move : correctMoves)
        {
            boolean found = false;
            for (Move move2 : moves)
            {
                if (move.equals(move2))
                {
                    generatedMovesList.remove(move2);
                    missingMovesList.remove(move);

                    found = true;
                    break;
                }
            }
            if (found == true)
            {
                counter++;
            }
            else
            {
                testSucced = false;
            }
        }
        if (!testSucced)
        {
            System.out.printf(
                    "Test result: %b \t %d/%d moves correct moves \t %d incorrect moves \t %d missing moves\n",
                    testSucced, counter, correctMoves.length, moves.length - counter,
                    correctMoves.length - counter);
            if (generatedMovesList.size() > 0)
            {
                System.out.println("These moves shouldn't be here: " + generatedMovesList);
            }
            if (correctMoves.length - counter > 0)
            {
                System.out.println("These moves are missing: " + missingMovesList);

            }
        }
        return testSucced;
    }

    @Test
    void mateTest()
    {
        board = new Board();
        // System.out.println("<<<MATE TEST>>>");


        board.tryToMakeMove(new Point(4, 1), new Point(4, 3)); // 1.e4
        board.tryToMakeMove(new Point(1, 7), new Point(2, 5)); // 1.Nc6
        board.tryToMakeMove(new Point(3, 0), new Point(7, 4)); // 2.Qh4
        board.tryToMakeMove(new Point(2, 5), new Point(1, 7)); // 2.Nb8
        board.tryToMakeMove(new Point(5, 0), new Point(2, 3)); // 3.Bc4
        board.tryToMakeMove(new Point(1, 7), new Point(2, 5)); // 3.Nc6
        board.tryToMakeMove(new Point(7, 4), new Point(5, 6)); // 4.Qf6#

        Move[] correctMoves = new Move[0];
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        // part two: King is not safe behind itself.
        board = new Board();


        board.tryToMakeMove(new Point(4, 1), new Point(4, 2)); // 1.e3
        board.tryToMakeMove(new Point(4, 6), new Point(4, 4)); // 1.e5
        board.tryToMakeMove(new Point(3, 0), new Point(7, 4)); // 2.Qh5
        board.tryToMakeMove(new Point(4, 7), new Point(4, 6)); // 2.Ke7
        board.tryToMakeMove(new Point(7, 4), new Point(4, 4)); // 3.Qxe5#

        correctMoves = new Move[0];
        assertTrue(compare(board.getLegalMoves(), correctMoves));

    }

    @Test
    void broadTest()
    {
        // System.out.println("<<<BROAD TEST>>>");
        // Start position test
        Move[] correctMoves =
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(5, 2)), new Move(new Point(6, 0), new Point(7, 2))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));


        // bishop on diagonal moves test.

        board.tryToMakeMove(new Point(6, 1), new Point(6, 3)); // 1.g4
        board.tryToMakeMove(new Point(6, 6), new Point(6, 5)); // 1.g6

        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 3), new Point(6, 4)), new Move(new Point(7, 1), new Point(7, 2)),
            new Move(new Point(7, 1), new Point(7, 3)), new Move(new Point(1, 0), new Point(0, 2)),
            new Move(new Point(1, 0), new Point(2, 2)), new Move(new Point(6, 0), new Point(5, 2)),
            new Move(new Point(6, 0), new Point(7, 2)), new Move(new Point(5, 0), new Point(6, 1)),
            new Move(new Point(5, 0), new Point(7, 2))};

        assertTrue(compare(board.getLegalMoves(), correctMoves));

        // can't move king into check test

        board.tryToMakeMove(new Point(3, 1), new Point(3, 3)); // 2.d5
        board.tryToMakeMove(new Point(5, 7), new Point(7, 5)); // 2.bh6

        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 3), new Point(3, 4)), new Move(new Point(4, 1), new Point(4, 2)),
            new Move(new Point(4, 1), new Point(4, 3)), new Move(new Point(5, 1), new Point(5, 2)),
            new Move(new Point(5, 1), new Point(5, 3)), new Move(new Point(6, 3), new Point(6, 4)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(5, 2)), new Move(new Point(6, 0), new Point(7, 2)),
            new Move(new Point(5, 0), new Point(6, 1)), new Move(new Point(5, 0), new Point(7, 2)),
            new Move(new Point(3, 0), new Point(3, 1)), new Move(new Point(3, 0), new Point(3, 2)),
            new Move(new Point(2, 0), new Point(3, 1)), new Move(new Point(2, 0), new Point(4, 2)),
            new Move(new Point(2, 0), new Point(5, 3)), new Move(new Point(2, 0), new Point(6, 4)),
            new Move(new Point(2, 0), new Point(7, 5)), new Move(new Point(1, 0), new Point(3, 1))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        board.tryToMakeMove(new Point(2, 0), new Point(7, 5)); // Bxh6
        board.tryToMakeMove(new Point(6, 7), new Point(7, 5)); // Nxh6
        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 3), new Point(3, 4)), new Move(new Point(4, 1), new Point(4, 2)),
            new Move(new Point(4, 1), new Point(4, 3)), new Move(new Point(5, 1), new Point(5, 2)),
            new Move(new Point(5, 1), new Point(5, 3)), new Move(new Point(6, 3), new Point(6, 4)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(5, 2)), new Move(new Point(6, 0), new Point(7, 2)),
            new Move(new Point(5, 0), new Point(6, 1)), new Move(new Point(5, 0), new Point(7, 2)),
            new Move(new Point(3, 0), new Point(3, 1)), new Move(new Point(3, 0), new Point(3, 2)),
            new Move(new Point(1, 0), new Point(3, 1)), new Move(new Point(4, 0), new Point(3, 1)),
            new Move(new Point(3, 0), new Point(2, 0))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));
        board.tryToMakeMove(new Point(3, 0), new Point(3, 1)); // Qd2
        board.tryToMakeMove(new Point(7, 5), new Point(5, 4)); // Nf5
        board.tryToMakeMove(new Point(3, 1), new Point(7, 5)); // Qh6
        board.tryToMakeMove(new Point(5, 4), new Point(3, 3)); // Nxd4
        board.tryToMakeMove(new Point(7, 5), new Point(5, 7)); // Qf8+
        correctMoves = new Move[]
        {new Move(new Point(4, 7), new Point(5, 7)), new Move(new Point(7, 7), new Point(5, 7))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        board.tryToMakeMove(new Point(4, 7), new Point(5, 7)); // Kxf8
        board.tryToMakeMove(new Point(4, 0), new Point(3, 0)); // Kd1
        board.tryToMakeMove(new Point(1, 7), new Point(2, 5)); // Nc6
        board.tryToMakeMove(new Point(3, 0), new Point(4, 0)); // Ke1
        board.tryToMakeMove(new Point(3, 3), new Point(2, 1)); // Nxc2+

        correctMoves = new Move[]
        {new Move(new Point(4, 0), new Point(3, 0)), new Move(new Point(4, 0), new Point(3, 1))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));


        board.tryToMakeMove(new Point(4, 0), new Point(3, 1)); // Kd2
        board.tryToMakeMove(new Point(1, 6), new Point(1, 5)); // b6
        board.tryToMakeMove(new Point(3, 1), new Point(3, 2)); // Kd3
        board.tryToMakeMove(new Point(2, 7), new Point(0, 5)); // Ba6+
        correctMoves = new Move[]
        {new Move(new Point(3, 2), new Point(3, 1)), new Move(new Point(3, 2), new Point(2, 2)),
            new Move(new Point(3, 2), new Point(2, 1)), new Move(new Point(3, 2), new Point(4, 3))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        board.tryToMakeMove(new Point(3, 2), new Point(4, 3)); // Ke4
        board.tryToMakeMove(new Point(4, 6), new Point(4, 5)); // e6
        board.tryToMakeMove(new Point(4, 3), new Point(5, 2)); // Kf3
        board.tryToMakeMove(new Point(3, 7), new Point(5, 5)); // Qf6+
        correctMoves = new Move[]
        {new Move(new Point(5, 2), new Point(6, 2)), new Move(new Point(5, 2), new Point(4, 3)),
            new Move(new Point(5, 2), new Point(6, 1))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        board.tryToMakeMove(new Point(5, 2), new Point(6, 2)); // Kg3
        board.tryToMakeMove(new Point(5, 5), new Point(4, 4)); // Qe5+
    }

    @Test
    void testEnPassent()
    {
        board = new Board();
        // System.out.println("<<<EN PASSENT TEST>>>");


        board.tryToMakeMove(new Point(6, 1), new Point(6, 3)); // 1.g4
        board.tryToMakeMove(new Point(6, 6), new Point(6, 5)); // 1.g6
        board.tryToMakeMove(new Point(6, 3), new Point(6, 4)); // 2.g5
        board.tryToMakeMove(new Point(5, 6), new Point(5, 4)); // 2.f5

        Move[] correctMoves =
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 4), new Point(5, 5)), new Move(new Point(7, 1), new Point(7, 2)),
            new Move(new Point(7, 1), new Point(7, 3)), new Move(new Point(1, 0), new Point(0, 2)),
            new Move(new Point(1, 0), new Point(2, 2)), new Move(new Point(6, 0), new Point(5, 2)),
            new Move(new Point(6, 0), new Point(7, 2)), new Move(new Point(5, 0), new Point(6, 1)),
            new Move(new Point(5, 0), new Point(7, 2))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        board.tryToMakeMove(new Point(6, 4), new Point(5, 5)); // 3.gxf



        if (board.getPieceAtPoint(new Point(4, 5)) != null)
        {
            fail("En passent failed to remove the pawn it captured");
        }


    }

    @Test
    void testKingLegalMoves()
    {
        board = new Board();
        // System.out.println("<<<KING LEGAL MOVES TEST>>>");


        board.tryToMakeMove(new Point(5, 1), new Point(5, 2)); // 1.f3
        board.tryToMakeMove(new Point(4, 6), new Point(4, 5)); // 1.e6
        board.tryToMakeMove(new Point(4, 0), new Point(5, 1)); // 2.Kf2
        board.tryToMakeMove(new Point(1, 7), new Point(2, 5)); // 2.Nc6
        board.tryToMakeMove(new Point(5, 1), new Point(4, 2)); // 2.Ke3
        board.tryToMakeMove(new Point(5, 7), new Point(4, 6)); // 2.Be7
        board.tryToMakeMove(new Point(4, 2), new Point(5, 3)); // 4.Kf4
        board.tryToMakeMove(new Point(6, 7), new Point(5, 5)); // 4.Nf6



        Move[] correctMoves =
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(7, 2)), new Move(new Point(5, 3), new Point(6, 4)),
            new Move(new Point(5, 3), new Point(6, 2)), new Move(new Point(5, 3), new Point(4, 2)),
            new Move(new Point(3, 0), new Point(4, 0))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));


        // part two, it is safe behind a friendly piece.

        board = new Board();


        board.tryToMakeMove(new Point(5, 1), new Point(5, 2)); // 1.f3
        board.tryToMakeMove(new Point(6, 6), new Point(6, 5)); // 1.g6
        board.tryToMakeMove(new Point(4, 0), new Point(5, 1)); // 2.Kf2
        board.tryToMakeMove(new Point(5, 7), new Point(6, 6)); // 2.Bg7
        board.tryToMakeMove(new Point(5, 2), new Point(5, 3)); // 3.f4
        board.tryToMakeMove(new Point(6, 6), new Point(4, 4)); // 3.Be5


        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(5, 3), new Point(5, 4)), new Move(new Point(5, 3), new Point(4, 4)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(7, 2)), new Move(new Point(6, 0), new Point(5, 2)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(6, 2)),
            new Move(new Point(5, 1), new Point(4, 2)), new Move(new Point(5, 1), new Point(4, 0)),
            new Move(new Point(3, 0), new Point(4, 0))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

        // part three, enemy king is dangerous too.

        board = new Board();


        board.tryToMakeMove(new Point(5, 1), new Point(5, 2)); // 1.f3
        board.tryToMakeMove(new Point(5, 6), new Point(5, 5)); // 1.f6
        board.tryToMakeMove(new Point(4, 0), new Point(5, 1)); // 2.Kf2
        board.tryToMakeMove(new Point(4, 7), new Point(5, 6)); // 2.Kf7
        board.tryToMakeMove(new Point(5, 1), new Point(4, 2)); // 3.Ke3
        board.tryToMakeMove(new Point(5, 6), new Point(4, 5)); // 3.Ke6
        board.tryToMakeMove(new Point(4, 2), new Point(3, 2)); // 4.Kd3
        board.tryToMakeMove(new Point(4, 5), new Point(3, 4)); // 4.Kd5


        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(5, 2), new Point(5, 3)), new Move(new Point(6, 1), new Point(6, 2)),
            new Move(new Point(6, 1), new Point(6, 3)), new Move(new Point(7, 1), new Point(7, 2)),
            new Move(new Point(7, 1), new Point(7, 3)), new Move(new Point(1, 0), new Point(0, 2)),
            new Move(new Point(1, 0), new Point(2, 2)), new Move(new Point(6, 0), new Point(7, 2)),
            new Move(new Point(3, 2), new Point(4, 2)), new Move(new Point(3, 2), new Point(2, 2)),
            new Move(new Point(3, 0), new Point(4, 0))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));
    }

    @Test
    void testPins()
    {
        board = new Board();
        //System.out.println("<<<PINNED BISHOPS TEST>>>");


        board.tryToMakeMove(new Point(4, 1), new Point(4, 3)); // 1.e4
        board.tryToMakeMove(new Point(4, 6), new Point(4, 4)); // 1.e5
        board.tryToMakeMove(new Point(3, 1), new Point(3, 3)); // 2.d4
        board.tryToMakeMove(new Point(3, 7), new Point(7, 3)); // 2.Qh4
        board.tryToMakeMove(new Point(5, 0), new Point(4, 1)); // 3.Be2
        board.tryToMakeMove(new Point(7, 3), new Point(4, 3)); // 3.Qxe4

        // e2 bishop is pinned now
        Move[] correctMoves =
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 3), new Point(3, 4)), new Move(new Point(3, 3), new Point(4, 4)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(1, 0), new Point(3, 1)), new Move(new Point(6, 0), new Point(5, 2)),
            new Move(new Point(6, 0), new Point(7, 2)), new Move(new Point(3, 0), new Point(3, 1)),
            new Move(new Point(3, 0), new Point(3, 2)), new Move(new Point(4, 0), new Point(3, 1)),
            new Move(new Point(4, 0), new Point(5, 0)), new Move(new Point(2, 0), new Point(3, 1)),
            new Move(new Point(2, 0), new Point(4, 2)), new Move(new Point(2, 0), new Point(5, 3)),
            new Move(new Point(2, 0), new Point(6, 4)), new Move(new Point(2, 0), new Point(7, 5))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));


        board.tryToMakeMove(new Point(2, 0), new Point(4, 2)); // 4.Be3
        board.tryToMakeMove(new Point(4, 4), new Point(3, 3)); // 4.exd
        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(1, 0), new Point(3, 1)), new Move(new Point(6, 0), new Point(5, 2)),
            new Move(new Point(6, 0), new Point(7, 2)), new Move(new Point(3, 0), new Point(3, 1)),
            new Move(new Point(3, 0), new Point(3, 2)), new Move(new Point(3, 0), new Point(3, 3)),
            new Move(new Point(3, 0), new Point(2, 0)), new Move(new Point(4, 0), new Point(3, 1)),
            new Move(new Point(4, 0), new Point(5, 0)), new Move(new Point(4, 1), new Point(5, 2)),
            new Move(new Point(4, 1), new Point(6, 3)), new Move(new Point(4, 1), new Point(7, 4)),
            new Move(new Point(4, 1), new Point(3, 2)), new Move(new Point(4, 1), new Point(2, 3)),
            new Move(new Point(4, 1), new Point(1, 4)), new Move(new Point(4, 1), new Point(0, 5)),
            new Move(new Point(4, 1), new Point(5, 0)), new Move(new Point(4, 2), new Point(3, 1)),
            new Move(new Point(4, 2), new Point(2, 0)), new Move(new Point(4, 2), new Point(5, 3)),
            new Move(new Point(4, 2), new Point(6, 4)), new Move(new Point(4, 2), new Point(7, 5)),
            new Move(new Point(4, 2), new Point(3, 3))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));
        // no bishops should be pinned.

        // Test 3. should be able to move along a pin.
        board = new Board();


        board.tryToMakeMove(new Point(4, 1), new Point(4, 3)); // 1.e4
        board.tryToMakeMove(new Point(4, 6), new Point(4, 4)); // 1.e5
        board.tryToMakeMove(new Point(3, 0), new Point(4, 1)); // 2.Qe2
        board.tryToMakeMove(new Point(3, 7), new Point(7, 3)); // 2.Qh4
        board.tryToMakeMove(new Point(4, 1), new Point(4, 2)); // 3.Qe3
        board.tryToMakeMove(new Point(7, 3), new Point(4, 3)); // 3.Qxe4


        correctMoves = new Move[]
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(7, 1), new Point(7, 2)), new Move(new Point(7, 1), new Point(7, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(5, 2)), new Move(new Point(6, 0), new Point(7, 2)),
            new Move(new Point(5, 0), new Point(4, 1)), new Move(new Point(5, 0), new Point(3, 2)),
            new Move(new Point(5, 0), new Point(1, 4)), new Move(new Point(5, 0), new Point(2, 3)),
            new Move(new Point(5, 0), new Point(0, 5)), new Move(new Point(4, 2), new Point(4, 1)),
            new Move(new Point(4, 2), new Point(4, 3)), new Move(new Point(4, 0), new Point(4, 1)),
            new Move(new Point(4, 0), new Point(3, 0)), new Move(new Point(6, 0), new Point(4, 1))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

    }

    @Test
    void testCastling()
    {

        board = new Board();
        // System.out.println("<<<CASTLING TEST>>>");


        board.tryToMakeMove(new Point(6, 1), new Point(6, 2)); // 1.g3
        board.tryToMakeMove(new Point(4, 6), new Point(4, 5)); // 1.e6
        board.tryToMakeMove(new Point(5, 0), new Point(6, 1)); // 2.Bg2
        board.tryToMakeMove(new Point(4, 5), new Point(4, 4)); // 2.e5
        board.tryToMakeMove(new Point(6, 0), new Point(5, 2)); // 3.Nf3
        board.tryToMakeMove(new Point(4, 4), new Point(4, 3)); // 1.e4
        // Start position test
        Move[] correctMoves =
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(1, 1), new Point(1, 2)), new Move(new Point(1, 1), new Point(1, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)),

            new Move(new Point(6, 2), new Point(6, 3)), new Move(new Point(7, 1), new Point(7, 2)),
            new Move(new Point(7, 1), new Point(7, 3)), new Move(new Point(1, 0), new Point(0, 2)),
            new Move(new Point(1, 0), new Point(2, 2)), new Move(new Point(5, 2), new Point(6, 0)),
            new Move(new Point(5, 2), new Point(7, 3)), new Move(new Point(5, 2), new Point(6, 4)),
            new Move(new Point(5, 2), new Point(4, 4)), new Move(new Point(5, 2), new Point(3, 3)),
            new Move(new Point(6, 1), new Point(5, 0)), new Move(new Point(6, 1), new Point(7, 2)),
            new Move(new Point(4, 0), new Point(6, 0)), new Move(new Point(4, 0), new Point(5, 0)),
            new Move(new Point(7, 0), new Point(5, 0)),
            new Move(new Point(7, 0), new Point(6, 0)),};
        assertTrue(compare(board.getLegalMoves(), correctMoves));
        board.tryToMakeMove(new Point(4, 0), new Point(6, 0)); // 4. O-O
        Piece supposedRook = board.getPieceAtPoint(new Point(5, 0));
        if (supposedRook == null || supposedRook.type != PieceType.Rook)
        {
            fail("WARNING - ROOK ISN'T AT EXPECT SQUARE AFTER     ");
        }
        Piece noRookAnymore = board.getPieceAtPoint(new Point(7, 0));
        if (noRookAnymore != null && noRookAnymore.type == PieceType.Rook)
        {
            fail("WARNING - ROOK IS STILL AT IT'S ORIGINAL SQUARE");
        }
    }

    @Test
    void promotionTest()
    {
        board = new Board();
        // System.out.println("<<<PROMOTION TEST>>>");


        board.tryToMakeMove(new Point(7, 1), new Point(7, 3)); // 1.h4
        board.tryToMakeMove(new Point(0, 6), new Point(0, 4)); // 1.a5
        board.tryToMakeMove(new Point(7, 3), new Point(7, 4)); // 2.h5
        board.tryToMakeMove(new Point(0, 4), new Point(0, 3)); // 2.a4
        board.tryToMakeMove(new Point(7, 4), new Point(7, 5)); // 3.h6
        board.tryToMakeMove(new Point(0, 3), new Point(0, 2)); // 3.a3
        board.tryToMakeMove(new Point(7, 5), new Point(6, 6)); // 3.hxg
        board.tryToMakeMove(new Point(0, 2), new Point(1, 1)); // 3.axb
        board.tryToMakeMove(new Point(6, 6), new Point(7, 7)); // 3.gxhQ
        board.tryToMakeMove(new Point(1, 1), new Point(0, 0)); // 3.bxaQ
        // At this point you should be able to take the other queen at a1
        Move[] correctMoves =
        {new Move(new Point(0, 1), new Point(0, 2)), new Move(new Point(0, 1), new Point(0, 3)),
            new Move(new Point(2, 1), new Point(2, 2)), new Move(new Point(2, 1), new Point(2, 3)),
            new Move(new Point(3, 1), new Point(3, 2)), new Move(new Point(3, 1), new Point(3, 3)),
            new Move(new Point(4, 1), new Point(4, 2)), new Move(new Point(4, 1), new Point(4, 3)),
            new Move(new Point(5, 1), new Point(5, 2)), new Move(new Point(5, 1), new Point(5, 3)),
            new Move(new Point(6, 1), new Point(6, 2)), new Move(new Point(6, 1), new Point(6, 3)),
            new Move(new Point(1, 0), new Point(0, 2)), new Move(new Point(1, 0), new Point(2, 2)),
            new Move(new Point(6, 0), new Point(5, 2)), new Move(new Point(6, 0), new Point(7, 2)),
            new Move(new Point(2, 0), new Point(1, 1)), new Move(new Point(2, 0), new Point(0, 2)),
            new Move(new Point(7, 7), new Point(1, 1)), new Move(new Point(7, 7), new Point(2, 2)),
            new Move(new Point(7, 7), new Point(4, 4)), new Move(new Point(7, 7), new Point(3, 3)),
            new Move(new Point(7, 7), new Point(5, 5)), new Move(new Point(7, 7), new Point(6, 6)),
            new Move(new Point(7, 7), new Point(7, 6)), new Move(new Point(7, 7), new Point(6, 7)),
            new Move(new Point(7, 7), new Point(0, 0)), new Move(new Point(7, 0), new Point(7, 1)),
            new Move(new Point(7, 0), new Point(7, 2)), new Move(new Point(7, 0), new Point(7, 3)),
            new Move(new Point(7, 0), new Point(7, 4)), new Move(new Point(7, 0), new Point(7, 5)),
            new Move(new Point(7, 0), new Point(7, 6))};
        assertTrue(compare(board.getLegalMoves(), correctMoves));

    }
}
