package entities;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class LevelEducation {
	private Long id;
	private String name;
	private Set<Child> children;
	
	public LevelEducation(String name) {
		this.id = null;
		this.name = name;
		children = new LinkedHashSet<>();
	}
	
	public LevelEducation() {
		children = new LinkedHashSet<>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public boolean removeChild(Child c) {
		if(c != null) {
			children.removeIf(x -> x.getId() == c.getId());
			return true;
		}
		
		return false;
	}
	
	public Set<Child> getAllChildren(){
		return new LinkedHashSet<>(children);
	}
	
	public int getNumberStudents() {
		return children.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
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
