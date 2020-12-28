package dutinfo.javafx.controllers;

import dutinfo.game.Game;
import dutinfo.game.events.Scenario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class menu implements Initializable {
    @FXML
    private GridPane menu;

    private AnchorPane jeu;

    private FXMLLoader loader;

    @FXML
    private ComboBox difficulty, scenario;

    @FXML
    private TextField islandName, presidentName;

    private static Game game = Game.initGame();

    public void initialize(URL location, ResourceBundle resources) {
        difficulty.getItems().removeAll(difficulty.getItems());
        difficulty.getItems().addAll("EASY", "NORMAL", "HARD");
        difficulty.getSelectionModel().select("NORMAL");

        List<Scenario> scenarios = game.getScenarios();
        for(int i = 0; i < scenarios.size(); i++){
            scenario.getItems().add(i+" - "+scenarios.get(i));
        }
        scenario.getSelectionModel().select(0+" - "+scenarios.get(0)); // Picking the first scenario by default
    }

    @FXML
    private void JeMeLance() throws IOException {
        mainController.initParameters(game, difficulty.getSelectionModel().getSelectedItem().toString(), islandName.getText(), presidentName.getText(), scenario.getSelectionModel().getSelectedItem().toString());

        loader = new FXMLLoader(getClass().getResource("/view/view.fxml"));

        jeu = loader.load();

        /* Cleaning the gridpane  */
        while(menu.getRowConstraints().size() > 0){
            menu.getRowConstraints().remove(0);
        }

        while(menu.getColumnConstraints().size() > 0){
            menu.getColumnConstraints().remove(0);
        }

        // Add the game to the first row and column of GP
        menu.add(jeu,0,1);
    }
}
