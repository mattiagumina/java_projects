package delivery;

import java.util.HashMap;
import java.util.Map;

import delivery.Delivery.OrderStatus;

public class Order {

	int orderId;
	int customerId;
	Map<String, Integer> items = new HashMap<>();
	OrderStatus status;
	int deliveryTime;
	
	public Order(int orderId, int customerId) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.status = OrderStatus.NEW;
	}
	
}
