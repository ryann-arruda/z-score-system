package entities.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBException;
import db.Database;
import entities.ZscoreTable;
import entities.repository.ZscoreTableDao;

public class ZscoreTableDaoImpl implements ZscoreTableDao{
	private Connection conn;
	
	public ZscoreTableDaoImpl(Connection conn){
		this.conn = conn;
	}

	@Override
	public List<ZscoreTable> findAll() {
		List<ZscoreTable> zscoreTables = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM ZscoreTableBoys");
			
			zscoreTables = new ArrayList<>();
			if(rs.next()) {
				zscoreTables.add(instantiateChild());
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to retrieve all ZscoreTable objects");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return zscoreTables;
	}

}
