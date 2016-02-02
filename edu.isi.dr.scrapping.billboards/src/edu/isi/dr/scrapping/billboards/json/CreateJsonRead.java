package edu.isi.dr.scrapping.billboards.json;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.simple.JSONObject;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;
import edu.isi.dr.scrapping.billboards.DataStructure.Movie;

public class CreateJsonRead {

	public JSONObject createJsonObect(Article article) {
		JSONObject obj = new JSONObject();
		obj.put("artistName", article.getArtistName());
		obj.put("lastWeekRank", article.getLastWeekRank());
		obj.put("peakPositionRank", article.getPeakPositionRank());
		obj.put("rank", article.getRank());
		obj.put("title", article.getTitle());
		obj.put("totalWeeksOnChart", article.getWeaksOnChart());
		obj.put("weekNo", article.getWeekNo());

		return obj;
	}

	public JSONObject createJsonObect(Movie movie) {
		JSONObject obj = new JSONObject();
		obj.put("average", movie.getAverage());
		obj.put("budget", movie.getBudgetInMillions());
		obj.put("change", movie.getChange());
		obj.put("date", movie.getDate());
		obj.put("lastweek", movie.getLastWeek());
		obj.put("noweeks", movie.getNoWeeks());
		obj.put("percentchange", movie.getPercentChange());
		obj.put("studio", movie.getStudio());
		obj.put("theatrecount", movie.getTheatreCount());
		obj.put("thisweek", movie.getThisWeek());
		obj.put("title", movie.getTitle());
		obj.put("totalgross", movie.getTotalGross());
		obj.put("weeklygross", movie.getWeeklyGross());
		obj.put("weekNo", movie.getWeekNo());
		obj.put("weekyearno", movie.getWeekYearNo());
		obj.put("year", movie.getYear());

		return obj;
	}

	public Article createArticle(JSONObject article, String week) {
		Article obj = new Article();
		obj.setWeek(week);
		obj.setArtistName(((String) article.get("artistName")).replace(
				" Watch", "").trim());
		obj.setLastWeekRank((String) article.get("lastWeekRank"));
		obj.setPeakPositionRank((String) article.get("peakPositionRank"));
		obj.setRank((String) article.get("rank"));
		obj.setTitle((String) article.get("title"));
		obj.setWeaksOnChart((String) article.get("totalWeeksOnChart"));
		obj.setWeekNo((Long) article.get("weekNo"));
		return obj;
	}

	public Movie createMovie(JSONObject movie, String week) {
		Movie obj = new Movie();

		obj.setAverage((String) movie.get("average"));
		obj.setBudgetInMillions((String) movie.get("budget"));
		obj.setChange((String) movie.get("change"));
		obj.setDate((String) movie.get("date"));
		obj.setLastWeek((String) movie.get("lastweek"));
		obj.setNoWeeks((String) movie.get("noweeks"));
		obj.setPercentChange((String) movie.get("percentchange"));
		obj.setStudio((String) movie.get("studio"));
		obj.setTheatreCount((String) movie.get("theatrecount"));
		obj.setThisWeek((String) movie.get("thisweek"));
		obj.setTotalGross((String) movie.get("totalgross"));
		obj.setWeeklyGross((String) movie.get("weeklygross"));
		obj.setWeekNo((long) movie.get("weekNo"));
		obj.setWeekYearNo((Long) movie.get("weekyearno"));
		obj.setYear((Long) movie.get("year"));
		obj.setTitle((String)movie.get("title"));

		return obj;
	}

	public void writeToFile(JSONObject results, String location) {
		try {

			FileWriter file = new FileWriter(location);
			file.write(results.toJSONString());
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToCsvFile(List<Article> results, String location) {
		try {

			PrintWriter file = new PrintWriter(location);
			file.println("Week No,Rank,Title,Artist Name,Weeks on Chart,Last week Position,Peak Position,week");
			for (Article article : results) {
				file.println(article.getWeekNo() + ","
						+ Integer.parseInt(article.getRank()) + "," + "\""
						+ article.getTitle() + "\"" + "," + "\""
						+ article.getArtistName() + "\"" + ","
						+ article.getWeaksOnChart() + ","
						+ article.getLastWeekRank() + ","
						+ article.getPeakPositionRank() + ","
						+ article.getWeek());
			}

			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeToCsvFileMovie(List<Movie> results, String location) {
		try {
			PrintWriter file = new PrintWriter(location);
			file.println("Week No,This Week,Last Week,Title,Studio,Weekly Gross,% Change,Theater Count / Change	Average,Total Gross,Budget,Week#,Date");
			for (Movie article : results) {
				file.println(article.getWeekNo() + ","
						+ (article.getThisWeek()) + "," 
						+ article.getLastWeek() +  ","+ "\"" 
						+ article.getTitle() + "\"" + "," + "\"" 
						+ article.getStudio() + "\"" + "," + "\"" 
						+ article.getWeeklyGross() + "\"" + "," + "\"" 
						+ article.getPercentChange()  + "\"" + "," + "\"" 
						+ article.getTheatreCount() + "   "
						+ article.getChange() + "\"" + "," + "\"" 
						+ article.getAverage() + "\"" + "," + "\"" 
						+ article.getTotalGross() +"\"" + "," + "\"" 
						+ article.getBudgetInMillions() + "\"" + "," + "\"" 
						+ article.getNoWeeks() + "\"" + "," + "\"" 
						+article.getDate()	+"\""					
						);
			}

			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
