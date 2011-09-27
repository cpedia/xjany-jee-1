package com.lti.action.admin.portfolio;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport portfolios;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private PortfolioManager portfolioManager;

	private UserManager userManager;
	
	private Integer portfolioMode;

	public Integer getPortfolioMode() {
		return portfolioMode;
	}

	public void setPortfolioMode(Integer portfolioMode) {
		this.portfolioMode = portfolioMode;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

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
		
		portfolios = portfolioManager.getPortfolios(DetachedCriteria.forClass(Portfolio.class),pageSize, startIndex);
		
		if(portfolios != null && portfolios.getItems().size()!=0){
			for(int i=0;i< portfolios.getItems().size(); i++){
				Portfolio p= (Portfolio) portfolios.getItems().get(i);
				if(p.getUserName()==null&&p.getUserID()!=null){
					p.setUserName(userManager.get(p.getUserID()).getUserName());
					portfolioManager.update(p);
				}
				
			}
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


	

}
