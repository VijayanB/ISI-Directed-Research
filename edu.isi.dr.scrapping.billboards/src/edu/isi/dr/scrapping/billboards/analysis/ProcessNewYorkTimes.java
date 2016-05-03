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
import edu.isi.dr.scrapping.billboards.DataStructure.Movie;
import edu.isi.dr.scrapping.billboards.analysis.ProcessMovies.Percentage;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class ProcessNewYorkTimes {
	class Percentage {
		Double low;
		Double high;

		public Percentage(Double low, Double high) {
			this.low = low;
			this.high = high;
		}
	}

	private String folder;

	public ProcessNewYorkTimes(String folder) {
		this.folder = folder;
	}

	public Map<Book, LinkedList<Book>> convertFileToJsonObjectList(float startYear, float endYear)
			throws FileNotFoundException, IOException, ParseException {

		Map<Book, LinkedList<Book>> bookLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();

			for (Object bookDate : keySet) {

				int year = Integer.parseInt(((String) bookDate).split(":")[1]);
				if (year >= startYear && year <= endYear) {
					JSONArray books = (JSONArray) root.get(bookDate);
					Iterator<JSONObject> ll = books.iterator();
					while (ll.hasNext()) {
						Book createBook = jread.createBook(ll.next());
						if (Integer.parseInt(createBook.getRank()) > 10)
							continue;
						if (bookLists.containsKey(createBook)) {
							bookLists.get(createBook).add(createBook);
						} else {
							LinkedList<Book> arts = new LinkedList<>();
							arts.add(createBook);
							bookLists.put(createBook, arts);
						}
					}
				}

			}

		}
		return bookLists;
	}

	public Map<Book, Integer> convertFileToJsonObjectCount(float startYear, float endYear)
			throws FileNotFoundException, IOException, ParseException {

		Map<Book, Integer> bookLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();

			for (Object bookDate : keySet) {

				int year = Integer.parseInt(((String) bookDate).split(":")[1]);
				if (year >= startYear && year <= endYear) {
					JSONArray books = (JSONArray) root.get(bookDate);
					Iterator<JSONObject> ll = books.iterator();
					while (ll.hasNext()) {
						Book createArticle = jread.createBook(ll.next());
						if (Integer.parseInt(createArticle.getRank()) > 10)
							continue;
						if (bookLists.containsKey(createArticle)) {
							bookLists.put(createArticle, bookLists.get(createArticle) + 1);
						} else {
							bookLists.put(createArticle, 1);
						}
					}
				}

			}

		}
		return bookLists;
	}

	public void processWeeksOnChart(String fileLocation, Map<Book, LinkedList<Book>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();
		List<String> checkList = new ArrayList<String>();
		for (Book art : filteredList.keySet()) {
			// contents.append(filteredList.get(art).size()
			// + ",");
			checkList.add(art.getTitle() + " " + filteredList.get(art).size());
		}
		Collections.sort(checkList);
		for (String line : checkList) {
			writer.println(line);
		}
		writer.close();
		/*
		 * String finalContent = contents.toString(); finalContent =
		 * finalContent.substring(0, finalContent.length() - 1);
		 * writer.print(finalContent); writer.close();
		 */

	}

	public Map<Integer, List<Book>> findUniqueBooks() throws FileNotFoundException, IOException, ParseException {
		Map<Integer, List<Book>> bookLists = new TreeMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			int year = 0;
			for (Object bookDate : keySet) {
				year = Integer.parseInt(((String) bookDate).split(":")[1]);
				JSONArray songs = (JSONArray) root.get(bookDate);
				Iterator<JSONObject> ll = songs.iterator();
				while (ll.hasNext()) {
					Book createBook = jread.createBook(ll.next());
					if (Integer.parseInt(createBook.getRank()) > 10)
						continue;
					if (bookLists.containsKey(year)) {
						if (!bookLists.get(year).contains(createBook))
							bookLists.get(year).add(createBook);
					} else {
						List<Book> songArray = new ArrayList<Book>();
						songArray.add(createBook);
						bookLists.put(year, songArray);
					}
				}

			}

		}

		return bookLists;
	}

	public void persistUniqueBooksByYear(String fileLocation, Map<Integer, List<Book>> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		for (Integer art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art).size());
		}
		writer.close();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		ProcessNewYorkTimes nytimes = new ProcessNewYorkTimes("books");
		// nytimes.histogramsWeeksOnChart();
		// nytimes.uniqueBooksByYearChart();
		// nytimes.plotGiniCoefficient();
		nytimes.plotMovingAverage();
	}

	// Gini co-efficient
	public void plotMovingAverage() throws FileNotFoundException, IOException, ParseException {
		Map<Float, Percentage> graph = new TreeMap<>();
		for (float start = 1950; start+4 <= 2015; start += 1) {
			Map<Book, LinkedList<Book>> convertFileToJsonObjectList = this.convertFileToJsonObjectList(start,
					start + 4);
			graph.put(start, this.processWeeksOnChartPercentage(convertFileToJsonObjectList));
		}
		this.processGeneric("ny_times_statistics/top10/book", graph, true);
	}

	private void uniqueBooksByYearChart() throws FileNotFoundException, IOException, ParseException {
		Map<Book, LinkedList<Book>> convertFileToJsonObjectList = this.convertFileToJsonObjectList(1950, 2015);
		Map<Integer, List<Book>> findUniqueBooks = this.findUniqueBooks();
		persistUniqueBooksByYear("ny_times_statistics/analysis/hist/uniqueBooks10.txt", findUniqueBooks);

	}

	private void histogramsWeeksOnChart()
			throws FileNotFoundException, IOException, ParseException, UnsupportedEncodingException {
		for (int start = 1950; start <= 2011; start += 1) {
			Map<Book, LinkedList<Book>> convertFileToJsonObjectList = this.convertFileToJsonObjectList(start,
					start + 1);
			this.processWeeksOnChart("ny_times_statistics/analysis/hist/uniq_Book_top10" + start,
					convertFileToJsonObjectList);
		}
	}

	// Gini co-efficient
	public void plotGiniCoefficient() throws FileNotFoundException, IOException, ParseException {
		Map<Float, Double> graph = new TreeMap<>();
		for (float start = 1950; start + 4 <= 2015; start += 1) {
			Map<Book, Integer> objLists = this.convertFileToJsonObjectCount(start, start + 4);
			graph.put(start, this.processGiniCooefficient(objLists));
			// Map<Book, Integer> objLists1 =
			// this.convertFileToJsonObjectTop100SixMonths(start, start + 1);
			// graph.put((float) (start + 0.5),
			// this.processGiniCooefficient(objLists1));
		}
		// this.processGeneric("ny_times_statistics/gini/gini_author_new_10.txt",
		// graph);
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

	public void processGeneric(String fileLocation, Map<Float, Double> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");

		for (Float art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art));
		}

		writer.close();
	}

	public Map<Book, Integer> convertFileToJsonObjectTop100SixMonths(float startYear, float endYear)
			throws FileNotFoundException, IOException, ParseException {

		List<String> firstSixMonths = new ArrayList<>();
		firstSixMonths.add("January");
		firstSixMonths.add("Feburary");
		firstSixMonths.add("March");
		firstSixMonths.add("April");
		firstSixMonths.add("May");
		firstSixMonths.add("June");

		List<String> lastSixMonths = new ArrayList<>();
		lastSixMonths.add("July");
		lastSixMonths.add("August");
		lastSixMonths.add("September");
		lastSixMonths.add("October");
		lastSixMonths.add("November");
		lastSixMonths.add("December");

		Map<Book, Integer> bookLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			for (Object bookDate : keySet) {
				int year = Integer.parseInt(((String) bookDate).split(":")[1]);
				if (year >= startYear && year <= endYear) {
					String months = ((String) bookDate).split(" ")[0];
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

					JSONArray songs = (JSONArray) root.get(bookDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Book createArticle = jread.createBook(ll.next());
						if (bookLists.containsKey(createArticle)) {
							bookLists.put(createArticle, bookLists.get(createArticle) + 1);
						} else {
							bookLists.put(createArticle, 1);
						}
					}
				}

			}

		}
		return bookLists;
	}

	public Double processGiniCooefficient(Map<Book, Integer> art) {
		Double gini = 0.0;
		List<Double> values = new ArrayList<Double>();
		for (Integer val : art.values()) {
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

	public Percentage processWeeksOnChartPercentage(Map<Book, LinkedList<Book>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		List<Integer> contents = new ArrayList<>();
		List<String> checkList = new ArrayList<String>();
		Double totalRevenue = 0.0;
		for (Book art : filteredList.keySet()) {
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

}
