package dutinfo.game.events;

import dutinfo.game.GameUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Action {

	private static int counterAction = 0;
	private int id;
	private final String title;
	private HashMap<Integer, Integer> factionsEffects;
	private HashMap<Integer, Integer> fieldsEffects;
	private Set<Integer> repercussions;

	public Action(String title, HashMap<Integer, Integer> factionsEffects, HashMap<Integer, Integer> fieldsEffects, Set<Integer> repercussions) {
		this.title = Objects.requireNonNull(title, "Action must have a title");
		int id = GameUtils.idByHashString(title) + 1;
		incCouterAction();
		this.factionsEffects = factionsEffects;
		this.fieldsEffects = fieldsEffects;
		this.repercussions = repercussions;
	}

	private void incCouterAction(){
		counterAction++;
	}

	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getFactions() {
		return factionsEffects.get(GameUtils.idByHashString("Capitalists"));
	}

	public int getFields() {
		// TODO - implement Action.getFields
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString(){
		String str = ""+factionsEffects;
		return str;
	}


}