package com.lti.action.admin.stock;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.action.Action;
import com.lti.action.admin.quartz.SaveAction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.service.FinancialStatementManager;
import com.lti.util.LTIDownLoader;

public class UpdateConfigurationAction extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	private String action;
	
	private String updateMode;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}

	public void validate(){
		if(this.action==null||this.action.equals(""))this.action=ACTION_VIEW;
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			if(!(updateMode.equalsIgnoreCase("weekly")||updateMode.equalsIgnoreCase("monthly")||updateMode.equalsIgnoreCase("quarterly"))){
				addFieldError("updateMode","updateMode is not validate!");
				return;
			}
		}
	}
	
	public String execute(){

		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			Configuration.set("UPDATE_MODE", updateMode);
			this.action = ACTION_VIEW;
			return Action.SUCCESS;
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			return Action.SUCCESS;			
		}
		
		return Action.ERROR;
	}
}
