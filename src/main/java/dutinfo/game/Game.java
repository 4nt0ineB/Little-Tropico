package dutinfo.game;

import dutinfo.console.App;
import dutinfo.game.environment.Island;
import dutinfo.game.events.Event;
import dutinfo.game.events.Scenario;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;
import dutinfo.console.App.Color;


import java.util.*;

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
	private List<Event> nextEvents;
	private Event event;
	private Scenario scenario;

	private static List<Faction> FACTIONS;
	private static List<Field> FIELDS;
	private static List<Scenario> SCENARIOS;

	public Game(List<Faction> factions, List<Field> fields, List<Scenario> scenarios, HashMap<Integer, List<Event>> events){
		this.FACTIONS = factions;
		this.events = events;
		this.FIELDS = fields;
		this.SCENARIOS = scenarios;
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

	public void setIsland(Island island){
		this.island = island;
	}

	public void setScenario(Scenario scenario) { this.scenario = scenario; }

	public Scenario getScenario() { return this.scenario; }

	public List<Scenario> getScenarios(){
		return SCENARIOS;
	}

	public List<Faction> getFactions(){
		return FACTIONS;
	}

	public List<Field> getFields(){
		return FIELDS;
	}

	public double getTreasure(int scenario){
		List<Scenario> scenarios = getScenarios();
		Scenario selectedScenario = scenarios.get(scenario);

		return selectedScenario.getTreasure();
	}

	/**
	 * get all the events by the current scenario and the common ones
	 * @param scenario we use the current scenario to use this function
	 */
	public List<Event> getEvents(Scenario scenario) {
		List<Event> eventsList = new ArrayList<>();
		events.keySet().forEach(
				x -> {
					if(scenario.getPackageIds().contains(x)){
						eventsList.addAll(events.get(x));
					} });

		return eventsList;
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
		this.FACTIONS = factions;
    }

    public Faction getFactionById(int id){
		return (Faction) FACTIONS.stream().filter(f -> f.getId() == id);
	}

	public String printStats(){
		StringBuilder sb = new StringBuilder();

		sb.append("\n\n"+Color.ANSI_WHITE_BACKGROUND+""+Color.ANSI_BLACK+"---- End of the day : Stats ----\n");
		sb.append(Color.ANSI_RESET+""+Color.ANSI_ITALIC+"Treasury : "+Color.ANSI_RESET+Color.ANSI_GREEN+"$"+getIsland().getTreasury());
		sb.append(Color.ANSI_RESET+""+Color.ANSI_ITALIC+"\nCurrent season : "+Color.ANSI_RESET+getIsland().getSeason());
		sb.append(Color.ANSI_ITALIC+"\nFactions satisfaction : ");
		getIsland().getFactions().stream().forEach(x -> {
			sb.append("\n"+Color.ANSI_RESET+ Color.ANSI_RED+x.getName()+": "+Color.ANSI_BOLD+Color.ANSI_WHITE+x.getApprobationPercentage()+"%");
		});

		return sb.toString();
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

	/**
	 * Parse all json files needed for the game
	 * @return a Game object
	 */
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
			pathToData = file.getParentFile().getPath()+"\\ress\\";


		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		String pathToFactionsFile = pathToData+"factions.json";
		String pathToFieldsFile = pathToData+"fields.json";
		String pathToScenariosDir = pathToData+"scenarios\\";
		String pathToEventsDir = pathToData+"events\\";
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
		Game game = new Game(factions, fields, scenarios, events);

		return game;
	}
}