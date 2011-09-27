package com.lti.action.admin.indicator.dailydata;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.IndicatorManager;
import com.lti.service.bo.Indicator;
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
	
	private Long indicatorID;
		
	private IndicatorManager indicatorManager;
	
	private String title;
	
	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setIndicatorManager(IndicatorManager indicatorManager) {
		this.indicatorManager = indicatorManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
		if(indicatorID==null){
			
			addFieldError("indicatorID","Indicator ID is not validate!");
			
		}else{
			
			Indicator g=indicatorManager.get(indicatorID);
			
			if(g==null)addFieldError("ID","ID is not validate!");
			
			title="Indicator : "+g.getSymbol();
		}
		
	}
	@Override
	public String execute() throws Exception {
		
		Date today=new Date();
		
		Interval interval=new Interval(date,today);
		
		dailydatas=indicatorManager.getDailydata(indicatorID, interval, pageSize, startIndex);
		
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


	public Long getIndicatorID() {
		return indicatorID;
	}


	public void setIndicatorID(Long indicatorID) {
		this.indicatorID = indicatorID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
