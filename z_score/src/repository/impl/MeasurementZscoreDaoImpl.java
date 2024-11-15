package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import db.DBException;
import entities.MeasurementZscore;
import repository.MeasurementZscoreDao;

public class MeasurementZscoreDaoImpl implements MeasurementZscoreDao{
	private Connection conn;
	
	public MeasurementZscoreDaoImpl(Connection conn) {
		this.conn = conn;
	}
	
	private MeasurementZscore instantiateMeasurementZscore(ResultSet rs) throws SQLException {
		MeasurementZscore msz = new MeasurementZscore();
		
		msz.setId(rs.getLong("measurement_zscore_id"));
		msz.setzScore(rs.getDouble("zscore"));
		msz.setDate(new Date(rs.getDate("zscore_date").getTime()));
		
		return msz;
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		MeasurementZscore mzs = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM MeasurementZscore WHERE measurementzscore = ?");
			
			ps.setLong(1, 1);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				mzs = instantiateMeasurementZscore(rs);
			}
		}
		catch(SQLException e) {
			throw new DBException("ID was null");
		}
		
		return mzs;
	}

	@Override
	public List<MeasurementZscore> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
