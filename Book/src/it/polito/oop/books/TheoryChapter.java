package it.polito.oop.books;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.*;


public class TheoryChapter {
	String title;
	int numPages;
	String text;
	Set<Topic> topics = new HashSet<>();
	
	public TheoryChapter(String title, int numPages, String text) {
		this.title = title;
		this.numPages = numPages;
		this.text = text;
	}

    public String getText() {
		return this.text;
	}

    public void setText(String newText) {
    	this.text = newText;
    }


	public List<Topic> getTopics() {
        return this.topics.stream().sorted(Comparator.comparing(t -> t.toString())).collect(toList());
	}

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
    
    public void addTopic(Topic topic) {
    	List<Topic> addedTopics = new ArrayList<>();
    	List<Topic> tempTopics = new ArrayList<>();
    	this.topics.add(topic);
    	addedTopics.add(topic);
    	while(!addedTopics.stream().allMatch(t -> t.subTopics.size() == 0)) {
    		tempTopics.clear();
    		addedTopics.stream().map(t -> t.subTopics.values()).flatMap(list -> list.stream()).forEach(t -> {
    																											this.topics.add(t);
    																											tempTopics.add(t);
    																										});
    		addedTopics.clear();
    		tempTopics.stream().forEach(t -> addedTopics.add(t));;
    	}
    }
    
}
