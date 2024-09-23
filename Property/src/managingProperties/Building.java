package managingProperties;

import java.util.Arrays;
import java.util.stream.Stream;

public class Building {
	
	public enum StateApartment{free, occupied};

	protected String id;
	protected int apartmentsNumber;
	protected StateApartment[] stateApartments;
	
	public Building(String id, int apartmentsNumber) {
		this.id = id;
		this.apartmentsNumber = apartmentsNumber;
		this.stateApartments = new StateApartment[apartmentsNumber];
		Arrays.fill(stateApartments, StateApartment.free);
	}

	public String getId() {
		return id;
	}

	public int getApartmentsNumber() {
		return apartmentsNumber;
	}

	public StateApartment[] getStateApartments() {
		return stateApartments;
	}
	
}
