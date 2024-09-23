package ticketing;

import ticketing.Ticket.Severity;
import ticketing.Ticket.State;

/**
 * Class representing the ticket linked to an issue or malfunction.
 * 
 * The ticket is characterized by a severity and a state.
 */
public class Ticket {
	
	protected int id;
	protected String username;
	protected String pathComponent;
	protected String description;
	protected Severity severity;
	protected State state;
	protected String assignee;
	protected String solution;

    public Ticket(int id, String username, String pathComponent, String description, Severity severity) {
		this.id = id;
		this.username = username;
		this.pathComponent = pathComponent;
		this.description = description;
		this.severity = severity;
		this.state = Ticket.State.Open;
		this.assignee = null;
		this.solution = null;
	}

	/**
     * Enumeration of possible severity levels for the tickets.
     * 
     * Note: the natural order corresponds to the order of declaration
     */
    public enum Severity { Blocking, Critical, Major, Minor, Cosmetic };
    
    /**
     * Enumeration of the possible valid states for a ticket
     */
    public static enum State { Open, Assigned, Closed }
    
    public int getId(){
        return this.id;
    }

    public String getDescription(){
        return this.description;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }

    public String getAuthor(){
        return this.username;
    }
    
    public String getComponent(){
        return this.pathComponent;
    }
    
    public State getState(){
        return this.state;
    }
    
    public String getSolutionDescription() throws TicketException {
        if(this.state != State.Closed)
        	throw new TicketException("The ticket is not in the Closed state");
        return this.solution;
    }
    
    public String getAssignee() {
    	return this.assignee;
    }
    
    public void setState(Ticket.State state) {
    	this.state = state;
    }
    
    public void setAssignee(String assignee) {
    	this.assignee = assignee;
    }
    
    public void setSolution(String solution) {
    	this.solution = solution;
    }
}
