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

    private static Game game;

    private Model appModel = new Model();

    @FXML
    private ImageView sun;

    @FXML
    private Label moneyAmount, season, day, presidente, industryPercentage, farmingPercentage, capitalistsPercentage, communistsPercentage, liberalistsPercentage, religiousPercentage, militaristsPercentage, ecologistsPercentage, nationalistsPercentage, loyalistsPercentage, islandName;

    @Override
    public void handle(MouseEvent e) {
    }

    /**
     * Function that inits parameters before launching JavaFX window
     * @param gameObject
     * @param difficulty
     * @param islandName
     * @param presidentName
     * @param scenario
     */
    public static void initParameters(Game gameObject, String difficulty, String islandName, String presidentName, String scenario){
        game = gameObject;
        President president = new President(presidentName);
        game.setScenario(game.getScenarios().get(Integer.parseInt(scenario.split(" -")[0]))); // get the scenario number to initialize it
        game.setIsland(islandName, president);
        game.getScenario().setEvents(game.getEvents(game.getScenario()));
        game.setDifficulty(Game.Difficulty.valueOf(difficulty));
    }

    /**
     * Allows the percentages to be automatically colored by their level
     * @param element
     * @param percentage
     */
    public void colorPercentages(Label element, double percentage){
        if (percentage <= 50 && percentage > 20){
            element.setText(percentage+"%");
            element.setStyle("-fx-text-fill: #d9a400;");
        } else if (percentage <= 20){
            element.setText(percentage+"%");
            element.setStyle("-fx-text-fill: #ff0000;");
        } else if (percentage > 60){
            element.setText(percentage+"%");
            element.setStyle("-fx-text-fill: #38ae38;");
        }
    }

    /**
     * Initialize all the game window (animations, texts, etc...)
     */
    public void initialize() {
        appModel.startAnimations(sun); // sun rolling

        islandName.setText(game.getIsland().getName());
        presidente.setText(game.getIsland().getPresident().getName());
        moneyAmount.setText("$"+game.getIsland().getTreasury().toString()); // Treasury

        colorPercentages(industryPercentage, game.getFieldByName("Industry").getExploitationPercentage()); // industry
        colorPercentages(farmingPercentage, game.getFieldByName("Agriculture").getExploitationPercentage()); // farming


        season.setText("Current season: "+game.getIsland().getSeason()); // season
        day.setText("Day 1");

        /* FACTIONS */
        colorPercentages(capitalistsPercentage, game.getFactionByName("Capitalists").getApprobationPercentage());
        colorPercentages(communistsPercentage, game.getFactionByName("Communists").getApprobationPercentage());
        colorPercentages(liberalistsPercentage, game.getFactionByName("Liberals").getApprobationPercentage());
        colorPercentages(religiousPercentage, game.getFactionByName("Religious").getApprobationPercentage());
        colorPercentages(militaristsPercentage, game.getFactionByName("Militarists").getApprobationPercentage());
        colorPercentages(ecologistsPercentage, game.getFactionByName("Ecologists").getApprobationPercentage());
        colorPercentages(nationalistsPercentage, game.getFactionByName("Nationalists").getApprobationPercentage());
        colorPercentages(loyalistsPercentage, game.getFactionByName("Loyalists").getApprobationPercentage());


    }



}