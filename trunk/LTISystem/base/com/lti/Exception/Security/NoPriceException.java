package com.lti.Exception.Security;


public class NoPriceException extends Exception {
	
	public NoPriceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoPriceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoPriceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	java.lang.Long securityID=null;
	
	java.lang.String securityName=null;
	
	java.util.Date date=null;
	
	public NoPriceException(){
		super();
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

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public String getMessage() {
		
		String returnMessage="Exception : No price exception!";
		
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
		
		
		return returnMessage;
	}
}
