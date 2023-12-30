package chess.Bots;

import java.util.ArrayList;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Piece;

public class V1 implements IBot
{
    private Move bestMove;

    public Move think(Board board)
    {

        // Iterative deepening
        for (int i = 0; i < 5; i++)
        {
            negamax(board);
        }
        return bestMove;
    }

    int i = 0;

    public void negamax(Board board)
    {
        Move[] legalMoves = board.getLegalMoves();
        for (Move move : legalMoves)
        {
            i++;
            bestMove = move;
            board.makeMove(bestMove);
            board.undoMove(bestMove);
            System.out.println(i);

            if (i == 226)
            {
                System.out.println("hi");
            }
            if (board.board[5][7] != null)
            {
                System.out.println("some pieces aren't the same!");
            }
        }

    }

}
