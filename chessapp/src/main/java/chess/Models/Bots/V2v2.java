package chess.Models.Bots;

import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;
import chess.Models.Evaluation.Eval2;


public class V2v2 implements IBot
{
    private Move bestMove;
    int checkmateCount = 0;
    Timer timer;
    Board board;
    long maxUseTime;
    boolean isWhite;
    boolean quit;

    public Move think(Board board, Timer timer)
    {
        print("V2v2 booted up, and thinking");
        bestMove = null;
        this.timer = timer;
        this.board = board;
        isWhite = board.whiteToMove;
        print("iswhite = " + isWhite);
        print("time = " +timer.getRemainingTime(isWhite));
        maxUseTime = timer.getRemainingTime(isWhite)/30 + timer.getIncrement(isWhite)/2;
        print("maxusetime = " + maxUseTime);

        quit = false;
        for (int i = 2; i < 10; i++)
        {
            print("Now going at depth: " + i);
            print("final Eval + " + negamax(i, -9999999, 9999999, 0) * (isWhite ? 1 : -1));
            if(quit)
            {
                break;
            }
        }
        print("time used: " + timer.timeElapsedOnCurrentTurn());
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
        if(quit)
        {
            return 0;
        }
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
            return Eval2.evaluation(board) * (board.whiteToMove ? 1 : -1);
        }

        Move[] legalMoves = board.getLegalMoves();
        if(isRoot && bestMove != null)
        {
            //int[] ratings = new int[legalMoves.length];
            Move[] orderedMoves = new Move[legalMoves.length];
            int counter = 0;
            for (Move move : legalMoves) {
                if(!move.equals(bestMove))
                {
                    counter++;
                    orderedMoves[counter] = move;
                }
            }
            orderedMoves[0] = bestMove;
            legalMoves = orderedMoves;
        }


        for (Move move : legalMoves)
        {
            if(quit || timer.timeElapsedOnCurrentTurn() > maxUseTime)
            {
                quit = true;
                break;
            }
            board.makeMove(move);
            int eval = -negamax(depth - 1, -beta, -alpha, ply + 1);
            board.undoMove(move);

            if (eval > bestEval)
            {
                if (isRoot)
                {
                    bestMove = move;
                }
                bestEval = eval;

                if (eval > beta) // beta cutoff, this position is too good. Oponnent wouldn't get on
                                 // this branch in the first place.
                {
                    return eval;
                }
            }


            if (eval < alpha) // alpha cutoff, we have another branch that at the least is
            // better than this.
            {
                return eval;
            }
        }
        return bestEval;
    }


    private void print(String string)
    {
        System.out.println("INFOSTRING --- " + string);
    }
}
