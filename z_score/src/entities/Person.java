package entities;

import java.util.Date;
import java.util.Objects;

public abstract class Person {
	private String name;
	private Date date_birth;
	
	public Person(String name, Date date_birth) {
		// TODO Check if date of birth is null
		this.name = name;
		this.date_birth = date_birth;
	}
	
	public Person() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate_birth() {
		return date_birth;
	}

	public void setDate_birth(Date date_birth) {
		this.date_birth = date_birth;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date_birth, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(date_birth, other.date_birth) && Objects.equals(name, other.name);
	}	
	
	@Override
	public String toString() {
		return name + "(" + date_birth.toString() + ")";
	}
}
