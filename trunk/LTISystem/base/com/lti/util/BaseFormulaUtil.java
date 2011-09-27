/**
 * 
 */
package com.lti.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.BisectionSolver;
import org.apache.commons.math.analysis.PolynomialFunction;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.LTINumber;
import com.lti.type.MPT;
import com.lti.type.SourceType;
import com.lti.type.TimePara;
import com.lti.type.TimeUnit;

/**
 * @author ccd
 *
 */
public class BaseFormulaUtil {
	
	class MPTSigmaData{
		public double sigmaXB =0;
		public double sigmaX =0;
		public double sigmaXX =0;
		public double sigmaB =0;
		public double sigmaBB =0;
		public double sigmaR =0;
		public double lastR =0;
		public double lastX =0;
		public double lastB =0;
		public double lastLogR =0;
		public double lastLogX =0;
		public double sigmaLogXX =0;
		public double sigmaLogX =0;
		public double sigmaSR =0;
		public double maxAmount =0;
		public double maxDrawDown =0;
		public int maxIndex =0;
		public int preMaxIndex =0;
		public int minIndex =0;
		public int interval =0;
		public int startIndex = 0;
		public boolean sigmaFirst = true;
		public boolean deleteFlag = false;
		
		
		public void setData(MPTSigmaData msd){
			this.sigmaFirst = false;
			this.sigmaXB = msd.sigmaXB;
			this.sigmaX = msd.sigmaX;
			this.sigmaXX = msd.sigmaXX;
			this.sigmaB = msd.sigmaB;
			this.sigmaBB = msd.sigmaBB;
			this.sigmaR = msd.sigmaR;
			this.sigmaLogX = msd.sigmaLogX;
			this.sigmaLogXX = msd.sigmaLogXX;
			this.sigmaSR = msd.sigmaSR;
			this.lastR = msd.lastR;
			this.lastX = msd.lastX;
			this.lastB = msd.lastB;
			this.lastLogR = msd.lastLogR;
			this.lastLogX = msd.lastLogX;
			this.maxIndex = msd.maxIndex;
			this.minIndex = msd.minIndex;
			this.maxAmount = msd.maxAmount;
			this.maxDrawDown = msd.maxDrawDown;
			this.preMaxIndex = msd.preMaxIndex;
			this.startIndex = msd.startIndex;
			this.deleteFlag = false;
			
		}
		
		public void addData(double curPortfolioAmount, int index, double portfolioReturn, double benchMarkReturn, double riskFreeReturn, double portfolioLogReturn, double riskFreeLogReturn){
			this.sigmaXB += portfolioReturn * benchMarkReturn;
			this.sigmaX += portfolioReturn;
			this.sigmaXX += portfolioReturn * portfolioReturn;
			this.sigmaB += benchMarkReturn;
			this.sigmaBB += benchMarkReturn * benchMarkReturn;
			this.sigmaR += + riskFreeReturn;
			this.sigmaLogXX += portfolioLogReturn * portfolioLogReturn;
			this.sigmaLogX += portfolioLogReturn;
			if (portfolioLogReturn < riskFreeLogReturn)
				this.sigmaSR += (portfolioLogReturn - riskFreeLogReturn) * (portfolioLogReturn - riskFreeLogReturn);
			if (this.maxAmount < curPortfolioAmount) {
				this.maxAmount = curPortfolioAmount;
				this.preMaxIndex = index;
			}
			double tmpDrawDown = (this.maxAmount - curPortfolioAmount)/ this.maxAmount;
			if (this.maxDrawDown < tmpDrawDown) {
				this.maxDrawDown = tmpDrawDown;
				this.minIndex = index;
				this.maxIndex = this.preMaxIndex;
			}
		}
		
		public void doIncData(double lastX, double lastB, double lastR, double curPortfolioAmount, int index){
			this.lastX = lastX;
			this.lastR = lastR;
			this.lastB = lastB;
			this.lastLogX = Math.log(1 + lastX);
			this.lastLogR = Math.log(1 + lastR);
			if(this.startIndex < index){
				this.sigmaXB -= lastX * lastB;
				this.sigmaX -= lastX;
				this.sigmaXX -= lastX * lastX;
				this.sigmaB -= lastB;
				this.sigmaBB -= lastB * lastB;
				this.sigmaR -= lastR;
				this.sigmaLogXX -= this.lastLogX * this.lastLogX;
				this.sigmaLogX -= this.lastLogX;
				if (this.lastLogX < this.lastLogR)
					this.sigmaSR -= (this.lastLogX - this.lastLogR) * (this.lastLogX - this.lastLogR);
				if (!this.deleteFlag)
					this.deleteFlag = (this.startIndex == this.maxIndex);
				++this.startIndex;
			}else{
				this.sigmaXB += lastX * lastB;
				this.sigmaX += lastX;
				this.sigmaXX += lastX * lastX;
				this.sigmaB += lastB;
				this.sigmaBB += lastB * lastB;
				this.sigmaR += lastR;
				this.sigmaLogXX += this.lastLogX * this.lastLogX;
				this.sigmaLogX += this.lastLogX;
				if (this.lastLogX < this.lastLogR)
					this.sigmaSR += (this.lastLogX - this.lastLogR) * (this.lastLogX - this.lastLogR);
				if (!this.deleteFlag)
					this.deleteFlag = (this.startIndex == this.maxIndex);
				if (!deleteFlag)
					this.deleteFlag = (this.startIndex == this.minIndex);
				--this.startIndex;
			}
		}
		
		public void setDrawDownData(List<Double> portfolioAmountList){
			this.maxAmount = portfolioAmountList.get(0);
			this.maxDrawDown = 0;
			this.minIndex = 0;
			this.maxIndex = 0;
			this.preMaxIndex = 0;
			for (int i = 0; i < portfolioAmountList.size(); ++i) {
				double tmpAmount = portfolioAmountList.get(i);
				if (this.maxAmount < tmpAmount) {
					this.maxAmount = tmpAmount;
					this.preMaxIndex = i;
				}
				double tmpDrawDown = (maxAmount - tmpAmount) / maxAmount;
				if (this.maxDrawDown < tmpDrawDown) {
					this.maxDrawDown = tmpDrawDown;
					this.minIndex = i;
					this.maxIndex = preMaxIndex;
				}
			}
			this.maxIndex += this.startIndex;
			this.minIndex += this.startIndex;
			this.preMaxIndex += this.startIndex;
		}
	}
	
	private double rate = 0.04546;
	/**
	 * check if the num is zero
	 * @param num
	 * @return
	 */
	private boolean isZero(double num) {
		if (Math.abs(num - 0.0) <= LTINumber.ZERO)
			return true;
		return false;
	}
	/**
	 * check if the num is infinite
	 * @param num
	 * @return
	 */
	private boolean isINF(double num) {
		if (Math.abs(num) >= LTINumber.INF)
			return true;
		return false;
	}
	/**
	 * compute average
	 * @param valList
	 * @return
	 */
	private double computeAverage(List<Double> valList){
		if(valList == null || valList.size() < 1)
			return 0;
		double average = 0;
		int size = valList.size();
		for(int i=0;i<size;++i)
			average += valList.get(i);
		return average / size;
	}
	/**
	 * compute Alpha
	 * @param beta
	 * @param portfolioReturn
	 * @param benchmarkReturn
	 * @return if any parameter is INF, we will retrun INF as Alpha
	 */
	public double computeAlpha(double beta, double portfolioReturn, double benchmarkReturn) {
		if (isINF(beta) || isINF(portfolioReturn) || isINF(benchmarkReturn))
			return LTINumber.INF;
		return portfolioReturn - beta * benchmarkReturn;
	}
	
