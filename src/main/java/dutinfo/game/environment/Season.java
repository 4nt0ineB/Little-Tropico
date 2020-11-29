package dutinfo.game.environment;

public enum Season {
	Spring(0),
	Summer(1),
	Autumn(2),
	Winter(3);

	private int id;
	private String label;

	Season(int id) {
		this.id = id;
		this.label = switch (id){
			case 0:
				yield "Spring";
			case 1:
				yield "Summer";
			case 2:
				yield "Autumn";
			case 3:
				yield "Winter";
			default:
				throw new IllegalStateException("There is only four seasons.");
		};

	}

	public int getId() {
		return id;
	}


	@Override
	public String toString() {
		return label;
	}
}