package managingProperties;

import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

public class Owner {

	protected String id;
	protected List<String> apartments;
	
	public Owner(String id, List<String> apartments) {
		this.id = id;
		this.apartments = apartments;
	}

	public String getId() {
		return id;
	}

	public List<String> getApartments() {
		return apartments;
	}
}
