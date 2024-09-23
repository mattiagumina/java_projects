package timesheet;

import java.util.HashMap;
import java.util.Map;

public class Project {
	
	protected String name;
	protected int maxHours;
	protected Map<String, Activity> activities;
	protected int workedHours;
	
	public Project(String name, int maxHours) {
		this.name = name;
		this.maxHours = maxHours;
		this.activities = new HashMap<>();
		this.workedHours = 0;
	}

	public String getName() {
		return name;
	}

	public int getMaxHours() {
		return maxHours;
	}
	
	public String toString() {
		return this.name + ": " + this.maxHours;
	}
	
	public Map<String, Activity> getActivities(){
		return this.activities;
	}
	
	public int getWorkedHours() {
		return this.workedHours;
	}
	
	public void incrementWorkedHours(int workedHours) {
		this.workedHours += workedHours;
	}

}
