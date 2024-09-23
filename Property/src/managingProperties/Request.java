package managingProperties;

import managingProperties.Request.StateRequest;

public class Request {
	
	protected int requestNumber;
	protected String owner;
	protected String apartment;
	protected StateRequest state;
	protected String profession;
	protected String professional;
	protected int expense;
	
	public enum StateRequest{pending, assigned, completed}

	public Request(int requestNumber, String owner, String apartment, String profession) {
		this.requestNumber = requestNumber;
		this.owner = owner;
		this.apartment = apartment;
		this.profession = profession;
		this.state = StateRequest.pending;
		this.professional = null;
		this.expense = -1;
	}

	public StateRequest getState() {
		return state;
	}

	public void setState(StateRequest state) {
		this.state = state;
	}

	public String getOwner() {
		return owner;
	}

	public int getRequestNumber() {
		return requestNumber;
	}

	public String getApartment() {
		return apartment;
	}

	public String getProfession() {
		return profession;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}
	
	public void setExpense(int expense) {
		this.expense = expense;
	}
	
	public int getExpense() {
		return this.expense;
	}
	
	public String getBuilding() {
		return this.apartment.split(":")[0];
	}
}
