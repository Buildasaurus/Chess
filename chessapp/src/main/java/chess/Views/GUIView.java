package chess.Views;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GUIView extends BorderPane {
    private ChessView chessView;
    private Button button1, button2; // Add more buttons as needed

    public GUIView(ChessView chessView) {
        this.chessView = chessView;

        // Create buttons for different game modes
        button1 = new Button("Game Mode 1");
        button2 = new Button("Game Mode 2");
        // Add buttons to a horizontal box
        HBox hbox = new HBox(button1, button2);

        // Add the chess view and buttons to this GUI view
        this.setCenter(chessView);
        this.setBottom(hbox);

    }
}
