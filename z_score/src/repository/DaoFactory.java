package repository;

import db.Database;
import repository.impl.ChildDaoImpl;
import repository.impl.LevelEducationDaoImpl;
import repository.impl.MeasurementZscoreDaoImpl;
import repository.impl.NutritionistDaoImpl;
import repository.impl.SchoolDaoImpl;

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
}
