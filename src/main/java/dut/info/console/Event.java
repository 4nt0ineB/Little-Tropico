package dut.info.console;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Event {

	private final int id;
	private final Season season;
	private final String title;
	private ArrayList<Action> actions;

	private Event(int id, Season season, String title, ArrayList<Action> actions) {
		this.id = id;
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
		// List of all event from parsing the events json files
		List<Event> eventsList = alleventsFiles.stream().map(x -> {
			//System.out.println(x.getPath());
			Object jsonEvents =  GameUtils.jsonToObject(x.getPath());
			JSONArray events = (JSONArray) jsonEvents;
			Object o1 = events.get(0);
			JSONObject event = (JSONObject) o1;
			int id = (int) (long) event.get("id");
			String title = (String) event.get("title");
			Object factionsO = event.get("factions");
			

			return new Event();
		}).collect(Collectors.toList());
		//alleventsFiles.stream().forEach(x -> System.out.println(x.getParentFile().getName()));
		return eventsList;
	}



}