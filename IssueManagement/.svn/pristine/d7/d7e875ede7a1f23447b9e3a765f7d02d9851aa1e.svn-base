package ticketing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IssueManager {
	
	private Map<String,User> users = new HashMap<>();
	
	//Alternative solution (not recommended)
	//private Map<String,Set<UserClass>> classiUtenti = new HashMap<>();
	
	private Map<String,Component> components = new HashMap<>();
	
	private int ticketId = 1;
	
	private Map<Integer,Ticket> tickets = new HashMap<>();
	
	

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
        Set<UserClass> classeSet = new HashSet<>();
        for(UserClass klass : classes) {
        	classeSet.add(klass);
        }
        // OPPURE
        classeSet = Stream.of(classes).collect(toSet());
        
        
//        User u = new User(username,classeSet);
//        users.put(username, u);
        // OR
        createUser(username,classeSet);
    }

    /**
     * Creates a new user
     * 
     * @param username name of the user
     * @param classes user classes
     * @throws TicketException if the username has already been created or if no user class has been specified
     */
    public void createUser(String username, Set<UserClass> classes) throws TicketException {
    	if(users.containsKey(username)) throw new TicketException("Duplicate user");
    	if(classes.size()==0) throw new TicketException("No user class provided");
        User u = new User(username,classes);
        users.put(username, u);
    }
   
    /**
     * Retrieves the user classes for a given user
     * 
     * @param username name of the user
     * @return the set of user classes the user belongs to
     */
    public Set<UserClass> getUserClasses(String username){
        return users.get(username).getClasses();
    }
    
    /**
     * Creates a new component
     * 
     * @param name unique name of the new component
     * @throws TicketException if a component with the same name already exists
     */
    public void defineComponent(String name) throws TicketException {
    	if(components.containsKey(name)) 
    		throw new TicketException("Component name is not unique");
        Component c = new Component(name);
        components.put("/"+name,c);
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
    	Component parent = components.get(parentPath);
    	if(parent==null) throw new TicketException();
    	
        Component c = new Component(name);
        components.put(parentPath+"/"+name,c);
        parent.addSubComponent(c);
    }
    
    /**
     * Retrieves the sub-components of an existing component
     * 
     * @param path the path of the parent
     * @return set of children sub-components
     */
    public Set<String> getSubComponents(String path){
    	Component c = components.get(path);
        return c.getSubComponents();
    }

    /**
     * Retrieves the parent component
     * 
     * @param path the path of the parent
     * @return name of the parent
     */
    public String getParentComponent(String path){
    	
    	// "/System"
        String parent =  path.replaceAll("/[^/]+$", "");
        if(parent.length()==0) return null;
        return parent;
        
//        int pos = path.lastIndexOf("/");
//        if(pos==0) return null;
//        return path.substring(0, pos);
        		
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
    	if(!users.containsKey(username))
    		throw new TicketException("Username non found: " + username);
    	if(!components.containsKey(componentPath)) {
    		throw new TicketException("Component non found: " + componentPath);
    	}
    	if(! users.get(username)
    			.getClasses()
    			.contains(UserClass.Reporter) ) {
    		throw new TicketException("User is not a reporter: " + username);
    	}
    	
    	Ticket t = new Ticket(ticketId,username,componentPath,description,severity);
    	tickets.put(ticketId, t);
    	
        return ticketId++;
    }
    
    /**
     * Returns a ticket object given its id
     * 
     * @param ticketId id of the tickets
     * @return the corresponding ticket object
     */
    public Ticket getTicket(int ticketId){
        return tickets.get(ticketId);
    }
    
    /**
     * Returns all the existing tickets sorted by severity
     * 
     * @return list of ticket objects
     */
    public List<Ticket> getAllTickets(){
    	ArrayList<Ticket> res = new ArrayList<>();
    	res.addAll(tickets.values());
    	Collections.sort(res,
    			//(a,b)-> a.getSeverity().compareTo(b.getSeverity())
    			// OR
    			Comparator.comparing(Ticket::getSeverity)
    			);
        return res;
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
        User u = users.get(username);
        if(u==null ||
           ! u.getClasses().contains(UserClass.Maintainer) ) {
        	throw new TicketException("Invalid user");
        }
        Ticket t = tickets.get(ticketId);
        if( t==null || t.getState() != Ticket.State.Open ) {
        	throw new TicketException("Invalid ticket");
        }
        
        t.assign(u);
        
        	
    }

    /**
     * Closes a ticket
     * 
     * @param ticketId id of the ticket
     * @param description description of how the issue was handled and solved
     * @throws TicketException if the ticket is not in state <i>Assigned</i>
     */
    public void closeTicket(int ticketId, String description) throws TicketException {
        Ticket t = tickets.get(ticketId);
        if( t==null || t.getState() != Ticket.State.Assigned ) {
        	throw new TicketException("Invalid ticket");
        }
        
        t.close(description);

    }

    /**
     * returns a sorted map (keys sorted in natural order) with the number of  
     * tickets per Severity, considering only the tickets with the specific state.
     *  
     * @param state state of the tickets to be counted, all tickets are counted if <i>null</i>
     * @return a map with the severity and the corresponding count 
     */
    public SortedMap<Ticket.Severity,Long> countBySeverityOfState(Ticket.State state){
    	
    	return tickets.values().stream()
    	.filter( t -> state==null || t.getState() == state)
    	.collect(groupingBy(
    			Ticket::getSeverity,
    			TreeMap::new,
    			counting()
    			));
    	
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
    	
    	return users.values().stream()
    			.sorted(//comparing(User::getCountClosed)
    					comparing( (User u) -> u.getCountClosed() )
    					.reversed()
    					.thenComparing(User::getName))
    			.map( u -> u.getName() + ":" + u.getCountClosed())
    			.collect(toList());
    	
    }

}
