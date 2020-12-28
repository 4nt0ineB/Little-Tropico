package dutinfo.javafx.controllers;

import dutinfo.game.Game;
import dutinfo.game.society.President;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import dutinfo.javafx.models.Model;

public class mainController implements EventHandler<MouseEvent> {

    private Model appModel = new Model();

    @FXML
    private ImageView sun;

    @FXML
    private Label moneyAmount, industryPercentage, farmingPercentage, capitalistsPercentage, communistsPercentage, liberalistsPercentage, religiousPercentage, militaristsPercentage, ecologistsPercentage, nationalistsPercentage, loyalistsPercentage, islandName;

    @Override
    public void handle(MouseEvent e) {
    }

    public void initialize() {
        appModel.startAnimations(sun); // sun rolling

        Game game = Game.initGame();
        President president = new President("el president");
        game.setScenario(game.getScenarios().get(0));
        game.setIsland("tropicoIsland", president);
        game.getScenario().setEvents(game.getEvents(game.getScenario()));
        game.setDifficulty(Game.Difficulty.EASY);

        islandName.setText(game.getIsland().getName());
        moneyAmount.setText("$"+game.getIsland().getTreasury().toString()); // Treasury
        industryPercentage.setText(game.getFieldByName("Industry").getExploitationPercentage()+"%"); // industry
        farmingPercentage.setText(game.getFieldByName("Agriculture").getExploitationPercentage()+"%"); // farming

        /* FACTIONS */
        capitalistsPercentage.setText(game.getFactionByName("Capitalists").getApprobationPercentage()+"%");
        communistsPercentage.setText(game.getFactionByName("Communists").getApprobationPercentage()+"%");
        liberalistsPercentage.setText(game.getFactionByName("Liberals").getApprobationPercentage()+"%");
        religiousPercentage.setText(game.getFactionByName("Religious").getApprobationPercentage()+"%");
        militaristsPercentage.setText(game.getFactionByName("Militarists").getApprobationPercentage()+"%");
        ecologistsPercentage.setText(game.getFactionByName("Ecologists").getApprobationPercentage()+"%");
        nationalistsPercentage.setText(game.getFactionByName("Nationalists").getApprobationPercentage()+"%");
        loyalistsPercentage.setText(game.getFactionByName("Loyalists").getApprobationPercentage()+"%");

    }



}