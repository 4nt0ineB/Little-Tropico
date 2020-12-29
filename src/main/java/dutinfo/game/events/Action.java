package dutinfo.game.events;

import dutinfo.game.GameUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Action {

	private static int counterAction = 0;
	private int id;
	private final String title;
	private HashMap<Integer, Double> factionsEffects;
	private HashMap<Integer, Double> fieldsEffects;
	private Set<Integer> repercussions;

	public Action(String title, HashMap<Integer, Double> factionsEffects, HashMap<Integer, Double> fieldsEffects,
				  Set<Integer> repercussions) {
		this.title = Objects.requireNonNull(title, "Action must have a title");
		int id = GameUtils.idByHashString(title) + 1;
		incCouterAction();
		this.factionsEffects = factionsEffects;
		this.fieldsEffects = fieldsEffects;
		this.repercussions = repercussions;
	}


	public HashMap<Integer, Double> getFactionsEffects(){
		return factionsEffects;
	}

	public HashMap<Integer, Double> getFieldsEffects(){
		return fieldsEffects;
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

	public Double getFactions() {
		return factionsEffects.get(GameUtils.idByHashString("Capitalists"));
	}

	public int getFields() {
		// TODO - implement Action.getFields
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return title;
	}




	public String dataToString() {
		return "Action{" + "\n\tid=" + id + "\n\t, title='" + title + '\'' + "\n\t, factionsEffects=" + factionsEffects
				+ "\n\t, fieldsEffects=" + fieldsEffects + "\n\t, repercussions=" + repercussions + '}';
	}
}