package com.lti.action.admin.indicator;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.IndicatorManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.Indicator;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private java.lang.Long ID;

	private java.lang.String symbol;
	
	private java.lang.String description;
	
	private IndicatorManager indicatorManager;

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
				Indicator i=indicatorManager.get(ID);
				
				if(i==null)addFieldError("ID","ID is not validate!");
			}
			
			if(symbol==null||symbol.equals(""))addFieldError("name","Name is not validate!");
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Indicator i=indicatorManager.get(ID);
				
				if(i==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Indicator i=indicatorManager.get(ID);
				
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
		
		Indicator indicator=new Indicator();
		
		indicator.setID(ID);
		
		indicator.setSymbol(symbol);
		
		indicator.setDescription(description);
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			indicator.setID(null);
			
			indicatorManager.save(indicator);
			
			ID=indicator.getID();
			
			action=ACTION_UPDATE;
			
			title="Indicator : "+indicator.getSymbol();
			
			addActionMessage("Add Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			indicatorManager.update(indicator);
			
			action=ACTION_UPDATE;
			
			title="Indicator : "+indicator.getSymbol();
			
			addActionMessage("Update Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			indicatorManager.remove(ID);
			
			action=ACTION_CREATE;
			
			addActionMessage("Delete Successfully!");
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			indicator=indicatorManager.get(ID);
			
			ID=indicator.getID();
			
			symbol=indicator.getSymbol();
			
			description=indicator.getDescription();
			
			action=ACTION_UPDATE;
			
			title="Indicator : "+indicator.getSymbol();
			
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="New Indicator";
			
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

	public void setIndicatorManager(IndicatorManager indicatorManager) {
		this.indicatorManager = indicatorManager;
	}

	public String getDescription() {
	
		return description;
	}

	
	public void setDescription(String description) {
	
		this.description = description;
	}
	

}
