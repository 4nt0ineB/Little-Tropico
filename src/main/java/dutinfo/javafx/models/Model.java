package dutinfo.javafx.models;

import dutinfo.game.Game;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Model {

    @FXML
    private ImageView sun;

    public void startAnimations(ImageView sun) {

        RotateTransition rt = new RotateTransition(Duration.millis(30000), sun);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

    }




}