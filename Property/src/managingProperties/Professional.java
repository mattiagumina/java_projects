package managingProperties;

public class Professional {
	
	protected String id;
	protected String profession;
	
	public Professional(String id, String profession) {
		this.id = id;
		this.profession = profession;
	}

	public String getId() {
		return id;
	}

	public String getProfession() {
		return profession;
	}
}
