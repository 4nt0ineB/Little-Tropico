package dutinfo.game.environment;

import dutinfo.game.Game;
import dutinfo.game.GameUtils;
import dutinfo.game.society.Faction;
import dutinfo.game.society.President;
import dutinfo.game.society.Field;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Island {

	private String name;
	private Season currentSeason;
	private President president;
	private List<Faction> factions;
	private List<Field> fields;
	private float treasury;
	private int foodUnits;

	public Island(String name, President president, List<Faction> factions, List<Field> fields, float treasury) {
		this.name = name;
		this.currentSeason = Season.Spring;
		this.president = president;
		this.factions = factions;
		this.fields = fields;
		this.treasury = treasury;
		foodUnits = 0;
	}

	public int getFoodUnits(){
		return foodUnits;
	}

	public String getName() {
		return name;
	}

	public List<Faction> getFactions() {
		return factions;
	}

	public List<Field> getFields() {
		return fields;
	}

	public float getTreasury() {
		return treasury;
	}

	public void updateTreasure(double amount){
		treasury+=amount;
	}

	public void updateFoodUnits(int amount){
		foodUnits+=amount;
	}

	public Season getSeason() {
		return currentSeason;
	}

	public void incrementSeason() {
		currentSeason = Season.getNext(currentSeason);
	}

	public President getPresident() {
		return president;
	}

	private float getNewValueField(Field field, float inputV){
		float t = 0;
		t+= fields.stream().filter(x -> x.getId() == GameUtils.idByHashString("Industry")).findFirst().get().getExploitationPercentage();
		t+= fields.stream().filter(x -> x.getId() == GameUtils.idByHashString("Agriculture")).findFirst().get().getExploitationPercentage();
		if(t >= 100){
			return 0;
		}
		if((inputV + t)>= 100){
			return 100;
		}
		return inputV;
	}

	public void updateFactions(HashMap<Integer, Float[]> factionsValues){
		factions.parallelStream().forEach(x -> {
			var w = factionsValues.get(x.getId());
			if(w != null){
				x.setApprobationPercentage(x.getApprobationPercentage()+w[0]);
				x.setNbrSupporters(x.getNbrSupporters()+w[1].intValue());
			}
		});
	}

	public void updateFields(HashMap<Integer, Float> fieldsValues){
		fields.stream().forEach(x -> {
			var w = fieldsValues.get(x.getId());
			float sum = x.getExploitationPercentage();
			if(w != null){
				if(x.getId() == GameUtils.idByHashString("Agriculture") || x.getId() == GameUtils.idByHashString("Industry")){
					if(w > 0){
						sum+=getNewValueField(x, w);
					}
				}else{
					sum+=w;
				}
				x.setExploitationPercentage(sum);
			}
		});
	}

	public void generateYearlyResources(){ // Début d'année
		treasury+=fields.stream().filter(x -> x.getId() == GameUtils.idByHashString("Industry")).findFirst().get().generateProfit(treasury);
		foodUnits+=fields.stream().filter(x -> x.getId() == GameUtils.idByHashString("Agriculture")).findFirst().get().generateProfit((float)foodUnits);
	}

	public void updateSeasonResources(){ //début du tour
		//treasure : the dept
		if(treasury < 0){
			treasury+=(treasury)*(-0.2);
		}
		if((totalSupporters()*4)/foodUnits < 1){ //kill
			int s = totalSupporters();
			while ((s*4)/foodUnits < 1){
				 s--;
			}
			randomlyKillPeople(s);
		}else{ //create

		}
		//food ~ //supporters : kill or create
	}

	private void randomlyKillPeople(int n){

	}

	/**
	 * Set the treasury
	 *
	 * @param treasury
	 */
	public void setTreasury(float treasury) {
		this.treasury = treasury;
	}

	public int totalSupporters(){
		int x = 0;
		for (var f: factions
			 ) {
			x+=f.getNbrSupporters();
		}
		//return factions.stream().mapToInt(x -> x.getNbrSupporters()).sum();
		return x;
	}

	public float globalSatisfaction(){
		int totalsupp = totalSupporters();
		return (totalsupp != 0)? (float) factions.stream().mapToDouble(x -> x.getNbrSupporters()*x.getApprobationPercentage()).sum()/totalsupp : 0f;

	}

	@Override
	public String toString() {
		return "Island{" + "name='" + name + '\'' + ", currentSeason=" + currentSeason + ", president=" + president
				+ ", factions=" + factions + ", fields=" + fields + ", treasury=" + treasury + '}';
	}
}