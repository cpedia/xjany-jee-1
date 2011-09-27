package com.lti.Exception;

public class MPTException extends Exception{
private static final long serialVersionUID = 1L;
	
	public static int UNKNOWN_ERROR=0;
	public static int DEVIDE_ZERO=1;
	public static int NO_SECURITY_ADJCLOSE_PRICE=2;
	public static int ALPHA_ERROR=3;
	public static int BETA_ERROR=4;
	public static int AR_ERROR=5;
	public static int STANDARD_DEVIATION_ERROR=6;
	public static int RSI_ERROR=7;
	public static int RSQUARED_ERROR=8;
	public static int DRAWDOWN_ERROR=9;
	public static int SHARPE_RATIO_ERROR=10;
	public static int TREYNOR_RATIO_ERROR=11;
	public static String MESSAGE[]={"UNKNOWN_ERROR","DEVIDE_ZERO",
		"NO_SECURITY_ADJCLOSE_PRICE",
		"ALPHA_ERROR",
		"BETA_ERROR",
		"AR_ERROR",
		"STANDARD_DEVIATION_ERROR",
		"RSI_ERROR",
		"RSQUARED_ERROR",
		"DRAWDOWN_ERROR",
		"SHARPE_RATIO_ERROR",
		"TREYNOR_RATIO_ERROR",};
	
	private int Detail=0;
	private String Message=null;
	public MPTException(){
		
	}
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
	public MPTException(int detail){
		this.Detail=detail;
		if(this.Detail>=SecurityException.MESSAGE.length)this.Detail=0;
	}
	public MPTException(int detail,String message){
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

}

