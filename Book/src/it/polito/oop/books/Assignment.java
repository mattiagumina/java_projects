package it.polito.oop.books;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;


public class Assignment {
	String ID;
	ExerciseChapter chapter;
	Map<Question, Double> responses = new HashMap<>();
	
	public Assignment(String ID, ExerciseChapter chapter) {
		this.ID = ID;
		this.chapter = chapter;
	}

    public String getID() {
        return this.ID;
    }

    public ExerciseChapter getChapter() {
        return this.chapter;
    }

    public double addResponse(Question q,List<String> answers) {
    	double score = q.answers.size();
    	score -= answers.stream().filter(string -> q.answers.containsKey(string) && !q.answers.get(string)).count();
    	score -= q.answers.entrySet().stream().filter(entry -> entry.getValue() && !answers.contains(entry.getKey())).count();
    	score /= q.answers.size();
    	this.responses.put(q, score);
        return score;
    }
    
    public double totalScore() {
        return this.responses.values().stream().collect(summingDouble(x -> x));
    }

}
