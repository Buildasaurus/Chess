package chess.Models.Bots;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.TTEntry;
import chess.Models.Timer;
import chess.Models.TranspositionTable;
import chess.Models.Evaluation.Eval2;

// V4.1

public class MyBot implements IBot
{
    private TranspositionTable table;
    private Move bestMove;

    private int lookupCount;
    private Timer timer;
    private Board board;
    private long maxUseTime;
    private boolean isWhite;
    private boolean quit;

    private Move[] killers;

    public MyBot()
    {
        table = new TranspositionTable(0x400000);
    }

    public Move think(Board board, Timer timer)
    {
        print("MyBot thinking");
        bestMove = null;
        lookupCount = 0;
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
            print("Time for depth: " + (System.nanoTime() - startTime) / Math.pow(10, 9)
                    + " seconds");


            if (quit)
            {
                break;
            }
        }
        print("time used: " + timer.timeElapsedOnCurrentTurn());
        print("Lookupcount: " + lookupCount);
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


        int oldAlpha = alpha;
        // Transposition table lookup
        long zobristHash = board.getZobristHash();
        TTEntry entry = table.getEntry(board.getZobristHash());

        // If we have an "exact" score (a < score < beta) just use that
        // If we have a lower bound (an alpha) better than beta, use that
        // If we have an upper bound (a beta) worse than alpha, use that
        if (entry != null && !isRoot && entry.hash == zobristHash && entry.depth >= depth
                && Math.abs(entry.evaluation) < 50000 && (
                // Exact
                entry.flag == 1 ||
                // Upperbound
                        entry.flag == 2 && entry.evaluation <= alpha ||
                        // Lowerbound
                        entry.flag == 3 && entry.evaluation >= beta))
        {
            lookupCount++;
            return entry.evaluation;
        }


        // Move ordering
        Move[] legalMoves = board.getLegalMoves();
        Move goodMove = entry != null ? entry.bestMove : null;;

        legalMoves = orderMoves(legalMoves, goodMove, ply);

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


            if (alpha >= beta) // beta cutoff, this branch is too good.
            {
                killers[ply] = goodMove != null ? goodMove : move;
                break; // not return, because we want to store in TT
            }
        }

        if (entry != null)
        {
            entry.update(board.getZobristHash(), goodMove, (byte) depth,
                    (byte) (bestEval >= beta ? 3 : bestEval <= oldAlpha ? 2 : 1), bestEval);
        }
        else
        {
            TTEntry newEntry = new TTEntry(board.getZobristHash(), goodMove, (byte) depth,
            (byte) (bestEval >= beta ? 3 : bestEval <= oldAlpha ? 2 : 1), bestEval);
            table.setEntry(board.getZobristHash(), newEntry);
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
        legalMoves = orderMoves(legalMoves, null, ply);

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

    private Move[] orderMoves(Move[] legalMoves, Move goodMove, int ply)
    {
        int[] ratings = new int[legalMoves.length];
        for (int i = 0; i < legalMoves.length; i++)
        {
            int rating = 0;
            Move legalMove = legalMoves[i];
            if (goodMove != null && legalMove.equals(goodMove))
            {
                rating = 100_000;
            }
            else if (legalMoves[i].isCapture)
            {

                rating += pieceValues[legalMove.getCapturePieceType().ordinal()];
                rating -= pieceValues[board.getPieceAtPoint(legalMove.startSquare).type.ordinal()]
                        / 10;

            }
            else if (killers[ply] != null && killers[ply].equals(legalMove))
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
