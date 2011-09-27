package com.lti.action.admin.strategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.bean.StrategyItem;

public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport strategies;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private String key;
	
	private StrategyManager strategyManager;
	
	
	
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
		
		strategies=strategyManager.getStrategiesByName(key,this.pageSize, this.startIndex);

		if(strategies==null||strategies.getItems()==null||strategies.getItems().size()==0){
			
			//output some messages
			
			return Action.ERROR;
		}
		
		
		List<StrategyItem> sis=new ArrayList<StrategyItem>();
		
		List<Strategy> strs=strategies.getItems();
		
		for(int i=0;i<strs.size();i++){
			
			Strategy str=strs.get(i);
			
			StrategyItem si=new StrategyItem();
			
			si.setID(str.getID());
			
			si.setName(str.getName());
			
			List<Portfolio> portfolios=strategyManager.getModelPortfolios(str.getID());
			
			if(portfolios!=null&&portfolios.size()>0){
				
				//the portfolio must has the best performance
				//but here just get the first item of the strategy
				
				Portfolio portfolio = portfolios.iterator().next();
				
				si.setPortfolioID(portfolio.getID());
				
				si.setPortfolioName(portfolio.getName());
				
				si.setAR1("0.0");
				si.setAR3("0.0");
				si.setAR5("0.0");
				
				si.setBeta1("0.0");
				si.setBeta3("0.0");
				si.setBeta5("0.0");
				
				si.setSharpeRatio1("0.0");
				si.setSharpeRatio3("0.0");
				si.setSharpeRatio5("0.0");
			}

			sis.add(si);
			
		}
		
		strategies.setItems(sis);
		
		
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	

}
