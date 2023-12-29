package chess.Views;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import chess.Models.Piece;
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
        double columnSize = 100; // board will be 8 times this size
        double dimension = 8 * columnSize;
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();

        for (int i = 0; i < 8; i++)
        {
            ColumnConstraints column = new ColumnConstraints(columnSize);
            RowConstraints row = new RowConstraints(columnSize);
            this.getColumnConstraints().add(column);
            this.getRowConstraints().add(row);
        }

        Image backgroundImage = new Image("/board.png");
        BackgroundSize backgroundSize =
                new BackgroundSize(dimension, dimension, false, false, false, false);
        BackgroundImage background =
                new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
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
                // Text text = new Text(x + " " + y);
                // this.add(text, x, 7 - y);
                if (pieces[y][x] != null)
                {
                    ImageView imageView = new ImageView(pieces[y][x].getImage());
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    this.add(imageView, x, 7 - y);
                }
            }
        }
    }

    public void showWinner(boolean WhiteWon)
    {
        String winner = WhiteWon ? "White" : "Black";
        System.out.println(winner + " Won!");
    }

    public void displayDraw()
    {
        System.out.println("Draw");
    }
}
