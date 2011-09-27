package com.lti.service.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import com.lti.service.bo.base.BasePortfolioMPT;

public class PortfolioMPT extends BasePortfolioMPT implements Serializable{
	public final static int DELAY_LAST_ONE_YEAR=-7;
	public final static int DELAY_LAST_THREE_YEAR=-9;
	public final static int DELAY_LAST_FIVE_YEAR=-11;
	public final static int DELAY_FROM_STARTDATE_TO_ENDDATE=-13;
	
	public final static int DELAY_FROM_FOLLOWDATE_TO_ENDDATE = -10000; 
	public final static int FROM_FOLLOWDATE_TO_ENDATE = 10000;
	
	public final static int LAST_ONE_YEAR=-1;
	public final static int LAST_THREE_YEAR=-3;
	public final static int LAST_FIVE_YEAR=-5;
	public final static int FROM_STARTDATE_TO_ENDDATE=0;
	public final static int SORT_BY_AR=0;
	public final static int SORT_BY_ALPHA=1;
	public final static int SORT_BY_BETA=2;
	public final static int SORT_BY_RSQUARED=3;
	public final static int SORT_BY_SHARPERATIO=4;
	public final static int SORT_BY_STANDARDDEVIATION=5;
	public final static int SORT_BY_TREYNORRATIO=6;
	public final static int SORT_BY_DRAWDOWN=7;
	public static final int SORT_BY_YEAR = 8;
	private java.util.Date startDate;
	private java.util.Date endDate;
	private java.lang.String yearString;
	
	private java.lang.String strategyName;

	private java.lang.Long strategyUserID;
	
	private java.util.List<String> MPTStatistics;
	
	private Integer State;
	
	public static int getSortNum(String sort){
		if(sort.equalsIgnoreCase("sharpe") || sort.equalsIgnoreCase("sharperatio")){
			return SORT_BY_SHARPERATIO;
		}
		else if(sort.equalsIgnoreCase("AR")){
			return SORT_BY_AR;
		}
		else if(sort.equalsIgnoreCase("Alpha")){
			return SORT_BY_ALPHA;
		}
		else if(sort.equalsIgnoreCase("Beta")){
			return SORT_BY_BETA;
		}
		else if(sort.equalsIgnoreCase("RSquared")){
			return SORT_BY_RSQUARED;
		}
		else if(sort.equalsIgnoreCase("StandardDeviation")){
			return SORT_BY_STANDARDDEVIATION;
		}
		else if(sort.equalsIgnoreCase("TreynorRatio")){
			return SORT_BY_TREYNORRATIO;
		}
		else if(sort.equalsIgnoreCase("DrawDown")){
			return SORT_BY_DRAWDOWN;
		}
		else
			return SORT_BY_SHARPERATIO;
	}
	
	
	public Integer getState() {
		return State;
	}



	public void setState(Integer state) {
		State = state;
	}



	private Date lastTransactionDate;
	
	public PortfolioMPT() {
		// TODO Auto-generated constructor stub
		super();
		MPTStatistics = new ArrayList<String>();
	}
	
	
	
	public PortfolioMPT(Long portfolioID, Double alpha, Double beta, Double ar, Double squared, Double sharpeRatio, Double standardDeviation, Double treynorRatio, Double drawDown) {
		super(portfolioID, alpha, beta, ar, squared, sharpeRatio, standardDeviation, treynorRatio, drawDown);
		// TODO Auto-generated constructor stub
	}
	public java.lang.Long getStrategyUserID() {
		return strategyUserID;
	}
	public void setStrategyUserID(java.lang.Long strategyUserID) {
		this.strategyUserID = strategyUserID;
	}
	public java.lang.String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(java.lang.String strategyName) {
		this.strategyName = strategyName;
	}
	public java.lang.String getYearString() {
		return yearString;
	}
	public void setYearString() {
		if(year > 0)
			this.yearString = year.toString();
		else{
			switch (year) {
			case FROM_STARTDATE_TO_ENDDATE:
				yearString = "From Starting Date to End Date";
				break;
			case LAST_ONE_YEAR:
				yearString = "Last One Year";
				break;
			case LAST_THREE_YEAR:
				yearString = "Last Three Years";
				break;
			case LAST_FIVE_YEAR:
				yearString = "Last Five Years";
				break;
			case DELAY_FROM_STARTDATE_TO_ENDDATE:
				yearString = "From Starting Date to End Date(delayed)";
				break;
			case DELAY_LAST_ONE_YEAR:
				yearString = "Last One Year(delayed)";
				break;
			case DELAY_LAST_THREE_YEAR:
				yearString = "Last Three Years(delayed)";
				break;
			case DELAY_LAST_FIVE_YEAR:
				yearString = "Last Five Years(delayed)";
				break;				
			default:
				break;
			}
		}
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	
	
	public void setAlpha(Double alpha) {
		if(alpha==null||alpha.isNaN())this.alpha=null;
		else this.alpha = alpha;
	}
	public void setBeta(Double beta) {
		if(beta==null||beta.isNaN())this.beta=null;
		else this.beta = beta;
	}
	public void setAR(Double ar) {
		if(ar==null||ar.isNaN())this.AR=null;
		else AR = ar;
	}
	public void setRSquared(Double squared) {
		if(squared==null||squared.isNaN())this.RSquared=null;
		else RSquared = squared;
	}
	public void setSharpeRatio(Double sharpeRatio) {
		if(sharpeRatio==null||sharpeRatio.isNaN())this.sharpeRatio=null;
		else this.sharpeRatio = sharpeRatio;
	}
	public void setStandardDeviation(Double standardDeviation) {
		if(standardDeviation==null||standardDeviation.isNaN())this.standardDeviation=null;
		else this.standardDeviation = standardDeviation;
	}
	public void setTreynorRatio(Double treynorRatio) {
		if(treynorRatio==null||treynorRatio.isNaN())this.treynorRatio=null;
		else this.treynorRatio = treynorRatio;
	}
	public void setDrawDown(Double drawDown) {
		if(drawDown==null||drawDown.isNaN())this.drawDown=null;
		else this.drawDown = drawDown;
	}
	public void setYearString(java.lang.String yearString) {
		this.yearString = yearString;
	}



	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}



	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}


	public java.util.List<String> getMPTStatistics() {
		return MPTStatistics;
	}


	public void setMPTStatistics(java.util.List<String> statistics) {
		MPTStatistics = statistics;
	}

}
