package chess.Models.Board;

public class PieceList
{
    int[] occupiedSquares;

    /*
     * At the i'th index, the position of the i'th piece will be in the occupiedSquares array. A
     * position could be 42, which is 42%8 = 2 = C file and 42/8 = 5 = 6th rank, so C6. This would
     * eg be a 0, meaning that the piece that is there, is at index 0 in the occupiedSquares array
     */
    int[] map;

    int pieceCount = 0;


    public PieceList(int maxPossiblePieces)
    {
        occupiedSquares = new int[maxPossiblePieces];
        map = new int[64];
    }

    public void addPieceAtSquare(int square)
    {
        occupiedSquares[pieceCount] = square;
        map[square] = pieceCount;
        pieceCount++;
    }
    public void addPieceAtSquare(Point square)
    {
        addPieceAtSquare(pointToInt(square));

    }

    public void removePieceAtSquare(int square)
    {
        // get the index of this element in the occupiedSquares array
        int pieceIndex = map[square];

        // move last element in array to the place of the removed element
        occupiedSquares[pieceIndex] = occupiedSquares[pieceCount - 1];
        map[occupiedSquares[pieceIndex]] = pieceIndex;
        // update map to point to the moved element's new location in the array
        pieceCount--;
    }

    public void removePieceAtSquare(Point square)
    {
        removePieceAtSquare(pointToInt(square));
    }

    public void removePieceAtSquare(int x, int y)
    {
        removePieceAtSquare(pointToInt(x, y));
    }

    public void movePiece(Point startSquare, Point targetSquare)
    {
        int pieceIndex = map[pointToInt(startSquare)];
        // get the index of this element in the occupiedSquares array
        occupiedSquares[pieceIndex] = pointToInt(targetSquare);
        map[pointToInt(targetSquare)] = pieceIndex;
    }

    private int pointToInt(Point point)
    {
        return point.x + point.y*8;
    }

    private int pointToInt(int x, int y)
    {
        return x + y*8;
    }
}
