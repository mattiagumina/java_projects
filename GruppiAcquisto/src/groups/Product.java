package groups;

import java.util.List;

public class Product {

	String productType;
	List<String> supplierNames;
	
	
	public Product(String productType, List<String> supplierNames) {
		this.productType = productType;
		this.supplierNames = supplierNames;
	}
	
}
