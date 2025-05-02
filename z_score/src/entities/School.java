package entities;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class School {
	private Long id;
	private String name;
	private String nationalRegistryLegalEntities;
	private Set<LevelEducation> educationLevels;
	
	public School(String name, String nationalRegistryLegalEntities) {
		this.id = null;
		this.name = name;
		this.nationalRegistryLegalEntities = nationalRegistryLegalEntities;
		
		this.educationLevels = new LinkedHashSet<>();
	}
	
	public School() {
		this.id = null;
		this.educationLevels = new LinkedHashSet<>();
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

	public String getNationalRegistryLegalEntities() {
		return nationalRegistryLegalEntities;
	}

	public void setNationalRegistryLegalEntities(String nationalRegistryLegalEntities) {
		this.nationalRegistryLegalEntities = nationalRegistryLegalEntities;
	}
	
	public boolean addEducationLevel(LevelEducation el) {
		if(educationLevels == null || el == null) {
			return false;
		}
		
		educationLevels.add(el);
		return true;
	}
	
	public boolean removeEducationLevel(LevelEducation el) {
		if(el == null) {
			return false;
		}
		
		educationLevels.remove(el);
		return true;
	}
	
	public Set<LevelEducation> getAllEducationLevels() {
		return new LinkedHashSet<>(educationLevels);
	}
	
	public int getNumberStudents() {
		int numberStudents = 0;
		
		for(LevelEducation le : educationLevels) {
			numberStudents += le.getAllChildren().size();
		}
		
		return numberStudents;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, nationalRegistryLegalEntities);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		School other = (School) obj;
		return Objects.equals(name, other.name)
				&& Objects.equals(nationalRegistryLegalEntities, other.nationalRegistryLegalEntities);
	}
	
	@Override
	public String toString() {
		return name + " (" + nationalRegistryLegalEntities + ")";
	}
}
