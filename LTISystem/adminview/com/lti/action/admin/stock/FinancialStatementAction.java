package com.lti.action.admin.stock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.FinanStateJob;
import com.opensymphony.xwork2.ActionSupport;

public class FinancialStatementAction extends ActionSupport implements Action {
	

	static Log log = LogFactory.getLog(FinancialStatementAction.class);
	
	private String action;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String execute()throws Exception {
		if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			return Action.SUCCESS;			
		}
		return Action.ERROR;
	}
}
