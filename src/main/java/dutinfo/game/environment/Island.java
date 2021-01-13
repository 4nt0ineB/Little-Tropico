package dutinfo.game.environment;

import dutinfo.game.Game;
import dutinfo.game.GameUtils;
import dutinfo.game.society.Faction;
import dutinfo.game.society.President;
import dutinfo.game.society.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Island {

	private String name;
	private Season currentSeason;
	private President president;
	private List<Faction> factions;
	private List<Field> fields;
	private float treasury;
	private int foodUnits;
	private int nbPastYears;

	public Island(String name, President president, List<Faction> factions, List<Field> fields, float treasury, int foodUnits) {
		this.name = name;
		this.currentSeason = Season.Spring;
		this.president = president;
		this.factions = factions;
		this.fields = fields;
		this.treasury = treasury;
		this.foodUnits = foodUnits;
	}

	public int getNbPastYears() {
		return nbPastYears;
	}

	public void incrementYears(){
		nbPastYears++;
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
		return (float) GameUtils.round(treasury, 2);
	}

	public void updateTreasure(double amount){
		treasury+=amount;
	}

	public void updateFoodUnits(float amount){
		if(foodUnits == 0) return;
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

	public void updateFactions(HashMap<Integer, Float[]> factionsValues, float coef){
		factions.parallelStream().forEach(x -> {
			var w = factionsValues.get(x.getId());
			if(w != null){
				x.setApprobationPercentage(x.getApprobationPercentage()+(w[0]*coef));
				x.setNbrSupporters(x.getNbrSupporters()+((int) (w[1].intValue()*coef)));
			}
		});
	}

	public void updateFields(HashMap<Integer, Float> fieldsValues, float coef){
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
				x.setExploitationPercentage(sum*coef);
			}
		});
	}

	public void generateYearlyResources(){ // Début d'année
		treasury+=(fields.stream().filter(x -> x.getId() == GameUtils.idByHashString("Industry")).findFirst().get()).generateProfit(treasury);
		foodUnits+=(fields.stream().filter(x -> x.getId() == GameUtils.idByHashString("Agriculture")).findFirst().get()).generateProfit((float)foodUnits);
	}


	public boolean canBeBribed(int factionId, int amount){
		Faction f = factions.stream().filter(x -> x.getId() == factionId).findFirst().get();
		int price = (f.getNbrSupporters() * Game.bribingPrice)*amount;
		if(treasury < price){
			return false;
		}
		return true;
	}


	public void bribeFaction(int factionId, int amount){
		Faction f = factions.stream().filter(x -> x.getId() == factionId).findFirst().get();
		int price = (f.getNbrSupporters() * Game.bribingPrice)*amount;
		factions.stream().filter(x -> x.getId() == factionId).findFirst().get().setApprobationPercentage(f.getApprobationPercentage()+(2*amount));
		treasury-=price;
	}


	public int updatePopulationAndTreasury(){ //début du tour
		//treasure : the debt
		if(treasury < 0){
			treasury+=(treasury)*(-0.2);
		}
		if(foodUnits == 0) {
			int x = totalSupporters();
			randomlyKillPeople(x);
			return x;
		} else if((totalSupporters()*4) > foodUnits){ //kill
			int s = totalSupporters();
			while ((s*4)/foodUnits < 1){
				 s--;
			}
			randomlyKillPeople(s);
			return -s;
		}else{ //create
			Random r = new Random();
			int x = ((r.nextInt((10 - 0) + 1) + 0)/100)*(totalSupporters());
			System.out.println(x);
			randomlyCreatePeople(x);
			return x;
		}
	}

	private void randomlyCreatePeople(int n){
		Random r = new Random();
		Faction f;
		while (n > 0){
			f = factions.get(r.nextInt(((factions.size()-1) - 0) + 1) + 0);
			int nbtoCreate = r.nextInt((n - 0) + 1) + 0;
			f.setNbrSupporters(f.getNbrSupporters()+nbtoCreate);
			n-=nbtoCreate;
		}
	}

	private void randomlyKillPeople(int n){
		System.out.println("tokill: "+n);
		Random r = new Random();
		Faction f;
		while (n > 0){
			int i = r.nextInt(((factions.size() -1) - 0) + 1) + 0;
			if(factions.get(i).getNbrSupporters() != 0){
				int nbToKill = r.nextInt((n - 0) + 1) + 0;
				int thenpop = factions.get(i).getNbrSupporters();
				int rest = factions.get(i).getNbrSupporters()-nbToKill;
				//System.out.println("| "+thenpop+" | -"+nbToKill + " => "+rest);
				factions.get(i).setNbrSupporters(rest);
				factions.get(i).setApprobationPercentage(factions.get(i).getApprobationPercentage()-((((thenpop-nbToKill) <= 0 )? thenpop: nbToKill)*2));
				if(rest < 0){
					n-=(rest + nbToKill);
				}else if(rest >= 0){
					n-= nbToKill;
				}

			}

		}

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