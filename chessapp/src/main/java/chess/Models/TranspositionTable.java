package chess.Models;

public class TranspositionTable
{
    TTEntry[] table;

    int size;
    public TranspositionTable(int _size)
    {
        size = _size;
        // Size is 4*2^20 = 2^22 = 4*10^6 = 4 million - idk how many mbs this is. (yet)
        table = new TTEntry[size];
    }

    public TTEntry getEntry(long zobristHash)
    {
        // Anding with 0x3FFFFFFF as that is the same as and-ing with 1111111111111111111111,
        // essentially just keeping the first 22 bits of the hash.
        return table[(int) (zobristHash & size-1)];
    }

    public void setEntry(long zobristHash, TTEntry entry)
    {
        table[(int) (zobristHash & size-1)] = entry;
    }
}
