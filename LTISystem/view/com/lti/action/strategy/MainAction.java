package com.lti.action.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.bean.StrategyClassBean;
import com.lti.bean.StrategyItem;
import com.lti.service.StrategyClassManager;
import com.lti.service.UserManager;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);


	private StrategyClassManager strategyClassManager;

	private List<StrategyClassBean> strategyClassBeans;

	private UserManager userManager;

	private Long userID;

	private Boolean realtime=false;

	public Boolean getRealtime() {
		return realtime;
	}

	public void setRealtime(Boolean realtime) {
		this.realtime = realtime;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
		User user = userManager.getLoginUser();
		if (user == null) {
			userID = Configuration.USER_ANONYMOUS;
		} else
			userID = user.getID();
		if (!userID.equals(Configuration.USER_ANONYMOUS)) {
			realtime = true;
		}
	}


	@Override
	public String execute() throws Exception {
		Long portfolioStrategyID = strategyClassManager.get("PORTFOLIO STRATEGY").getID();
		List<StrategyClass> portfolioStrategies = strategyClassManager.getChildClass(portfolioStrategyID);
		Long assetStrategyID = strategyClassManager.get("ASSET STRATEGY").getID();
		List<StrategyClass> assetStrategies = strategyClassManager.getChildClass(assetStrategyID);

		strategyClassBeans = new ArrayList<StrategyClassBean>();
		StrategyClassBean scb;
		// set portfolio strategies
		if (portfolioStrategies != null) {
			for (int i = 0; i < portfolioStrategies.size(); i++) {
				List<StrategyItem> items = new ArrayList<StrategyItem>();
				StrategyClass sc = portfolioStrategies.get(i);
				scb = new StrategyClassBean();
				scb.setName(sc.getName());
				scb.setClassID(sc.getID());
				scb.setItems(items);
				strategyClassBeans.add(scb);
			}
		}

		List<StrategyItem> portfolioStrategyItems = new ArrayList<StrategyItem>();
		// setTopStrategies(portfolioStrategyItems, portfolioStrategyID,0);
		scb = new StrategyClassBean();
		scb.setClassID(portfolioStrategyID);
		scb.setName("Other Portfolio Strategies");
		scb.setItems(portfolioStrategyItems);
		strategyClassBeans.add(scb);

		// set asset strategies
		if (assetStrategies != null) {
			for (int j = 0; j < assetStrategies.size(); j++) {
				List<StrategyItem> items = new ArrayList<StrategyItem>();
				StrategyClass sc = assetStrategies.get(j);
				scb = new StrategyClassBean();
				scb.setClassID(sc.getID());
				scb.setName(sc.getName());
				scb.setItems(items);
				strategyClassBeans.add(scb);
			}
		}

		List<StrategyItem> assetStrategyItems = new ArrayList<StrategyItem>();
		scb = new StrategyClassBean();
		scb.setName("Other Asset Strategies");
		scb.setClassID(assetStrategyID);
		scb.setItems(assetStrategyItems);
		strategyClassBeans.add(scb);
		return Action.SUCCESS;

	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}


	public List<StrategyClassBean> getStrategyClassBeans() {
		return strategyClassBeans;
	}



	public void setStrategyClassBeans(List<StrategyClassBean> strategyClassBeans) {
		this.strategyClassBeans = strategyClassBeans;
	}
	
	

}
