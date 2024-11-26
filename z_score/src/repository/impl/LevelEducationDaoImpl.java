package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import db.DBException;
import entities.LevelEducation;
import repository.LevelEducationDao;

public class LevelEducationDaoImpl implements LevelEducationDao{
	private Connection conn;
	
	public LevelEducationDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(LevelEducation obj) {
		PreparedStatement ps = null;
		Long levelEducationId = null;
		
		try {
			if(obj.getId() == null) {
				conn.setAutoCommit(false);
				
				ps = conn.prepareStatement("INSERT INTO LevelEducation(?) VALUES(?)");
				ps.setString(1, obj.getName());
			}

		}
		catch(SQLException e) {
			throw new DBException("");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot insert a null object");
		}
		finally {
			
		}
		
		return levelEducationId;
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
