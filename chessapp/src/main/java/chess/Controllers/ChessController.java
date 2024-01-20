package chess.Controllers;

import chess.Settings;
import chess.Models.Board;
import chess.Models.Point;
import chess.Views.ChessView;
import javafx.scene.input.MouseEvent;

public class ChessController
{
    ChessView view;
    Board model;

    public ChessController(ChessView _view, Board _model)
    {
        model = _model;
        view = _view;

        // Add a mouse pressed event handler to the view
        view.setOnMousePressed(this::handleMousePress);

        // Add a mouse released event handler to the view
        view.setOnMouseReleased(this::handleMouseRelease);
        view.initializeBoard(model.board);
    }

    Point start;

    private void handleMousePress(MouseEvent event)
    {
        // Get the x and y coordinates of the mouse click
        double x = event.getX();
        double y = Settings.getBoardSize() - event.getY();

        // Convert the coordinates to a position on the chess board
        int row = (int) (y / Settings.getColumnWidth());
        int col = (int) (x / Settings.getColumnWidth());

        start = new Point(col, row);
    }

    Point end;

    private void handleMouseRelease(MouseEvent event)
    {
        // Get the x and y coordinates of the mouse click
        double x = event.getX();
        double y = Settings.getBoardSize() - event.getY();

        // Convert the coordinates to a position on the chess board
        int row = (int) (y / Settings.getColumnWidth());
        int col = (int) (x / Settings.getColumnWidth());

        end = new Point(col, row);
        model.movePiece(start, end);
        // Handle mouse release event
        view.updateBoard(model.board);
        System.out.println(model.getZobristHash());

        if (model.isCheckmate())
        {
            view.showWinner(!model.whiteToMove);
        }
        if (model.isDraw())
        {
            view.displayDraw();
        }

    }
}
