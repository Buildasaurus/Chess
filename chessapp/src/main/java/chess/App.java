package chess;

import Tests.ChessModelTests;
import chess.Controllers.MainController;
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

        scene = new Scene(controller.view, Settings.WindowSize, Settings.WindowSize); // Add the view to the scene. Adjust the
                                                           // size as needed
        stage.setScene(scene);
        stage.show();
        if(test)
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
        launch();
    }

}
