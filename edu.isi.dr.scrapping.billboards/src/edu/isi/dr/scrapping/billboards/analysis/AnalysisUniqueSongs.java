package edu.isi.dr.scrapping.billboards.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import edu.isi.dr.scrapping.billboards.DataStructure.Article;

public class AnalysisUniqueSongs {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ParseException {
		ParseJsonFields obj = new ParseJsonFields(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/input");
		Map<Integer, List<Article>> objLists = obj.findUniqueSongs(100);
	/*	obj.processUniqueSongLists(
				"/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/uniquesongstop40/",
				objLists); */
		obj.processUniqueSong("/home/vijay/git/ISI-Directed-Research/edu.isi.dr.scrapping.billboards/uniquesongstop100/analysis_unique_count_top100",objLists);
	}

}
