package com.lti.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.service.AssetClassManager;
import com.lti.service.BaseFormulaManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager; //import com.lti.service.bo.FundAlert;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.SecurityMPTIncData;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.type.MPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.ConfidenceCheck;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.LTINumber;
import com.lti.type.Mycomparator;
import com.lti.type.ReturnComparator;
import com.lti.type.SecurityValue;
import com.lti.type.SourceType;
import com.lti.type.TimePara;
import com.lti.type.TimeUnit;
import com.lti.type.finance.HoldingInf;
import com.lti.util.CopyUtil;
import com.lti.util.LTIDate;
import com.lti.util.LTIIRR;
import com.lti.util.StringUtil;

public class BaseFormulaManagerImpl implements BaseFormulaManager, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double rate = 0.04546;

	private double lastAmount = 0;
	private double thisAmount = 0;
	private boolean firstYear = true;

	private static Security Cash;

	private static Security IRR;

	private static Security getCash() {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		if (Cash == null)
			Cash = securityManager.get(Configuration.getCashSymbol());

		return Cash;
	}

	private static Security getIRR() {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		if (Cash == null)
			Cash = securityManager.get("^IRR");

		return Cash;
	}

	static org.apache.commons.logging.Log log = LogFactory.getLog(BaseFormulaManagerImpl.class);

	public double computeAlpha(double beta, double portfolioReturn, double benchmarkReturn) {

		if (isINF(beta) || isINF(portfolioReturn) || isINF(benchmarkReturn))
			return LTINumber.INF;

		return portfolioReturn - beta * benchmarkReturn;

	}

	private boolean isZero(double num) {
		if (Math.abs(num - 0.0) <= LTINumber.ZERO)
			return true;
		else
			return false;
	}

	private boolean isINF(double num) {
		if (Math.abs(num) >= LTINumber.INF)
			return true;
		else
			return false;
	}

	public double computeAnnualizedReturn(double currentPrice, double initialPrice, int days) {
		if (isZero(days))
			return LTINumber.INF;
		double rate = 365.0 / days;
		if (isZero(initialPrice))
			return 0;
		return Math.pow((currentPrice / initialPrice), rate) - 1;
	}

	public double computeBeta(List<Double> portfolioReturns, List<Double> benchmarkReturns) {

		if (portfolioReturns == null || benchmarkReturns == null || portfolioReturns.size() < 1 || benchmarkReturns.size() < 1)
			return LTINumber.INF;

		if (portfolioReturns.size() != benchmarkReturns.size())
			return LTINumber.INF;

		int n = portfolioReturns.size();

		Iterator<Double> itPort = portfolioReturns.iterator();

		Iterator<Double> itBenchmark = benchmarkReturns.iterator();

		double sumPortfolio = 0;

		double sumPortAndBench = 0;

		double sumBenchmark = 0;

		double sumBenchSquared = 0;

		while (itPort.hasNext()) {
			double tmp1 = (Double) itPort.next();

			sumPortfolio += tmp1;

			double tmp2 = (Double) itBenchmark.next();

			sumBenchmark += tmp2;

			sumPortAndBench += tmp1 * tmp2;

			sumBenchSquared += tmp2 * tmp2;
		}
		double test = n * sumBenchSquared - sumBenchmark * sumBenchmark;

		if (isZero(test))
			return LTINumber.INF;

		return (n * sumPortAndBench - sumBenchmark * sumPortfolio) / (n * sumBenchSquared - sumBenchmark * sumBenchmark);
	}

	public double computeCurrentPercetage(double subAmount, double totalAmount) {
		// TODO Auto-generated method stub
		if (isZero(totalAmount))
			return LTINumber.INF;

		return subAmount / totalAmount;
	}

	public double computeEMA(double closePrice, int days, double lastEMA) {
		// TODO Auto-generated method stub
		if (days <= 1)
			return LTINumber.INF;

		return closePrice * 2 / (days - 1) + lastEMA * days / (days + 1);
	}

	public double computeRatio(double portfolioReturn, double riskFree, double portfolioDeviatio) {
		// TODO Auto-generated method stub
		if (isZero(portfolioDeviatio))
			return LTINumber.INF;

		return (portfolioReturn - riskFree) / portfolioDeviatio;
	}

	public double computeRSI(double returnA, double returnB) {
		// TODO Auto-generated method stub
		if (isZero(returnA + returnB))
			return LTINumber.INF;

		return 100 * returnA / (returnA + returnB);
	}

	public double computeSMA(List<Double> closePrices) {
		// TODO Auto-generated method stub
		double sum = 0;

		Iterator<Double> it = closePrices.iterator();

		while (it.hasNext()) {
			double tmp = (Double) it.next();

			sum += tmp;
		}
		if (isZero(closePrices.size()))
			return LTINumber.INF;

		return sum / closePrices.size();
	}

	public double computeCorrelationCoefficient(List<Double> valsOne, List<Double> valsTwo) {
		if (valsOne == null || valsTwo == null || valsOne.size() <= 1 || valsTwo.size() <= 1 || valsOne.size() != valsTwo.size())
			return 0;

		double sdA = computeStandardDeviation(valsOne);
		double sdB = computeStandardDeviation(valsTwo);
		double co = computeCovariance(valsOne, valsTwo);

		if (isZero(sdA * sdB) || isZero(co))
			return LTINumber.INF;

		return co / ((sdA * sdB));
	}

	public double computeCovariance(List<Double> valsOne, List<Double> valsTwo) {
		// TODO Auto-generated method stub
		if (valsOne.size() != valsTwo.size())
			return LTINumber.INF;

		int n = valsOne.size();

		if (n == 0)
			return LTINumber.INF;

		Iterator<Double> iterOne = valsOne.iterator();

		Iterator<Double> iterTwo = valsTwo.iterator();

		double averOne = 0;

		double averTwo = 0;

		while (iterOne.hasNext()) {
			averOne += (Double) iterOne.next();

		}
		while (iterTwo.hasNext()) {
			averTwo += (Double) iterTwo.next();
		}

		averOne /= n;

		averTwo /= n;

		iterOne = valsOne.iterator();

		iterTwo = valsTwo.iterator();

		double tmpOne = 0;

		double tmpTwo = 0;

		double sum = 0;

		while (iterOne.hasNext()) {
			tmpOne = (Double) iterOne.next();

			tmpTwo = (Double) iterTwo.next();

			sum += (tmpOne - averOne) * (tmpTwo - averTwo);
		}
		if (n == 1)
			return LTINumber.INF;

		return sum / (n - 1);
	}

	public double computeIntervalReturn(double current, double last) {
		// TODO Auto-generated method stub
		if (isZero(last))
			return 0.0;

		return (current - last) / last;
	}

	public double computeLogIntervalReturn(double current, double last) {
		// TODO Auto-generated method stub
		if (isZero(last))
			return 0.0;

		return Math.log(current / last);
	}

	public double computeSecurityWeight(double currentPrice, double shares, double totalAmount) {
		// TODO Auto-generated method stub
		if (isZero(totalAmount) || totalAmount <= 0)
			return LTINumber.INF;

		return currentPrice * shares / totalAmount;
	}

	public double computeStandardDeviation(List<Double> values) {
		// TODO Auto-generated method stub
		if (values.size() == 0)
			// return LTINumber.INF;
			return 0;

		if (values.size() == 1)
			return 0;

		double sum = 0;

		Iterator<Double> iter = values.iterator();

		double tmp = 0;

		double aver = 0;

		while (iter.hasNext()) {
			tmp = (Double) iter.next();

			aver += tmp;
		}
		aver /= values.size();

		iter = values.iterator();

		while (iter.hasNext()) {
			tmp = (Double) iter.next();

			sum += (tmp - aver) * (tmp - aver);
		}
		if (sum / (values.size() - 1) <= 0.0)
			return LTINumber.INF;

		return Math.sqrt(sum / (values.size() - 1));
	}

	public double computeAnnulizedStandardDeviation(List<Double> values, TimeUnit tu) {

		for (int i = 0; i < values.size(); i++) {
			double tmp = values.get(i);
			tmp = Math.log(1 + tmp);
			values.set(i, tmp);
		}

		double sd = this.computeStandardDeviation(values);

		if (isINF(sd))
			return sd;
		if (isZero(sd))
			return sd;

		if (tu == TimeUnit.DAILY) {
			sd = sd * Math.pow(TimePara.workingday, 0.5);
		} else if (tu == TimeUnit.WEEKLY) {
			sd = sd * Math.pow(52, 0.5);
		} else if (tu == TimeUnit.MONTHLY) {
			sd = sd * Math.pow(12, 0.5);
		} else if (tu == TimeUnit.QUARTERLY) {
			sd = sd * Math.pow(4, 0.5);
		}
		// sd = sd * Math.pow(TimePara.workingday, 0.5);

		return sd;

	}

	public double computeRiskFromYearly(double yearlyValue, int n) {
		if (n <= 0)
			return LTINumber.INF;

		double confidence = 1.0 / n;

		return Math.pow(yearlyValue + 1, confidence);
	}

	public double computeIntervalReturnToAnnulized(double intervalReturn, double factor) {
		return intervalReturn * factor;
	}

	public double computeTRIN(double Nr, double Nd, double Vr, double Vd) {
		if (isZero(Vd) || isZero(Vr) || isZero(Nd))
			return LTINumber.INF;
		return (Nr / Nd) / (Vr / Vd);
	}

	public double computeAverage(List<Double> source) {
		double sum = 0.0;

		if (source == null)
			return sum;
		if (source.size() == 0)
			return sum;

		Iterator<Double> iter = source.iterator();

		while (iter.hasNext()) {
			sum += (Double) iter.next();
		}
		if (source == null || source.size() == 0)
			return LTINumber.INF;

		return sum / source.size();
	}

	public List<Double> computePeriodReturn(List<Double> source) {
		List<Double> periodReturns = new ArrayList<Double>();

		Iterator<Double> iter = source.iterator();

		double pre = (Double) iter.next();

		double current = 0.0;

		while (iter.hasNext()) {
			current = (Double) iter.next();

			periodReturns.add(computeIntervalReturn(current, pre));

			pre = current;
		}
		return periodReturns;
	}

	public double computeDrawDown(List<Double> portAmounts) {
		double drawDown = 0;

		if (portAmounts == null || portAmounts.size() == 0)
			return drawDown;

		Iterator<Double> iter = portAmounts.iterator();

		double maxVal = (Double) iter.next();

		while (iter.hasNext()) {
			double currentVal = (Double) iter.next();

			double temp = (maxVal - currentVal) / maxVal;

			drawDown = drawDown > temp ? drawDown : temp;

			maxVal = maxVal > currentVal ? maxVal : currentVal;
		}
		return drawDown;
	}

	public double computeAnnualizedReturn(List<Double> portfolioList, int interval) {
		if (isZero(interval))
			return LTINumber.INF;

		double annualizedReturn = 0;

		double currentPrice = 0;

		double initialPrice = 0;

		int size = portfolioList.size();

		if (size == 0)
			return LTINumber.INF;

		for (int i = 0; i < size; i++) {
			if (portfolioList.get(i) != null) {
				initialPrice = portfolioList.get(i);

				break;
			}
		}

		for (int i = size - 1; i >= 0; i--) {
			if (portfolioList.get(i) != null) {
				currentPrice = portfolioList.get(i);

				break;
			}
		}

		if (initialPrice == 0.0)
			return LTINumber.INF;

		annualizedReturn = Math.pow(currentPrice / initialPrice, TimePara.workingday / (interval * 1.0)) - 1;// 1.0*interval))-1;//0.25)-1;

		return annualizedReturn;
	}

	public List<Double> getTimeUnitReturns(List<Double> dailydataList) {
		List<Double> portReturns = new ArrayList<Double>();

		int size = dailydataList.size();

		double lastValue = 0;

		double preValue = 0;

		double currentValue;

		java.lang.Boolean foundFirst = false;

		for (int i = 0; i < size; i++) {
			if (foundFirst == false) {
				if (dailydataList.get(i) == null) {
					preValue = lastValue;
				} else {
					preValue = dailydataList.get(i);

					lastValue = preValue;
				}
				foundFirst = true;
			} else {

				if (dailydataList.get(i) == null) {
					currentValue = lastValue;
				} else {
					currentValue = dailydataList.get(i);

					lastValue = currentValue;
				}
				portReturns.add(this.computeIntervalReturn(currentValue, preValue));

				preValue = currentValue;
			}
		}

		return portReturns;
	}

	public double getRiskFree(Date startDate, Date endDate) throws NoPriceException {
		double riskFree = 0;

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		// Security se = securityManager.get("CASH");
		Security se = getCash();

		double preValue = se.getAdjClose(startDate);
		double curValue = se.getAdjClose(endDate);

		// riskFree = curValue/preValue - 1;
		riskFree = this.computeIntervalReturn(curValue, preValue);

		return riskFree;
	}

	public double getAnnualizedRiskFree(Date startDate, Date endDate) throws NoPriceException {
		double riskFree = 0;

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		// Security se = securityManager.get("CASH");
		Security se = getCash();

		List<SecurityDailyData> sdds = securityManager.getDailyDatas(se.getID(), startDate, endDate);

		if (sdds == null || sdds.size() == 0)
			return riskFree;

		double preValue = sdds.get(0).getAdjClose();
		double curValue = sdds.get(sdds.size() - 1).getAdjClose();

		if (preValue != 0.0)
			riskFree = curValue / preValue;
		else
			return 0.0;

		int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		if (interval == 0)
			return riskFree;

		riskFree = Math.pow(riskFree, TimePara.workingday * 1.0 / interval) - 1;

		return riskFree;
	}

	public double getAnnualizedRiskFree(List<Double> sdds, Date startDate, Date endDate) throws NoPriceException {
		double riskFree = 0;

		if (sdds == null || sdds.size() == 0)
			return riskFree;

		double preValue = sdds.get(0);
		double curValue = sdds.get(sdds.size() - 1);

		if (preValue != 0.0)
			riskFree = curValue / preValue;
		else
			return 0.0;

		int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		if (interval == 0)
			return riskFree;

		riskFree = Math.pow(riskFree, TimePara.workingday * 1.0 / interval) - 1;

		return riskFree;
	}

	public double getRiskFree(TimeUnit unit) {
		return rate;
	}

	public double getAverageRiskFree(Date startDate, Date endDate) {
		double riskFree = 0;

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		// Security se = securityManager.get("CASH");
		Security se = getCash();

		List<SecurityDailyData> sdds = securityManager.getDailyDatas(se.getID(), startDate, endDate);

		if (sdds == null || sdds.size() == 0)
			return riskFree;

		double pre = -1, cur = -1;

		int count = 0;

		for (int i = 0; i < sdds.size(); i++) {
			SecurityDailyData sdd = sdds.get(i);
			if (sdd == null || sdd.getAdjClose() == null)
				continue;
			// riskFree += sdd.getAdjClose();
			if (pre == -1) {
				pre = sdd.getAdjClose();
				continue;
			} else {
				cur = sdd.getAdjClose();
				riskFree += this.computeIntervalReturn(cur, pre);// cur/pre -
				// 1;
				count++;
				pre = cur;
			}
		}

		riskFree = riskFree / count;

		return riskFree;
	}

	public double getAverageRiskFree2(Date startDate, Date endDate) throws NoPriceException {
		double riskFree = 0;

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Security se = getIRR();

		riskFree = se.getAdjClose(endDate);

		riskFree = Math.pow((1 + riskFree), TimePara.yearday) - 1;
		return riskFree;
	}

	/**
	 * *************************************************************************
	 * ********************************************************
	 */

	/**
	 * *************************************************************************
	 * ********************************************************
	 */

	/**
	 * general mpt api,both security and portfolio can use add by chaos
	 * 2008-2-29
	 */
	/**
	 * *************************************************************************
	 * *********************************************************
	 */

	public List<Double> getReturns(Date startDate, Date endDate, TimeUnit tu, long id, SourceType st, boolean nav) throws NoPriceException {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Double> Returns = new ArrayList<Double>();

		Security se = new Security();
		Portfolio pf = new Portfolio();

		Date lastDate = new Date();

		if (st == SourceType.PORTFOLIO_RETURN || st == SourceType.PORTFOLIO_AMOUNT) {
			pf = portfolioManager.get(id);
			lastDate = pf.getEndDate();
		}

		else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
			se = securityManager.get(id);
			lastDate = securityManager.getEndDate(id);
		}

		else if (st == SourceType.BENCHMARK_AMOUNT || st == SourceType.BENCHMARK_RETURN) {
			se = securityManager.get(id);

			se = securityManager.get(se.getAssetClass().getBenchmarkID());

			if (se == null)
				return null;

			lastDate = securityManager.getEndDate(id);
		}

		Date curDate = new Date();

		curDate = startDate;

		if (tu == TimeUnit.DAILY) {
			if (!LTIDate.isNYSETradingDay(startDate)) {
				curDate = LTIDate.getNewNYSETradingDay(startDate, 1);
			}
		}

		else if (tu == TimeUnit.WEEKLY) {
			curDate = LTIDate.getLastNYSETradingDayOfWeek(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEWeek(curDate, 1);
			}
		}

		else if (tu == TimeUnit.MONTHLY) {
			curDate = LTIDate.getLastNYSETradingDayOfMonth(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEMonth(curDate, 1);
			}
		}

		else if (tu == TimeUnit.QUARTERLY) {
			curDate = LTIDate.getLastNYSETradingDayOfQuarter(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEQuarter(curDate, 1);
			}
		}

		else if (tu == TimeUnit.YEARLY) {
			curDate = LTIDate.getLastNYSETradingDayOfYear(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEYear(curDate, 1);
			}
		}

		double curValue = 0;

		double preValue = 0;

		double singleReturn = 0;

		boolean firstone = true;

		while (true) {
			if (curDate.after(endDate))
				break;

			if (firstone) {
				if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
					try {
						// preValue = pf.getTotalAmount(curDate);
						preValue = portfolioManager.getDailyDataIngoreError(id, curDate).getAmount();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						preValue = 0;
						System.out.println("NO Amount on this date!!");
						// e.printStackTrace();
					}
				} else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
					if (!nav)
						preValue = se.getAdjClose(curDate);
					else
						preValue = se.getAdjNav(curDate);
				} else if (st == SourceType.BENCHMARK_AMOUNT || st == SourceType.BENCHMARK_RETURN) {
					if (!nav)
						preValue = se.getAdjClose(curDate);
					else
						preValue = se.getAdjNav(curDate);
				}
				if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.SECURITY_AMOUNT || st == SourceType.BENCHMARK_AMOUNT) {
					Returns.add(preValue);
				}

				firstone = !firstone;
			} else {
				if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
					try {
						// curValue = pf.getTotalAmount(curDate);
						curValue = portfolioManager.getDailyDataIngoreError(id, curDate).getAmount();
					} catch (Exception e) {
						curValue = preValue;
						System.out.println("NO Amount on this date!!");
						// e.printStackTrace();
					}
				} else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
					if (!nav)
						curValue = se.getAdjClose(curDate);
					else
						curValue = se.getAdjNav(curDate);
				}

				else if (st == SourceType.BENCHMARK_AMOUNT || st == SourceType.BENCHMARK_RETURN) {
					if (!nav)
						curValue = se.getAdjClose(curDate);
					else
						curValue = se.getAdjNav(curDate);
				}

				if (st == SourceType.PORTFOLIO_RETURN || st == SourceType.SECURITY_RETURN || st == SourceType.BENCHMARK_RETURN) {
					singleReturn = this.computeIntervalReturn(curValue, preValue);
					Returns.add(singleReturn);
				}

				else if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.SECURITY_AMOUNT || st == SourceType.BENCHMARK_AMOUNT) {
					Returns.add(curValue);
				}

				preValue = curValue;

			}

			Date preDate = curDate;

			if (tu == TimeUnit.DAILY) {
				curDate = LTIDate.getNewNYSETradingDay(curDate, 1);
			}

			else if (tu == TimeUnit.WEEKLY) {
				// Date preDate = curDate;
				curDate = LTIDate.getNewNYSEWeek(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfWeek(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate)) {
						curDate = LTIDate.getRecentNYSETradingDayForward(endDate);
					}
				}
			}

			else if (tu == TimeUnit.MONTHLY) {
				// Date preDate = curDate;
				curDate = LTIDate.getNewNYSEMonth(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfMonth(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate)) {
						curDate = LTIDate.getRecentNYSETradingDayForward(endDate);
					}
				}

			}

			else if (tu == TimeUnit.QUARTERLY) {
				// Date preDate = curDate;
				curDate = LTIDate.getNewNYSEQuarter(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfQuarter(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate)) {
						curDate = LTIDate.getRecentNYSETradingDayForward(endDate);
					}
				}
			}

			else if (tu == TimeUnit.YEARLY) {
				preDate = curDate;
				curDate = LTIDate.getNewNYSEYear(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfYear(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate)) {
						curDate = LTIDate.getRecentNYSETradingDayForward(endDate);
					}
				}
			}

			if (curDate.after(lastDate) || LTIDate.equals(curDate, preDate))
				break;
		}
		return Returns;
	}

	/**
	 * added by CCD on 2009-11-10
	 * 
	 * @author CCD
	 * @param securityList
	 */
	public List<Double> getReturns(List<SecurityDailyData> securityList) {
		List<Double> returns = new ArrayList<Double>();

		int size = securityList.size();
		double prePrice, curPrice;
		prePrice = securityList.get(0).getAdjClose();
		for (int i = 0; i < size; ++i) {
			curPrice = securityList.get(i).getAdjClose();
			returns.add(this.computeIntervalReturn(curPrice, prePrice));
			prePrice = curPrice;
		}
		return returns;
	}

	public List<Double> getAmounts(Date startDate, Date endDate, TimeUnit tu, long id, SourceType st, boolean nav) throws NoPriceException {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Double> Returns = new ArrayList<Double>();

		Security se = new Security();

		Portfolio pf = new Portfolio();

		if (st == SourceType.PORTFOLIO_RETURN || st == SourceType.PORTFOLIO_AMOUNT) {
			pf = portfolioManager.get(id);
		}

		else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN || st == SourceType.SECURITY_PRICE) {
			se = securityManager.get(id);
		}

		else if (st == SourceType.BENCHMARK_AMOUNT || st == SourceType.BENCHMARK_RETURN) {
			se = securityManager.get(id);
			se = securityManager.get(se.getAssetClass().getBenchmarkID());
		}

		Date curDate = new Date();

		curDate = startDate;

		if (tu == TimeUnit.DAILY) {
			if (!LTIDate.isNYSETradingDay(startDate)) {
				curDate = LTIDate.getNewNYSETradingDay(startDate, 1);
			}
		}

		else if (tu == TimeUnit.WEEKLY) {
			curDate = LTIDate.getLastNYSETradingDayOfWeek(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEWeek(curDate, 1);
			}
		}

		else if (tu == TimeUnit.MONTHLY) {
			curDate = LTIDate.getLastNYSETradingDayOfMonth(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEMonth(curDate, 1);
			}
		}

		else if (tu == TimeUnit.QUARTERLY) {
			curDate = LTIDate.getLastNYSETradingDayOfQuarter(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEQuarter(curDate, 1);
			}
		}

		else if (tu == TimeUnit.YEARLY) {
			curDate = LTIDate.getLastNYSETradingDayOfYear(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEYear(curDate, 1);
			}
		}

		double value = 0;

		while (true) {
			if (curDate.after(endDate))
				break;

			if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
				try {
					// preValue = pf.getTotalAmount(curDate);
					value = portfolioManager.getDailydata(id, curDate).getAmount();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					value = 0;
					System.out.println("NO Amount on this date!!");
					// e.printStackTrace();
				}
			} else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
				if (!nav)
					value = se.getAdjClose(curDate);
				else
					value = se.getAdjNav(curDate);
			} else if (st == SourceType.BENCHMARK_AMOUNT || st == SourceType.BENCHMARK_RETURN) {
				if (!nav)
					value = se.getAdjClose(curDate);
				else
					value = se.getAdjNav(curDate);
			} else if (st == SourceType.SECURITY_PRICE) {
				try {
					value = se.getCurrentPrice(curDate);
				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
					// e.printStackTrace();
					value = 0.0;
				}
			}

			Returns.add(value);

			if (tu == TimeUnit.DAILY) {
				curDate = LTIDate.getNewNYSETradingDay(curDate, 1);
			}

			else if (tu == TimeUnit.WEEKLY) {
				Date preDate = curDate;
				curDate = LTIDate.getNewNYSEWeek(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfWeek(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate))
						curDate = endDate;
				}
			}

			else if (tu == TimeUnit.MONTHLY) {
				Date preDate = curDate;
				curDate = LTIDate.getNewNYSEMonth(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfMonth(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate))
						curDate = endDate;
				}

			}

			else if (tu == TimeUnit.QUARTERLY) {
				Date preDate = curDate;
				curDate = LTIDate.getNewNYSEQuarter(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfQuarter(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate))
						curDate = endDate;
				}
			}

			else if (tu == TimeUnit.YEARLY) {
				Date preDate = curDate;
				curDate = LTIDate.getNewNYSEYear(curDate, 1);
				curDate = LTIDate.getLastNYSETradingDayOfYear(curDate);
				if (curDate.after(endDate)) {
					if (!LTIDate.equals(preDate, endDate))
						curDate = endDate;
				}
			}
		}
		return Returns;
	}

	public double computeBeta(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		double beta = 0;

		List<Double> portReturns = new ArrayList<Double>();

		List<Double> benchReturns = new ArrayList<Double>();

		if (st == SourceType.PORTFOLIO_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.PORTFOLIO_RETURN, false);

			benchReturns = this.getReturns(startDate, endDate, tu, portfolioManager.getBenchmarkID(id), SourceType.SECURITY_RETURN, false);
		}

		else if (st == SourceType.SECURITY_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.SECURITY_RETURN, nav);

			benchReturns = this.getReturns(startDate, endDate, tu, id, SourceType.BENCHMARK_RETURN, nav);
		}

		beta = this.computeBeta(portReturns, benchReturns);

		return beta;
	}

	public double computeAlpha(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav, String bench) throws NoPriceException {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Security se = securityManager.get(id);

		Security benchMark = securityManager.get(bench);

		if (se == null)
			return -LTINumber.INF;

		long benchID = benchMark.getID();

		double beta = 0;

		List<Double> portReturns = new ArrayList<Double>();

		List<Double> benchReturns = new ArrayList<Double>();

		portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.SECURITY_RETURN, nav);

		benchReturns = this.getReturns(startDate, endDate, tu, benchID, SourceType.SECURITY_RETURN, nav);

		beta = this.computeBeta(portReturns, benchReturns);

		double averageRiskFree = this.getAverageRiskFree(startDate, endDate);

		double averagePort = this.computeAverage(portReturns) - averageRiskFree;

		double averageBench = this.computeAverage(benchReturns) - averageRiskFree;

		return this.computeAlpha(beta, averagePort, averageBench);
	}

	public double computeAlpha(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, Long benchMarkID, boolean nav) throws NoPriceException {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		double beta = 0;
		List<Double> portReturns = new ArrayList<Double>();
		List<Double> benchReturns = new ArrayList<Double>();
		if (st == SourceType.PORTFOLIO_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.PORTFOLIO_RETURN, false);

			benchReturns = this.getReturns(startDate, endDate, tu, portfolioManager.getBenchmarkID(id), SourceType.SECURITY_RETURN, false);
		}

		else if (st == SourceType.SECURITY_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.SECURITY_RETURN, nav);

			if (benchMarkID == null)
				benchReturns = this.getReturns(startDate, endDate, tu, id, SourceType.BENCHMARK_RETURN, nav);
			else
				benchReturns = this.getReturns(startDate, endDate, tu, benchMarkID, SourceType.SECURITY_RETURN, nav);
		}
		beta = this.computeBeta(portReturns, benchReturns);

		double averageRiskFree = this.getAverageRiskFree(startDate, endDate);

		double averagePort = this.computeAverage(portReturns) - averageRiskFree;

		double averageBench = this.computeAverage(benchReturns) - averageRiskFree;

		return this.computeAlpha(beta, averagePort, averageBench);
	}

	public double computeAlpha(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {
		return computeAlpha(startDate, endDate, tu, st, id, null, nav);
	}

	public double computeRSquared(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		double RS = 0;

		List<Double> portReturns = new ArrayList<Double>();

		List<Double> benchReturns = new ArrayList<Double>();

		if (st == SourceType.PORTFOLIO_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.PORTFOLIO_RETURN, false);

			benchReturns = this.getReturns(startDate, endDate, tu, portfolioManager.getBenchmarkID(id), SourceType.SECURITY_RETURN, false);
		}

		else if (st == SourceType.SECURITY_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.SECURITY_RETURN, nav);

			benchReturns = this.getReturns(startDate, endDate, tu, id, SourceType.BENCHMARK_RETURN, nav);
		}

		RS = this.computeCorrelationCoefficient(portReturns, benchReturns);

		return RS * RS;
	}

	public double computeTreynorRatio(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		double treynorRatio = 0;

		double beta = 0;

		List<Double> portReturns = new ArrayList<Double>();

		List<Double> benchReturns = new ArrayList<Double>();

		if (st == SourceType.PORTFOLIO_RETURN) {
			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.PORTFOLIO_RETURN, false);

			benchReturns = this.getReturns(startDate, endDate, tu, portfolioManager.getBenchmarkID(id), SourceType.SECURITY_RETURN, false);
		}

		else if (st == SourceType.SECURITY_RETURN) {

			portReturns = this.getReturns(startDate, endDate, tu, id, SourceType.SECURITY_RETURN, nav);

			benchReturns = this.getReturns(startDate, endDate, tu, id, SourceType.BENCHMARK_RETURN, nav);
		}

		beta = this.computeBeta(portReturns, benchReturns);

		double annualizedRiskFree = this.getAnnualizedRiskFree(startDate, endDate);

		// double average = this.computeAverage(portReturns);

		double totalReturn = this.computeAnnualizedReturn(startDate, endDate, st, id, 1, nav);

		// treynorRatio = (average-riskFree)/beta;

		treynorRatio = this.computeRatio(totalReturn, annualizedRiskFree, beta);

		return treynorRatio;
	}

	public double computeStandardDeviation(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		double standardDeviation = 0;

		List<Double> returns = this.getReturns(startDate, endDate, tu, id, st, nav);

		standardDeviation = this.computeStandardDeviation(returns);

		return standardDeviation;
	}

	public double computeSharpeRatio(List<Double> seList) {
		return 0;
	}

	public double computeSharpeRatio(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		double sharpeRatio;

		if (st == SourceType.BENCHMARK_AMOUNT || st == SourceType.BENCHMARK_RETURN || st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
			List<SecurityDailyData> seList = securityManager.getDailyDatas(id, startDate, endDate);
			/*
			 * for(int i = 0;i<seList.size();i++){
			 * if(!nav)amounts.add((Double)seList.get(i).getAdjClose()); else
			 * amounts.add((Double)seList.get(i).getAdjNAV()); }
			 */
			if (seList == null)
				return -1 * 500;
			if (seList.size() == 0)
				return -1 * 500;
			if (seList.get(0).getDate().before(startDate))
				return -1 * 500;
		} else if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
			List<PortfolioDailyData> poList = portfolioManager.getDailydatasByPeriod(id, startDate, endDate);
			/*
			 * for(int i = 0;i<poList.size();i++){
			 * amounts.add((Double)poList.get(i).getAmount()); }
			 */
			if (poList == null)
				return -1 * 500;
			if (poList.size() == 0)
				return -1 * 500;
			if (poList.get(0).getDate().before(startDate))
				return -1 * 500;
		}

		List<Double> Returns = this.getReturns(startDate, endDate, tu, id, st, nav);
		double totalReturn = this.computeAnnualizedReturn(startDate, endDate, st, id, 1, nav);
		/* double totalReturn = this.computeTotalReturn(amounts); */

		// if(amounts.size()<Returns.size())return -1*LTINumber.INF;
		double devsecurity = this.computeAnnulizedStandardDeviation(Returns, tu);
		double annulizedRiskFree = this.getAnnualizedRiskFree(startDate, endDate);

		/*
		 * int days = LTIDate.calculateInterval(startDate, endDate);
		 * 
		 * double annulizedRiskFree = Math.pow(riskFree+1,
		 * TimePara.yearday/(1.0*days))-1;
		 */

		sharpeRatio = this.computeRatio(totalReturn, annulizedRiskFree, devsecurity);

		if (this.isINF(sharpeRatio))
			return -1 * 500;
		else
			return sharpeRatio;
	}

	public double computeDrawDown(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		double drawDown = 0;

		List<Double> amounts = this.getReturns(startDate, endDate, tu, id, st, nav);

		Iterator<Double> iter = amounts.iterator();

		double maxVal = (Double) iter.next();

		while (iter.hasNext()) {
			double currentVal = (Double) iter.next();

			double temp = (maxVal - currentVal) / maxVal;

			drawDown = drawDown > temp ? drawDown : temp;

			maxVal = maxVal > currentVal ? maxVal : currentVal;
		}
		return drawDown;
	}

	/*
	 * public double getPortfolioAmount(Date date,int id){ PortfolioManager
	 * portfolioManager = (PortfolioManager)
	 * ContextHolder.getInstance().getApplicationContext
	 * ().getBean("portfolioManager"); List<PortfolioDailyData> pd =
	 * portfolioManager.get.getDailydatas(id);
	 * if(date.equals(pd.get(0).getDate()))return pd.get(0).getAmount(); for(int
	 * i=1;i<pd.size();i++){ if(date.equals(pd.get(i).getDate()))return
	 * pd.get(i).getAmount(); if(date) } }
	 * 
	 * public double computeAnnualizedReturn(int year,SourceType st, long id){
	 * Date thisYear = LTIDate.getDate(year, 12, 31); Date lastYear =
	 * LTIDate.getDate(year-1, 12, 31);
	 * 
	 * Date startDate = new Date();
	 * 
	 * PortfolioManager portfolioManager = (PortfolioManager)
	 * ContextHolder.getInstance
	 * ().getApplicationContext().getBean("portfolioManager");
	 * 
	 * SecurityManager securityManager=(SecurityManager)
	 * ContextHolder.getInstance
	 * ().getApplicationContext().getBean("securityManager");
	 * 
	 * double thisAmount = 0; double lastAmount = 0; double annualizedReturn =
	 * 0;
	 * 
	 * if(st == SourceType.PORTFOLIO_AMOUNT || st ==
	 * SourceType.PORTFOLIO_RETURN){ startDate =
	 * portfolioManager.getEarliestDate(id);
	 * if(thisYear.before(startDate))return LTINumber.INF; else
	 * if(lastYear.after(startDate)){ try { thisAmount =
	 * portfolioManager.getDailydata(id, thisYear).getAmount(); lastAmount =
	 * portfolioManager.getDailydata(id, lastYear).getAmount(); } catch
	 * (Exception e) { e.printStackTrace(); } if(lastAmount!=0) annualizedReturn
	 * = thisAmount/lastAmount - 1; } else{ lastYear = startDate; try {
	 * thisAmount = portfolioManager.getDailydata(id, thisYear).getAmount();
	 * lastAmount = portfolioManager.get(id).getOriginalAmount(); } catch
	 * (Exception e) { e.printStackTrace(); } if(lastAmount!=0) annualizedReturn
	 * = thisAmount/lastAmount - 1; } } else if(st == SourceType.SECURITY_AMOUNT
	 * || st == SourceType.SECURITY_RETURN){ startDate =
	 * securityManager.getStartDate(id); if(thisYear.before(startDate))return
	 * LTINumber.INF; else if(lastYear.after(startDate)){ try { thisAmount =
	 * securityManager.getAdjPrice(id, thisYear); lastAmount =
	 * securityManager.getAdjPrice(id,lastYear); } catch (Exception e) {
	 * e.printStackTrace(); } if(lastAmount!=0) annualizedReturn =
	 * thisAmount/lastAmount - 1; } else{ lastYear = startDate; try { thisAmount
	 * = securityManager.getAdjPrice(id, thisYear); lastAmount =
	 * securityManager.getAdjPrice(id,lastYear); } catch (Exception e) {
	 * e.printStackTrace(); } if(lastAmount!=0) annualizedReturn =
	 * thisAmount/lastAmount - 1; } }
	 * 
	 * return annualizedReturn; }
	 */

	public double computeTotalReturn(List<Double> amounts) {
		double totalReturn = 0;
		double preValue = 0;
		double lastValue = 0;
		for (int i = 0; i < amounts.size(); i++) {
			if (amounts.get(i) != null) {
				preValue = amounts.get(i);
				break;
			}
		}
		for (int j = amounts.size() - 1; j >= 0; j--) {
			if (amounts.get(j) != null) {
				lastValue = amounts.get(j);
				break;
			}
		}
		if (preValue != 0) {
			totalReturn = this.computeIntervalReturn(lastValue, preValue);// lastValue/preValue-1;

			/*
			 * if(amounts.size() == 0)return totalReturn;
			 * 
			 * totalReturn =
			 * Math.pow(totalReturn,TimePara.workingday*1.0/amounts.size()) - 1;
			 */
		}

		return totalReturn;
	}

	public double computeAnnualizedReturn(Double curPrice, Double prePrice, int interval) {
		double annualizedReturn = 0.0;
		try {
			annualizedReturn = Math.pow(curPrice / prePrice, TimePara.workingday / (1.0 * interval)) - 1;
			return annualizedReturn;
		} catch (Exception e) {
			return 0;
		}
	}

	public double computeAnnualizedReturn(Date startDate, Date endDate, SourceType st, long id, int flag, boolean nav) {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		double annualizedReturn = 0;

		Interval interval = new Interval();

		if (startDate.after(endDate)) {
			Date date = startDate;
			startDate = endDate;
			endDate = date;
		}

		interval.setStartDate(startDate);

		interval.setEndDate(endDate);

		double currentPrice = 0;

		double initialPrice = 0;

		List<SecurityDailyData> securityList;

		List<SecurityDailyData> navList;

		List<PortfolioDailyData> portfolioList;

		int size, navSize;

		if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
			if (!nav) {
				securityList = securityManager.getDailyDatas(id, interval.getStartDate(), interval.getEndDate());
				if (securityList == null || securityList.size() == 0)
					return 0;
				size = securityList.size();
				for (int i = 0; i < size; i++) {
					if (securityList.get(i).getAdjClose() != null) {
						initialPrice = securityList.get(i).getAdjClose();
						break;
					}
				}
				for (int i = size - 1; i >= 0; i--) {
					if (securityList.get(i).getAdjClose() != null) {
						currentPrice = securityList.get(i).getAdjClose();
						break;
					}
				}
			} else {
				navList = securityManager.getNAVList(id, startDate, endDate);
				if (navList == null || navList.size() == 0)
					return 0;
				navSize = navList.size();
				for (int i = 0; i < navSize; i++) {
					if (navList.get(i).getAdjNAV() != null) {
						initialPrice = navList.get(i).getAdjNAV();
						break;
					}
				}
				for (int i = navSize - 1; i >= 0; i--) {
					if (navList.get(i).getAdjNAV() != null) {
						currentPrice = navList.get(i).getAdjNAV();
						break;
					}
				}
			}
		}

		else if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
			portfolioList = portfolioManager.getDailydatas(id, interval);
			if (portfolioList != null && portfolioList.size() > 0) {
				size = portfolioList.size();

				for (int i = 0; i < size; i++) {
					if (portfolioList.get(i).getAmount() != null) {
						initialPrice = portfolioList.get(i).getAmount();
						break;
					}
				}

				for (int i = size - 1; i >= 0; i--) {
					if (portfolioList.get(i).getAmount() != null) {
						currentPrice = portfolioList.get(i).getAmount();
						break;
					}
				}
			} else
				return 0;
		}

		int days = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		annualizedReturn = this.computeIntervalReturn(currentPrice, initialPrice);
		if (flag == 0)
			annualizedReturn = Math.pow(currentPrice / initialPrice, TimePara.workingday / (1.0 * days)) - 1;
		// else annualizedReturn = Math.pow(currentPrice/initialPrice,1)-1;

		return annualizedReturn;
	}

	public double computeRSI(Date startDate, Date endDate, SourceType st, long id, boolean nav) {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		double RSI = 0;

		double positive = 0, nagative = 0;

		double pre, cur;

		Interval interval = new Interval(startDate, endDate);

		if (st == SourceType.SECURITY_AMOUNT) {
			List<SecurityDailyData> dailydataList = securityManager.getDailyDatas(id, interval.getStartDate(), interval.getEndDate());
			if (dailydataList == null || dailydataList.size() == 0)
				return RSI;

			for (int i = 1; i < dailydataList.size(); i++) {
				if (!nav) {
					pre = dailydataList.get(i - 1).getAdjClose();
					cur = dailydataList.get(i).getAdjClose();
				} else {
					pre = dailydataList.get(i - 1).getAdjNAV();
					cur = dailydataList.get(i).getAdjNAV();
				}

				if (cur > pre)
					positive += cur - pre;
				else
					nagative += pre - cur;
			}
			if (positive + nagative == 0)
				return 0.0;
			RSI = (positive * 100) / (positive + nagative);
		}

		else if (st == SourceType.PORTFOLIO_AMOUNT) {
			List<PortfolioDailyData> dailydataList = portfolioManager.getDailydatas(id, interval);
			if (dailydataList == null || dailydataList.size() == 0)
				return RSI;

			for (int i = 1; i < dailydataList.size(); i++) {
				pre = dailydataList.get(i - 1).getAmount();

				cur = dailydataList.get(i).getAmount();

				if (cur > pre)
					positive += cur - pre;
				else
					nagative += pre - cur;
			}
			if (positive + nagative == 0)
				return 0.0;
			RSI = (positive * 100) / (positive + nagative);
		}
		return RSI;
	}

	public double computeSMA(Date endDate, int count, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {

		double SMA;

		List<Double> Returns = new ArrayList<Double>();

		Date startDate = new Date();

		count--;

		if (tu == TimeUnit.DAILY) {
			startDate = LTIDate.getNewNYSETradingDay(endDate, -1 * count);
		} else if (tu == TimeUnit.WEEKLY) {
			startDate = LTIDate.getNewNYSEWeek(endDate, -1 * count);
		} else if (tu == TimeUnit.MONTHLY) {
			startDate = LTIDate.getNewNYSEMonth(endDate, -1 * count);
		} else if (tu == TimeUnit.QUARTERLY) {
			startDate = LTIDate.getNewNYSEQuarter(endDate, -1 * count);
		} else if (tu == TimeUnit.YEARLY) {
			startDate = LTIDate.getNewNYSEYear(endDate, -1 * count);
		}

		// Returns = this.getReturns(startDate, endDate, tu, id, st,nav);

		Returns = this.getAmounts(startDate, endDate, tu, id, st, nav);

		SMA = this.computeAverage(Returns);

		return SMA;
	}

	public double computeEMA(Date startDate, Date endDate, int count, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {
		double EMA = 0;

		List<Double> Returns = new ArrayList<Double>();

		count--;

		if (tu == TimeUnit.DAILY) {
			startDate = LTIDate.getNewNYSETradingDay(startDate, -1 * count);
		} else if (tu == TimeUnit.WEEKLY) {
			startDate = LTIDate.getNewNYSEWeek(startDate, -1 * count);
		} else if (tu == TimeUnit.MONTHLY) {
			startDate = LTIDate.getNewNYSEMonth(startDate, -1 * count);
		} else if (tu == TimeUnit.QUARTERLY) {
			startDate = LTIDate.getNewNYSEQuarter(startDate, -1 * count);
		} else if (tu == TimeUnit.YEARLY) {
			startDate = LTIDate.getNewNYSEYear(startDate, -1 * count);
		}

		// Returns = this.getReturns(startDate, endDate, tu, id, st,nav);

		Returns = this.getAmounts(startDate, endDate, tu, id, st, nav);

		if (Returns == null || Returns.size() == 0)
			return 0;

		double nowC = Returns.get(0);

		EMA = nowC;

		int size = Returns.size();

		count++;

		for (int i = 1; i < size; i++) {
			nowC = Returns.get(i);

			EMA = (nowC * 2.0 + EMA * (count - 1) * 1.0) / (count + 1);
		}

		return EMA;
	}

	public double computeMACD(Date startDate, Date endDate, SourceType st, TimeUnit tu, long id, boolean nav) throws NoPriceException {

		double MACD = 0;

		double EMA1 = this.computeEMA(startDate, endDate, 12, tu, st, id, nav);

		double EMA2 = this.computeEMA(startDate, endDate, 26, tu, st, id, nav);

		MACD = EMA1 - EMA2;

		return MACD;

	}

	public double computeDEA(Date startDate, Date endDate, SourceType st, TimeUnit tu, long id, boolean nav) throws NoPriceException {

		double MACD = 0;

		double EMA1;
		double EMA2;
		double DIF;
		double DEA;

		Date date = new Date();

		int time = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		DEA = 0;
		DIF = 0;

		for (int i = 0; i < time; i++) {
			date = LTIDate.getNewNYSETradingDay(startDate, time);

			EMA1 = this.computeEMA(startDate, date, 12, tu, st, id, nav);

			EMA2 = this.computeEMA(startDate, date, 26, tu, st, id, nav);

			DIF = EMA1 - EMA2;

			DEA = 0.2 * DIF + 0.8 * DEA;
		}

		return DEA;
	}

	public double computeTRIN() {
		return 0;
	}

	@Override
	public MPT computeMPTS(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, boolean yearly, boolean nav) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		MPT mpt = new MPT();

		List<Double> mainReturns = new ArrayList<Double>();
		List<Double> benchReturns = new ArrayList<Double>();
		List<Double> mainAmounts = new ArrayList<Double>();

		Security se = new Security();
		Security bm = new Security();
		HoldingInf pf = new HoldingInf();

		if (st == SourceType.PORTFOLIO_RETURN || st == SourceType.PORTFOLIO_AMOUNT) {
			pf = portfolioManager.getOriginalHolding(id);
			bm = securityManager.get(pf.getBenchmarkID());
		} else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
			se = securityManager.get(id);
			bm = securityManager.get(se.getAssetClass().getBenchmarkID());
		}

		Date curDate = new Date();

		curDate = startDate;

		if (tu == TimeUnit.DAILY) {
			if (!LTIDate.isNYSETradingDay(startDate)) {
				curDate = LTIDate.getNewNYSETradingDay(startDate, 1);
			}
		} else if (tu == TimeUnit.WEEKLY) {
			curDate = LTIDate.getLastNYSETradingDayOfWeek(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEWeek(curDate, 1);
			}
		} else if (tu == TimeUnit.MONTHLY) {
			curDate = LTIDate.getLastNYSETradingDayOfMonth(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEMonth(curDate, 1);
			}
		} else if (tu == TimeUnit.QUARTERLY) {
			curDate = LTIDate.getLastNYSETradingDayOfQuarter(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEQuarter(curDate, 1);
			}
		} else if (tu == TimeUnit.YEARLY) {
			curDate = LTIDate.getLastNYSETradingDayOfYear(startDate);
			if (curDate.before(startDate)) {
				curDate = LTIDate.getNewNYSEYear(curDate, 1);
			}
		}

		double curMainValue = 0;
		double curBenchValue = 0;
		double preMainValue = 0;
		double preBenchValue = 0;
		double singleMainReturn = 0;
		double singleBenchReturn = 0;

		boolean firstone = true;

		while (true) {
			if (curDate.after(endDate)) {
				break;
			}

			if (firstone) {
				if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
					try {
						// preMainValue = pf.getTotalAmount(curDate);
						preMainValue = portfolioManager.getDailydata(id, curDate).getAmount();
						mainAmounts.add(preMainValue);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						preMainValue = pf.getAmount();
						mainAmounts.add(preMainValue);
						// e.printStackTrace();
					}
					preBenchValue = bm.getAdjClose(curDate);
				} else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
					if (!nav) {
						preMainValue = se.getAdjClose(curDate);
						preBenchValue = bm.getAdjClose(curDate);
					} else {
						preMainValue = se.getAdjNav(curDate);
						preBenchValue = bm.getAdjNav(curDate);
					}
					mainAmounts.add(preMainValue);
				}

				firstone = !firstone;
			} else {
				if (st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.PORTFOLIO_RETURN) {
					try {
						// curMainValue = pf.getTotalAmount(curDate);
						curMainValue = portfolioManager.getDailydata(id, curDate).getAmount();
						mainAmounts.add(curMainValue);
					} catch (Exception e) {
						curMainValue = preMainValue;
						mainAmounts.add(curMainValue);
						// e.printStackTrace();
					}
					if (!nav)
						curBenchValue = bm.getAdjClose(curDate);
					else
						curBenchValue = bm.getAdjNav(curDate);

					singleMainReturn = this.computeIntervalReturn(curMainValue, preMainValue);
					singleBenchReturn = this.computeIntervalReturn(curBenchValue, preBenchValue);
				} else if (st == SourceType.SECURITY_AMOUNT || st == SourceType.SECURITY_RETURN) {
					if (!nav) {
						curMainValue = se.getAdjClose(curDate);
						curBenchValue = bm.getAdjClose(curDate);
					} else {
						curMainValue = se.getAdjNav(curDate);
						curBenchValue = bm.getAdjNav(curDate);
					}
					mainAmounts.add(curMainValue);
					singleMainReturn = this.computeIntervalReturn(curMainValue, preMainValue);
					singleBenchReturn = this.computeIntervalReturn(curBenchValue, preBenchValue);
				}
				mainReturns.add(singleMainReturn);
				benchReturns.add(singleBenchReturn);
				preMainValue = curMainValue;
				preBenchValue = curBenchValue;

			}
			if (tu == TimeUnit.DAILY) {
				curDate = LTIDate.getNewNYSETradingDay(curDate, 1);
			} else if (tu == TimeUnit.WEEKLY) {
				curDate = LTIDate.getNewNYSEWeek(curDate, 1);
			} else if (tu == TimeUnit.MONTHLY) {
				curDate = LTIDate.getNewNYSEMonth(curDate, 1);
			} else if (tu == TimeUnit.QUARTERLY) {
				curDate = LTIDate.getNewNYSEQuarter(curDate, 1);
			} else if (tu == TimeUnit.YEARLY) {
				curDate = LTIDate.getNewNYSEYear(curDate, 1);
			}
		}

		double beta = this.computeBeta(mainReturns, benchReturns);
		if (beta < LTINumber.INF)
			mpt.setBeta(beta);

		double annualizedRiskFree = this.getAnnualizedRiskFree(startDate, endDate);
		double averageRiskFree = this.getAverageRiskFree(startDate, endDate);

		double averagePort = this.computeAverage(mainReturns) - averageRiskFree;

		double averageBench = this.computeAverage(benchReturns) - averageRiskFree;

		double alpha = this.computeAlpha(beta, averagePort, averageBench);

		if (alpha < LTINumber.INF)
			mpt.setAlpha(alpha);

		double RS = this.computeCorrelationCoefficient(mainReturns, benchReturns);

		if (RS < LTINumber.INF)
			mpt.setRSquared(RS * RS);

		double standardDeviation = this.computeAnnulizedStandardDeviation(mainReturns, tu);
		if (standardDeviation < LTINumber.INF)
			mpt.setStandardDeviation(standardDeviation);

		double drawDown = 0;

		if (mainAmounts.size() != 0) {
			Iterator<Double> iter = mainAmounts.iterator();

			double maxVal = (Double) iter.next();

			while (iter.hasNext()) {
				double currentVal = (Double) iter.next();

				double temp = (maxVal - currentVal) / maxVal;

				drawDown = drawDown > temp ? drawDown : temp;

				maxVal = maxVal > currentVal ? maxVal : currentVal;
			}
		}

		if (drawDown < LTINumber.INF)
			mpt.setDrawDown(drawDown);

		int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		double ar = this.computeAnnualizedReturn(mainAmounts, interval);

		if (ar < LTINumber.INF)
			mpt.setAR(ar);

		double sharpeRatio = this.computeRatio(ar, annualizedRiskFree, standardDeviation);

		if (sharpeRatio < LTINumber.INF)
			mpt.setSharpeRatio(sharpeRatio);

		double treynorRatio = this.computeRatio(ar, annualizedRiskFree, beta);

		if (treynorRatio < LTINumber.INF)
			mpt.setTreynorRatio(treynorRatio);

		mpt.setStartDate(startDate);
		mpt.setEndDate(endDate);
		mpt.setYear(endDate.getYear() + 1900);

		return mpt;
	}

	public List<MPT> computeEveryYearMPTs(TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {
		List<MPT> mptList = new ArrayList<MPT>();

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Date startDate = new Date();
		Date endDate = new Date();

		if (st == SourceType.PORTFOLIO_RETURN) {
			Portfolio portfolio = new Portfolio();
			portfolio = portfolioManager.get(id);
			startDate = portfolio.getStartingDate();
			endDate = portfolio.getEndDate();
		} else if (st == SourceType.SECURITY_RETURN) {
			Security security = new Security();
			security = securityManager.get(id);
			startDate = security.getStartDate();
			endDate = security.getEndDate();
		}

		Date curEndDate = new Date();

		// curEndDate = LTIDate.getDate(year, 12, 31);

		curEndDate = LTIDate.getLastNYSETradingDayOfYear(startDate);

		MPT mpt = new MPT();

		while (true) {
			if (curEndDate.after(endDate))
				break;
			mpt = this.computeMPTS(startDate, curEndDate, tu, st, id, true, nav);
			mptList.add(mpt);
			startDate = curEndDate;
			curEndDate = LTIDate.getLastNYSETradingDayOfYear(LTIDate.getNewNYSEYear(curEndDate, 1));
		}

		mpt = this.computeMPTS(startDate, endDate, tu, st, id, true, nav);

		mptList.add(mpt);

		return mptList;
	}

	/**
	 * *************************************************************************
	 * ***********************************************************
	 */
	@Override
	public double computeSingleDEA(Date endDate, int count, TimeUnit tu, SourceType st, long id, boolean nav) {
		return 0;
	}

	@Override
	public double computeSingleEMA(Date endDate, int count, TimeUnit tu, SourceType st, long id, boolean nav) throws NoPriceException {
		double EMA = 0;

		List<Double> Returns = new ArrayList<Double>();

		count--;

		Date startDate = new Date();

		if (tu == TimeUnit.DAILY) {
			startDate = LTIDate.getNewNYSETradingDay(endDate, -1 * count);
		} else if (tu == TimeUnit.WEEKLY) {
			startDate = LTIDate.getNewNYSEWeek(endDate, -1 * count);
		} else if (tu == TimeUnit.MONTHLY) {
			startDate = LTIDate.getNewNYSEMonth(endDate, -1 * count);
		} else if (tu == TimeUnit.QUARTERLY) {
			startDate = LTIDate.getNewNYSEQuarter(endDate, -1 * count);
		} else if (tu == TimeUnit.YEARLY) {
			startDate = LTIDate.getNewNYSEYear(endDate, -1 * count);
		}

		// Returns = this.getReturns(startDate, endDate, tu, id, st,nav);

		Returns = this.getAmounts(startDate, endDate, tu, id, st, nav);

		double nowC = Returns.get(0);

		EMA = nowC;

		int size = Returns.size();

		count++;

		for (int i = 1; i < size; i++) {
			nowC = Returns.get(i);

			EMA = (nowC * 2.0 + EMA * (count - 1) * 1.0) / (count + 1);
		}

		return EMA;
	}

	@Override
	public double computeSingleMACE(Date endDate, int count, TimeUnit tu, SourceType st, long id, boolean nav) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * *************************************************************************
	 * ****************************************************
	 */
	public void computeMPTs(final Portfolio portfolio) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		Long t1 = System.currentTimeMillis();
		Long portfolioID = portfolio.getID();
		HoldingInf holding=portfolioManager.getOriginalHolding(portfolio.getID());
		// Portfolio portfolio = portfolioManager.get(portfolioID);
		if (portfolio == null )
			return;

		List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolioID);
		if (pdds == null || pdds.size() == 0)
			return;

		Date startDate = pdds.get(0).getDate();
		Date endDate = pdds.get(pdds.size() - 1).getDate();

		SecurityManager sm = ContextHolder.getSecurityManager();
		List<SecurityDailyData> sdds = sm.getDailyDatas(holding.getBenchmarkID(), LTIDate.getNewWeekDay(startDate, -5), LTIDate.getNewWeekDay(endDate, 5));
		if (sdds == null || sdds.size() == 0)
			return;

		pdds = this.getNewPortfolioDailyData(portfolio, pdds, sdds);

		/*
		 * for(int i=0;i<pdds.size();i++)
		 * this.getHibernateTemplate()..update(pdds.get(i));
		 */

		portfolioManager.saveOrUpdateAll(pdds);

		Long t2 = System.currentTimeMillis();

		System.out.println("compute MPT Time:" + (t2 - t1));

	}

	/***************************************************************************
	 * @Override public void computeMPTs(Long
	 *           portfolioID,List<PortfolioDailyData> pdds)throws
	 *           NoPriceException {
	 * 
	 *           PortfolioManager portfolioManager=(PortfolioManager)
	 *           ContextHolder.getInstance().getApplicationContext().getBean(
	 *           "portfolioManager");
	 * 
	 *           Long t1 = System.currentTimeMillis(); Portfolio portfolio =
	 *           portfolioManager.get(portfolioID); if (portfolio == null ||
	 *           portfolio.getBenchmarkID() == null) return;
	 * 
	 *           if (pdds == null || pdds.size() == 0) return;
	 * 
	 *           Date startDate = pdds.get(0).getDate(); Date endDate =
	 *           pdds.get(pdds.size() - 1).getDate();
	 * 
	 *           SecurityManager sm=ContextHolder.getSecurityManager();
	 *           List<SecurityDailyData> sdds =
	 *           sm.getDailyDatas(portfolio.getBenchmarkID(),
	 *           LTIDate.getNewWeekDay(startDate,-5),
	 *           LTIDate.getNewWeekDay(endDate,5)); if (sdds == null ||
	 *           sdds.size() == 0) return;
	 * 
	 *           Long t2 = System.currentTimeMillis();
	 * 
	 *           System.out.println("before compute MPT Time:" + (t2 - t1));
	 * 
	 *           pdds = this.getNewPortfolioDailyData(pdds, sdds); <<<<<<<
	 *           BaseFormulaManagerImpl.java //for(int i=0;i<pdds.size();i++) //
	 *           this.getHibernateTemplate()..update(pdds.get(i)); ======= Long
	 *           t3 = System.currentTimeMillis();
	 * 
	 *           System.out.println("compute MPT Time:" + (t3 - t2));
	 * 
	 *           /*for(int i=0;i<pdds.size();i++)
	 *           this.getHibernateTemplate()..update(pdds.get(i));
	 */

	// }
	/***************************************************************************
	 * public PortfolioDailyData getOneYearMPT(List<Double>
	 * portfolioReturns,List<Double> benchmarkReturns,List<Double>
	 * portfolioAmounts,Date totalStartDate,Date currentDate,int interval)
	 * throws NoPriceException { PortfolioDailyData pdd = new
	 * PortfolioDailyData();
	 * 
	 * double beta = this.computeBeta(portfolioReturns, benchmarkReturns);
	 * 
	 * double annualizedRiskFree = this.getAnnualizedRiskFree(totalStartDate,
	 * currentDate); double averageRiskFree =
	 * this.getAverageRiskFree(totalStartDate, currentDate);
	 * 
	 * double averagePortfolioReturn = this.computeAverage(portfolioReturns) -
	 * averageRiskFree; double averageBenchmarkReturn =
	 * this.computeAverage(benchmarkReturns) - averageRiskFree;
	 * 
	 * double alpha = this.computeAlpha(beta, averagePortfolioReturn,
	 * averageBenchmarkReturn);
	 * 
	 * double RSquared = this.computeCorrelationCoefficient(portfolioReturns,
	 * benchmarkReturns); RSquared = RSquared * RSquared;
	 * 
	 * double standardDeviation =
	 * this.computeAnnulizedStandardDeviation(portfolioReturns,
	 * TimeUnit.DAILY);// .computeStandardDeviation(portfolioReturns);
	 * 
	 * //double std = this.computeStandardDeviation(portfolioReturns);
	 * 
	 * double AR = this.computeAnnualizedReturn(portfolioAmounts, interval);
	 * 
	 * double totalReturn = this.computeTotalReturn(portfolioAmounts);
	 * 
	 * double sharpeRatio = this.computeRatio(AR, annualizedRiskFree,
	 * standardDeviation);
	 * 
	 * double treynorRatio = this.computeRatio(AR, annualizedRiskFree, beta);
	 * 
	 * double drawDown = this.computeDrawDown(portfolioAmounts);
	 * 
	 * if(alpha < LTINumber.INF)pdd.setAlpha(alpha); if(beta <
	 * LTINumber.INF)pdd.setBeta(beta); if(AR < LTINumber.INF)pdd.setAR(AR);
	 * if(RSquared < LTINumber.INF)pdd.setRSquared(RSquared);
	 * if(standardDeviation <
	 * LTINumber.INF)pdd.setStandardDeviation(standardDeviation); if(sharpeRatio
	 * < LTINumber.INF)pdd.setSharpeRatio(sharpeRatio); if(treynorRatio <
	 * LTINumber.INF)pdd.setTreynorRatio(treynorRatio); if(drawDown <
	 * LTINumber.INF)pdd.setDrawDown(drawDown); log.debug(currentDate+"
	 * succeess."); return pdd; }
	 * 
	 * 
	 * public List<PortfolioDailyData>
	 * getNewPortfolioDailyData(List<PortfolioDailyData>
	 * pdds,List<SecurityDailyData> sdds)throws NoPriceException{
	 * 
	 * PortfolioManager portfolioManager=(PortfolioManager)
	 * ContextHolder.getInstance
	 * ().getApplicationContext().getBean("portfolioManager");
	 * 
	 * StrategyManager strategyManager=(StrategyManager)
	 * ContextHolder.getInstance
	 * ().getApplicationContext().getBean("strategyManager");
	 * 
	 * List<Double> portfolioAmounts = new ArrayList<Double>(); List<Double>
	 * benchmarkAmounts = new ArrayList<Double>();
	 * 
	 * List<Double> portfolioReturns = new ArrayList<Double>(); List<Double>
	 * benchmarkReturns = new ArrayList<Double>();
	 * 
	 * List<Double> yearAmounts = new ArrayList<Double>(); List<Double>
	 * yearBenchsA = new ArrayList<Double>(); List<Double> yearReturns = new
	 * ArrayList<Double>(); List<Double> yearBenchsR = new ArrayList<Double>();
	 * 
	 * Date totalStartDate, oneYearStartDate=new Date(), threeYearStartDate=new
	 * Date(), fiveYearStartDate=new Date();
	 * 
	 * boolean caculateYearEnd = false;
	 * 
	 * boolean isCashFlow = false;
	 * 
	 * Long portfolioID = pdds.get(0).getPortfolioID(); Long benchMarkID =
	 * sdds.get(0).getSecurityID(); Portfolio thisOne =
	 * portfolioManager.get(portfolioID); Long strategyID =
	 * thisOne.getMainStrategyID(); Long classID=null; Strategy strategy=null;
	 * if(thisOne.getMainStrategyID()!=null){
	 * strategy=strategyManager.get(thisOne.getMainStrategyID());
	 * if(strategy!=null) { classID=strategy.getStrategyClassID();
	 * if(strategy.getStrategyClassID()==5) { isCashFlow = true; } } }
	 * 
	 * Long userID = thisOne.getUserID(); String name = thisOne.getName();
	 * Boolean isModel = thisOne.getIsModelPortfolio();
	 * 
	 * double prePortfolioValue = 0; double curPortfolioValue; double
	 * preBenchMarkValue = 0; double curBenchMarkValue;
	 * 
	 * double totalOriginalAmount = 0,oneYearAmount = 0,threeYearAmount = 0,
	 * fiveYearAmount = 0;
	 * 
	 * List<PortfolioMPT> pmpts = new ArrayList<PortfolioMPT>();
	 * 
	 * List<Date> dateList = new ArrayList<Date>();
	 * 
	 * Date startDate = pdds.get(0).getDate();
	 * 
	 * totalStartDate = startDate;
	 * 
	 * totalOriginalAmount = pdds.get(0).getAmount();
	 * 
	 * List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();
	 * 
	 * for (int i = 0; i < pdds.size(); i++) {
	 * 
	 * Date currentDate = pdds.get(i).getDate();
	 * 
	 * Executor.setComputeMPTDate(pdds.get(i).getID(), currentDate);
	 * 
	 * caculateYearEnd = false;
	 * 
	 * int interval = LTIDate.calculateIntervalIgnoreHolidDay(totalStartDate,
	 * currentDate); PortfolioDailyData pdd = pdds.get(i);
	 * 
	 * curPortfolioValue = pdd.getAmount(); curBenchMarkValue =
	 * getBenchmarkAmount(sdds, currentDate);
	 * 
	 * portfolioAmounts.add(curPortfolioValue);
	 * benchmarkAmounts.add(curBenchMarkValue);
	 * 
	 * dateList.add(currentDate);
	 * 
	 * if(i == 0){ prePortfolioValue = curPortfolioValue; preBenchMarkValue =
	 * curBenchMarkValue; } if(i>0){ double portfolioReturn =
	 * this.computeIntervalReturn(curPortfolioValue, prePortfolioValue); double
	 * benchMarkReturn = this.computeIntervalReturn(curBenchMarkValue,
	 * preBenchMarkValue);
	 * 
	 * portfolioReturns.add(portfolioReturn);
	 * benchmarkReturns.add(benchMarkReturn); prePortfolioValue =
	 * curPortfolioValue; preBenchMarkValue = curBenchMarkValue; }
	 * 
	 * 
	 * if(i == pdds.size()-1) { PortfolioDailyData newPdd =
	 * this.getOneYearMPT(portfolioReturns, benchmarkReturns, portfolioAmounts,
	 * totalStartDate, currentDate, interval); pdd = setAllMPT(pdd, newPdd,0);
	 * if(isCashFlow) { double ar = this.computeIRR_OldVersion(portfolioID,
	 * totalStartDate, currentDate,totalOriginalAmount,curPortfolioValue);
	 * pdd.setAR(ar); } else pdd.setAR(newPdd.getAR()); }
	 * 
	 * if(LTIDate.isLastNYSETradingDayOfYear(currentDate) || i == pdds.size() -1
	 * || LTIDate.isYearEnd(currentDate)) caculateYearEnd = true;
	 * 
	 * int days = LTIDate.calculateInterval(totalStartDate, currentDate);
	 * 
	 * if(days>=TimePara.yearday){ int index = getSubListIndex(dateList,
	 * currentDate, 1); startDate = dateList.get(index); oneYearStartDate =
	 * startDate; yearAmounts = portfolioAmounts.subList(index, i+1);
	 * yearBenchsA = benchmarkAmounts.subList(index, i+1);
	 * 
	 * yearReturns = portfolioReturns.subList(index, i); yearBenchsR =
	 * benchmarkReturns.subList(index, i);
	 * 
	 * oneYearAmount = yearAmounts.get(0);
	 * 
	 * PortfolioDailyData newPdd = this.getOneYearMPT(yearReturns, yearBenchsR,
	 * yearAmounts, startDate, currentDate, TimePara.workingday); pdd =
	 * setAllMPT(pdd, newPdd,1);
	 * 
	 * if(pdd.getSharpeRatio1()>=5)log.warn(currentDate+" "+portfolioID+" "+1+"
	 * "+pdd.getSharpeRatio1()+" happen at monitor"); }
	 * if(days>=TimePara.yearday*3){ int index = getSubListIndex(dateList,
	 * currentDate, 3);
	 * 
	 * startDate = dateList.get(index); threeYearStartDate = startDate;
	 * 
	 * yearAmounts = portfolioAmounts.subList(index, i+1); yearBenchsA =
	 * benchmarkAmounts.subList(index, i+1);
	 * 
	 * yearReturns = portfolioReturns.subList(index, i); yearBenchsR =
	 * benchmarkReturns.subList(index, i);
	 * 
	 * threeYearAmount = yearAmounts.get(0);
	 * 
	 * PortfolioDailyData newPdd = this.getOneYearMPT(yearReturns, yearBenchsR,
	 * yearAmounts, startDate, currentDate, TimePara.workingday*3); pdd =
	 * setAllMPT(pdd, newPdd, 3);
	 * 
	 * if(pdd.getSharpeRatio3()>=5)log.warn(currentDate+" "+portfolioID+" "+3+"
	 * "+pdd.getSharpeRatio3()+" happen at monitor"); }
	 * if(days>=TimePara.yearday*5){ int index = getSubListIndex(dateList,
	 * currentDate, 5);
	 * 
	 * startDate = dateList.get(index); fiveYearStartDate = startDate;
	 * 
	 * yearAmounts = portfolioAmounts.subList(index, i+1); yearBenchsA =
	 * benchmarkAmounts.subList(index, i+1);
	 * 
	 * yearReturns = portfolioReturns.subList(index, i); yearBenchsR =
	 * benchmarkReturns.subList(index, i);
	 * 
	 * fiveYearAmount = yearAmounts.get(0);
	 * 
	 * PortfolioDailyData newPdd = this.getOneYearMPT(yearReturns, yearBenchsR,
	 * yearAmounts, startDate, currentDate, TimePara.workingday*5); pdd =
	 * setAllMPT(pdd, newPdd, 5);
	 * 
	 * if(pdd.getSharpeRatio5()>=5)log.warn(currentDate+" "+portfolioID+" "+5+"
	 * "+pdd.getSharpeRatio5()+" happen at monitor"); } newpdds.add(pdd);
	 * 
	 * if(caculateYearEnd){
	 * 
	 * int index = getYearEndIndex(dateList, currentDate);
	 * 
	 * startDate = dateList.get(index);
	 * 
	 * yearAmounts = portfolioAmounts.subList(index, i+1); yearBenchsA =
	 * benchmarkAmounts.subList(index, i+1);
	 * 
	 * yearReturns = portfolioReturns.subList(index, i); yearBenchsR =
	 * benchmarkReturns.subList(index, i);
	 * 
	 * PortfolioDailyData newPdd = this.getOneYearMPT(yearReturns, yearBenchsR,
	 * yearAmounts, startDate, currentDate,
	 * LTIDate.calculateIntervalIgnoreHolidDay(startDate, currentDate));
	 * 
	 * PortfolioMPT pmpt;
	 * 
	 * int year = currentDate.getYear()+1900;
	 * 
	 * pmpt = portfolioManager.getPortfolioMPT(portfolioID, year);
	 * 
	 * if(pmpt == null)pmpt = new PortfolioMPT(); pmpt = setAllBasicInfo(pmpt,
	 * portfolioID, benchMarkID, name, strategyID, userID,isModel, classID,
	 * year,newPdd);
	 * 
	 * if(pmpt.getSharpeRatio()!=null&&pmpt.getSharpeRatio()>=5)log.warn(
	 * currentDate+" "+portfolioID+" "+year+" "+pdd.getSharpeRatio()+" happen at
	 * monitor");
	 * 
	 * if(isCashFlow) { double originalAmount = yearAmounts.get(0); double
	 * lastAmount = yearAmounts.get(yearAmounts.size()-1); double ar =
	 * this.computeIRR(portfolioID, startDate,
	 * currentDate,originalAmount,lastAmount); pmpt.setAR(ar); }
	 * pmpts.add(pmpt); } }
	 * 
	 * //from start date to end date if(newpdds!=null){ PortfolioDailyData pdd;
	 * pdd=newpdds.get(newpdds.size()-1); double lastAmount = pdd.getAmount();
	 * if (pdd!=null) {
	 * 
	 * PortfolioMPT pmpt; pmpt = portfolioManager.getPortfolioMPT(portfolioID,
	 * 0);
	 * 
	 * if(pmpt == null)pmpt = new PortfolioMPT();
	 * 
	 * pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID,
	 * strategyID, isModel, classID, PortfolioMPT.FROM_STARTDATE_TO_ENDDATE,
	 * pdd);
	 * 
	 * pmpts.add(pmpt);
	 * 
	 * //1 year pmpt = portfolioManager.getPortfolioMPT(portfolioID, -1);
	 * 
	 * if(pmpt == null)pmpt = new PortfolioMPT();
	 * 
	 * pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID,
	 * strategyID, isModel, classID, PortfolioMPT.LAST_ONE_YEAR, pdd);
	 * 
	 * if(isCashFlow && pdd.getAR1()!=null) { double ar =
	 * this.computeIRR(portfolioID
	 * ,oneYearStartDate,pdd.getDate(),oneYearAmount,lastAmount);
	 * pmpt.setAR(ar); pdd.setAR1(ar); } pmpts.add(pmpt);
	 * 
	 * //3 year pmpt = portfolioManager.getPortfolioMPT(portfolioID, -3);
	 * 
	 * if(pmpt == null)pmpt = new PortfolioMPT();
	 * 
	 * pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID,
	 * strategyID, isModel, classID, PortfolioMPT.LAST_THREE_YEAR, pdd);
	 * 
	 * if(isCashFlow && pdd.getAR3()!=null) { double ar =
	 * this.computeIRR(portfolioID
	 * ,threeYearStartDate,pdd.getDate(),threeYearAmount,lastAmount);
	 * pmpt.setAR(ar); pdd.setAR3(ar); } pmpts.add(pmpt);
	 * 
	 * //5 year pmpt = portfolioManager.getPortfolioMPT(portfolioID, -5);
	 * 
	 * if(pmpt == null)pmpt = new PortfolioMPT();
	 * 
	 * pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID,
	 * strategyID, isModel, classID, PortfolioMPT.LAST_FIVE_YEAR, pdd);
	 * 
	 * if(isCashFlow && pdd.getAR5()!=null) { double ar =
	 * this.computeIRR(portfolioID
	 * ,fiveYearStartDate,pdd.getDate(),fiveYearAmount,lastAmount);
	 * pmpt.setAR(ar); pdd.setAR5(ar); } pmpts.add(pmpt);
	 * 
	 * 
	 * newpdds.remove(newpdds.size()-1); newpdds.add(pdd); } }
	 * 
	 * portfolioManager.saveOrUpdateAll(pmpts);
	 * 
	 * return newpdds; } /
	 **************************************************************************/

	public void computeMPTs(final Portfolio portfolio, List<PortfolioDailyData> pdds) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		Long t1 = System.currentTimeMillis();
		Long portfolioID = portfolio.getID();
		HoldingInf holding=portfolioManager.getOriginalHolding(portfolio.getID());
		// Portfolio portfolio = portfolioManager.get(portfolioID);
		if (portfolio == null)
			return;

		if (pdds == null || pdds.size() == 0)
			return;

		Date startDate = pdds.get(0).getDate();
		Date endDate = pdds.get(pdds.size() - 1).getDate();

		SecurityManager sm = ContextHolder.getSecurityManager();
		List<SecurityDailyData> sdds = sm.getDailyDatas(holding.getBenchmarkID(), LTIDate.getNewWeekDay(startDate, -1), LTIDate.getNewWeekDay(endDate, 1));
		if (sdds == null || sdds.size() == 0)
			return;

		Long t2 = System.currentTimeMillis();

		System.out.println("before compute MPT Time:" + (t2 - t1));

		pdds = this.getNewPortfolioDailyData(portfolio, pdds, sdds);

		Long t3 = System.currentTimeMillis();

		System.out.println("compute MPT Time:" + (t3 - t2));

		// for(int i=0;i<pdds.size();i++)
		// this.getHibernateTemplate()..update(pdds.get(i));
	}

	/**
	 * *************************************************************************
	 * ***************************************
	 */
	public PortfolioDailyData getOneYearMPT(double firstAmount, double currentAmount, double size, double firstRiskFree, double lastRiskFree, double drawDown, double sigmaXB, double sigmaX, double sigmaXX, double sigmaB, double sigmaBB, double sigmaR, double sigmaSR, double sigmaLogXX, double sigmaLogX, Date totalStartDate, Date currentDate, int interval, boolean isCashFlow, final Portfolio portfolio, double totalOriginalAmount) throws NoPriceException {
		PortfolioDailyData pdd = new PortfolioDailyData();

		// double size = (double)portfolioAmounts.size()-1;
		double beta = (sigmaXB * size - (sigmaX * sigmaB));
		// beta = beta/(size*sigmaBB-sigmaX*sigmaX);
		beta = beta / (size * sigmaBB - sigmaB * sigmaB);

		double annualizedRiskFree = this.computeIntervalReturn(lastRiskFree, firstRiskFree);
		if (interval != -100)
			annualizedRiskFree = Math.pow(lastRiskFree / firstRiskFree, TimePara.workingday / ((size + 1) * 1.0)) - 1;
		// else annualizedRiskFree = lastRiskFree/firstRiskFree - 1;

		double averageRiskFree = sigmaR / (interval);

		double averagePortfolioReturn = sigmaX / size - averageRiskFree;
		double averageBenchmarkReturn = sigmaB / size - averageRiskFree;

		double alpha = this.computeAlpha(beta, averagePortfolioReturn, averageBenchmarkReturn);

		double standardDeviation = (sigmaLogXX - sigmaLogX * sigmaLogX / size) / (size - 1);
		standardDeviation = Math.sqrt(standardDeviation);
		standardDeviation *= Math.pow(TimePara.workingday, 0.5);
		// double averageR = sigmaX/size;
		// standardDeviation =
		// Math.sqrt(Math.pow((standardDeviation+(1+averageR)*(1+averageR)),
		// TimePara.workingday)-Math.pow((1+averageR)*(1+averageR),
		// TimePara.workingday));

		// double RSquared = 1.0 - (sigmaXX - alpha*sigmaX -
		// beta*sigmaXB)/(sigmaXX - sigmaX*sigmaX/size);

		double RSquared = 0;
		double co = (sigmaXB - sigmaX * sigmaB / size) / (size - 1);
		co = co * co;
		double stdA = (sigmaXX - sigmaX * sigmaX / size) / (size - 1);
		double stdB = (sigmaBB - sigmaB * sigmaB / size) / (size - 1);
		RSquared = co / (stdA * stdB);

		// double currentAmount =
		// portfolioAmounts.get(portfolioAmounts.size()-1);
		// double firstAmount = portfolioAmounts.get(0);
		double AR;
		/** if isCashFlow is true, use computeIRR to get AR* */
		if (isCashFlow) {
			AR = this.computeIRR(portfolio, totalStartDate, currentDate, totalOriginalAmount, currentAmount);
		} else {
			AR = this.computeIntervalReturn(currentAmount, firstAmount);
			if (interval != -100) {
				AR = Math.pow(currentAmount / firstAmount, TimePara.workingday / (interval * 1.0)) - 1;// this.computeAnnualizedReturn(portfolioAmounts,
				// interval);
			}
		}

		/*
		 * else { AR = currentAmount/firstAmount-1; }
		 */

		double sharpeRatio = this.computeRatio(AR, annualizedRiskFree, standardDeviation);

		double treynorRatio = this.computeRatio(AR, annualizedRiskFree, beta);

		// also change the API when this fomular change
		double tmpHalfDiviation = Math.sqrt(sigmaSR / (size - 1));
		// System.out.println(sigmaSR);
		tmpHalfDiviation *= Math.pow(TimePara.workingday, 0.5);

		double sortinoRatio = this.computeRatio(AR, annualizedRiskFree, tmpHalfDiviation);// averagePortfolioReturn/tmpHalfDiviation;

		// double drawDown = this.computeDrawDown(portfolioAmounts);

		if (alpha < LTINumber.INF)
			pdd.setAlpha(alpha);
		if (beta < LTINumber.INF)
			pdd.setBeta(beta);
		if (AR < LTINumber.INF)
			pdd.setAR(AR);
		if (RSquared < LTINumber.INF)
			pdd.setRSquared(RSquared);
		if (standardDeviation < LTINumber.INF)
			pdd.setStandardDeviation(standardDeviation);
		if (sharpeRatio < LTINumber.INF)
			pdd.setSharpeRatio(sharpeRatio);
		if (treynorRatio < LTINumber.INF)
			pdd.setTreynorRatio(treynorRatio);
		if (drawDown < LTINumber.INF)
			pdd.setDrawDown(drawDown);
		if (sortinoRatio < LTINumber.INF)
			pdd.setSortinoRatio(sortinoRatio);
		// log.debug(currentDate + " succeess.");
		return pdd;
	}

	/**
	 * calculate the securityMPT using incremental datas
	 * 
	 * @param firstAmount
	 * @param currentAmount
	 * @param size
	 * @param firstRiskFree
	 * @param currentRiskFree
	 * @param sigmaXB
	 * @param sigmaX
	 * @param sigmaXX
	 * @param sigmaB
	 * @param sigmaBB
	 * @param sigmaR
	 * @param sigmaLogXX
	 * @param sigmaLogX
	 * @param interval
	 * @param drawDownHigh
	 * @param drawDownLow
	 * @param drawDownNextHigh
	 * @param security
	 * @return return the security's mpt
	 */
	public SecurityMPT calculateOneSecurityMPT_Incremental(SecurityMPT securityMPT, double firstAmount, double currentAmount, double size, double firstRiskFree, double currentRiskFree, double sigmaXB, double sigmaX, double sigmaXX, double sigmaB, double sigmaBB, double sigmaR, double sigmaLogXX, double sigmaLogX, int interval, int year) {
		if (securityMPT == null)
			securityMPT = new SecurityMPT();
		/** beta **/
		double beta = 0;
		double beta_up = (size * sigmaXB - sigmaX * sigmaB);
		double beta_down = size * sigmaBB - sigmaB * sigmaB;
		if (beta_down != 0)
			beta = beta_up / beta_down;
		/** annualizedRiskFree ***/
		double annualizedRiskFree = this.computeIntervalReturn(currentRiskFree, firstRiskFree);
		if (interval != 0)
			annualizedRiskFree = Math.pow(currentRiskFree / firstRiskFree, TimePara.workingday / (interval * 1.0)) - 1;
		/** averagerRiskFree ***/
		double averageRiskFree = sigmaR / (interval);
		/** averageSecurityReturn & averageBenchmarkReturn **/
		double averageSecurityReturn = sigmaX / size - averageRiskFree;
		double averageBenchmarkReturn = sigmaB / size - averageRiskFree;
		/** alpha **/
		double alpha = this.computeAlpha(beta, averageSecurityReturn, averageBenchmarkReturn);
		/** RSquared **/
		double correlation = (sigmaXB - sigmaX * sigmaB / size) / (size - 1);
		double securitySquareStd = (sigmaXX - sigmaX * sigmaX / size) / (size - 1);
		double benchMarkSquareStd = (sigmaBB - sigmaB * sigmaB / size) / (size - 1);
		double RSquared = correlation * correlation / (securitySquareStd * benchMarkSquareStd);
		/** standardDeviation TimeUnit.DAILY ***/
		double standardDeviation = (sigmaLogXX - sigmaLogX * sigmaLogX / size) / (size - 1);
		standardDeviation = Math.sqrt(standardDeviation);
		standardDeviation *= Math.pow(TimePara.workingday, 0.5);

		/*** totalReturn **/
		double totalReturn = computeIntervalReturn(currentAmount, firstAmount);
		/*** AR **/
		double AR = Math.pow(currentAmount / firstAmount, TimePara.workingday / (interval * 1.0)) - 1;
		/*** sharpeRatio **/
		double sharpeRatio = computeRatio(AR, annualizedRiskFree, standardDeviation);
		/*** treynorRatio **/
		double treynorRatio = computeRatio(AR, annualizedRiskFree, beta);
		if (year > 0)
			AR = totalReturn;
		/*** drawDown calculated outside for incremental data saved **/

		/*****************************************************************************************/
		if (alpha < LTINumber.INF)
			securityMPT.setAlpha(alpha);
		if (beta < LTINumber.INF)
			securityMPT.setBeta(beta);
		if (AR < LTINumber.INF)
			securityMPT.setAR(AR);
		if (RSquared < LTINumber.INF)
			securityMPT.setRSquared(RSquared);
		if (standardDeviation < LTINumber.INF)
			securityMPT.setStandardDeviation(standardDeviation);
		if (sharpeRatio < LTINumber.INF)
			securityMPT.setSharpeRatio(sharpeRatio);
		if (treynorRatio < LTINumber.INF)
			securityMPT.setTreynorRatio(treynorRatio);
		if (totalReturn < LTINumber.INF)
			securityMPT.setReturn(totalReturn);

		return securityMPT;
	}

	/***
	 * @author CCD get incremental data for MPT calculation and calculate mpt
	 * @return return the MPT result using Incremental calculation added on
	 *         2009-11-12 for junit test. *
	 ***/
	public List<SecurityMPT> calculateOneSecurityINCMPT(final Security security, final Security benchmark, List<SecurityMPTIncData> securityMPTIncDataList, Date today) throws NoPriceException {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		/************ incremenatl data******************* */

		double[] sigmaSB = new double[5];
		double[] sigmaS = new double[5];
		double[] sigmaSS = new double[5];
		double[] sigmaB = new double[5];
		double[] sigmaBB = new double[5];
		double[] sigmaR = new double[5];
		double[] sigmaLogS = new double[5];
		double[] sigmaLogSS = new double[5];
		double[] lastSB = new double[5];
		double[] lastS = new double[5];
		double[] lastSS = new double[5];
		double[] lastB = new double[5];
		double[] lastBB = new double[5];
		double[] lastR = new double[5];
		double[] lastLogS = new double[5];
		double[] lastLogSS = new double[5];
		double newSB, newS, newSS, newB, newBB, newR, newLogS, newLogSS;
		double[] firstAmount = new double[5];
		double[] firstRiskFree = new double[5];
		double[] drawDown = new double[5];
		double[] drawDownHigh = new double[5];
		int[] size = new int[5];
		int[] interval = new int[5];
		List<SecurityMPT> securityMPTList = new ArrayList<SecurityMPT>();
		SecurityMPT mpt = new SecurityMPT();
		double currentAmount = 0, currentRiskFree = 0, tempDrawDown;
		SecurityMPT[] securityMPT = new SecurityMPT[5];

		/** security info **/
		Long cashID = securityManager.get("CASH").getID();
		Long securityID = security.getID();
		Long assetClassID = security.getClassID();
		String assetClassName = security.getClassName();
		String symbol = security.getSymbol();
		String securityName = security.getName();
		int securityType = security.getSecurityType();
		/**
		 * 
		 * 0 stands for five year to today 1 stands for three year to today 2
		 * stands for one year to today 3 stands for startDate to today 4 stands
		 * for thisyear to today *
		 ***/
		Date startDate = security.getStartDate();
		Date endDate = securityMPTIncDataList.get(0).getDataLastDate();
		Date[] yearStartDate = new Date[5];
		Date[] yearNextStartDate = new Date[5];
		boolean[] inc = new boolean[5];
		SecurityMPTIncData[] securityMPTIncDatas = new SecurityMPTIncData[5];
		for (int i = 0; i < 5; ++i) {
			securityMPTIncDatas[i] = securityMPTIncDataList.get(i);
			drawDown[i] = securityMPTIncDatas[i].getDrawDown();
			drawDownHigh[i] = securityMPTIncDatas[i].getDrawDownHigh();
		}
		/*** get the incremental data's start date **/
		for (int i = 0; i < 5; ++i) {
			yearStartDate[i] = securityMPTIncDatas[i].getDataStartDate();
			yearNextStartDate[i] = LTIDate.getNewNYSETradingDay(yearStartDate[i], 1);
			inc[i] = false;
		}
		/** if the incremental data size satisfy the time para ,set inc true **/
		for (int i = 0; i < 3; ++i)
			if (securityMPTIncDatas[i].getSize() == (TimePara.workingday * (0 - securityMPTIncDatas[i].getYear())))
				inc[i] = true;

		/** year indexs for security mpt ****/
		int[] securityMPTIndex = new int[5];

		securityMPTIndex[0] = -5;
		securityMPTIndex[1] = -3;
		securityMPTIndex[2] = -1;
		securityMPTIndex[3] = 0;
		securityMPTIndex[4] = today.getYear() + 1900;

		securityMPTIncDataList.clear();
		/*** calculate & adjust for incremental data ***/
		/**
		 * 
		 * 0 stands for five year to today 1 stands for three year to today 2
		 * stands for one year to today 3 stands for startDate to today PS: all
		 * the last data should be zero 4 stands for thisyear to today PS: all
		 * the last data should be zero *
		 ***/

		try {
			/** initial last data ***/
			for (int i = 0; i < 5; ++i) {
				if (!inc[i]) {
					firstAmount[i] = securityManager.getAdjPrice(securityID, yearStartDate[i]);
					firstRiskFree[i] = securityManager.getAdjPrice(cashID, yearStartDate[i]);
					lastS[i] = lastB[i] = lastR[i] = lastLogS[i] = lastSB[i] = lastSS[i] = lastBB[i] = lastLogSS[i] = 0.0;
					continue;
				}
				double securityLastAmount = securityManager.getAdjPrice(securityID, yearStartDate[i]);
				double securityNextLastAmount = securityManager.getAdjPrice(securityID, yearNextStartDate[i]);
				double benchmarkLastAmount = securityManager.getAdjPrice(benchmark.getID(), yearStartDate[i]);
				double benchmarkNextLastAmount = securityManager.getAdjPrice(benchmark.getID(), yearNextStartDate[i]);
				double cashLastAmount = securityManager.getAdjPrice(cashID, yearStartDate[i]);
				double cashNextLastAmount = securityManager.getAdjPrice(cashID, yearNextStartDate[i]);
				firstRiskFree[i] = cashNextLastAmount;
				firstAmount[i] = securityNextLastAmount;
				lastS[i] = this.computeIntervalReturn(securityNextLastAmount, securityLastAmount);
				lastB[i] = this.computeIntervalReturn(benchmarkNextLastAmount, benchmarkLastAmount);
				lastR[i] = this.computeIntervalReturn(cashNextLastAmount, cashLastAmount);
				lastLogS[i] = this.computeLogIntervalReturn(securityNextLastAmount, securityLastAmount);
				lastSB[i] = lastS[i] * lastB[i];
				lastSS[i] = lastS[i] * lastS[i];
				lastBB[i] = lastB[i] * lastB[i];
				lastLogSS[i] = lastLogS[i] * lastLogS[i];
			}

			/** initial new data ***/
			double securityLastAmount = securityManager.getAdjPrice(securityID, endDate);
			double securityNextLastAmount = securityManager.getAdjPrice(securityID, today);
			double benchmarkLastAmount = securityManager.getAdjPrice(benchmark.getID(), endDate);
			double benchmarkNextLastAmount = securityManager.getAdjPrice(benchmark.getID(), today);
			double cashLastAmount = securityManager.getAdjPrice(cashID, endDate);
			double cashNextLastAmount = securityManager.getAdjPrice(cashID, today);
			currentRiskFree = cashNextLastAmount;
			currentAmount = securityNextLastAmount;
			newS = this.computeIntervalReturn(securityNextLastAmount, securityLastAmount);
			newB = this.computeIntervalReturn(benchmarkNextLastAmount, benchmarkLastAmount);
			newR = this.computeIntervalReturn(cashNextLastAmount, cashLastAmount);
			newLogS = this.computeLogIntervalReturn(securityNextLastAmount, securityLastAmount);
			newSB = newS * newB;
			newSS = newS * newS;
			newBB = newB * newB;
			newLogSS = newLogS * newLogS;

			/**
			 * update mpt incremental data and calculate mpt
			 * */
			for (int i = 4; i >= 0; --i) {
				sigmaSB[i] = securityMPTIncDatas[i].getSigmaSB() - lastSB[i] + newSB;
				sigmaS[i] = securityMPTIncDatas[i].getSigmaS() - lastS[i] + newS;
				sigmaSS[i] = securityMPTIncDatas[i].getSigmaSS() - lastSS[i] + newSS;
				sigmaB[i] = securityMPTIncDatas[i].getSigmaB() - lastB[i] + newB;
				sigmaBB[i] = securityMPTIncDatas[i].getSigmaBB() - lastBB[i] + newBB;
				sigmaR[i] = securityMPTIncDatas[i].getSigmaR() - lastR[i] + newR;
				sigmaLogS[i] = securityMPTIncDatas[i].getSigmaLS() - lastLogS[i] + newLogS;
				sigmaLogSS[i] = securityMPTIncDatas[i].getSigmaLSS() - lastLogSS[i] + newLogSS;
				size[i] = securityMPTIncDatas[i].getSize();
				interval[i] = securityMPTIncDatas[i].getIntervalDays();
				if (!inc[i]) {
					++size[i];
					++interval[i];
				}
				// we could not calculate standarddeviation and so on if size<=1
				if (i == 4 && size[i] == 1) {
					// we have two dates' data, the last trading date of last
					// year and the first trading date of the new year
					tempDrawDown = (securityLastAmount - securityNextLastAmount) / securityLastAmount;
					drawDown[i] = tempDrawDown > drawDown[i] ? tempDrawDown : drawDown[i];
					drawDownHigh[i] = securityLastAmount > securityNextLastAmount ? securityLastAmount : securityNextLastAmount;
					mpt = new SecurityMPT();
				} else {
					// update mpt
					securityMPT[i] = securityManager.getSecurityMPT(securityID, securityMPTIndex[i]);
					mpt = calculateOneSecurityMPT_Incremental(securityMPT[i], firstAmount[i], currentAmount, size[i], firstRiskFree[i], currentRiskFree, sigmaSB[i], sigmaS[i], sigmaSS[i], sigmaB[i], sigmaBB[i], sigmaR[i], sigmaLogSS[i], sigmaLogS[i], interval[i], securityMPTIndex[i]);
					if (i >= 3) {
						tempDrawDown = (drawDownHigh[i] - currentAmount) / drawDownHigh[i];
						drawDown[i] = drawDown[i] > tempDrawDown ? drawDown[i] : tempDrawDown;
						drawDownHigh[i] = drawDownHigh[i] > currentAmount ? drawDownHigh[i] : currentAmount;
					} else {
						drawDown[i] = 0.0;
						drawDownHigh[i] = 0.0;
					}
				}

				mpt.setDrawDown(drawDown[i]);
				mpt.setSecurityID(securityID);
				mpt.setSecurityName(securityName);
				mpt.setSymbol(symbol);
				mpt.setSecurityType(securityType);
				mpt.setYear(new Long(securityMPTIndex[i]));
				mpt.setAssetClassID(assetClassID);
				mpt.setAssetClassName(assetClassName);
				securityMPTList.add(mpt);

				securityMPTIncDatas[i].setSigmaSB(sigmaSB[i]);
				securityMPTIncDatas[i].setSigmaS(sigmaS[i]);
				securityMPTIncDatas[i].setSigmaSS(sigmaSS[i]);
				securityMPTIncDatas[i].setSigmaB(sigmaB[i]);
				securityMPTIncDatas[i].setSigmaBB(sigmaBB[i]);
				securityMPTIncDatas[i].setSigmaR(sigmaR[i]);
				securityMPTIncDatas[i].setSigmaLS(sigmaLogS[i]);
				securityMPTIncDatas[i].setSigmaLSS(sigmaLogSS[i]);
				securityMPTIncDatas[i].setDrawDownHigh(drawDownHigh[i]);
				securityMPTIncDatas[i].setDataLastDate(today);
				if (inc[i])
					securityMPTIncDatas[i].setDataStartDate(yearNextStartDate[i]);
				else
					securityMPTIncDatas[i].setDataStartDate(yearStartDate[i]);
				securityMPTIncDatas[i].setSize(size[i]);
				securityMPTIncDatas[i].setIntervalDays(interval[i]);
				securityMPTIncDatas[i].setDrawDown(drawDown[i]);
				securityMPTIncDataList.add(securityMPTIncDatas[i]);

			}
			/**
			 * if it's the end of year ,we should resize the size and interval
			 * and so on
			 ***/
			if (LTIDate.equals(today, LTIDate.getLastNYSETradingDayOfYear(today))) {
				Date newYearDate = LTIDate.getNewNYSETradingDay(today, 1);
				securityMPTIncDatas[4].setSize(0);
				securityMPTIncDatas[4].setIntervalDays(0);
				securityMPTIncDatas[4].setDataStartDate(today);
				securityMPTIncDatas[4].setDataLastDate(today);
				securityMPTIncDatas[4].setSigmaB(0.0);
				securityMPTIncDatas[4].setSigmaBB(0.0);
				securityMPTIncDatas[4].setSigmaLS(0.0);
				securityMPTIncDatas[4].setSigmaLSS(0.0);
				securityMPTIncDatas[4].setSigmaR(0.0);
				securityMPTIncDatas[4].setSigmaSB(0.0);
				securityMPTIncDatas[4].setSigmaSS(0.0);
				securityMPTIncDatas[4].setYear(newYearDate.getYear() + 1900);
				securityMPTIncDatas[4].setDrawDownHigh(0.0);
				securityMPTIncDatas[4].setDrawDown(0.0);
			}
			securityManager.saveOrUpdateAllSecurityMPTIncDate(securityMPTIncDataList);
			securityManager.saveOrUpdateAllSecurityMPT(securityMPTList);

			return securityMPTList;

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * @author CCD update incremental data for MPT calculation and calculate mpt
	 *         *
	 ***/
	public void getOneSecurityMPT_Incremental(final Security security, final Security benchmark, List<SecurityMPTIncData> securityMPTIncDataList, Date today) throws NoPriceException {

		// System.out.println(today);
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Long cashID = securityManager.get("CASH").getID();
		/************ incremenatl data******************* */

		double[] sigmaSB = new double[5];
		double[] sigmaS = new double[5];
		double[] sigmaSS = new double[5];
		double[] sigmaB = new double[5];
		double[] sigmaBB = new double[5];
		double[] sigmaR = new double[5];
		double[] sigmaLogS = new double[5];
		double[] sigmaLogSS = new double[5];
		double[] lastSB = new double[5];
		double[] lastS = new double[5];
		double[] lastSS = new double[5];
		double[] lastB = new double[5];
		double[] lastBB = new double[5];
		double[] lastR = new double[5];
		double[] lastLogS = new double[5];
		double[] lastLogSS = new double[5];
		double newSB, newS, newSS, newB, newBB, newR, newLogS, newLogSS;
		double[] firstAmount = new double[5];
		double[] firstRiskFree = new double[5];
		double[] drawDown = new double[5];
		double[] drawDownHigh = new double[5];
		int[] size = new int[5];
		int[] interval = new int[5];
		List<SecurityMPT> securityMPTList = new ArrayList<SecurityMPT>();
		SecurityMPT mpt = new SecurityMPT();
		double currentAmount = 0, currentRiskFree = 0, tempDrawDown;
		SecurityMPT[] securityMPT = new SecurityMPT[5];

		/** security info **/
		Long securityID = security.getID();
		Long assetClassID = security.getClassID();
		String assetClassName = security.getClassName();
		String symbol = security.getSymbol();
		String securityName = security.getName();
		int securityType = security.getSecurityType();
		/**
		 * 0 stands for five year to today 1 stands for three year to today 2
		 * stands for one year to today 3 stands for startDate to today 4 stands
		 * for thisyear to today
		 */
		Date startDate = security.getStartDate();
		Date endDate = securityMPTIncDataList.get(0).getDataLastDate();
		Date[] yearStartDate = new Date[5];
		Date[] yearNextStartDate = new Date[5];
		boolean[] inc = new boolean[5];
		SecurityMPTIncData[] securityMPTIncDatas = new SecurityMPTIncData[5];
		for (int i = 0; i < 5; ++i) {
			securityMPTIncDatas[i] = securityMPTIncDataList.get(i);
			drawDown[i] = securityMPTIncDatas[i].getDrawDown();
			drawDownHigh[i] = securityMPTIncDatas[i].getDrawDownHigh();
		}
		/*** get the incremental data's start date **/
		for (int i = 0; i < 5; ++i) {
			yearStartDate[i] = securityMPTIncDatas[i].getDataStartDate();
			yearNextStartDate[i] = LTIDate.getNewNYSETradingDay(yearStartDate[i], 1);
			inc[i] = false;
		}
		/** if the incremental data size satisfy the time para ,set inc true **/
		for (int i = 0; i < 3; ++i)
			if (securityMPTIncDatas[i].getSize() == (TimePara.workingday * (0 - securityMPTIncDatas[i].getYear())))
				inc[i] = true;

		/** year indexs for security mpt ****/
		int[] securityMPTIndex = new int[5];
		securityMPTIndex[0] = -5;
		securityMPTIndex[1] = -3;
		securityMPTIndex[2] = -1;
		securityMPTIndex[3] = 0;
		securityMPTIndex[4] = today.getYear() + 1900;

		securityMPTIncDataList.clear();
		/*** calculate & adjust for incremental data ***/
		/**
		 * 
		 * 0 stands for five year to today 1 stands for three year to today 2
		 * stands for one year to today 3 stands for startDate to today PS: all
		 * the last data should be zero 4 stands for thisyear to today PS: all
		 * the last data should be zero *
		 ***/

		try {
			/** initial last data ***/
			for (int i = 0; i < 5; ++i) {
				if (!inc[i]) {
					firstAmount[i] = securityManager.getAdjPrice(securityID, yearStartDate[i]);
					firstRiskFree[i] = securityManager.getAdjPrice(cashID, yearStartDate[i]);
					lastS[i] = lastB[i] = lastR[i] = lastLogS[i] = lastSB[i] = lastSS[i] = lastBB[i] = lastLogSS[i] = 0.0;
					continue;
				}
				double securityLastAmount = securityManager.getAdjPrice(securityID, yearStartDate[i]);
				double securityNextLastAmount = securityManager.getAdjPrice(securityID, yearNextStartDate[i]);
				double benchmarkLastAmount = securityManager.getAdjPrice(benchmark.getID(), yearStartDate[i]);
				double benchmarkNextLastAmount = securityManager.getAdjPrice(benchmark.getID(), yearNextStartDate[i]);
				double cashLastAmount = securityManager.getAdjPrice(cashID, yearStartDate[i]);
				double cashNextLastAmount = securityManager.getAdjPrice(cashID, yearNextStartDate[i]);
				firstRiskFree[i] = cashNextLastAmount;
				firstAmount[i] = securityNextLastAmount;
				lastS[i] = this.computeIntervalReturn(securityNextLastAmount, securityLastAmount);
				lastB[i] = this.computeIntervalReturn(benchmarkNextLastAmount, benchmarkLastAmount);
				lastR[i] = this.computeIntervalReturn(cashNextLastAmount, cashLastAmount);
				lastLogS[i] = this.computeLogIntervalReturn(securityNextLastAmount, securityLastAmount);
				lastSB[i] = lastS[i] * lastB[i];
				lastSS[i] = lastS[i] * lastS[i];
				lastBB[i] = lastB[i] * lastB[i];
				lastLogSS[i] = lastLogS[i] * lastLogS[i];
			}

			/** initial new data ***/
			double securityLastAmount = securityManager.getAdjPrice(securityID, endDate);
			double securityNextLastAmount = securityManager.getAdjPrice(securityID, today);
			double benchmarkLastAmount = securityManager.getAdjPrice(benchmark.getID(), endDate);
			double benchmarkNextLastAmount = securityManager.getAdjPrice(benchmark.getID(), today);
			double cashLastAmount = securityManager.getAdjPrice(cashID, endDate);
			double cashNextLastAmount = securityManager.getAdjPrice(cashID, today);
			currentRiskFree = cashNextLastAmount;
			currentAmount = securityNextLastAmount;
			newS = this.computeIntervalReturn(securityNextLastAmount, securityLastAmount);
			newB = this.computeIntervalReturn(benchmarkNextLastAmount, benchmarkLastAmount);
			newR = this.computeIntervalReturn(cashNextLastAmount, cashLastAmount);
			newLogS = this.computeLogIntervalReturn(securityNextLastAmount, securityLastAmount);
			newSB = newS * newB;
			newSS = newS * newS;
			newBB = newB * newB;
			newLogSS = newLogS * newLogS;

			/**
			 * update mpt incremental data and calculate mpt
			 * */
			for (int i = 0; i < 5; ++i) {
				sigmaSB[i] = securityMPTIncDatas[i].getSigmaSB() - lastSB[i] + newSB;
				sigmaS[i] = securityMPTIncDatas[i].getSigmaS() - lastS[i] + newS;
				sigmaSS[i] = securityMPTIncDatas[i].getSigmaSS() - lastSS[i] + newSS;
				sigmaB[i] = securityMPTIncDatas[i].getSigmaB() - lastB[i] + newB;
				sigmaBB[i] = securityMPTIncDatas[i].getSigmaBB() - lastBB[i] + newBB;
				sigmaR[i] = securityMPTIncDatas[i].getSigmaR() - lastR[i] + newR;
				sigmaLogS[i] = securityMPTIncDatas[i].getSigmaLS() - lastLogS[i] + newLogS;
				sigmaLogSS[i] = securityMPTIncDatas[i].getSigmaLSS() - lastLogSS[i] + newLogSS;
				size[i] = securityMPTIncDatas[i].getSize();
				interval[i] = securityMPTIncDatas[i].getIntervalDays();
				if (!inc[i]) {
					++size[i];
					++interval[i];
				}
				// we could not calculate standarddeviation and so on if size<=1
				if (i == 4 && size[i] == 1) {
					// we have two dates' data, the last trading date of last
					// year and the first trading date of the new year
					tempDrawDown = (securityLastAmount - securityNextLastAmount) / securityLastAmount;
					drawDown[i] = tempDrawDown > drawDown[i] ? tempDrawDown : drawDown[i];
					drawDownHigh[i] = securityLastAmount > securityNextLastAmount ? securityLastAmount : securityNextLastAmount;
					mpt = new SecurityMPT();
				} else {
					// update mpt
					securityMPT[i] = securityManager.getSecurityMPT(securityID, securityMPTIndex[i]);
					mpt = calculateOneSecurityMPT_Incremental(securityMPT[i], firstAmount[i], currentAmount, size[i], firstRiskFree[i], currentRiskFree, sigmaSB[i], sigmaS[i], sigmaSS[i], sigmaB[i], sigmaBB[i], sigmaR[i], sigmaLogSS[i], sigmaLogS[i], interval[i], securityMPTIndex[i]);
					if (i >= 3) {
						tempDrawDown = (drawDownHigh[i] - currentAmount) / drawDownHigh[i];
						drawDown[i] = drawDown[i] > tempDrawDown ? drawDown[i] : tempDrawDown;
						drawDownHigh[i] = drawDownHigh[i] > currentAmount ? drawDownHigh[i] : currentAmount;
					} else {
						drawDown[i] = 0.0;
						drawDownHigh[i] = 0.0;
					}
				}

				mpt.setDrawDown(drawDown[i]);
				mpt.setSecurityID(securityID);
				mpt.setSecurityName(securityName);
				mpt.setSymbol(symbol);
				mpt.setSecurityType(securityType);
				mpt.setYear(new Long(securityMPTIndex[i]));
				mpt.setAssetClassID(assetClassID);
				mpt.setAssetClassName(assetClassName);
				// System.out.println(mpt.toString());
				securityMPTList.add(mpt);

				securityMPTIncDatas[i].setSigmaSB(sigmaSB[i]);
				securityMPTIncDatas[i].setSigmaS(sigmaS[i]);
				securityMPTIncDatas[i].setSigmaSS(sigmaSS[i]);
				securityMPTIncDatas[i].setSigmaB(sigmaB[i]);
				securityMPTIncDatas[i].setSigmaBB(sigmaBB[i]);
				securityMPTIncDatas[i].setSigmaR(sigmaR[i]);
				securityMPTIncDatas[i].setSigmaLS(sigmaLogS[i]);
				securityMPTIncDatas[i].setSigmaLSS(sigmaLogSS[i]);
				securityMPTIncDatas[i].setDrawDownHigh(drawDownHigh[i]);
				securityMPTIncDatas[i].setDataLastDate(today);
				if (inc[i])
					securityMPTIncDatas[i].setDataStartDate(yearNextStartDate[i]);
				else
					securityMPTIncDatas[i].setDataStartDate(yearStartDate[i]);
				securityMPTIncDatas[i].setSize(size[i]);
				securityMPTIncDatas[i].setIntervalDays(interval[i]);
				securityMPTIncDatas[i].setDrawDown(drawDown[i]);
				securityMPTIncDataList.add(securityMPTIncDatas[i]);

			}
			/**
			 * if it's the end of year ,we should resize the size and interval
			 * and so on
			 ***/
			if (LTIDate.equals(today, LTIDate.getLastNYSETradingDayOfYear(today))) {
				Date newYearDate = LTIDate.getNewNYSETradingDay(today, 1);
				securityMPTIncDatas[4].setSize(0);
				securityMPTIncDatas[4].setIntervalDays(0);
				securityMPTIncDatas[4].setDataStartDate(today);
				securityMPTIncDatas[4].setDataLastDate(today);
				securityMPTIncDatas[4].setSigmaB(0.0);
				securityMPTIncDatas[4].setSigmaBB(0.0);
				securityMPTIncDatas[4].setSigmaLS(0.0);
				securityMPTIncDatas[4].setSigmaLSS(0.0);
				securityMPTIncDatas[4].setSigmaR(0.0);
				securityMPTIncDatas[4].setSigmaSB(0.0);
				securityMPTIncDatas[4].setSigmaSS(0.0);
				securityMPTIncDatas[4].setYear(newYearDate.getYear() + 1900);
				securityMPTIncDatas[4].setDrawDownHigh(0.0);
				securityMPTIncDatas[4].setDrawDown(0.0);
			}
			securityManager.saveOrUpdateAllSecurityMPTIncDate(securityMPTIncDataList);
			securityManager.saveOrUpdateAllSecurityMPT(securityMPTList);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
		}
	}

	private List<SecurityDailyData> adjustBenchMarkList(List<PortfolioDailyData> pdds, List<SecurityDailyData> sdds) {
		List<SecurityDailyData> newList = new ArrayList<SecurityDailyData>();

		int securityIndex = 0;

		int securitySize = sdds.size();

		for (int i = 0; i < pdds.size(); i++) {

			Date curDate = pdds.get(i).getDate();

			if (securityIndex >= sdds.size()) {
				newList.add(sdds.get(securitySize - 1));
				continue;
			}

			SecurityDailyData curSeData = sdds.get(securityIndex);

			Date seDate = curSeData.getDate();
			if (LTIDate.equals(curDate, seDate)) {
				newList.add(curSeData);
				securityIndex++;
			} else {
				if (seDate.after(curDate)) {
					int j = securityIndex;
					SecurityDailyData tmp = sdds.get(j);
					boolean flag = false;
					while (j >= 0) {
						tmp = sdds.get(j);
						Date tmpDate = tmp.getDate();
						if (LTIDate.equals(tmpDate, curDate)) {
							newList.add(tmp);
							flag = true;
							break;
						} else if (tmpDate.before(curDate)) {
							newList.add(tmp);
							flag = true;
							break;
						}
						j--;
					}
					if (j < 0) {
						j = 0;
						if (!flag)
							newList.add(sdds.get(0));
					}
					securityIndex = j + 1;
				} else {
					int j = securityIndex;
					boolean flag = false;
					SecurityDailyData tmp = sdds.get(j);
					while (j < securitySize) {
						tmp = sdds.get(j);
						Date tmpDate = tmp.getDate();
						if (LTIDate.equals(tmpDate, curDate)) {
							newList.add(tmp);
							flag = true;
							break;
						} else if (tmpDate.after(curDate)) {
							newList.add(sdds.get(j - 1));
							flag = true;
							break;
						}
						j++;
					}
					if (j > securitySize - 1) {
						j = securitySize - 1;
						if (!flag)
							newList.add(sdds.get(j));
					}
					securityIndex = j + 1;
				}
			}
		}

		return newList;
	}

	private int adjustIndex(List<Date> dateList, int index, Date currentDate, int year) {
		int dateSize = dateList.size();
		Date pre = LTIDate.getNewNYSEYear(currentDate, year * -1);
		Date lastDate = dateList.get(index);
		if (LTIDate.equals(pre, lastDate))
			return index;
		else if (pre.after(lastDate)) {
			int tmpIndex = index + 1;
			while (true) {
				if (tmpIndex == dateSize)
					return tmpIndex - 1;
				Date cur = dateList.get(tmpIndex);
				if (LTIDate.equals(cur, pre))
					return tmpIndex;
				if (pre.after(cur)) {
					tmpIndex++;
				} else {
					return tmpIndex - 1;
				}
			}
		} else {
			int tmpIndex = index - 1;
			while (true) {
				if (tmpIndex == -1)
					return tmpIndex + 1;
				Date cur = dateList.get(tmpIndex);
				if (LTIDate.equals(cur, pre))
					return tmpIndex;
				if (pre.before(cur)) {
					tmpIndex--;
				} else {
					return tmpIndex + 1;
				}
			}
		}
	}

	private double[] getMaxFromList(List<Double> valueList) {
		double maxValue = valueList.get(0);
		double drawDown = 0;
		double minIndex = 0, maxIndex = 0, preMaxIndex = 0;
		for (int i = 0; i < valueList.size(); i++) {
			double tmp1 = valueList.get(i);
			if (maxValue < tmp1) {
				maxValue = tmp1;
				preMaxIndex = i;
			}
			double tmp2 = (maxValue - tmp1) / maxValue;
			if (drawDown < tmp2) {
				drawDown = tmp2;
				minIndex = i;
				maxIndex = preMaxIndex;
			}

		}
		double re[] = new double[5];
		re[0] = maxIndex;
		re[1] = minIndex;
		re[2] = preMaxIndex;
		re[3] = maxValue;
		re[4] = drawDown;
		return re;
	}
	@Override
	@Deprecated
	public List<PortfolioMPT> computeMPTs(Portfolio portfolio,HoldingInf holding, List<PortfolioDailyData> pdds) throws NoPriceException {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		Long t1 = System.currentTimeMillis();
		Long portfolioID = holding.getPortfolioID();
		// Portfolio portfolio = portfolioManager.get(portfolioID);
		if (holding == null || holding.getBenchmarkID() == 0l)
			return null;

		if (pdds == null || pdds.size() == 0)
			return null;

		Date startDate = pdds.get(0).getDate();
		Date endDate = pdds.get(pdds.size() - 1).getDate();

		SecurityManager sm = ContextHolder.getSecurityManager();
		List<SecurityDailyData> sdds = sm.getDailyDatas(holding.getBenchmarkID(), LTIDate.getNewWeekDay(startDate, -1), LTIDate.getNewWeekDay(endDate, 1));
		if (sdds == null || sdds.size() == 0)
			return null;

		Long t2 = System.currentTimeMillis();

		//System.out.println("before compute MPT Time:" + (t2 - t1));

		StrategyManager strategyManager = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Double> portfolioAmounts = new ArrayList<Double>();
		List<Double> riskFreeAmounts = new ArrayList<Double>();
		List<Double> riskFreeReturns = new ArrayList<Double>();

		List<Double> portfolioReturns = new ArrayList<Double>();
		List<Double> benchmarkReturns = new ArrayList<Double>();

		List<Double> yearAmounts = new ArrayList<Double>();

		Date totalStartDate, oneYearStartDate = new Date(), threeYearStartDate = new Date(), fiveYearStartDate = new Date();

		boolean caculateYearEnd = false;

		boolean isCashFlow = false;

		/** ************************************************************* */
		double sigmaXB1 = 0, sigmaXB3 = 0, sigmaXB5 = 0, sigmaXBN = 0;
		double sigmaX1 = 0, sigmaX3 = 0, sigmaX5 = 0, sigmaXN = 0;
		double sigmaXX1 = 0, sigmaXX3 = 0, sigmaXX5 = 0, sigmaXXN = 0;
		double sigmaB1 = 0, sigmaB3 = 0, sigmaB5 = 0, sigmaBN = 0;
		double sigmaBB1 = 0, sigmaBB3 = 0, sigmaBB5 = 0, sigmaBBN = 0;

		double sigmaR1 = 0, sigmaR3 = 0, sigmaR5 = 0, sigmaRN = 0;

		double lastR1 = 0, lastR3 = 0, lastR5 = 0, lastRN = 0;
		double lastX1 = 0, lastX3 = 0, lastX5 = 0, lastXN = 0;
		double lastB1 = 0, lastB3 = 0, lastB5 = 0, lastBN = 0;

		double lastLogR1 = 0, lastLogR3 = 0, lastLogR5 = 0, lastLogRN = 0;
		double lastLogX1 = 0, lastLogX3 = 0, lastLogX5 = 0, lastLogXN = 0;

		double sigmaLogXX1 = 0, sigmaLogXX3 = 0, sigmaLogXX5 = 0, sigmaLogXXN = 0;
		double sigmaLogX1 = 0, sigmaLogX3 = 0, sigmaLogX5 = 0, sigmaLogXN = 0;

		// sigmaSr = sigma(r-rf)^2 where r<rf
		double sigmaSR1 = 0, sigmaSR3 = 0, sigmaSR5 = 0, sigmaSRN = 0;

		boolean sigma1First = false, sigma3First = false, sigma5First = false;

		int index1 = 0, index3 = 0, index5 = 0;

		boolean firstYearEnd = true;

		double maxValue1 = 0, maxValue3 = 0, maxValue5 = 0, maxValueN = 0;
		int maxIndex1 = 0, maxIndex3 = 0, maxIndex5 = 0, maxIndexN = 0;
		int preMaxIndex1 = 0, preMaxIndex3 = 0, preMaxIndex5 = 0, preMaxIndexN = 0;
		int minIndex1 = 0, minIndex3 = 0, minIndex5 = 0, minIndexN = 0;
		double maxDrawDown1 = 0, maxDrawDown3 = 0, maxDrawDown5 = 0, maxDrawDownN = 0;

		Long benchMarkID = sdds.get(0).getSecurityID();
		// Portfolio thisOne = portfolioManager.get(portfolioID);
		Long strategyID = portfolio.getMainStrategyID();
		Long classID = null;
		Strategy strategy = null;
		if (portfolio.getMainStrategyID() != null) {
			strategy = strategyManager.get(portfolio.getMainStrategyID());
			if (strategy != null) {
				classID = strategy.getStrategyClassID();
				if (strategy.getStrategyClassID() == 5) {
					isCashFlow = true;
				}
			}
		}

		/** ************************************************************* */
		Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pdds.get(pdds.size() - 1).getDate());
		PortfolioMPT pmpt1;
		pmpt1 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_ONE_YEAR);
		if (pmpt1 == null)
			pmpt1 = new PortfolioMPT();
		PortfolioMPT pmpt3;
		pmpt3 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_THREE_YEAR);
		if (pmpt3 == null)
			pmpt3 = new PortfolioMPT();
		PortfolioMPT pmpt5;
		pmpt5 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_FIVE_YEAR);
		if (pmpt5 == null)
			pmpt5 = new PortfolioMPT();
		PortfolioMPT pmptA;
		pmptA = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE);
		if (pmptA == null)
			pmptA = new PortfolioMPT();
		boolean pmpt1Flag = false, pmpt3Flag = false, pmpt5Flag = false, pmptAFlag = false;
		/** ************************************************************* */

		Long userID = portfolio.getUserID();
		String name = portfolio.getName();
		Boolean isModel = portfolio.getMainStrategyID() == null ? false : true;

		double prePortfolioValue = 0;
		double curPortfolioValue;
		double preBenchMarkValue = 0;
		double curBenchMarkValue;

		double totalOriginalAmount = 0, oneYearAmount = 0, threeYearAmount = 0, fiveYearAmount = 0;

		List<PortfolioMPT> pmpts = new ArrayList<PortfolioMPT>();

		List<Date> dateList = new ArrayList<Date>();

		startDate = pdds.get(0).getDate();

		totalStartDate = startDate;

		totalOriginalAmount = pdds.get(0).getAmount();

		List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();

		PortfolioDailyData oneYearPdd = new PortfolioDailyData();

		double portfolioReturn = 0;
		double benchMarkReturn = 0;
		double riskFreeReturn = 0;

		double portfolioLogReturn = 0;
		double riskFreeLogReturn = 0;

		/**
		 * *********************************************************************
		 * *******************
		 */
		sdds = this.adjustBenchMarkList(pdds, sdds);
		Security se = getCash();
		List<SecurityDailyData> riskFreeList = securityManager.getDailyDatas(se.getID(), startDate, pdds.get(pdds.size() - 1).getDate());
		riskFreeList = this.adjustBenchMarkList(pdds, riskFreeList);
		double curRiskFreeValue = 0;
		double preRiskFreeValue = 0;
		/**
		 * *********************************************************************
		 * *******************
		 */

		for (int i = 0; i < pdds.size(); i++) {

			Date currentDate = pdds.get(i).getDate();

			caculateYearEnd = false;

			int interval = LTIDate.calculateIntervalIgnoreHolidDay(totalStartDate, currentDate);
			PortfolioDailyData pdd = pdds.get(i);

			curPortfolioValue = pdd.getAmount();
			/**
			 * if isCashFlow is true, we shoule modify the curPortfolioValue by
			 * take the transaction amount into consideration*
			 */
			if (isCashFlow) {
				//TODO:Here
				List<Transaction> transactionList=null ;//= portfolio.getTransactions(currentDate);
				if (transactionList != null && transactionList.size() > 0) {
					int ti;
					Transaction tmp;
					for (ti = 0; ti < transactionList.size(); ++ti) {
						tmp = transactionList.get(ti);
						if (tmp.getOperation().equals("DEPOSIT")) {
							curPortfolioValue += tmp.getAmount();
						}
						if (tmp.getOperation().equals("WITHDRAW")) {
							curPortfolioValue -= tmp.getAmount();
						}
					}
				}
			}

			curBenchMarkValue = sdds.get(i).getAdjClose();
			curRiskFreeValue = riskFreeList.get(i).getAdjClose();

			portfolioAmounts.add(curPortfolioValue);

			riskFreeAmounts.add(curRiskFreeValue);

			/** *************************** */
			if (maxValueN < curPortfolioValue) {
				maxValueN = curPortfolioValue;
				preMaxIndexN = i;
			}
			/** *************************** */

			dateList.add(currentDate);

			if (i == 0) {
				prePortfolioValue = curPortfolioValue;
				preBenchMarkValue = curBenchMarkValue;
				preRiskFreeValue = curRiskFreeValue;
			}
			if (i > 0) {
				portfolioReturn = this.computeIntervalReturn(curPortfolioValue, prePortfolioValue);
				benchMarkReturn = this.computeIntervalReturn(curBenchMarkValue, preBenchMarkValue);
				riskFreeReturn = this.computeIntervalReturn(curRiskFreeValue, preRiskFreeValue);

				portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioValue, prePortfolioValue);
				riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeValue, preRiskFreeValue);

				portfolioReturns.add(portfolioReturn);
				benchmarkReturns.add(benchMarkReturn);
				riskFreeReturns.add(riskFreeReturn);
				prePortfolioValue = curPortfolioValue;
				preBenchMarkValue = curBenchMarkValue;
				preRiskFreeValue = curRiskFreeValue;

				sigmaXBN = sigmaXBN + portfolioReturn * benchMarkReturn;
				sigmaXN = sigmaXN + portfolioReturn;
				sigmaXXN = sigmaXXN + portfolioReturn * portfolioReturn;
				sigmaBN = sigmaBN + benchMarkReturn;
				sigmaBBN = sigmaBBN + benchMarkReturn * benchMarkReturn;
				sigmaRN = sigmaRN + riskFreeReturn;

				sigmaLogXXN = sigmaLogXXN + portfolioLogReturn * portfolioLogReturn;
				sigmaLogXN = sigmaLogXN + portfolioLogReturn;

				if (portfolioLogReturn < riskFreeLogReturn)
					sigmaSRN = sigmaSRN + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

				double tmpDrawDown = (maxValueN - curPortfolioValue) / maxValueN;
				if (maxDrawDownN < tmpDrawDown) {
					maxDrawDownN = tmpDrawDown;
					minIndexN = i;
					maxIndexN = preMaxIndexN;
				}

			}

			boolean isHoldingDate = LTIDate.equals(lastHoldingDate, currentDate);

			// if last date or holding date
			if (i == pdds.size() - 1 || isHoldingDate) {
				double firstAmount = portfolioAmounts.get(0);
				double size = (double) portfolioAmounts.size() - 1;
				double lastAmount = portfolioAmounts.get((int) size);

				double firstRiskFree = riskFreeAmounts.get(0);
				double lastRiskFree = riskFreeAmounts.get((int) size);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDownN, sigmaXBN, sigmaXN, sigmaXXN, sigmaBN, sigmaBBN, sigmaRN, sigmaSRN, sigmaLogXXN, sigmaLogXN, totalStartDate, currentDate, interval, isCashFlow, portfolio, totalOriginalAmount);
				pdd = setAllMPT(pdd, newPdd, 0);

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmptA = this.setAllBasicInfo(pmptA, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE, newPdd);
					pmptAFlag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}

			if (LTIDate.isLastNYSETradingDayOfYear(currentDate) || i == pdds.size() - 1 || LTIDate.isYearEnd(currentDate))
				caculateYearEnd = true;

			int days = LTIDate.calculateInterval(totalStartDate, currentDate);

			if (days >= TimePara.yearday) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 1);
				int tmpIndex = this.adjustIndex(dateList, index1 + 1, currentDate, 1);
				startDate = dateList.get(tmpIndex);

				oneYearStartDate = startDate;

				oneYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma1First == false) {
					sigma1First = true;
					sigmaXB1 = sigmaXBN;
					sigmaX1 = sigmaXN;
					sigmaXX1 = sigmaXXN;
					sigmaB1 = sigmaBN;
					sigmaBB1 = sigmaBBN;
					sigmaR1 = sigmaRN;
					sigmaSR1 = sigmaSRN;
					sigmaLogXX1 = sigmaLogXXN;
					sigmaLogX1 = sigmaLogXN;
					index1 = 0;

					maxValue1 = maxValueN;
					maxDrawDown1 = maxDrawDownN;
					minIndex1 = minIndexN;
					maxIndex1 = maxIndexN;
					preMaxIndex1 = preMaxIndexN;
				} else {
					sigmaXB1 = sigmaXB1 + portfolioReturn * benchMarkReturn;
					sigmaX1 = sigmaX1 + portfolioReturn;
					sigmaXX1 = sigmaXX1 + portfolioReturn * portfolioReturn;
					sigmaB1 = sigmaB1 + benchMarkReturn;
					sigmaBB1 = sigmaBB1 + benchMarkReturn * benchMarkReturn;
					sigmaR1 = sigmaR1 + riskFreeReturn;

					sigmaLogXX1 = sigmaLogXX1 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX1 = sigmaLogX1 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR1 = sigmaSR1 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue1 < curPortfolioValue) {
						maxValue1 = curPortfolioValue;
						preMaxIndex1 = i;
					}
					double tmpDrawDown = (maxValue1 - curPortfolioValue) / maxValue1;
					if (maxDrawDown1 < tmpDrawDown) {
						maxDrawDown1 = tmpDrawDown;
						minIndex1 = i;
						maxIndex1 = preMaxIndex1;
					}

					if (index1 <= tmpIndex - 1) {
						while (index1 <= tmpIndex - 1) {
							lastX1 = portfolioReturns.get(index1);
							lastB1 = benchmarkReturns.get(index1);
							lastR1 = riskFreeReturns.get(index1);

							lastLogX1 = Math.log(1 + lastX1);
							lastLogR1 = Math.log(1 + lastR1);

							sigmaXB1 = sigmaXB1 - lastX1 * lastB1;
							sigmaX1 = sigmaX1 - lastX1;
							sigmaXX1 = sigmaXX1 - lastX1 * lastX1;
							sigmaB1 = sigmaB1 - lastB1;
							sigmaBB1 = sigmaBB1 - lastB1 * lastB1;
							sigmaR1 = sigmaR1 - lastR1;

							sigmaLogXX1 = sigmaLogXX1 - lastLogX1 * lastLogX1;
							sigmaLogX1 = sigmaLogX1 - lastLogX1;

							if (lastLogX1 < lastLogR1)
								sigmaSR1 = sigmaSR1 - (lastLogX1 - lastLogR1) * (lastLogX1 - lastLogR1);

							if (!deleteFlag)
								deleteFlag = (index1 == maxIndex1);
							index1++;
						}

					}

					else if (index1 > tmpIndex) {
						while (index1 > tmpIndex) {
							lastX1 = portfolioReturns.get(index1);
							lastB1 = benchmarkReturns.get(index1);
							lastR1 = riskFreeReturns.get(index1);

							lastLogX1 = Math.log(1 + lastX1);
							lastLogR1 = Math.log(1 + lastR1);

							sigmaXB1 = sigmaXB1 + lastX1 * lastB1;
							sigmaX1 = sigmaX1 + lastX1;
							sigmaXX1 = sigmaXX1 + lastX1 * lastX1;
							sigmaB1 = sigmaB1 + lastB1;
							sigmaBB1 = sigmaBB1 + lastB1 * lastB1;
							sigmaR1 = sigmaR1 + lastR1;

							sigmaLogXX1 = sigmaLogXX1 + lastLogX1 * lastLogX1;
							sigmaLogX1 = sigmaLogX1 + lastLogX1;

							if (lastLogX1 < lastLogR1)
								sigmaSR1 = sigmaSR1 + (lastLogX1 - lastLogR1) * (lastLogX1 - lastLogR1);

							if (!deleteFlag)
								deleteFlag = (index1 == maxIndex1);
							if (!deleteFlag)
								deleteFlag = (index1 == minIndex1);
							index1--;

						}
					}

					index1 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index1, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex1 = (int) sb[0] + index1;
					minIndex1 = (int) sb[1] + index1;
					preMaxIndex1 = (int) sb[2] + index1;
					maxValue1 = sb[3];
					maxDrawDown1 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index1);
				double size = (double) (i - index1);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index1);
				double lastRiskFree = riskFreeAmounts.get(i);
				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown1, sigmaXB1, sigmaX1, sigmaXX1, sigmaB1, sigmaBB1, sigmaR1, sigmaSR1, sigmaLogXX1, sigmaLogX1, startDate, currentDate, TimePara.workingday, isCashFlow, portfolio, oneYearAmount);

				pdd = setAllMPT(pdd, newPdd, 1);

				CopyUtil.Translate(newPdd, oneYearPdd);

				if (pdd.getSharpeRatio1() != null && pdd.getSharpeRatio1() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 1 + " "+
					// pdd.getSharpeRatio1() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt1 = this.setAllBasicInfo(pmpt1, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_ONE_YEAR, newPdd);
					pmpt1Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			if (days >= TimePara.yearday * 3) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 3);
				int tmpIndex = this.adjustIndex(dateList, index3 + 1, currentDate, 3);
				startDate = dateList.get(tmpIndex);

				threeYearStartDate = startDate;

				threeYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma3First == false) {
					sigma3First = true;
					sigmaXB3 = sigmaXBN;
					sigmaX3 = sigmaXN;
					sigmaXX3 = sigmaXXN;
					sigmaB3 = sigmaBN;
					sigmaBB3 = sigmaBBN;
					sigmaR3 = sigmaRN;
					sigmaSR3 = sigmaSRN;
					sigmaLogXX3 = sigmaLogXXN;
					sigmaLogX3 = sigmaLogXN;

					index3 = 0;

					maxValue3 = maxValueN;
					maxDrawDown3 = maxDrawDownN;
					minIndex3 = minIndexN;
					maxIndex3 = maxIndexN;
					preMaxIndex3 = preMaxIndexN;
				} else {
					sigmaXB3 = sigmaXB3 + portfolioReturn * benchMarkReturn;
					sigmaX3 = sigmaX3 + portfolioReturn;
					sigmaXX3 = sigmaXX3 + portfolioReturn * portfolioReturn;
					sigmaB3 = sigmaB3 + benchMarkReturn;
					sigmaBB3 = sigmaBB3 + benchMarkReturn * benchMarkReturn;
					sigmaR3 = sigmaR3 + riskFreeReturn;

					sigmaLogXX3 = sigmaLogXX3 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX3 = sigmaLogX3 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR3 = sigmaSR3 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue3 < curPortfolioValue) {
						maxValue3 = curPortfolioValue;
						preMaxIndex3 = i;
					}
					double tmpDrawDown = (maxValue3 - curPortfolioValue) / maxValue3;
					if (maxDrawDown3 < tmpDrawDown) {
						maxDrawDown3 = tmpDrawDown;
						minIndex3 = i;
						maxIndex3 = preMaxIndex3;
					}

					if (index3 <= tmpIndex - 1) {
						while (index3 <= tmpIndex - 1) {
							lastX3 = portfolioReturns.get(index3);
							lastB3 = benchmarkReturns.get(index3);
							lastR3 = riskFreeReturns.get(index3);

							lastLogX3 = Math.log(1 + lastX3);
							lastLogR3 = Math.log(1 + lastR3);

							sigmaXB3 = sigmaXB3 - lastX3 * lastB3;
							sigmaX3 = sigmaX3 - lastX3;
							sigmaXX3 = sigmaXX3 - lastX3 * lastX3;
							sigmaB3 = sigmaB3 - lastB3;
							sigmaBB3 = sigmaBB3 - lastB3 * lastB3;
							sigmaR3 = sigmaR3 - lastR3;

							sigmaLogXX3 = sigmaLogXX3 - lastLogX3 * lastLogX3;
							sigmaLogX3 = sigmaLogX3 - lastLogX3;

							if (lastLogX3 < lastLogR3)
								sigmaSR3 = sigmaSR3 - (lastLogX3 - lastLogR3) * (lastLogX3 - lastLogR3);

							if (!deleteFlag)
								deleteFlag = (index3 == maxIndex3);
							index3++;
						}
					} else if (index3 > tmpIndex) {
						while (index3 > tmpIndex) {
							lastX3 = portfolioReturns.get(index3);
							lastB3 = benchmarkReturns.get(index3);
							lastR3 = riskFreeReturns.get(index3);

							lastLogX3 = Math.log(1 + lastX3);
							lastLogR3 = Math.log(1 + lastR3);

							sigmaXB3 = sigmaXB3 + lastX3 * lastB3;
							sigmaX3 = sigmaX3 + lastX3;
							sigmaXX3 = sigmaXX3 + lastX3 * lastX3;
							sigmaB3 = sigmaB3 + lastB3;
							sigmaBB3 = sigmaBB3 + lastB3 * lastB3;
							sigmaR3 = sigmaR3 + lastR3;

							sigmaLogXX3 = sigmaLogXX3 + lastLogX3 * lastLogX3;
							sigmaLogX3 = sigmaLogX3 + lastLogX3;

							if (lastLogX3 < lastLogR3)
								sigmaSR3 = sigmaSR3 + (lastLogX3 - lastLogR3) * (lastLogX3 - lastLogR3);

							if (!deleteFlag)
								deleteFlag = (index3 == maxIndex3);
							if (!deleteFlag)
								deleteFlag = (index3 == minIndex3);

							index3--;
						}
					}

					index3 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index3, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex3 = (int) sb[0] + index3;
					minIndex3 = (int) sb[1] + index3;
					preMaxIndex3 = (int) sb[2] + index3;
					maxValue3 = sb[3];
					maxDrawDown3 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index3);
				double size = (double) (i - index3);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index3);
				double lastRiskFree = riskFreeAmounts.get(i);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown3, sigmaXB3, sigmaX3, sigmaXX3, sigmaB3, sigmaBB3, sigmaR3, sigmaSR3, sigmaLogXX3, sigmaLogX3, startDate, currentDate, TimePara.workingday * 3, isCashFlow, portfolio, threeYearAmount);

				pdd = setAllMPT(pdd, newPdd, 3);

				if (pdd.getSharpeRatio3() != null && pdd.getSharpeRatio3() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 3 + " "+
					// pdd.getSharpeRatio3() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt3 = this.setAllBasicInfo(pmpt3, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_THREE_YEAR, newPdd);
					pmpt3Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			if (days >= TimePara.yearday * 5) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 5);
				int tmpIndex = this.adjustIndex(dateList, index5 + 1, currentDate, 5);
				startDate = dateList.get(tmpIndex);

				fiveYearStartDate = startDate;

				fiveYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma5First == false) {
					sigma5First = true;
					sigmaXB5 = sigmaXBN;
					sigmaX5 = sigmaXN;
					sigmaXX5 = sigmaXXN;
					sigmaB5 = sigmaBN;
					sigmaBB5 = sigmaBBN;
					sigmaR5 = sigmaRN;
					sigmaSR5 = sigmaSRN;
					sigmaLogXX5 = sigmaLogXXN;
					sigmaLogX5 = sigmaLogXN;

					index5 = 0;

					maxValue5 = maxValueN;
					maxDrawDown5 = maxDrawDownN;
					minIndex5 = minIndexN;
					maxIndex5 = maxIndexN;
					preMaxIndex5 = preMaxIndexN;

				} else {
					sigmaXB5 = sigmaXB5 + portfolioReturn * benchMarkReturn;
					sigmaX5 = sigmaX5 + portfolioReturn;
					sigmaXX5 = sigmaXX5 + portfolioReturn * portfolioReturn;
					sigmaB5 = sigmaB5 + benchMarkReturn;
					sigmaBB5 = sigmaBB5 + benchMarkReturn * benchMarkReturn;
					sigmaR5 = sigmaR5 + riskFreeReturn;

					sigmaLogXX5 = sigmaLogXX5 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX5 = sigmaLogX5 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR5 = sigmaSR5 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue5 < curPortfolioValue) {
						maxValue5 = curPortfolioValue;
						preMaxIndex5 = i;
					}
					double tmpDrawDown = (maxValue5 - curPortfolioValue) / maxValue5;
					if (maxDrawDown5 < tmpDrawDown) {
						maxDrawDown5 = tmpDrawDown;
						minIndex5 = i;
						maxIndex5 = preMaxIndex5;
					}

					if (index5 <= tmpIndex - 1) {
						while (index5 <= tmpIndex - 1) {
							lastX5 = portfolioReturns.get(index5);
							lastB5 = benchmarkReturns.get(index5);
							lastR5 = riskFreeReturns.get(index5);

							lastLogX5 = Math.log(1 + lastX5);
							lastLogR5 = Math.log(1 + lastR5);

							sigmaXB5 = sigmaXB5 - lastX5 * lastB5;
							sigmaX5 = sigmaX5 - lastX5;
							sigmaXX5 = sigmaXX5 - lastX5 * lastX5;
							sigmaB5 = sigmaB5 - lastB5;
							sigmaBB5 = sigmaBB5 - lastB5 * lastB5;
							sigmaR5 = sigmaR5 - lastR5;

							sigmaLogXX5 = sigmaLogXX5 - lastLogX5 * lastLogX5;
							sigmaLogX5 = sigmaLogX5 - lastLogX5;

							if (lastLogX5 < lastLogR5)
								sigmaSR5 = sigmaSR5 - (lastLogX5 - lastLogR5) * (lastLogX5 - lastLogR5);

							if (!deleteFlag)
								deleteFlag = (index5 == maxIndex5);
							index5++;
						}
					} else if (index5 > tmpIndex) {
						while (index5 > tmpIndex) {
							lastX5 = portfolioReturns.get(index5);
							lastB5 = benchmarkReturns.get(index5);
							lastR5 = riskFreeReturns.get(index5);

							lastLogX5 = Math.log(1 + lastX5);
							lastLogR5 = Math.log(1 + lastR5);

							sigmaXB5 = sigmaXB5 + lastX5 * lastB5;
							sigmaX5 = sigmaX5 + lastX5;
							sigmaXX5 = sigmaXX5 + lastX5 * lastX5;
							sigmaB5 = sigmaB5 + lastB5;
							sigmaBB5 = sigmaBB5 + lastB5 * lastB5;
							sigmaR5 = sigmaR5 + lastR5;

							sigmaLogXX5 = sigmaLogXX5 + lastLogX5 * lastLogX5;
							sigmaLogX5 = sigmaLogX5 + lastLogX5;

							if (lastLogX5 < lastLogR5)
								sigmaSR5 = sigmaSR5 + (lastLogX5 - lastLogR5) * (lastLogX5 - lastLogR5);

							if (!deleteFlag)
								deleteFlag = (index5 == maxIndex5);
							if (!deleteFlag)
								deleteFlag = (index5 == minIndex5);
							index5--;
						}
					}

					index5 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index5, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex5 = (int) sb[0] + index5;
					minIndex5 = (int) sb[1] + index5;
					preMaxIndex5 = (int) sb[2] + index5;
					maxValue5 = sb[3];
					maxDrawDown5 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index5);
				double size = (double) (i - index5);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index5);
				double lastRiskFree = riskFreeAmounts.get(i);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown5, sigmaXB5, sigmaX5, sigmaXX5, sigmaB5, sigmaBB5, sigmaR5, sigmaSR5, sigmaLogXX5, sigmaLogX5, startDate, currentDate, TimePara.workingday * 5, isCashFlow, portfolio, fiveYearAmount);

				pdd = setAllMPT(pdd, newPdd, 5);

				if (pdd.getSharpeRatio5() != null && pdd.getSharpeRatio5() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 5 + " "+
					// pdd.getSharpeRatio5() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt5 = this.setAllBasicInfo(pmpt5, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_FIVE_YEAR, newPdd);
					pmpt5Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			newpdds.add(pdd);

			if (caculateYearEnd) {

				PortfolioDailyData newPdd;
				int index = 0;

				if (firstYearEnd) {
					double firstAmount = portfolioAmounts.get(0);
					double size = (double) (portfolioAmounts.size() - 1);
					double lastAmount = portfolioAmounts.get((int) size);

					double firstRiskFree = riskFreeAmounts.get(0);
					double lastRiskFree = riskFreeAmounts.get(i);

					newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDownN, sigmaXBN, sigmaXN, sigmaXXN, sigmaBN, sigmaBBN, sigmaRN, sigmaSRN, sigmaLogXXN, sigmaLogXN, totalStartDate, currentDate, -100, isCashFlow, portfolio, firstAmount);

					firstYearEnd = false;
				} else {
					double XB = sigmaXB1, X = sigmaX1, XX = sigmaXX1, BB = sigmaBB1, B = sigmaB1, R = sigmaR1, lastX, lastB, lastR, lastLogX, lastLogR, logXX = sigmaLogXX1, logX = sigmaLogX1;
					double SR = sigmaSR1;
					index = getYearEndIndex(dateList, currentDate);
					if (index != index1) {
						int tmpLastIndex = index1;

						while (tmpLastIndex < index) {
							lastX = portfolioReturns.get(tmpLastIndex);
							lastB = benchmarkReturns.get(tmpLastIndex);
							lastR = riskFreeReturns.get(tmpLastIndex);

							lastLogX = Math.log(1 + lastX);
							lastLogR = Math.log(1 + lastR);

							XB = XB - lastX * lastB;
							X = X - lastX;
							XX = XX - lastX * lastX;
							B = B - lastB;
							BB = BB - lastB * lastB;
							R = R - lastR;

							logXX = logXX - lastLogX * lastLogX;
							logX = logX - lastLogX;

							if (lastLogX < lastLogR)
								SR = SR - (lastLogX - lastLogR) * (lastLogX - lastLogR);

							tmpLastIndex++;
						}
					}
					List<Double> lastYearAmounts = portfolioAmounts.subList(index, i + 1);
					startDate = dateList.get(index);
					double drawDown = this.computeDrawDown(lastYearAmounts);
					double firstAmount = lastYearAmounts.get(0);
					double size = (double) lastYearAmounts.size() - 1;
					double lastAmount = lastYearAmounts.get((int) size);

					double firstRiskFree = riskFreeAmounts.get(index);
					double lastRiskFree = riskFreeAmounts.get(i);

					newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, drawDown, XB, X, XX, B, BB, R, SR, logXX, logX, startDate, currentDate, -100, isCashFlow, portfolio, firstAmount);

				}

				PortfolioMPT pmpt;

				int year = currentDate.getYear() + 1900;

				pmpt = portfolioManager.getPortfolioMPT(portfolioID, year);

				if (pmpt == null)
					pmpt = new PortfolioMPT();
				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, strategyID, userID, isModel, classID, year, newPdd);

				if (pmpt.getSharpeRatio() != null && pmpt.getSharpeRatio() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + year +
					// " "+ pdd.getSharpeRatio() + " happen at monitor");
				}

				pmpts.add(pmpt);
			}
		}

		// from start date to end date
		if (newpdds != null) {
			PortfolioDailyData pdd;
			pdd = newpdds.get(newpdds.size() - 1);
			double lastAmount = pdd.getAmount();
			if (pdd != null) {

				PortfolioMPT pmpt;
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, 0);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.FROM_STARTDATE_TO_ENDDATE, pdd);

				pmpts.add(pmpt);

				// 1 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -1);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_ONE_YEAR, pdd);

				if (isCashFlow && pdd.getAR1() != null) {
					Double ar = this.computeIRR(portfolio, oneYearStartDate, pdd.getDate(), oneYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR1(ar);
					}
				}
				pmpts.add(pmpt);

				// 3 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -3);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_THREE_YEAR, pdd);
				if (isCashFlow && pdd.getAR3() != null) {
					Double ar = this.computeIRR(portfolio, threeYearStartDate, pdd.getDate(), threeYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR3(ar);
					}
				}
				pmpts.add(pmpt);

				// 5 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -5);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_FIVE_YEAR, pdd);
				if (isCashFlow && pdd.getAR5() != null) {
					Double ar = this.computeIRR(portfolio, fiveYearStartDate, pdd.getDate(), fiveYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR5(ar);
					}
				}
				pmpts.add(pmpt);

				newpdds.remove(newpdds.size() - 1);
				newpdds.add(pdd);
			}

		}

		/** ******************************************************************** */
		if (pmpt1Flag)
			pmpts.add(pmpt1);
		if (pmpt3Flag)
			pmpts.add(pmpt3);
		if (pmpt5Flag)
			pmpts.add(pmpt5);
		if (pmptAFlag)
			pmpts.add(pmptA);
		/** ******************************************************************** */

		pdds=newpdds;

		Long t3 = System.currentTimeMillis();

		//System.out.println("compute MPT Time:" + (t3 - t2));
		return pmpts;
	}

	@Deprecated
	public List<PortfolioDailyData> getNewPortfolioDailyData(final Portfolio portfolio, List<PortfolioDailyData> pdds, List<SecurityDailyData> sdds) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		StrategyManager strategyManager = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Double> portfolioAmounts = new ArrayList<Double>();
		List<Double> riskFreeAmounts = new ArrayList<Double>();
		List<Double> riskFreeReturns = new ArrayList<Double>();

		List<Double> portfolioReturns = new ArrayList<Double>();
		List<Double> benchmarkReturns = new ArrayList<Double>();

		List<Double> yearAmounts = new ArrayList<Double>();

		Date totalStartDate, oneYearStartDate = new Date(), threeYearStartDate = new Date(), fiveYearStartDate = new Date();

		boolean caculateYearEnd = false;

		boolean isCashFlow = false;

		/** ************************************************************* */
		double sigmaXB1 = 0, sigmaXB3 = 0, sigmaXB5 = 0, sigmaXBN = 0;
		double sigmaX1 = 0, sigmaX3 = 0, sigmaX5 = 0, sigmaXN = 0;
		double sigmaXX1 = 0, sigmaXX3 = 0, sigmaXX5 = 0, sigmaXXN = 0;
		double sigmaB1 = 0, sigmaB3 = 0, sigmaB5 = 0, sigmaBN = 0;
		double sigmaBB1 = 0, sigmaBB3 = 0, sigmaBB5 = 0, sigmaBBN = 0;

		double sigmaR1 = 0, sigmaR3 = 0, sigmaR5 = 0, sigmaRN = 0;

		double lastR1 = 0, lastR3 = 0, lastR5 = 0, lastRN = 0;
		double lastX1 = 0, lastX3 = 0, lastX5 = 0, lastXN = 0;
		double lastB1 = 0, lastB3 = 0, lastB5 = 0, lastBN = 0;

		double lastLogR1 = 0, lastLogR3 = 0, lastLogR5 = 0, lastLogRN = 0;
		double lastLogX1 = 0, lastLogX3 = 0, lastLogX5 = 0, lastLogXN = 0;

		double sigmaLogXX1 = 0, sigmaLogXX3 = 0, sigmaLogXX5 = 0, sigmaLogXXN = 0;
		double sigmaLogX1 = 0, sigmaLogX3 = 0, sigmaLogX5 = 0, sigmaLogXN = 0;

		// sigmaSr = sigma(r-rf)^2 where r<rf
		double sigmaSR1 = 0, sigmaSR3 = 0, sigmaSR5 = 0, sigmaSRN = 0;

		boolean sigma1First = false, sigma3First = false, sigma5First = false;

		int index1 = 0, index3 = 0, index5 = 0;

		boolean firstYearEnd = true;

		double maxValue1 = 0, maxValue3 = 0, maxValue5 = 0, maxValueN = 0;
		int maxIndex1 = 0, maxIndex3 = 0, maxIndex5 = 0, maxIndexN = 0;
		int preMaxIndex1 = 0, preMaxIndex3 = 0, preMaxIndex5 = 0, preMaxIndexN = 0;
		int minIndex1 = 0, minIndex3 = 0, minIndex5 = 0, minIndexN = 0;
		double maxDrawDown1 = 0, maxDrawDown3 = 0, maxDrawDown5 = 0, maxDrawDownN = 0;

		Long portfolioID = pdds.get(0).getPortfolioID();
		Long benchMarkID = sdds.get(0).getSecurityID();
		// Portfolio thisOne = portfolioManager.get(portfolioID);
		Long strategyID = portfolio.getMainStrategyID();
		Long classID = null;
		Strategy strategy = null;
		if (portfolio.getMainStrategyID() != null) {
			strategy = strategyManager.get(portfolio.getMainStrategyID());
			if (strategy != null) {
				classID = strategy.getStrategyClassID();
				if (strategy.getStrategyClassID() == 5) {
					isCashFlow = true;
				}
			}
		}

		/** ************************************************************* */
		Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pdds.get(pdds.size() - 1).getDate());
		PortfolioMPT pmpt1;
		pmpt1 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_ONE_YEAR);
		if (pmpt1 == null)
			pmpt1 = new PortfolioMPT();
		PortfolioMPT pmpt3;
		pmpt3 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_THREE_YEAR);
		if (pmpt3 == null)
			pmpt3 = new PortfolioMPT();
		PortfolioMPT pmpt5;
		pmpt5 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_FIVE_YEAR);
		if (pmpt5 == null)
			pmpt5 = new PortfolioMPT();
		PortfolioMPT pmptA;
		pmptA = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE);
		if (pmptA == null)
			pmptA = new PortfolioMPT();
		boolean pmpt1Flag = false, pmpt3Flag = false, pmpt5Flag = false, pmptAFlag = false;
		/** ************************************************************* */

		Long userID = portfolio.getUserID();
		String name = portfolio.getName();
		Boolean isModel = portfolio.getMainStrategyID() == null ? false : true;

		double prePortfolioValue = 0;
		double curPortfolioValue;
		double preBenchMarkValue = 0;
		double curBenchMarkValue;

		double totalOriginalAmount = 0, oneYearAmount = 0, threeYearAmount = 0, fiveYearAmount = 0;

		List<PortfolioMPT> pmpts = new ArrayList<PortfolioMPT>();

		List<Date> dateList = new ArrayList<Date>();

		Date startDate = pdds.get(0).getDate();

		totalStartDate = startDate;

		totalOriginalAmount = pdds.get(0).getAmount();

		List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();

		PortfolioDailyData oneYearPdd = new PortfolioDailyData();

		double portfolioReturn = 0;
		double benchMarkReturn = 0;
		double riskFreeReturn = 0;

		double portfolioLogReturn = 0;
		double riskFreeLogReturn = 0;

		/**
		 * *********************************************************************
		 * *******************
		 */
		sdds = this.adjustBenchMarkList(pdds, sdds);
		Security se = getCash();
		List<SecurityDailyData> riskFreeList = securityManager.getDailyDatas(se.getID(), startDate, pdds.get(pdds.size() - 1).getDate());
		riskFreeList = this.adjustBenchMarkList(pdds, riskFreeList);
		double curRiskFreeValue = 0;
		double preRiskFreeValue = 0;
		/**
		 * *********************************************************************
		 * *******************
		 */

		for (int i = 0; i < pdds.size(); i++) {

			Date currentDate = pdds.get(i).getDate();

			caculateYearEnd = false;

			int interval = LTIDate.calculateIntervalIgnoreHolidDay(totalStartDate, currentDate);
			PortfolioDailyData pdd = pdds.get(i);

			curPortfolioValue = pdd.getAmount();
			/**
			 * if isCashFlow is true, we shoule modify the curPortfolioValue by
			 * take the transaction amount into consideration*
			 */
			if (isCashFlow) {
				//TODO:hhhhhhhhhhhhhhhhhhere
				List<Transaction> transactionList=null;// = portfolio.getTransactions(currentDate);
				if (transactionList != null && transactionList.size() > 0) {
					int ti;
					Transaction tmp;
					for (ti = 0; ti < transactionList.size(); ++ti) {
						tmp = transactionList.get(ti);
						if (tmp.getOperation().equals("DEPOSIT")) {
							curPortfolioValue += tmp.getAmount();
						}
						if (tmp.getOperation().equals("WITHDRAW")) {
							curPortfolioValue -= tmp.getAmount();
						}
					}
				}
			}

			curBenchMarkValue = sdds.get(i).getAdjClose();
			curRiskFreeValue = riskFreeList.get(i).getAdjClose();

			portfolioAmounts.add(curPortfolioValue);

			riskFreeAmounts.add(curRiskFreeValue);

			/** *************************** */
			if (maxValueN < curPortfolioValue) {
				maxValueN = curPortfolioValue;
				preMaxIndexN = i;
			}
			/** *************************** */

			dateList.add(currentDate);

			if (i == 0) {
				prePortfolioValue = curPortfolioValue;
				preBenchMarkValue = curBenchMarkValue;
				preRiskFreeValue = curRiskFreeValue;
			}
			if (i > 0) {
				portfolioReturn = this.computeIntervalReturn(curPortfolioValue, prePortfolioValue);
				benchMarkReturn = this.computeIntervalReturn(curBenchMarkValue, preBenchMarkValue);
				riskFreeReturn = this.computeIntervalReturn(curRiskFreeValue, preRiskFreeValue);

				portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioValue, prePortfolioValue);
				riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeValue, preRiskFreeValue);

				portfolioReturns.add(portfolioReturn);
				benchmarkReturns.add(benchMarkReturn);
				riskFreeReturns.add(riskFreeReturn);
				prePortfolioValue = curPortfolioValue;
				preBenchMarkValue = curBenchMarkValue;
				preRiskFreeValue = curRiskFreeValue;

				sigmaXBN = sigmaXBN + portfolioReturn * benchMarkReturn;
				sigmaXN = sigmaXN + portfolioReturn;
				sigmaXXN = sigmaXXN + portfolioReturn * portfolioReturn;
				sigmaBN = sigmaBN + benchMarkReturn;
				sigmaBBN = sigmaBBN + benchMarkReturn * benchMarkReturn;
				sigmaRN = sigmaRN + riskFreeReturn;

				sigmaLogXXN = sigmaLogXXN + portfolioLogReturn * portfolioLogReturn;
				sigmaLogXN = sigmaLogXN + portfolioLogReturn;

				if (portfolioLogReturn < riskFreeLogReturn)
					sigmaSRN = sigmaSRN + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

				double tmpDrawDown = (maxValueN - curPortfolioValue) / maxValueN;
				if (maxDrawDownN < tmpDrawDown) {
					maxDrawDownN = tmpDrawDown;
					minIndexN = i;
					maxIndexN = preMaxIndexN;
				}

			}

			boolean isHoldingDate = LTIDate.equals(lastHoldingDate, currentDate);

			// if last date or holding date
			if (i == pdds.size() - 1 || isHoldingDate) {
				double firstAmount = portfolioAmounts.get(0);
				double size = (double) portfolioAmounts.size() - 1;
				double lastAmount = portfolioAmounts.get((int) size);

				double firstRiskFree = riskFreeAmounts.get(0);
				double lastRiskFree = riskFreeAmounts.get((int) size);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDownN, sigmaXBN, sigmaXN, sigmaXXN, sigmaBN, sigmaBBN, sigmaRN, sigmaSRN, sigmaLogXXN, sigmaLogXN, totalStartDate, currentDate, interval, isCashFlow, portfolio, totalOriginalAmount);
				pdd = setAllMPT(pdd, newPdd, 0);

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmptA = this.setAllBasicInfo(pmptA, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE, newPdd);
					pmptAFlag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}

			if (LTIDate.isLastNYSETradingDayOfYear(currentDate) || i == pdds.size() - 1 || LTIDate.isYearEnd(currentDate))
				caculateYearEnd = true;

			int days = LTIDate.calculateInterval(totalStartDate, currentDate);

			if (days >= TimePara.yearday) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 1);
				int tmpIndex = this.adjustIndex(dateList, index1 + 1, currentDate, 1);
				startDate = dateList.get(tmpIndex);

				oneYearStartDate = startDate;

				oneYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma1First == false) {
					sigma1First = true;
					sigmaXB1 = sigmaXBN;
					sigmaX1 = sigmaXN;
					sigmaXX1 = sigmaXXN;
					sigmaB1 = sigmaBN;
					sigmaBB1 = sigmaBBN;
					sigmaR1 = sigmaRN;
					sigmaSR1 = sigmaSRN;
					sigmaLogXX1 = sigmaLogXXN;
					sigmaLogX1 = sigmaLogXN;
					index1 = 0;

					maxValue1 = maxValueN;
					maxDrawDown1 = maxDrawDownN;
					minIndex1 = minIndexN;
					maxIndex1 = maxIndexN;
					preMaxIndex1 = preMaxIndexN;
				} else {
					sigmaXB1 = sigmaXB1 + portfolioReturn * benchMarkReturn;
					sigmaX1 = sigmaX1 + portfolioReturn;
					sigmaXX1 = sigmaXX1 + portfolioReturn * portfolioReturn;
					sigmaB1 = sigmaB1 + benchMarkReturn;
					sigmaBB1 = sigmaBB1 + benchMarkReturn * benchMarkReturn;
					sigmaR1 = sigmaR1 + riskFreeReturn;

					sigmaLogXX1 = sigmaLogXX1 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX1 = sigmaLogX1 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR1 = sigmaSR1 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue1 < curPortfolioValue) {
						maxValue1 = curPortfolioValue;
						preMaxIndex1 = i;
					}
					double tmpDrawDown = (maxValue1 - curPortfolioValue) / maxValue1;
					if (maxDrawDown1 < tmpDrawDown) {
						maxDrawDown1 = tmpDrawDown;
						minIndex1 = i;
						maxIndex1 = preMaxIndex1;
					}

					if (index1 <= tmpIndex - 1) {
						while (index1 <= tmpIndex - 1) {
							lastX1 = portfolioReturns.get(index1);
							lastB1 = benchmarkReturns.get(index1);
							lastR1 = riskFreeReturns.get(index1);

							lastLogX1 = Math.log(1 + lastX1);
							lastLogR1 = Math.log(1 + lastR1);

							sigmaXB1 = sigmaXB1 - lastX1 * lastB1;
							sigmaX1 = sigmaX1 - lastX1;
							sigmaXX1 = sigmaXX1 - lastX1 * lastX1;
							sigmaB1 = sigmaB1 - lastB1;
							sigmaBB1 = sigmaBB1 - lastB1 * lastB1;
							sigmaR1 = sigmaR1 - lastR1;

							sigmaLogXX1 = sigmaLogXX1 - lastLogX1 * lastLogX1;
							sigmaLogX1 = sigmaLogX1 - lastLogX1;

							if (lastLogX1 < lastLogR1)
								sigmaSR1 = sigmaSR1 - (lastLogX1 - lastLogR1) * (lastLogX1 - lastLogR1);

							if (!deleteFlag)
								deleteFlag = (index1 == maxIndex1);
							index1++;
						}

					}

					else if (index1 > tmpIndex) {
						while (index1 > tmpIndex) {
							lastX1 = portfolioReturns.get(index1);
							lastB1 = benchmarkReturns.get(index1);
							lastR1 = riskFreeReturns.get(index1);

							lastLogX1 = Math.log(1 + lastX1);
							lastLogR1 = Math.log(1 + lastR1);

							sigmaXB1 = sigmaXB1 + lastX1 * lastB1;
							sigmaX1 = sigmaX1 + lastX1;
							sigmaXX1 = sigmaXX1 + lastX1 * lastX1;
							sigmaB1 = sigmaB1 + lastB1;
							sigmaBB1 = sigmaBB1 + lastB1 * lastB1;
							sigmaR1 = sigmaR1 + lastR1;

							sigmaLogXX1 = sigmaLogXX1 + lastLogX1 * lastLogX1;
							sigmaLogX1 = sigmaLogX1 + lastLogX1;

							if (lastLogX1 < lastLogR1)
								sigmaSR1 = sigmaSR1 + (lastLogX1 - lastLogR1) * (lastLogX1 - lastLogR1);

							if (!deleteFlag)
								deleteFlag = (index1 == maxIndex1);
							if (!deleteFlag)
								deleteFlag = (index1 == minIndex1);
							index1--;

						}
					}

					index1 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index1, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex1 = (int) sb[0] + index1;
					minIndex1 = (int) sb[1] + index1;
					preMaxIndex1 = (int) sb[2] + index1;
					maxValue1 = sb[3];
					maxDrawDown1 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index1);
				double size = (double) (i - index1);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index1);
				double lastRiskFree = riskFreeAmounts.get(i);
				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown1, sigmaXB1, sigmaX1, sigmaXX1, sigmaB1, sigmaBB1, sigmaR1, sigmaSR1, sigmaLogXX1, sigmaLogX1, startDate, currentDate, TimePara.workingday, isCashFlow, portfolio, oneYearAmount);

				pdd = setAllMPT(pdd, newPdd, 1);

				CopyUtil.Translate(newPdd, oneYearPdd);

				if (pdd.getSharpeRatio1() != null && pdd.getSharpeRatio1() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 1 + " "+
					// pdd.getSharpeRatio1() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt1 = this.setAllBasicInfo(pmpt1, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_ONE_YEAR, newPdd);
					pmpt1Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			if (days >= TimePara.yearday * 3) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 3);
				int tmpIndex = this.adjustIndex(dateList, index3 + 1, currentDate, 3);
				startDate = dateList.get(tmpIndex);

				threeYearStartDate = startDate;

				threeYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma3First == false) {
					sigma3First = true;
					sigmaXB3 = sigmaXBN;
					sigmaX3 = sigmaXN;
					sigmaXX3 = sigmaXXN;
					sigmaB3 = sigmaBN;
					sigmaBB3 = sigmaBBN;
					sigmaR3 = sigmaRN;
					sigmaSR3 = sigmaSRN;
					sigmaLogXX3 = sigmaLogXXN;
					sigmaLogX3 = sigmaLogXN;

					index3 = 0;

					maxValue3 = maxValueN;
					maxDrawDown3 = maxDrawDownN;
					minIndex3 = minIndexN;
					maxIndex3 = maxIndexN;
					preMaxIndex3 = preMaxIndexN;
				} else {
					sigmaXB3 = sigmaXB3 + portfolioReturn * benchMarkReturn;
					sigmaX3 = sigmaX3 + portfolioReturn;
					sigmaXX3 = sigmaXX3 + portfolioReturn * portfolioReturn;
					sigmaB3 = sigmaB3 + benchMarkReturn;
					sigmaBB3 = sigmaBB3 + benchMarkReturn * benchMarkReturn;
					sigmaR3 = sigmaR3 + riskFreeReturn;

					sigmaLogXX3 = sigmaLogXX3 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX3 = sigmaLogX3 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR3 = sigmaSR3 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue3 < curPortfolioValue) {
						maxValue3 = curPortfolioValue;
						preMaxIndex3 = i;
					}
					double tmpDrawDown = (maxValue3 - curPortfolioValue) / maxValue3;
					if (maxDrawDown3 < tmpDrawDown) {
						maxDrawDown3 = tmpDrawDown;
						minIndex3 = i;
						maxIndex3 = preMaxIndex3;
					}

					if (index3 <= tmpIndex - 1) {
						while (index3 <= tmpIndex - 1) {
							lastX3 = portfolioReturns.get(index3);
							lastB3 = benchmarkReturns.get(index3);
							lastR3 = riskFreeReturns.get(index3);

							lastLogX3 = Math.log(1 + lastX3);
							lastLogR3 = Math.log(1 + lastR3);

							sigmaXB3 = sigmaXB3 - lastX3 * lastB3;
							sigmaX3 = sigmaX3 - lastX3;
							sigmaXX3 = sigmaXX3 - lastX3 * lastX3;
							sigmaB3 = sigmaB3 - lastB3;
							sigmaBB3 = sigmaBB3 - lastB3 * lastB3;
							sigmaR3 = sigmaR3 - lastR3;

							sigmaLogXX3 = sigmaLogXX3 - lastLogX3 * lastLogX3;
							sigmaLogX3 = sigmaLogX3 - lastLogX3;

							if (lastLogX3 < lastLogR3)
								sigmaSR3 = sigmaSR3 - (lastLogX3 - lastLogR3) * (lastLogX3 - lastLogR3);

							if (!deleteFlag)
								deleteFlag = (index3 == maxIndex3);
							index3++;
						}
					} else if (index3 > tmpIndex) {
						while (index3 > tmpIndex) {
							lastX3 = portfolioReturns.get(index3);
							lastB3 = benchmarkReturns.get(index3);
							lastR3 = riskFreeReturns.get(index3);

							lastLogX3 = Math.log(1 + lastX3);
							lastLogR3 = Math.log(1 + lastR3);

							sigmaXB3 = sigmaXB3 + lastX3 * lastB3;
							sigmaX3 = sigmaX3 + lastX3;
							sigmaXX3 = sigmaXX3 + lastX3 * lastX3;
							sigmaB3 = sigmaB3 + lastB3;
							sigmaBB3 = sigmaBB3 + lastB3 * lastB3;
							sigmaR3 = sigmaR3 + lastR3;

							sigmaLogXX3 = sigmaLogXX3 + lastLogX3 * lastLogX3;
							sigmaLogX3 = sigmaLogX3 + lastLogX3;

							if (lastLogX3 < lastLogR3)
								sigmaSR3 = sigmaSR3 + (lastLogX3 - lastLogR3) * (lastLogX3 - lastLogR3);

							if (!deleteFlag)
								deleteFlag = (index3 == maxIndex3);
							if (!deleteFlag)
								deleteFlag = (index3 == minIndex3);

							index3--;
						}
					}

					index3 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index3, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex3 = (int) sb[0] + index3;
					minIndex3 = (int) sb[1] + index3;
					preMaxIndex3 = (int) sb[2] + index3;
					maxValue3 = sb[3];
					maxDrawDown3 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index3);
				double size = (double) (i - index3);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index3);
				double lastRiskFree = riskFreeAmounts.get(i);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown3, sigmaXB3, sigmaX3, sigmaXX3, sigmaB3, sigmaBB3, sigmaR3, sigmaSR3, sigmaLogXX3, sigmaLogX3, startDate, currentDate, TimePara.workingday * 3, isCashFlow, portfolio, threeYearAmount);

				pdd = setAllMPT(pdd, newPdd, 3);

				if (pdd.getSharpeRatio3() != null && pdd.getSharpeRatio3() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 3 + " "+
					// pdd.getSharpeRatio3() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt3 = this.setAllBasicInfo(pmpt3, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_THREE_YEAR, newPdd);
					pmpt3Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			if (days >= TimePara.yearday * 5) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 5);
				int tmpIndex = this.adjustIndex(dateList, index5 + 1, currentDate, 5);
				startDate = dateList.get(tmpIndex);

				fiveYearStartDate = startDate;

				fiveYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma5First == false) {
					sigma5First = true;
					sigmaXB5 = sigmaXBN;
					sigmaX5 = sigmaXN;
					sigmaXX5 = sigmaXXN;
					sigmaB5 = sigmaBN;
					sigmaBB5 = sigmaBBN;
					sigmaR5 = sigmaRN;
					sigmaSR5 = sigmaSRN;
					sigmaLogXX5 = sigmaLogXXN;
					sigmaLogX5 = sigmaLogXN;

					index5 = 0;

					maxValue5 = maxValueN;
					maxDrawDown5 = maxDrawDownN;
					minIndex5 = minIndexN;
					maxIndex5 = maxIndexN;
					preMaxIndex5 = preMaxIndexN;

				} else {
					sigmaXB5 = sigmaXB5 + portfolioReturn * benchMarkReturn;
					sigmaX5 = sigmaX5 + portfolioReturn;
					sigmaXX5 = sigmaXX5 + portfolioReturn * portfolioReturn;
					sigmaB5 = sigmaB5 + benchMarkReturn;
					sigmaBB5 = sigmaBB5 + benchMarkReturn * benchMarkReturn;
					sigmaR5 = sigmaR5 + riskFreeReturn;

					sigmaLogXX5 = sigmaLogXX5 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX5 = sigmaLogX5 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR5 = sigmaSR5 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue5 < curPortfolioValue) {
						maxValue5 = curPortfolioValue;
						preMaxIndex5 = i;
					}
					double tmpDrawDown = (maxValue5 - curPortfolioValue) / maxValue5;
					if (maxDrawDown5 < tmpDrawDown) {
						maxDrawDown5 = tmpDrawDown;
						minIndex5 = i;
						maxIndex5 = preMaxIndex5;
					}

					if (index5 <= tmpIndex - 1) {
						while (index5 <= tmpIndex - 1) {
							lastX5 = portfolioReturns.get(index5);
							lastB5 = benchmarkReturns.get(index5);
							lastR5 = riskFreeReturns.get(index5);

							lastLogX5 = Math.log(1 + lastX5);
							lastLogR5 = Math.log(1 + lastR5);

							sigmaXB5 = sigmaXB5 - lastX5 * lastB5;
							sigmaX5 = sigmaX5 - lastX5;
							sigmaXX5 = sigmaXX5 - lastX5 * lastX5;
							sigmaB5 = sigmaB5 - lastB5;
							sigmaBB5 = sigmaBB5 - lastB5 * lastB5;
							sigmaR5 = sigmaR5 - lastR5;

							sigmaLogXX5 = sigmaLogXX5 - lastLogX5 * lastLogX5;
							sigmaLogX5 = sigmaLogX5 - lastLogX5;

							if (lastLogX5 < lastLogR5)
								sigmaSR5 = sigmaSR5 - (lastLogX5 - lastLogR5) * (lastLogX5 - lastLogR5);

							if (!deleteFlag)
								deleteFlag = (index5 == maxIndex5);
							index5++;
						}
					} else if (index5 > tmpIndex) {
						while (index5 > tmpIndex) {
							lastX5 = portfolioReturns.get(index5);
							lastB5 = benchmarkReturns.get(index5);
							lastR5 = riskFreeReturns.get(index5);

							lastLogX5 = Math.log(1 + lastX5);
							lastLogR5 = Math.log(1 + lastR5);

							sigmaXB5 = sigmaXB5 + lastX5 * lastB5;
							sigmaX5 = sigmaX5 + lastX5;
							sigmaXX5 = sigmaXX5 + lastX5 * lastX5;
							sigmaB5 = sigmaB5 + lastB5;
							sigmaBB5 = sigmaBB5 + lastB5 * lastB5;
							sigmaR5 = sigmaR5 + lastR5;

							sigmaLogXX5 = sigmaLogXX5 + lastLogX5 * lastLogX5;
							sigmaLogX5 = sigmaLogX5 + lastLogX5;

							if (lastLogX5 < lastLogR5)
								sigmaSR5 = sigmaSR5 + (lastLogX5 - lastLogR5) * (lastLogX5 - lastLogR5);

							if (!deleteFlag)
								deleteFlag = (index5 == maxIndex5);
							if (!deleteFlag)
								deleteFlag = (index5 == minIndex5);
							index5--;
						}
					}

					index5 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index5, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex5 = (int) sb[0] + index5;
					minIndex5 = (int) sb[1] + index5;
					preMaxIndex5 = (int) sb[2] + index5;
					maxValue5 = sb[3];
					maxDrawDown5 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index5);
				double size = (double) (i - index5);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index5);
				double lastRiskFree = riskFreeAmounts.get(i);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown5, sigmaXB5, sigmaX5, sigmaXX5, sigmaB5, sigmaBB5, sigmaR5, sigmaSR5, sigmaLogXX5, sigmaLogX5, startDate, currentDate, TimePara.workingday * 5, isCashFlow, portfolio, fiveYearAmount);

				pdd = setAllMPT(pdd, newPdd, 5);

				if (pdd.getSharpeRatio5() != null && pdd.getSharpeRatio5() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 5 + " "+
					// pdd.getSharpeRatio5() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt5 = this.setAllBasicInfo(pmpt5, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_FIVE_YEAR, newPdd);
					pmpt5Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			newpdds.add(pdd);

			if (caculateYearEnd) {

				PortfolioDailyData newPdd;
				int index = 0;

				if (firstYearEnd) {
					double firstAmount = portfolioAmounts.get(0);
					double size = (double) (portfolioAmounts.size() - 1);
					double lastAmount = portfolioAmounts.get((int) size);

					double firstRiskFree = riskFreeAmounts.get(0);
					double lastRiskFree = riskFreeAmounts.get(i);

					newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDownN, sigmaXBN, sigmaXN, sigmaXXN, sigmaBN, sigmaBBN, sigmaRN, sigmaSRN, sigmaLogXXN, sigmaLogXN, totalStartDate, currentDate, -100, isCashFlow, portfolio, firstAmount);

					firstYearEnd = false;
				} else {
					double XB = sigmaXB1, X = sigmaX1, XX = sigmaXX1, BB = sigmaBB1, B = sigmaB1, R = sigmaR1, lastX, lastB, lastR, lastLogX, lastLogR, logXX = sigmaLogXX1, logX = sigmaLogX1;
					double SR = sigmaSR1;
					index = getYearEndIndex(dateList, currentDate);
					if (index != index1) {
						int tmpLastIndex = index1;

						while (tmpLastIndex < index) {
							lastX = portfolioReturns.get(tmpLastIndex);
							lastB = benchmarkReturns.get(tmpLastIndex);
							lastR = riskFreeReturns.get(tmpLastIndex);

							lastLogX = Math.log(1 + lastX);
							lastLogR = Math.log(1 + lastR);

							XB = XB - lastX * lastB;
							X = X - lastX;
							XX = XX - lastX * lastX;
							B = B - lastB;
							BB = BB - lastB * lastB;
							R = R - lastR;

							logXX = logXX - lastLogX * lastLogX;
							logX = logX - lastLogX;

							if (lastLogX < lastLogR)
								SR = SR - (lastLogX - lastLogR) * (lastLogX - lastLogR);

							tmpLastIndex++;
						}
					}
					List<Double> lastYearAmounts = portfolioAmounts.subList(index, i + 1);
					startDate = dateList.get(index);
					double drawDown = this.computeDrawDown(lastYearAmounts);
					double firstAmount = lastYearAmounts.get(0);
					double size = (double) lastYearAmounts.size() - 1;
					double lastAmount = lastYearAmounts.get((int) size);

					double firstRiskFree = riskFreeAmounts.get(index);
					double lastRiskFree = riskFreeAmounts.get(i);

					newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, drawDown, XB, X, XX, B, BB, R, SR, logXX, logX, startDate, currentDate, -100, isCashFlow, portfolio, firstAmount);

				}

				PortfolioMPT pmpt;

				int year = currentDate.getYear() + 1900;

				pmpt = portfolioManager.getPortfolioMPT(portfolioID, year);

				if (pmpt == null)
					pmpt = new PortfolioMPT();
				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, strategyID, userID, isModel, classID, year, newPdd);

				if (pmpt.getSharpeRatio() != null && pmpt.getSharpeRatio() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + year +
					// " "+ pdd.getSharpeRatio() + " happen at monitor");
				}

				pmpts.add(pmpt);
			}
		}

		// from start date to end date
		if (newpdds != null) {
			PortfolioDailyData pdd;
			pdd = newpdds.get(newpdds.size() - 1);
			double lastAmount = pdd.getAmount();
			if (pdd != null) {

				PortfolioMPT pmpt;
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, 0);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.FROM_STARTDATE_TO_ENDDATE, pdd);

				pmpts.add(pmpt);

				// 1 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -1);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_ONE_YEAR, pdd);

				if (isCashFlow && pdd.getAR1() != null) {
					Double ar = this.computeIRR(portfolio, oneYearStartDate, pdd.getDate(), oneYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR1(ar);
					}
				}
				pmpts.add(pmpt);

				// 3 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -3);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_THREE_YEAR, pdd);
				if (isCashFlow && pdd.getAR3() != null) {
					Double ar = this.computeIRR(portfolio, threeYearStartDate, pdd.getDate(), threeYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR3(ar);
					}
				}
				pmpts.add(pmpt);

				// 5 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -5);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_FIVE_YEAR, pdd);
				if (isCashFlow && pdd.getAR5() != null) {
					Double ar = this.computeIRR(portfolio, fiveYearStartDate, pdd.getDate(), fiveYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR5(ar);
					}
				}
				pmpts.add(pmpt);

				newpdds.remove(newpdds.size() - 1);
				newpdds.add(pdd);
			}

		}

		/** ******************************************************************** */
		if (pmpt1Flag)
			pmpts.add(pmpt1);
		if (pmpt3Flag)
			pmpts.add(pmpt3);
		if (pmpt5Flag)
			pmpts.add(pmpt5);
		if (pmptAFlag)
			pmpts.add(pmptA);
		/** ******************************************************************** */

		portfolioManager.saveOrUpdateAll(pmpts);

		return newpdds;
	}

	/**
	 * *************************************************************************
	 * *******************************
	 */
	private double getBenchmarkAmount(List<SecurityDailyData> sdds, Date date) {
		double preAmount = 0.0;
		for (int i = 0; i < sdds.size(); i++) {
			SecurityDailyData sdd = sdds.get(i);

			Date curDate = sdd.getDate();

			// if (curDate.equals(date)) {
			if (LTIDate.equals(curDate, date)) {
				if (sdd.getAdjClose() != null)
					return sdd.getAdjClose();
				else
					return preAmount;
			} else if (curDate.before(date)) {
				try {
					preAmount = sdd.getAdjClose();
				} catch (Exception e) {
				}
			}

			if (curDate.after(date)) {
				break;
			}

		}

		return preAmount;
	}

	@Deprecated
	public List<PortfolioDailyData> updatePortfolioDailyData(final Portfolio portfolio, List<PortfolioDailyData> pList, long benchMarkID) throws NoPriceException {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		StrategyManager strategyManager = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Long portfolioID = portfolio.getID();
		List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolioID);
		int startIndex = pdds.size();
		if (pList != null && pList.size() > 0)
			pdds.addAll(pList);

		List<Double> portfolioAmounts = new ArrayList<Double>();
		List<Double> riskFreeAmounts = new ArrayList<Double>();
		List<Double> riskFreeReturns = new ArrayList<Double>();

		List<Double> portfolioReturns = new ArrayList<Double>();
		List<Double> benchmarkReturns = new ArrayList<Double>();

		List<Double> yearAmounts = new ArrayList<Double>();

		Date totalStartDate, oneYearStartDate = new Date(), threeYearStartDate = new Date(), fiveYearStartDate = new Date();

		boolean caculateYearEnd = false;

		boolean isCashFlow = false;

		/** ************************************************************* */
		double sigmaXB1 = 0, sigmaXB3 = 0, sigmaXB5 = 0, sigmaXBN = 0;
		double sigmaX1 = 0, sigmaX3 = 0, sigmaX5 = 0, sigmaXN = 0;
		double sigmaXX1 = 0, sigmaXX3 = 0, sigmaXX5 = 0, sigmaXXN = 0;
		double sigmaB1 = 0, sigmaB3 = 0, sigmaB5 = 0, sigmaBN = 0;
		double sigmaBB1 = 0, sigmaBB3 = 0, sigmaBB5 = 0, sigmaBBN = 0;

		double sigmaR1 = 0, sigmaR3 = 0, sigmaR5 = 0, sigmaRN = 0;
		double lastR1 = 0, lastR3 = 0, lastR5 = 0, lastRN = 0;

		double sigmaSR1 = 0, sigmaSR3 = 0, sigmaSR5 = 0, sigmaSRN = 0;

		double lastX1 = 0, lastX3 = 0, lastX5 = 0, lastXN = 0;
		double lastB1 = 0, lastB3 = 0, lastB5 = 0, lastBN = 0;

		double lastLogR1 = 0, lastLogR3 = 0, lastLogR5 = 0, lastLogRN = 0;
		double lastLogX1 = 0, lastLogX3 = 0, lastLogX5 = 0, lastLogXN = 0;

		double sigmaLogXX1 = 0, sigmaLogXX3 = 0, sigmaLogXX5 = 0, sigmaLogXXN = 0;
		double sigmaLogX1 = 0, sigmaLogX3 = 0, sigmaLogX5 = 0, sigmaLogXN = 0;

		boolean sigma1First = false, sigma3First = false, sigma5First = false;

		int index1 = 0, index3 = 0, index5 = 0;

		boolean firstYearEnd = true;

		double maxValue1 = 0, maxValue3 = 0, maxValue5 = 0, maxValueN = 0;
		int maxIndex1 = 0, maxIndex3 = 0, maxIndex5 = 0, maxIndexN = 0;
		int preMaxIndex1 = 0, preMaxIndex3 = 0, preMaxIndex5 = 0, preMaxIndexN = 0;
		int minIndex1 = 0, minIndex3 = 0, minIndex5 = 0, minIndexN = 0;
		double maxDrawDown1 = 0, maxDrawDown3 = 0, maxDrawDown5 = 0, maxDrawDownN = 0;

		/** ************************************************************* */

		/** ************************************************************* */
		Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pList.get(pList.size() - 1).getDate());
		PortfolioMPT pmpt1;
		pmpt1 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_ONE_YEAR);
		if (pmpt1 == null)
			pmpt1 = new PortfolioMPT();
		PortfolioMPT pmpt3;
		pmpt3 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_THREE_YEAR);
		if (pmpt3 == null)
			pmpt3 = new PortfolioMPT();
		PortfolioMPT pmpt5;
		pmpt5 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_FIVE_YEAR);
		if (pmpt5 == null)
			pmpt5 = new PortfolioMPT();
		PortfolioMPT pmptA;
		pmptA = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE);
		if (pmptA == null)
			pmptA = new PortfolioMPT();
		boolean pmpt1Flag = false, pmpt3Flag = false, pmpt5Flag = false, pmptAFlag = false;
		/** ************************************************************* */

		// Long portfolioID = pdds.get(0).getPortfolioID();
		// Long benchMarkID = sdds.get(0).getSecurityID();
		// Portfolio thisOne = portfolioManager.get(portfolioID);
		Long strategyID = portfolio.getMainStrategyID();
		Long classID = null;
		Strategy strategy = null;
		if (portfolio.getMainStrategyID() != null) {
			strategy = strategyManager.get(portfolio.getMainStrategyID());
			if (strategy != null) {
				classID = strategy.getStrategyClassID();
				if (strategy.getStrategyClassID() == 5) {
					isCashFlow = true;
				}
			}
		}

		Long userID = portfolio.getUserID();
		String name = portfolio.getName();
		Boolean isModel = portfolio.getMainStrategyID() == null ? false : true;

		double prePortfolioValue = 0;
		double curPortfolioValue;
		double preBenchMarkValue = 0;
		double curBenchMarkValue;

		double totalOriginalAmount = 0, oneYearAmount = 0, threeYearAmount = 0, fiveYearAmount = 0;

		List<PortfolioMPT> pmpts = new ArrayList<PortfolioMPT>();

		List<Date> dateList = new ArrayList<Date>();

		Date startDate = pdds.get(0).getDate();
		Date endDate = pdds.get(pdds.size() - 1).getDate();

		totalStartDate = startDate;

		totalOriginalAmount = pdds.get(0).getAmount();

		List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();

		PortfolioDailyData oneYearPdd = new PortfolioDailyData();

		double portfolioReturn = 0;
		double benchMarkReturn = 0;
		double riskFreeReturn = 0;

		double portfolioLogReturn = 0;
		double riskFreeLogReturn = 0;

		/**
		 * *********************************************************************
		 * *******************
		 */
		List<SecurityDailyData> sdds = securityManager.getDailyDatas(benchMarkID, LTIDate.getNewWeekDay(startDate, -5), LTIDate.getNewWeekDay(endDate, 5));
		if (sdds == null || sdds.size() == 0)
			return null;

		sdds = this.adjustBenchMarkList(pdds, sdds);
		Security se = getCash();
		List<SecurityDailyData> riskFreeList = securityManager.getDailyDatas(se.getID(), startDate, pdds.get(pdds.size() - 1).getDate());
		riskFreeList = this.adjustBenchMarkList(pdds, riskFreeList);
		double curRiskFreeValue = 0;
		double preRiskFreeValue = 0;
		/**
		 * *********************************************************************
		 * *******************
		 */

		for (int i = 0; i < pdds.size(); i++) {

			Date currentDate = pdds.get(i).getDate();

			caculateYearEnd = false;

			int interval = LTIDate.calculateIntervalIgnoreHolidDay(totalStartDate, currentDate);
			PortfolioDailyData pdd = pdds.get(i);

			curPortfolioValue = pdd.getAmount();
			/**
			 * if isCashFlow is true, we shoule modify the curPortfolioValue by
			 * take the transaction amount into consideration*
			 */
			if (isCashFlow) {
				//TODO:here
				List<Transaction> transactionList=null;// = portfolio.getTransactions(currentDate);
				if (transactionList != null && transactionList.size() > 0) {
					int ti;
					Transaction tmp;
					for (ti = 0; ti < transactionList.size(); ++ti) {
						tmp = transactionList.get(ti);
						if (tmp.getOperation().equals("DEPOSIT")) {
							curPortfolioValue += tmp.getAmount();
						}
						if (tmp.getOperation().equals("WITHDRAW")) {
							curPortfolioValue -= tmp.getAmount();
						}
					}
				}
			}
			// curBenchMarkValue = getBenchmarkAmount(sdds, currentDate);
			curBenchMarkValue = sdds.get(i).getAdjClose();
			curRiskFreeValue = riskFreeList.get(i).getAdjClose();

			portfolioAmounts.add(curPortfolioValue);

			riskFreeAmounts.add(curRiskFreeValue);

			/** *************************** */
			if (maxValueN < curPortfolioValue) {
				maxValueN = curPortfolioValue;
				preMaxIndexN = i;
			}
			/** *************************** */

			dateList.add(currentDate);

			if (i == 0) {
				prePortfolioValue = curPortfolioValue;
				preBenchMarkValue = curBenchMarkValue;
				preRiskFreeValue = curRiskFreeValue;
			}
			if (i > 0) {
				portfolioReturn = this.computeIntervalReturn(curPortfolioValue, prePortfolioValue);
				benchMarkReturn = this.computeIntervalReturn(curBenchMarkValue, preBenchMarkValue);
				riskFreeReturn = this.computeIntervalReturn(curRiskFreeValue, preRiskFreeValue);

				portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioValue, prePortfolioValue);
				riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeValue, preRiskFreeValue);

				portfolioReturns.add(portfolioReturn);
				benchmarkReturns.add(benchMarkReturn);
				riskFreeReturns.add(riskFreeReturn);
				prePortfolioValue = curPortfolioValue;
				preBenchMarkValue = curBenchMarkValue;
				preRiskFreeValue = curRiskFreeValue;

				sigmaXBN = sigmaXBN + portfolioReturn * benchMarkReturn;
				sigmaXN = sigmaXN + portfolioReturn;
				sigmaXXN = sigmaXXN + portfolioReturn * portfolioReturn;
				sigmaBN = sigmaBN + benchMarkReturn;
				sigmaBBN = sigmaBBN + benchMarkReturn * benchMarkReturn;
				sigmaRN = sigmaRN + riskFreeReturn;

				sigmaLogXXN = sigmaLogXXN + portfolioLogReturn * portfolioLogReturn;
				sigmaLogXN = sigmaLogXN + portfolioLogReturn;

				if (portfolioLogReturn < riskFreeLogReturn)
					sigmaSRN = sigmaSRN + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

				double tmpDrawDown = (maxValueN - curPortfolioValue) / maxValueN;
				if (maxDrawDownN < tmpDrawDown) {
					maxDrawDownN = tmpDrawDown;
					minIndexN = i;
					maxIndexN = preMaxIndexN;
				}

			}

			if (i < startIndex)
				continue;

			boolean isHoldingDate = LTIDate.equals(lastHoldingDate, currentDate);

			// if last date or holding date
			if (i == pdds.size() - 1 || isHoldingDate) {
				double firstAmount = portfolioAmounts.get(0);
				double size = (double) portfolioAmounts.size() - 1;
				double lastAmount = portfolioAmounts.get((int) size);

				double firstRiskFree = riskFreeAmounts.get(0);
				double lastRiskFree = riskFreeAmounts.get((int) size);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDownN, sigmaXBN, sigmaXN, sigmaXXN, sigmaBN, sigmaBBN, sigmaRN, sigmaSRN, sigmaLogXXN, sigmaLogXN, totalStartDate, currentDate, interval, isCashFlow, portfolio, totalOriginalAmount);

				pdd = setAllMPT(pdd, newPdd, 0);

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmptA = this.setAllBasicInfo(pmptA, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE, newPdd);
					pmptAFlag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */

			}

			if (LTIDate.isLastNYSETradingDayOfYear(currentDate) || i == pdds.size() - 1 || LTIDate.isYearEnd(currentDate))
				caculateYearEnd = true;

			int days = LTIDate.calculateInterval(totalStartDate, currentDate);

			if (days >= TimePara.yearday) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 1);
				int tmpIndex = this.adjustIndex(dateList, index1 + 1, currentDate, 1);
				startDate = dateList.get(tmpIndex);

				oneYearStartDate = startDate;

				oneYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma1First == false) {
					sigma1First = true;
					sigmaXB1 = sigmaXBN;
					sigmaX1 = sigmaXN;
					sigmaXX1 = sigmaXXN;
					sigmaB1 = sigmaBN;
					sigmaBB1 = sigmaBBN;
					sigmaR1 = sigmaRN;
					sigmaSR1 = sigmaSRN;
					sigmaLogXX1 = sigmaLogXXN;
					sigmaLogX1 = sigmaLogXN;
					index1 = 0;

					maxValue1 = maxValueN;
					maxDrawDown1 = maxDrawDownN;
					minIndex1 = minIndexN;
					maxIndex1 = maxIndexN;
					preMaxIndex1 = preMaxIndexN;
				}
				// else
				{
					sigmaXB1 = sigmaXB1 + portfolioReturn * benchMarkReturn;
					sigmaX1 = sigmaX1 + portfolioReturn;
					sigmaXX1 = sigmaXX1 + portfolioReturn * portfolioReturn;
					sigmaB1 = sigmaB1 + benchMarkReturn;
					sigmaBB1 = sigmaBB1 + benchMarkReturn * benchMarkReturn;
					sigmaR1 = sigmaR1 + riskFreeReturn;

					sigmaLogXX1 = sigmaLogXX1 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX1 = sigmaLogX1 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR1 = sigmaSR1 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue1 < curPortfolioValue) {
						maxValue1 = curPortfolioValue;
						preMaxIndex1 = i;
					}
					double tmpDrawDown = (maxValue1 - curPortfolioValue) / maxValue1;
					if (maxDrawDown1 < tmpDrawDown) {
						maxDrawDown1 = tmpDrawDown;
						minIndex1 = i;
						maxIndex1 = preMaxIndex1;
					}

					if (index1 <= tmpIndex - 1) {
						while (index1 <= tmpIndex - 1) {
							lastX1 = portfolioReturns.get(index1);
							lastB1 = benchmarkReturns.get(index1);
							lastR1 = riskFreeReturns.get(index1);

							lastLogX1 = Math.log(1 + lastX1);
							lastLogR1 = Math.log(1 + lastR1);

							sigmaXB1 = sigmaXB1 - lastX1 * lastB1;
							sigmaX1 = sigmaX1 - lastX1;
							sigmaXX1 = sigmaXX1 - lastX1 * lastX1;
							sigmaB1 = sigmaB1 - lastB1;
							sigmaBB1 = sigmaBB1 - lastB1 * lastB1;
							sigmaR1 = sigmaR1 - lastR1;

							sigmaLogXX1 = sigmaLogXX1 - lastLogX1 * lastLogX1;
							sigmaLogX1 = sigmaLogX1 - lastLogX1;

							if (lastLogX1 < lastLogR1)
								sigmaSR1 = sigmaSR1 - (lastLogX1 - lastLogR1) * (lastLogX1 - lastLogR1);

							if (!deleteFlag)
								deleteFlag = (index1 == maxIndex1);
							index1++;
						}

					}

					else if (index1 > tmpIndex) {
						while (index1 > tmpIndex) {
							lastX1 = portfolioReturns.get(index1);
							lastB1 = benchmarkReturns.get(index1);
							lastR1 = riskFreeReturns.get(index1);

							lastLogX1 = Math.log(1 + lastX1);
							lastLogR1 = Math.log(1 + lastR1);

							sigmaXB1 = sigmaXB1 + lastX1 * lastB1;
							sigmaX1 = sigmaX1 + lastX1;
							sigmaXX1 = sigmaXX1 + lastX1 * lastX1;
							sigmaB1 = sigmaB1 + lastB1;
							sigmaBB1 = sigmaBB1 + lastB1 * lastB1;
							sigmaR1 = sigmaR1 + lastR1;

							sigmaLogXX1 = sigmaLogXX1 + lastLogX1 * lastLogX1;
							sigmaLogX1 = sigmaLogX1 + lastLogX1;

							if (lastLogX1 < lastLogR1)
								sigmaSR1 = sigmaSR1 + (lastLogX1 - lastLogR1) * (lastLogX1 - lastLogR1);

							if (!deleteFlag)
								deleteFlag = (index1 == maxIndex1);
							if (!deleteFlag)
								deleteFlag = (index1 == minIndex1);
							index1--;

						}
					}

					index1 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index1, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex1 = (int) sb[0] + index1;
					minIndex1 = (int) sb[1] + index1;
					preMaxIndex1 = (int) sb[2] + index1;
					maxValue1 = sb[3];
					maxDrawDown1 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index1);
				double size = (double) (i - index1);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index1);
				double lastRiskFree = riskFreeAmounts.get(i);
				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown1, sigmaXB1, sigmaX1, sigmaXX1, sigmaB1, sigmaBB1, sigmaR1, sigmaSR1, sigmaLogXX1, sigmaLogX1, startDate, currentDate, TimePara.workingday, isCashFlow, portfolio, oneYearAmount);

				pdd = setAllMPT(pdd, newPdd, 1);

				CopyUtil.Translate(newPdd, oneYearPdd);

				double sr1 = pdd.getSharpeRatio1() == null ? 0.0 : pdd.getSharpeRatio1();
				if (sr1 >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 1 + " "
					// + sr1 + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt1 = this.setAllBasicInfo(pmpt1, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_ONE_YEAR, newPdd);
					pmpt1Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */

			}
			if (days >= TimePara.yearday * 3) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 3);
				int tmpIndex = this.adjustIndex(dateList, index3 + 1, currentDate, 3);
				startDate = dateList.get(tmpIndex);

				threeYearStartDate = startDate;

				threeYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma3First == false) {
					sigma3First = true;
					sigmaXB3 = sigmaXBN;
					sigmaX3 = sigmaXN;
					sigmaXX3 = sigmaXXN;
					sigmaB3 = sigmaBN;
					sigmaBB3 = sigmaBBN;
					sigmaR3 = sigmaRN;
					sigmaSR3 = sigmaSRN;
					sigmaLogXX3 = sigmaLogXXN;
					sigmaLogX3 = sigmaLogXN;
					index3 = 0;

					maxValue3 = maxValueN;
					maxDrawDown3 = maxDrawDownN;
					minIndex3 = minIndexN;
					maxIndex3 = maxIndexN;
					preMaxIndex3 = preMaxIndexN;
				}
				// else
				{
					sigmaXB3 = sigmaXB3 + portfolioReturn * benchMarkReturn;
					sigmaX3 = sigmaX3 + portfolioReturn;
					sigmaXX3 = sigmaXX3 + portfolioReturn * portfolioReturn;
					sigmaB3 = sigmaB3 + benchMarkReturn;
					sigmaBB3 = sigmaBB3 + benchMarkReturn * benchMarkReturn;
					sigmaR3 = sigmaR3 + riskFreeReturn;

					sigmaLogXX3 = sigmaLogXX3 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX3 = sigmaLogX3 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR3 = sigmaSR3 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue3 < curPortfolioValue) {
						maxValue3 = curPortfolioValue;
						preMaxIndex3 = i;
					}
					double tmpDrawDown = (maxValue3 - curPortfolioValue) / maxValue3;
					if (maxDrawDown3 < tmpDrawDown) {
						maxDrawDown3 = tmpDrawDown;
						minIndex3 = i;
						maxIndex3 = preMaxIndex3;
					}

					if (index3 <= tmpIndex - 1) {
						while (index3 <= tmpIndex - 1) {
							lastX3 = portfolioReturns.get(index3);
							lastB3 = benchmarkReturns.get(index3);
							lastR3 = riskFreeReturns.get(index3);

							lastLogX3 = Math.log(1 + lastX3);
							lastLogR3 = Math.log(1 + lastR3);

							sigmaXB3 = sigmaXB3 - lastX3 * lastB3;
							sigmaX3 = sigmaX3 - lastX3;
							sigmaXX3 = sigmaXX3 - lastX3 * lastX3;
							sigmaB3 = sigmaB3 - lastB3;
							sigmaBB3 = sigmaBB3 - lastB3 * lastB3;
							sigmaR3 = sigmaR3 - lastR3;

							sigmaLogXX3 = sigmaLogXX3 - lastLogX3 * lastLogX3;
							sigmaLogX3 = sigmaLogX3 - lastLogX3;

							if (lastLogX3 < lastLogR3)
								sigmaSR3 = sigmaSR3 - (lastLogX3 - lastLogR3) * (lastLogX3 - lastLogR3);

							if (!deleteFlag)
								deleteFlag = (index3 == maxIndex3);
							index3++;
						}
					} else if (index3 > tmpIndex) {
						while (index3 > tmpIndex) {
							lastX3 = portfolioReturns.get(index3);
							lastB3 = benchmarkReturns.get(index3);
							lastR3 = riskFreeReturns.get(index3);

							lastLogX3 = Math.log(1 + lastX3);
							lastLogR3 = Math.log(1 + lastR3);

							sigmaXB3 = sigmaXB3 + lastX3 * lastB3;
							sigmaX3 = sigmaX3 + lastX3;
							sigmaXX3 = sigmaXX3 + lastX3 * lastX3;
							sigmaB3 = sigmaB3 + lastB3;
							sigmaBB3 = sigmaBB3 + lastB3 * lastB3;
							sigmaR3 = sigmaR3 + lastR3;

							sigmaLogXX3 = sigmaLogXX3 + lastLogX3 * lastLogX3;
							sigmaLogX3 = sigmaLogX3 + lastLogX3;

							if (lastLogX3 < lastLogR3)
								sigmaSR3 = sigmaSR3 + (lastLogX3 - lastLogR3) * (lastLogX3 - lastLogR3);

							if (!deleteFlag)
								deleteFlag = (index3 == maxIndex3);
							if (!deleteFlag)
								deleteFlag = (index3 == minIndex3);

							index3--;
						}
					}

					index3 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index3, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex3 = (int) sb[0] + index3;
					minIndex3 = (int) sb[1] + index3;
					preMaxIndex3 = (int) sb[2] + index3;
					maxValue3 = sb[3];
					maxDrawDown3 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index3);
				double size = (double) (i - index3);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index3);
				double lastRiskFree = riskFreeAmounts.get(i);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown3, sigmaXB3, sigmaX3, sigmaXX3, sigmaB3, sigmaBB3, sigmaR3, sigmaSR3, sigmaLogXX3, sigmaLogX3, startDate, currentDate, TimePara.workingday * 3, isCashFlow, portfolio, threeYearAmount);

				pdd = setAllMPT(pdd, newPdd, 3);

				if (pdd.getSharpeRatio3() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 3 + " "+
					// pdd.getSharpeRatio3() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt3 = this.setAllBasicInfo(pmpt3, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_THREE_YEAR, newPdd);
					pmpt3Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */

			}
			if (days >= TimePara.yearday * 5) {

				// int tmpIndex = getSubListIndex(dateList, currentDate, 5);
				int tmpIndex = this.adjustIndex(dateList, index5 + 1, currentDate, 5);
				startDate = dateList.get(tmpIndex);

				fiveYearStartDate = startDate;

				fiveYearAmount = portfolioAmounts.get(tmpIndex);

				boolean deleteFlag = false;

				if (sigma5First == false) {
					sigma5First = true;
					sigmaXB5 = sigmaXBN;
					sigmaX5 = sigmaXN;
					sigmaXX5 = sigmaXXN;
					sigmaB5 = sigmaBN;
					sigmaBB5 = sigmaBBN;
					sigmaR5 = sigmaRN;
					sigmaSR5 = sigmaSRN;
					sigmaLogXX5 = sigmaLogXXN;
					sigmaLogX5 = sigmaLogXN;
					index5 = 0;

					maxValue5 = maxValueN;
					maxDrawDown5 = maxDrawDownN;
					minIndex5 = minIndexN;
					maxIndex5 = maxIndexN;
					preMaxIndex5 = preMaxIndexN;

				}
				// else
				{
					sigmaXB5 = sigmaXB5 + portfolioReturn * benchMarkReturn;
					sigmaX5 = sigmaX5 + portfolioReturn;
					sigmaXX5 = sigmaXX5 + portfolioReturn * portfolioReturn;
					sigmaB5 = sigmaB5 + benchMarkReturn;
					sigmaBB5 = sigmaBB5 + benchMarkReturn * benchMarkReturn;
					sigmaR5 = sigmaR5 + riskFreeReturn;

					sigmaLogXX5 = sigmaLogXX5 + portfolioLogReturn * portfolioLogReturn;
					sigmaLogX5 = sigmaLogX5 + portfolioLogReturn;

					if (portfolioLogReturn < riskFreeLogReturn)
						sigmaSR5 = sigmaSR5 + (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);

					if (maxValue5 < curPortfolioValue) {
						maxValue5 = curPortfolioValue;
						preMaxIndex5 = i;
					}
					double tmpDrawDown = (maxValue5 - curPortfolioValue) / maxValue5;
					if (maxDrawDown5 < tmpDrawDown) {
						maxDrawDown5 = tmpDrawDown;
						minIndex5 = i;
						maxIndex5 = preMaxIndex5;
					}

					if (index5 <= tmpIndex - 1) {
						while (index5 <= tmpIndex - 1) {
							lastX5 = portfolioReturns.get(index5);
							lastB5 = benchmarkReturns.get(index5);
							lastR5 = riskFreeReturns.get(index5);

							lastLogX5 = Math.log(1 + lastX5);
							lastLogR5 = Math.log(1 + lastR5);

							sigmaXB5 = sigmaXB5 - lastX5 * lastB5;
							sigmaX5 = sigmaX5 - lastX5;
							sigmaXX5 = sigmaXX5 - lastX5 * lastX5;
							sigmaB5 = sigmaB5 - lastB5;
							sigmaBB5 = sigmaBB5 - lastB5 * lastB5;
							sigmaR5 = sigmaR5 - lastR5;

							sigmaLogXX5 = sigmaLogXX5 - lastLogX5 * lastLogX5;
							sigmaLogX5 = sigmaLogX5 - lastLogX5;

							if (lastLogX5 < lastLogR5)
								sigmaSR5 = sigmaSR5 - (lastLogX5 - lastLogR5) * (lastLogX5 - lastLogR5);

							if (!deleteFlag)
								deleteFlag = (index5 == maxIndex5);
							index5++;
						}
					} else if (index5 > tmpIndex) {
						while (index5 > tmpIndex) {
							lastX5 = portfolioReturns.get(index5);
							lastB5 = benchmarkReturns.get(index5);
							lastR5 = riskFreeReturns.get(index5);

							lastLogX5 = Math.log(1 + lastX5);
							lastLogR5 = Math.log(1 + lastR5);

							sigmaXB5 = sigmaXB5 + lastX5 * lastB5;
							sigmaX5 = sigmaX5 + lastX5;
							sigmaXX5 = sigmaXX5 + lastX5 * lastX5;
							sigmaB5 = sigmaB5 + lastB5;
							sigmaBB5 = sigmaBB5 + lastB5 * lastB5;
							sigmaR5 = sigmaR5 + lastR5;

							sigmaLogXX5 = sigmaLogXX5 + lastLogX5 * lastLogX5;
							sigmaLogX5 = sigmaLogX5 + lastLogX5;

							if (lastLogX5 < lastLogR5)
								sigmaSR5 = sigmaSR5 + (lastLogX5 - lastLogR5) * (lastLogX5 - lastLogR5);

							if (!deleteFlag)
								deleteFlag = (index5 == maxIndex5);
							if (!deleteFlag)
								deleteFlag = (index5 == minIndex5);
							index5--;
						}
					}

					index5 = tmpIndex;

				}

				if (deleteFlag) {
					List<Double> portfolioYearsAmounts = portfolioAmounts.subList(index5, i + 1);
					double sb[] = new double[5];
					sb = this.getMaxFromList(portfolioYearsAmounts);
					maxIndex5 = (int) sb[0] + index5;
					minIndex5 = (int) sb[1] + index5;
					preMaxIndex5 = (int) sb[2] + index5;
					maxValue5 = sb[3];
					maxDrawDown5 = sb[4];
				}

				double firstAmount = portfolioAmounts.get(index5);
				double size = (double) (i - index5);
				double lastAmount = portfolioAmounts.get(i);

				double firstRiskFree = riskFreeAmounts.get(index5);
				double lastRiskFree = riskFreeAmounts.get(i);

				PortfolioDailyData newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDown5, sigmaXB5, sigmaX5, sigmaXX5, sigmaB5, sigmaBB5, sigmaR5, sigmaSR5, sigmaLogXX5, sigmaLogX5, startDate, currentDate, TimePara.workingday * 5, isCashFlow, portfolio, fiveYearAmount);

				pdd = setAllMPT(pdd, newPdd, 5);

				if (pdd.getSharpeRatio5() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + 5 + " "+
					// pdd.getSharpeRatio5() + " happen at monitor");
				}

				/**
				 * *************************************************************
				 * ****
				 */
				if (isHoldingDate) {
					pmpt5 = this.setAllBasicInfo(pmpt5, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.DELAY_LAST_FIVE_YEAR, newPdd);
					pmpt5Flag = true;
				}
				/**
				 * *************************************************************
				 * ****
				 */
			}
			newpdds.add(pdd);

			int index = 0;
			if (caculateYearEnd) {

				PortfolioDailyData newPdd;

				if (totalStartDate.getYear() != currentDate.getYear())
					firstYearEnd = false;
				;

				if (firstYearEnd) {
					double firstAmount = portfolioAmounts.get(0);
					double size = (double) (portfolioAmounts.size() - 1);
					double lastAmount = portfolioAmounts.get((int) size);

					double firstRiskFree = riskFreeAmounts.get(0);
					double lastRiskFree = riskFreeAmounts.get(i);

					newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, maxDrawDownN, sigmaXBN, sigmaXN, sigmaXXN, sigmaBN, sigmaBBN, sigmaRN, sigmaSRN, sigmaLogXX5, sigmaLogX5, totalStartDate, currentDate, -100, isCashFlow, portfolio, firstAmount);

					firstYearEnd = false;
				} else {
					double XB = sigmaXB1, X = sigmaX1, XX = sigmaXX1, BB = sigmaBB1, B = sigmaB1, R = sigmaR1, lastX, lastB, lastR, lastLogX, lastLogR, logXX = sigmaLogXX1, logX = sigmaLogX1;
					double SR = sigmaSR1;
					index = getYearEndIndex(dateList, currentDate);
					if (index != index1) {
						int tmpLastIndex = index1;

						while (tmpLastIndex < index) {
							lastX = portfolioReturns.get(tmpLastIndex);
							lastB = benchmarkReturns.get(tmpLastIndex);
							lastR = riskFreeReturns.get(tmpLastIndex);

							lastLogX = Math.log(1 + lastX);
							lastLogR = Math.log(1 + lastR);

							XB = XB - lastX * lastB;
							X = X - lastX;
							XX = XX - lastX * lastX;
							B = B - lastB;
							BB = BB - lastB * lastB;
							R = R - lastR;

							logXX = logXX - lastLogX * lastLogX;
							logX = logX - lastLogX;

							if (lastLogX < lastLogR)
								SR = SR - (lastLogX - lastLogR) * (lastLogX - lastLogR);

							tmpLastIndex++;
						}
					}
					List<Double> lastYearAmounts = portfolioAmounts.subList(index, i + 1);
					startDate = dateList.get(index);
					double drawDown = this.computeDrawDown(lastYearAmounts);
					double firstAmount = lastYearAmounts.get(0);
					double size = (double) lastYearAmounts.size() - 1;
					double lastAmount = lastYearAmounts.get((int) size);

					double firstRiskFree = riskFreeAmounts.get(index);
					double lastRiskFree = riskFreeAmounts.get(i);

					newPdd = this.getOneYearMPT(firstAmount, lastAmount, size, firstRiskFree, lastRiskFree, drawDown, XB, X, XX, B, BB, R, SR, logXX, logX, startDate, currentDate, -100, isCashFlow, portfolio, firstAmount);
				}

				PortfolioMPT pmpt;

				int year = currentDate.getYear() + 1900;

				pmpt = portfolioManager.getPortfolioMPT(portfolioID, year);

				if (pmpt == null)
					pmpt = new PortfolioMPT();
				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, strategyID, userID, isModel, classID, year, newPdd);

				if (pmpt.getSharpeRatio() != null && pmpt.getSharpeRatio() >= 5) {
					// log.warn(currentDate + " " + portfolioID + " " + year +
					// " "+ pdd.getSharpeRatio() + " happen at monitor");
				}
				pmpts.add(pmpt);
			}
		}

		// from start date to end date
		if (newpdds != null) {
			PortfolioDailyData pdd;
			pdd = newpdds.get(newpdds.size() - 1);
			double lastAmount = pdd.getAmount();
			if (pdd != null) {

				PortfolioMPT pmpt;
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, 0);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.FROM_STARTDATE_TO_ENDDATE, pdd);

				pmpts.add(pmpt);

				// 1 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -1);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_ONE_YEAR, pdd);
				if (isCashFlow && pdd.getAR1() != null) {
					Double ar = this.computeIRR(portfolio, oneYearStartDate, pdd.getDate(), oneYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR1(ar);
					}
				}
				pmpts.add(pmpt);

				// 3 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -3);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_THREE_YEAR, pdd);
				if (isCashFlow && pdd.getAR3() != null) {
					Double ar = this.computeIRR(portfolio, threeYearStartDate, pdd.getDate(), threeYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR3(ar);
					}
				}
				pmpts.add(pmpt);

				// 5 year
				pmpt = portfolioManager.getPortfolioMPT(portfolioID, -5);

				if (pmpt == null)
					pmpt = new PortfolioMPT();

				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, PortfolioMPT.LAST_FIVE_YEAR, pdd);
				if (isCashFlow && pdd.getAR5() != null) {
					Double ar = this.computeIRR(portfolio, fiveYearStartDate, pdd.getDate(), fiveYearAmount, lastAmount);
					if (ar < LTINumber.INF) {
						pmpt.setAR(ar);
						pdd.setAR5(ar);
					}
				}
				pmpts.add(pmpt);

				newpdds.remove(newpdds.size() - 1);
				newpdds.add(pdd);
			}

		}
		/** ******************************************************************** */
		if (pmpt1Flag)
			pmpts.add(pmpt1);
		if (pmpt3Flag)
			pmpts.add(pmpt3);
		if (pmpt5Flag)
			pmpts.add(pmpt5);
		if (pmptAFlag)
			pmpts.add(pmptA);
		/** ******************************************************************** */

		portfolioManager.saveOrUpdateAll(pmpts);

		return newpdds;
	}

	@Deprecated
	public double computeIRR_OldVersion(final Portfolio portfolio, Date startDate, Date endDate, double originalAmount, double lastAmount) {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		//TODO:hereeeeeeeeeeeeeeeeeeeeeeeee
		List<Transaction> transactionList = null;// portfolio.getTransactions();// portfolioManager.getCashFlowTransaction(id,
		// startDate,
		// endDate);

		if (transactionList == null || transactionList.size() == 0) {
			int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);
			return Math.pow(lastAmount / originalAmount, TimePara.workingday / (interval * 1.0)) - 1;
		}

		List<Double> dlist = new ArrayList<Double>();

		List<Integer> ilist = new ArrayList<Integer>();

		int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		dlist.add(-originalAmount);

		ilist.add(interval);

		double currentTotal = 0;

		Date currentDate, preDate = null;

		boolean firstTime = true;

		for (int i = 0; i < transactionList.size(); i++) {
			Transaction tmp = transactionList.get(i);

			currentDate = tmp.getDate();

			if (currentDate.after(endDate) || currentDate.before(startDate))
				continue;

			double amount = tmp.getAmount();

			if (tmp.getOperation().equals("DEPOSIT")) {
				currentTotal -= amount;
			}

			else if (tmp.getOperation().equals("WITHDRAW")) {
				currentTotal += amount;
			}

			/*
			 * if(i%30==0 || i==transactionList.size()-1) {
			 * dlist.add(currentTotal); currentDate = tmp.getDate(); interval =
			 * LTIDate.calculateIntervalIgnoreHolidDay(currentDate, endDate);
			 * ilist.add(interval); currentTotal=0; }
			 */

			if (i == transactionList.size() - 1) {
				currentDate = tmp.getDate();
				dlist.add(currentTotal);
				interval = LTIDate.calculateIntervalIgnoreHolidDay(currentDate, endDate);
				ilist.add(interval);
			}

			else if (i % 15 == 0) {
				if (firstTime) {
					preDate = tmp.getDate();
					firstTime = false;
				} else {
					firstTime = true;

					dlist.add(currentTotal);
					interval = LTIDate.calculateIntervalIgnoreHolidDay(preDate, endDate);
					ilist.add(interval);

					currentTotal = 0;
				}
			}
		}

		double irr = LTIIRR.getInstance().getIRR(dlist, ilist, lastAmount);

		return irr;
	}
	@Deprecated
	public Double computeIRR(final Portfolio po, Date startDate, Date endDate, double originalAmount, double lastAmount) {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		List<Transaction> transactionList = null;//po.getTransactions(startDate, endDate);// portfolioManager.getCashFlowTransaction(id,
		// startDate, endDate);

		// if(transactionList == null || transactionList.size()==0)return 0.0;

		List<Double> dlist = new ArrayList<Double>();

		List<Integer> ilist = new ArrayList<Integer>();

		int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		dlist.add(-originalAmount);

		ilist.add(interval);

		for (int i = 0; i < transactionList.size(); i++) {
			Transaction tmp = transactionList.get(i);

			double amount = tmp.getAmount();

			Date currentDate = tmp.getDate();

			if (currentDate.after(endDate) || currentDate.before(startDate))
				continue;

			if (tmp.getOperation().equals("DEPOSIT")) {
				dlist.add(-amount);

				interval = LTIDate.calculateIntervalIgnoreHolidDay(currentDate, endDate);

				ilist.add(interval);
			}

			else if (tmp.getOperation().equals("WITHDRAW")) {
				dlist.add(amount);

				interval = LTIDate.calculateIntervalIgnoreHolidDay(currentDate, endDate);

				ilist.add(interval);
			}
		}

		/*
		 * long t1 = System.currentTimeMillis(); double irr =
		 * LTIIRR.getInstance().getIRR(dlist,ilist, lastAmount); long t2 =
		 * System.currentTimeMillis(); System.out.println((t2-t1)+" "+irr);
		 */

		double irr = this.solveFunction3(dlist, ilist, lastAmount);
		irr = Math.pow(irr, 252) - 1;

		Double p = new Double(irr);
		if (p.isInfinite())
			return 0.0;

		return irr;
	}

	public double getFunction(List<Double> A, List<Integer> B, double amount, double x) {
		double result = 0;
		for (int i = 0; i < A.size(); i++) {
			result += A.get(i) * Math.pow(x, B.get(i));
		}
		result += amount;
		return result;
	}

	public double getFunction2(List<Double> A, List<Integer> B, double amount, double x) {
		double result;
		int bigest = B.get(0);
		result = 0;
		for (int i = bigest; i >= 0; i--) {
			int index = B.indexOf(i);
			double tmp = 0;
			if (index >= 0) {
				tmp = A.get(index);
			}

			if (i != 0)
				result = x * (result + tmp);
			else
				result = result + tmp;
		}
		result += amount;
		return result;
	}

	private BigDecimal getFunction3(List<Double> A, List<Integer> B, double amount, BigDecimal x) {
		BigDecimal result = new BigDecimal(0);
		int bigest = B.get(0);
		for (int i = bigest; i >= 0; i--) {
			int index = B.indexOf(i);
			BigDecimal tmp = new BigDecimal(0);
			if (index >= 0) {
				tmp = new BigDecimal(A.get(index));
			}

			if (i != 0)
				result = x.multiply((result.add(tmp)));
			else
				result = result.add(tmp);
		}
		result = result.add(new BigDecimal(amount));
		return result;
	}

	public double solveFunction2(List<Double> A, List<Integer> B, double amount) {
		BigDecimal pre = new BigDecimal(-10000.0);
		BigDecimal las = new BigDecimal(10000.0);
		BigDecimal preValue = this.getFunction3(A, B, amount, pre);

		BigDecimal lasValue = this.getFunction3(A, B, amount, las);

		BigDecimal x = new BigDecimal(0);
		x = (pre.add(las)).divide(new BigDecimal(2.0));

		int time = 0;
		while (true) {
			time++;
			if (time > 10000)
				break;
			/*
			 * if(preValue.compareTo(new BigDecimal(0))==1 &&
			 * lasValue.compareTo(new BigDecimal(0))==1) return -1000;
			 * if(preValue.compareTo(new BigDecimal(0))==-1 &&
			 * lasValue.compareTo(new BigDecimal(0))==-1) return -1000;
			 */
			if (preValue.compareTo(new BigDecimal(0)) == 0)
				return pre.doubleValue();
			if (lasValue.compareTo(new BigDecimal(0)) == 0)
				return las.doubleValue();

			BigDecimal curValue = this.getFunction3(A, B, amount, x);

			if (Math.abs(curValue.doubleValue()) < 0.01)
				break;
			if (preValue.compareTo(new BigDecimal(0)) == -1 && lasValue.compareTo(new BigDecimal(0)) == 1) {
				if (curValue.compareTo(new BigDecimal(0)) == -1) {
					preValue = curValue;
					pre = x;
					x = (x.add(las)).divide(new BigDecimal(2));
				} else {
					lasValue = curValue;
					las = x;
					// x = (pre+x)/2;
					x = (x.add(pre)).divide(new BigDecimal(2));
				}
			}
			if (preValue.compareTo(new BigDecimal(0)) == 1 && lasValue.compareTo(new BigDecimal(0)) == -1) {
				if (curValue.compareTo(new BigDecimal(0)) == -1) {
					lasValue = curValue;
					las = x;
					// x = (pre+x)/2;
					x = (x.add(pre)).divide(new BigDecimal(2));
				} else {
					preValue = curValue;
					pre = x;
					// x = (x+las)/2;
					x = (x.add(las)).divide(new BigDecimal(2));
				}
			}
		}

		return x.doubleValue();
	}

	public double solveFunction3(List<Double> A, List<Integer> B, double amount) {
		double result = 0;

		double[] c = new double[B.get(0) + 1];
		for (int i = 0; i <= B.size(); ++i) {
			c[i] = 0;
		}
		for (int i = 0; i < B.size(); i++) {
			c[B.get(i)] += A.get(i);
		}
		c[0] += amount;
		PolynomialFunction f = new PolynomialFunction(c);

		BisectionSolver n = new BisectionSolver(f);
		try {
			// result = n.solve(-1000.0, 1000.0);
			result = n.solve(0.8, 1.05);
		} catch (MaxIterationsExceededException e) {
			System.out.println(StringUtil.getStackTraceString(e));
			// e.printStackTrace();
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
			// e.printStackTrace();
		}
		/*
		 * System.out.println("==========="); System.out.println(result);
		 * System.out.println(f.value(result)); double p = this.solveFunction(A,
		 * B, amount); System.out.println(p); System.out.println(f.value(p));
		 * System.out.println("===========");
		 */
		return result;
	}

	public double solveFunction(List<Double> A, List<Integer> B, double amount) {
		double pre = -100.0;
		double las = 100.0;
		double preValue = this.getFunction2(A, B, amount, pre);

		double lasValue = this.getFunction2(A, B, amount, las);

		double x = (pre + las) / 2;

		while (true) {
			if (preValue > 0 && lasValue > 0)
				return -1000;
			if (preValue < 0 && lasValue < 0)
				return -1000;
			if (preValue == 0)
				return pre;
			if (lasValue == 0)
				return las;

			double curValue = this.getFunction2(A, B, amount, x);

			if (Math.abs(curValue - 0) < 0.01)
				break;
			if (preValue < 0 && lasValue > 0) {
				if (curValue < 0) {
					preValue = curValue;
					pre = x;
					x = (x + las) / 2;
				} else {
					lasValue = curValue;
					las = x;
					x = (pre + x) / 2;
				}
			}
			if (preValue > 0 && lasValue < 0) {
				if (curValue < 0) {
					lasValue = curValue;
					las = x;
					x = (pre + x) / 2;
				} else {
					preValue = curValue;
					pre = x;
					x = (x + las) / 2;
				}
			}
		}

		return x;
	}

	private PortfolioMPT setAllBasicInfo(PortfolioMPT pmpt, Long portfolioID, Long benchMarkID, String name, Long userID, Long strategyID, Boolean isModel, Long classID, Integer year, PortfolioDailyData newPdd) {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		Portfolio po = portfolioManager.get(portfolioID);

		pmpt.setPortfolioID(portfolioID);
		pmpt.setBenchmarkID(benchMarkID);
		pmpt.setName(name);
		pmpt.setUserID(userID);
		pmpt.setStrategyID(strategyID);
		pmpt.setIsModelPortfolio(isModel);
		pmpt.setClassID(classID);
		pmpt.setYear(year);

		if (year == PortfolioMPT.FROM_STARTDATE_TO_ENDDATE) {
			pmpt.setAlpha(newPdd.getAlpha());
			pmpt.setBeta(newPdd.getBeta());
			pmpt.setRSquared(newPdd.getRSquared());
			pmpt.setStandardDeviation(newPdd.getStandardDeviation());
			pmpt.setSharpeRatio(newPdd.getSharpeRatio());
			pmpt.setTreynorRatio(newPdd.getTreynorRatio());
			pmpt.setDrawDown(newPdd.getDrawDown());
			pmpt.setAR(newPdd.getAR());
			pmpt.setSortinoRatio(newPdd.getSortinoRatio());
		} else if (year == PortfolioMPT.LAST_ONE_YEAR) {
			pmpt.setAlpha(newPdd.getAlpha1());
			pmpt.setBeta(newPdd.getBeta1());
			pmpt.setRSquared(newPdd.getRSquared1());
			pmpt.setStandardDeviation(newPdd.getStandardDeviation1());
			pmpt.setSharpeRatio(newPdd.getSharpeRatio1());
			pmpt.setTreynorRatio(newPdd.getTreynorRatio1());
			pmpt.setDrawDown(newPdd.getDrawDown1());
			pmpt.setAR(newPdd.getAR1());
			pmpt.setSortinoRatio(newPdd.getSortinoRatio1());
		} else if (year == PortfolioMPT.LAST_THREE_YEAR) {
			pmpt.setAlpha(newPdd.getAlpha3());
			pmpt.setBeta(newPdd.getBeta3());
			pmpt.setRSquared(newPdd.getRSquared3());
			pmpt.setStandardDeviation(newPdd.getStandardDeviation3());
			pmpt.setSharpeRatio(newPdd.getSharpeRatio3());
			pmpt.setTreynorRatio(newPdd.getTreynorRatio3());
			pmpt.setDrawDown(newPdd.getDrawDown3());
			pmpt.setAR(newPdd.getAR3());
			pmpt.setSortinoRatio(newPdd.getSortinoRatio3());
		} else if (year == PortfolioMPT.LAST_FIVE_YEAR) {
			pmpt.setAlpha(newPdd.getAlpha5());
			pmpt.setBeta(newPdd.getBeta5());
			pmpt.setRSquared(newPdd.getRSquared5());
			pmpt.setStandardDeviation(newPdd.getStandardDeviation5());
			pmpt.setSharpeRatio(newPdd.getSharpeRatio5());
			pmpt.setTreynorRatio(newPdd.getTreynorRatio5());
			pmpt.setDrawDown(newPdd.getDrawDown5());
			pmpt.setAR(newPdd.getAR5());
			pmpt.setSortinoRatio(newPdd.getSortinoRatio5());
		} else {
			pmpt.setAlpha(newPdd.getAlpha());
			pmpt.setBeta(newPdd.getBeta());
			pmpt.setRSquared(newPdd.getRSquared());
			pmpt.setStandardDeviation(newPdd.getStandardDeviation());
			pmpt.setSharpeRatio(newPdd.getSharpeRatio());
			pmpt.setTreynorRatio(newPdd.getTreynorRatio());
			pmpt.setDrawDown(newPdd.getDrawDown());
			pmpt.setAR(newPdd.getAR());
			pmpt.setSortinoRatio(newPdd.getSortinoRatio());
		}
		return pmpt;
	}

	private PortfolioDailyData setAllMPT(PortfolioDailyData oldPdd, PortfolioDailyData newPdd, Integer year) {
		if (year == 0) {
			oldPdd.setAlpha(newPdd.getAlpha());
			oldPdd.setBeta(newPdd.getBeta());
			oldPdd.setRSquared(newPdd.getRSquared());
			oldPdd.setStandardDeviation(newPdd.getStandardDeviation());
			oldPdd.setSharpeRatio(newPdd.getSharpeRatio());
			oldPdd.setTreynorRatio(newPdd.getTreynorRatio());
			oldPdd.setDrawDown(newPdd.getDrawDown());
			oldPdd.setAR(newPdd.getAR());
			oldPdd.setSortinoRatio(newPdd.getSortinoRatio());
		} else if (year == 1) {
			oldPdd.setAlpha1(newPdd.getAlpha());
			oldPdd.setBeta1(newPdd.getBeta());
			oldPdd.setRSquared1(newPdd.getRSquared());
			oldPdd.setStandardDeviation1(newPdd.getStandardDeviation());
			oldPdd.setSharpeRatio1(newPdd.getSharpeRatio());
			oldPdd.setTreynorRatio1(newPdd.getTreynorRatio());
			oldPdd.setDrawDown1(newPdd.getDrawDown());
			oldPdd.setAR1(newPdd.getAR());
			oldPdd.setSortinoRatio1(newPdd.getSortinoRatio());
		} else if (year == 3) {
			oldPdd.setAlpha3(newPdd.getAlpha());
			oldPdd.setBeta3(newPdd.getBeta());
			oldPdd.setRSquared3(newPdd.getRSquared());
			oldPdd.setStandardDeviation3(newPdd.getStandardDeviation());
			oldPdd.setSharpeRatio3(newPdd.getSharpeRatio());
			oldPdd.setTreynorRatio3(newPdd.getTreynorRatio());
			oldPdd.setDrawDown3(newPdd.getDrawDown());
			oldPdd.setAR3(newPdd.getAR());
			oldPdd.setSortinoRatio3(newPdd.getSortinoRatio());
		} else if (year == 5) {
			oldPdd.setAlpha5(newPdd.getAlpha());
			oldPdd.setBeta5(newPdd.getBeta());
			oldPdd.setRSquared5(newPdd.getRSquared());
			oldPdd.setStandardDeviation5(newPdd.getStandardDeviation());
			oldPdd.setSharpeRatio5(newPdd.getSharpeRatio());
			oldPdd.setTreynorRatio5(newPdd.getTreynorRatio());
			oldPdd.setDrawDown5(newPdd.getDrawDown());
			oldPdd.setAR5(newPdd.getAR());
			oldPdd.setSortinoRatio5(newPdd.getSortinoRatio());
		}
		return oldPdd;
	}

	public int getSubListIndex(List<Date> dateList, Date today, int year) {

		Date lastDate = LTIDate.getNewNYSEYear(today, year * -1);
		Date curDate = new Date();
		Date preDate = (Date) dateList.get(0);

		if (LTIDate.equals(preDate, lastDate))
			return 0;

		if (preDate.after(lastDate))
			return 0;
		int pre = 0;
		int last = dateList.size() - 1;
		int mid;
		while (true) {

			if (last - pre == 1)
				break;

			mid = (pre + last) / 2;
			curDate = dateList.get(mid);
			if (LTIDate.equals(curDate, lastDate)) {
				return mid;
			} else if (curDate.before(lastDate)) {
				pre = mid;
			} else if (curDate.after(lastDate)) {
				last = mid;
			}
		}
		return pre;

	}

	public int getYearEndIndex(List<Date> dateList, Date today) {

		Date lastDate = LTIDate.getNewNYSEYear(today, -1);
		lastDate = LTIDate.getLastNYSETradingDayOfYear(lastDate);
		Date curDate = new Date();
		Date preDate = (Date) dateList.get(0);

		if (LTIDate.equals(preDate, lastDate))
			return 0;

		if (preDate.after(lastDate))
			return 0;
		int pre = 0;
		int last = dateList.size() - 1;
		int mid;
		while (true) {

			if (last - pre == 1)
				break;

			mid = (pre + last) / 2;
			curDate = dateList.get(mid);
			if (LTIDate.equals(curDate, lastDate)) {
				return mid;
			} else if (curDate.before(lastDate)) {
				pre = mid;
			} else if (curDate.after(lastDate)) {
				last = mid;
			}
		}
		return pre;

	}

	/**
	 * *************************************************************************
	 * ****************************************************
	 */

	public List<Double> getReturns(List<PortfolioDailyData> pdds, TimeUnit tu) {
		List<Double> results = new ArrayList<Double>();
		double pre = 0, cur = 0;
		Date preDate, curDate, next;
		if (pdds == null || pdds.size() == 0)
			return null;
		preDate = curDate = next = pdds.get(0).getDate();
		if (tu == TimeUnit.DAILY) {
			for (int i = 0; i < pdds.size(); i++) {
				if (i == 0) {
					pre = pdds.get(i).getAmount();
				} else {
					cur = pdds.get(i).getAmount();
					double re = 0;
					if (pre != 0.0)
						re = this.computeIntervalReturn(cur, pre);// cur/pre -
					// 1.0;
					results.add(re);
					pre = cur;
				}
			}
		} else {
			for (int i = 0; i < pdds.size(); i++)

			{
				if (i == 0) {
					preDate = pdds.get(i).getDate();
					next = LTIDate.getNewTradingDateForward(preDate, tu, 1);
					pre = pdds.get(i).getAmount();
				} else {
					curDate = pdds.get(i).getDate();
					if (i == pdds.size() - 1 || (LTIDate.equals(curDate, next)) || (pdds.get(i - 1).getDate().before(next) && curDate.after(next))) {
						cur = pdds.get(i).getAmount();
						next = LTIDate.getNewTradingDateForward(curDate, tu, 1);
						double re = 0;
						if (pre != 0.0)
							re = this.computeIntervalReturn(cur, pre);// cur/pre
						// -
						// 1.0;
						results.add(re);
						pre = cur;
					}
				}
			}
		}
		return results;
	}

	@Override
	public void getConfidenceValue(long portfolioID, TimeUnit tu) {
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		Date startDate = portfolioManager.getEarliestDate(portfolioID);

		Date endDate = portfolioManager.getLatestDate(portfolioID);

		List<Double> valueList = new ArrayList<Double>();

		List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolioID);

		if (pdds == null || pdds.size() == 0)
			return;

		/*
		 * try { valueList = this.getReturns(startDate, endDate, tu,
		 * portfolioID, SourceType.PORTFOLIO_RETURN,false); } catch
		 * (NoPriceException e) { e.printStackTrace(); }
		 */
		valueList = this.getReturns(pdds, tu);

		if (valueList == null || valueList.size() == 0)
			return;

		this.setAllConfidencevalue(portfolioID, -1, valueList, false, "");

	}

	public void getConfidneceValue(long portfolioID, long strategyID, String ruleName, List<Double> amounts) {
		List<Double> valueList = new ArrayList<Double>();

		// valueList = this.getTimeUnitReturns(amounts);

		this.setAllConfidencevalue(portfolioID, strategyID, amounts, true, ruleName);
	}

	private void setAllConfidencevalue(long portfolioID, long strategyID, List<Double> returns, boolean strategy, String ruleName) {
		if (returns == null || portfolioID < 0 || ruleName == null || returns.size() == 0)
			return;

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		List<ConfidenceCheck> tmpList = portfolioManager.checkConfidence(portfolioID, strategyID, strategy);
		ConfidenceCheck cc = null;
		boolean newone = true;
		if (strategy) {
			for (int i = 0; i < tmpList.size(); i++) {
				if (tmpList.get(i).getStrategyID() == strategyID && tmpList.get(i).getPortfolioID() == portfolioID && tmpList.get(i).getRuleName().equalsIgnoreCase(ruleName)) {
					cc = tmpList.get(i);
					newone = false;
					break;
				}
			}
		} else {
			for (int i = 0; i < tmpList.size(); i++) {
				if (tmpList.get(i).getPortfolioID() == portfolioID && tmpList.get(i).getStrategyID() == null) {
					cc = tmpList.get(i);
					newone = false;
					break;
				}
			}
		}
		if (newone)
			cc = new ConfidenceCheck();

		ReturnComparator comparator = new ReturnComparator();
		Collections.sort(returns, comparator);

		double mean = this.computeAverage(returns);

		double variance = this.computeStandardDeviation(returns);

		double value5, value10, value15, value20, value30, value40, value50, value60;

		long sampleSize = returns.size();

		if (sampleSize == 0) {
			cc.setAboveMeanPossibility(0.0);
			cc.setMaxReturnUnderSampleProportion5(0.0);
			cc.setMaxReturnUnderSampleProportion10(0.0);
			cc.setMaxReturnUnderSampleProportion15(0.0);
			cc.setMaxReturnUnderSampleProportion20(0.0);
			cc.setMaxReturnUnderSampleProportion30(0.0);
			cc.setMaxReturnUnderSampleProportion40(0.0);
			cc.setMaxReturnUnderSampleProportion50(0.0);
			cc.setMaxReturnUnderSampleProportion60(0.0);
			cc.setMean(mean);
			cc.setVariance(variance);

			if (strategy)
				cc.setStrategyID(strategyID);
			cc.setPortfolioID(portfolioID);

			cc.setSampleSize(sampleSize);
			cc.setRuleName(ruleName);

			portfolioManager.saveConfidenceCheck(cc);
			return;
		}

		if (sampleSize == 1) {
			value5 = returns.get(0);
			value10 = returns.get(0);
			value15 = returns.get(0);
			value20 = returns.get(0);
			value30 = returns.get(0);
			value40 = returns.get(0);
			value50 = returns.get(0);
			value60 = returns.get(0);
		}

		else {
			value5 = returns.get((int) Math.ceil(sampleSize * 0.95) - 1);

			value10 = returns.get((int) Math.ceil(sampleSize * 0.9) - 1);

			value15 = returns.get((int) Math.ceil(sampleSize * 0.85) - 1);

			value20 = returns.get((int) Math.ceil(sampleSize * 0.80) - 1);

			value30 = returns.get((int) Math.ceil(sampleSize * 0.70) - 1);

			value40 = returns.get((int) Math.ceil(sampleSize * 0.60) - 1);

			value50 = returns.get((int) Math.ceil(sampleSize * 0.50) - 1);

			value60 = returns.get((int) Math.ceil(sampleSize * 0.40) - 1);
		}

		double aboveMeanPossibility;

		long aboveNum = 0;

		for (int i = 1; i <= sampleSize; i++) {
			if (returns.get(i - 1) > mean) {
				aboveNum = sampleSize - i + 1;
				break;
			}
		}

		aboveMeanPossibility = aboveNum * 1.0 / sampleSize;

		cc.setAboveMeanPossibility(aboveMeanPossibility);
		cc.setMaxReturnUnderSampleProportion5(value5);
		cc.setMaxReturnUnderSampleProportion10(value10);
		cc.setMaxReturnUnderSampleProportion15(value15);
		cc.setMaxReturnUnderSampleProportion20(value20);
		cc.setMaxReturnUnderSampleProportion30(value30);
		cc.setMaxReturnUnderSampleProportion40(value40);
		cc.setMaxReturnUnderSampleProportion50(value50);
		cc.setMaxReturnUnderSampleProportion60(value60);
		cc.setMean(mean);
		cc.setVariance(variance);

		if (strategy)
			cc.setStrategyID(strategyID);
		cc.setPortfolioID(portfolioID);

		cc.setSampleSize(sampleSize);
		cc.setRuleName(ruleName);

		portfolioManager.saveConfidenceCheck(cc);
	}

	/**
	 * *************************************************************************
	 * ************************************************************
	 */
	/*
	 * private int getDateIndex(List<Date> dateList,int count, TimeUnit tu,Date
	 * today) { Date lastDate = LTIDate.getNewTradingDateBackward(today, tu,
	 * count); Date curDate = dateList.get(dateList.size()-1); Date preDate =
	 * (Date)dateList.get(0);
	 * 
	 * if(LTIDate.equals(preDate, lastDate))return 0;
	 * 
	 * if(preDate.after(lastDate))return 0; int pre = 0; int last =
	 * dateList.size() - 1; int mid; while(true){
	 * 
	 * if(last - pre == 1)break;
	 * 
	 * mid = (pre + last) /2; curDate = dateList.get(mid);
	 * if(LTIDate.equals(curDate, lastDate)){ return mid; } else
	 * if(curDate.before(lastDate)){ pre = mid; } else
	 * if(curDate.after(lastDate)){ last = mid; } } return pre; }
	 */

	/*
	 * for fund Alert if no alter, return 0;if n*green star, return n;if n*red
	 * star, return -n;
	 */
	/*
	 * public FundAlert getFundAlert(List<Double> returns,List<Double>
	 * benchs,List<Date> dateList,double DR,double DBR,final Security security,
	 * TimeUnit tu, int count,Date today)//, List<Double> points) { try{
	 * 
	 * FileWriter buff = new FileWriter("C://HSGFX.csv",true);
	 * 
	 * int index = this.getDateIndex(dateList, count, tu, today);
	 * 
	 * List<Double> values = returns.subList(index, returns.size());
	 * List<Double> benchValues = benchs.subList(index, benchs.size());
	 * 
	 * double std = this.computeStandardDeviation(values); double mean =
	 * this.computeAverage(values); double NDR = (DR-mean)/(std);
	 * 
	 * double bStd = this.computeStandardDeviation(benchValues); double bMean =
	 * this.computeAverage(benchValues); double NDBR = (DBR-bMean)/(bStd);
	 * 
	 * double FB = Math.abs(NDR/NDBR);
	 * 
	 * double skew = this.getSkew(values); double kutosis =
	 * this.getKurtosis(values);
	 * 
	 * double alertScore = 0; if(NDBR > 0) { if(NDR > 0)alertScore = FB; else
	 * alertScore = NDR - NDBR; } else if(NDBR < 0){ if(NDR > 0)alertScore = NDR
	 * - NDBR; else if(NDR <0)alertScore = -FB; else if(NDR == 0)alertScore =
	 * NDR; }
	 * 
	 * 
	 * buff.write(today+","+DR+","+mean+","+std+","+NDR+","+DBR+","+bMean+","+bStd
	 * +","+NDBR+","+FB+","+alertScore+","+skew+","+kutosis+"\r\n");
	 * 
	 * 
	 * FundAlert fa = new FundAlert(); fa.setDate(today); fa.setDR(DR);
	 * fa.setMean(mean); fa.setSecurityID(security.getID()); fa.setSTD(std);
	 * //fa.setSTD(alertScore);
	 * 
	 * if(alertScore>3) fa.setPointType(3.0); else if(alertScore>=2.5)
	 * fa.setPointType(2.5); else if(alertScore>=2) fa.setPointType(2.0); else
	 * if(alertScore>=1.5) fa.setPointType(1.5); else if(alertScore<-3)
	 * fa.setPointType(-3.0); else if(alertScore<=-2.5) fa.setPointType(-2.5);
	 * else if(alertScore<=-2) fa.setPointType(-2.0); else if(alertScore<=-1.5)
	 * fa.setPointType(-1.5); else fa.setPointType(0.0);
	 * 
	 * buff.flush(); buff.close();
	 * 
	 * return fa; } catch (Exception e){ return null; }
	 * 
	 * /*ReturnComparator comparator = new ReturnComparator();
	 * Collections.sort(points, comparator);
	 */

	/*
	 * double curValue = 0; for(int i=0;i<points.size();i++){ curValue = mean +
	 * points.get(i)*std; if(DR>curValue) { FundAlert fa = new FundAlert();
	 * fa.setDate(today); fa.setDR(DR); fa.setMean(mean);
	 * fa.setPointType(points.get(i)); fa.setSecurityID(security.getID());
	 * fa.setSTD(std); return fa; } curValue = mean - points.get(i)*std;
	 * if(DR<curValue) { FundAlert fa = new FundAlert(); fa.setDate(today);
	 * fa.setDR(DR); fa.setMean(mean); fa.setPointType(points.get(i));
	 * fa.setSecurityID(security.getID()); fa.setSTD(std); return fa; } }
	 * 
	 * /*double value1 = mean + std; double value2 = value1 + std; if(DR>value2)
	 * type = 2; else if(DR>value1) type = 1; else { value1 = mean-std; value2 =
	 * value1 - std; if(DR<value2) type = -2; else if(DR<value1) type = -1; }
	 * 
	 * if(type == 0){ } else { FundAlert fa = new FundAlert();
	 * fa.setDate(today); fa.setDR(DR); fa.setMean(mean); fa.setPointType(type);
	 * fa.setSecurityID(security.getID()); fa.setSTD(std); return fa; }
	 */

	// }
	/*
	 * public double getSkew(List<Double> values) { double result = 0;
	 * 
	 * double average = this.computeAverage(values); //double std =
	 * this.computeStandardDeviation(values); double m3 = 0,m2=0; double n =
	 * values.size(); for(int i=0;i<n;i++) { m3+=Math.pow(values.get(i)-average,
	 * 3.0); m2+=Math.pow(values.get(i)-average,2.0); } result =
	 * (m3/n)/(Math.pow((m2/n), 1.5)); return result; } public double
	 * getKurtosis(List<Double> values) { double result = 0;
	 * 
	 * double average = this.computeAverage(values); double m3 = 0,m2=0; double
	 * n = values.size(); for(int i=0;i<n;i++) {
	 * m3+=Math.pow(values.get(i)-average, 4.0);
	 * m2+=Math.pow(values.get(i)-average,2.0); } result =
	 * (m3/n)/(Math.pow((m2/n), 2))-3; return result; }
	 */
	/**
	 * *************************************************************************
	 * ************************************************************
	 */
	public static void main(String[] args) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		// baseFormulaManager.getConfidenceValue(2828L, TimeUnit.MONTHLY);
		// Date date = LTIDate.getDate(2002, 1, 14);
		// baseFormulaManager.computeRSI(date, date, SourceType.SECURITY_AMOUNT,
		// 1551L, false);
		double growth = baseFormulaManager.computeEPSGrowth(1, 243, 5);
		System.out.println(growth);
		/*
		 * List<Double> a = new ArrayList<Double>(); List<Integer> b = new
		 * ArrayList<Integer>(); a.add(2.0); b.add(2);
		 * System.out.println(baseFormulaManager.getFunction(a, b, 1, -1));
		 * /*System.out.println(baseFormulaManager.getFunction2(a, b, 1, -1));
		 */
	}

	/***************************************************************************
	 * public List<Double> addToPriorityList(List<Double> list, double value) {
	 * List<Double> returnList = new ArrayList<Double>(); int size= list.size();
	 * int front=0; int tail=list.size()-1; int mid=0; while(front<=tail) { mid
	 * = (front+tail)/2; if(front == tail || tail==-1 || front==size) { break; }
	 * double tmp = list.get(mid); if(tmp == value) { break; } if(tmp<value) {
	 * tail=mid-1; } else { front=mid+1; } } if(front==size)returnList=list;
	 * else if(tail==-1); else{ double cur = list.get(mid);
	 * if(cur<=value)returnList = list.subList(0, mid); else returnList =
	 * list.subList(0, mid+1); } returnList.add(value); return returnList; }
	 * 
	 * public List<Double> deleteFromPriorityList(List<Double> list, double
	 * value) { int size= list.size(); int front=0; int tail=list.size()-1; int
	 * mid=0; while(front<=tail) { mid = (front+tail)/2; double tmp =
	 * list.get(mid); if(tmp == value) { list.remove(mid); break; }
	 * 
	 * if(front == tail)break;
	 * 
	 * if(tmp<value) { tail=mid-1; } else { front=mid+1; } if(tail==-1 ||
	 * front==size) { break; } } return list; }
	 * 
	 * private double getDrawDown(List<Double> list) { if(list == null)return 0;
	 * if(list.size()==0)return 0; double head = list.get(0); double last =
	 * list.get(list.size()-1); return (head-last)/head; } /
	 **************************************************************************/
	public double computeHalfDeviation(List<Double> values, List<Double> benchs, TimeUnit tu) {
		double halfDividend = 0;
		int size = values.size();
		for (int i = 0; i < size; i++) {
			double value = Math.log(1 + values.get(i));
			double bench = Math.log(1 + benchs.get(i));
			if (value < bench) {
				halfDividend += (value - bench) * (value - bench);
			}
		}
		halfDividend = Math.sqrt(halfDividend / (size - 1));
		if (tu == TimeUnit.DAILY)
			halfDividend = halfDividend * Math.pow(TimePara.workingday, 0.5);
		else if (tu == TimeUnit.WEEKLY)
			halfDividend = halfDividend * Math.pow(52, 0.5);
		else if (tu == TimeUnit.MONTHLY)
			halfDividend = halfDividend * Math.pow(12, 0.5);
		else if (tu == TimeUnit.QUARTERLY)
			halfDividend = halfDividend * Math.pow(4, 0.5);
		else if (tu == TimeUnit.YEARLY)
			halfDividend = halfDividend * Math.pow(1, 0.5);
		return halfDividend;
	}

	public double getSortinoRatio(long securityID, Date startDate, Date endDate, TimeUnit tu, SourceType st) {
		try {
			List<Double> values = this.getReturns(startDate, endDate, tu, securityID, st, false);
			List<Double> risk = this.getReturns(startDate, endDate, tu, this.getCash().getID(), SourceType.SECURITY_RETURN, false);
			double hd = this.computeHalfDeviation(values, risk, tu);
			double AR = this.computeAnnualizedReturn(startDate, endDate, st, securityID, 0, false);
			double rR = this.computeAnnualizedReturn(startDate, endDate, st, this.getCash().getID(), 0, false);
			return this.computeRatio(AR, rR, hd);
		} catch (NoPriceException e) {
			System.out.println(StringUtil.getStackTraceString(e));
			// e.printStackTrace();
			return 0;
		}
	}

	/********************************************************************/
	/** ** calculate the compound EPS Growth Ratio **** */
	/** ** derived by the formula:eps1(1+growth)^n=eps2 **** */
	/*******************************************************************/
	@Override
	public double computeEPSGrowth(double eps1, double eps2, int count) {
		double growth;
		try {
			if (eps2 / eps1 < 0.1)
				growth = -1 - Math.pow(-(eps2 / eps1), 1.0 / count);
			else
				growth = -1 + Math.pow(eps2 / eps1, 1.0 / count);
			return growth;
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
			// e.printStackTrace();
		}
		return Double.NEGATIVE_INFINITY;
	}

}
