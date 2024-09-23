package discounts;

import java.util.HashMap;
import java.util.Map;

public class Card {
	int id;
	String clientName;
	Map<String, Integer> categoriesDiscount = new HashMap<>();
	
	public Card(int id, String clientName) {
		this.id = id;
		this.clientName = clientName;
	}
}
