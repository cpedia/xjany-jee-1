package com.lti.service;

import java.util.*;

import com.lti.Exception.Security.NoPriceException;
//import com.lti.service.bo.FundAlert;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.SecurityMPTIncData;
//import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.type.MPT;
import com.lti.type.SourceType;
import com.lti.type.TimeUnit;
import com.lti.type.finance.HoldingInf;

public interface BaseFormulaManager {

	public double computeAverage(List<Double> source)
					throws ArithmeticException;

	public double computeIntervalReturn(double current,double last)
					throws ArithmeticException;
	
	public double computeLogIntervalReturn(double current,double last)
					throws ArithmeticException;
	
	public double computeSecurityWeight(double currentPrice,double shares,double totalAmount)
					throws ArithmeticException;
	
	public double computeStandardDeviation(List<Double> values)
					throws ArithmeticException;
	
	double computeAnnulizedStandardDeviation(List<Double> values, TimeUnit tu)
					throws ArithmeticException;
	
	public double computeCovariance(List<Double> valsOne,List<Double> valsTwo)
					throws ArithmeticException;
	
	public double computeCorrelationCoefficient(List<Double> valsOne,List<Double> valsTwo)
					throws ArithmeticException;
	
	public double computeCurrentPercetage(double subAmount,double totalAmount)
					throws ArithmeticException;
	
	public double computeAnnualizedReturn(double currentPrice,double initialPrice,int days)
					throws ArithmeticException;
	
	public double computeAlpha(double beta,double portfolioReturn,double benchmarkReturn);
	
	public double computeBeta(List<Double> portfolioReturns,List<Double> benchmarkReturns)
					throws ArithmeticException;
	
	public double computeRatio(double portfolioReturn,double riskFree,double portfolioDeviatio)
					throws ArithmeticException;

	public double computeRiskFromYearly(double yearlyValue,int n)
					throws ArithmeticException;

	public double computeRSI(double returnA,double returnB)
					throws ArithmeticException;
	

	public double computeSMA(List<Double> closePrices)
					throws ArithmeticException;
	

	public double computeEMA(double closePrice,int days,double lastEMA)
					throws ArithmeticException;
	

	public double computeIntervalReturnToAnnulized(double intervalReturn,double factor)
					throws ArithmeticException;


	public double computeTRIN(double Nr,double Nd,double Vr,double Vd)
					throws ArithmeticException;

	
	public List<Double> computePeriodReturn(List<Double> source);

	public double getRiskFree(TimeUnit unit);
	
	public double getRiskFree(Date startDate, Date endDate) throws NoPriceException;
	
	public double getAnnualizedRiskFree(Date startDate, Date endDate) throws NoPriceException;
	
	public double getAverageRiskFree2(Date startDate, Date endDate) throws NoPriceException;
	
	public double getAverageRiskFree(Date startDate, Date endDate);

	List<Double> getTimeUnitReturns(List<Double> dailydataList);

	double computeDrawDown(List<Double> portAmounts);

	double computeTotalReturn(List<Double> amounts);
	
	double computeAnnualizedReturn(List<Double> portfolioList, int interval);
	
	/**
	 * @throws NoPriceException **********************************************************************************************************/
	
	public List<Double> getReturns(Date startDate, Date endDate, TimeUnit tu,long id, SourceType st,boolean nav) throws NoPriceException;
	
	public List<Double> getReturns(List<SecurityDailyData> securityList);
	
