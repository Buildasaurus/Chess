package chess.Controllers;

import chess.Models.Board.Board;
import chess.Models.Board.Move;
import chess.Models.Bots.IBot;
import chess.Views.ChessView;
import javafx.scene.input.MouseEvent;

public class HumanVsComputerController extends GameController
{
    public boolean computerIsWhite = false;
    IBot bot;

    public HumanVsComputerController(ChessView _view, Board _model, boolean _computerIsWhite,
            IBot computer)
    {
        super(_view, _model);
        computerIsWhite = _computerIsWhite;
        bot = computer;
        if (computerIsWhite == model.whiteToMove)
        {
            makeComputerMove();
        }
    }

    private void makeComputerMove()
    {
        Move botMove = bot.think(model, timer);
        if (timer.getRemainingTime(computerIsWhite) < 0)
        {
            System.out.println("PC ran out of time");
        }
        else if (model.tryToMakeMove(botMove))
        {
            timer.switchTurn();
            System.out.println("pc moved");
        }
        else
        {
            System.out.println("Computer made illegal move, human won.");
        }
        refreshView();
    }

    protected void handleMouseRelease(MouseEvent event)
    {
        super.handleMouseRelease(event);
        if (timer.getRemainingTime(!computerIsWhite) < 0)
        {
            System.out.println("You lost on time!");
        }
        if (model.isCheckmate()) // early catch, if the game already is over.
        {
            return;
        }
        if (model.whiteToMove != computerIsWhite)
        {
            if (model.tryToMakeMove(start, end))
            {

                timer.switchTurn();
                refreshView();
                if (!model.isCheckmate())
                {
                    makeComputerMove();
                }
            }
            else
            {
                System.out.println("Illegal move, try again.");
            }

        }
    }


}
