package delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

import java.util.Collection;

public class Order {

    private Customer customer;
    private int id;
    private Map<Item,Integer> items = new HashMap<>();
    private Delivery.OrderStatus status;
    
    public Order(int order, Customer c){
        id = order;
        customer = c;
        status = Delivery.OrderStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public int add(Item item, int qty) {
        
        if(items.containsKey(item)){
            qty += items.get(item).intValue();
        }
        
        items.put(item, qty);
        
        return qty;
    }
    
    public Collection<Map.Entry<Item, Integer>> getItems(){
        return items.entrySet();
    }
    
    public List<String> toStringList(){
        return
        items.entrySet().stream().
            map(e -> e.getKey().getDescription() + ", " + e.getValue()).collect(toList());
    }

    public double getTotal() {
        return
        items.entrySet().stream().
            mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
    }

    public Delivery.OrderStatus getStatus() {
        return status;
    }

    public void confirm() throws DeliveryException {
        if(status!=Delivery.OrderStatus.NEW) throw new DeliveryException("Wrong status");
        status = Delivery.OrderStatus.CONFIRMED;
    }
    public void start() throws DeliveryException {
        if(status!=Delivery.OrderStatus.CONFIRMED) throw new DeliveryException("Wrong status");
        status = Delivery.OrderStatus.PREPARATION;
    }
    public void deliver() throws DeliveryException {
        if(status!=Delivery.OrderStatus.PREPARATION) throw new DeliveryException("Wrong status");
        status = Delivery.OrderStatus.ON_DELIVERY;
    }
    public void complete() throws DeliveryException {
        if(status!=Delivery.OrderStatus.ON_DELIVERY) throw new DeliveryException("Wrong status");
        status = Delivery.OrderStatus.DELIVERED;
    }
    
    public int getEstimate(){
        int estimate = 0;
        switch(status){
        case CONFIRMED:   estimate = 5;
        case PREPARATION: estimate += items.keySet().stream().mapToInt(Item::getTime).max().getAsInt();
        case ON_DELIVERY: estimate += 15;
                          break;
        default: estimate = -1;
        }
        return estimate;
    }

    public Customer getCustomer() {
        return customer;
    }
}
