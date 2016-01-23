package ir.assignments.three.db;
import java.sql.*;

/**
 * Maps to the pages database table. */
public class PageRepo {

	/**
	 * Pages database schema.
	 * table: pages
	 * columns: int id, string url, text html, text text */
	public static String schema =
		"ID SERIAL PRIMARY KEY, URL CHAR(2083) UNIQUE NOT NULL, HTML TEXT NOT NULL, TEXT TEXT NOT NULL";

	/**
	 * Does a page exist in the database with this URL? */
	public static boolean existsWithURL(String url) {
		boolean exists = false;
		Connection con = null;
		try {
			con = Database.getConnection();
			PreparedStatement st = con.prepareStatement(
					"SELECT EXISTS(SELECT 1 FROM PAGES WHERE URL = ?)");
			st.setString(1, url);
			ResultSet rs = st.executeQuery();
			rs.next();
			exists = rs.getBoolean(1);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try { con.close(); } catch (SQLException e) {}
			}
		}
		return exists;
	}

	public static boolean insert(String url, String html, String text) {
		Connection con = null;
		try {
			con = Database.getConnection();
			PreparedStatement st = con.prepareStatement(
					"INSERT INTO PAGES (URL,HTML,TEXT) VALUES (?,?,?)");
			st.setString(1, url);
			st.setString(2, html);
			st.setString(3, text);
			st.executeUpdate();
			st.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try { con.close(); } catch (SQLException e) {}
			}
		}
		return false;
	}
}
