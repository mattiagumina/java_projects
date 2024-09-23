package managingProperties;

import java.util.*;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;


public class PropertyManager {
	
	private Map<String, Building> buildings = new TreeMap<>();
	private Map<String, List<String>> owners = new HashMap<>(); //basta un treeSet di owner id 
	private Map<String, String> aptOwner = new HashMap<>(); //mappa appartamento owner
//	private SortedMap<String, Integer> professions = new TreeMap<>();
	private Map<String, String> professionals = new HashMap<>();
	private Map<Integer, Request> requests = new TreeMap<>();
	
	private boolean checkApartment(String apartment) {
		String[] items = apartment.split(":");
		String building = items[0]; 
		int n = Integer.parseInt(items[1]);
		boolean res = buildings.containsKey(building)  
				  && n >= 1 && n <= buildings.get(building).getN();
		return res;
	}
	public void addBuilding(String building, int n) throws PropertyException {
		if (buildings.containsKey(building) 
				|| n < 1 || n > 100) 
			throw new PropertyException ("");
		buildings.put(building, new Building(building, n));
	}

	public void addOwner(String owner, String... apartments) throws PropertyException {
		if (owners.containsKey(owner) ) throw new PropertyException ("");

		ArrayList<String> aptList = new ArrayList<>();
		for (String apartment:apartments) {
			if (!checkApartment(apartment)) throw new PropertyException ("");
			if (aptOwner.containsKey(apartment)) throw new PropertyException ("");
			aptList.add(apartment);
			aptOwner.put(apartment, owner);
		}
		owners.put(owner, aptList);
	}

	public SortedMap<Integer, List<String>> getBuildings() {
		SortedMap<Integer, List<String>> res = 
		buildings.values().stream()
		.sorted(comparing(Building::getId))
		.collect(groupingBy(Building::getN, 
							TreeMap::new, 
							mapping(Building::getId,toList())
							));
		return res;
	}

	public void addProfessionals(String profession, String... professionals) throws PropertyException {
		if( this.professionals.values().stream().anyMatch( s -> s.equals(profession)) ) {
			throw new PropertyException ("");
		}
		
		for (String professional: professionals) {
			if ( this.professionals.containsKey(professional) ) 
				throw new PropertyException ("");
			this.professionals.put(professional, profession);
		}
	}

	public SortedMap<String, Integer> getProfessions() {
		
		return professionals.values().stream()
		.collect(groupingBy( p -> p,
				TreeMap::new,
				collectingAndThen(counting(), l -> l.intValue())
				// OR (tricky version)
				//summingInt( p -> 1)
				));
		
	}

	int nRequest = 0;
	public int addRequest(String owner, String apartment, String profession) throws PropertyException {
		if (!owners.containsKey(owner) 
				|| !checkApartment(apartment) 
				|| !owners.get(owner).contains(apartment)
				|| this.professionals.values().stream().noneMatch( s -> s.equals(profession)) ) 
			throw new PropertyException (owner + " " + apartment + " " + profession);
		
		int id = ++nRequest;
		requests.put(id, new Request(id, owner, apartment, profession));
		return id;
	}

	public void assign(int requestN, String professional) throws PropertyException {
		if (!requests.containsKey(requestN)  
			|| !professionals.containsKey(professional)) 
				throw new PropertyException ("");
		
		Request req = requests.get(requestN);
		if (!req.getProfession().equals(professionals.get(professional))) 
			throw new PropertyException ("");
		req.assign(professional);
	}

	public List<Integer> getAssignedRequests() {
		return requests.values().stream()
				.filter(Request::isAssigned)
				.map(Request::getId).collect(toList());
	}

	public void charge(int requestN, int amount) throws PropertyException {
		if (!requests.containsKey(requestN) 
			|| !requests.get(requestN).isAssigned()
			|| amount < 0 || amount > 1000) 
				throw new PropertyException ("");
		
		
		requests.get(requestN).charge(amount);
	}

	public List<Integer> getCompletedRequests() {
		return requests.values().stream()
				.filter(Request::isCompleted)
				.map(Request::getId).collect(toList());
	}


	public SortedMap<String, Integer> getCharges() {
		SortedMap<String, Integer> res = 
		requests.values().stream()
		.filter(Request::isCompleted)
		.collect(groupingBy(Request::getOwner, 
							TreeMap::new, 
							summingInt(Request::getAmount)));	
		return res;
	}
				// building      profess  amount
	public SortedMap<String, Map<String, Integer>> getChargesOfBuildings() {
		SortedMap<String, Map<String, Integer>> res = 		
		requests.values().stream()
		.filter(Request::isCompleted)
		.filter( r -> r.getAmount()>0)
		.collect(groupingBy(Request::getBuilding, 
							TreeMap::new, 
							groupingBy(Request::getProfessional,
										TreeMap::new,
										summingInt(Request::getAmount))));
		return res;
	}

}

