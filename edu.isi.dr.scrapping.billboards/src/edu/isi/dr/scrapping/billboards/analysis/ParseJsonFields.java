package edu.isi.dr.scrapping.billboards.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class ParseJsonFields {

	private String folder;

	public ParseJsonFields(String folder) {
		this.folder = folder;
	}

	public Map<Article, Article> convertFileToJsonObject(int startYear,
			int endYear) throws FileNotFoundException, IOException,
			ParseException {
		Map<Article, Article> songLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser
					.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			Date date; // your date
			Calendar cal = Calendar.getInstance();
			int year = 0;
			for (Object songDate : keySet) {

				if (!songDate.equals("null")) {
					String sDate = (String) (songDate);
					year = Integer.parseInt(sDate.substring(sDate.length() - 5)
							.trim());
				} else {
					year = 2016;
				}
				if (year >= startYear && year <= endYear) {
					JSONArray songs = (JSONArray) root.get(songDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Article createArticle = jread.createArticle(ll.next());
						if (songLists.containsKey(createArticle)) {
							Article old = songLists.get(createArticle);
							int oldWeek = getIntegerValue(old);
							int newWeek = getIntegerValue(createArticle);
							if (newWeek > oldWeek) {
								songLists.put(createArticle, createArticle);
							}
						} else {
							songLists.put(createArticle, createArticle);
						}

					}
				}

			}

		}

		return songLists;
	}

	private int getIntegerValue(Article old) {
		int res = 0;
		try {
			res = Integer.parseInt(old.getWeaksOnChart());
		} catch (Exception e) {

		}

		return res;
	}

	public void processWeeksOnChart(String fileLocation,
			Map<Article, Article> filteredList) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();
		/*
		 * for (Article art : filteredList.keySet()) {
		 * contents.append(art.getTitle() + ":" + art.getArtistName() + ":" +
		 * filteredList.get(art).getWeaksOnChart() + "\n"); }
		 */

		for (Article art : filteredList.keySet()) {
			contents.append(" " + filteredList.get(art).getWeaksOnChart() + ",");
		}

		String finalContent = contents.toString();
		finalContent = finalContent.substring(0, finalContent.length() - 1);
		writer.print(finalContent);
		writer.close();

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/data");
		Map<Article, Article> objLists = obj
				.convertFileToJsonObject(2010, 2016);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/result/result_2010.txt",
				objLists);
	}

}
