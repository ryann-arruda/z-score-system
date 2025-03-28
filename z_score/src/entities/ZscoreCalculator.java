package entities;

import entities.service.ZscoreTableService;

public class ZscoreCalculator {
	private ZscoreTableService service;
	
	public ZscoreCalculator(ZscoreTableService service) {
		this.service = service;
	}
}
