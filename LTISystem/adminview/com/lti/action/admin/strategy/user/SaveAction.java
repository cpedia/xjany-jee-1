package com.lti.action.admin.strategy.user;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StrategyManager strategyManager;
	
	private UserManager userManager;
	
	private Strategy strategy;
	
	private String userName;
	
	private String oldName;
	
	private Long strategyID;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(strategyID == null)
		{
			addActionError("No Such Strategy");
			return;
		}
	}

	public String view(){
		strategy = strategyManager.get(strategyID);
		if(strategy.getUserID() != null){
			oldName = userManager.get(strategy.getUserID()).getUserName();
		}
		return Action.SUCCESS;
	}
	
	public String save(){
		if(userName == null){
			addActionMessage("No Changed!");
			return Action.SUCCESS;
		}
		else if(userName.equals(oldName)){
			addActionMessage("No Changed!");
			return Action.SUCCESS;
		}
		User user = userManager.get(userName);
		if(user == null){
			addActionError("No Such User!");
			return Action.SUCCESS;
		}
		strategy = strategyManager.get(strategyID);
		strategy.setUserID(user.getID());
		try {
			strategyManager.update(strategy);
			oldName = userName;
			userName = "";
			addActionMessage("Update Successfully!");
		} catch (Exception e) {
			// TODO: handle exception
			addActionError("Can't update the user!");
		}
		return Action.SUCCESS;	
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

}
