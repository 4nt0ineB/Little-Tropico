package dutinfo.console;

import dutinfo.game.*;
import dutinfo.game.events.Event;
import dutinfo.game.events.Scenario;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;

import java.util.*;

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
        // <package id, event list>
        HashMap<Integer, List<Event>> events = Event.initEvents(pathToEventsDir);

        if(!events.isEmpty()){
            System.out.println(events);
        }

        
        return game;
    }

    private static void startGame(Game game){
        // To do.
        int x= 0;
    }

}
