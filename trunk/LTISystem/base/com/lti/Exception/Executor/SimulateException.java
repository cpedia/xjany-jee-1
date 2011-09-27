package com.lti.Exception.Executor;

public class SimulateException extends Exception {

	private static final long serialVersionUID = -4717510688804962436L;

	protected long PortfolioID;
	protected String PortfolioName;
	protected long StrategyID;
	protected String StrategyName;
	
	
	public SimulateException() {
		super();
	}

	public SimulateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SimulateException(String arg0) {
		super(arg0);
	}

	public SimulateException(Throwable arg0) {
		super(arg0);
	}
	
	

	public SimulateException(String message,Throwable cause,long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message,cause);
		PortfolioID = portfolioID;
		PortfolioName = portfolioName;
		StrategyID = strategyID;
		StrategyName = strategyName;
	}
	
	public SimulateException(String message,long portfolioID, String portfolioName, long strategyID, String strategyName) {
		super(message);
		PortfolioID = portfolioID;
		PortfolioName = portfolioName;
		StrategyID = strategyID;
		StrategyName = strategyName;
	}

	public SimulateException(String message,Throwable cause,long portfolioID, String portfolioName) {
		super(message,cause);
		PortfolioID = portfolioID;
		PortfolioName = portfolioName;
	}
	
	public SimulateException(String message,long portfolioID, String portfolioName) {
		super(message);
		PortfolioID = portfolioID;
		PortfolioName = portfolioName;
	}
	
	public long getPortfolioID() {
		return PortfolioID;
	}

	public void setPortfolioID(long portfolioID) {
		PortfolioID = portfolioID;
	}

	public String getPortfolioName() {
		return PortfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		PortfolioName = portfolioName;
	}

	public long getStrategyID() {
		return StrategyID;
	}

	public void setStrategyID(long strategyID) {
		StrategyID = strategyID;
	}

	public String getStrategyName() {
		return StrategyName;
	}

	public void setStrategyName(String strategyName) {
		StrategyName = strategyName;
	}

	
}
