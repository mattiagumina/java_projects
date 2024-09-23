package timesheet;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;

public class Timesheet {
	
	protected Map<Integer, Day> days;
	protected boolean holidaysSetted;
	protected Map<String, Project> projects;
	protected Map<String, Profile> profiles;
	protected Map<String, Worker> workers;
	protected List<Report> reports;
	
	public Timesheet() {
		this.days = new TreeMap<>();
		for(int i = 1; i <= 365; i++)
			this.days.put(i, new Day(i));
		this.holidaysSetted = false;
		this.projects = new HashMap<>();
		this.profiles = new HashMap<>();
		this.workers = new HashMap<>();
		this.reports = new ArrayList<>();
	}

	// R1
	public void setHolidays(int... holidays) {
		if(!this.holidaysSetted) {
			for(int day: holidays)
				if(day >= 1)
					this.days.get(day).setDayHoliday();
		}
		this.holidaysSetted = true;
	}
	
	public boolean isHoliday(int day) {
		if(day > 0)
			return this.days.get(day).isHoliday();
		return false;
	}
	
	public void setFirstWeekDay(int weekDay) throws TimesheetException {
		if(weekDay < 0 || weekDay > 6)
			throw new TimesheetException();
		for(int i = 1; i <= 365; i++) {
			this.days.get(i).setDayOfWeek(weekDay);
			weekDay++;
			if(weekDay == 7)
				weekDay = 0;
		}
	}
	
	public int getWeekDay(int day) throws TimesheetException {
		if(day < 1)
			throw new TimesheetException();
	    return this.days.get(day).getDayOfWeek();
	    
	}
	
	// R2
	public void createProject(String projectName, int maxHours) throws TimesheetException {
		if(maxHours < 0)
			throw new TimesheetException();
		this.projects.put(projectName, new Project(projectName, maxHours));
	}
	
	public List<String> getProjects() {
        return this.projects.values().stream()
        									.sorted(Comparator.comparing(project -> project.getName()))
        									.sorted(Comparator.comparing(project -> 1.0D / project.getMaxHours()))
        									.map(project -> project.toString())
        									.collect(toList());
	}
	
	public void createActivity(String projectName, String activityName) throws TimesheetException {
		if(!this.projects.containsKey(projectName))
			throw new TimesheetException();
		this.projects.get(projectName).getActivities().put(activityName, new Activity(activityName));
	}
	
	public void closeActivity(String projectName, String activityName) throws TimesheetException {
		if(!this.projects.containsKey(projectName) || !this.projects.get(projectName).getActivities().containsKey(activityName))
			throw new TimesheetException();
		this.projects.get(projectName).getActivities().get(activityName).setCompleted(true);
	}
	
	public List<String> getOpenActivities(String projectName) throws TimesheetException {
		if(!this.projects.containsKey(projectName))
			throw new TimesheetException();
        return this.projects.get(projectName).getActivities().values().stream()
        									.filter(activity -> !activity.isCompleted())
        									.map(activity -> activity.getName())
        									.collect(toList());
	}

	// R3
	public String createProfile(int... workHours) throws TimesheetException {
		if(workHours.length != 7)
			throw new TimesheetException();
		String idProfile = "" + (this.profiles.size() + 1);
		this.profiles.put(idProfile, new Profile(idProfile, workHours));
        return idProfile;
	}
	
	public String getProfile(String profileID) throws TimesheetException {
		if(!this.profiles.containsKey(profileID))
			throw new TimesheetException();
        return this.profiles.get(profileID).toString();
	}
	
	public String createWorker(String name, String surname, String profileID) throws TimesheetException {
		if(!this.profiles.containsKey(profileID))
			throw new TimesheetException();
		String idWorker = "" + (this.workers.size() + 1);
		this.workers.put(idWorker, new Worker(idWorker, name, surname, this.profiles.get(profileID)));
        return idWorker;
	}
	
	public String getWorker(String workerID) throws TimesheetException {
		if(!this.workers.containsKey(workerID))
			throw new TimesheetException();
        return this.workers.get(workerID).toString();
	}
	
	// R4
	public void addReport(String workerID, String projectName, String activityName, int day, int workedHours) throws TimesheetException {
		if(!this.workers.containsKey(workerID))
			throw new TimesheetException();
		if(day <= 0 || day >= 366 || this.days.get(day).isHoliday())
			throw new TimesheetException();
		if(workedHours < 0)
			throw new TimesheetException();
		if(this.workers.get(workerID).getWorkedHours()[day - 1] + workedHours > this.workers.get(workerID).getProfile().getWorkHours()[this.days.get(day).getDayOfWeek()])
			throw new TimesheetException();
		if(!this.projects.containsKey(projectName))
			throw new TimesheetException();
		if(!this.projects.get(projectName).getActivities().containsKey(activityName))
			throw new TimesheetException();
		if(this.projects.get(projectName).getWorkedHours() + workedHours > this.projects.get(projectName).getMaxHours())
			throw new TimesheetException();
		if(this.projects.get(projectName).getActivities().get(activityName).isCompleted())
			throw new TimesheetException();
		this.reports.add(new Report(workerID, projectName, activityName, day, workedHours));
		this.projects.get(projectName).incrementWorkedHours(workedHours);
		this.workers.get(workerID).incrementWorkedHours(workedHours, day - 1);
	}
	
	public int getProjectHours(String projectName) throws TimesheetException {
		if(!this.projects.containsKey(projectName))
			throw new TimesheetException();
        return this.projects.get(projectName).getWorkedHours();
	}
	
	public int getWorkedHoursPerDay(String workerID, int day) throws TimesheetException {
		if(!this.workers.containsKey(workerID) || day < 1)
			throw new TimesheetException();
        return this.workers.get(workerID).getWorkedHours()[day - 1];
	}
	
	// R5
	public Map<String, Integer> countActivitiesPerWorker() {
		Map<String, Integer> result = new HashMap<>();
        Map<Map.Entry<String, String>, List<String>> map = this.reports.stream()
        							.collect(groupingBy(report -> Map.entry(report.getProjectName(), report.getActivityName()), HashMap::new, mapping(report -> report.getWorkerId(), toList())));
        for(Worker w: this.workers.values()) {
        	int n = ((Long) map.entrySet().stream().filter(entry -> entry.getValue().contains(w.getId())).count()).intValue();
        	result.put(w.getId(), n);
        }
        return result;
	}
	
	public Map<String, Integer> getRemainingHoursPerProject() {
        return this.projects.values().stream()
        								.map(project -> Map.entry(project.getName(), project.getMaxHours() - project.getWorkedHours()))
        								.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	public Map<String, Map<String, Integer>> getHoursPerActivityPerProject() {
        return this.reports.stream()
        							.collect(groupingBy(report -> report.getProjectName(), HashMap::new, groupingBy(report -> report.getActivityName(), HashMap::new, mapping(report -> report.getWorkedHours(), summingInt(x -> x)))));
	}
}
