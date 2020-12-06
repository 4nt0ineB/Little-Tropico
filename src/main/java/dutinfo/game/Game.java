package dutinfo.game;

import dutinfo.game.environment.Island;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Event;
import dutinfo.game.events.Scenario;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;
import dutinfo.game.society.President;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Game {

	private enum Difficulty{
		EASY(0.5), NORMAL(1), HARD(2);
		private double multiplier;
		Difficulty(double multiplier){
			this.multiplier = multiplier;
		}
	}

	private Island island;
	private Difficulty difficulty;
	private HashMap<Integer, List<Event>> events;
	private Event event;
	private Event nextEvents;
	List<Faction> factions;
	List<Field> fields;
	List<Scenario> scenarios;

	public Game(List<Faction> factions, List<Field> fields, List<Scenario> scenarios, HashMap<Integer, List<Event>> events){
		this.factions = factions;
		this.fields = fields;
		this.scenarios = scenarios;
		this.difficulty = Difficulty.NORMAL;
	}

	public void setDifficulty(Difficulty difficulty){
		this.difficulty = difficulty;
	}

	public void checkDefeat() {
		// TODO - implement Game.checkDefeat
		throw new UnsupportedOperationException();
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


	public static Game initGame(){

		/*   When making jar    */
		/*
		String pathToData = "";
		try{
			String jarPath = Game.class
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.toURI()
					.getPath();
			File file = new File(jarPath);
			pathToData = file.getParentFile().getPath();

			System.out.println();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		String pathToFactionsFile = pathToData+"factions.json";
		String pathToFieldsFile = pathToData+"fields.json";
		String pathToScenariosDir = pathToData+"scenarios";
		String pathToEventsDir = pathToData+"events";
		*/

		/* Paths */
		String pathToFactionsFile = ".\\src\\main\\resources\\factions.json";
		String pathToFieldsFile = ".\\src\\main\\resources\\fields.json";
		String pathToScenariosDir = ".\\src\\main\\resources\\scenarios";
		String pathToEventsDir = ".\\src\\main\\resources\\events\\";



		/* Init factions */
		List<Faction> factions = Faction.initFaction(pathToFactionsFile);

		/* Init fields */
		List<Field> fields = Field.initField(pathToFieldsFile);

		/* Init scenarios */
		List<Scenario> scenarios = Scenario.initScenarios(pathToScenariosDir);

		/* Init events from packages*/
		// <package id, event list>
		HashMap<Integer, List<Event>> events = Event.initEvents(pathToEventsDir);

		//System.out.println(events);

		return new Game(factions, fields, scenarios, events);
	}
}