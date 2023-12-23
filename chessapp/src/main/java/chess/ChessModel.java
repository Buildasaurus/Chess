package chess;

import java.util.ArrayList;
import java.util.Arrays;
import chess.Piece.PieceType;

public class ChessModel
{
    private Move[] legalMoves;
    /**
     * Same objects as in blackPieces and whitePieces
     */
    Piece[][] board;

    public Piece getPieceAtPoint(Point point)
    {
        if (!isInBounds(point))
            return null;
        return board[point.y][point.x];
    }

    boolean canCastle = true;
    boolean whiteToMove = true;
    Piece enPassentablePawn;
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

        blackPieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(false, PieceType.Rook, new Point(0, 7)),
                        new Piece(false, PieceType.Knight, new Point(1, 7)),
                        new Piece(false, PieceType.Bishop, new Point(2, 7)),
                        new Piece(false, PieceType.Queen, new Point(3, 7)),
                        new Piece(false, PieceType.King, new Point(4, 7)),
                        new Piece(false, PieceType.Bishop, new Point(5, 7)),
                        new Piece(false, PieceType.Knight, new Point(6, 7)),
                        new Piece(false, PieceType.Rook, new Point(7, 7))));
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
            Piece whitePawn = new Piece(true, PieceType.Pawn, new Point(i, 1));
            Piece blackPawn = new Piece(false, PieceType.Pawn, new Point(i, 6));
            whitePieces.add(whitePawn);
            blackPieces.add(blackPawn);
            board[1][i] = whitePawn;
            board[6][i] = blackPawn;
        }
        // add nullPieces


    }

    public void movePiece(Point start, Point end)
    {
        Piece pieceToMove = board[start.y][start.x];
        Move move = new Move(start, end);
        if (pieceToMove != null && pieceToMove.isWhite == whiteToMove)
        // legal piece to try to move
        {
            Move[] moves = getLegalMoves();
            if (moves.length == 0)
                System.out.println("No legal moves");
            else
            {
                boolean wasLegalMove = false;
                for (Move legalMove : moves)
                {
                    if (move.equals(legalMove))
                    {
                        enPassentablePawn = null;
                        if (board[end.y][end.x] != null)
                        {
                            if (!whiteToMove)
                            {
                                whitePieces.remove(board[end.y][end.x]);
                            }
                            else
                            {
                                blackPieces.remove(board[end.y][end.x]);
                            }
                        }
                        else if (pieceToMove.type == PieceType.Pawn
                                && move.startSquare.x - move.targetSquare.x != 0)
                        // if is en passent (pawn attacking diagonally but moving to empty field)
                        {
                            if (!whiteToMove)
                            {
                                whitePieces.remove(board[end.y + 1][end.x]);
                                board[end.y + 1][end.x] = null;
                            }
                            else
                            {
                                blackPieces.remove(board[end.y - 1][end.x]);
                                board[end.y - 1][end.x] = null;
                            }

                        }
                        pieceToMove.position = new Point(end.x, end.y);
                        board[start.y][start.x] = null;
                        board[end.y][end.x] = pieceToMove;
                        legalMoves = null;
                        whiteToMove = !whiteToMove;
                        wasLegalMove = true;
                        if (pieceToMove.type == PieceType.Pawn)
                        {
                            if (Math.abs(move.targetSquare.y - move.startSquare.y) == 2)
                            {
                                enPassentablePawn = pieceToMove;
                            }
                        }
                    }
                }
                if (!wasLegalMove)
                {
                    System.out.println("Failed to do move: " + move);
                }
            }
        }
        else
        {
            System.out.println("piece is null");
        }
    }

    public Move[] getLegalMoves()
    {
        if (legalMoves != null)
        {
            return legalMoves;
        }
        else
        {
            ArrayList<Move> legalMovesList = new ArrayList<>();
            ArrayList<Piece> pieces =
                    whiteToMove ? new ArrayList<>(whitePieces) : new ArrayList<>(blackPieces);
            ArrayList<Point> checkDirections = new ArrayList<>();
            ArrayList<Point> legalSquares = new ArrayList<>();
            // Find all pins, and checks.
            Piece king = getAllPieces(PieceType.King, whiteToMove)[0];
            // diagonals
            Point coordinate;
            for (int dx = -1; dx < 2; dx++) // iterate over 8 directions
            {
                for (int dy = -1; dy < 2; dy++)
                {
                    if (dy == 0 && dx == 0)
                        continue;

                    coordinate = king.position;
                    Piece possiblyPinnedPiece = null;
                    Point direction = new Point(dx, dy);
                    while (isInBounds(coordinate = coordinate.add(direction)))
                    {
                        Piece pieceAtSquare = board[coordinate.y][coordinate.x];
                        if (pieceAtSquare != null)
                        {
                            if (pieceAtSquare.isWhite != whiteToMove) // opponnent piece
                            {
                                if (!canAttackInDirection(pieceAtSquare, direction.multiply(-1))
                                        || pieceAtSquare.type == PieceType.Pawn
                                                && king.position.distance(coordinate) > 1)
                                {
                                    break;
                                }
                                if (possiblyPinnedPiece != null)
                                {
                                    System.out.println(pieces.remove(possiblyPinnedPiece));
                                }
                                else // this is a check
                                {
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
                                if (possiblyPinnedPiece != null) // two friendly pieces between king
                                                                 // and anything
                                {
                                    break;
                                }
                                possiblyPinnedPiece = pieceAtSquare;
                            }
                        }
                    }
                }
            }
            // knight checks
            Point[] directions =
            {new Point(2, 1), new Point(2, -1), new Point(1, 2), new Point(1, -2),
                new Point(-1, -2), new Point(-1, 2), new Point(-2, 1), new Point(-2, -1)};
            for (Point direction : directions)
            {
                Point possibleKnight = king.position.add(direction);
                if (isInBounds(possibleKnight))
                {
                    Piece pieceAtSquare = board[possibleKnight.y][possibleKnight.x];
                    if (pieceAtSquare != null && pieceAtSquare.isWhite != whiteToMove
                            && pieceAtSquare.type == PieceType.Knight)
                    {
                        checkDirections.add(possibleKnight);
                        legalSquares.add(possibleKnight);
                    }
                }
            }



            // generate other moves
            if (checkDirections.size() == 0)
            {
                // all possible moves, are legal (so you don't have to block a check)
                legalMovesList.addAll(generateAllMoves(pieces));
            }
            else if (checkDirections.size() == 1)
            {
                // Loop over all pieces.
                ArrayList<Move> allMoves = generateAllMoves(pieces);
                // generate legal squares, that moves must end on (in front of king, or taking the
                // chekcing piece)
                for (Move move : allMoves)
                {
                    if (getPieceAtPoint(move.startSquare).type == PieceType.King)
                    {
                        legalMovesList.add(move);
                        continue;
                    }
                    for (Point legalSquare : legalSquares)
                    {
                        if (move.targetSquare.equals(legalSquare))
                        {
                            legalMovesList.add(move);
                            break;
                        }
                    }
                }
            }
            else
            {
                // Loop over all pieces.
                ArrayList<Move> allMoves = generateAllMoves(pieces);
                // generate legal squares, that moves must end on (in front of king, or taking the
                // chekcing piece)
                for (Move move : allMoves)
                {
                    if (getPieceAtPoint(move.startSquare).type == PieceType.King)
                    {
                        legalMovesList.add(move);
                    }
                }
            }
            // if checkdirections is more than 1, then the kingmoves are the only legal ones, and
            // those are already generated.
            legalMoves = legalMovesList.toArray(new Move[legalMovesList.size()]);
        }
        return legalMoves;
    }

    /**
     * Generates all moves that pieces can make, regardless of whether king is in check, or if
     * pieces are pinned.
     *
     * @param pieces
     * @return
     */
    private ArrayList<Move> generateAllMoves(ArrayList<Piece> pieces)
    {
        Piece king = getAllPieces(PieceType.King, whiteToMove)[0];

        ArrayList<Move> legalMovesList = new ArrayList<Move>();

        // Generate kingMoves.
        for (int dx = -1; dx < 2; dx++) // iterate over 8 directions
        {
            for (int dy = -1; dy < 2; dy++)
            {
                if (dy == 0 && dx == 0)
                    continue;
                Point position = king.position.add(new Point(dx, dy));
                if (!squareIsAttacked(position) && isInBounds(position)
                        && (board[position.y][position.x] == null
                                || board[position.y][position.x].isWhite != whiteToMove))
                {
                    legalMovesList.add(new Move(king.position, position));
                }
            }
        }
        // Castling
        if (canCastle)
        {

        }
        // Other moves
        for (Piece piece : pieces)
        {
            if (piece.isWhite != whiteToMove)
                continue;
            switch (piece.type)
            {
                case King: // already generated
                    break;
                case Pawn:
                    int upDirection = whiteToMove ? 1 : -1;
                    Point[] attackSquares =
                    {piece.position.add(new Point(-1, upDirection)),
                        piece.position.add(new Point(1, upDirection))};
                    for (Point point : attackSquares)
                    {
                        Piece pieceAtPoint = getPieceAtPoint(point);
                        if (pieceAtPoint != null && isInBounds(point)
                                && pieceAtPoint.isWhite != piece.isWhite)
                        {
                            Move move = new Move(piece.position, point);
                            move.isCapture = true;
                            if (point.y == 7 || point.y == 0)
                                move.isPromotion = true;
                            legalMovesList.add(move);
                        }
                    }
                    // nonattacking moves.
                    int movecount = (piece.position.y == 1 || piece.position.y == 6) ? 2 : 1;
                    for (int dy = upDirection; Math.abs(dy) <= movecount; dy += upDirection)
                    {
                        Point point = new Point(piece.position.x, piece.position.y + dy);
                        Piece pieceAtPoint = getPieceAtPoint(point);
                        if (pieceAtPoint == null && isInBounds(point))
                        {
                            Move move = new Move(piece.position, point);
                            move.isCapture = false;
                            if (point.y == 7 || point.y == 0)
                                move.isPromotion = true;
                            legalMovesList.add(move);
                        }
                        else
                        {
                            break;
                        }
                    }
                    break;
                case Bishop:
                    legalMovesList.addAll(generateDiagonalMoves(piece.position));
                    break;
                case Knight:
                    legalMovesList.addAll(generateKnightMoves(piece.position));
                    break;
                case Rook:
                    legalMovesList.addAll(generateStraightMoves(piece.position));
                    break;
                case Queen:
                    legalMovesList.addAll(generateStraightMoves(piece.position));
                    legalMovesList.addAll(generateDiagonalMoves(piece.position));
                    break;
                default:
                    System.out.println("This piece is no piece??");
                    break;
            }
        }


        // generate en passent
        if (enPassentablePawn != null)
        {
            Piece first = getPieceAtPoint(enPassentablePawn.position.add(1, 0));
            Piece second = getPieceAtPoint(enPassentablePawn.position.add(-1, 0));
            int dir = whiteToMove ? 1 : -1;
            if (first != null && isInBounds(first.position) && first.type == PieceType.Pawn)
            {
                legalMovesList
                        .add(new Move(first.position, enPassentablePawn.position.add(0, dir)));
            }
            if (second != null && isInBounds(second.position) && second.type == PieceType.Pawn)
            {
                legalMovesList
                        .add(new Move(second.position, enPassentablePawn.position.add(0, dir)));
            }

        }
        // generate castling

        return legalMovesList;
    }

    /**
     * Returns a boolean represeting if a square is attacked by enemy pieces, or protected by enemy
     * pieces.
     *
     * @param square The square the check
     * @return Whether any enemy pieces see the square.
     */
    public boolean squareIsAttacked(Point square)
    {
        for (int dx = -1; dx < 2; dx++) // iterate over 8 directions
        {
            for (int dy = -1; dy < 2; dy++)
            {
                if (dy == 0 && dx == 0)
                    continue;

                Point coordinate = square;
                Point direction = new Point(dx, dy);
                while (isInBounds(coordinate = coordinate.add(direction)))
                {
                    Piece pieceAtSquare = board[coordinate.y][coordinate.x];
                    if (pieceAtSquare != null)
                    {
                        if (pieceAtSquare.isWhite != whiteToMove) // opponnent piece
                        {
                            if (!canAttackInDirection(pieceAtSquare, direction.multiply(-1))
                                    || pieceAtSquare.type == PieceType.Pawn
                                            && coordinate.distance(square) > 1)
                            {
                                break;
                            }
                            else
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        Point[] directions =
        {new Point(2, 1), new Point(2, -1), new Point(1, 2), new Point(1, -2), new Point(-1, -2),
            new Point(-1, 2), new Point(-2, 1), new Point(-2, -1)};
        for (Point direction : directions)
        {
            Point coordinate = square.add(direction);
            if (isInBounds(coordinate))
            {
                Piece pieceAtSquare = board[coordinate.y][coordinate.x];
                if (pieceAtSquare != null && pieceAtSquare.isWhite != whiteToMove
                        && pieceAtSquare.type == PieceType.Knight)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * returns all the pieces of a specific type.
     *
     * @param type
     * @param isWhite
     * @return
     */
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
        return coord.x >= 0 && coord.y >= 0 && coord.x < 8 && coord.y < 8;
    }

    public ArrayList<Move> generateKnightMoves(Point startSquare)
    {
        ArrayList<Move> moves = new ArrayList<>();
        Point[] directions =
        {new Point(2, 1), new Point(2, -1), new Point(1, 2), new Point(1, -2), new Point(-1, -2),
            new Point(-1, 2), new Point(-2, 1), new Point(-2, -1)};
        for (Point direction : directions)
        {
            Point coordinate = startSquare.add(direction);
            if (isInBounds(coordinate))
            {
                Piece pieceAtSquare = board[coordinate.y][coordinate.x];
                if (pieceAtSquare == null || pieceAtSquare.isWhite != whiteToMove)
                {
                    moves.add(new Move(startSquare, coordinate));
                }
            }

        }
        return moves;
    }

    public ArrayList<Move> generateDiagonalMoves(Point startSquare)
    {
        Point[] directions =
        {new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1)};
        return generateMovesInDirection(startSquare, directions);

    }

    public ArrayList<Move> generateStraightMoves(Point startSquare)
    {
        Point[] directions =
        {new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1)};
        return generateMovesInDirection(startSquare, directions);
    }

    private ArrayList<Move> generateMovesInDirection(Point startSquare, Point[] directions)
    {
        ArrayList<Move> moves = new ArrayList<>();
        Point coordinate;
        for (Point direction : directions)
        {
            coordinate = startSquare;

            while (isInBounds(coordinate = coordinate.add(direction)))
            {
                Piece pieceAtSquare = board[coordinate.y][coordinate.x];
                if (pieceAtSquare != null)
                {
                    if (pieceAtSquare.isWhite != whiteToMove)
                    {
                        moves.add(new Move(startSquare, coordinate));
                    }
                    break;
                }
                else
                {
                    moves.add(new Move(startSquare, coordinate));
                }
            }
        }
        return moves;

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
        boolean isDiagonal = Math.abs(direction.x * direction.y) == 1;
        return piece.type == PieceType.Queen
                || isDiagonal && (piece.type == PieceType.Bishop
                        || (piece.type == PieceType.Pawn && (direction.y > 0 == piece.isWhite)))
                || !isDiagonal && (piece.type == PieceType.Rook);
    }

    boolean isCheckmate()
    {
        return getLegalMoves().length == 0;
    }

    boolean isDraw()
    {
        return false;
    }
}
