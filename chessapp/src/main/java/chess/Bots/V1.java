package chess.Bots;

import chess.Bots.Evaluation.SimpleEval;
import chess.Models.Board;
import chess.Models.Move;


public class V1 implements IBot
{
    private Move bestMove;
    long movecount;
    long time = 0;
    long enPassentcount;
    long castleCount;
    long promotionCount;
    long checkCount;
    long captureCount;
    int checkmateCount = 0;

    public Move think(Board board)
    {
        for (int i = 1; i < 4; i++)
        {
            negamax(board, i, 0);
        }

        return bestMove;
    }


    public int negamax(Board board, int depth, int ply)
    {
        if(board.isCheckmate())
        {
            return -99999999 + ply;
        }
        if(board.isDraw())
        {
            return 0;
        }
        int bestEval = -9999999; // Standard really bad eval.
        if (depth <= 0)
        {
            return SimpleEval.evaluation(board);
        }
        Move[] legalMoves = board.getLegalMoves();

        for (Move move : legalMoves)
        {
            board.makeMove(move);
            int eval = -negamax(board, depth - 1, ply + 1);
            if(eval > bestEval)
            {
                bestEval = eval;
                if(ply == 0)
                {
                    bestMove = move;
                }
            }
            board.undoMove(move);
        }
        return bestEval;
    }

}
