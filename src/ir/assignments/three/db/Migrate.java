package ir.assignments.three.db;

/**
 * A very basic class that knows how to migrate the database to desired schema. */
public class Migrate {

	/**
	 * A really primitive set of migrations. */
	static String migrations[] = {
		"CREATE TABLE IF NOT EXISTS pages("+PageRepo.schema+")"
	};

	/**
	 * Runs migrations in production.
	 *
	 * Argument `pristine` drops all tables before migration */
	public static void main(String[] args) {
		Database.connect("prod");

		if (args.length >= 1 && args[0].equals("pristine")) {
			dropAllTables();
		}

		runMigrations();
	}

	/**
	 * Runs all the migrations. */
	public static void runMigrations() {
		for (String sql : migrations)
			Database.executeUpdate(sql);
	}

	/**
	 * Destructive action that drops all database tables. */
	public static void dropAllTables() {
		Database.executeUpdate("DROP TABLE IF EXISTS PAGES");
	}
}
