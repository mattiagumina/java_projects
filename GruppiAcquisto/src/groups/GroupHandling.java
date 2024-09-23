package groups;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;


public class GroupHandling {
	
	Map<String, Product> products = new HashMap<>();
	Set<String> suppliers = new HashSet<>();
	Map<String, Group> groups = new HashMap<>();
	
//R1	
	public void addProduct (String productTypeName, String... supplierNames) 
			throws GroupHandlingException {
		if(this.products.containsKey(productTypeName))
			throw new GroupHandlingException("Il nome del tipo del prodotto è duplicato");
		this.products.put(productTypeName, new Product(productTypeName, Arrays.asList(supplierNames)));
		Stream.of(supplierNames).forEach(supplier -> this.suppliers.add(supplier));
	}
	
	public List<String> getProductTypes (String supplierName) {
		return this.products.values().stream()
										.filter(product -> product.supplierNames.contains(supplierName))
										.map(product -> product.productType)
										.sorted()
										.toList();
	}
	
//R2	
	public void addGroup (String name, String productName, String... customerNames) 
			throws GroupHandlingException {
		if(this.groups.containsKey(name))
			throw new GroupHandlingException("Il nome del gruppo è duplicato");
		if(!this.products.containsKey(productName))
			throw new GroupHandlingException("Il tipo di prodotto non è stato definito");
		this.groups.put(name, new Group(name, productName, Arrays.asList(customerNames)));
	}
	
	public List<String> getGroups (String customerName) {
        return this.groups.values().stream()
        								.filter(group -> group.customers.contains(customerName))
        								.map(group -> group.name)
        								.sorted()
        								.toList();
	}

//R3
	public void setSuppliers (String groupName, String... supplierNames)
			throws GroupHandlingException {
		if(!Stream.of(supplierNames).allMatch(supplier -> this.suppliers.contains(supplier)))
			throw new GroupHandlingException("Un fornitore risulta indefinito");
		if(!Stream.of(supplierNames).allMatch(supplier -> this.getProductTypes(supplier).contains(this.groups.get(groupName).productType)))
			throw new GroupHandlingException("Un fornitore non tratta il tipo di prodotto richiesto dal gruppo");
		this.groups.get(groupName).suppliers = Arrays.asList(supplierNames);
	}
	
	public void addBid (String groupName, String supplierName, int price)
			throws GroupHandlingException {
		if(!this.groups.get(groupName).suppliers.contains(supplierName))
			throw new GroupHandlingException("Il fornitore non è collegato al gruppo");
		this.groups.get(groupName).bids.put(supplierName, price);
	}
	
	public String getBids (String groupName) {
        return this.groups.get(groupName).bids.entrySet().stream()
        									.sorted(Comparator.comparing(entry -> entry.getKey()))
        									.sorted(Comparator.comparing(entry -> entry.getValue()))
        									.map(entry -> entry.getKey() + ":" + entry.getValue())
        									.reduce("", (s1, s2) -> s1 + "," + s2).substring(1);
	}
	
	
//R4	
	public void vote (String groupName, String customerName, String supplierName)
			throws GroupHandlingException {
		if(!this.groups.get(groupName).customers.contains(customerName))
			throw new GroupHandlingException("Il cliente non partecipa al gruppo");
		if(!this.groups.get(groupName).bids.containsKey(supplierName))
			throw new GroupHandlingException("Il fornitore non ha presentato un'offerta per il gruppo");
		this.groups.get(groupName).votes.put(customerName, supplierName);
	}
	
	public String  getVotes (String groupName) {
        return this.groups.get(groupName).votes.entrySet().stream()
        									.collect(groupingBy(entry -> entry.getValue(), TreeMap::new, counting()))
        									.entrySet().stream()
        									.map(entry -> entry.getKey() + ":" + entry.getValue())
        									.reduce("", (s1, s2) -> s1 + "," + s2).substring(1);
	}
	
	public String getWinningBid (String groupName) {
        if(this.groups.get(groupName).bids.isEmpty())
        	return null;
        return Arrays.asList(this.getVotes(groupName).split(",")).stream()
        									.map(string -> Map.entry(string.split(":")[0], Integer.valueOf(string.split(":")[1])))
        									.sorted(Comparator.comparing(entry -> this.groups.get(groupName).bids.get(entry.getKey())))
        									.sorted(Comparator.comparing(entry -> entry.getValue()))
        									.map(entry -> entry.getKey() + ":" + entry.getValue())
        									.findFirst().get();
	}
	
//R5
	public SortedMap<String, Integer> maxPricePerProductType() { //serve toMap
        return this.groups.values().stream()
        									.filter(group -> group.bids.size() > 0)
        									.map(group -> Map.entry(group.productType, group.bids.values().stream().collect(maxBy(Comparator.comparing(x -> x))).get()))
        									.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, Math::max, TreeMap::new));
	}
	
	public SortedMap<Integer, List<String>> suppliersPerNumberOfBids() {
        return this.groups.values().stream()
        									.map(group -> group.bids)
        									.flatMap(map -> map.entrySet().stream())
        									.collect(groupingBy(entry -> entry.getKey(), TreeMap::new, toList()))
        									.entrySet().stream()
        									.collect(groupingBy(entry -> entry.getValue().size(), () -> new TreeMap<>(Comparator.reverseOrder()), mapping(entry -> entry.getKey(), toList())));
	}
	
	public SortedMap<String, Long> numberOfCustomersPerProductType() {
        return this.groups.values().stream()
        									.collect(groupingBy(group -> group.productType, TreeMap::new, mapping(group -> group.customers.size(), summingLong(x -> x))));
	}
	
}
