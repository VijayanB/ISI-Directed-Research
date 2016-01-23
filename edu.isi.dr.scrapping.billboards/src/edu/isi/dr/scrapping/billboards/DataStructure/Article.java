package edu.isi.dr.scrapping.billboards.DataStructure;

public class Article {

	private String rank;
	private String title;
	private String lastWeekRank;
	private String peakPositionRank;
	private String weaksOnChart;
	private String artistName;

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

}
