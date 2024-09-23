package ticketing;

import java.util.Set;

import ticketing.IssueManager.UserClass;

public class User {
	
	protected String name;
	protected Set<UserClass> classes;
	
	public User(String name, Set<UserClass> classes) {
		this.name = name;
		this.classes = classes;
	}

	public String getName() {
		return name;
	}

	public Set<UserClass> getClasses() {
		return classes;
	}

}
