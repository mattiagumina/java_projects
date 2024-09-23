package diet;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents and order issued by an {@link Customer} for a {@link Restaurant}.
 *
 * When an order is printed to a string is should look like:
 * <pre>
 *  RESTAURANT_NAME, USER_FIRST_NAME USER_LAST_NAME : DELIVERY(HH:MM):
 *  	MENU_NAME_1->MENU_QUANTITY_1
 *  	...
 *  	MENU_NAME_k->MENU_QUANTITY_k
 * </pre>
 */
public class Order {
	
	protected String restaurantName;
	protected String userLastName;
	protected String userFirstName;
	protected String deliveryTime;
	protected OrderStatus orderStatus;
	protected PaymentMethod paymentMethod;
	protected SortedMap<String, Integer> menus;
	
	public Order(String restaurantName, String userLastName, String userFirstName, String deliveryTime) {
		this.restaurantName = restaurantName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.deliveryTime = addZero(deliveryTime);
		this.orderStatus = OrderStatus.ORDERED;
		this.paymentMethod = PaymentMethod.CASH;
		this.menus = new TreeMap<>();
	}
	
	private String addZero(String time) {
		String newTime = time;
		if(time.charAt(1) == ':')
			newTime = "0" + time;
		return newTime;
	}
	
	/**
	 * Possible order statuses
	 */
	public enum OrderStatus {
		ORDERED, READY, DELIVERED
	}

	/**
	 * Accepted payment methods
	 */
	public enum PaymentMethod {
		PAID, CASH, CARD
	}

	/**
	 * Set payment method
	 * @param pm the payment method
	 */
	public void setPaymentMethod(PaymentMethod pm) {
		this.paymentMethod = pm;
	}

	/**
	 * Retrieves current payment method
	 * @return the current method
	 */
	public PaymentMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	/**
	 * Set the new status for the order
	 * @param os new status
	 */
	public void setStatus(OrderStatus os) {
		this.orderStatus = os;
	}

	/**
	 * Retrieves the current status of the order
	 *
	 * @return current status
	 */
	public OrderStatus getStatus() {
		return this.orderStatus;
	}

	/**
	 * Add a new menu to the order with a given quantity
	 *
	 * @param menu	menu to be added
	 * @param quantity quantity
	 * @return the order itself (allows method chaining)
	 */
	public Order addMenus(String menu, int quantity) {
		if(this.menus.containsKey(menu))
			this.menus.remove(menu);
		this.menus.put(menu, quantity);
		return this;
	}
	
	@Override
	public String toString() {
		String result = this.restaurantName + ", " + this.userFirstName + " " + this.userLastName + " : (" + this.deliveryTime + "):\n";
		for(String menu: this.menus.keySet()) {
			result += "\t" + menu + "->" + this.menus.get(menu) + "\n";
		}
		return result;
	}
	
}
