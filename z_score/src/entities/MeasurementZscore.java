package entities;

import java.util.Date;
import java.util.Objects;

public class MeasurementZscore {
	private Long id;
	private Double zScore;
	private Date date;
	
	public MeasurementZscore(Double zScore, Date date) {
		this.zScore = zScore;
		this.date = date;
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

	@Override
	public int hashCode() {
		return Objects.hash(date, zScore);
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
		return Objects.equals(date, other.date) && Objects.equals(zScore, other.zScore);
	}
	
	@Override
	public String toString() {
		return zScore.toString() + " (" + date.toString() + ")";
	}
}
