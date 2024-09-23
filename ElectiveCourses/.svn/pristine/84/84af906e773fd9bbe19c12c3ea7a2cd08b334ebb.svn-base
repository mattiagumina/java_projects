package it.polito.oop.elective;

import java.util.Collection;
import java.util.List;

public class Student {

    private String id;
    private double gradeAverage;
    private List<Course> requests;
    private Course enrolledIn;

    public Student(String id, double gradeAverage) {
        this.id = id;
        this.gradeAverage = gradeAverage;
    }

    public String getId() {
        return id;
    }

    public double getGradeAverage() {
        return gradeAverage;
    }

    public void setRequests(List<Course> requests) {
        this.requests = requests;
    }

    public int choiceNo(String course) {
        for(int i=0; i<requests.size(); ++i) {
            if(requests.get(i).getName().equals(course)) return i+1;
        }
        return -1;
    }

    Collection<Course> getPreferences() {
        return requests;
    }

    void setEnrolled(Course course) {
        enrolledIn = course;
    }
    
    boolean isEnrolled() {
        return enrolledIn != null;
    }
    
//    @Override
//    public String toString() {
//        return this.id + " ( " + this.gradeAverage + ")";
//    }

    public boolean isEnrolled(int choice) {
        return isEnrolled() && choice <= requests.size() && enrolledIn == requests.get(choice-1);
    }

}
