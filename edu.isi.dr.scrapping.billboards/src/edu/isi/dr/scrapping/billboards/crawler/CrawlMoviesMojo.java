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
import edu.isi.dr.scrapping.billboards.DataStructure.Movie;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class CrawlMoviesMojo implements ICrawlUrls {
	private static Document retrieveDocumentFromUrl(String url)
			throws IOException, InterruptedException {
		Thread.currentThread().sleep(1000);
		Document doc = null;
		doc = Jsoup.connect(url).timeout(10 * 1000).get();
		return doc;
	}

	private Map<String, List<Movie>> crawlMovies() throws IOException,
			InterruptedException {
		// public static void main(String args[]) throws IOException,
		// ParseException {

		int ITERATION = 1;
		long count = 1;
		String crawlUrl = MOJO_BEG;
		Map<String, List<Movie>> dataSet = new HashMap<>();
		while (true) {

			Document webPage = retrieveDocumentFromUrl(crawlUrl);
			// process webpage
			List<Movie> movieList = new LinkedList<Movie>();
			Elements articles = webPage.select("div[id=body").select("center")
					.select("tbody").select("table").select("tbody")
					.select("tr");
			String key = webPage.select("tbody").select("h2").text();
			for (int i = 1; i < articles.size() - 1; i++) {
				// primary
				Article song = new Article();
				Movie movieItem = new Movie();
				movieItem.setDate(webPage.select("tbody").select("h2").text());
				Elements row = articles.get(i).select("td");
				int index = -1;
				movieItem
						.setThisWeek((row.get(++index).text()));
				movieItem
						.setLastWeek(row.get(++index).text());
				movieItem.setTitle(row.get(++index).text());
				movieItem.setStudio(row.get(++index).text());
				movieItem.setWeeklyGross(row.get(++index).text());
				movieItem.setPercentChange(row.get(++index).text());
				movieItem.setTheatreCount(row.get(++index).text());
				movieItem.setChange(row.get(++index).text());
				movieItem.setAverage(row.get(++index).text());
				movieItem.setTotalGross(row.get(++index).text());
				movieItem.setBudgetInMillions(row.get(++index).text());
				movieItem.setNoWeeks((row.get(++index).text()));
				movieItem.setWeekNo(count);
				Map<String, String> uriParameters = getUriParameters(crawlUrl);
				movieItem.setYear(Long.valueOf(uriParameters.get("yr")));
				movieItem.setWeekYearNo(Long.valueOf(uriParameters
						.get("wk")));
				movieList.add(movieItem);
			}

			/*
			 * if (BILLBOARD_CHART_100_START_URL.equals(crawlUrl)) {
			 * dataSet.put(null, articleList);
			 * 
			 * } else {
			 */

			dataSet.put(key, movieList);
			// } */
			Elements memberList = webPage
					.select("a[href^=/weekly/chart/?view=]");
			boolean next = false;
			if (memberList != null) {
				for (Element element : memberList) {
					if (element.text().startsWith("Next Week")) {
						crawlUrl = MOJO + element.select("a").attr("href");
						System.out.println(crawlUrl);
						next = true;
						break;
					}
				}

				if (next == false) {
					break;
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
		CrawlMoviesMojo run = new CrawlMoviesMojo();
		Map<String, List<Movie>> crawlMovies = run.crawlMovies();
		Set<String> keySet = crawlMovies.keySet();
		CreateJsonRead obj = new CreateJsonRead();
		JSONObject root = new JSONObject();
		for (String date : keySet) {
			JSONArray lists = new JSONArray();

			for (Movie art : crawlMovies.get(date)) {
				lists.add(obj.createJsonObect(art));
			}
			root.put(date, lists);

		}
		obj.writeToFile(
				root,
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/data/moviemojo_dataset"
						+ ".json");
	}

	private Map<String, String> getUriParameters(String uri) {
		System.out.println(uri);
		Map<String, String> parameter = new HashMap<String, String>();
		String[] split = uri.split("&");
		for (int i = 1; i < split.length; i = i + 1) {
			String[] key =split[i].split("=");
			parameter.put(key[0], key[1]);
		}
		return parameter;
	}
	// till
	/*
	 * 1995-01-21 1995-01-14 1995-01-07
	 * 
	 * 1975-11-08
	 */
}
