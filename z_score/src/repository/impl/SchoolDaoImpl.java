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
						
						conn.commit();
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("");
			}
			
			throw new DBException("");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("");
			}
			
			throw new DBException("");
		}
		finally{
			
		}
		
		return schoolId;
	}

	private void insertRelationships(List<Long> levelEducationIds, Long schoolId) {
		// TODO Auto-generated method stub
		
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
