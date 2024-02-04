package chess.Controllers;


import chess.Resources;
import chess.Models.Board;
import chess.Models.FenReader;
import chess.Models.Bots.*;
import chess.Views.ChessView;
import chess.Views.GUIView;
import chess.Views.GUIView.ButtonClickListener;


public class MainController implements ButtonClickListener
{
    Board model;
    ChessView board;
    public GUIView view;
    Object controller;
    boolean isWhite = false;
    int currentFenString = 0;
    String[] fenStrings;

    public MainController()
    {

        fenStrings = Resources.getFileByName("fens");


        model = FenReader.loadFenString(fenStrings[currentFenString % fenStrings.length]);
        board = new ChessView();
        view = new GUIView(board, this);

        controller = new HumanVsComputerController(board, model, isWhite, new V2());

    }

    public GUIView getView()
    {
        return view;
    }

    public void onButtonClick(String buttonName)
    {
        // Reset the board
        model = FenReader.loadFenString(fenStrings[currentFenString % fenStrings.length]);
        currentFenString++;

        if (buttonName.equals("Human VS Human"))
        {
            controller = new HumanVsHumanController(board, model);
        }
        if (buttonName.equals("Human VS TesterBot"))
        {
            controller =
                    new HumanVsComputerController(board, model, model.whiteToMove, new TesterBot());
            isWhite = !isWhite;
        }
        if (buttonName.equals("Human VS V1"))
        {
            controller = new HumanVsComputerController(board, model, model.whiteToMove, new V1());
            isWhite = !isWhite;
        }
        if (buttonName.equals("Human VS V2"))
        {
            controller = new HumanVsComputerController(board, model, model.whiteToMove, new V2());
            isWhite = !isWhite;
        }
        if (buttonName.equals("Human VS MyBot"))
        {
            controller = new HumanVsComputerController(board, model, model.whiteToMove, new MyBot());
            isWhite = !isWhite;
        }
    }

}
