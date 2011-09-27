package com.lti.Exception;

public class XClassException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static int UNKNOWN_ERROR=0;
	public static int NO_PARENT_ERROR=1;
	public static int NO_EXISTS_ERROR=2;

	
	public static String MESSAGE[]={"UNKNOWN_ERROR","NO_PARENT_ERROR","NO_EXISTS_ERROR"};
	
	private int Detail=0;
	
	private String Message=null;

	private java.lang.Long classID=null;
	private java.lang.String className=null;
	private java.lang.Long parentID=null;
	private java.lang.String parentName=null;
	
	public XClassException(){

	}
	
	public XClassException(int detail,String message){
		this.Detail=detail;
		if(this.Detail>=XClassException.MESSAGE.length){
			this.Detail=0;
		}
		Message=message;
		
	}
	
	public String getMessage() {
		
		String returnMessage="Exception : "+XClassException.MESSAGE[this.Detail];
		
		if(this.classID!=null){
			returnMessage+="\r\n";
			returnMessage+="Security ID : "+this.classID;
			if(this.className!=null){
				returnMessage+="\r\n";
				returnMessage+="Security Name : "+this.className;
			}
		}
		
		if(this.parentID!=null){
			returnMessage+="\r\n";
			returnMessage+="Security ID : "+this.parentID;
			if(this.parentName!=null){
				returnMessage+="\r\n";
				returnMessage+="Security Name : "+this.parentName;
			}
		}
		
		if(this.Message!=null)returnMessage+="\r\nAdditional Message : \r\n"+this.Message+"\r\n";
		
		return returnMessage;
	}
	
	public XClassException(int detail){
		this.Detail=detail;
		if(this.Detail>=XClassException.MESSAGE.length)this.Detail=0;
	}
	
	public String toString(){
		return this.getMessage();
	}
	
	
	public void setAdditionalMessage(String message) {
		Message = message;
	}

	public int getDetail() {
		return Detail;
	}

	public java.lang.Long getClassID() {
		return classID;
	}

	public java.lang.String getClassName() {
		return className;
	}

	public java.lang.Long getParentID() {
		return parentID;
	}

	public java.lang.String getParentName() {
		return parentName;
	}

	public void setDetail(int detail) {
		Detail = detail;
	}

	public void setClassID(java.lang.Long classID) {
		this.classID = classID;
	}

	public void setClassName(java.lang.String className) {
		this.className = className;
	}

	public void setParentID(java.lang.Long parentID) {
		this.parentID = parentID;
	}

	public void setParentName(java.lang.String parentName) {
		this.parentName = parentName;
	}
	

}
