package chess.Bots;

import chess.Models.Board;
import chess.Models.Move;


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

    public Move think(Board board)
    {
        long starttime = System.nanoTime();
        for (int i = 1; i < 5; i++)
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
            System.out.println("nodes\tCapture\tE.P\tcastles\tPromo\tChecks\tCheckmates");

            System.out.print(movecount + "\t");
            System.out.print(captureCount + "\t");
            System.out.print(enPassentcount + "\t");
            System.out.print(castleCount + "\t");
            System.out.print(promotionCount + "\t");
            System.out.print(checkCount + "\t");
            System.out.print(checkmateCount + "\n");
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
}
