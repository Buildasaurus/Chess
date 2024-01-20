package chess;

public class Settings
{
    private static int initalWindowSize = 800;
    public static int WindowSize = initalWindowSize/9 * 9;

    public static int getColumnWidth()
    {
        return WindowSize / 9;
    }

    public static int getBoardSize()
    {
        return getColumnWidth()*8;
    }


}
