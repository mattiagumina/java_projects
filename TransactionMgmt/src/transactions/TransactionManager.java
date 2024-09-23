package transactions;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
//import static java.util.Comparator.*;

public class TransactionManager {
	Map<String, Place> places = new HashMap<>();
	Map<String, Carrier> carriers = new HashMap<>();
	Map<String, Request> requests = new HashMap<>();
	Map<String, Offer> offers = new HashMap<>();
	Map<String, Transaction> transactions = new HashMap<>();	
//R1
	public List<String> addRegion(String regionName, String... placeNames) {
		List<String> toAddPlace = Stream.of(placeNames).filter(place -> !this.places.containsKey(place)).distinct().sorted().collect(toList());
		toAddPlace.stream().forEach(place -> this.places.put(place, new Place(place,regionName)));
		return toAddPlace;
	}
	
	public List<String> addCarrier(String carrierName, String... regionNames) { 
		List<String> suppliedRegions = Stream.of(regionNames).distinct().filter(region -> this.places.values().stream().map(place -> place.region).toList().contains(region)).sorted().collect(toList());
		this.carriers.put(carrierName, new Carrier(carrierName, suppliedRegions));
		return suppliedRegions;
	}
	
	public List<String> getCarriersForRegion(String regionName) { 
		return this.carriers.values().stream()
											.filter(carrier -> carrier.regions.contains(regionName))
											.map(carrier -> carrier.name)
											.sorted()
											.collect(toList());
	}
	
//R2
	public void addRequest(String requestId, String placeName, String productId) 
			throws TMException {
		if(!this.places.containsKey(placeName))
			throw new TMException();
		if(this.requests.containsKey(requestId))
			throw new TMException();
		this.requests.put(requestId, new Request(requestId, placeName, productId));
	}
	
	public void addOffer(String offerId, String placeName, String productId) 
			throws TMException {
		if(this.offers.containsKey(offerId))
			throw new TMException();
		if(!this.places.containsKey(placeName))
			throw new TMException();
		this.offers.put(offerId, new Offer(offerId, placeName, productId));
	}
	

//R3
	public void addTransaction(String transactionId, String carrierName, String requestId, String offerId) 
			throws TMException {
		if(this.transactions.values().stream().map(transaction -> transaction.requestId).toList().contains(requestId))
			throw new TMException();
		if(this.transactions.values().stream().map(transaction -> transaction.offerId).toList().contains(offerId))
			throw new TMException();
		if(!this.requests.get(requestId).productId.equals(this.offers.get(offerId).productId))
			throw new TMException();
		if(!this.carriers.get(carrierName).regions.contains(this.places.get(this.requests.get(requestId).place).region))
			throw new TMException();
		if(!this.carriers.get(carrierName).regions.contains(this.places.get(this.offers.get(offerId).place).region))
			throw new TMException();
		this.transactions.put(transactionId, new Transaction(transactionId, carrierName, requestId, offerId));
	}
	
	public boolean evaluateTransaction(String transactionId, int score) {
		if(score < 1 || score > 10)
			return false;
		this.transactions.get(transactionId).score = score;
		return true;
	}
	
//R4
	public SortedMap<Long, List<String>> deliveryRegionsPerNT() {
		return this.transactions.values().stream()
											.collect(groupingBy(transaction -> this.places.get(this.requests.get(transaction.requestId).place).region, TreeMap::new, counting()))
											.entrySet().stream()
											.collect(groupingBy(entry -> entry.getValue(), () -> new TreeMap<>(Comparator.reverseOrder()), mapping(entry -> entry.getKey(), toList())));
	}
	
	public SortedMap<String, Integer> scorePerCarrier(int minimumScore) {
		return this.transactions.values().stream()
											.filter(transaction -> transaction.score >= minimumScore)
											.collect(groupingBy(transaction -> transaction.carrier, TreeMap::new, summingInt(transaction -> transaction.score)));
	}
	
	public SortedMap<String, Long> nTPerProduct() {
		return this.transactions.values().stream()
											.collect(groupingBy(transaction -> this.requests.get(transaction.requestId).productId, TreeMap::new, counting()));
	}
	
	
}

