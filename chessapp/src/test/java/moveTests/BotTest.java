package moveTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import chess.Models.FenReader;
import chess.Models.Board.Board;
import chess.Models.Bots.TesterBot;

public class BotTest
{


    private void testBotAtFen(String fen, long[][] expectedResults)
    {
        Board board = FenReader.loadFenString(fen);
        int depth = 1;
        for (long[] results : expectedResults)
        {
            TesterBot bot = new TesterBot(depth);
            bot.think(board, null);
            long[] actualResults = bot.getResults();
            for (int i = 0; i < results.length; i++)
            {
                assertEquals(results[i], actualResults[i]);
            }
            depth++;
        }
    }
    @Test
    public void startPositionTest()
    {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        long[][] results = new long[][]
        {
            {20, 0, 0, 0, 0, 0, 0},
            {400, 0, 0, 0, 0, 0, 0},
            {8902, 34, 0, 0, 0, 12, 0},
            {197281, 1576, 0, 0, 0, 469, 8},};
        testBotAtFen(fen, results);
    }

    @Test
    public void Position2Test()
    {
        String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 10 10";
        long[][] results = new long[][]
        {
            {48, 8, 0, 2, 0, 0, 0},
            {2039, 351, 1, 91, 0, 3, 0},
            {97862, 17102, 45, 3162, 0, 993, 1}};
        testBotAtFen(fen, results);
    }

    @Test
    public void Position3Test()
    {
        String fen = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 10 10";
        long[][] results = new long[][]
        {
            {14, 1, 0, 0, 0, 2, 0},
            {191, 14, 0, 0, 0, 10, 0},
            {2812, 209, 2, 0, 0, 267, 0},
            {43238, 3348, 123, 0, 0, 1680, 17},
            {674624, 52051, 1165, 0, 0, 52950, 0}};


        testBotAtFen(fen, results);
    }

    @Test
    public void Position4Test()
    {
        String fen = "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";
        long[][] results = new long[][]
        {
            {6, 0, 0, 0, 0, 0, 0},
            {264, 87, 0, 6, 48, 10, 0},
            {9467, 1021, 4, 0, 120, 38, 22},
            {422333, 131393, 0, 7795, 60032, 15492, 5}};


        testBotAtFen(fen, results);
    }

    @Test
    public void Position5Test()
    {
        String fen = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";
        long[][] results = new long[][]
        {
            {44},
            {1486},
            {62379},
            {2103487}};


        testBotAtFen(fen, results);
    }


    @Test
    public void Position6Test()
    {
        String fen = "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10";
        long[][] results = new long[][]
        {
            {46},
            {2079},
            {89890}};


        testBotAtFen(fen, results);
    }


}
