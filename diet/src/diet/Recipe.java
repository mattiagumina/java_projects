package diet;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe implements NutritionalElement {
	
	protected String name;
	protected double calories;
	protected double proteins;
	protected double carbs;
	protected double fat;
	protected Food foods;
	protected double weight;
	protected List<String> ingredients;
	
	/**
	 * Adds the given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 * @return the same Recipe object, it allows method chaining.
	 */
	
	public Recipe(Food f, String name) {
		this.foods = f;
		this.name = name;
		this.calories = 0.0;
		this.proteins = 0.0;
		this.carbs = 0.0;
		this.fat = 0.0;
		this.weight = 0.0;
		this.ingredients = new ArrayList<>();
	}
	
	public Recipe addIngredient(String material, double quantity) {
		NutritionalElement mat;
		mat = foods.getRawMaterial(material);
		this.calories += mat.getCalories() * quantity / 100;
		this.proteins += mat.getProteins() * quantity / 100;
		this.carbs += mat.getCarbs() * quantity / 100;
		this.fat += mat.getFat() * quantity / 100;
		this.weight += quantity;
		this.ingredients.add(material);
		return this;
	}

	@Override
	public String getName() {
		return this.name;
	}

	
	@Override
	public double getCalories() {
		return this.calories * 100 / this.weight;
	}
	

	@Override
	public double getProteins() {
		return this.proteins * 100 / this.weight;
	}

	@Override
	public double getCarbs() {
		return this.carbs * 100 / this.weight;
	}

	@Override
	public double getFat() {
		return this.fat * 100 / this.weight;
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expresses nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	
	public String toString() {
		String result = "";
		for(String ing: this.ingredients) {
			result += ing + "\n";
		}
		return result;
	}
	
	@Override
	public boolean per100g() {
		return true;
	}
	
}
