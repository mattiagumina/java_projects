package diet;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import diet.Order.OrderStatus;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents a restaurant class with given opening times and a set of menus.
 */
public class Restaurant {
	
	protected String name;
	protected List<String> openingTimes;
	protected List <String> closingTimes;
	protected List<Menu> menus;
	protected SortedSet<Order> orders;
	
	
	public Restaurant(String name) {
		this.name = name;
		this.openingTimes = new ArrayList<>();
		this.closingTimes = new ArrayList<>();
		this.menus = new ArrayList<>();
		this.orders = new TreeSet<>(Comparator.comparing(Order::toString));
	}
	
	/**
	 * retrieves the name of the restaurant.
	 *
	 * @return name of the restaurant
	 */
	public String getName() {
		return this.name;
	}
	
	private String addZero(String time) {
		String newTime = time;
		if(time.charAt(1) == ':')
			newTime = "0" + time;
		return newTime;
	}

	/**
	 * Define opening times.
	 * Accepts an array of strings (even number of elements) in the format {@code "HH:MM"},
	 * so that the closing hours follow the opening hours
	 * (e.g., for a restaurant opened from 8:15 until 14:00 and from 19:00 until 00:00,
	 * arguments would be {@code "08:15", "14:00", "19:00", "00:00"}).
	 *
	 * @param hm sequence of opening and closing times
	 */
	public void setHours(String ... hm) {
		int i;
		for(i = 0; i < hm.length; i+= 2) {
			hm[i] = addZero(hm[i]);
			this.openingTimes.add(hm[i]);
			this.closingTimes.add(hm[i + 1]);
		}
	}

	/**
	 * Checks whether the restaurant is open at the given time.
	 *
	 * @param time time to check
	 * @return {@code true} is the restaurant is open at that time
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
	
	public boolean isOpenAt(String time){
		int i;
		time = addZero(time);
		for(i = 0; i < this.openingTimes.size(); i++) {
			if((compareTime(time, this.openingTimes.get(i)) == 1 && compareTime(this.closingTimes.get(i), time) == 1) || compareTime(time, this.openingTimes.get(i)) == 0 || compareTime(time, this.closingTimes.get(i)) == 0)
				return true;
		}
		return false;
	}

	/**
	 * Adds a menu to the list of menus offered by the restaurant
	 *
	 * @param menu	the menu
	 */
	public void addMenu(Menu menu) {
		this.menus.add(menu);
	}

	/**
	 * Gets the restaurant menu with the given name
	 *
	 * @param name	name of the required menu
	 * @return menu with the given name
	 */
	public Menu getMenu(String name) {
		for(Menu menu: menus) {
			if(menu.getName() == name)
				return menu;
		}
		return null;
	}

	/**
	 * Retrieve all order with a given status with all the relative details in text format.
	 *
	 * @param status the status to be matched
	 * @return textual representation of orders
	 */
	public String ordersWithStatus(OrderStatus status) {
		String result = "";
		for(Order ord: orders) {
			if(ord.getStatus() == status) {
				result += this.name + ", " + ord.userFirstName + " " + ord.userLastName + " : (" + ord.deliveryTime + "):\n";
				for(String menu: ord.menus.keySet()) {
					result += "\t" + menu + "->" + ord.menus.get(menu) + "\n";
				}
			}
		}
		return result;
	}
}
