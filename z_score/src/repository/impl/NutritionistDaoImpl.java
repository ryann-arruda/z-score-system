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
import entities.Nutritionist;
import entities.School;
import repository.DaoFactory;
import repository.NutritionistDao;
import repository.SchoolDao;

public class NutritionistDaoImpl implements NutritionistDao{
	private Connection conn;
	
	public NutritionistDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(Nutritionist obj) {
		SchoolDao schoolDaoImpl = DaoFactory.createSchoolDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long nutritionistId = null;
		
		try {
			if(obj.getId() == null) {
				conn.setAutoCommit(false);
				
				ps = conn.prepareStatement("INSERT INTO Nutritionist(nutritionist_name, date_birth, regional_council_nutritionists)" +
										   " VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
				
				ps.setString(1, obj.getName());
				ps.setDate(2, new java.sql.Date(obj.getDate_birth().getTime()));
				ps.setString(3, obj.getRegionalCouncilNutritionists());
				
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Nutritionist findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
