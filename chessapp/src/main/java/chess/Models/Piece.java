package chess.Models;

import java.io.FileInputStream;
import javafx.scene.image.Image;

public class Piece
{
    public enum PieceType
    {
        King, Queen, Rook, Bishop, Knight, Pawn, None
    }

    Image image;
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

    public Image getImage()
    {
        if (image == null)
        {
            try
            {
                String imageName;
                imageName = isWhite ? "white-" : "black-";
                imageName += type.toString().toLowerCase();

                FileInputStream fis = new FileInputStream(
                        "F:\\Github Projects\\Chess\\chessapp\\src\\main\\resources\\" + imageName
                                + ".png");
                image = new Image(fis);
            }
            // "C:\\Githubting\\Chess\\chessapp\\src\\main\\resources\\"
            catch (Exception e)
            {
                System.out.println("Image not found");
            }
        }

        return image;
    }

    public boolean equals(Piece other)
    {
        return other.hasMoved == hasMoved && other.isWhite == isWhite
                && other.position.equals(position) && other.type == type;
    }

}
