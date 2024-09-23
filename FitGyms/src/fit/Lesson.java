package fit;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
	
	String gymName;
	String activity;
	int maxAttendees;
	int slot;
	int day;
	List<String> instructors;
	List<Integer> attendees;
	String instructor;
	
	public Lesson(String gymName, String activity, int maxAttendees, int slot, int day, List<String> instructors) {
		this.gymName = gymName;
		this.activity = activity;
		this.maxAttendees = maxAttendees;
		this.slot = slot;
		this.day = day;
		this.instructors = instructors;
		this.attendees = new ArrayList<>();
		this.instructor = null;
	}
	
}
