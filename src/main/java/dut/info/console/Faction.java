package dut.info.console;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Faction {

	private final int id;
	private final String name;
	private Double approbationPercentage;
	private int nbrSupporters;


	public Faction(String name) {
		this.name = Objects.requireNonNull(name, "Faction must have a name");
		this.id = GameUtils.idByHashString(name);
		approbationPercentage = 0.0d;
		this.nbrSupporters = 0;
	}

	public int getId() {
		return id;
	}

	/**
	 * Set the approbation percentage rate
	 * @param approbationPercentage Approbation percentage
	 */
	public void setApprobationPercentage(Double approbationPercentage) {
		this.approbationPercentage = approbationPercentage;
	}

	/**
	 * Get the approbation percentage rate
	 */
	public Double getApprobationPercentage() {
		return this.approbationPercentage;
	}

	/**
	 * Factory returning Set of factions
     * @param pathToFactionsFile Path to faction json file
     * @return*/
	public static List<Faction> initFaction(String pathToFactionsFile){
		List<Faction> factions = new ArrayList<>();
		Object jsonEvents = GameUtils.jsonToObject(pathToFactionsFile);
		JSONArray events = (JSONArray) jsonEvents;
		assert events != null;
		Object o1 = events.get(0);
		JSONObject ev1 = (JSONObject) o1;
		JSONArray factionNames = (JSONArray) ev1.get("names");
		factionNames.forEach(x -> { factions.add(new Faction((String) x)); });
		return factions;
	}
}