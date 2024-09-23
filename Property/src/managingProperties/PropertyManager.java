package managingProperties;

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

import java.util.ArrayList;

import managingProperties.Building.StateApartment;
import managingProperties.Request.StateRequest;

public class PropertyManager {
	
	protected SortedMap<String, Building> buildings;
	protected SortedMap<String, Owner> owners;
	protected SortedMap<String, Integer> professions;
	protected SortedMap<String, Professional> professionals;
	protected SortedMap<Integer, Request> requests;
	
	public PropertyManager() {
		this.buildings = new TreeMap<>();
		this.owners = new TreeMap<>();
		this.professions = new TreeMap<>();
		this.professionals = new TreeMap<>();
		this.requests = new TreeMap<>();
	}

	/**
	 * Add a new building 
	 */
	public void addBuilding(String building, int n) throws PropertyException {
		if(this.buildings.containsKey(building))
			throw new PropertyException("The id has already been used");
		if(n < 1 || n > 100)
			throw new PropertyException("The number is not in the range 1 to 100");
		this.buildings.put(building, new Building(building, n));
	}

	public void addOwner(String owner, String... apartments) throws PropertyException {
		if(this.owners.containsKey(owner))
			throw new PropertyException("The id of the owner has already been defined");
		if(!Stream.of(apartments).map(string -> string.split(":")[0]).allMatch(buildingId -> this.buildings.containsKey(buildingId)))
			throw new PropertyException("The id of the building does not exist");
		if(!Stream.of(apartments).map(string -> Map.entry(string.split(":")[0], string.split(":")[1])).allMatch(entry -> this.buildings.get(entry.getKey()).getApartmentsNumber() >= Integer.valueOf(entry.getValue())))
			throw new PropertyException("The number does not correspond to an apartment");
		if(!Stream.of(apartments).map(string -> Map.entry(string.split(":")[0], Integer.valueOf(string.split(":")[1]))).allMatch(entry -> this.buildings.get(entry.getKey()).getStateApartments()[entry.getValue()] == StateApartment.free))
			throw new PropertyException("The apartment already has an owner");
		this.owners.put(owner, new Owner(owner, Arrays.asList(apartments)));
		Stream.of(apartments).map(string -> Map.entry(string.split(":")[0], Integer.valueOf(string.split(":")[1]))).forEach(entry -> this.buildings.get(entry.getKey()).getStateApartments()[entry.getValue()] = StateApartment.occupied);
	}

	/**
	 * Returns a map ( number of apartments => list of buildings ) 
	 * 
	 */
	public SortedMap<Integer, List<String>> getBuildings() {
		return this.buildings.values().stream()
									.collect(groupingBy(building -> building.getApartmentsNumber(), TreeMap::new, mapping(building -> building.getId(), toList())));
	}

	public void addProfessionals(String profession, String... professionals) throws PropertyException {
		if(this.professions.containsKey(profession))
			throw new PropertyException("The same profession has already been used in a previous invocation");
		if(Stream.of(professionals).anyMatch(professionalId -> this.professionals.containsKey(professionalId)))
			throw new PropertyException("A worker's id has already been used");
		if(Stream.of(professionals).anyMatch(professional -> Arrays.asList(professionals).lastIndexOf(professional) != Arrays.asList(professionals).indexOf(professional)))
			throw new PropertyException("A worker's id has already been used");
		this.professions.put(profession, professionals.length);
		Stream.of(professionals).forEach(professionalId -> this.professionals.put(professionalId, new Professional(professionalId, profession)));
	}

	/**
	 * Returns a map ( profession => number of workers )
	 *
	 */
	public SortedMap<String, Integer> getProfessions() {
		return this.professions;
	}

	public int addRequest(String owner, String apartment, String profession) throws PropertyException {
		int requestNumber = this.requests.size() + 1;
		if(!this.owners.containsKey(owner) || !this.buildings.containsKey(apartment.split(":")[0]) || this.buildings.get(apartment.split(":")[0]).getApartmentsNumber() < Integer.valueOf(apartment.split(":")[1]) || !this.professions.containsKey(profession))
			throw new PropertyException("The owner, the apartment, or the profession do not exist");
		if(!this.owners.get(owner).getApartments().contains(apartment))
			throw new PropertyException("The owner does not own the apartment");
		this.requests.put(requestNumber, new Request(requestNumber, owner, apartment, profession));
		return requestNumber;
	}

	public void assign(int requestN, String professional) throws PropertyException {
		if(!this.requests.containsKey(requestN))
			throw new PropertyException("The request does not exits");
		if(this.requests.get(requestN).getState() != StateRequest.pending)
			throw new PropertyException("The request is not in the pending state anymore");
		if(!this.professionals.get(professional).getProfession().equals(this.requests.get(requestN).getProfession()))
			throw new PropertyException("The worker does not exercise the profession required by the request");
		this.requests.get(requestN).setState(StateRequest.assigned);
		this.requests.get(requestN).setProfessional(professional);
	}

	public List<Integer> getAssignedRequests() {
		return this.requests.values().stream()
											.filter(request -> request.getState() == StateRequest.assigned)
											.map(request -> request.getRequestNumber())
											.sorted(Comparator.comparing(n -> n))
											.toList();
	}

	
	public void charge(int requestN, int amount) throws PropertyException {
		if(!this.requests.containsKey(requestN))
			throw new PropertyException("The requests does not exits");
		if(this.requests.get(requestN).getState() != StateRequest.assigned)
			throw new PropertyException("The request is not in the assigned state");
		if(amount < 0 || amount > 1000)
			throw new PropertyException("The sum is not in the 0 to 1000 range");
		this.requests.get(requestN).setExpense(amount);
		this.requests.get(requestN).setState(StateRequest.completed);
		
	}

	/**
	 * Returns the list of request ids
	 * 
	 */
	public List<Integer> getCompletedRequests() {
		return this.requests.values().stream()
											.filter(request -> request.getState() == StateRequest.completed)
											.map(request -> request.getRequestNumber())
											.sorted(Comparator.comparing(m -> m))
											.toList();
	}
	
	/**
	 * Returns a map ( owner => total expenses )
	 * 
	 */
	public SortedMap<String, Integer> getCharges() {
		return this.requests.values().stream()
											.filter(request -> request.getState() == StateRequest.completed && request.getExpense() > 0)
											.collect(groupingBy(request -> request.getOwner(), TreeMap::new, mapping(request -> request.getExpense(), summingInt(x -> x))));
	}

	/**
	 * Returns the map ( building => ( profession => total expenses) ).
	 * Both buildings and professions are sorted alphabetically
	 * 
	 */
	public SortedMap<String, Map<String, Integer>> getChargesOfBuildings() {
		return this.requests.values().stream()
											.filter(request -> request.getState() == StateRequest.completed && request.getExpense() > 0)
											.collect(groupingBy(request -> request.getBuilding(), TreeMap::new, groupingBy(request -> request.getProfessional(), TreeMap::new, mapping(request -> request.getExpense(), summingInt(x -> x)))));
	}

}
