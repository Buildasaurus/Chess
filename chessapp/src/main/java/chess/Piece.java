package chess;



public class Piece
{
    public enum PieceType
    {
        King, Queen, Rook, Bishop, Knight, Pawn, None
    }

    public boolean isWhite;
    public PieceType type;
    public Piece(boolean _isWhite, PieceType _type)
    {
        isWhite = _isWhite;
        type = _type;
    }
}
