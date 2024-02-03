package chess.Models;

public class TTEntry
{
    public long hash; // Needed to make sure there are no hash collisions, as the table can't store
                      // all 2^63 different options.
    public Move bestMove;
    public byte depth;
    /**
     * Flag for the entry:
     * 1 means that exact score is stored.
     * 2 means an lower bound is stored
     * 3 means a upper bound is stored
     */
    public byte flag;
    public int evaluation;

    public TTEntry(long _hash, Move _bestMove, byte _depth, byte _flag, int _evaluation)
    {
        hash = _hash;
        bestMove = _bestMove;
        depth = _depth;
        flag = _flag;
        evaluation = _evaluation;
    }

    public void update(long _hash, Move _bestMove, byte _depth, byte _flag, int _evaluation)
    {
        hash = _hash;
        bestMove = _bestMove;
        depth = _depth;
        flag = _flag;
        evaluation = _evaluation;
    }
}
