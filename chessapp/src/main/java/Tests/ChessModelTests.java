package Tests;

import java.util.ArrayList;
import java.util.Arrays;
import chess.ChessModel;
import chess.Move;
import chess.Piece;
import chess.Point;
import chess.Piece.PieceType;

public class ChessModelTests
{
    ChessModel board;

    public ChessModelTests()
    {
        board = new ChessModel();
        System.out.println("------------------START TEST----------------");
        broadTest();
        mateTest();
        testEnPassent();
        testKingLegalMoves();
        testPins();
        testCastling();
        promotionTest();
        System.out.println("-----------------END TEST-----------------");
    }

    void compare(Move[] moves, Move[] correctMoves)
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

    void mateTest()
    {
        board = new ChessModel();
        System.out.println("<<<MATE TEST>>>");


        board.movePiece(new Point(4, 1), new Point(4, 3)); // 1.e4
        board.movePiece(new Point(1, 7), new Point(2, 5)); // 1.Nc6
        board.movePiece(new Point(3, 0), new Point(7, 4)); // 2.Qh4
        board.movePiece(new Point(2, 5), new Point(1, 7)); // 2.Nb8
        board.movePiece(new Point(5, 0), new Point(2, 3)); // 3.Bc4
        board.movePiece(new Point(1, 7), new Point(2, 5)); // 3.Nc6
        board.movePiece(new Point(7, 4), new Point(5, 6)); // 4.Qf6#

        Move[] correctMoves = new Move[0];
        compare(board.getLegalMoves(), correctMoves);

        // part two: King is not safe behind itself.
        board = new ChessModel();


        board.movePiece(new Point(4, 1), new Point(4, 2)); // 1.e3
        board.movePiece(new Point(4, 6), new Point(4, 4)); // 1.e5
        board.movePiece(new Point(3, 0), new Point(7, 4)); // 2.Qh5
        board.movePiece(new Point(4, 7), new Point(4, 6)); // 2.Ke7
        board.movePiece(new Point(7, 4), new Point(4, 4)); // 3.Qxe5#

        correctMoves = new Move[0];
        compare(board.getLegalMoves(), correctMoves);

    }

    void broadTest()
    {
        System.out.println("<<<BROAD TEST>>>");
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
        compare(board.getLegalMoves(), correctMoves);


        // bishop on diagonal moves test.

        board.movePiece(new Point(6, 1), new Point(6, 3)); // 1.g4
        board.movePiece(new Point(6, 6), new Point(6, 5)); // 1.g6

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

        compare(board.getLegalMoves(), correctMoves);

        // can't move king into check test

        board.movePiece(new Point(3, 1), new Point(3, 3)); // 2.d5
        board.movePiece(new Point(5, 7), new Point(7, 5)); // 2.bh6

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
        compare(board.getLegalMoves(), correctMoves);

        board.movePiece(new Point(2, 0), new Point(7, 5)); // Bxh6
        board.movePiece(new Point(6, 7), new Point(7, 5)); // Nxh6
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
        compare(board.getLegalMoves(), correctMoves);
        board.movePiece(new Point(3, 0), new Point(3, 1)); // Qd2
        board.movePiece(new Point(7, 5), new Point(5, 4)); // Nf5
        board.movePiece(new Point(3, 1), new Point(7, 5)); // Qh6
        board.movePiece(new Point(5, 4), new Point(3, 3)); // Nxd4
        board.movePiece(new Point(7, 5), new Point(5, 7)); // Qf8+
        correctMoves = new Move[]
        {new Move(new Point(4, 7), new Point(5, 7)), new Move(new Point(7, 7), new Point(5, 7))};
        compare(board.getLegalMoves(), correctMoves);

        board.movePiece(new Point(4, 7), new Point(5, 7)); // Kxf8
        board.movePiece(new Point(4, 0), new Point(3, 0)); // Kd1
        board.movePiece(new Point(1, 7), new Point(2, 5)); // Nc6
        board.movePiece(new Point(3, 0), new Point(4, 0)); // Ke1
        board.movePiece(new Point(3, 3), new Point(2, 1)); // Nxc2+

        correctMoves = new Move[]
        {new Move(new Point(4, 0), new Point(3, 0)), new Move(new Point(4, 0), new Point(3, 1))};
        compare(board.getLegalMoves(), correctMoves);


        board.movePiece(new Point(4, 0), new Point(3, 1)); // Kd2
        board.movePiece(new Point(1, 6), new Point(1, 5)); // b6
        board.movePiece(new Point(3, 1), new Point(3, 2)); // Kd3
        board.movePiece(new Point(2, 7), new Point(0, 5)); // Ba6+
        correctMoves = new Move[]
        {new Move(new Point(3, 2), new Point(3, 1)), new Move(new Point(3, 2), new Point(2, 2)),
            new Move(new Point(3, 2), new Point(2, 1)), new Move(new Point(3, 2), new Point(4, 3))};
        compare(board.getLegalMoves(), correctMoves);

        board.movePiece(new Point(3, 2), new Point(4, 3)); // Ke4
        board.movePiece(new Point(4, 6), new Point(4, 5)); // e6
        board.movePiece(new Point(4, 3), new Point(5, 2)); // Kf3
        board.movePiece(new Point(3, 7), new Point(5, 5)); // Qf6+
        correctMoves = new Move[]
        {new Move(new Point(5, 2), new Point(6, 2)), new Move(new Point(5, 2), new Point(4, 3)),
            new Move(new Point(5, 2), new Point(6, 1))};
        compare(board.getLegalMoves(), correctMoves);

        board.movePiece(new Point(5, 2), new Point(6, 2)); // Kg3
        board.movePiece(new Point(5, 5), new Point(4, 4)); // Qe5+
    }

