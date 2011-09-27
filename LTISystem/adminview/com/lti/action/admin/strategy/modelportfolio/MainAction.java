package com.lti.action.admin.strategy.modelportfolio;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<Portfolio> portfolios;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private Long strategyID;
	
	private StrategyManager strategyManager;


	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
		if(this.strategyID==null){
			addFieldError("strategyID","Strategy ID is not validate!");
		}else{
			Strategy strategy=strategyManager.get(strategyID);
			
			if(strategy==null)addFieldError("strategyID","Strategy ID is not validate!");
		}
		

	}

	@Override
	public String execute() throws Exception {
		
		portfolios=strategyManager.getModelPortfolios(strategyID);
		
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

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}


	



}
