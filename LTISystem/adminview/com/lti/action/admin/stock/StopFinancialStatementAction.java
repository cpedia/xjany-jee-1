package com.lti.action.admin.stock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.FinanStateJob;
import com.lti.jobscheduler.FinanYearlyStateJob;
import com.opensymphony.xwork2.ActionSupport;

public class StopFinancialStatementAction extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(StopFinancialStatementAction.class);
	
	private String action;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String execute()throws Exception {
		if(action.equals("stop")){
			try {
				FinanStateJob  fs = new FinanStateJob();
				fs.cancel();
				
			} catch (RuntimeException e) {
				return Action.ERROR;
			}
			return Action.SUCCESS;
		}
		else if(action.equals("stopYearly")){
			try {
				FinanYearlyStateJob  fs = new FinanYearlyStateJob();
				fs.cancelYearly();
				
			} catch (RuntimeException e) {
				return Action.ERROR;
			}
			return Action.SUCCESS;
		}
		else return Action.ERROR;
		
	}
}

