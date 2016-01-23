package ir.assignments.three;

import ir.assignments.three.db.Database;
import ir.assignments.three.db.PageRepo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;

import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class Crawler extends WebCrawler {

	static ArrayList<String> visitedURLs;

	private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

	/**
	 * Generates crawler configuration.
	 */
	public static CrawlConfig makeConfig() {
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder("_tmp/crawl/root");
		config.setPolitenessDelay(600); // No less than 600ms!
		config.setUserAgentString(
				"UCI Inf141-CS121 crawler 63393716 32393047 22863530 82181685");
		config.setResumableCrawling(false);
		return config;
	}

	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 *
	 * We don't use the database here since it's not necessary and might have an
	 * unexpected problem, thus impacting results of the programmatic grader.
	 */
	public static Collection<String> crawl(String seedURL) {
		resetVisitedURLs();

		int numberOfCrawlers = 4;

		CrawlConfig config = makeConfig();

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

		try {
			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
			/*
			 * For each crawl, you need to add some seed urls. These are the first
			 * URLs that are fetched and then the crawler starts following links
			 * which are found in these pages
			 */
			controller.addSeed(seedURL);

			/*
			 * Start the crawl. This is a blocking operation, meaning that your code
			 * will reach the line after this only when crawling is finished.
			 */
			controller.start(Crawler.class, numberOfCrawlers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("DONE!");

		System.out.println(getVisitedURLs());

		return getVisitedURLs();
	}

	/**
	 * Resets the visitedURLs to an empty list
	 */
	public static void resetVisitedURLs() {
		visitedURLs = new ArrayList<>();
	}

	/**
	 * Returns visitedURLs
	 */
	public static ArrayList<String> getVisitedURLs() {
		return visitedURLs;
	}

	/**
	 * Adds a URL to the visitedURLs
	 */
	public static boolean addVisitedURL(String url) {
		return visitedURLs.add(url);
	}

	/**
	 * Visited a page.
	 * This function is called when a page is fetched and ready
	 * to be processed by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();

		addVisitedURL(url);

		if (page.getParseData() instanceof HtmlParseData) {
			if (Database.configured()) {
			   	if (PageRepo.existsWithURL(url)) {
					logger.info("URL exists in db: "+url);
				} else {
					HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
					String text = htmlParseData.getText();
					String html = htmlParseData.getHtml();
					if (PageRepo.insert(url, html, text)) {
						logger.info("URL saved to db: "+url);
					}
				}
			}
		}
	}

	/**
	 * Decide if the page should be visited. The first parameter is the page
	 * in which we have discovered this new url and the second parameter is
	 * the new url. You should implement this function to specify whether
	 * the given url should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL weburl) {
		String url = weburl.getURL().toLowerCase();

		try {
			URI uri = new URI(url);

			String path = uri.getPath();
			String host = uri.getHost();
			String queryString = uri.getQuery();

			// limit to ics.uci.edu
			if (! host.matches(".*ics\\.uci\\.edu$")) return false;

			// this should prevent the calendar trap
			// and similar ones based on query string
			if (queryString != null) return false;

			// prevent images, pdfs, etc
			if (path.matches(".+\\.png$")) return false;
			if (path.matches(".+\\.jpg$")) return false;
			if (path.matches(".+\\.gif$")) return false;
			if (path.matches(".+\\.css$")) return false;
			if (path.matches(".+\\.js$")) return false;
			if (path.matches(".+\\.pdf$")) return false;
			if (path.matches(".+\\.ps$")) return false;
			if (path.matches(".+\\.ppt$")) return false;
			if (path.matches(".+\\.pptx$")) return false;
			if (path.matches(".+\\.doc$")) return false;
			if (path.matches(".+\\.docx$")) return false;
			if (path.matches(".+\\.ico$")) return false;

		} catch (URISyntaxException e) {
			// ignore and dont try to visit malformed URL
			return false;
		}

		logger.info("Will visit: "+url);

		// Otherwise visit the page
		return true;
	}

	/**
	 * Main method to run the crawler.
	 *
	 * Connects to the production database, and then crawls ICS.
	 */
	public static void main(String[] args) {
		// If we don't run with a database, does it still hang forever or will it complete eventually?
		//if (Database.configure("prod")) {
		//	System.out.println("Connected to database");
		//}
		crawl("http://www.ics.uci.edu/");
	}
}
