/**
 * 
 */
package com.lti.action.betagainranking;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.FactorBetaGain;
import com.lti.service.bo.SecurityRanking;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class FactorDetailsTableAction extends ActionSupport implements Action{
	
	
	private String title="Factor Details";
	
	private String url;

	private String symbol;
	
	private List<FactorBetaGain> factorBetaGainList;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<FactorBetaGain> getFactorBetaGainList() {
		return factorBetaGainList;
	}

	public void setFactorBetaGainList(List<FactorBetaGain> factorBetaGainList) {
		this.factorBetaGainList = factorBetaGainList;
	}
	
	public String execute() throws Exception {
		MutualFundManager mutualFundManager = (MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		try{
			factorBetaGainList= mutualFundManager.getFactorDetailsBySymbol(symbol);
			factorBetaGainList.get(factorBetaGainList.size()-1).setFactor("Sum");
		}catch(Exception e){
			return Action.ERROR;
		}
		return  Action.SUCCESS;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
