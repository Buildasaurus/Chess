package chess;

import Tests.ChessModelTests;
import chess.Controllers.HumanVsComputerController;
import chess.Models.ChessModel;
import chess.Views.ChessView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{

    private static Scene scene;

    @Override
    public void start(Stage stage)
    {
        ChessModel model = new ChessModel(); // Create your model
        ChessView view = new ChessView(); // Create your view
        HumanVsComputerController controller = new HumanVsComputerController(view, model);
        // Create your controller

        scene = new Scene(view, 800, 800); // Add the view to the scene. Adjust the size as needed
        stage.setScene(scene);
        stage.show();
        ChessModelTests tests = new ChessModelTests();
    }

    public static void main(String[] args)
    {
        launch();
    }

}
