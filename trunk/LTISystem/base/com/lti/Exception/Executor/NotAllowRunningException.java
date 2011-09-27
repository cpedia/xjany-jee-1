package com.lti.Exception.Executor;

public class NotAllowRunningException extends SimulateException {

	private static final long serialVersionUID = 7880022857311058923L;

	public NotAllowRunningException() {
		super();
	}

	public NotAllowRunningException(String message, long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message, portfolioID, portfolioName, strategyID, strategyName);
	}

	public NotAllowRunningException(String message, long portfolioID, String portfolioName) {
		super(message, portfolioID, portfolioName);
		// TODO Auto-generated constructor stub
	}

	public NotAllowRunningException(String message, Throwable cause, long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message, cause, portfolioID, portfolioName, strategyID, strategyName);
	}

	public NotAllowRunningException(String message, Throwable cause, long portfolioID, String portfolioName) {
		super(message, cause, portfolioID, portfolioName);
	}

	public NotAllowRunningException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotAllowRunningException(String arg0) {
		super(arg0);
	}

	public NotAllowRunningException(Throwable arg0) {
		super(arg0);
	}

}
