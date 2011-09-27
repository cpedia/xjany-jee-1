/**
 * 
 */
package com.lti.action.betagainranking;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.action.betagain.GetFactorListAction;
import com.lti.service.AssetClassManager;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.SecurityRanking;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * get the historical ranking info accoring to the symbol of the fund
 * @author CCD
 *
 */
public class HistoricalRankingTableAction extends ActionSupport implements Action{
	

	private String symbol;
	
	private int interval;
	
	private List<SecurityRanking> securityRankingList;
	/**
	 * according to the Symbol,return the historical ranking list
	 * interval=3 return the short-term ranking
	 * interval=12 return the mid-term ranking
	 * interval=36 return the long-term ranking
	 */
	public void validate()
	{
		if(symbol==null)
			symbol="KMKNX";
	}
	public String execute() throws Exception {
		MutualFundManager mutualFundManager = (MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		securityRankingList = mutualFundManager.getHistoricalRankingBySymbol(symbol,interval);
		return  Action.SUCCESS;
	}
	
	

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public  List<SecurityRanking> getSecurityRankingList() {
		return securityRankingList;
	}

	public void setSecurityRankingList(List<SecurityRanking> securityRankingList) {
		this.securityRankingList = securityRankingList;
	}
	
	public static void  main(String[] args) throws Exception
	{
		HistoricalRankingTableAction ghr=new HistoricalRankingTableAction();
		ghr.execute();
		System.out.println(ghr.getSecurityRankingList().toString());
		
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
}
