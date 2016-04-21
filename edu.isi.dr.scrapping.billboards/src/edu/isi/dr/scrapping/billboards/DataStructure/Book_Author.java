package edu.isi.dr.scrapping.billboards.DataStructure;

public class Book implements Comparable<Book> {

	String rank;
	String year;
	private String category;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	String week;

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getLastWeekRank() {
		return lastWeekRank;
	}

	public void setLastWeekRank(String lastWeekRank) {
		this.lastWeekRank = lastWeekRank;
	}

	public String getWeeksOnList() {
		return weeksOnList;
	}

	public void setWeeksOnList(String weeksOnList) {
		this.weeksOnList = weeksOnList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(this.title.charAt(this.title.length()-1) == ',') {
			this.title = this.title.substring(0, this.title.length()-1);
		}
		if(this.title.contains("(")){
		this.title = this.title.substring(0,this.title.indexOf("("));
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		
		this.author = author;
		if(this.author != null && this.author.charAt(this.author.length()-1) == '.') {
			this.author = this.author.substring(0, this.author.length()-1);
		}
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	String lastWeekRank;
	String weeksOnList;
	String title;
	String author;
	private String description;

	@Override
	public int compareTo(Book obj) {
		if (Integer.valueOf(this.year) < Integer.valueOf(obj.getYear())) {
			return 1;
		} else if (Integer.valueOf(this.year) > Integer.valueOf(obj.getYear())) {
			return -1;
		}
		if (Integer.valueOf(this.rank) > Integer.valueOf(obj.getYear()))
			return 1;
		return -1;

	}

	@Override
	public boolean equals(Object obj) {
		Book cmp = (Book) obj;
		if(cmp.getAuthor() == null || this.getAuthor() == null){
			return false;
		}
		return(cmp.getAuthor().trim().replace(" ","").equalsIgnoreCase(this.getAuthor().trim().replace(" ", "")));
		/*
		if (cmp.getTitle() != null && this.getTitle() != null) {
			if (cmp.getAuthor() != null && this.getAuthor() != null) {
				if(cmp.getAuthor().contains("Dominick") && this.getAuthor().contains("Dominick")){
					if(!cmp.getTitle().trim().replace(" ","").equalsIgnoreCase(this.getTitle().trim().replace(" ", ""))
						&& cmp.getAuthor().trim().replace(" ","").equalsIgnoreCase(this.getAuthor().trim().replace(" ", ""))){
						System.out.println("interested");
					}
				}
				return (cmp.getTitle().trim().replace(" ","").equalsIgnoreCase(this.getTitle().trim().replace(" ", ""))
						&& cmp.getAuthor().trim().replace(" ","").equalsIgnoreCase(this.getAuthor().trim().replace(" ", "")));
			} else {
				return (cmp.getTitle().trim().replace(" ","").equalsIgnoreCase(this.getTitle().trim()));
			}
		}*/
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		if(this.getAuthor() == null)
			return new String("").hashCode();
		return new String(this.getAuthor().trim().replace(" ","").toLowerCase()).hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Book title : " + this.getTitle() + ", Book author : " + this.getAuthor() + ", Book lastWeek : "
				+ this.getLastWeekRank() + ", total weeks" + this.weeksOnList + "," + week + " " + year;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
