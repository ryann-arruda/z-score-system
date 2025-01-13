package entities.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DBException;
import db.Database;
import entities.Nutritionist;
import entities.School;
import entities.repository.DaoFactory;
import entities.repository.NutritionistDao;
import entities.repository.SchoolDao;

public class NutritionistDaoImpl implements NutritionistDao{
	private Connection conn;
	
	public NutritionistDaoImpl(Connection conn) {
		this.conn = conn;
	}
	
	private Nutritionist instantiateNutritionist(ResultSet rs) throws SQLException{
		Nutritionist nutritionist = new Nutritionist(rs.getString("nutritionist_name"),
													 new Date(rs.getDate("date_birth").getTime()), 
													 rs.getString("regional_council_nutritionists"),
													 rs.getString("nutritionist_username"),
													 rs.getString("nutritionist_password"));
		
		nutritionist.setId(rs.getLong("nutritionist_id"));
		
		Set<School> schools = getSchools(nutritionist.getId());
		
		for(School school : schools) {
			nutritionist.addSchool(school);
		}
		
		return nutritionist;
	}

	private Set<School> getSchools(Long id) {
		SchoolDao schoolDao = DaoFactory.createSchoolDao();
		Set<School> schools = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT school_id FROM Nutritionist_School WHERE nutritionist_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			schools = new HashSet<>();
			while(rs.next()) {
				School school  = schoolDao.findById(rs.getLong("school_id"));
				
				if(school != null) {
					schools.add(school);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to recover schools for this nutritionist");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return schools;
	}

	@Override
	public Long insert(Nutritionist obj) {
		SchoolDao schoolDaoImpl = DaoFactory.createSchoolDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long nutritionistId = null;
		
		try {
			if(obj.getId() == null) {
				if(findByAuthenticationInformation(obj.getUsername(), obj.getPassword()) == null) {
					conn.setAutoCommit(false);
					
					ps = conn.prepareStatement("INSERT INTO Nutritionist(nutritionist_name, date_birth, regional_council_nutritionists, " + 
											   "nutritionist_username, nutritionist_password)" +
											   " VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
					
					ps.setString(1, obj.getName());
					ps.setDate(2, new java.sql.Date(obj.getDate_birth().getTime()));
					ps.setString(3, obj.getRegionalCouncilNutritionists());
					ps.setString(4, obj.getUsername());
					ps.setString(5, obj.getPassword());
					
					List<Long> schoolsId = new ArrayList<>();
					for(School school: obj.getAllSchools()) {
						Long id = schoolDaoImpl.insert(school);
						school.setId(id);
						schoolsId.add(id);
					}
					
					if(ps.executeUpdate() > 0) {
						rs = ps.getGeneratedKeys();				
						
						if(rs.next()) {
							nutritionistId = rs.getLong(1);
							
							insertRelationships(schoolsId, nutritionistId);
							
							conn.commit();
						}
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to insert a new Nutritionist object into the database and undo what has been done");
			}
			
			throw new DBException("Unable to insert a new Nutritionist object into the database");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to insert a new Nutritionist object into the database and undo what has been done");
			}
			
			throw e;
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutritionistId;
	}

	private void insertRelationships(List<Long> schoolsId, Long nutritionistId) {
		PreparedStatement ps = null;
		
		try {
			for(Long schoolId : schoolsId) {				
				ps = conn.prepareStatement("INSERT INTO Nutritionist_School(nutritionist_id, school_id) VALUES(?,?)");
				ps.setLong(1, nutritionistId);
				ps.setLong(2, schoolId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("It's not possible to insert a relationship between the Nutritionist object and the School object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("It's not possible to insert a relationship between the Nutritionist object and the School object");
		}
		finally {
			Database.closeStatement(ps);
		}		
	}

	@Override
	public boolean update(Nutritionist obj) {
		SchoolDao schoolDao = DaoFactory.createSchoolDao();
		PreparedStatement ps = null;
		
		try {
			if(obj.getId() != null) {
				
				if(findById(obj.getId()) != null) {
					conn.setAutoCommit(false);
					
					ps = conn.prepareStatement("UPDATE Nutritionist SET nutritionist_name = ?, date_birth = ?, " + 
											   "regional_council_nutritionists = ?, nutritionist_username = ?, " +
									           "nutritionist_password = ? WHERE nutritionist_id = ?");
					
					ps.setString(1, obj.getName());
					ps.setDate(2, new java.sql.Date(obj.getDate_birth().getTime()));
					ps.setString(3, obj.getRegionalCouncilNutritionists());
					ps.setString(4, obj.getUsername());
					ps.setString(5, obj.getPassword());
					ps.setLong(6, obj.getId());
					
					if(ps.executeUpdate() > 0) {
						List<Long> newRelationshipsIds = new ArrayList<>();
						
						for(School school : obj.getAllSchools()) {
							if(!schoolDao.update(school)) {
								Long id = schoolDao.insert(school);
								school.setId(id);
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
				throw new DBException("It's not possible to update a Nutritionist object and undo what has already been updated");
			}
			
			throw new DBException("Unable to update a Nutritionist object");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot update a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to update a Nutritionist object and undo what has already been updated");
			}	
			
			throw e;
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeBrokenRelationships(Nutritionist obj) {
		SchoolDao schoolDao = DaoFactory.createSchoolDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT school_id FROM Nutritionist_School WHERE nutritionist_id = ?");
			ps.setLong(1, obj.getId());
			
			rs = ps.executeQuery();
			
			Set<School> schools = obj.getAllSchools();
			while(rs.next()) {
				ps = conn.prepareStatement("DELETE FROM Nutritionist_School WHERE school_id = ?");
				
				Long id = rs.getLong(1);
				
				if(schools.stream().noneMatch(x -> x.getId() == id)) {
					ps.setLong(1, id);
					ps.execute();
					
					schoolDao.deleteById(id);
				}
			}
		}
		catch(SQLException e) {			
			throw new DBException("You cannot break relationships between a Nutritionist object and its School object");
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
				
				ps = conn.prepareStatement("DELETE FROM Nutritionist WHERE nutritionist_id = ?");
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
				throw new DBException("It's not possible to delete the Nutritionist object and revert what has already been deleted");
			}
			
			throw new DBException("Unable to delete Nutritionist object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("It's not possible to delete the Nutritionist object and revert what has already been deleted");
			}
			
			throw e;
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeRelationships(Long id) {
		SchoolDao schoolDao = DaoFactory.createSchoolDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Long> schoolsId = null;
		
		try {			
			ps = conn.prepareStatement("SELECT school_id FROM Nutritionist_School WHERE nutritionist_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			schoolsId = new ArrayList<>();
			while(rs.next()) {
				schoolsId.add(rs.getLong(1));
			}
			
			for(Long schoolId : schoolsId) {
				ps = conn.prepareStatement("DELETE FROM Nutritionist_School WHERE school_id = ?");
				ps.setLong(1, schoolId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("Unable to delete relationships from Nutritionist object");
				}
				
				if(!schoolDao.deleteById(schoolId)) {
					throw new DBException("Unable to delete relationships from Nutritionist object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to delete relationships from Nutritionist object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
	}

	@Override
	public Nutritionist findById(Long id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutritionist = null;
		
		try {
			if(id != null) {
				ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE nutritionist_id = ?");
				ps.setLong(1, id);
				
				rs = ps.executeQuery();
				
				if(rs.next()) {
					nutritionist = instantiateNutritionist(rs);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve a Nutritionist object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutritionist;
	}

	@Override
	public Nutritionist findByAuthenticationInformation(String username, String password) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutritionist = null;
		
		try {
			if(username != null && password != null) {
				ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE nutritionist_username = ? AND nutritionist_password = ?");
				ps.setString(1, username);
				ps.setString(2, password);
				
				rs = ps.executeQuery();
				
				if(rs.next()) {
					nutritionist = instantiateNutritionist(rs);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve a Nutritionist object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutritionist;
	}
}
