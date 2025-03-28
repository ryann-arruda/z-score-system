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
import entities.ZscoreTableRow;
import entities.repository.ZscoreTableDao;

public class ZscoreTableDaoImpl implements ZscoreTableDao{
	private Connection conn;
	
	public ZscoreTableDaoImpl(Connection conn){
		this.conn = conn;
	}
	
	private ZscoreTable instantiateChild(ResultSet rs) throws SQLException {
		ZscoreTable zscoreTable = new ZscoreTable();
		
		while(rs.next()) {
			ZscoreTableRow zscoreTableRow = new ZscoreTableRow();
			
			zscoreTableRow.setId(rs.getLong("zscore_table_boys_id"));
			zscoreTableRow.setMedian(rs.getDouble("median"));
			zscoreTableRow.setStdDeviation(rs.getDouble("std_deviation"));
			
			zscoreTable.addRow(rs.getInt("months"), zscoreTableRow);
		}
		
		return zscoreTable;
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
				zscoreTables.add(instantiateChild(rs));
			}
			
			rs = st.executeQuery("SELECT * FROM ZscoreTableGirls");
			if(rs.next()) {
				zscoreTables.add(instantiateChild(rs));
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
