package it.polito.oop.books;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExerciseChapter extends Chapter {
	protected List<Question> questions = new LinkedList<>();

	public ExerciseChapter(String title, int numPages) {
		super(title, numPages);
	}

	public List<Topic> getTopics() {
		Set<Topic> topics = new HashSet<>();
		for(Question q: questions) {
			topics.add(q.getMainTopic()); 
		}
		
		ArrayList<Topic> lst = new ArrayList<>(topics);
		Collections.sort(lst);
		return lst;
	};
	
	public void addQuestion(Question question) {
		questions.add(question);
	}
}
