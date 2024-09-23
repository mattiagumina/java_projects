package it.polito.oop.elective;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;

/**
 * Manages elective courses enrollment.
 * 
 *
 */
public class ElectiveManager {
	
	Map<String, Course> courses = new TreeMap<>();
	Map<String, Student> students = new TreeMap<>();
	List<Notifier> notifiers = new ArrayList<>();

    /**
     * Define a new course offer.
     * A course is characterized by a name and a number of available positions.
     * 
     * @param name : the label for the request type
     * @param availablePositions : the number of available positions
     */
    public void addCourse(String name, int availablePositions) {
        this.courses.put(name, new Course(name, availablePositions));
    }
    
    /**
     * Returns a list of all defined courses
     * @return
     */
    public SortedSet<String> getCourses(){
        return (SortedSet<String>) this.courses.keySet();
    }
    
    /**
     * Adds a new student info.
     * 
     * @param id : the id of the student
     * @param gradeAverage : the grade average
     */
    public void loadStudent(String id, 
                                  double gradeAverage){
    	this.students.put(id, new Student(id, gradeAverage));
        
    }

    /**
     * Lists all the students.
     * 
     * @return : list of students ids.
     */
    public Collection<String> getStudents(){
        return this.students.keySet();
    }
    
    /**
     * Lists all the students with grade average in the interval.
     * 
     * @param inf : lower bound of the interval (inclusive)
     * @param sup : upper bound of the interval (inclusive)
     * @return : list of students ids.
     */
    public Collection<String> getStudents(double inf, double sup){
        return this.students.values().stream()
        								.filter(student -> student.gradeAverage >=inf && student.gradeAverage <= sup)
        								.map(student -> student.id)
        								.collect(toList());
    }


    /**
     * Adds a new enrollment request of a student for a set of courses.
     * <p>
     * The request accepts a list of course names listed in order of priority.
     * The first in the list is the preferred one, i.e. the student's first choice.
     * 
     * @param id : the id of the student
     * @param selectedCourses : a list of of requested courses, in order of decreasing priority
     * 
     * @return : number of courses the user expressed a preference for
     * 
     * @throws ElectiveException : if the number of selected course is not in [1,3] or the id has not been defined.
     */
    public int requestEnroll(String id, List<String> courses)  throws ElectiveException {
        if(courses.size() < 1 || courses.size() > 3)
        	throw new ElectiveException();
        if(!this.students.containsKey(id))
        	throw new ElectiveException();
        if(!courses.stream().allMatch(course -> this.courses.containsKey(course)))
        	throw new ElectiveException();
        this.students.get(id).desiresCourses = courses;
        this.notifiers.stream().forEach(notifier -> notifier.requestReceived(id));
        return courses.size();
    }
    
