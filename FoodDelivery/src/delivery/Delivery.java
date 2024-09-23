package delivery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

public class Delivery {
    
    public static enum OrderStatus { NEW, CONFIRMED, PREPARATION, ON_DELIVERY, DELIVERED } 
    
    Map<Integer, Customer> customers = new HashMap<>();
    List<Menu> menus = new ArrayList<>();
    Map<Integer, Order> orders = new HashMap<>();
    
    /**
     * Creates a new customer entry and returns the corresponding unique ID.
     * 
     * The ID for the first customer must be 1.
     * 
     * 
     * @param name name of the customer
     * @param address customer address
     * @param phone customer phone number
     * @param email customer email
     * @return unique customer ID (positive integer)
     */
    public int newCustomer(String name, String address, String phone, String email) throws DeliveryException {
        int customerId = this.customers.size() + 1;
    	if(this.customers.values().stream().map(customer -> customer.email).anyMatch(customerEmail -> customerEmail.equals(email)))
    		throw new DeliveryException("The same email is already associated to another customer");
    	this.customers.put(customerId, new Customer(customerId, name, address, phone, email));
    	return customerId;
    }
    
    /**
     * Returns a string description of the customer in the form:
     * <pre>
     * "NAME, ADDRESS, PHONE, EMAIL"
     * </pre>
     * 
     * @param customerId
     * @return customer description string
     */
    public String customerInfo(int customerId){
        return this.customers.get(customerId).name + ", " + this.customers.get(customerId).address + ", " + this.customers.get(customerId).phone + ", " + this.customers.get(customerId).email;
    }
    
    /**
     * Returns the list of customers, sorted by name
     * 
     */
    public List<String> listCustomers(){
        return this.customers.values().stream()
        								.map(customer -> this.customerInfo(customer.customerId))
        								.sorted()
        								.toList();
    }
    
    /**
     * Add a new item to the delivery service menu
     * 
     * @param description description of the item (e.g. "Pizza Margherita", "Bunet")
     * @param price price of the item (e.g. 5 EUR)
     * @param category category of the item (e.g. "Main dish", "Dessert")
     * @param prepTime estimate preparation time in minutes
     */
    public void newMenuItem(String description, double price, String category, int prepTime){
        this.menus.add(new Menu(description, price, category, prepTime));
    }
    
    /**
     * Search for items matching the search string.
     * The items are sorted by category first and then by description.
     * 
     * The format of the items is:
     * <pre>
     * "[CATEGORY] DESCRIPTION : PRICE"
     * </pre>
     * 
     * @param search search string
     * @return list of matching items
     */
    public List<String> findItem(String search){
        return this.menus.stream()
        							.filter(menu -> menu.description.toLowerCase().contains(search.toLowerCase()))
        							.map(menu -> "[" + menu.category + "] " + menu.description + " : " + String.format("%.2f", menu.price))
        							.sorted().collect(toList());
    }
    
    /**
     * Creates a new order for the given customer.
     * Returns the unique id of the order, a progressive
     * integer number starting at 1.
     * 
     * @param customerId
     * @return order id
     */
    public int newOrder(int customerId){
        int orderId = this.orders.size() + 1;
        this.orders.put(orderId, new Order(orderId, customerId));
        return orderId;
    }
    
    /**
     * Add a new item to the order with the given quantity.
     * 
     * If the same item is already present in the order is adds the
     * given quantity to the current quantity.
     * 
     * The method returns the final total quantity of the item in the order. 
     * 
     * The item is found through the search string as in {@link #findItem}.
     * If none or more than one item is matched, then an exception is thrown.
     * 
     * @param orderId the id of the order
     * @param search the item search string
     * @param qty the quantity of the item to be added
     * @return the final quantity of the item in the order
     * @throws DeliveryException in case of non unique match or invalid order ID
     */
    public int addItem(int orderId, String search, int qty) throws DeliveryException {
    	int currentQuantity = 0;
        if(this.findItem(search).size() != 1)
        	throw new DeliveryException("The search pattern must identify exactly one menu item");
        if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID must be valid");
        if(this.orders.get(orderId).items.containsKey(this.findItem(search).get(0))) {
        	currentQuantity = this.orders.get(orderId).items.get(this.findItem(search).get(0));
        	this.orders.get(orderId).items.put(this.findItem(search).get(0), currentQuantity + qty);
        }
        else
        	this.orders.get(orderId).items.put(this.findItem(search).get(0), qty);
        return currentQuantity + qty;
    }
    
    /**
     * Show the items of the order using the following format
     * <pre>
     * "DESCRIPTION, QUANTITY"
     * </pre>
     * 
     * @param orderId the order ID
     * @return the list of items in the order
     * @throws DeliveryException when the order ID in invalid
     */
    public List<String> showOrder(int orderId) throws DeliveryException {
        if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
        return this.orders.get(orderId).items.entrySet().stream()
        										.map(entry -> entry.getKey().split("]")[1].split(":")[0].trim() + ", " + entry.getValue())
        										.collect(toList());
    }
    
