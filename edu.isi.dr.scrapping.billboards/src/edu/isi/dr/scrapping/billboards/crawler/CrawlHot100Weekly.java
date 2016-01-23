package edu.isi.dr.scrapping.billboards.crawler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class CrawlHot100Weekly implements ICrawlUrls {

	private static Document retrieveDocumentFromUrl(String url)
			throws IOException, InterruptedException {
		Thread.currentThread().sleep(1000);
		Document doc = null;
		doc = Jsoup.connect(url).timeout(10 * 1000).get();
		return doc;
	}

	private Map<Date, Set<Article>> crawlHot100Songs() throws IOException,
			ParseException, InterruptedException {
		// public static void main(String args[]) throws IOException,
		// ParseException {

		int ITERATION = 1000;
		int count = 0;
		String crawlUrl = BILLBOARD_CHART_100_START_URL;
		Map<Date, Set<Article>> dataSet = new HashMap<>();
		while (count < ITERATION) {

			Document webPage = retrieveDocumentFromUrl(crawlUrl);
			// process webpage
			Set<Article> articleList = new LinkedHashSet<Article>();
			Elements articles = webPage.select("article[id]");
			for (Element article : articles) {
				// primary
				Article song = new Article();
				Elements primary = article.select("div.row-primary");
				if (primary != null) {
					song.setRank(primary.get(0).select("span.this-week").text());
					song.setTitle(primary.get(0).select("h2").text().trim());
					song.setArtistName(primary.get(0)
							.select("a[data-tracklabel]").text().trim());
				}
				Elements secondary = article.select("div.row-secondary");
				if (secondary != null) {
					Elements statsLastWeek = secondary.get(0).select(
							"div.stats-last-week");
					song.setLastWeekRank(statsLastWeek.get(0)
							.select("span.value").text().trim());

					Elements statsTopSpot = secondary.get(0).select(
							"div.stats-top-spot");
					song.setPeakPositionRank(statsTopSpot.get(0)
							.select("span.value").text().trim());

					Elements statsWeeks = secondary.get(0).select(
							"div.stats-weeks-on-chart");
					song.setWeaksOnChart(statsWeeks.get(0).select("span.value")
							.text().trim());

				}
				articleList.add(song);
			}

		/*	if (BILLBOARD_CHART_100_START_URL.equals(crawlUrl)) {
				dataSet.put(null, articleList);

			} else { */

				String pattern = "yyyy-MM-dd";
				String[] urlSegments = crawlUrl.split("/");
				SimpleDateFormat df = new SimpleDateFormat(pattern);
				Date date = df.parse(urlSegments[urlSegments.length - 1]);
				System.out.println(urlSegments[urlSegments.length - 1]);
				dataSet.put(date, articleList);
			//}
			Elements memberList = webPage.select("a.chart-nav-link.prev");
			if (memberList != null) {
				String link = memberList.get(0).attr("href");
				if (link == null) {
					break;
				} else {
					crawlUrl = BILL_BOARD + link;
				}
			}
			count++;

		}
		return dataSet;
	}

	public static void main(String[] args) throws IOException, ParseException,
			InterruptedException {
		CrawlHot100Weekly run = new CrawlHot100Weekly();
		Map<Date, Set<Article>> crawlHot100Songs = run.crawlHot100Songs();
		Set<Date> keySet = crawlHot100Songs.keySet();
		CreateJsonRead obj = new CreateJsonRead();
		JSONObject root = new JSONObject();
		for (Date date : keySet) {
			JSONArray lists = new JSONArray();

			for (Article art : crawlHot100Songs.get(date)) {
				lists.add(obj.createJsonObect(art));
			}
			root.put(date, lists);

		}
		obj.writeToFile(root, "/home/vijay/Desktop/billboard_dec_31_1994" + ".json");
	}
// till
	/*
	 * 1995-01-21
1995-01-14
1995-01-07
	 */
}