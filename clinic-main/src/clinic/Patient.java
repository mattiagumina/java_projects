package clinic;

public class Patient {
	
	protected String first;
	protected String last;
	protected String ssn;
	
	public Patient(String first, String last, String SSN) {
		this.first = first;
		this.last = last;
		this.ssn = SSN;
	}

	public String getFirst() {
		return this.first;
	}

	public String getLast() {
		return this.last;
	}

	public String getSSN() {
		return this.ssn;
	}
	
	

}