    /**
     * Returns the number of students that selected each course.
     * <p>
     * Since each course can be selected as 1st, 2nd, or 3rd choice,
     * the method reports three numbers corresponding to the
     * number of students that selected the course as i-th choice. 
     * <p>
     * In case of a course with no requests at all
     * the method reports three zeros.
     * <p>
     * 
     * @return the map of list of number of requests per course
     */
    public Map<String,List<Long>> numberRequests(){
    	Map<String,List<Long>> result = new HashMap<>();
    	Map<String, Long> firstChoiceMap = this.students.values().stream().filter(student -> student.desiresCourses.size() > 0).collect(groupingBy(student -> student.desiresCourses.get(0), HashMap::new, counting()));
    	Map<String, Long> secondChoiceMap = this.students.values().stream().filter(student -> student.desiresCourses.size() > 1).collect(groupingBy(student -> student.desiresCourses.get(1), HashMap::new, counting()));
    	Map<String, Long> thirdChoiceMap = this.students.values().stream().filter(student -> student.desiresCourses.size() > 2).collect(groupingBy(student -> student.desiresCourses.get(2), HashMap::new, counting()));
    	for(String course: this.courses.keySet()) {
    		List<Long> choices = new ArrayList<>();
    		if(firstChoiceMap.containsKey(course))
    			choices.add(firstChoiceMap.get(course));
    		else
    			choices.add(0L);
    		if(secondChoiceMap.containsKey(course))
    			choices.add(secondChoiceMap.get(course));
    		else
    			choices.add(0L);
    		if(thirdChoiceMap.containsKey(course))
    			choices.add(thirdChoiceMap.get(course));
    		else
    			choices.add(0L);
    		result.put(course, choices);
    	}
        return result;
    }
    
    
    /**
     * Make the definitive class assignments based on the grade averages and preferences.
     * <p>
     * Student with higher grade averages are assigned to first option courses while they fit
     * otherwise they are assigned to second and then third option courses.
     * <p>
     *  
     * @return the number of students that could not be assigned to one of the selected courses.
     */
    public long makeClasses() {
    	this.students.values().stream()
    								.sorted(Comparator.comparing(student -> 1.0D / student.gradeAverage))
    								.forEach(student -> {
    									if(student.desiresCourses.size() > 0 && this.courses.get(student.desiresCourses.get(0)).enrolledStudent.size() < this.courses.get(student.desiresCourses.get(0)).availablePositions) {
    										this.courses.get(student.desiresCourses.get(0)).enrolledStudent.add(student.id);
    										this.students.get(student.id).enrolledCourse = this.students.get(student.id).desiresCourses.get(0);
    									}
    									else if(student.desiresCourses.size() > 1 && this.courses.get(student.desiresCourses.get(1)).enrolledStudent.size() < this.courses.get(student.desiresCourses.get(1)).availablePositions) {
    										this.courses.get(student.desiresCourses.get(1)).enrolledStudent.add(student.id);
    										this.students.get(student.id).enrolledCourse = this.students.get(student.id).desiresCourses.get(1);
    									}
    									else if(student.desiresCourses.size() > 2 && this.courses.get(student.desiresCourses.get(2)).enrolledStudent.size() < this.courses.get(student.desiresCourses.get(2)).availablePositions) {
    										this.courses.get(student.desiresCourses.get(2)).enrolledStudent.add(student.id);
    										this.students.get(student.id).enrolledCourse = this.students.get(student.id).desiresCourses.get(2);
    									}
    									if(student.enrolledCourse != null)
    										this.notifiers.stream().forEach(notifier -> notifier.assignedToCourse(student.id, student.enrolledCourse));
    								});;
    	return this.students.values().stream().filter(student -> student.enrolledCourse == null).count();
    }
    
    
    /**
     * Returns the students assigned to each course.
     * 
     * @return the map course name vs. student id list.
     */
    public Map<String,List<String>> getAssignments(){
        return this.courses.values().stream()
        		.collect(toMap(course -> course.name, course -> course.enrolledStudent));
    }
    
    
    /**
     * Adds a new notification listener for the announcements
     * issues by this course manager.
     * 
     * @param listener : the new notification listener
     */
    public void addNotifier(Notifier listener) {
        this.notifiers.add(listener);
    }
    
    /**
     * Computes the success rate w.r.t. to first 
     * (second, third) choice.
     * 
     * @param choice : the number of choice to consider.
     * @return the success rate (number between 0.0 and 1.0)
     */
    public double successRate(int choice){
        return this.students.values().stream().filter(student -> student.desiresCourses.size() >= choice && student.enrolledCourse != null && student.enrolledCourse.equals(student.desiresCourses.get(choice - 1))).count() / Double.valueOf(this.students.size());
    }

    
    /**
     * Returns the students not assigned to any course.
     * 
     * @return the student id list.
     */
    public List<String> getNotAssigned(){
        return this.students.values().stream()
        								.filter(student -> student.enrolledCourse == null)
        								.map(student -> student.id)
        								.collect(toList());
    }
    
    
}
