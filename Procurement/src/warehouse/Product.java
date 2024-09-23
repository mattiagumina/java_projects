package warehouse;

import java.util.Comparator;
import java.util.List;

import warehouse.Order.OrderState;

public class Product {
	
	protected Warehouse warehouse;
	protected String code;
	protected String description;
	protected int quantity;
	
	public Product(Warehouse warehouse, String code, String description) {
		this.warehouse = warehouse;
		this.code = code;
		this.description = description;
	}

	
	public String getCode(){
		// TODO: completare!
		return this.code;
	}

	public String getDescription(){
		// TODO: completare!
		return this.description;
	}
	
	public void setQuantity(int quantity){
		// TODO: completare!
		this.quantity = quantity;
	}

	public void decreaseQuantity(){
		// TODO: completare!
		this.quantity--;
	}

	public int getQuantity(){
		// TODO: completare!
		return this.quantity;
	}

	public List<Supplier> suppliers(){
		// TODO: completare!
		return this.warehouse.getSuppliers().values().stream()
													.filter(supplier -> supplier.getSuppliedProducts().containsKey(this.code))
													.sorted(Comparator.comparing(supplier -> supplier.getNome()))
													.toList();
	}

	public List<Order> pendingOrders(){
		// TODO: completare
		return this.warehouse.getOrders().values().stream()
													.filter(order -> order.getProductCode().equals(this.code))
													.filter(order -> order.getState() != OrderState.delivered)
													.sorted(Comparator.comparing(order -> 1.0 / order.getRequiredQuantity()))
													.toList();
	}
}
