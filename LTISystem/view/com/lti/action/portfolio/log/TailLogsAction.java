package com.lti.action.portfolio.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Group;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.LongString;
import com.lti.type.PaginationSupport;
import com.lti.util.CustomizeUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class TailLogsAction extends ActionSupport implements Action {

	static Log log = LogFactory.getLog(TailLogsAction.class);
	
	//private PaginationSupport logs;

	private List logs;
	
	private Long portfolioID;
	
	
	private PortfolioManager portfolioManager;
	


	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}


	public void validate(){
		
		
	}
	
	
	@Override
	public String execute() throws Exception {
		
		if(portfolioID==null)return Action.SUCCESS;
		
		logs=portfolioManager.getTailLogs(portfolioID, 2);
		
		return Action.SUCCESS;

	}
	


	public Long getPortfolioID() {
		return portfolioID;
	}


	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}


	public void setLogs(List logs) {
		this.logs = logs;
	}


	public List getLogs() {
		return logs;
	}

}
