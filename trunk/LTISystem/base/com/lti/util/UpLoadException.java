package com.lti.util;

public class UpLoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public UpLoadException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UpLoadException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UpLoadException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	UpLoadException()
	{
		super();
	}
	
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
