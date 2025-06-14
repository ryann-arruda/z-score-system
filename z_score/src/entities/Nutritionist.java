package entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import entities.enums.PersonSex;

public class Nutritionist extends Person{
	private Long id;
	private String regionalCouncilNutritionists;
	private Set <School> schools;
	private String username;
	private String password;
	
	public Nutritionist() {
		super();
		
		schools = new HashSet<>();
	}

	public Nutritionist(String name, Date date_birth, PersonSex sex, String regionalCouncilNutritionists, String username, String password) {
		super(name, date_birth, sex);
		
		this.regionalCouncilNutritionists = regionalCouncilNutritionists;
		this.username = username;
		this.password = password;
		
		schools = new HashSet<>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getRegionalCouncilNutritionists() {
		return regionalCouncilNutritionists;
	}

	public void setRegionalCouncilNutritionists(String regionalCouncilNutritionists) {
		this.regionalCouncilNutritionists = regionalCouncilNutritionists;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean addSchool(School s) {
		if(s != null) {
			schools.add(s);
			return true;
		}
		
		return false;
	}
	
	public Set <School> getAllSchools(){
		return new HashSet<>(schools);
	}
	
	public boolean removeSchool(School s) {
		if(s != null) {
			schools.removeIf(x -> x.getId() == s.getId());
			return true;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(regionalCouncilNutritionists);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nutritionist other = (Nutritionist) obj;
		return Objects.equals(regionalCouncilNutritionists, other.regionalCouncilNutritionists);
	}
	
	@Override
	public String toString() {
		return super.toString() + " - Regional Council Nutritionists: "+ regionalCouncilNutritionists;
	}
}
