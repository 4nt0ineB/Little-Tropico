package dutinfo.javafx.views;

import dutinfo.game.Game;
import dutinfo.game.society.President;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * JavaFX dutinfo.uge.App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}