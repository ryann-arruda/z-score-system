package repository.impl;

import java.sql.Connection;
import java.util.List;

import entities.MeasurementZscore;
import repository.MeasurementZscoreDao;

public class MeasurementZscoreDaoImpl implements MeasurementZscoreDao{
	private Connection conn;
	
	public MeasurementZscoreDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(MeasurementZscore obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(MeasurementZscore obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MeasurementZscore findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeasurementZscore> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
