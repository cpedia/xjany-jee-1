package com.lti.MutualFundTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.rosuda.JRI.REXP;

import com.lti.Exception.Security.NoPriceException;
import com.lti.MutualFundTool.Exception.InvalidParametersException;
import com.lti.cache.LTICache;
import com.lti.service.BaseFormulaManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.impl.SecurityManagerImplWithCache;
import com.lti.system.ContextHolder;
import com.lti.type.SourceType;
import com.lti.type.TimeUnit;
import com.lti.util.LTIDate;
import com.lti.util.LTIRInterface;
import com.lti.util.StringUtil;

import org.apache.commons.math.stat.regression.*;

/*
===Backgrand of RAA===
Author:chaos
Helper: ZhiBin
Date: 2009-4-18
version:1.0
Tool:use quadprog in R;
Document:http://mirrors.geoexpat.com/cran/web/packages/quadprog/quadprog.pdf

Target: Minimize following function
	min('b + 1/2b'Db) with the constraints A' b >= b0.

Analyst:
Suppose that the index is X (more than one), and the target fund is Y; 
In fact,we calculate the weights(B) of Y to minimize the  residual error of Y and X;
so the residual error of Y and X is:
  (Y-XB)'(Y-XB)
= (Y'-B'X')(Y-XB)
= Y'Y-B'X'Y-Y'XB+B'X'XB

because Y'Y is constants,

  min(Y'Y-B'X'Y-Y'XB+B'X'XB) 
= min(C-B'X'Y-Y'XB+B'X'XB)
= min(-B'X'Y-Y'XB+B'X'XB)
= min(-2Y'XB+B'X'XB)

In order to fit our target,

  min(-2Y'XB+B'X'XB)
= min(1/2*(-Y'XB+1/2B'X'XB))
= min(-Y'XB+1/2B'X'XB)

compare with our target, we can find out that

Y'X = d, it's dvec in quadprog
X'X = D, it's Dmat in quadprog

A'b >= b0 is the constraints

we can set it into Amat and bvec. 

the result is B means the weights of each X;
*/
public class WeightEstimate {
	static Log log = LogFactory.getLog(WeightEstimate.class);
	private static final Date NULL = null;
	private PortfolioManager portfolioManager;
	
	private double[][] weights;
	
	private double[] upper;
	private double[] lower;
	
	private double[] cashList;
	

	public double[] getCashList() {
		return cashList;
	}

	public void setCashList(double[] cashList) {
		this.cashList = cashList;
	}

	public double[] getUpper() {
		return upper;
	}

	public void setUpper(double[] upper) {
		this.upper = upper;
	}

	public double[] getLower() {
		return lower;
	}

	public void setLower(double[] lower) {
		this.lower = lower;
	}

	public WeightEstimate() {
		super();

		portfolioManager = ContextHolder.getPortfolioManager();
		this.cashList = null;
	}

