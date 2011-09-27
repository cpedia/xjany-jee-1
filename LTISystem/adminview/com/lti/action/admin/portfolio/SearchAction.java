package com.lti.action.admin.portfolio;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.type.PaginationSupport;
import com.opensymphony.xwork2.ActionSupport;

public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport portfolios;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private PortfolioManager portfolioManager;

	private String key;


	public PaginationSupport getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(PaginationSupport portfolios) {
		this.portfolios = portfolios;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Portfolio.class);
		
		detachedCriteria.add(Restrictions.like("Name", "%"+key+"%"));
		
		//portfolios=portfolioManager.getAllPortfolios(pageSize, startIndex);
		
		portfolios = portfolioManager.getPortfolios(detachedCriteria, pageSize, startIndex);
		
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
