package chess;

import java.util.ArrayList;
import java.util.Arrays;
import javax.print.PrintException;
import chess.Piece.PieceType;

public class ChessModel
{
    private Move[] legalMoves;
    /**
     * Same objects as in blackPieces and whitePieces
     */
    Piece[][] board;
    boolean whiteToMove = true;
    /**
     * Same objects as in board
     */
    ArrayList<Piece> whitePieces;
    /**
     * Same objects as in board
     */
    ArrayList<Piece> blackPieces;

    public ChessModel()
    {
        board = new Piece[8][8];
        whitePieces =
                new ArrayList<Piece>(Arrays.asList(new Piece(true, PieceType.Rook, new Point(0, 0)),
                        new Piece(true, PieceType.Knight, new Point(1, 0)),
                        new Piece(true, PieceType.Bishop, new Point(2, 0)),
                        new Piece(true, PieceType.Queen, new Point(3, 0)),
                        new Piece(true, PieceType.King, new Point(4, 0)),
                        new Piece(true, PieceType.Bishop, new Point(5, 0)),
                        new Piece(true, PieceType.Knight, new Point(6, 0)),
                        new Piece(true, PieceType.Rook, new Point(7, 0))));

        whitePieces =
                new ArrayList<Piece>(Arrays.asList(new Piece(true, PieceType.Rook, new Point(0, 0)),
                        new Piece(false, PieceType.Knight, new Point(1, 0)),
                        new Piece(false, PieceType.Bishop, new Point(2, 0)),
                        new Piece(false, PieceType.Queen, new Point(3, 0)),
                        new Piece(false, PieceType.King, new Point(4, 0)),
                        new Piece(false, PieceType.Bishop, new Point(5, 0)),
                        new Piece(false, PieceType.Knight, new Point(6, 0)),
                        new Piece(false, PieceType.Rook, new Point(7, 0))));
        for (Piece piece : blackPieces)
        {
            board[piece.position.y][piece.position.x] = piece;
        }
        for (Piece piece : whitePieces)
        {
            board[piece.position.y][piece.position.x] = piece;
        }
        // add pawns
        for (int i = 0; i < board.length; i++)
        {
            Piece whitePawn = new Piece(true, PieceType.Pawn, new Point(1, i));
            Piece blackPawn = new Piece(false, PieceType.Pawn, new Point(7, i));
            whitePieces.add(whitePawn);
            whitePieces.add(blackPawn);
            board[1][i] = whitePawn;
            board[7][i] = whitePawn;
        }


    }

    void movePiece(Point start, Point end)
    {
        Piece pieceToMove = board[start.y][start.x];
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
            ArrayList<Move> legalmoves = new ArrayList<>();
            ArrayList<Piece> pieces =
                    whiteToMove ? new ArrayList<>(whitePieces) : new ArrayList<>(blackPieces);
            ArrayList<Point> checkDirections = new ArrayList<>();
            ArrayList<Point> legalSquares = new ArrayList<>();
            // Find all pins, and checks.
            Piece king = getAllPieces(PieceType.King, whiteToMove)[0];
            // diagonals
            Point coordinate;
            boolean isInCheck = false;
            for (int dx = -1; dx < 2; dx++) // iterate over 8 directions
            {
                for (int dy = -1; dy < 2; dy++)
                {
                    if (dy == 0 && dx == 0)
                        break;

                    coordinate = king.position;
                    Piece possiblyPinnedPiece = null;
                    Point direction = new Point(dx, dy);
                    while (isInBounds(coordinate = coordinate.add(direction)))
                    {
                        Piece pieceAtSquare = board[coordinate.y][coordinate.x];
                        if (pieceAtSquare.type != PieceType.None)
                        {
                            if (pieceAtSquare.isWhite != whiteToMove) // opponnent piece
                            {
                                if (!canAttackInDirection(pieceAtSquare, direction))
                                {
                                    break;
                                }
                                if (possiblyPinnedPiece != null)
                                {
                                    System.out.println(pieces.remove(possiblyPinnedPiece));
                                }
                                else
                                {
                                    isInCheck = true;
                                    for (int i = 1; i <= Math.max(
                                            Math.abs(coordinate.x - king.position.x),
                                            Math.abs(coordinate.y - king.position.y)); i++)
                                    {
                                        legalSquares.add(king.position.add(direction.multiply(i)));

                                    }
                                    checkDirections.add(new Point(dx, dy));
                                }
                                break;
                            }
                            else
                            {
                                possiblyPinnedPiece = pieceAtSquare;
                            }
                        }
                    }
                }
            }
            System.out.println(isInCheck);


            // now we know if we are in check, and can generate moves, based on that.
            //Generate kingMoves.

            if(checkDirections.size() == 1)
            {
                //Loop over all pieces.
            }
            if(checkDirections.size() == 2)
            {

            }
        }
        return legalMoves;
    }

    public Piece[] getAllPieces(PieceType type, boolean isWhite)
    {
        ArrayList<Piece> pieces = whiteToMove ? whitePieces : blackPieces;
        ArrayList<Piece> correctPieces = new ArrayList<>();
        for (Piece piece : pieces)
        {
            if (piece.type == type)
            {
                correctPieces.add(piece);
            }
        }
        return correctPieces.toArray(new Piece[correctPieces.size()]);
    }

    private boolean isInBounds(Point coord)
    {
        return coord.x > 0 && coord.y > 0 && coord.x < 8 && coord.y < 8;
    }

    /**
     * Returns whether the given piece can attack in the given direction. a direction could be (1,0)
     * for attacking right to left.
     *
     * @param piece
     * @param direction
     * @return
     */
    private boolean canAttackInDirection(Piece piece, Point direction)
    {
        boolean isDiagonal = direction.x * direction.y == 1;
        return piece.type == PieceType.Queen || isDiagonal && (piece.type == PieceType.Bishop)
                || !isDiagonal && (piece.type == PieceType.Rook);
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
