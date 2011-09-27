package com.lti.action.admin.security;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.type.PaginationSupport;
import com.lti.util.LTIDate;
import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private String action;
	
	private PaginationSupport securities;
	
	private PaginationSupport delaySecurities;

	private Integer startIndex;
	
	private Date endDate;
	
	private int year,month,day;
	
	private Integer pageSize;
	
	private SecurityManager securityManager;
	

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		
		securities=securityManager.getSecurities(this.pageSize, this.startIndex);
		
		if(securities!=null&&securities.getItems()!=null){
			for(int i=0;i<securities.getItems().size();i++){
				Security s=(Security) securities.getItems().get(i);
				//s.setStartDate(securityManager.getStartDate(s.getID()));
				//s.setEndDate(securityManager.getEndDate(s.getID()));
			}
			if(action!=null)
			{
			if(action.equals("delay"))
			{
				endDate=LTIDate.getDate(year, month, day);
				for(int i=0;i<securities.getItems().size();){
					Security s=(Security) securities.getItems().get(i);
					if(s.getEndDate().after(this.endDate))
						securities.getItems().remove(i);
					else
						i++;
				}
				action="";
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

	public PaginationSupport getSecurities() {
		return securities;
	}

	public void setSecurities(PaginationSupport securities) {
		this.securities = securities;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public PaginationSupport getDelaySecurities() {
		return delaySecurities;
	}

	public void setDelaySecurities(PaginationSupport delaySecurities) {
		this.delaySecurities = delaySecurities;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}


	

}
