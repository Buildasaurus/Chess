package chess.Models.Bots;

import java.util.Arrays;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;
import chess.Models.Evaluation.SimpleEval;


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
    Timer timer;
    Board board;

    public Move think(Board board, Timer timer)
    {
        System.out.println("V1 booted up, and thinking");

        bestMove = null;
        this.timer = timer;
        this.board = board;
        for (int i = 2; i < 3; i++)
        {
            negamax(i, -9999999, 9999999, 0);
        }
        return bestMove;
    }

    /**
     * figures out the eval of the given position. If at ply 1, also overwrites the best move.
     *
     * @param depth The depth to search
     * @param alpha If there is found a move under this value, then it's too bad to choose, as
     *        another move sequence is better
     * @param beta If you find a move above this value, then the oponnent won't allow you into this
     *        sequence, because it's too good
     * @param ply How deep we have searched for now.
     * @return
     */
    public int negamax(int depth, int alpha, int beta, int ply)
    {
        boolean isRoot = ply == 0;
        if (board.isCheckmate())
        {
            return -999999 + ply;
        }
        if (!isRoot && board.isDraw())
        {
            return 0;
        }

        int bestEval = -99999999; // Standard really bad eval. not reachable
        if (depth <= 0)
        {
            return SimpleEval.evaluation(board) * (board.whiteToMove ? 1 : -1);
        }
        Move[] legalMoves = board.getLegalMoves();


        for (Move move : legalMoves)
        {
            board.makeMove(move);
            int eval = -negamax(depth - 1, beta, alpha, ply + 1);
            board.undoMove(move);

            if (eval > bestEval)
            {
                if (isRoot)
                {
                    bestMove = move;
                }
                bestEval = eval;

                /* untested for now
                if (eval > beta) // beta cutoff, this position is too good. Oponnent wouldn't get on
                                 // this branch in the first place.
                {
                    return eval;
                } */
            }

            /*
            if (eval < alpha) // alpha cutoff, we have another branch that at the least is
            // better than this.
            {
                return eval;
            } */
        }
        return bestEval;
    }

}
