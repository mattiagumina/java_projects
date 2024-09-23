package groups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {

	String name;
	String productType;
	List<String> customers;
	List<String> suppliers;
	Map<String, Integer> bids = new HashMap<>();
	Map<String, String> votes = new HashMap<>();
	
	public Group(String name, String productType, List<String> customers) {
		this.name = name;
		this.productType = productType;
		this.customers = customers;
	}
	
}
