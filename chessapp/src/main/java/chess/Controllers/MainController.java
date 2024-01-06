package chess.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import chess.Models.Board;
import chess.Models.FenReader;
import chess.Views.ChessView;
import chess.Views.GUIView;
import chess.Views.GUIView.ButtonClickListener;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class MainController implements ButtonClickListener
{
    Board model;
    ChessView board;
    public GUIView view;
    Object controller;
    boolean isWhite = true;
    int currentFenString = 0;
    String[] fenStrings;

    public MainController()
    {

        try
        {
            // Get the path to the fens.txt file in the resources folder
            Path path = Paths.get(getClass().getResource("/fens.txt").toURI());
            // Read all lines from the file
            List<String> lines = Files.readAllLines(path);
            // Convert the List to an array
            fenStrings = lines.toArray(new String[0]);
        }
        catch (IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }

        model = FenReader.loadFenString(fenStrings[currentFenString % fenStrings.length]); // Create
                                                                                           // your
                                                                                           // model
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
        currentFenString += 1; // TODO this might be too often, if you make more buttons
        // Reset the board
        model = FenReader.loadFenString(fenStrings[currentFenString % fenStrings.length]);

        if (buttonName.equals("Human VS Human"))
        {
            controller = new ChessController(board, model);
        }
        if (buttonName.equals("Human VS PC"))
        {
            controller = new HumanVsComputerController(board, model, model.whiteToMove);
            isWhite = !isWhite;
        }
    }

}
