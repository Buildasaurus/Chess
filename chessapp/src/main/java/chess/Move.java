package chess;

public class Move {
    public Square startSquare;
    public Square targetSquare;
    public Move(Square _startSquare, Square _targetSquare)
    {
        startSquare = _startSquare;
        targetSquare = _targetSquare;
    }
}
