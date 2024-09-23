package it.polito.oop.futsal;

import java.util.HashMap;
import java.util.Map;

import it.polito.oop.futsal.Fields.Features;

public class Field implements FieldOption {
	int id;
	Features features;
	Map<String, Integer> bookings;
	
	public Field(int id, Features features) {this.id = id;
	this.features = features;
	this.bookings= new HashMap<>();
	}

	@Override
	public int getField() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public int getOccupation() {
		// TODO Auto-generated method stub
		return this.bookings.size();
	}

}
