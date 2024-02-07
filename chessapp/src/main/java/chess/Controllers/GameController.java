package chess.Controllers;

import chess.Settings;
import chess.Models.Timer;
import chess.Models.Board.Board;
import chess.Models.Board.Point;
import chess.Views.ChessView;
import javafx.scene.input.MouseEvent;

public abstract class GameController
{
    protected ChessView view;
    protected Board model;
    Point start;
    Point end;
    Timer timer;

    public GameController(ChessView _view, Board _model)
    {
        model = _model;
        view = _view;

        // Add a mouse pressed event handler to the view
        view.setOnMousePressed(this::handleMousePress);

        // Add a mouse released event handler to the view
        view.setOnMouseReleased(this::handleMouseRelease);
        view.initializeBoard(model.board);

        timer = new Timer(60000, 60000, 1000, model.whiteToMove);
    }


    /**
     * Calculates the start point of the mouse, as a square. Saves it in the {@link #start} variable
     *
     * @param event
     */
    protected void handleMousePress(MouseEvent event)
    {
        // Get the x and y coordinates of the mouse click
        double x = event.getX();
        double y = Settings.getBoardSize() - event.getY();

        // Convert the coordinates to a position on the chess board
        int row = (int) (y / Settings.getColumnWidth());
        int col = (int) (x / Settings.getColumnWidth());

        start = new Point(col, row);
    }

    protected void refreshView()
    {
        view.updateBoard(model.board);
        if (model.isCheckmate())
        {
            view.showWinner(!model.whiteToMove);
        }
        else if (model.isDraw())
        {
            view.displayDraw();
        }
        System.out.printf("Time is: (W: %d, B: %d)\n", timer.remainingTimeInSeconds(true), timer.remainingTimeInSeconds(false));

    }

    /**
     * Calculates the end point of the mouse, as a square. Saves it in the {@link #end} variable
     *
     * @param event
     */
    protected void handleMouseRelease(MouseEvent event)
    {
        // Get the x and y coordinates of the mouse click
        double x = event.getX();
        double y = Settings.getBoardSize() - event.getY();

        // Convert the coordinates to a position on the chess board
        int row = (int) (y / Settings.getColumnWidth());
        int col = (int) (x / Settings.getColumnWidth());

        end = new Point(col, row);
    }
}
