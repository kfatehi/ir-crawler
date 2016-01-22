package ir.assignments.three.db;

import java.sql.*;
import java.util.Properties;

/**
 * Provides access to a postgres database. */
public class Database {

	/**
	 * Database connection */
	public static Connection conn;

	/**
	 * Returns true if connected to the database, otherwise returns false */
	public static boolean connected() {
		return conn != null;
	}

	/**
	 * Connect to the database.
	 * @param env Environment. Can be either prod or test.  */
	public static boolean connect(String env) {
		if (connected()) {
			return true;
		} else {
			try {
				String url = "jdbc:postgresql://keyvan.pw:25432/ir_"+env;
				Properties props = new Properties();
				props.setProperty("user", "ir_"+env);
				props.setProperty("password", System.getProperty("pg_password"));
				props.setProperty("ssl", "true");
				conn = DriverManager.getConnection(url, props);
				return true;
			} catch(SQLException e) {
				System.out.println("SQLException. Continuing without database.");
				return false;
			}
		}
	}

	/**
	 * Execute a SQL command such as INSERT, UPDATE, or DELETE,
	 * or any SQL command that returns nothing. */
	public static boolean executeUpdate(String sql) {
		if (connected()) {
			try {
				Statement st = conn.createStatement();
				st.executeUpdate(sql);
				st.close();
				System.out.println(sql);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
