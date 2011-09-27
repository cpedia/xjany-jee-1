package com.lti.action.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.SecurityType;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionSupport;


public class SaveAction extends ActionSupport implements Action {

	static Log log = LogFactory.getLog(SaveAction.class);
	
	private static final long serialVersionUID = 1L;	
	
	private String action;
	
	private java.lang.Long ID;

	// fields
	private java.lang.Long classID;
	
	private List<AssetClass> acs;
	
	private java.lang.String className;

	private java.lang.Double currentPrice;

	private java.lang.String symbol;

	private boolean diversified;

	private java.lang.String name;

	private java.lang.Integer securityType;
	
	private List<SecurityType> sts;

	private AssetClassManager assetClassManager;
	
	private SecurityManager securityManager;
	
	private UserManager userManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;
	
	private String title;
	
	private List<SecurityMPT> smpt;
	
	private Double AR1,AR3,AR5,Beta1,Beta3,Beta5,Sharpe1,Sharpe3,Sharpe5;
	
	public void validate(){
		
		sts=securityManager.getSecurityTypes();
		acs=assetClassManager.getClasses();
		

		
		if(this.action==null||this.action.equals(""))
			this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			if(symbol==null||symbol.equals(""))addFieldError("name","Name is not validate!");
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Security i=securityManager.get(ID);
				
				if(i==null)addFieldError("ID","ID is not validate!");
			}
			
			if(symbol==null||symbol.equals(""))addFieldError("name","Name is not validate!");
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Security i=securityManager.get(ID);
				
