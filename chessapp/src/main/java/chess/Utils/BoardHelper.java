package chess.Utils;

import chess.Models.Point;

// Static class
public class BoardHelper
{
    public final static String fileNames = "abcdefgh";
    public final static String rankNames = "12345678";

    public static Point squareIndexFromName(String name)
    {
        char fileName = name.charAt(0);
        char rankName = name.charAt(1);

        int fileIndex = fileNames.indexOf(fileName);
        int rankIndex = rankNames.indexOf(rankName);
        return new Point(fileIndex, rankIndex);
    };

}
