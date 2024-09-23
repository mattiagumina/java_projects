package timesheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Worker {
	
	protected String id;
	protected String name;
	protected String surname;
	protected Profile profile;
	protected int[] workedHours;
	
	public Worker(String id, String name, String surname, Profile profile) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.profile = profile;
		this.workedHours = new int[365];
		Arrays.fill(workedHours, 0);
	}
	
	public String getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public Profile getProfile() {
		return profile;
	}
	
	public String toString() {
		return this.name + " " + this.surname + " (" + this.profile.toString() + ")";
	}
	
	public int[] getWorkedHours() {
		return this.workedHours;
	}
	
	public void incrementWorkedHours(int workedHours, int day) {
		this.workedHours[day] += workedHours;
	}

}
