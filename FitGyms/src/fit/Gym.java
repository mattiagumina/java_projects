package fit;

import java.util.HashMap;
import java.util.Map;

public class Gym {
	
	String name;
	Map<Map.Entry<Integer, Integer>, Lesson> lessons;
	
	public Gym(String name) {
		this.name = name;
		this.lessons = new HashMap<>();
	}
}
