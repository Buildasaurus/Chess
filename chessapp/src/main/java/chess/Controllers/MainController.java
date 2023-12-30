package chess.Controllers;

import chess.Models.Board;
import chess.Views.ChessView;
import chess.Views.GUIView;
import chess.Views.GUIView.ButtonClickListener;

public class MainController implements ButtonClickListener
{
    Board model;
    ChessView board;
    public GUIView view;
    Object controller;
    boolean isWhite = true;
    public MainController()
    {
        model = new Board(); // Create your model
        board = new ChessView(); // Create your view
        view = new GUIView(board, this); // Create your view

        controller = new HumanVsComputerController(board, model, isWhite);
    }

    public GUIView getView()
    {
        return view;
    }

    public void onButtonClick(String buttonName)
    {

        if (buttonName.equals("Human VS Human"))
        {
            model = new Board(); // Reset the board

            controller = new ChessController(board, model);
        }
        if (buttonName.equals("Human VS PC"))
        {
            model = new Board(); // Reset the board

            controller = new HumanVsComputerController(board, model, isWhite);
            isWhite = !isWhite;
        }
    }

}
