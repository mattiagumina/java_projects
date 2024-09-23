package it.polito.oop.milliways;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.*;

public class Race {
	
	String name;
	Set<String> requirments;
	
	public Race(String name) {
		this.name = name;
		this.requirments = new HashSet<>();
	}
    
	public void addRequirement(String requirement) throws MilliwaysException {
		if(this.requirments.contains(requirement))
			throw new MilliwaysException();
		this.requirments.add(requirement);
	}
	
	public List<String> getRequirements() {
        return this.requirments.stream().sorted().collect(toList());
	}
	
	public String getName() {
        return this.name;
	}
}
