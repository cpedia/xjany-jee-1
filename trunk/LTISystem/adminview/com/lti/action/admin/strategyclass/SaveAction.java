package com.lti.action.admin.strategyclass;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.StrategyClassManager;
import com.lti.service.bo.StrategyClass;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	private String action;
	
	private java.lang.Long ID;

	private java.lang.Long benchmarkID;
	
	private java.lang.String benchmarkName;
	
	private List<com.lti.type.Pair> dynamicAttribute;

	private java.lang.String name;

	private java.lang.Long parentID;
	
	private java.lang.String parentName;

	private List<com.lti.type.Pair> staticAttribute;
	
	private StrategyClassManager strategyClassManager;
	
	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_VIEW;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			if(name==null||name.equals(""))addFieldError("name","Name is not validate!");
			
			if(parentID==null){
				addFieldError("parentID","Parent ID is not validate!");
				return;
			}else{	
				StrategyClass parent=strategyClassManager.get(parentID);
				
				if(parent==null){
					addFieldError("parentID","Parent ID is not validate!");
					return;
				}
				parentName=parent.getName();
			}
			
			//other fields
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}
			else{
				
				StrategyClass strategyClass=strategyClassManager.get(ID);
				
				if(strategyClass==null)addFieldError("ID","ID is not validate!");
			}
			
			if(name==null||name.equals(""))addFieldError("name","Name is not validate!");
			
			if(parentID==null){
				addFieldError("parentID","Parent ID is not validate!");
				return;
			}else{	
				StrategyClass parent=strategyClassManager.get(parentID);
				
				if(parent==null){
					addFieldError("parentID","Parent ID is not validate!");
					return;
				}
				parentName=parent.getName();
			}
			//other fields
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				
				StrategyClass strategyClass=strategyClassManager.get(ID);
				
				if(strategyClass==null)addFieldError("ID","ID is not validate!");
			}
			
			//other fields
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}
			else{
				
				StrategyClass strategyClass=strategyClassManager.get(ID);
				
				if(strategyClass==null)addFieldError("ID","ID is not validate!");
			}
			
			
			//other fields
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			if(this.parentID==null)parentID=0l;
			StrategyClass ac=strategyClassManager.get(parentID);
			if(ac==null){
				addFieldError("parentID", "Parent ID is invalid!");
				return;
			}
			this.parentName=ac.getName();
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	
	
	@Override
	public String execute() throws Exception {
		
		StrategyClass strategyClass=new StrategyClass();
		
		strategyClass.setID(ID);
		
		strategyClass.setName(name);
		
		
		strategyClass.setParentID(parentID);
		
		
		
		//other fields
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			strategyClass.setID(null);
			
			strategyClassManager.add(strategyClass);
			
			ID=strategyClass.getID();
			
			action=ACTION_UPDATE;
			
			ID=strategyClass.getID();
			
			return Action.INPUT;
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			
			strategyClassManager.update(strategyClass);
			
				
			return Action.INPUT;
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			strategyClassManager.delete(strategyClass.getID());
			
			action=ACTION_CREATE;
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			strategyClass=strategyClassManager.get(ID);
			
			ID=strategyClass.getID();
			
			name=strategyClass.getName();
			
			
			parentID=strategyClass.getParentID();
			
			
			

			StrategyClass parent=strategyClassManager.get(parentID);
			
			if(parent!=null){
				parentName=parent.getName();
			}
				
			
			action=ACTION_UPDATE;
			
			return Action.INPUT;
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			action=ACTION_SAVE;
			return Action.INPUT;
		}
		
		//add message
		
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





	public java.lang.Long getBenchmarkID() {
		return benchmarkID;
	}



	public void setBenchmarkID(java.lang.Long benchmarkID) {
		this.benchmarkID = benchmarkID;
	}



	public java.lang.String getBenchmarkName() {
		return benchmarkName;
	}



	public void setBenchmarkName(java.lang.String benchmarkName) {
		this.benchmarkName = benchmarkName;
	}



	public List<com.lti.type.Pair> getDynamicAttribute() {
		return dynamicAttribute;
	}



	public void setDynamicAttribute(List<com.lti.type.Pair> dynamicAttribute) {
		this.dynamicAttribute = dynamicAttribute;
	}



	public java.lang.String getName() {
		return name;
	}



	public void setName(java.lang.String name) {
		this.name = name;
	}



	public java.lang.Long getParentID() {
		return parentID;
	}



	public void setParentID(java.lang.Long parentID) {
		this.parentID = parentID;
	}



	public java.lang.String getParentName() {
		return parentName;
	}



	public void setParentName(java.lang.String parentName) {
		this.parentName = parentName;
	}



	public List<com.lti.type.Pair> getStaticAttribute() {
		return staticAttribute;
	}



	public void setStaticAttribute(List<com.lti.type.Pair> staticAttribute) {
		this.staticAttribute = staticAttribute;
	}



	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}



}
