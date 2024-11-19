package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
	private static Connection conn;
	
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String dburl = props.getProperty("dburl");
				conn = DriverManager.getConnection(dburl, props);
			}
			catch(SQLException e) {
				throw new DBException("Unable to connect to database");
			}
		}
		
		return conn;
	}
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			}
			catch(SQLException e) {
				throw new DBException("Unable to close database connection");
			}
		}
	}

	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			
			return props;
		}
		catch(IOException e) {
			throw new DBException("Unable to load database properties");
		}
	}
	
	public static void closeStatement(Statement st) {
		
		try {
			if(st != null) {
				st.close();
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to close resources");
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
			}
		}
		catch(SQLException e) {
			throw new DBException("Unable to close resources");
		}
	}
}
