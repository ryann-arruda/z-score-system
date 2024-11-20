package repository;

import db.Database;
import repository.impl.ChildDaoImpl;
import repository.impl.MeasurementZscoreDaoImpl;

public class DaoFactory {	
	
	private DaoFactory() {
		
	}
	
	public static MeasurementZscoreDao createMeasurementZscoreDao() {
		return new MeasurementZscoreDaoImpl(Database.getConnection());
	}
	
	public static ChildDao createChildDao() {
		return new ChildDaoImpl(Database.getConnection());
	}
}
