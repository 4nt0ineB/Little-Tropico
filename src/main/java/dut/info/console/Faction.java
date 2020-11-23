package dut.info.console;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Faction {

	private final int id;
	private final String name;
	private Double approbationPercentage;
	private int nbrSupporters;


	public Faction(int id, String name, double initApprobationPercentage, int nbrSupporters) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "Faction must have a name");
		approbationPercentage = initApprobationPercentage;
		this.nbrSupporters = nbrSupporters;
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
	 * @param factionsPath Path to faction json file
	 * */
	public static Set<Faction> initFaction(String factionsPath){
		Set<Faction> factions = new HashSet<>();
		Object jsonEvents = GameUtils.jsonToObject(factionsPath);
		JSONArray events = (JSONArray) jsonEvents;
		assert events != null;
		Object o1 = events.get(0);
		JSONObject ev1 = (JSONObject) o1;
		System.out.println(ev1.get("title"));
		return factions;
	}
}