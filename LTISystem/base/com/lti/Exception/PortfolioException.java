package com.lti.Exception;

public class PortfolioException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static int UNKNOWN_ERROR=0;
	public static int INIT_MANAGER_ERROR=1;
	public PortfolioException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PortfolioException(String arg0) {
		super(arg0);
	}

	public PortfolioException(Throwable arg0) {
		super(arg0);
	}
	public static int ADD_ASSET_ERROR=2;
	public static int ADD_CASH_ERROR=3;
	public static int BALANCING_ERROR=4;
	public static int OUT_OF_CASH_ERROR=5;
	public static int BUY_ERROR=6;
	public static int NO_MATCH_ASSET_ERROR=7;
	public static int GET_ASSET_AMOUNT_ERROR=8;
	public static int GET_ASSET_NAME_SET_ERROR=9;
	public static int OUT_OF_SHARES_ERROR=10;
	public static int SELL_ERROR=11;
	public static int GET_TOTALAMOUNT_ERROR=12;
	public static int GET_ASSET_TARGET_PERCENTAGE_ERROR=13;
	public static int GET_ASSET_COLLECTION_AMOUNT_ERROR=14;
	public static int OUT_OF_CONFIDENCE_PERCENTAGE_ERROR=15;
	
	public static String MESSAGE[]={"UNKNOWN_ERROR","INIT_MANAGER_ERROR",
		"ADD_ASSET_ERROR","ADD_CASH_ERROR","BALANCING_ERROR","OUT_OF_CASH_ERROR","BUY_ERROR",
		"NO_MATCH_ASSET_ERROR","GET_ASSET_AMOUNT_ERROR","GET_ASSET_NAME_SET_ERROR",
		"OUT_OF_SHARES_ERROR","SELL_ERROR","GET_TOTALAMOUNT_ERROR",
		"GET_ASSET_TARGET_PERCENTAGE_ERROR","GET_ASSET_COLLECTION_AMOUNT_ERROR"};
	
	private int Detail=0;
	
	private String Message=null;

	private java.lang.Long securityID=null;
	private java.lang.String securityName=null;
	private java.util.Date date=null;
	private java.lang.String assetName=null;
	private java.lang.Double cash=null;
	private java.lang.Double tradeAmount=null;
	private Long[] securityIDs=null;
	
	public PortfolioException(){

	}
	
	public PortfolioException(int detail,String message){
		this.Detail=detail;
		if(this.Detail>=PortfolioException.MESSAGE.length){
			this.Detail=0;
		}
		Message=message;
		
	}
	
	public PortfolioException(int detail,String message,Throwable e){
		super(e);
		this.Detail=detail;
		if(this.Detail>=PortfolioException.MESSAGE.length){
			this.Detail=0;
		}
		Message=message;
		
	}
	
	public String getMessage() {
		
		String returnMessage="Exception : "+PortfolioException.MESSAGE[this.Detail];
		
		if(super.getMessage()!=null&&!super.getMessage().equals("")){
			returnMessage+="\r\n";
			returnMessage+=super.getMessage();
		}
		
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
		
		if(this.assetName!=null){
			returnMessage+="\r\n";
			returnMessage+="Asset Name : "+this.assetName;
		}
		
		if(securityIDs!=null&&securityIDs.length!=0){
			for(int i=0;i<securityIDs.length;i++){
				returnMessage+="\r\n";
				returnMessage+="Security ID["+i+"] : "+this.securityID;
			}
		}
		
		if(this.cash!=null){
			returnMessage+="\r\n";
			returnMessage+="Cash : "+this.cash;
		}
		
		if(this.tradeAmount!=null){
			returnMessage+="\r\n";
			returnMessage+="Trade Amount : "+this.tradeAmount;
		}
		
		if(this.Message!=null)returnMessage+="\r\nAdditional Message : \r\n"+this.Message+"\r\n";
		
		return returnMessage;
	}
	
	public PortfolioException(int detail){
		this.Detail=detail;
		if(this.Detail>=PortfolioException.MESSAGE.length)this.Detail=0;
	}
	public PortfolioException(int detail,Throwable e){
		super(e);
		this.Detail=detail;
		if(this.Detail>=PortfolioException.MESSAGE.length)this.Detail=0;
	}
	
	public String toString(){
		return this.getMessage();
	}
	
	public void setAdditionalMessage(String message) {
		Message = message;
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
	public void setDetail(int detail) {
		Detail = detail;
	}
	public int getDetail() {
		return Detail;
	}
	public java.lang.String getAssetName() {
		return assetName;
	}
	public void setAssetName(java.lang.String assetName) {
		this.assetName = assetName;
	}
	public java.lang.Double getCash() {
		return cash;
	}
	public void setCash(java.lang.Double cash) {
		this.cash = cash;
	}
	public java.lang.Double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(java.lang.Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public Long[] getSecurityIDs() {
		return securityIDs;
	}
	public void setSecurityIDs(Long[] securityIDs) {
		this.securityIDs = securityIDs;
	}
}
