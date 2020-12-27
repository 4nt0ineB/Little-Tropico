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
    private final String description;
    private final double generalSatisfaction;
    private final HashMap<Integer, Double> facPercentage; //those indicated in "exceptions"
    private final int followers;
    private final HashMap<Integer, Double> filPercentage;
    private final double treasure;
    private final Set<Integer> eventPackIds;
    private List<Event> events;


    private Scenario(String description, String title, double generalSatisfaction, HashMap<Integer, Double> facPercentage, int followers, HashMap<Integer, Double> filPercentage, int treasure, Set<Integer> packageIds) {
        this.description = description;
        this.title = Objects.requireNonNull(title);
        assert title.isEmpty() != true;
        this.id = GameUtils.idByHashString(title);
        this.generalSatisfaction = generalSatisfaction;
        this.facPercentage = facPercentage;
        this.filPercentage = filPercentage;
        this.followers = followers;
        this.treasure = treasure;
        this.eventPackIds = packageIds;
    }

    /**
     * Get the init satisfaction of a faction from the scenario
     * If found in satisfaction exceptions return it, else return the general satisfaction value
     * @param facId id of the faction
     * @return
     */
    public double getSatisFaction(int facId){
        Double p = facPercentage.get(facId);
        if(p != null){
            return p;
        }
        return generalSatisfaction;
    }

    /**
     * Get the init exploitation percentage of a field from the scenario
     * Throw an error if the id is not found (meaning that its not set in the json of the scenario)
     * @param  fieldId id of the field
     * @return the percentage rate of exploitation
     */
    public double getExploitField(int fieldId){
        Double p = filPercentage.get(fieldId);
        if(p != null){
            return p;
        }
        throw new IllegalArgumentException();
    }


    public double getTreasure(){ return treasure; }

    public void setEvents(List<Event> events) { this.events = events; }

    public Set<Integer> getPackageIds(){ return eventPackIds; }

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

            //Description
            String description = (String) ((Object) ar.get("description"));

            //Satisfaction and exploitation
            JSONObject satisfaction = (JSONObject) ((Object) ar.get("satisfaction"));
            int generalSatisfaction = (int) (long) satisfaction.get("general");

            JSONArray factionException = (JSONArray) satisfaction.get("exceptions");
            HashMap<Integer, Double> facPercentage = new HashMap<>();

            factionException.forEach(fac -> {
                JSONObject exc = (JSONObject) fac;
                String name = (String) exc.get("name");
                assert (Faction.exist(name));
                facPercentage.put(
                        GameUtils.idByHashString(name),
                        (double) (long) exc.get("percentage")
                );
            });

            //Fields
            JSONArray fields = (JSONArray) ar.get("fields");
            HashMap<Integer, Double> filPercentage = new HashMap<>();
            fields.forEach(fi -> {
                JSONObject exc = (JSONObject) fi;
                filPercentage.put(
                        GameUtils.idByHashString((String) exc.get("name")),
                        (double) (long) exc.get("percentage")
                );
            });

            //Followers
            int followers = (int) (long) ar.get("followers");
            //Treasure
            int treasure = (int) (long) ar.get("treasure");

            //Event packages
            Set<Integer> packageIds = new HashSet<>();
            packageIds.add(GameUtils.idByHashString("Commons")); // <---- Very important : add the common event package (present in each scenario)
            JSONArray pNames = (JSONArray) ar.get("event_packages");
            pNames.forEach(x->packageIds.add(GameUtils.idByHashString((String) x)));

            scenarios.add(new Scenario(description, title, generalSatisfaction, facPercentage, followers, filPercentage, treasure, packageIds));
        } );
        return scenarios;
    }

    @Override
    public String toString() {
        return "__________________\n|"+title+"|"+description+"\n";
    }

    public String datatoString(){
        String str = "\nScenario " + title + "\n";
        str += "General satisfaction : "+generalSatisfaction+ "\n";
        str += "Followers by factions : "+followers+"\n";
        str += "Starting treasure : "+treasure+"\n";
        return str;
    }
}
