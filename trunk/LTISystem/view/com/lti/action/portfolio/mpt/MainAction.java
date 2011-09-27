package com.lti.action.portfolio.mpt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.type.LongString;
import com.lti.type.MPT;
import com.lti.type.PaginationSupport;
import com.lti.type.TimeUnit;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.util.LTIDate;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);

	private Long portfolioID;

	private PortfolioManager portfolioManager;

	private StrategyManager strategyManager;

	private List<MPTBean> MPTBeans;

	private java.util.Date startDate;

	private java.util.Date endDate;

	private String portfolioName;

	private UserManager userManager;

	private CustomizeRegionManager customizeRegionManager;

	private CustomizeRegion customizeRegion;

	private List<ConfidenceBean> confidenceBeans;

	private String confidence;

	private Integer width;

	private Boolean isHolding;

	private String holdingDate;

	private Long userID;

	private Boolean basicunit = false;

	private String type = "full";

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		confidence = confidence;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
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

	public List<MPTBean> getMPTBeans() {
		return MPTBeans;
	}

	public void setMPTBeans(List<MPTBean> beans) {
		MPTBeans = beans;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		User user = userManager.getLoginUser();
		if (user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		CustomizeUtil.setRegion(customizeRegion, userID);
	}

	private String message;
	@Override
	public String execute() throws Exception {

		if (portfolioID == null){
			message="The ID is null." ;
			addActionError(message);
			return Action.ERROR;
		}
			

		Portfolio portfolio = portfolioManager.get(portfolioID);

		if (portfolio == null){
			message="The portfolio doesn't exist.";
			addActionError(message);
			return Action.ERROR;
		}

		portfolioName = portfolio.getName();

		PermissionChecker pc=new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		Boolean read = null;
		if(portfolio.isFullyPublic()){
			read = true;
		}else{
			read = pc.hasViewRole();
		}

		if (read == false) {
			message="You have no right to read the data";
			addActionError(message);
			return Action.ERROR;
		}


		startDate = LTIDate.clearHMSM(portfolio.getStartingDate());
		endDate = LTIDate.clearHMSM(portfolio.getEndDate());;
		MPTBeans = new ArrayList<MPTBean>();
		
		boolean realtime=pc.hasRealtimeRole();

		// List<MPT> MPTs = portfolio.getEveryYearMPTS(TimeUnit.DAILY);

		List<PortfolioMPT> MPTs = portfolioManager.getEveryYearsMPT(portfolioID);
		int num = 0;
		MPTBean bean1 = null;
		MPTBean bean3 = null;
		MPTBean bean5 = null;
		MPTBean bean0 = null;
		if (MPTs != null && MPTs.size() > 0) {
			for (int i = 0; i < MPTs.size(); i++) {
				PortfolioMPT mpt = MPTs.get(i);
				MPTBean bean = new MPTBean();

				switch (mpt.getYear()) {
				case PortfolioMPT.FROM_STARTDATE_TO_ENDDATE:					
					bean0 = bean;
					break;
				case PortfolioMPT.LAST_ONE_YEAR:
					bean1 = bean;
					break;
				case PortfolioMPT.LAST_THREE_YEAR:
					bean3 = bean;
					break;
				case PortfolioMPT.LAST_FIVE_YEAR:
					bean5 = bean;
					break;
				case PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE:					
						continue;					
				case PortfolioMPT.DELAY_LAST_ONE_YEAR:
						continue;
				case PortfolioMPT.DELAY_LAST_THREE_YEAR:
						continue;					
				case PortfolioMPT.DELAY_LAST_FIVE_YEAR:
						continue;
				default:
					MPTBeans.add(bean);
					break;
				}

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

				mpt.setYearString();
				bean.setStartDate(mpt.getStartDate());
				bean.setEndDate(mpt.getEndDate());
				bean.setYear(mpt.getYear());
				bean.setYearString(translateYearString(mpt.getYear()));
				num++;

			}

		}
		if (bean1 != null) {
			MPTBeans.add(bean1);
		}
		if (bean3 != null) {
			MPTBeans.add(bean3);
		}
		if (bean5 != null) {
			MPTBeans.add(bean5);
		}

		if (bean0 != null) {
			MPTBeans.add(bean0);
		}

		// if (realtime == false) {
		// PortfolioDailyData pdd =
		// portfolioManager.getDailydata(portfolio.getID(), legalDate);
		// List<MPTBean> beans = translatePDDToMPTs(pdd);
		// if(beans != null && beans.size() != 0){
		// MPTBeans.addAll(beans);
		// num += beans.size();
		// }
		//	
		// }
		if (num > 0)
			width = 100 / num;
			portfolioName = portfolio.getName();

		if (basicunit) {
			return Action.BASICUNIT;
		} else {
			return Action.SUCCESS;
		}

	}
	
	private InputStream fis;
	
	public String download()throws Exception{
		execute();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		CsvListWriter clw=new CsvListWriter(new OutputStreamWriter(baos) , CsvPreference.STANDARD_PREFERENCE);
		
		String[] titles={"","Annualized Return (%)","Sharpe Ratio (%)","Alpha(%)","Beta",
				"RSquare","Standard Deviation","Treynor Ratio","Draw Down","Sortino Ratio"};
		for(int i=0;i<titles.length;i++){
			List<String> list=new ArrayList<String>();
			list.add(titles[i]);
			for(MPTBean mpt:MPTBeans){
				if(titles[i].equals("")){
					list.add(mpt.getYearString());
				}else if(titles[i].equals("Annualized Return (%)")){
					list.add(mpt.getAR());
				}else if(titles[i].equals("Sharpe Ratio (%)")){
					list.add(mpt.getSharpeRatio());
				}else if(titles[i].equals("Alpha(%)")){
					list.add(mpt.getAlpha());
				}else if(titles[i].equals("Beta")){
					list.add(mpt.getBeta());
				}else if(titles[i].equals("RSquare")){
					list.add(mpt.getRSquared());
				}else if(titles[i].equals("Standard Deviation")){
					list.add(mpt.getStandardDeviation());
				}else if(titles[i].equals("Treynor Ratio")){
					list.add(mpt.getTreynorRatio());
				}else if(titles[i].equals("Draw Down")){
					list.add(mpt.getDrawDown());
				}else if(titles[i].equals("Sortino Ratio")){
					list.add(mpt.getSortinoRatio());
				}else{
					list.add("NA");
				}
			}
			clw.write(list);
		}
		clw.close();
		fis=new ByteArrayInputStream(baos.toByteArray());
		return Action.SUCCESS;
	}

	public InputStream getFis() {
		return fis;
	}

	public void setFis(InputStream fis) {
		this.fis = fis;
	}

	private List<MPTBean> translatePDDToMPTs(PortfolioDailyData pdd) {
		List<MPTBean> beans = new ArrayList<MPTBean>();
		MPTBean bean0 = PDDToMPTItem(pdd, PortfolioMPT.FROM_STARTDATE_TO_ENDDATE);
		if (bean0 != null)
			beans.add(bean0);
		MPTBean bean1 = PDDToMPTItem(pdd, PortfolioMPT.LAST_ONE_YEAR);
		if (bean1 != null)
			beans.add(bean1);
		MPTBean bean3 = PDDToMPTItem(pdd, PortfolioMPT.LAST_THREE_YEAR);
		if (bean3 != null)
			beans.add(bean3);
		MPTBean bean5 = PDDToMPTItem(pdd, PortfolioMPT.LAST_FIVE_YEAR);
		if (bean5 != null)
			beans.add(bean5);
		return beans;
	}

	private MPTBean PDDToMPTItem(PortfolioDailyData pdd, int year) {
		MPTBean bean = new MPTBean();
		bean.setYearString(translateYearString(year));
		if (pdd == null) {
			bean.setAlpha("NA");
			bean.setBeta("NA");
			bean.setAR("NA");
			bean.setRSquared("NA");
			bean.setSharpeRatio("NA");
			bean.setStandardDeviation("NA");
			bean.setTreynorRatio("NA");
			bean.setDrawDown("NA");
			bean.setSortinoRatio("NA");
			return bean;
		}
		switch (year) {
		case PortfolioMPT.LAST_ONE_YEAR: {
			if (pdd.getAlpha1() == null || pdd.getAlpha1() > 10000.0)
				bean.setAlpha("NA");
			else
				bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(pdd.getAlpha1()));

			if (pdd.getBeta1() == null || pdd.getBeta1() > 10000.0)
				bean.setBeta("NA");
			else
				bean.setBeta(com.lti.util.FormatUtil.formatQuantity(pdd.getBeta1()));

			if (pdd.getAR1() == null || pdd.getAR1() > 10000.0)
				bean.setAR("NA");
			else
				bean.setAR(com.lti.util.FormatUtil.formatPercentage(pdd.getAR1()));

			if (pdd.getRSquared1() == null || pdd.getRSquared1() > 10000.0)
				bean.setRSquared("NA");
			else
				bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(pdd.getRSquared1()));

			if (pdd.getSharpeRatio1() == null || pdd.getSharpeRatio1() > 10000.0)
				bean.setSharpeRatio("NA");
			else
				bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(pdd.getSharpeRatio1()));

			if (pdd.getStandardDeviation1() == null || pdd.getStandardDeviation1() > 10000.0)
				bean.setStandardDeviation("NA");
			else
				bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(pdd.getStandardDeviation1()));

			if (pdd.getTreynorRatio1() == null || pdd.getTreynorRatio1() > 10000.0)
				bean.setTreynorRatio("NA");
			else
				bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getTreynorRatio1()));

			if (pdd.getDrawDown1() == null || pdd.getDrawDown1() > 10000.0)
				bean.setDrawDown("NA");
			else
				bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(pdd.getDrawDown1()));

			if (pdd.getSortinoRatio1() == null || pdd.getSortinoRatio1() > 10000.0)
				bean.setSortinoRatio("NA");
			else
				bean.setSortinoRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getSortinoRatio1()));

			break;
		}
		case PortfolioMPT.LAST_THREE_YEAR: {
			if (pdd.getAlpha3() == null || pdd.getAlpha3() > 10000.0)
				bean.setAlpha("NA");
			else
				bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(pdd.getAlpha3()));

			if (pdd.getBeta3() == null || pdd.getBeta3() > 10000.0)
				bean.setBeta("NA");
			else
				bean.setBeta(com.lti.util.FormatUtil.formatQuantity(pdd.getBeta3()));

			if (pdd.getAR3() == null || pdd.getAR3() > 10000.0)
				bean.setAR("NA");
			else
				bean.setAR(com.lti.util.FormatUtil.formatPercentage(pdd.getAR3()));

			if (pdd.getRSquared3() == null || pdd.getRSquared3() > 10000.0)
				bean.setRSquared("NA");
			else
				bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(pdd.getRSquared3()));

			if (pdd.getSharpeRatio3() == null || pdd.getSharpeRatio3() > 10000.0)
				bean.setSharpeRatio("NA");
			else
				bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(pdd.getSharpeRatio3()));

			if (pdd.getStandardDeviation3() == null || pdd.getStandardDeviation3() > 10000.0)
				bean.setStandardDeviation("NA");
			else
				bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(pdd.getStandardDeviation3()));

			if (pdd.getTreynorRatio3() == null || pdd.getTreynorRatio3() > 10000.0)
				bean.setTreynorRatio("NA");
			else
				bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getTreynorRatio3()));

			if (pdd.getDrawDown3() == null || pdd.getDrawDown3() > 10000.0)
				bean.setDrawDown("NA");
			else
				bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(pdd.getDrawDown3()));

			if (pdd.getSortinoRatio3() == null || pdd.getSortinoRatio3() > 10000.0)
				bean.setSortinoRatio("NA");
			else
				bean.setSortinoRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getSortinoRatio3()));

			break;
		}
		case PortfolioMPT.LAST_FIVE_YEAR: {
			if (pdd.getAlpha5() == null || pdd.getAlpha5() > 10000.0)
				bean.setAlpha("NA");
			else
				bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(pdd.getAlpha5()));

			if (pdd.getBeta5() == null || pdd.getBeta5() > 10000.0)
				bean.setBeta("NA");
			else
				bean.setBeta(com.lti.util.FormatUtil.formatQuantity(pdd.getBeta5()));

			if (pdd.getAR5() == null || pdd.getAR5() > 10000.0)
				bean.setAR("NA");
			else
				bean.setAR(com.lti.util.FormatUtil.formatPercentage(pdd.getAR5()));

			if (pdd.getRSquared5() == null || pdd.getRSquared5() > 10000.0)
				bean.setRSquared("NA");
			else
				bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(pdd.getRSquared5()));

			if (pdd.getSharpeRatio5() == null || pdd.getSharpeRatio5() > 10000.0)
				bean.setSharpeRatio("NA");
			else
				bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(pdd.getSharpeRatio5()));

			if (pdd.getStandardDeviation5() == null || pdd.getStandardDeviation5() > 10000.0)
				bean.setStandardDeviation("NA");
			else
				bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(pdd.getStandardDeviation5()));

			if (pdd.getTreynorRatio5() == null || pdd.getTreynorRatio5() > 10000.0)
				bean.setTreynorRatio("NA");
			else
				bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getTreynorRatio5()));

			if (pdd.getDrawDown5() == null || pdd.getDrawDown5() > 10000.0)
				bean.setDrawDown("NA");
			else
				bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(pdd.getDrawDown5()));

			if (pdd.getSortinoRatio5() == null || pdd.getSortinoRatio5() > 10000.0)
				bean.setSortinoRatio("NA");
			else
				bean.setSortinoRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getSortinoRatio5()));

			break;
		}
		case PortfolioMPT.FROM_STARTDATE_TO_ENDDATE: {
			if (pdd.getAlpha() == null || pdd.getAlpha() > 10000.0)
				bean.setAlpha("NA");
			else
				bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(pdd.getAlpha()));

			if (pdd.getBeta() == null || pdd.getBeta() > 10000.0)
				bean.setBeta("NA");
			else
				bean.setBeta(com.lti.util.FormatUtil.formatQuantity(pdd.getBeta()));

			if (pdd.getAR() == null || pdd.getAR() > 10000.0)
				bean.setAR("NA");
			else
				bean.setAR(com.lti.util.FormatUtil.formatPercentage(pdd.getAR()));

			if (pdd.getRSquared() == null || pdd.getRSquared() > 10000.0)
				bean.setRSquared("NA");
			else
				bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(pdd.getRSquared()));

			if (pdd.getSharpeRatio() == null || pdd.getSharpeRatio() > 10000.0)
				bean.setSharpeRatio("NA");
			else
				bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(pdd.getSharpeRatio()));

			if (pdd.getStandardDeviation() == null || pdd.getStandardDeviation() > 10000.0)
				bean.setStandardDeviation("NA");
			else
				bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(pdd.getStandardDeviation()));

			if (pdd.getTreynorRatio() == null || pdd.getTreynorRatio() > 10000.0)
				bean.setTreynorRatio("NA");
			else
				bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getTreynorRatio()));

			if (pdd.getDrawDown() == null || pdd.getDrawDown() > 10000.0)
				bean.setDrawDown("NA");
			else
				bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(pdd.getDrawDown()));

			if (pdd.getSortinoRatio() == null || pdd.getSortinoRatio() > 10000.0)
				bean.setSortinoRatio("NA");
			else
				bean.setSortinoRatio(com.lti.util.FormatUtil.formatQuantity(pdd.getSortinoRatio()));

			break;
		}
		default:
			break;
		}
		return bean;
	}

	// /////////////////////////////
	public String confidenceLevelCheck() throws Exception {
		if (portfolioID == null)
			return Action.SUCCESS;
		FormatUtil formatUtil = new FormatUtil();
		Portfolio portfolio = portfolioManager.get(portfolioID);
		confidence = portfolio.getAttributes().get("Confidence");

		if (portfolio == null)
			return Action.SUCCESS;
		confidenceBeans = new ArrayList<ConfidenceBean>();
		List<ConfidenceCheck> cc = portfolioManager.checkConfidence(portfolio.getID(), -1l, false);
		if (cc != null || cc.size() > 0) {
			for (int i = 0; i < cc.size(); i++) {
				ConfidenceCheck cCheck = cc.get(i);
				ConfidenceBean bean = new ConfidenceBean();
				if (cCheck.getMean() != null)
					bean.setMean(formatUtil.formatPercentage(cCheck.getMean()));
				if (cCheck.getSampleSize() != null)
					bean.setSampleSize(cCheck.getSampleSize());
				if (cCheck.getVariance() != null)
					bean.setVariance((cCheck.getVariance()).toString().substring(0, 5));
				if (cCheck.getAboveMeanPossibility() != null)
					bean.setAboveMeanPossibility(formatUtil.formatPercentage(cCheck.getAboveMeanPossibility()));

				if (cCheck.getMaxReturnUnderSampleProportion5() != null)
					bean.setMaxReturnUnderSampleProportion5(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion5()));

				if (cCheck.getMaxReturnUnderSampleProportion10() != null)
					bean.setMaxReturnUnderSampleProportion10(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion10()));

				if (cCheck.getMaxReturnUnderSampleProportion15() != null)
					bean.setMaxReturnUnderSampleProportion15(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion15()));

				if (cCheck.getMaxReturnUnderSampleProportion20() != null)
					bean.setMaxReturnUnderSampleProportion20(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion20()));

				if (cCheck.getMaxReturnUnderSampleProportion30() != null)
					bean.setMaxReturnUnderSampleProportion30(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion30()));

				if (cCheck.getMaxReturnUnderSampleProportion40() != null)
					bean.setMaxReturnUnderSampleProportion40(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion40()));

				if (cCheck.getMaxReturnUnderSampleProportion50() != null)
					bean.setMaxReturnUnderSampleProportion50(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion50()));

				if (cCheck.getMaxReturnUnderSampleProportion60() != null)
					bean.setMaxReturnUnderSampleProportion60(formatUtil.formatPercentage(cCheck.getMaxReturnUnderSampleProportion60()));

				bean.setRuleName(cCheck.getRuleName());
				try {
					Strategy strategy = strategyManager.get(cCheck.getStrategyID());
					bean.setStrategyName(strategy.getName());
				} catch (Exception e) {
				}
				;

				confidenceBeans.add(bean);
			}
		}
		return Action.SUCCESS;
	}

	// ////////////////////////////////////////////

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

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

	public List<ConfidenceBean> getConfidenceBeans() {
		return confidenceBeans;
	}

	public void setConfidenceBeans(List<ConfidenceBean> confidenceBeans) {
		this.confidenceBeans = confidenceBeans;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Boolean getIsHolding() {
		return isHolding;
	}

	public void setIsHolding(Boolean isHolding) {
		this.isHolding = isHolding;
	}

	public String getHoldingDate() {
		return holdingDate;
	}

	public void setHoldingDate(String holdingDate) {
		this.holdingDate = holdingDate;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Boolean getBasicunit() {
		return basicunit;
	}

	public void setBasicunit(Boolean basicunit) {
		this.basicunit = basicunit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
