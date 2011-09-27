package com.lti.action.portfolio.screening;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.service.CustomizeRegionManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.StrategyClass;

import com.lti.action.Action;
import com.lti.bean.SortTypeBean;
import com.opensymphony.xwork2.ActionSupport;

public class ScreeningAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Name;
	
	private String StartingDateStr;
	
	private String EndDateStr;
	
	private Date StartingDate;
	
	private Date EndDate;
	
	private List<StrategyClass> ClassList;
	
	private List<StrategyClass> Classes;
	
	private List<SortTypeBean> SortList;
	
	private Integer Sort;
	
	private PortfolioManager portfolioManager;
	
	private StrategyClassManager strategyClassManager;
	
	private StrategyManager strategyManager;
	
	private UserManager userManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getStartingDateStr() {
		return StartingDateStr;
	}

	public void setStartingDateStr(String startingDateStr) {
		StartingDateStr = startingDateStr;
	}

	public String getEndDateStr() {
		return EndDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		EndDateStr = endDateStr;
	}

	public Date getStartingDate() {
		return StartingDate;
	}

	public void setStartingDate(Date startingDate) {
		StartingDate = startingDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public List<StrategyClass> getClassList() {
		return ClassList;
	}

	public void setClassList() {
		ClassList = new ArrayList<StrategyClass>();
		
	}

	public List<StrategyClass> getClasses() {
		return Classes;
	}

	public void setClasses(List<StrategyClass> classes) {
		Classes = classes;
	}

	public List<SortTypeBean> getSortList() {
		return SortList;
	}

	public void setSortList(List<SortTypeBean> sortList) {
		SortList = sortList;
	}

	public Integer getSort() {
		return Sort;
	}

	public void setSort(Integer sort) {
		Sort = sort;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	@Override
	public void validate() {
		
	}
	
	public String screeningmain(){
		return Action.SUCCESS;
	}

}
