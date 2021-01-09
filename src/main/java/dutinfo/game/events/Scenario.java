package dutinfo.game.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dutinfo.game.GameUtils;
import dutinfo.game.environment.Season;
import dutinfo.game.society.Faction;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    public Event getEventById(int id){
        try{
            return events.parallelStream().filter(x -> x.getId() == id).findFirst().get();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Get a random event
     *
     * @return
     */
    public Event getRandomEvent() {
        List<Event> evl = events.parallelStream().filter(x -> !x.isOnlyARepercussion()).collect(Collectors.toList());
        Random rand = new Random();
        return evl.get(rand.nextInt(((events.size()-1) - 0) + 1) + 0);
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
            JsonObject ar = GameUtils.jsonToObject(f.getPath());

            // Title
            String title = f.getName().substring(0, f.getName().lastIndexOf("."));

            // Description
            String description = ar.get("description").getAsString();

            // Satisfaction and exploitation
            JsonObject satisfaction = ar.getAsJsonObject("satisfaction");
            int generalSatisfaction = satisfaction.get("general").getAsInt();

            JsonArray factionException = satisfaction.getAsJsonArray("exceptions");
            HashMap<Integer, Float> facPercentage = new HashMap<>();

            factionException.forEach(fac -> {
                JsonObject exc = fac.getAsJsonObject();
                String name = exc.get("name").getAsString();
                assert (Faction.exist(name));
                facPercentage.put(GameUtils.idByHashString(name), exc.get("percentage").getAsFloat());
            });

            // Fields
            JsonArray fields = ar.getAsJsonArray("fields");
            HashMap<Integer, Float[]> filPercentage = new HashMap<>();
            fields.forEach(fi -> {
                JsonObject exc = fi.getAsJsonObject();

                Float[] vals = new Float[2];
                int yieldPercent;
                try{
                    vals[0] = exc.get("percentage").getAsFloat();
                }catch (Exception e){
                    vals[0] = (float) generalSatisfaction;
                }
                try{
                    vals[1] = exc.get("yield").getAsFloat();
                }catch (Exception e){
                    vals[1] = 0f;
                }
                filPercentage.put(GameUtils.idByHashString(exc.get("name").getAsString()),vals);
            });

            // Followers
            int followers = ar.get("followers").getAsInt();
            // Treasure
            int treasure = ar.get("treasure").getAsInt();

            // Event packages
            Set<Integer> packageIds = new HashSet<>();
            packageIds.add(GameUtils.idByHashString("Commons")); // <---- Very important : add the common event package
            // (present in each scenario)
            JsonArray pNames = ar.getAsJsonArray("event_packages");
            pNames.forEach(x -> packageIds.add(GameUtils.idByHashString(x.getAsString())));

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
