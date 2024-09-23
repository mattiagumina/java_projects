package managingProperties;

class Request {

//	static final String PENDING = "pending";
//	static final String ASSIGNED = "assigned";
//	static final String COMPLETED = "completed";
	// OR (better)
	enum RequestState {
		PENDING, ASSIGNED, COMPLETED
	};

	private int id;

//	private String state;
	private RequestState stateEnum;

	private String owner;
	private String apartment;
	private String profession;
	private String professional;
	private int amount;
	private String building;

	Request(int id, String owner, String apartment, String profession) {
		this.id = id;
		this.owner = owner;
		this.apartment = apartment;
		this.profession = profession;
		//state = PENDING;
		// OR
		stateEnum = RequestState.PENDING;
		building = apartment.split(":")[0];
		// System.out.println(apartment + " " + building + " " +
		// apartment.split(":")[1]);
	}

	public String getBuilding() {
		return building;
	}

//	public String getState() {
//		return state;
//	}

//	public void setState(String state) {
//		this.state = state;
//	}

	public int getId() {
		return id;
	}

	public String getOwner() {
		return owner;
	}

	public String getApartment() {
		return apartment;
	}

	public String getProfession() {
		return profession;
	}

	void assign(String professional) throws PropertyException {
//		if (state != PENDING)
		if (stateEnum != RequestState.PENDING)
			throw new PropertyException("Invalid request state");

		this.professional = professional;
//		state = ASSIGNED;
		stateEnum = RequestState.ASSIGNED;
	}

	public boolean isPenndig() {
		//return state.equals(ASSIGNED);
		return stateEnum == RequestState.PENDING;
	}

	public boolean isAssigned() {
		//return state.equals(ASSIGNED);
		return stateEnum == RequestState.ASSIGNED;
	}

	public boolean isCompleted() /* throws PropertyException */ {
//		return state == COMPLETED;
		return stateEnum == RequestState.COMPLETED;
	}

	public void charge(int amount) {
		this.amount = amount;
//		state = COMPLETED;
		stateEnum = RequestState.COMPLETED;
	}

	public String getProfessional() {
		return professional;
	}

	public int getAmount() {
		return amount;
	}

}
