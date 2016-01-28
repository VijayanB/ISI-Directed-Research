package edu.isi.dr.scrapping.billboards.crawler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

	private Map<Date, List<Article>> crawlHot100Songs() throws IOException,
			InterruptedException {
		// public static void main(String args[]) throws IOException,
		// ParseException {

		int ITERATION = 1;
		long count = 2001;
		String crawlUrl = BILLBOARD_CHART_100_START_URL;
		Map<Date, List<Article>> dataSet = new HashMap<>();
		while (count < 3001) {

			Document webPage = retrieveDocumentFromUrl(crawlUrl);
			// process webpage
			List<Article> articleList = new LinkedList<Article>();
			Elements articles = webPage.select("article[id]");
			for (Element article : articles) {
				// primary
				Article song = new Article();
				song.setWeekNo(count);
				Elements primary = article.select("div.row-primary");
				if (primary != null) {
					song.setRank(primary.get(0).select("span.this-week").text());
					song.setTitle(primary.get(0).select("h2").text().trim());
					Elements artistLink = primary.get(0).select("div.row-title").select(
							"a[data-tracklabel]");
					if (artistLink == null || artistLink.text().equals("")) {
						song.setArtistName(primary.get(0).select("h3").text()
								.trim());
					} else {
						song.setArtistName(artistLink.text().trim());
					}
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

			/*
			 * if (BILLBOARD_CHART_100_START_URL.equals(crawlUrl)) {
			 * dataSet.put(null, articleList);
			 * 
			 * } else {
			 */

			String pattern = "yyyy-MM-dd";
			String[] urlSegments = crawlUrl.split("/");
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			Date date;
			try {
				date = df.parse(urlSegments[urlSegments.length - 1]);
			} catch (ParseException e) {
				break;
			}
			System.out.println(urlSegments[urlSegments.length - 1]);
			dataSet.put(date, articleList);
			// }
			Elements memberList = webPage.select("a.chart-nav-link.next");
			if (memberList != null) {
				String link = memberList.get(0).attr("href");
				if (link == null) {
					break;
				} else {
					crawlUrl = BILL_BOARD + link;
				}
			} else {
				break;
			}
			count++;

		}
		return dataSet;
	}

	public static void main(String[] args) throws IOException, ParseException,
			InterruptedException {
		CrawlHot100Weekly run = new CrawlHot100Weekly();
		Map<Date, List<Article>> crawlHot100Songs = run.crawlHot100Songs();
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
		obj.writeToFile(root, "/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/data/billboard_dataset_2"
				+ ".json");
	}
	// till
	/*
	 * 1995-01-21 1995-01-14 1995-01-07
	 * 
	 * 1975-11-08
	 */
}
