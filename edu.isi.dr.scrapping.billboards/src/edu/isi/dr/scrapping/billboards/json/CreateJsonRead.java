package edu.isi.dr.scrapping.billboards.json;

import java.io.FileWriter;
import java.io.IOException;

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

		return obj;
	}

	public Article createArticle(JSONObject article) {
		Article obj = new Article();

		obj.setArtistName(((String) article.get("artistName")).replace(" Watch","").trim());
		obj.setLastWeekRank((String) article.get("lastWeekRank"));
		obj.setPeakPositionRank((String) article.get("peakPositionRank"));
		obj.setRank((String) article.get("rank"));
		obj.setTitle((String) article.get("title"));
		obj.setWeaksOnChart((String) article.get("totalWeeksOnChart"));

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
}
