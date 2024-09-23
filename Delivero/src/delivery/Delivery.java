package delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;


public class Delivery {
	
	Map<String, Long> categories = new HashMap<>();
	Map<String, Restaurant> restaurants = new HashMap<>();
	Map<Integer, Order> orders = new LinkedHashMap<>();
	
	// R1
	
    /**
     * adds one category to the list of categories managed by the service.
     * 
     * @param category name of the category
     * @throws DeliveryException if the category is already available.
     */
	public void addCategory (String category) throws DeliveryException {
		if(this.categories.containsKey(category))
			throw new DeliveryException("");
		this.categories.put(category, 0L);
		}
	
	/**
	 * retrieves the list of defined categories.
	 * 
	 * @return list of category names
	 */
	public List<String> getCategories() {
		return this.categories.keySet().stream().collect(toList());
	}
	
	/**
	 * register a new restaurant to the service with a related category
	 * 
	 * @param name     name of the restaurant
	 * @param category category of the restaurant
	 * @throws DeliveryException if the category is not defined.
	 */
	public void addRestaurant (String name, String category) throws DeliveryException {
		if(!this.categories.containsKey(category))
			throw new DeliveryException("");
		this.restaurants.put(name, new Restaurant(name, category));
	}
	
	/**
	 * retrieves an ordered list by name of the restaurants of a given category. 
	 * It returns an empty list in there are no restaurants in the selected category 
	 * or the category does not exist.
	 * 
	 * @param category name of the category
	 * @return sorted list of restaurant names
	 */
	public List<String> getRestaurantsForCategory(String category) {
        return this.restaurants.values().stream()
        										.filter(restaurant -> restaurant.category.equals(category))
        										.map(restaurant -> restaurant.name)
        										.sorted()
        										.collect(toList());
	}
	
	// R2
	
	/**
	 * adds a dish to the list of dishes of a restaurant. 
	 * Every dish has a given price.
	 * 
	 * @param name             name of the dish
	 * @param restaurantName   name of the restaurant
	 * @param price            price of the dish
	 * @throws DeliveryException if the dish name already exists
	 */
	public void addDish(String name, String restaurantName, float price) throws DeliveryException {
		if(this.restaurants.get(restaurantName).dishes.containsKey(name))
			throw new DeliveryException("");
		this.restaurants.get(restaurantName).dishes.put(name, price);
	}
	
