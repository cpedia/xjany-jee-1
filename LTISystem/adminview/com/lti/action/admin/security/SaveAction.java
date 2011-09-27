package com.lti.action.admin.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private java.lang.Long ID;

	// fields
	private java.lang.Long classID;

	private java.lang.Double currentPrice;

	private java.lang.String symbol;

	private boolean diversified;

	private java.lang.String name;

	private java.lang.Integer securityType;

	
	private SecurityManager securityManager;
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
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
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Security i=securityManager.get(ID);
				
				if(i==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		Security security=new Security();
		
		security.setID(ID);
		
		security.setSymbol(symbol);
		
		security.setName(name);
		
		security.setClassID(classID);
		
		security.setDiversified(diversified);
		
		security.setSecurityType(securityType);
		
		
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			security.setID(null);
			
			securityManager.add(security);
			
			ID=security.getID();
			
			action=ACTION_UPDATE;
			
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Create Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			securityManager.update(security);
			
			action=ACTION_UPDATE;
			
			title="Security : "+security.getSymbol();
			
			addActionMessage("Update Successfully!");
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			securityManager.delete(ID);
			
			action=ACTION_CREATE;
			
			
			addActionMessage("Delete Successfully!");
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			security=securityManager.get(ID);
			
			ID=security.getID();
			
			symbol=security.getSymbol();
			
			name=security.getName();
			
			classID=security.getClassID();
			
			diversified=security.isDiversified();
			
			securityType=security.getSecurityType();
			
			
			action=ACTION_UPDATE;
			
			title="Security : "+security.getSymbol();
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="New Security";
			
			
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

	
	public Long getID() {
	
		return ID;
	}

	
	public void setID(Long id) {
	
		ID = id;
	}

	

	
	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
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



}
