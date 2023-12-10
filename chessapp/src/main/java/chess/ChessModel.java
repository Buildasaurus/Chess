package chess;

import java.util.ArrayList;
import java.util.Arrays;
import chess.Piece.PieceType;

public class ChessModel
{
    private Move[] legalMoves;
    Piece[][] board;
    boolean whiteToMove = true;
    ArrayList<Piece> whitePieces;
    ArrayList<Piece> blackPieces;

    public ChessModel()
    {
        board = new Piece[8][8];
        whitePieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(true, PieceType.Rook, new Square(0, 0)),
                        new Piece(true, PieceType.Knight, new Square(1, 0)),
                        new Piece(true, PieceType.Bishop, new Square(2, 0)),
                        new Piece(true, PieceType.Queen, new Square(3, 0)),
                        new Piece(true, PieceType.King, new Square(4, 0)),
                        new Piece(true, PieceType.Bishop, new Square(5, 0)),
                        new Piece(true, PieceType.Knight, new Square(6, 0)),
                        new Piece(true, PieceType.Rook, new Square(7, 0))));

        whitePieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(true, PieceType.Rook, new Square(0, 0)),
                        new Piece(false, PieceType.Knight, new Square(1, 0)),
                        new Piece(false, PieceType.Bishop, new Square(2, 0)),
                        new Piece(false, PieceType.Queen, new Square(3, 0)),
                        new Piece(false, PieceType.King, new Square(4, 0)),
                        new Piece(false, PieceType.Bishop, new Square(5, 0)),
                        new Piece(false, PieceType.Knight, new Square(6, 0)),
                        new Piece(false, PieceType.Rook, new Square(7, 0))));
        for (Piece piece : blackPieces)
        {
            board[piece.position.rank][piece.position.file] = piece;
        }
        for (Piece piece : whitePieces)
        {
            board[piece.position.rank][piece.position.file] = piece;
        }
        // add pawns
        for (int i = 0; i < board.length; i++)
        {
            Piece whitePawn = new Piece(true, PieceType.Pawn, new Square(1, i));
            Piece blackPawn = new Piece(false, PieceType.Pawn, new Square(7, i));
            whitePieces.add(whitePawn);
            whitePieces.add(blackPawn);
            board[1][i] = whitePawn;
            board[7][i] = whitePawn;
        }


    }

    void movePiece(Square start, Square end)
    {
        Piece pieceToMove = board[start.rank][start.file];
        Move move = new Move(start, end);
        if (pieceToMove.type != PieceType.None && pieceToMove.isWhite == whiteToMove)
        // legal piece to try to move
        {
            for (Move legalMove : getLegalMoves())
            {
                if (move.equals(legalMove))
                {
                    // TODO do stuff, move something
                }
            }
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
            ArrayList<Piece> pieces = whiteToMove ? whitePieces : blackPieces;

            //TODO : generate legal moves.
            for (Piece piece : pieces)
            {
                switch(piece.type)
                {
                    case Pawn:
                        if(end.file != start.file)
                        {
                            if(end.rank == start.rank + 1 && board[end.rank][end.file].type != PieceType.None && board[end.rank][end.file])
                        }
                        //promotion
                        if(start.rank == 6)
                        {

                        }
                        for(int i = 1; i < 3; i++)

                }
                board[pieceToMove.position.rank][pieceToMove.position.file]
            }

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
