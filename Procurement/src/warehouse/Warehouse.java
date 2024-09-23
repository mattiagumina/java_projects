package warehouse;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

import warehouse.Order.OrderState;

public class Warehouse {
	
	protected Map<String, Product> products;
	protected Map<String, Supplier> suppliers;
	protected Map<String, Order> orders;
	
	public Warehouse() {
		this.products = new HashMap<>();
		this.suppliers = new HashMap<>();
		this.orders = new HashMap<>();
	}
	
	public Map<String, Product> getProducts(){
		return this.products;
	}
	
	public Map<String, Supplier> getSuppliers(){
		return this.suppliers;
	}
	
	public Map<String, Order> getOrders(){
		return this.orders;
	}
	
	public Product newProduct(String code, String description){
		// TODO: completare
		Product p = new Product(this, code, description); 
		this.products.put(code, p);
		return p;
	}
	
	public Collection<Product> products(){
		// TODO: completare
		return this.products.values();
	}

	public Product findProduct(String code){
		// TODO: completare
		return this.products.get(code);
	}

	public Supplier newSupplier(String code, String name){
		// TODO: completare
		Supplier s = new Supplier(code, name);
		this.suppliers.put(code, s);
		return s;
	}
	
	public Supplier findSupplier(String code){
		// TODO: completare
		return this.suppliers.get(code);
	}

	public Order issueOrder(Product prod, int quantity, Supplier supp)
		throws InvalidSupplier {
		// TODO: completare
		String orderCode = "ORD" + String.valueOf(this.orders.size() + 1);
		if(!supp.getSuppliedProducts().containsKey(prod.getCode()))
			throw new InvalidSupplier();
		Order o = new Order(this, orderCode, prod.getCode(), supp.getCodice(), quantity);
		this.orders.put(orderCode, o);
		return o;
	}

	public Order findOrder(String code){
		// TODO: completare
		return this.orders.get(code);
	}
	
	public List<Order> pendingOrders(){
		// TODO: completare
		return this.orders.values().stream()
										.filter(order -> order.getState() != OrderState.delivered)
										.sorted(Comparator.comparing(order -> order.getProductCode()))
										.toList();
	}

	public Map<String,List<Order>> ordersByProduct(){
	    return this.orders.values().stream()
	    								.collect(groupingBy(order -> order.getProductCode(), HashMap::new, toList()));
	}
	
	public Map<String,Long> orderNBySupplier(){
	    return this.orders.values().stream()
	    								.filter(order -> order.getState() == OrderState.delivered)
	    								.collect(groupingBy(order -> this.suppliers.get(order.getSupplierCode()).getNome(), TreeMap::new, counting()));
	}
	
	public List<String> countDeliveredByProduct(){
	    return this.orders.values().stream()
	    								.filter(order -> order.getState() == OrderState.delivered)
	    								.collect(groupingBy(order -> order.getProductCode(), HashMap::new, counting()))
	    								.entrySet().stream()
	    								.sorted(Comparator.comparing(entry -> 1.0 / entry.getValue()))
	    								.map(entry -> entry.getKey() + " - " + String.valueOf(entry.getValue()))
	    								.toList();
	}
}
