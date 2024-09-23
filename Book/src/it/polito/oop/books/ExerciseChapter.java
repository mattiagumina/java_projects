package it.polito.oop.books;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.*;


public class ExerciseChapter {
	String title;
	int numPages;
	List<Question> questions = new ArrayList<>();;
	
	public ExerciseChapter(String title, int numPages) {
		this.title = title;
		this.numPages = numPages;
	}

    public List<Topic> getTopics() {
        return this.questions.stream()
        					.map(question -> question.mainTopic)
        					.distinct()
        					.sorted(Comparator.comparing(t -> t.toString()))
        					.collect(toList());
	};
	

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
    	this.title = newTitle;
    }

    public int getNumPages() {
        return this.numPages;
    }
    
    public void setNumPages(int newPages) {
    	this.numPages = newPages;
    }
    

	public void addQuestion(Question question) {
		this.questions.add(question);
	}	
}
