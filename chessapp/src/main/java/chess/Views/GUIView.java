package chess.Views;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GUIView extends BorderPane
{
    private ChessView chessView;
    private Button button1, button2, button3; // Add more buttons as needed

    public interface ButtonClickListener
    {
        void onButtonClick(String buttonText);
    }

    public GUIView(ChessView chessView, ButtonClickListener listener)
    {
        this.chessView = chessView;

        // Create buttons for different game modes
        button1 = new Button("Human VS TesterBot");
        button2 = new Button("Human VS Human");
        button3 = new Button("Human VS V1");

        // Add buttons to a horizontal box
        HBox hbox = new HBox(button1, button2, button3);

        // Set the button click listeners
        button1.setOnAction(e -> listener.onButtonClick(button1.getText()));
        button2.setOnAction(e -> listener.onButtonClick(button2.getText()));
        button3.setOnAction(e -> listener.onButtonClick(button3.getText()));


        // Add the chess view and buttons to this GUI view
        this.setCenter(chessView);
        this.setBottom(hbox);
    }
}
