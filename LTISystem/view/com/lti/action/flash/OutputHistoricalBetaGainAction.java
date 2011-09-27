/**
 * 
 */
package com.lti.action.flash;

import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.bean.SortTypeBean;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.SecurityRanking;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class OutputHistoricalBetaGainAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private String symbol;
	
	private String xml;
	
	private int interval;
	
	private List<SecurityRanking> securityRankingList;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(symbol==null)
			symbol="ILGIX";
	}
	
	public String execute(){
		if(symbol==null)
			symbol="ILGIX";
		MutualFundManager mutualFundManager = (MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		securityRankingList=mutualFundManager.getHistoricalRankingBySymbol(symbol,interval);
		xml=getXML();
		return Action.SUCCESS;
	}
	
	public String getXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<BetaGain><Data>");
		if(securityRankingList == null || securityRankingList.size() == 0)
		{
			sb.append("</Data></BetaGain>");
			return sb.toString();
		}
		int i;
		SecurityRanking securityRanking;
		for(i=0;i<securityRankingList.size();++i)
		{
			securityRanking=securityRankingList.get(i);
			sb.append("<E d='" + securityRanking.getEndDate() + "' v='" + securityRanking.getBetaGain()+"'/>");
		}
		sb.append("</Data></BetaGain>");
		return sb.toString();
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public List<SecurityRanking> getSecurityRankingList() {
		return securityRankingList;
	}

	public void setSecurityRankingList(List<SecurityRanking> securityRankingList) {
		this.securityRankingList = securityRankingList;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
