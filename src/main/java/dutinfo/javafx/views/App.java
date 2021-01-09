package dutinfo.javafx.views;


import dutinfo.game.events.Event;
import dutinfo.javafx.controllers.mainController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * JavaFX dutinfo.uge.App
 */
public class App extends Application {

    public static Stage mstage;

    @Override
    public void start(Stage stage) {

        try {
            mstage = stage;
            GridPane root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            root.setStyle("-fx-background-image: url('https://i.imgur.com/Y1uRiCV.jpg')");
            Scene scene = new Scene(root, 1400, 788);
            mainController.menu = scene;
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();




        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    public static void main(String[] args) {
        launch();
    }

}