package fit;

import java.util.*;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;


public class Fit {
    
    public static int MONDAY    = 1;
    public static int TUESDAY   = 2;
    public static int WEDNESDAY = 3;
    public static int THURSDAY  = 4;
    public static int FRIDAY    = 5;
    public static int SATURDAY  = 6;
    public static int SUNDAY    = 7;
    
    Map<String, Gym> gyms;
    Map<Integer, Customer> customers;
    
	public Fit() {
		this.gyms = new HashMap<>();
		this.customers = new HashMap<>();
	}
	// R1 
	
	public void addGymn (String name) throws FitException {
		if(this.gyms.containsKey(name))
			throw new FitException();
		this.gyms.put(name, new Gym(name));
	}
	
	public int getNumGymns() {
		return this.gyms.size();
	}
	
	//R2

	public void addLessons (String gymnname, 
	                        String activity, 
	                        int maxattendees, 
	                        String slots, 
	                        String ...allowedinstructors) throws FitException{
		if(!this.gyms.containsKey(gymnname))
			throw new FitException();
		if(!Stream.of(slots.split(",")).map(string -> Integer.valueOf(string.split("\\.")[0])).allMatch(day -> day >= MONDAY && day <= SUNDAY))
			throw new FitException();
		if(!Stream.of(slots.split(",")).map(string -> Integer.valueOf(string.split("\\.")[1])).allMatch(slot -> slot >= 8 && slot <= 20))
			throw new FitException();
	    if(Stream.of(slots.split(",")).anyMatch(string -> Arrays.asList(slots.split(",")).indexOf(string) != Arrays.asList(slots.split(",")).lastIndexOf(string)))
	    	throw new FitException();
	    if(Stream.of(slots.split(",")).map(string -> Map.entry(Integer.valueOf(string.split("\\.")[0]), Integer.valueOf(string.split("\\.")[1]))).anyMatch(entry -> this.gyms.get(gymnname).lessons.containsKey(entry)))
	    	throw new FitException();
	    Stream.of(slots.split(",")).map(string -> Map.entry(Integer.valueOf(string.split("\\.")[0]), Integer.valueOf(string.split("\\.")[1]))).forEach(entry -> this.gyms.get(gymnname).lessons.put(entry, new Lesson(gymnname, activity, maxattendees, entry.getValue(), entry.getKey(), Arrays.asList(allowedinstructors))));
	}
	
	//R3
	public int addCustomer(String name) {
		int code = this.customers.size() + 1;
		this.customers.put(code, new Customer(code, name));
		return code;
	}
	
	public String getCustomer (int customerid) throws FitException{
	    if(!this.customers.containsKey(customerid))
	    	throw new FitException();
	    return this.customers.get(customerid).name;
	}
	
	//R4
	
	public void placeReservation(int customerid, String gymnname, int day, int slot) throws FitException{
		if(!this.customers.containsKey(customerid))
			throw new FitException();
		if(!this.gyms.containsKey(gymnname))
			throw new FitException();
		if(!this.gyms.get(gymnname).lessons.containsKey(Map.entry(day, slot)))
			throw new FitException();
		if(this.gyms.get(gymnname).lessons.get(Map.entry(day, slot)).attendees.size() >= this.gyms.get(gymnname).lessons.get(Map.entry(day, slot)).maxAttendees)
			throw new FitException();
		if(this.gyms.get(gymnname).lessons.get(Map.entry(day, slot)).attendees.contains(customerid))
			throw new FitException();
		this.gyms.get(gymnname).lessons.get(Map.entry(day, slot)).attendees.add(customerid);
		this.customers.get(customerid).numLessons++;
	}
	
	public int getNumLessons(int customerid) {
		return this.customers.get(customerid).numLessons;
	}
	
	
	//R5
	
	public void addLessonGiven (String gymnname, int day, int slot, String instructor) throws FitException{
		if(!this.gyms.containsKey(gymnname))
			throw new FitException();
		if(!this.gyms.get(gymnname).lessons.containsKey(Map.entry(day, slot)))
			throw new FitException();
		if(!this.gyms.get(gymnname).lessons.get(Map.entry(day, slot)).instructors.contains(instructor))
			throw new FitException();
		this.gyms.get(gymnname).lessons.get((Map.entry(day, slot))).instructor = instructor;

	}
	
	public int getNumLessonsGiven (String gymnname, String instructor) throws FitException {
	    if(!this.gyms.containsKey(gymnname))
	    	throw new FitException();
	    return this.gyms.get(gymnname).lessons.values().stream()
	    								.filter(lesson -> lesson.instructor != null && lesson.instructor.equals(instructor))
	    								.map(x -> 1)
	    								.collect(summingInt(x -> x));
	}
	//R6
	
	public String mostActiveGymn() {
		return this.gyms.values().stream()
										.max(Comparator.comparing(gym -> gym.lessons.size()))
										.get().name;
	}
	
	public Map<String, Integer> totalLessonsPerGymn() {		
		return this.gyms.values().stream()
										.map(gym -> Map.entry(gym.name, gym.lessons.size()))
										.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	public SortedMap<Integer, List<String>> slotsPerNofParticipants(String gymnname) throws FitException{
		if(!this.gyms.containsKey(gymnname))
			throw new FitException();
		return this.gyms.get(gymnname).lessons.values().stream()
														.map(lesson -> Map.entry(lesson.day + "." + lesson.slot, lesson.attendees.size()))
														.collect(groupingBy(entry -> entry.getValue(), TreeMap::new, mapping(entry -> entry.getKey(), toList())));
	}
}
