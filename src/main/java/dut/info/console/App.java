package dut.info.console;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Tropico App main
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Game game = initGame();
        startGame(game);
    }

    private static Game initGame(){

        Game game = new Game();

        /* Paths */
        String pathToFactionsFile = ".\\src\\main\\resources\\factions.json";
        String pathToFieldsFile = ".\\src\\main\\resources\\fields.json";
        String pathToScenariosDir = ".\\src\\main\\resources\\scenarios\\";
        String pathToEventsDir = ".\\src\\main\\resources\\events\\";

        /* Init factions */
        List<Faction> factions = Faction.initFaction(pathToFactionsFile);
        game.setFactions(factions);

        /* Init fields */
        List<Field> fields = Field.initField(pathToFieldsFile);

        /* Init scenarios */
        List<Scenario> scenarios = Scenario.initScenarios(pathToScenariosDir);

        /* Init events from packages*/
        List<Event> events = Event.initEvents(pathToEventsDir);
        if(!events.isEmpty()){
            System.out.println(events.get(0).getActions());
        }

        return game;
    }

    private static void startGame(Game game){
        // To do.
        int x= 0;
    }

}
