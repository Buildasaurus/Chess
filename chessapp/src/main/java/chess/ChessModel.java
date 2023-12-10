package chess;

import chess.Piece.PieceType;

public class ChessModel
{
    private Move[] legalMoves;
    Piece[][] board;
    boolean whiteToMove = true;

    void movePiece(Square from, Square to)
    {
        Piece pieceToMove = board[from.rank][from.file];
        if (pieceToMove.type != PieceType.None && pieceToMove.isWhite == whiteToMove)
        // legal piece to try to move
        {

        }
        return;
    }

    public Move[] getLegalMoves()
    {
        if (legalMoves != null)
        {
            return legalMoves;
        }
        else
        {
            legalMoves = new Move[10];
        }
        return legalMoves;
    }

    boolean isCheckmate()
    {
        return true;
    }

    boolean isDraw()
    {
        return false;
    }


}
