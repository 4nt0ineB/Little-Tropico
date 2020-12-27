package dutinfo.javafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class menu {
    @FXML
    private AnchorPane menu,jeu;

    private FXMLLoader loader;

    @FXML
    private void JeMeLance() throws IOException, IOException {
        loader = new FXMLLoader(getClass().getResource("/view/view.fxml"));

        jeu = loader.load();

        menu.getChildren().setAll(jeu);
    }
}
