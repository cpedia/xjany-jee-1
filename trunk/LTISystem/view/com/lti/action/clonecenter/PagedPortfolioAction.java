package com.lti.action.clonecenter;


import com.lti.action.Action;
import com.lti.system.ContextHolder;

public class PagedPortfolioAction {
	
	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public com.lti.type.PaginationSupport getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(com.lti.type.PaginationSupport portfolios) {
		this.portfolios = portfolios;
	}

	private Integer startIndex=0;
	private Integer pageSize=10;
	private com.lti.type.PaginationSupport portfolios;
	@Deprecated
	public String execute(){
//		portfolios=ContextHolder.getPortfolioManager().getAllPortfolios(pageSize, startIndex);
		return Action.SUCCESS;
	}
}
