package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import db.DBException;
import db.Database;
import entities.Child;
import entities.MeasurementZscore;
import repository.ChildDao;
import repository.DaoFactory;
import repository.MeasurementZscoreDao;

public class ChildDaoImpl implements ChildDao{
	private Connection conn;
	
	public ChildDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(Child obj) {
		MeasurementZscoreDao mszImpl = DaoFactory.createMeasurementZscoreDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		Long childId = null;
		
		try {
			if(obj.getId() == null) {
				ps = conn.prepareStatement("INSERT INTO Child(child_name, date_birth) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
				conn.setAutoCommit(false);
				
				ps.setString(1, obj.getName());
				ps.setDate(2, new java.sql.Date(obj.getDate_birth().getTime()));
				
				List<Long> measurementZscoreIds = new ArrayList<>();
				for(MeasurementZscore msz : obj.getAllZscores()) {
					Long id = mszImpl.insert(msz);
					msz.setId(id);
					measurementZscoreIds.add(id);
				}
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					rs = ps.getGeneratedKeys();
					
					while(rs.next()) {
						childId = rs.getLong(1);
						
						insertRelationships(measurementZscoreIds, childId);
												
						conn.commit();
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It was not possible to insert a new Child object and undo the one that was already inserted");
			}
			throw new DBException("Unable to insert a new Child object into the database");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return childId;
	}

	private void insertRelationships(List<Long> measurementZscoreIds, Long childId) {
		PreparedStatement ps = null;
		
		try {
			for(Long id : measurementZscoreIds) {
				int rowsAffected = -1;
				ps = conn.prepareStatement("INSERT INTO Child_MeasurementZscore(child_id, measurement_zscore_id) VALUES(?,?)");
				
				ps.setLong(1, childId);
				ps.setLong(2, id);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected < 0) {
					throw new DBException("Unable to insert relationship between Child object and MeasurementZscore object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to insert relationship between Child object and MeasurementZscore object");
		}
		finally {
			Database.closeStatement(ps);
		}
	}

	@Override
	public boolean update(Child obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Child findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Child> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
