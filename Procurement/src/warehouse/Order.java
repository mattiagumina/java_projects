package warehouse;

import warehouse.Order.OrderState;

public class Order {
	
	public enum OrderState{notDelivered, delivered};

	protected Warehouse warehouse;
	protected String code;
	protected String productCode;
	protected String supplierCode;
	protected int requiredQuantity;
	protected OrderState state;
	
	public Order(Warehouse warehouse, String code, String productCode, String supplierCode, int requiredQuantity) {
		this.warehouse = warehouse;
		this.code = code;
		this.productCode = productCode;
		this.supplierCode = supplierCode;
		this.requiredQuantity = requiredQuantity;
		this.state = OrderState.notDelivered;
	}
	
	public String getCode(){
		// TODO: Completare!
		return this.code;
	}
	
	public OrderState getState() {
		return this.state;
	}
	
	public String getProductCode() {
		return this.productCode;
	}
	
	public int getRequiredQuantity() {
		return this.requiredQuantity;
	}
	
	public String getSupplierCode() {
		return this.supplierCode;
	}
	
	public boolean delivered(){
		// TODO: Completare!
		return this.state == OrderState.delivered;
	}

	public void setDelivered() throws MultipleDelivery {
		// TODO: Completare!
		if(this.state == OrderState.delivered)
			throw new MultipleDelivery();
		int oldQuantity = warehouse.products.get(productCode).getQuantity();
		warehouse.products.get(productCode).setQuantity(oldQuantity + requiredQuantity);
		this.state = OrderState.delivered;
	}
	
	public String toString(){
		// TODO: Completare!
		return "Order " + this.code + " for " + this.requiredQuantity + " of " + this.productCode + " : " + warehouse.getProducts().get(productCode).getDescription() + " from " + warehouse.getSuppliers().get(supplierCode).getNome();
	}
}
