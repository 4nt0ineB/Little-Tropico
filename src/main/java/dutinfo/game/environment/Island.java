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
	private int foodUnits;

	public Island(String name, President president, List<Faction> factions, List<Field> fields, double treasury) {
		this.name = name;
		this.currentSeason = Season.Spring;
		this.president = president;
		this.factions = factions;
		this.fields = fields;
		this.treasury = treasury;
		foodUnits = 0;
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

	public void updateTreasure(double amount){
		treasury+=amount;
	}

	public void updateFoodUnits(int amount){
		foodUnits+=amount;
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



	public void updateFactions(HashMap<Integer, Double[]> factionsValues){
		factions.stream().forEach(x -> {
			var w = factionsValues.get(x.getId());
			if(w != null){
				x.setApprobationPercentage(x.getApprobationPercentage()+w[0]);
				x.setNbrSupporters(x.getNbrSupporters()+w[1].intValue());
			}
		});
	}

	public void updateFields(HashMap<Integer, Double> fieldsValues){
		fields.stream().forEach(x -> {
			var w = fieldsValues.get(x.getId());

			if(w != null){
				x.setExploitationPercentage(x.getExploitationPercentage()+w);
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

	public int totalSupporters(){

		return factions.stream().mapToInt(x -> x.getNbrSupporters()).sum();
	}

	public double globalSatisfaction(){
		System.out.println(totalSupporters());
		int totalsupp = totalSupporters();
		return (totalsupp != 0)? factions.stream().mapToDouble(x -> x.getNbrSupporters()*x.getApprobationPercentage()).sum()/totalsupp : 0;

	}

	@Override
	public String toString() {
		return "Island{" + "name='" + name + '\'' + ", currentSeason=" + currentSeason + ", president=" + president
				+ ", factions=" + factions + ", fields=" + fields + ", treasury=" + treasury + '}';
	}
}