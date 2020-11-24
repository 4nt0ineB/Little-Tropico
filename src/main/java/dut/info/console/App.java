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

        /* Paths */
        String pathToScenarios = ".\\src\\main\\resources\\scenarios\\";


        /* Init events and Scenarios*/
        List<Event> events = Event.initEvents(pathToScenarios);


        /**
        JSONParser jsonTest = new JSONParser();
        String test = new File("./src/main/resources/test.json").getAbsolutePath();
        try(FileReader filereader = new FileReader(test)){
            Object jsonEvents = jsonTest.parse(filereader);
            JSONArray events = (JSONArray) jsonEvents;
            Object o1 = events.get(0);
            JSONObject ev1 = (JSONObject) o1;
            System.out.println(ev1.get("title"));

        } catch (IOException | ParseException e){
            e.printStackTrace();
        }
         */
        return new Game();
    }

    private static void startGame(Game game){
        // To do.
        int x= 0;
    }

}
