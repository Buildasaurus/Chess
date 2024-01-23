package chess.Models;

import chess.Resources;
import javafx.scene.image.ImageView;

public class Piece
{
    public enum PieceType
    {
        Pawn, Knight, Bishop, Rook, Queen, King
    }

    public boolean hasMoved = false;
    public boolean isWhite;
    public PieceType type;
    public Point position;

    public Piece(boolean _isWhite, PieceType _type, Point startPosition)
    {
        position = startPosition;
        isWhite = _isWhite;
        type = _type;
    }



    public ImageView getImage()
    {
        String imageName;
        imageName = isWhite ? "white-" : "black-";
        imageName += type.toString().toLowerCase();

        ImageView image = new ImageView(Resources.getImageByName(imageName));
        return image;
    }

    public boolean equals(Piece other)
    {
        return other.hasMoved == hasMoved && other.isWhite == isWhite
                && other.position.equals(position) && other.type == type;
    }

}
