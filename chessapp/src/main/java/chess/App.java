package chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        ChessModel model = new ChessModel(); // Create your model
        ChessView view = new ChessView(); // Create your view
        ChessController controller = new ChessController(view, model); // Create your controller

        scene = new Scene(view, 800, 800); // Add the view to the scene. Adjust the size as needed
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
