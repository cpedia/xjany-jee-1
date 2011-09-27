package com.lti.action.admin.strategy;


import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class AssignAdminPlanToUserAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long planID;
	
	private Long userID;
	
	private String message = "fail";
	
	public String execute() throws Exception{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		strategyManager.assignAdminPlanToUser(planID, userID);
		message = "successful";
		return Action.MESSAGE;
	}


	public Long getPlanID() {
		return planID;
	}


	public void setPlanID(Long planID) {
		this.planID = planID;
	}


	public Long getUserID() {
		return userID;
	}


	public void setUserID(Long userID) {
		this.userID = userID;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
}
