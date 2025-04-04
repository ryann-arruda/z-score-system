package entities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import entities.enums.PersonSex;

public abstract class Person {
	private String name;
	private Date dateBirth;
	private PersonSex sex;
	
	public Person() {

	}
	
	public Person(String name, Date date_birth) {
		if(name != null && date_birth != null) {
			this.name = name;
			this.dateBirth = date_birth;
		}
		else {
			throw new IllegalStateException("Parameters cannot be null");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date date_birth) {
		this.dateBirth = date_birth;
	}
	
	public long getMonthsLife() {
		LocalDate currentDate = LocalDate.now();
		LocalDate date = dateBirth.toInstant()
				                  .atZone(ZoneId.systemDefault())
				                  .toLocalDate();
		
		return ChronoUnit.MONTHS.between(currentDate, date);
	}
	
	public PersonSex getSex() {
		return sex;
	}
	
	public void setSex(PersonSex sex) {
		this.sex = sex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateBirth, name);
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
		return Objects.equals(dateBirth, other.dateBirth) && Objects.equals(name, other.name);
	}	
	
	@Override
	public String toString() {
		return name + "(" + dateBirth.toString() + ")";
	}
}
