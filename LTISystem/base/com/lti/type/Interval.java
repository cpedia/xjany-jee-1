package com.lti.type;

import java.io.Serializable;

public class Interval implements Serializable{
	private static final long serialVersionUID = -8434886823931657907L;
	private java.util.Date StartDate;
	private java.util.Date EndDate;
	public Interval(){
		
	}
	public Interval(java.util.Date s,java.util.Date e){
		StartDate=s;
		EndDate=e;
	}
	public java.util.Date getStartDate() {
		return StartDate;
	}
	public void setStartDate(java.util.Date startDate) {
		StartDate = startDate;
	}
	public java.util.Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}
	/*
	 * calculate the count of days between the interval
	 * @return the count of days between the interval
	 */
	public int getDays()
	{
		long diff = StartDate.getTime() - EndDate.getTime();
		int days = (int) (diff / (1000 * 60 * 60 * 24));
		return days;
	}
}
