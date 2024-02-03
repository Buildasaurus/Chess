package chess;

import java.lang.reflect.Constructor;
import java.util.Set;
import org.reflections.Reflections;
import Tests.ChessModelTests;
import chess.Controllers.MainController;
import chess.Models.Bots.IBot;
import chess.Utils.UCI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{

    private static Scene scene;
    private boolean test = false;

    @Override
    public void start(Stage stage)
    {
        MainController controller = new MainController();
        // Create your controller
        scene = new Scene(controller.view, Settings.WindowSize, Settings.WindowSize); // Add the
                                                                                      // view to the
                                                                                      // scene.
                                                                                      // Adjust the
        // size as needed
        stage.setScene(scene);
        stage.show();
        if (test)
        {
            new ChessModelTests();
        }
        else
        {
            System.out.println("!!!!! Testing is turned OFF !!!!!!!");
        }
    }

    public static void main(String[] args)
    {
        if (args.length > 1 && args[0].equals("uci"))
        {
            System.out.println("Starting up in UCI mode...");
            startUCI(args);
            return;
        }
        launch();
    }

    public static void startUCI(String[] args)
    {
        IBot bot = null;

        // Nice StackOverflow code to dynamiccaly get all classes that extends fruit, and spawn them
        // https://stackoverflow.com/questions/205573/at-runtime-find-all-classes-in-a-java-application-that-extend-a-base-class
        Reflections reflections = new Reflections("chess");
        Set<Class<? extends IBot>> classes = reflections.getSubTypesOf(chess.Models.Bots.IBot.class);
        for (Class<? extends IBot> aClass : classes)
        {
            System.out.println(aClass.getSimpleName());
            if (aClass.getSimpleName().equals(args[1]))
            {
                try
                { // cursed code from
                  // https://stackoverflow.com/questions/5533702/instantiating-object-of-same-class-from-within-class-in-java
                    Constructor constructor = aClass.getConstructor();
                    bot = (IBot) constructor.newInstance();
                }
                catch (Exception e)
                {
                    System.out.println(
                            "Couldn't find the constructor of the bot with name" + args[1]);
                    System.out.println(e);
                }
            }

        }
        if (bot == null)
        {
            System.err.println("Couldn't find the bot with the name " + args[1]);
            return;
        }

        UCI uci = new UCI(bot);
        uci.run();
    }

}
