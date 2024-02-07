package chess.Models.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import chess.Models.ZobristHasher;
import chess.Models.Board.Piece.PieceType;
import chess.Utils.BoardHelper;

public class MagicBoard
{
    long allPiecesBitBoard;
    PieceList[] sidePiecesBitBoard;
    PieceList[] kingBitBoards;
    PieceList[] queenBitBoards;
    PieceList[] rookBitBoards;
    PieceList[] bishopBitBoards;
    PieceList[] knightBitBoards;
    PieceList[] pawnBitBoards;
    /**
     * Stored as zobrist hashes.
     */
    public ArrayList<Long> previousPositions = new ArrayList<>();
    private long cachedHash = 0;
    private boolean hashIsCached = false;
    public long legalMovesTime = 0;
    public long makeMoveTime = 0;
    public long undoTime = 0;
    public int halfPlyCount = 0;
    public int fullPlyCount = 0;
    public ArrayList<Move> playedMoves = new ArrayList<Move>();
    private Move[] legalMoves;
    /**
     * Same objects as in blackPieces and whitePieces
     */
    public Piece[][] board;

    private Boolean isInCheck;

    public boolean whiteToMove = true;
    public Piece enPassentablePawn;
    /**
     * Same objects as in board
     */
    public ArrayList<Piece> whitePieces;
    /**
     * Same objects as in board
     */
    public ArrayList<Piece> blackPieces;

    /**
     * Instantiates a chess board, with pieces on the standard squares.
     */
    public MagicBoard()
    {
        board = new Piece[8][8];
        // 0 for white, 1 for black
        sidePiecesBitBoard = new PieceList[]
        {new PieceList(16), new PieceList(16)};
        kingBitBoards = new PieceList[]
        {new PieceList(1), new PieceList(1)};
        queenBitBoards = new PieceList[]
        {new PieceList(9), new PieceList(9)};
        rookBitBoards = new PieceList[]
        {new PieceList(10), new PieceList(10)};
        bishopBitBoards = new PieceList[]
        {new PieceList(10), new PieceList(10)};
        knightBitBoards = new PieceList[]
        {new PieceList(10), new PieceList(10)};
        pawnBitBoards = new PieceList[]
        {new PieceList(8), new PieceList(8)};

        for (int i = 0; i < 16; i++)
        {
            sidePiecesBitBoard[0].AddPieceAtSquare(i);
            sidePiecesBitBoard[1].AddPieceAtSquare(i + 48);
        }
        kingBitBoards[0].AddPieceAtSquare(4);
        kingBitBoards[1].AddPieceAtSquare(60);

        queenBitBoards[0].AddPieceAtSquare(3);
        queenBitBoards[1].AddPieceAtSquare(59);

        rookBitBoards[0].AddPieceAtSquare(0);
        rookBitBoards[0].AddPieceAtSquare(7);
        rookBitBoards[1].AddPieceAtSquare(56);
        rookBitBoards[1].AddPieceAtSquare(63);

        bishopBitBoards[0].AddPieceAtSquare(2);
        bishopBitBoards[0].AddPieceAtSquare(5);
        bishopBitBoards[1].AddPieceAtSquare(58);
        bishopBitBoards[1].AddPieceAtSquare(61);

        knightBitBoards[0].AddPieceAtSquare(1);
        knightBitBoards[0].AddPieceAtSquare(6);
        knightBitBoards[1].AddPieceAtSquare(57);
        knightBitBoards[1].AddPieceAtSquare(62);

        for (int i = 0; i < 8; i++)
        {
            pawnBitBoards[0].AddPieceAtSquare(i+8);
            pawnBitBoards[1].AddPieceAtSquare(i+48);
        }

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
    }

