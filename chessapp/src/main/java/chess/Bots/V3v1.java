package chess.Bots;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import chess.Evaluation.Eval2;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;

// Finished game 246 (EvilBot vs MyBot): * {No result}
// Score of MyBot vs EvilBot: 166 - 39 - 40  [0.759] 245
// ...      MyBot playing White: 76 - 25 - 22  [0.707] 123
// ...      MyBot playing Black: 90 - 14 - 18  [0.811] 122
// ...      White vs Black: 90 - 115 - 40  [0.449] 245
// Elo difference: 199.5 +/- 45.2, LOS: 100.0 %, DrawRatio: 16.3 %

// compared to V3:
//Score of MyBot vs EvilBot: 98 - 1 - 13  [0.933] 112
// ...      MyBot playing White: 52 - 0 - 5  [0.956] 57
// ...      MyBot playing Black: 46 - 1 - 8  [0.909] 55
// ...      White vs Black: 53 - 46 - 13  [0.531] 112
// Elo difference: 457.6 +/- 102.9, LOS: 100.0 %, DrawRatio: 11.6 %
// SPRT: llr 3.03 (103.0%), lbound -2.94, ubound 2.94 - H1 was accepted

public class V3v1 implements IBot
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
        print("V3.1 booted up, and thinking");
        bestMove = null;
        this.timer = timer;
        this.board = board;
        isWhite = board.whiteToMove;
        print("iswhite = " + isWhite);
        print("time = " + timer.getRemainingTime(isWhite));
        maxUseTime = timer.getRemainingTime(isWhite) / 30 + timer.getIncrement(isWhite) / 2;
        print("maxusetime = " + maxUseTime);

        quit = false;
        for (int i = 2; i < 10; i++)
        {
            print("Now going at depth: " + i);
            print("final Eval: " + negamax(i, -9999999, 9999999, 0) * (isWhite ? 1 : -1));
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
            return Eval2.evaluation(board) * (board.whiteToMove ? 1 : -1);
        }

        // Move ordering
        Move[] legalMoves = board.getLegalMoves();
        legalMoves = orderMoves(legalMoves, isRoot);

        boolean firstMove = true;
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
                bestEval = eval;

            }
            alpha = Math.max(eval, alpha);



            if (alpha >= beta) // alpha cutoff, we have another branch that at the least is
            // better than this.
            {
                return eval;
            }
        }
        return bestEval;
    }

    int[] pieceValues = new int[]
    {1000, 3000, 3500, 5000, 9000, 0};

    private Move[] orderMoves(Move[] legalMoves, boolean isRoot)
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
