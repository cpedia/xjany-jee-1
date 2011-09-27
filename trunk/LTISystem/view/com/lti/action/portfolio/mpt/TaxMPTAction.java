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
import com.lti.util.PortfolioAdjustAmountUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.util.LTIDate;

public class TaxMPTAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(TaxMPTAction.class);

	private Long portfolioID;

	private PortfolioManager portfolioManager;

	private List<MPTBean> MPTBeans;

	private Double commissionAmountEachTransaction = 7.0;
	
	private Double taxRateOnLongReturn = 10.0;
	
	private Double taxRateOnShortReturn = 30.0;
	
	private Double withDrawAmount = 100.0;
	
	private Double taxOnWithdraw = 0.0;
	
	private Double depositAmount = 0.0;
	
	private Double initialAmount = 10000.0;
	
	private Integer interval = 12;

	private Date startDate;
	
	private Date endDate;

	public List<MPTBean> getMPTBeans() {
		return MPTBeans;
	}

	public void setMPTBeans(List<MPTBean> beans) {
		MPTBeans = beans;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
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
		
		if(initialAmount != 0){
			withDrawAmount *= 10000.0 / initialAmount;
		}
		taxRateOnLongReturn /= 100;
		taxRateOnShortReturn /= 100;
		taxOnWithdraw /= 100;

		List<PortfolioMPT>  portfolioMPTList = PortfolioAdjustAmountUtil.calculatePerformance(portfolioID, PortfolioAdjustAmountUtil.COMMISSION_TYPE_EACHPAY, commissionAmountEachTransaction, 0.0, taxRateOnLongReturn, taxRateOnShortReturn, withDrawAmount, taxOnWithdraw, depositAmount, true, interval);
		
		startDate = LTIDate.clearHMSM(portfolio.getStartingDate());
		endDate = portfolio.getEndDate();

		MPTBeans = new ArrayList<MPTBean>();


		int num = 0;
		MPTBean bean1 = null;
		MPTBean bean3 = null;
		MPTBean bean5 = null;
		MPTBean bean0 = null;
		
		if (portfolioMPTList != null && portfolioMPTList.size() > 0) {
			for (PortfolioMPT mpt : portfolioMPTList) {
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

		return Action.SUCCESS;

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


	// ////////////////////////////////////////////

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Double getTaxRateOnLongReturn() {
		return taxRateOnLongReturn;
	}

	public void setTaxRateOnLongReturn(Double taxRateOnLongReturn) {
		this.taxRateOnLongReturn = taxRateOnLongReturn;
	}

	public Double getTaxRateOnShortReturn() {
		return taxRateOnShortReturn;
	}

	public void setTaxRateOnShortReturn(Double taxRateOnShortReturn) {
		this.taxRateOnShortReturn = taxRateOnShortReturn;
	}

	public Double getWithDrawAmount() {
		return withDrawAmount;
	}

	public void setWithDrawAmount(Double withDrawAmount) {
		this.withDrawAmount = withDrawAmount;
	}

	public Double getTaxOnWithdraw() {
		return taxOnWithdraw;
	}

	public void setTaxOnWithdraw(Double taxOnWithdraw) {
		this.taxOnWithdraw = taxOnWithdraw;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public Double getCommissionAmountEachTransaction() {
		return commissionAmountEachTransaction;
	}

	public void setCommissionAmountEachTransaction(
			Double commissionAmountEachTransaction) {
		this.commissionAmountEachTransaction = commissionAmountEachTransaction;
	}

	public Double getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(Double initialAmount) {
		this.initialAmount = initialAmount;
	}


}
