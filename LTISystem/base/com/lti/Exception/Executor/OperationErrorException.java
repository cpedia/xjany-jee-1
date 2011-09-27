package com.lti.Exception.Executor;

import java.util.Date;

public class OperationErrorException extends SimulateException {

	private static final long serialVersionUID = 7880022857311058923L;

	public OperationErrorException() {
		super();
	}

	public OperationErrorException(String message, long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message, portfolioID, portfolioName, strategyID, strategyName);
	}

	public OperationErrorException(String message, long portfolioID, String portfolioName) {
		super(message, portfolioID, portfolioName);
	}

	public OperationErrorException(String message, Throwable cause, long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message, cause, portfolioID, portfolioName, strategyID, strategyName);
	}

	public OperationErrorException(String message, Throwable cause, long portfolioID, String portfolioName) {
		super(message, cause, portfolioID, portfolioName);
	}

	public OperationErrorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OperationErrorException(String arg0) {
		super(arg0);
	}

	public OperationErrorException(Throwable arg0) {
		super(arg0);
	}
	
	protected String operation;
	protected double amount;
	protected Date date;

	public OperationErrorException(String message, Throwable cause, long portfolioID, String portfolioName,String operation, double amount,Date date) {
		super(message,cause);
		this.operation = operation;
		this.amount = amount;
		this.date = date;
	}
	public OperationErrorException(String message,long portfolioID, String portfolioName,String operation, double amount,Date date) {
		super(message);
		this.operation = operation;
		this.amount = amount;
		this.date = date;
	}
	

}
