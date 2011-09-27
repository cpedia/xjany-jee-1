package com.lti.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ojalgo.finance.portfolio.BlackLittermanModel;
import org.ojalgo.finance.portfolio.MarketEquilibrium;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.store.BigDenseStore;
import org.ojalgo.matrix.store.MatrixStore;

import com.lti.Exception.Security.NoPriceException;
import com.lti.service.BaseFormulaManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.type.SourceType;
import com.lti.type.TimeUnit;


public class LTIBLInterface extends Object{

	/**************************************/
	
	private BasicMatrix marketWeights;
	
	private BasicMatrix sigma;
	
	private BasicMatrix myWeight;
	
	private BasicMatrix omigaMatrix;
	
	private BigDecimal tao;
	
	private BigDecimal delta;
	
	/**************************************/
		
	private List<String> securityName;
	
	private Date startDate;
	
	private Date endDate;
	
	/**************************************/
	
	private List<List<Double>> allReturns;
	
	private List<Double> marketWeightList;
	
	private List<List<Double>> sigmaList;
	
	private List<List<Double>> myWeightList;
	
	private List<Double> omigaList;
	
	private List<Double> TargetValue;
	
	/**************************************/
	
	public LTIBLInterface()
	{
		
	}
	
	public void setMarketWeights(List<Double> marketWeight)//w_m
	{
		this.marketWeightList = marketWeight;
		
		int col = 1;
		int row = marketWeight.size();
		MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(row, col);
		marketWeights = new BigMatrix(a);
		
		for(int i=1;i<=marketWeight.size();i++)
		{
			marketWeights = this.marketWeights.add(i-1, 0, marketWeight.get(i-1));
		}
	}
	
	public void setSigma(List<List<Double>> tmpSigma)//covarienence
	{
		this.sigmaList = tmpSigma;
		
		int row = tmpSigma.size();
		int col = tmpSigma.get(0).size();
		MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(row, col);
		this.sigma = new BigMatrix(a);
		
		for(int i=1;i<=row;i++)
		{
			for(int j=1;j<=col;j++)
			{
				this.sigma = this.sigma.add(i-1, j-1, tmpSigma.get(i-1).get(j-1));
			}
		}
	}
	
	public void setMyWeight(List<List<Double>> tmpMyWeight)//p
	{
		this.myWeightList = tmpMyWeight;
		
		int row = tmpMyWeight.size();
		int col = tmpMyWeight.get(0).size();
		MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(row, col);
		this.myWeight = new BigMatrix(a);
		
		for(int i=1;i<=row;i++)
		{
			for(int j=1;j<=col;j++)
			{
				this.myWeight = this.myWeight.add(i-1, j-1, tmpMyWeight.get(i-1).get(j-1));
			}
		}
	}
	
	public void setOmigaMatrix(List<Double> tmpOmiga)//omiga
	{
		this.omigaList = tmpOmiga;
		
		int col = tmpOmiga.size();
		int row = tmpOmiga.size();
		MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(row, col);
		this.omigaMatrix = new BigMatrix(a);
		
		for(int i=1;i<=col;i++)
		{
			this.omigaMatrix = this.omigaMatrix.add(i-1, i-1, tmpOmiga.get(i-1));
		}
	}
	
	public void setTargetValue(List<Double> tmpTargetValue)//q
	{
		this.TargetValue = tmpTargetValue;
	}
	
	public void setTao(BigDecimal tao)
	{
		this.tao = tao;
	}
	
	public BigDecimal calculateTao()
	{
		BigDecimal bd = new BigDecimal(1);
		return bd;
	}
	
	public void setDelta(BigDecimal delta)
	{
		this.delta = delta;
	}
	
