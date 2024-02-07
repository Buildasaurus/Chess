package chess.Models.Bots;

import java.util.Random;
import chess.Models.Timer;
import chess.Models.Board.Board;
import chess.Models.Board.Move;

public class Randombot implements IBot
{
    public Move think(Board board, Timer timer)
    {
        Random rand = new Random();
        Move[] legalmoves =  board.getLegalMoves();
        return legalmoves[rand.nextInt(legalmoves.length)];
    }
}
