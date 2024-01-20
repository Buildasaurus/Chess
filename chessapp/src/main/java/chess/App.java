package chess;

import Tests.ChessModelTests;
import chess.Controllers.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{

    private static Scene scene;

    @Override
    public void start(Stage stage)
    {

        MainController controller = new MainController();
        // Create your controller

        scene = new Scene(controller.view, Settings.WindowSize, Settings.WindowSize); // Add the view to the scene. Adjust the
                                                           // size as needed
        stage.setScene(scene);
        stage.show();
        ChessModelTests tests = new ChessModelTests();
    }

    public static void main(String[] args)
    {
        launch();
    }

}
