package chess.Models;

public class Timer
{
    /**
     * The remaining time for the white player (in nanoseconds)
     */
    public long whiteRemainingTime;
    public long blackRemainingTime;
    public long blackIncrement;
    public long whiteIncrement;
    private boolean whitesTurn;
    private long turnStartTime;
    long gameStartTime;

    /**
     * A Timer can be used to keep track of the time of the game.
     *
     * @param blackTime The time for the black player, in seconds.
     * @param whiteTime The time for the black player, in seconds.
     * @param increment The increment time for both players, in seconds.
     */
    public Timer(long blackTime, long whiteTime, long increment, boolean whiteToMove)
    {
        whiteRemainingTime = whiteTime * 1_000_000_000;
        blackRemainingTime = blackTime * 1_000_000_000;
        blackIncrement = increment * 1_000_000_000;
        whiteIncrement = increment * 1_000_000_000;
        whitesTurn = whiteToMove;
        gameStartTime = System.nanoTime();
        turnStartTime = System.nanoTime();
    }

    public void switchTurn()
    {
        if (whitesTurn)
        {
            whiteRemainingTime -= timeElapsedOnCurrentTurn() - whiteIncrement;
        }
        else
        {
            blackRemainingTime -= timeElapsedOnCurrentTurn() - blackIncrement;
        }
        whitesTurn = !whitesTurn;
        turnStartTime = System.nanoTime();
    }

    public long timeElapsedOnCurrentTurn()
    {
        return System.nanoTime() - turnStartTime;
    }

    public long remainingTime(boolean white)
    {
        return white ? whiteRemainingTime : blackRemainingTime;
    }

    public long remainingTimeInSeconds(boolean white)
    {
        return white ? Math.round(whiteRemainingTime/Math.pow(10, 9)) : Math.round(blackRemainingTime/Math.pow(10, 9));
    }
}
