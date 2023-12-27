package chess.Bots;

import java.util.Random;
import chess.Models.ChessModel;
import chess.Models.Move;

public class Randombot implements IBot
{
    public Move think(ChessModel board)
    {
        Random rand = new Random();
        Move[] legalmoves =  board.getLegalMoves();
        return legalmoves[rand.nextInt(legalmoves.length)];
    }
}
