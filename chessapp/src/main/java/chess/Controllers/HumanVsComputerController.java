package chess.Controllers;

import chess.Bots.IBot;
import chess.Bots.Randombot;
import chess.Bots.V1;
import chess.Models.Board;
import chess.Models.Point;
import chess.Views.ChessView;
import javafx.scene.input.MouseEvent;

public class HumanVsComputerController
{
    ChessView view;
    Board model;
    public boolean computerIsWhite = false;
    IBot bot = new V1();

    public HumanVsComputerController(ChessView _view, Board _model, boolean _computerIsWhite)
    {
        computerIsWhite = _computerIsWhite;
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
        double y = 800 - event.getY();

        // Convert the coordinates to a position on the chess board
        int row = (int) (y / 100);
        int col = (int) (x / 100);

        start = new Point(col, row);
    }

    Point end;

    private void handleMouseRelease(MouseEvent event)
    {
        if (model.isCheckmate())
        {
            return;
        }
        // Get the x and y coordinates of the mouse click
        double x = event.getX();
        double y = 800 - event.getY();

        // Convert the coordinates to a position on the chess board
        int row = (int) (y / 100);
        int col = (int) (x / 100);

        end = new Point(col, row);
        if (model.whiteToMove != computerIsWhite)
        {
            model.movePiece(start, end);
        }
        // Handle mouse release event
        view.updateBoard(model.board);

        if (model.isCheckmate())
        {
            view.showWinner(!model.whiteToMove);
        }
        else if (model.isDraw())
        {
            view.displayDraw();
        }
        else
        {
            if (model.whiteToMove == computerIsWhite)
            {
                model.tryToMakeMove(bot.think(model));
                view.updateBoard(model.board);
                if (model.isCheckmate())
                {
                    view.showWinner(!model.whiteToMove);
                }
            }
        }

    }
}
