package chess.Models.Bots;

import java.util.Random;
import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;

public class Randombot implements IBot
{
    public Move think(Board board, Timer timer)
    {
        Random rand = new Random();
        Move[] legalmoves =  board.getLegalMoves();
        return legalmoves[rand.nextInt(legalmoves.length)];
    }
}