    void testEnPassent()
    {
        board = new ChessModel();
        System.out.println("<<<EN PASSENT TEST>>>");


        board.movePiece(new Point(6, 1), new Point(6, 3)); // 1.g4
        board.movePiece(new Point(6, 6), new Point(6, 5)); // 1.g6
        board.movePiece(new Point(6, 3), new Point(6, 4)); // 2.g5
        board.movePiece(new Point(5, 6), new Point(5, 4)); // 2.f5

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
        compare(board.getLegalMoves(), correctMoves);

        board.movePiece(new Point(6, 4), new Point(5, 5)); // 3.gxf

        if (board.getPieceAtPoint(new Point(4, 5)) != null)
        {
            System.out.println("En passent failed to remove the pawn it caputed");
        }

    }

    void testKingLegalMoves()
    {
        board = new ChessModel();
        System.out.println("<<<KING LEGAL MOVES TEST>>>");


        board.movePiece(new Point(5, 1), new Point(5, 2)); // 1.f3
        board.movePiece(new Point(4, 6), new Point(4, 5)); // 1.e6
        board.movePiece(new Point(4, 0), new Point(5, 1)); // 2.Kf2
        board.movePiece(new Point(1, 7), new Point(2, 5)); // 2.Nc6
        board.movePiece(new Point(5, 1), new Point(4, 2)); // 2.Ke3
        board.movePiece(new Point(5, 7), new Point(4, 6)); // 2.Be7
        board.movePiece(new Point(4, 2), new Point(5, 3)); // 4.Kf4
        board.movePiece(new Point(6, 7), new Point(5, 5)); // 4.Nf6



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
        compare(board.getLegalMoves(), correctMoves);


        // part two, it is safe behind a friendly piece.

        board = new ChessModel();


        board.movePiece(new Point(5, 1), new Point(5, 2)); // 1.f3
        board.movePiece(new Point(6, 6), new Point(6, 5)); // 1.g6
        board.movePiece(new Point(4, 0), new Point(5, 1)); // 2.Kf2
        board.movePiece(new Point(5, 7), new Point(6, 6)); // 2.Bg7
        board.movePiece(new Point(5, 2), new Point(5, 3)); // 3.f4
        board.movePiece(new Point(6, 6), new Point(4, 4)); // 3.Be5


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
        compare(board.getLegalMoves(), correctMoves);

        // part three, enemy king is dangerous too.

        board = new ChessModel();


        board.movePiece(new Point(5, 1), new Point(5, 2)); // 1.f3
        board.movePiece(new Point(5, 6), new Point(5, 5)); // 1.f6
        board.movePiece(new Point(4, 0), new Point(5, 1)); // 2.Kf2
        board.movePiece(new Point(4, 7), new Point(5, 6)); // 2.Kf7
        board.movePiece(new Point(5, 1), new Point(4, 2)); // 3.Ke3
        board.movePiece(new Point(5, 6), new Point(4, 5)); // 3.Ke6
        board.movePiece(new Point(4, 2), new Point(3, 2)); // 4.Kd3
        board.movePiece(new Point(4, 5), new Point(3, 4)); // 4.Kd5


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
        compare(board.getLegalMoves(), correctMoves);
    }

