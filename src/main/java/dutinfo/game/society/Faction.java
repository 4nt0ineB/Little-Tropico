package dutinfo.game.society;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dutinfo.game.GameUtils;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class Faction {

	private static Set<Faction> listFactions = new HashSet<>();

	private final int id;
	private final String name;
	private float approbationPercentage;
	private int nbrSupporters;



	public Faction(String name) {
		this.name = Objects.requireNonNull(name, "Faction must have a name");
		id = GameUtils.idByHashString(name);
		approbationPercentage = 0;
		nbrSupporters = 0;
		addFaction(this);

	}

	public int getNbrSupporters(){
		return nbrSupporters;
	}

	public void setNbrSupporters(int nbrSupporters) {
		if(nbrSupporters <= 0){
			this.nbrSupporters = 0;
		}else{
			this.nbrSupporters = nbrSupporters;
		}

	}

	private static void addFaction(Faction faction){
		listFactions.add(faction);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Faction faction = (Faction) o;
		return id == faction.id;
	}

	public static boolean exist(String n){
		return listFactions.stream().anyMatch(x -> x.getId() == GameUtils.idByHashString(n));
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	/**
	 * Set the approbation percentage rate
	 * @param approbationPercentage Approbation percentage
	 */
	public void setApprobationPercentage(float approbationPercentage) {
		if (GameUtils.round(approbationPercentage, 1) == 0.0){
			;
		}else if(approbationPercentage >= 100){
				this.approbationPercentage = 100;
			}else if(approbationPercentage <= 0){
				this.approbationPercentage = 0;
			}else{
				this.approbationPercentage = approbationPercentage;
			}
	}

	/**
	 * Get the approbation percentage rate
	 */
	public float getApprobationPercentage() {
		return this.approbationPercentage;
	}

	/**
	 * Factory returning Set of factions
     * @param pathToFactionsFile Path to faction json file
     * @return*/
	public static List<Faction> initFaction(String pathToFactionsFile){

     	try{
			List<Faction> factions = new ArrayList<>();
			String jsonString = GameUtils.getResourceFileAsString(pathToFactionsFile);
			JsonObject convertedObject = new Gson().fromJson(jsonString, JsonObject.class);
			JsonArray array = convertedObject.getAsJsonArray("names");
			array.forEach(x -> {
				factions.add(new Faction(x.getAsString()));
			});

			return factions;
		}catch (Exception e){
			System.out.println(e);
		}
     	return null;
	}

	@Override
	public String toString(){
     	return name;
	}
}