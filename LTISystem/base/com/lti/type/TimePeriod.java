
package com.lti.type;

public class TimePeriod {
	java.util.Date StartDate;
	java.util.Date EndDate;
	public TimePeriod(java.util.Date s,java.util.Date e){
		this.StartDate=s;
		this.EndDate=e;
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
}
