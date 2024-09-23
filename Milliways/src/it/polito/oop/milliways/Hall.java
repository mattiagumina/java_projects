package it.polito.oop.milliways;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import static java.util.stream.Collectors.*;

public class Hall {
	
	int id;
	Set<String> facilities;
	
	public Hall(int id) {
		this.id = id;
		this.facilities = new HashSet<>();
	}

	public int getId() {
		return this.id;
	}

	public void addFacility(String facility) throws MilliwaysException {
		if(this.facilities.contains(facility))
			throw new MilliwaysException();
		this.facilities.add(facility);
	}

	public List<String> getFacilities() {
        return this.facilities.stream().sorted().collect(toList());
	}
	
	int getNumFacilities(){
        return -1;
	}

	public boolean isSuitable(Party party) {
		return party.companions.keySet().stream()
										.map(race -> race.requirments)
										.flatMap(set -> set.stream())
										.allMatch(requirment -> this.facilities.contains(requirment));
	}

}
