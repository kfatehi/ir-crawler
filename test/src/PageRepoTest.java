package ir.test;

import ir.assignments.three.db.PageRepo;

import static ir.test.TestHelper.*;

import junit.framework.TestCase;

public class PageRepoTest extends TestCase {
	public void setUp() {
		resetDatabase();
	}

	public void testExistsWithURL() throws Exception {
		String url = "somewhere";
		String text = "something";
		assertFalse(PageRepo.existsWithURL(url));
		PageRepo.insert(url, text);
		assertTrue(PageRepo.existsWithURL(url));
	}
}
