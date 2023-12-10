package chess;



public class Piece
{
    public enum PieceType
    {
        King, Queen, Rook, Bishop, Knight, Pawn, None
    }

    public boolean isWhite;
    public PieceType type;
    public Square position;
    public Piece(boolean _isWhite, PieceType _type, Square startPosition)
    {
        position = startPosition;
        isWhite = _isWhite;
        type = _type;
    }
}
