package it.polito.oop.milliways;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

public class Party {
	
	Map<Race, Integer> companions;
	
	public Party() {
		this.companions = new HashMap<>();
	}

    public void addCompanions(Race race, int num) {
    	this.companions.put(race, num);
	}

	public int getNum() {
        return this.companions.values().stream().collect(summingInt(x -> x));
	}

	public int getNum(Race race) {
	    return this.companions.get(race);
	}

	public List<String> getRequirements() {
        return this.companions.keySet().stream()
        								.map(race -> race.requirments)
        								.flatMap(set -> set.stream())
        								.distinct()
        								.sorted()
        								.collect(toList());
	}

    public Map<String,Integer> getDescription(){
        return this.companions.entrySet().stream()
        									.map(entry -> Map.entry(entry.getKey().name, entry.getValue()))
        									.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
