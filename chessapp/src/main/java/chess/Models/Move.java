package chess.Models;

import chess.Models.Piece.PieceType;

public class Move
{
    public Point startSquare;
    public Point targetSquare;
    public boolean isCapture;
    public boolean isPromotion;
    public boolean isEnPassent;
    public boolean isCastling;
    public boolean firstMove = false;
    private PieceType capturePieceType;

    public Move(Point _startSquare, Point _targetSquare)
    {
        startSquare = _startSquare;
        targetSquare = _targetSquare;
        isCapture = false;
        isPromotion = false;
        isEnPassent = false;
    }

    public boolean equals(Move otherMove)
    {
        return otherMove.startSquare.equals(startSquare)
                && otherMove.targetSquare.equals(targetSquare);
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
        isCapture = true;
    }
    public PieceType getCapturePieceType()
    {
        return capturePieceType;
    }
}
