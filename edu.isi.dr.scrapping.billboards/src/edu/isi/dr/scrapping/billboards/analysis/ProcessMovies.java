package edu.isi.dr.scrapping.billboards.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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

import edu.isi.dr.scrapping.billboards.DataStructure.Movie;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class ProcessMovies {
	private String folder;

	public ProcessMovies(String folder) {
		this.folder = folder;
	}

	public Map<Movie, LinkedList<Movie>> convertFileToJsonObjectList(float startYear, float endYear)
			throws FileNotFoundException, IOException, ParseException {

		Map<Movie, LinkedList<Movie>> movieLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();

			for (Object movieDate : keySet) {

				int year = Integer.parseInt(((String) movieDate).split(",")[1].trim());
				if (year >= startYear && year <= endYear) {
					JSONArray movies = (JSONArray) root.get(movieDate);
					Iterator<JSONObject> ll = movies.iterator();
					while (ll.hasNext()) {
						Movie createMovie = jread.createMovie(ll.next());
						try {
							if (Integer.parseInt(createMovie.getThisWeek()) > 11)
								continue;
						} catch (Exception e) {
							continue;
						}
						if (movieLists.containsKey(createMovie)) {
							movieLists.get(createMovie).add(createMovie);
						} else {
							LinkedList<Movie> arts = new LinkedList<>();
							arts.add(createMovie);
							movieLists.put(createMovie, arts);
						}
					}
				}

			}

		}
		return movieLists;
	}

	public Map<Movie, Integer> convertFileToJsonObjectCount(float startYear, float endYear)
			throws FileNotFoundException, IOException, ParseException {

		Map<Movie, Integer> MovieLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();

			for (Object MovieDate : keySet) {

				int year = Integer.parseInt(((String) MovieDate).split(",")[1].trim());
				if (year >= startYear && year <= endYear) {
					JSONArray Movies = (JSONArray) root.get(MovieDate);
					Iterator<JSONObject> ll = Movies.iterator();
					while (ll.hasNext()) {
						Movie createArticle = jread.createMovie(ll.next());
						try {
							if (Integer.parseInt(createArticle.getThisWeek()) > 10)
								continue;
						} catch (Exception e) {
							continue;
						}
						if (MovieLists.containsKey(createArticle)) {
							MovieLists.put(createArticle, MovieLists.get(createArticle) + 1);
						} else {
							MovieLists.put(createArticle, 1);
						}
					}
				}

			}

		}
		return MovieLists;
	}

	public Double processWeeksOnChartPercentage(String fileLocation, Map<Movie, LinkedList<Movie>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		List<Integer> contents = new ArrayList<>();
		List<String> checkList = new ArrayList<String>();
		for (Movie art : filteredList.keySet()) {
			contents.add(filteredList.get(art).size());
			// checkList.add(art.getTitle() + " " + );
		}
		Collections.sort(contents);
		/*
		 * Collections.sort(checkList); for (String line : checkList) {
		 * writer.println(line); }
		 */
		// writer.close();
		int lowPercentage = contents.size() * 20 / 100;
		Double res = 0.0;
		for (int i = 0; i < lowPercentage; i++) {

			res += contents.get(i);
		}
		for (int j = contents.size() -1; j > contents.size() - lowPercentage; j--) {
			res += contents.get(j);
		}
		return res / (2 * lowPercentage )  ;

	}

	public void processWeeksOnChart(String fileLocation, Map<Movie, LinkedList<Movie>> filteredList)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();
		List<String> checkList = new ArrayList<String>();
		for (Movie art : filteredList.keySet()) {
			contents.append(filteredList.get(art).size() + ",");
			// checkList.add(art.getTitle() + " " + );
		}
		/*
		 * Collections.sort(checkList); for (String line : checkList) {
		 * writer.println(line); }
		 */
		// writer.close();

		String finalContent = contents.toString();
		finalContent = finalContent.substring(0, finalContent.length() - 1);
		writer.print(finalContent);
		writer.close();

	}

	public Map<Integer, List<Movie>> findUniqueMovies() throws FileNotFoundException, IOException, ParseException {
		Map<Integer, List<Movie>> MovieLists = new TreeMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			int year = 0;
			for (Object movieDate : keySet) {
				year = Integer.parseInt(((String) movieDate).split(",")[1].trim());
				if (year < 1985 || year > 2015)
					continue;
				JSONArray songs = (JSONArray) root.get(movieDate);
				Iterator<JSONObject> ll = songs.iterator();
				while (ll.hasNext()) {
					Movie createMovie = jread.createMovie(ll.next());
					try {
						if (Integer.parseInt(createMovie.getThisWeek()) > 10)
							continue;
					} catch (Exception e) {
						continue;
					}
					if (MovieLists.containsKey(year)) {
						if (!MovieLists.get(year).contains(createMovie))
							MovieLists.get(year).add(createMovie);
					} else {
						List<Movie> songArray = new ArrayList<Movie>();
						songArray.add(createMovie);
						MovieLists.put(year, songArray);
					}
				}

			}

		}

		return MovieLists;
	}

	public void persistUniqueMoviesByYear(String fileLocation, Map<Integer, List<Movie>> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		for (Integer art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art).size());
		}
		writer.close();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		ProcessMovies movieMojo = new ProcessMovies("movies");
		// movieMojo.histogramsWeeksOnChart();
	//	movieMojo.uniqueMoviesByYearChart();
	//	movieMojo.plotGiniCoefficient();
		movieMojo.plotMovingAverage();
	}

	private void uniqueMoviesByYearChart() throws FileNotFoundException, IOException, ParseException {
		// Map<Movie, LinkedList<Movie>> convertFileToJsonObjectList =
		// this.convertFileToJsonObjectList(1985, 2015);
		Map<Integer, List<Movie>> findUniqueMovies = this.findUniqueMovies();
		persistUniqueMoviesByYear("movieanalytics/top10/uniqueMovies.txt", findUniqueMovies);

	}

	private void histogramsWeeksOnChart()
			throws FileNotFoundException, IOException, ParseException, UnsupportedEncodingException {
		for (int start = 1986; start <= 2014; start += 5) {
			Map<Movie, LinkedList<Movie>> convertFileToJsonObjectList = this.convertFileToJsonObjectList(start,
					start + 4);
			this.processWeeksOnChart("movieanalytics/top10/" + start + "-" + (start + 4), convertFileToJsonObjectList);
		}
	}

	// Gini co-efficient
	public void plotGiniCoefficient() throws FileNotFoundException, IOException, ParseException {
		Map<Float, Double> graph = new TreeMap<>();
		for (float start = 1985; start + 4 <= 2015; start += 1) {
			Map<Movie, Integer> objLists = this.convertFileToJsonObjectCount(start, start + 4);
			graph.put(start, this.processGiniCooefficient(objLists));
			// Map<Movie, Integer> objLists1 =
			// this.convertFileToJsonObjectTop100SixMonths(start, start + 1);
			// graph.put((float) (start + 0.5),
			// this.processGiniCooefficient(objLists1));
		}
		this.processGeneric("movieanalytics/top10/gini_movies_top10.txt", graph);
	}

	// Gini co-efficient
	public void plotMovingAverage() throws FileNotFoundException, IOException, ParseException {
		Map<Float, Double> graph = new TreeMap<>();
		for (float start = 1982; start <= 2014; start += 1) {
			Map<Movie, LinkedList<Movie>> convertFileToJsonObjectList = this.convertFileToJsonObjectList(start,
					start + 4);
			graph.put(start,this.processWeeksOnChartPercentage("movieanalytics/top10/" + start + "-" + (start + 4), convertFileToJsonObjectList));
		}
		this.processGeneric("movieanalytics/top10/rollingaverage.txt", graph);
	}

	public void processGeneric(String fileLocation, Map<Float, Double> objLists)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");

		for (Float art : objLists.keySet()) {
			writer.println(art + "," + objLists.get(art));
		}

		writer.close();
	}

	public Map<Movie, Integer> convertFileToJsonObjectTop100SixMonths(float startYear, float endYear)
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

		Map<Movie, Integer> MovieLists = new HashMap<>();
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			for (Object MovieDate : keySet) {
				int year = Integer.parseInt(((String) MovieDate).split(":")[1]);
				if (year >= startYear && year <= endYear) {
					String months = ((String) MovieDate).split(" ")[0];
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

					JSONArray songs = (JSONArray) root.get(MovieDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						Movie createArticle = jread.createMovie(ll.next());
						if (MovieLists.containsKey(createArticle)) {
							MovieLists.put(createArticle, MovieLists.get(createArticle) + 1);
						} else {
							MovieLists.put(createArticle, 1);
						}
					}
				}

			}

		}
		return MovieLists;
	}

	public Double processGiniCooefficient(Map<Movie, Integer> art) {
		Double gini = 0.0;
		List<Double> values = new ArrayList<Double>();
		for (Integer val : art.values()) {
			values.add(val / 261.0);
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

}
