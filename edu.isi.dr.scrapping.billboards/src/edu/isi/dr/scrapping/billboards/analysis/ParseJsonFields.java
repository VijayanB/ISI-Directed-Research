package edu.isi.dr.scrapping.billboards.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseJsonFields {

	private String folder;

	public ParseJsonFields(String folder) {
		this.folder = folder;
	}

	public Set<JSONObject> convertFileToJsonObject(int startYear, int endYear)
			throws FileNotFoundException, IOException, ParseException {
		Set<JSONObject> songLists = new LinkedHashSet<JSONObject>();
        
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
				
				if(!songDate.equals("null")){
					String sDate = (String)(songDate);
					System.out.println(sDate);
					year = Integer.parseInt(sDate.substring(sDate.length()-5).trim());
				}
				else {
					year = 2016;
				}
				if (year >= startYear && year <= endYear) {
					JSONArray songs = (JSONArray) root.get(songDate);
					Iterator<JSONObject> ll = songs.iterator();
					while (ll.hasNext()) {
						songLists.add(ll.next());
					}
				}

			}

		}

		return songLists;
	}

	public void processWeeksOnChart(String fileLocation,
			Set<JSONObject> filteredList) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
		StringBuilder contents = new StringBuilder();
		contents.append("[");
		for (JSONObject jsonObject : filteredList) {
			String object = (String) jsonObject.get("totalWeeksOnChart");
			contents.append(" " + object + ",");
		}
		System.out.println(filteredList.size());

		String finalContent = contents.toString();
		finalContent = finalContent.substring(0, finalContent.length() - 1)
				+ " ]";
		writer.print(finalContent);
		writer.close();

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/data");
		Set<JSONObject> objLists = obj.convertFileToJsonObject(2005, 2015);
		obj.processWeeksOnChart(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/result/result.txt",
				objLists);
	}

}
