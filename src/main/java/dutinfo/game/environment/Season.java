package dutinfo.game.environment;

public enum Season {
	Spring(0), Summer(1), Autumn(2), Winter(3);

	private int id;
	private String label;

	Season(int id) {
		this.id = id;
		if(id == 0){
			this.label = "Spring";

		}else if(id == 1){
			this.label = "Summer";

		}else if(id == 2){
			this.label = "Autumn";

		}else if(id == 3){
			this.label = "Winter";
		}else{
			throw new IllegalStateException("There is only four seasons.");
		}
	}

	public int getId() {
		return id;
	}

	public static Season getNext(Season season) {
		Season s = null;
		if(season.id == 0){
			s = Summer;
		}else if(season.id == 1){
			s = Autumn;

		}else if(season.id == 2){
			s = Winter;
		}else{
			s = Spring;
		}
		return s;
	}

	@Override
	public String toString() {
		return label;
	}
}