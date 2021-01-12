package dutinfo.javafx.controllers;

import dutinfo.game.Game;
import dutinfo.game.GameUtils;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Action;
import dutinfo.game.events.Event;
import dutinfo.game.society.Faction;
import dutinfo.game.society.President;
import dutinfo.javafx.views.App;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import dutinfo.javafx.models.Model;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class mainController implements EventHandler<MouseEvent> {

    public static Scene menu;

    private static Game game;

    private Model appModel = new Model();

    @FXML
    private ImageView sun, vein, sweat, stars;

    @FXML
    private Label globalSatisfac, moneyAmount, EOYLabel, infoLabel, EOYChoiceLabel, errorMsgEOY, citizensCount, foodAmount, season, day, presidente, eventLabel, industryPercentage, farmingPercentage, capitalistsPercentage, communistsPercentage, liberalistsPercentage, religiousPercentage, militaristsPercentage, ecologistsPercentage, nationalistsPercentage, loyalistsPercentage, islandName;

    @FXML
    private Rectangle eventAlert, endYearAlert, infoAlert, EOGAlert, rectSeason;

    @FXML
    private ComboBox eventChoice, choiceEOY, factionEOY;

    @FXML
    private Button selectEvent, submitEOY, menuBtn, infoBtn, buttonPassEOYAction, EOGClose;

    @FXML
    private Text eventDescription, eventEffects, eventEffects2, infoContent, msgEndGame, EOGTitle;

    @FXML
    private Line eventLine, eventSeparator;

    @FXML
    private TextField fieldEOY;

    private Event currentEvent;
    private int currentActionId;
    private int chosenOption = 3;
    private int valueEOY;
    private boolean onGoingEvent;
    private boolean onGoingEOY;
    private boolean onGoingEOYInfo;
    private boolean onGoingEndOfGame;



    private long firstTime;
    private AnimationTimer timer;

    MediaPlayer player;
    MediaPlayer player2;

    Media media = new Media(getClass().getClassLoader().getResource("italianmusic-cut.mp3").toString());
    Media media2 = new Media(getClass().getClassLoader().getResource("beachambiance-cut.mp3").toString());
    Media SoundClick = new Media(getClass().getClassLoader().getResource("click.mp3").toString());
    Media SoundEOY = new Media(getClass().getClassLoader().getResource("eoy-sfx.mp3").toString());
    Media SoundReview = new Media(getClass().getClassLoader().getResource("pop-elec.mp3").toString());
    Media SoundLose = new Media(getClass().getClassLoader().getResource("lose.mp3").toString());

    MediaPlayer ephemeralSound;




    @Override
    public void handle(MouseEvent mouseEvent) {

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
        menuBtn.setStyle("-fx-background-color: #7a0000; -fx-text-fill: white;");
        infoBtn.setStyle("-fx-background-color: #7a0000; -fx-text-fill: white;");
        rectSeason.setStyle("-fx-border-radius: 10px;");


        // Hide president anger
        vein.setVisible(false);
        sweat.setVisible(false);

        // Hide end of year and recap by default
        closeEndYearWindow();
        closeInfoWindow();
        closeGameOver();
        onGoingEOYInfo = false;
        onGoingEndOfGame = false;


        // la musica
        player = new MediaPlayer(media);
        player2 = new MediaPlayer(media2);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player2.setAutoPlay(true);
        player2.setCycleCount(MediaPlayer.INDEFINITE);

        passTurn();
        updateAll();
        timer = new AnimationTimer() { //////////////  GAME LOOP


            private long lastUpdate = 0 ;

            @Override
            public void start() {
                super.start();


            }

            @Override
            public void handle(long l) {

                if(l - lastUpdate >= 1_500_000_000L){ // only consider events each 1.5 seconds
                    lastUpdate = l;
                    if(!onGoingEvent && !onGoingEOY && !onGoingEOYInfo && !onGoingEndOfGame){
                        Event ev = game.getEvent();
                        if(ev != null){
                            openEventWindow(ev); // get random event for the current season
                        }else{
                            closeEventWindow();
                            passTurn(); // pass the turn
                            updateAll();
                        }
                    }

                    if(game.getIsland().getSeason() == Season.Winter && !onGoingEndOfGame) {
                        if(!onGoingEOY && !onGoingEOYInfo){
                            openEndYearWindow();
                        }
                        
                    }
                }
            }
            
            @Override
            public void stop() {
                super.stop();
                player.stop();
                player2.stop();
            }
        };

        timer.start();

    }

    public void updateAll(){
        sweat.setVisible(false);
        season.setText(""+game.getIsland().getSeason()); // season
        day.setText("Year "+game.getIsland().getNbPastYears());

        islandName.setText(game.getIsland().getName());
        presidente.setText(game.getIsland().getPresident().getName());
        moneyAmount.setText("$"+game.getIsland().getTreasury()); // Treasury
        foodAmount.setText(game.getIsland().getFoodUnits()+"");
        citizensCount.setText(game.getIsland().totalSupporters()+"");
        globalSatisfac.setText(""+game.getIsland().globalSatisfaction()+"%");

        /* PERCENTAGES */
        refreshPercentages();
    }

    /**
     * Function that permits choice to be submitted to the MCP and launched when click on "Select" button during an event choice
     */
    public void submitEventChoice(){

        ephemeralSound = new MediaPlayer(SoundClick);
        ephemeralSound.play();

        currentActionId = GameUtils.idByHashString(eventChoice.getSelectionModel().getSelectedItem().toString());

        game.playAction(currentEvent.getActions().stream().filter(x -> GameUtils.idByHashString(x.getTitle()) == currentActionId).findFirst().get());

        // test: show president anger if he lose pts
        sweat.setVisible(true);
        /* Close event window by setting it to false */
        closeEventWindow();

        refreshPercentages(); // refresh to show new percentages
        closeEventWindow();
        passTurn(); // pass the turn
        updateAll();
    }

    /**
     * Replace the scene of the game window by the menu
     */
    public void toMenu(){
        ephemeralSound = new MediaPlayer(SoundClick);
        ephemeralSound.play();
        timer.stop(); // Stop the sound and the game loop
        try{
            GridPane root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            root.setStyle("-fx-background-image: url('https://i.imgur.com/Y1uRiCV.jpg')");
            Scene scene = new Scene(root, 1400, 788);
            App.mstage.setScene(scene);
            App.mstage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
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
        onGoingEvent = false;
    }

    public void closeInfoWindow(){
        ephemeralSound = new MediaPlayer(SoundClick);
        ephemeralSound.play();

        infoAlert.setVisible(false);
        infoBtn.setVisible(false);
        infoContent.setVisible(false);
        infoLabel.setVisible(false);
        onGoingEOYInfo = false;
        passTurn(); // pass the turn
    }

    public void openInfoWindow(){
        ephemeralSound = new MediaPlayer(SoundReview);
        ephemeralSound.play();

        closeEventWindow();
        onGoingEOYInfo = true;
        infoAlert.setVisible(true);
        infoBtn.setVisible(true);
        infoContent.setVisible(true);
        infoLabel.setVisible(true);

        String updateResInfo = game.updateResourcesEndofYear();
        System.out.println(updateResInfo);
        updateAll();
        if (updateResInfo.isEmpty()){ // if string is empty - no major incident
            infoContent.setText("Nothing really important to report on this year.");
        } else {
            infoContent.setText(updateResInfo);
        }
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
        onGoingEvent = true;
        this.currentEvent = event;
        eventDescription.setText(event.getTitle());

        eventChoice.getSelectionModel().clearSelection();
        eventChoice.getItems().clear();

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
                    if(newValue != null){
                        Action temp = currentEvent.getActions().stream().filter(x -> GameUtils.idByHashString(x.getTitle()) == GameUtils.idByHashString(newValue)).findFirst().get();
                        eventEffects.setText(temp.getEffectsPercentagesToString()); // change label to factions effects
                        eventEffects2.setText(temp.getEffectsToString());
                    }
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

        ephemeralSound = new MediaPlayer(SoundEOY);
        ephemeralSound.play();

        onGoingEOY = true;

        endYearAlert.setVisible(true);
        choiceEOY.setVisible(true);
        fieldEOY.setVisible(false);
        factionEOY.setVisible(false);
        errorMsgEOY.setVisible(false);
        submitEOY.setDisable(false);
        submitEOY.setVisible(true);
        EOYLabel.setVisible(true);
        EOYChoiceLabel.setVisible(true);
        buttonPassEOYAction.setVisible(true);

        choiceEOY.getSelectionModel().clearSelection();
        choiceEOY.getItems().clear();

        choiceEOY.getItems().add("1. Buy some food $8 by unit.");
        choiceEOY.getItems().add("2. Bribe a Faction (+10% satisfaction for $15 by supporter).");

        // Select the last one - pass


        choiceEOY.valueProperty().addListener(new ChangeListener<String>() { // change effects when changing combobox value
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                if(newValue != null) {


                    if (newValue.startsWith("2")) { // first option
                        chosenOption = 1;
                        fieldEOY.clear();
                        fieldEOY.setVisible(true);
                        fieldEOY.setPromptText("Amount of 10% (5 -> 5 * 10%)");
                        factionEOY.setVisible(true);
                        errorMsgEOY.setVisible(false);

                        for (Faction fac : game.getFactions()) {
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


                    } else if (newValue.startsWith("1")) { // second option
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
                    }
                }
            }
        });

        choiceEOY.getSelectionModel().selectFirst();
        fieldEOY.setVisible(true);
    }

    public void submitEOYChoice(){

        ephemeralSound = new MediaPlayer(SoundClick);
        ephemeralSound.play();

        if (chosenOption != 3 || chosenOption == 0) {
            valueEOY = Integer.parseInt(fieldEOY.getText());
        }

        if (chosenOption == 1){ // faction bribe
            game.getIsland().bribeFaction(GameUtils.idByHashString(factionEOY.getSelectionModel().getSelectedItem().toString()), valueEOY);
        } else if (chosenOption == 2){ // food
            game.buyFood(valueEOY);
        }
        updateAll();
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
        buttonPassEOYAction.setVisible(false);
        openInfoWindow();
        onGoingEOY = false;
        onGoingEOYInfo = true;

    }

    private void passTurn(){
        if(!game.nextTurn()){
            showGameOver();
        }
    }

    private void showGameOver(){
        stars.setVisible(false);
        player.stop();
        player2.stop();
        ephemeralSound = new MediaPlayer(SoundLose);
        ephemeralSound.play();
        vein.setVisible(true);

        onGoingEndOfGame = true;
        EOGAlert.setVisible(true);
        EOGTitle.setVisible(true);
        msgEndGame.setText("Too bad, the level of satisfaction of the population has passed the critical stage and you have been dismissed by the citizens of "+game.getIsland().getName()+"!");
        msgEndGame.setVisible(true);
        EOGClose.setVisible(true);
    }
    private void closeGameOver(){
        EOGAlert.setVisible(false);
        EOGTitle.setVisible(false);
        msgEndGame.setVisible(false);
        EOGClose.setVisible(false);
        onGoingEndOfGame = false;
    }



}