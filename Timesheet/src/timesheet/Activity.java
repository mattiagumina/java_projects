package timesheet;

import java.util.HashMap;
import java.util.Map;

public class Activity {
	
	protected String name;
	protected boolean completed;
	
	public Activity(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
