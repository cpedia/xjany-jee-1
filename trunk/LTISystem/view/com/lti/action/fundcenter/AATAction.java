package com.lti.action.fundcenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.action.TemplateAction;
import com.lti.type.TimeUnit;
import com.lti.util.LTIDate;
import com.lti.util.LTIFactorManager;
import com.lti.service.bo.Security;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIFactorManager.Factor;
import com.opensymphony.xwork2.ActionSupport;

public class AATAction extends TemplateAction implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean IsRAA;
	
	private Boolean IsResult;
	
	private String symbol;
	
	private List<String> indexes;
	
	private List<Double> lowers;
	
	private List<Double> uppers;
	
	private String startDate;
	
	private String endDate;
	
	private Integer interval;
	
	private int IsFirst = 1;

	@Override
	public void validate() {
		super.validate();
		if(symbol == null){
			addActionError("No symbol was set! Use the default setting");
			IsResult = false; //not set the IsRAA to use the default setting;
			return;
		}
	}

	@Override
	public String execute() throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		/*set indexes and their default value*/
		LTIFactorManager factorManager = LTIFactorManager.getInstance("RAA");
		List<Factor> factors = factorManager.getFactorsForSecurity(symbol);
		if(factors != null && factors.size() > 0){
			indexes = new ArrayList<String>();
			lowers = new ArrayList<Double>();
			uppers = new ArrayList<Double>();
			for(int i = 0; i < factors.size(); i++){
				indexes.add(factors.get(i).getName());
				lowers.add(0.0);
				uppers.add(1.0);
			}
			if(factors.size()>2)
				interval=30;
			else
				interval=20;
		}
		//get the default startDate
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security security = securityManager.getBySymbol(symbol);
		Date eDate = security.getEndDate();
		endDate = sdf.format(eDate);
		Date sDate = LTIDate.getLastYear(eDate);
		IsRAA = true;
		IsResult = false;
		IsFirst = 1;
		Date earliestDate = security.getStartDate();
		earliestDate = LTIDate.getNewNYSETradingDay(earliestDate,interval+2);
		if(sDate.before(earliestDate))
			sDate = earliestDate;
		startDate = sdf.format(sDate);
		return Action.SUCCESS;
	}


	public Boolean getIsRAA() {
		return IsRAA;
	}

	public void setIsRAA(Boolean isRAA) {
		IsRAA = isRAA;
	}

	public Boolean getIsResult() {
		return IsResult;
	}

	public void setIsResult(Boolean isResult) {
		IsResult = isResult;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<String> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<String> indexes) {
		this.indexes = indexes;
	}

	public List<Double> getLowers() {
		return lowers;
	}

	public void setLowers(List<Double> lowers) {
		this.lowers = lowers;
	}

	public List<Double> getUppers() {
		return uppers;
	}

	public void setUppers(List<Double> uppers) {
		this.uppers = uppers;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public int getIsFirst() {
		return IsFirst;
	}

	public void setIsFirst(int isFirst) {
		this.IsFirst = isFirst;
	}
	
}
