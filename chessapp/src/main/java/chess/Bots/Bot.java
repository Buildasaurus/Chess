package chess.Bots;

import chess.Models.ChessModel;
import chess.Models.Move;

public interface Bot
{
    public Move think(ChessModel board);
}
