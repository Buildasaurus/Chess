package chess;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import javafx.scene.image.Image;

public class Resources
{
    private static HashMap<String, Image> map = new HashMap<String, Image>();

    /**
     * Returns an image of the image with the given name. Only expects the name, not the filetype of
     * the image. Will look for the image in the resources folder
     *
     * @param name The name of the file - not including .png. Is assumed to a png file
     * @return The image with the given name.
     */
    public static Image getImageByName(String name)
    {
        if (!map.containsKey(name))
        {
            URL url = Resources.class.getResource("/" + name + ".png");
            Image image = new Image(url.toString());
            map.put(name, image);

        }
        return map.get(name);
    }


    public static String[] getFileByName(String name)
    {

        String[] array = new String[0];
        try
        {
            URL url = Resources.class.getResource("/" + name + ".txt");
            Path filePath = Paths.get(url.toURI());
            array =  Files.readAllLines(filePath).toArray(new String[0]);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return array;
    }
}
