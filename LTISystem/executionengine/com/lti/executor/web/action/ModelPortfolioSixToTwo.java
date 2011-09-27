/**
 * 
 */
package com.lti.executor.web.action;

import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author CCD
 *
 */
public class ModelPortfolioSixToTwo extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * 先检查是否是moderate，是返回false
	 * 再检查是否有email alert，是返回false
	 * 返回true
	 * @param portfolio
	 * @return
	 */
	public boolean canDelete(Portfolio portfolio) {
		UserManager userManager = ContextHolder.getUserManager();
		String portfolioName = portfolio.getName();
		if(portfolioName.endsWith("Tactical Asset Allocation Moderate") || portfolioName.endsWith("Strategic Asset Allocation Moderate"))
			return false;
		int count = userManager.getEmailNotificationsByPortfolioID(portfolio.getID());
		if(count > 0)
			return false;
		if(portfolioName.endsWith("Tactical Asset Allocation Growth") || portfolioName.endsWith("Tactical Asset Allocation Conservative") || portfolioName.endsWith("Strategic Asset Allocation Growth") || portfolioName.endsWith("Strategic Asset Allocation Conservative"))
			return true;
		return false;
	}

	public String executeOnePlan(Long planID) {
		String result = "";
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<Portfolio> portfolioList = strategyManager.getModelPortfolios(planID);
		if(portfolioList != null){
			for(Portfolio portfolio : portfolioList){
				if(canDelete(portfolio)){
					try{
						portfolioManager.remove(portfolio.getID());
						result += planID + "," + portfolio.getName() + ",success";
					}catch(Exception e){
						result += planID + "," + portfolio.getName() + ",fail";
					}
					result += "<br>\r\n";
				}
			}
		}
		return result;
	}
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		StringBuffer sb=new StringBuffer();
		try{
			List<Strategy> planList = strategyManager.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
			if(planList != null){
				for(Strategy plan : planList) {
					sb.append(executeOnePlan(plan.getID()));
				}
			}
		}catch(Exception e){
			
		}
		info = sb.toString();
		return "info.ftl";
	}
	
}
