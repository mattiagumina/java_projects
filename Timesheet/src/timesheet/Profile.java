package timesheet;

public class Profile {
	
	protected String id;
	protected int[] workHours;
	
	public Profile(String id, int[] workHours) {
		super();
		this.id = id;
		this.workHours = workHours;
	}

	public String getId() {
		return id;
	}

	public int[] getWorkHours() {
		return workHours;
	}

	public String toString() {
		return String.format("Sun: %d; Mon: %d; Tue: %d; Wed: %d; Thu: %d; Fri: %d; Sat: %d", workHours[0], workHours[1], workHours[2], workHours[3], workHours[4], workHours[5], workHours[6]);
	}
}
