package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Child extends Person{
	private List<MeasurementZscore> zScores;

	public Child(String name, Date date_birth) {
		super(name, date_birth);
		
		zScores = new ArrayList<>();
	}
	
	public void addZscore(MeasurementZscore mzs) {
		zScores.add(mzs);
	}
	
	public List<MeasurementZscore> getAllZscores(){
		return new ArrayList<>(zScores);
	}
}
