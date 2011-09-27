package com.lti.action.security.dailydata;

import java.util.Hashtable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.User;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(SaveAction.class);

	private String action;
	
	private java.lang.Long ID;

	// fields

	private java.util.Date date;
	
	
	private java.lang.Double split;
	private java.lang.Double dividend;

	/**
	 * 
	 */
	private java.lang.Double EPS;

	private java.lang.Double marketCap;

	private java.lang.Double PE;

	private java.lang.Double close;

	private java.lang.Double open;

	private java.lang.Double high;

	private java.lang.Double low;

	private java.lang.Double adjClose;

	private java.lang.Long volume;

	private java.lang.Double returnDividend;

	private java.lang.Long securityID;

	private java.lang.Double turnoverRate;
	
	private java.lang.Double NAV;
	
	private java.lang.Double adjNAV;
	
	private SecurityManager securityManager;
	

	private String title;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private UserManager userManager;
	
	private CustomizeRegion customizeRegion;

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.lang.Double getSplit() {
		return split;
	}

	public void setSplit(java.lang.Double split) {
		this.split = split;
	}

	public java.lang.Double getDividend() {
		return dividend;
	}

	public void setDividend(java.lang.Double dividend) {
		this.dividend = dividend;
	}


	public java.lang.Double getEPS() {
		return EPS;
	}

	public void setEPS(java.lang.Double eps) {
		EPS = eps;
	}

	public java.lang.Double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(java.lang.Double marketCap) {
		this.marketCap = marketCap;
	}

	public java.lang.Double getPE() {
		return PE;
	}

	public void setPE(java.lang.Double pe) {
		PE = pe;
	}

	public java.lang.Double getClose() {
		return close;
	}

	public void setClose(java.lang.Double close) {
		this.close = close;
	}

	public java.lang.Double getOpen() {
		return open;
	}

	public void setOpen(java.lang.Double open) {
		this.open = open;
	}

	public java.lang.Double getHigh() {
		return high;
	}

	public void setHigh(java.lang.Double high) {
		this.high = high;
	}

	public java.lang.Double getLow() {
		return low;
	}

	public void setLow(java.lang.Double low) {
		this.low = low;
	}

	public java.lang.Double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(java.lang.Double adjClose) {
		this.adjClose = adjClose;
	}

	public java.lang.Long getVolume() {
		return volume;
	}

	public void setVolume(java.lang.Long volume) {
		this.volume = volume;
	}

	public java.lang.Double getReturnDividend() {
		return returnDividend;
	}

	public void setReturnDividend(java.lang.Double returnDividend) {
		this.returnDividend = returnDividend;
	}

	public java.lang.Long getSecurityID() {
		return securityID;
	}

	public void setSecurityID(java.lang.Long securityID) {
		this.securityID = securityID;
	}

	public java.lang.Double getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(java.lang.Double turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public java.lang.Double getNAV() {
		return NAV;
	}

	public void setNAV(java.lang.Double nav) {
		NAV = nav;
	}

	public java.lang.Double getAdjNAV() {
		return adjNAV;
	}

	public void setAdjNAV(java.lang.Double adjNAV) {
		this.adjNAV = adjNAV;
	}

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			if(date==null)addFieldError("date","Date is not validate!");
			
			if(close==null)addFieldError("close","Value is not validate!");
			
			if(securityID==null){
				
				addFieldError("securityID","Security ID is not validate!");
				
				Security security=securityManager.get(securityID);
				
				if(security==null){
					addFieldError("securityID","Security ID is not validate!");
				}
			}
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				SecurityDailyData idd=(SecurityDailyData)securityManager.getSecurityDailyData( ID);;
				
				if(idd==null)addFieldError("ID","ID is not validate!");
			}
			
			if(date==null)addFieldError("date","Date is not validate!");
			
			if(close==null)addFieldError("close","Value is not validate!");
			
			if(securityID==null){
				
				addFieldError("securityID","Security ID is not validate!");
				
				Security security=securityManager.get(securityID);
				
				if(security==null){
					addFieldError("securityID","Security ID is not validate!");
				}
			}
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				SecurityDailyData idd=(SecurityDailyData) securityManager.getSecurityDailyData(ID);
				
				if(idd==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				SecurityDailyData idd=(SecurityDailyData) securityManager.getSecurityDailyData(ID);
				
				if(idd==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}
	
	@Override
	public String execute() throws Exception {
		
		customizeRegion = customizeRegionManager.get("Security Daily Data Individual Page");
		
		User user = userManager.getLoginUser();
		
		Long userID;
		
		if(user == null){
			userID = 0l;
		}
		else
		{
			userID = user.getID();
		}
		
		CustomizeUtil.setRegion(customizeRegion, userID);
		
		Security security=securityManager.get(securityID);
		
		SecurityDailyData dailydata=new SecurityDailyData();
		
		dailydata.setID(ID);
		
		dailydata.setAdjClose(adjClose);
		
		dailydata.setAdjNAV(adjNAV);
		
		dailydata.setClose(close);
		
		dailydata.setDate(date);
		
		dailydata.setDividend(dividend);
		
		dailydata.setEPS(EPS);
		
		dailydata.setHigh(high);
		
		dailydata.setLow(low);
		
		dailydata.setMarketCap(marketCap);
		
		dailydata.setNAV(NAV);
		
		dailydata.setOpen(open);
		
		dailydata.setPE(PE);
		
		dailydata.setReturnDividend(returnDividend);
		
		dailydata.setSecurityID(securityID);
		
		dailydata.setSplit(split);
		
		dailydata.setTurnoverRate(turnoverRate);
		
		dailydata.setVolume(volume);
		
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			dailydata.setID(null);
			
			securityManager.saveDailyData(dailydata);
			
			ID=dailydata.getID();
			
			action=ACTION_UPDATE;
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Create Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			securityManager.updateDailyData(dailydata);
			
			action=ACTION_UPDATE;
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Update Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			securityManager.removeSecurityDailyData(ID);
			
			action=ACTION_CREATE;
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Delete Successfully!");
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			dailydata=(SecurityDailyData) securityManager.getSecurityDailyData( ID);
			
			ID=dailydata.getID();
			
			adjClose=dailydata.getAdjClose();
			
			adjNAV=dailydata.getAdjNAV();
			
			close=dailydata.getClose();
			
			date=dailydata.getDate();
			
			dividend=dailydata.getDividend();
			
			EPS=dailydata.getEPS();
			
			high=dailydata.getHigh();
			
			low=dailydata.getLow();
			
			marketCap=dailydata.getMarketCap();
			
			NAV=dailydata.getNAV();
			
			open=dailydata.getOpen();
			
			PE=dailydata.getPE();
			
			returnDividend=dailydata.getReturnDividend();
			
			securityID=dailydata.getSecurityID();
			
			split=dailydata.getSplit();
			
			turnoverRate=dailydata.getTurnoverRate();
			
			volume=dailydata.getVolume();
			
			action=ACTION_UPDATE;
			
			title="Security : "+security.getSymbol();
			
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="New Daily Data";
			
			
			return Action.INPUT;
		}
		
		return Action.ERROR;

	}

}
