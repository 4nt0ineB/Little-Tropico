package dutinfo.game.events;

import dutinfo.game.Game;
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
	//faction and field
	public String getEffectsPercentagesToString(){
		StringBuilder sb = new StringBuilder();

		factionsEffects.entrySet().forEach((x -> {
			String approb = (x.getValue()[0] != 0.0) ? ( (x.getValue()[0].toString().startsWith("-")) ?  "": "+") + x.getValue()[0] + ((x.getValue()[1] == 0)?"%, " :"% "): "";
			String foll = (x.getValue()[1] != 0) ? ( (x.getValue()[0].toString().startsWith("-")) ?  "": "+") +x.getValue()[1].intValue()+" Supporters, ": "";
			String cct = (!(approb.isEmpty() && foll.isEmpty())) ? Game.getFactionNameById(x.getKey())  + " " + approb  + " " + foll : "";
			sb.append(cct);
		}));

		sb.append("\n");

		fieldsEffects.entrySet().forEach((x -> {
			String cct = (x.getValue() != 0) ?Game.getFieldNameById(x.getKey())  + " " + ( (x.getValue().toString().startsWith("-")) ?  "": "+")+x.getValue()+"%, ": "";
			sb.append(cct);
		}));

		return sb.toString();
	}

	// treasure and food
	public String getEffectsToString() {
		String fo = (food < 0 && food != 0) ? "" + food+", ": (food != 0) ? "+" + food+", " : "";
		String tr = (treasure < 0 && treasure != 0) ? "" + treasure+", ": (treasure != 0) ? "" + food+", " : "";
		return ((!tr.isEmpty()) ? "Treasure "+tr: "") + ((!fo.isEmpty()) ? "Food "+fo: "");
	}
}