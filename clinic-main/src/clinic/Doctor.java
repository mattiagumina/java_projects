package clinic;

public class Doctor {
	
	protected String first;
	protected String last;
	protected String ssn;
	protected int id;
	protected String specialization;
	
	public Doctor(String first, String last, String ssn, int id, String specialization) {
		this.first = first;
		this.last = last;
		this.ssn = ssn;
		this.id = id;
		this.specialization = specialization;
	}

	public String getFirst() {
		return this.first;
	}

	public String getLast() {
		return this.last;
	}

	public String getSsn() {
		return this.ssn;
	}

	public int getId() {
		return this.id;
	}

	public String getSpecialization() {
		return this.specialization;
	}
	
	

}