	/**
	 * compute Beta
	 * @param portfolioReturns
	 * @param benchmarkReturns
	 * @return if error, return INF as Beta
	 */
	public double computeBeta(List<Double> portfolioReturns, List<Double> benchmarkReturns) {
		if(portfolioReturns == null || benchmarkReturns == null || portfolioReturns.size() != benchmarkReturns.size() || portfolioReturns.size() < 1)
			return LTINumber.INF;
		int size= portfolioReturns.size();
		double sumPortfolio = 0, sumPortAndBench = 0, sumBenchmark = 0, sumBenchSquared = 0;
		for(int i=0;i<size;++i){
			sumPortfolio += portfolioReturns.get(i);
			sumBenchmark += benchmarkReturns.get(i);
			sumPortAndBench += portfolioReturns.get(i) * benchmarkReturns.get(i);
			sumBenchSquared += benchmarkReturns.get(i) * benchmarkReturns.get(i);
		}
		double below = size * sumBenchSquared - sumBenchmark * sumBenchmark;
		if (isZero(below)) 
			return LTINumber.INF;
		return (size * sumPortAndBench - sumBenchmark * sumPortfolio) / below;
	}
	/**
	 * compute AR include holidays
	 * @param curValue
	 * @param preValue
	 * @param days
	 * @return
	 */
	public double computeAnnualizedReturnIncludeHolidays(double curValue, double preValue, int interval) {
		if (isZero(interval))
			return LTINumber.INF;
		if (isZero(preValue))
			return 0;
		double rate = 365.0 / interval;
		return Math.pow((curValue / preValue), rate) - 1;
	}
	/**
	 * compute AR exclude holidays
	 * @param curValue
	 * @param preValue
	 * @param interval
	 * @return
	 */
	public double computeAnnualizedReturn(double curValue, double preValue, int interval){
		if(isZero(preValue))
			return 0;
		double ar = curValue / preValue;
		if(interval != 0)
			ar = Math.pow(ar, TimePara.workingday * 1.0 / interval) - 1;
		return ar;
	}
	/**
	 * compute interval return
	 * @param curValue
	 * @param preValue
	 * @return if preValue is 0, return 0 as interval return
	 */
	public double computeIntervalReturn(double curValue, double preValue) {
		if (isZero(preValue))
			return 0.0;
		return (curValue - preValue) / preValue;
	}
	/**
	 * compute log interval return
	 * @param curValue
	 * @param preValue
	 * @return
	 */
	public double computeLogIntervalReturn(double curValue, double preValue) {
		if (isZero(preValue))
			return 0.0;
		return Math.log(curValue/preValue);
	}
	/**
	 * compute standard deviation
	 * @param valList
	 * @return
	 */
	public double computeStandardDeviation(List<Double> valList) {
		if(valList==null || valList.size()<2)
			return 0;
		int size = valList.size();
		double above = 0, average = 0, tmp = 0;
		for(int i=0;i<size;++i)
			average += valList.get(i);
		average /= size;
		for(int i=0;i<size;++i){
			tmp = valList.get(i) - average;
			above += tmp * tmp;
		}
		return Math.sqrt(above /(size - 1));
	}
	/**
	 * compute covariance
	 * @param valList1
	 * @param valList2
	 * @return
	 */
	public double computeCovariance(List<Double> valList1, List<Double> valList2) {
		if(valList1 == null || valList2 == null || valList1.size()!= valList2.size() || valList1.size()<2 )
			return LTINumber.INF;
		int size = valList1.size();
		double average1 = 0, average2 = 0, above = 0, tmp=0;
		for(int i=0;i<size;++i){
			average1 += valList1.get(i);
			average2 += valList2.get(i);
		}
		average1 /= size;
		average2 /= size;
		for(int i=0;i<size;++i)
			above += (valList1.get(i) - average1) * (valList2.get(i) - average2);
		return above / (size - 1);
	}
	/**
	 * compute correlation coefficient
	 * @param valList1
	 * @param valList2
	 * @return
	 */
	public double computeCorrelationCoefficient(List<Double> valList1, List<Double> valList2) {
		if(valList1 == null || valList2 == null || valList1.size()!= valList2.size() || valList1.size()<2 )
			return LTINumber.INF;
		double sdA = computeStandardDeviation(valList1);
		double sdB = computeStandardDeviation(valList2);
		double co = computeCovariance(valList1, valList2);
		if(isZero(sdA) || isZero(sdB))
			return LTINumber.INF;
		return co / (sdA * sdB);
	}
	/**
	 * compute SMA
	 * @param closePrices
	 * @return
	 */
	public double computeSMA(List<Double> closePrices) {
		return computeAverage(closePrices);
	}
	/**
	 * compute EMA
	 * @param closePrice
	 * @param days
	 * @param preEMA
	 * @return
	 */
	public double computeEMA(double closePrice, int days, double preEMA) {
		// TODO Auto-generated method stub
		if (days <= 1)
			return LTINumber.INF;

		return closePrice * 2 / (days - 1) + preEMA * days / (days + 1);
	}
	/**
	 * compute EMA
	 * @param valList
	 * @param count
	 * @return
	 */
	public double computeEMA(List<Double> valList, int count){
		double EMA = 0;
		if(valList !=  null && valList.size() > 1){
			int size = valList.size();
			EMA = valList.get(0);
			for(int i=1;i<size;++i){
				EMA = (valList.get(i) * 2.0 + EMA * (count - 1) * 1.0) / (count + 1);
			}
		}
		return EMA;
	}
	/**
	 * compute ratio
	 * @param portfolioReturn
	 * @param riskFreeReturn
	 * @param portfolioDeviation
	 * @return
	 */
	public double computeRatio(double portfolioReturn, double riskFreeReturn, double portfolioDeviation) {
		if (isZero(portfolioDeviation))
			return LTINumber.INF;
		return (portfolioReturn - riskFreeReturn) / portfolioDeviation;
	}
	/**
	 * compute RSI
	 * @param return1
	 * @param return2
	 * @return
	 */
	public double computeRSI(double return1, double return2) {
		double below = return1 + return2;
		if (isZero(below))
			return LTINumber.INF;
		return 100 * return1 / below;
	}
	/**
	 * compute current percentage
	 * @param partAmount
	 * @param totalAmount
	 * @return
	 */
	public double computeCurrentPercetage(double partAmount, double totalAmount) {
		if (isZero(totalAmount))
			return LTINumber.INF;
		return partAmount / totalAmount;
	}
	/**
	 * compute annulized standard deviation
	 * @param valList
	 * @param tu
	 * @return
	 */
	public double computeAnnualizedStandardDeviation(List<Double> valList, TimeUnit tu) {
		if(valList == null || valList.size() < 1)
			return 0;
		int size = valList.size();
		List<Double> logValList = new ArrayList<Double>();
		for (int i = 0; i < size; i++) 
			logValList.add(Math.log(1 + valList.get(i)));
		double sd = computeStandardDeviation(logValList);
		if(isINF(sd) || isZero(sd))
			return sd;
		if (tu == TimeUnit.DAILY) {
			sd = sd * Math.pow(TimePara.workingday, 0.5);
		} else if (tu == TimeUnit.WEEKLY) {
			sd = sd * Math.pow(TimePara.weekday, 0.5);
		} else if (tu == TimeUnit.MONTHLY) {
			sd = sd * Math.pow(TimePara.monthday, 0.5);
		} else if (tu == TimeUnit.QUARTERLY) {
			sd = sd * Math.pow(TimePara.quarterday, 0.5);
		}
		return sd;
	}
	/**
	 * compute TRIN
	 * @param nr
	 * @param nd
	 * @param vr
	 * @param vd
	 * @return
	 */
	public double computeTRIN(double nr, double nd, double vr, double vd) {
		if (isZero(vd) || isZero(vr) || isZero(nd))
			return LTINumber.INF;
		return nr * vd / (nd * vr);
	}
	/**
	 * compute period return
	 * @param valList
	 * @return
	 */
	public List<Double> computePeriodReturn(List<Double> valList) {
		if(valList == null || valList.size()<2)
			return null;
		List<Double> periodReturns = new ArrayList<Double>();
		int size = valList.size();
		double preValue = valList.get(0);
		double curValue = 0;
		for(int i=1;i<size;++i){
			curValue = valList.get(i);
			periodReturns.add(computeIntervalReturn(curValue, preValue));
			preValue = curValue;
		}
		return periodReturns;
	}
	/**
	 * compute drawdown
	 * @param valList
	 * @return
	 */
	public double computeDrawDown(List<Double> valList) {
		if (valList == null || valList.size() < 2)
			return 0;
		double drawDown = 0, curValue = 0, tmp = 0;
		int size = valList.size();
		double maxValue = valList.get(0);
		for(int i=1;i<size;++i){
			curValue = valList.get(i);
			tmp = (maxValue - curValue) / maxValue;
			drawDown = drawDown > tmp? drawDown : tmp;
			maxValue = maxValue > curValue ? maxValue : curValue;
		}
		return drawDown;
	}
	/**
	 * compute annualized return
	 * @param valList
	 * @param interval
	 * @return
	 */
	public double computeAnnualizedReturn(List<Double> valList, int interval) {
		if (isZero(interval) || valList == null || valList.size() < 2)
			return LTINumber.INF;
		double curValue = 0, preValue = 0;
		int size = valList.size();
		for (int i = 0; i < size; ++i) 
			if (valList.get(i)!= null) {
				preValue = valList.get(i);
				break;
			}
		if(isZero(preValue))
			return LTINumber.INF;
		for (int i = size - 1; i >= 0; --i) 
			if (valList.get(i)!= null) {
				curValue = valList.get(i);
				break;
			}
		return computeAnnualizedReturn(curValue, preValue, interval);
	}
	/**
	 * compute risk free return
	 * @param curValue adjcolse
	 * @param preValue adjcolse
	 * @return
	 */
	public double computeRiskFree(double curValue, double preValue){
		return computeIntervalReturn(curValue, preValue);
	}
	/**
	 * compute annualized risk free
	 * @param curValue adjclose
	 * @param preValue adjclose
	 * @param interval ignore holiday
	 * @return
	 */
	public double computeAnnualizedRiskFree(double curValue, double preValue, int interval){
		return computeAnnualizedReturn(curValue, preValue, interval);
	}
	/**
	 * compute annualized risk free
	 * @param riskFreeList
	 * @return
	 */
	public double computeAnnualizedRiskFree(List<SecurityDailyData> riskFreeList){
		if(riskFreeList == null || riskFreeList.size() < 2)
			return 0;
		int size = riskFreeList.size();
		return computeAnnualizedRiskFree(riskFreeList.get(size-1).getAdjClose(), riskFreeList.get(0).getAdjClose(), size);
	}
	/**
	 * compute average return
	 * @param sdds
	 * @return
	 */
	public double computeAverageReturn(List<Double> valList){
		if(valList == null || valList.size() <2)
			return 0;
		int count = 0;
		double averageReturn = 0, preValue = -1, curValue = -1;
		for (int i = 0; i < valList.size(); i++) {
			if (preValue == -1) {
				preValue = valList.get(i);
				continue;
			} else {
				curValue = valList.get(i);
				averageReturn += this.computeIntervalReturn(curValue, preValue);
				++count;
				preValue = curValue;
			}
		}
		if(count == 0)
			return 0;
		return averageReturn / count;
	}
	
	
	/**
	 * get risk free default
	 * @param unit
	 * @return
	 */
	public double getRiskFree(TimeUnit unit) {
		return rate;
	}
	/**
	 * get value list
	 * @param dailyDataList
	 * @param st
	 * @param nav
	 * @return
	 */
	public List<Double> getValList(List<? extends DailyData> dailyDataList, SourceType st, boolean nav){
		List<Double> valList = new ArrayList<Double>();
		int priceType = Configuration.PRICE_TYPE_ADJCLOSE;
		if(nav)
			priceType = Configuration.PRICE_TYPE_ADJNAV;
		boolean isAmount = st == SourceType.PORTFOLIO_AMOUNT || st == SourceType.SECURITY_AMOUNT || st == SourceType.BENCHMARK_AMOUNT;
		double curValue = 0, preValue = 0;
		if( dailyDataList != null && dailyDataList.size() > 1){
			preValue = dailyDataList.get(0).getValue(priceType);
			if( isAmount)
				valList.add(preValue);
			for(int i=1;i<dailyDataList.size();++i){
				curValue = dailyDataList.get(i).getValue(priceType);
				if(isAmount)
					valList.add(curValue);
				else{
					valList.add(computeIntervalReturn(curValue, preValue));
				}
				preValue = curValue;
			}
		}
		return valList;
	}
	/**
	 * get the amount for security daily data or portfolio daily data list
	 * @param dailyDataList
	 * @param st
	 * @param nav
	 * @return
	 */
	public List<Double> getAmounts(List<? extends DailyData> dailyDataList, SourceType st, boolean nav){
		List<Double> valList = new ArrayList<Double>();
		if(dailyDataList!=null){
			if(st == SourceType.PORTFOLIO_RETURN || st == SourceType.PORTFOLIO_AMOUNT){
				st = SourceType.PORTFOLIO_AMOUNT;
			}
			else
				st = SourceType.SECURITY_AMOUNT;
			int priceType = Configuration.PRICE_TYPE_ADJCLOSE;
			if(nav)
				priceType = Configuration.PRICE_TYPE_ADJNAV;
			if( dailyDataList != null && dailyDataList.size() > 1){
				for(int i=0;i<dailyDataList.size();++i)
					valList.add(dailyDataList.get(i).getValue(priceType));
			}
		}
		
		return valList;
	}
	/**
	 * compute interval return
	 * @param dailyDataList
	 * @param nav
	 * @return
	 */
	public double computeIntervalReturn(List<DailyData> dailyDataList, boolean nav){
		if(dailyDataList!= null && dailyDataList.size()>1){
			double curValue = 0, preValue = 0;
			int priceType = Configuration.PRICE_TYPE_ADJCLOSE;
			if(nav)
				priceType = Configuration.PRICE_TYPE_ADJNAV;
			int size = dailyDataList.size();
			preValue = dailyDataList.get(0).getValue(priceType);
			curValue = dailyDataList.get(size-1).getValue(priceType);
			return computeIntervalReturn(curValue, preValue);
		}
		return 0;
	}
	/**
	 * compute AR ignore holiday
	 * @param dailyDataList
	 * @param nav
	 * @return
	 */
	public double computeAnnualizedReturn(List<DailyData> dailyDataList, boolean nav){
		if(dailyDataList!= null && dailyDataList.size()>1){
			double curValue = 0, preValue = 0;
			int priceType = Configuration.PRICE_TYPE_ADJCLOSE;
			if(nav)
				priceType = Configuration.PRICE_TYPE_ADJNAV;
			int size = dailyDataList.size();
			preValue = dailyDataList.get(0).getValue(priceType);
			curValue = dailyDataList.get(size-1).getValue(priceType);
			return computeAnnualizedReturn(curValue, preValue, size);
		}
		return 0;
	}
	/**
	 * compute total return
	 * @param valList
	 * @return
	 */
	public double computeTotalReturn(List<Double> valList){
		double totalReturn = 0;
		if( valList != null && valList.size() > 1){
			double preValue = 0, curValue = 0;
			for (int i = 0; i < valList.size(); i++) {
				if (valList.get(i) != null) {
					preValue = valList.get(i);
					break;
				}
			}
			for (int i = valList.size() - 1; i >= 0; i--) {
				if (valList.get(i) != null) {
					curValue = valList.get(i);
					break;
				}
			}
			return computeIntervalReturn(curValue, preValue);
		}
		return totalReturn;
	}
	/**
	 * 
	 * @param dailyDataList
	 * @param benchList
	 * @param riskFreeList
	 * @param st should be **_RETURN
	 * @param nav
	 * @return
	 */
	public double computeAlpha(List<DailyData> dailyDataList, List<SecurityDailyData> benchList, List<SecurityDailyData> riskFreeList, SourceType st, boolean nav){
		if(st == SourceType.PORTFOLIO_RETURN)
			nav = false;
		List<Double> dailyDataReturns = getValList(dailyDataList, st, nav);
		List<Double> benchReturns = getValList(benchList, SourceType.SECURITY_RETURN, nav);
		List<Double> riskFreeReturns = getValList(riskFreeList, SourceType.SECURITY_RETURN, false);
		double beta = computeBeta(dailyDataReturns, benchReturns);
		double averageRiskFree = computeAverage(riskFreeReturns);
		double averageSecurity = computeAverage(dailyDataReturns) - averageRiskFree;
		double averageBench = computeAverage(benchReturns) - averageRiskFree;
		return this.computeAlpha(beta, averageSecurity, averageBench);
	}
	/**
	 * 
	 * @param dailyDataList
	 * @param benchList
	 * @param st should be **_RETURN
	 * @param nav
	 * @return
	 */
	public double computeBeta(List<DailyData> dailyDataList, List<SecurityDailyData> benchList, SourceType st, boolean nav){
		if(st == SourceType.PORTFOLIO_RETURN)
			nav = false;
		List<Double> dailyDataReturns = getValList(dailyDataList, st, nav);
		List<Double> benchReturns = getValList(benchList, SourceType.SECURITY_RETURN, nav);
		return computeBeta(dailyDataReturns, benchReturns);
	}
	/**
	 * compute standard deviation
	 * @param dailyDataList
	 * @param st should be **_RETURN
	 * @param nav
	 * @return
	 */
	public double computeStandardDeviation(List<DailyData> dailyDataList, SourceType st, boolean nav){
		List<Double> dailyDataReturns = getValList(dailyDataList, st, nav);
		return computeStandardDeviation(dailyDataReturns);
	}
	/**
	 * compute R square
	 * @param dailyDataList
	 * @param benchList
	 * @param st should be **_RETURN
	 * @param nav
	 * @return
	 */
	public double computeRSquared(List<DailyData> dailyDataList, List<SecurityDailyData> benchList, SourceType st, boolean nav){
		if(st == SourceType.PORTFOLIO_RETURN)
			nav = false;
		double rs = 0;
		List<Double> dailyDataReturns = getValList(dailyDataList, st, nav);
		List<Double> benchReturns = getValList(benchList, SourceType.SECURITY_RETURN, nav);
		rs = this.computeCorrelationCoefficient(dailyDataReturns, benchReturns);
		return rs * rs;
	}
	/**
	 * compute sharpe ratio
	 * @param dailyDataList
	 * @param riskFreeList
	 * @param tu
	 * @param st should be **_RETURN
	 * @param nav
	 * @return
	 */
	public double computeSharpeRatio(List<DailyData> dailyDataList, List<SecurityDailyData> riskFreeList, TimeUnit tu, SourceType st, boolean nav){
		if(dailyDataList == null || dailyDataList.size() < 2)
			return -500;
		if(st == SourceType.PORTFOLIO_RETURN)
			nav = false;
		List<Double> dailyDataReturns = getValList(dailyDataList, st, nav);
		double totalReturn = computeIntervalReturn(dailyDataList, nav);
		double deviation = computeAnnualizedStandardDeviation(dailyDataReturns, tu);
		double annualizedRiskFree = computeAnnualizedRiskFree(riskFreeList);
		double sharpeRatio = this.computeRatio(totalReturn, annualizedRiskFree, deviation);
		if (isINF(sharpeRatio))
			return -500;
		return sharpeRatio;
	}
	/**
	 * compute treynor ration
	 * @param dailyDataList
	 * @param benchList
	 * @param riskFreeList
	 * @param st should be **_RETURN
	 * @param nav
	 * @return
	 */
	public double computeTreynorRatio(List<DailyData> dailyDataList, List<SecurityDailyData> benchList, List<SecurityDailyData> riskFreeList, SourceType st, boolean nav){
		if(st == SourceType.PORTFOLIO_RETURN)
			nav = false;
		List<Double> dailyDataReturns = getValList(dailyDataList, st, nav);
		List<Double> benchReturns = getValList(benchList, SourceType.SECURITY_RETURN, nav);
		double beta = this.computeBeta(dailyDataReturns, benchReturns);
		double annualizedRiskFree = computeAnnualizedRiskFree(riskFreeList);
		double totalReturn = computeIntervalReturn(dailyDataList, nav);
		double treynorRatio = computeRatio(totalReturn, annualizedRiskFree, beta);
		return treynorRatio;
	}
	/**
	 * compute drawdown
	 * @param dailyDataList
	 * @param st shoule be **_AMOUNT
	 * @param nav
	 * @return
	 */
	public double computeDrawDown(List<DailyData> dailyDataList, SourceType st, boolean nav){
		List<Double> dailyAmounts = getValList(dailyDataList, st, nav);
		return computeDrawDown(dailyAmounts);
	}
	/**
	 * compute RSI
	 * @param dailyDataList
	 * @param st shoule be **_AMOUNT
	 * @param nav
	 * @return
	 */
	public double computeRSI(List<DailyData> dailyDataList, SourceType st, boolean nav){
		double preValue = 0, curValue = 0, positive = 0, negative = 0;
		double rsi = 0;
		List<Double> dailyAmounts = getValList(dailyDataList, st, nav);
		if(dailyAmounts!=null && dailyAmounts.size()>1){
			preValue = dailyAmounts.get(0);
			for(int i=0;i<dailyAmounts.size();++i){
				curValue = dailyAmounts.get(i);
				if(curValue > preValue)
					positive += curValue - preValue;
				else
					negative += preValue - curValue;
				preValue = curValue;
			}
			double below = positive + negative;
			if(!isZero(below))
				rsi = positive * 100 / below;
		}
		return rsi;
	}
	/**
	 * compute SMA
	 * @param dailyDataList
	 * @param st should **_AMOUNT
	 * @param nav
	 * @return
	 */
	public double computeSMA(List<DailyData> dailyDataList, SourceType st, boolean nav){
		List<Double> dailyAmounts = getValList(dailyDataList, st, nav);
		return computeAverage(dailyAmounts);
	}
	/**
	 * compute EMA
	 * @param dailyDataList
	 * @param count
	 * @param st
	 * @param nav
	 * @return
	 */
	public double computeEMA(List<DailyData> dailyDataList, int count, SourceType st, boolean nav){
		List<Double> dailyAmounts = getValList(dailyDataList, st, nav);
		return computeEMA(dailyAmounts, count);
	}
	/**
	 * compute MACD
	 * @param dailyDataList1 get by timeUnit and count = 12
	 * @param dailyDataList2 get by timeUnit and count = 26
	 * @param st
	 * @param nav
	 * @return
	 */
	public double computeMACD(List<DailyData> dailyDataList1, List<DailyData> dailyDataList2, SourceType st, boolean nav){
		double EMA1 = computeEMA(dailyDataList1, 12, st, nav);
		double EMA2 = computeEMA(dailyDataList2, 26, st, nav);
		return EMA1 - EMA2;
	}
	/**
	 * compute MPT
	 * @param dailyDataList
	 * @param benchList
	 * @param riskFreeList
	 * @param tu
	 * @param st
	 * @param interval
	 * @param startDate
	 * @param endDate
	 * @param nav
	 * @return
	 */
	public MPT computeMPTS(List<DailyData> dailyDataList, List<SecurityDailyData> benchList, List<SecurityDailyData> riskFreeList, TimeUnit tu, SourceType st, Date startDate, Date endDate, boolean nav){
		MPT mpt = new MPT();
		int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);
		List<Double> mainAmounts = this.getAmounts(dailyDataList, st, nav);
		List<Double> mainReturns = this.getValList(dailyDataList, st, nav);
		List<Double> benchReturns = this.getValList(benchList, SourceType.SECURITY_RETURN, nav);
		List<Double> riskFreeReturns = getValList(riskFreeList, SourceType.SECURITY_RETURN, false);
		double beta = this.computeBeta(mainReturns, benchReturns);
		double annualizedRiskFree = this.computeAnnualizedRiskFree(riskFreeList);
		double averageRiskFree = this.computeAverage(riskFreeReturns);
		double averageMain = this.computeAverage(mainReturns) - averageRiskFree;
		double averageBench = this.computeAverage(benchReturns) - averageRiskFree;
		double alpha = this.computeAlpha(beta, averageMain, averageBench);
		double rs = this.computeCorrelationCoefficient(mainReturns, benchReturns);
		double standardDeviation = this.computeAnnualizedStandardDeviation(mainReturns, tu);
		double drawDown = this.computeDrawDown(mainAmounts);
		double ar = this.computeAnnualizedReturn(mainAmounts, interval);
		double sharpeRatio = this.computeRatio(ar, annualizedRiskFree, standardDeviation);
		double treynorRatio = this.computeRatio(ar, annualizedRiskFree, beta);
		if(beta < LTINumber.INF)
			mpt.setBeta(beta);
		if(alpha < LTINumber.INF)
			mpt.setAlpha(alpha);
		if(rs < LTINumber.INF)
			mpt.setRSquared(rs);
		if(standardDeviation < LTINumber.INF)
			mpt.setStandardDeviation(standardDeviation);
		if(drawDown < LTINumber.INF)
			mpt.setDrawDown(drawDown);
		if(ar < LTINumber.INF)
			mpt.setAR(ar);
		if(sharpeRatio < LTINumber.INF)
			mpt.setSharpeRatio(sharpeRatio);
		if(treynorRatio < LTINumber.INF)
			mpt.setTreynorRatio(treynorRatio);
		mpt.setStartDate(startDate);
		mpt.setEndDate(endDate);
		mpt.setYear(endDate.getYear() + 1900);
		return mpt;
	}
	/**
	 * compute every year MPT
	 * @param dailyDataListList
	 * @param benchListList
	 * @param riskFreeListList
	 * @param startDateList
	 * @param endDateList
	 * @param tu
	 * @param st
	 * @param nav
	 * @return
	 */
	public List<MPT> computeEveryYearMPTs(List<List<DailyData>> dailyDataListList, List<List<SecurityDailyData>> benchListList, List<List<SecurityDailyData>> riskFreeListList, List<Date> startDateList, List<Date> endDateList, TimeUnit tu, SourceType st, boolean nav){
		List<MPT> mptList = new ArrayList<MPT>();
		if(dailyDataListList != null){
			for(int i=0;i<dailyDataListList.size();++i){
				MPT mpt = this.computeMPTS(dailyDataListList.get(i), benchListList.get(i), riskFreeListList.get(i), tu, st, startDateList.get(i), endDateList.get(i), nav);
				mptList.add(mpt);
			}
		}
		return mptList;
	}
	/**
	 * get the adjust security daily data list
	 * @param pdds
	 * @param sdds
	 * @return
	 */
	private List<SecurityDailyData> adjustSecurityDailyDataList(List<PortfolioDailyData> pdds, List<SecurityDailyData> sdds){
		if(sdds.size() == pdds.size())
			return sdds;
		List<SecurityDailyData> newList = new ArrayList<SecurityDailyData>();
		int securityIndex = 0;
		int securitySize = sdds.size();
		for(int i=0;i<pdds.size();++i){
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
				int tempIndex = securityIndex;
				SecurityDailyData tmp = sdds.get(tempIndex);
				if (seDate.after(curDate)) {
					boolean flag = false;
					while (tempIndex >= 0) {
						tmp = sdds.get(tempIndex);
						Date tmpDate = tmp.getDate();
						if(!LTIDate.after(tmpDate, curDate)){
							newList.add(tmp);
							flag = true;
							break;
						}
						--tempIndex;
					}
					if (!flag){
						tempIndex = 0;
						newList.add(sdds.get(tempIndex));
					}
					securityIndex = tempIndex + 1;
				} else {
					boolean flag = false;
					while (tempIndex < securitySize) {
						tmp = sdds.get(tempIndex);
						Date tmpDate = tmp.getDate();
						if(!LTIDate.before(tmpDate, curDate)){
							newList.add(tmp);
							flag = true;
							break;
						}
						++tempIndex;
					}
					if (!flag){
						tempIndex = securitySize - 1;
						newList.add(sdds.get(tempIndex));
					}
					securityIndex = tempIndex + 1;
				}
			}
		}
		return newList;
	}
	
	
	
