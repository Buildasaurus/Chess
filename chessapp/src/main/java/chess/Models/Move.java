package chess.Models;

import chess.Models.Piece.PieceType;
import chess.Utils.BoardHelper;

public class Move
{
    public Point startSquare;
    public Point targetSquare;
    public boolean isCapture;
    public boolean isPromotion;
    public boolean isEnPassent;
    public PieceType promotionType;
    public boolean isCastling;
    private boolean firstMove;
    private PieceType capturePieceType;
    public boolean capturedPieceHadMoved;
    public int previousHalfPlyCount;

    public Move(Point _startSquare, Point _targetSquare)
    {
        System.out.println(
                "Warning, incorrect constructor! - Only for testing - missing halfMoveCount");
        firstMove = false;
        startSquare = _startSquare;
        targetSquare = _targetSquare;
        isCapture = false;
        isPromotion = false;
        isEnPassent = false;
        promotionType = PieceType.Queen;
    }

    public Move(Point _startSquare, Point _targetSquare, int halfMoveCount)
    {
        firstMove = false;
        startSquare = _startSquare;
        targetSquare = _targetSquare;
        isCapture = false;
        isPromotion = false;
        isEnPassent = false;
        promotionType = PieceType.Queen;
    }

    /**
     * Creates a move from a UCI name. Eg turns e2e4 into the move e2e4. If promotion, the UCI
     * notation should include what it promotes to.
     *
     * @param moveNameUCI
     * @param board
     */
    public Move(String moveNameUCI)
    {
        this(BoardHelper.squareIndexFromName(moveNameUCI.charAt(0) + "" + moveNameUCI.charAt(1)),
                BoardHelper
                        .squareIndexFromName(moveNameUCI.charAt(2) + "" + moveNameUCI.charAt(3)));

        char promoteChar =
                moveNameUCI.length() > 3 ? moveNameUCI.charAt(moveNameUCI.length() - 1) : ' ';

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

        this.promotionType = promotePieceType;
    }

    public boolean equals(Move otherMove)
    {
        return otherMove.startSquare.equals(startSquare)
                && otherMove.targetSquare.equals(targetSquare)
                && promotionType == otherMove.promotionType;
    }

    /**
     * Prints Uci format of the move.
     */
    @Override
    public String toString()
    {
        String promotionString;
        switch (capturePieceType)
        {
            case Queen:
                promotionString = "Q";
                break;
            case Knight:
                promotionString = "N";
                break;
            case Rook:
                promotionString = "R";
                break;
            case Bishop:
                promotionString = "B";
                break;
            default:
                promotionString = "";
                break;
        }
        return (char) (startSquare.x + 65) + Integer.toString(startSquare.y + 1)
                + (char) (targetSquare.x + 65) + Integer.toString(targetSquare.y + 1)
                + promotionString;
    }

    public void setCapturePieceType(PieceType type)
    {
        if (type == PieceType.King)
        {
            System.out.println("Someone just took a king...");
        }
        capturePieceType = type;
        if (type != null)
        {
            isCapture = true;
        }
    }

    public PieceType getCapturePieceType()
    {
        return capturePieceType;
    }

    public void setFirstMove(boolean value)
    {
        firstMove = value;
    }

    public boolean getFirstMove()
    {
        return firstMove;
    }

    public Move copy()
    {
        Move move = new Move(startSquare, targetSquare, previousHalfPlyCount);
        move.setFirstMove(firstMove);
        move.capturedPieceHadMoved = capturedPieceHadMoved;
        move.isCapture = isCapture;
        move.isCastling = isCastling;
        move.isEnPassent = isEnPassent;
        move.promotionType = promotionType;
        move.isPromotion = isPromotion;
        move.setCapturePieceType(capturePieceType);
        return move;
    }
}