    void testPins()
    {
        board = new ChessModel();
        System.out.println("<<<PINNED BISHOPS TEST>>>");


        board.movePiece(new Point(4, 1), new Point(4, 3)); // 1.e4
        board.movePiece(new Point(4, 6), new Point(4, 4)); // 1.e5
        board.movePiece(new Point(3, 1), new Point(3, 3)); // 2.d4
        board.movePiece(new Point(3, 7), new Point(7, 3)); // 2.Qh4
        board.movePiece(new Point(5, 0), new Point(4, 1)); // 3.Be2
        board.movePiece(new Point(7, 3), new Point(4, 3)); // 3.Qxe4

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
        compare(board.getLegalMoves(), correctMoves);


        board.movePiece(new Point(2, 0), new Point(4, 2)); // 4.Be3
        board.movePiece(new Point(4, 4), new Point(3, 3)); // 4.exd
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
        compare(board.getLegalMoves(), correctMoves);
        // no bishops should be pinned.
    }

    void testCastling()
    {

        board = new ChessModel();
        System.out.println("<<<CASTLING TEST>>>");


        board.movePiece(new Point(6, 1), new Point(6, 2)); // 1.g3
        board.movePiece(new Point(4, 6), new Point(4, 5)); // 1.e6
        board.movePiece(new Point(5, 0), new Point(6, 1)); // 2.Bg2
        board.movePiece(new Point(4, 5), new Point(4, 4)); // 2.e5
        board.movePiece(new Point(6, 0), new Point(5, 2)); // 3.Nf3
        board.movePiece(new Point(4, 4), new Point(4, 3)); // 1.e4
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
        compare(board.getLegalMoves(), correctMoves);
        board.movePiece(new Point(4, 0), new Point(6, 0)); // 4. O-O
        Piece supposedRook = board.getPieceAtPoint(new Point(5, 0));
        if (supposedRook == null || supposedRook.type != PieceType.Rook)
        {
            System.out.println("WARNING - ROOK ISN'T AT EXPECT SQUARE AFTER     ");
        }
        Piece noRookAnymore = board.getPieceAtPoint(new Point(7, 0));
        if (noRookAnymore != null && noRookAnymore.type == PieceType.Rook)
        {
            System.out.println("WARNING - ROOK IS STILL AT IT'S ORIGINAL SQUARE");
        }
    }

    void promotionTest()
    {
        board = new ChessModel();
        System.out.println("<<<PROMOTION TEST>>>");


        board.movePiece(new Point(7, 1), new Point(7, 3)); // 1.h4
        board.movePiece(new Point(0, 6), new Point(0, 4)); // 1.a5
        board.movePiece(new Point(7, 3), new Point(7, 4)); // 2.h5
        board.movePiece(new Point(0, 4), new Point(0, 3)); // 2.a4
        board.movePiece(new Point(7, 4), new Point(7, 5)); // 3.h6
        board.movePiece(new Point(0, 3), new Point(0, 2)); // 3.a3
        board.movePiece(new Point(7, 5), new Point(6, 6)); // 3.hxg
        board.movePiece(new Point(0, 2), new Point(1, 1)); // 3.axb
        board.movePiece(new Point(6, 6), new Point(7, 7)); // 3.gxhQ
        board.movePiece(new Point(1, 1), new Point(0, 0)); // 3.bxa
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
            new Move(new Point(7, 7), new Point(0, 0)), new Move(new Point(7, 0), new Point(7, 1)),
            new Move(new Point(7, 0), new Point(7, 2)), new Move(new Point(7, 0), new Point(7, 3)),
            new Move(new Point(7, 0), new Point(7, 4)), new Move(new Point(7, 0), new Point(7, 5)),
            new Move(new Point(7, 0), new Point(7, 6))};
        compare(board.getLegalMoves(), correctMoves);

    }
}
