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
import entities.LevelEducation;
import entities.School;
import repository.DaoFactory;
import repository.LevelEducationDao;
import repository.SchoolDao;

public class SchoolDaoImpl implements SchoolDao{
	private Connection conn;
	
	public SchoolDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(School obj) {
		LevelEducationDao levelEducationDao = DaoFactory.createLevelEducationDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long schoolId = null;
		int i = -1;
		
		try {
			if(obj.getId() == null) {
				conn.setAutoCommit(false);
				
				ps = conn.prepareStatement("INSERT INTO School(school_name, national_registry_legal_entities) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
				
				ps.setString(1, obj.getName());
				ps.setString(2, obj.getNationalRegistryLegalEntities());
				
				List<Long> levelEducationIds = new ArrayList<>();
				for(LevelEducation levelEducation: obj.getAllEducationLevels()) {
					Long id = levelEducationDao.insert(levelEducation);
					levelEducation.setId(id);
					levelEducationIds.add(id);
				}
				
				if(ps.executeUpdate() > 0) {
					rs = ps.getGeneratedKeys();				
					
					if(rs.next()) {
						schoolId = rs.getLong(1);
						
						insertRelationships(levelEducationIds, schoolId);
						
						if(i < 0) {
							throw new SQLException();
						}	
						
						conn.commit();
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("Unable to insert a new School object and undo the one already inserted");
			}
			
			throw new DBException("Unable to insert a new School object into the database");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to insert a new School object and undo the one already inserted");
			}
			
			throw e;
		}
		finally{
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return schoolId;
	}

	private void insertRelationships(List<Long> levelEducationIds, Long schoolId) {
		PreparedStatement ps = null;
		
		try {
			for(Long levelEducationId : levelEducationIds) {				
				ps = conn.prepareStatement("INSERT INTO School_LevelEducation(school_id, level_education_id) VALUES(?,?)");
				ps.setLong(1, schoolId);
				ps.setLong(2, levelEducationId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("Unable to insert relationship between School object and LevelEducation object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to insert relationship between School object and LevelEducation object");
		}
		finally {
			Database.closeStatement(ps);
		}
	}

	@Override
	public boolean update(School obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public School findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<School> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
