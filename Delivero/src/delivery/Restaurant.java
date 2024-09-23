package delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant {
	
	String name;
	String category;
	Map<String, Float> dishes;
	List<Integer> ratings = new ArrayList<>();
	
	public Restaurant(String name, String category) {
		this.name = name;
		this.category = category;
		this.dishes = new HashMap<>();
	}

}
