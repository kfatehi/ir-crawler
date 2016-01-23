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

		url.setURL("http://something.else");
		assertFalse("does not visit a non-ics url", crawler.shouldVisit(ref, url));

		url.setURL("http://ics.uci.edu/");
		assertTrue("visits an ics url", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/foo");
		assertTrue("visits ics url with single subdomain", crawler.shouldVisit(ref, url));

		url.setURL("http://foo.bar.baz.ics.uci.edu/foo");
		assertTrue("visits ics url with multipart subdomain", crawler.shouldVisit(ref, url));
	}

	/**
	 * Does not crawl certain urls */
	public void testShouldVisitExampleTwo() throws Exception {
		Page ref = new Page(new WebURL());
		WebURL url = new WebURL();

		url.setURL("http://www.ics.uci.edu/calendar.php?somedate=123123");
		assertFalse("won't visit query string urls", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/bin/img/logos/socialmedia/twitter.png");
		assertFalse("won't visit .png", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/bin/img/logos/socialmedia/twitter.jpg");
		assertFalse("won't visit .jpg", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/bin/img/logos/socialmedia/twitter.gif");
		assertFalse("won't visit .gif", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/somefile.css");
		assertFalse("won't visit .css", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/somefile.pdf");
		assertFalse("won't visit .pdf", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/somefile.ps");
		assertFalse("won't visit .ps", crawler.shouldVisit(ref, url));

		url.setURL("http://www.ics.uci.edu/somefile.js");
		assertFalse("won't visit .js", crawler.shouldVisit(ref, url));
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
