package entities.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import db.DBException;
import db.Database;
import entities.Child;
import entities.MeasurementZscore;
import entities.enums.PersonSex;
import entities.repository.ChildDao;
import entities.repository.DaoFactory;
import entities.repository.MeasurementZscoreDao;

public class ChildDaoImpl implements ChildDao{
	private Connection conn;
	
	public ChildDaoImpl(Connection conn) {
		this.conn = conn;
	}
	
	private Child instantiateChild(ResultSet rs) throws SQLException{
		Child child = new Child();
		
		child.setId(rs.getLong("child_id"));
		child.setName(rs.getString("child_name"));
		child.setDateBirth(new Date(rs.getDate("date_birth").getTime()));
		child.setSex(PersonSex.valueOf(rs.getString("sex")));
		
		Set<MeasurementZscore> zScores = getMeasurementRelationships(child.getId());
		
		if(zScores != null) {
			for(MeasurementZscore msz : zScores) {
				child.addZscore(msz);
			}
		}
		
		return child;
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
				ps = conn.prepareStatement("INSERT INTO Child(child_name, date_birth, sex) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
				
				ps.setString(1, obj.getName());
				ps.setDate(2, new java.sql.Date(obj.getDateBirth().getTime()));
				ps.setString(3, obj.getSex().name());
				
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
					}
				}
			}
		}
		catch(SQLException e) {
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
				ps = conn.prepareStatement("INSERT INTO Child_MeasurementZscore(child_id, measurement_zscore_id) VALUES(?,?)");
				
				ps.setLong(1, childId);
				ps.setLong(2, id);
				
				if(ps.executeUpdate() < 0) {
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
		MeasurementZscoreDao mszDao = DaoFactory.createMeasurementZscoreDao();
		PreparedStatement ps = null;
		
		try {
			if(obj.getId() != null) {
				
				if(findById(obj.getId()) != null) {
					ps = conn.prepareStatement("UPDATE Child SET child_name = ?, date_birth = ?, sex = ? " + 
											   "WHERE child_id = ?");
					
					obj.setName(obj.getName());
					obj.setDateBirth(obj.getDateBirth());
					obj.setSex(obj.getSex());
					
					ps.setString(1, obj.getName());
					ps.setDate(2, new java.sql.Date(obj.getDateBirth().getTime()));
					ps.setString(3, obj.getSex().name());
					ps.setLong(4, obj.getId());
					
					if(ps.executeUpdate() > 0) {
						List<Long> newRelationshipsIds = new ArrayList<>();
						
						for(MeasurementZscore msz : obj.getAllZscores()) {
							if(!mszDao.update(msz)) {
								Long id = mszDao.insert(msz);
								msz.setId(id);
								newRelationshipsIds.add(id);
							}
						}
						
						insertRelationships(newRelationshipsIds, obj.getId());
						removeBrokenRelationships(obj);
						
						return true;
					}
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to update a Child object");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot update a null object");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeBrokenRelationships(Child obj) {
		MeasurementZscoreDao mszDao = DaoFactory.createMeasurementZscoreDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT measurement_zscore_id FROM Child_MeasurementZscore WHERE child_id = ?");
			ps.setLong(1, obj.getId());
			
			rs = ps.executeQuery();
			
			Set<MeasurementZscore> zscores = obj.getAllZscores();
			while(rs.next()) {
				ps = conn.prepareStatement("DELETE FROM Child_MeasurementZscore WHERE measurement_zscore_id = ?");
				
				Long id = rs.getLong(1);
				
				if(zscores.stream().noneMatch(x -> x.getId() == id)) {
					ps.setLong(1, id);
					ps.execute();
					
					mszDao.deleteById(id);
				}
			}
		}
		catch(SQLException e) {			
			throw new DBException("You cannot break relationships between a child object and its z-score measurement");
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
				removeRelationships(id);
				
				ps = conn.prepareStatement("DELETE FROM Child WHERE child_id = ?");
				ps.setLong(1, id);
				
				if(ps.executeUpdate() > 0) {
					return true;
				}
			}
		}
		catch(SQLException e) {			
			throw new DBException("Cannot delete Child object");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeRelationships(Long id) {
		MeasurementZscoreDao mszDao = DaoFactory.createMeasurementZscoreDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Long> zscoreIds = null;
		
		try {
			ps = conn.prepareStatement("SELECT measurement_zscore_id FROM Child_MeasurementZscore WHERE child_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			zscoreIds = new ArrayList<>();
			while(rs.next()) {
				zscoreIds.add(rs.getLong(1));
			}
			
			for(Long zscoreId : zscoreIds) {
				ps = conn.prepareStatement("DELETE FROM Child_MeasurementZscore WHERE measurement_zscore_id = ?");
				ps.setLong(1, zscoreId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("Cannot delete relationships from Child object");
				}
				
				if(!mszDao.deleteById(zscoreId)) {
					throw new DBException("Cannot delete relationships from Child object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Cannot delete relationships from Child object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
	}

	@Override
	public Child findById(Long id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Child child = null;
		
		try {
			if(id != null) {
				ps = conn.prepareStatement("SELECT * FROM Child WHERE child_id = ?");
				
				ps.setLong(1, id);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					child = instantiateChild(rs);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve a Child object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return child;
	}

	private Set<MeasurementZscore> getMeasurementRelationships(Long id) {
		MeasurementZscoreDao mszDao = DaoFactory.createMeasurementZscoreDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<MeasurementZscore> zScores = null;
		
		try {
			ps = conn.prepareStatement("SELECT measurement_zscore_id FROM Child_MeasurementZscore WHERE child_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			zScores = new LinkedHashSet<>();
			while(rs.next()) {
				MeasurementZscore msz = mszDao.findById(rs.getLong("measurement_zscore_id"));
				
				if(msz != null) {
					zScores.add(msz);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve z-score measure relationships from Child object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return zScores;
	}

	@Override
	public List<Child> findAll() {
		Statement st = null;
		ResultSet rs = null;
		List<Child> children = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM Child");
			
			children = new ArrayList<>();
			while(rs.next()) {				
				children.add(instantiateChild(rs));
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve all Child objects");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return children;
	}

}
