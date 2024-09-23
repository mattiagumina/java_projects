package warehouse;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supplier {
	
	protected String code;
	protected String name;
	protected Map<String, Product> suppliedProducts;
	
	public Supplier(String code, String name) {
		this.code = code;
		this.name = name;
		this.suppliedProducts = new HashMap<>();
	}

	public String getCodice(){
		// TODO: completare!
		return this.code;
	}

	public String getNome(){
		// TODO: completare!
		return this.name;
	}
	
	public void newSupply(Product product){
		// TODO: completare!
		this.suppliedProducts.put(product.getCode(), product);
	}
	
	public List<Product> supplies(){
		// TODO: completare!
		return this.suppliedProducts.values().stream()
												.sorted(Comparator.comparing(product -> product.getDescription()))
												.toList();
	}
	
	public Map<String, Product> getSuppliedProducts(){
		return this.suppliedProducts;
	}
}
