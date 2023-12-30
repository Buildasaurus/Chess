package chess.Bots;

import java.util.Random;
import chess.Models.Board;
import chess.Models.Move;

public class Randombot implements IBot
{
    public Move think(Board board)
    {
        Random rand = new Random();
        Move[] legalmoves =  board.getLegalMoves();
        return legalmoves[rand.nextInt(legalmoves.length)];
    }
}
