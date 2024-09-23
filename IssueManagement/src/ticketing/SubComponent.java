package ticketing;

import java.util.HashSet;
import java.util.Set;

public class SubComponent {
	
	protected String name;
	protected String path;
	protected String parentPath;
	protected Set<String> subcomponents;
	
	public SubComponent(String name, String path, String parentPath) {
		this.name = name;
		this.path = path;
		this.parentPath = parentPath;
		this.subcomponents = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getParentPath() {
		return parentPath;
	}

	public Set<String> getSubcomponents() {
		return subcomponents;
	}
}
