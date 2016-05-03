package edu.isi.dr.scrapping.billboards.DataStructure;

public class Article {

	private String rank;
	private String title;
	private String lastWeekRank;
	private String peakPositionRank;
	private String weaksOnChart;
	private String artistName;
	private String week;
	private Long weekNo;

	public String getWeaksOnChart() {
		return weaksOnChart;
	}

	public void setWeaksOnChart(String weaksOnChart) {
		this.weaksOnChart = weaksOnChart;
	}

	public String getPeakPositionRank() {
		return peakPositionRank;
	}

	public void setPeakPositionRank(String peakPositionRank) {
		this.peakPositionRank = peakPositionRank;
	}

	public String getLastWeekRank() {
		return lastWeekRank;
	}

	public void setLastWeekRank(String lastWeekRank) {
		this.lastWeekRank = lastWeekRank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	@Override
	public boolean equals(Object obj) {
		Article article1 = (Article) obj;
		if (this.artistName.toLowerCase().trim().equals(article1.artistName.toLowerCase())
				&& this.title.toLowerCase().equals(article1.title.toLowerCase().trim()))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return new String(this.artistName.toLowerCase()).hashCode();//+ "-" + this.title.toLowerCase()).hashCode();
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Long getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(Long long1) {
		this.weekNo = long1;
	}

}
