package ir.test;

import ir.assignments.three.Crawler;

import static ir.test.TestHelper.*;

import junit.framework.TestCase;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerTest extends TestCase {

	Crawler crawler = new Crawler();

	/**
	 * Tests important settings on the crawler config. */
	public void testMakeConfig() throws Exception {
		CrawlConfig config = Crawler.makeConfig();
		assertTrue("politeness delay must >= 600", config.getPolitenessDelay() >= 600);
		assertEquals("user agent string is to specification", config.getUserAgentString(),
				"UCI Inf141-CS121 crawler 63393716 32393047 22863530 82181685");
	}

	/**
	 * Tests that we only crawl ICS. */
	public void testShouldVisitExampleOne() throws Exception {
		Page ref = new Page(new WebURL());
		WebURL url = new WebURL();

		url.setURL("something else");
		assertFalse("does not visit a non-ics url", crawler.shouldVisit(ref, url));

		url.setURL("http://ics.uci.edu/");
		assertTrue("visits an ics url", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/foo");
		assertTrue("visits ics url with single subdomain", crawler.shouldVisit(ref, url));

		url.setURL("http://foo.bar.baz.ics.uci.edu/foo");
		assertTrue("visits ics url with multipart subdomain", crawler.shouldVisit(ref, url));
	}

	/**
	 * Tests that visited URLs are saved in a list. */
	public void testVisitSavesURL() throws Exception {
		WebURL url = new WebURL();
		url.setURL("abc");
		Page page = new Page(url);

		Crawler.resetVisitedURLs();
		crawler.visit(page);
		assertEquals("abc", Crawler.getVisitedURLs().get(0));
	}
}
