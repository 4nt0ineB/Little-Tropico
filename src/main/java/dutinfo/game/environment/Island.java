package dutinfo.game.environment;

import dutinfo.game.society.Faction;
import dutinfo.game.society.President;
import dutinfo.game.society.Field;

import java.util.HashMap;
import java.util.List;

public class Island {

	private String name;
	private Season currentSeason;
	private President president;
	private List<Faction> factions;
	private List<Field> fields;
	private Double treasury;

	public Island(String name, President president, List<Faction> factions, List<Field> fields, double treasury) {
		this.name = name;
		this.currentSeason = Season.Spring;
		this.president = president;
		this.factions = factions;
		this.fields = fields;
		this.treasury = treasury;
	}

	public String getName() {
		return name;
	}

	public List<Faction> getFactions() {
		return factions;
	}

	public List<Field> getFields() {
		return fields;
	}

	public Double getTreasury() {
		return treasury;
	}

	public Season getSeason() {
		return currentSeason;
	}

	public void incrementSeason() {
		currentSeason = Season.getNext(currentSeason);
	}

	public President getPresident() {
		return president;
	}


	public void updateFactionValues(HashMap<Integer, Double> factionsValues){
		fields.stream().forEach(x -> {
			var w = factionsValues.get(x.getId());
			if(w != null){
				x.setExploitationPercentage(x.getExploitationPercentage()+w);
			}
		});
	}

	public void updateFieldValues(HashMap<Integer, Double> fieldsValues){
		factions.stream().forEach(x -> {
			var w = fieldsValues.get(x.getId());
			if(w != null){
				x.setApprobationPercentage(x.getApprobationPercentage()+w);
			}
		});
	}

	/**
	 * Set the treasury
	 *
	 * @param treasury
	 */
	public void setTreasury(double treasury) {
		this.treasury = treasury;
	}

	@Override
	public String toString() {
		return "Island{" + "name='" + name + '\'' + ", currentSeason=" + currentSeason + ", president=" + president
				+ ", factions=" + factions + ", fields=" + fields + ", treasury=" + treasury + '}';
	}
}