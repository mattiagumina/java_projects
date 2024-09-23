package delivery;

import java.util.HashMap;
import java.util.Map;

public class Order {
	
	int id;
	Map<String, Integer> dishes = new HashMap<>();
	String customerName;
	Restaurant restaurant;
	int deliveryTime;
	int deliveryDistance;
	boolean assigned;
	
	public Order(int id, String[] dishNames, int[] quantities, String customerName, Restaurant restaurant, int deliveryTime,
			int deliveryDistance) {
		this.id = id;
		for(int i = 0; i < dishNames.length; i++)
			this.dishes.put(dishNames[i], quantities[i]);
		this.customerName = customerName;
		this.restaurant = restaurant;
		this.deliveryTime = deliveryTime;
		this.deliveryDistance = deliveryDistance;
		this.assigned = false;
	}
	
	

}