	public double computeBeta(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	
	public double computeAlpha(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav, String bench) throws NoPriceException;
	public double computeAlpha(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	public double computeAlpha(Date startDate, Date endDate, TimeUnit tu, SourceType st, long id, Long benchMarkID, boolean nav) throws NoPriceException;
	
	public double computeRSquared(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	
	public double computeTreynorRatio(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	
	public double computeStandardDeviation(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	
	public double computeSharpeRatio(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	
	public double computeDrawDown(Date startDate, Date endDate, TimeUnit tu, SourceType st,long id,boolean nav) throws NoPriceException;
	
	public double computeAnnualizedReturn(Date startDate, Date endDate, SourceType st,long id,int flag,boolean nav);
	
	public double computeAnnualizedReturn(Double curPrice, Double prePrice, int interval);
	
	public double computeRSI(Date startDate, Date endDate, SourceType st,long id,boolean nav);
		
	public double computeSMA(Date endDate, int count, TimeUnit tu, SourceType st, long id,boolean nav) throws NoPriceException;
	
	public double computeEMA(Date startDate,Date endDate,int count, TimeUnit tu, SourceType st, long id,boolean nav) throws NoPriceException;
	
	public double computeSingleEMA(Date endDate,int count,TimeUnit tu,SourceType st, long id,boolean nav) throws NoPriceException;
	
	public double computeMACD(Date startDate,Date endDate,SourceType st, TimeUnit tu,long id,boolean nav) throws NoPriceException;
	
	public double computeSingleMACE(Date endDate,int count,TimeUnit tu,SourceType st,long id,boolean nav);
	
	public double computeDEA(Date startDate,Date endDate,SourceType st, TimeUnit tu,long id,boolean nav) throws NoPriceException;
	
	public double computeSingleDEA(Date endDate,int count,TimeUnit tu,SourceType st,long id,boolean nav);
	
	public double computeTRIN();
	
	public MPT computeMPTS(Date startDate, Date endDate, TimeUnit tu,SourceType st ,long id, boolean yearly,boolean nav) throws NoPriceException;
	
	public List<MPT> computeEveryYearMPTs(TimeUnit tu, SourceType st, long id,boolean nav) throws NoPriceException;
	
	/************************************************************************************************************/
	void computeMPTs(final Portfolio portfolio) throws NoPriceException;
	@Deprecated
	List<PortfolioDailyData> getNewPortfolioDailyData(final Portfolio portfolio,List<PortfolioDailyData> pdds,List<SecurityDailyData> sdds) throws NoPriceException;

	void computeMPTs(final Portfolio portfolio,List<PortfolioDailyData> pdds) throws NoPriceException;
	@Deprecated
	Double computeIRR(final Portfolio portfolio,Date startDate,Date endDate,double originalAmount, double lastAmount);
	@Deprecated
	double computeIRR_OldVersion(final Portfolio portfolio,Date startDate,Date endDate,double originalAmount, double lastAmount);
		
	/*
	public List<PortfolioDailyData> updatePortfolioDailyData(List<PortfolioDailyData> pList, long portfolioID, long benchMarkID) throws NoPriceException;

	
	public PortfolioDailyData getOneYearMPT(List<Double> portfolioReturns,List<Double> benchmarkReturns,List<Double> portfolioAmounts,Date totalStartDate,Date currentDate,int interval) throws NoPriceException;

	/************************************************************************************************************/
	@Deprecated
	public List<PortfolioDailyData> updatePortfolioDailyData(final Portfolio portfolio,List<PortfolioDailyData> pList,long benchMarkID) throws NoPriceException;


	public PortfolioDailyData getOneYearMPT(double firstAmount,double currentAmount,double size,double firstRiskFree,double lastRiskFree
			,double drawDown,double sigmaXB,double sigmaX,double sigmaXX,double sigmaB,double sigmaBB
			,double sigmaR,double sigmaSR,double sigmaLogXX,double sigmaLogX,Date totalStartDate
			,Date currentDate,int interval,boolean isCashFlow,final Portfolio portfolio
			, double totalOriginalAmount) throws NoPriceException;
	/***************************************************************************************************************/
	public void getConfidenceValue(long portfolioID,TimeUnit tu);
	
	public void getConfidneceValue(long portfolioID,long strategyID,String ruleName,List<Double> amounts);
	
	public int getSubListIndex(List<Date> dateList, Date today, int year);
	public int getYearEndIndex(List<Date> dateList, Date today);
	
	/***************************************************************************************************************/
	public double getFunction2(List<Double> A,List<Integer> B,double amount,double x);
	public double getFunction(List<Double> A,List<Integer> B,double amount,double x);
	
	/**************************************************************************************/
	//public List<Double> addToPriorityList(List<Double> list, double value);
	//public List<Double> deleteFromPriorityList(List<Double> list, double value);
	/*public FundAlert getFundAlert(List<Double> returns,List<Double> benchs,List<Date> dateList,double DR,double DBR,final Security security, TimeUnit tu, int count,Date today);
	public double getSkew(List<Double> values);
	public double getKurtosis(List<Double> values);*/
	
	public List<Double> getAmounts(Date startDate, Date endDate, TimeUnit tu,long id, SourceType st,boolean nav)throws NoPriceException;
	
	public double computeHalfDeviation(List<Double> values,List<Double> benchs,TimeUnit tu);
	public double getSortinoRatio(long securityID,Date startDate,Date endDate,TimeUnit tu,SourceType st);
	
	public double computeEPSGrowth(double eps1,double eps2,int count);
	
	
	
	public void getOneSecurityMPT_Incremental(final Security security,final Security benchmark, List<SecurityMPTIncData> securityMPTIncDataList,Date today) throws NoPriceException;
	
	public List<SecurityMPT> calculateOneSecurityINCMPT(final Security security,final Security benchmark, List<SecurityMPTIncData> securityMPTIncDataList,Date today) throws NoPriceException;
	
	public SecurityMPT calculateOneSecurityMPT_Incremental(SecurityMPT securityMPT,double firstAmount,double currentAmount, 
			double size, double firstRiskFree,double currentRiskFree, double sigmaXB,
			double sigmaX, double sigmaXX, double sigmaB, double sigmaBB,double sigmaR, 
			double sigmaLogXX, double sigmaLogX,int interval,int year);
	@Deprecated
	List<PortfolioMPT> computeMPTs(Portfolio portfolio, HoldingInf holding, List<PortfolioDailyData> pdds) throws NoPriceException;
	
}
