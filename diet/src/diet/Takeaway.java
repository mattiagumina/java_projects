package diet;

import java.util.*;


/**
 * Represents a takeaway restaurant chain.
 * It allows managing restaurants, customers, and orders.
 */
public class Takeaway {
	
	protected Food foods;
	protected SortedSet<Restaurant> restaurants;
	protected SortedSet<Customer> customers;

	/**
	 * Constructor
	 * @param food the reference {@link Food} object with materials and products info.
	 */
	public Takeaway(Food food){
		this.foods = food;
		this.restaurants = new TreeSet<>(Comparator.comparing(Restaurant::getName));
		this.customers = new TreeSet<>(Comparator.comparing(x -> {
																	String str = "";
																	str += x.getLastName() + " " + x.getFirstName();
																	return str;
																 }));
	}

	/**
	 * Creates a new restaurant with a given name
	 *
	 * @param restaurantName name of the restaurant
	 * @return the new restaurant
	 */
	public Restaurant addRestaurant(String restaurantName) {
		Restaurant rest = new Restaurant(restaurantName);
		restaurants.add(rest);
		return rest;
	}

	/**
	 * Retrieves the names of all restaurants
	 *
	 * @return collection of restaurant names
	 */
	public Collection<String> restaurants() {
		Collection<String> restaurantsNameList = new ArrayList<>();
		for(Restaurant rest: this.restaurants) {
			restaurantsNameList.add(rest.getName());
		}
		return restaurantsNameList;
	}

	/**
	 * Creates a new customer for the takeaway
	 * @param firstName first name of the customer
	 * @param lastName	last name of the customer
	 * @param email		email of the customer
	 * @param phoneNumber mobile phone number
	 *
	 * @return the object representing the newly created customer
	 */
	public Customer registerCustomer(String firstName, String lastName, String email, String phoneNumber) {
		Customer cust = new Customer(firstName, lastName, email, phoneNumber);
		customers.add(cust);
		return cust;
	}

	/**
	 * Retrieves all registered customers
	 *
	 * @return sorted collection of customers
	 */
	public Collection<Customer> customers(){
		return customers;
	}


	/**
	 * Creates a new order for the chain.
	 *
	 * @param customer		 customer issuing the order
	 * @param restaurantName name of the restaurant that will take the order
	 * @param time	time of desired delivery
	 * @return order object
	 */
	
	private int compareTime(String time1, String time2) {
		int h1, m1, h2, m2;
		h1 = Integer.valueOf(time1.substring(0, 2));
		m1 = Integer.valueOf(time1.substring(3, 5));
		h2 = Integer.valueOf(time2.substring(0, 2));
		m2 = Integer.valueOf(time2.substring(3, 5));
		if(h1 > h2 || (h1 == h2 && m1 > m2))
			return 1;
		else if(h1 < h2 || (h1 == h2 && m2 > m1))
			return -1;
		else
			return 0;
	}
	
	public Order createOrder(Customer customer, String restaurantName, String time) {
		Order ord = null;
		int i;
		for(Restaurant rest: restaurants) {
			if(rest.getName() == restaurantName) {
				if(!rest.isOpenAt(time)) {
					for(i = rest.openingTimes.size() - 1; i >= 0; i--) {
						if(i == 0 && compareTime(time, rest.openingTimes.get(i)) == -1){
							time = rest.openingTimes.get(i);
							break;
						}
						else if(i == rest.openingTimes.size() - 1 && compareTime(time, rest.closingTimes.get(i)) == 1) {
							time = rest.openingTimes.get(0);
							break;
						}
						else if(compareTime(time, rest.openingTimes.get(i)) == 1){
							time = rest.openingTimes.get(i + 1);
							break;
						}
					}
				}
				ord = new Order(restaurantName, customer.getLastName(), customer.getFirstName(), time);
				rest.orders.add(ord);
				break;
			}
		}
		return ord;
	}

	/**
	 * Find all restaurants that are open at a given time.
	 *
	 * @param time the time with format {@code "HH:MM"}
	 * @return the sorted collection of restaurants
	 */
	public Collection<Restaurant> openRestaurants(String time){
		Collection<Restaurant> res = new ArrayList<>();
		for(Restaurant rest: this.restaurants) {
			if(rest.isOpenAt(time))
				res.add(rest);
		}
		return res;
	}
}
