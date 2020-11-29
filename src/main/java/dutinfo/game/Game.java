package dutinfo.game;

import dutinfo.game.environment.Island;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Event;
import dutinfo.game.society.Faction;
import dutinfo.game.society.President;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Game {

	private double difficulty;
	private Season season;
	private President president;
	private Island island;
	private Event events;
	private Event nextEvents;
	private List<Faction> factions;
	private List<Field> fields;

	public Game() {
	}

	public void checkDefeat() {
		// TODO - implement Game.checkDefeat
		throw new UnsupportedOperationException();
	}

	public President getPresident() {
		return this.president;
	}

	public Island getIsland() {
		return this.island;
	}

	public Event getEvents() {
		// TODO - implement Game.getEvents
		throw new UnsupportedOperationException();
	}

	public Event getNextEvents() {
		// TODO - implement Game.getNextEvents
		throw new UnsupportedOperationException();
	}

	public void addNextEvents() {
		// TODO - implement Game.addNextEvents
		throw new UnsupportedOperationException();
	}

    public void setFactions(List<Faction> factions) {
		this.factions = factions;
    }

    public Faction getFactionById(int id){
		return (Faction) factions.stream().filter(f -> f.getId() == id);
	}

	/**
	 * Master Control Program
	 * This nested class store all errors of the game from the init of the files to the end of the party.
	 * */
	public static class MCP{
		private static HashMap<String, Set<String>> failedEventsFiles;

		/**
		 * add all the data from parsing error in the field assigned for it in the class
		 * @param files all the files name by their Scenario name if the parsing failed
		 */
		public static void addFailedEventFile(HashMap<String, Set<String>> files){
			failedEventsFiles.putAll(files);
		}

		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			if(failedEventsFiles != null){
				sb.append("[Failed scenarios files to read]");
				failedEventsFiles.forEach((x, y) -> {
					sb.append("| Scenario\n");
					y.forEach(name -> sb.append("\t").append(name).append("\n"));
				});
			}
			return sb.toString();
		}

	}
}