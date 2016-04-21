package edu.isi.dr.scrapping.billboards.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.isi.dr.scrapping.billboards.DataStructure.Book;
import edu.isi.dr.scrapping.billboards.json.CreateJsonRead;

public class NewYorkBestSellers {

	private String URL = "http://hawes.com/";

	private static Document retrieveDocumentFromUrl(String url) throws IOException, InterruptedException {
		Thread.currentThread().sleep(1000);
		Document doc = null;
		doc = Jsoup.connect(url).ignoreContentType(true).timeout(10 * 1000).get();
		return doc;
	}

	private void crawlIeeeWebsites(String fileLocation) throws IOException, InterruptedException {

		int count = 1950;
		while (count <= 2015) {
			Document webPage = retrieveDocumentFromUrl(URL + count + "/" + count + ".htm");
			File folder = new File(fileLocation + "/" + count);
			folder.mkdir();
			Elements content = webPage.select("CENTER").get(3).select("a");
			for (Element element : content) {
				URL url = new URL(URL + count + "/" + element.attr("href"));
				InputStream in = url.openStream();
				FileOutputStream fos = new FileOutputStream(
						new File(folder.getAbsolutePath() + "/" + element.text() + ".pdf"));
				int length = -1;
				byte[] buffer = new byte[1024];// buffer for portion of data
												// from connection
				while ((length = in.read(buffer)) > -1) {
					fos.write(buffer, 0, length);
				}
				fos.close();
				in.close();
			}

			count++;
		}

	}

	private void processPdfFiles(File folder, Map<String, List<Book>> bookList) {
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File[] files = folder.listFiles();
		//File[] files = new File[] { folder };
		for (File file : files) {
			if (file.getName().contains(".DS_Store"))
				continue;
			try {

				PDFManager pdfManager = new PDFManager();
				pdfManager.setFilePath(file.getAbsolutePath());
				String[] lines = pdfManager.ToText().split("\n");
				List<String> bookListLines = new ArrayList<String>();
				StringBuilder temp = new StringBuilder();
				for (int i = 10; i < lines.length; i += 1) {
					if (lines[i].trim().length() == 0) {
						bookListLines.add(temp.toString());
						temp = new StringBuilder();
					} else {
						temp.append(lines[i]);
					}

				}

				List<Book> bestSellerList = new ArrayList<Book>();
				boolean rank, end;
				Book book = null;
				boolean result = true;
				for (String line : bookListLines) {
					if (!line.trim().equals("")) {
						// if(lines[i].trim().charAt(index))
						if (result) {
							book = new Book();
						}
						if (line.contains("UNKNOWN") || line.trim().equals("List"))
							continue;
						if (line.contains("This is an “unofficial” list compiled from weeks before and after. "))
							break;
						result = processBooksName(line, file.getName(), file.getParentFile().getName(), book, result);
						if (result)
							bestSellerList.add(book);
					} else {
						continue;
					}
				}
				bookList.put(file.getName() + ":" + folder.getName(), bestSellerList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean processBooksName(String string, String week, String year, Book book, boolean res) {
		if (book.getYear() == null) {
			book.setYear(year);
		}
		if (book.getWeek() == null) {
			book.setWeek(week);
			book.setCategory("Fiction");
		}
		char[] characters = string.toCharArray();
		int i = 0;
		// book.setRank(Character.toString(characters[0]));
		if (book.getRank() == null) {
			// rank
			while (i < characters.length) {
				int charVal = characters[i];
				if (!Character.isDigit(characters[i])) {
					if (isNumeric(string.substring(0, i).trim())) {
						book.setRank(string.substring(0, i).trim());
						break;
					} else {
						return false;
					}
				}
				i++;
			}
		}
		if (book.getTitle() == null) {
			// title
			while (i < characters.length) {
				int charVal = characters[i];
				if (charVal >= 97 && charVal <= 122) {
					if (i == 0)
						break;

					book.setTitle(new StringBuilder(
							new StringBuilder(string.substring(2, i)).reverse().toString().replaceFirst(",", ""))
									.reverse().toString().trim());
					if (book.getTitle().equals("A FEARFUL JOY")) {
						book.setAuthor("Joyce Cary");
					}
					break;
				}
				i++;
			}
		}
		// author
		if (book.getAuthor() == null) {

			// System.out.println(string);
			String[] authors = string.split(" by");
			if (authors.length <= 1) {
				authors = string.split(" By");
				if (authors.length <= 1) {
					authors = string.split(",");
					if (authors.length <= 1) {
						book.setAuthor("UNKNOWN");
					}
				}

			}
			i = 0;
			if (authors.length > 1) {
				String author = authors[1].trim();
				char[] authorLetters = author.toCharArray();
				// author
				int temp = i;
				if (author.indexOf("(") > 0) {
					book.setAuthor(author.substring(0, author.indexOf("(")));

				} else {
					while (i < authorLetters.length - 1) {
						int charVal = authorLetters[i];
						int next = authorLetters[i + 1];
						if (charVal >= 97 && charVal <= 122 && next == 46) {
							book.setAuthor(author.substring(0, i + 1));
							break;
						}

						i++;
					}
					if (book.getAuthor() == null) {
						i = temp;
						while (i < authorLetters.length - 1) {
							int charVal = authorLetters[i];
							int next = authorLetters[i + 1];
							if (((charVal >= 97 && charVal <= 122) || (charVal == 32))
									&& (next == 45 || (next >= 49 && next <= 57))) {
								book.setAuthor(author.substring(0, i + 1));
								break;
							}

							i++;
						}
					}

				}
			}
		}

		// list on weeks
		String lines[] = string.trim().split("\\.");
		String[] inputs = lines[lines.length - 1].split(" ");
		if (inputs.length < 2)
			return false;
		if (isNumeric(inputs[inputs.length - 1])) {
			book.setWeeksOnList(inputs[inputs.length - 1]);
		} else
			return false;
		if (isNumeric(inputs[inputs.length - 2])) {
			book.setLastWeekRank(inputs[inputs.length - 2]);
		} else {
			return false;
		}
		// System.out.println(book);
		return true;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		NewYorkBestSellers seller = new NewYorkBestSellers();
		Map<String, List<Book>> bookList = new HashMap<>();
			for (File file : new File("result/newyork/4").listFiles()) {
				if (file.getName().contains(".DS_Store"))
					continue;
				seller.processPdfFiles(file, bookList);
			}
			/*
			 * for (File file : new File("test/book").listFiles()) { if
			 * (file.getName().contains(".DS_Store")) continue;
			 * seller.processPdfFiles(file, bookList); }
			 */

			// seller.processPdfFiles(new File("result/newyork/2015"),
			// bookList);
			CreateJsonRead obj = new CreateJsonRead();
			JSONObject root = new JSONObject();
			for (String date : bookList.keySet()) {
				JSONArray lists = new JSONArray();

				for (Book book : bookList.get(date)) {
					lists.add(obj.createJsonObect(book));
				}
				root.put(date, lists);

			}
			obj.writeToFile(root, "data/ny_fiction_res_4" + ".json");
		}

	public static boolean isNumeric(String str) {
		if (str.contains("--"))
			return true;
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

}
