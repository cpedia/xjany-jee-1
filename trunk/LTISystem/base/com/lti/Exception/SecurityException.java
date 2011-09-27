package com.lti.Exception;

public class SecurityException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static int UNKNOWN_ERROR=0;
	public static int NULL_SECURITY=1;
	public static int NO_SECURITY_ADJCLOSE_PRICE=2;
	public static int NO_SECURITY_CLOSE_PRICE=3;
	public static int NO_HIGHEST_PRICE=4;
	public static int NO_LOWEST_PRICE=5;
	public static int SECURITY_ALREADY_EXISTS=6;
	public static int DATE_ERROR=7;
	public static int NO_NAV_CLOSE_PRICE=8;
	public static int NO_NAV_ADJCLOSE_PRICE=9;
	public static String MESSAGE[]={"UNKNOWN_ERROR","NULL_SECURITY",
		"NO_SECURITY_ADJCLOSE_PRICE",
		"NO_SECURITY_CLOSE_PRICE",
		"NO_HIGHEST_PRICE",
		"NO_LOWEST_PRICE",
		"SECURITY_ALREADY_EXISTS",
		"DATE_ERROR",
		"NO_NAV_CLOSE_PRICE",
		"NO_NAV_ADJCLOSE_PRICE"};
	
	private int Detail=0;
	private String Message=null;
	public SecurityException(){
		
	}
	
	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}
	public SecurityException(String message) {
		super(message);
	}
	public SecurityException(Throwable cause) {
		super(cause);
	}

	java.lang.Long securityID=null;
	
	java.lang.String securityName=null;
	
	java.util.Date date=null;
	
	java.util.Date startDate=null;
	
	java.util.Date endDate=null;
	
	
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
	public String getMessage() {
		
		String returnMessage="Exception : "+SecurityException.MESSAGE[this.Detail];
		
		if(this.securityID!=null){
			returnMessage+="\r\n";
			returnMessage+="Security ID : "+this.securityID;
			
		}
		
		if(this.securityName!=null){
			returnMessage+="\r\n";
			returnMessage+="Security Name : "+this.securityName;
		}
		
		if(this.date!=null){
			returnMessage+="\r\n";
			returnMessage+="Date : "+this.date;
		}
		
		if(this.startDate!=null){
			returnMessage+="\r\n";
			returnMessage+="Start Date : "+this.startDate;
		}
		
		if(this.endDate!=null){
			returnMessage+="\r\n";
			returnMessage+="End Date : "+this.endDate;
		}
		
		if(this.Message!=null)returnMessage+="\r\nAdditional Message : \r\n"+this.Message+"\r\n";
		
		return returnMessage;
	}
	public void setAdditionalMessage(String message) {
		Message = message;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	public void setDetail(int detail) {
		Detail = detail;
	}
	public SecurityException(int detail){
		this.Detail=detail;
		if(this.Detail>=SecurityException.MESSAGE.length)this.Detail=0;
	}
	public SecurityException(int detail,String message){
		this.Detail=detail;
		if(this.Detail>=SecurityException.MESSAGE.length){
			this.Detail=0;
		}
		Message=message;
	}
	public String toString(){
		return this.getMessage();
	}

	public int getDetail() {
		return Detail;
	}
	public java.lang.Long getSecurityID() {
		return securityID;
	}
	public void setSecurityID(java.lang.Long securityID) {
		this.securityID = securityID;
	}
	public java.lang.String getSecurityName() {
		return securityName;
	}
	public void setSecurityName(java.lang.String securityName) {
		this.securityName = securityName;
	}
	

}
