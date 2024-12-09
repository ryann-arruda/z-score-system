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
	
	private LevelEducation instantiateLevelEducation(ResultSet rs) throws SQLException {
		LevelEducation levelEducation = new LevelEducation(rs.getString("level_education_name"));
		
		levelEducation.setId(rs.getLong("level_education_id"));
		
		List<Child> children = getChildrenLevelEducation(levelEducation.getId());
		
		for(Child child : children) {
			levelEducation.addChild(child);
		}
		
		return levelEducation;
	}

	private List<Child> getChildrenLevelEducation(Long id) {
		ChildDao childDao = DaoFactory.createChildDao();
		List<Child> children = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT child_id FROM LevelEducation_Child WHERE level_education_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			children = new ArrayList<>();
			while(rs.next()) {
				Child child = childDao.findById(rs.getLong("child_id"));
				
				if(child != null) {
					children.add(child);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("It's not possible to retrieve children from this Level of Education");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return children;
	}

	@Override
	public Long insert(LevelEducation obj) {
		ChildDao childDao = DaoFactory.createChildDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long levelEducationId = null;
		
		try {
			if(obj.getId() == null) {
				conn.setAutoCommit(false);
				
				ps = conn.prepareStatement("INSERT INTO LevelEducation(level_education_name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, obj.getName());
				
				List<Long> childIds = new ArrayList<>();
				for(Child child : obj.getAllChildren()) {
					Long id = childDao.insert(child);
					child.setId(id);
					childIds.add(id);
				}
				
				if(ps.executeUpdate() > 0) {
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
		ChildDao childDao = DaoFactory.createChildDao();
		PreparedStatement ps = null;
		
		try {
			if(obj.getId() != null) {
				
				if(findById(obj.getId()) != null) {
					conn.setAutoCommit(false);
					
					ps = conn.prepareStatement("UPDATE LevelEducation SET level_education_name = ? WHERE level_education_id = ?");
					
					ps.setString(1, obj.getName());
					ps.setLong(2, obj.getId());
					
					if(ps.executeUpdate() > 0) {
						List<Long> newRelationshipsIds = new ArrayList<>();
						
						for(Child child : obj.getAllChildren()) {
							if(!childDao.update(child)) {
								Long id = childDao.insert(child);
								child.setId(id);
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
				throw new DBException("You cannot update a LevelEducation object and undo what has already been updated");
			}
			
			throw new DBException("Unable to update a LevelEducation object");
		}
		catch(NullPointerException e) {
			throw new DBException("Cannot update a null object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("You cannot update a LevelEducation object and undo what has already been updated");
			}	
			
			throw e;
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeBrokenRelationships(LevelEducation obj) {
		ChildDao childDao = DaoFactory.createChildDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT child_id FROM LevelEducation_Child WHERE level_education_id = ?");
			ps.setLong(1, obj.getId());
			
			rs = ps.executeQuery();
			
			List<Child> children = obj.getAllChildren();
			while(rs.next()) {
				ps = conn.prepareStatement("DELETE FROM LevelEducation_Child WHERE child_id = ?");
				
				Long id = rs.getLong(1);
				
				if(children.stream().noneMatch(x -> x.getId() == id)) {
					ps.setLong(1, id);
					ps.execute();
					
					childDao.deleteById(id);
				}
			}
		}
		catch(SQLException e) {			
			throw new DBException("You cannot break relationships between a LevelEducation object and its Child object");
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
				
				ps = conn.prepareStatement("DELETE FROM LevelEducation WHERE level_education_id = ?");
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
				throw new DBException("Unable to delete LevelEducation object and revert what has already been deleted");
			}
			
			throw new DBException("Cannot delete LevelEducation object");
		}
		catch(DBException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				throw new DBException("Unable to delete LevelEducation object and revert what has already been deleted");
			}
			
			throw e;
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}

	private void removeRelationships(Long id) {
		ChildDao childDao = DaoFactory.createChildDao();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Long> childrenIds = null;
		
		try {			
			ps = conn.prepareStatement("SELECT child_id FROM LevelEducation_Child WHERE level_education_id = ?");
			
			ps.setLong(1, id);
			
			rs = ps.executeQuery();
			
			childrenIds = new ArrayList<>();
			while(rs.next()) {
				childrenIds.add(rs.getLong(1));
			}
			
			for(Long childId : childrenIds) {
				ps = conn.prepareStatement("DELETE FROM LevelEducation_Child WHERE child_id = ?");
				ps.setLong(1, childId);
				
				if(ps.executeUpdate() < 0) {
					throw new DBException("Unable to delete relationships from LevelEducation object");
				}
				
				if(!childDao.deleteById(childId)) {
					throw new DBException("Unable to delete relationships from LevelEducation object");
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to delete relationships from LevelEducation object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
	}

	@Override
	public LevelEducation findById(Long id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		LevelEducation levelEducation = null;
		
		try {
			if(id != null) {
				ps = conn.prepareStatement("SELECT * FROM LevelEducation WHERE level_education_id = ?");
				
				ps.setLong(1, id);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					levelEducation = instantiateLevelEducation(rs);
				}
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve a LevelEducation object");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return levelEducation;
	}

	@Override
	public List<LevelEducation> findAll() {
		List<LevelEducation> educationLevels = null;;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM LevelEducation");
			
			educationLevels = new ArrayList<>();
			while(rs.next()) {
				educationLevels.add(instantiateLevelEducation(rs));
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve all LevelEducation objects");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return educationLevels;
	}

}