    /**
     * Retrieves the total amount of the order.
     * 
     * @param orderId the order ID
     * @return the total amount of the order
     * @throws DeliveryException when the order ID in invalid
     */
    public double totalOrder(int orderId) throws DeliveryException {
    	if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
    	return this.orders.get(orderId).items.entrySet().stream()
    											.map(entry -> entry.getValue() * (Double.valueOf(entry.getKey().split(":")[1].trim().split(",")[0]) + (Double.valueOf(entry.getKey().split(":")[1].trim().split(",")[1]) / 100.0D)))
    											.collect(summingDouble(x -> x));
    }
    
    /**
     * Retrieves the status of an order
     * 
     * @param orderId the order ID
     * @return the current status of the order
     * @throws DeliveryException when the id is invalid
     */
    public OrderStatus getStatus(int orderId) throws DeliveryException {
    	if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
    	return this.orders.get(orderId).status;
    }
    
    /**
     * Confirm the order. The status goes from {@code NEW} to {@code CONFIRMED}
     * 
     * Returns the delivery time estimate computed as the sum of:
     * <ul>
     * <li>start-up delay (conventionally 5 min)
     * <li>preparation time (max of all item preparation time)
     * <li>transportation time (conventionally 15 min)
     * </ul>
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code NEW}
     */
    public int confirm(int orderId) throws DeliveryException {
    	int deliveryTime = 20;
    	if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
    	if(this.orders.get(orderId).status != OrderStatus.NEW)
    		throw new DeliveryException("The order is not in the state NEW");
    	deliveryTime += this.menus.stream()
    									.filter(menu -> this.orders.get(orderId).items.containsKey(this.findItem(menu.description).get(0)))
    									.map(menu -> menu.preparationTime)
    									.max(Comparator.comparingInt(x -> x)).get();
    	this.orders.get(orderId).status = OrderStatus.CONFIRMED;
    	this.orders.get(orderId).deliveryTime = deliveryTime;
    	return deliveryTime;
    }

    /**
     * Start the order preparation. The status goes from {@code CONFIRMED} to {@code PREPARATION}
     * 
     * Returns the delivery time estimate computed as the sum of:
     * <ul>
     * <li>preparation time (max of all item preparation time)
     * <li>transportation time (conventionally 15 min)
     * </ul>
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code CONFIRMED}
     */
    public int start(int orderId) throws DeliveryException {
    	if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
    	if(this.orders.get(orderId).status != OrderStatus.CONFIRMED)
    		throw new DeliveryException("The order is not in the state CONFIRMED");
    	this.orders.get(orderId).status = OrderStatus.PREPARATION;
    	this.orders.get(orderId).deliveryTime -= 5;
    	return this.orders.get(orderId).deliveryTime;
    }

    /**
     * Begins the order delivery. The status goes from {@code PREPARATION} to {@code ON_DELIVERY}
     * 
     * Returns the delivery time estimate computed as the sum of:
     * <ul>
     * <li>transportation time (conventionally 15 min)
     * </ul>
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code PREPARATION}
     */
    public int deliver(int orderId) throws DeliveryException {
    	if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
    	if(this.orders.get(orderId).status != OrderStatus.PREPARATION)
    		throw new DeliveryException("The order is not in the state PREPARATION");
    	this.orders.get(orderId).status = OrderStatus.ON_DELIVERY;
    	this.orders.get(orderId).deliveryTime = 15;
    	return this.orders.get(orderId).deliveryTime;
    }
    
    /**
     * Complete the delivery. The status goes from {@code ON_DELIVERY} to {@code DELIVERED}
     * 
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code ON_DELIVERY}
     */
    public void complete(int orderId) throws DeliveryException {
    	if(!this.orders.containsKey(orderId))
        	throw new DeliveryException("The ORDER ID is not valid");
    	if(this.orders.get(orderId).status != OrderStatus.ON_DELIVERY)
    		throw new DeliveryException("The order is not in the state ON_DELIVERY");
    	this.orders.get(orderId).status = OrderStatus.DELIVERED;
    }
    
    /**
     * Retrieves the total amount for all orders of a customer.
     * 
     * @param customerId the customer ID
     * @return total amount
     */
    public double totalCustomer(int customerId){
        return this.orders.values().stream()
        								.filter(order -> order.customerId == customerId)
        								.map(order -> {
											try {
												return this.totalOrder(order.orderId);
											} catch (DeliveryException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											return null;
										})
        								.collect(summingDouble(x-> x));
    }
    
    /**
     * Computes the best customers by total amount of orders.
     *  
     * @return the classification
     */
    public SortedMap<Double,List<String>> bestCustomers(){
        return this.orders.values().stream()
        								.collect(groupingBy(order -> order.customerId, HashMap::new, mapping(order -> {
											try {
												return this.totalOrder(order.orderId);
											} catch (DeliveryException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											return null;
										}, summingDouble(x -> x))))
        								.entrySet().stream()
        								.collect(groupingBy(entry -> entry.getValue(), () -> new TreeMap<>(Comparator.reverseOrder()), mapping(entry -> this.customerInfo(entry.getKey()), toList())));
    }
    
// NOT REQUIRED
//
//    /**
//     * Computes the best items by total amount of orders.
//     *  
//     * @return the classification
//     */
//    public List<String> bestItems(){
//        return null;
//    }
//    
//    /**
//     * Computes the most popular items by total quantity ordered.
//     *  
//     * @return the classification
//     */
//    public List<String> popularItems(){
//        return null;
//    }

}