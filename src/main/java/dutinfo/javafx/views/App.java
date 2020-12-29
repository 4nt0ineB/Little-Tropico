package dutinfo.javafx.views;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * JavaFX dutinfo.uge.App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            root.setStyle("-fx-background-image: url('https://i.imgur.com/Y1uRiCV.jpg')");
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}