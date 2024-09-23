package it.polito.oop.futsal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

import java.util.Comparator;

/**
 * Represents a infrastructure with a set of playgrounds, it allows teams
 * to book, use, and  leave fields.
 *
 */
public class Fields {
	   
    public static class Features {
        public final boolean indoor; // otherwise outdoor
        public final boolean heating;
        public final boolean ac;
        public Features(boolean i, boolean h, boolean a) {
            this.indoor=i; this.heating=h; this.ac = a;
        }
    }
    
    String openingTime, closingTime;
    Map<Integer, Field> fields = new HashMap<>();
    Map<Integer, Associate> associates = new HashMap<>();
    
    public void defineFields(Features... features) throws FutsalException {
    	int fieldId = this.fields.size() + 1;
    	for(Features f: features) {
    		if(!f.indoor && (f.heating || f.ac))
    			throw new FutsalException();
    		this.fields.put(fieldId, new Field(fieldId, f));
    		fieldId++;
    	}
        
    }
    
    public long countFields() {
        return this.fields.size();
    }

    public long countIndoor() {
        return this.fields.values().stream().filter(field -> field.features.indoor).count();
    }
    
    public String getOpeningTime() {
        return this.openingTime;
    }
    
    public void setOpeningTime(String time) {
    	this.openingTime = time;
    }
    
    public String getClosingTime() {
        return this.closingTime;
    }
    
    public void setClosingTime(String time) {
    	this.closingTime = time;
    }

    public int newAssociate(String first, String last, String mobile) {
        int associateId = this.associates.size() + 1;
        this.associates.put(associateId, new Associate(associateId, first, last, mobile));
        return associateId;
    }
    
    public String getFirst(int partyId) throws FutsalException {
        if(!this.associates.containsKey(partyId))
        	throw new FutsalException();
        return this.associates.get(partyId).name;
    }
    
    public String getLast(int associate) throws FutsalException {
    	if(!this.associates.containsKey(associate))
        	throw new FutsalException();
        return this.associates.get(associate).surname;
    }
    
    public String getPhone(int associate) throws FutsalException {
    	if(!this.associates.containsKey(associate))
        	throw new FutsalException();
        return this.associates.get(associate).phone;
    }
    
    public int countAssociates() {
        return this.associates.size();
    }
    
    public void bookField(int field, int associate, String time) throws FutsalException {
    	if(!this.fields.containsKey(field))
    		throw new FutsalException();
    	if(!this.associates.containsKey(associate))
    		throw new FutsalException();
    	if(time.compareTo(openingTime) < 0 || time.compareTo(closingTime) >= 0)
    		throw new FutsalException();
    	if(!time.split(":")[1].equals(openingTime.split(":")[1]))
    		throw new FutsalException();
    	this.fields.get(field).bookings.put(time, associate);
    }

    public boolean isBooked(int field, String time) {
        return this.fields.get(field).bookings.containsKey(time);
    }
    

    public int getOccupation(int field) {
        return this.fields.get(field).bookings.size();
    }
    
    
    public List<FieldOption> findOptions(String time, Features required){
        return this.fields.values().stream()
        								.filter(field -> !this.isBooked(field.id, time))
        								.filter(field -> required.indoor ? field.features.indoor : true)
        								.filter(field -> required.heating ? field.features.heating : true)
        								.filter(field -> required.ac ? field.features.ac : true)
        								.sorted(Comparator.comparing(field -> field.getField()))
        								.sorted(Comparator.comparing(field -> field.getOccupation()))
        								.collect(toList());
    }
    
    public long countServedAssociates() {
        return this.fields.values().stream()
        								.map(field -> field.bookings)
        								.flatMap(map -> map.values().stream())
        								.distinct()
        								.count();
    }
    
    public Map<Integer,Long> fieldTurnover() {
        return this.fields.values().stream()
        									.map(field -> Map.entry(field.getField(), Long.valueOf(field.getOccupation())))
        									.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public double occupation() {
    	double slotNumber = (Integer.valueOf(this.closingTime.split(":")[0]) - Integer.valueOf(this.openingTime.split(":")[0])) * this.fields.size();
    	double bookingNumber = this.fields.values().stream().map(field -> field.getOccupation()).collect(summingInt(x -> x));
        return bookingNumber / slotNumber;
    }
    
}