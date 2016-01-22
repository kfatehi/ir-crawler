package ir.test;

import java.io.File;

public class Fixtures {
	public static File get(String name) {
		return new File("test/fixtures/"+name);
	}
}
