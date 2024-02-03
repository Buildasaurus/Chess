package chess.Models.Bots;

import chess.Models.Board;
import chess.Models.Move;
import chess.Models.Timer;

public interface IBot
{
    public Move think(Board board, Timer timer);
}
