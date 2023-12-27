package chess.Bots;

import chess.Models.ChessModel;
import chess.Models.Move;

public class V1 implements IBot
{
    private Move bestMove;

    public Move think(ChessModel board)
    {

        // Iterative deepening
        for (int i = 0; i < 5; i++)
        {
            negamax();
        }
        return bestMove;
    }

    public void negamax()
    {

    }

}
