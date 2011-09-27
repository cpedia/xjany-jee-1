package com.lti.action.strategy;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.util.CustomizeUtil;
import com.lti.util.EscapeUnescapeUtil;
import com.lti.util.FormatUtil;
import com.lti.util.StrategyUtil;
import com.lti.util.StringUtil;
import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.bean.PortfolioItem;
import com.lti.bean.StrategyItem;

public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private List<StrategyItem> strategyItems;
	
	private Boolean realtime;
	
	private String name;
	
	private String categories;
	
	private StrategyManager strategyManager;
	
	private UserManager userManager;
	
	private PortfolioManager portfolioManager;
	
	private GroupManager groupManager;
	
	private Map session;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private StrategyClassManager strategyClassManager;
	
	private CustomizeRegion customizeRegion;
	
	private String resultString;

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public String search() throws Exception {
		User user = userManager.getLoginUser();
		Long userID;
		if(user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		name = StringUtil.sortCategories(name);
		List<Strategy> strategyList = strategyManager.searchStrategiesByCategory(name, userID);
		strategyItems = translateStrToStrItem(strategyList, userID);
		
		customizeRegion = customizeRegionManager.get(CustomizeUtil.STRATEGY_SEARCHRESULT);
		CustomizeUtil.setRegion(customizeRegion, userID);
		ActionContext ac = ActionContext.getContext();
		session = ac.getSession();
		session.put("strategies", strategyItems);
		session.put("name", name);
		session.put("categories", categories);
		return Action.SUCCESS;

	}
	
	private List<StrategyItem> translateStrToStrItem(List<Strategy> strategyList, Long userID){
		List<StrategyItem> strategies = new ArrayList<StrategyItem>();
		if(strategyList != null){
			for(int i = 0; i < strategyList.size(); i++){
				Strategy s =(Strategy) strategyList.get(i);
				StrategyItem si = new StrategyItem();
				si.setID(s.getID());
				si.setName(s.getName());
				si.setShowName();
				if(s.getStrategyClassID() != null){
					StrategyClass sc = strategyClassManager.get(s.getStrategyClassID());
					if(sc != null){
						si.setStrategyClass(sc.getName());
					}
					else
						si.setStrategyClass("UNKNOWN");
				}
				si.setStyles(s.getCategories());
				List<PortfolioMPT> portfolios;
				if(realtime == true)
					portfolios = strategyManager.getTopPortfolioInStrategy(s.getID(), PortfolioMPT.LAST_ONE_YEAR, PortfolioMPT.SORT_BY_SHARPERATIO, userID, 1);
				else
					portfolios = strategyManager.getTopPortfolioInStrategy(s.getID(), PortfolioMPT.DELAY_LAST_ONE_YEAR, PortfolioMPT.SORT_BY_SHARPERATIO, userID, 1);
				if(portfolios != null && portfolios.size() != 0){
					PortfolioMPT pm = portfolios.get(0);
					Portfolio p = portfolioManager.get(pm.getPortfolioID());
					if (p != null) {
						
						Date lastValidDate = p.getEndDate();
						if(lastValidDate != null){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							si.setLastValidDate(sdf.format(lastValidDate));
						}
						Date lastTransactionDate = portfolioManager.getTransactionLatestDate(p.getID());
						if(lastTransactionDate != null){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							si.setLastTransactionDate(sdf.format(lastTransactionDate));
						}
						si.setPortfolioID(p.getID());
						si.setPortfolioName(p.getName());
//						if(p.getState() != null && p.getState() == Configuration.PORTFOLIO_STATE_ALIVE){
//							si.setPortfolioState("Live");
//							si.setPortfolioShortName(Configuration.PORTFOLIO_STATE_ALIVE);
//						}
//						else
//						{
//							si.setPortfolioState("Not Live");
//							si.setPortfolioShortName(0);
//						}
						PortfolioMPT mpt1 = pm;
						PortfolioMPT mpt2;
						PortfolioMPT mpt3;
						if(realtime == true){
							mpt2 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_THREE_YEAR);
							mpt3 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_FIVE_YEAR);
						}
						else
						{
							mpt2 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.DELAY_LAST_THREE_YEAR);
							mpt3 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.DELAY_LAST_FIVE_YEAR);
						}
						
						if (mpt1 != null) {
							si.setAR1(FormatUtil.formatPercentage(mpt1.getAR()));
							si.setBeta1(FormatUtil.formatQuantity(mpt1.getBeta()));
							si.setSharpeRatio1(FormatUtil.formatPercentage(mpt1.getSharpeRatio()));
						}
						if (mpt2 != null) {
							si.setAR3(FormatUtil.formatPercentage(mpt2.getAR()));
							si.setBeta3(FormatUtil.formatQuantity(mpt2.getBeta()));
							si.setSharpeRatio3(FormatUtil.formatPercentage(mpt2.getSharpeRatio()));
						}
						if (mpt3 != null) {
							si.setAR5(FormatUtil.formatPercentage(mpt3.getAR()));
							si.setBeta5(FormatUtil.formatQuantity(mpt3.getBeta()));
							si.setSharpeRatio5(FormatUtil.formatPercentage(mpt3.getSharpeRatio()));
						}
					}
				}
				strategies.add(si);
			}
		}
		return strategies;
	}
	
	
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		User user = userManager.getLoginUser();
		Long userID;
		if(user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		Object[] groupIDs = groupManager.getGroupIDs(userID);
		Long roleID=groupManager.getRoleByName(Configuration.ROLE_PORTFOLIO_REALTIME).getID();
	    realtime = groupManager.hasrole(roleID, groupIDs, Configuration.RESOURCE_TYPE_ROLE);
	}

	public String nameSearch(){
		User user = userManager.getLoginUser();
		Long userID;
		if(user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		//name = StringUtil.sortCategories(name);
		name = EscapeUnescapeUtil.unescape(name);
		List<Strategy> strategyList = strategyManager.searchStrategiesByName(name, userID);
		strategyItems = translateStrToStrItem(strategyList, userID);
		
		/*customizeRegion = customizeRegionManager.get(CustomizeUtil.STRATEGY_SEARCHRESULT);
		CustomizeUtil.setRegion(customizeRegion, userID);
		ActionContext ac = ActionContext.getContext();
		session = ac.getSession();
		session.put("strategies", strategyItems);
		session.put("name", name);
		session.put("categories", categories);*/
		return Action.SUCCESS;
	}
	
	public String categorySearch(){
		User user = userManager.getLoginUser();
		Long userID;
		if(user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		categories = EscapeUnescapeUtil.unescape(categories);
		List<Strategy> strategyList = strategyManager.searchStrategiesByCategory(categories, userID);
		strategyItems = translateStrToStrItem(strategyList, userID);
		
		/*customizeRegion = customizeRegionManager.get(CustomizeUtil.STRATEGY_SEARCHRESULT);
		CustomizeUtil.setRegion(customizeRegion, userID);
		ActionContext ac = ActionContext.getContext();
		session = ac.getSession();
		session.put("strategies", strategyItems);
		session.put("name", name);
		session.put("categories", categories);*/
		return Action.SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String searchResult(){
		session =(Map) ActionContext.getContext().get(ActionContext.SESSION);
		name = (String) session.get("name");
		categories = (String) session.get("categories");
		strategyItems = (List<StrategyItem>) session.get("strategies");
		resultString = StrategyUtil.generateXML(strategyItems);
		System.out.println(resultString);
		return Action.SUCCESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<StrategyItem> getStrategyItems() {
		return strategyItems;
	}

	public void setStrategyItems(List<StrategyItem> strategyItems) {
		this.strategyItems = strategyItems;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public Boolean getRealtime() {
		return realtime;
	}

	public void setRealtime(Boolean realtime) {
		this.realtime = realtime;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}	

}
