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
