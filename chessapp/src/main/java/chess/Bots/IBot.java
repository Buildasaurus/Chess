package chess.Bots;

import chess.Models.Board;
import chess.Models.Move;

public interface IBot
{
    public Move think(Board board);
}
