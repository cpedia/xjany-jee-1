package com.lti.action.admin.group;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionSupport;

public class FetchNamesAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(FetchNamesAction.class);
	
	public static String ACTION_TYPE_PORTFOLIO="portfolio";
	
	public static String ACTION_TYPE_STRATEGY="strategy";
	
	public static String ACTION_TYPE_USER="user";
	
	private String action;
	
	private String name;
	
	private List<String> names;
	
	private PortfolioManager portfolioManager;
	
	private UserManager userManager;
	
	private StrategyManager strategyManager;

	public void validate(){
		
		
	}

	@Override
	public String execute() throws Exception {
		
		names=new ArrayList<String>();
		
		if(action==null||name==null)return Action.JSON;
		
		if(action.equalsIgnoreCase(this.ACTION_TYPE_PORTFOLIO)){
			List<Portfolio> portfolios=portfolioManager.getPortfoliosByName(name);
			if(portfolios!=null&&portfolios.size()>=0){
				for(int i=0;i<portfolios.size();i++){
					names.add(portfolios.get(i).getName());
				}
			}
			
			return Action.JSON;
			
		}else if(action.equalsIgnoreCase(this.ACTION_TYPE_STRATEGY)){
			List<Strategy> strategies=strategyManager.getStrategiesByName(name);
			if(strategies!=null&&strategies.size()>=0){
				for(int i=0;i<strategies.size();i++){
					names.add(strategies.get(i).getName());
				}
			}
			
			return Action.JSON;			
		}else if(action.equalsIgnoreCase(this.ACTION_TYPE_USER)){
			DetachedCriteria detachedCriteria=DetachedCriteria.forClass(User.class);
			detachedCriteria.add(Restrictions.like("UserName", "%"+name+"%"));
			List<User> users=userManager.getUsers(detachedCriteria);
			if(users!=null&&users.size()>=0){
				for(int i=0;i<users.size();i++){
					names.add(users.get(i).getUserName());
				}
			}
			
			return Action.JSON;			
		}
		
		return Action.JSON;

	}

	
	
	
	public String getAction() {
	
		return action;
	}

	
	public void setAction(String action) {
	
		this.action = action;
	}


	
	public String getName() {
	
		return name;
	}

	
	public void setName(String name) {
	
		this.name = name;
	}

	

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
	

}
