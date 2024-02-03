package chess.Models.Evaluation;

import java.util.ArrayList;
import chess.Models.Board;
import chess.Models.Piece;
import chess.Models.Piece.PieceType;



// Static class
public class SimpleEval
{
    static int[] pieceValues =
    {82, 337, 365, 477, 1025, 0};
    static int[] endGameValues =
    {94, 281, 297, 512, 936, 0};
    static int[][] mg_knight_table =
    {
        {-167, -89, -34, -49, 61, -97, -15, -107},
        {-73, -41, 72, 36, 23, 62, 7, -17},
        {-47, 60, 37, 65, 84, 129, 73, 44},
        {-9, 17, 19, 53, 37, 69, 18, 22},
        {-13, 4, 16, 13, 28, 19, 21, -8},
        {-23, -9, 12, 10, 19, 17, 25, -16},
        {-29, -53, -12, -3, -1, 18, -14, -19},
        {-105, -21, -58, -33, -17, -28, -19, -23}};

    static int[][] mg_bishop_table =
    {
        {-29, 4, -82, -37, -25, -42, 7, -8},
        {-26, 16, -18, -13, 30, 59, 18, -47},
        {-16, 37, 43, 40, 35, 50, 37, -2},
        {-4, 5, 19, 50, 37, 37, 7, -2},
        {-6, 13, 13, 26, 34, 12, 10, 4},
        {0, 15, 15, 15, 14, 27, 18, 10},
        {4, 15, 16, 0, 7, 21, 33, 1},
        {-33, -3, -14, -21, -13, -12, -39, -21}};


    static int[][] mg_rook_table =
    {
        {32, 42, 32, 51, 63, 9, 31, 43},
        {27, 32, 58, 62, 80, 67, 26, 44},
        {-5, 19, 26, 36, 17, 45, 61, 16},
        {-24, -11, 7, 26, 24, 35, -8, -20},
        {-36, -26, -12, -1, 9, -7, 6, -23},
        {-45, -25, -16, -17, 3, 0, -5, -33},
        {-44, -16, -20, -9, -1, 11, -6, -71},
        {-19, -13, 1, 17, 16, 7, -37, -26}};

    static int[][] mg_queen_table =
    {
        {-28, 0, 29, 12, 59, 44, 43, 45},
        {-24, -39, -5, 1, -16, 57, 28, 54},
        {-13, -17, 7, 8, 29, 56, 47, 57},
        {-27, -27, -16, -16, -1, 17, -2, 1},
        {-9, -26, -9, -10, -2, -4, 3, -3},
        {-14, 2, -11, -2, -5, 2, 14, 5},
        {-35, -8, 11, 2, 8, 15, -3, 1},
        {-1, -18, -9, 10, -15, -25, -31, -50}};
    static int[][] mg_pawn_table =
    {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {98, 134, 61, 95, 68, 126, 34, -11},
        {-6, 7, 26, 31, 65, 56, 25, -20},
        {-14, 13, 6, 21, 23, 12, 17, -23},
        {-27, -2, -5, 12, 17, 6, 10, -25},
        {-26, -4, -4, -10, 3, 3, 33, -12},
        {-35, -1, -20, -23, -15, 24, 38, -22},
        {0, 0, 0, 0, 0, 0, 0, 0}};
    static int[][] eg_rook_table =
    {
        {13, 10, 18, 15, 12, 12, 8, 5},
        {11, 13, 13, 11, -3, 3, 8, 3},
        {7, 7, 7, 5, 4, -3, -5, -3},
        {4, 3, 13, 1, 2, 1, -1, 2},
        {3, 5, 8, 4, -5, -6, -8, -11},
        {-4, 0, -5, -1, -7, -12, -8, -16},
        {-6, -6, 0, 2, -9, -9, -11, -3},
        {-9, 2, 3, -1, -5, -13, 4, -20}};
    static int[][] mg_king_table =
    {
        {-65, 23, 16, -15, -56, -34, 2, 13},
        {29, -1, -20, -7, -8, -4, -38, -29},
        {-9, 24, 2, -16, -20, 6, 22, -22},
        {-17, -20, -12, -27, -30, -25, -14, -36},
        {-49, -1, -27, -39, -46, -44, -33, -51},
        {-14, -14, -22, -46, -44, -30, -15, -27},
        {1, 7, -8, -64, -43, -16, 9, 8},
        {-15, 36, 12, -54, 8, -28, 24, 14}};

    static int[] phase_weight =
    {0, 1, 1, 2, 4, 0};

    static int[][] eg_pawn_table =
    {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {178, 173, 158, 134, 147, 132, 165, 187},
        {94, 100, 85, 67, 56, 53, 82, 84},
        {32, 24, 13, 5, -2, 4, 17, 17},
        {13, 9, -3, -7, -7, -8, 3, -1},
        {4, 7, -6, 1, 0, -5, -1, -8},
        {13, 8, 8, 10, 13, 0, 2, -7},
        {0, 0, 0, 0, 0, 0, 0, 0}};



    public static int evaluation(Board board)
    {
        int eval = 0;
        int gamePhase = 24;
        for (PieceType type : PieceType.values())
        {
            int piececount =
                    board.getAllPieces(type, false).length + board.getAllPieces(type, true).length;
            gamePhase -= piececount * phase_weight[type.ordinal()];
        }
        gamePhase = Math.max(gamePhase, 0);
        for (ArrayList<Piece> pieces : board.getAllPieceLists())
        {
            int playerSign = pieces.get(0).isWhite ? 1 : -1;
            int openingEval = 0;
            int endgameEval = 0;
            for (Piece piece : pieces)
            {
                openingEval += pieceValues[piece.type.ordinal()];
                endgameEval += endGameValues[piece.type.ordinal()];

                int rank = piece.isWhite ? 7 - piece.position.y : piece.position.y;
                int file = piece.position.x;

                switch (piece.type)
                {
                    case Pawn:
                        openingEval += mg_pawn_table[rank][file];
                        endgameEval += eg_pawn_table[rank][file];
                        break;
                    case Knight:
                        openingEval += mg_knight_table[rank][file];
                        endgameEval += mg_knight_table[rank][file];
                        break;
                    case Bishop:
                        openingEval += mg_bishop_table[rank][file];
                        endgameEval += mg_bishop_table[rank][file];
                        break;
                    case Queen:
                        openingEval += mg_queen_table[rank][file];
                        endgameEval += mg_queen_table[rank][file];
                        break;
                    case Rook:
                        openingEval += mg_rook_table[rank][file];
                        endgameEval += eg_rook_table[rank][file];
                        break;
                    case King:
                        openingEval += mg_king_table[rank][file];
                        endgameEval += mg_knight_table[rank][file];
                        break;
                }
            }
            eval += ((openingEval * (24 - gamePhase)) + (endgameEval * gamePhase)) / 24
                    * playerSign;

        }
        return eval;
    }
}
