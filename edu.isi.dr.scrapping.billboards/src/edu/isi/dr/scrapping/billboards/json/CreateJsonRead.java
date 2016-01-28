package edu.isi.dr.scrapping.billboards.json;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.simple.JSONObject;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;

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
}
