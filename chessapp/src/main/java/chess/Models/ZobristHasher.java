package chess.Models;

import java.util.Random;

// Static class
// Assumes board is of size 64.
public class ZobristHasher
{


    static long[][][] zobristTable;
    static int boardWidth = 8;
    static int boardHeight = 8;
    /**
     * 0-11 is pawn, knight, bishop, rook, quuen, king, for black and white 12 is enpassent black,
     * 13 is enpassent white.
     */
    static int pieceTypes = 14;
    static int whiteToMoveHash;
    static long[] castlinghashes;

    static
    {
        initalizeValues();
    }


    static void initalizeValues()
    {
        castlinghashes = new long[4];
        zobristTable = new long[boardHeight][boardWidth][pieceTypes];
        Random rand = new Random();
        for (int row = 0; row < boardHeight; row++)
        {
            for (int column = 0; column < boardWidth; column++)
            {
                for (int pieceType = 0; pieceType < pieceTypes; pieceType++)
                {
                    zobristTable[row][column][pieceType] = rand.nextLong();
                }
            }
        }
        for (int i = 0; i < castlinghashes.length; i++)
        {
            castlinghashes[i] = rand.nextLong();
        }
        whiteToMoveHash = rand.nextInt();
    }

    static long generateHash(Board board)
    {
        long hash = board.whiteToMove ? whiteToMoveHash : 0;
        for (int row = 0; row < board.board.length; row++)
        {
            for (int column = 0; column < board.board[0].length; column++)
            {
                Piece piece = board.board[row][column];
                if (piece != null) // if == null, then just don't do xor
                {
                    int whitePiece = piece.isWhite ? 0 : pieceTypes/2;
                    hash ^= zobristTable[row][column][piece.type.ordinal() + whitePiece];
                }
            }
        }
        if (board.canCastle(true, true))
        {
            hash ^= castlinghashes[0];
        }
        if (board.canCastle(true, false))
        {
            hash ^= castlinghashes[1];
        }
        if (board.canCastle(false, true))
        {
            hash ^= castlinghashes[2];
        }
        if (board.canCastle(false, false))
        {
            hash ^= castlinghashes[3];
        }
        Piece enPassentPawn =  board.enPassentablePawn;
        if(enPassentPawn != null)
        {
            hash ^=  zobristTable[enPassentPawn.position.y][enPassentPawn.position.x][enPassentPawn.isWhite ? 12 : 13];
        }
        return hash;
    }
}
