package chess.Models;

import chess.Models.Piece.PieceType;
import chess.Utils.BoardHelper;

public class Move
{
    public Point startSquare;
    public Point targetSquare;
    public boolean isCapture;
    public boolean isPromotion;
    public boolean isEnPassent;
    public PieceType promotionType;
    public boolean isCastling;
    private boolean firstMove;
    private PieceType capturePieceType;
    public boolean capturedPieceHadMoved;
    public int previousHalfPlyCount;

    public Move(Point _startSquare, Point _targetSquare)
    {
        System.out.println(
                "Warning, incorrect constructor! - Only for testing - missing halfMoveCount");
        firstMove = false;
        startSquare = _startSquare;
        targetSquare = _targetSquare;
        isCapture = false;
        isPromotion = false;
        isEnPassent = false;
        promotionType = PieceType.Queen;
    }

    public Move(Point _startSquare, Point _targetSquare, int halfMoveCount)
    {
        firstMove = false;
        startSquare = _startSquare;
        targetSquare = _targetSquare;
        isCapture = false;
        isPromotion = false;
        isEnPassent = false;
        promotionType = PieceType.Queen;
    }


    public boolean equals(Move otherMove)
    {
        return otherMove.startSquare.equals(startSquare)
                && otherMove.targetSquare.equals(targetSquare)
                && promotionType == otherMove.promotionType;
    }

    /**
     * Prints Uci format of the move.
     */
    @Override
    public String toString()
    {
        String promotionString = "";
        if(isPromotion)
        {
            switch (promotionType)
            {
                case Queen:
                    promotionString = "q";
                    break;
                case Knight:
                    promotionString = "n";
                    break;
                case Rook:
                    promotionString = "r";
                    break;
                case Bishop:
                    promotionString = "b";
                    break;
                default:
                    promotionString = "";
                    break;
            }
        }

        return (char) (startSquare.x + 97) + Integer.toString(startSquare.y + 1)
                + (char) (targetSquare.x + 97) + Integer.toString(targetSquare.y + 1)
                + promotionString;
    }

    public void setCapturePieceType(PieceType type)
    {
        if (type == PieceType.King)
        {
            System.out.println("Someone just took a king...");
        }
        capturePieceType = type;
        if (type != null)
        {
            isCapture = true;
        }
    }

    public PieceType getCapturePieceType()
    {
        return capturePieceType;
    }

    public void setFirstMove(boolean value)
    {
        firstMove = value;
    }

    public boolean getFirstMove()
    {
        return firstMove;
    }

    public Move copy()
    {
        Move move = new Move(startSquare, targetSquare, previousHalfPlyCount);
        move.setFirstMove(firstMove);
        move.capturedPieceHadMoved = capturedPieceHadMoved;
        move.isCapture = isCapture;
        move.isCastling = isCastling;
        move.isEnPassent = isEnPassent;
        move.promotionType = promotionType;
        move.isPromotion = isPromotion;
        move.setCapturePieceType(capturePieceType);
        return move;
    }
}
