package chess.Bots;

import chess.Models.Board;
import chess.Models.Move;


public class V1 implements IBot
{
    private Move bestMove;
    long movecount;
    long time = 0;

    public Move think(Board board)
    {
        movecount = 0;
        long starttime = System.nanoTime();
        negamax(board, 2);
        System.out.println(movecount);
        System.out.println("movegeneration: " + board.legalMovesTime / Math.pow(10, 9) + "s of "
                + (System.nanoTime() - starttime) / Math.pow(10, 9));
        System.out.println("make move: " + board.makeMoveTime / Math.pow(10, 9) + "s of "
                + (System.nanoTime() - starttime) / Math.pow(10, 9));
        System.out.println("undo move: " + board.undoTime / Math.pow(10, 9) + "s of "
                + (System.nanoTime() - starttime) / Math.pow(10, 9));
        System.out.println(board.testcounter);
        return bestMove;
    }


    public void negamax(Board board, int depth)
    {
        if (depth <= 0)
        {
            movecount++;
            return;
        }
        long start = System.nanoTime();
        Move[] legalMoves = board.getLegalMoves();
        time += System.nanoTime() - start;

        for (Move move : legalMoves)
        {
            board.makeMove(move);
            negamax(board, depth - 1);
            board.undoMove(move);
            bestMove = move;
        }

    }
}
