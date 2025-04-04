package entities;

import java.util.Map;

import entities.enums.PersonSex;
import entities.enums.ZscoreClassification;
import entities.service.ZscoreTableService;

public class ZscoreCalculator {
	private ZscoreTableService service;
	private ZscoreTable zscoreTableBoys;
	private ZscoreTable zscoreTableGirls;
	
	public ZscoreCalculator(ZscoreTableService service) {
		this.service = service;
		
		getTables();
	}

	private void getTables() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		Map<String, ZscoreTable> tables = service.getZscoreTable();
		
		zscoreTableBoys = tables.get("boys");
		zscoreTableBoys = tables.get("girls");
	}
	
	private ZscoreTableRow getZscoreTableRow(Child child) {
		
		if(child.getSex() == PersonSex.MALE) {
			return zscoreTableBoys.getRow(child.getMonthsLife());
		}
		else {
			return zscoreTableGirls.getRow(child.getMonthsLife());
		}
	}
	
	private ZscoreClassification getClassification(Double zscoreValue) {
		if (zscoreValue > 3) {
			return ZscoreClassification.OBESITY;
		}
		else if(zscoreValue > 2) {
			return ZscoreClassification.OVERWEIGHT;
		}
		else if(zscoreValue > -2) {
			return ZscoreClassification.NORMAL;
		}
		else if(zscoreValue > -3) {
			return ZscoreClassification.MODERATE_MALNUTRITION;
		}
		else {
			return ZscoreClassification.MILD_MALNUTRITION;
		}
	}
	
	public Map<String, Object> calculateZscore(Child child, Double weightValue){
		Map<String, Object> result;
		
		ZscoreTableRow zscoreTableRow = getZscoreTableRow(child);
		Double median = zscoreTableRow.getMedian();
		Double stdDeviation = zscoreTableRow.getStdDeviation();
		
		Double zscoreValue = (weightValue - median)/(stdDeviation - median);
		ZscoreClassification zscoreClassification = getClassification(zscoreValue);
	}
}
