package sports;

import java.util.List;
import java.util.Map;

public class Product {

	String name;
	String activity;
	Map.Entry<String, List<String>> category;
	
	public Product(String name,String activity, Map.Entry<String, List<String>> category) {
		this.name = name;
		this.activity = activity;
		this.category = category;
	}
}
