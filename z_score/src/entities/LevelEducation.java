package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelEducation {
	private String name;
	private List<Child> children;
	
	public LevelEducation(String name) {
		this.name = name;
		children = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean addChild(Child c) {
		if(c != null) {
			children.add(c);
			return true;
		}
		
		return false;
	}
	
	public List<Child> getAllChildren(){
		return new ArrayList<>(children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(children, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LevelEducation other = (LevelEducation) obj;
		return Objects.equals(children, other.children) && Objects.equals(name, other.name);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(name + "\n");
		
		for(Child c : children) {
			sb.append(c + "\n");
		}
		
		return sb.toString();
	}
}
