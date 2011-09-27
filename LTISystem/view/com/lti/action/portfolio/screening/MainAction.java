package com.lti.action.portfolio.screening;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.bo.PortfolioMPT;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<PortfolioMPT> portfolios;

	private Integer year;
	private Long classID;
	private Boolean isModelPortfolio;
	private Integer sortMethod;
	
	private PortfolioManager portfolioManager;
	private StrategyClassManager strategyClassManager;

	public void validate(){
		
		
	}

	@Override
	public String execute() throws Exception {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		if(classID!=null){
			Object[] classIDs=strategyClassManager.getClassIDs(classID);
			if(classIDs!=null)detachedCriteria.add(Restrictions.in("classID", classIDs));
		}
		
		if(year==null){
			year=PortfolioMPT.FROM_STARTDATE_TO_ENDDATE;
		}
		detachedCriteria.add(Restrictions.eq("year", year));
		
		if(isModelPortfolio!=null)detachedCriteria.add(Restrictions.eq("isModelPortfolio", isModelPortfolio));

		if(sortMethod==null){
			sortMethod=PortfolioMPT.SORT_BY_ALPHA;
		}
		switch(sortMethod){
			case PortfolioMPT.SORT_BY_ALPHA:
				detachedCriteria.addOrder(Order.desc("alpha"));
				break;
			case PortfolioMPT.SORT_BY_AR:
				detachedCriteria.addOrder(Order.desc("AR"));
				break;
			case PortfolioMPT.SORT_BY_BETA:
				detachedCriteria.addOrder(Order.desc("beta"));
				break;
			case PortfolioMPT.SORT_BY_DRAWDOWN:
				detachedCriteria.addOrder(Order.desc("drawDown"));
				break;
			case PortfolioMPT.SORT_BY_RSQUARED:
				detachedCriteria.addOrder(Order.desc("RSquared"));
				break;
			case PortfolioMPT.SORT_BY_SHARPERATIO:
				detachedCriteria.addOrder(Order.desc("sharpeRatio"));
				break;
			case PortfolioMPT.SORT_BY_STANDARDDEVIATION:
				detachedCriteria.addOrder(Order.desc("standardDeviation"));
				break;
			case PortfolioMPT.SORT_BY_TREYNORRATIO:
				detachedCriteria.addOrder(Order.desc("treynorRatio"));
				break;
			default:
				detachedCriteria.addOrder(Order.desc("alpha"));
				break;
		}

		portfolios = portfolioManager.findByCriteria(detachedCriteria);

		if(portfolios==null)return Action.SUCCESS;
		
		com.lti.util.FormatUtil.formatObject(portfolios, 3);
		
		for(int i=0;i<portfolios.size();i++){
			Integer sw=portfolios.get(i).getYear();
			if(sw==null)break;
			switch(sw){
			case PortfolioMPT.FROM_STARTDATE_TO_ENDDATE:
				portfolios.get(i).setYearString("From Start Date to End Date");
				break;
			case PortfolioMPT.LAST_FIVE_YEAR:
				portfolios.get(i).setYearString("Last Five Years");
				break;
			case PortfolioMPT.LAST_ONE_YEAR:
				portfolios.get(i).setYearString("Last One Years");
				break;
			case PortfolioMPT.LAST_THREE_YEAR:
				portfolios.get(i).setYearString("Last Three Years");
				break;	
			default:
				portfolios.get(i).setYearString(String.valueOf(sw));
				break;	
			}
		}
		
		return Action.SUCCESS;

	}


	public List<PortfolioMPT> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<PortfolioMPT> portfolios) {
		this.portfolios = portfolios;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getClassID() {
		return classID;
	}

	public void setClassID(Long classID) {
		this.classID = classID;
	}


	public Boolean getIsModelPortfolio() {
		return isModelPortfolio;
	}

	public void setIsModelPortfolio(Boolean isModelPortfolio) {
		this.isModelPortfolio = isModelPortfolio;
	}

	public Integer getSortMethod() {
		return sortMethod;
	}

	public void setSortMethod(Integer sortMethod) {
		this.sortMethod = sortMethod;
	}


	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	

	

}
