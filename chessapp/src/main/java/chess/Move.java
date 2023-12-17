package chess;

public class Move
{
    public Point startSquare;
    public Point targetSquare;
    public boolean isCapture;
    public boolean isPromotion;
    public boolean isEnPassent;

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
        return otherMove.startSquare == startSquare && otherMove.startSquare == startSquare
                && otherMove.isCapture == isCapture && otherMove.isPromotion == isPromotion
                && otherMove.isEnPassent == isEnPassent;
    }
}
