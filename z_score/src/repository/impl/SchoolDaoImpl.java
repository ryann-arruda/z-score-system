package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
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
	
	private School instantiateSchool(ResultSet rs) throws SQLException {
		School school = new School(rs.getString("school_name"), rs.getString("national_registry_legal_entities"));
		
		school.setId(rs.getLong("school_id"));
		
		List<LevelEducation> educationLevels = getEducationLevels(school.getId());
		
		for(LevelEducation le : educationLevels) {
			school.addEducationLevel(le);
		}
		
		return school;
	}

	private List<LevelEducation> getEducationLevels(Long id) {
		LevelEducationDao levelEducDao = DaoFactory.createLevelEducationDao();
		List<LevelEducation> educationLevels = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT level_education_id FROM School_LevelEducation WHERE school_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			educationLevels = new ArrayList<>();
			while(rs.next()) {
				LevelEducation levelEducation  = levelEducDao.findById(rs.getLong("level_education_id"));
				
				if(levelEducation != null) {
					educationLevels.add(levelEducation);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to recover education levels for this school");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return educationLevels;
	}

	@Override
	public Long insert(School obj) {
		LevelEducationDao levelEducationDao = DaoFactory.createLevelEducationDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long schoolId = null;
		
		try {
			if(obj.getId() == null) {
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
					}
				}
			}
		}
		catch(SQLException e) {			
			throw new DBException("Unable to insert a new School object into the database");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
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
		LevelEducationDao levelEducDao = DaoFactory.createLevelEducationDao();
		PreparedStatement ps = null;
		
		try {
			if(obj.getId() != null) {
				
				if(findById(obj.getId()) != null) {
					conn.setAutoCommit(false);
					
					ps = conn.prepareStatement("UPDATE School SET school_name = ?, national_registry_legal_entities = ? WHERE school_id = ?");
					
					ps.setString(1, obj.getName());
					ps.setString(2, obj.getNationalRegistryLegalEntities());
					ps.setLong(3, obj.getId());
					
					if(ps.executeUpdate() > 0) {
						List<Long> newRelationshipsIds = new ArrayList<>();
						
						for(LevelEducation le : obj.getAllEducationLevels()) {
							if(!levelEducDao.update(le)) {
								Long id = levelEducDao.insert(le);
								le.setId(id);
								newRelationshipsIds.add(id);
							}
						}
						
						insertRelationships(newRelationshipsIds, obj.getId());
						removeBrokenRelationships(obj);
						
						conn.commit();
						
						return true;
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to update a School object and undo what has already been updated");
			}
			
			throw new DBException("Unable to update a School object");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot update a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to update a School object and undo what has already been updated");
			}	
			
			throw e;
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeBrokenRelationships(School obj) {
		LevelEducationDao levelEducDao = DaoFactory.createLevelEducationDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT level_education_id FROM School_LevelEducation WHERE school_id = ?");
			ps.setLong(1, obj.getId());
			
			rs = ps.executeQuery();
			
			List<LevelEducation> educationLevels = obj.getAllEducationLevels();
			while(rs.next()) {
				ps = conn.prepareStatement("DELETE FROM School_LevelEducation WHERE level_education_id = ?");
				
				Long id = rs.getLong(1);
				
				if(educationLevels.stream().noneMatch(x -> x.getId() == id)) {
					ps.setLong(1, id);
					ps.execute();
					
					levelEducDao.deleteById(id);
				}
			}
		}
		catch(SQLException e) {			
			throw new DBException("You cannot break relationships between a School object and its LevelEducation object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
	}

	@Override
	public boolean deleteById(Long id) {
		PreparedStatement ps = null;
		
		try {
			if(findById(id) != null) {
				conn.setAutoCommit(false);
				
				removeRelationships(id);
				
				ps = conn.prepareStatement("DELETE FROM School WHERE school_id = ?");
				ps.setLong(1, id);
				
				if(ps.executeUpdate() > 0) {					
					conn.commit();
					
					return true;
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to delete the School object and revert what has already been deleted");
			}
			
			throw new DBException("Cannot delete School object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to delete the School object and revert what has already been deleted");
			}
			
			throw e;
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeRelationships(Long id) {
		LevelEducationDao levelEducDao = DaoFactory.createLevelEducationDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Long> educationLevelsIds = null;
		
		try {			
			ps = conn.prepareStatement("SELECT level_education_id FROM School_LevelEducation WHERE school_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			educationLevelsIds = new ArrayList<>();
			while(rs.next()) {
				educationLevelsIds.add(rs.getLong(1));
			}
			
			for(Long levelEducationId : educationLevelsIds) {
				ps = conn.prepareStatement("DELETE FROM School_LevelEducation WHERE level_education_id = ?");
				ps.setLong(1, levelEducationId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("Unable to delete relationships from School object");
				}
				
				if(!levelEducDao.deleteById(levelEducationId)) {
					throw new DBException("Unable to delete relationships from School object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to delete relationships from School object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
	}

	@Override
	public School findById(Long id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		School school = null;
		
		try {
			if(id != null) {
				ps = conn.prepareStatement("SELECT * FROM School WHERE school_id = ?");
				
				ps.setLong(1, id);
				
				rs = ps.executeQuery();
				
				if(rs.next()) {
					school = instantiateSchool(rs);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve a School object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return school;
	}

	@Override
	public Set<School> findAll() {
		Set<School> schools = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM School");
			
			schools = new HashSet<>();
			while(rs.next()) {
				schools.add(instantiateSchool(rs));
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve all School objects");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);			
		}
		
		return schools;
	}
}