    /**
     * Attempts to move a piece from the start point to the end point.
     *
     * @param start
     * @param end
     */
    public boolean tryToMakeMove(String uciMove)
    {
        Point start = BoardHelper.squareIndexFromName(uciMove.charAt(0) + "" + uciMove.charAt(1));
        Point end = BoardHelper.squareIndexFromName(uciMove.charAt(2) + "" + uciMove.charAt(3));
        Move move = new Move(start, end, halfPlyCount);

        char promoteChar = uciMove.length() > 3 ? uciMove.charAt(uciMove.length() - 1) : ' ';

        PieceType promotePieceType;
        switch (promoteChar)
        {
            case 'q':
                promotePieceType = PieceType.Queen;
                break;
            case 'r':
                promotePieceType = PieceType.Rook;
                break;
            case 'n':
                promotePieceType = PieceType.Knight;
                break;
            case 'b':
                promotePieceType = PieceType.Bishop;
                break;
            default:
                promotePieceType = PieceType.Queen;
                break;
        }

        move.promotionType = promotePieceType;
        boolean succesful = tryToMakeMove(move);
        return succesful;
    }

    /**
     * Attempts to move a piece from the start point to the end point.
     *
     * @param start
     * @param end
     */
    public boolean tryToMakeMove(Point start, Point end)
    {
        Move move = new Move(start, end, halfPlyCount);
        boolean succesful = tryToMakeMove(move);
        return succesful;

    }

    /**
     * Attempts to move a piece from the given startpoint, to the endpoint. If it isn't possible, it
     * does nothing and returns false. Will generate all legal moves.
     *
     * @param start The start square that the piece moves from
     * @param end The square that the piece wants to move to
     */
    public boolean tryToMakeMove(Move move)
    {
        Point start = move.startSquare;
        Piece pieceToMove = board[start.y][start.x];
        if (pieceToMove != null && pieceToMove.isWhite == whiteToMove)
        // legal piece to try to move
        {
            Move[] moves = getLegalMoves();
            if (isDraw())
                System.out.println("The game is draw");
            else if (moves.length == 0)
                System.out.println("No legal moves");
            else
            {
                for (Move legalMove : moves)
                {
                    if (move.equals(legalMove))
                    {
                        makeMove(legalMove);
                        return true;
                    }
                }
                System.out.println("The following move isn't legal: " + move);
            }
        }
        else
        {
            System.out.println("piece is null, or not of the right color.");
        }
        return false;
    }

