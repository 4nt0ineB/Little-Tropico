package dutinfo.javafx.controllers;

import dutinfo.game.Game;
import dutinfo.game.GameUtils;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Action;
import dutinfo.game.events.Event;
import dutinfo.game.society.President;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import dutinfo.javafx.models.Model;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class mainController implements EventHandler<MouseEvent> {

    private static Game game;

    private Model appModel = new Model();

    @FXML
    private ImageView sun, vein, sweat;

    @FXML
    private Label moneyAmount, citizensCount, season, day, presidente, eventLabel, industryPercentage, farmingPercentage, capitalistsPercentage, communistsPercentage, liberalistsPercentage, religiousPercentage, militaristsPercentage, ecologistsPercentage, nationalistsPercentage, loyalistsPercentage, islandName;

    @FXML
    private Rectangle eventAlert;

    @FXML
    private ComboBox eventChoice;

    @FXML
    private Button selectEvent;

    @FXML
    private Text eventDescription, eventEffects, eventEffects2;

    @FXML
    private Line eventLine, eventSeparator;

    private Event currentEvent;
    private int currentActionId;


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
        selectEvent.setStyle("-fx-background-color: #3f7886; -fx-text-fill: white;");

        // Hide president anger
        vein.setVisible(false);
        sweat.setVisible(false);

        islandName.setText(game.getIsland().getName());
        presidente.setText(game.getIsland().getPresident().getName());
        moneyAmount.setText("$"+game.getIsland().getTreasury().toString()); // Treasury


        citizensCount.setText(game.getScenario().getFollowers()+"");


        season.setText("Current season: "+game.getIsland().getSeason()); // season
        day.setText("Day 1");

        /* PERCENTAGES */
        refreshPercentages();

        openEventWindow(game.getScenario().getRandomEventOnSeason(game.getIsland().getSeason())); // get random event by season
    }

    /**
     * Function that permits choice to be submitted to the MCP and launched when click on "Select" button during an event choice
     */
    public void submitEventChoice(){

        // TODO: Event choice handler to make

        currentActionId = GameUtils.idByHashString(eventChoice.getSelectionModel().getSelectedItem().toString());

        game.playAction(currentEvent.getActions().stream().filter(x -> GameUtils.idByHashString(x.getTitle()) == currentActionId).findFirst().get());

        // test: show president anger if he lose pts
        vein.setVisible(true);
        sweat.setVisible(true);


        /* Close event window by setting it to false */
        closeEventWindow();

        refreshPercentages(); // refresh to show new percentages
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
        eventEffects.setVisible(false);
        eventEffects2.setVisible(false);
        eventLine.setVisible(false);
        eventSeparator.setVisible(false);
    }

    public void refreshPercentages(){
        colorPercentages(industryPercentage, game.getFieldByName("Industry").getExploitationPercentage()); // industry
        colorPercentages(farmingPercentage, game.getFieldByName("Agriculture").getExploitationPercentage()); // farming

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

    /**
     * Function that sets window to visible and that autocomplete labels and selection by event
     * @param event
     */
    public void openEventWindow(Event event){

        this.currentEvent = event;
        // TODO: Handle Event fields

        eventDescription.setText(event.getTitle());


        for (Action a:
             event.getActions()) {
            eventChoice.getItems().add(a.getTitle());
        }
        eventChoice.getSelectionModel().select(event.getActions().get(0).getTitle()); // Picking the first option by default

        // auto load auto selected effects
        eventEffects.setText(currentEvent.getActions().stream().filter(x -> GameUtils.idByHashString(x.getTitle()) == GameUtils.idByHashString(event.getActions().get(0).getTitle())).findFirst().get().getEffectsPercentagesToString());
        eventEffects2.setText(currentEvent.getActions().stream().filter(x -> GameUtils.idByHashString(x.getTitle()) == GameUtils.idByHashString(event.getActions().get(0).getTitle())).findFirst().get().getEffectsToString());
        // s'affiche sous la forme nombre +/- @Java.Double. etc

        eventChoice.valueProperty().addListener(new ChangeListener<String>() { // change effects when changing combobox value
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                Action temp = currentEvent.getActions().stream().filter(x -> GameUtils.idByHashString(x.getTitle()) == GameUtils.idByHashString(newValue)).findFirst().get();
                eventEffects.setText(temp.getEffectsPercentagesToString()); // change label to factions effects TODO: Format data! (shows adress)
                eventEffects2.setText(temp.getEffectsToString());
            }
        });


        eventAlert.setVisible(true);
        selectEvent.setVisible(true);
        eventChoice.setVisible(true);
        eventDescription.setVisible(true);
        eventLabel.setVisible(true);
        eventEffects.setVisible(true);
        eventEffects2.setVisible(true);
        eventLine.setVisible(true);
        eventSeparator.setVisible(true);
    }



}