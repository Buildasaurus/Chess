package chess.Bots.Evaluation;

import java.util.ArrayList;
import chess.Models.Board;
import chess.Models.Piece;
import chess.Models.Piece.PieceType;


/*
 * possible better than simple eval by
 * Score of V2.1 vs V2.2: 162 - 201 - 87  [0.457] 450
...      V2.1 playing White: 89 - 93 - 44  [0.491] 226
...      V2.1 playing Black: 73 - 108 - 43  [0.422] 224
...      White vs Black: 197 - 166 - 87  [0.534] 450
Elo difference: -30.2 +/- 28.9, LOS: 2.0 %, DrawRatio: 19.3 %
SPRT: llr -0.756 (-25.7%), lbound -2.94, ubound 2.94
 */
// Static class
public class Eval2
{
    static int[] mgValues =
    {82, 337, 365, 477, 1025, 0};
    static int[] egValues =
    {94, 281, 297, 512, 936, 0};

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

    static int[][] eg_knight_table =
    {
        {-58, -38, -13, -28, -31, -27, -63, -99},
        {-25, -8, -25, -2, -9, -25, -24, -52},
        {-24, -20, 10, 9, -1, -9, -19, -41},
        {-17, 3, 22, 22, 22, 11, 8, -18},
        {-18, -6, 16, 25, 16, 17, 4, -18},
        {-23, -3, -1, 15, 10, -3, -20, -22},
        {-42, -20, -10, -5, -2, -20, -23, -44},
        {-29, -51, -23, -15, -22, -18, -50, -64}};


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

    static int[][] eg_bishop_table =
    {
        {-14, -21, -11, -8, -7, -9, -17, -24},
        {-8, -4, 7, -12, -3, -13, -4, -14},
        {2, -8, 0, -1, -2, 6, 0, 4},
        {-3, 9, 12, 9, 14, 10, 3, 2},
        {-6, 3, 13, 19, 7, 10, -3, -9},
        {-12, -3, 8, 10, 13, 3, -7, -15},
        {-14, -18, -7, -1, 4, -9, -15, -27},
        {-23, -9, -23, -5, -9, -16, -5, -17}};


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

    static int[][] eg_queen_table =
    {
        {-9, 22, 22, 27, 27, 19, 10, 20},
        {-17, 20, 32, 41, 58, 25, 30, 0},
        {-20, 6, 9, 49, 47, 35, 19, 9},
        {3, 22, 24, 45, 57, 40, 57, 36},
        {-18, 28, 19, 47, 31, 34, 39, 23},
        {-16, -27, 15, 6, 9, 17, 10, 5},
        {-22, -23, -30, -16, -16, -23, -36, -32},
        {-33, -28, -22, -43, -5, -32, -20, -41}};


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

    static int[][] eg_king_table =
    {
        {-74, -35, -18, -18, -11, 15, 4, -17},
        {-12, 17, 14, 17, 17, 38, 23, 11},
        {10, 17, 23, 15, 20, 45, 44, 13},
        {-8, 22, 24, 27, 26, 33, 26, 3},
        {-18, -4, 21, 24, 27, 23, 9, -11},
        {-19, -3, 11, 21, 23, 16, 7, -9},
        {-27, -11, 4, 13, 14, 4, -5, -17},
        {-53, -34, -21, -11, -28, -14, -24, -43}};



    static int[] phase_weight =
    {0, 1, 1, 2, 4, 0};

    // initializer
    static
    {
        addValue(mg_pawn_table, mgValues[0]);
        addValue(eg_pawn_table, egValues[0]);
        addValue(mg_knight_table, mgValues[1]);
        addValue(eg_knight_table, egValues[1]);
        addValue(mg_bishop_table, mgValues[2]);
        addValue(eg_bishop_table, egValues[2]);
        addValue(mg_rook_table, mgValues[3]);
        addValue(eg_rook_table, egValues[3]);
        addValue(mg_queen_table, mgValues[4]);
        addValue(eg_queen_table, egValues[4]);
    }

    public static void addValue(int[][] table, int value)
    {
        for (int row = 0; row < table.length; row++)
        {
            for (int column = 0; column < table[0].length; column++)
            {
                table[row][column] += value;
            }
        }
    }

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
                        endgameEval += eg_knight_table[rank][file];
                        break;
                    case Bishop:
                        openingEval += mg_bishop_table[rank][file];
                        endgameEval += eg_bishop_table[rank][file];
                        break;
                    case Queen:
                        openingEval += mg_queen_table[rank][file];
                        endgameEval += eg_queen_table[rank][file];
                        break;
                    case Rook:
                        openingEval += mg_rook_table[rank][file];
                        endgameEval += eg_rook_table[rank][file];
                        break;
                    case King:
                        openingEval += mg_king_table[rank][file];
                        endgameEval += eg_king_table[rank][file];
                        break;
                }
            }
            eval += ((openingEval * (24 - gamePhase)) + (endgameEval * gamePhase)) / 24
                    * playerSign;

        }
        return eval;
    }
}
