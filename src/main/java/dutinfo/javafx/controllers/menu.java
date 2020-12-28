package dutinfo.javafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class menu implements Initializable {
    @FXML
    private GridPane menu;

    private AnchorPane jeu;

    private FXMLLoader loader;

    @FXML
    private ComboBox difficulty;

    @FXML
    private TextField islandName, presidentName;

    public void initialize(URL location, ResourceBundle resources) {
        difficulty.getItems().removeAll(difficulty.getItems());
        difficulty.getItems().addAll("EASY", "NORMAL", "HARD");
        difficulty.getSelectionModel().select("NORMAL");
    }

    @FXML
    private void JeMeLance() throws IOException {
        mainController.initParameters(difficulty.getSelectionModel().getSelectedItem().toString(), islandName.getText(), presidentName.getText());

        loader = new FXMLLoader(getClass().getResource("/view/view.fxml"));

        jeu = loader.load();

        /* ON FAIT PLACE NETTE */
        while(menu.getRowConstraints().size() > 0){
            menu.getRowConstraints().remove(0);
        }

        while(menu.getColumnConstraints().size() > 0){
            menu.getColumnConstraints().remove(0);
        }

        // on ajoute le jeu au gridpane
        menu.add(jeu,0,1);
    }
}