    /**
     * Makes the given move. Assumes it is legal, and just bruteforces it through.
     *
     * @param move The given legal move.
     */
    public void makeMove(Move move)
    {
        long starttime = System.nanoTime();
        Point start = move.startSquare;
        Point end = move.targetSquare;
        Piece pieceToMove = board[start.y][start.x];

        // update the piece lists if there are captures
        enPassentablePawn = null;
        if (board[end.y][end.x] != null)
        {
            halfPlyCount = -1; // reset halfmove on capture
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

        // update the old pieces and vacate the square the piece came from.
        pieceToMove.position = new Point(end.x, end.y);
        board[start.y][start.x] = null;
        board[end.y][end.x] = pieceToMove;
        pieceToMove.hasMoved = true;


        if (move.isPromotion)
        {
            Piece newPiece = new Piece(whiteToMove, move.promotionType, end);
            newPiece.hasMoved = true;
            if (whiteToMove)
            {
                whitePieces.remove(pieceToMove);
                whitePieces.add(newPiece);
            }
            else
            {
                blackPieces.remove(pieceToMove);
                blackPieces.add(newPiece);
            }
            setPieceAtPoint(newPiece, end);
        }
        if (move.isCastling)
        {
            if (move.targetSquare.x > 4) // kingside castling
            {
                Piece rookToMove = board[move.targetSquare.y][7];
                rookToMove.hasMoved = true;

                rookToMove.position = new Point(5, move.targetSquare.y);
                setPieceAtPoint(rookToMove, rookToMove.position);
                setPieceAtPoint(null, new Point(7, move.targetSquare.y));

            }
            else// queenside
            {
                Piece rookToMove = board[move.targetSquare.y][0];
                rookToMove.hasMoved = true;
                rookToMove.position = new Point(3, move.targetSquare.y);
                setPieceAtPoint(rookToMove, rookToMove.position);
                setPieceAtPoint(null, new Point(0, move.targetSquare.y));
            }
        }

        // for future moves, save that that if move was a pawn pushed two squares,
        // it may be en-passented
        if (pieceToMove.type == PieceType.Pawn)
        {
            halfPlyCount = -1;
            if (Math.abs(move.targetSquare.y - move.startSquare.y) == 2)
            {
                enPassentablePawn = pieceToMove;
            }
        }
        legalMoves = null;
        whiteToMove = !whiteToMove;
        isInCheck = null;
        playedMoves.add(move);
        previousPositions.add(getZobristHash());
        makeMoveTime += System.nanoTime() - starttime;
        fullPlyCount += 1;
        halfPlyCount += 1;
        hashIsCached = false;
    }



    /**
     * Undos the given move, assuming it is legal.
     *
     * @param move The move to undo
     */
    public void undoMove(Move move)
    {
        long startTime = System.nanoTime();

        ArrayList<Piece> opponnentPieces = whiteToMove ? whitePieces : blackPieces;
        ArrayList<Piece> ownPieces = whiteToMove ? blackPieces : whitePieces;

        Piece pieceToMove;
        if (move.isPromotion)
        {
            pieceToMove = new Piece(!whiteToMove, PieceType.Pawn, move.startSquare);
            pieceToMove.hasMoved = true;
            ownPieces.add(pieceToMove);
            ownPieces.remove(getPieceAtPoint(move.targetSquare));
        }
        else
        {
            pieceToMove = getPieceAtPoint(move.targetSquare);
        }

        pieceToMove.position = move.startSquare;
        pieceToMove.hasMoved = !move.getFirstMove();
        setPieceAtPoint(pieceToMove, move.startSquare);

        if (move.isEnPassent)
        {
            Piece pawn = new Piece(whiteToMove, PieceType.Pawn,
                    move.targetSquare.add(0, whiteToMove ? 1 : -1));
            pawn.hasMoved = true;
            setPieceAtPoint(pawn, pawn.position);
            setPieceAtPoint(null, move.targetSquare);
            opponnentPieces.add(pawn);

        }
        else if (move.isCapture)
        {

            enPassentablePawn = null;
            Piece piece = new Piece(whiteToMove, move.getCapturePieceType(), move.targetSquare);
            piece.hasMoved = move.capturedPieceHadMoved;
            opponnentPieces.add(piece);
            setPieceAtPoint(piece, piece.position);

        }
        else
        {
            enPassentablePawn = null;
            setPieceAtPoint(null, move.targetSquare);
        }

        if (move.isCastling)
        {
            int y = whiteToMove ? 7 : 0;
            // move the rook
            if (move.targetSquare.x < 4) // if queenside castling
            {
                Point rookPoint = new Point(3, y);
                Piece rook = getPieceAtPoint(rookPoint);
                rook.hasMoved = false;
                rook.position = new Point(0, y);
                setPieceAtPoint(rook, rook.position);
                setPieceAtPoint(null, rookPoint);
            }
            else
            {
                Point rookPoint = new Point(5, y);
                Piece rook = getPieceAtPoint(rookPoint);
                rook.hasMoved = false;
                rook.position = new Point(7, y);
                setPieceAtPoint(rook, rook.position);
                setPieceAtPoint(null, rookPoint);
            }
        }
        whiteToMove = !whiteToMove;
        legalMoves = null;
        isInCheck = null;
        if (previousPositions.size() >= 1)
        {
            previousPositions.remove(previousPositions.size() - 1);
        }
        if (playedMoves.size() >= 1)
        {
            playedMoves.remove(playedMoves.size() - 1);
            if (playedMoves.size() >= 1)
            {
                // update enpassentable pawn, based on if the move before this we just undid, was a
                // pawnmove.
                // IMPORTANT that this is after having removed the newest move.
                Move previousMove = playedMoves.get(playedMoves.size() - 1);
                if (previousMove.getFirstMove()
                        && getPieceAtPoint(previousMove.targetSquare).type == PieceType.Pawn
                        && previousMove.targetSquare.y - previousMove.startSquare.y == 2)
                {
                    enPassentablePawn = getPieceAtPoint(previousMove.targetSquare);
                }
            }
        }
        halfPlyCount = move.previousHalfPlyCount;
        fullPlyCount -= 1;
        undoTime += System.nanoTime() - startTime;
        hashIsCached = false;
    }

    /**
     * Finds all the legal moves in the current position
     *
     * @return A Move array of the legal moves of the current board state.
     */
    public Move[] getLegalMoves()
    {
        long start = System.nanoTime();
        if (legalMoves != null)
        {
            return legalMoves;
        }
        else
        {
            ArrayList<Move> legalMovesList = new ArrayList<>();
            ArrayList<Piece> pieces =
                    whiteToMove ? new ArrayList<>(whitePieces) : new ArrayList<>(blackPieces);
            ArrayList<Piece> pinnedPieces = new ArrayList<>();
            ArrayList<Point> pinnedDirections = new ArrayList<>();

            ArrayList<Point> checkDirections = new ArrayList<>();
            ArrayList<Point> legalSquares = new ArrayList<>();
            Piece king = null;
            king = getAllPieces(PieceType.King, whiteToMove)[0];


            // Find all pins, and checks.
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
                                boolean dangerousPiece =
                                        pieceIsDangerous(pieceAtSquare, direction, king.position);
                                if (dangerousPiece && possiblyPinnedPiece == null) // in check
                                {
                                    for (int i = 1; i <= Math.max(
                                            Math.abs(coordinate.x - king.position.x),
                                            Math.abs(coordinate.y - king.position.y)); i++)
                                    {
                                        legalSquares.add(king.position.add(direction.multiply(i)));
                                    }
                                    checkDirections.add(new Point(dx, dy));
                                    isInCheck = true;
                                }
                                else if (dangerousPiece && possiblyPinnedPiece != null)
                                {
                                    pieces.remove(possiblyPinnedPiece);
                                    pinnedPieces.add(possiblyPinnedPiece);
                                    pinnedDirections.add(direction);
                                }
                                // if it is not dangerous, it's just a nonimportant enemy piece
                                // shielding us.
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
                        isInCheck = true;
                        checkDirections.add(possibleKnight);
                        legalSquares.add(possibleKnight);
                    }
                }
            }
            if (isInCheck == null)
            {
                isInCheck = false;
            }

