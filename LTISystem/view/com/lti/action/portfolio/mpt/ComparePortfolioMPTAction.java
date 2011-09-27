package com.lti.action.portfolio.mpt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.bean.ConfidenceBean;
import com.lti.bean.MPTBean;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.ConfidenceCheck;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.LongString;
import com.lti.type.MPT;
import com.lti.type.PaginationSupport;
import com.lti.type.TimeUnit;
import com.lti.util.BaseFormulaUtil;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.util.LTIDate;

public class ComparePortfolioMPTAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ComparePortfolioMPTAction.class);

	private List<List<MPTBean>> MPTBeans;
	
	private List<MPTBean> firstMPTBeans;
	
	private String firstSymbol;
	
	private List<String> symbolList;

	private String message;
	
	private boolean isAnonymous = false;
	
	private boolean hasRealtimeAR = true;
	
	private List<String> portfolioNameList;
	
	private int compareSize;
	
	private String portfolioString;
	
	private List<Integer> years = new ArrayList<Integer>();
	
	private List<String>firstMPTLastDateList = new ArrayList<String>();
	
	private List<String> MPTLastDateList = new ArrayList<String>();
	
	private MPTBean translateSecurityMPTToMPTBean(SecurityMPT mpt){
		MPTBean bean = new MPTBean();
		if (mpt.getAlpha() == null || mpt.getAlpha() > 10000.0)
			bean.setAlpha("NA");
		else
			bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(mpt.getAlpha()));

		if (mpt.getBeta() == null || mpt.getBeta() > 10000.0)
			bean.setBeta("NA");
		else
			bean.setBeta(com.lti.util.FormatUtil.formatQuantity(mpt.getBeta()));

		if (mpt.getAR() == null || mpt.getAR() > 10000.0)
			bean.setAR("NA");
		else
			bean.setAR(com.lti.util.FormatUtil.formatPercentage(mpt.getAR()));

		if (mpt.getRSquared() == null || mpt.getRSquared() > 10000.0)
			bean.setRSquared("NA");
		else
			bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(mpt.getRSquared()));

		if (mpt.getSharpeRatio() == null || mpt.getSharpeRatio() > 10000.0)
			bean.setSharpeRatio("NA");
		else
			bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(mpt.getSharpeRatio()));

		if (mpt.getStandardDeviation() == null || mpt.getStandardDeviation() > 10000.0)
			bean.setStandardDeviation("NA");
		else
			bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(mpt.getStandardDeviation()));

		if (mpt.getTreynorRatio() == null || mpt.getTreynorRatio() > 10000.0)
			bean.setTreynorRatio("NA");
		else
			bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(mpt.getTreynorRatio()));

		if (mpt.getDrawDown() == null || mpt.getDrawDown() > 10000.0)
			bean.setDrawDown("NA");
		else
			bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(mpt.getDrawDown()));
		//Security MPT don't have sortinoratio
		bean.setSortinoRatio("NA");

		bean.setYear(mpt.getYear().intValue());
		bean.setType(Configuration.MPT_TYPE_SECURITY);
		return bean;
	}
	
	private MPTBean translatePortfolioMPTToMPTBean(PortfolioMPT mpt){
		MPTBean bean = new MPTBean();
		if (mpt.getAlpha() == null || mpt.getAlpha() > 10000.0)
			bean.setAlpha("NA");
		else
			bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(mpt.getAlpha()));

		if (mpt.getBeta() == null || mpt.getBeta() > 10000.0)
			bean.setBeta("NA");
		else
			bean.setBeta(com.lti.util.FormatUtil.formatQuantity(mpt.getBeta()));

		if (mpt.getAR() == null || mpt.getAR() > 10000.0)
			bean.setAR("NA");
		else
			bean.setAR(com.lti.util.FormatUtil.formatPercentage(mpt.getAR()));

		if (mpt.getRSquared() == null || mpt.getRSquared() > 10000.0)
			bean.setRSquared("NA");
		else
			bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(mpt.getRSquared()));

		if (mpt.getSharpeRatio() == null || mpt.getSharpeRatio() > 10000.0)
			bean.setSharpeRatio("NA");
		else
			bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(mpt.getSharpeRatio()));

		if (mpt.getStandardDeviation() == null || mpt.getStandardDeviation() > 10000.0)
			bean.setStandardDeviation("NA");
		else
			bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(mpt.getStandardDeviation()));

		if (mpt.getTreynorRatio() == null || mpt.getTreynorRatio() > 10000.0)
			bean.setTreynorRatio("NA");
		else
			bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(mpt.getTreynorRatio()));

		if (mpt.getDrawDown() == null || mpt.getDrawDown() > 10000.0)
			bean.setDrawDown("NA");
		else
			bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(mpt.getDrawDown()));

		if (mpt.getSortinoRatio() == null || mpt.getSortinoRatio() > 10000.0)
			bean.setSortinoRatio("NA");
		else
			bean.setSortinoRatio(com.lti.util.FormatUtil.formatQuantity(mpt.getSortinoRatio()));

		bean.setYear(mpt.getYear());
		bean.setType(Configuration.MPT_TYPE_PORTFOLIO);
		return bean;
	}
	
	private void initializeYears(Date startDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int curYear = cal.get(Calendar.YEAR);
		cal.setTime(startDate);
		int startYear = cal.get(Calendar.YEAR);
		for(int i=startYear;i<=curYear;++i)
			years.add(i);
		years.add(PortfolioMPT.LAST_ONE_YEAR);
		years.add(PortfolioMPT.LAST_THREE_YEAR);
		years.add(PortfolioMPT.LAST_FIVE_YEAR);
		years.add(PortfolioMPT.FROM_STARTDATE_TO_ENDDATE);
	}
	
	@Override
	public String execute() throws Exception {
		
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		UserManager userManager = ContextHolder.getUserManager();
		String[] portfolioNames = null;
		HashMap<Integer, List<MPTBean>> beanMap = new HashMap<Integer, List<MPTBean>>();
		if(!StringUtils.isBlank(portfolioString)){
			portfolioNames = portfolioString.split(",");
		}
		Date startDate = LTIDate.getDate(2000, 12, 31);
		portfolioNameList = new ArrayList<String>();
		symbolList = new ArrayList<String>();
		compareSize = 0;
		User user = userManager.getLoginUser();
		if(user == null){
			isAnonymous = true;
		}
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		for(String name : portfolioNames){
			Portfolio portfolio = portfolioManager.get(name);
			if(portfolio == null){
				Security security = securityManager.get(name);
				String date = df.format(security.getMptLastDate());
				if(security != null){
					if(compareSize == 0){
						firstSymbol = security.getSymbol();
					}						
					else{
						symbolList.add(security.getSymbol());						
					}
						
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
					
					//if the securityMPT AR is not realtime, show warning message to anonymous
					if(LTIDate.equals(security.getMptLastDate(), security.getEndDate())){
						hasRealtimeAR = false;
					}
					if(compareSize == 0){
						firstMPTLastDateList.add(date);
					}						
					else{					
						MPTLastDateList.add(date);
					}
					List<SecurityMPT> securityMPTList = securityManager.getSecurityMPTS(security.getID());
					List<MPTBean> mptBeanList = new ArrayList<MPTBean>();
					if(securityMPTList != null) {
						for(SecurityMPT sm : securityMPTList){
							MPTBean mptBean = this.translateSecurityMPTToMPTBean(sm);
							mptBeanList.add(mptBean);
						}
					}
					if(security.getStartDate() != null && LTIDate.before(security.getStartDate(), startDate))
						startDate = security.getStartDate();
					beanMap.put(compareSize, mptBeanList);
					portfolioNameList.add(security.getName());					
					++compareSize;
				}
			}else{
				portfolioNameList.add(name);
				Long portfolioID = portfolio.getID();
				String date = df.format(portfolio.getEndDate());
				if(compareSize == 0){
					firstSymbol = portfolio.getSymbol();
					firstMPTLastDateList.add(date);
				}					
				else{
					symbolList.add(portfolio.getSymbol());
					MPTLastDateList.add(date);
				}
					
				List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolioID);
				List<MPTBean> mptBeanList = new ArrayList<MPTBean>();
				if(portfolioMPTList != null) {
					for(PortfolioMPT pm : portfolioMPTList){
						MPTBean mptBean = this.translatePortfolioMPTToMPTBean(pm);
						mptBeanList.add(mptBean);
					}
				}
				if(portfolio.getStartingDate() != null && LTIDate.before(portfolio.getStartingDate(), startDate))
					startDate = portfolio.getStartingDate();
				beanMap.put(compareSize, mptBeanList);
				++compareSize;
			}
		}
		initializeYears(startDate);
		MPTBeans = new ArrayList<List<MPTBean>>();
		firstMPTBeans = new ArrayList<MPTBean>();
		for(int i=0;i<compareSize;++i){
			List<MPTBean> mptBeanList = beanMap.get(i);
			List<MPTBean> oneBeanList = new ArrayList<MPTBean>();
			for(int year : years){
				boolean found = false;
				for(MPTBean curBean : mptBeanList){
					if(curBean.getYear() == year){
						oneBeanList.add(curBean);
						found = true;
					}
				}
				if(!found){
					MPTBean curBean = new MPTBean();
					curBean.setYear(year);
					oneBeanList.add(curBean);
				}
			}
			if(i==0)
				firstMPTBeans = oneBeanList;
			else
				MPTBeans.add(oneBeanList);
		}
		return Action.SUCCESS;

	}
	

	// ////////////////////////////////////////////

	private String translateYearString(Integer year) {
		String yearString = "";
		if (year > 0)
			yearString = year.toString();
		else {
			switch (year) {
			case PortfolioMPT.FROM_STARTDATE_TO_ENDDATE:
				yearString = getText("from.starting.to.end");
				break;
			case PortfolioMPT.LAST_ONE_YEAR:
				yearString = getText("last.one.years");
				break;
			case PortfolioMPT.LAST_THREE_YEAR:
				yearString = getText("last.three.years");
				break;
			case PortfolioMPT.LAST_FIVE_YEAR:
				yearString = getText("last.five.years");
				break;
			default:
				break;
			}
		}
		return yearString;
	}

	public List<List<MPTBean>> getMPTBeans() {
		return MPTBeans;
	}

	public void setMPTBeans(List<List<MPTBean>> beans) {
		MPTBeans = beans;
	}

	public List<MPTBean> getFirstMPTBeans() {
		return firstMPTBeans;
	}

	public void setFirstMPTBeans(List<MPTBean> firstMPTBeans) {
		this.firstMPTBeans = firstMPTBeans;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getPortfolioNameList() {
		return portfolioNameList;
	}

	public void setPortfolioNameList(List<String> portfolioNameList) {
		this.portfolioNameList = portfolioNameList;
	}

	public int getCompareSize() {
		return compareSize;
	}

	public void setCompareSize(int compareSize) {
		this.compareSize = compareSize;
	}

	public String getPortfolioString() {
		return portfolioString;
	}

	public void setPortfolioString(String portfolioString) {
		this.portfolioString = portfolioString;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public String getFirstSymbol() {
		return firstSymbol;
	}

	public void setFirstSymbol(String firstSymbol) {
		this.firstSymbol = firstSymbol;
	}

	public List<String> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(List<String> symbolList) {
		this.symbolList = symbolList;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public boolean isHasRealtimeAR() {
		return hasRealtimeAR;
	}

	public void setHasRealtimeAR(boolean hasRealtimeAR) {
		this.hasRealtimeAR = hasRealtimeAR;
	}

	public List<String> getMPTLastDateList() {
		return MPTLastDateList;
	}

	public void setMPTLastDateList(List<String> mPTLastDateList) {
		MPTLastDateList = mPTLastDateList;
	}

	public List<String> getFirstMPTLastDateList() {
		return firstMPTLastDateList;
	}

	public void setFirstMPTLastDateList(List<String> firstMPTLastDateList) {
		this.firstMPTLastDateList = firstMPTLastDateList;
	}


}
