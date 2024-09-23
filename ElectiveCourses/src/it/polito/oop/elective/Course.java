package it.polito.oop.elective;

import java.util.ArrayList;
import java.util.List;

public class Course {

	String name;
	int availablePositions;
	List<String> enrolledStudent = new ArrayList<>();
	
	public Course(String name, int availablePositions) {
		this.name = name;
		this.availablePositions = availablePositions;
	}
}
