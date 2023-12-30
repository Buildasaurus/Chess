package chess.Bots;

import chess.Models.Board;
import chess.Models.Move;


public class V1 implements IBot
{
    private Move bestMove;
    long movecount;

    public Move think(Board board)
    {
        movecount = 0;

        // Iterative deepening
        negamax(board, 5);
        System.out.println(movecount);

        return bestMove;
    }


    public void negamax(Board board, int depth)
    {
        Move[] legalMoves = board.getLegalMoves();
        if (depth <= 0)
        {
            movecount++;
            if (movecount == 46078)
            {
                System.out.println(movecount);
            }
            return;
        }
        for (Move move : legalMoves)
        {
            board.makeMove(move);
            negamax(board, depth - 1);
            board.undoMove(move);
            bestMove = move;
        }

    }
}
