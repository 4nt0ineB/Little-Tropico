package dutinfo.game.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dutinfo.game.Game;
import dutinfo.game.GameUtils;
import dutinfo.game.environment.Season;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;


import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Event {

	private final int id;
	private final String eventName;
	private final Season season;
	private final String packageTitle;
	private final String title;
	private List<Action> actions;
	private final boolean isOnlyARepercussion;

	private Event(Season season, String packageTitle, String eventName, String title, List<Action> actions,
				  Boolean isOnlyARepercussion) {

		this.title = Objects.requireNonNull(title, "Event's title can't be null");
		this.eventName = Objects.requireNonNull(eventName, "file name can't by null or empty.");
		this.id = GameUtils.idByHashString(eventName);
		this.packageTitle = Objects.requireNonNull(packageTitle);
		this.season = season;
		this.actions = actions;
		this.isOnlyARepercussion = isOnlyARepercussion;
	}

	private Event() {
		this(Season.Autumn, "package test", "bop", "test event", new ArrayList<>(), false);
	}

	public int getId() {
		return this.id;
	}

	public Season getSeason() {
		return this.season;
	}

	public String getTitle() {
		return this.title;
	}

	public List<Action> getActions() {
		return actions;
	}

	public String getPackageTitle() {
		return packageTitle;
	}

	public boolean isOnlyARepercussion() {
		return isOnlyARepercussion;
	}

	/**
	 * Parse all the json files to create the events
	 * @param pathToEventsDir path to the scenarios dir
	 * @return list of the Events
	 * */
	public static HashMap<Integer, List<Event>> initEvents(String pathToEventsDir){
		// all subfolder of the scenarios directory
		List<File> allDirScenarios = Arrays.stream(Objects.requireNonNull(Paths.get(pathToEventsDir).toFile().listFiles())).filter(File::isDirectory).collect(Collectors.toList());

		// List of all existing events (json files) from all scenarios subfolders
		List<File> alleventsFiles = new ArrayList<>();

		for (File d: allDirScenarios) {
			alleventsFiles.addAll(GameUtils.allJsonFromDir(d));
		}

		// List of all events from parsing all the json files
		HashMap<Integer, List<Event>> eventsList = new HashMap<>();

		// All files failed to parse for each scenario
		HashMap<String, Set<String>> failedFiles = new HashMap<>();

		for(File f: alleventsFiles) {

			// Global array of the file
			JsonObject ev = GameUtils.jsonToObject(f.getPath());
			try{

				//init Event() parameters
				Season season;
				String title;
				List<Action> actions = new ArrayList<>();

				try{
					int rs = ev.get("season").getAsInt();
					if(rs == 0){
						season = Season.Spring;
					}else if(rs == 1){
						season = Season.Summer;
					}else if(rs == 2){
						season = Season.Autumn;
					}else if(rs == 3){
						season = Season.Winter;
					}else {
						season = null;
					}
				}catch (Exception e){
					season = null;
				}

				//Event name (name of the file)
				String eventName = f.getName().substring(0,  f.getName().lastIndexOf('.'));
				if(eventName.isEmpty()) throw new IllegalArgumentException("the name of the file can't be empty.");

				// Title : the string of the question/problem itself
				title = ev.get("title").getAsString();
				assert title != null;

				//Package title
				String packageTitle = f.getParentFile().getName();

				// Check if the event is only a repercussion to an other event
				boolean currEventOnlyRepercussion;
				var x = ev.get("repercussion");
				if(x == null){
					currEventOnlyRepercussion = false;
				}else{
					currEventOnlyRepercussion = x.getAsBoolean();
				}


				//actions
				HashMap<Integer, Float[]> factionsEffects = new HashMap<>();
				HashMap<Integer, Float> fieldsEffects = new HashMap<>();
				JsonArray actionsData = ev.getAsJsonArray("actions");
				actionsData.forEach(a -> {
					JsonObject action = a.getAsJsonObject();
					String actionTitle = action.get("title").getAsString();

					String[] properties = {"factions", "fields"};
					for (String prop: properties
					) {
						JsonArray propArray = action.getAsJsonArray(prop);
						if(propArray != null){
							propArray.forEach(obj ->{
								JsonObject property = obj.getAsJsonObject();
								String name = property.get("name").getAsString();
								if(prop.equals("factions")){
									if(!Faction.exist(name)){
										throw new IllegalArgumentException("Faction doesn't exist. Check factions.json");
									}
								}else{
									if(!Field.exist(name)){
										throw new IllegalArgumentException("Field doesn't exist. Check fields.json");
									}
								}

								float effect = 0;
								int followers = 0;


								try{
									effect =  property.get("effect").getAsFloat();
								}catch(Exception e){
								}

								try{
									followers = property.get("followers").getAsInt();
								}catch (Exception e){
								}




								Float[] vals = new Float[3];
								if(prop.equals("factions")){
									vals[0]= effect;
									vals[1]= (float) followers;
									factionsEffects.put(GameUtils.idByHashString(name), vals);
								}else if(prop.equals("fields")){
									fieldsEffects.put(GameUtils.idByHashString(name), effect);
								}else{
									throw new IllegalStateException("The property " + prop + " shouldn't exist.");
								}
							});
						}
					}

					//Treasure
					float treasure = 0.0f;
					try{
						treasure = action.get("treasure").getAsFloat();
					}catch (Exception e){

					}

					//Food
					int food = 0;
					try{
						food = action.get("food").getAsInt();
					}catch (Exception e){
					}


					// Repercussions
					Set<Integer> repercussions = new HashSet<>();
					JsonArray repO = action.getAsJsonArray("repercussions");
					if(repO != null){
						repO.forEach(p -> {
							repercussions.add(GameUtils.idByHashString(p.getAsString()));
						}
						);
					}

					actions.add(new Action(actionTitle, treasure, food,(HashMap<Integer, Float[]>) factionsEffects.clone(), (HashMap<Integer, Float>) fieldsEffects.clone(), repercussions));

					factionsEffects.clear();
					fieldsEffects.clear();

				});


				// Add event to his correspondent package name in hashmap
				Integer packId = GameUtils.idByHashString(packageTitle);
				if(!eventsList.containsKey(packId)){
					eventsList.put(packId, new ArrayList<>());
				}
				eventsList.get(packId).add(new Event(season, packageTitle, eventName ,title, actions, currEventOnlyRepercussion));
			}catch(Exception e){
				System.out.println(e);
				String scenarioName = f.getParentFile().getName();
				Set<String> filesName = failedFiles.get(scenarioName);
				if(null == filesName){
					filesName = new HashSet<>();
					filesName.add(f.getName());
					failedFiles.put(scenarioName, filesName);
				}else{
					failedFiles.get(scenarioName).add(f.getName());
					Game.MCP.addFailedEventFile(failedFiles); // put in a error history
				}

			}
		}
		return eventsList;
	}

	public String toString() {
		return title;

	}

	public String datatoString() {
		return "Event{" + "\nid=" + id + "\nis repercussion " + isOnlyARepercussion + "\n, eventName='" + eventName
				+ '\'' + "\n, season=" + season + "\n, packageTitle='" + packageTitle + '\'' + "\n, title='" + title
				+ '\'' + "\n, actions=" + actions + '}';
	}
}