package Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import chess.ChessModel;
import chess.Move;
import chess.Point;

public class ChessModelTests
{
    ChessModel board;

    public ChessModelTests()
    {
        board = new ChessModel();
        System.out.println("-------------START TEST-------------");


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
            new Move(new Point(2, 0), new Point(7, 5))};
        compare(board.getLegalMoves(), correctMoves);

        // board.movePiece(new Point(0, 1), new Point(0, 2)); // a3
        // board.movePiece(new Point(7, 5), new Point(3, 1)); // bd2+

        System.out.println("-------------END TEST-------------");
    }

    void compare(Move[] moves, Move[] correctMoves)
    {
        boolean testSucced = true;
        List<Move> moveList = Arrays.asList(moves);
        int counter = 0;
        for (Move move : correctMoves)
        {
            boolean found = false;
            for (Move move2 : moveList)
            {
                if (move.equals(move2))
                {
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
                "Test result: %b, with %d/%d moves correct moves. %d incorrect moves. %d missing moves\n",
                testSucced, counter, correctMoves.length, moveList.size() - counter,
                correctMoves.length - counter);
    }
}
