package entities;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import entities.enums.PersonSex;

public class Child extends Person{
	private Long id;
	private Set<MeasurementZscore> zScores;

	public Child(String name, Date date_birth, PersonSex sex) {
		super(name, date_birth, sex);
		
		this.id = null;
		zScores = new LinkedHashSet<>();
	}
	
	public Child() {
		super();
		
		zScores = new LinkedHashSet<>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public boolean updateZscore(MeasurementZscore mzs) {
		if(mzs == null) {
			return false;
		}
		
		for(MeasurementZscore zscore : zScores) {
			
			if(zscore.getId() == mzs.getId()) {
				zscore.setzScore(mzs.getzScore());
				return true;
			}
		}
		
		return false;
	}
	
	public Set<MeasurementZscore> getAllZscores(){
		return new LinkedHashSet<>(zScores);
	}
	
	public double getLatestZscoreMeasurement() {
		double zScoreValue = 0.0;
		
		for(MeasurementZscore measure : zScores) {
			zScoreValue = measure.getzScore();
		}
		
		return zScoreValue;
	}
}
