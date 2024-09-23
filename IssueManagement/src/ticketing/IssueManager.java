package ticketing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

import java.util.Comparator;

public class IssueManager {
	
	protected Map<String, User> users;
	protected Map<String, Component> components;
	protected Map<Integer, Ticket> tickets;
	
	public IssueManager() {
		this.users = new HashMap<>();
		this.components = new HashMap<>();
		this.tickets = new HashMap<>();
	}

    /**
     * Eumeration of valid user classes
     */
    public static enum UserClass {
        /** user able to report an issue and create a corresponding ticket **/
        Reporter, 
        /** user that can be assigned to handle a ticket **/
        Maintainer }
    
    /**
     * Creates a new user
     * 
     * @param username name of the user
     * @param classes user classes
     * @throws TicketException if the username has already been created or if no user class has been specified
     */
    public void createUser(String username, UserClass... classes) throws TicketException {
    	if(this.users.containsKey(username))
    		throw new TicketException("The username has already been created");
        if(classes.length == 0)
        	throw new TicketException("No user class has been specified");
        this.users.put(username, new User(username, Stream.of(classes).collect(toSet())));
    }

    /**
     * Creates a new user
     * 
     * @param username name of the user
     * @param classes user classes
     * @throws TicketException if the username has already been created or if no user class has been specified
     */
    public void createUser(String username, Set<UserClass> classes) throws TicketException {
        if(this.users.containsKey(username))
        	throw new TicketException("The username has already been created");
        if(classes.isEmpty())
        	throw new TicketException("No user class has been specified");
        this.users.put(username, new User(username, classes));
    }
   
    /**
     * Retrieves the user classes for a given user
     * 
     * @param username name of the user
     * @return the set of user classes the user belongs to
     */
    public Set<UserClass> getUserClasses(String username){
        return this.users.get(username).getClasses();
    }
    
    /**
     * Creates a new component
     * 
     * @param name unique name of the new component
     * @throws TicketException if a component with the same name already exists
     */
    public void defineComponent(String name) throws TicketException {
        if(this.components.containsKey(name))
        	throw new TicketException("Component with the same name already exists");
        this.components.put(name, new Component(name, null));
    }
    
    /**
     * Creates a new sub-component as a child of an existing parent component
     * 
     * @param name unique name of the new component
     * @param parentPath path of the parent component
     * @throws TicketException if the the parent component does not exist or 
     *                          if a sub-component of the same parent exists with the same name
     */
    public void defineSubComponent(String name, String parentPath) throws TicketException {
    	if(this.components.containsKey(name))
    		throw new TicketException("A component with the same name already exists");
        if(!this.components.containsKey(parentPath.split("/")[1]))
        	throw new TicketException("The parent component does not exist");
        if(this.components.get(parentPath.split("/")[1]).getSubComponents().containsKey(name))
        	throw new TicketException("A sub-component of the same parent exists with the same name");
        if(parentPath.split("/").length > 2)
        	this.components.get(parentPath.split("/")[1]).getSubComponents().get(parentPath.split("/").length).getSubcomponents().add(name);
        else
        	this.components.get(parentPath.split("/")[1]).getSubComponentsSet().add(name);
        this.components.get(parentPath.split("/")[1]).getSubComponents().put(name, new SubComponent(name, parentPath + "/" + name, parentPath));
    }
    
    /**
     * Retrieves the sub-components of an existing component
     * 
     * @param path the path of the parent
     * @return set of children sub-components
     */
    public Set<String> getSubComponents(String path){
        if(this.components.values().stream().map(component -> component.getPath()).anyMatch(componentPath -> componentPath.equals(path)))
        	return this.components.get(path.split("/")[1]).getSubComponentsSet();
        return this.components.values().stream().map(component -> component.getSubComponents().values()).flatMap(list -> list.stream()).filter(subComponent -> subComponent.getPath().equals(path)).findFirst().get().getSubcomponents();
    }

