/**
 * 
 */
package com.lti.action.betagainranking;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 * unfinished
 */
public class GetCurrentRankingAction extends ActionSupport implements Action{
	
	private String symbol;
	
	private Integer ranking;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		symbol = symbol;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		ranking = ranking;
	}
	
	
	public String execute() throws Exception {
		MutualFundManager mutualFundManager = (MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		ranking=mutualFundManager.getCurrentRankingBySymbol(symbol);
		return Action.SUCCESS;
	}
	
}
