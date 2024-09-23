package delivery;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;



public class Delivery {
    
    public static enum OrderStatus { NEW, CONFIRMED, PREPARATION, ON_DELIVERY, DELIVERED } 
    
    private LinkedHashMap<String,Customer> customers = new LinkedHashMap<>();
    private List<Item> items = new LinkedList<>();
    private int order = 1;
    private LinkedList<Order> orders = new LinkedList<>();

    /**
     * Creates a new customer entry and returns the corresponding unique ID.
     * 
     * 
     * @param name name of the customer
     * @param address customer address
     * @param phone customer phone number
     * @param email customer email
     * @return unique customer ID (positive integer)
     */
    public int newCustomer(String name, String address, String phone, String email) throws DeliveryException {
        
        if(customers.containsKey(email)) throw new DeliveryException("Duplicate customer");
        
        Customer c = new Customer(name,address,phone,email);
        customers.put(email, c);
        
        return customers.size();
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
        Customer c = customers.values().stream().skip(customerId-1).findFirst().orElse(null);
        if(c==null) return "";
        return c.toString();
    }
    
    /**
     * Returns the list of customers, sorted by name
     * 
     */
    public List<String> listCustomers(){
        return customers.values().stream().
                sorted(comparing(Customer::getName)).
                map(Customer::toString).
                collect(toList());
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
        Item it = new Item(description,price,category,prepTime);
        items.add(it);
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
        Stream<Item> its=items.stream();
        if(search.length()>0){
            its = its.filter(it -> it.match(search));
        }
        return its.sorted(comparing(Item::getCategory).thenComparing(Item::getDescription))
                .map(Item::toString).collect(toList());
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
        Customer c = customers.values().stream().skip(customerId-1).findFirst().orElse(null);
        Order o = new Order(this.order, c);
        orders.add(o);
        return this.order++;
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
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        List<Item> item = items.stream().
                filter(it -> it.match(search)).
                collect(toList());
        if(item.size()!=1) throw new DeliveryException("Search string must match exactly one item");
        return o.add(item.get(0),qty);
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
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        return o.toStringList();
    }
    
    /**
     * Retrieves the total amount of the order.
     * 
     * @param orderId the order ID
     * @return the total amount of the order
     * @throws DeliveryException when the order ID in invalid
     */
    public double totalOrder(int orderId) throws DeliveryException {
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        return o.getTotal();
    }
    
    /**
     * Retrieves the status of an order
     * 
     * @param orderId the order ID
     * @return the current status of the order
     * @throws DeliveryException when the id is invalid
     */
    public OrderStatus getStatus(int orderId) throws DeliveryException {
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        return o.getStatus();
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
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        o.confirm();
        return o.getEstimate();
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
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        o.start();
        return o.getEstimate();
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
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        o.deliver();
        return o.getEstimate();
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
        if(orderId<1 || orderId>orders.size()) throw new DeliveryException("Wrong order id: " + orderId);
        Order o = orders.get(orderId-1);
        o.complete();
    }
    
    /**
     * Retrieves the total amount for all orders of a customer.
     * 
     * @param customerId the customer ID
     * @return total amount
     */
    public double totalCustomer(int customerId){
        Customer c = customers.values().stream().skip(customerId-1).findFirst().orElse(null);
        return orders.stream().
                filter(o -> o.getCustomer() == c).
                filter(o->o.getStatus()!=OrderStatus.NEW).
                //peek(o -> System.out.println(o.getCustomer().getName() + " : " + o.getTotal() + " - " + o.getStatus())).
                mapToDouble(Order::getTotal).sum();
    }
    
    /**
     * Computes the best customers by total amount of orders.
     *  
     * @return the classification
     */
    public SortedMap<Double,List<String>> bestCustomers(){
        
        Map<String,Double> totals = orders.stream().
                filter(o->o.getStatus()!=OrderStatus.NEW).
                collect(groupingBy(o->o.getCustomer().toString(),summingDouble(Order::getTotal)));
        return totals.entrySet().stream().collect(groupingBy(
                                                  e -> e.getValue(),
                                                  () -> new TreeMap<Double,List<String>>(reverseOrder()),
                                                  mapping(e -> e.getKey(),toList())
                                                    ));
    }
    
    /**
     * Computes the best items by total amount of orders.
     *  
     * @return the classification
     */
    public List<String> bestItems(){
        return orders.stream().
                filter(o->o.getStatus()!=OrderStatus.NEW).
                flatMap(o -> o.getItems().stream()).
                collect(groupingBy(e->e.getKey().getDescription(),
                                    summingDouble(e->e.getValue()*e.getKey().getPrice())
                        )).
                entrySet().stream().
                sorted(comparing(e->-e.getValue())).
                map(r -> r.getKey() + ", " +  String.format("%.2f", r.getValue())).
                collect(toList())
                ;
    }
    
    /**
     * Computes the most popular items by total quantity ordered.
     *  
     * @return the classification
     */
    public List<String> popularItems(){
        return orders.stream().
                filter(o->o.getStatus()!=OrderStatus.NEW).
                flatMap(o -> o.getItems().stream()).
                collect(groupingBy(e->e.getKey().getDescription(),
                                    summingInt(e->e.getValue())
                        )).
                entrySet().stream().
                sorted(comparing(e->-e.getValue())).
                map(r -> r.getKey() + ", " +  r.getValue()).
                collect(toList())
                ;
    }

}
