package dut.info.console;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

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
        String factionsPath = "./src/main/resources/test.json";
        Set<Faction> factions = Faction.initFaction(factionsPath);

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
        return new Game();
    }

    private static void startGame(Game game){
        int x= 0;
    }

}
