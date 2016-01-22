package ir.test;

import ir.assignments.three.db.Database;
import ir.assignments.three.db.Migrate;

/**
 * Defines static methods for use in tests.
 *
 * It is not meant to be instantiated. Instead, import it into your tests with
 * <code>import static ir.test.TestHelper.*</code>
 */
public class TestHelper {
	public static void resetDatabase() {
		Database.connect("test");
		Migrate.dropAllTables();
		Migrate.runMigrations();
	}
}
