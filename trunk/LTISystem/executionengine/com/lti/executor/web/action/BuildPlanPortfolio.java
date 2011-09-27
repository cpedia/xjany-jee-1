/**
 * 
 */
package com.lti.executor.web.action;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.jobscheduler.DailyExecutionJob;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.HoldingInf;
import com.lti.util.StringUtil;

/**
 * @author ccd
 *
 */
public class BuildPlanPortfolio extends BasePage {
	
	public StrategyManager strategyManager = ContextHolder.getStrategyManager();
	public PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
	public UserManager userManager = ContextHolder.getUserManager();
	public User admin;
	
	public BuildPlanPortfolio(){
		admin = userManager.get("Admin");
	}
	
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * 
	 * @param planID
	 * @param portfolioID
	 * @param type type为0时表示SAA,type为1时表示TAA
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public Long buildOnePortfolio(Long planID, String type) throws CloneNotSupportedException{
		Long portfolioID = null;
		List<Portfolio> modelPortfolioList = portfolioManager.getModelPortfolios(planID);
		if(modelPortfolioList != null && modelPortfolioList.size() > 0){
			for(Portfolio p: modelPortfolioList){
				if((type.equals("TAA") && p.getName().endsWith("Tactical Asset Allocation Moderate")) || (type.equals("SAA") && p.getName().endsWith("Strategic Asset Allocation Moderate"))){
					portfolioID = p.getID();
					break;
				}
			}
		}
		if(portfolioID == null)
			return null;
		Portfolio portfolio = portfolioManager.get(portfolioID);
		Portfolio newPortfolio = portfolio.clone();
		newPortfolio.setName(portfolio.getName() + "_GENERATE_DATA_OBJECT");
		newPortfolio.setUserID(admin.getID());
		newPortfolio.setUserName(admin.getUserName());
		if(newPortfolio.getMainStrategyID()!=null){
			Strategy mainStrategy=strategyManager.get(newPortfolio.getMainStrategyID());
			if(mainStrategy!=null&&mainStrategy.getUserID()!=null&&mainStrategy.getUserID().equals(admin.getUserName())){
				
			}else{
				newPortfolio.setMainStrategyID(null);
			}
		}
		portfolioManager.save(newPortfolio);
		PortfolioInf pif = portfolioManager.getPortfolioInf(portfolioID, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		PortfolioInf npif = new PortfolioInf();
		HoldingInf nhi = null;
		Long newPortfolioID = newPortfolio.getID();
		if (pif != null && pif.getHolding() != null) {
			nhi = pif.getHolding().clone();
		} else {
			nhi = new HoldingInf(newPortfolioID, 10000.0, newPortfolio.getStartingDate());
		}
		nhi.setPortfolioID(newPortfolioID);
		nhi.setPortfolioName(newPortfolio.getName());
		nhi.setCurrentDate(newPortfolio.getStartingDate());
		
		npif.setHolding(nhi);
		npif.setPortfolioID(newPortfolioID);
		npif.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		portfolioManager.savePortfolioInf(npif);
		return newPortfolioID;
	}
	
	public void monitorPortfolios(List<Long> portfolioIDList){
		String IDStr = "";
		for(Long portfolioID : portfolioIDList){
			if(!IDStr.equals(""))
				IDStr +=",";
			IDStr += portfolioID;
		}
		String url = "http://127.0.0.1:8081/Dailystart?filter=PortfolioIDFilter&ids=" + IDStr + "&forceMonitor=true&mode=1";
		try {
			DailyExecutionJob.sendRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public String execute(){
		StringBuffer sb=new StringBuffer();
		try{
			FileReader fr = new FileReader(ContextHolder.getServletPath()+"/customizeportfolio.csv");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			List<Long> portfolioIDList = new ArrayList<Long>();
			while(line != null){
				String[] pairs = line.split(",");
				Long planID = null;
				try{
					planID = Long.parseLong(pairs[1]);
				}catch(Exception e){}
				if(planID != null){
					try{
						Long portfolioID = buildOnePortfolio(planID, pairs[0]);
						if(portfolioID == null)
							sb.append(planID + ",0");
						else{
							sb.append(planID + "," + portfolioID);
							portfolioIDList.add(portfolioID);
						}
					}catch(Exception e){
						sb.append(planID + ",0");
					}
					sb.append("<br>\r\n");
				}
				line = br.readLine();
			}
			monitorPortfolios(portfolioIDList);
			sb.append("<br>\r\n");
			sb.append("<br>\r\n");
		}catch(Exception e){
			info = StringUtil.getStackTraceString(e);
		}
		info = sb.toString();
		return "info.ftl";
	}
}
