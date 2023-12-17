package chess;

import java.io.FileInputStream;
import javafx.scene.image.Image;

public class Piece
{
    public enum PieceType
    {
        King, Queen, Rook, Bishop, Knight, Pawn, None
    }

    Image image;

    public boolean isWhite;
    public PieceType type;
    public Point position;

    public Piece(boolean _isWhite, PieceType _type, Point startPosition)
    {
        position = startPosition;
        isWhite = _isWhite;
        type = _type;
        try
        {
            String imageName;
            switch (type)
            {
                case Pawn:
                    imageName = "BlackPawn";
                    break;
                case Bishop:
                    imageName = "pawnNot";
                    break;
                case King:
                    if (isWhite)
                        imageName = "BlackPawn";
                    else
                    {
                        imageName = "BlackKing";
                    }
                    break;
                case Knight:
                    imageName = "BlackKnight";
                    break;
                default:
                    imageName = "pawnNot";
                    break;
            }

            FileInputStream fis = new FileInputStream(
                    "F:\\Github Projects\\Chess\\chessapp\\src\\main\\resources\\" + imageName
                            + ".png");
            image = new Image(fis);
        }
        catch (Exception e)
        {
            System.out.println("Image not found");
        }

    }

    public Image getImage()
    {
        return image;
    }

}
