package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBException;
import db.Database;
import entities.Child;
import entities.LevelEducation;
import repository.ChildDao;
import repository.DaoFactory;
import repository.LevelEducationDao;

public class LevelEducationDaoImpl implements LevelEducationDao{
	private Connection conn;
	
	public LevelEducationDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(LevelEducation obj) {
		ChildDao childDao = DaoFactory.createChildDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		Long levelEducationId = null;
		
		try {
			if(obj.getId() == null) {
				conn.setAutoCommit(false);
				int i = -1;
				
				ps = conn.prepareStatement("INSERT INTO LevelEducation(level_education_name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, obj.getName());
				
				List<Long> childIds = new ArrayList<>();
				for(Child child : obj.getAllChildren()) {
					Long id = childDao.insert(child);
					child.setId(id);
					childIds.add(id);
					
					if(i < 0) {
						throw new DBException("test");
					}
				}
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					rs = ps.getGeneratedKeys();
					
					if(rs.next()) {
						levelEducationId = rs.getLong(1);
						
						insertRelationships(childIds, levelEducationId);
						
						conn.commit();
					}
				}
			}

		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("Unable to insert a new LevelEducation object and undo the one already inserted");
			}
			
			throw new DBException("Unable to insert a new LevelEducation object into the database");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("Unable to insert a new LevelEducation object and undo the one already inserted");
			}	
			
			throw e;
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return levelEducationId;
	}

	private void insertRelationships(List<Long> childIds, Long levelEducationId) {
		PreparedStatement ps = null;
		
		try {
			for(Long childId : childIds) {				
				ps = conn.prepareStatement("INSERT INTO LevelEducation_Child(level_education_id, child_id) VALUES(?,?)");
				ps.setLong(1, levelEducationId);
				ps.setLong(2, childId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("Cannot insert relationship between LevelEducation object and Child object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Cannot insert relationship between LevelEducation object and Child object");
		}
		finally {
			Database.closeStatement(ps);
		}
	}

	@Override
	public boolean update(LevelEducation obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LevelEducation findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LevelEducation> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
