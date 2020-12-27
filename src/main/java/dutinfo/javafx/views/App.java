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

        Game game = Game.initGame();
        President president = new President("el president");
        game.setScenario(game.getScenarios().get(0));
        game.setIsland("tropicoIsland", president);
        game.getScenario().setEvents(game.getEvents(game.getScenario()));
        game.setDifficulty(Game.Difficulty.EASY);

        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("view\\view.fxml"));
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}