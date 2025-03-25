package entities;

import java.util.Objects;

public class ZscoreTableRow {
	private Long id;
	private Double median;
	private Double stdDeviation;
	
	public ZscoreTableRow(Long id, Double median, Double stdDeviation) {
		this.id = id;
		this.median = median;
		this.stdDeviation = stdDeviation;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getMedian() {
		return median;
	}
	
	public void setMedian(Double median) {
		this.median = median;
	}
	
	public Double getStdDeviation() {
		return stdDeviation;
	}
	
	public void setStdDeviation(Double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, median, stdDeviation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZscoreTableRow other = (ZscoreTableRow) obj;
		return Objects.equals(id, other.id) && Objects.equals(median, other.median)
				&& Objects.equals(stdDeviation, other.stdDeviation);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID (" + id.toString() + ") - ");
		sb.append("Mediana (" + median.toString() +  ") - ");
		sb.append("Desvio Padrão (" + stdDeviation.toString() + ")");
		
		return sb.toString();
	}
}
