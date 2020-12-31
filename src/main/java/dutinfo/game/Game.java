package dutinfo.game;

import dutinfo.console.App;
import dutinfo.game.environment.Island;
import dutinfo.game.environment.Season;
import dutinfo.game.events.Action;
import dutinfo.game.events.Event;
import dutinfo.game.events.Scenario;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;
import dutinfo.console.App.Color;
import dutinfo.game.society.President;

import java.io.InputStream;
import java.util.*;

public class Game {

	public enum Difficulty {
		EASY(0.5f, 10f), NORMAL(1f, 20f), HARD(2f, 50f);

		private float multiplier;
		private float minGlobalSatisfaction;

		Difficulty(float multiplier, float minGlobalSatisfaction) {
			this.multiplier = multiplier;
			this.minGlobalSatisfaction = minGlobalSatisfaction;
		}

		public double getMinGlobalSatisfaction(){
			return minGlobalSatisfaction;
		}
	}

	private Island island;
	private Difficulty difficulty;
	private HashMap<Integer, List<Event>> events;
	/*
	 * The events stack (next ev to pick if correspond to season) <------< (last ev
	 * append to stack)
	 */
	private List<Event> nextEvents; // repercussions
	private Event event; // Current / last event
	private Scenario scenario; // Current scenario

	private static List<Faction> FACTIONS; // All factions in init file
	private static List<Field> FIELDS; // All field in init file
	private static List<Scenario> SCENARIOS; // All Scenarios in folders
	private static final float monthlyPriceForDept = 0.3f; // on total value

	public Game(List<Faction> factions, List<Field> fields, List<Scenario> scenarios,
				HashMap<Integer, List<Event>> events) {
		this.FACTIONS = factions;
		this.events = events;
		this.FIELDS = fields;
		this.SCENARIOS = scenarios;
		this.difficulty = Difficulty.NORMAL;
		nextEvents = new ArrayList<>();

	}

	public void setDifficulty(Difficulty difficulty) {
		Objects.requireNonNull(difficulty, "diffuculty can't be null");
		this.difficulty = difficulty;
	}

	public Island getIsland() {
		return this.island;
	}