	public BigDecimal calculateDelta()
	{
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		
		if(this.securityName == null || startDate == null || endDate == null)
			return null;
		
		if(this.allReturns == null)
		{
			this.allReturns = this.getAllReturns();
		}
		
		if(this.sigma == null)
		{
			this.setSigma(this.calculateSigma(this.allReturns));
		}
		
		if(this.marketWeightList == null)
			return null;
		
		int size = this.marketWeightList.size();

		double tmpCov = 0;
		
		for(int i=0;i<size;i++)
		{
			double tmpValue = 0;
			for(int j=0;j<size;j++)
			{
				tmpValue += this.marketWeightList.get(j) * (this.sigmaList.get(j).get(i));
			}
			tmpCov += this.marketWeightList.get(i) * tmpValue;			
		}
		
		double riskFree = 0;
		try {
			riskFree = baseFormulaManager.getRiskFree(this.startDate, this.endDate);
		} catch (NoPriceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		double u = 0;
		for(int i=0;i<size;i++)
		{
			u += baseFormulaManager.computeAverage(this.allReturns.get(i)) * this.marketWeightList.get(i); 
		}
		
		BigDecimal bd = new BigDecimal((u-riskFree)/tmpCov);
		return bd;
	}
	
	public BigDecimal calculateDelta2()
	{
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		
		Security se = securityManager.get("^GSPC");
		
		if(startDate == null || endDate == null)
			return null;
		
		List<Double> returnList = new ArrayList<Double>();
		
		try {
			returnList = baseFormulaManager.getReturns(this.startDate, this.endDate, TimeUnit.DAILY, se.getID(), SourceType.SECURITY_RETURN, false);
		} catch (NoPriceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double riskFree = 0;
		try {
			riskFree = baseFormulaManager.getRiskFree(this.startDate, this.endDate);
		} catch (NoPriceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double cov = baseFormulaManager.computeCovariance(returnList, returnList);
		
		double average = baseFormulaManager.computeAverage(returnList);
		
		BigDecimal bd = new BigDecimal((average-riskFree)/cov);
		return bd;
	}
	
	public List<BigDecimal> getmodifedWeights()
	{		
		MarketEquilibrium marketEquilibrium = new MarketEquilibrium(this.sigma,this.delta);
		BlackLittermanModel bl = new BlackLittermanModel(marketEquilibrium,this.marketWeights);
		bl.setConfidence(this.tao);
		//bl.setWeightOnViews(this.tao);
		
		List<BasicMatrix> tmpList = this.myWeight.toListOfRows();		
		
		for(int i=0;i<tmpList.size();i++)
		{			
			//bl.addViewWithVariance(tmpList.get(i).toListOfElements(), new BigDecimal(this.TargetValue.get(i)),new BigDecimal(this.omigaList.get(i)));         //set p and q and viewconfidence
			bl.addViewWithBalancedConfidence((List<BigDecimal>) tmpList.get(i).toListOfElements(), new BigDecimal(this.TargetValue.get(i)));
		}		
		
		return bl.getWeights();//.getModifiedWeights().toListOfElements();
		
	}
	
	public List<List<Double>> calculateSigma(List<List<Double>> returns)
	{
		this.allReturns = returns;
		
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		
		int size = returns.size();
		
		List<List<Double>> newList = new ArrayList<List<Double>>();
		
		for(int i=0;i<size;i++)
		{
			List<Double> tmpList = new ArrayList<Double>();
			for(int j=0;j<size;j++)
			{
				double covarience = baseFormulaManager.computeCovariance(returns.get(i), returns.get(j));
				tmpList.add(covarience);
				//System.out.print(covarience+" ");
			}
			//System.out.println();
			newList.add(tmpList);
		}		
		
		return newList;
	}
	
	public List<List<Double>> getAllReturns()
	{
		if(this.allReturns != null)
			return this.allReturns;
		
		List<List<Double>> newList = new ArrayList<List<Double>>();
		
		if(this.securityName == null || startDate == null || endDate == null)
			return null;
		
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		
		for(int i=0;i<this.securityName.size();i++)
		{
			Security se = securityManager.get(securityName.get(i));
			try {
				newList.add(baseFormulaManager.getReturns(startDate, endDate, TimeUnit.DAILY, se.getID(), SourceType.SECURITY_RETURN, false));
			} catch (NoPriceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return newList;
	}
	
	public void setSecurityName(List<String> securityName)
	{
		this.securityName = securityName;
	}
	
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	
	public List<BigDecimal> getmodifedWeights2()// not normalize
	{		
		List<BasicMatrix> tmpList = this.myWeight.toListOfRows();		
		
		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(int i=0;i<this.marketWeightList.size();i++)
			result.add(new BigDecimal(0.0));
		
		for(int i=0;i<tmpList.size();i++)
		{			
			MarketEquilibrium marketEquilibrium = new MarketEquilibrium(this.sigma,this.delta);
			BlackLittermanModel bl = new BlackLittermanModel(marketEquilibrium,this.marketWeights);
			bl.setConfidence(this.tao);
			//bl.setWeightOnViews(this.tao);
			
			bl.addViewWithVariance((List<BigDecimal>) tmpList.get(i).toListOfElements(), new BigDecimal(this.TargetValue.get(i)),new BigDecimal(0));         //set p and q and viewconfidence

			List<BigDecimal> bdList = bl.getWeights();//.getModifiedWeights().toListOfElements();//
			
			for(int j=0;j<bdList.size();j++)
			{
				BigDecimal tmpBD = new BigDecimal(0.0);
				tmpBD = bdList.get(j).add(new BigDecimal(0.0-this.marketWeightList.get(j)));
				
				tmpBD = result.get(j).add(tmpBD.multiply(new BigDecimal(this.omigaList.get(i))));
				result.set(j, tmpBD);
			}
		}		
		
		List<BigDecimal> lastResult = new ArrayList<BigDecimal>();
		
		for(int i=0;i<result.size();i++)
		{
			BigDecimal tmpBD = new BigDecimal(0.0);
			tmpBD = result.get(i).multiply(new BigDecimal(1.0/result.size()));
			tmpBD = tmpBD.add(new BigDecimal(this.marketWeightList.get(i)));
			lastResult.add(tmpBD);
		}
		
		return lastResult;
	}
	
	public List<BigDecimal> getmodifedWeights3()//normalize
	{		
		List<BasicMatrix> tmpList = this.myWeight.toListOfRows();		
		
		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(int i=0;i<this.marketWeightList.size();i++)
			result.add(new BigDecimal(0.0));
		
		for(int i=0;i<tmpList.size();i++)
		{			
			MarketEquilibrium marketEquilibrium = new MarketEquilibrium(this.sigma,this.delta);
			BlackLittermanModel bl = new BlackLittermanModel(marketEquilibrium,this.marketWeights);
			bl.setConfidence(this.tao);
			//bl.setWeightOnViews(this.tao);
			
			bl.addViewWithVariance((List<BigDecimal>) tmpList.get(i).toListOfElements(), new BigDecimal(this.TargetValue.get(i)),new BigDecimal(0));         //set p and q and viewconfidence

			List<BigDecimal> bdList = bl.getWeights();//getNormalisedWeights().toListOfElements();//
			
			for(int j=0;j<bdList.size();j++)
			{
				BigDecimal tmpBD = new BigDecimal(0.0);
				tmpBD = bdList.get(j).add(new BigDecimal(0.0-this.marketWeightList.get(j)));
				
				tmpBD = result.get(j).add(tmpBD.multiply(new BigDecimal(this.omigaList.get(i))));
				result.set(j, tmpBD);
			}
		}		
		
		List<BigDecimal> lastResult = new ArrayList<BigDecimal>();
		
		for(int i=0;i<result.size();i++)
		{
			BigDecimal tmpBD = new BigDecimal(0.0);
			tmpBD = result.get(i).multiply(new BigDecimal(1.0/result.size()));
			tmpBD = tmpBD.add(new BigDecimal(this.marketWeightList.get(i)));
			lastResult.add(tmpBD);
		}
		
		lastResult = this.normalize2(lastResult);
		return lastResult;
	}
	
	public List<Double> normalize(List<Double> blList)
	{
		List<Double> list = new ArrayList<Double>();
		double bd = 0;
		for(int i=0;i<blList.size();i++)
		{
			bd+=blList.get(i);
		}
		for(int i=0;i<blList.size();i++)
		{
			list.add(blList.get(i)/bd);
		}
		return list;
	}
	
	public List<BigDecimal> normalize2(List<BigDecimal> blList)
	{
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		BigDecimal bd = new BigDecimal(0.0);
		for(int i=0;i<blList.size();i++)
		{
			bd = bd.add(blList.get(i));
		}
		for(int i=0;i<blList.size();i++)
		{
			double tmp = blList.get(i).doubleValue();
			list.add(new BigDecimal(tmp/bd.doubleValue()));
		}
		return list;
	}
	
	
	public List<List<Double>> getSigma()
	{
		return this.sigmaList;
	}
	
	public static void main(String[] args)
	{
		LTIBLInterface lb = new LTIBLInterface();
		
		List<Double> marketWeight = new ArrayList<Double>();
		
		Date start = LTIDate.getDate(2000, 1, 1);
		Date end = LTIDate.getDate(2007, 1, 1);
		lb.setStartDate(start);
		lb.setEndDate(end);
		
		for(int i=1;i<=2;i++)
			marketWeight.add(0.5);
		lb.setMarketWeights(marketWeight);
		
		List<Double> a = new ArrayList<Double>();
		a.add(1.0);
		a.add(0.0);
		
		List<List<Double>> b = new ArrayList<List<Double>>();
		b.add(a);
		List<Double> aa = new ArrayList<Double>();
		aa.add(0.0);
		aa.add(1.0);
		b.add(aa);
		
		lb.setMyWeight(b);
		
		List<Double> omi = new ArrayList<Double>();
		//BigDecimal bd = new BigDecimal(0.5);
		omi.add(0.5);
		omi.add(0.5);
		
		lb.setOmigaMatrix(omi);
		
		BigDecimal bd2 = new BigDecimal(0.4);
		lb.setTao(bd2);
		
		List<List<Double>> c= new ArrayList<List<Double>>();
		List<Double> d = new ArrayList<Double>();
		d.add(0.02);
		d.add(0.03);
		d.add(0.01);
		d.add(0.05);
		d.add(0.04);
		c.add(d);
		List<Double> dd = new ArrayList<Double>();
		dd.add(0.01);
		dd.add(0.05);
		dd.add(0.03);
		dd.add(0.04);
		dd.add(0.02);
		c.add(dd);
		lb.setSigma(lb.calculateSigma(c));
		
		List<Double> e = new ArrayList<Double>();
		e.add(0.09);
		e.add(0.1);
		lb.setTargetValue(e);
		
		BigDecimal bd3 = new BigDecimal(3.0);
		lb.setDelta(bd3);
		//lb.setDelta(lb.calculateDelta2());
		
		System.out.println(lb.getmodifedWeights());
		System.out.println(lb.getmodifedWeights2());
		System.out.println(lb.getmodifedWeights3());
	}
	
	/*************************************************************************************************************************************/
	public List<Double> getBLWeight(List<String> seName,List<Double> marketWeight,List<List<Double>> myWeight,List<Double> targetValue,List<Double> myViewOnWeight,int interval,TimeUnit tu)
	{
		Date endDate = new Date();
		Date startDate = LTIDate.getNewTradingDate(endDate, tu, interval);
		
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setSecurityName(seName);
		this.setMarketWeights(marketWeight);
		this.setMyWeight(myWeight);
		this.setOmigaMatrix(myViewOnWeight);
		this.setSigma(this.calculateSigma(this.getAllReturns()));
		this.setTargetValue(targetValue);
		this.setTao(new BigDecimal(1.0));
		this.setDelta(new BigDecimal(3.0));
		List<BigDecimal> blList = this.getmodifedWeights2();
		//List<BigDecimal> norBLList = this.normalize(blList);
		List<Double> result = new ArrayList<Double>();
		for(int i=0;i<blList.size();i++)
			result.add(blList.get(i).doubleValue());
		return result;
	}
	/************************************************************************************************************************************/
	public List<Double> getBLWeight2(List<String> seName,List<Double> marketWeight,List<List<Double>> myWeight,List<Double> targetValue,List<Double> myViewOnWeight,int interval,TimeUnit tu)
	{
		Date endDate = new Date();
		Date startDate = LTIDate.getNewTradingDate(endDate, tu, interval);
		
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setSecurityName(seName);
		this.setMarketWeights(marketWeight);
		this.setMyWeight(myWeight);
		this.setOmigaMatrix(myViewOnWeight);
		this.setSigma(this.calculateSigma(this.getAllReturns()));
		this.setTargetValue(targetValue);
		this.setTao(new BigDecimal(1.0));
		this.setDelta(new BigDecimal(3.0));
		List<BigDecimal> blList = this.getmodifedWeights3();
		//List<BigDecimal> norBLList = this.normalize(blList);
		List<Double> result = new ArrayList<Double>();
		for(int i=0;i<blList.size();i++)
			result.add(blList.get(i).doubleValue());
		return result;
	}
}
