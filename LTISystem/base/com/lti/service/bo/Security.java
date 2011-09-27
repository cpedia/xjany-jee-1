package com.lti.service.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.service.AssetClassManager;
import com.lti.service.BaseFormulaManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.base.BaseSecurity;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.LTINumber;
import com.lti.type.MPT;
import com.lti.type.SourceType;
import com.lti.type.TimePeriod;
import com.lti.type.TimeUnit;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class Security extends BaseSecurity implements Serializable {
	private static final long serialVersionUID = 1L;

	private java.util.Date dividendLastDate;

	private java.util.Date priceLastDate;

	private java.util.Date navLastDate;

	private java.util.Date mptLastDate;

	private java.lang.String securityTypeName;

	private java.lang.String className;

	public java.util.Date getDividendLastDate() {
		return dividendLastDate;
	}

	public void setDividendLastDate(java.util.Date dividendLastDate) {
		this.dividendLastDate = dividendLastDate;
	}

	public java.util.Date getPriceLastDate() {

		return priceLastDate;
	}

	public void setPriceLastDate(java.util.Date priceLastDate) {
		this.priceLastDate = priceLastDate;
	}

	public java.util.Date getNavLastDate() {
		return navLastDate;
	}

	public void setNavLastDate(java.util.Date navLastDate) {
		this.navLastDate = navLastDate;
	}

	private java.util.Hashtable<String, Double> MPTs;

	public java.util.Hashtable<String, Double> getMPTs() {
		return MPTs;
	}

	public void setMPTs(java.util.Hashtable<String, Double> ts) {
		MPTs = ts;
	}

	public java.lang.Double getCurrentPrice(Date date) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		return securityManager.getPrice(this.ID, date);

	}

	/** ************************************************************************************************************************************** */
	/* Over ride */
	public double getAlpha(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeAlpha(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getBeta(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeBeta(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getRSquared(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeRSquared(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getSharpeRatio(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeSharpeRatio(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getTreynorRatio(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeTreynorRatio(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getStandardDeviation(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeStandardDeviation(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getDrawDown(Date startDate, Date endDate, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeDrawDown(startDate, endDate, unit, SourceType.SECURITY_AMOUNT, this.getID(), false);
	}

	public double getAnnualizedReturn(Date startDate, Date endDate) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeAnnualizedReturn(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), 0, false);
	}

	public Double getAnnualizedStandardDeviation(Date startDate, Date endDate, TimeUnit unit) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		List<Double> values;
		try {
			values = baseFormulaManager.getReturns(startDate, endDate, unit, this.getID(), SourceType.SECURITY_RETURN, false);
			return baseFormulaManager.computeAnnulizedStandardDeviation(values, unit);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public double getAnnualReturn(Date startDate, Date endDate) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeAnnualizedReturn(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), 1, false);
	}

	public double getRSI(Date startDate, Date endDate) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeRSI(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), false);
	}

	// When calculating SMA, we use 5 days, 10 days, 30 days, 60 days, 13weeks,
	// 26weeks and 52weeks as N
	public double getSMA(Date endDate, int count, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeSMA(endDate, count, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getEMA(Date startDate, Date endDate, int count, TimeUnit unit) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeEMA(startDate, endDate, count, unit, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	public double getMACD(Date startDate, Date endDate, TimeUnit tu) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeMACD(startDate, endDate, SourceType.SECURITY_RETURN, tu, this.getID(), false);
	}

	public double getDEA(Date startDate, Date endDate, TimeUnit tu) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeDEA(startDate, endDate, SourceType.SECURITY_RETURN, tu, this.getID(), false);
	}

	public MPT getMPTS(Date startDate, Date endDate, TimeUnit tu) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeMPTS(startDate, endDate, tu, SourceType.SECURITY_RETURN, this.getID(), false, false);
	}

	public List<MPT> getEveryYearMPTS(TimeUnit tu) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeEveryYearMPTs(tu, SourceType.SECURITY_RETURN, this.getID(), false);
	}

	/** ************************************************************************************************************************************** */

	public double getAlpha(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeAlpha(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getAlpha(Date startDate, Date endDate, TimeUnit unit, Long benchMarkID, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeAlpha(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), benchMarkID, nav);
	}

	public double getBeta(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeBeta(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getRSquared(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeRSquared(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getSharpeRatio(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeSharpeRatio(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getRiskFree(TimeUnit unit) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.getRiskFree(unit);
	}

	public double getTreynorRatio(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeTreynorRatio(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getStandardDeviation(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeStandardDeviation(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getDrawDown(Date startDate, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeDrawDown(startDate, endDate, unit, SourceType.SECURITY_AMOUNT, this.getID(), nav);
	}

	public double getAnnualizedReturn(Date startDate, Date endDate, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeAnnualizedReturn(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), 0, nav);
	}

	public double getAnnualReturn(Date startDate, Date endDate, boolean nav) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeAnnualizedReturn(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), 1, nav);
	}

	public double getRSI(Date startDate, Date endDate, boolean nav) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeRSI(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), nav);
	}

	// When calculating SMA, we use 5 days, 10 days, 30 days, 60 days, 13weeks,
	// 26weeks and 52weeks as N
	public double getSMA(Date endDate, int count, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeSMA(endDate, count, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getSMA(Date endDate, int count, TimeUnit unit, boolean adjust, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		if (adjust)
			return baseFormulaManager.computeSMA(endDate, count, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
		else
			return baseFormulaManager.computeSMA(endDate, count, unit, SourceType.SECURITY_PRICE, this.getID(), nav);
	}

	public double getEMA(Date startDate, Date endDate, int count, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeEMA(startDate, endDate, count, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getEMA(Date currentDate, int count, TimeUnit unit, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeSingleEMA(currentDate, count, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getMACD(Date startDate, Date endDate, TimeUnit tu, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeMACD(startDate, endDate, SourceType.SECURITY_RETURN, tu, this.getID(), nav);
	}

	public double getDEA(Date startDate, Date endDate, TimeUnit tu, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeDEA(startDate, endDate, SourceType.SECURITY_RETURN, tu, this.getID(), nav);
	}

	public double getTRIN(Date date) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeTRIN();
	}

	public MPT getMPTS(Date startDate, Date endDate, TimeUnit tu, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeMPTS(startDate, endDate, tu, SourceType.SECURITY_RETURN, this.getID(), false, nav);
	}

	public List<MPT> getEveryYearMPTS(TimeUnit tu, boolean nav) throws NoPriceException {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeEveryYearMPTs(tu, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	/** ******************************************************************************************************************************************** */
	public double getAlpha(int count, Date endDate, TimeUnit unit, boolean nav, String bench) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeAlpha(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav, bench);
	}

	/** ******************************************************************************************************************************************** */
	public double getAlpha(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeAlpha(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getBeta(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeBeta(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getRSquared(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeRSquared(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getSharpeRatio(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeSharpeRatio(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getTreynorRatio(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeTreynorRatio(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getStandardDeviation(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeStandardDeviation(startDate, endDate, unit, SourceType.SECURITY_RETURN, this.getID(), nav);
	}

	public double getDrawDown(int count, Date endDate, TimeUnit unit, boolean nav) throws NoPriceException {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeDrawDown(startDate, endDate, unit, SourceType.SECURITY_AMOUNT, this.getID(), nav);
	}

	public double getAnnualizedReturn(int count, Date endDate, TimeUnit unit, boolean nav) {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeAnnualizedReturn(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), 0, nav);
	}

	public double getAnnualReturn(int count, Date endDate, TimeUnit unit, boolean nav) {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		return baseFormulaManager.computeAnnualizedReturn(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), 1, nav);
	}

	public double getRSI(int count, Date endDate, TimeUnit unit, boolean nav) {
		Date startDate = LTIDate.getNewTradingDate(endDate, unit, count);
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		return baseFormulaManager.computeRSI(startDate, endDate, SourceType.SECURITY_AMOUNT, this.getID(), nav);
	}

	/** ******************************************************************************************************************************************** */

	public double getHighestAdjustPrice(Date startDate, Date endDate) throws SecurityException {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getHighestAdjPrice(this.getName(), startDate, endDate);
	}

	public double getHigh(Date curDate) throws NoPriceException {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getHigh(this.getID(), curDate);
	}

	public double getLow(Date curDate) throws NoPriceException {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getLow(this.getID(), curDate);
	}

	public double getLowestAdjustPrice(Date startDate, Date endDate) throws SecurityException {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getLowestAdjPrice(this.getID(), startDate, endDate);
	}

	public double getHighestPrice(Date startDate, Date endDate) throws SecurityException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getHighestPrice(this.getID(), startDate, endDate);
	}

	public double getLowestPrice(Date startDate, Date endDate) throws SecurityException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getLowestPrice(this.getID(), startDate, endDate);
	}

	public double getHighestNAVPrice(Date startDate, Date endDate) throws SecurityException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getHighestNAVPrice(this.getID(), startDate, endDate);
	}

	public double getLowestNAVPrice(Date startDate, Date endDate) throws SecurityException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		return securityManager.getLowestNAVPrice(this.getID(), startDate, endDate);
	}

	public boolean isEquity() {
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");

		try {
			long classID = this.getClassID();
			AssetClass ac = assetClassManager.get(classID);
			if (ac != null)
				return false;
			while (ac.getParentID() != null && ac.getParentID() != 0) {
				ac = assetClassManager.get(ac.getParentID());
			}
			if (ac.getName().toUpperCase().equals(com.lti.system.Configuration.EQUITY.toUpperCase()))
				return true;
			else
				return false;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// log.warn("[Security.isEquity()]Can not get the ClassType of the
			// security!");
			return false;
		}
	}

	public double getReturn(Date lastYear, Date curDate) throws SecurityException, NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		double sReturn = 0.0;
		double lastYearPrice = securityManager.getAdjPrice(this.ID, lastYear);
		double curDatePrice = securityManager.getAdjPrice(this.ID, curDate);
		if (lastYearPrice != 0.0)
			sReturn = (curDatePrice - lastYearPrice) / lastYearPrice;
		// System.out.println(sReturn);
		return sReturn;

	}

	public double getReturn(Date lastYear, Date curDate, boolean nav) throws SecurityException, NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		double sReturn = 0.0;
		double lastYearPrice;
		if (!nav)
			lastYearPrice = securityManager.getAdjPrice(this.ID, lastYear);
		else
			lastYearPrice = securityManager.getNAVPrice(this.ID, lastYear);

		double curDatePrice;
		if (!nav)
			curDatePrice = securityManager.getAdjPrice(this.ID, curDate);
		else
			curDatePrice = securityManager.getNAVPrice(this.ID, curDate);

		if (lastYearPrice != 0.0)
			sReturn = (curDatePrice - lastYearPrice) / lastYearPrice;
		// System.out.println(sReturn);
		return sReturn;

	}

	public double getReturn(TimePeriod p) throws SecurityException, NoPriceException {
		return getReturn(p.getStartDate(), p.getEndDate());
	}

	public double getReturn(Date curDate, TimeUnit tu, int count) throws NoPriceException {
		double sReturn;
		Date lastDate = new Date();
		if (tu == TimeUnit.DAILY) {
			lastDate = LTIDate.getNewNYSETradingDay(curDate, count);
		} else if (tu == TimeUnit.WEEKLY) {
			lastDate = LTIDate.getNewNYSEWeek(curDate, count);
		} else if (tu == TimeUnit.MONTHLY) {
			lastDate = LTIDate.getNewNYSEMonth(curDate, count);
		} else if (tu == TimeUnit.QUARTERLY) {
			lastDate = LTIDate.getNewNYSEQuarter(curDate, count);
		} else if (tu == TimeUnit.YEARLY) {
			lastDate = LTIDate.getNewNYSEYear(curDate, count);
		}
		try {
			sReturn = this.getReturn(lastDate, curDate);
		} catch (SecurityException e) {
			sReturn = 0.0;
			e.printStackTrace();
		}
		return sReturn;
	}

	public double getReturn(Date curDate, TimeUnit tu, int count, boolean nav) throws NoPriceException {
		double sReturn;
		Date lastDate = new Date();
		if (tu == TimeUnit.DAILY) {
			lastDate = LTIDate.getNewNYSETradingDay(curDate, count);
		} else if (tu == TimeUnit.WEEKLY) {
			lastDate = LTIDate.getNewNYSEWeek(curDate, count);
		} else if (tu == TimeUnit.MONTHLY) {
			lastDate = LTIDate.getNewNYSEMonth(curDate, count);
		} else if (tu == TimeUnit.QUARTERLY) {
			lastDate = LTIDate.getNewNYSEQuarter(curDate, count);
		} else if (tu == TimeUnit.YEARLY) {
			lastDate = LTIDate.getNewNYSEYear(curDate, count);
		}
		try {
			sReturn = this.getReturn(lastDate, curDate, nav);
		} catch (SecurityException e) {
			sReturn = 0.0;
			// e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return sReturn;
	}

	public double getTotalDivident(Date startDate, Date endDate) {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		double divident = 0.0;
		List<SecurityDailyData> dailydataList = new ArrayList<SecurityDailyData>();
		Interval interval = new Interval();
		interval.setStartDate(startDate);
		interval.setEndDate(endDate);
		// dailydataList = securityManager.getDailyDatas(this.getID(),
		// interval.getStartDate(),interval.getEndDate());
		dailydataList = null;
		dailydataList = securityManager.getDailyDatas(this.getID(), interval.getStartDate(), interval.getEndDate());

		int size = dailydataList.size();
		for (int i = 0; i < size; i++) {
			if (dailydataList.get(i).getDividend() != null) {
				divident += dailydataList.get(i).getDividend();
			}
		}
		return divident;
	}

	public java.lang.Double getCurrentPrice() {
		return CurrentPrice;
	}

	public double getDiscountRate(Date date) {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Double nav = 0.0;
		Double close = 0.0;
		SecurityDailyData se = securityManager.getLatestNAVDailydata(this.getID(), date);
		if (se == null) {
			SecurityException e = new SecurityException();
			e.setDate(date);
			e.setSecurityID(this.getID());
			e.setDetail(SecurityException.DATE_ERROR);
			return 0;
		}
		nav = se.getNAV();
		close = se.getClose();
		if (close == null || close == 0 || nav == null)
			return 0.0;
		else
			return (close - nav) / nav;
	}

	public double getAverageDiscountRate(int num, TimeUnit tu, Date endDate) {
		double average = 0;
		long sum = 0;
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(endDate);
		if (tu == TimeUnit.DAILY)
			tmpCa.add(Calendar.DAY_OF_YEAR, num);
		else if (tu == TimeUnit.MONTHLY)
			tmpCa.add(Calendar.MONTH, num);
		else if (tu == TimeUnit.WEEKLY)
			tmpCa.add(Calendar.WEEK_OF_YEAR, num);
		else if (tu == TimeUnit.YEARLY)
			tmpCa.add(Calendar.YEAR, num);
		Interval interval = new Interval();
		interval.setStartDate(tmpCa.getTime());
		interval.setEndDate(endDate);

		List<SecurityDailyData> list = null;
		list = securityManager.getDailyDatas(this.getID(), interval.getStartDate(), interval.getEndDate());

		for (int i = 0; i < list.size(); i++) {
			SecurityDailyData sd = list.get(i);
			if (sd != null) {
				if (sd.getNAV() != null && sd.getClose() != null && sd.getClose() != 0) {
					sum++;
					average += (sd.getClose() - sd.getNAV()) / sd.getNAV();
				}
			}
		}

		average = average / sum;
		return average;

	}

	public Double getAverageDiscountRate(Date startDate, Date endDate, TimeUnit tu) {
		double average = 0;
		long sum = 0;
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Interval interval = new Interval();
		interval.setStartDate(startDate);
		interval.setEndDate(endDate);

		List<SecurityDailyData> list = null;
		list = securityManager.getDailyDatas(this.getID(), interval.getStartDate(), interval.getEndDate());

		if (list == null || list.size() == 0)
			return null;

		Date curDate = list.get(0).getDate();

		for (int i = 0; i < list.size(); i++) {
			SecurityDailyData sd = list.get(i);

			if (sd == null)
				continue;

			Date date = sd.getDate();

			if (LTIDate.equals(date, curDate) || i == list.size() - 1 || (i != list.size() - 1 && list.get(i + 1).getDate().after(curDate) && date.before(curDate))) {

				if (sd.getNAV() != null && sd.getClose() != null && sd.getClose() != 0) {
					sum++;
					average += (sd.getClose() - sd.getNAV()) / sd.getNAV();
				}
				curDate = LTIDate.getNewTradingDate(curDate, tu, 1);
			}
		}

		if (sum != 0) {
			average = average / sum;
			return average;
		} else
			return null;

	}

	public AssetClass getAssetClass() {
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		return assetClassManager.get(this.getClassID());
	}

	public double getAdjClose(Date currentDate) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		try {
			return securityManager.getAdjPrice(this.ID, currentDate);
		} catch (NoPriceException e) {
			throw e;
		} catch (SecurityException se) {
			NoPriceException npe = new NoPriceException(se.getMessage(), se);
			npe.setDate(se.getDate());
			npe.setSecurityID(se.getSecurityID());
			npe.setSecurityName(se.getSecurityName());
			throw npe;
		}
	}

	public double getClose(Date currentDate) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		return securityManager.getPrice(this.ID, currentDate);
	}

	public double getNav(Date currentDate) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		try {
			return securityManager.getNAVPrice(this.ID, currentDate);
		} catch (SecurityException se) {
			NoPriceException npe = new NoPriceException(se.getMessage(), se);
			npe.setDate(se.getDate());
			npe.setSecurityID(se.getSecurityID());
			npe.setSecurityName(se.getSecurityName());
			throw npe;
		}

	}

	public double getAdjNav(Date currentDate) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		try {
			return securityManager.getAdjNAVPrice(this.ID, currentDate);
		} catch (SecurityException se) {
			NoPriceException npe = new NoPriceException(se.getMessage(), se);
			npe.setDate(se.getDate());
			npe.setSecurityID(se.getSecurityID());
			npe.setSecurityName(se.getSecurityName());
			throw npe;
		}
	}

	public boolean equals(Object o) {
		try {
			if (o instanceof java.lang.String) {
				if (this.Symbol.equals(o) || this.Name.equals(o))
					return true;
				else
					return false;
			} else if (o instanceof java.lang.Long) {
				if (this.ID.equals(o))
					return true;
				else
					return false;
			}
			if (o instanceof com.lti.service.bo.Security) {
				Security s = (com.lti.service.bo.Security) o;
				if (s.Symbol.equals(this.Symbol))
					return true;
				else
					return false;
			}
		} catch (RuntimeException e) {
		}
		return false;
	}

	public double getRS(String symbol, int Type, Date date) {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		try {
			return securityManager.getRSGrade(symbol, Type, date);
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	public static double getD(){
		Double d=null;
		return d;
	}

	public static void main(String[] args) throws Exception{
		Security s=ContextHolder.getSecurityManager().get(735l);
		Double d=getD();
		System.out.println();
		System.out.println(s.getAdjNav(LTIDate.getDate(2010, 3, 24)));
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		/*
		 * Date startDate = LTIDate.getDate(2006, 5, 31); Date endDate =
		 * LTIDate.getDate(2007, 12, 31);
		 * 
		 * Security se = securityManager.get(557L); try {
		 * System.out.println(se.getDiscountRate(startDate)); } catch (Exception
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } /*long
		 * t3 = System.currentTimeMillis();
		 * System.out.println(baseFormulaManager.computeIRR_OldVersion(1041L,
		 * startDate, endDate, 10000.0, 84350.0)); long t1 =
		 * System.currentTimeMillis(); System.out.println(t1-t3);
		 * System.out.println(baseFormulaManager.computeIRR(1041L, startDate,
		 * endDate, 10000.0, 84350.0)); long t2 = System.currentTimeMillis();
		 * System.out.println(t2-t1);
		 */
		Date startDate = LTIDate.getDate(2007, 4, 8);
		Date endDate = LTIDate.getDate(2009, 3, 4);

		try {
			// for(long k=100;k<150;k++)
			{
				// System.out.println(k);
				Security se = securityManager.getBySymbol("ZTR");
				long a = System.currentTimeMillis();
				System.out.println(se.getAverageDiscountRate(startDate, endDate, TimeUnit.WEEKLY));
				long b = System.currentTimeMillis();
				System.out.println(b - a);
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
		}
		// securityManager.getAllSecurityMPT(false);
	}

	public java.lang.String getSecurityTypeName() {
		return securityTypeName;
	}

	public void setSecurityTypeName(java.lang.String securityTypeName) {
		this.securityTypeName = securityTypeName;
	}

	public java.lang.String getClassName() {
		return className;
	}

	public void setClassName(java.lang.String className) {
		this.className = className;
	}

	public java.util.Date getMptLastDate() {
		return mptLastDate;
	}

	public void setMptLastDate(java.util.Date mptLastDate) {
		this.mptLastDate = mptLastDate;
	}

}
