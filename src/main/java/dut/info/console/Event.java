package dut.info.console;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Event {

	private final int id;
	private final Season season;
	private final String title;
	private ArrayList<Action> actions;

	private Event(Season season, String title, ArrayList<Action> actions) {
		this.id = GameUtils.idByHashString(title);
		this.title = Objects.requireNonNull(title, "Event's title can't be null");
		this.season = season;
		this.actions = actions;
	}
	private Event(){
		this(0, Season.Autumn, "test event", new ArrayList<>());
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

	public Action getActions() {
		// TODO - implement Event.getActions
		throw new UnsupportedOperationException();
	}



	public static List<Event> initEvents(String pathToScenarios){
		// all subfolder of the scenarios directory
		List<File> allDirScenarios = Arrays.stream(Paths.get(pathToScenarios).toFile().listFiles()).filter(x -> x.isDirectory()).collect(Collectors.toList());
		// List of all existing events from all scenarios subfolders (json files)
		List<File> alleventsFiles = new ArrayList<>();
		for (File d: allDirScenarios) {
			alleventsFiles.addAll(Arrays.stream(d.listFiles()).filter(x -> {
				String name = x.getName();
				int i = name.lastIndexOf('.');
				return (i > 0) && name.substring(i + 1).equals("json");
			}).collect(Collectors.toList()));
		}
		// List of all events from parsing all the json files
		List<Event> eventsList = new ArrayList<>();

		HashMap<String, List<String>> failedFiles;
		for(File f: alleventsFiles) {
			// Global array of the file
			JSONArray ar = (JSONArray) ((Object) GameUtils.jsonToObject(f.getPath()));
			ar.stream().forEach(x -> {
				/* Parse the file x */
				String title; List<Action> actions; Season season; //init Event() parameters
				JSONObject eventO = (JSONObject) x;
				season = switch ((int) eventO.get("season")){
					case Season.Spring:
						return Season.Spring;



				};
				title = (String) eventO.get("title");
				//actions
				String actionTitle;
				JSONArray factionsO = (JSONArray) eventO.get("actions");
				JSONObject o2 = (JSONObject) ((JSONArray) eventO.get("actions")).get(0);
				actionTitle = (String) o2.get("title");






				eventsList.add(new Event());
			});

			;
		}

/**
 *




			return new Event();
		}).collect(Collectors.toList());
		//alleventsFiles.stream().forEach(x -> System.out.println(x.getParentFile().getName()));
 */
		return eventsList;
	}



}