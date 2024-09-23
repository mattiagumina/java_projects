package ticketing;

import java.util.HashSet;
import java.util.Set;

import ticketing.IssueManager.UserClass;

class User {
	private String username;
	private Set<IssueManager.UserClass> classes;
	private int countClosed;

	
	public User(String username, Set<UserClass> classes) {
		this.username = username;
		this.classes = new HashSet<>(classes);
		this.countClosed = 0;
	}
	
	public String getName() {
		return username;
	}


	public Set<UserClass> getClasses() {
		return classes;
	}


	 void incrementClosed() {
		this.countClosed++;
	}

	 int getCountClosed() {
		 return countClosed;
	 }
	
}
