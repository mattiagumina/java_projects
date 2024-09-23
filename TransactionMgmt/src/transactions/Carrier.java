package transactions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Carrier {

	String name;
	Set<String> regions = new HashSet<>();
	
	public Carrier(String name, List<String> regions) {
		this.name = name;
		regions.stream().forEach(region -> this.regions.add(region));
	}
}
