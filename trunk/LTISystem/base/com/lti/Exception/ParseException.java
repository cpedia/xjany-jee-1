package com.lti.Exception;

public class ParseException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static int UNKNOWN_ERROR=0;
	public static int VARIABLE_INTEGER_ARRAY_ERROR=1;
	public static int VARIABLE_DOUBLE_ARRAY_ERROR=2;
	public static int VARIABLE_STRING_ARRAY_ERROR=3;

	
	public static String MESSAGE[]={"UNKNOWN_ERROR","VARIABLE_INTEGER_ARRAY_ERROR",
		"VARIABLE_DOUBLE_ARRAY_ERROR",
		"VARIABLE_STRING_ARRAY_ERROR"};
	
	private int Detail=0;
	
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

	private String Message=null;

	
	public ParseException(){

	}
	
	public ParseException(int detail,String message){
		this.Detail=detail;
		if(this.Detail>=ParseException.MESSAGE.length){
			this.Detail=0;
		}
		Message=message;
		
	}
	
	public String getMessage() {
		
		String returnMessage="Exception : "+ParseException.MESSAGE[this.Detail];
		
		
		if(this.Message!=null)returnMessage+="\r\nAdditional Message : \r\n"+this.Message+"\r\n";
		
		return returnMessage;
	}
	
	public ParseException(int detail){
		this.Detail=detail;
		if(this.Detail>=ParseException.MESSAGE.length)this.Detail=0;
	}
	
	public String toString(){
		return this.getMessage();
	}
	
	
	public void setAdditionalMessage(String message) {
		Message = message;
	}

	public void setDetail(int detail) {
		Detail = detail;
	}

	public int getDetail() {
		return Detail;
	}
}
