package it.polito.oop.books;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.*;

public class Question {
	String question;
	Topic mainTopic;
	Map<String, Boolean> answers = new HashMap<>();
	
	public Question(String question, Topic mainTopic) {
		this.question = question;
		this.mainTopic = mainTopic;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public Topic getMainTopic() {
		return this.mainTopic;
	}

	public void addAnswer(String answer, boolean correct) {
		this.answers.put(answer, correct);
	}
	
    @Override
    public String toString() {
        return this.question + " (" + this.mainTopic + ")";
    }

	public long numAnswers() {
	    return this.answers.size();
	}

	public Set<String> getCorrectAnswers() {
		return this.answers.entrySet().stream()
										.filter(entry -> entry.getValue())
										.map(entry -> entry.getKey())
										.collect(toSet());
	}

	public Set<String> getIncorrectAnswers() {
		return this.answers.entrySet().stream()
				.filter(entry -> !entry.getValue())
				.map(entry -> entry.getKey())
				.collect(toSet());
	}
}

