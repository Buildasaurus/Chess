package chess.Bots;

import chess.Models.ChessModel;
import chess.Models.Move;

public interface IBot
{
    public Move think(ChessModel board);
}
