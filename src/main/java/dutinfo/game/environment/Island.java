package dutinfo.game.environment;

import dutinfo.game.society.Faction;
import dutinfo.game.society.President;

import java.lang.reflect.Field;
import java.util.List;

public class Island {

	private String name;
	private Season currentSeason;
	private President president;
	private List<Faction> factions;
	private List<Field> fields;
	private Double treasury;


	public Island(String name, President president, List<Faction> factions, List<Field> fields, double treasury){
		this.president = president;
		this.factions = factions;
		this.fields = fields;
		this.treasury = treasury;
	}

	public Double getTreasury() {
		return treasury;
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

}