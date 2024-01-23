package chess.Controllers;

import chess.Models.Board;
import chess.Views.ChessView;
import javafx.scene.input.MouseEvent;

public class HumanVsHumanController extends GameController
{


    public HumanVsHumanController(ChessView _view, Board _model)
    {
        super(_view, _model);
    }

    protected void handleMouseRelease(MouseEvent event)
    {
        super.handleMouseRelease(event);
        if (model.tryToMakeMove(start, end))
        {
            timer.switchTurn();
            refreshView();
        }
    }
}
