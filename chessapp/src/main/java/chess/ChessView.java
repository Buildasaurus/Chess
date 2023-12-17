package chess;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ChessView extends GridPane
{
    public void updateBoard(Piece[][] pieces)
    {
        this.getChildren().clear(); // Clear the current view

        double size = 100;

        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();

        for (int i = 0; i < 8; i++)
        {
            ColumnConstraints column = new ColumnConstraints(size);
            RowConstraints row = new RowConstraints(size);
            this.getColumnConstraints().add(column);
            this.getRowConstraints().add(row);
        }

        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                Text text = new Text(x + " " + y);
                this.add(text, x, 7-y);
                if (pieces[y][x] != null)
                {
                    ImageView imageView = new ImageView(pieces[y][x].getImage());
                    this.add(imageView, x, 7-y);
                }
            }
        }
    }
}