				if(i==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null && symbol == null){
				addFieldError("ID","ID or symbol is not validate!");
			}
			Security i;
			if(ID != null)
				i=securityManager.get(ID);
			else if(symbol != null)
				i = securityManager.getBySymbol(symbol);
			else
				i = null;
			if(i==null)
				addFieldError("ID","ID or symbol is not validate!");

		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}
	
	@Override
	public String execute() throws Exception {
		
		smpt=new ArrayList<SecurityMPT>();
		
		Security security=new Security();
		
		security.setID(ID);
		
		security.setSymbol(symbol);
		
		security.setName(name);
		
		security.setClassID(classID);
		
		security.setDiversified(diversified);
		
		security.setSecurityType(securityType);
		
		Long userID = userManager.getLoginUser().getID();
		
		customizeRegion = customizeRegionManager.get(CustomizeUtil.SECURITY_INDIVIDUAL);
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			security.setID(null);
			
			securityManager.add(security);
			
			ID=security.getID();
			
			action=ACTION_UPDATE;
			
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Create Successfully!");
			
			CustomizeUtil.setRegion(customizeRegion, userID);
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			securityManager.update(security);
			
			action=ACTION_UPDATE;
			
			smpt=securityManager.getSecurityMPTS(ID);
			
			AR1=securityManager.getSecurityMPT(ID, -1).getAR();
			Beta1=securityManager.getSecurityMPT(ID, -1).getBeta();
			Sharpe1=securityManager.getSecurityMPT(ID, -1).getSharpeRatio();
			
			AR3=securityManager.getSecurityMPT(ID, -3).getAR();
			Beta3=securityManager.getSecurityMPT(ID, -3).getBeta();
			Sharpe3=securityManager.getSecurityMPT(ID, -3).getSharpeRatio();
			
			AR5=securityManager.getSecurityMPT(ID, -5).getAR();
			Beta5=securityManager.getSecurityMPT(ID, -5).getBeta();
			Sharpe5=securityManager.getSecurityMPT(ID, -5).getSharpeRatio();
			
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Update Successfully!");
			
			CustomizeUtil.setRegion(customizeRegion, userID);
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			securityManager.delete(ID);
			
			action=ACTION_CREATE;
			
			
			addActionMessage("Delete Successfully!");
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID != null)
				security=securityManager.get(ID);
			if(symbol != null)
				security = securityManager.getBySymbol(symbol);
			
			ID=security.getID();
			
			symbol=security.getSymbol();
			
			name=security.getName();
			
			classID=security.getClassID();
			
			diversified=security.isDiversified();
			
			securityType=security.getSecurityType();
			
			action=ACTION_UPDATE;
			
			smpt=securityManager.getSecurityMPTS(ID);
			
			SecurityMPT mpt1=securityManager.getSecurityMPT(ID, -1);
			if(mpt1!=null){
				AR1=mpt1.getAR();
				Beta1=mpt1.getBeta();
				Sharpe1=mpt1.getSharpeRatio();
			}
			
			SecurityMPT mpt3=securityManager.getSecurityMPT(ID, -3);
			if (mpt3!=null) {
				AR3 = mpt3.getAR();
				Beta3 = mpt3.getBeta();
				Sharpe3 = mpt3.getSharpeRatio();
			}
			
			SecurityMPT mpt5=securityManager.getSecurityMPT(ID, -5);
			if (mpt5!=null) {
				AR5 = mpt5.getAR();
				Beta5 = mpt5.getBeta();
				Sharpe5 = mpt5.getSharpeRatio();
			}
			title="Security : "+security.getSymbol();
			
			CustomizeUtil.setRegion(customizeRegion, userID);
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="New Security";
			
			CustomizeUtil.setRegion(customizeRegion, userID);
			
			return Action.INPUT;
		}
		
		return Action.ERROR;

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

	public java.lang.Long getClassID() {
		return classID;
	}

	public void setClassID(java.lang.Long classID) {
		this.classID = classID;
	}

	public java.lang.Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(java.lang.Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public boolean isDiversified() {
		return diversified;
	}

	public void setDiversified(boolean diversified) {
		this.diversified = diversified;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Integer getSecurityType() {
		return securityType;
	}

	public void setSecurityType(java.lang.Integer securityType) {
		this.securityType = securityType;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public java.lang.String getClassName() {
		return className;
	}

	public void setClassName(java.lang.String className) {
		this.className = className;
	}


	public List<SecurityType> getSts() {
		return sts;
	}

	public void setSts(List<SecurityType> sts) {
		this.sts = sts;
	}

	public List<AssetClass> getAcs() {
		return acs;
	}

	public void setAcs(List<AssetClass> acs) {
		this.acs = acs;
	}

	public AssetClassManager getAssetClassManager() {
		return assetClassManager;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

	public Double getAR1() {
		return AR1;
	}

	public void setAR1(Double ar1) {
		AR1 = ar1;
	}

	public Double getAR3() {
		return AR3;
	}

	public void setAR3(Double ar3) {
		AR3 = ar3;
	}

	public Double getAR5() {
		return AR5;
	}

	public void setAR5(Double ar5) {
		AR5 = ar5;
	}

	public Double getBeta1() {
		return Beta1;
	}

	public void setBeta1(Double beta1) {
		Beta1 = beta1;
	}

	public Double getBeta3() {
		return Beta3;
	}

	public void setBeta3(Double beta3) {
		Beta3 = beta3;
	}

	public Double getBeta5() {
		return Beta5;
	}

	public void setBeta5(Double beta5) {
		Beta5 = beta5;
	}

	public Double getSharpe1() {
		return Sharpe1;
	}

	public void setSharpe1(Double sharpe1) {
		Sharpe1 = sharpe1;
	}

	public Double getSharpe3() {
		return Sharpe3;
	}

	public void setSharpe3(Double sharpe3) {
		Sharpe3 = sharpe3;
	}

	public Double getSharpe5() {
		return Sharpe5;
	}

	public void setSharpe5(Double sharpe5) {
		Sharpe5 = sharpe5;
	}

	public List<SecurityMPT> getSmpt() {
		return smpt;
	}

	public void setSmpt(List<SecurityMPT> smpt) {
		this.smpt = smpt;
	}

}
