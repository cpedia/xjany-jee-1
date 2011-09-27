package com.lti.action.admin.indicator;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.IndicatorManager;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport indicators;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private IndicatorManager indicatorManager;
	

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		indicators=indicatorManager.getList(this.pageSize, this.startIndex);
		
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

	public PaginationSupport getIndicators() {
		return indicators;
	}

	public void setIndicators(PaginationSupport indicators) {
		this.indicators = indicators;
	}

	public void setIndicatorManager(IndicatorManager indicatorManager) {
		this.indicatorManager = indicatorManager;
	}


	

}
