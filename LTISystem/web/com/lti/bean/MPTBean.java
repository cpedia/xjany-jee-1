package com.lti.bean;

public class MPTBean {
	private java.util.Date startDate;
	private java.util.Date endDate;
	private java.lang.Integer year;
	private java.lang.String yearString;
	private java.lang.String Alpha = "NA";
	private java.lang.String Beta = "NA";
	private java.lang.String AR = "NA";
	private java.lang.String RSquared = "NA";
	private java.lang.String SharpeRatio = "NA";
	private java.lang.String StandardDeviation = "NA";
	private java.lang.String TreynorRatio = "NA";
	private java.lang.String DrawDown = "NA";
	private java.lang.String SortinoRatio = "NA";
	private java.lang.Integer type;
	public java.lang.String getSortinoRatio() {
		return SortinoRatio;
	}
	public void setSortinoRatio(java.lang.String sortinoRatio) {
		SortinoRatio = sortinoRatio;
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public java.lang.String getAlpha() {
		return Alpha;
	}
	public void setAlpha(java.lang.String alpha) {
		Alpha = alpha;
	}
	public java.lang.String getBeta() {
		return Beta;
	}
	public void setBeta(java.lang.String beta) {
		Beta = beta;
	}
	public java.lang.String getAR() {
		return AR;
	}
	public void setAR(java.lang.String ar) {
		AR = ar;
	}
	public java.lang.String getRSquared() {
		return RSquared;
	}
	public void setRSquared(java.lang.String squared) {
		RSquared = squared;
	}
	public java.lang.String getSharpeRatio() {
		return SharpeRatio;
	}
	public void setSharpeRatio(java.lang.String sharpeRatio) {
		SharpeRatio = sharpeRatio;
	}
	public java.lang.String getStandardDeviation() {
		return StandardDeviation;
	}
	public void setStandardDeviation(java.lang.String standardDeviation) {
		StandardDeviation = standardDeviation;
	}
	public java.lang.String getTreynorRatio() {
		return TreynorRatio;
	}
	public void setTreynorRatio(java.lang.String treynorRatio) {
		TreynorRatio = treynorRatio;
	}
	public java.lang.String getDrawDown() {
		return DrawDown;
	}
	public void setDrawDown(java.lang.String drawDown) {
		DrawDown = drawDown;
	}
	public java.lang.Integer getYear() {
		return year;
	}
	public void setYear(java.lang.Integer year) {
		this.year = year;
	}
	public java.lang.String getYearString() {
		return yearString;
	}
	public void setYearString(java.lang.String yearString) {
		this.yearString = yearString;
	}
	public java.lang.Integer getType() {
		return type;
	}
	public void setType(java.lang.Integer type) {
		this.type = type;
	}

}
