package timesheet;

public class Report {
	
	protected String workerId;
	protected String projectName;
	protected String activityName;
	protected int day;
	protected int workedHours;
	
	public Report(String workerId, String projectName, String activityName, int day, int workedHours) {
		super();
		this.workerId = workerId;
		this.projectName = projectName;
		this.activityName = activityName;
		this.day = day;
		this.workedHours = workedHours;
	}

	public String getWorkerId() {
		return workerId;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getActivityName() {
		return activityName;
	}

	public int getDay() {
		return day;
	}

	public int getWorkedHours() {
		return workedHours;
	}

}
