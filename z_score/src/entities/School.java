package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class School {
	private String name;
	private String nationalRegistryLegalEntities;
	private List<LevelEducation> educationLevels;
	
	public School(String name, String nationalRegistryLegalEntities) {
		this.name = name;
		this.nationalRegistryLegalEntities = nationalRegistryLegalEntities;
		
		this.educationLevels = new ArrayList<>();
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
	
	public List<LevelEducation> getAllEducationLevels() {
		return new ArrayList<>(educationLevels);
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
