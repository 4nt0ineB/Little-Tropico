package dutinfo.game.environment;

import dutinfo.game.society.Faction;
import dutinfo.game.society.President;
import dutinfo.game.society.Field;

import java.util.List;

public class Island {

	private String name;
	private Season currentSeason;
	private President president;
	private List<Faction> factions;
	private List<Field> fields;
	private Double treasury;

	public Island(String name, President president, List<Faction> factions, List<Field> fields, double treasury){
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

	public President getPresident() {
		return president;
	}

	/**
	 * 
	 * @param treasury
	 */
	public void setTreasury(int treasury) {
		// TODO - implement Island.setTreasury
		throw new UnsupportedOperationException();
	}

	public Island() {
		// TODO - implement Island.Island
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "Island{" +
				"name='" + name + '\'' +
				", currentSeason=" + currentSeason +
				", president=" + president +
				", factions=" + factions +
				", fields=" + fields +
				", treasury=" + treasury +
				'}';
	}
}