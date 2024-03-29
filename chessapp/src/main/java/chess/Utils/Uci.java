package chess.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;
import chess.Models.FenReader;
import chess.Models.Timer;
import chess.Models.Board.Board;
import chess.Models.Board.Move;
import chess.Models.Bots.IBot;
import javafx.application.Platform;

/**
 * For playing with bots using the UCI format. First implement a timer.
 */
public class UCI
{
    IBot bot;
    Board board;

    public UCI(IBot bot)
    {
        this.bot = bot;
        board = new Board();
    }

    void positionCommand(String[] args)
    {
        int idx = -1;

        for (int i = 0; i < args.length; i++)
        {
            if ("moves".equals(args[i]))
            {
                idx = i;
                break;
            }
        }
        if (idx == -1)
        {
            if (args[1].equals("startpos"))
            {
                board = new Board();
            }
            else
            {

                board = FenReader
                        .loadFenString(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
            }
        }
        else
        {
            if (args[1].equals("startpos"))
            {
                board = new Board();
            }
            else
            {
                board = FenReader.loadFenString(String.join(" ", Arrays.copyOfRange(args, 2, idx)));
            }

            for (int i = idx + 1; i < args.length; i++)
            {
                board.tryToMakeMove(args[i]);
            }
        }


    }

    void goCommand(String[] args)
    {
        int wtime = 0, btime = 0, winc = 0, binc = 0;

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("wtime"))
            {
                wtime = Integer.parseInt(args[i + 1]);
            }
            else if (args[i].equals("btime"))
            {
                btime = Integer.parseInt(args[i + 1]);
            }
            else if (args[i].equals("movetime"))
            {
                btime = Integer.parseInt(args[i + 1]);
                wtime = btime;
            }
            else if (args[i].equals("winc"))
            {
                winc = Integer.parseInt(args[i + 1]);
            }
            else if (args[i].equals("binc"))
            {
                binc = Integer.parseInt(args[i + 1]);
            }
        }
        System.out.println(wtime + " " + btime);
        Timer timer = new Timer(wtime, btime, winc, true);
        Move move = bot.think(board, timer);
        System.out.println("bestmove " + move.toString());
    }

    void execCommand(String line)
    {
        // default split by whitespace
        String[] tokens = line.split(" ");

        if (tokens.length == 0)
            return;

        switch (tokens[0])
        {
            case "uci":
                System.out.println("id name Java Chess Bot");
                System.out.println("id author Jonathan Sommerlund");
                System.out.println("uciok");
                break;
            case "ucinewgame":
                try
                {
                    bot = bot.getClass().getConstructor().newInstance();
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException
                        | NoSuchMethodException e)
                {
                    System.out.println(
                            "Error in uci.java in trying to create new bot of same instance.");
                    e.printStackTrace();
                }
                break;
            case "position":
                positionCommand(tokens);
                break;
            case "isready":
                System.out.println("readyok");
                break;
            case "go":
                goCommand(tokens);
                break;
        }
    }

    public void run()
    {
        Scanner console = new Scanner(System.in);
        while (true)
        {
            String line = console.nextLine();

            if (line.equals("quit") || line.equals("exit"))
            {
                System.out.println("Quitting");
                console.close();
                Platform.runLater(() -> {
                    Platform.exit();
                    System.exit(0);
                });
                return;
            }
            execCommand(line);
        }
    }

}
