package chess;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.File;
import java.net.MalformedURLException;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;


public class ChessView extends GridPane
{
    public void initializeBoard(Piece[][] pieces)
    {
        double size = 100;

        for (int i = 0; i < 8; i++)
        {
            ColumnConstraints column = new ColumnConstraints(size);
            RowConstraints row = new RowConstraints(size);
            this.getColumnConstraints().add(column);
            this.getRowConstraints().add(row);
        }

        Image backgroundImage = new Image("/board.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        this.setBackground(new Background(background));
        updateBoard(pieces);



    }

    public void updateBoard(Piece[][] pieces)
    {
        this.getChildren().clear(); // Clear the current view

        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                Text text = new Text(x + " " + y);
                this.add(text, x, 7 - y);
                if (pieces[y][x] != null)
                {
                    ImageView imageView = new ImageView(pieces[y][x].getImage());
                    this.add(imageView, x, 7 - y);
                }
            }
        }
    }
}
