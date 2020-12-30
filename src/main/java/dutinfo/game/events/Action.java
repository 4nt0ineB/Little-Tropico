package dutinfo.game.events;

import dutinfo.game.GameUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Action {

	private static int counterAction = 0;
	private int id;
	private final String title;
	private final double treasure;
	private final int food;
	private HashMap<Integer, Double[]> factionsEffects;
	private HashMap<Integer, Double> fieldsEffects;
	private Set<Integer> repercussions;


	public Action(String title, double treasure, int food, HashMap<Integer, Double[]> factionsEffects, HashMap<Integer, Double> fieldsEffects,
				  Set<Integer> repercussions) {
		this.title = Objects.requireNonNull(title, "Action must have a title");
		int id = GameUtils.idByHashString(title) + 1;
		incCouterAction();
		this.treasure = treasure;
		this.factionsEffects = factionsEffects;
		this.fieldsEffects = fieldsEffects;
		this.repercussions = repercussions;
		this.food = food;
	}

	public HashMap<Integer, Double[]> getFactionsEffects(){
		return factionsEffects;
	}

	public HashMap<Integer, Double> getFieldsEffects(){
		return fieldsEffects;
	}

	public double getTreasure(){
		return treasure;
	}

	public int getFood(){
		return  food;
	}

	private void incCouterAction() {
		counterAction++;
	}

	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	/*public Double getFactions() {
		return factionsEffects.get(GameUtils.idByHashString("Capitalists"));
	}*/


	@Override
	public String toString() {
		return title;
	}


	public String dataToString() {
		return "Action{" + "\n\tid=" + id + "\n\t, title='" + title + '\'' + "\n\t, factionsEffects=" + factionsEffects
				+ "\n\t, fieldsEffects=" + fieldsEffects + "\n\t, repercussions=" + repercussions + '}';
	}

	public String getEffectsPercentagesToString(){
		StringBuilder sb = new StringBuilder();

		for (Integer name: factionsEffects.keySet()){
			String key = name.toString();
			String value = factionsEffects.get(name)[0].toString();
			if (value.startsWith("-")){
				sb.append(key + " " + value+"%, "); // shows faction -XX%,
			} else {
				sb.append(key + " +" + value+"%, "); // shows faction +XX%,
			}
		}

		sb.append("\n");

		for (Integer name: fieldsEffects.keySet()){
			String key = name.toString();
			String value = fieldsEffects.get(name).toString();
			if (value.startsWith("-")){
				sb.append(key + " " + value+"%, "); // shows field -XX%,
			} else {
				sb.append(key + " +" + value+"%, "); // shows field +XX%,
			}
		}
		return sb.toString();
	}

	public String getEffectsToString() {
		StringBuilder sb = new StringBuilder();

		if (food < 0 && food != 0){
			sb.append("" + food+", "); // shows field -XX%,
		} else if (food != 0) {
			sb.append("+" + food+", "); // shows field +XX%,
		}

		return sb.toString();
	}
}