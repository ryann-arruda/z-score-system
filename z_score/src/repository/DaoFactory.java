package repository;

import java.sql.Connection;

import repository.impl.MeasurementZscoreDaoImpl;

public class DaoFactory {
	private Connection conn;
	
	public DaoFactory(Connection conn) {
		this.conn = conn;
	}
	
	public MeasurementZscoreDao createMeasurementZscoreDao() {
		return new MeasurementZscoreDaoImpl(conn);
	}
}
