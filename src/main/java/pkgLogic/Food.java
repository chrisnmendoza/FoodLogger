package pkgLogic;

public class Food {
	private String name;
	private int calories;
	private int protein;
	private int carbohydrates;
	private int fat;
	
	public Food(String name, int calories, int protein, int carbohydrates, int fat) {
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.carbohydrates = carbohydrates;
		this.fat = fat;
	}

	public String getName() {
		return name;
	}

	public int getCalories() {
		return calories;
	}

	public int getProtein() {
		return protein;
	}

	public int getCarbohydrates() {
		return carbohydrates;
	}

	public int getFat() {
		return fat;
	}
	
}
