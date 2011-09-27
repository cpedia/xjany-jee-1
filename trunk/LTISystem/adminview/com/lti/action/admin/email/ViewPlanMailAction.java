package com.lti.action.admin.email;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.system.ContextHolder;

public class ViewPlanMailAction {
	private Long planID;
	
	public Long getPlanID() {
		return planID;
	}

	public void setPlanID(Long planID) {
		this.planID = planID;
	}
	
	private Strategy plan;
	

	public Strategy getPlan() {
		return plan;
	}

	public void setPlan(Strategy plan) {
		this.plan = plan;
	}

	public String execute(){
		StrategyManager sm=ContextHolder.getStrategyManager();
		plan=sm.get(planID);
		return Action.SUCCESS;
	}
}
