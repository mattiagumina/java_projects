package ticketing;

/**
 * Class representing the ticket linked to an issue or malfunction.
 * 
 * The ticket is characterized by a severity and a state.
 */
public class Ticket {
    
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

	private String author;
	private int id;
	private String description;
	private Severity severity;
	private String component;
	private State state;
	private String resolution;
	private User maintainer;
    
    Ticket(int ticketId, String username, String componentPath, String description, Severity severity) {
		this.author = username;
		this.id = ticketId;
		this.description = description;
		this.severity = severity;
		this.component = componentPath;
		this.state = State.Open;
	}

	public int getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }
    
    public Severity getSeverity() {
        return severity;
    }

    public String getAuthor(){
        return author;
    }
    
    public String getComponent(){
        return component;
    }
    
    public State getState(){
        return state;
    }
    
    public String getSolutionDescription() throws TicketException {
    	if( state != State.Closed ) {
    		throw new TicketException("Invalid state");
    	}
        return resolution;
    }

    void assign(User u) {
		
		this.state = State.Assigned;
		this.maintainer = u;
		
	}

	void close(String resolution) {
		
		this.resolution = resolution;
		maintainer.incrementClosed();
		this.state = State.Closed;
		
	}
}
