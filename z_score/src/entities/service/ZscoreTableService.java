package entities.service;

import java.util.Map;

import entities.ZscoreTable;
import entities.repository.DaoFactory;
import entities.repository.ZscoreTableDao;

public class ZscoreTableService {
	
	private ZscoreTableDao zscoreTableDaoImpl = DaoFactory.createZscoreTableDao();
	
	public Map<String, ZscoreTable> getZscoreTable(){
		return zscoreTableDaoImpl.findAll();
	}
}
