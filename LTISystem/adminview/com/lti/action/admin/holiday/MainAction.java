package com.lti.action.admin.holiday;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.HolidayManager;
import com.lti.service.bo.Holiday;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport holidays;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private HolidayManager holidayManager;
	
	private Date date;


	public PaginationSupport getHolidays() {
		return holidays;
	}

	public void setHolidays(PaginationSupport holidays) {
		this.holidays = holidays;
	}

	public void setHolidayManager(HolidayManager holidayManager) {
		this.holidayManager = holidayManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		
		
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Holiday.class);
		
		if(date!=null)detachedCriteria.add(Restrictions.ge("Date", date));
		
		detachedCriteria.addOrder(Order.asc("Date"));
		
		holidays=holidayManager.findPage(detachedCriteria, pageSize, startIndex);
		
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	

}
