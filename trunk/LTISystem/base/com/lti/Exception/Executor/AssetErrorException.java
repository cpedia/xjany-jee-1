package com.lti.Exception.Executor;

public class AssetErrorException extends SimulateException {

	private static final long serialVersionUID = 7880022857311058923L;

	public AssetErrorException() {
		super();
	}

	public AssetErrorException(String message, long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message, portfolioID, portfolioName, strategyID, strategyName);
	}

	public AssetErrorException(String message, long portfolioID, String portfolioName) {
		super(message, portfolioID, portfolioName);
		// TODO Auto-generated constructor stub
	}

	public AssetErrorException(String message, Throwable cause, long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message, cause, portfolioID, portfolioName, strategyID, strategyName);
	}

	public AssetErrorException(String message, Throwable cause, long portfolioID, String portfolioName) {
		super(message, cause, portfolioID, portfolioName);
	}

	public AssetErrorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AssetErrorException(String arg0) {
		super(arg0);
	}

	public AssetErrorException(Throwable arg0) {
		super(arg0);
	}

}
