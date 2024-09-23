package discounts;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Discounts {

	Map<Integer, Card> cards = new HashMap<>();
	Map<String, Product> products = new HashMap<>();
	Map<Integer, Purchase> purchases = new HashMap<>();
	
	//R1
	public int issueCard(String name) {
	    int cardId = this.cards.size() + 1;
	    this.cards.put(cardId, new Card(cardId, name));
	    return cardId;
	}
	
    public String cardHolder(int cardN) {
        return this.cards.get(cardN).clientName;
    }
    

	public int nOfCards() {
	       return this.cards.size();

	}
	
	//R2
	public void addProduct(String categoryId, String productId, double price) 
			throws DiscountsException {
		if(this.products.containsKey(productId))
			throw new DiscountsException();
		this.products.put(productId, new Product(productId, categoryId, price));
	}
	
	public double getPrice(String productId) 
			throws DiscountsException {
        if(!this.products.containsKey(productId))
        	throw new DiscountsException();
        return this.products.get(productId).price;
	}

	public int getAveragePrice(String categoryId) throws DiscountsException {
        if(!this.products.values().stream().map(p -> p.category).toList().contains(categoryId))
        	throw new DiscountsException();
        Long result = Math.round(this.products.values().stream()
        								.filter(product -> product.category.equals(categoryId))
        								.collect(averagingDouble(product -> product.price)));
        return result.intValue();
	}
	
	//R3
	public void setDiscount(String categoryId, int percentage) throws DiscountsException {
		if(!this.products.values().stream().map(p -> p.category).toList().contains(categoryId))
        	throw new DiscountsException();
		if(percentage < 0 || percentage > 50)
			throw new DiscountsException();
		this.products.values().stream()
									.filter(product -> product.category.equals(categoryId))
									.forEach(product -> product.discountPercentage = percentage);
		this.cards.values().stream()
									.forEach(card -> card.categoriesDiscount.put(categoryId, percentage));
	}

	public int getDiscount(String categoryId) {
        return this.products.values().stream()
        									.filter(product -> product.category.equals(categoryId))
        									.findAny().get().discountPercentage;
	}

	//R4
	public int addPurchase(int cardId, String... items) throws DiscountsException {
		int purchaseId = this.purchases.size() + 1;
        if(!Stream.of(items).allMatch(item -> this.products.containsKey(item.split(":")[0])))
        	throw new DiscountsException();
        Map<Product, Integer> map = Stream.of(items).map(item -> Map.entry(this.products.get(item.split(":")[0]), Integer.valueOf(item.split(":")[1]))).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.purchases.put(purchaseId, new Purchase(purchaseId, this.cards.get(cardId), map));
        return purchaseId;
	}

	public int addPurchase(String... items) throws DiscountsException {
		int purchaseId = this.purchases.size() + 1;
        if(!Stream.of(items).allMatch(item -> this.products.containsKey(item.split(":")[0])))
        	throw new DiscountsException();
        Map<Product, Integer> map = Stream.of(items).map(item -> Map.entry(this.products.get(item.split(":")[0]), Integer.valueOf(item.split(":")[1]))).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.purchases.put(purchaseId, new Purchase(purchaseId, null, map));
        return purchaseId;
	}
	
	public double getAmount(int purchaseCode) {
        return this.purchases.get(purchaseCode).totalAmount;
	}
	
	public double getDiscount(int purchaseCode)  {
        return this.purchases.get(purchaseCode).totalDiscount;
	}
	
	public int getNofUnits(int purchaseCode) {
        return this.purchases.get(purchaseCode).numberOfUnit;
	}
	
	//R5
	public SortedMap<Integer, List<String>> productIdsPerNofUnits() {
        return this.purchases.values().stream()
        									.map(purchase -> purchase.items)
        									.flatMap(map -> map.entrySet().stream())
        									.collect(groupingBy(entry -> entry.getKey().code, TreeMap::new, mapping(entry -> entry.getValue(), summingInt(x -> x))))
        									.entrySet().stream()
        									.collect(groupingBy(entry -> entry.getValue(), TreeMap::new, mapping(entry -> entry.getKey(), toList())));
	}
	
	public SortedMap<Integer, Integer> totalPurchasePerCard() {
        return this.purchases.values().stream()
        									.filter(purchase -> purchase.card != null)
        									.collect(groupingBy(purchase -> purchase.card.id, TreeMap::new, collectingAndThen(summingDouble(purchase -> purchase.totalAmount), x -> ((Long) Math.round(x)).intValue())));
	}
	
	public int totalPurchaseWithoutCard() {
        return ((Long) Math.round(this.purchases.values().stream()
        									.filter(purchase -> purchase.card == null)
        									.collect(summingDouble(purchase -> purchase.totalAmount)))).intValue();
	}
	
	public SortedMap<Integer, Integer> totalDiscountPerCard() {
        return this.purchases.values().stream()
        									.filter(purchase -> purchase.card != null && purchase.totalDiscount > 0)
        									.collect(groupingBy(purchase -> purchase.card.id, TreeMap::new, collectingAndThen(summingDouble(purchase -> purchase.totalDiscount), x -> ((Long) Math.round(x)).intValue())));
	}


}
