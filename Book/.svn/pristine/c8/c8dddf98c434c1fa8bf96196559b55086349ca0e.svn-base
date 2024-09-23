package it.polito.oop.books;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Topic implements Comparable<Topic> {
	private String keyword;
	private Set<Topic> subTopics = new HashSet<Topic>();

	/* two topics are the same if they have the same keyword */
	@Override
	public int compareTo(Topic o) {
		return keyword.compareTo(o.keyword);
	}
	@Override
	public int hashCode() {
		return keyword.hashCode();
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		return keyword.equals(((Topic)obj).keyword);
//	}

    Topic(String keyword) throws BookException {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}
	
	@Override
	public String toString() {
	    return keyword;
	}

	public boolean addSubTopic(Topic topic) {
		if (subTopics.contains(topic)) {
			return false;
		} else {
			subTopics.add(topic);
			return true;
		}
	}

	/*
	 * Returns a sorted list of subtopics. Topics in the list *MAY* be modified without
	 * affecting any of the Book topic.
	 */
	public List<Topic> getSubTopics() {
		ArrayList<Topic> lst = new ArrayList<>(subTopics);
		Collections.sort(lst);
		return lst;
	}
}
