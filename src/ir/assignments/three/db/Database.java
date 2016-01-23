package ir.assignments.three.db;

import java.sql.*;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Provides access to a postgres database. */
public class Database {

	public static HikariDataSource ds;

	/**
	 * Returns true if database is configured, otherwise returns false */
	public static boolean configured() {
		return ds != null;
	}

	/**
	 * Configure the database connection pool.
	 *
	 * @param env Environment. Can be either prod or test.  */
	public static boolean configure(String env) {
		if (configured()) {
			return true;
		} else {
			String url = "jdbc:postgresql://keyvan.pw:25432/ir_"+env;
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(url);
			config.setUsername("ir_"+env);
			config.setPassword(System.getProperty("pg_password"));
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			ds = new HikariDataSource(config);
			return true;
		}
	}

	/**
	 * Get a connection from the connection pool.
	 */
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	/**
	 * Execute a SQL command such as INSERT, UPDATE, or DELETE,
	 * or any SQL command that returns nothing. */
	public static boolean executeUpdate(String sql) {
		if (configured()) {
			try {
				Statement st = getConnection().createStatement();
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
