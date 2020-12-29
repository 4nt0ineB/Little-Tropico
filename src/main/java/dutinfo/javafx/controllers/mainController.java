package dutinfo.javafx.controllers;

import dutinfo.game.Game;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Action;
import dutinfo.game.events.Event;
import dutinfo.game.society.President;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import dutinfo.javafx.models.Model;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class mainController implements EventHandler<MouseEvent> {

    private static Game game;

    private Model appModel = new Model();

    @FXML
    private ImageView sun;

    @FXML
    private Label moneyAmount, season, day, presidente, eventLabel, industryPercentage, farmingPercentage, capitalistsPercentage, communistsPercentage, liberalistsPercentage, religiousPercentage, militaristsPercentage, ecologistsPercentage, nationalistsPercentage, loyalistsPercentage, islandName;

    @FXML
    private Rectangle eventAlert;

    @FXML
    private ComboBox eventChoice;

    @FXML
    private Button selectEvent;

    @FXML
    private Text eventDescription;


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

        openEventWindow(game.getScenario().getRandomEventOnSeason(game.getIsland().getSeason())); // get random event by season
    }

    /**
     * Function that permits choice to be submitted to the MCP and launched when click on "Select" button during an event choice
     */
    public void submitEventChoice(){

        // TODO: Event choice handler to make
        System.out.println(eventChoice.getSelectionModel().getSelectedItem().toString()); // action chosen by president



        /* Close event window by setting it to false */
        closeEventWindow();
    }

    /**
     * Function that sets windows to invisible but it stills here
     */
    public void closeEventWindow(){
        eventAlert.setVisible(false);
        selectEvent.setVisible(false);
        eventChoice.setVisible(false);
        eventDescription.setVisible(false);
        eventLabel.setVisible(false);
    }

    /**
     * Function that sets window to visible and that autocomplete labels and selection by event
     * @param event
     */
    public void openEventWindow(Event event){
        // TODO: Handle Event fields

        eventDescription.setText(event.getTitle());

        for (Action a:
             event.getActions()) {
            eventChoice.getItems().add(a.getTitle());
        }
        eventChoice.getSelectionModel().select(event.getActions().get(0).getTitle()); // Picking the first option by default


        eventAlert.setVisible(true);
        selectEvent.setVisible(true);
        eventChoice.setVisible(true);
        eventDescription.setVisible(true);
        eventLabel.setVisible(true);
    }



}