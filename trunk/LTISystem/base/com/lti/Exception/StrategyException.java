package com.lti.Exception;

public class StrategyException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static int UNKNOWN_ERROR=0;
	
	public static int ASSET_ALLOCATION_STRATEGY_RUN_EXCEPTION=1;
	public static int ASSET_ALLOCATION_STRATEGY_INIT_EXCEPTION=2;
	
	public static int CASH_FLOW_STRATEGY_RUN_EXCEPTION=3;
	public static int CASH_FLOW_STRATEGY_INIT_EXCEPTION=4;
	
	public static int REBALANCING_STRATEGY_RUN_EXCEPTION=5;
	public static int REBALANCING_STRATEGY_INIT_EXCEPTION=6;
	
	public static int ASSET_STRATEGY_RUN_EXCEPTION=7;
	public static int ASSET_STRATEGY_INIT_EXCEPTION=8;
	
	public static int UPDATE_DAILYDATA_EXCEPTION=9;
	public static int RUNNING_DATE_ERROR=10;
	
	public static int INIT_BASE_STRATEGY_CLASS_FAILED=11;
	
	public static String MESSAGE[]={"UNKNOWN_ERROR=0","ASSET_ALLOCATION_STRATEGY_RUN_EXCEPTION",
		"ASSET_ALLOCATION_STRATEGY_INIT_EXCEPTION",
		"CASH_FLOW_STRATEGY_RUN_EXCEPTION","CASH_FLOW_STRATEGY_INIT_EXCEPTION",
		"REBALANCING_STRATEGY_RUN_EXCEPTION","REBALANCING_STRATEGY_INIT_EXCEPTION",
		"ASSET_STRATEGY_RUN_EXCEPTION","ASSET_STRATEGY_INIT_EXCEPTION",
		"UPDATE_DAILYDATA_EXCEPTION","RUNNING_DATE_ERROR","INIT_BASE_STRATEGY_CLASS_FAILED"};
	
	
	
	
	private int Detail=0;
	private String PortfolioName=null;
	private String AssetName=null;
	private String StrategyName=null;
	private String SecurityName=null;
	private String Message=null;
	
	private java.util.Date runningDate=null;
	
	private Long portfolioID=null;
	private Long strategyID=null;
	private Long securityID=null;

	
	
	public long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(long strategyID) {
		this.strategyID = strategyID;
	}

	public long getSecurityID() {
		return securityID;
	}

	public void setSecurityID(long securityID) {
		this.securityID = securityID;
	}

	public StrategyException(){
		
	}
	
	public StrategyException(int detail){
		this.Detail=detail;
		if(this.Detail>=StrategyException.MESSAGE.length)this.Detail=0;
	}
	public StrategyException(int detail,String message){
		this.Detail=detail;
		if(this.Detail>=StrategyException.MESSAGE.length){
			this.Detail=0;
		}
		Message=message;
	}
	
	public StrategyException(int detail,String portfolioName,String strategyName){
		this.Detail=detail;
		if(this.Detail>=StrategyException.MESSAGE.length){
			this.Detail=0;
		}
		this.PortfolioName=portfolioName;
		this.StrategyName=strategyName;
		
	}
	public StrategyException(int detail,String portfolioName,String assetName,String strategyName){
		this.Detail=detail;
		if(this.Detail>=StrategyException.MESSAGE.length){
			this.Detail=0;
		}
		this.PortfolioName=portfolioName;
		this.AssetName=assetName;
		this.StrategyName=strategyName;
	}	
	
	public String toString(){
		return this.getMessage();
	}

	public int getDetail() {
		return Detail;
	}

	public String getPortfolioName() {
		return PortfolioName;
	}

	public String getAssetName() {
		return AssetName;
	}

	public String getStrategyName() {
		return StrategyName;
	}

	public String getMessage() {
		String info="";
		
		info+="Exception:"+StrategyException.MESSAGE[this.Detail]+"\r\n";
		
		if(this.portfolioID!=null)info+="Portfolio ID :"+this.portfolioID+"\r\n";
		if(this.PortfolioName!=null)info+="Portfolio Name :"+this.PortfolioName+"\r\n";
		
		if(this.strategyID!=null)info+="Strategy ID :"+this.strategyID+"\r\n";
		if(this.StrategyName!=null)info+="Strategy Name :"+this.StrategyName+"\r\n";
		
		if(this.securityID!=null)info+="Security ID :"+this.securityID+"\r\n";
		if(this.SecurityName!=null)info+="Security Name :"+this.SecurityName+"\r\n";
		
		if(this.runningDate!=null)info+="Date : "+this.runningDate+"\r\n";
		
		if(this.Message!=null)info+="\r\nAdditional Message : \r\n"+this.Message+"\r\n";
		
		return info;
		
	}

	public String getSecurityName() {
		return SecurityName;
	}

	public void setSecurityName(String securityName) {
		SecurityName = securityName;
	}

	public void setDetail(int detail) {
		Detail = detail;
	}

	public void setPortfolioName(String portfolioName) {
		PortfolioName = portfolioName;
	}

	public void setAssetName(String assetName) {
		AssetName = assetName;
	}

	public void setStrategyName(String strategyName) {
		StrategyName = strategyName;
	}

	public void setAdditionalMessage(String message) {
		Message = message;
	}

	public java.util.Date getRunningDate() {
		return runningDate;
	}

	public void setRunningDate(java.util.Date runningDate) {
		this.runningDate = runningDate;
	}
	

}
