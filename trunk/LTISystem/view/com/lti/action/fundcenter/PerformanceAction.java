package com.lti.action.fundcenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.action.Action;
import com.lti.service.BaseFormulaManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.SourceType;
import com.lti.type.TimeUnit;
import com.lti.util.BaseFormulaUtil;
import com.lti.util.SecurityUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author cherry
 *
 */
public class PerformanceAction extends ActionSupport implements Action {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private java.util.Map<String, Boolean> MPTMap;
	
	private java.lang.String chosenMPT;	//separated by ","
	
	//if you want to choose last 1, 3, 5 years, you can use -1, -3, -5 on behalf of them.
	private java.lang.String chosenYear;	//separated by ","
	
	private java.lang.Boolean yearByYear;
	
	private java.util.List<SecurityMPT> securityMPTList;
	
	private java.lang.String symbol;
	
	private java.lang.String compareSymbol;
	
	private java.util.List<SecurityMPT> compareSecurityMPTList;
	
	private java.lang.Boolean compare;
	
	private SecurityManager securityManager;
	
	private PortfolioManager portfolioManager;
	
	private Security security;
	
	private Date displayDate;
	
	private Date startDate;
	
	private UserManager userManager;
	
	private java.lang.Boolean alpha = false;
	
	private java.lang.Boolean beta = false;
	
	private java.lang.Boolean AR = false;
	
	private java.lang.Boolean RSquared = false;
	
	private java.lang.Boolean sharpeRatio = false;
	
	private java.lang.Boolean standardDeviation = false;
	
	private java.lang.Boolean treynorRatio = false;
	
	private java.lang.Boolean drawDown = false;
	
	private java.lang.Boolean totalReturn = false; 
	
	private java.util.List<Long> years;
	
	private java.lang.Boolean upToDate;
	
	private java.util.Date endDate;
	