            if (checkDirections.size() == 0 || checkDirections.size() == 1)
            {
                for (int i = 0; i < pinnedPieces.size(); i++) // might be more pinned pieces
                {
                    ArrayList<Move> pinnedMoves = generateAllMoves(pinnedPieces.get(i));
                    for (Move move : pinnedMoves)
                    {
                        if (move.targetSquare.subtract(move.startSquare)
                                .isParallel(pinnedDirections.get(i)))
                        {
                            if (checkDirections.size() == 1
                                    && legalSquares.contains(move.targetSquare))
                            {
                                legalMovesList.add(move);
                            }
                            else if (checkDirections.size() == 0)
                            {
                                legalMovesList.add(move);
                            }
                        }
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
                    if (move.isEnPassent) // en passent is always generated in a legal way.
                    {
                        legalMovesList.add(move);
                        break;
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
                legalMovesList.addAll(generateAllMoves(new ArrayList<>(Arrays.asList(king))));

            }
            // if checkdirections is more than 1, then the kingmoves are the only legal ones, and
            // those are already generated.
            legalMoves = legalMovesList.toArray(new Move[legalMovesList.size()]);
        }
        legalMovesTime += System.nanoTime() - start;
        return legalMoves;
    }

    private ArrayList<Move> generateAllMoves(Piece piece)
    {
        return generateAllMoves(new ArrayList<>(Collections.singletonList(piece)));
    }

    /**
     * Generates all moves that pieces can make, regardless of whether king is in check, or if
     * pieces are pinned. Except for en passent, it only generates legal moves. (it checks if the
     * king is in check.)
     *
     * @param pieces
     * @return
     */
    private ArrayList<Move> generateAllMoves(ArrayList<Piece> pieces)
    {
        Piece king = getAllPieces(PieceType.King, whiteToMove)[0];

        ArrayList<Move> legalMovesList = new ArrayList<Move>();


        // Other moves
        for (Piece piece : pieces)
        {
            if (piece.isWhite != whiteToMove)
                continue;
            switch (piece.type)
            {
                case King:
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
                                Move kingMove = new Move(king.position, position, halfPlyCount);
                                kingMove.setFirstMove(!king.hasMoved);
                                if (board[position.y][position.x] != null)
                                {
                                    kingMove.isCapture = true;
                                    kingMove.capturedPieceHadMoved =
                                            getPieceAtPoint(position).hasMoved;
                                    kingMove.setCapturePieceType(getPieceAtPoint(position).type);
                                }
                                legalMovesList.add(kingMove);
                            }
                        }
                    }
                    // Castling
                    if (!king.hasMoved && !squareIsAttacked(king.position))
                    {
                        int yPosition = whiteToMove ? 0 : 7;
                        // kingside
                        if (canCastle(true, whiteToMove))
                        {
                            Move kingsideCastling = new Move(new Point(4, yPosition),
                                    new Point(6, yPosition), halfPlyCount);
                            kingsideCastling.isCastling = true;
                            kingsideCastling.setFirstMove(true);
                            legalMovesList.add(kingsideCastling);
                        }
                        if (canCastle(false, whiteToMove))
                        {
                            Move queensideCastling = new Move(new Point(4, yPosition),
                                    new Point(2, yPosition), halfPlyCount);
                            queensideCastling.isCastling = true;
                            queensideCastling.setFirstMove(true);
                            legalMovesList.add(queensideCastling);
                        }
                    }
                    break;
                case Pawn:

                    // Attacking moves
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
                            Move move = new Move(piece.position, point, halfPlyCount);
                            move.setFirstMove(!piece.hasMoved);
                            move.setCapturePieceType(getPieceAtPoint(pieceAtPoint.position).type);
                            move.capturedPieceHadMoved =
                                    getPieceAtPoint(pieceAtPoint.position).hasMoved;
                            if (point.y == 7 || point.y == 0)
                            {
                                move.isPromotion = true;
                                move.setFirstMove(false);
                                Move otherPromotion = move.copy();
                                otherPromotion.promotionType = PieceType.Knight;
                                legalMovesList.add(otherPromotion);
                                Move otherPromotion2 = move.copy();
                                otherPromotion2.promotionType = PieceType.Bishop;
                                legalMovesList.add(otherPromotion2);
                                Move otherPromotion3 = move.copy();
                                otherPromotion3.promotionType = PieceType.Queen;
                                legalMovesList.add(otherPromotion3);
                                Move otherPromotion4 = move.copy();
                                otherPromotion4.promotionType = PieceType.Rook;
                                legalMovesList.add(otherPromotion4);
                            }
                            else
                            {
                                legalMovesList.add(move);
                            }
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
                            Move move = new Move(piece.position, point, halfPlyCount);
                            move.isCapture = false;
                            move.setFirstMove(!piece.hasMoved);
                            if (point.y == 7 || point.y == 0)
                            {
                                move.isPromotion = true;
                                move.setFirstMove(false);
                                Move otherPromotion = move.copy();
                                otherPromotion.promotionType = PieceType.Knight;
                                legalMovesList.add(otherPromotion);
                                Move otherPromotion2 = move.copy();
                                otherPromotion2.promotionType = PieceType.Bishop;
                                legalMovesList.add(otherPromotion2);
                                Move otherPromotion3 = move.copy();
                                otherPromotion3.promotionType = PieceType.Queen;
                                legalMovesList.add(otherPromotion3);
                                Move otherPromotion4 = move.copy();
                                otherPromotion4.promotionType = PieceType.Rook;
                                legalMovesList.add(otherPromotion4);
                            }
                            else
                            {
                                legalMovesList.add(move);
                            }
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
            if (first != null && isInBounds(first.position) && first.type == PieceType.Pawn
                    && first.isWhite == whiteToMove && pieces.contains(first))
            {
                Move enpassent = new Move(first.position, enPassentablePawn.position.add(0, dir),
                        halfPlyCount);
                enpassent.setFirstMove(false);
                enpassent.isEnPassent = true;
                enpassent.setCapturePieceType(PieceType.Pawn);
                enpassent.capturedPieceHadMoved = first.hasMoved;
                enpassent.isCapture = true;
                setPieceAtPoint(null, first.position);
                setPieceAtPoint(null, enPassentablePawn.position);
                setPieceAtPoint(first, enpassent.targetSquare);
                if (!squareIsAttacked(king.position))
                {
                    legalMovesList.add(enpassent);
                }
                setPieceAtPoint(first, first.position);
                setPieceAtPoint(enPassentablePawn, enPassentablePawn.position);
                setPieceAtPoint(null, enpassent.targetSquare);
            }
            if (second != null && isInBounds(second.position) && second.type == PieceType.Pawn
                    && second.isWhite == whiteToMove && pieces.contains(second))
            {
                Move enpassent = new Move(second.position, enPassentablePawn.position.add(0, dir),
                        halfPlyCount);
                enpassent.isEnPassent = true;
                enpassent.setFirstMove(false);
                enpassent.setCapturePieceType(PieceType.Pawn);
                enpassent.capturedPieceHadMoved = second.hasMoved;

                enpassent.isCapture = true;
                setPieceAtPoint(null, second.position);
                setPieceAtPoint(null, enPassentablePawn.position);
                setPieceAtPoint(second, enpassent.targetSquare);
                if (!squareIsAttacked(king.position))
                {
                    legalMovesList.add(enpassent);
                }
                setPieceAtPoint(second, second.position);
                setPieceAtPoint(enPassentablePawn, enPassentablePawn.position);
                setPieceAtPoint(null, enpassent.targetSquare);
            }
        }
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
                            if (pieceIsDangerous(pieceAtSquare, direction, square))
                            {
                                return true;
                            }
                            else
                            {
                                break;
                            }

                        }
                        else if (pieceAtSquare.type != PieceType.King)
                        {
                            break;
                        }
                    }
                }
            }
        }

        // Check for knights attacking
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
     *
     * @param enemyPiece
     * @param direction
     * @param friendlyPiece
     * @return Returns a boolean representing whether the friendly piece is threatened, by the enemy
     *         piece. Possibly through other pieces.
     */
    boolean pieceIsDangerous(Piece enemyPiece, Point direction, Point friendlyPiece)
    {
        return canAttackInDirection(enemyPiece, direction.multiply(-1))
                && !((enemyPiece.type == PieceType.Pawn || enemyPiece.type == PieceType.King)
                        && friendlyPiece.distance(enemyPiece.position) > 1);
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

    /**
     * returns the black and white list of pieces
     *
     * @return
     */
    public ArrayList<ArrayList<Piece>> getAllPieceLists()
    {
        ArrayList<ArrayList<Piece>> pieces = new ArrayList<ArrayList<Piece>>();
        pieces.add(blackPieces);
        pieces.add(whitePieces);
        return pieces;

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
                    Move move = new Move(startSquare, coordinate, halfPlyCount);
                    move.setFirstMove(!getPieceAtPoint(startSquare).hasMoved);

                    if (pieceAtSquare != null)
                    {
                        move.isCapture = true;
                        move.capturedPieceHadMoved =
                                getPieceAtPoint(pieceAtSquare.position).hasMoved;
                        move.setCapturePieceType(getPieceAtPoint(pieceAtSquare.position).type);
                    }
                    moves.add(move);
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
                        Move move = new Move(startSquare, coordinate, halfPlyCount);
                        move.setFirstMove(!getPieceAtPoint(startSquare).hasMoved);
                        move.capturedPieceHadMoved =
                                getPieceAtPoint(pieceAtSquare.position).hasMoved;
                        move.setCapturePieceType(getPieceAtPoint(pieceAtSquare.position).type);
                        moves.add(move);
                    }
                    break;
                }
                else
                {
                    Move move = new Move(startSquare, coordinate, halfPlyCount);
                    move.setFirstMove(!getPieceAtPoint(startSquare).hasMoved);
                    moves.add(move);
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
        return piece.type == PieceType.Queen || piece.type == PieceType.King
                || isDiagonal && (piece.type == PieceType.Bishop || (piece.type == PieceType.Pawn
                        && (direction.y > 0 == piece.isWhite) && direction.x != 0))
                || !isDiagonal && (piece.type == PieceType.Rook);
    }

    public Piece getPieceAtPoint(Point point)
    {
        if (!isInBounds(point))
            return null;
        return board[point.y][point.x];
    }

    public void setPieceAtPoint(Piece piece, Point point)
    {
        if (!isInBounds(point))
            return;
        board[point.y][point.x] = piece;
    }

    public boolean isCheckmate()
    {
        return getLegalMoves().length == 0 && isInCheck();
    }

    private boolean isInBounds(Point coord)
    {
        return coord.x >= 0 && coord.y >= 0 && coord.x < 8 && coord.y < 8;
    }

    boolean isSafeEmptySquare(int x, int y)
    {
        return board[y][x] == null && !squareIsAttacked(new Point(x, y));
    }

    public boolean isDraw()
    {
        return getLegalMoves().length == 0 && !isInCheck() || halfPlyCount == 50
                || isRepeatedPosition();
    }

    public boolean isRepeatedPosition()
    {
        int repetitions = 0;
        if (halfPlyCount < 3 || previousPositions.size() < 3)
        {
            return false;
        }
        for (int i = previousPositions.size() - 1; i > Math.max(-1,
                previousPositions.size() - 1 - halfPlyCount - 1); i--)
        // subtracting 1 from halfplycount because it starts at 0 with a pawnmove or capture
        {
            if (previousPositions.get(i) == getZobristHash())
            {
                repetitions += 1;
            }
        }
        return repetitions > 2;
    }


    public boolean isInCheck()
    {
        if (isInCheck == null)
        {
            Piece king = getAllPieces(PieceType.King, whiteToMove)[0];
            isInCheck = squareIsAttacked(king.position);
            return isInCheck;
        }
        return isInCheck;
    }

    public ArrayList<Piece> getWhitePieces()
    {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces()
    {
        return blackPieces;
    }

    public long getZobristHash()
    {
        if (!hashIsCached)
        {
            // TODO cachedHash = ZobristHasher.generateHash(this);
            hashIsCached = true;
        }
        return cachedHash;
    }

    /**
     * Figures out if you can castle to a given side (0 or 7), for the given color
     *
     * @param side Kingside (7) or queenside (0)
     * @param white Whether the pieces are white (true) or not (false)
     * @return
     */
    public boolean canCastle(boolean kingside, boolean white)
    {
        int rank = white ? 0 : 7;
        Piece piece = board[rank][kingside ? 7 : 0];
        if (kingside) // kingside
        {
            return piece != null && piece.type == PieceType.Rook && piece.isWhite == whiteToMove
                    && !piece.hasMoved && isSafeEmptySquare(5, rank) && isSafeEmptySquare(6, rank);
        }
        else
        {
            return piece != null && piece.type == PieceType.Rook && piece.isWhite == whiteToMove
                    && !piece.hasMoved && isSafeEmptySquare(3, rank) && isSafeEmptySquare(2, rank)
                    && board[rank][1] == null;
        }

    }
}
