package discounts;

public class Product {
	String code;
	String category;
	double price;
	int discountPercentage = 0;
	
	public Product(String code, String category, double price) {
		this.code = code;
		this.category = category;
		this.price = price;
	}
}
