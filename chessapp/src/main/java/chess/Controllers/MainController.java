package chess.Controllers;

import chess.Models.ChessModel;
import chess.Views.ChessView;
import chess.Views.GUIView;
import chess.Views.GUIView.ButtonClickListener;

public class MainController implements ButtonClickListener
{
    ChessModel model;
    ChessView board;
    public GUIView view;
    Object controller;

    public MainController()
    {
        model = new ChessModel(); // Create your model
        board = new ChessView(); // Create your view
        view = new GUIView(board, this); // Create your view

        controller = new HumanVsComputerController(board, model);
    }

    public GUIView getView()
    {
        return view;
    }

    public void onButtonClick(String buttonName)
    {

        if (buttonName.equals("Human VS Human"))
        {
            model = new ChessModel(); // Reset the board

            controller = new ChessController(board, model);
        }
        if (buttonName.equals("Human VS PC"))
        {
            model = new ChessModel(); // Reset the board

            controller = new HumanVsComputerController(board, model);
        }
    }

}
