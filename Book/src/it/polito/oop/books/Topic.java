package it.polito.oop.books;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.*;

public class Topic {
	
	String keyword;
	Map<String, Topic> subTopics = new TreeMap<>();
	
	public Topic(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
        return this.keyword;
	}
	
	@Override
	public String toString() {
	    return this.keyword;
	}

	public boolean addSubTopic(Topic topic) {
        if(this.subTopics.containsKey(topic.keyword))
        	return false;
        this.subTopics.put(topic.keyword, topic);
        return true;
	}

	/*
	 * Returns a sorted list of subtopics. Topics in the list *MAY* be modified without
	 * affecting any of the Book topic.
	 */
	public List<Topic> getSubTopics() {
        return this.subTopics.values().stream().collect(toList());
	}
}
