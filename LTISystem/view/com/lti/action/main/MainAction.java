package com.lti.action.main;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.bean.StrategyClassBean;
import com.lti.bean.StrategyItem;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private StrategyManager strategyManager;
	
	private PortfolioManager portfolioManager;
	
	private StrategyClassManager strategyClassManager;
	
	private UserManager userManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	
	private CustomizeRegion customizeRegion;
	
	private List<StrategyClassBean> Strategies;
	


	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setTopStrategies(List<StrategyItem> strategies, Long strategyClassID, Long userID)
	{
//		
//		Object[] IDs = strategyClassManager.getClassIDs(strategyClassID);
//		
//		List<PortfolioMPT> mpts = strategyManager.getTopStrategyByMPT(IDs, com.lti.service.bo.PortfolioMPT.LAST_ONE_YEAR, com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO, userID, 5);
//
//		if(mpts == null){
//			return;
//		}
//		for(int i=0; i<mpts.size();i++){			
//
//			StrategyItem si = new StrategyItem();
//			// Strategy s=strategyManager.get(mpts.get(i).getStrategyID());
//			si.setID(mpts.get(i).getStrategyID());
//			si.setName(mpts.get(i).getStrategyName());
//			si.setShowName();
//
//			Portfolio p = portfolioManager.get(mpts.get(i).getPortfolioID());
//			if (p != null&&p.getState()!=Configuration.PORTFOLIO_STATE_TEMP) {
//				si.setPortfolioID(p.getID());
//				si.setPortfolioName(p.getName());
//				si.setPortfolioShortName(p.getState());
//				Date lastValidDate = p.getEndDate();
//				if(lastValidDate != null){
//					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//					si.setLastValidDate(sdf.format(lastValidDate));
//				}
//
//				PortfolioMPT mpt1 = mpts.get(i);
//				PortfolioMPT mpt2 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_THREE_YEAR);
//				PortfolioMPT mpt3 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_FIVE_YEAR);
//
//				if (mpt1 != null) {
//					si.setAR1(FormatUtil.formatPercentage(mpt1.getAR()));
//					si.setBeta1(FormatUtil.formatQuantity(mpt1.getBeta()));
//					si.setSharpeRatio1(FormatUtil.formatPercentage(mpt1.getSharpeRatio()));
//				}
//				if (mpt2 != null) {
//					si.setAR3(FormatUtil.formatPercentage(mpt2.getAR()));
//					si.setBeta3(FormatUtil.formatQuantity(mpt2.getBeta()));
//					si.setSharpeRatio3(FormatUtil.formatPercentage(mpt2.getSharpeRatio()));
//				}
//				if (mpt3 != null) {
//					si.setAR5(FormatUtil.formatPercentage(mpt3.getAR()));
//					si.setBeta5(FormatUtil.formatQuantity(mpt3.getBeta()));
//					si.setSharpeRatio5(FormatUtil.formatPercentage(mpt3.getSharpeRatio()));
//				}
//			}
//			strategies.add(si);
//		}
	}
	

	@Override
	public String execute() throws Exception {
//		StrategyClass sc1=strategyClassManager.get("PORTFOLIO STRATEGY");
//		PortfolioManager pm=ContextHolder.getPortfolioManager();
//		List<PortfolioDailyData> pdds=pm.getDailydatas(890L);
//		Long portfolioStrategyID = sc1.getID();
//		List<StrategyClass>	portfolioStrategies = strategyClassManager.getChildClass(portfolioStrategyID);
//		Long assetStrategyID = strategyClassManager.get("ASSET STRATEGY").getID();
//		List<StrategyClass> assetStrategies = strategyClassManager.getChildClass(assetStrategyID);
//		Long userID;
//		User user = userManager.getLoginUser();
//		if(user == null){
//			userID =  0l;
//		}
//		else
//			userID = user.getID();
//		
//		Strategies = new ArrayList<StrategyClassBean>();
//		if(portfolioStrategies!=null){
//			for(int i=0; i<portfolioStrategies.size();i++){
//				List<StrategyItem> items = new ArrayList<StrategyItem>();
//				StrategyClass sc = portfolioStrategies.get(i);
//				setTopStrategies(items, sc.getID(), userID);
//				if(items.size()>0){
//					StrategyClassBean scb = new StrategyClassBean();
//					scb.setName(sc.getName());
//					scb.setItems(items);
//					Strategies.add(scb);
//				}
//			}
//		}
//		
//		if(assetStrategies!=null){
//			for(int j=0; j<assetStrategies.size(); j++){
//				List<StrategyItem> items = new ArrayList<StrategyItem>();
//				StrategyClass sc = assetStrategies.get(j);
//				setTopStrategies(items, sc.getID(), userID);
//				if(items.size()>0){
//					StrategyClassBean scb = new StrategyClassBean();
//					scb.setName(getText(sc.getName()));
//					scb.setItems(items);
//					Strategies.add(scb);
//				}
//			}
//		}
//		
//		customizeRegion = customizeRegionManager.get(CustomizeUtil.HOME_PAGE);
//		
//		CustomizeUtil.setRegion(customizeRegion, userID);
		
		return Action.SUCCESS;

	}
	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public List<StrategyClassBean> getStrategies() {
		return Strategies;
	}

	public void setStrategies(List<StrategyClassBean> strategies) {
		Strategies = strategies;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

}
