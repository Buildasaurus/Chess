package chess;

public class Move {
    public Square startSquare;
    public Square targetSquare;
    public boolean isCapture;
    public boolean isPromotion;
    public boolean isEnPassent;
    public Move(Square _startSquare, Square _targetSquare)
    {
        startSquare = _startSquare;
        targetSquare = _targetSquare;
    }
}
