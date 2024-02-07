package chess.Models.Bots;

import chess.Models.Timer;
import chess.Models.Board.Board;
import chess.Models.Board.Move;

public interface IBot
{
    public Move think(Board board, Timer timer);
}
