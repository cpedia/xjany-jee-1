package com.lti.action.admin.security.dailydata;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.type.Interval;
import com.lti.type.PaginationSupport;
import com.opensymphony.xwork2.ActionSupport;


public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport dailydatas;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private Long securityID;
		
	private SecurityManager securityManager;
	
	private Date date;
	
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
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
		}
		
	}
	@Override
	public String execute() throws Exception {
		
		Date today=new Date();
		
		Interval interval=new Interval(date,today);
		
		dailydatas=securityManager.getDailyDatas(securityID, interval.getStartDate(),interval.getEndDate(), pageSize, startIndex);
		
		dailydatas.addParameter("date", date);
		
		return Action.SUCCESS;

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

}
