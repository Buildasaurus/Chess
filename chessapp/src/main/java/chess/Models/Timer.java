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
     * @param blackTime The time for the black player, in miliseconds.
     * @param whiteTime The time for the black player, in miliseconds.
     * @param increment The increment time for both players, in miliseconds.
     */
    public Timer(long whiteTime, long blackTime, long increment, boolean whiteToMove)
    {
        whiteRemainingTime = whiteTime * 1_000_000;
        blackRemainingTime = blackTime * 1_000_000;
        blackIncrement = increment * 1_000_000;
        whiteIncrement = increment * 1_000_000;
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

    public long getRemainingTime(boolean white)
    {
        return white ? whiteRemainingTime : blackRemainingTime;
    }

    public long getIncrement(boolean white)
    {
        return white ? whiteIncrement : blackIncrement;
    }

    public long remainingTimeInSeconds(boolean white)
    {
        return white ? Math.round(whiteRemainingTime/Math.pow(10, 6)) : Math.round(blackRemainingTime/Math.pow(10, 6));
    }

}
