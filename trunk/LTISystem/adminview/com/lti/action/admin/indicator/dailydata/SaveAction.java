package com.lti.action.admin.indicator.dailydata;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.IndicatorManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.IndicatorDailyData;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private java.lang.Long ID;

	private java.util.Date date;
	
	private java.lang.Double value;
	
	private java.lang.Long indicatorID;
	
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
			
			if(date==null)addFieldError("date","Date is not validate!");
			
			if(value==null)addFieldError("value","Value is not validate!");
			
			if(indicatorID==null){
				
				addFieldError("indicatorID","Indicator ID is not validate!");
				
				Indicator indicator=indicatorManager.get(indicatorID);
				
				if(indicator==null){
					addFieldError("indicatorID","Indicator ID is not validate!");
				}
			}
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				IndicatorDailyData idd=(IndicatorDailyData) indicatorManager.getIndicatorDailyData(ID);
				
				if(idd==null)addFieldError("ID","ID is not validate!");
			}
			
			if(date==null)addFieldError("date","Date is not validate!");
			
			if(value==null)addFieldError("value","Value is not validate!");
			
			if(indicatorID==null){
				
				addFieldError("indicatorID","Indicator ID is not validate!");
				
				return;
				

			}else{
				Indicator indicator=indicatorManager.get(indicatorID);
				
				if(indicator==null){
					addFieldError("indicatorID","Indicator ID is not validate!");
				}
			}
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				IndicatorDailyData idd=(IndicatorDailyData) indicatorManager.getIndicatorDailyData( ID);
				
				if(idd==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				IndicatorDailyData idd=(IndicatorDailyData) indicatorManager.getIndicatorDailyData( ID);
				
				if(idd==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			Indicator indicator=indicatorManager.get(indicatorID);
			
			if(indicator==null){
				addFieldError("indicatorID","Indicator ID is not validate!");
			}
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		Indicator indicator=indicatorManager.get(indicatorID);
		
		IndicatorDailyData dailydata=new IndicatorDailyData();
		
		dailydata.setID(ID);
		
		dailydata.setDate(date);
		
		dailydata.setValue(value);
		
		dailydata.setIndicatorID(indicatorID);
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			dailydata.setID(null);
			
			indicatorManager.addDailyData(dailydata);
			
			ID=dailydata.getID();
			
			action=ACTION_UPDATE;
			
			title="Indicator : "+indicator.getSymbol();
			
			addActionMessage("Add Daily Data Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			indicatorManager.updateDailyData(dailydata);
			
			action=ACTION_UPDATE;
			
			title="Indicator : "+indicator.getSymbol();
			
			addActionMessage("Update Daily Data Successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			indicatorManager.removeIndicatorDailyData(ID);
			
			action=ACTION_CREATE;
			
			addActionMessage("Delete Daily Data Successfully!");
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			dailydata=(IndicatorDailyData) indicatorManager.getIndicatorDailyData( ID);
			
			ID=dailydata.getID();
			
			date=dailydata.getDate();
			
			value=dailydata.getValue();
			
			indicatorID=dailydata.getIndicatorID();
			
			action=ACTION_UPDATE;
			
			title="Indicator : "+indicator.getSymbol();
			
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="New Daily Data ["+indicator.getSymbol()+"]";
			
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

	


	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.lang.Double getValue() {
		return value;
	}

	public void setValue(java.lang.Double value) {
		this.value = value;
	}

	public java.lang.Long getIndicatorID() {
		return indicatorID;
	}

	public void setIndicatorID(java.lang.Long indicatorID) {
		this.indicatorID = indicatorID;
	}

	public void setIndicatorManager(IndicatorManager indicatorManager) {
		this.indicatorManager = indicatorManager;
	}



	

}
