package dutinfo.game.events;

import dutinfo.game.GameUtils;
import dutinfo.game.environment.Season;
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
    private final float generalSatisfaction;
    private final HashMap<Integer, Float> facPercentage; // those indicated in "exceptions"
    private final int followers;
    private final HashMap<Integer, Float[]> filPercentage;
    private final float treasure;

    private final Set<Integer> eventPackIds;
    private List<Event> events;

    private Scenario(String description, String title, float generalSatisfaction,
                     HashMap<Integer, Float> facPercentage, int followers, HashMap<Integer, Float[]> filPercentage, int treasure,
                     Set<Integer> packageIds) {
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
     * Get a random event from the wanted season
     *
     * @param season
     * @return
     */
    public Event getRandomEventOnSeason(Season season) {
        Objects.requireNonNull(season, "Must give a proper season to get an event depending on season.");
        return events.parallelStream()
                .filter(x -> x.getSeason() == season || x.getSeason() == null && !x.isOnlyARepercussion()).findFirst()
                .get();

        /*
         * VS Collections.shuffle(events); Event ev = null; for (Event e: events ) {
         * if(e.getSeason() == season){ ev = e; } } return ev;
         */
    }

    /**
     * Get a random event
     *
     * @return
     */
    public Event getRandomEvent() {
        Collections.shuffle(events);
        return events.parallelStream().filter(x -> !x.isOnlyARepercussion()).findFirst().get();
    }

    /**
     * Get the init satisfaction of a faction from the scenario If found in the
     * exceptions value of satisfaction return it, else return the general
     * satisfaction value
     *
     * @param facId id of the faction
     * @return
     */
    public float getSatisFaction(int facId) {
        Float p = facPercentage.get(facId);
        if (p != null) {
            return p;
        }
        return generalSatisfaction;
    }

    /**
     * Get the init exploitation percentage of a field from the scenario Throw an
     * error if the id is not found (meaning that its not set in the json of the
     * scenario)
     *
     * @param fieldId id of the field
     * @return the percentage rate of exploitation
     */
    public float getExploitField(int fieldId) {
        Float p = filPercentage.get(fieldId)[0];
        if (p != null) {
            return p;
        }
        throw new IllegalArgumentException("field doesn't exist");
    }

    public float getFieldYieldPercentage(int fieldId) {
        Float p = filPercentage.get(fieldId)[1];
        if (p != null) {
            return p;
        }
        throw new IllegalArgumentException("field doesn't exist");
    }



    public float getTreasure() {
        return treasure;
    }

    public int getFollowers(){ return followers; }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Set<Integer> getPackageIds() {
        return eventPackIds;
    }

    /**
     * Return the list of all scenarios objects
     *
     * @param pathToScenarioDir Path to the scenarios directory
     */
    public static List<Scenario> initScenarios(String pathToScenarioDir) {

        // All scenarios json
        List<File> scenariosJson = GameUtils.allJsonFromDir(new File(pathToScenarioDir));
        // All scenarios
        List<Scenario> scenarios = new ArrayList<>();

        // All files failed to parse for each scenario
        Set<String> failedFiles = new HashSet<>();

        scenariosJson.stream().forEach(f -> {
            // Global array of the file
            JSONObject ar = (JSONObject) ((Object) GameUtils.jsonToObject(f.getPath()));

            // Title
            String title = f.getName().substring(0, f.getName().lastIndexOf("."));

            // Description
            String description = (String) ((Object) ar.get("description"));

            // Satisfaction and exploitation
            JSONObject satisfaction = (JSONObject) ((Object) ar.get("satisfaction"));
            int generalSatisfaction = (int) (long) satisfaction.get("general");

            JSONArray factionException = (JSONArray) satisfaction.get("exceptions");
            HashMap<Integer, Float> facPercentage = new HashMap<>();

            factionException.forEach(fac -> {
                JSONObject exc = (JSONObject) fac;
                String name = (String) exc.get("name");
                assert (Faction.exist(name));
                facPercentage.put(GameUtils.idByHashString(name), (float) (long) exc.get("percentage"));
            });

            // Fields
            JSONArray fields = (JSONArray) ar.get("fields");
            HashMap<Integer, Float[]> filPercentage = new HashMap<>();
            fields.forEach(fi -> {
                JSONObject exc = (JSONObject) fi;

                Float[] vals = new Float[2];
                int yieldPercent;
                try{
                    vals[0] = (float) (long) exc.get("percentage");
                }catch (Exception e){
                    vals[0] = (float) generalSatisfaction;
                }
                try{
                    vals[1] = ((float) (long) exc.get("yield"));
                }catch (Exception e){
                    vals[1] = 0f;
                }
                filPercentage.put(GameUtils.idByHashString((String) exc.get("name")),vals);
            });

            // Followers
            int followers = (int) (long) ar.get("followers");
            // Treasure
            int treasure = (int) (long) ar.get("treasure");

            // Event packages
            Set<Integer> packageIds = new HashSet<>();
            packageIds.add(GameUtils.idByHashString("Commons")); // <---- Very important : add the common event package
            // (present in each scenario)
            JSONArray pNames = (JSONArray) ar.get("event_packages");
            pNames.forEach(x -> packageIds.add(GameUtils.idByHashString((String) x)));

            scenarios.add(new Scenario(description, title, generalSatisfaction, facPercentage, followers, filPercentage,
                    treasure, packageIds));
        });
        return scenarios;
    }

    @Override
    public String toString() {
        return title + "|" + description + "\n";
    }

    public String datatoString() {
        String str = "\nScenario " + title + "\n";
        str += "General satisfaction : " + generalSatisfaction + "\n";
        str += "Followers by factions : " + followers + "\n";
        str += "Starting treasure : " + treasure + "\n";
        return str;
    }
}
