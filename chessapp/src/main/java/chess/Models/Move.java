package chess.Models;

import chess.Models.Piece.PieceType;

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

    public Move(Point _startSquare, Point _targetSquare)
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
        return (char) (startSquare.x + 65) + Integer.toString(startSquare.y + 1)
                + (char) (targetSquare.x + 65) + Integer.toString(targetSquare.y + 1)
                + (isPromotion ? "Q" : "");
    }

    public void setCapturePieceType(PieceType type)
    {
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
        Move move = new Move(startSquare, targetSquare);
        move.firstMove = firstMove;
        move.isCapture = isCapture;
        move.isCastling = isCastling;
        move.isEnPassent = isEnPassent;
        move.promotionType = promotionType;
        move.isPromotion = isPromotion;
        move.setCapturePieceType(capturePieceType);
        return move;
    }
}
