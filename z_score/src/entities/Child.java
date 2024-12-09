package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Child extends Person{
	private Long id;
	private List<MeasurementZscore> zScores;

	public Child(String name, Date date_birth) {
		super(name, date_birth);
		
		this.id = null;
		zScores = new ArrayList<>();
	}
	
	public Child() {
		super();
		
		zScores = new ArrayList<>();
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
	
	public List<MeasurementZscore> getAllZscores(){
		return new ArrayList<>(zScores);
	}
}