	public void setIsland(String islandName, President president) {

		Objects.requireNonNull(scenario, "Can't process new island without scenario");

		//set the factions
		List<Faction> facs = FACTIONS;
		facs.stream().forEach(x -> {
			x.setApprobationPercentage(scenario.getSatisFaction(x.getId()));
			x.setNbrSupporters(scenario.getFollowers());
		});

		//set the fields
		List<Field> fields = FIELDS;
		fields.stream().forEach(x -> {
			x.setExploitationPercentage(scenario.getExploitField(x.getId()));
			x.setYieldPercentage(scenario.getFieldYieldPercentage(x.getId()));
		});

		//set

		this.island = new Island(islandName, president, facs, fields, scenario.getTreasure());
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public static String getFactionNameById(int id){
		try{
			Faction f =  FACTIONS.stream().filter(x -> x.getId() == id).findFirst().get();
			return f.getName();
		}catch (Exception e){
			return "";
		}
	}

	public static String getFieldNameById(int id){
		try{
			Field f =  FIELDS.stream().filter(x -> x.getId() == id).findFirst().get();
			return f.getName();
		}catch (Exception e){
			return "";
		}
	}

	public Scenario getScenario() {
		return this.scenario;
	}

	public Field getFieldByName(String name) {
		return FIELDS.stream().filter(x -> x.getName().equals(name)).findFirst().get();
	}

	public Faction getFactionByName(String name) {
		return FACTIONS.stream().filter(x -> x.getName().equals(name)).findFirst().get();
	}

	/**
	 * @return all the scenarios found in game folders
	 */
	public List<Scenario> getScenarios() {
		return SCENARIOS;
	}

	/**
	 * @return all the factions set in init file
	 */
	public List<Faction> getFactions() {
		return FACTIONS;
	}

	/**
	 * @return all the fields setted in game folders
	 */
	public List<Field> getFields() {
		return FIELDS;
	}

	/**
	 * get all the events by the current scenario and the common ones
	 *
	 * @param scenario we use the current scenario to use this function
	 */
	public List<Event> getEvents(Scenario scenario) {
		List<Event> eventsList = new ArrayList<>();
		events.keySet().forEach(x -> {
			if (scenario.getPackageIds().contains(x)) {
				eventsList.addAll(events.get(x));
			}
		});

		return eventsList;
	}

	/**
	 * Check if the player lose the game
	 *
	 * @return
	 */
	public boolean checkLose() {
		System.out.println(island.totalSupporters());
		System.out.println(island.globalSatisfaction()+" < "+ difficulty.getMinGlobalSatisfaction());
		return !(island.globalSatisfaction() > difficulty.getMinGlobalSatisfaction());
	}

	public boolean nextTurn() {
		event = null;
		// add total random new event to stack
		addNextEvents();
		// get the next valid event and set it has the new current event
		setCurrentEvent(pickNextEvents());
		//update the season of the island
		island.incrementSeason();
		//new year mode
		return !checkLose();
	}

	/**
	 * Get the current event - Can be null ! in this case the player can just pass a
	 * turn
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * add an event from the scenario to the event stack
	 */
	public void addNextEvents() {
		var ev = scenario.getRandomEvent();
		if (ev == null) {
			return;
		}
		nextEvents.add(ev);
	}

	/**
	 * Get the next event from the stack corresponding to the current season or with
	 * no bound season. Can return null.
	 *
	 * @return
	 */
	public Event pickNextEvents() {
		try {
			return nextEvents.stream().filter(x -> x.getSeason() == island.getSeason() || x.getSeason() == null)
					.findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}

	public void setCurrentEvent(Event event) {
		this.event = event;
	}

	public void playAction(Action action){
		island.updateFactions(action.getFactionsEffects());
		island.updateFields(action.getFieldsEffects());
		island.updateFoodUnits(action.getFood());
		island.updateTreasure(action.getTreasure());
	}

	/**
	 * Get the correspondent faction from the id on its empty form
	 *
	 * @param id Id of the faction
	 * @return
	 */
	public Faction getFactionById(int id) {
		return (Faction) FACTIONS.stream().filter(f -> f.getId() == id);
	}

	public String printStats() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\n" + Color.ANSI_WHITE_BACKGROUND + "" + Color.ANSI_BLACK + "---- "+island.getName()+" : Infos ----\n");
		sb.append(Color.ANSI_RESET + "" + Color.ANSI_ITALIC + "Treasury : " + Color.ANSI_RESET + Color.ANSI_GREEN + "$"
				+ getIsland().getTreasury());
		sb.append("\n Food: ").append(island.getFoodUnits());
		sb.append("\nCitizens : ").append(island.totalSupporters());
		sb.append(Color.ANSI_RESET + "" + Color.ANSI_ITALIC + "\nCurrent season : " + Color.ANSI_RESET
				+ getIsland().getSeason());
		sb.append(Color.ANSI_ITALIC + "\nFactions satisfaction : ");
		getIsland().getFactions().stream().forEach(x -> {
			sb.append("\n" + Color.ANSI_RESET + Color.ANSI_RED + x.getName() + ": " + Color.ANSI_BOLD + Color.ANSI_WHITE
					+ GameUtils.round(x.getApprobationPercentage(),2) + "%");
			sb.append(" | Supporters : ").append(x.getNbrSupporters());
		});

		return sb.toString();
	}

	/**
	 * Master Control Program This nested class store all errors of the game from
	 * the init of the files to the end of the party.
	 */
	public static class MCP {
		private static HashMap<String, Set<String>> failedEventsFiles;

		/**
		 * add all the data from parsing error in the field assigned for it in the class
		 *
		 * @param files all the files name by their Scenario name if the parsing failed
		 */
		public static void addFailedEventFile(HashMap<String, Set<String>> files) {
			failedEventsFiles.putAll(files);
		}

		public static String print() {
			StringBuilder sb = new StringBuilder();
			if (failedEventsFiles != null) {
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
	 *
	 * @return a Game object
	 */
	public static Game initGame() {

		// https://howtodoinjava.com/gson/gson-jsonparser/
		// https://stackoverflow.com/questions/3133006/jsonparser-getresourceasstream
		// https://stackoverflow.com/questions/53542142/returning-json-file-as-jsonarray-in-spring-boot

		/* When making jar */
		/*
		 * String pathToData = ""; try{ String jarPath = Game.class
		 * .getProtectionDomain() .getCodeSource() .getLocation() .toURI() .getPath();
		 * File file = new File(jarPath); pathToData =
		 * file.getParentFile().getPath()+"\\ress\\";
		 *
		 *
		 * } catch (URISyntaxException e) { e.printStackTrace(); }
		 *
		 * String pathToFactionsFile = pathToData+"factions.json"; String
		 * pathToFieldsFile = pathToData+"fields.json"; String pathToScenariosDir =
		 * pathToData+"scenarios\\"; String pathToEventsDir = pathToData+"events\\";
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

		/* Init events from packages */
		// <package id, event list>
		HashMap<Integer, List<Event>> events = Event.initEvents(pathToEventsDir);
		// à décommenter pour voir les evnmts/ events.values().forEach(x ->
		// x.forEach(System.out::println));

		// System.out.println(events);
		Game game = new Game(factions, fields, scenarios, events);

		return game;
	}
}