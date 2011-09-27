package com.lti.action.security.dailydata;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Security;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.type.Interval;
import com.lti.type.PaginationSupport;
import com.lti.util.CustomizeUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;


public class SearchAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport dailydatas;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private Long securityID;
		
	private SecurityManager securityManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private UserManager	userManager;
	
	private CustomizeRegion customizeRegion;
	
	private Date date;
	
	private String title;
	
	private String symbol;
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public PaginationSupport getDailydatas() {
		return dailydatas;
	}

	public void setDailydatas(PaginationSupport dailydatas) {
		this.dailydatas = dailydatas;
	}

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

	public Long getSecurityID() {
		return securityID;
	}

	public void setSecurityID(Long securityID) {
		this.securityID = securityID;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
		if(securityID==null){
			
			addFieldError("securityID","Security ID is not validate!");
			
		}else{
			
			Security g=securityManager.get(securityID);
			
			if(g==null)addFieldError("ID","ID is not validate!");
			
			title="Security : "+g.getSymbol();
			
			symbol=g.getSymbol();
		}
		if (date == null) {
			date = new Date();
			date = LTIDate.clearHMSM(date);
		}
	}

	@Override
	public String execute() throws Exception {
		customizeRegion = customizeRegionManager.get("Security Daily Data Main Page");
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null){
			userID = 0l;
		}
		else{
			userID = user.getID();
		}
		CustomizeUtil.setRegion(customizeRegion, userID);
		
		Date today=new Date();
		
		Interval interval=new Interval(date,today);
		
		dailydatas=securityManager.getDailyDatas(securityID, interval.getStartDate(),interval.getEndDate(), pageSize, startIndex);
		
		dailydatas.addParameter("date", date);
		
		return Action.SUCCESS;

	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
