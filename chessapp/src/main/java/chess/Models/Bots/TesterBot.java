package chess.Models.Bots;

import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;


public class TesterBot implements IBot
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
    int depth = 4;
    /**
     *
     * @param depth The ply to go to, ply 0 doesn't make sense. ply 1 is all the legal moves in the position.
     */
    public TesterBot(int depth)
    {
        this.depth = depth;
    }

    public Move think(Board board, Timer timer)
    {
        long starttime = System.nanoTime();
        for (int i = 1; i <= depth; i++)
        {
            System.out.println("---------------- depth " + i + "-----------");
            movecount = 0;
            movecount = 0;
            time = 0;
            enPassentcount = 0;
            castleCount = 0;
            promotionCount = 0;
            checkCount = 0;
            captureCount = 0;
            checkmateCount = 0;

            negamax(board, i);
            System.out.println("movegeneration: " + board.legalMovesTime / Math.pow(10, 9) + "s of "
                    + (System.nanoTime() - starttime) / Math.pow(10, 9));
            System.out.println("make move: " + board.makeMoveTime / Math.pow(10, 9) + "s of "
                    + (System.nanoTime() - starttime) / Math.pow(10, 9));
            System.out.println("undo move: " + board.undoTime / Math.pow(10, 9) + "s of "
                    + (System.nanoTime() - starttime) / Math.pow(10, 9));
        }

        return bestMove;
    }


    public void negamax(Board board, int depth)
    {

        if (depth <= 0)
        {
            movecount++;
            if (board.playedMoves.get(board.playedMoves.size() - 1).isCapture)
            {
                captureCount += 1;
            }
            if (board.playedMoves.get(board.playedMoves.size() - 1).isPromotion)
            {
                promotionCount += 1;
            }
            if (board.playedMoves.get(board.playedMoves.size() - 1).isEnPassent)
            {
                enPassentcount += 1;
            }
            if (board.isInCheck())
            {
                checkCount += 1;
            }
            if (board.playedMoves.get(board.playedMoves.size() - 1).isCastling)
            {
                castleCount += 1;
            }
            if (board.isCheckmate())
            {
                checkmateCount += 1;
            }
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

    /**
     *
     * @return An array with nodes, captures, en-passents, castles, promotions, checks, checkmates
     */
    public long[] getResults()
    {
        return new long[]{movecount, captureCount, enPassentcount, castleCount, promotionCount, checkCount, checkmateCount};
    }
}
