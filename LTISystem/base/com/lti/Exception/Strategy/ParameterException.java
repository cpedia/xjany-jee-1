package com.lti.Exception.Strategy;

public class ParameterException extends Exception {
	
	public ParameterException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ParameterException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	public ParameterException(String message){
		super(message);
	}

}
