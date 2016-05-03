package edu.isi.dr.scrapping.billboards.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;
import edu.isi.dr.scrapping.billboards.DataStructure.Book;
import edu.isi.dr.scrapping.billboards.analysis.ProcessNewYorkTimes.Percentage;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class ParseJsonFields {

	private String folder;

	public ParseJsonFields(String folder) {
		this.folder = folder;
	}

	public Map<Article, LinkedList<Article>> convertFileToJsonObjectList(float startYear, float endYear)
			throws FileNotFoundException, IOException, ParseException {
		Map<Article, LinkedList<Article>> songLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			Date date; // your date
			Calendar cal = Calendar.getInstance();
			int year = 0;
			for (Object songDate : keySet) {

				if (!songDate.equals("null")) {
					String sDate = (String) (songDate);
					year = Integer.parseInt(sDate.substring(sDate.length() - 5).trim());
				} else {
					year = 2016;
				}
				if (year >= startYear && year <= endYear) {
					JSONArray songs = (JSONArray) root.get(songDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Article createArticle = jread.createArticle(ll.next(), (String) songDate);
						if (Integer.parseInt(createArticle.getRank()) <= 10) {
							if (songLists.containsKey(createArticle)) {
								songLists.get(createArticle).add(createArticle);
							} else {
								LinkedList<Article> arts = new LinkedList<>();
								arts.add(createArticle);
								songLists.put(createArticle, arts);
							}
						}
					}
				}

			}

		}
		return songLists;
	}

	public Map<Article, Integer> convertFileToJsonObjectTop10(float startYear, float endYear, int rank)
			throws FileNotFoundException, IOException, ParseException {
		Map<Article, Integer> songLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			Date date; // your date
			Calendar cal = Calendar.getInstance();
			int year = 0;
			for (Object songDate : keySet) {

				if (!songDate.equals("null")) {
					String sDate = (String) (songDate);
					year = Integer.parseInt(sDate.substring(sDate.length() - 5).trim());
				} else {
					year = 2016;
				}
				if (year >= startYear && year <= endYear) {
					JSONArray songs = (JSONArray) root.get(songDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Article createArticle = jread.createArticle(ll.next(), (String) songDate);
						if (Integer.parseInt(createArticle.getRank()) <= rank) {
							if (songLists.containsKey(createArticle)) {
								songLists.put(createArticle, songLists.get(createArticle) + 1);
							} else {
								songLists.put(createArticle, 1);
							}
						}
					}
				}

			}

		}
		return songLists;
	}

	public Map<Article, Integer> convertFileToJsonObjectTop100SixMonths(float startYear, float endYear, int rank)
			throws FileNotFoundException, IOException, ParseException {

		List<String> firstSixMonths = new ArrayList<>();
		firstSixMonths.add("Jan");
		firstSixMonths.add("Feb");
		firstSixMonths.add("Mar");
		firstSixMonths.add("Apr");
		firstSixMonths.add("May");
		firstSixMonths.add("Jun");

		List<String> lastSixMonths = new ArrayList<>();
		lastSixMonths.add("Jul");
		lastSixMonths.add("Aug");
		lastSixMonths.add("Sep");
		lastSixMonths.add("Oct");
		lastSixMonths.add("Nov");
		lastSixMonths.add("Dec");

		Map<Article, Integer> songLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			Date date; // your date
			Calendar cal = Calendar.getInstance();
			int year = 0;
			for (Object songDate : keySet) {
				if (!songDate.equals("null")) {
					String sDate = (String) (songDate);
					year = Integer.parseInt(sDate.substring(sDate.length() - 5).trim());
				} else {
					year = 2016;
				}
				if (year >= startYear && year <= endYear) {
					String months = ((String) songDate).split(" ")[1];
					if (year == startYear) {
						if (firstSixMonths.contains(months)) {
							continue;
						}
					}
					if (year == endYear) {
						if (lastSixMonths.contains(months)) {
							continue;
						}
					}

					JSONArray songs = (JSONArray) root.get(songDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Article createArticle = jread.createArticle(ll.next(), (String) songDate);
						if (Integer.parseInt(createArticle.getRank()) <= rank) {
							if (songLists.containsKey(createArticle)) {
								songLists.put(createArticle, songLists.get(createArticle) + 1);
							} else {
								songLists.put(createArticle, 1);
							}
						}
					}
				}

			}

		}
		return songLists;
	}

	public Double processGiniCooefficient(Map<Article, Integer> art) {
		Double gini = 0.0;
		List<Double> values = new ArrayList<Double>();
		for (Integer val : art.values()) {
			if (val == 20)
				continue;
			values.add(val / 260.0);
		}

		for (Double xi : values) {
			for (Double xj : values) {
				gini += Math.abs(xi - xj);
			}
		}
		Double denom = 0.0;
		for (Double xi : values) {
			for (Double xj : values) {
				denom += xi;
			}
		}

		return gini / (2 * denom);
	}

	public Map<Article, Integer> convertFileToJsonObject(int startYear, int endYear)
			throws FileNotFoundException, IOException, ParseException {
		Map<Article, Integer> songLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			Date date; // your date
			Calendar cal = Calendar.getInstance();
			int year = 0;
			for (Object songDate : keySet) {

				if (!songDate.equals("null")) {
					String sDate = (String) (songDate);
					year = Integer.parseInt(sDate.substring(sDate.length() - 5).trim());
				} else {
					year = 2016;
				}
				if (year >= startYear && year <= endYear) {
					JSONArray songs = (JSONArray) root.get(songDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Article createArticle = jread.createArticle(ll.next(), (String) songDate);
						if (songLists.containsKey(createArticle)) {
							songLists.put(createArticle, songLists.get(createArticle) + 1);
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

	public void processWeeksOnChart(String fileLocation, Map<Article, Integer> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();

		/*
		 * for (Article art : filteredList.keySet()) {
		 * contents.append(art.getTitle() + ":" + art.getArtistName() + ":" +
		 * filteredList.get(art) +":"+art.getWeek()+ "\n"); }
		 */

		for (Article art : filteredList.keySet()) {
			contents.append(" " + filteredList.get(art) + ",");
		}

		String finalContent = contents.toString();
		finalContent = finalContent.substring(0, finalContent.length() - 1);
		writer.print(finalContent);
		writer.close();

	}

	public void processWeeksOnChartOnly20(String fileLocation, Map<Article, LinkedList<Article>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();

		for (Article art : filteredList.keySet()) {

			if (filteredList.get(art).size() > 20) {
				Collections.sort(filteredList.get(art), new Comparator<Article>() {

					@Override
					public int compare(Article o1, Article o2) {
						return o1.getWeekNo().compareTo(o2.getWeekNo());
					}
				});
				// contents.append(art.getTitle() + ":" + art.getArtistName() +
				// ":" + filteredList.get(art).size() +" :
				// "+filteredList.get(art).getFirst().getRank()+" :
				// "+filteredList.get(art).getLast().getRank());
				Article high, low;
				int highest = Integer.MAX_VALUE, lowest = Integer.MIN_VALUE;
				for (Article artis : filteredList.get(art)) {
					try {
						int temp = highest;
						highest = Math.min(highest, Integer.valueOf(artis.getRank()));
						if (temp != highest) {
							high = artis;
						}
						int low1 = lowest;
						lowest = Math.max(lowest, Integer.valueOf(artis.getRank()));
						if (low1 != lowest) {
							low = artis;
						}
					} catch (NumberFormatException e) {

					}
				}

				contents.append(lowest + " ," + highest + "\n");
			}
		}

		String finalContent = contents.toString();
		// finalContent = finalContent.substring(0, finalContent.length() - 1);
		writer.print(finalContent);
		writer.close();

	}

	public void processWeeksOnChartOnly20All(String fileLocation, Map<Article, LinkedList<Article>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();

		for (Article art : filteredList.keySet()) {

			if (filteredList.get(art).size() == 20) {
				Collections.sort(filteredList.get(art), new Comparator<Article>() {

					@Override
					public int compare(Article o1, Article o2) {
						return o1.getWeekNo().compareTo(o2.getWeekNo());
					}
				});

				contents.append("[");
				int count = 1;
				for (Article title : filteredList.get(art)) {
					contents.append("(" + count + "," + title.getRank() + ")");
					if (count < filteredList.get(art).size()) {
						contents.append(",");
					}
					count++;
				}
				contents.append("]\n");
			}

		}

		String finalContent = contents.toString();
		// finalContent = finalContent.substring(0, finalContent.length() - 1);
		writer.print(finalContent);
		writer.close();

	}

	public void histogramFor5Years() throws FileNotFoundException, IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		for (int start = 1959; start <= 2015; start += 5) {
			Map<Article, Integer> objLists = obj.convertFileToJsonObjectTop10(start, start + 4, 10);
			obj.processWeeksOnChart(
					"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/result/histogram5years"
							+ start + ".txt",
					objLists);
		}

	}

	public void findUniqueSongsList(int str) throws FileNotFoundException, IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		Map<Integer, Integer> uniqueSongsCount = new TreeMap<Integer, Integer>();
		for (int start = 1959; start <= 2015; start += 1) {
			Map<Article, Integer> objLists = obj.convertFileToJsonObjectTop10(start, start, str);
			int song = 0;
			for (int val : objLists.values()) {
				if (val == 20) {
					continue;
				}
				song++;
			}
			uniqueSongsCount.put(start, song);

		}
		PrintWriter writer = new PrintWriter("without20/ana_art_top" + String.valueOf(str) + ".txt", "UTF-8");

		for (Integer art : uniqueSongsCount.keySet()) {
			writer.println(art + "," + uniqueSongsCount.get(art));
		}

		writer.close();

	}

	public void find20WeeksOnchartAndRank() throws FileNotFoundException, IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		Map<Article, LinkedList<Article>> objLists = obj.convertFileToJsonObjectList(1959, 2015);
		obj.processWeeksOnChartOnly20(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/result/songsrank/"
						+ "scatterplot_above_20" + ".txt",
				objLists);

	}

	public void plotGiniCoefficient() throws FileNotFoundException, IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		Map<Float, Double> graph = new TreeMap<>();
		for (float start = 1959; start + 4 <= 2015; start += 1) {
			Map<Article, Integer> objLists = obj.convertFileToJsonObjectTop10(start, start + 4, 1);
			graph.put(start, obj.processGiniCooefficient(objLists));
			// Map<Article, Integer> objLists1 =
			// obj.convertFileToJsonObjectTop100SixMonths(start, start + 1,10);
			// graph.put((float) (start + 0.5),
			// obj.processGiniCooefficient(objLists1));
		}
		obj.processGeneric(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/without20/ginisongrolling_song_top1.txt",
				graph);
	}

	public void plotGiniCoefficientAuthor() throws FileNotFoundException, IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		Map<Float, Double> graph = new TreeMap<>();
		for (float start = 1959; start + 4 <= 2015; start += 1) {
			Map<Article, Integer> objLists = obj.convertFileToJsonObjectTop10(start, start + 4, 1);
			graph.put(start, obj.processGiniCooefficient(objLists));
			// Map<Article, Integer> objLists1 =
			// obj.convertFileToJsonObjectTop100SixMonths(start, start + 4,100);
			// graph.put((float) (start + 0.5),
			// obj.processGiniCooefficient(objLists1));
		}
		obj.processGeneric(
				"/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/without20/ginisongrolling_author_5years_top1.txt",
				graph);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		ParseJsonFields prog = new ParseJsonFields("input");
		prog.plotMovingAverage();
	//	prog.plotGiniCoefficientAuthor();
		// prog.find20WeeksOnchartAndRank();
		// prog.find20WeeksOnchartAndRank();
		/*
		 * prog.findUniqueSongsList(100); prog.findUniqueSongsList(10);
		 * prog.findUniqueSongsList(40); prog.findUniqueSongsList(1);
		 */
		/*
		 * obj.processWeeksOnChart(
		 * "/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_10_1958_1965.txt"
		 * , objLists); objLists = obj.convertFileToJsonObjectTop10(1964, 1968);
		 * obj.processWeeksOnChart(
		 * "/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_10_1966_1975.txt"
		 * , objLists); objLists = obj.convertFileToJsonObjectTop10(1969, 1973);
		 * obj.processWeeksOnChart(
		 * "/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_10_1976_1985.txt"
		 * , objLists); objLists = obj.convertFileToJsonObjectTop10(1978, 1982);
		 * obj.processWeeksOnChart(
		 * "/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_10_1986_1995.txt"
		 * , objLists); objLists = obj.convertFileToJsonObjectTop10(1983, 1987);
		 * obj.processWeeksOnChart(
		 * "/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_10_1996_2005.txt"
		 * , objLists); objLists = obj.convertFileToJsonObjectTop10(1988, 1992);
		 * obj.processWeeksOnChart(
		 * "/Users/vijayan/Documents/Directed Research/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/analysis/analysis_10_2006_2016.txt"
		 * , objLists);
		 */
	}

	public Percentage processWeeksOnChartPercentage(Map<Article, LinkedList<Article>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		List<Integer> contents = new ArrayList<>();
		List<String> checkList = new ArrayList<String>();
		Double totalRevenue = 0.0;
		for (Article art : filteredList.keySet()) {
			contents.add(filteredList.get(art).size());
			totalRevenue += filteredList.get(art).size();
		}

		// checkList.add(art.getTitle() + " " + );

		/*
		 * Collections.sort(checkList); for (String line : checkList) {
		 * writer.println(line); }
		 */
		// writer.close();
		Collections.sort(contents);
		int lowPercentage = contents.size() * 20 / 100;
		Float res = 0.0f;
		for (int i = 0; i < lowPercentage; i++) {
			res += contents.get(i);
		}
		Double lowVal = (double) res / lowPercentage;

		res = 0.0f;
		List<Integer> results = new ArrayList<>();
		for (int j = contents.size() - 1; j > contents.size() - lowPercentage; j--) {
			res += contents.get(j);
		}

		Double highVal = (double) res / lowPercentage;

		return new Percentage(lowVal, highVal);

	}

	// Gini co-efficient
	public void plotMovingAverage() throws FileNotFoundException, IOException, ParseException {
		Map<Float, Percentage> graph = new TreeMap<>();
		for (float start = 1959; start+4 <= 2015; start += 1) {
			Map<Article, LinkedList<Article>> convertFileToJsonObjectList = this.convertFileToJsonObjectList(start,
					start + 4);
			graph.put(start, this.processWeeksOnChartPercentage(convertFileToJsonObjectList));
		}
		this.processGeneric("ny_times_statistics/top10/articles", graph, true);
	}
	
	public void processGeneric(String fileLocation, Map<Float, Percentage> objLists, boolean a)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation + "_low", "UTF-8");

		for (Float art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art).low);
		}

		writer.close();

		writer = new PrintWriter(fileLocation + "_high", "UTF-8");

		for (Float art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art).high);
		}
		writer.close();
	}


	public Map<Integer, List<Article>> findUniqueSongs(int rank)
			throws FileNotFoundException, IOException, ParseException {
		Map<Integer, List<Article>> songLists = new TreeMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			int year = 0;
			for (Object songDate : keySet) {

				if (!songDate.equals("null")) {
					String sDate = (String) (songDate);
					year = Integer.parseInt(sDate.substring(sDate.length() - 5).trim());
				} else {
					year = 2016;
				}
				JSONArray songs = (JSONArray) root.get(songDate);
				Iterator<JSONObject> ll = songs.iterator();
				while (ll.hasNext()) {
					Article createArticle = jread.createArticle(ll.next(), (String) songDate);
					if (Integer.parseInt(createArticle.getRank()) <= rank) {
						if (songLists.containsKey(year)) {
							if (!songLists.get(year).contains(createArticle))
								songLists.get(year).add(createArticle);
						} else {
							List<Article> songArray = new ArrayList<Article>();
							songArray.add(createArticle);
							songLists.put(year, songArray);
						}
					}
				}

			}

		}

		return songLists;
	}

	public void processUniqueSong(String fileLocation, Map<Integer, List<Article>> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");

		for (Integer art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art).size());
		}

		writer.close();
	}

	public void processEntropy(String fileLocation, Map<Integer, Double> entropy)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");

		for (Integer art : entropy.keySet()) {
			writer.println(art + "," + entropy.get(art));
		}

		writer.close();
	}

	public void processYear(String fileLocation, Map<Integer, List<Article>> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");

		for (Integer art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art).size());
		}

		writer.close();
	}

	public void processGeneric(String fileLocation, Map<Float, Double> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");

		for (Float art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art));
		}

		writer.close();
	}

	public void processUniqueSongLists(String fileLocation, Map<Integer, List<Article>> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		for (Integer art : objLists.keySet()) {
			File newFile = new File(fileLocation + art + ".txt");
			PrintWriter writer = new PrintWriter(newFile, "UTF-8");
			List<String> contents = new ArrayList<String>();
			for (Article article : objLists.get(art)) {
				contents.add(new String(article.getTitle() + " : " + article.getArtistName()));
			}
			Collections.sort(contents);
			for (String line : contents) {
				writer.println(line);
			}
			writer.close();
		}
	}

	public Map<Integer, Double> analyseArtistByYear(Map<Integer, List<Article>> objLists) {
		Map<Integer, Double> result = new HashMap<>();
		for (Integer year : objLists.keySet()) {
			Map<String, List<Article>> groupByArtist = new HashMap<>();
			for (Article song : objLists.get(year)) {
				if (groupByArtist.containsKey(song.getArtistName())) {
					groupByArtist.get(song.getArtistName()).add(song);
				} else {
					List<Article> songList = new ArrayList<>();
					songList.add(song);
					groupByArtist.put(song.getArtistName(), songList);
				}
			}

		}

		return result;

	}

	private double calculateLogBase2(double x) {
		return Math.log(x) / Math.log(2);

	}

	private double calculateEntropy(Map<String, List<Article>> groupByArtist) {

		double totalCount = 0;
		double entropy = 0;
		for (String artistName : groupByArtist.keySet()) {
			totalCount += groupByArtist.get(artistName).size();
		}
		for (String artistName : groupByArtist.keySet()) {
			double temp = groupByArtist.get(artistName).size() / totalCount;
			entropy += temp * calculateLogBase2(temp);
			;
		}

		return entropy * -1;

	}

	class Percentage {
		Double low;
		Double high;

		public Percentage(Double low, Double high) {
			this.low = low;
			this.high = high;
		}
	}
}
