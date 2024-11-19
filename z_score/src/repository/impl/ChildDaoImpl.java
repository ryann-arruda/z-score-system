package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DBException;
import db.Database;
import entities.Child;
import repository.ChildDao;

public class ChildDaoImpl implements ChildDao{
	private Connection conn;
	
	public ChildDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(Child obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		Long childId = null;
		
		try {
			if(obj.getId() != null) {
				ps = conn.prepareStatement("INSERT INTO Child(child_name, date_birth) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
				conn.setAutoCommit(false);
				
				ps.setString(1, obj.getName());
				ps.setDate(2, new java.sql.Date(obj.getDate_birth().getTime()));
				
				// TODO Enter all z-score measurements
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					rs = ps.getGeneratedKeys();
					
					while(rs.next()) {
						childId = rs.getLong(1);
						conn.setAutoCommit(true);
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
