package it.polito.oop.elective;

import java.util.ArrayList;
import java.util.List;

public class Student {

	String id;
	double gradeAverage;
	List<String> desiresCourses = new ArrayList<>();
	String enrolledCourse;
	
	public Student(String id, double gradeAverage) {
		this.id = id;
		this.gradeAverage = gradeAverage;
		this.enrolledCourse = null;
	}
}
