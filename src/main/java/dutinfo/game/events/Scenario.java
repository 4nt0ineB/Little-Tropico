package dutinfo.game.events;

import dutinfo.game.GameUtils;
import dutinfo.game.society.Faction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;


public class Scenario {

    private final int id;
    private final String title;
    private final int generalSatisfaction;
    private final HashMap<Integer, Integer> facPercentage;
    private final int followers;
    private final HashMap<Integer, Integer> filPercentage;
    private final int treasure;
    private final Set<Integer> eventPackIds;


    private Scenario(String title, int generalSatisfaction, HashMap<Integer, Integer> facPercentage, int followers, HashMap<Integer, Integer> filPercentage, int treasure, Set<Integer> packageIds) {
        this.title = Objects.requireNonNull(title);
        assert title.isEmpty() != true;
        id = GameUtils.idByHashString(title);
        this.generalSatisfaction = generalSatisfaction;
        this.facPercentage = facPercentage;
        this.filPercentage = filPercentage;
        this.followers = followers;
        this.treasure = treasure;
        this.eventPackIds = packageIds;
    }

    /** Return the list of all scenarios objects
     * @param pathToScenarioDir Path to the scenarios directory
     * */
    public static List<Scenario> initScenarios(String pathToScenarioDir){


        // All scenarios json
        List<File> scenariosJson = GameUtils.allJsonFromDir(new File(pathToScenarioDir));
        // All scenarios
        List<Scenario> scenarios = new ArrayList<>();

        // All files failed to parse for each scenario
        Set<String> failedFiles = new HashSet<>();

        scenariosJson.stream().forEach(f -> {
            // Global array of the file
            JSONObject ar = (JSONObject) ((Object) GameUtils.jsonToObject(f.getPath()));

            //Title
            String title = f.getName().substring(0, f.getName().lastIndexOf("."));


            //Satisfaction and exploitation
            JSONObject satisfaction = (JSONObject) ((Object) ar.get("satisfaction"));
            int generalSatisfaction = (int) (long) satisfaction.get("general");
            JSONArray factionException = (JSONArray) satisfaction.get("exceptions");
            HashMap<Integer, Integer> facPercentage = new HashMap<>();

            factionException.forEach(fac -> {
                JSONObject exc = (JSONObject) fac;
                String name = (String) exc.get("name");
                assert (Faction.exist(name));
                facPercentage.put(
                        GameUtils.idByHashString(name),
                        (int) (long) exc.get("percentage")
                );
            });

            //Followers
            int followers = (int) (long) ar.get("followers");

            //Fields
            JSONArray fields = (JSONArray) ar.get("fields");
            HashMap<Integer, Integer> filPercentage = new HashMap<>();
            fields.forEach(fi -> {
                JSONObject exc = (JSONObject) fi;
                filPercentage.put(
                        GameUtils.idByHashString((String) exc.get("name")),
                        (int) (long) exc.get("percentage")
                );
            });
            //Treasure
            int treasure = (int) (long) ar.get("treasure");

            //Event packages
            Set<Integer> packageIds = new HashSet<>();
            packageIds.add(GameUtils.idByHashString("Commons")); // <---- Very important : add the common event package (present in each scenario)
            JSONArray pNames = (JSONArray) ar.get("event_packages");
            pNames.forEach(x->packageIds.add(GameUtils.idByHashString((String) x)));

            scenarios.add(new Scenario(title, generalSatisfaction, facPercentage, followers, filPercentage, treasure, packageIds));
        } );
        return scenarios;
    }

    @Override
    public String toString(){
        String str = "\nScenario " + title + "\n";
        str += "General satisfaction : "+generalSatisfaction+ "\n";
        str += "Followers by factions : "+followers+"\n";
        str += "Starting treasure : "+treasure+"\n";
        return str;
    }
}
