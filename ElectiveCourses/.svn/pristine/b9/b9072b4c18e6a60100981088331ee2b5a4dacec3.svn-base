package it.polito.oop.elective;

import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.*;

public class Course {

    private String name;
    private int availablePositions;
    private List<Student> enrolled = new LinkedList<>();

    public Course(String name, int availablePositions) {
        this.name = name;
        this.availablePositions = availablePositions;
    }

    public String getName() {
        return name;
    }
    
    public boolean hasRoom() {
        return enrolled.size()<availablePositions;
    }

    public void enroll(Student s) {
        enrolled.add(s);
        s.setEnrolled(this);
    }
    

    public List<String> getEnrolled() {
        return enrolled.stream().map(Student::getId).collect(toList());
    }


//    @Override
//    public String toString() {
//        return this.name + "( " + this.enrolled.size() + " / " + this.availablePositions + ")";
//    }

//  @Override
//  public boolean equals(Object o) {
//      if(o==null) return false;
//      if(o instanceof String){
//          return name.equals(o);
//      }
//      if(o instanceof Course) {
//          return name.equals(((Course)o).name);
//      }
//      return false;
//  }
}
