package edu.isi.dr.scrapping.billboards.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;

public class ConvertToCsv {

	public void convertJsonToCsv(String folder) throws FileNotFoundException,
			IOException, ParseException {
		CreateJsonRead jread = new CreateJsonRead();
		JSONParser parser = new JSONParser();
		List<Article> listSongs = new ArrayList<>();
		File[] listFiles = new File(folder).listFiles();
		for (File jsonFile : listFiles) {
			JSONObject root = (JSONObject) parser
					.parse(new FileReader(jsonFile));
			Set keySet = root.keySet();
			for (Object songDate : keySet) {
				JSONArray songs = (JSONArray) root.get(songDate);
				Iterator<JSONObject> ll = songs.iterator();
				while (ll.hasNext()) {
					Article createArticle = jread.createArticle(ll.next(),
							(String) songDate);
					listSongs.add(createArticle);
				}
			}

		}
		
		new CreateJsonRead().writeToCsvFile(listSongs, "/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/result/billboard.csv");

	}
	
	public static void main(String[] args) {
		try {
			new ConvertToCsv().convertJsonToCsv("/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
