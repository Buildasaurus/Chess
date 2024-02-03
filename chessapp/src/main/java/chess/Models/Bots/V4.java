package chess.Models.Bots;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;
import chess.Models.Evaluation.Eval2;

// Compared to V3v2 - now with QSearch
// Score of MyBot vs EvilBot: 71 - 1 - 25  [0.861] 97
// ...      MyBot playing White: 37 - 0 - 12  [0.878] 49
// ...      MyBot playing Black: 34 - 1 - 13  [0.844] 48
// ...      White vs Black: 38 - 34 - 25  [0.521] 97
// Elo difference: 316.5 +/- 70.2, LOS: 100.0 %, DrawRatio: 25.8 %
// SPRT: llr 2.97 (100.9%), lbound -2.94, ubound 2.94 - H1 was accepted

public class V4 implements IBot
{
    private Move bestMove;
    int checkmateCount = 0;
    Timer timer;
    Board board;
    long maxUseTime;
    boolean isWhite;
    boolean quit;

    Move[] killers;

    public Move think(Board board, Timer timer)
    {
        print("MyBot booted up, and thinking");
        bestMove = null;
        this.timer = timer;
        this.board = board;
        isWhite = board.whiteToMove;
        maxUseTime = timer.getRemainingTime(isWhite) / 30 + timer.getIncrement(isWhite) / 2;
        killers = new Move[1024];
        quit = false;
        for (int i = 1; i < 99; i++)
        {
            print("Now going at depth: " + i);
            long startTime = System.nanoTime();
            print("final Eval: " + negamax(i, -9999999, 9999999, 0) * (isWhite ? 1 : -1));
            print("Final move: " + bestMove);
            print("Time for depth: " + (System.nanoTime() - startTime)/Math.pow(10, 9) + " seconds");


            if (quit)
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
        if (quit)
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
            return QSearch(alpha, beta, ply);
        }

        // Move ordering
        Move[] legalMoves = board.getLegalMoves();
        legalMoves = orderMoves(legalMoves, isRoot, ply);
        Move goodMove = null;

        for (Move move : legalMoves)
        {
            board.makeMove(move);
            int eval = -negamax(depth - 1, -beta, -alpha, ply + 1);
            board.undoMove(move);
            if (quit || timer.timeElapsedOnCurrentTurn() > maxUseTime)
            {
                quit = true;
                return 0;
            }
            if (isRoot)
            {
                print("Move " + move + " got eval " + eval);
            }
            if (eval > bestEval)
            {
                if (isRoot)
                {
                    bestMove = move;
                }
                goodMove = move;
                bestEval = eval;

            }
            alpha = Math.max(eval, alpha);



            if (alpha >= beta) // alpha cutoff, we have another branch that at the least is
            // better than this.
            {
                killers[ply] = goodMove != null ? goodMove : move;
                return bestEval;
            }
        }
        return bestEval;
    }

    // Doesn't seem to work yet
    public int QSearch(int alpha, int beta, int ply)
    {
        int standingPat = Eval2.evaluation(board) * (board.whiteToMove ? 1 : -1);
        if (standingPat >= beta)
            return beta;
        if (alpha < standingPat)
        {
            alpha = standingPat;
        }
        if (quit)
        {
            return 0;
        }

        // Move ordering
        Move[] legalMoves = board.getLegalMoves();
        legalMoves = orderMoves(legalMoves, false, ply);

        for (Move move : legalMoves)
        {
            if (!move.isCapture)
            {
                continue;
            }
            board.makeMove(move);
            int eval = -QSearch(-beta, -alpha, ply + 1);
            board.undoMove(move);
            if (quit || timer.timeElapsedOnCurrentTurn() > maxUseTime)
            {
                quit = true;
                return 0;
            }
			if (eval >= beta)
			{
				return beta;
			}
			if (eval > alpha)
			{
				alpha = eval;
			}
        }
        return alpha;
    }

    int[] pieceValues = new int[]
    {1000, 3000, 3500, 5000, 9000, 0};

    private Move[] orderMoves(Move[] legalMoves, boolean isRoot, int ply)
    {
        int[] ratings = new int[legalMoves.length];
        for (int i = 0; i < legalMoves.length; i++)
        {
            int rating = 0;
            Move legalMove = legalMoves[i];
            if (isRoot && bestMove != null && legalMove.equals(bestMove))
            {
                rating = 100_000;
            }
            else if (legalMoves[i].isCapture)
            {

                rating += pieceValues[legalMove.getCapturePieceType().ordinal()];
                rating -= pieceValues[board.getPieceAtPoint(legalMove.startSquare).type.ordinal()]
                        / 10;

            }
            else if(killers[ply] != null && killers[ply].equals(legalMove))
            {
                rating = 999; // pieceValues[0]-1
            }
            ratings[i] = rating;
        }

        // Create an array of indices
        Integer[] indices = IntStream.range(0, ratings.length).boxed().toArray(Integer[]::new);
        // Sort the array of indices based on the values in the numbers array
        Arrays.sort(indices, Comparator.comparingInt((Integer i) -> ratings[i]).reversed());
        // Use the sorted indices to reorder the objects array
        Move[] orderedMoves = Arrays.stream(indices).map(i -> legalMoves[i]).toArray(Move[]::new);

        return orderedMoves;
    }

    private void print(String string)
    {
        System.out.println("INFOSTRING --- " + string);
    }
}
