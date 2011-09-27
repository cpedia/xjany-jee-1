package com.lti.action.admin.holiday;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.HolidayManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.Holiday;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private java.lang.Long ID;

	private java.util.Date date;
	
	private java.lang.String description;
	
	private java.lang.String symbol;
	
	private HolidayManager holidayManager;
	

	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			if(symbol==null||symbol.equals(""))addFieldError("symbol","Symbol is not validate!");
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Holiday h=holidayManager.get(ID);
				
				if(h==null)addFieldError("ID","ID is not validate!");
			}
			
			if(symbol==null||symbol.equals(""))addFieldError("symbol","Symbol is not validate!");
			
			if(date==null)addFieldError("date","Date is not validate!");
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Holiday h=holidayManager.get(ID);
				
				if(h==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null&&date==null){
				addFieldError("ID","ID is not validate!");
				return;
			}else{
				if(ID!=null){
					Holiday h=holidayManager.get(ID);
					
					if(h==null)addFieldError("ID","ID is not validate!");
					return;
				}
				if(ID==null&&date!=null){
					Holiday h=holidayManager.get(date);
					if(h==null){
						addFieldError("date","Date is not validate!");
						return;
					}
					ID=h.getID();
					
				}

			}
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		Holiday holiday=new Holiday();
		
		holiday.setID(ID);
		
		holiday.setDate(date);
		
		holiday.setSymbol(symbol);
		
		holiday.setDescription(description);
		
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			holiday.setID(null);
			
			holidayManager.save(holiday);
			
			ID=holiday.getID();
			
			action=ACTION_UPDATE;
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			holidayManager.update(holiday);
			
			action=ACTION_UPDATE;
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			holidayManager.remove(ID);
			
			action=ACTION_CREATE;
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			holiday=holidayManager.get(ID);
			
			ID=holiday.getID();
			
			date=holiday.getDate();
			
			symbol=holiday.getSymbol();
			
			description=holiday.getDescription();
			
			action=ACTION_UPDATE;
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
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


	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public void setHolidayManager(HolidayManager holidayManager) {
		this.holidayManager = holidayManager;
	}


}
