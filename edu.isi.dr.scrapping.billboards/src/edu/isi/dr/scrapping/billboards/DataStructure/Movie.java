package edu.isi.dr.scrapping.billboards.DataStructure;

public class Movie {

	String thisWeek;
	String lastWeek;
	String title;
	String studio;
	String weeklyGross;
	String percentChange;
	String theatreCount;
	String change;
	String average;
	String totalGross;
	String budgetInMillions;
	String noWeeks;
	private long weekNo;
	private Long year;
	private String date;
	private Long weekYearNo;

	public String getThisWeek() {
		return thisWeek;
	}

	public void setThisWeek(String string) {
		this.thisWeek = string;
	}

	public String getLastWeek() {
		return lastWeek;
	}

	public void setLastWeek(String lastWeek) {
		this.lastWeek = lastWeek;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getWeeklyGross() {
		return weeklyGross;
	}

	public void setWeeklyGross(String weeklyGross) {
		this.weeklyGross = weeklyGross;
	}

	public String getPercentChange() {
		return percentChange;
	}

	public void setPercentChange(String percentChange) {
		this.percentChange = percentChange;
	}

	public String getTheatreCount() {
		return theatreCount;
	}

	public void setTheatreCount(String theatreCount) {
		this.theatreCount = theatreCount;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getTotalGross() {
		return totalGross;
	}

	public void setTotalGross(String totalGross) {
		this.totalGross = totalGross;
	}

	public String getBudgetInMillions() {
		return budgetInMillions;
	}

	public void setBudgetInMillions(String budgetInMillions) {
		this.budgetInMillions = budgetInMillions;
	}

	public String getNoWeeks() {
		return noWeeks;
	}

	public void setNoWeeks(String string) {
		this.noWeeks = string;
	}

	public long getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(long weekNo) {
		this.weekNo = weekNo;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long long1) {
		this.year = long1;
	}

	public Long getWeekYearNo() {
		return weekYearNo;
	}

	public void setWeekYearNo(Long long1) {
		this.weekYearNo = long1;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/*
	 * @Override public boolean equals(Object obj) { Movie cmp = ((Movie) obj);
	 * if (this.getTitle().equalsIgnoreCase(cmp.getTitle()) &&
	 * this.getStudio().equalsIgnoreCase(cmp.getStudio())) { return true; } else
	 * { return false; } }
	 */

	@Override
	public boolean equals(Object obj) {
		if (this.getWeekNo() == (long)obj) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int) this.getWeekNo();
	}
	
	public double getWeeklyShare() {
		return weeklyShare;
	}

	public void setWeeklyShare(double d) {
		this.weeklyShare = d;
	}

	private double weeklyShare;
	

}
