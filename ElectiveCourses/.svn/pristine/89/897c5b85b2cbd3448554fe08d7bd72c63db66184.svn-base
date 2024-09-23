package it.polito.oop.elective;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.ArrayList;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

/**
 * Manages elective courses enrollment.
 * 
 *
 */
public class ElectiveManager {
    
    private TreeMap<String,Course> courses = new TreeMap<>();
    private TreeMap<String, Student> students = new TreeMap<>();
    private List<Notifier> listeners = new LinkedList<>();

    /**
     * Define a new course offer.
     * A course is characterized by a name and a number of available positions.
     * 
     * @param name : the label for the request type
     * @param availablePositions : the number of available positions
     */
    public void addCourse(String name, int availablePositions) {
        courses.put(name, new Course(name,availablePositions));
    }
    
    /**
     * Returns a list of all defined courses
     * @return
     */
    public SortedSet<String> getCourses(){
        return courses.navigableKeySet();
    }
    
    /**
     * Adds a new student info.
     * 
     * @param id : the id of the student
     * @param gradeAverage : the grade average
     */
    public void loadStudent(String id, double gradeAverage){
        students.put(id,new Student(id,gradeAverage));
    }

    /**
     * Lists all the students.
     * 
     * @return : list of students ids.
     */
    public Collection<String> getStudents(){
        return students.keySet();
    }
    
    /**
     * Lists all the students with grade average in the interval.
     * 
     * @param inf : lower bound of the interval (inclusive)
     * @param sup : upper bound of the interval (inclusive)
     * @return : list of students ids.
     */
    public Collection<String> getStudents(double inf, double sup){
        return students.values().stream()
                .filter(s -> s.getGradeAverage()<=sup && s.getGradeAverage()>=inf)
                .map(Student::getId)
                .collect(toList())
                ;
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
    public int requestEnroll(String id, List<String> courses) throws ElectiveException {
        if(!students.containsKey(id)) {
            throw new ElectiveException("Student " + id + " unknown");
        }
        if(! courses.stream().allMatch(this.courses::containsKey) ){
            throw new ElectiveException("Course unknown");
        }
        if(courses.size()<1 || courses.size()>3) {
            throw new ElectiveException("Preferences shouls be between 1 and 3");
        }

        Student s = students.get(id);
        s.setRequests(courses.stream().map(this.courses::get).collect(toList()));
        
        listeners.forEach(l->l.requestReceived(id));
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
        return
           courses.keySet().stream().
           collect(
                 toMap(
                    c-> (String) c,
                    c -> (List<Long>) students.values().stream().
                         map(s->s.choiceNo(c)).
                         collect(collectingAndThen(
                                 groupingBy(n->n,counting()),
                                 m -> {
                                     ArrayList<Long> res = new ArrayList<>();
                                     for(int i=1; i<=3; ++i) {
                                         res.add( m.getOrDefault(i, 0L) );
                                     }
                                     return res;
                                 }
                                 )
                         )
                    ));
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
        return
        students.values().stream()
        .sorted(comparing(Student::getGradeAverage).reversed())
        .mapToInt(s -> {
//            for(Course c : s.getPreferences()) {
//                if(c.hasRoom()) {
//                    c.enroll(s);
//                }
//            }
            // OR
            if(s.getPreferences()!=null) {
                s.getPreferences().stream()
                .filter(Course::hasRoom)
                .findFirst().ifPresent(c->{
                    c.enroll(s);
                    listeners.forEach(l->l.assignedToCourse(s.getId(), c.getName()));
                });
                
            }
            return s.isEnrolled()?0:1;
        }).sum();
    }
    
    
    /**
     * Returns the students assigned to each course.
     * 
     * @return the map course name vs. student id list.
     */
    public Map<String,List<String>> getAssignments(){
        return courses.values().stream()
                .collect(toMap(
                    Course::getName,
                    c -> c.getEnrolled()
                ))
                ;
    }
    
    
    /**
     * Adds a new notification listener for the announcements
     * issues by this course manager.
     * 
     * @param listener : the new notification listener
     */
    public void addNotifier(Notifier listener) {
        listeners.add(listener);
    }
    
    /**
     * Computes the success rate w.r.t. to first 
     * (second, third) choice.
     * 
     * @param choice : the number of choice to consider.
     * @return the success rate (number between 0.0 and 1.0)
     */
    public double successRate(int choice){
        return students.values().stream()
        .filter(s -> s.isEnrolled(choice))
        .count() / (double)students.size()
                ;
    }
    
    /**
     * Returns the number of tickets served by type.
     * <p>
     * A ticket is considered as served after it has
     * been called to a counter by method {@link #serveNext(String)}
     * 
     * @return the map of count of served tickets vs. the ticket type 
     */
    public Map<String,Long> servedByType(){
        return null;
    }

    /**
     * Returns the number of tickets served by counter.
     * The count of each counter is reported as a map
     * reporting the number of tickets by type.
     * <p>
     * A ticket is considered as served after it has
     * been called to a counter by method {@link #serveNext(String)}
     * 
     * @return the map of count of served tickets vs. the ticket type vs. the counter.
     */
    public Map<String,Map<String,Long>> servedByCounter(){
        return null;
    }
    
    /**
     * Returns the students not assigned to any course.
     * 
     * @return the student id list.
     */
    public List<String> getNotAssigned(){
        return students.values().stream()
               .filter(s->!s.isEnrolled())
               .map(Student::getId)
               .collect(toList());
    }
    
}
