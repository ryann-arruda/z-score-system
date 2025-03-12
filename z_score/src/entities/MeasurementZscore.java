package entities;

import java.util.Date;
import java.util.Objects;

import entities.enums.ZscoreClassification;

public class MeasurementZscore {
	private Long id;
	private Double zScore;
	private Date date;
	private Double weight;
	private Double height;
	private ZscoreClassification classification;
	
	public MeasurementZscore(Double zScore, Date date, Double weight, Double height, ZscoreClassification classification) {
		this.id = null;
		this.zScore = zScore;
		this.date = date;
		this.weight = weight;
		this.height = height;
		this.classification = classification;
	}
	
	public MeasurementZscore() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Double getzScore() {
		return zScore;
	}

	public void setzScore(Double zScore) {
		this.zScore = zScore;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Double getWeight() {
		return weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	public Double getHeight() {
		return height;
	}
	
	public void setHeight(Double height) {
		this.height = height;
	}
	
	public ZscoreClassification getClassification() {
		return classification;
	}
	
	public void setClassification(ZscoreClassification classification) {
		this.classification = classification;
	}

	@Override
	public int hashCode() {
		return Objects.hash(classification, date, id, zScore);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementZscore other = (MeasurementZscore) obj;
		return classification == other.classification && Objects.equals(date, other.date)
				&& Objects.equals(id, other.id) && Objects.equals(zScore, other.zScore);
	}

	@Override
	public String toString() {
		return zScore.toString() + " (" + date.toString() + ")";
	}
}
