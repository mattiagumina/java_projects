package delivery;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Comparable<Restaurant> {
	
	private String name;
	private String category;
	private ArrayList<Dish> dishes = new ArrayList<>();
	private ArrayList<Integer> ratings = new ArrayList<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}



	public Restaurant (String name, String category) {
		this.name = name;
		this.category = category;
		dishes = new ArrayList<Dish>();
	}

	@Override
	public int compareTo(Restaurant o) {
		return name.compareTo(o.name);
	}

	public void addDish (String name, float price) throws DeliveryException {
		if (dishes.stream().filter(d->d.getName().equals(name)).findAny().isPresent() ) {
			throw new DeliveryException ("Dish already present");
		} else {
			dishes.add(new Dish (name,price,this));
		}
	}

	public List<Dish> getDishes() {
		return dishes;
	}
	
	public void addRating(int rating)
	{
		ratings.add(rating);
	}
	
	boolean hasRatings() {
	    return ratings.size() > 0;
	}
	
	public double getAvgRating()
	{
		return ratings.stream().mapToInt(x->x)
		.average().orElse(0.0);
		//TODO se non ci sono valutazioni cosa si restituisce?
	}
}