	/**
	 * returns a map associating the name of each restaurant with the 
	 * list of dish names whose price is in the provided range of price (limits included). 
	 * If the restaurant has no dishes in the range, it does not appear in the map.
	 * 
	 * @param minPrice  minimum price (included)
	 * @param maxPrice  maximum price (included)
	 * @return map restaurant -> dishes
	 */
	public Map<String,List<String>> getDishesByPrice(float minPrice, float maxPrice) {
        return this.restaurants.values().stream()
        									.map(restaurant -> Map.entry(restaurant.name, restaurant.dishes.entrySet().stream().filter(entry -> entry.getValue() >= minPrice && entry.getValue() <= maxPrice).map(entry -> entry.getKey()).collect(toList())))
        									.filter(entry -> entry.getValue().size() > 0)
        									.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	/**
	 * retrieve the ordered list of the names of dishes sold by a restaurant. 
	 * If the restaurant does not exist or does not sell any dishes 
	 * the method must return an empty list.
	 *  
	 * @param restaurantName   name of the restaurant
	 * @return alphabetically sorted list of dish names 
	 */
	public List<String> getDishesForRestaurant(String restaurantName) {
        if(!this.restaurants.containsKey(restaurantName))
        	return Collections.emptyList();
        return this.restaurants.get(restaurantName).dishes.keySet().stream()
        										.sorted()
        										.collect(toList());
	}
	
	/**
	 * retrieves the list of all dishes sold by all restaurants belonging to the given category. 
	 * If the category is not defined or there are no dishes in the category 
	 * the method must return and empty list.
	 *  
	 * @param category     the category
	 * @return 
	 */
	public List<String> getDishesByCategory(String category) {
        return this.restaurants.values().stream()
        									.filter(restaurant -> restaurant.category.equals(category))
        									.map(restaurant -> restaurant.dishes.keySet())
        									.flatMap(set -> set.stream())
        									.collect(toList());
	}
	
	//R3
	
	/**
	 * creates a delivery order. 
	 * Each order may contain more than one product with the related quantity. 
	 * The delivery time is indicated with a number in the range 8 to 23. 
	 * The delivery distance is expressed in kilometers. 
	 * Each order is assigned a progressive order ID, the first order has number 1.
	 * 
	 * @param dishNames        names of the dishes
	 * @param quantities       relative quantity of dishes
	 * @param customerName     name of the customer
	 * @param restaurantName   name of the restaurant
	 * @param deliveryTime     time of delivery (8-23)
	 * @param deliveryDistance distance of delivery
	 * 
	 * @return order ID
	 */
	public int addOrder(String dishNames[], int quantities[], String customerName, String restaurantName, int deliveryTime, int deliveryDistance) {
		int  idOrder = this.orders.size() + 1;
		this.orders.put(idOrder, new Order(idOrder, dishNames, quantities, customerName, this.restaurants.get(restaurantName), deliveryTime, deliveryDistance));
		Long nOrder = this.categories.get(this.restaurants.get(restaurantName).category);
		this.categories.put(this.restaurants.get(restaurantName).category, nOrder + 1L);
	    return idOrder;
	}
	
	/**
	 * retrieves the IDs of the orders that satisfy the given constraints.
	 * Only the  first {@code maxOrders} (according to the orders arrival time) are returned
	 * they must be scheduled to be delivered at {@code deliveryTime} 
	 * whose {@code deliveryDistance} is lower or equal that {@code maxDistance}. 
	 * Once returned by the method the orders must be marked as assigned 
	 * so that they will not be considered if the method is called again. 
	 * The method returns an empty list if there are no orders (not yet assigned) 
	 * that meet the requirements.
	 * 
	 * @param deliveryTime required time of delivery 
	 * @param maxDistance  maximum delivery distance
	 * @param maxOrders    maximum number of orders to retrieve
	 * @return list of order IDs
	 */
	public List<Integer> scheduleDelivery(int deliveryTime, int maxDistance, int maxOrders) {
		List<Integer> result = new ArrayList<>();;
        this.orders.values().stream()
        								.filter(order -> !order.assigned)
        								.filter(order -> order.deliveryTime == deliveryTime && order.deliveryDistance <= maxDistance)
        								.limit(maxOrders)
        								.forEach(order -> {
        									order.assigned = true;
        									result.add(order.id);
        								});
        return result;
	}
	
	/**
	 * retrieves the number of orders that still need to be assigned
	 * @return the unassigned orders count
	 */
	public int getPendingOrders() {
        return this.orders.values().stream()
        								.filter(order -> !order.assigned)
        								.map(order -> 1)
        								.collect(summingInt(x -> x));
	}
	
	// R4
	/**
	 * records a rating (a number between 0 and 5) of a restaurant.
	 * Ratings outside the valid range are discarded.
	 * 
	 * @param restaurantName   name of the restaurant
	 * @param rating           rating
	 */
	public void setRatingForRestaurant(String restaurantName, int rating) {
		if(rating >= 0 || rating <= 5)
			this.restaurants.get(restaurantName).ratings.add(rating);
	}
	
	/**
	 * retrieves the ordered list of restaurant. 
	 * 
	 * The restaurant must be ordered by decreasing average rating. 
	 * The average rating of a restaurant is the sum of all rating divided by the number of ratings.
	 * 
	 * @return ordered list of restaurant names
	 */
	public List<String> restaurantsAverageRating() {
        return this.restaurants.values().stream()
        									.filter(restaurant -> restaurant.ratings.size() > 0)
        									.map(restaurant -> Map.entry(restaurant.name, restaurant.ratings.stream().collect(averagingInt(x -> x))))
        									.sorted(Comparator.comparing(entry -> 1.0D / entry.getValue()))
        									.map(entry -> entry.getKey())
        									.collect(toList());
	}
	
	//R5
	/**
	 * returns a map associating each category to the number of orders placed to any restaurant in that category. 
	 * Also categories whose restaurants have not received any order must be included in the result.
	 * 
	 * @return map category -> order count
	 */
	public Map<String,Long> ordersPerCategory() {
        return this.categories;
	}
	
	/**
	 * retrieves the name of the restaurant that has received the higher average rating.
	 * 
	 * @return restaurant name
	 */
	public String bestRestaurant() {
		return this.restaurants.values().stream()
				.filter(restaurant -> restaurant.ratings.size() > 0)
				.map(restaurant -> Map.entry(restaurant.name, restaurant.ratings.stream().collect(averagingInt(x -> x))))
				.max(Comparator.comparing(entry -> entry.getValue()))
				.map(entry -> entry.getKey())
				.orElse("");
	}
}
