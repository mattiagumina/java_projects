package it.polito.oop.milliways;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;


public class Restaurant {
	
	Map<String, Race> races;
	List<Party> parties;
	Map<Integer, Hall> halls;
	List<Map.Entry<Hall, Party>> accomodates;

    public Restaurant() {
    	this.races = new HashMap<>();
    	this.parties = new ArrayList<>();
    	this.halls = new LinkedHashMap<>();
    	this.accomodates = new ArrayList<>();
	}
	
	public Race defineRace(String name) throws MilliwaysException{
	    if(this.races.containsKey(name))
	    	throw new MilliwaysException();
	    Race r = new Race(name);
	    this.races.put(name, r);
	    return r;
	}
	
	public Party createParty() {
		Party p = new Party();
		this.parties.add(p);
	    return p;
	}
	
	public Hall defineHall(int id) throws MilliwaysException{
	    if(this.halls.containsKey(id))
	    	throw new MilliwaysException();
	    Hall h = new Hall(id);
	    this.halls.put(id, h);
	    return h;
	}

	public List<Hall> getHallList() {
		return this.halls.values().stream().collect(toList());
	}

	public Hall seat(Party party, Hall hall) throws MilliwaysException {
        if(!hall.isSuitable(party))
        	throw new MilliwaysException();
        this.accomodates.add(Map.entry(hall, party));
        return hall;
	}

	public Hall seat(Party party) throws MilliwaysException {
        Hall suitableHall = this.halls.values().stream()
        									.filter(hall -> hall.isSuitable(party))
        									.findFirst().orElse(null);
        if(suitableHall == null)
        	throw new MilliwaysException();
        this.accomodates.add(Map.entry(suitableHall, party));
        return suitableHall;
	}

	public Map<Race, Integer> statComposition() {
        return this.accomodates.stream()
        						.map(entry -> entry.getValue().companions)
        						.flatMap(map -> map.entrySet().stream())
        						.collect(groupingBy(entry -> entry.getKey(), HashMap::new, mapping(entry -> entry.getValue(), summingInt(x -> x))));
	}

	public List<String> statFacility() {
        return this.halls.values().stream()
        								.map(hall -> hall.facilities)
        								.flatMap(set -> set.stream())
        								.collect(groupingBy(facility -> facility, TreeMap::new, counting()))
        								.entrySet().stream()
        								.sorted(Comparator.comparing(entry -> 1.0 / entry.getValue()))
        								.map(entry -> entry.getKey())
        								.collect(toList());
	}
	
	public Map<Integer,List<Integer>> statHalls() {
        return this.halls.values().stream()
        								.sorted(Comparator.comparing(hall -> hall.id))
        								.collect(groupingBy(hall -> hall.facilities.size(), TreeMap::new, mapping(hall -> hall.id, toList())));
	}

}