//	public List<PortfolioDailyData> getNewPortfolioDailyData(final Portfolio portfolio, Long classID, List<PortfolioDailyData> pddList,List<SecurityDailyData> sddList, List<SecurityDailyData> riskFreeList, List<Transaction> transactionList, List<PortfolioMPT> portfolioMPTList){
//		List<MPTSigmaData> sigmaList = new ArrayList<MPTSigmaData>(4);
//		int[] interval = new int[4];
//		int[] year = new int[4];
//		double lastX, lastB, lastR;
//		int[] delayYear = new int[4];
//		boolean[] pmptFlag = new boolean[4];
//		int[] dayInterval = new int[4]; 
//		int pddListMaxIndex = 0;
//		int lastTransactionIndex = 0, transactionSize = 0;
//		double prePortfolioAmount = 0, curPortfolioAmount = 0, preBenchMarkAmount = 0, curBenchMarkAmount = 0, preRiskFreeAmount = 0, curRiskFreeAmount = 0;
//		double portfolioReturn = 0, benchMarkReturn = 0, riskFreeReturn = 0, portfolioLogReturn = 0, riskFreeLogReturn = 0;
//		
//		List<Double> portfolioAmountList = new ArrayList<Double>();
//		List<Double> riskFreeAmountList = new ArrayList<Double>();
//		List<Date> dateList = new ArrayList<Date>();
//		List<Double> portfolioReturns = new ArrayList<Double>();
//		List<Double> benchMarkReturns = new ArrayList<Double>();
//		List<Double> riskFreeReturns = new ArrayList<Double>();
//		PortfolioMPT[] delayMPTs = new PortfolioMPT[4];
//		List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();
//		boolean isCashFlow = false;
//		boolean firstYearEnd = true;
//		List<PortfolioMPT> newPortfolioMPTList = new ArrayList<PortfolioMPT>();
//		if(pddList!=null && pddList.size()>0){
//			//basic information
//			dayInterval[0] = pddList.size(); dayInterval[1] = TimePara.yearday; dayInterval[2] = TimePara.yearday * 3; dayInterval[3] = TimePara.yearday * 5; 
//			interval[0] = 0; interval[1] = TimePara.workingday; interval[2] = TimePara.workingday * 3; interval[3] = TimePara.workingday * 5;
//			delayYear[0] = PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE; delayYear[1] = PortfolioMPT.DELAY_LAST_ONE_YEAR; delayYear[2] = PortfolioMPT.DELAY_LAST_THREE_YEAR; delayYear[3] = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
//			year[0] = PortfolioMPT.FROM_STARTDATE_TO_ENDDATE; year[1] = PortfolioMPT.LAST_ONE_YEAR; year[2] = PortfolioMPT.LAST_THREE_YEAR; year[3] = PortfolioMPT.LAST_FIVE_YEAR;
//			if(classID != null)
//				isCashFlow = (classID == 5);
//			if(portfolioMPTList == null)
//				portfolioMPTList = new ArrayList<PortfolioMPT>();
//			for(int i=0;i<4;++i){
//				pmptFlag[i] = false;
//				PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, delayYear[i]);
//				if(pmpt == null){
//					pmpt = new PortfolioMPT();
//					pmpt.setYear(delayYear[i]);
//				}
//				delayMPTs[i]=pmpt;
//			}
//			Long portfolioID = pddList.get(0).getPortfolioID();
//			Long benchMarkID = sddList.get(0).getSecurityID();
//			Long strategyID = portfolio.getMainStrategyID();
//			Long userID = portfolio.getUserID();
//			String name = portfolio.getName();
//			Boolean isModel = portfolio.isModel();
//			pddListMaxIndex = pddList.size()-1;
//			
//			Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pddList.get(pddListMaxIndex).getDate());
//			if(transactionList!=null && transactionList.size()>0)
//				transactionSize = transactionList.size();
//			this.adjustSecurityDailyDataList(pddList, sddList);
//			this.adjustSecurityDailyDataList(pddList, riskFreeList);
//			
//			PortfolioDailyData pddOriginal = pddList.get(0);
//			Date originalStartDate = pddOriginal.getDate();
//			for (int i = 0; i < pddList.size(); i++) {
//				PortfolioDailyData pdd = pddList.get(i);
//				Date curDate = pdd.getDate();
//				curPortfolioAmount = pdd.getAmount();
//				curBenchMarkAmount = sddList.get(i).getAdjClose();
//				curRiskFreeAmount = riskFreeList.get(i).getAdjClose();
//				while(lastTransactionIndex < transactionSize){
//					//transactinList just include transaction with operation as DEPOSIT or WITHDRAW, and it order by date asc
//					Transaction tmp = transactionList.get(lastTransactionIndex);
//					if(tmp.getDate().equals(curDate)){
//						if(tmp.getOperation().equals("DEPOSIT"))
//							curPortfolioAmount += tmp.getAmount();
//						else//withdraw
//							curPortfolioAmount -= tmp.getAmount();
//						++lastTransactionIndex;
//					}else
//						break;
//				}
//				dateList.add(curDate);
//				++interval[0];
//				portfolioAmountList.add(curPortfolioAmount);
//				riskFreeAmountList.add(curRiskFreeAmount);
//				if(i == 0){
//					prePortfolioAmount = curPortfolioAmount;
//					preBenchMarkAmount = curBenchMarkAmount;
//					preRiskFreeAmount = curRiskFreeAmount;
//				}
//				if (i > 0) {
//					portfolioReturn = this.computeIntervalReturn(curPortfolioAmount,prePortfolioAmount);
//					benchMarkReturn = this.computeIntervalReturn(curBenchMarkAmount,preBenchMarkAmount);
//					riskFreeReturn = this.computeIntervalReturn(curRiskFreeAmount,preRiskFreeAmount);
//					
//					portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioAmount, prePortfolioAmount);
//					riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeAmount, preRiskFreeAmount);
//					portfolioReturns.add(portfolioReturn);
//					benchMarkReturns.add(benchMarkReturn);
//					riskFreeReturns.add(riskFreeReturn);
//					
//					sigmaList.get(0).addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
//					
//					prePortfolioAmount = curPortfolioAmount;
//					preBenchMarkAmount = curBenchMarkAmount;
//					preRiskFreeAmount = curRiskFreeAmount;
//					
//					
//				}
//				List<Transaction> cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(0), lastTransactionIndex);
//				boolean lastOne = (i == pddListMaxIndex);
//				boolean isHoldingDate = LTIDate.equals(lastHoldingDate, curDate);
//				if (lastOne || isHoldingDate) {
//					double size = (double) portfolioAmountList.size() - 1;
//					double preAmount = portfolioAmountList.get(0);
//					double curAmount = portfolioAmountList.get((int) size);
//					double preRiskFree = riskFreeAmountList.get(0);
//					double curRiskFree = riskFreeAmountList.get((int) size);
//					//如果是cash flow, AR要用IRR算
//					PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, interval[0], isCashFlow, cashFlowTransactionList);
//					setAllMPT(pdd, newPdd, year[0]);
//					//inception
//					if (isHoldingDate) {
//						delayMPTs[0] = setAllBasicInfo(delayMPTs[0], portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, delayYear[0], newPdd);
//						pmptFlag[0] = true;
//					}
//				}
//				int days = LTIDate.calculateInterval(originalStartDate, curDate);
//				for(int j=1;j<4;++j){
//					if (days >= dayInterval[j]) {
//						MPTSigmaData curSigma = sigmaList.get(j);
//						int tmpIndex = this.getAdjustIndex(dateList, curSigma.startIndex + 1, curDate, year[j]);
//						curSigma.deleteFlag = false;
//						if(!curSigma.sigmaFirst)
//							curSigma.setData(sigmaList.get(0));
//						else{
//							curSigma.addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
//							while(curSigma.startIndex!=tmpIndex){
//								lastX = portfolioReturns.get(curSigma.startIndex);
//								lastB = benchMarkReturns.get(curSigma.startIndex);
//								lastR = riskFreeReturns.get(curSigma.startIndex);
//								curSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
//							}
//							if (curSigma.deleteFlag) {
//								List<Double> portfolioYearsAmounts = portfolioAmountList.subList(curSigma.startIndex, i + 1);
//								curSigma.setDrawDownData(portfolioYearsAmounts);
//							}
//							Date startDate = dateList.get(curSigma.startIndex);
//							double size = (double) (i - curSigma.startIndex);
//							double preAmount = portfolioAmountList.get(curSigma.startIndex);
//							double curAmount = portfolioAmountList.get(i);
//							double preRiskFree = riskFreeAmountList.get(curSigma.startIndex);
//							double curRiskFree = riskFreeAmountList.get(i);
//							//we don't consider cash flow here, it means we don't need to user compute IRR for AR
//							cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(curSigma.startIndex), lastTransactionIndex);
//							PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, curSigma, startDate, curDate, interval[j], lastOne && isCashFlow, cashFlowTransactionList);
//							setAllMPT(pdd, newPdd, year[j]);
//							if(isHoldingDate){
//								delayMPTs[j] = setAllBasicInfo(delayMPTs[j], portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, delayYear[j], newPdd);
//								pmptFlag[j] = true;
//							}
//						}
//						
//					}
//				}
//				if (LTIDate.isLastNYSETradingDayOfYear(curDate) || i == pddListMaxIndex || LTIDate.isYearEnd(curDate)){
//					PortfolioDailyData newPdd;
//					if (firstYearEnd) {
//						double size = (double) (portfolioAmountList.size() - 1);
//						double preAmount = portfolioAmountList.get(0);
//						double curAmount = portfolioAmountList.get((int) size);
//						double preRiskFree = riskFreeAmountList.get(0);
//						double curRiskFree = riskFreeAmountList.get(i);
//						cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(0), lastTransactionIndex);
//						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, -100, isCashFlow, cashFlowTransactionList);
//						firstYearEnd = false;
//					}else{
//						MPTSigmaData tmpSigma = new MPTSigmaData();
//						tmpSigma.setData(sigmaList.get(1));//use one year sigma
//						int tmpIndex = getYearEndIndex(dateList, curDate);
//						while(tmpSigma.startIndex < tmpIndex){
//							lastX = portfolioReturns.get(tmpSigma.startIndex);
//							lastB = benchMarkReturns.get(tmpSigma.startIndex);
//							lastR = riskFreeReturns.get(tmpSigma.startIndex);
//							tmpSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
//						}
//						tmpSigma.startIndex = tmpIndex;
//						List<Double> lastYearAmounts = portfolioAmountList.subList(tmpSigma.startIndex, i + 1);
//						Date startDate = dateList.get(tmpSigma.startIndex);
//						tmpSigma.setDrawDownData(lastYearAmounts);
//						double size = (double) lastYearAmounts.size() - 1;
//						double preAmount = lastYearAmounts.get(0);
//						double curAmount = lastYearAmounts.get((int) size);
//						double preRiskFree = riskFreeAmountList.get(tmpSigma.startIndex);
//						double curRiskFree = riskFreeAmountList.get(i);
//						cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(tmpSigma.startIndex), lastTransactionIndex);
//						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, tmpSigma, startDate, curDate, -100, isCashFlow, cashFlowTransactionList);
//					}
//					int curYear = curDate.getYear() + 1900;
//					PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, curYear);
//					if(pmpt == null){
//						pmpt = new PortfolioMPT();
//					}
//					pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, strategyID, userID, isModel, classID, curYear, newPdd);
//					newPortfolioMPTList.add(pmpt);
//				}
//			}
//			if (newpdds != null && newpdds.size()>0){
//				PortfolioDailyData pdd = newpdds.get(newpdds.size() - 1);
//				for(int i=0;i<4;++i){
//					PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, year[i]);
//					if(pmpt == null)
//						pmpt = new PortfolioMPT();
//					pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, year[i], pdd);
//					newPortfolioMPTList.add(pmpt);
//				}
//			}
//			for(int i=0;i<4;++i){
//				if(pmptFlag[i])
//					newPortfolioMPTList.add(delayMPTs[i]);
//			}
//		}
//		return newpdds;
//	}
	
	private List<Transaction> getCashFlowTransactions(List<Transaction> transactionList, Date startDate, int lastIndex){
		if(transactionList != null){
			for(int i=0;i<lastIndex;++i)
				if(!LTIDate.before(transactionList.get(i).getDate(), startDate))
					return transactionList.subList(i, lastIndex);
		}
		return null;
	}
	
	public List<SecurityMPT> computeSecurityMPTForAR(Security se) throws NoPriceException, SecurityException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		List<SecurityMPT> updateList = new ArrayList<SecurityMPT>();
		Date mptLastDate = se.getMptLastDate();
		Date endDate = se.getEndDate();
		int curYear = endDate.getYear()+1900;
		Long curLongYear = (long)curYear;
		Long[] years = {0L, -1L, -3L, -5L, curLongYear};
		int[] interval = new int[4];
		interval[0] = 0; interval[1] = TimePara.workingday; interval[2] = TimePara.workingday * 3; interval[3] = TimePara.workingday * 5;
		if(mptLastDate != null && endDate != null && LTIDate.before(mptLastDate, endDate)) {
			List<SecurityMPT> securityMPTList = securityManager.getSecurityMPTByYears(se.getID(), years);
			double curPrice = securityManager.getAdjPrice(se.getID(), endDate);
			double prePrice = 0;
			Date preDate = new Date();

			if(securityMPTList != null) {
				for(SecurityMPT seMPT : securityMPTList) {
					try{
						int curInterval = 0;
						switch(seMPT.getYear().intValue()) {
						case -1:
							preDate = LTIDate.getNewTradingDateBackward(endDate, TimeUnit.DAILY, interval[1]);
							curInterval = interval[1];
							break;
						case -3:
							preDate = LTIDate.getNewTradingDateBackward(endDate, TimeUnit.DAILY, interval[2]);
							curInterval = interval[2];
							break;
						case -5:
							preDate = LTIDate.getNewTradingDateBackward(endDate, TimeUnit.DAILY, interval[3]);
							curInterval = interval[3];
							break;
						case 0:
							preDate = se.getStartDate();
							curInterval = LTIDate.calculateIntervalIgnoreHolidDay(preDate, endDate);
							break;
						default:
							preDate = LTIDate.getLastYearLastNYSETradingDayOfYear(endDate);
							curInterval = TimePara.workingday;
							break;
						}
						
						
						prePrice = securityManager.getAdjPrice(se.getID(), preDate);
						double AR = this.computeAnnualizedReturn(curPrice, prePrice, curInterval);
						seMPT.setAR(AR);
						updateList.add(seMPT);
					}catch(Exception e){
						
					}
				}
			}
		}
		return updateList;
	}
	
	public List<PortfolioMPT> computePortfolioMPTsForAR(Long portfolioID, Date startDate, PortfolioDailyData curPdd, List<PortfolioMPT> portfolioMPTList) {
		//Configuration.writePortfolioUpdateLog("Part 3.2:update portfolioMPT step 1(first four MPT)", new Date(), "\n************************************\n Part 3.2:update portfolioMPT step 1(first four MPT) start \n************************************\n");
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		int[] interval = new int[4];
		int[] year = new int[4];
		//int[] delayYear = new int[4];
		Date curDate = curPdd.getDate();
		//Date delayDate = LTIDate.getHoldingDateMonthEnd(curPdd.getDate());
		year[0] = PortfolioMPT.FROM_STARTDATE_TO_ENDDATE; year[1] = PortfolioMPT.LAST_ONE_YEAR; year[2] = PortfolioMPT.LAST_THREE_YEAR; year[3] = PortfolioMPT.LAST_FIVE_YEAR;
		//delayYear[0] = PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE; delayYear[1] = PortfolioMPT.DELAY_LAST_ONE_YEAR; delayYear[2] = PortfolioMPT.DELAY_LAST_THREE_YEAR; delayYear[3] = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
		interval[0] = 0; interval[1] = TimePara.workingday; interval[2] = TimePara.workingday * 3; interval[3] = TimePara.workingday * 5;
		//PortfolioDailyData curDelayPdd = pm.getDailydata(portfolioID, delayDate);
		PortfolioDailyData startPdd = pm.getDailydata(portfolioID, LTIDate.getNewNYSETradingDay(startDate));
		//PortfolioMPT delayMPT = this.getMPTByYear(portfolioMPTList, delayYear[0]);
		PortfolioMPT curMPT = this.getMPTByYear(portfolioMPTList, year[0]);
		Double curAmount = curPdd.getAmount();
		//Double delayAmount = 0.0;
		//if(curDelayPdd!=null)delayAmount=curDelayPdd.getAmount();
		
		Double startAmount = startPdd.getAmount();
		int curInterval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, curDate);
		//int delayInterval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, delayDate);
		double AR = this.computeAnnualizedReturn(curAmount, startAmount, curInterval);
		//double delayAR = this.computeAnnualizedReturn(delayAmount, startAmount, delayInterval);
		curMPT.setAR(AR);
		//delayMPT.setAR(delayAR);
		for(int i=1;i<4;++i){
			//delayMPT = this.getMPTByYear(portfolioMPTList, delayYear[i]);
			curMPT = this.getMPTByYear(portfolioMPTList, year[i]);
			PortfolioDailyData lastPdd = pm.getDailydata(portfolioID, LTIDate.getNewTradingDateBackward(curDate, TimeUnit.DAILY, interval[i]));
			//PortfolioDailyData delayLastPdd = pm.getDailydata(portfolioID, LTIDate.getNewTradingDateBackward(delayDate, TimeUnit.DAILY, interval[i]));
			if(curMPT != null && lastPdd != null){
				AR = this.computeAnnualizedReturn(curAmount, lastPdd.getAmount(), interval[i]);
				curMPT.setAR(AR);
			}
			//if(delayMPT != null && delayLastPdd != null){
			//	delayAR = this.computeAnnualizedReturn(delayAmount, delayLastPdd.getAmount(), interval[i]);
			//	delayMPT.setAR(delayAR);
			//}
		}
		//Configuration.writePortfolioUpdateLog("Part 3.3:update portfolioMPT step 2(last 1 MPT)", new Date(), "\n************************************\n Part 3.3:update portfolioMPT step 2(last 1 MPT) start \n************************************\n");
		try{
			int curYear = curDate.getYear() + 1900;
			Date lastYearLastTradingDate = LTIDate.getLastYearLastNYSETradingDayOfYear(curDate);
			int curYearInterval = LTIDate.calculateIntervalIgnoreHolidDay(lastYearLastTradingDate, curDate);
			PortfolioMPT curYearMPT = this.getMPTByYear(portfolioMPTList, curYear);
			PortfolioDailyData CurYearlastPdd = pm.getDailydata(portfolioID, LTIDate.getNewTradingDateBackward(curDate, TimeUnit.DAILY, curYearInterval));
			if(curYearMPT != null && CurYearlastPdd != null){
				double curAR = this.computeIntervalReturn(curAmount, CurYearlastPdd.getAmount());
				curYearMPT.setAR(curAR);
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		//Configuration.writePortfolioUpdateLog("Part 3.3:update portfolioMPT step 2(last 1 MPT)", new Date(), "\n************************************\n Part 3.3:update portfolioMPT step 2(last 1 MPT) End \n************************************\n");
		return portfolioMPTList;
	}
	
	
//	public List<PortfolioMPT> computePortfolioMPTInTime(Long portfolioID, List<PortfolioDailyData> pddList, List<SecurityDailyData> sddList, List<SecurityDailyData> riskFreeList) {
//		double prePortfolioAmount = 0, curPortfolioAmount = 0, preBenchMarkAmount = 0, curBenchMarkAmount = 0, preRiskFreeAmount = 0, curRiskFreeAmount = 0;
//		double portfolioReturn = 0, benchMarkReturn = 0, riskFreeReturn = 0, portfolioLogReturn = 0, riskFreeLogReturn = 0;
//		double lastX, lastB, lastR;
//		int[] interval = new int[4];
//		int[] year = new int[4];
//		int[] delayYear = new int[4];
//		int[] dayInterval = new int[4]; 
//		boolean[] pmptFlag = new boolean[4];
//		double[] mptStartAmount = new double[4];
//		Date[] mptStartDate = new Date[4];
//		int pddListMaxIndex = 0;
//		List<MPTSigmaData> sigmaList = new ArrayList<MPTSigmaData>(4);
//		List<Double> portfolioAmountList = new ArrayList<Double>();
//		List<Double> riskFreeAmountList = new ArrayList<Double>();
//		List<Date> dateList = new ArrayList<Date>();
//		List<Double> portfolioReturns = new ArrayList<Double>();
//		List<Double> benchMarkReturns = new ArrayList<Double>();
//		List<Double> riskFreeReturns = new ArrayList<Double>();
//		PortfolioMPT[] delayMPTs = new PortfolioMPT[4];
//		//List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();
//		//HashMap<Integer, PortfolioMPT> portfolioMPTMap = new HashMap<Integer, PortfolioMPT>();
//		List<PortfolioMPT> newPortfolioMPTList = new ArrayList<PortfolioMPT>();
//		boolean isCashFlow = false;
//		boolean firstYearEnd = true;
//		int transactionIndex = 0;
//		if(pddList!=null && pddList.size()>0){
//			//basic information
//			dayInterval[0] = pddList.size(); dayInterval[1] = TimePara.yearday; dayInterval[2] = TimePara.yearday * 3; dayInterval[3] = TimePara.yearday * 5; 
//			interval[0] = 0; interval[1] = TimePara.workingday; interval[2] = TimePara.workingday * 3; interval[3] = TimePara.workingday * 5;
//			delayYear[0] = PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE; delayYear[1] = PortfolioMPT.DELAY_LAST_ONE_YEAR; delayYear[2] = PortfolioMPT.DELAY_LAST_THREE_YEAR; delayYear[3] = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
//			year[0] = PortfolioMPT.FROM_STARTDATE_TO_ENDDATE; year[1] = PortfolioMPT.LAST_ONE_YEAR; year[2] = PortfolioMPT.LAST_THREE_YEAR; year[3] = PortfolioMPT.LAST_FIVE_YEAR;
//			List<PortfolioMPT> portfolioMPTList = new ArrayList<PortfolioMPT>();
//			for(int i=0;i<4;++i){
//				MPTSigmaData sigmaData = new MPTSigmaData();
//				sigmaList.add(sigmaData);
//				pmptFlag[i] = false;
//				PortfolioMPT pmpt = new PortfolioMPT();
//				pmpt.setYear(delayYear[i]);
//				delayMPTs[i]=pmpt;
//			}
//			Long benchMarkID = sddList.get(0).getSecurityID();
//			pddListMaxIndex = pddList.size()-1;
//			
//			Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pddList.get(pddListMaxIndex).getDate());
//			sddList = this.adjustSecurityDailyDataList(pddList, sddList);
//			riskFreeList = this.adjustSecurityDailyDataList(pddList, riskFreeList);
//			
//			PortfolioDailyData pddOriginal = pddList.get(0);
//			Date originalStartDate = pddOriginal.getDate();
//			for (int i = 0; i < pddList.size(); i++) {
//				PortfolioDailyData pdd = pddList.get(i);
//				Date curDate = pdd.getDate();
//				curPortfolioAmount = pdd.getAmount();
//				
//				curBenchMarkAmount = sddList.get(i).getAdjClose();
//				curRiskFreeAmount = riskFreeList.get(i).getAdjClose();
//				
//				dateList.add(curDate);
//				++interval[0];
//				portfolioAmountList.add(curPortfolioAmount);
//				riskFreeAmountList.add(curRiskFreeAmount);
//				if(i == 0){
//					prePortfolioAmount = curPortfolioAmount;
//					preBenchMarkAmount = curBenchMarkAmount;
//					preRiskFreeAmount = curRiskFreeAmount;
//				}
//				if (i > 0) {
//					portfolioReturn = this.computeIntervalReturn(curPortfolioAmount,prePortfolioAmount);
//					benchMarkReturn = this.computeIntervalReturn(curBenchMarkAmount,preBenchMarkAmount);
//					riskFreeReturn = this.computeIntervalReturn(curRiskFreeAmount,preRiskFreeAmount);
//					
//					portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioAmount, prePortfolioAmount);
//					riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeAmount, preRiskFreeAmount);
//					portfolioReturns.add(portfolioReturn);
//					benchMarkReturns.add(benchMarkReturn);
//					riskFreeReturns.add(riskFreeReturn);
//					
//					sigmaList.get(0).addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
//					
//					prePortfolioAmount = curPortfolioAmount;
//					preBenchMarkAmount = curBenchMarkAmount;
//					preRiskFreeAmount = curRiskFreeAmount;
//					
//					
//				}
//				boolean isHoldingDate = LTIDate.equals(lastHoldingDate, curDate);
//				boolean lastOne = (i== pddListMaxIndex);
//				if (lastOne || isHoldingDate) {//是不是最后一天或者是最后一天的上个holding date
//					double size = (double) portfolioAmountList.size() - 1;
//					double preAmount = portfolioAmountList.get(0);
//					double curAmount = portfolioAmountList.get((int) size);
//					double preRiskFree = riskFreeAmountList.get(0);
//					double curRiskFree = riskFreeAmountList.get((int) size);
//					PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, interval[0], isCashFlow, null);
//					setAllMPT(pdd, newPdd, year[0]);
//					//inception
//					if (isHoldingDate) {
//						delayMPTs[0] = setAllBasicInfo(delayMPTs[0], portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, delayYear[0], newPdd);
//						pmptFlag[0] = true;
//					}
//				}
//				int days = LTIDate.calculateInterval(originalStartDate, curDate);
//				//只有最后一天了才要判断是否为cash flow， 是的话用IRR算AR，其它日子用普通算法就可以了, 所以isCashFlow在函数里设为lastOne && isCashFlow
//				for(int j=1;j<4;++j){//增量改变数据
//					if (days >= dayInterval[j]) {
//						MPTSigmaData curSigma = sigmaList.get(j);
//						int tmpIndex = this.getAdjustIndex(dateList, curSigma.startIndex + 1, curDate, year[j]);
//						
//						curSigma.deleteFlag = false;
//						if(curSigma.sigmaFirst)
//							curSigma.setData(sigmaList.get(0));
//						else{
//							curSigma.addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
//							while(curSigma.startIndex!=tmpIndex){
//								lastX = portfolioReturns.get(curSigma.startIndex);
//								lastB = benchMarkReturns.get(curSigma.startIndex);
//								lastR = riskFreeReturns.get(curSigma.startIndex);
//								curSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
//							}
//							if (curSigma.deleteFlag) {
//								List<Double> portfolioYearsAmounts = portfolioAmountList.subList(curSigma.startIndex, i + 1);
//								curSigma.setDrawDownData(portfolioYearsAmounts);
//							}
//							if (lastOne || isHoldingDate) {//如果是最后一天或者最后一个上个holding date，则计算1年、3年、5年
//								Date startDate = dateList.get(curSigma.startIndex);
//								double size = (double) (i - curSigma.startIndex);
//								double preAmount = portfolioAmountList.get(curSigma.startIndex);
//								double curAmount = portfolioAmountList.get(i);
//								double preRiskFree = riskFreeAmountList.get(curSigma.startIndex);
//								double curRiskFree = riskFreeAmountList.get(i);
//								cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(curSigma.startIndex), transactionIndex);
//								PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, curSigma, startDate, curDate, interval[j], lastOne && isCashFlow, cashFlowTransactionList);
//								setAllMPT(pdd, newPdd, year[j]);
//								if(isHoldingDate){
//									delayMPTs[j] = setAllBasicInfo(delayMPTs[j], portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, delayYear[j], newPdd);
//									pmptFlag[j] = true;
//								}
//							}
//						}
//						
//					}
//				}
//				if (LTIDate.isLastNYSETradingDayOfYear(curDate) || LTIDate.isYearEnd(curDate) || lastOne){//是不是第年最后一个交易日，或者最后一天
//					PortfolioDailyData newPdd;
//					if (firstYearEnd) {
//						double size = (double) (portfolioAmountList.size() - 1);
//						double preAmount = portfolioAmountList.get(0);
//						double curAmount = portfolioAmountList.get((int) size);
//						double preRiskFree = riskFreeAmountList.get(0);
//						double curRiskFree = riskFreeAmountList.get(i);
//						cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(0), transactionIndex);
//						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, -100, isCashFlow, cashFlowTransactionList);
//						firstYearEnd = false;
//					}else{
//						MPTSigmaData  tmpSigma = new MPTSigmaData();
//						tmpSigma.setData(sigmaList.get(1));//use one year sigma
//						int tmpIndex = getYearEndIndex(dateList, curDate);
//						while(tmpSigma.startIndex < tmpIndex){
//							lastX = portfolioReturns.get(tmpSigma.startIndex);
//							lastB = benchMarkReturns.get(tmpSigma.startIndex);
//							lastR = riskFreeReturns.get(tmpSigma.startIndex);
//							tmpSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
//						}
//						tmpSigma.startIndex = tmpIndex;
//						List<Double> lastYearAmounts = portfolioAmountList.subList(tmpSigma.startIndex, i + 1);
//						Date startDate = dateList.get(tmpSigma.startIndex);
//						tmpSigma.setDrawDownData(lastYearAmounts);
//						double size = (double) lastYearAmounts.size() - 1;
//						double preAmount = lastYearAmounts.get(0);
//						double curAmount = lastYearAmounts.get((int) size);
//						double preRiskFree = riskFreeAmountList.get(tmpSigma.startIndex);
//						double curRiskFree = riskFreeAmountList.get(i);
//						cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(tmpSigma.startIndex), transactionIndex);
//						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, tmpSigma, startDate, curDate, -100, isCashFlow, cashFlowTransactionList);
//					}
//					int curYear = curDate.getYear() + 1900;
//					PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, curYear);
//					if(pmpt == null){
//						pmpt = new PortfolioMPT();
//					}
//					pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, curYear, newPdd);
//					newPortfolioMPTList.add(pmpt);
//					//portfolioMPTMap.put(curYear, pmpt);
//				}
//			}
//			PortfolioDailyData pdd = pddList.get(pddListMaxIndex);
//			for(int i=0;i<4;++i){
//				PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, year[i]);
//				if(pmpt == null)
//					pmpt = new PortfolioMPT();
//				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, year[i], pdd);
//				newPortfolioMPTList.add(pmpt);
//				//portfolioMPTMap.put(year[i], pmpt);
//			}
//			for(int i=0;i<4;++i){
//				if(pmptFlag[i])
//					newPortfolioMPTList.add(delayMPTs[i]);
//					//portfolioMPTMap.put(delayMPTs[i].getYear(), delayMPTs[i]);
//			}
//		}
//		//return portfolioMPTMap;
//		return newPortfolioMPTList;
//	}
	
	
	public List<PortfolioMPT> computePortfolioMPTConsiderTax(final Portfolio portfolio, List<PortfolioDailyData> pddList, List<SecurityDailyData> sddList, List<SecurityDailyData> riskFreeList){
		double prePortfolioAmount = 0, curPortfolioAmount = 0, preBenchMarkAmount = 0, curBenchMarkAmount = 0, preRiskFreeAmount = 0, curRiskFreeAmount = 0;
		double portfolioReturn = 0, benchMarkReturn = 0, riskFreeReturn = 0, portfolioLogReturn = 0, riskFreeLogReturn = 0;
		double lastX, lastB, lastR;
		int[] interval = new int[4];
		int[] year = new int[4];
		int[] dayInterval = new int[4]; 
		int pddListMaxIndex = 0;
		List<MPTSigmaData> sigmaList = new ArrayList<MPTSigmaData>(4);
		List<Double> portfolioAmountList = new ArrayList<Double>();
		List<Double> riskFreeAmountList = new ArrayList<Double>();
		List<Date> dateList = new ArrayList<Date>();
		List<Double> portfolioReturns = new ArrayList<Double>();
		List<Double> benchMarkReturns = new ArrayList<Double>();
		List<Double> riskFreeReturns = new ArrayList<Double>();
		List<PortfolioMPT> newPortfolioMPTList = new ArrayList<PortfolioMPT>();
		boolean firstYearEnd = true;
		if(pddList!=null && pddList.size()>0){
			//basic information
			dayInterval[0] = pddList.size(); dayInterval[1] = TimePara.yearday; dayInterval[2] = TimePara.yearday * 3; dayInterval[3] = TimePara.yearday * 5; 
			interval[0] = 0; interval[1] = TimePara.workingday; interval[2] = TimePara.workingday * 3; interval[3] = TimePara.workingday * 5;
			year[0] = PortfolioMPT.FROM_STARTDATE_TO_ENDDATE; year[1] = PortfolioMPT.LAST_ONE_YEAR; year[2] = PortfolioMPT.LAST_THREE_YEAR; year[3] = PortfolioMPT.LAST_FIVE_YEAR;
			Long portfolioID = portfolio.getID();
			Long strategyID = portfolio.getMainStrategyID();
			Long userID = portfolio.getUserID();
			String name = portfolio.getName();
			Long benchMarkID = sddList.get(0).getSecurityID();
			pddListMaxIndex = pddList.size()-1;
			for(int i=0;i<4;++i){
				MPTSigmaData sigmaData = new MPTSigmaData();
				sigmaList.add(sigmaData);
			}
			Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pddList.get(pddListMaxIndex).getDate());
			sddList = this.adjustSecurityDailyDataList(pddList, sddList);
			riskFreeList = this.adjustSecurityDailyDataList(pddList, riskFreeList);
			
			PortfolioDailyData pddOriginal = pddList.get(0);
			Date originalStartDate = pddOriginal.getDate();
			for (int i = 0; i < pddList.size(); i++) {
				PortfolioDailyData pdd = pddList.get(i);
				Date curDate = pdd.getDate();
				curPortfolioAmount = pdd.getAmount();
				
				curBenchMarkAmount = sddList.get(i).getAdjClose();
				curRiskFreeAmount = riskFreeList.get(i).getAdjClose();
				
				dateList.add(curDate);
				++interval[0];
				portfolioAmountList.add(curPortfolioAmount);
				riskFreeAmountList.add(curRiskFreeAmount);
				if(i == 0){
					prePortfolioAmount = curPortfolioAmount;
					preBenchMarkAmount = curBenchMarkAmount;
					preRiskFreeAmount = curRiskFreeAmount;
				}
				if (i > 0) {
					portfolioReturn = this.computeIntervalReturn(curPortfolioAmount,prePortfolioAmount);
					benchMarkReturn = this.computeIntervalReturn(curBenchMarkAmount,preBenchMarkAmount);
					riskFreeReturn = this.computeIntervalReturn(curRiskFreeAmount,preRiskFreeAmount);
					
					portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioAmount, prePortfolioAmount);
					riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeAmount, preRiskFreeAmount);
					portfolioReturns.add(portfolioReturn);
					benchMarkReturns.add(benchMarkReturn);
					riskFreeReturns.add(riskFreeReturn);
					
					sigmaList.get(0).addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
					
					prePortfolioAmount = curPortfolioAmount;
					preBenchMarkAmount = curBenchMarkAmount;
					preRiskFreeAmount = curRiskFreeAmount;
					
					
				}
				boolean isHoldingDate = LTIDate.equals(lastHoldingDate, curDate);
				boolean lastOne = (i== pddListMaxIndex);
				if (lastOne || isHoldingDate) {//是不是最后一天或者是最后一天的上个holding date
					double size = (double) portfolioAmountList.size() - 1;
					double preAmount = portfolioAmountList.get(0);
					double curAmount = portfolioAmountList.get((int) size);
					double preRiskFree = riskFreeAmountList.get(0);
					double curRiskFree = riskFreeAmountList.get((int) size);
					PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, interval[0], false, null);
					setAllMPT(pdd, newPdd, year[0]);
				}
				int days = LTIDate.calculateInterval(originalStartDate, curDate);
				//只有最后一天了才要判断是否为cash flow， 是的话用IRR算AR，其它日子用普通算法就可以了, 所以isCashFlow在函数里设为lastOne && isCashFlow
				for(int j=1;j<4;++j){//增量改变数据
					if (days >= dayInterval[j]) {
						MPTSigmaData curSigma = sigmaList.get(j);
						int tmpIndex = this.getAdjustIndex(dateList, curSigma.startIndex + 1, curDate, year[j]);
						
						curSigma.deleteFlag = false;
						if(curSigma.sigmaFirst)
							curSigma.setData(sigmaList.get(0));
						else{
							curSigma.addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
							while(curSigma.startIndex!=tmpIndex){
								lastX = portfolioReturns.get(curSigma.startIndex);
								lastB = benchMarkReturns.get(curSigma.startIndex);
								lastR = riskFreeReturns.get(curSigma.startIndex);
								curSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
							}
							if (curSigma.deleteFlag) {
								List<Double> portfolioYearsAmounts = portfolioAmountList.subList(curSigma.startIndex, i + 1);
								curSigma.setDrawDownData(portfolioYearsAmounts);
							}
							if (lastOne || isHoldingDate) {//如果是最后一天或者最后一个上个holding date，则计算1年、3年、5年
								Date startDate = dateList.get(curSigma.startIndex);
								double size = (double) (i - curSigma.startIndex);
								double preAmount = portfolioAmountList.get(curSigma.startIndex);
								double curAmount = portfolioAmountList.get(i);
								double preRiskFree = riskFreeAmountList.get(curSigma.startIndex);
								double curRiskFree = riskFreeAmountList.get(i);
								PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, curSigma, startDate, curDate, interval[j], false, null);
								setAllMPT(pdd, newPdd, year[j]);
							}
						}
						
					}
				}
				if (LTIDate.isLastNYSETradingDayOfYear(curDate) || LTIDate.isYearEnd(curDate) || lastOne){//是不是第年最后一个交易日，或者最后一天
					PortfolioDailyData newPdd;
					if (firstYearEnd) {
						double size = (double) (portfolioAmountList.size() - 1);
						double preAmount = portfolioAmountList.get(0);
						double curAmount = portfolioAmountList.get((int) size);
						double preRiskFree = riskFreeAmountList.get(0);
						double curRiskFree = riskFreeAmountList.get(i);
						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, -100, false, null);
						firstYearEnd = false;
					}else{
						MPTSigmaData  tmpSigma = new MPTSigmaData();
						tmpSigma.setData(sigmaList.get(1));//use one year sigma
						int tmpIndex = getYearEndIndex(dateList, curDate);
						while(tmpSigma.startIndex < tmpIndex){
							lastX = portfolioReturns.get(tmpSigma.startIndex);
							lastB = benchMarkReturns.get(tmpSigma.startIndex);
							lastR = riskFreeReturns.get(tmpSigma.startIndex);
							tmpSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
						}
						tmpSigma.startIndex = tmpIndex;
						List<Double> lastYearAmounts = portfolioAmountList.subList(tmpSigma.startIndex, i + 1);
						Date startDate = dateList.get(tmpSigma.startIndex);
						tmpSigma.setDrawDownData(lastYearAmounts);
						double size = (double) lastYearAmounts.size() - 1;
						double preAmount = lastYearAmounts.get(0);
						double curAmount = lastYearAmounts.get((int) size);
						double preRiskFree = riskFreeAmountList.get(tmpSigma.startIndex);
						double curRiskFree = riskFreeAmountList.get(i);
						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, tmpSigma, startDate, curDate, -100, false, null);
					}
					int curYear = curDate.getYear() + 1900;
					PortfolioMPT pmpt = new PortfolioMPT();
					pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, false, 0L, curYear, newPdd);
					newPortfolioMPTList.add(pmpt);
				}
			}
			PortfolioDailyData pdd = pddList.get(pddListMaxIndex);
			for(int i=0;i<4;++i){
				PortfolioMPT pmpt =  new PortfolioMPT();
				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, false, 0L, year[i], pdd);
				newPortfolioMPTList.add(pmpt);
			}
		}
		return newPortfolioMPTList;
	}
	
	public List<PortfolioMPT> computePortfolioMPTs(final Portfolio portfolio, Long classID, List<PortfolioDailyData> pddList,List<SecurityDailyData> sddList, List<SecurityDailyData> riskFreeList, List<PortfolioMPT> portfolioMPTList, List<Transaction> transactionList){
		double prePortfolioAmount = 0, curPortfolioAmount = 0, preBenchMarkAmount = 0, curBenchMarkAmount = 0, preRiskFreeAmount = 0, curRiskFreeAmount = 0;
		double portfolioReturn = 0, benchMarkReturn = 0, riskFreeReturn = 0, portfolioLogReturn = 0, riskFreeLogReturn = 0;
		double lastX, lastB, lastR;
		int[] interval = new int[4];
		int[] year = new int[4];
		int[] delayYear = new int[4];
		int[] dayInterval = new int[4]; 
		boolean[] pmptFlag = new boolean[4];
		double[] mptStartAmount = new double[4];
		Date[] mptStartDate = new Date[4];
		int pddListMaxIndex = 0;
		List<MPTSigmaData> sigmaList = new ArrayList<MPTSigmaData>(4);
		List<Double> portfolioAmountList = new ArrayList<Double>();
		List<Double> riskFreeAmountList = new ArrayList<Double>();
		List<Date> dateList = new ArrayList<Date>();
		List<Double> portfolioReturns = new ArrayList<Double>();
		List<Double> benchMarkReturns = new ArrayList<Double>();
		List<Double> riskFreeReturns = new ArrayList<Double>();
		PortfolioMPT[] delayMPTs = new PortfolioMPT[4];
		//List<PortfolioDailyData> newpdds = new ArrayList<PortfolioDailyData>();
		//HashMap<Integer, PortfolioMPT> portfolioMPTMap = new HashMap<Integer, PortfolioMPT>();
		List<PortfolioMPT> newPortfolioMPTList = new ArrayList<PortfolioMPT>();
		boolean isCashFlow = false;
		boolean firstYearEnd = true;
		int transactionIndex = 0;
		if(pddList!=null && pddList.size()>0){
			//basic information
			dayInterval[0] = pddList.size(); dayInterval[1] = TimePara.yearday; dayInterval[2] = TimePara.yearday * 3; dayInterval[3] = TimePara.yearday * 5; 
			interval[0] = 0; interval[1] = TimePara.workingday; interval[2] = TimePara.workingday * 3; interval[3] = TimePara.workingday * 5;
			delayYear[0] = PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE; delayYear[1] = PortfolioMPT.DELAY_LAST_ONE_YEAR; delayYear[2] = PortfolioMPT.DELAY_LAST_THREE_YEAR; delayYear[3] = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
			year[0] = PortfolioMPT.FROM_STARTDATE_TO_ENDDATE; year[1] = PortfolioMPT.LAST_ONE_YEAR; year[2] = PortfolioMPT.LAST_THREE_YEAR; year[3] = PortfolioMPT.LAST_FIVE_YEAR;
			if(classID != null)
				isCashFlow = (classID == 5)? true: false;
			if(portfolioMPTList == null)
				portfolioMPTList = new ArrayList<PortfolioMPT>();
			for(int i=0;i<4;++i){
				MPTSigmaData sigmaData = new MPTSigmaData();
				sigmaList.add(sigmaData);
				pmptFlag[i] = false;
				PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, delayYear[i]);
				if(pmpt == null){
					pmpt = new PortfolioMPT();
					pmpt.setYear(delayYear[i]);
				}
				delayMPTs[i]=pmpt;
			}
			Long portfolioID = pddList.get(0).getPortfolioID();
			Long benchMarkID = sddList.get(0).getSecurityID();
			Long strategyID = portfolio.getMainStrategyID();
			Long userID = portfolio.getUserID();
			String name = portfolio.getName();
			Boolean isModel = portfolio.isModel();
			pddListMaxIndex = pddList.size()-1;
			
			Date lastHoldingDate = LTIDate.getHoldingDateMonthEnd(pddList.get(pddListMaxIndex).getDate());
			sddList = this.adjustSecurityDailyDataList(pddList, sddList);
			riskFreeList = this.adjustSecurityDailyDataList(pddList, riskFreeList);
			
			PortfolioDailyData pddOriginal = pddList.get(0);
			Date originalStartDate = pddOriginal.getDate();
			for (int i = 0; i < pddList.size(); i++) {
				PortfolioDailyData pdd = pddList.get(i);
				Date curDate = pdd.getDate();
				curPortfolioAmount = pdd.getAmount();
				
				if (isCashFlow) {
					if (transactionList != null && transactionList.size() > 0) {
						for (int ti = transactionIndex; ti < transactionList.size(); ++ti) {
							Transaction tr = transactionList.get(ti);
							if(LTIDate.equals(curDate, tr.getDate())){
								String operation = tr.getOperation();
								if(operation.equals(Configuration.WITHDRAW))
									curPortfolioAmount -= tr.getAmount();
								else//Configuration.DEPOSIT
									curPortfolioAmount += tr.getAmount();
							}else{
								transactionIndex = ti;
								break;
							}
						}
					}
				}
				
				curBenchMarkAmount = sddList.get(i).getAdjClose();
				curRiskFreeAmount = riskFreeList.get(i).getAdjClose();
				
				dateList.add(curDate);
				++interval[0];
				portfolioAmountList.add(curPortfolioAmount);
				riskFreeAmountList.add(curRiskFreeAmount);
				if(i == 0){
					prePortfolioAmount = curPortfolioAmount;
					preBenchMarkAmount = curBenchMarkAmount;
					preRiskFreeAmount = curRiskFreeAmount;
				}
				if (i > 0) {
					portfolioReturn = this.computeIntervalReturn(curPortfolioAmount,prePortfolioAmount);
					benchMarkReturn = this.computeIntervalReturn(curBenchMarkAmount,preBenchMarkAmount);
					riskFreeReturn = this.computeIntervalReturn(curRiskFreeAmount,preRiskFreeAmount);
					
					portfolioLogReturn = this.computeLogIntervalReturn(curPortfolioAmount, prePortfolioAmount);
					riskFreeLogReturn = this.computeLogIntervalReturn(curRiskFreeAmount, preRiskFreeAmount);
					portfolioReturns.add(portfolioReturn);
					benchMarkReturns.add(benchMarkReturn);
					riskFreeReturns.add(riskFreeReturn);
					
					sigmaList.get(0).addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
					
					prePortfolioAmount = curPortfolioAmount;
					preBenchMarkAmount = curBenchMarkAmount;
					preRiskFreeAmount = curRiskFreeAmount;
					
					
				}
				List<Transaction> cashFlowTransactionList = null;
				cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(0), transactionIndex);
				boolean isHoldingDate = LTIDate.equals(lastHoldingDate, curDate);
				boolean lastOne = (i== pddListMaxIndex);
				if (lastOne || isHoldingDate) {//是不是最后一天或者是最后一天的上个holding date
					double size = (double) portfolioAmountList.size() - 1;
					double preAmount = portfolioAmountList.get(0);
					double curAmount = portfolioAmountList.get((int) size);
					double preRiskFree = riskFreeAmountList.get(0);
					double curRiskFree = riskFreeAmountList.get((int) size);
					PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, interval[0], isCashFlow, cashFlowTransactionList);
					setAllMPT(pdd, newPdd, year[0]);
					//inception
					if (isHoldingDate) {
						delayMPTs[0] = setAllBasicInfo(delayMPTs[0], portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, delayYear[0], newPdd);
						pmptFlag[0] = true;
					}
				}
				int days = LTIDate.calculateInterval(originalStartDate, curDate);
				//只有最后一天了才要判断是否为cash flow， 是的话用IRR算AR，其它日子用普通算法就可以了, 所以isCashFlow在函数里设为lastOne && isCashFlow
				for(int j=1;j<4;++j){//增量改变数据
					if (days >= dayInterval[j]) {
						MPTSigmaData curSigma = sigmaList.get(j);
						int tmpIndex = this.getAdjustIndex(dateList, curSigma.startIndex + 1, curDate, year[j]);
						
						curSigma.deleteFlag = false;
						if(curSigma.sigmaFirst)
							curSigma.setData(sigmaList.get(0));
						else{
							curSigma.addData(curPortfolioAmount, i, portfolioReturn, benchMarkReturn, riskFreeReturn, portfolioLogReturn, riskFreeLogReturn);
							while(curSigma.startIndex!=tmpIndex){
								lastX = portfolioReturns.get(curSigma.startIndex);
								lastB = benchMarkReturns.get(curSigma.startIndex);
								lastR = riskFreeReturns.get(curSigma.startIndex);
								curSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
							}
							if (curSigma.deleteFlag) {
								List<Double> portfolioYearsAmounts = portfolioAmountList.subList(curSigma.startIndex, i + 1);
								curSigma.setDrawDownData(portfolioYearsAmounts);
							}
							if (lastOne || isHoldingDate) {//如果是最后一天或者最后一个上个holding date，则计算1年、3年、5年
								Date startDate = dateList.get(curSigma.startIndex);
								double size = (double) (i - curSigma.startIndex);
								double preAmount = portfolioAmountList.get(curSigma.startIndex);
								double curAmount = portfolioAmountList.get(i);
								double preRiskFree = riskFreeAmountList.get(curSigma.startIndex);
								double curRiskFree = riskFreeAmountList.get(i);
								cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(curSigma.startIndex), transactionIndex);
								PortfolioDailyData newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, curSigma, startDate, curDate, interval[j], lastOne && isCashFlow, cashFlowTransactionList);
								setAllMPT(pdd, newPdd, year[j]);
								if(isHoldingDate){
									delayMPTs[j] = setAllBasicInfo(delayMPTs[j], portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, delayYear[j], newPdd);
									pmptFlag[j] = true;
								}
							}
						}
						
					}
				}
				if (LTIDate.isLastNYSETradingDayOfYear(curDate) || LTIDate.isYearEnd(curDate) || lastOne){//是不是第年最后一个交易日，或者最后一天
					PortfolioDailyData newPdd;
					if (firstYearEnd) {
						double size = (double) (portfolioAmountList.size() - 1);
						double preAmount = portfolioAmountList.get(0);
						double curAmount = portfolioAmountList.get((int) size);
						double preRiskFree = riskFreeAmountList.get(0);
						double curRiskFree = riskFreeAmountList.get(i);
						cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(0), transactionIndex);
						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, sigmaList.get(0), originalStartDate, curDate, -100, isCashFlow, cashFlowTransactionList);
						firstYearEnd = false;
					}else{
						MPTSigmaData  tmpSigma = new MPTSigmaData();
						tmpSigma.setData(sigmaList.get(1));//use one year sigma
						int tmpIndex = getYearEndIndex(dateList, curDate);
						while(tmpSigma.startIndex < tmpIndex){
							lastX = portfolioReturns.get(tmpSigma.startIndex);
							lastB = benchMarkReturns.get(tmpSigma.startIndex);
							lastR = riskFreeReturns.get(tmpSigma.startIndex);
							tmpSigma.doIncData(lastX, lastB, lastR, curPortfolioAmount, tmpIndex);
						}
						tmpSigma.startIndex = tmpIndex;
						List<Double> lastYearAmounts = portfolioAmountList.subList(tmpSigma.startIndex, i + 1);
						Date startDate = dateList.get(tmpSigma.startIndex);
						tmpSigma.setDrawDownData(lastYearAmounts);
						double size = (double) lastYearAmounts.size() - 1;
						double preAmount = lastYearAmounts.get(0);
						double curAmount = lastYearAmounts.get((int) size);
						double preRiskFree = riskFreeAmountList.get(tmpSigma.startIndex);
						double curRiskFree = riskFreeAmountList.get(i);
						cashFlowTransactionList = this.getCashFlowTransactions(transactionList, dateList.get(tmpSigma.startIndex), transactionIndex);
						newPdd = this.getPortfolioMPTData(preAmount, curAmount, size, preRiskFree, curRiskFree, tmpSigma, startDate, curDate, -100, isCashFlow, cashFlowTransactionList);
					}
					int curYear = curDate.getYear() + 1900;
					PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, curYear);
					if(pmpt == null){
						pmpt = new PortfolioMPT();
					}
					pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, curYear, newPdd);
					newPortfolioMPTList.add(pmpt);
					//portfolioMPTMap.put(curYear, pmpt);
				}
			}
			PortfolioDailyData pdd = pddList.get(pddListMaxIndex);
			for(int i=0;i<4;++i){
				PortfolioMPT pmpt = this.getMPTByYear(portfolioMPTList, year[i]);
				if(pmpt == null)
					pmpt = new PortfolioMPT();
				pmpt = setAllBasicInfo(pmpt, portfolioID, benchMarkID, name, userID, strategyID, isModel, classID, year[i], pdd);
				newPortfolioMPTList.add(pmpt);
				//portfolioMPTMap.put(year[i], pmpt);
			}
			for(int i=0;i<4;++i){
				if(pmptFlag[i])
					newPortfolioMPTList.add(delayMPTs[i]);
					//portfolioMPTMap.put(delayMPTs[i].getYear(), delayMPTs[i]);
			}
		}
		//return portfolioMPTMap;
		return newPortfolioMPTList;
	}
	
	/**
	 * compute IRR(AR) for cash flow
	 * @param endDate
	 * @param preAmount
	 * @param curAmount
	 * @param transactionList
	 * @param interval
	 * @return
	 */
	public Double computeIRR(Date startDate, Date endDate, double preAmount, double curAmount, List<Transaction> transactionList) {

		List<Double> cashList = new ArrayList<Double>();

		List<Integer> intervalList = new ArrayList<Integer>();

		int interval  = LTIDate.calculateIntervalIgnoreHolidDay(startDate, endDate);

		cashList.add(-preAmount);

		intervalList.add(interval);

		for (int i = 0; i < transactionList.size(); i++) {
			
			Transaction tr = transactionList.get(i);

			double amount = tr.getAmount();

			Date curDate = tr.getDate();
			
			interval = LTIDate.calculateIntervalIgnoreHolidDay(curDate, endDate);
			
			intervalList.add(interval);
			
			if (tr.getOperation().equals(Configuration.DEPOSIT))
				cashList.add(-amount);
			else //Configuration.WITHDRAW
				cashList.add(amount);
	
		}

		/*
		 * long t1 = System.currentTimeMillis(); double irr =
		 * LTIIRR.getInstance().getIRR(dlist,ilist, lastAmount); long t2 =
		 * System.currentTimeMillis(); System.out.println((t2-t1)+" "+irr);
		 */

		double irr = this.solveFunction(cashList, intervalList, curAmount);
		
		irr = Math.pow(irr, TimePara.workingday) - 1;

		Double p = new Double(irr);
		
		if (p.isInfinite())
			return 0.0;

		return irr;
	}
	
	/**
	 * compute the ar for cash flow
	 * @param A the amount list, A[0] is the amount for the max interval B[0]
	 * @param B the interval list, B[0] is the max interval between startdate and last date
	 * @param amount
	 * @return
	 */
	public double solveFunction(List<Double> A, List<Integer> B, double amount) {
		double result = 0;

		//B[0] is the max interval, so that c should be c[0]..c[interval], and its length is B[0]+1
		double[] C = new double[B.get(0) + 1];
		
		for (int i = 0; i < C.length; ++i) {
			C[i] = 0;
		}
		
		for (int i = 0; i < B.size(); i++) {
			C[B.get(i)]+= A.get(i);
		}
		
		C[0] += amount;//C[0] is stand for that interval is 0, that it's the last date amount
		
		PolynomialFunction f = new PolynomialFunction(C);

		BisectionSolver bs = new BisectionSolver(f);
		
		try {
			result = bs.solve(0.8, 1.05);
		} catch (MaxIterationsExceededException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		/*
		 * System.out.println("==========="); System.out.println(result);
		 * System.out.println(f.value(result)); double p = this.solveFunction(A,
		 * B, amount); System.out.println(p); System.out.println(f.value(p));
		 * System.out.println("===========");
		 */
		return result;
	}
/**
 * get the portfolio MPT by year
 * @param pmptList
 * @param year
 * @return
 */
public PortfolioMPT getMPTByYear(List<PortfolioMPT> pmptList, int year){
	if(pmptList!=null && pmptList.size()>0){
		for(int i=0;i<pmptList.size();++i)
			if(pmptList.get(i).getYear() == year)
				return pmptList.get(i);
	}
	return null;
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
	private int getAdjustIndex(List<Date> dateList, int index, Date curDate, int year){
		int dateSize = dateList.size();
		Date pre = LTIDate.getNewNYSEYear(curDate, year);
		Date lastDate = dateList.get(index);
		if(LTIDate.equals(pre, lastDate))
			return index;
		if(LTIDate.after(pre, lastDate)){
			int tmpIndex = index + 1;
			while(tmpIndex < dateSize){
				Date tmpDate = dateList.get(tmpIndex);
				if(LTIDate.equals(pre, tmpDate))
					return tmpIndex;
				if(LTIDate.after(tmpDate, pre))
					return tmpIndex-1;
				++tmpIndex;
			}
			return dateSize-1;
		}else{
			int tmpIndex = index - 1;
			while(tmpIndex > -1){
				Date tmpDate = dateList.get(tmpIndex);
				if(LTIDate.equals(pre, tmpDate))
					return tmpIndex;
				if(LTIDate.before(tmpDate, pre))
					return tmpIndex+1;
				--tmpIndex;
			}
			return 0;
		}
	}
	private PortfolioMPT setAllBasicInfo(PortfolioMPT pmpt, Long portfolioID,
			Long benchMarkID, String name, Long userID, Long strategyID,
			Boolean isModel, Long classID, Integer year,
			PortfolioDailyData newPdd) {

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

	private void setAllMPT(PortfolioDailyData oldPdd, PortfolioDailyData newPdd, Integer year) {
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
		} else if (year == -1) {
			oldPdd.setAlpha1(newPdd.getAlpha());
			oldPdd.setBeta1(newPdd.getBeta());
			oldPdd.setRSquared1(newPdd.getRSquared());
			oldPdd.setStandardDeviation1(newPdd.getStandardDeviation());
			oldPdd.setSharpeRatio1(newPdd.getSharpeRatio());
			oldPdd.setTreynorRatio1(newPdd.getTreynorRatio());
			oldPdd.setDrawDown1(newPdd.getDrawDown());
			oldPdd.setAR1(newPdd.getAR());
			oldPdd.setSortinoRatio1(newPdd.getSortinoRatio());
		} else if (year == -3) {
			oldPdd.setAlpha3(newPdd.getAlpha());
			oldPdd.setBeta3(newPdd.getBeta());
			oldPdd.setRSquared3(newPdd.getRSquared());
			oldPdd.setStandardDeviation3(newPdd.getStandardDeviation());
			oldPdd.setSharpeRatio3(newPdd.getSharpeRatio());
			oldPdd.setTreynorRatio3(newPdd.getTreynorRatio());
			oldPdd.setDrawDown3(newPdd.getDrawDown());
			oldPdd.setAR3(newPdd.getAR());
			oldPdd.setSortinoRatio3(newPdd.getSortinoRatio());
		} else if (year == -5) {
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
	}
	
	public PortfolioDailyData getPortfolioMPTData(double preAmount, double curAmount, double size, 
			double preRiskFree, double curRiskFree, MPTSigmaData msd, Date startDate, 
			Date curDate, int interval, boolean isCashFlow, List<Transaction> transactionList){
		
		PortfolioDailyData pdd = new PortfolioDailyData();

		double beta = (msd.sigmaXB * size - (msd.sigmaX * msd.sigmaB));
		beta = beta / (size * msd.sigmaBB - msd.sigmaB * msd.sigmaB);

		double annualizedRiskFree = this.computeIntervalReturn(curRiskFree, preRiskFree);
		if (interval != -100)
			annualizedRiskFree = Math.pow(curRiskFree / preRiskFree, TimePara.workingday / ((size + 1) * 1.0)) - 1;

		double averageRiskFree = msd.sigmaR / (interval);

		double averagePortfolioReturn = msd.sigmaX / size - averageRiskFree;
		double averageBenchmarkReturn = msd.sigmaB / size - averageRiskFree;

		double alpha = this.computeAlpha(beta, averagePortfolioReturn, averageBenchmarkReturn);

		double standardDeviation = (msd.sigmaLogXX - msd.sigmaLogX * msd.sigmaLogX / size)/(size - 1);
		standardDeviation = Math.sqrt(standardDeviation);
		standardDeviation *= Math.pow(TimePara.workingday, 0.5);

		double RSquared = 0;
		double co = (msd.sigmaXB - msd.sigmaX * msd.sigmaB / size) / (size - 1);
		co = co * co;
		double stdA = (msd.sigmaXX - msd.sigmaX * msd.sigmaX / size) / (size - 1);
		double stdB = (msd.sigmaBB - msd.sigmaB * msd.sigmaB / size) / (size - 1);
		RSquared = co / (stdA * stdB);

		double AR=0;
		/** if isCashFlow is true, use computeIRR to get AR* */
		if (isCashFlow) {
			AR = this.computeIRR(startDate, curDate, preAmount, curAmount, transactionList);
		} else {
			AR = this.computeIntervalReturn(curAmount, preAmount);
			if (interval != -100) 
				AR = Math.pow(curAmount / preAmount, TimePara.workingday / (interval * 1.0)) - 1;
		}

		double sharpeRatio = this.computeRatio(AR, annualizedRiskFree,
				standardDeviation);

		double treynorRatio = this.computeRatio(AR, annualizedRiskFree, beta);

		double tmpHalfDeviation = Math.sqrt(msd.sigmaSR / (size - 1));
		tmpHalfDeviation *= Math.pow(TimePara.workingday, 0.5);

		double sortinoRatio = this.computeRatio(AR, annualizedRiskFree,tmpHalfDeviation);// averagePortfolioReturn/tmpHalfDiviation;

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
		if (msd.maxDrawDown < LTINumber.INF)
			pdd.setDrawDown(msd.maxDrawDown);
		if (sortinoRatio < LTINumber.INF)
			pdd.setSortinoRatio(sortinoRatio);
		return pdd;
	}
	public SecurityMPT calculateOneSecurityMPT_Incremental(SecurityMPT securityMPT,double firstAmount,double currentAmount, 
			double size, double firstRiskFree,double currentRiskFree, double sigmaXB,
			double sigmaX, double sigmaXX, double sigmaB, double sigmaBB,double sigmaR, 
			double sigmaLogXX, double sigmaLogX,int interval,int year)
	{
		if(securityMPT==null)
			securityMPT = new SecurityMPT();
		/**beta**/
		double beta=0;
		double beta_up = (size * sigmaXB - sigmaX * sigmaB);
		double beta_down = size * sigmaBB - sigmaB * sigmaB;
		if(beta_down!=0)
			beta = beta_up / beta_down;
		/**annualizedRiskFree***/
		double annualizedRiskFree = this.computeIntervalReturn(currentRiskFree,firstRiskFree);
		if (interval != 0)
			annualizedRiskFree = Math.pow(currentRiskFree / firstRiskFree,TimePara.workingday /(interval * 1.0)) - 1;
		/**averagerRiskFree***/
		double averageRiskFree = sigmaR / (interval);
		/**averageSecurityReturn & averageBenchmarkReturn**/
		double averageSecurityReturn = sigmaX / size - averageRiskFree;
		double averageBenchmarkReturn = sigmaB / size - averageRiskFree;
		/**alpha**/
		double alpha = this.computeAlpha(beta, averageSecurityReturn,averageBenchmarkReturn);
		/**RSquared**/
		double correlation = (sigmaXB -sigmaX * sigmaB / size) / (size - 1);
		double securitySquareStd = (sigmaXX - sigmaX * sigmaX / size) / (size - 1);
		double benchMarkSquareStd = (sigmaBB - sigmaB * sigmaB/ size) / (size - 1);
		double RSquared = correlation * correlation /(securitySquareStd * benchMarkSquareStd);
		/**standardDeviation   TimeUnit.DAILY***/
		double standardDeviation = (sigmaLogXX - sigmaLogX * sigmaLogX / size)/(size - 1);
		standardDeviation = Math.sqrt(standardDeviation);
		standardDeviation *= Math.pow(TimePara.workingday, 0.5);
		
		/***totalReturn**/
		double totalReturn = computeIntervalReturn(currentAmount,firstAmount);
		/***AR**/
		double AR = Math.pow(currentAmount / firstAmount, TimePara.workingday / (interval*1.0)) - 1;
		/***sharpeRatio**/
		double sharpeRatio = computeRatio(AR, annualizedRiskFree, standardDeviation);
		/***treynorRatio**/
		double treynorRatio = computeRatio(AR, annualizedRiskFree, beta);
		if(year>0)
			AR = totalReturn;
		/***drawDown calculated outside for incremental data saved**/
		
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseFormulaUtil bfu = new BaseFormulaUtil();
		Double curPrice = 104.508815605255;
		Double prePrice = 82.1968826101872;
		int interval = TimePara.workingday * 5;
		Double result = bfu.computeAnnualizedReturn(curPrice, prePrice, interval);
		System.out.println(result);
		
	}

}
