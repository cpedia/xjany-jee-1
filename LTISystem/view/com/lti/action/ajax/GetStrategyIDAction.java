package com.lti.action.ajax;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.opensymphony.xwork2.ActionSupport;

public class GetStrategyIDAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private StrategyManager strategyManager;
	
	private String resultString;
	
	private String strategyName;

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}
	
	public String execute(){
		Strategy strategy = strategyManager.get(strategyName);
		Long strategyID;
		if(strategy == null)
			resultString = null;
		else
		{
			strategyID = strategy.getID();
			resultString = strategyID.toString();
		}
		return Action.SUCCESS;
	}
}
