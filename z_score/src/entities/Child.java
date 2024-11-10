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
	
	public boolean addZscore(MeasurementZscore mzs) {
		if(mzs == null) {
			return false;
		}
		
		zScores.add(mzs);
		return true;
	}
	
	public boolean removeZscore(MeasurementZscore mzs) {
		if(mzs == null) {
			return false;
		}
		
		zScores.remove(mzs);
		return true;
	}
	
	public List<MeasurementZscore> getAllZscores(){
		return new ArrayList<>(zScores);
	}
}
