package chess.Models;

import java.util.ArrayList;
import chess.Models.Piece.PieceType;

public class FenReader
{
    public static Board loadFenString(String fenString)
    {
        // Fen strings have 6 components, seperated by white spaces
        Board boardBoard = new Board();
        String[] components = fenString.split(" ");
        Piece[][] board = new Piece[8][8];
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();

        // First component is piece placement - Starts from eigth rank, first file. A8.
        // p is pawn, r is rook, n is knight, b is bishop, q is queen, k is king.
        // White pieces are upper case. Empty squares are denoted with numbers, that
        // shows how many empty squares are to come.

        // first, split op the first component by slashes, representing different ranks/rows
        String[] rows = components[0].split("/");
        int rowIndex = 7;
        boolean whiteKingRookHasMoved = !components[2].contains("K");
        boolean whiteQueenRookHasMoved = !components[2].contains("Q");
        boolean blackKingRookHasMoved = !components[2].contains("k");
        boolean blackQueenRookHasMoved = !components[2].contains("q");
        // During piece placement, third component will also be handled, the castling rights.
        for (String row : rows)
        {
            int columnIndex = 0;
            for (char character : row.toCharArray())
            {
                if (Character.isDigit(character))
                {
                    columnIndex += Character.getNumericValue(character);
                }
                else
                {
                    boolean isWhitePiece = Character.isUpperCase(character);
                    ArrayList<Piece> pieces = isWhitePiece ? whitePieces : blackPieces;
                    switch (Character.toLowerCase(character))
                    {
                        case 'r':
                            Piece rook = new Piece(isWhitePiece, PieceType.Rook,
                                    new Point(columnIndex, rowIndex));
                            if(columnIndex != 0 || columnIndex != 7 || rowIndex != 0 ||rowIndex != 7)
                            {
                                rook.hasMoved = true;
                            }
                            if (!isWhitePiece)
                            {
                                if (columnIndex == 0)
                                {
                                    rook.hasMoved = blackQueenRookHasMoved;
                                }
                                if (columnIndex == 7)
                                {
                                    rook.hasMoved = blackKingRookHasMoved;
                                }
                            }
                            else
                            {
                                if (columnIndex == 0)
                                {
                                    rook.hasMoved = whiteQueenRookHasMoved;
                                }
                                if (columnIndex == 7)
                                {
                                    rook.hasMoved = whiteKingRookHasMoved;
                                }
                            }
                            pieces.add(rook);
                            break;
                        case 'q':
                            pieces.add(new Piece(isWhitePiece, PieceType.Queen,
                                    new Point(columnIndex, rowIndex)));

                            break;

                        case 'p':
                            Piece pawn = new Piece(isWhitePiece, PieceType.Pawn,
                                    new Point(columnIndex, rowIndex));
                            int startRank = isWhitePiece ? 1 : 6;
                            if (startRank != rowIndex)
                                pawn.hasMoved = true;
                            pieces.add(pawn);
                            break;

                        case 'k': // not caring whether king has moved, as there is enough
                                  // information for castling rights, in whether the rooks have
                                  // moved. It defaults to false, which is what it must.
                            pieces.add(new Piece(isWhitePiece, PieceType.King,
                                    new Point(columnIndex, rowIndex)));
                            break;

                        case 'b':
                            pieces.add(new Piece(isWhitePiece, PieceType.Bishop,
                                    new Point(columnIndex, rowIndex)));
                            break;

                        case 'n':
                            pieces.add(new Piece(isWhitePiece, PieceType.Knight,
                                    new Point(columnIndex, rowIndex)));
                            break;
                        default:
                            break;
                    }
                    columnIndex++;
                }
            }
            rowIndex--;
        }

        for (Piece piece : whitePieces)
        {
            board[piece.position.y][piece.position.x] = piece;
        }
        for (Piece piece : blackPieces)
        {
            board[piece.position.y][piece.position.x] = piece;
        }
        // second part is active color
        boardBoard.whiteToMove = components[1].equals("w");


        // En passent
        if (!components[3].equals("-"))
        {
            Point point = notationToPoint(components[3]);
            // because notation gives the actual square you can enpassent to, we need to convert
            // this to where the pawn then must be.
            int yPoint = boardBoard.whiteToMove ? point.y + 1 : point.y - 1;
            boardBoard.enPassentablePawn = board[yPoint][point.x];
        }

        boardBoard.board = board;
        boardBoard.whitePieces = whitePieces;
        boardBoard.blackPieces = blackPieces;
        return boardBoard;

        // halfmove - for how long we are on to 50-move rule TODO implement this
        // TODO implement fullmove, which is total number of moves.
    }

    public static void saveToFen(Board board)
    {

    }

    private static Point notationToPoint(String string)
    {
        // Convert the first character to a number from 0 to 7
        int x = string.charAt(0) - 'a';
        // Convert the second character to a number from 0 to 7
        int y = '8' - string.charAt(1);
        // Return a new Point
        return new Point(x, y);
    }

}
