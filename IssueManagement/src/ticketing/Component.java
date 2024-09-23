package ticketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Component {
	
	protected String name;
	protected String path;
	protected String parentPath;
	protected Map<String, SubComponent> subComponents;
	protected Set<String> subComponentsSet;
	
	public Component(String name, String parentPath) {
		this.name = name;
		this.path = "/" + name;
		this.parentPath = null;
		this.subComponents = new HashMap<>();
		this.subComponentsSet = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public Map<String, SubComponent> getSubComponents() {
		return subComponents;
	}

	public Set<String> getSubComponentsSet() {
		return subComponentsSet;
	}

	public String getParentPath() {
		return parentPath;
	}
}
