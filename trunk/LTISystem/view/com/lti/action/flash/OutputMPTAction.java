package com.lti.action.flash;

import java.util.List;

import com.lti.action.Action;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OutputMPTAction extends ActionSupport implements Action{
	private String portfolioID;
	private String resultString;
	private PortfolioManager portfolioManager;
	private String requiredMPT;
	
	
	public String execute() throws Exception
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><MPT><Data>");
		if (portfolioID != null & !portfolioID.equals("")) 
		{
			long id = Long.parseLong(portfolioID);
			List<PortfolioDailyData> resultList=portfolioManager.getDailydatas(id);
			for(int i=0;i<resultList.size();i++)
			{
				PortfolioDailyData p=resultList.get(i);
				
					String mpt = "";
					if(requiredMPT.equals("alpha")){
						mpt = p.getAlpha1()
							+ ","
							+ p.getAlpha3()
							+ ","
							+ p.getAlpha5();
					}
					else if(requiredMPT.equals("beta")){
						mpt =p.getBeta1()
							+ ","
							+ p.getBeta3()
							+ ","
							+ p.getBeta5();
					}else if(requiredMPT.equals("sharpe")){
						mpt =p.getSharpeRatio1()
							+ ","
							+ p.getSharpeRatio3()
							+ ","
							+ p.getSharpeRatio5();
					}
					sb.append("<E d='" + p.getDate() + "' v='" + mpt + "'/>");
				
			}
		}		
		sb.append("</Data></MPT>");
		resultString=sb.toString();
		return Action.SUCCESS;
	}


	public String getPortfolioID() {
		return portfolioID;
	}


	public void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}


	public String getResultString() {
		return resultString;
	}


	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}


	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}


	public String getRequiredMPT() {
		return requiredMPT;
	}


	public void setRequiredMPT(String requiredMPT) {
		this.requiredMPT = requiredMPT;
	}
}