    /**
     * Retrieves the parent component
     * 
     * @param path the path of the parent
     * @return name of the parent
     */
    public String getParentComponent(String path){
        if(this.components.values().stream().map(component -> component.getPath()).anyMatch(componentPath -> componentPath.equals(path)))
        	return this.components.get(path.substring(1)).getParentPath();
        return this.components.values().stream().map(component -> component.getSubComponents().entrySet()).flatMap(set -> set.stream()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue)).get(path.split("/")[path.split("/").length - 1]).getParentPath();
    }

    /**
     * Opens a new ticket to report an issue/malfunction
     * 
     * @param username name of the reporting user
     * @param componentPath path of the component or sub-component
     * @param description description of the malfunction
     * @param severity severity level
     * 
     * @return unique id of the new ticket
     * 
     * @throws TicketException if the user name is not valid, the path does not correspond to a defined component, 
     *                          or the user does not belong to the Reporter {@link IssueManager.UserClass}.
     */
    public int openTicket(String username, String componentPath, String description, Ticket.Severity severity) throws TicketException {
    	int id = this.tickets.size() + 1;
        if(!this.users.containsKey(username))
        	throw new TicketException("The user name is not valid");
        if(!this.components.values().stream().map(component -> component.getPath()).anyMatch(path -> path.equals(componentPath)) && !this.components.values().stream().map(component -> component.getSubComponents().values()).flatMap(list -> list.stream()).map(subComponent -> subComponent.getPath()).anyMatch(path -> path.equals(componentPath)))
        	throw new TicketException("The path does not correspond to a defined component");
        if(!this.users.get(username).getClasses().contains(UserClass.Reporter))
        	throw new TicketException("The user does not belong to the Reporter user class");
        this.tickets.put(id, new Ticket(id, username, componentPath, description, severity));
        return id;
    }
    
    /**
     * Returns a ticket object given its id
     * 
     * @param ticketId id of the tickets
     * @return the corresponding ticket object
     */
    public Ticket getTicket(int ticketId){
    	if(this.tickets.containsKey(ticketId))
    		return this.tickets.get(ticketId);
    	return null;
    }
    
    /**
     * Returns all the existing tickets sorted by severity
     * 
     * @return list of ticket objects
     */
    public List<Ticket> getAllTickets(){
        return this.tickets.values().stream().sorted(Comparator.comparing(ticket -> ticket.getSeverity())).toList();
    }
    
    /**
     * Assign a maintainer to an open ticket
     * 
     * @param ticketId  id of the ticket
     * @param username  name of the maintainer
     * @throws TicketException if the ticket is in state <i>Closed</i>, the ticket id or the username
     *                          are not valid, or the user does not belong to the <i>Maintainer</i> user class
     */
    public void assingTicket(int ticketId, String username) throws TicketException {
        if(!this.users.containsKey(username) || !this.tickets.containsKey(ticketId))
        	throw new TicketException("The ticket id or the username are not valid");
        if(this.tickets.get(ticketId).getState() == Ticket.State.Closed)
        	throw new TicketException("The ticket is in state Closed");
        if(!this.users.get(username).getClasses().contains(UserClass.Maintainer))
        	throw new TicketException("The user does not belong to the Maintainer user class");
        this.tickets.get(ticketId).setState(Ticket.State.Assigned);
        this.tickets.get(ticketId).setAssignee(username);
    }

    /**
     * Closes a ticket
     * 
     * @param ticketId id of the ticket
     * @param description description of how the issue was handled and solved
     * @throws TicketException if the ticket is not in state <i>Assigned</i>
     */
    public void closeTicket(int ticketId, String description) throws TicketException {
        if(this.tickets.get(ticketId).getState() != Ticket.State.Assigned)
        	throw new TicketException("The ticket is not in state Assigned");
        this.tickets.get(ticketId).setState(Ticket.State.Closed);
        this.tickets.get(ticketId).setSolution(description);
    }

    /**
     * returns a sorted map (keys sorted in natural order) with the number of  
     * tickets per Severity, considering only the tickets with the specific state.
     *  
     * @param state state of the tickets to be counted, all tickets are counted if <i>null</i>
     * @return a map with the severity and the corresponding count 
     */
    public SortedMap<Ticket.Severity,Long> countBySeverityOfState(Ticket.State state){
        if(state == null)
        	return this.tickets.values().stream()
        									.collect(groupingBy(ticket -> ticket.getSeverity(), TreeMap::new, counting()));
        return this.tickets.values().stream()
        								.filter(ticket -> ticket.getState() == state)
        								.collect(groupingBy(ticket -> ticket.getSeverity(), TreeMap::new, counting()));
    }

    /**
     * Find the top maintainers in terms of closed tickets.
     * 
     * The elements are strings formatted as <code>"username:###"</code> where <code>username</code> 
     * is the user name and <code>###</code> is the number of closed tickets. 
     * The list is sorter by descending number of closed tickets and then by username.

     * @return A list of strings with the top maintainers.
     */
    public List<String> topMaintainers(){
        return this.tickets.values().stream()
        								.filter(ticket -> ticket.getState() == Ticket.State.Closed)
        								.collect(groupingBy(ticket -> ticket.getAssignee(), TreeMap::new, counting()))
        								.entrySet().stream()
        								.sorted(Comparator.comparingLong(entry -> 1L / entry.getValue()))
        								.map(entry -> entry.getKey() + ":" + String.format("%3d", entry.getValue()))
        								.toList();
    }

}
