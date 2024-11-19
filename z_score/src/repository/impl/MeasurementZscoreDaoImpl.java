package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DBException;
import db.Database;
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
	public Long insert(MeasurementZscore obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		Long measurementZscoreId = -1L;
		
		try {
			if(obj.getId() == null) {
				ps = conn.prepareStatement("INSERT INTO MeasurementZscore(zscore, zscore_date) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
				
				ps.setDouble(1, obj.getzScore());
				ps.setDate(2, new java.sql.Date(obj.getDate().getTime()));
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					rs = ps.getGeneratedKeys();
					
					while(rs.next()) {
						measurementZscoreId = rs.getLong(1);
					}
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to insert a new z-score measurement into the database");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return measurementZscoreId;
	}

	@Override
	public boolean update(MeasurementZscore obj) {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		MeasurementZscore msz = null;
		
		try {
			if(obj.getId() != null) {
				msz = findById(obj.getId());
				
				if(msz != null) {
					msz.setDate(obj.getDate());
					msz.setzScore(obj.getzScore());
					
					ps = conn.prepareStatement("UPDATE MeasurementZscore SET zscore = ?, zscore_date = ? "+
					                           "WHERE measurement_zscore_id = ?");
					
					ps.setDouble(1, msz.getzScore());
					ps.setDate(2, new java.sql.Date(msz.getDate().getTime()));
					ps.setLong(3, msz.getId());
					
					rowsAffected = ps.executeUpdate();
					
					if(rowsAffected > 0) {
						return true;
					}
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to update this measurement z-score");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	@Override
	public boolean deleteById(Long id) {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			MeasurementZscore msz = findById(id);
			
			if(msz != null) {
				ps = conn.prepareStatement("DELETE FROM MeasurementZscore WHERE measurement_zscore_id = ?");
				
				ps.setLong(1, id);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					return true;
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to remove this z-score measurement from the database");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	@Override
	public MeasurementZscore findById(Long id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		MeasurementZscore mzs = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM MeasurementZscore WHERE measurement_zscore_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				mzs = instantiateMeasurementZscore(rs);
			}
		}
		catch(SQLException e) {
			throw new DBException("ID was null");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return mzs;
	}

	@Override
	public List<MeasurementZscore> findAll() {
		Statement st = null;
		ResultSet rs = null;
		List<MeasurementZscore> allMeasurementZscore = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM MeasurementZscore");
			
			allMeasurementZscore = new ArrayList<>();
			while(rs.next()) {
				MeasurementZscore mzs = instantiateMeasurementZscore(rs);
				allMeasurementZscore.add(mzs);
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve z-score measurements");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return allMeasurementZscore;
	}

}
