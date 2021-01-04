package dutinfo.javafx.controllers;

import dutinfo.game.Game;
import dutinfo.game.GameUtils;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Action;
import dutinfo.game.events.Event;
import dutinfo.game.society.Faction;
import dutinfo.game.society.President;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Label moneyAmount, EOYLabel, EOYChoiceLabel, errorMsgEOY, citizensCount, foodAmount, season, day, presidente, eventLabel, industryPercentage, farmingPercentage, capitalistsPercentage, communistsPercentage, liberalistsPercentage, religiousPercentage, militaristsPercentage, ecologistsPercentage, nationalistsPercentage, loyalistsPercentage, islandName;

    @FXML
    private Rectangle eventAlert, endYearAlert;

    @FXML
    private ComboBox eventChoice, choiceEOY, factionEOY;

    @FXML
    private Button selectEvent, submitEOY;

    @FXML
    private Text eventDescription, eventEffects, eventEffects2;

    @FXML
    private Line eventLine, eventSeparator;

    @FXML
    private TextField fieldEOY;

    private Event currentEvent;
    private int currentActionId;
    private int chosenOption = 1;
    private int valueEOY;

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
    public void colorPercentages(Label element, double percentage, int supporters){
        if (percentage <= 50 && percentage > 20){
            element.setStyle("-fx-text-fill: #d9a400;");
        } else if (percentage <= 20){
            element.setStyle("-fx-text-fill: #ff0000;");
        } else if (percentage > 60){
            element.setStyle("-fx-text-fill: #38ae38;");
        }

        if (supporters != -1){
            element.setText(Math.round((percentage) * 10) / 10.0+"% / "+ supporters);
        } else {
            element.setText(Math.round((percentage) * 10) / 10.0 +"%");
        }
    }

    /**
     * Initialize all the game window (animations, texts, etc...)
     */
    public void initialize() {
        appModel.startAnimations(sun); // sun rolling
        selectEvent.setStyle("-fx-background-color: #3f7886; -fx-text-fill: white;");
        submitEOY.setStyle("-fx-background-color: #3f7886; -fx-text-fill: white;");

        // Hide president anger
        vein.setVisible(false);
        sweat.setVisible(false);

        // Hide end of year by default
        closeEndYearWindow();

        islandName.setText(game.getIsland().getName());
        presidente.setText(game.getIsland().getPresident().getName());
        moneyAmount.setText("$"+game.getIsland().getTreasury()); // Treasury
        foodAmount.setText(game.getIsland().getFoodUnits()+"");
        citizensCount.setText(game.getIsland().totalSupporters()+"");


        season.setText("Current season: "+game.getIsland().getSeason()); // season
        day.setText("Day 1");

        /* PERCENTAGES */
        refreshPercentages();

        boolean running = true;
        game.nextTurn();
        while (running) {

            openEventWindow(game.getEvent()); // get random event by season

            // if end of year make the recap (bribe or buy food)
            if(game.getIsland().getSeason() == Season.Winter) {
                openEndYearWindow();
            }
            break;

        }

    }

    /**
     * Function that permits choice to be submitted to the MCP and launched when click on "Select" button during an event choice
     */
    public void submitEventChoice(){
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
        /* STATIC */
        moneyAmount.setText("$"+game.getIsland().getTreasury()); // Treasury
        foodAmount.setText(game.getIsland().getFoodUnits()+"");
        citizensCount.setText(game.getIsland().totalSupporters()+"");


        colorPercentages(industryPercentage, game.getFieldByName("Industry").getExploitationPercentage(), -1); // industry
        colorPercentages(farmingPercentage, game.getFieldByName("Agriculture").getExploitationPercentage(), -1); // farming

        /* FACTIONS */
        colorPercentages(capitalistsPercentage, game.getFactionByName("Capitalists").getApprobationPercentage(), game.getFactionByName("Capitalists").getNbrSupporters());
        colorPercentages(communistsPercentage, game.getFactionByName("Communists").getApprobationPercentage(), game.getFactionByName("Communists").getNbrSupporters());
        colorPercentages(liberalistsPercentage, game.getFactionByName("Liberals").getApprobationPercentage(), game.getFactionByName("Liberals").getNbrSupporters());
        colorPercentages(religiousPercentage, game.getFactionByName("Religious").getApprobationPercentage(), game.getFactionByName("Religious").getNbrSupporters());
        colorPercentages(militaristsPercentage, game.getFactionByName("Militarists").getApprobationPercentage(), game.getFactionByName("Militarists").getNbrSupporters());
        colorPercentages(ecologistsPercentage, game.getFactionByName("Ecologists").getApprobationPercentage(), game.getFactionByName("Ecologists").getNbrSupporters());
        colorPercentages(nationalistsPercentage, game.getFactionByName("Nationalists").getApprobationPercentage(), game.getFactionByName("Nationalists").getNbrSupporters());
        colorPercentages(loyalistsPercentage, game.getFactionByName("Loyalists").getApprobationPercentage(), game.getFactionByName("Loyalists").getNbrSupporters());
    }

    /**
     * Function that sets window to visible and that autocomplete labels and selection by event
     * @param event
     */
    public void openEventWindow(Event event){

        this.currentEvent = event;
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
                eventEffects.setText(temp.getEffectsPercentagesToString()); // change label to factions effects
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

    public void openEndYearWindow(){
        endYearAlert.setVisible(true);
        choiceEOY.setVisible(true);
        fieldEOY.setVisible(false);
        factionEOY.setVisible(false);
        errorMsgEOY.setVisible(false);
        submitEOY.setDisable(true);
        submitEOY.setVisible(true);
        EOYLabel.setVisible(true);
        EOYChoiceLabel.setVisible(true);

        choiceEOY.getItems().add(" ");
        choiceEOY.getItems().add("1. Bribe a Faction (+10% satisfaction for $15 by supporters).");
        choiceEOY.getItems().add("2. Buy some food $8 by unit.");
        choiceEOY.getItems().add("3. Pass.");

        // Select the first one
        choiceEOY.getSelectionModel().selectFirst();

        choiceEOY.valueProperty().addListener(new ChangeListener<String>() { // change effects when changing combobox value
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                System.out.println(newValue);
                if (newValue.startsWith("1")){ // first option
                    chosenOption = 1;
                    fieldEOY.clear();
                    fieldEOY.setVisible(true);
                    fieldEOY.setPromptText("Amount of 10% (5 -> 5 * 10%)");
                    factionEOY.setVisible(true);
                    errorMsgEOY.setVisible(false);

                    for (Faction fac: game.getFactions()) {
                        factionEOY.getItems().add(fac.getName());
                    }
                    factionEOY.getSelectionModel().selectFirst();

                    fieldEOY.textProperty().addListener((observable, old, newv) -> {

                        if (fieldEOY.getLength() > 0) {
                            if (newv.matches("-?\\d+")) {
                                if (!game.getIsland().canBeBribed(GameUtils.idByHashString(factionEOY.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(newv))) {
                                    errorMsgEOY.setVisible(true);
                                    errorMsgEOY.setText("You don't have enough money");
                                    submitEOY.setDisable(true);
                                } else {
                                    submitEOY.setDisable(false);
                                    errorMsgEOY.setVisible(false);
                                    submitEOY.setDisable(false);
                                }
                            } else {
                                errorMsgEOY.setVisible(true);
                                errorMsgEOY.setText("You must enter a correct number");
                                submitEOY.setDisable(true);
                            }
                        }
                    });


                } else if (newValue.startsWith("2")){ // second option
                    chosenOption = 2;
                    fieldEOY.setVisible(true);
                    fieldEOY.clear();
                    factionEOY.setVisible(false);
                    errorMsgEOY.setVisible(false);
                    fieldEOY.setPromptText("Amount of units");
                    fieldEOY.textProperty().addListener((observable, old, newv) -> {
                        if (fieldEOY.getLength() > 0) {
                            if (newv.matches("-?\\d+")) {
                                if (!game.canBuyFood(Integer.parseInt(newv))) {
                                    errorMsgEOY.setVisible(true);
                                    errorMsgEOY.setText("You don't have enough money");
                                    submitEOY.setDisable(true);
                                } else {
                                    submitEOY.setDisable(false);
                                    errorMsgEOY.setVisible(false);
                                    submitEOY.setDisable(false);
                                }
                            } else {
                                errorMsgEOY.setVisible(true);
                                errorMsgEOY.setText("You must enter a correct number");
                                submitEOY.setDisable(true);
                            }
                        }
                    });
                }  else if (newValue.startsWith(" ")){ // empty option
                    chosenOption = 0;
                    errorMsgEOY.setText("Select an option");
                    submitEOY.setDisable(true);
                    errorMsgEOY.setVisible(true);
                    fieldEOY.setVisible(false);
                    factionEOY.setVisible(false);
                    submitEOY.setDisable(true);
                }  else { // pass
                    chosenOption = 3;
                    submitEOY.setDisable(false);
                    fieldEOY.setVisible(false);
                    errorMsgEOY.setVisible(false);
                    factionEOY.setVisible(false);
                }
            }
        });
    }

    public void submitEOYChoice(){
        valueEOY = Integer.parseInt(fieldEOY.getText());

        if (chosenOption == 1){ // faction bribe
            game.getIsland().bribeFaction(GameUtils.idByHashString(factionEOY.getSelectionModel().getSelectedItem().toString()), valueEOY);
        } else if (chosenOption == 2){ // food
            game.buyFood(valueEOY);
        } else if (chosenOption == 3){ // pass
            // nothing lol
        }
        closeEndYearWindow();
        refreshPercentages();
    }

    public void closeEndYearWindow(){
        endYearAlert.setVisible(false);
        choiceEOY.setVisible(false);
        fieldEOY.setVisible(false);
        factionEOY.setVisible(false);
        errorMsgEOY.setVisible(false);
        submitEOY.setVisible(false);
        EOYLabel.setVisible(false);
        EOYChoiceLabel.setVisible(false);
    }



}