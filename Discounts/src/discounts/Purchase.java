package discounts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

public class Purchase {
	
	int id;
	Card card;
	Map<Product, Integer> items = new HashMap<>();
	double totalAmount;
	double totalDiscount = 0;
	int numberOfUnit = 0;
	
	public Purchase(int id, Card card, Map<Product, Integer> items) {
		this.id = id;
		this.card = card;
		this.items = items;
		this.totalAmount = this.items.entrySet().stream()
													.map(entry -> {
														this.numberOfUnit += entry.getValue();
														if(card != null && card.categoriesDiscount.containsKey(entry.getKey().category)) {
															this.totalDiscount += (entry.getKey().price * card.categoriesDiscount.get(entry.getKey().category) / 100.0D) * entry.getValue();
															return entry.getKey().price * (1 - card.categoriesDiscount.get(entry.getKey().category) / 100.0D) * entry.getValue();
														}
														else
															return entry.getKey().price * entry.getValue();
													})
													.collect(summingDouble(x -> x));
	}
}