	public double[][] MatrixMultiply(double[][] A, double[][] B)throws Exception {
		int i;
		int j;
		int k;
		int m = A.length;
		int n = A[0].length;
		int p = B.length;
		int q = B[0].length;
		double[][] C = new double[m][q];

		if (p != n) {
			throw new InvalidParametersException("The dimention of two Matrix should be mateched!");
		}

		for (i = 0; i < m; i++) {

			for (j = 0; j < q; j++) {
				for (k = 0; k < n; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;
	}

	public double[][] Transpose(double[][] A) throws Exception {

		int m = A.length;
		int n = A[0].length;
		double[][] B = new double[n][m];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {

				B[j][i] = A[i][j];
			}
		}
		return B;
	}

	//peterson method
	public double ComputeR(double[][] A, double[][] B) {
		int T;
		double R1 = 0;

		double AveA = 0;
		double SumR = 0;
		double SumA = 0;
		T = A[0].length;
		
		for (int i = 0; i < T; i++) {

			AveA += A[0][i];
	
		}

		AveA = AveA / T;
	
	
		for (int j = 0; j < T; j++) {
			SumR += (A[0][j] - B[0][j]) * (A[0][j] - B[0][j]);
			SumA += (A[0][j]-AveA) * (A[0][j]-AveA);
		}

		R1 = 1 - (SumR / SumA);
		return R1;

	}
	
	//normal method for R
	public double ComputeR2(double[][] A, double[][] B)
	{
		int T;
		double R1 = 0;

		double AveA = 0;
		double AveB = 0;
		double SumA = 0;
 		double SumB=0;
 
		T = A[0].length;
		
		for (int i = 0; i < T; i++) {

			AveA += A[0][i];
    			AveB += B[0][i];
	
		}

		AveA = AveA / T;
		AveB = AveB / T;
	
	
		for (int j = 0; j < T; j++) {
			SumB += (B[0][j]-AveB) * (B[0][j]-AveB);
			SumA += (A[0][j]-AveA) * (A[0][j]-AveA);
		}

		R1 =SumB/ SumA;
		return R1;

	}

	public Double[] newRAA(int interval, Date date, TimeUnit tu,Security mutualFund, String[] factors,boolean isWLSOrOLS)throws Exception{
		SecurityManagerImplWithCache s = new SecurityManagerImplWithCache();
		try {
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			SecurityManager securityManager=s;
			long mutualFundID = mutualFund.getID();
			Date[] CDate=new Date[interval+1];
			for (int i = 0; i < interval + 1; i++) {

				if (tu == TimeUnit.DAILY) {
					CDate[i] = LTIDate.getNewNYSETradingDay(date, -interval - 1 + i);
				} else if (tu == TimeUnit.WEEKLY) {
					CDate[i] = LTIDate.getNewNYSEWeek(date, -interval - 1 + i);
				} else if (tu == TimeUnit.MONTHLY) {
					CDate[i] = LTIDate.getNewNYSEMonth(date, -interval - 1 + i);
				} else if (tu == TimeUnit.QUARTERLY) {
					CDate[i] = LTIDate.getNewNYSEQuarter(date, -interval - 1 + i);
				} else if (tu == TimeUnit.YEARLY) {
					CDate[i] = LTIDate.getNewNYSEYear(date, -interval - 1 + i);
				} else
					return null;
			}
			double[] mfAdjPrice=new double[interval+1];
			for (int i = 0; i < interval+1; i++) {
				try {
					mfAdjPrice[i] = securityManager.getAdjPrice(mutualFundID, CDate[i]);
				} catch (NoPriceException e) {
				}
			}
			double[][] indexAdjPrice=new double[factors.length][interval+1];
			for(int i=0;i<factors.length;i++){
				long securityID = securityManager.getBySymbol(factors[i]).getID();
				for(int j=0;j<interval+1;j++){
					indexAdjPrice[i][j]=securityManager.getAdjPrice(securityID, CDate[j]);
				}
			}
			return this.RAA_QP(interval, mfAdjPrice, indexAdjPrice, isWLSOrOLS, true);
		} catch (Exception e) {
			throw e;
		}finally{
			if(s!=null){
				LTICache c=s.getDailyDataCache();
				if(c!=null){
					c.removeAll();
					c=null;
					s.setDailyDataCache(null);
					System.gc();
				}
			}
		}
	}
	
	private Double[][] calculateCovariances(List<List<Double>> returns)
	{		
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		int size = returns.size();
		Double[][] convariance = new Double[size][size];
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<=i;j++)
			{
				double cov = baseFormulaManager.computeCovariance(returns.get(i), returns.get(j));
				convariance[i][j] = cov;
				convariance[j][i] = cov;
			}
		}		
		return convariance;
	}
	/**
	 * @author CCD
	 * @param indexFunds
	 * @param RAF
	 * @param month
	 * @param SMAorEMA
	 * @param constraints
	 * @param endDate
	 * @return
	 * @throws Exception
	 * note: sum =1, lower =0, upper =1, constraints are all use >= as default
	 * perform MVO constraints base on R with 
	 */
	public Double[] MVOwithLinearLimit (String[] indexFunds, double RAF, int month, String SMAorEMA, double[][] constraints, Date endDate) throws Exception{
		SecurityManagerImplWithCache s = new SecurityManagerImplWithCache();
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		try {
			List<List<Double>> returns = new ArrayList<List<Double>>();
			Double[] expectedExcessReturn = new Double[indexFunds.length];
			Double[][] convariance = new Double[indexFunds.length][indexFunds.length];
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			SecurityManager securityManager=s;
			//
			Date startDate = LTIDate.getNewNYSEMonth(endDate, -month);
			/**************************** first calculate convariance no matter SMA or EMA *************************************************/
			double riskFreeReturn = 0;
			List<Double> riskReturnList = null;
			long cashID = securityManager.get("CASH").getID();
			for(int i=0;i<indexFunds.length;++i){
				String symbol = indexFunds[i];
				Security security = securityManager.getBySymbol(symbol);
				List<Double> tmpReturnList = null;
				if(security!=null){
					tmpReturnList = baseFormulaManager.getReturns(startDate, endDate, TimeUnit.DAILY, security.getID(), SourceType.SECURITY_RETURN, false);
				}
				returns.add(tmpReturnList);
			}
			convariance = calculateCovariances(returns);
			if(SMAorEMA.equalsIgnoreCase("SMA")){
				try {
					riskReturnList = baseFormulaManager.getReturns(startDate, endDate, TimeUnit.DAILY, cashID, SourceType.SECURITY_RETURN, false);
				} catch (NoPriceException e1) {
					System.out.println(StringUtil.getStackTraceString(e1));
				}
				if(riskReturnList!=null && riskReturnList.size()!=0)
					riskFreeReturn = baseFormulaManager.computeAverage(riskReturnList);
				for(int k=0;k<returns.size();++k){
					List<Double> tmpReturnList = returns.get(k);
					double tmpAverage=0;
					if(tmpReturnList!=null && tmpReturnList.size()>0)
						tmpAverage= baseFormulaManager.computeAverage(tmpReturnList);
					expectedExcessReturn[k] = tmpAverage-riskFreeReturn;
				}
			}
			else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				Date curDate = LTIDate.clearHMSM(cal.getTime());
				double cashCurr,cashPre;
				double[] curPrice = new double[indexFunds.length];
				double[] prePrice = new double[indexFunds.length];
				double[] fundDaily= new double[indexFunds.length];
				long[] securityID = new long[indexFunds.length];
				cashCurr = securityManager.getAdjPrice(cashID,curDate);
				for(int k=0;k<indexFunds.length;++k){
					securityID[k] = securityManager.getBySymbol(indexFunds[k]).getID();
					curPrice[k] = securityManager.getAdjPrice(securityID[k], curDate);
					fundDaily[k] = 0;
				}
				double cashDaily=0;
				for(int j=1,m=month;j<=month;++j,--m){
					Date preDate = LTIDate.getNewNYSEMonth(curDate, -1);
					cashPre = 0;
					cashPre = securityManager.getAdjPrice(cashID,preDate);
					if(cashPre!=0){
						cashDaily += m*(cashCurr-cashPre)/cashPre;
					}
					cashCurr = cashPre;
					for(int k=0;k<indexFunds.length;++k){
						prePrice[k] = 0;
						prePrice[k] = securityManager.getAdjPrice(securityID[k], preDate);
						double tmpReturn = 0;
						if(prePrice[k]!=0){
							tmpReturn = (curPrice[k]-prePrice[k])/prePrice[k];
						}
						fundDaily[k] += tmpReturn * m;
						curPrice[k] = prePrice[k];
					}
					curDate = preDate;
				}
				for(int k=0;k<indexFunds.length;++k){
					expectedExcessReturn[k] = (fundDaily[k] - cashDaily) / 11 / month / (month + 1);
				}
			}
			return MVO_RAA_WITH_CONSTRAINTS(expectedExcessReturn, convariance, constraints, RAF);
		} catch (Exception e) {
			throw e;
		}finally{
			if(s!=null){
				LTICache c=s.getDailyDataCache();
				if(c!=null){
					c.removeAll();
					c=null;
					s.setDailyDataCache(null);
					System.gc();
				}
			}
		}
	}
	public Double[] RAA(int interval, Date date,TimeUnit tu, String MF,String[] index,boolean allowShort,boolean withConstraint) throws Exception{
		SecurityManagerImplWithCache s = new SecurityManagerImplWithCache();
		try {
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			SecurityManager securityManager=s;
			
			Security MutualFund=securityManager.get(MF);
			if(MF==null)throw new InvalidParametersException("MF parameter is invalid!");
			Date[] CDate=new Date[interval+1];
			for (int i = 0; i < interval + 1; i++) {

				if (tu == TimeUnit.DAILY) {
					CDate[i] = LTIDate.getNewNYSETradingDay(date, -interval - 1 + i);
				} else if (tu == TimeUnit.WEEKLY) {
					CDate[i] = LTIDate.getNewNYSEWeek(date, -interval - 1 + i);
				} else if (tu == TimeUnit.MONTHLY) {
					CDate[i] = LTIDate.getNewNYSEMonth(date, -interval - 1 + i);
				} else if (tu == TimeUnit.QUARTERLY) {
					CDate[i] = LTIDate.getNewNYSEQuarter(date, -interval - 1 + i);
				} else if (tu == TimeUnit.YEARLY) {
					CDate[i] = LTIDate.getNewNYSEYear(date, -interval - 1 + i);
				} else
					return null;
			}
			double[] mfAdjPrice=new double[interval+1];
			for (int i = 0; i < interval+1; i++) {
				try {
					mfAdjPrice[i] = securityManager.getAdjPrice(MutualFund.getID(), CDate[i]);
				} catch (NoPriceException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			
			double[][] indexAdjPrice=new double[index.length][interval+1];
			for(int i=0;i<indexAdjPrice.length;i++){
				Security cIndex=securityManager.get(index[i]);
				if(cIndex==null)throw new InvalidParametersException("MF parameter is invalid!");
				for(int j=0;j<interval+1;j++){
					indexAdjPrice[i][j]=securityManager.getAdjPrice(cIndex.getID(), CDate[j]);
				}
			}
			
			if(withConstraint)
			{
				if(upper!=null && lower!=null)return this.RAA_WithLimit(interval,  mfAdjPrice, indexAdjPrice);
				else return this.RAA(interval,  mfAdjPrice, indexAdjPrice,allowShort);
			}
			else return this.RAA_NoConstraint(interval,  mfAdjPrice, indexAdjPrice);
						
		} catch (Exception e) {
			throw e;
		}finally{
			if(s!=null){
				LTICache c=s.getDailyDataCache();
				if(c!=null){
					c.removeAll();
					c=null;
					s.setDailyDataCache(null);
					System.gc();
				}
			}
		}
		
	}
	
	
	public Double[] RAA(int interval, double[] MF,	double[][] index,boolean allowShort) throws Exception{
		if(interval<=0||MF==null||index==null){
			throw new InvalidParametersException("Interval or MF or index parameter is invalid!");
		}
		if(MF.length-1!=interval){
			throw new InvalidParametersException("MF's length does not match invertal!");
		}
		for(int i=0;i<index.length;i++){
			if(index[i]==null){
				throw new InvalidParametersException("Index parameter is invalid!");
			}
			if(index[i].length-1!=interval){
				throw new InvalidParametersException("Index[i]'s length does not match invertal!");
			}
		}
		
		double[][] MFy1 = new double[1][interval + 1];
		double[][] MFY = new double[1][interval];
		double[][] MFx1 = new double[index.length][interval + 1];
		double[][] MFX = new double[index.length][interval];
		double[][] D = new double[index.length][index.length];
		double[][] d = new double[index.length][1];
		double[][] A = new double[index.length][index.length + 1];
		double[] b0 = new double[index.length + 1];

		// get the change ratio of price for MF
		for (int i = 0; i < interval + 1; i++) {
			MFy1[0][i] = MF[i];
		}
		for (int j = 1; j < interval + 1; j++) {
			MFY[0][j - 1] = (MFy1[0][j] - MFy1[0][j - 1]) / MFy1[0][j - 1];
		}

		// get the change ratio of index
		MFx1=index;
		int L = index.length;
		for (int k = 0; k < L; k++) {
			for (int j = 1; j < interval + 1; j++) {
				MFX[k][j - 1] = (MFx1[k][j] - MFx1[k][j - 1]) / MFx1[k][j - 1];
			}

		}

		// calculate matrix D,d:D=MFX*MFX',d=MFX*MFY'
		D = MatrixMultiply( MFX,Transpose(MFX));
		d = MatrixMultiply( MFY,Transpose(MFX));

		// initiate A,b0
		for (int i = 1; i < index.length + 1; i++) {
			b0[i] = 0;
		}
		;
		b0[0] = 1;
		for (int i = 0; i < index.length; i++) {
			A[i][0] = 1;
		}		
		for (int i = 0; i < index.length; i++) {
			for (int j = 1; j < index.length + 1; j++) {
				if (i + 1 == j)
					A[i][j] = 1;
				else
					A[i][j] = 0;
			}
		}
		
		double[] A1 = new double[index.length];
		for (int i = 0; i < index.length; i++) {
			A1[i] = 1;
		}

		LTIRInterface li = LTIRInterface.getInstance();
		String[] inString = new String[7];
		REXP[] solution = new REXP[1];

		// connect dvec to d with inString[1]
		inString[0] = "library(quadprog);";
		String tmp = "dvec<-c(";

		for (int i = 0; i < index.length - 1; i++) {
			Double left = (Double) d[0][i];
			tmp += left.toString() + ",";
		}
		Double left111 = (Double) d[0][index.length - 1];
		tmp += left111.toString() + ")";
		inString[1] = tmp + ";";

		// connect bvec to b0 with inString[2]
		String tmp2;
		tmp2= "bvec<-c(1,";
		for (int i = 1; i < index.length; i++) {
			Double left1 = (Double) b0[i];
			tmp2 += left1.toString() + ",";
		}
		Double left11 = (Double) b0[index.length];
		tmp2 += left11.toString() + ")";
		if(!allowShort)inString[2] = tmp2 + ";";
		else inString[2] = "bvec<-1";

		// connect Amat to A with inString[3]
		String tmp3 = "Amat<-matrix(c(";
		String s1 = new String();
		Integer num = (Integer) index.length;
		s1 = num.toString();
		if(!allowShort)
		{
			for (int i = 0; i < index.length; i++) {
				for (int j = 0; j < index.length; j++) {
					Double left3 = (Double) A[j][i];
					tmp3 += left3.toString() + ",";
				}
			}
			
			for (int i = 0; i < index.length - 1; i++) {
				
				Double left33 = (Double) A[i][index.length];
				tmp3 += left33.toString() + ",";
			}
			Double left333 = (Double) A[index.length - 1][index.length];
			tmp3 += left333.toString();

			tmp3 += ")," + s1 + "," + s1 + "+1)";
		}
		else
		{
			for (int i = 0; i < index.length; i++) {
					Double left3 = (Double) A1[i];
					if(i<index.length-1)tmp3 += left3.toString() + ",";
					else tmp3+=left3.toString();
			}
			tmp3 += "))";
		}
		
		inString[3] = tmp3 + ";";

		// connect Dmat to D with inString[4]
		String tmp4 = "Dmat<-matrix(c(";
		for (int i = 0; i < index.length - 1; i++) {
			for (int j = 0; j < index.length; j++) {
				Double left4 = (Double) D[j][i];
				tmp4 += left4.toString() + ",";
			}
		}
		for (int i = 0; i < index.length - 1; i++) {
			Double left44 = (Double) D[i][index.length - 1];
			tmp4 += left44.toString() + ",";
		}
		Double left444 = (Double) D[index.length - 1][index.length - 1];
		tmp4 += left444.toString() + ")," + s1 + "," + s1 + ");";
		inString[4] = tmp4;

		// solve.QP to get betas
		inString[5] = "solution=solve.QP(Dmat,dvec,Amat,bvec,meq=1);";
		inString[6] = "LTI_return1=solution[1];";

		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inString.length;i++){
			sb.append(inString[i]);
			sb.append("\n");
		}
		System.out.println(sb);
		//log.info(sb.toString());
		
		solution = li.RCall2(inString, 1);
		li.closeEngine();
		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Double[] beta = li.parseVector(solution[0]);

		double[][] beta2 = new double[index.length][1];
		for (int i = 0; i < index.length; i++) {
			beta2[i][0] = (double) beta[i];
		}

		double R = ComputeR(MFY, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
		Double[] beta3 = new Double[index.length + 1];
		for (int i = 0; i < index.length; i++) {
			beta3[i] = beta2[i][0];
		}
		//convert R square to the last of beta3
		beta3[index.length] = R;
		return this.adjustResult(beta3,index.length,4);
	}
	
	public Double[] RAA_WithLimit(int interval, double[] MF,double[][] index) throws Exception{
		return RAA_QP(interval, MF, index, false,true);
	}
	/**
	 * @author CCD
	 * @param interval
	 * @param MF
	 * @param index
	 * @param bound
	 * @param isWLS
	 * @param isSumOne
	 * @return
	 * to perform RAA like MVO with linear restrictions
	 */
	public Double[] MVO_RAA_WITH_CONSTRAINTS(Double[] expectedExcessReturn, Double[][] convariance, double[][] constraints, double RAF)throws Exception{
		int indexL = expectedExcessReturn.length;
		int totalLength = indexL * 2 + 1;
		if(constraints!=null && constraints.length>0){
			totalLength += constraints.length;
		}
		double[][] AA = new double[indexL][totalLength];
		double[] bb = new double[totalLength];
		/**************************for RAF *************************************/
		for(int i=0;i<indexL;++i)
			for(int j=0;j<indexL;++j){
				convariance[i][j]*=RAF;
			}
		/**************************for sum one or not ***************************/
		//sigmaX = 1
		for (int i = 0; i < indexL; i++) {
			AA[i][0] = 1.0;
		}
		bb[0] = 1.0;
		/***************************for lower************************************/
		//Xi>0
		for(int i=1; i< indexL +1; ++i)
			bb[i] = 0.0;
		for (int i = 0; i < indexL; i++)
			AA[i][i+1]=1.0;
		/***************************for upper************************************/
		//Xi<1 ->  -Xi>-1 
		for (int i = indexL + 1; i < indexL * 2 + 1; ++i)
			bb[i] = -1.0;
		for (int i = 0; i < indexL; ++i) 
			AA[i][i+indexL + 1]=-1.0;
		/***************************for constraints************************************/
		if(constraints!=null && constraints.length>0){
			for(int i= indexL * 2 + 1, k=0 ; i< totalLength; ++i,++k){
				for(int j = 0; j< indexL; ++j)	
					AA[j][i] = constraints[k][j];
				bb[i] = constraints[k][indexL];
			}
		}
		/***********************************************/
		
		LTIRInterface li = LTIRInterface.getInstance();
		String[] inString = new String[7];
		REXP[] solution = new REXP[1];

		// connect dvec to d with inString[1]
		inString[0] = "library(quadprog);";
		String tmp = "dvec<-c(";
		for (int i = 0; i < indexL - 1; i++)
			tmp += expectedExcessReturn[i].toString() + ",";
		tmp += expectedExcessReturn[indexL-1].toString() + ")";
		inString[1] = tmp + ";";

		/************************************************************/
		// connect bvec to b0 with inString[2]
		String tmp2;
		tmp2= "bvec<-c(";
		for (int i = 0; i < totalLength -1; i++)
			tmp2 += ((Double)bb[i]).toString() + ",";
		tmp2 += ((Double)bb[totalLength -1]).toString() + ")";
		inString[2] = tmp2 + ";";

		// connect Amat to A with inString[3]
		String tmp3 = "Amat<-matrix(c(";
		String s1, s2;
		s1 = ((Integer) indexL).toString();
		s2 = ((Integer) totalLength).toString();
		for (int i = 0; i < totalLength -1; i++) {
			for (int j = 0; j < indexL; j++) {
				tmp3 += ((Double)AA[j][i]).toString() + ",";
			}
		}		
		for (int i = 0; i < indexL - 1; i++) {
			tmp3 += ((Double)AA[i][totalLength-1]).toString() + ",";
		}
		tmp3 += ((Double)AA[indexL - 1][totalLength-1]).toString();
		tmp3 += ")," + s1 + "," + s2 + ")";		
		inString[3] = tmp3 + ";";
		/************************************************************/
		
		// connect Dmat to D with inString[4]
		String tmp4 = "Dmat<-matrix(c(";
		for (int i = 0; i < indexL - 1; i++) 
			for (int j = 0; j < indexL; j++) 
				tmp4 += convariance[j][i].toString() + ",";
		for (int i = 0; i < indexL - 1; i++) {
			tmp4 += convariance[i][indexL - 1].toString() + ",";
		}
		tmp4 += convariance[indexL - 1][indexL - 1].toString() + ")," + s1 + "," + s1 + ");";
		inString[4] = tmp4;
		// solve.QP to get betas
		inString[5] = "solution=solve.QP(Dmat,dvec,Amat,bvec,meq=1);";
		inString[6] = "LTI_return1=solution[1];";
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inString.length;i++){
			sb.append(inString[i]);
			sb.append("\n");
		}
		//log.info(sb.toString());
		solution = li.RCall2(inString, 1);
		li.closeEngine();
		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Double[] beta = li.parseVector(solution[0]);
		return beta;
	}
	/**
	 * Calculate RAA  with lower and upder limit and sum =1, if isWLS is true, use WLS, otherwise use OLS
	 * if lower[i] is not null ,set the lower limit as -100000
	 * if upper[i] is not null ,set the upper limit as 100000
	 * sum=1 is valid iff isSumOne=true and index.length>1(that meas it shoule have at least two factors)
	 * @author CCD
	 * @param interval
	 * @param MF
	 * @param index
	 * @param isWLS
	 * @param isSumOne
	 * @return
	 * @throws Exception
	 */
	public Double[] RAA_QP(int interval, double[] MF,	double[][] index ,boolean isWLS , boolean isSumOne) throws Exception{
		if(interval<=0||MF==null||index==null){
			throw new InvalidParametersException("Interval or MF or index parameter is invalid!");
		}
		if(MF.length-1!=interval){
			throw new InvalidParametersException("MF's length does not match invertal!");
		}
		for(int i=0;i<index.length;i++){
			if(index[i]==null){
				throw new InvalidParametersException("Index parameter is invalid!");
			}
			if(index[i].length-1!=interval){
				throw new InvalidParametersException("Index[i]'s length does not match invertal!");
			}
		}
		
		double[][] MFY = new double[1][interval];
		double[][] MFX = new double[index.length][interval];
		double[][] D = new double[index.length][index.length];
		double[][] d = new double[index.length][1];		

		// get the change ratio of price for MF
		for (int j = 1; j < interval + 1; j++) {
			MFY[0][j - 1] = (MF[j] - MF[j - 1]) / MF[j - 1];
		}

		// get the change ratio of index
		int L = index.length;
		for (int k = 0; k < L; k++) {
			for (int j = 1; j < interval + 1; j++) {
				MFX[k][j - 1] = (index[k][j] - index[k][j - 1]) / index[k][j - 1];
			}
		}

		//for the data in interval days, the weight of each is [1..sqrt(interval)]
		
		if(isWLS)
		{
			// in WLS,D= X'W'WX , d=Y'W'WX ,
			// that means D=MFX*weights*weights'*MFX' , d=MFY*weights*weights'*MFX'
			D = MatrixMultiply(MFX,weights);
			D = MatrixMultiply(D,Transpose(weights));
			D = MatrixMultiply(D,Transpose(MFX));
			
			d = MatrixMultiply( MFY,weights);
			d = MatrixMultiply( d,Transpose(weights));
			d = MatrixMultiply( d,Transpose(MFX));
		}
		else
		{
			// in OLS,calculate matrix D,d:D=MFX*MFX',d=MFX*MFY'
			D = MatrixMultiply( MFX,Transpose(MFX));
			d = MatrixMultiply( MFY,Transpose(MFX));
		}

		/************************************************/
		double[][] A = new double[index.length][index.length*2 + 1];
		double[] b0 = new double[index.length*2 + 1];
		// initiate A,b0
		
		//Xi>lower[i]
		for (int i = 1; i < index.length + 1; i++) {
			b0[i] = lower[i-1];
		}
		//Xi<upper[i] ->  -Xi>-upper[i] 
		for (int i = index.length + 1; i < index.length*2 + 1; i++) {
			b0[i] = -upper[i-index.length-1];
		}
		/***if isSumOne ==1 && index.length>=2 we set sigmaX=1**/
		if(isSumOne && index.length>1)
		{
			//sigmaX = 1
			for (int i = 0; i < index.length; i++) {
				A[i][0] = 1;
			}
			b0[0] = 1;
		}
		else
		{
			/**isSumOne == false we just let 0=0***/
			for (int i = 0; i < index.length; i++) {
				A[i][0] = 0;
			}
			b0[0] = 0;
		}
			
		//Xi>lower[i]
		for (int i = 0; i < index.length; i++) {
			for (int j = 1; j < index.length + 1; j++) {
					A[i][j] = 0;
			}
			A[i][i+1]=1;
		}
		//Xi<upper[i] ->  -Xi>-upper[i]
		for (int i = 0; i < index.length; i++) {
			for (int j = index.length + 1; j < index.length*2 + 1; j++) {
					A[i][j] = 0;
			}
			A[i][i+index.length + 1]=-1;
		}
		/***********************************************/
		
		LTIRInterface li = LTIRInterface.getInstance();
		String[] inString = new String[7];
		REXP[] solution = new REXP[1];

		// connect dvec to d with inString[1]
		inString[0] = "library(quadprog);";
		String tmp = "dvec<-c(";

		for (int i = 0; i < index.length - 1; i++) {
			Double left = (Double) d[0][i];
			tmp += left.toString() + ",";
		}
		Double left111 = (Double) d[0][index.length - 1];
		tmp += left111.toString() + ")";
		inString[1] = tmp + ";";

		/************************************************************/
		// connect bvec to b0 with inString[2]
		String tmp2;
		tmp2= "bvec<-c(";
		for (int i = 0; i < index.length*2; i++) {
			Double left1 = (Double) b0[i];
			tmp2 += left1.toString() + ",";
		}
		Double left11 = (Double) b0[index.length*2];
		tmp2 += left11.toString() + ")";
		inString[2] = tmp2 + ";";

		// connect Amat to A with inString[3]
		String tmp3 = "Amat<-matrix(c(";
		String s1 = new String();
		Integer num = (Integer) index.length;
		s1 = num.toString();
		
		for (int i = 0; i < index.length*2; i++) {
			for (int j = 0; j < index.length; j++) {
				Double left3 = (Double) A[j][i];
				tmp3 += left3.toString() + ",";
			}
		}		
		for (int i = 0; i < index.length - 1; i++) {
			
			Double left33 = (Double) A[i][index.length*2];
			tmp3 += left33.toString() + ",";
		}
		Double left333 = (Double) A[index.length - 1][index.length*2];
		tmp3 += left333.toString();

		tmp3 += ")," + s1 + "," + s1 + "*2+1)";		
		inString[3] = tmp3 + ";";
		/************************************************************/
		
		// connect Dmat to D with inString[4]
		String tmp4 = "Dmat<-matrix(c(";
		for (int i = 0; i < index.length - 1; i++) {
			for (int j = 0; j < index.length; j++) {
				Double left4 = (Double) D[j][i];
				tmp4 += left4.toString() + ",";
			}
		}
		for (int i = 0; i < index.length - 1; i++) {
			Double left44 = (Double) D[i][index.length - 1];
			tmp4 += left44.toString() + ",";
		}
		Double left444 = (Double) D[index.length - 1][index.length - 1];
		tmp4 += left444.toString() + ")," + s1 + "," + s1 + ");";
		inString[4] = tmp4;

		// solve.QP to get betas
		inString[5] = "solution=solve.QP(Dmat,dvec,Amat,bvec,meq=1);";
		inString[6] = "LTI_return1=solution[1];";

		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inString.length;i++){
			sb.append(inString[i]);
			sb.append("\n");
		}

		//log.info(sb.toString());
		
		solution = li.RCall2(inString, 1);
		li.closeEngine();
		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Double[] beta = li.parseVector(solution[0]);

		double[][] beta2 = new double[index.length][1];
		for (int i = 0; i < index.length; i++) {
			beta2[i][0] = (double) beta[i];
		}

		double R = ComputeR(MFY, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
		Double[] beta3 = new Double[index.length + 1];
		for (int i = 0; i < index.length; i++) {
			beta3[i] = beta2[i][0];
		}
		//convert R square to the last of beta3
		beta3[index.length] = R;
		return beta3;
		//return this.adjustResult(beta3,index.length,4);
	}
	
	
	public Double[] RAA_WithLimit_ForWLS(int interval, double[] MF,	double[][] index) throws Exception{
		if(interval<=0||MF==null||index==null){
			throw new InvalidParametersException("Interval or MF or index parameter is invalid!");
		}
		if(MF.length-1!=interval){
			throw new InvalidParametersException("MF's length does not match invertal!");
		}
		for(int i=0;i<index.length;i++){
			if(index[i]==null){
				throw new InvalidParametersException("Index parameter is invalid!");
			}
			if(index[i].length-1!=interval){
				throw new InvalidParametersException("Index[i]'s length does not match invertal!");
			}
		}
		
		double[][] MFy1 = new double[1][interval + 1];
		double[][] MFY = new double[1][interval];
		double[][] MFx1 = new double[index.length][interval + 1];
		double[][] MFX = new double[index.length][interval];
		double[][] D = new double[index.length][index.length];
		double[][] d = new double[index.length][1];		

		// get the change ratio of price for MF
		for (int i = 0; i < interval + 1; i++) {
			MFy1[0][i] = MF[i];
		}
		for (int j = 1; j < interval + 1; j++) {
			MFY[0][j - 1] = (MFy1[0][j] - MFy1[0][j - 1]) / MFy1[0][j - 1];
		}

		// get the change ratio of index
		MFx1=index;
		int L = index.length;
		for (int k = 0; k < L; k++) {
			for (int j = 1; j < interval + 1; j++) {
				MFX[k][j - 1] = (MFx1[k][j] - MFx1[k][j - 1]) / MFx1[k][j - 1];
			}

		}
		double[][] weights = new double[interval][interval];
		
		//for the data in interval days, the weight of each is [1..sqrt(interval)]
		for(int i=0;i<interval;++i)
			weights[i][i]=Math.sqrt(i+1);
		
		// in OLS,calculate matrix D,d:D=MFX*MFX',d=MFX*MFY'
		//D = MatrixMultiply( MFX,Transpose(MFX));
		//d = MatrixMultiply( MFY,Transpose(MFX));
		
		// in WLS,D= X'W'WX , d=Y'W'WX ,
		// that means D=MFX*weights*weights'*MFX' , d=MFY*weights*weights'*MFX'
		D = MatrixMultiply( MFX,weights);
		D = MatrixMultiply(D,Transpose(weights));
		D = MatrixMultiply(D,Transpose(MFX));
		
		d = MatrixMultiply( MFY,weights);
		d = MatrixMultiply( d,Transpose(weights));
		d = MatrixMultiply( d,Transpose(MFX));
		

		/************************************************/
		double[][] A = new double[index.length][index.length];
		double[] b0 = new double[index.length];
		// initiate A,b0
		
		//Xi>=lower[i]
		for (int i = 0; i < index.length; i++) {
			b0[i] = 0;
		}
		for (int i = 0; i < index.length; i++) {
			A[i][i] = 1;
		}		
		
		/***********************************************/
		
		double[] A1 = new double[index.length];
		for (int i = 0; i < index.length; i++) {
			A1[i] = 1;
		}

		LTIRInterface li = LTIRInterface.getInstance();
		String[] inString = new String[7];
		REXP[] solution = new REXP[1];

		// connect dvec to d with inString[1]
		inString[0] = "library(quadprog);";
		String tmp = "dvec<-c(";

		for (int i = 0; i < index.length - 1; i++) {
			Double left = (Double) d[0][i];
			tmp += left.toString() + ",";
		}
		Double left111 = (Double) d[0][index.length - 1];
		tmp += left111.toString() + ")";
		inString[1] = tmp + ";";

		/************************************************************/
		// connect bvec to b0 with inString[2]
		String tmp2;
		tmp2= "bvec<-c(";
		for (int i = 0; i < index.length-1; i++) {
			Double left1 = (Double) b0[i];
			tmp2 += left1.toString() + ",";
		}
		Double left11 = (Double) b0[index.length-1];
		tmp2 += left11.toString() + ")";
		inString[2] = tmp2 + ";";

		// connect Amat to A with inString[3]
		String tmp3 = "Amat<-matrix(c(";
		String s1 = new String();
		Integer num = (Integer) index.length;
		s1 = num.toString();
		
		int count=0;
		int maxCount = num*num;
		for (int i = 0; i < index.length; i++) {
			for (int j = 0; j < index.length; j++) {
				++count;
				Double left3 = (Double) A[j][i];
				tmp3 += left3.toString();
				if(count<maxCount)
					tmp3 += ",";
			}
		}		
		tmp3 += ")," + s1 + "," + s1 + ")";		
		inString[3] = tmp3 + ";";
		/************************************************************/
		
		// connect Dmat to D with inString[4]
		String tmp4 = "Dmat<-matrix(c(";
		for (int i = 0; i < index.length - 1; i++) {
			for (int j = 0; j < index.length; j++) {
				Double left4 = (Double) D[j][i];
				tmp4 += left4.toString() + ",";
			}
		}
		for (int i = 0; i < index.length - 1; i++) {
			Double left44 = (Double) D[i][index.length - 1];
			tmp4 += left44.toString() + ",";
		}
		Double left444 = (Double) D[index.length - 1][index.length - 1];
		tmp4 += left444.toString() + ")," + s1 + "," + s1 + ");";
		inString[4] = tmp4;

		// solve.QP to get betas
		inString[5] = "solution=solve.QP(Dmat,dvec,Amat,bvec,meq=0);";
		inString[6] = "LTI_return1=solution[1];";

		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inString.length;i++){
			sb.append(inString[i]);
			sb.append("\n");
		}

		//log.info(sb.toString());
		
		solution = li.RCall2(inString, 1);
		li.closeEngine();
		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Double[] beta = li.parseVector(solution[0]);

		double[][] beta2 = new double[index.length][1];
		for (int i = 0; i < index.length; i++) {
			beta2[i][0] = (double) beta[i];
		}

		double R = ComputeR(MFY, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
		Double[] beta3 = new Double[index.length + 1];
		for (int i = 0; i < index.length; i++) {
			beta3[i] = beta2[i][0];
		}
		//convert R square to the last of beta3
		beta3[index.length] = R;
		return beta3;
		//return this.adjustResult(beta3,index.length,4);
	}
	
	/*public double[] RAA_NoConstraint_ForWLS2(int interval, double[] MF,	double[][] index,boolean isWLSorOLS) throws Exception
	{
		double[] ff = new double[index.length];
		for(int i=0;i<index.length;i++)ff[i]=1.0;
		return ff;
	}*/
	public Double[] RAA_NoConstraint_ForWLS(int interval, double[] MF,	double[][] index,boolean isWLSorOLS,boolean[] isFFStyle) throws Exception
	{
		return RAA_LM(interval,MF,index,isWLSorOLS,isFFStyle,false,false);
	}
	public Double[] RAA_NoConstraint_ForWLS(int interval, double[] MF,	double[][] index,boolean isWLSorOLS,boolean[] isFFStyle,boolean factorIsCash ) throws Exception
	{
		return RAA_LM(interval,MF,index,isWLSorOLS,isFFStyle,factorIsCash ,false);
	}
	
	
	
	/**
	 * @author CCD
	 * @param interval
	 * @param MF
	 * @param index
	 * @param isWLSorOLS
	 * @param isFFStyle
	 * @param factorIsCash
	 * @param isSumOne
	 * @return
	 * @throws Exception
	 */
	public Double[] RAA_LM(int interval, double[] MF,	double[][] index,boolean isWLSorOLS,boolean[] isFFStyle,boolean factorIsCash , boolean isSumOne) throws Exception
	{		
		double[][] MFY = new double[1][interval];
		double[][] MFX = new double[index.length][interval];
		boolean isSum=false;
			
		int L = index.length;
		if(this.cashList==null){
			// get the change ratio of price for MF
			for (int j = 1; j < interval + 1; j++) {
				MFY[0][j - 1] = (MF[j] - MF[j - 1]) / MF[j - 1];
			}
			// get the change ratio of index
			for (int k = 0; k < L; k++) {
				for (int j = 1; j < interval + 1; j++) {
					MFX[k][j - 1] = (index[k][j] -index[k][j - 1]) / index[k][j - 1];
				}
			}
		}
		else{
			double[] MFC = new double[interval];
			for(int j=1;j<interval+1;j++)
			{
				double curCash = this.cashList[j];
				double preCash = this.cashList[j-1];
				MFC[j-1] = Math.log(curCash/preCash);
			}
			
			// get the change ratio of price for MF
			for (int j = 1; j < interval + 1; j++) {
				MFY[0][j - 1] = Math.log(MF[j]/ MF[j - 1]);
				if(!factorIsCash)
					MFY[0][j - 1] -= MFC[j-1];
			}
	
			// get the change ratio of index
			for (int k = 0; k < L; k++) {
				for (int j = 1; j < interval + 1; j++) {
					MFX[k][j - 1] = Math.log(index[k][j]/ index[k][j - 1]);
					if(!isFFStyle[k]&& !factorIsCash)
						MFX[k][j - 1] -= MFC[j-1];
				}
			}
		}
		double[][] MFx2 = new double[index.length-1][interval];
		int stringSize = index.length;
		/**if isSumOne true && index.length>1 we adjust Y and X*/
		if(isSumOne && index.length>1)
		{
			int last = index.length-1;
			//Y = Y - Xl ( Xl is returns of the last factor)
			for(int i=0;i<interval;++i)
				MFY[0][i]-=MFX[last][i];
			//Xi = Xi - Xl( Xl is returns of the last factor)
			for(int i=0;i<last;++i)
				for(int j=0;j<interval;++j)
					MFx2[i][j]= MFX[i][j] - MFX[last][j];
			MFX = new double[last][interval];
			MFX = MFx2;
			isSum=true;
			--stringSize;
		}
		LTIRInterface li = LTIRInterface.getInstance();
		
		String[] inString = new String[stringSize+3];
		REXP[] solution = new REXP[1];
		
		inString[0]="Y<-c(";
		for(int i=0;i<interval;i++){
			Double tmp = (Double)MFY[0][i];
			inString[0]+=tmp.toString();
			if(i==interval-1)
				inString[0]+=");";
			else
				inString[0]+=",";
		}
		
		for(int i=0;i<stringSize;i++)
		{
			inString[i+1]="X"+((Integer)(i+1)).toString()+"<-c(";
			for(int j=0;j<interval;j++)
			{
				Double tmp = (Double)MFX[i][j];
				inString[i+1]+=tmp.toString();
				if(j==interval-1)
					inString[i+1]+=");";
				else
					inString[i+1]+=",";
			}
		}
		
		inString[stringSize+1]="solution=lm(Y~";
		for(int i=0;i<stringSize;i++)
		{
			inString[stringSize+1]+="X"+((Integer)(i+1)).toString();
			if(i == stringSize-1)
			{
				if(!isWLSorOLS)inString[stringSize+1]+=");";
				else inString[stringSize+1]+=",";
			}
			else
				inString[stringSize+1]+="+";
		}
		
		/************************************************************/
		//New Method of WLS(with the weights parameter of lm)
		
		if(isWLSorOLS)
		{
			String weight="weights=c(";
			for(int i=0;i<interval;++i)
			{
				weight+=String.valueOf(Math.sqrt((double)(i+1)));
				if(i==interval-1)
					weight+="));";
				else
					weight+=",";
			}
			inString[stringSize+1]+=weight;
		}
		/*************************************************************/
		
		inString[stringSize+2]="LTI_return1=solution[1];";
		
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inString.length;i++){
			sb.append(inString[i]);
			sb.append("\n");
		}
		
		//log.info(sb.toString());
		
		solution = li.RCall2(inString, 1);
		
		/*******weights************************************/
		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Double[] beta = li.parseVector(solution[0]);
		Double[] betan = new Double[index.length];
		for(int i=1;i<beta.length;++i)
			betan[i-1] = beta[i];
		if(isSum)
		{
			betan[stringSize]=1.0;
			for(int i=0;i<stringSize;++i)
				betan[stringSize]-=betan[i];
		}
		//System.out.println("DF: "+Arrays.asList(beta));
		
		/**********p-Values********************************/
		/***here it incurs a problem as the solution is just the solution of n-1 factors**/
		inString = new String[2];
		REXP[] solution2 = new REXP[1];
		inString[0]="solution2=anova(solution)";
		inString[1]="LTI_return1=solution2[5]";
		solution2 = li.RCall2(inString, 1);
		
		Double[] tmpValue = li.parseVector(solution2[0]);
		Double[] pValue = new Double[index.length];
		for(int i=0;i<stringSize;i++)
			pValue[i] = tmpValue[i];
		//the p-value of the last factor is set 0.0 default when we use Y-X3~(X1-X3)+(X2-X3),we don't get the p-value for X3
		if(isSum)
			pValue[index.length-1]=0.0;
		//System.out.println("FFFFFFFFFFF"+Arrays.asList(pValue));
		
		/****************************************************************************/
		li.closeEngine();
		
		double[][] beta2 = new double[stringSize][1];
		for (int i = 0; i < stringSize; i++) {
			beta2[i][0] = (double) betan[i];
		}

		/*********************for R squread***********************************************************/
		
		double R = ComputeR2(MFY, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
		/*********************for weights*****************************************************************/
		
		Double[] beta3 = new Double[index.length*2+ 1];
		for (int i = 0; i < index.length; i++) {
			beta3[i] = betan[i];
		}
		//convert R square to the last of beta3
		beta3[index.length] = R;
		
		/*********************for p-Values******************************************************************/
		for(int i=1;i<=index.length;i++){
			beta3[index.length+i]=pValue[i-1];
		}
		return beta3;
		//return this.adjustResult(beta3,index.length,4);
	}
	
//	public Double[] RAA_LM(int interval, double[] MF,	double[][] index,boolean isWLSorOLS,boolean[] isFFStyle,boolean factorIsCash , boolean isSumOne) throws Exception
//	{		
//		double[] MFY = new double[interval];
//		double[][] MFx1 = new double[index.length][interval + 1];
//		double[][] MFX = new double[index.length][interval];
//		boolean isSum=false;
//			
//		int L = index.length;
//		if(this.cashList==null){
//			// get the change ratio of price for MF
//			for (int j = 1; j < interval + 1; j++) {
//				MFY[j - 1] = (MF[j] - MF[j - 1]) / MF[j - 1];
//			}
//	
//			// get the change ratio of index
//			MFx1=index;
//			for (int k = 0; k < L; k++) {
//				for (int j = 1; j < interval + 1; j++) {
//					MFX[k][j - 1] = (MFx1[k][j] - MFx1[k][j - 1]) / MFx1[k][j - 1];
//				}
//			}
//		}
//		else{
//			double[] MFC = new double[interval];
//			for(int j=1;j<interval+1;j++)
//			{
//				double curCash = this.cashList[j];
//				double preCash = this.cashList[j-1];
//				MFC[j-1] = Math.log(curCash/preCash);
//			}
//			
//			// get the change ratio of price for MF
//			for (int j = 1; j < interval + 1; j++) {
//				MFY[j - 1] = Math.log(MF[j]/ MF[j - 1]);
//				if(!factorIsCash)
//					MFY[j - 1] -= MFC[j-1];
//			}
//	
//			// get the change ratio of index
//			MFx1=index;
//			for (int k = 0; k < L; k++) {
//				for (int j = 1; j < interval + 1; j++) {
//					MFX[k][j - 1] = Math.log(MFx1[k][j]/ MFx1[k][j - 1]);
//					if(!isFFStyle[k]&& !factorIsCash)
//						MFX[k][j - 1] -= MFC[j-1];
//				}
//			}
//		}
//		
//		/**if isSumOne true && index.length>1 we adjust Y and X*/
//		if(isSumOne && index.length>1)
//		{
//			int last = index.length-1;
//			//Y = Y - Xl ( Xl is returns of the last factor)
//			for(int i=0;i<interval;++i)
//				MFY[i]-=MFX[last][i];
//			//Xi = Xi - Xl
//			for(int i=0;i<last;++i)
//				for(int j=0;j<interval;++j)
//					MFX[i][j]-=MFX[last][j];
//			isSum=true;
//		}
//		LTIRInterface li = LTIRInterface.getInstance();
//		int stringSize = index.length;
//		//we have present the last factor by another way
//		if(isSum)
//			--stringSize;
//		String[] inString = new String[stringSize+3];
//		REXP[] solution = new REXP[1];
//		
//		inString[0]="Y<-c(";
//		for(int i=0;i<MFY.length;i++){
//			Double tmp = (Double)MFY[i];
//			inString[0]+=tmp.toString();
//			if(i==MFY.length-1)
//				inString[0]+=");";
//			else
//				inString[0]+=",";
//		}
//		
//		for(int i=0;i<stringSize;i++)
//		{
//			inString[i+1]="X"+((Integer)(i+1)).toString()+"<-c(";
//			int size = MFX[i].length;
//			for(int j=0;j<size;j++)
//			{
//				Double tmp = (Double)MFX[i][j];
//				inString[i+1]+=tmp.toString();
//				if(j==size-1)
//					inString[i+1]+=");";
//				else
//					inString[i+1]+=",";
//			}
//		}
//		
//		inString[stringSize+1]="solution=lm(Y~";
//		for(int i=0;i<stringSize;i++)
//		{
//			inString[stringSize+1]+="X"+((Integer)(i+1)).toString();
//			if(i == stringSize-1)
//			{
//				if(!isWLSorOLS)inString[stringSize+1]+=");";
//				else inString[stringSize+1]+=",";
//			}
//			else
//				inString[stringSize+1]+="+";
//		}
//		
//		/************************************************************/
//		//New Method of WLS(with the weights parameter of lm)
//		if(isWLSorOLS)
//		{
//			String weights="weights=c(";
//			int size = MF.length;
//			for(int i=1;i<size;i++){
//				Double tmp = Math.sqrt(i);
//				weights+=tmp.toString();
//				if(i<size-1)weights+=",";
//				else weights+="));";
//			}
//			inString[stringSize+1]+=weights;
//		}
//		/*************************************************************/
//		
//		inString[stringSize+2]="LTI_return1=solution[1];";
//		
//		StringBuffer sb=new StringBuffer();
//		for(int i=0;i<inString.length;i++){
//			sb.append(inString[i]);
//			sb.append("\n");
//		}
//		
//		//System.out.println("SB="+sb);
//		
//		log.info(sb.toString());
//		
//		solution = li.RCall2(inString, 1);
//		
//		/*******weights************************************/
//		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		Double[] beta = li.parseVector(solution[0]);
//		if(isSum)
//		{
//			//calculate the last factor's beta by using 1 to subtract all the others' beta.
//			beta[stringSize]=1.0;
//			for(int i=0;i<stringSize;++i)
//				beta[stringSize]-=beta[i];
//		}
//		//System.out.println("DF: "+Arrays.asList(beta));
//		
//		/**********p-Values********************************/
//		/***here it incurs a problem as the solution is just the solution of n-1 factors**/
//		inString = new String[2];
//		REXP[] solution2 = new REXP[1];
//		inString[0]="solution2=anova(solution)";
//		inString[1]="LTI_return1=solution2[5]";
//		solution2 = li.RCall2(inString, 1);
//		
//		Double[] tmpValue = li.parseVector(solution2[0]);
//		Double[] pValue = new Double[stringSize];
//		for(int i=0;i<stringSize;i++)
//			pValue[i] = tmpValue[i];
//
//		//System.out.println("FFFFFFFFFFF"+Arrays.asList(pValue));
//		
//		/****************************************************************************/
//		li.closeEngine();
//		
//		double[][] beta2 = new double[stringSize][1];
//		for (int i = 0; i < stringSize; i++) {
//			beta2[i][0] = (double) beta[i+1];
//		}
//
//		/*********************for R squread***********************************************************/
//		double[][] MFy1 = new double[1][interval + 1];
//		double[][] MFY2 = new double[1][interval];
//		for (int i = 0; i < interval + 1; i++) {
//			MFy1[0][i] = MF[i];
//		}
//		for (int j = 1; j < interval + 1; j++) {
//			MFY2[0][j - 1] = (MFy1[0][j] - MFy1[0][j - 1]) / MFy1[0][j - 1];
//		}		
//		double R = ComputeR2(MFY2, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
//		/*********************for weights*****************************************************************/
//		
//		Double[] beta3 = new Double[index.length*2+ 1];
//		for (int i = 0; i < index.length; i++) {
//			beta3[i] = beta2[i][0];
//		}
//		//convert R square to the last of beta3
//		beta3[index.length] = R;
//		
//		/*********************for p-Values******************************************************************/
//		for(int i=1;i<=index.length;i++){
//			beta3[index.length+i]=pValue[i-1];
//		}
//		
//		return beta3;
//		//return this.adjustResult(beta3,index.length,4);
//	}
	
	public Double[] adjustResult(Double[] beta,int len,int type){	
		
		for(int i=0;i<beta.length;i++){
			System.out.print("  "+beta[i]);
		}
		System.out.println("bb=======================");
		
		double sum=0.0;
		String es = "0.";
	    for(int i=1;i<type;i++)es+="0";
	    es+="1";
	    double e = Double.parseDouble(es);
	    
	    java.text.DecimalFormat formater = new   java.text.DecimalFormat("###0.00000000");
		for(int i=0,size=len;i<size-1;i++){
			if(Math.abs(beta[i]-0)<e){
				beta[i]=0.0;
				continue;
			}
			String tmp = formater.format(beta[i]);
			int index = tmp.indexOf(".");
			int sub = index+type+1>tmp.length()?tmp.length():index+type+1;
			beta[i]=Double.parseDouble(tmp.substring(0, sub));
			sum+=beta[i];
		}
		if(Math.abs((1.0-sum)-0)<e){
			beta[len-1]=0.0;
		}
		else{
			formater.setMinimumFractionDigits(type);
			beta[len-1]=Double.parseDouble(formater.format(1.0-sum));		
		}
		for(int i=0;i<beta.length;i++){
			System.out.print("  "+beta[i]);
		}
		System.out.println("FF=======================");
		return beta;
	}
	
	public Double[] RAA_NoConstraint(int interval, double[] MF,	double[][] index) throws Exception
	{
		
		double[] MFY = new double[interval];
		double[][] MFx1 = new double[index.length][interval + 1];
		double[][] MFX = new double[index.length][interval];
		// get the change ratio of price for MF
		for (int j = 1; j < interval + 1; j++) {
			MFY[j - 1] = (MF[j] - MF[j - 1]) / MF[j - 1];
		}

		// get the change ratio of index
		MFx1=index;
		int L = index.length;
		for (int k = 0; k < L; k++) {
			for (int j = 1; j < interval + 1; j++) {
				MFX[k][j - 1] = (MFx1[k][j] - MFx1[k][j - 1]) / MFx1[k][j - 1];
			}

		}
		/****************************************************************/		
		
		LTIRInterface li = LTIRInterface.getInstance();
		int stringSize = index.length;
		String[] inString = new String[stringSize+3];
		REXP[] solution = new REXP[1];
		
		inString[0]="Y<-c(";
		for(int i=0;i<MFY.length;i++){
			Double tmp = (Double)MFY[i];
			inString[0]+=tmp.toString();
			if(i==MFY.length-1)
			{
				inString[0]+=");";
			}
			else
			{
				inString[0]+=",";
			}
		}
		
		for(int i=0;i<stringSize;i++)
		{
			inString[i+1]="X"+((Integer)(i+1)).toString()+"<-c(";
			int size = MFX[i].length;
			for(int j=0;j<size;j++)
			{
				Double tmp = (Double)MFX[i][j];
				inString[i+1]+=tmp.toString();
				if(j==size-1)
				{
					inString[i+1]+=");";
				}
				else
				{
					inString[i+1]+=",";
				}
			}
		}
		
		inString[stringSize+1]="solution=lm(Y~";
		for(int i=0;i<stringSize;i++)
		{
			inString[stringSize+1]+="X"+((Integer)(i+1)).toString();
			if(i == stringSize-1)
			{
				inString[stringSize+1]+=");";
			}
			else
				inString[stringSize+1]+="+";
		}
		
		inString[stringSize+2]="LTI_return1=solution[1];";
		
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inString.length;i++){
			sb.append(inString[i]);
			sb.append("\n");
		}
		
		//log.info(sb.toString());
		
		solution = li.RCall2(inString, 1);
		
		/*******weights************************************/
		if(solution[0] == null)System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Double[] beta = li.parseVector(solution[0]);

		System.out.println("DF: "+Arrays.asList(beta));
		
		/**********p-Values********************************/
		inString = new String[2];
		REXP[] solution2 = new REXP[1];
		inString[0]="solution2=anova(solution)";
		inString[1]="LTI_return1=solution2[5]";
		solution2 = li.RCall2(inString, 1);
		
		Double[] tmpValue = li.parseVector(solution2[0]);
		Double[] pValue = new Double[stringSize];
		for(int i=0;i<stringSize;i++)
			pValue[i] = tmpValue[i];

		//System.out.println("FFFFFFFFFFF"+Arrays.asList(pValue));
		
		/****************************************************************************/
		li.closeEngine();
		
		double[][] beta2 = new double[stringSize][1];
		for (int i = 0; i < stringSize; i++) {
			beta2[i][0] = (double) beta[i+1];
		}

		/*********************for R squread***********************************************************/
		double[][] MFy1 = new double[1][interval + 1];
		double[][] MFY2 = new double[1][interval];
		for (int i = 0; i < interval + 1; i++) {
			MFy1[0][i] = MF[i];
		}
		for (int j = 1; j < interval + 1; j++) {
			MFY2[0][j - 1] = (MFy1[0][j] - MFy1[0][j - 1]) / MFy1[0][j - 1];
		}		
		double R = ComputeR2(MFY2, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
		/*********************for weights*****************************************************************/
		
		Double[] beta3 = new Double[index.length*2+ 1];
		for (int i = 0; i < index.length; i++) {
			beta3[i] = beta2[i][0];
		}
		//convert R square to the last of beta3
		beta3[index.length] = R;
		
		/*********************for p-Values******************************************************************/
		for(int i=1;i<=index.length;i++){
			beta3[index.length+i]=pValue[i-1];
		}
		
		return beta3;
		//return this.adjustResult(beta3,index.length,4);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

        /*String[] index = { "VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
        Arrays.sort(index);
        String MF="DODBX";

		WeightEstimate we = new WeightEstimate();
		Date date = new Date();

		date = LTIDate.getDate(2008, 6, 3);

		try {
			int interval[] = { 30 };
			    Long t1 = System.currentTimeMillis();
				double[] d = we.RAA(interval[0],date,TimeUnit.DAILY, MF,index,false,true);
                
                int L=d.length;
                System.out.println("The weight of "+MF+" on "+date+" are:");
                Long t2 = System.currentTimeMillis();
				for (int i = 0; i < L-1; i++) {
					System.out.println(index[i]+"\t"+d[i]);
				}
				System.out.println("R square is "+d[L-1]);
				System.out.println("Time: " + (t2 - t1));
                
				/*int L=index.length;
                System.out.println("The weight of "+MF+" on "+date+" are:");
                Long t2 = System.currentTimeMillis();
				for (int i = 0; i <L; i++) {
					System.out.println(index[i]+"\t"+d[i]);
					System.out.println(index[i]+"\t"+d[L+i+1]);
				}
				System.out.println("R square is "+d[L]);
				System.out.println("Time: " + (t2 - t1));*/
		

		/*} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
		WeightEstimate we = new WeightEstimate();
		Double[] s = {0.664415154,
				0.08936756,
				-2.33E-24,
				0.020972195,
				0.0,
				0.225245091,
				-2.00E-20,
				0.987735916};
		we.adjustResult(s,3, 4);

	}

	public double[][] getWeights() {
		return weights;
	}

	public void setWeights(double[][] weights) {
		this.weights = weights;
	}

}
