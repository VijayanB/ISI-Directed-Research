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

	public Map<Article, Integer> convertFileToJsonObject(int startYear,
			int endYear) throws FileNotFoundException, IOException,
			ParseException {
		Map<Article, Integer> songLists = new HashMap<>();
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
						Article createArticle = jread.createArticle(ll.next(),(String)songDate);
						if (songLists.containsKey(createArticle)) {
							songLists.put(createArticle, songLists.get(createArticle)+1);
						} else {
							songLists.put(createArticle, 1);
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
			Map<Article, Integer> filteredList) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();
		
		/* for (Article art : filteredList.keySet()) {
		  contents.append(art.getTitle() + ":" + art.getArtistName() + ":" +
		  filteredList.get(art) +":"+art.getWeek()+ "\n"); } */
		 

		for (Article art : filteredList.keySet()) {
			contents.append(" " + filteredList.get(art) + ",");
		}

		String finalContent = contents.toString();
		finalContent = finalContent.substring(0, finalContent.length() - 1);
		writer.print(finalContent);
		writer.close();

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		Map<Article, Integer> objLists = obj
				.convertFileToJsonObject(1958,1965);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_1958_1965.txt",
				objLists);
		 objLists = obj
				.convertFileToJsonObject(1966,1975);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_1966_1975.txt",
				objLists);
		 objLists = obj
				.convertFileToJsonObject(1976,1985);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_1976_1985.txt",
				objLists);
		 objLists = obj
				.convertFileToJsonObject(1986,1995);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_1986_1995.txt",
				objLists);
		objLists = obj
				.convertFileToJsonObject(1996,2005);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_1996_2005.txt",
				objLists);
		objLists = obj
				.convertFileToJsonObject(2006,2016);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_2006_2016.txt",
				objLists);
	}

}
