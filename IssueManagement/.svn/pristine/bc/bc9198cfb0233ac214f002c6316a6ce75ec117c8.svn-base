package ticketing;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.TreeSet;
import static java.util.stream.Collectors.*;

public class Component implements Comparable<Component>{
	private String name;
//	private List<Component> subcomponents = new LinkedList<>();
//	private TreeSet<Component> subcomponents = new TreeSet<>();
	private HashSet<Component> subcomponents = new HashSet<>();

	public Component(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void addSubComponent(Component c) throws TicketException {
		if(subcomponents.contains(c)) {
			throw new TicketException();
		}
		subcomponents.add(c);	
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null) return false;
		if(! (o instanceof Component) ) return false;
		
		return this.name.equals(((Component)o).name);
	}
	
	@Override
	public int hashCode() {  // this is required for HashSet
		return name.hashCode();
	}

	@Override
	public int compareTo(Component o) { // this is required for TreeSet
		return this.name.compareTo(o.name);
	}

	public Set<String> getSubComponents() {
		return subcomponents.stream()
				.map( c -> c.name ) // ok since it's within Component class
// 					.map(Component::getName)
				.collect(toSet());
	}

}
