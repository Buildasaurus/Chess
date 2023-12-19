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
        System.out.println("squares same: " + (otherMove.startSquare == startSquare));
        return otherMove.startSquare.equals(startSquare) && otherMove.targetSquare.equals(targetSquare)
                && otherMove.isCapture == isCapture && otherMove.isPromotion == isPromotion
                && otherMove.isEnPassent == isEnPassent;
    }
}
