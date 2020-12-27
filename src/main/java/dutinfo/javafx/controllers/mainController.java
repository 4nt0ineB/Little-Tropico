package dutinfo.javafx.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import dutinfo.javafx.models.Model;

public class mainController implements EventHandler<MouseEvent> {

    private Model appModel = new Model();

    @FXML
    private ImageView sun;

    @Override
    public void handle(MouseEvent e) {
    }

    public void initialize() {

        appModel.startAnimations(sun);
    }



}