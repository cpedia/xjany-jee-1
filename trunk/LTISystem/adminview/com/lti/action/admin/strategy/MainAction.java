package com.lti.action.admin.strategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.bean.StrategyItem;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport strategies;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private StrategyManager strategyManager;
	
	private UserManager userManager;
	
	public PaginationSupport getStrategies() {
		return strategies;
	}

	public void setStrategies(PaginationSupport strategies) {
		this.strategies = strategies;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		strategies=strategyManager.getStrategies(this.pageSize, this.startIndex);
		
		if(strategies==null||strategies.getItems()==null||strategies.getItems().size()==0){
			
			//out some messages
			
			return Action.ERROR;
		}
		
		
		List<Strategy> strs=strategies.getItems();
		
		for(int i=0;i<strs.size();i++){
			
			Strategy str=strs.get(i);
			
			
			
		}
		
		
		return Action.SUCCESS;

	}

	
	public int getStartIndex() {
	
		return startIndex;
	}


	
	public void setStartIndex(int startIndex) {
	
		this.startIndex = startIndex;
	}


	
	public int getPageSize() {
	
		return pageSize;
	}


	
	public void setPageSize(int pageSize) {
	
		this.pageSize = pageSize;
	}


	

	
	public void setStartIndex(Integer startIndex) {
	
		this.startIndex = startIndex;
	}


	
	public void setPageSize(Integer pageSize) {
	
		this.pageSize = pageSize;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	

}
