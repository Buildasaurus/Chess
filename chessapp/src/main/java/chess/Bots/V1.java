package chess.Bots;

import java.util.ArrayList;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Piece;
import chess.Models.Point;

public class V1 implements IBot
{
    private Move bestMove;
    int movecount;

    public Move think(Board board)
    {
        movecount = 0;

        // Iterative deepening
        negamax(board, 4);
        System.out.println(movecount);

        return bestMove;
    }


    public void negamax(Board board, int depth)
    {
        Move[] legalMoves = board.getLegalMoves();
        if (depth <= 0)
        {
            movecount++;
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
