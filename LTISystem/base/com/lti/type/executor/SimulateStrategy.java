package com.lti.type.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lti.Exception.Executor.SimulateException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.Exception.Strategy.ParameterException;
import com.lti.MutualFundTool.WeightEstimate;
import com.lti.executor.Simulator;
import com.lti.listener.PrintLogProcessor;
import com.lti.service.AssetClassManager;
import com.lti.service.BaseFormulaManager;
import com.lti.service.CloningCenterManager;
import com.lti.service.HolidayManager;
import com.lti.service.IndicatorManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.GlobalObject;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Mycomparator;
import com.lti.type.Quaternion;
import com.lti.type.SecurityValue;
import com.lti.type.SortType;
import com.lti.type.SourceType;
import com.lti.type.TimePeriod;
import com.lti.type.TimeUnit;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public abstract class SimulateStrategy {
	public final static int RUNNING_MODE_MONITOR = 0;

	public final static int RUNNING_MODE_UPDATE = 1;

	protected int RunningMode = -1;

	protected Date CurrentDate;

	protected HoldingInf SimulateHolding;

	@Deprecated
	protected SimulateStrategy CurrentPortfolio = this;

	@Deprecated
	protected String curAsset;

	protected double version = 1.0;

	public double getVersion() {
		return version;
	}

	protected Portfolio SimulatePortfolio;
	protected Asset SimulateAsset;

	protected Simulator _simulator;

	public void setSimulator(Simulator simulator) {
		_simulator = simulator;
	}

	protected AssetClassManager assetClassManager;
	protected PortfolioManager portfolioManager;
	protected SecurityManager securityManager;
	protected StrategyManager strategyManager;
	protected HolidayManager holidayManager;
	protected IndicatorManager indicatorManager;
	protected BaseFormulaManager baseFormulaManager;
	protected CloningCenterManager cloneCenterManager;

	public SimulateStrategy() {
		assetClassManager = (AssetClassManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		portfolioManager = (PortfolioManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		securityManager = (SecurityManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		strategyManager = (StrategyManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		holidayManager = (HolidayManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
		indicatorManager = (IndicatorManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
		baseFormulaManager = ContextHolder.getBaseFormulaManager();
		cloneCenterManager = new CloningCenterManager();
	}

	public long PortfolioID;
	public long StrategyID;
	public long StrategyClassID;

	protected Map<String, String> SimulateParameters;

	protected Map<String, String> DefaultParameters;
	
	public void initDefaultParameters(List<Quaternion> list){
		DefaultParameters=new HashMap<String, String>();
		if(list!=null){
			for(Quaternion q:list){
				DefaultParameters.put(q.getSecond(), q.getThird());
			}
		}
	}
	
	public void before() throws Exception {

	}

	public abstract void init() throws Exception;

	public abstract void action() throws Exception, NoPriceException;

	public abstract void reinit() throws Exception, NoPriceException;

	public void end() throws Exception {

	}

	public void afterExecuting() throws Exception {

	}

	public void afterUpdating() throws Exception {

	}

	public abstract void writeObject(ObjectOutputStream stream) throws IOException;

	public abstract void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException;

	public boolean checkVersion() {
		String oldversion=SimulateParameters.get("version");
		if(oldversion!=null&&!oldversion.equals("")){
			if(Double.parseDouble(oldversion)!=this.version)return false;
		}
		return true;
	}

	public void fetchParameters() throws ParameterException {
	}
	

	public int getRunningMode() {
		return RunningMode;
	}

	public void setRunningMode(int runningMode) {
		RunningMode = runningMode;
	}

	public Date getCurrentDate() {
		return CurrentDate;
	}

	public void setCurrentDate(Date currentDate) {
		CurrentDate = currentDate;
	}

	public HoldingInf getSimulateHolding() {
		return SimulateHolding;
	}

	public void setSimulateHoldings(HoldingInf simulateHolding) {
		SimulateHolding = simulateHolding;
	}

	public String getCurAsset() {
		return curAsset;
	}

	public void setCurAsset(String curAsset) {
		this.curAsset = curAsset;
	}

	public Portfolio getSimulatePortfolio() {
		return SimulatePortfolio;
	}

	public void setSimulatePortfolio(Portfolio simulatePortfolio) {
		SimulatePortfolio = simulatePortfolio;
	}

	public Asset getSimulateAsset() {
		return SimulateAsset;
	}

	public void setSimulateAsset(Asset simulateAsset) {
		SimulateAsset = simulateAsset;
	}

	public long getPortfolioID() {
		return PortfolioID;
	}

	public void setPortfolioID(long portfolioID) {
		PortfolioID = portfolioID;
	}

	public Map<String, String> getSimulateParameters() {
		return SimulateParameters;
	}

	public void setSimulateParameters(Map<String, String> simulateParameters) {
		SimulateParameters = simulateParameters;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public void addAsset(Asset currentAsset) throws SimulateException {
		this.SimulateHolding.addAsset(currentAsset);
		this.SimulateHolding.refreshAmounts();
	}

	public void buy(String assetname, String symbol, double amount) throws NoPriceException, SimulateException {
		Security s = securityManager.get(symbol);
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.baseBuy(assetname, s.getID(), s.getSymbol(), amount, open, close, true, Configuration.TRANSACTION_TYPE_REAL);
	}

	private PrintLogProcessor logProccessor;

	public void setLogProccessor(PrintLogProcessor logProccessor) {
		this.logProccessor = logProccessor;
	}

	public void printToLog(double[] message) {
		if (message == null)
			return;
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = "";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String[] message) {
		if (message == null)
			return;
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = "";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(int[] message) {
		if (message == null)
			return;
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = "";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(Object message) {
		if (message == null)
			return;
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		log.setMessage(String.valueOf(message));
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, Object[] message) {
		if (message == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, Map m) {
		if (m == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		Iterator<String> keys = m.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			s += key + ": " + String.valueOf(m.get(key)) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String message) {
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		log.setMessage(message);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, double[] message) {
		if (message == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, float[] message) {
		if (message == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, boolean[] message) {
		if (message == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, int[] message) {
		if (message == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(String name, String[] message) {
		if (message == null)
			return;

		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = name + "\r\n\r\n";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	@Deprecated
	public double getTotalAmount(Date currentDate2) {
		return SimulateHolding.getAmount();
	}

	@Deprecated
	public void sellAssetCollection(Date currentDate2) throws SimulateException {
		SimulateHolding.sellAssetCollection();
	}

	/**
	 * get the strategy's id by its name
	 * 
	 * @param strategyName
	 * @return
	 */
	public long getStrategyID(String strategyName) {
		if (strategyName != null && strategyName.equalsIgnoreCase("static")) {
			return 0;
		}
		Strategy s = strategyManager.get(strategyName);
		if (s != null)
			return s.getID();
		else
			return 0;
	}

	/**
	 * get the asset class's id by its name
	 * 
	 * @param assetClassName
	 *            Asset Class
	 * @return ID
	 */
	@Deprecated
	public AssetClass getAssetClassID(String assetClassName) {
		AssetClass ac = assetClassManager.get(assetClassName);
		return ac;
	}

	/**
	 * if today is the month end, it will return true
	 * 
	 * @param date
	 * @return
	 */
	public boolean isMonthEnd(java.util.Date date) {
		Date yearEnd = com.lti.util.LTIDate.getLastNYSETradingDayOfMonth(date);
		return com.lti.util.LTIDate.equals(date, yearEnd);
	}

	/**
	 * if today is the quarter end , it will return true
	 * 
	 * @return
	 */
	public boolean isQuarterEnd(java.util.Date date) {
		return com.lti.util.LTIDate.isQuarterEnd(date);

	}

	/**
	 * if today is the year end, it will return true;
	 * 
	 * @return
	 */
	public boolean isYearEnd(java.util.Date date) {
		Date yearEnd = com.lti.util.LTIDate.getLastNYSETradingDayOfYear(date);
		return com.lti.util.LTIDate.equals(date, yearEnd);
	}

	public Security getSecurity(long id) {
		Security se = securityManager.get(id);
		return se;
	}

	public Security getSecurity(String name) {
		Security se = securityManager.get(name);
		return se;
	}

	public Date getStartDate(String name) {
		Security se = getSecurity(name);
		return securityManager.getStartDate(se.getID());
	}

	public Date getEndDate(String name) {
		Security se = getSecurity(name);
		return securityManager.getEndDate(se.getID());
	}

	@Deprecated
	public void buy(String assetname, String symbol, double amount, Date currentDate2) throws Exception, SimulateException {
		this.buy(assetname, symbol, amount);
	}

	@Deprecated
	public void shortSell(String assetname, String symbol, double amount, Date currentDate) throws Exception {
		Security s = securityManager.get(symbol);
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.shortSell(assetname, s.getID(), s.getSymbol(), amount, close, open);
	}

	@Deprecated
	public double getAssetAmount(String assetname, Date CurrentDate) {
		return SimulateHolding.getAsset(assetname).getAmount();
	}

	@Deprecated
	public void sell(String assetname, String symbol, double amount, Date currentDate2) throws SimulateException {
		Security s = securityManager.get(symbol);
		SimulateHolding.baseSell(assetname, s.getID(), s.getSymbol(), amount, Configuration.TRANSACTION_TYPE_REAL);
	}

	@Deprecated
	public void sellAsset(String assetname, Date currentDate2) throws SimulateException {
		SimulateHolding.sellAsset(assetname);
	}
	@Deprecated
	public void sellAssetAtClose(String assetname, Date currentDate2) throws SimulateException {
		SimulateHolding.sellAsset(assetname);
	}

	public Date getEarliestAvaliablePriceDate(Security se) {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Date date = securityManager.getStartDate(se.getID());
		if (date == null) {
			date = new Date(8000, 1, 1);
		}
		return date;
	}

	public void sellAssetCollection() throws SimulateException{
		SimulateHolding.sellAssetCollection();
	}
	/**
	 * physical date
	 * 
	 * @return current Month
	 */
	public int CurrentMonth() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		return month;
	}

	/**
	 * get all mutual funds
	 * 
	 * @return Mutual Funds
	 */
	public java.util.List<Security> getAllMutualFunds() {
		return securityManager.getMutualFunds();
	}

	/**
	 * return the date of previous year
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getPreviousYear(Date CurrentDate) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(CurrentDate);
		ca.set(ca.get(Calendar.YEAR) - 1, ca.get(Calendar.MONTH), ca.get(Calendar.DATE), 0, 0, 0);
		Date lastYear = ca.getTime();
		return lastYear;
	}

	/**
	 * get the security's name by its id
	 * 
	 * @param securityName
	 * @return ID
	 */
	public long getSecurityID(String securityName) {
		// TODO Auto-generated method stub
		Security s = this.getSecurity(securityName);
		if (s != null)
			return s.getID();
		else
			return 0;
	}

	/**
	 * get securities by the names
	 * 
	 * @param nameArray
	 * @return
	 */
	public List<Security> getSecurityList(String[] nameArray) {
		if (nameArray != null) {
			List<Security> list = new ArrayList<Security>();
			for (int i = 0; i < nameArray.length; i++) {
				Security s = getSecurity(nameArray[i]);
				if (s != null)
					list.add(s);
			}
			return list;
		}
		return null;
	}

	/**
	 * get portfolios by names
	 * 
	 * @param nameArray
	 * @return
	 */
	public List<Portfolio> getPortfolioList(String[] nameArray) {
		if (nameArray != null) {
			List<Portfolio> list = new ArrayList<Portfolio>();
			for (int i = 0; i < nameArray.length; i++) {
				Portfolio s = getPortfolio(nameArray[i]);
				if (s != null)
					list.add(s);
			}
			return list;
		}
		return null;
	}

	/**
	 * get a portfolio from database by its symbol
	 * 
	 * @param symbol
	 * @return
	 */
	public Portfolio getPortfolio(String sybmol) {
		Portfolio port = portfolioManager.get(sybmol);
		return port;
	}

	public Indicator getIndicator(long id) {
		Indicator in = indicatorManager.get(id);
		return in;
	}

	public Indicator getIndicator(String name) {
		Indicator in = indicatorManager.get(name);
		return in;
	}

	public void printToLog(Transaction tran) {
		StringBuffer sb = new StringBuffer();
		sb.append("Date: " + tran.getDate());
		sb.append("\n");
		sb.append("Operation:" + tran.getOperation());
		sb.append("\n");
		sb.append("Amount: " + tran.getAmount());
		sb.append("\n");
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		log.setMessage(sb.toString());
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public void printToLog(Object[] message) {
		if (message == null)
			return;
		Log log = new Log();
		log.setStrategyID(this.StrategyID);
		log.setPortfolioID(this.PortfolioID);
		log.setLogDate(this.CurrentDate);
		String s = "";
		for (int i = 0; i < message.length; i++) {
			s += String.valueOf(message[i]) + "\r\n";
		}
		log.setMessage(s);
		if (logProccessor != null)
			logProccessor.printToLog(log);
	}

	public String getStackTraceString(Throwable e) {

		return com.lti.util.StringUtil.getStackTraceString(e);
	}

	public List<Security> getAssetSecurity(long assetID) {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		return securityManager.getSecuritiesByAsset(assetID);
	}

	public List<Security> getAssetSecurity(String name) {
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		long assetID = assetClassManager.get(name).getID();
		return this.getAssetSecurity(assetID);
	}

	public List<Security> getSecurityByParentClass(long assetID) {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		return securityManager.getSecuritiesByClass(assetID);
	}

	public List<Security> getCEF(long assetID) {
		return securityManager.getCEF(assetID);
	}

	public List<Security> getCEF(Asset asset) {
		return securityManager.getCEF(asset.getAssetClassID());
	}

	public List<Security> getSecurityByClass(long assetID) {
		return securityManager.getSecuritiesByAsset(assetID);
	}

	public List<Security> getSecurityByClass(Asset asset) {
		return securityManager.getSecuritiesByAsset(asset.getAssetClassID());
	}

	public List<Security> getMutualFundByClass(Asset asset) {
		return securityManager.getMutualFund(asset.getAssetClassID());
	}

	public List<Security> getMutualFundByClass(long assetID) {
		return securityManager.getMutualFund(assetID);
	}

	public AssetClass getAssetClass(String name) {
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		return assetClassManager.get(name);
	}

	/**
	 * 
	 * @param securityList
	 * @param benchmark
	 * @param startDate
	 * @param endDate
	 * @param st
	 * @param nav
	 * @return added by CCD on 2010-03-02 for getting Alpha with benchMark
	 */
	private List<SecurityValue> getSecurityValueList(List<Security> securityList, String benchmark, Date startDate, Date endDate, SortType st, boolean nav) {
		List<SecurityValue> svList = new ArrayList<SecurityValue>();
		Long benchMarkID = null;
		TimeUnit tu = TimeUnit.DAILY;
		TimePeriod p = new TimePeriod(startDate, endDate);
		if (securityList == null)
			return null;
		if (benchmark != null) {
			Security benchFund = securityManager.getBySymbol(benchmark);
			if (benchFund != null)
				benchMarkID = benchFund.getID();
		}
		int size = securityList.size();
		for (int i = 0; i < size; i++) {
			SecurityValue sv = new SecurityValue();
			Security se = securityList.get(i);
			if (se == null)
				continue;
			Date start = se.getStartDate();
			Date end = se.getEndDate();
			if (start == null || startDate == null)
				continue;
			if (end == null || endDate == null)
				continue;
			if (start.after(startDate))
				continue;
			if (end.before(endDate))
				continue;
			sv.name = se.getSymbol();
			double value = 0;
			if (st == SortType.RETURN) {
				try {
					value = se.getReturn(startDate, endDate, nav);
				} catch (Exception e) {
					if (nav) {
						try {
							value = se.getReturn(p);
						} catch (Exception ee) {
							System.out.println(StringUtil.getStackTraceString(ee));
							continue;
						}
					} else
						continue;
				}
			} else {
				/***
				 * if nav is true, judge if it has NAV before we calculate,if
				 * not ,we will set it false for the security
				 ***/
				try {
					boolean tmpNav = nav;
					if (tmpNav) {
						Date tempStartDate = LTIDate.getNewTradingDate(endDate, tu, -10);
						List<Double> tmpReturnList = baseFormulaManager.getReturns(tempStartDate, endDate, tu, se.getID(), SourceType.SECURITY_RETURN, nav);
						if (tmpReturnList != null && tmpReturnList.size() > 0 && tmpReturnList.get(0) == 0.0)
							tmpNav = false;
					}
					if (st == SortType.ALPHA)
						value = se.getAlpha(startDate, endDate, tu, benchMarkID, tmpNav);
					if (st == SortType.BETA)
						value = se.getBeta(startDate, endDate, tu, tmpNav);
					if (st == SortType.ANNULIZEDRETURN)
						value = se.getAnnualizedReturn(startDate, endDate, tmpNav);
					if (st == SortType.DRAWDOWN)
						value = se.getDrawDown(startDate, endDate, tu, tmpNav);
					if (st == SortType.SHARPE)
						value = se.getSharpeRatio(startDate, endDate, tu, tmpNav);
					if (st == SortType.RSQUARED)
						value = se.getRSquared(startDate, endDate, tu, tmpNav);
					if (st == SortType.STANDARDDIVIATION)
						value = se.getStandardDeviation(startDate, endDate, tu, tmpNav);
					if (st == SortType.TREYNOR)
						value = se.getTreynorRatio(startDate, endDate, tu, tmpNav);
					if (st == SortType.DISCOUNTRATE)
						value = se.getAverageDiscountRate(startDate, endDate, tu);
				} catch (Exception e) {
					continue;
				}
			}
			sv.value = value;
			svList.add(sv);
		}
		return svList;

	}

	/**
	 * *************************************************************************
	 * ************
	 */
	/**
	 * return security with value calculated. modified by CCD last modified on
	 * 2009-11-20 if nav is true,use NAV first and if NAV is null,then use price
	 * instead.
	 */
	private List<SecurityValue> getSecurityValueList(List<Security> securityList, Date startDate, Date endDate, SortType st, boolean nav) {
		return this.getSecurityValueList(securityList, null, startDate, endDate, st, nav);
	}

	/**
	 * 
	 * @param count
	 * @param startDate
	 * @param endDate
	 * @param sortType
	 * @param tu
	 * @param bench
	 * @param nav
	 * @return
	 * @throws NoPriceException
	 *             added by CCD on 2010-03-02
	 */
	public List<Security> getTopSecurity(int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu, String bench, boolean nav) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Security> securityList = new ArrayList<Security>();
		securityList = securityManager.getSecurities();

		List<SecurityValue> svList = new ArrayList<SecurityValue>();

		svList = this.getSecurityValueList(securityList, bench, startDate, endDate, sortType, nav);

		if (svList == null || svList.size() == 0)
			return null;

		Mycomparator comparator = new Mycomparator();
		Collections.sort(svList, comparator);

		securityList.clear();

		count = count < svList.size() ? count : svList.size();

		for (int i = 0; i < count; i++) {
			securityList.add(securityManager.getBySymbol(svList.get(i).name));
		}

		return securityList;
	}

	/**
	 * return the Top count security order by sortType, it orders all the
	 * securities in database. modified by CCD
	 * **/
	@SuppressWarnings("unchecked")
	public List<Security> getTopSecurity(int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu, boolean nav) throws NoPriceException {
		return this.getTopSecurity(count, startDate, endDate, sortType, tu, null, nav);
	}

	/**
	 * return the Top count security order by sortType,it orders the security
	 * which's id in securityID
	 * 
	 * @author CCD run for test.
	 * **/
	public List<Security> testGetTopSecurity(int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu, boolean nav) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Security> securityList = new ArrayList<Security>();
		Security security;
		Long[] securityID = { 191L, 195L, 196L, 66L, 190L, 338L, 4619L };
		for (int i = 0; i < 7; ++i) {
			security = securityManager.get(securityID[i]);
			securityList.add(security);
		}
		List<SecurityValue> svList = new ArrayList<SecurityValue>();
		svList = this.getSecurityValueList(securityList, startDate, endDate, sortType, nav);

		if (svList == null || svList.size() == 0)
			return null;

		Mycomparator comparator = new Mycomparator();
		Collections.sort(svList, comparator);

		securityList.clear();
		count = count < svList.size() ? count : svList.size();
		for (int i = 0; i < count; i++)
			securityList.add(securityManager.getBySymbol(svList.get(i).name));

		return securityList;

	}

	/**
	 * 
	 * @param count
	 * @param time
	 * @param currentDate
	 * @param tu
	 * @param sortType
	 * @param bench
	 * @param nav
	 * @return
	 * @throws NoPriceException
	 *             added by CCD on 2010-03-02
	 */
	public List<Security> getTopSecurity(int count, int time, Date currentDate, TimeUnit tu, SortType sortType, String bench, boolean nav) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Date startDate = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(currentDate);
		if (time > 0)
			time = time * -1;
		if (tu == TimeUnit.DAILY)
			startDate = LTIDate.getNewNYSETradingDay(currentDate, time);
		else if (tu == TimeUnit.WEEKLY)
			startDate = LTIDate.getNewNYSEWeek(currentDate, time);
		else if (tu == TimeUnit.MONTHLY)
			startDate = LTIDate.getNewNYSEMonth(currentDate, time);
		else if (tu == TimeUnit.YEARLY)
			startDate = LTIDate.getNewNYSEYear(currentDate, time);

		List<Security> securityList = new ArrayList<Security>();
		securityList = securityManager.getSecurities();

		List<SecurityValue> svList = new ArrayList<SecurityValue>();

		svList = this.getSecurityValueList(securityList, bench, startDate, currentDate, sortType, nav);

		if (svList == null || svList.size() == 0)
			return null;

		Mycomparator comparator = new Mycomparator();
		Collections.sort(svList, comparator);

		securityList.clear();
		count = count < svList.size() ? count : svList.size();

		for (int i = 0; i < count; i++) {
			securityList.add(securityManager.getBySymbol(svList.get(i).name));
		}

		return securityList;
	}

	@SuppressWarnings("unchecked")
	public List<Security> getTopSecurity(int count, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException {
		return this.getTopSecurity(count, time, currentDate, tu, sortType, null, nav);
	}

	/**
	 * 
	 * @param names
	 * @param bench
	 * @param time
	 * @param currentDate
	 * @param tu
	 * @param sortType
	 * @param nav
	 * @return
	 * @throws NoPriceException
	 *             added by CCD on 2010-03-02
	 */
	public String[] getTopSecurityArray(String[] names, String bench, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Date startDate = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(currentDate);
		if (time > 0)
			time = time * -1;
		if (tu == TimeUnit.DAILY)
			// ca.add(Calendar.DAY_OF_YEAR, time);
			startDate = LTIDate.getNewNYSETradingDay(currentDate, time);
		else if (tu == TimeUnit.WEEKLY)
			// ca.add(Calendar.WEEK_OF_YEAR, time);
			startDate = LTIDate.getNewNYSEWeek(currentDate, time);
		else if (tu == TimeUnit.MONTHLY)
			// ca.add(Calendar.MONTH, time);
			startDate = LTIDate.getNewNYSEMonth(currentDate, time);
		else if (tu == TimeUnit.YEARLY)
			// ca.add(Calendar.YEAR, time);
			startDate = LTIDate.getNewNYSEYear(currentDate, time);
		// startDate = ca.getTime();

		List<Security> securityList = new ArrayList<Security>();
		// securityList = securityManager.getSecurities();
		for (int i = 0; i < names.length; i++)
			securityList.add(securityManager.get(names[i]));

		List<SecurityValue> svList = new ArrayList<SecurityValue>();

		svList = this.getSecurityValueList(securityList, bench, startDate, currentDate, sortType, nav);

		if (svList == null || svList.size() == 0)
			return null;

		Mycomparator comparator = new Mycomparator();
		Collections.sort(svList, comparator);
		String[] returns = new String[securityList.size()];
		for (int i = 0; i < svList.size(); i++) {
			returns[i] = svList.get(i).name;
		}
		return returns;
	}

	public String[] getTopSecurityArray(String[] names, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException {
		return this.getTopSecurityArray(names, null, time, currentDate, tu, sortType, nav);
	}

	/**
	 * 
	 * @param securityList
	 * @param count
	 * @param time
	 * @param currentDate
	 * @param tu
	 * @param sortType
	 * @param nav
	 * @return
	 * @throws NoPriceException
	 *             added by CCD on 2010-03-02
	 */
	public List<Security> getTopSecurity(List<Security> securityList, String benchmark, int count, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		Date startDate = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(currentDate);
		if (time > 0)
			time = time * -1;
		if (tu == TimeUnit.DAILY)
			startDate = LTIDate.getNewNYSETradingDay(currentDate, time);
		else if (tu == TimeUnit.WEEKLY)
			startDate = LTIDate.getNewNYSEWeek(currentDate, time);
		else if (tu == TimeUnit.MONTHLY)
			startDate = LTIDate.getNewNYSEMonth(currentDate, time);
		else if (tu == TimeUnit.YEARLY)
			startDate = LTIDate.getNewNYSEYear(currentDate, time);

		List<SecurityValue> svList = new ArrayList<SecurityValue>();

		svList = this.getSecurityValueList(securityList, startDate, currentDate, sortType, nav);

		if (svList == null || svList.size() == 0)
			return null;

		Mycomparator comparator = new Mycomparator();
		Collections.sort(svList, comparator);

		// securityList.clear();
		List<Security> seList = new ArrayList<Security>();

		count = count < svList.size() ? count : svList.size();

		for (int i = 0; i < count; i++) {
			seList.add(securityManager.getBySymbol(svList.get(i).name));
		}

		return seList;
	}

	public List<Security> getTopSecurity(List<Security> securityList, int count, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException {
		return getTopSecurity(securityList, null, count, time, currentDate, tu, sortType, nav);
	}

	public int getCurrentYear() {
		Calendar ca = Calendar.getInstance();
		ca.setTime(CurrentDate);
		return ca.get(Calendar.YEAR);
	}

	public int getCurrentMonth() {
		Calendar ca = Calendar.getInstance();
		ca.setTime(CurrentDate);
		return ca.get(Calendar.MONTH)+1;
	}

	/**
	 * 此函数需要重写
	 * 
	 * @param portfolioList
	 * @param startDate
	 * @param endDate
	 * @param st
	 * @param tu
	 * @return
	 * @throws NoPriceException
	 */
	private List<SecurityValue> getPortfolioValueList(List<Portfolio> portfolioList, Date startDate, Date endDate, SortType st, TimeUnit tu) throws NoPriceException {
		return null;
		// List<SecurityValue> svList = new ArrayList<SecurityValue>();
		//
		// TimePeriod p = new TimePeriod(startDate, endDate);
		//
		// if(portfolioList == null)return null;
		//		
		// for (int i = 0; i < portfolioList.size(); i++) {
		// SecurityValue sv = new SecurityValue();
		// Portfolio po = portfolioList.get(i);
		//
		// if(po == null)continue;
		//			
		// long portfolioID = po.getID();
		//			
		// Date start = portfolioManager.getEarliestDate(portfolioID);
		//			
		// Date end = portfolioManager.getLatestDate(portfolioID);
		//
		// if(start == null || startDate == null)
		// continue;
		//			
		// if(end == null || endDate == null)
		// continue;
		//			
		// if (start.after(startDate))
		// continue;
		//
		// if(end.before(endDate))
		// continue;
		//			
		// sv.name = po.getSymbol();
		// double value = 0;
		// if (st == SortType.RETURN) {
		// try {
		// List<PortfolioDailyData> tmpList =
		// portfolioManager.getDailydatasByPeriod(po.getID(), startDate,
		// endDate);
		// value =
		// (tmpList.get(tmpList.size()-1).getAmount()-tmpList.get(0).getAmount())/tmpList.get(0).getAmount();
		//					
		// } catch (Exception e) {
		// //e.printStackTrace();
		// System.out.println(StringUtil.getStackTraceString(e));
		// }
		// }
		// tu = TimeUnit.DAILY;
		// if (st == SortType.ALPHA)
		// value = po.getAlpha(startDate, endDate, tu);
		// if (st == SortType.BETA)
		// value = po.getBeta(startDate, endDate, tu);
		// if (st == SortType.ANNULIZEDRETURN)
		// {
		// try {
		// value = po.getAnnualizedReturn(startDate, endDate);
		// } catch (PortfolioException e) {
		// //e.printStackTrace();
		// System.out.println(StringUtil.getStackTraceString(e));
		// }
		// }
		// if (st == SortType.DRAWDOWN)
		// value = po.getDrawDown(startDate, endDate, tu);
		// if (st == SortType.SHARPE)
		// value = po.getSharpeRatio(startDate, endDate, tu);
		// if (st == SortType.RSQUARED)
		// value = po.getRSquared(startDate, endDate, tu);
		// if (st == SortType.STANDARDDIVIATION)
		// value = po.getStandardDeviation(startDate, endDate, tu);
		// if (st == SortType.TREYNOR)
		// value = po.getTreynorRatio(startDate, endDate, tu);
		//			
		// sv.value = value;
		//
		// svList.add(sv);
		// }
		//
		// return svList;
	}

	public List<Portfolio> getTopPortfolio(List<String> poList, int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu) {
		List<Portfolio> plist = new ArrayList<Portfolio>();

		List<SecurityValue> newList = new ArrayList<SecurityValue>();

		List<Portfolio> poList1 = new ArrayList<Portfolio>();
		for (int i = 0; i < count; i++) {
			Portfolio tmpPortfoio = portfolioManager.get(poList.get(i));
			if (tmpPortfoio != null)
				poList1.add(tmpPortfoio);
		}

		poList1 = this.removeSamePortfolio(poList1);

		try {
			newList = this.getPortfolioValueList(poList1, startDate, endDate, sortType, tu);
		} catch (NoPriceException e) {
			// e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
		}

		if (newList == null || newList.size() == 0)
			return null;

		if (sortType != SortType.SHARPE) {
			Mycomparator comparator = new Mycomparator();
			Collections.sort(newList, comparator);
		}

		count = count < newList.size() ? count : newList.size();

		poList1 = new ArrayList<Portfolio>();
		for (int i = 0; i < count; i++) {
			poList1.add(portfolioManager.get(newList.get(i).name));
		}

		return plist;
	}

	/**
	 * 如何判断两个portfolio是否一致
	 * 
	 * @param poList
	 * @return
	 */
	private List<Portfolio> removeSamePortfolio(List<Portfolio> poList) {
		List<Portfolio> newList = new ArrayList<Portfolio>();
		boolean[] flag = new boolean[poList.size()];
		for (int i = 0; i < poList.size(); i++) {
			Portfolio tmp1 = poList.get(i);
			for (int j = i + 1; j < poList.size(); j++) {
				if (flag[j])
					continue;
				Portfolio tmp2 = poList.get(i);
				if (tmp1.getID().equals(tmp2.getID())) {
					int index = this.chooseLongerPortfolio(tmp1, tmp2);
					if (index == 2)
						flag[j] = true;
					else
						flag[i] = true;
				}
			}
			if (!flag[i])
				newList.add(tmp1);
		}

		return newList;
	}

	private int chooseLongerPortfolio(Portfolio p1, Portfolio p2) {
		Date sd1 = p1.getStartingDate();
		Date ed1 = p1.getEndDate();
		Date sd2 = p2.getStartingDate();
		Date ed2 = p2.getEndDate();
		int interval1 = LTIDate.calculateInterval(sd1, ed1);
		int interval2 = LTIDate.calculateInterval(sd2, ed2);
		if (interval1 > interval2)
			return 1;
		else
			return 2;
	}

	/**
	 * *************************************************************************
	 * *************
	 */

	protected WeightEstimate ws;

	public void setRAALimit(double[] upper, double[] lower) {
		if (ws == null) {
			ws = new WeightEstimate();
		}
		ws.setLower(lower);
		ws.setUpper(upper);
	}

	public double[] translateDoubleArray(Double[] ds) {
		if (ds == null || ds.length == 0)
			return null;
		double[] doubles = new double[ds.length];
		for (int i = 0; i < ds.length; i++) {
			doubles[i] = ds[i] == null ? 0.0 : ds[i];
		}
		return doubles;
	}

	public double[] RAA(int interval, Date date, TimeUnit tu, String MF, String[] index, boolean allowShort) throws Exception {
		if (ws == null) {
			ws = new WeightEstimate();
		}
		return translateDoubleArray(ws.RAA(interval, date, tu, MF, index, allowShort, true));
	}

	public double[] RAA(int interval, Date date, TimeUnit tu, String MF, String[] index) throws Exception {
		if (ws == null) {
			ws = new WeightEstimate();
		}
		return translateDoubleArray(ws.RAA(interval, date, tu, MF, index, false, true));
	}

	/**************************************************************************************************/
	private List<List<Double>> trigerList = new ArrayList<List<Double>>();
	private List<String> ruleList = new ArrayList<String>();
	private HashMap<String, Double> nameMap = new HashMap<String, Double>();
	private HashMap<String, Date> dateMap = new HashMap<String, Date>();

	public void confidenceCheckStart(String ruleName, double startAmount, Date startDate) {
		int index = ruleList.indexOf(ruleName);
		if (index < 0) {
			ruleList.add(ruleName);
			List<Double> tmpList = new ArrayList<Double>();
			trigerList.add(tmpList);
		}
		nameMap.put(ruleName, startAmount);
		dateMap.put(ruleName, startDate);
	}

	public void confidenceCheckEnd(String ruleName, double endAmount, Date endDate) {
		int index = ruleList.indexOf(ruleName);
		if (index < 0) {
			return;
		}
		double preAmount = nameMap.get(ruleName);
		Date startDate = dateMap.get(ruleName);

		int interval = LTIDate.calculateInterval(startDate, endDate);
		double re = Math.pow(endAmount / preAmount, 1.0 / interval) - 1.0;
		List<Double> tmpList = trigerList.get(index);
		tmpList.add(re);
		trigerList.set(index, tmpList);
	}

	public double getLastYearDividendYield(String symbol, Date currentDate) throws Exception {

		Date startDate = LTIDate.getNewNYSEYear(currentDate, -1);

		Date endDate = currentDate;

		double yeild = 0;

		if (symbol.equalsIgnoreCase("^GSPC"))
			yeild = securityManager.getAnnualGSPCYeild(startDate, endDate);

		else {
			Long securityID = securityManager.getBySymbol(symbol).getID();

			yeild = securityManager.getAnnualSecurityYeild(securityID, startDate, endDate);
		}

		return yeild;
	}

	public double getLastYearPE(String symbol, Date endDate) throws NoPriceException {
		return securityManager.getAnnualPE(symbol, endDate);
	}

	/**
	 * only run once after executing
	 */
	public void afterExecuting(Portfolio portfolio, Date CurrentDate) throws Exception {

	}

	/**
	 * only run once after updating
	 */
	public void afterUpdating(Portfolio portfolio, Date CurrentDate) throws Exception {

	}

	/**************** for stock strategies **********************/
	public List<String> getStockBySector(String[] sector) {
		return securityManager.getStockBySector(sector);
	}

	public List<String> getStockByIndustry(String[] industry) {
		return securityManager.getStockByIndustry(industry);
	}

	public List<String> getIndustryBySector(String[] sector) {
		return securityManager.getIndustryBySector(sector);
	}

	public List<String> getAllStocks() {
		return securityManager.getAllStocks();
	}

	public String getIndustry(String symbol) {
		return securityManager.getIndustry(symbol);
	}

	public List<Security> getSecuritiesByType(int type) {
		return securityManager.getSecuritiesByType(type);
	}

	protected com.lti.FinancialStatement.FinancialStatement financialStatement = new com.lti.FinancialStatement.FinancialStatementDatabaseImpl();

	public void loadData() {
		if (financialStatement == null || !(financialStatement instanceof com.lti.FinancialStatement.FinancialStatementMemoryImpl)) {
			financialStatement = new com.lti.FinancialStatement.FinancialStatementMemoryImpl();
		}

	}

	public String buildXML(Date[] dates, Double[] values) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");
		if (dates == null || values == null || dates.length != values.length) {
			sb.append("</Data></Beta>");
			String resultString = sb.toString();
			return resultString;
		}

		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dates.length; i++) {

			sb.append("<E d='" + df.format(dates[i]) + "' v='" + values[i] + "'/>");
		}

		sb.append("</Data></Beta>");
		String resultString = sb.toString();
		return resultString;
	}

	public static String buildXML(Date[] dates, Double[][] valueses) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");
		if (dates == null || valueses == null || dates.length != valueses.length) {
			sb.append("</Data></Beta>");
			String resultString = sb.toString();
			return resultString;
		}

		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dates.length; i++) {
			sb.append("<E d='");
			sb.append(df.format(dates[i]));
			sb.append("' v='");
			for (int j = 0; j < valueses[i].length; j++) {
				if (valueses[i][j] != null)
					sb.append(FormatUtil.formatQuantity(valueses[i][j]));
				else
					sb.append("null");
				if (j != valueses[i].length - 1)
					sb.append(",");
			}
			sb.append("'/>");
		}

		sb.append("</Data></Beta>");
		String resultString = sb.toString();
		return resultString;
	}

	/**
	 * return the parent assetclass of the assetclassname just below root.
	 * 
	 * @author CCD
	 * @param assetClassName
	 * @return
	 */
	public String getFirstParentClassName(String assetClassName) {
		AssetClass ac = assetClassManager.getFirstParentClass(assetClassName);
		if (ac != null)
			return ac.getName();
		else
			return "";
	}

	/**
	 * @author CCD
	 * @param parentclassname
	 * @param sonclassname
	 * @return if assetClass with name parentclassname is upper or the same with
	 *         assetClass with name sonclassname,return true, otherwise return
	 *         false.
	 */
	public boolean isUpperOrSameClass(String parentclassname, String sonclassname) {
		return assetClassManager.isUpperOrSameClass(parentclassname, sonclassname);
	}

	/**
	 * @author CCD
	 * @param parentclassname
	 * @param symbol
	 * @return if assetClass with name parentclassname is upper or the same with
	 *         assetClass of securtiy symbol,return true, otherwise return
	 *         false.
	 */
	public Boolean isUpperOrSameClassOfSymbol(String parentclassname, String symbol) {
		Security security = securityManager.get(symbol);
		if (security == null)
			return null;
		return assetClassManager.isUpperOrSameClass(parentclassname, assetClassManager.get(security.getClassID()).getName()) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * @author CCD
	 * @param
	 * @param availableFundList
	 * @return a map, of which the key is an available assetClass, and the value
	 *         of it is a list of available fund modified on 2010/06/30
	 */
	public HashMap<String, List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList, List<String> availableFundList) {
		return assetClassManager.getAvailableAssetClassSet(availableAssetClassList, availableFundList, false);
	}

	/**
	 * @author CCD
	 * @param
	 * @param availableFundList
	 * @param longTermBondFlag
	 * @return a map, of which the key is an available assetClass, and the value
	 *         of it is a list of available fund added on 2010/06/30
	 */
	public HashMap<String, List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList, List<String> availableFundList, boolean longTermBondFlag) {
		return assetClassManager.getAvailableAssetClassSet(availableAssetClassList, availableFundList, longTermBondFlag);
	}

	public double[] MVOwithLinearLimit(String[] indexFunds, double RAF, int month, String SMAorEMA, double[][] constraints, Date endDate) throws Exception {
		if (ws == null)
			ws = new WeightEstimate();
		return translateDoubleArray(ws.MVOwithLinearLimit(indexFunds, RAF, month, SMAorEMA, constraints, endDate));
	}

	public double[] doRAA(Security security, String[] factors, int interval, boolean isWLSOrOLS, Date endDate, TimeUnit tu) throws Exception {
		if (ws == null) {
			double[] lower = new double[factors.length];
			double[] upper = new double[factors.length];
			for (int i = 0; i < factors.length; ++i) {
				lower[i] = 0.0;
				upper[i] = 1.0;
			}
			ws = new WeightEstimate();
			ws.setLower(lower);
			ws.setUpper(upper);
		}
		if (isWLSOrOLS) {
			double[][] weights = new double[interval][interval];
			for (int i = 0; i < interval; ++i)
				weights[i][i] = Math.sqrt(i + 1);
			ws.setWeights(weights);
		}
		return translateDoubleArray(ws.newRAA(interval, endDate, tu, security, factors, isWLSOrOLS));
	}

	public double[] getWeightFromGuru(String[] targetFunds, String[] factorFunds, int topN, int sharpeMonth, double moneyAllocation, String WLSOrOLS, int interval, String targetType, Date curDate, TimeUnit tu) {
		List<Security> fundList = new ArrayList<Security>();
		double[] totalResult = null;
		boolean isWLSOrOLS = false;
		if (WLSOrOLS.equalsIgnoreCase("WLS"))
			isWLSOrOLS = true;
		if (targetType == null) {
			if (targetFunds != null)
				for (int i = 0; i < targetFunds.length; ++i) {
					Security security = securityManager.getBySymbol(targetFunds[i]);
					fundList.add(security);
				}
		} else if (targetType.equalsIgnoreCase("Conservative"))
			fundList = this.getSecurityByClass(66L);
		else if (targetType.equalsIgnoreCase("Moderate"))
			fundList = this.getSecurityByClass(65L);
		else if (targetType.equalsIgnoreCase("Growth"))
			fundList = this.getSecurityByClass(99L);
		if (fundList != null) {
			// calculate sharpe for compare
			try {
				List<Security> securityList = this.getTopSecurity(fundList, topN, sharpeMonth, curDate, TimeUnit.MONTHLY, SortType.SHARPE, false);
				if (securityList != null) {
					// do RAA for each fund and the get the average
					totalResult = new double[factorFunds.length];
					for (int i = 0; i < factorFunds.length; ++i)
						totalResult[i] = 0;
					int totalCount = 0;
					double[] lower = new double[factorFunds.length];
					double[] upper = new double[factorFunds.length];
					for (int i = 0; i < factorFunds.length; ++i) {
						lower[i] = 0.0;
						upper[i] = 1.0;
					}
					// initial for weight extimate
					if (ws == null)
						ws = new WeightEstimate();
					ws.setLower(lower);
					ws.setUpper(upper);
					if (isWLSOrOLS) {
						double[][] weights = new double[interval][interval];
						for (int i = 0; i < interval; ++i)
							weights[i][i] = Math.sqrt(i + 1);
						ws.setWeights(weights);
					}
					for (int i = 0; i < securityList.size(); ++i) {
						Security security = securityList.get(i);
						double[] result = null;
						try {
							result = doRAA(security, factorFunds, interval, isWLSOrOLS, curDate, tu);
						} catch (Exception e) {
							continue;
						}
						if (result != null) {
							++totalCount;
							for (int j = 0; j < factorFunds.length; ++j) {
								totalResult[j] += result[j];
							}
						}
					}
					if (totalCount != 0) {
						for (int j = 0; j < factorFunds.length; ++j) {
							totalResult[j] *= (1 - moneyAllocation) / totalCount;
						}
					}
				}
			} catch (NoPriceException e) {
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}
		return totalResult;
	}

	@Deprecated
	public double getAssetAmount(Asset cashAsset, Date currentDate2) {
		return this.getAssetAmount(cashAsset.getName(), currentDate2);
	}

	@Deprecated
	public void buy(String assetname, Long securityid, double amount, Date currentDate2) throws Exception {
		this.buy(assetname, securityid, amount, currentDate2,true);
	}
	@Deprecated
	public void buy(String assetname, Long securityid, double amount, Date currentDate2,boolean re) throws Exception {
		Security s = securityManager.get(securityid);
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.baseBuy(assetname, s.getID(), s.getSymbol(), amount, open, close, re, Configuration.TRANSACTION_TYPE_REAL);

	}

	@Deprecated
	public List<Asset> getCurrentAssetList() {
		return SimulateHolding.getAssets();
	}

	/**
	 * 返回portfolio下所有的Security??
	 * 
	 * @param name
	 * @param currentDate2
	 * @return
	 */
	@Deprecated
	public List<Security> getAssetSecurity(String name, Date currentDate2) {
		List<Security> securities = new ArrayList<Security>();
		for (Asset asset : SimulateHolding.getAssets()) {
			for (HoldingItem hi : asset.getHoldingItems()) {
				Security s = securityManager.get(hi.getSecurityID());
				securities.add(s);
			}
		}
		return securities;
	}

	public double getCash() {
		return SimulateHolding.getCash();
	}

	@Deprecated
	public String getInformation(Date currentDate2) {
		return SimulateHolding.getInformation();
	}

	public List<Security> getAssetSecurities(String assetname) throws Exception {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Asset asset = SimulateHolding.getAsset(assetname);
		if (asset == null)
			return null;
		List<Security> securities = new ArrayList<Security>();
		if (asset.getHoldingItems() != null) {
			Iterator<HoldingItem> iter = asset.getHoldingItems().iterator();
			while (iter.hasNext()) {
				HoldingItem si = iter.next();
				Security security = securityManager.get(si.getSecurityID());
				if (security != null)
					securities.add(security);
			}
		}
		return securities;
	}

	@Deprecated
	public Asset getAsset(String curAsset2) {
		return SimulateHolding.getAsset(curAsset2);
	}

	@Deprecated
	protected boolean persistable = true;

	public void sellAndDelAsset(String assetname, Date currentDate2) throws SimulateException {
		SimulateHolding.sellAndDelAsset(assetname);
	}

	@Deprecated
	public void buy(Asset cashAsset, String symbol, double amount, Date currentDate2) throws SimulateException, Exception {
		this.buy(cashAsset.getName(), symbol, amount, currentDate2);
	}

	public void sellAtNextOpen(String assetname, String symbol, double amount) throws SimulateException, NoPriceException {
		Security s = securityManager.get(symbol);
		SimulateHolding.sellAtNextOpen(assetname, s.getID(), s.getSymbol(), amount);
	}

	public void buyAtNextOpen(String assetname, String symbol, double amount, boolean reinvest) throws SimulateException, NoPriceException {
		Security s = securityManager.get(symbol);
		if(s == null){
			System.out.println(symbol);
		}
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.buyAtNextOpen(assetname, s.getID(), s.getSymbol(), amount, open, close, reinvest);
	}

	@Deprecated
	public void deposit(double adjustmentAmount, Date currentDate2) throws Exception {
		SimulateHolding.deposit(adjustmentAmount);
	}

	@Deprecated
	public void balance(Date currentDate2) throws Exception {
		SimulateHolding.balance();
	}

	public int[] transactionDetection(Date startDate, Date endDate, String[] duplicateFund) {
		return _simulator.transactionDetection(startDate, endDate, duplicateFund);
	}

	public double getAvailableTradingAmount(String symbol, Integer m, Date currentDate2, int priceType) {
		return _simulator.getAvailableTradingAmount(symbol, m, priceType);
	}

	@Deprecated
	public List<Asset> getAssets() {
		return SimulateHolding.getAssets();
	}

	@Deprecated
	public double getSecurityAmount(String assetName, String symbol, Date currentDate2) {
		return SimulateHolding.getSecurityAmount(assetName, symbol);
	}

	public double getAssetCollectionAmount(Date currentDate2) {
		return SimulateHolding.getAmount() - SimulateHolding.getCash();
	}

	@Deprecated
	public double getTargetPercentage(String name) {
		return SimulateHolding.getAsset(name).getTargetPercentage();
	}

	@Deprecated
	public void withdraw(double amount, Date currentDate2) throws SimulateException {
		SimulateHolding.withdraw(amount);
	}

	@Deprecated
	public void removeCash(double amount, Date currentDate2) throws SimulateException {
		SimulateHolding.withdraw(amount);

	}

	public void shortSellByShareNumber(String assetname, String symbol, int ShortShares, Date CurrentDate) throws NoPriceException, SimulateException {
		Security s = securityManager.get(symbol);
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.shortSellByShareNumber(assetname, s.getID(), s.getSymbol(), ShortShares, close, open);
	}

	public double getTotalAmount() {
		return SimulateHolding.getAmount();
	}

	@Deprecated
	public void buyAtNextOpen(String assetname, String symbol, double amount, Date currentDate2, boolean reinvest) throws SimulateException, NoPriceException {
		this.buyAtNextOpen(assetname, symbol, amount, reinvest);
	}

	@Deprecated
	public void sellAtNextOpen(String assetname, String symbol, double amount, Date currentDate2) throws SimulateException, NoPriceException {
		this.sellAtNextOpen(assetname, symbol, amount);

	}

	@Deprecated
	public double getAvailableTradingAmount(String symbol, Integer m, Date currentDate2, boolean b) {
		return this.getAvailableTradingAmount(symbol, m, currentDate2, b == false ? 0 : 1);
	}

	// @Deprecated
	// public List<HoldingItem> getSecurities() {
	// return SimulateHolding.getHoldingItems();
	// }

	public double getAssetAmount(String name) {
		return SimulateHolding.getAsset(name).getAmount();
	}

	public void addObject(String key, Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		GlobalObject go = new GlobalObject();
		go.setStrategyID(StrategyID);
		go.setPortfolioID(SimulatePortfolio.getID());
		go.setBytes(baos.toByteArray());
		_simulator.getGlobalObjects().add(go);
	}

	public Object getObject(String key) throws IOException, ClassNotFoundException {
		GlobalObject go = portfolioManager.getGlobalObject(key);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(go.getBytes()));
		Object o = ois.readObject();
		return o;
	}

	@Deprecated
	public String getName() {
		return SimulateHolding.getPortfolioName();
	}

	@Deprecated
	public void shortSellByShareNumber(String assetname, Long id, double shortShares, Date currentDate2) throws Exception {
		Security s = securityManager.get(id);
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.shortSellByShareNumber(assetname, s.getID(), s.getSymbol(), shortShares, close, open);

	}

	public void sellAsset(String assetname) throws SimulateException {
		this.sellAsset(assetname, null);
		
	}
	@Deprecated
	public void shortSellAtOpen(String assetname, String symbol, double amount, Date currentDate2) throws NoPriceException, SimulateException {
		Security s = securityManager.get(symbol);
		double open = securityManager.getOpenPrice(s.getID(), CurrentDate);
		double close = securityManager.getPrice(s.getID(), CurrentDate);
		SimulateHolding.shortSellAtOpen(assetname, s.getID(), s.getSymbol(), amount, close, open);
	}
	@Deprecated
	public void shortSellAtClose(String curAsset2, String shortSecurity2, double d, Date currentDate2) throws Exception {
		this.shortSell(curAsset2, shortSecurity2, d, currentDate2);
	}

	public void sellAssetAtOpen(String curAsset2, Date currentDate2) throws SimulateException {
		SimulateHolding.sellAssetAtOpen(curAsset2);
	}

	public Date getStartingDate() {
		return SimulatePortfolio.getStartingDate();
	}

	
	public List<HoldingItem> getHoldingItems() {
		return SimulateHolding.getHoldingItems();
	}

	public boolean holdSecurity(Long id) {
		List<HoldingItem> his=SimulateHolding.getHoldingItems();
		for(HoldingItem hi:his){
			if(hi.getSecurityID().equals(id))return true;
		}
		return false;
	}

	public void sendMail(){
		Transaction tr=new Transaction();
		tr.setPortfolioID(PortfolioID);
		tr.setDate(CurrentDate);
		tr.setSecurityID(0l);
		tr.setSymbol("NO_SYMBOL");
		tr.setAssetName("NO_ASSET");
		tr.setStrategyID(StrategyClassID);
		tr.setOperation("SEND_MAIL");
		tr.setShare(0.0);
		tr.setAmount(1.0);
		tr.setPercentage(0.0);
		tr.setTransactionType(Configuration.TRANSACTION_TYPE_MAIL);
		List<Transaction> trs=this._simulator.getSimulateTransactions();
		trs.add(tr);
	}
}
