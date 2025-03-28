package entities.repository;

import db.Database;
import entities.repository.impl.ChildDaoImpl;
import entities.repository.impl.LevelEducationDaoImpl;
import entities.repository.impl.MeasurementZscoreDaoImpl;
import entities.repository.impl.NutritionistDaoImpl;
import entities.repository.impl.SchoolDaoImpl;
import entities.repository.impl.ZscoreTableDaoImpl;

public class DaoFactory {	
	
	private DaoFactory() {
		
	}
	
	public static MeasurementZscoreDao createMeasurementZscoreDao() {
		return new MeasurementZscoreDaoImpl(Database.getConnection());
	}
	
	public static ChildDao createChildDao() {
		return new ChildDaoImpl(Database.getConnection());
	}
	
	public static LevelEducationDao createLevelEducationDao() {
		return new LevelEducationDaoImpl(Database.getConnection());
	}
	
	public static SchoolDao createSchoolDao() {
		return new SchoolDaoImpl(Database.getConnection());
	}
	
	public static NutritionistDao createNutritionistDao() {
		return new NutritionistDaoImpl(Database.getConnection());
	}
	
	public static ZscoreTableDao createZscoreTableDao() {
		return new ZscoreTableDaoImpl(Database.getConnection());
	}
}
