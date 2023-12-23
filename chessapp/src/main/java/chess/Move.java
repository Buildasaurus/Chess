package chess;

public class Move
{
    public Point startSquare;
    public Point targetSquare;
    public boolean isCapture;
    public boolean isPromotion;
    public boolean isEnPassent;
    public boolean isCastling;

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

    @Override
    public String toString()
    {
        return (char) (startSquare.x + 65) + Integer.toString(startSquare.y + 1) + " to "
                + (char) (targetSquare.x + 65) + Integer.toString(targetSquare.y + 1);
    }
}
