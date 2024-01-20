package chess.Models;

import java.util.Random;

// Static class
// Assumes board is of size 64.
public class ZobristHasher
{


    static int[][][] zobristTable;
    static int boardWidth = 8;
    static int boardHeight = 8;
    /**
     * 0-11 is pawn, knight, bishop, rook, quuen, king, for black and white
     * 12 is white queen rook, castlingright, 13 is king castling right. 14 and 15 is for black.
     * 16 is enpassent white, 17 enpssent black
     */
    static int pieceTypes = 18;
    static int whiteToMoveHash;

    static
    {
        initalizeValues();
    }


    static void initalizeValues()
    {
        zobristTable = new int[boardHeight][boardWidth][pieceTypes];
        Random rand = new Random();
        for (int row = 0; row < boardHeight; row++)
        {
            for (int column = 0; column < boardWidth; column++)
            {
                for (int pieceType = 0; pieceType < pieceTypes; pieceType++)
                {
                    zobristTable[row][column][pieceType] = rand.nextInt();
                }
            }
        }
        whiteToMoveHash = rand.nextInt();
    }

    static int generateHash(Board board)
    {
        int hash = board.whiteToMove ? whiteToMoveHash : 0;
        for (int row = 0; row < board.board.length; row++)
        {
            for (int column = 0; column < board.board[0].length; column++)
            {
                Piece piece = board.board[row][column];
                if (piece != null) // if == null, then just don't do xor
                {
                    int whitePiece = piece.isWhite ? 0 : 9;
                    hash ^= zobristTable[row][column][piece.type.ordinal() + whitePiece];

                    //TODO - special cases for enpassent things
                }
            }
        }
        return hash;
    }
}