	private SecurityMPT securityMPT;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(symbol == null || symbol.equals("")){
			addActionError("No Security Symbol!");
			return;
		}
		alpha = false; beta = false; AR = false; RSquared = false;
		sharpeRatio = false; standardDeviation = false; treynorRatio = false;
		drawDown = false; totalReturn = false;
		upToDate = false;
		//set choosen MPTs 
		if(chosenMPT != null && !chosenMPT.equals("")){
			String[] choosenMPTs = chosenMPT.split(",");
			if(choosenMPTs != null)
			{
				for(int i = 0; i < choosenMPTs.length; i++){
					if(choosenMPTs[i].equalsIgnoreCase("alpha")){
						alpha = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("beta")){
						beta = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("AR")){
						AR = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("RSquared")){
						RSquared = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("sharpeRatio")){
						sharpeRatio = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("standardDeviation")){
						standardDeviation = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("treynorRatio")){
						treynorRatio = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("drawDown")){
						drawDown = true;
						continue;
					}
					if(choosenMPTs[i].equalsIgnoreCase("totalReturn")){
						totalReturn = true;
					}
				}
			}
		}
		else{
			alpha = true; beta = true; AR = true; RSquared = true;
			sharpeRatio = true; standardDeviation = true; treynorRatio = true;
			drawDown = true; totalReturn = true;
		}
		if(yearByYear != null && yearByYear == true){
			years = new ArrayList<Long>();
			years.add(-1l); years.add(-3l); years.add(-5l);
		}
		else if (chosenYear != null && !chosenYear.equals("")) {
			List<String> chosenYears = StringUtil.splitKeywords(chosenYear);
			years = new ArrayList<Long>();
			if(chosenYears != null){
				for(int i = 0; i < chosenYears.size(); i++){
					Long year = Long.parseLong(chosenYears.get(i));
					if(year.longValue() == 0){
						upToDate = true;
						continue;
					}
					years.add(year);
				}
			}
		}
		if(compareSymbol != null && !compareSymbol.equals(""))
			compare = true;
		else
			compare = false;
	}
	
	private List<Long> getYearsInMPTs(List<SecurityMPT> securityMPTList){
		if(securityMPTList == null || securityMPTList.size() == 0)
			return null;
		List<Long> years = new ArrayList<Long>();
		for(int i = 0; i < securityMPTList.size(); i++){
			years.add(securityMPTList.get(i).getYear());
		}
		return years;
	}
	
	private List<SecurityMPT> getCompareMPTList(List<SecurityMPT> securityMPTList, Security compareSecurity){
		List<Long> years = getYearsInMPTs(securityMPTList);
		if(years == null)
			return null;
		List<SecurityMPT> tempCompareList;
		if (compareSecurity.getSecurityType().longValue() != Configuration.SECURITY_TYPE_PORTFOLIO) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
			detachedCriteria.add(Restrictions.in("Year", years));
			detachedCriteria.add(Restrictions.eq("SecurityID", compareSecurity.getID()));
			detachedCriteria.addOrder(Order.asc("Year"));
			tempCompareList = securityManager.getSecurityByMPT(detachedCriteria);
		}
		else
		{
			Long portfolioID = Long.parseLong(compareSecurity.getSymbol().substring(compareSecurity.getSymbol().indexOf("_")+1));
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
			detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
			List<Integer> portYears = translateYeas(years);
			detachedCriteria.add(Restrictions.in("year", portYears));
			detachedCriteria.addOrder(Order.asc("year"));
			List<PortfolioMPT> portfolioMPTList = portfolioManager.findByCriteria(detachedCriteria);
			if(portfolioMPTList != null && portfolioMPTList.size() > 0){
				tempCompareList = SecurityUtil.translatePortToSecMPT(portfolioMPTList, null);
			}
			else
				tempCompareList = null;
		}
		List<SecurityMPT> compareSecurityMPTList = new ArrayList<SecurityMPT>();
		if(tempCompareList == null || tempCompareList.size() == 0){
			compareSecurityMPTList = null;
			return compareSecurityMPTList;
		}
		int i = 0, j = 0;
		compareSecurityMPTList = new ArrayList<SecurityMPT>();
		while(i < securityMPTList.size() && j < tempCompareList.size()){
			SecurityMPT baseMPT = securityMPTList.get(i);
			SecurityMPT compareMPT = tempCompareList.get(j);
			long baseYear = baseMPT.getYear();
			long compareYear = compareMPT.getYear();
			if(baseYear == compareYear){
				compareSecurityMPTList.add(compareMPT);
				i++;j++;
				continue;
			}
			else if(baseYear < compareYear){
				SecurityMPT mpt = new SecurityMPT(baseMPT.getSecurityID(), baseMPT.getSecurityName(), baseMPT.getSymbol(), baseMPT.getSecurityType(), baseMPT.getAssetClassID(), baseMPT.getYear());
				compareSecurityMPTList.add(mpt);
				i++;
			}
			else
			{
				j++;continue;
			}
		}
		while(i < securityMPTList.size()){
			SecurityMPT baseMPT = securityMPTList.get(i);
			SecurityMPT mpt = new SecurityMPT(baseMPT.getSecurityID(), baseMPT.getSecurityName(), baseMPT.getSymbol(), baseMPT.getSecurityType(), baseMPT.getAssetClassID(), baseMPT.getYear());
			compareSecurityMPTList.add(mpt);
			i++;
		}
		return compareSecurityMPTList;
	}
	
	public String performance() throws Exception{
		Security security = securityManager.getBySymbol(symbol);
		if(security == null){
			addActionError("No such security!");
			return Action.ERROR;
		}
		if (security.getSecurityType().longValue() != Configuration.SECURITY_TYPE_PORTFOLIO) {
			UserManager userManager = ContextHolder.getUserManager();
			User user = userManager.getLoginUser();
			// while the user is not anonymous, Check MPT is realtime or not, if not realtime, update MPT
			if(user!=null){
				Date mptLastDate = security.getMptLastDate();
				if (mptLastDate != null && security.getEndDate() != null){
					if(mptLastDate.before(security.getEndDate())){
						BaseFormulaUtil bf = new BaseFormulaUtil();
						List<SecurityMPT> updateList = bf.computeSecurityMPTForAR(security);
						if (updateList.size() > 0) {
							security.setMptLastDate(security.getEndDate());
							securityManager.updateMPTLastDate(security);
							securityManager.saveOrUpdateAllSecurityMPT(updateList);
						}
					}
				}
			}
			
			securityMPTList = securityManager.getSecurityMPTS(security.getID());
		}
		else
		{
			Long portfolioID = Long.parseLong(symbol.substring(symbol.indexOf("_")+1));
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
			detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
			detachedCriteria.addOrder(Order.asc("year"));
			List<PortfolioMPT> portfolioMPTList = portfolioManager.findByCriteria(detachedCriteria);
			if(portfolioMPTList != null && portfolioMPTList.size() > 0){
				securityMPTList = SecurityUtil.translatePortToSecMPT(portfolioMPTList, null);
			}
		}
		if(compare == false || securityMPTList == null  || securityMPTList.size() == 0)
			return Action.SUCCESS;
		Security compareSecurity = securityManager.getBySymbol(compareSymbol);
		if(compareSecurity == null){
			addActionError("No Such Security!");
			return Action.SUCCESS;
		}
		compareSecurityMPTList = getCompareMPTList(securityMPTList, compareSecurity);
		return Action.SUCCESS;
	}
	
	public String allReturn() throws Exception{
		Security security = securityManager.getBySymbol(symbol);
		if(security == null){
			addActionError("No such security!");
			return Action.ERROR;
		}
		if (security.getSecurityType().longValue() != Configuration.SECURITY_TYPE_PORTFOLIO) {
			securityMPTList = securityManager.getSecurityMPTS(security.getID());
		}
		else
		{
			Long portfolioID = Long.parseLong(symbol.substring(symbol.indexOf("_")+1));
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
			detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
			detachedCriteria.addOrder(Order.asc("year"));
			List<PortfolioMPT> portfolioMPTList = portfolioManager.findByCriteria(detachedCriteria);
			if(portfolioMPTList != null && portfolioMPTList.size() > 0){
				securityMPTList = SecurityUtil.translatePortToSecMPT(portfolioMPTList, null);
			}
		}
		alpha = false; beta = false; AR = false; RSquared = false;
		sharpeRatio = false; standardDeviation = false; treynorRatio = false;
		drawDown = false; totalReturn = true;
		if(compare == false || securityMPTList == null  || securityMPTList.size() == 0)
			return Action.SUCCESS;
		Security compareSecurity = securityManager.getBySymbol(compareSymbol);
		compareSecurityMPTList = getCompareMPTList(securityMPTList, compareSecurity);
		return Action.SUCCESS;
	}
	
	public String partialPerformance() throws Exception{
		Security security = securityManager.getBySymbol(symbol);
		if(security == null){
			addActionError("No such security!");
			return Action.ERROR;
		}
		if (security.getSecurityType().longValue() != Configuration.SECURITY_TYPE_PORTFOLIO) {
			UserManager userManager = ContextHolder.getUserManager();
			User user = userManager.getLoginUser();
			// while the user is not anonymous, Check MPT is realtime or not, if not realtime, update MPT
			if(user!=null){
				Date mptLastDate = security.getMptLastDate();
				if (mptLastDate != null && security.getEndDate() != null){
					if(mptLastDate.before(security.getEndDate())){
						BaseFormulaUtil bf = new BaseFormulaUtil();
						List<SecurityMPT> updateList = bf.computeSecurityMPTForAR(security);
						if (updateList.size() > 0) {
							security.setMptLastDate(security.getEndDate());
							securityManager.updateMPTLastDate(security);
							securityManager.saveOrUpdateAllSecurityMPT(updateList);
						}
					}
				}
			}
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", security.getID()));
			if (yearByYear != null && yearByYear == true) {
				detachedCriteria.add(Restrictions.not(Restrictions.in("Year",years)));
			} else if (years != null) {
				detachedCriteria.add(Restrictions.in("Year", years));
			}
			detachedCriteria.addOrder(Order.asc("Year"));
			securityMPTList = securityManager.getSecurityByMPT(detachedCriteria);
		}
		else
		{
			Long portfolioID = Long.parseLong(symbol.substring(symbol.indexOf("_")+1));
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
			detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
			if (yearByYear != null && yearByYear == true) {
				List<Integer> years = translateYeas(this.years);
				
				detachedCriteria.add(Restrictions.not(Restrictions.in("year",years)));
			} else if (years != null && years.size() > 0) {
				List<Integer> years =  translateYeas(this.years);
				detachedCriteria.add(Restrictions.in("year", years));
			}
			detachedCriteria.addOrder(Order.asc("year"));
			List<PortfolioMPT> portfolioMPTList = portfolioManager.findByCriteria(detachedCriteria);
			if(portfolioMPTList != null && portfolioMPTList.size() > 0){
				securityMPTList = SecurityUtil.translatePortToSecMPT(portfolioMPTList, null);
			}
		}
		if(compare == false || securityMPTList == null  || securityMPTList.size() == 0)
			return Action.SUCCESS;
		Security compareSecurity = securityManager.getBySymbol(compareSymbol);
		compareSecurityMPTList = getCompareMPTList(securityMPTList, compareSecurity);

		return Action.SUCCESS;
	}
	
	public String detailPerformance(){
		security = securityManager.getBySymbol(symbol);
		if(security.getMptLastDate() == null)
			displayDate = null;
		else
			displayDate = security.getMptLastDate();
		startDate = securityManager.getStartDate(security.getID());
		return Action.SUCCESS;
	}
	
	/**
	 * @return
	 */
	public String calculatePerformance(){
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		security = securityManager.getBySymbol(symbol);
		if(security == null)
		{
			addActionError("No Such Fund");
			return Action.SUCCESS;
		}
		if(startDate == null){
			addActionError("No Start Date!");
			return Action.SUCCESS;
		}
		Date lastDataDate = securityManager.getLatestDailydata(security.getID()).getDate();
		if(endDate == null){
			endDate = lastDataDate;
		}
		else
		{
			if(endDate.after(lastDataDate))
				endDate = lastDataDate;
		}
		securityMPT = new SecurityMPT();
		securityMPT.setSymbol(security.getSymbol());
		securityMPT.setSecurityName(security.getName());
		securityMPT.setSecurityID(security.getID());
		securityMPT.setAssetClassID(security.getClassID());
		securityMPT.setSecurityType(security.getSecurityType());
		long t1 = System.currentTimeMillis();
		long t2;
		int interval;
		Double curPrice,prePrice;
		Double annulizedStandarddeviation=0.0,annulizedriskfree=0.0,ar=0.0,totalreturn=0.0,sharperatio=0.0;
		try {
			List<SecurityDailyData> securityDailyList = securityManager.getDailyDatas(security.getID(), startDate, endDate);
			interval = securityDailyList.size();
			prePrice = securityDailyList.get(0).getAdjClose();
			curPrice = securityDailyList.get(interval-1).getAdjClose();
			//List<Double> returns = baseFormulaManager.getReturns(startDate, endDate, TimeUnit.DAILY, security.getID(),SourceType.SECURITY_RETURN,false);
			List<Double> returns = baseFormulaManager.getReturns(securityDailyList);
			annulizedStandarddeviation = baseFormulaManager.computeAnnulizedStandardDeviation(returns, TimeUnit.DAILY);
			annulizedriskfree = baseFormulaManager.getAnnualizedRiskFree(startDate,endDate);
			ar = baseFormulaManager.computeAnnualizedReturn(curPrice,prePrice,interval-1);
			totalreturn = baseFormulaManager.computeIntervalReturn(curPrice,prePrice);
			sharperatio = baseFormulaManager.computeRatio(ar, annulizedriskfree,annulizedStandarddeviation);
		} catch (NoPriceException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			if (totalReturn == true) {
				try {
					//Double totalreturn = security.getReturn(startDate, endDate);
					securityMPT.setReturn(totalreturn);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setReturn(null);
				}
			}
			if (alpha == true) {
				try {
					Double Alpha = security.getAlpha(startDate, endDate,
							TimeUnit.DAILY);
					securityMPT.setAlpha(Alpha);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setAlpha(null);
				}
			}
			if (beta == true) {
				try {
					Double Beta = security.getBeta(startDate, endDate,
							TimeUnit.DAILY);
					securityMPT.setBeta(Beta);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setBeta(null);
				}
			}
			if (AR == true) {
				try {
					//Double AR = security.getAnnualizedReturn(startDate, endDate);
					securityMPT.setAR(ar);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setAR(null);
				}
			}
			if (drawDown == true) {
				try {
					Double DrawDown = security.getDrawDown(startDate, endDate,
							TimeUnit.DAILY);
					securityMPT.setDrawDown(DrawDown);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setDrawDown(null);
				}
			}
			if (treynorRatio == true) {
				try {
					Double TreynorRatio = security.getTreynorRatio(startDate,
							endDate, TimeUnit.DAILY);
					securityMPT.setTreynorRatio(TreynorRatio);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setTreynorRatio(null);
				}
			}
			if (sharpeRatio == true) {
				try {
					
					//Double SharpeRatio = security.getSharpeRatio(startDate,endDate, TimeUnit.DAILY);
					securityMPT.setSharpeRatio(sharperatio);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setSharpeRatio(null);
				}
			}
			if (standardDeviation == true) {
				try {
					//Double StandardDeviation = security.getAnnualizedStandardDeviation(startDate, endDate, TimeUnit.DAILY);
					securityMPT.setStandardDeviation(annulizedStandarddeviation);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setStandardDeviation(null);
				}
			}
			if (RSquared == true) {
				try {
					Double RSquared = security.getRSquared(startDate, endDate,
							TimeUnit.DAILY);
					securityMPT.setRSquared(RSquared);
				} catch (Exception e) {
					// TODO: handle exception
					securityMPT.setRSquared(null);
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			securityMPT.setReturn(null);
			securityMPT.setAlpha(null);
			securityMPT.setBeta(null);
			securityMPT.setAR(null);
			securityMPT.setDrawDown(null);
			securityMPT.setTreynorRatio(null);
			securityMPT.setSharpeRatio(null);
			securityMPT.setStandardDeviation(null);
			securityMPT.setRSquared(null);
		}
		t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
		return Action.SUCCESS;
	}
	
	private List<Integer> translateYeas(List<Long> years){
		if (years == null || years.size() == 0) {
			return null;
		}
		List<Integer> yearIntList = new ArrayList<Integer>();
		for(int i = 0; i < years.size(); i++){
			yearIntList.add(years.get(i).intValue());
		}
		return yearIntList;
	}

	public java.util.List<SecurityMPT> getSecurityMPTList() {
		return securityMPTList;
	}

	public void setSecurityMPTList(java.util.List<SecurityMPT> securityMPTList) {
		this.securityMPTList = securityMPTList;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public java.lang.String getChosenMPT() {
		return chosenMPT;
	}

	public void setChosenMPT(java.lang.String chosenMPT) {
		this.chosenMPT = chosenMPT;
	}

	public java.lang.String getChosenYear() {
		return chosenYear;
	}

	public void setChosenYear(java.lang.String chosenYear) {
		this.chosenYear = chosenYear;
	}

	public java.lang.Boolean getAlpha() {
		return alpha;
	}

	public void setAlpha(java.lang.Boolean alpha) {
		this.alpha = alpha;
	}

	public java.lang.Boolean getBeta() {
		return beta;
	}

	public void setBeta(java.lang.Boolean beta) {
		this.beta = beta;
	}

	public java.lang.Boolean getAR() {
		return AR;
	}

	public void setAR(java.lang.Boolean ar) {
		AR = ar;
	}

	public java.lang.Boolean getRSquared() {
		return RSquared;
	}

	public void setRSquared(java.lang.Boolean squared) {
		RSquared = squared;
	}

	public java.lang.Boolean getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(java.lang.Boolean sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}

	public java.lang.Boolean getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(java.lang.Boolean standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public java.lang.Boolean getTreynorRatio() {
		return treynorRatio;
	}

	public void setTreynorRatio(java.lang.Boolean treynorRatio) {
		this.treynorRatio = treynorRatio;
	}

	public java.lang.Boolean getDrawDown() {
		return drawDown;
	}

	public void setDrawDown(java.lang.Boolean drawDown) {
		this.drawDown = drawDown;
	}

	public java.lang.Boolean getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(java.lang.Boolean totalReturn) {
		this.totalReturn = totalReturn;
	}

	public java.util.List<Long> getYears() {
		return years;
	}

	public void setYears(java.util.List<Long> years) {
		this.years = years;
	}

	public java.lang.Boolean getYearByYear() {
		return yearByYear;
	}

	public void setYearByYear(java.lang.Boolean yearByYear) {
		this.yearByYear = yearByYear;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Date getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public java.lang.String getCompareSymbol() {
		return compareSymbol;
	}

	public void setCompareSymbol(java.lang.String compareSymbol) {
		this.compareSymbol = compareSymbol;
	}

	public java.util.List<SecurityMPT> getCompareSecurityMPTList() {
		return compareSecurityMPTList;
	}

	public void setCompareSecurityMPTList(
			java.util.List<SecurityMPT> compareSecurityMPTList) {
		this.compareSecurityMPTList = compareSecurityMPTList;
	}

	public java.lang.Boolean getCompare() {
		return compare;
	}

	public void setCompare(java.lang.Boolean compare) {
		this.compare = compare;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public SecurityMPT getSecurityMPT() {
		return securityMPT;
	}

	public void setSecurityMPT(SecurityMPT securityMPT) {
		this.securityMPT = securityMPT;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	
}
