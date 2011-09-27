package com.lti.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ojalgo.finance.portfolio.MarketEquilibrium;
import org.ojalgo.finance.portfolio.MarkowitzModel;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.store.BigDenseStore;
import org.ojalgo.matrix.store.MatrixStore;

import com.lti.Exception.Security.NoPriceException;
import com.lti.service.BaseFormulaManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.type.SourceType;
import com.lti.type.TimeUnit;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class LTIMVOInterface extends Object {

	private BasicMatrix aCovariances; // omigaMatrix

	private List<List<Double>> aCovariances2;

	private List<List<Double>> returns;

	private List<Double> lowerLimits;

	private List<Double> upperLimits;

	private BigDecimal targetReturn;

	private BigDecimal targetVariance;

	private BasicMatrix anExpectedExcessReturns;

	private List<Double> anExpectedExcessReturns2;
	private List<Double> myExpectedExcessReturns;

	private BigDecimal aRiskAversion;

	private List<String> securityList;

	private Date startDate;

	private Date endDate;

	private TimeUnit tu;

	private Double riskFreeReturn;

	private MarketEquilibrium marketEquilibrium;
	private MarkowitzModel markowitzModel;

	private List<Double> results;

	private List<String> portfolios;

	public List<String> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<String> portfolios) {
		this.portfolios = portfolios;
	}

	public BasicMatrix getACovariances() {
		return aCovariances;
	}

	public void setACovariances(BasicMatrix covariances) {
		aCovariances = covariances;
	}

	public BasicMatrix getAnExpectedExcessReturns() {
		return anExpectedExcessReturns;
	}

	public void setAnExpectedExcessReturns(BasicMatrix anExpectedExcessReturns) {
		this.anExpectedExcessReturns = anExpectedExcessReturns;
	}

	public BigDecimal getARiskAversion() {
		return aRiskAversion;
	}

	public void setARiskAversion(BigDecimal riskAversion) {
		aRiskAversion = riskAversion;
	}

	public List<String> getSecurityList() {
		return securityList;
	}

	public void setSecurityList(List<String> securityList) {
		this.securityList = securityList;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public TimeUnit getTu() {
		return tu;
	}

	public void setTu(TimeUnit tu) {
		this.tu = tu;
	}

	public List<List<Double>> getACovariances2() {
		return aCovariances2;
	}

	public void setACovariances2(List<List<Double>> covariances2) {
		aCovariances2 = covariances2;
	}

	public List<Double> getAnExpectedExcessReturns2() {
		return anExpectedExcessReturns2;
	}

	public void setAnExpectedExcessReturns2(List<Double> anExpectedExcessReturns2) {
		this.anExpectedExcessReturns2 = anExpectedExcessReturns2;
	}

	public List<List<Double>> getReturns() {
		return returns;
	}

	public void setReturns(List<List<Double>> returns) {
		this.returns = returns;
	}

	public LTIMVOInterface() {
		marketEquilibrium = null;
		markowitzModel = null;
	}

	private List<List<Double>> calculateCovariances(List<List<Double>> returns) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		int size = returns.size();

		List<List<Double>> newList = new ArrayList<List<Double>>();

		for (int i = 0; i < size; i++) {
			List<Double> tmpList = new ArrayList<Double>();
			for (int j = 0; j < size; j++) {
				double covarience = baseFormulaManager.computeCovariance(returns.get(i), returns.get(j));
				tmpList.add(covarience);
			}
			newList.add(tmpList);
		}

		return newList;
	}

	public void setACovariances(List<List<Double>> aCovariances2)// covarienence
	{
		this.aCovariances2 = aCovariances2;

		int row = aCovariances2.size();
		int col = aCovariances2.get(0).size();
		MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(row, col);
		this.aCovariances = new BigMatrix(a);

		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				this.aCovariances = this.aCovariances.add(i - 1, j - 1, aCovariances2.get(i - 1).get(j - 1));
			}
		}
	}

	public void setAnExpectedExcessReturns(List<Double> anExpectedExcessReturns) {
		this.anExpectedExcessReturns2 = anExpectedExcessReturns;

		int row = anExpectedExcessReturns2.size();

		MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(row, 1);
		this.anExpectedExcessReturns = new BigMatrix(a);

		for (int i = 1; i <= row; i++) {
			Double value = this.getMyExpectedExcessReturns().get(i - 1);
			if (value == null)
				value = anExpectedExcessReturns2.get(i - 1);
			this.anExpectedExcessReturns2.set(i - 1, value);
			this.anExpectedExcessReturns = this.anExpectedExcessReturns.add(i - 1, 0, value);
		}
	}

	public List<List<Double>> getAllReturns(boolean isSecurity) {
		return getAllReturns(isSecurity, false);
	}

	public List<List<Double>> getAllReturns(boolean isSecurity, boolean nav) {
		if (this.returns != null)
			return this.returns;

		List<List<Double>> newList = new ArrayList<List<Double>>();

		List<Double> totalReturnList = new ArrayList<Double>();

		if (this.securityList == null || this.startDate == null || this.endDate == null)
			return null;

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		/****************************************************************************************************************************************/
		List<Double> riskReturnList = null;
		try {
			riskReturnList = baseFormulaManager.getReturns(this.startDate, this.endDate, this.tu, securityManager.get("CASH").getID(), SourceType.SECURITY_RETURN, false);
		} catch (NoPriceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		double riskFreeReturn = 0;
		if (riskReturnList != null && riskReturnList.size() != 0)
			riskFreeReturn = baseFormulaManager.computeAverage(riskReturnList);
		this.setRiskFreeReturn(riskFreeReturn);
		/****************************************************************************************************************************************/

		if (isSecurity) {
			for (int i = 0; i < this.securityList.size(); i++) {
				Security se = securityManager.get(securityList.get(i));
				List<Double> tmpReturnList = null;
				try {
					if (se.getSecurityType() == 6) {
						String name = se.getName();
						Portfolio po = portfolioManager.get(name);
						tmpReturnList = baseFormulaManager.getReturns(this.startDate, this.endDate, this.tu, po.getID(), SourceType.PORTFOLIO_RETURN, nav);
						if (nav && tmpReturnList != null && tmpReturnList.size() > 0) {
							if (tmpReturnList.get(0) == 0)
								tmpReturnList = baseFormulaManager.getReturns(this.startDate, this.endDate, this.tu, po.getID(), SourceType.PORTFOLIO_RETURN, false);
						}
					} else {
						tmpReturnList = baseFormulaManager.getReturns(this.startDate, this.endDate, this.tu, se.getID(), SourceType.SECURITY_RETURN, nav);
						if (nav && tmpReturnList != null && tmpReturnList.size() > 0) {
							if (tmpReturnList.get(0) == 0)
								tmpReturnList = baseFormulaManager.getReturns(this.startDate, this.endDate, this.tu, se.getID(), SourceType.SECURITY_RETURN, false);
						}
					}
					newList.add(tmpReturnList);
					// System.out.println(tmpReturnList);
				} catch (NoPriceException e) {
					// TODO Auto-generated catch block
					System.out.println(StringUtil.getStackTraceString(e));
				}
				double tmpAverage = 0;
				if (tmpReturnList != null && tmpReturnList.size() != 0)
					tmpAverage = baseFormulaManager.computeAverage(tmpReturnList);
				totalReturnList.add(tmpAverage - riskFreeReturn);
			}
		} else {
			for (int i = 0; i < this.portfolios.size(); i++) {
				Portfolio po = portfolioManager.get(portfolios.get(i));
				List<Double> tmpReturnList = null;
				try {
					tmpReturnList = baseFormulaManager.getReturns(this.startDate, this.endDate, this.tu, po.getID(), SourceType.PORTFOLIO_RETURN, nav);
					newList.add(tmpReturnList);
				} catch (Exception e) {
					e.printStackTrace();
				}
				double tmpAverage = 0;
				if (tmpReturnList != null && tmpReturnList.size() != 0)
					tmpAverage = baseFormulaManager.computeAverage(tmpReturnList);
				totalReturnList.add(tmpAverage);
			}
		}

		this.setReturns(newList);

		this.setAnExpectedExcessReturns(totalReturnList);

		return newList;
	}

	public List<Double> checkExpectedReturns() {
		return this.anExpectedExcessReturns2;
	}

	public void createModel(List<String> securityList, List<Double> lowerLimitsList, List<Double> upperLimitsList, List<Double> expectedReturns, Double RAF, Date startDate, Date endDate, TimeUnit tu) {
		createModel(securityList, lowerLimitsList, upperLimitsList, expectedReturns, RAF, startDate, endDate, tu, false);
	}

	public void createModel(List<String> securityList, List<Double> lowerLimitsList, List<Double> upperLimitsList, List<Double> expectedReturns, Double RAF, Date startDate, Date endDate, TimeUnit tu, boolean nav) {
		this.setLowerLimits(lowerLimitsList);
		this.setUpperLimits(upperLimitsList);
		this.setSecurityList(securityList);
		this.setARiskAversion(new BigDecimal(RAF));
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setTu(tu);
		this.setMyExpectedExcessReturns(expectedReturns);
		this.setACovariances(this.calculateCovariances(this.getAllReturns(true, nav)));

		marketEquilibrium = new MarketEquilibrium(this.getACovariances(), this.getARiskAversion());
		markowitzModel = new MarkowitzModel(marketEquilibrium, this.getAnExpectedExcessReturns());
		markowitzModel.setShortingAllowed(false);

		/*
		 * for(int i=0;i<securityList.size();i++){
		 * System.out.println(this.getACovariances2().get(i)); }
		 * System.out.println(this.getARiskAversion()); for(int
		 * i=0;i<securityList.size();i++){
		 * System.out.println(this.getAnExpectedExcessReturns2().get(i)); }
		 * System.out.println(this.getLowerLimits());
		 * System.out.println(this.getUpperLimits());System.out.println(
		 * "=================================================================");
		 */
	}

	public List<Double> getMVOWeights()// List<String> securityList,List<Double>
										// expectedReturns,Double RAF,Date
										// startDate, Date endDate, TimeUnit tu)
	{
		List<BigDecimal> marList = markowitzModel.getWeights();
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < marList.size(); i++)
			result.add(marList.get(i).doubleValue());

		this.results = result;

		return result;

	}

	public List<Double> getMVOWeightsWithLimits()// List<String>
													// securityList,List<Double>
													// lowerLimits,List<Double>
													// upperLimits,List<Double>
													// expectedReturns,Double
													// RAF,Date startDate, Date
													// endDate, TimeUnit tu)
	{
		if (lowerLimits == null) {
			for (int i = 0; i < this.getSecurityList().size(); i++)
				markowitzModel.setLowerLimit(i, new BigDecimal(0.0));
		} else {
			for (int i = 0; i < this.getSecurityList().size(); i++)
				markowitzModel.setLowerLimit(i, new BigDecimal(this.getLowerLimits().get(i)));
		}

		if (upperLimits == null) {
			for (int i = 0; i < this.getSecurityList().size(); i++)
				markowitzModel.setUpperLimit(i, new BigDecimal(1.0));
		} else {
			for (int i = 0; i < this.getSecurityList().size(); i++)
				markowitzModel.setUpperLimit(i, new BigDecimal(this.getUpperLimits().get(i)));
		}

		List<BigDecimal> marList = markowitzModel.getWeights();
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < marList.size(); i++)
			result.add(marList.get(i).doubleValue());

		this.results = result;

		return result;

	}

	public List<List<Double>> getMVOcorrelation() {
		List<List<Double>> result = new ArrayList<List<Double>>();

		int row = this.aCovariances2.size();
		int col = this.aCovariances2.get(0).size();
		for (int i = 0; i < row; i++) {
			List<Double> tmpResult = new ArrayList<Double>();
			for (int j = 0; j < col; j++) {
				double tmpNum1 = this.aCovariances.doubleValue(i, j);
				double tmpNum2 = this.aCovariances.doubleValue(i, i);
				double tmpNum3 = this.aCovariances.doubleValue(j, j);
				double value = tmpNum1 / Math.sqrt(tmpNum2 * tmpNum3);
				tmpResult.add(value);
			}
			result.add(tmpResult);
		}
		return result;
	}

	@Deprecated
	public List<Double> getPortfolioWeights(List<String> portfolioList, List<Double> lowerLimits, List<Double> upperLimits, Double RAF, Date startDate, Date endDate, TimeUnit tu) {
//		CompiledStrategy bs = new CompiledStrategy();
//		int count = portfolioList.size() / 5;
//		if (count > 10)
//			count = 10;
//		List<Portfolio> poList = bs.getTopPortfolio(portfolioList, 10, startDate, endDate, SortType.SHARPE, tu);
//
//		this.setLowerLimits(lowerLimits);
//		this.setUpperLimits(upperLimits);
//		this.setPortfolios(portfolioList);
//		this.setARiskAversion(new BigDecimal(RAF));
//		this.setStartDate(startDate);
//		this.setEndDate(endDate);
//		this.setTu(tu);
//		this.setACovariances(this.calculateCovariances(this.getAllReturns(false)));
//
//		marketEquilibrium = new MarketEquilibrium(this.getACovariances(), this.getARiskAversion());
//		markowitzModel = new MarkowitzModel(marketEquilibrium, this.getAnExpectedExcessReturns());
//
//		if (lowerLimits == null) {
//			for (int i = 0; i < this.getSecurityList().size(); i++)
//				markowitzModel.setLowerLimit(i, new BigDecimal(0.0));
//		} else {
//			for (int i = 0; i < this.getSecurityList().size(); i++)
//				markowitzModel.setLowerLimit(i, new BigDecimal(this.getLowerLimits().get(i)));
//		}
//
//		if (upperLimits == null) {
//			for (int i = 0; i < this.getSecurityList().size(); i++)
//				markowitzModel.setUpperLimit(i, new BigDecimal(1.0));
//		} else {
//			for (int i = 0; i < this.getSecurityList().size(); i++)
//				markowitzModel.setUpperLimit(i, new BigDecimal(this.getUpperLimits().get(i)));
//		}
//
//		List<BigDecimal> marList = markowitzModel.getWeights();
//		List<Double> result = new ArrayList<Double>();
//		for (int i = 0; i < marList.size(); i++)
//			result.add(marList.get(i).doubleValue());
//
//		this.results = result;
//
//		return result;
		return null;
	}

	public double[][] getCorrelationMatrix() {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		int size = this.securityList.size();

		double result[][] = new double[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				double co = baseFormulaManager.computeCorrelationCoefficient(this.returns.get(i), this.returns.get(j));
				result[i][j] = co;
				result[j][i] = co;
			}
		}

		return result;
	}

	public static void main(String[] args) {
		// List<String> seList = new ArrayList<String>();
		// seList.add("CASH");
		// seList.add("VFINX");
		// seList.add("VGTSX");
		// seList.add("VGSIX");
		// seList.add("QRAAX");
		// seList.add("BEGBX");
		// seList.add("VBMFX");
		//		
		// Date startDate = LTIDate.getDate(2008, 3, 31);
		// Date endDate = LTIDate.getDate(2009, 3, 31);
		// LTIMVOInterface lmi = new LTIMVOInterface();
		// List<Double> ee = new ArrayList<Double>();
		// for(int i=0;i<7;i++)
		// ee.add(0.5);
		//		
		// lmi.createModel(seList,null,null,ee, 0.5, startDate, endDate,
		// TimeUnit.WEEKLY,true);
		// List<Double> re = lmi.getMVOWeightsWithLimits();
		//		
		// System.out.println("=======Return of cash====================");
		// System.out.println(lmi.getRiskFreeReturn());
		// System.out.println("======= each security's Return-Return of cash====================");
		// List<Double> re1 = lmi.getAnExpectedExcessReturns2();
		// for(int i=0;i<re1.size();i++)
		// {
		// System.out.println(re1.get(i));
		// }
		//		
		// System.out.println("=======result====================");
		// for(int i=0;i<re.size();i++)
		// {
		// System.out.println(re.get(i));
		// }

		// System.out.println("\n\n=======     nav =true    =========");
		//		
		//		
		// lmi.createModel(seList,null,null,ee, 0.5, startDate, endDate,
		// TimeUnit.WEEKLY);
		// re = lmi.getMVOWeightsWithLimits();
		//		
		// System.out.println("=======Return of cash====================");
		// System.out.println(lmi.getRiskFreeReturn());
		// System.out.println("=======each security's Return-Return of cash====================");
		// re1 = lmi.getAnExpectedExcessReturns2();
		// for(int i=0;i<re1.size();i++)
		// {
		// System.out.println(re1.get(i));
		// }
		//		
		// System.out.println("=======result====================");
		// for(int i=0;i<re.size();i++)
		// {
		// System.out.println(re.get(i));
		// }

		/*
		 * double result[][] = lmi.getCorrelationMatrix(); for(int
		 * i=0;i<result[0].length;i++) { for(int j=0;j<result[0].length;j++)
		 * System.out.print(" "+result[i][j]); System.out.println(); }
		 */
		// System.out.println(lmi.getACovariances2());
		// System.out.println(lmi.PlotPie());
		LTIMVOInterface li = new LTIMVOInterface();
		List<String> securityList = new ArrayList<String>();
		securityList.add("LBF");
		securityList.add("EVP");
		// securityList.add("CASH");
		securityList.add("AGC");
		securityList.add("BCL");
		securityList.add("BJZ");
		securityList.add("MPA");
		securityList.add("VFAIX");
		securityList.add("VCVSX");
		// securityList.add("VFINX");
		// securityList.add("VGTSX");
		// securityList.add("VGSIX");
		// securityList.add("QRAAX");
		// securityList.add("BEGBX");
		// securityList.add("VBMFX");
		// securityList.add("CASH");
		Date start = LTIDate.getDate(2007, 12, 19);
		Date end = LTIDate.getDate(2009, 6, 19);
		double RAF = 1.0;
		TimeUnit timeUnit = TimeUnit.DAILY;
		List<Double> lowerLimits = new ArrayList<Double>();
		lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		// lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		lowerLimits.add(0.0);
		List<Double> upperLimits = new ArrayList<Double>();
		upperLimits.add(1.0);
		// upperLimits.add(1.0);
		upperLimits.add(1.0);
		upperLimits.add(1.0);
		upperLimits.add(1.0);
		upperLimits.add(1.0);
		upperLimits.add(1.0);
		upperLimits.add(1.0);
		upperLimits.add(1.0);
		List<Double> expectedExcessReturns = new ArrayList<Double>();
		expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		// expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		expectedExcessReturns.add(null);
		li.createModel(securityList, lowerLimits, upperLimits, expectedExcessReturns, RAF, start, end, timeUnit, false);
		List<Double> returns = li.checkExpectedReturns();
		List<Double> weights = li.getMVOWeightsWithLimits();
		List<Double> noLimitWeights = li.getMVOWeights();
		for (int i = 0; i < returns.size(); i++) {
			System.out.println(securityList.get(i) + ": " + returns.get(i));
		}
		System.out.println("No limit:");
		for (int i = 0; i < weights.size(); ++i) {
			System.out.println(weights.get(i));
		}
		System.out.println("limit:");
		for (int i = 0; i < noLimitWeights.size(); ++i) {
			System.out.println(noLimitWeights.get(i));
		}
		return;
	}

	/*
	 * public static void main(String[] args) { BasicMatrix[] system = new
	 * BigMatrix[6];
	 * 
	 * MatrixStore<BigDecimal> a = BigDenseStore.FACTORY.makeZero(1, 7);
	 * system[0]= new BigMatrix(a); for(int i=1;i<=1;i++) { for(int
	 * j=1;j<=7;j++) { system[0]=system[0].set(i-1, j-1,new BigDecimal(1)); } }
	 * 
	 * a = BigDenseStore.FACTORY.makeZero(1, 1); system[1]=new BigMatrix(a);
	 * system[1]=system[1].set(0,0,new BigDecimal(1));
	 * 
	 * LTIMVOInterface lmi = new LTIMVOInterface(); lmi.setTu(TimeUnit.WEEKLY);
	 * Date startDate = LTIDate.getDate(2008, 6, 1); Date endDate =
	 * LTIDate.getDate(2008, 7, 20); lmi.setStartDate(startDate);
	 * lmi.setEndDate(endDate); List<String> seList = new ArrayList<String>();
	 * seList.add("VFINX"); seList.add("VGTSX"); seList.add("VGSIX");
	 * seList.add("QRAAX"); seList.add("BEGBX"); seList.add("VBMFX");
	 * seList.add("CASH"); lmi.setSecurityList(seList);
	 * lmi.setACovariances(lmi.calculateCovariances(lmi.getAllReturns(true)));
	 * system[2]=lmi.getACovariances();
	 * 
	 * System.out.println("Begin12.");
	 * 
	 * system[3]=lmi.getAnExpectedExcessReturns(); for(int j=1;j<=7;j++) {
	 * system[3]=system[3].set(j-1,0,new
	 * BigDecimal(-lmi.anExpectedExcessReturns2.get(j-1))); }
	 * 
	 * MatrixStore<BigDecimal> ab = BigDenseStore.FACTORY.makeZero(14,7);
	 * system[4]= new BigMatrix(ab); for(int j=1;j<=7;j++) {
	 * system[4]=system[4].set(j-1, j-1,new BigDecimal(1)); } for(int
	 * j=1;j<=7;j++) { system[4]=system[4].set(j+6, j-1,new BigDecimal(-1)); }
	 * 
	 * MatrixStore<BigDecimal> b = BigDenseStore.FACTORY.makeZero(14, 1);
	 * system[5]= new BigMatrix(b); for(int j=1;j<=7;j++) {
	 * system[5]=system[5].set(j-1,0,new BigDecimal(1)); }
	 * 
	 * System.out.println("Begin11.");
	 * 
	 * ActiveSetSolver as = new ActiveSetSolver(system);
	 * 
	 * System.out.println("Begin1."); OptimisationSolver.Result res =
	 * as.solve(); State state = res.getState();
	 * 
	 * System.out.println("Begin2."); if(state != state.FAILED ) { BasicMatrix
	 * solution = res.getSolution(); System.out.println("Success");
	 * System.out.println(solution.toString()); } System.out.println("End.");
	 * 
	 * }
	 */

	public Double getRiskFreeReturn() {
		return riskFreeReturn;
	}

	public void setRiskFreeReturn(Double riskFreeReturn) {
		this.riskFreeReturn = riskFreeReturn;
	}

	public List<Double> getLowerLimits() {
		return lowerLimits;
	}

	public void setLowerLimits(List<Double> lowerLimits) {
		this.lowerLimits = lowerLimits;
		if (this.markowitzModel != null) {
			if (lowerLimits == null) {
				for (int i = 0; i < this.getSecurityList().size(); i++)
					markowitzModel.setLowerLimit(i, new BigDecimal(0.0));
			} else {
				for (int i = 0; i < this.getSecurityList().size(); i++)
					markowitzModel.setLowerLimit(i, new BigDecimal(this.getLowerLimits().get(i)));
			}
		}
	}

	public List<Double> getUpperLimits() {
		return upperLimits;
	}

	public void setUpperLimits(List<Double> upperLimits) {
		this.upperLimits = upperLimits;
		if (this.markowitzModel != null) {
			if (upperLimits == null) {
				for (int i = 0; i < this.getSecurityList().size(); i++)
					markowitzModel.setUpperLimit(i, new BigDecimal(1.0));
			} else {
				for (int i = 0; i < this.getSecurityList().size(); i++)
					markowitzModel.setUpperLimit(i, new BigDecimal(this.getUpperLimits().get(i)));
			}
		}
	}

	public BigDecimal getTargetReturn() {
		return targetReturn;
	}

	public void setTargetReturn(BigDecimal targetReturn) {
		this.targetReturn = targetReturn;
	}

	public BigDecimal getTargetVariance() {
		return targetVariance;
	}

	public void setTargetVariance(BigDecimal targetVariance) {
		this.targetVariance = targetVariance;
	}

	public String PlotPie() {
		if (this.results == null)
			return null;

		List<Integer> tmpList = new ArrayList<Integer>();
		for (int i = 0; i < this.getSecurityList().size(); i++) {
			if (this.results.get(i) != 0)
				tmpList.add(i);
		}

		int size = tmpList.size();

		String[] index = new String[size];
		Double[] betas = new Double[size];

		for (int i = 0; i < size; i++) {
			index[i] = this.getSecurityList().get(tmpList.get(i));
			betas[i] = this.results.get(tmpList.get(i));
		}
		return this.PlotPie2("Asset", this.startDate, this.endDate, index, betas);
	}

	public boolean isLinux() {
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1) {
			return true;
		}
		return false;
	}

	private String PlotPie(String bond, Date startDate, Date endDate, String index[], Double[] betas) {
		long time = System.currentTimeMillis();
		String fileName = ContextHolder.getImagePath().replace("\\", "/") + "/" + bond + startDate.getTime() + endDate.getTime() + time + ".jpg";
		String main = bond + "  [" + new SimpleDateFormat("yyyy-MM-dd").format(startDate) + "] to" + "  [" + new SimpleDateFormat("yyyy-MM-dd").format(endDate) + "]";
		String sales = "";
		for (int i = 0; i < betas.length; i++) {
			sales += FormatUtil.formatQuantity(betas[i]);
			if (i != betas.length - 1)
				sales += ",";
		}
		String names = "";
		for (int i = 0; i < index.length; i++) {
			names += "\"" + index[i] + " " + FormatUtil.formatPercentage(betas[i]) + "%\"";
			if (i != index.length - 1)
				names += ",";
		}

		// LTIDownLoader ld = new LTIDownLoader();

		String[] cmds = { "jpeg(file=\"" + ContextHolder.getServletPath().replace("\\", "/") + "/" + fileName + "\", bg=\"transparent\",width = 700, height = 500)", "require(grDevices)", "pie(rep(1, 24), col = rainbow(24), radius = 0.9)", "pie.sales <- c(" + sales + ")", "names(pie.sales) <- c(" + names + ")", "pie(pie.sales,main=\"" + main + "\")", "dev.off()" };

		if (isLinux()) {
			String[] cmds1 = { "bitmap(file=\"" + ContextHolder.getServletPath().replace("\\", "/") + "/" + fileName + "\", type=\"jpeg\")", "require(grDevices)", "pie.sales <- c(" + sales + ")", "names(pie.sales) <- c(" + names + ")", "pie(pie.sales,main=\"" + main + "\")", "dev.off()" };
			cmds = cmds1;
		}
		LTIRInterface i = LTIRInterface.getInstance();
		String[] returns = i.RCall(cmds, 0);

		StringBuffer sb = new StringBuffer();
		sb.append("Call:\n\n");
		for (int j = 0; j < cmds.length; j++) {
			sb.append(cmds[j]);
			sb.append("\n");
		}
		sb.append("\n\n\n\nReturn:\n\n");
		if (returns != null) {

			for (int j = 0; j < returns.length; j++) {
				sb.append(returns[j]);
				sb.append("\n");
			}

		}
		return fileName;
	}

	private String PlotPie2(String bond, Date startDate, Date endDate, String products[], Double[] sales) {
		PieColors pc = new PieColors();
		// Colors Color
		Color dropShadow = new Color(240, 240, 240);
		// inner padding to make sure bars never touch the outer border
		int innerOffset = 20;
		// Set the graph?s outer width & height
		int WIDTH = 400;
		int HEIGHT = 200;
		int pieHeight = HEIGHT - (innerOffset * 2);
		int pieWidth = pieHeight;
		// To make a square (circular) pie
		int halfWidth = WIDTH / 2;
		// Width of the inner graphable area
		int innerWIDTH = WIDTH - (innerOffset * 2);
		// graph dimensions Dimension
		Dimension graphDim = new Dimension(WIDTH, HEIGHT);
		Rectangle graphRect = new Rectangle(graphDim);
		// border dimensions
		Dimension borderDim = new Dimension(halfWidth - 2, HEIGHT - 2);
		Rectangle borderRect = new Rectangle(borderDim);

		// ///////////////////////////////////////////////////////////
		// Set up the graph
		// //////////////////////////////////////////////////////////
		// Set content type
		// Create BufferedImage & Graphics2D
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bi.createGraphics();
		// Set Antialiasing RenderingHints
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(renderHints);
		// Set graph background color to white:
		g2d.setColor(Color.white);
		g2d.fill(graphRect);
		// Draw black border
		g2d.setColor(Color.black);
		borderRect.setLocation(1, 1);
		g2d.draw(borderRect);
		// Now draw border for legend
		borderRect.setLocation((WIDTH / 2) + 1, 1);
		g2d.draw(borderRect);
		// //////////////////////////////////////////////////////////////////
		// Draw data onto the graph:
		// //////////////////////////////////////////////////////////////////
		int x_pie = innerOffset;
		int y_pie = innerOffset;
		int border = 20;
		// Main chart Ellipse
		// Ellipse2D.Double el = new Ellipse2D.Double(x_pie, y_pie, pieWidth,
		// pieHeight);
		Ellipse2D.Double elb = new Ellipse2D.Double(x_pie - border / 2, y_pie - border / 2, pieWidth + border, pieHeight + border);
		// Shadow
		g2d.setColor(dropShadow);
		g2d.fill(elb);
		// Border
		g2d.setColor(Color.black);
		g2d.draw(elb);

		// ///////////////////////////////////////////////////////////////
		// Calculate the total sales
		// ///////////////////////////////////////////////////////////////
		float salesTotal = 0.0f;
		int lastElement = 0;
		for (int i = 0; i < products.length; i++) {
			if (sales[i] > 0.0f) {
				salesTotal += sales[i];
				lastElement = i;
			}
		}
		int startAngle = 0;
		// Legend variables
		int legendWidth = 20;
		int x_legendText = halfWidth + innerOffset / 2 + legendWidth + 5;
		int x_legendBar = halfWidth + innerOffset / 2;
		int textHeight = 20;
		int curElement = 0;
		int y_legend = 0;
		// Dimensions of the legend bar
		Dimension legendDim = new Dimension(legendWidth, textHeight / 2);
		Rectangle legendRect = new Rectangle(legendDim);
		for (int i = 0; i < products.length; i++) {
			if (sales[i] > 0.0f) {
				// Calculate percentage sales float
				double perc = (sales[i] / salesTotal);
				// Calculate new angle
				int sweepAngle = (int) (perc * 360);
				// Check that the last element goes back to 0 position
				if (i == lastElement) {
					sweepAngle = 360 - startAngle;
				}
				// Draw Arc
				g2d.setColor(pc.getPieColor());
				g2d.fillArc(x_pie, y_pie, pieWidth, pieHeight, startAngle, sweepAngle);
				// Increment startAngle with the sweepAngle
				startAngle += sweepAngle;
				// ///////////
				// Draw Legend
				// ///////////
				// Set y position for bar
				y_legend = curElement * textHeight + innerOffset;
				// Display the current product
				String display = products[i];
				g2d.setColor(Color.black);
				g2d.drawString(display, x_legendText, y_legend);
				// Display the total sales
				display = "" + (int) (sales[i] + 0);
				g2d.setColor(Color.black);
				g2d.drawString(display, x_legendText + 80, y_legend);
				// Display the sales percentage
				display = " (" + (int) (perc * 100) + "%)";
				g2d.setColor(Color.red);
				g2d.drawString(display, x_legendText + 110, y_legend);
				// Draw the bar
				g2d.setColor(pc.getPieColor());
				legendRect.setLocation(x_legendBar, y_legend - textHeight / 2);
				g2d.fill(legendRect);
				// Set new pie color
				pc.setNewColor();
				// Increment
				curElement++;
			}
		}
		// //////////////////////////////////////////////
		// Encode the graph
		// ///////////////////////////////////////
		try {
			long time = System.currentTimeMillis();
			String path = StringUtil.getPath(new String[] { ContextHolder.getServletPath(), ContextHolder.getImagePath(), bond + startDate.getTime() + endDate.getTime() + time + ".jpg" });

			OutputStream output = new FileOutputStream(new File(path));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			encoder.encode(bi);
			output.close();
			return ContextHolder.getImagePath().replace("\\", "/") + "/" + bond + startDate.getTime() + endDate.getTime() + time + ".jpg";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ContextHolder.getImagePath().replace("\\", "/") + "/" + "error.jpg";
	}

	public static class PieColors {

		Color pieColorArray[] = {
		// new Color(210, 60, 60),
				// new Color(60, 210, 60),
				// new Color(60, 60, 210),
				// new Color(120, 60, 120),
				// new Color(60, 120, 210),
				// new Color(210, 120, 60)
				new Color(56,94,15),
				new Color(210,105,30),
				
				new Color(65,105,225),
				
				new Color(115,74,18), 
				
				new Color(255,69,0), 
				
				new Color(3,168,158), 
				
				new Color(218,112,214),
				
				new Color(107,142,35), 
				
				new Color(237,145,33), 
				
				new Color(112,128,105), 
		};
		int curPieColor = 0;

		public Color getPieColor() {
			return pieColorArray[curPieColor];
		}

		public void setNewColor() {
			curPieColor++;
			if (curPieColor >= pieColorArray.length) {
				curPieColor = 0;
			}
		}
	}

	public List<Double> getMyExpectedExcessReturns() {
		return myExpectedExcessReturns;
	}

	public void setMyExpectedExcessReturns(List<Double> myExpectedExcessReturns) {
		this.myExpectedExcessReturns = myExpectedExcessReturns;
	}

}