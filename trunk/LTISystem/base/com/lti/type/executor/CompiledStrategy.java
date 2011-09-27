package com.lti.type.executor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lti.Exception.PortfolioException;
import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.Exception.Strategy.ParameterException;
import com.lti.MutualFundTool.WeightEstimate;
import com.lti.service.AssetClassManager;
import com.lti.service.BaseFormulaManager;
import com.lti.service.CloningCenterManager;
import com.lti.service.HolidayManager;
import com.lti.service.IndicatorManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Mycomparator;
import com.lti.type.SecurityValue;
import com.lti.type.SortType;
import com.lti.type.SourceType;
import com.lti.type.TimePeriod;
import com.lti.type.TimeUnit;
import com.lti.type.finance.Asset;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

/**
 * @author koko
 * 
 */
public class CompiledStrategy {
	
//	public final static int RUNNING_MODE_MONITOR=0;
//	public final static int RUNNING_MODE_UPDATE=1;
//	
//	protected int RunningMode=-1;
//	
//	// ----------------------------------------------------
//	// Current Portfolio
//	// ----------------------------------------------------
//	protected Portfolio CurrentPortfolio;
//
//	// ----------------------------------------------------
//	// Current Date
//	// ----------------------------------------------------
//	protected Date CurrentDate;
//
//	// ----------------------------------------------------
//	// Running information
//	// ----------------------------------------------------
//	// this field will override by sub strategy
//	protected Long StrategyID;
//	// this field will reset whenever init or action
//	protected Long PortfolioID;
//	
//	protected boolean persistable;
//	
//	public boolean isPersistable(){
//		return persistable;
//	}
//	
//	public void writeObject(ObjectOutputStream stream) throws IOException{
//		
//	}
//	
//	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
//		
//	}
//	
//	public String getVariables(){
//		StringBuffer sb=new StringBuffer();
//		return sb.toString();
//	}
//	
//	// ----------------------------------------------------
//	// curAsset, just for asset strategy
//	// ----------------------------------------------------
//	// this field will automatic add by asset object parameters getter
//	protected String curAsset;
//
//	public String getCurAsset() {
//		return curAsset;
//	}
//
//	public void setCurAsset(String curAsset) {
//		this.curAsset = curAsset;
//	}
//
//	// ----------------------------------------------------
//	// re-init
//	// ----------------------------------------------------
//	protected double version=1.0;
//	public double getVersion(){
//		return version;
//	}
//	public void reinit(com.lti.service.bo.Portfolio portfolio, Hashtable<String, String> parameters, Date pdate)throws Exception{
//		CurrentDate=pdate;
//		CurrentPortfolio=portfolio;
//		PortfolioID = CurrentPortfolio.getID();
//		fetchParameters(parameters);
//	}
//	public boolean checkVersion(Hashtable<String, String> parameters){
//		String _version=parameters.get("version");
//		String _v=""+StrategyID+"-"+getVersion();
//		if(_version==null||_version.equals("")){
//			parameters.put("version", _v);
//			return true;
//		}
//		
//		if(_v.equals(_version)){
//			return true;
//		}else{
//			parameters.put("version", _v);
//			return false;
//		}
//		
//	}
//
//	
//	// ----------------------------------------------------
//	// Constructor
//	// ----------------------------------------------------
//	public CompiledStrategy() {
//		assetClassManager = (AssetClassManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
//		portfolioManager = (PortfolioManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
//		securityManager = (SecurityManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//		strategyManager = (StrategyManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
//		holidayManager = (HolidayManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
//		indicatorManager = (IndicatorManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
//		baseFormulaManager = ContextHolder.getBaseFormulaManager();
//		cloneCenterManager = new CloningCenterManager();
//		StrategyID = -1l;
//		PortfolioID = -1l;
//
//	}
//
//	// ----------------------------------------------------
//	// XManagers
//	// ----------------------------------------------------
//	protected AssetClassManager assetClassManager;
//	protected PortfolioManager portfolioManager;
//	protected SecurityManager securityManager;
//	protected StrategyManager strategyManager;
//	protected HolidayManager holidayManager;
//	protected IndicatorManager indicatorManager;
//	protected BaseFormulaManager baseFormulaManager;
//	protected CloningCenterManager cloneCenterManager;
//	
//	
//	private Security Cash;
//	private Security Benchmark;
//	
//	public  Security getCash() {
//		if(Cash==null){
//			Cash=getSecurity(Configuration.getCashSymbol());;
//		}
//		return Cash;
//	}
//
//	public  Security getBenchmark() {
//		if(Benchmark==null){
//			Benchmark=getSecurity(CurrentPortfolio.getBenchmarkID());;
//		}
//		return Benchmark;
//	}
//
//	public void noPriceException() throws NoPriceException,SecurityException{
//		Security cash=getCash();
//		if(cash!=null)securityManager.getAdjPrice(cash.getID(), CurrentDate);
//		Security bm=getBenchmark();
//		if(bm!=null)securityManager.getAdjPrice(bm.getID(), CurrentDate);
//	}
//	
//	protected Map<String,Object> cachedParameters=new HashMap<String, Object>();
//	public Object fetchParameter(String type,String name,Hashtable<String,String> parameters)throws ParameterException{
//		Object o=cachedParameters.get(name);
//		if(o!=null)return o;
//		String value=parameters.get(name);
//		try {
//			if(value==null)o= null;
//			else if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {
//				o= value;
//			} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("long")) {
//				o= Long.parseLong(value);
//			} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("double")) {
//				o= Double.parseDouble(value);
//
//			} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
//				o= Integer.parseInt(value);
//
//			} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("float")) {
//				o=  Float.parseFloat(value) ;
//
//			} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("boolean")) {
//				o=   Boolean.parseBoolean(value);
//			} else if (type.equalsIgnoreCase("java.util.Date") || type.equalsIgnoreCase("java.sql.Timestamp") || type.equalsIgnoreCase("java.sql.Date")) {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				o=   sdf.parse(value) ;
//			} else {
//				if(!value.startsWith("{"))value="{"+value+"}";
//				if(type.matches("^\\s*String\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						String[] items=new String[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=array[i].replaceAll("\\\"", "");
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*Double\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						Double[] items=new Double[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Double.parseDouble(array[i]);
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*Float\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						Float[] items=new Float[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Float.parseFloat(array[i]);
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*Boolean\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						Boolean[] items=new Boolean[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Boolean.parseBoolean(array[i]);
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*Integer\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						Integer[] items=new Integer[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Integer.parseInt(array[i]);
//						}
//						o= items;
//					}
//				}else if(type.matches("^\\s*double\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						double[] items=new double[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Double.parseDouble(array[i]);
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*float\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						float[] items=new float[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Float.parseFloat(array[i]);
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*boolean\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						boolean[] items=new boolean[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Boolean.parseBoolean(array[i]);
//						}
//						o= items;
//					}
//				}
//				else if(type.matches("^\\s*int\\s*\\[\\s*\\]\\s*$")){
//					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
//					if(array.length>1){
//						int[] items=new int[array.length-1];
//						for(int i=1;i<array.length;i++){
//							items[i-1]=Integer.parseInt(array[i]);
//						}
//						o= items;
//					}
//				}else o=null;
//				
//			}
//		} catch (Exception e) {
//			ParameterException pe=new ParameterException("Cannot get the parameter of name: "+name,e);
//			throw pe;
//		}
//		if(o==null){
//			ParameterException pe=new ParameterException("Cannot get the parameter of name: "+name+", type:"+type+", value:"+value);
//			throw pe;
//		}else{
//			cachedParameters.put(name, o);
//			return o;
//		}
//	}
//	public void fetchParameters(Hashtable<String,String> ht)throws ParameterException{
//		
//	}
//
//	// ----------------------------------------------------
//	// running methods
//	// ----------------------------------------------------
//
//	public void init(com.lti.service.bo.Portfolio portfolio, Hashtable<String, String> parameters, Date pdate) throws Exception {
//
//	}
//
//	public void action(com.lti.service.bo.Portfolio portfolio, Hashtable<String, String> parameters, Date pdate) throws Exception {
//	}
//
//	// ----------------------------------------------------
//	// APIs
//	// ----------------------------------------------------
//	/**
//	 * physical date
//	 * format like yyyy-mm-dd 00:00:00
//	 * 
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public Date CurrentDate() {
//		Date date = new Date();
//		date.setHours(0);
//		date.setMinutes(0);
//		date.setMinutes(0);
//		return date;
//	}
//
//	/**
//	 * physical date
//	 * @return current Month
//	 */
//	public int CurrentMonth() {
//		Calendar cal = Calendar.getInstance();
//		int month = cal.get(Calendar.MONTH) + 1;
//		return month;
//	}
//
//	/**
//	 * get all mutual funds
//	 * 
//	 * @return Mutual Funds
//	 */
//	public java.util.List<Security> getAllMutualFunds() {
//		return securityManager.getMutualFunds();
//	}
//
//	/**
//	 * get the asset class's id by its name
//	 * 
//	 * @param assetClassName
//	 *            Asset Class
//	 * @return ID
//	 */
//	public long getAssetClassID(String assetClassName) {
//		// TODO Auto-generated method stub
//		AssetClass ac = assetClassManager.get(assetClassName);
//		if (ac != null)
//			return ac.getID();
//		else
//			return 0;
//	}
//	
//	/**
//	 * return the date of previous year
//	 * 
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public Date getPreviousYear(Date CurrentDate) {
//		Calendar ca = Calendar.getInstance();
//		ca.setTime(CurrentDate);
//		ca.set(ca.get(Calendar.YEAR) - 1, ca.get(Calendar.MONTH), ca.get(Calendar.DATE), 0, 0, 0);
//		Date lastYear = ca.getTime();
//		return lastYear;
//	}
//
//	/**
//	 * get the security's name by its id
//	 * 
//	 * @param securityName
//	 * @return ID
//	 */
//	public long getSecurityID(String securityName) {
//		// TODO Auto-generated method stub
//		Security s = this.getSecurity(securityName);
//		if (s != null)
//			return s.getID();
//		else
//			return 0;
//	}
//
//	/**
//	 * get securities by the names
//	 * 
//	 * @param nameArray
//	 * @return
//	 */
//	public List<Security> getSecurityList(String[] nameArray) {
//		if(nameArray!=null){
//			List<Security> list=new ArrayList<Security>();
//			for(int i=0;i<nameArray.length;i++){
//				Security s=getSecurity(nameArray[i]);
//				if(s!=null)list.add(s);
//			}
//			return list;
//		}
//		return null;
//	}
//
//	/**
//	 * get portfolios by names
//	 * @param nameArray
//	 * @return
//	 */
//	public List<Portfolio> getPortfolioList(String[] nameArray) {
//		if(nameArray!=null){
//			List<Portfolio> list=new ArrayList<Portfolio>();
//			for(int i=0;i<nameArray.length;i++){
//				Portfolio s=getPortfolio(nameArray[i]);
//				if(s!=null)list.add(s);
//			}
//			return list;
//		}
//		return null;
//	}	
//	/**
//	 * get the strategy's id by its name
//	 * 
//	 * @param strategyName
//	 * @return
//	 */
//	public long getStrategyID(String strategyName) {
//		// TODO Auto-generated method stub
//		if(strategyName!=null&&strategyName.equalsIgnoreCase("static")){
//			return 0;
//		}
//		Strategy s = strategyManager.get(strategyName);
//		if (s != null)
//			return s.getID();
//		else
//			return 0;
//	}
//
//	/**
//	 * if today is the month end, it will return true
//	 * 
//	 * @param date
//	 * @return
//	 */
//	public boolean isMonthEnd(java.util.Date date) {
//		Date yearEnd=com.lti.util.LTIDate.getLastNYSETradingDayOfMonth(date);
//		return com.lti.util.LTIDate.equals(date, yearEnd);
//	}
//
//	/**
//	 * if today is the quarter end , it will return true
//	 * 
//	 * @return
//	 */
//	public boolean isQuarterEnd(java.util.Date date) {
//		return com.lti.util.LTIDate.isQuarterEnd(date);
//
//	}
//
//	/**
//	 * if today is the year end, it will return true;
//	 * 
//	 * @return
//	 */
//	public boolean isYearEnd(java.util.Date date) {
//		Date yearEnd=com.lti.util.LTIDate.getLastNYSETradingDayOfYear(date);
//		return com.lti.util.LTIDate.equals(date, yearEnd);
//	}
//
//	public Security getSecurity(long id) {
//		Security se = securityManager.get(id);
//		return se;
//	}
//
//	public Security getSecurity(String name) {
//		Security se= securityManager.get(name);
//		return se;
//	}
//	
//	public Date getStartDate(String name) {
//		Security se=getSecurity(name);
//		return securityManager.getStartDate(se.getID());
//	}
//	public Date getEndDate(String name) {
//		Security se=getSecurity(name);
//		return securityManager.getEndDate(se.getID());
//	}
//	public Map<String,Portfolio> tmpPortfolioMap=new HashMap<String, Portfolio>();
//	/**
//	 * get a portfolio from database by its symbol
//	 * @param symbol
//	 * @return
//	 */
//	public Portfolio getPortfolio(String sybmol) {
//		Portfolio port=tmpPortfolioMap.get(sybmol);
//		if(port!=null)return port;
//		
//		long id=Long.parseLong(sybmol.substring(2));
//		port = portfolioManager.get(id);
//		if(port!=null){
//			tmpPortfolioMap.put(port.getSymbol(), port);
//		}
//		return port;
//	}
//
//	public Indicator getIndicator(long id) {
//		Indicator in = indicatorManager.get(id);
//		return in;
//	}
//
//	public Indicator getIndicator(String name) {
//		Indicator in = indicatorManager.get(name);
//		return in;
//	}
//	
//	public void printToLog(Transaction tran){
//		StringBuffer sb=new StringBuffer();
//		sb.append("Date: "+tran.getDate());
//		sb.append("\n");
//		sb.append("Operation:"+tran.getOperation());
//		sb.append("\n");
//		sb.append("Amount: "+tran.getAmount());
//		sb.append("\n");
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		log.setMessage(sb.toString());
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(String message) {
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		log.setMessage(message);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(Object message) {
//		if (message == null)
//			return;
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		log.setMessage(String.valueOf(message));
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(Object[] message) {
//		if (message == null)
//			return;
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = "";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(double[] message) {
//		if (message == null)
//			return;
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = "";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(String[] message) {
//		if (message == null)
//			return;
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = "";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(int[] message) {
//		if (message == null)
//			return;
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = "";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(String name, Object[] message) {
//		if (message == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//	
//	public void printToLog(String name, Map m) {
//		if (m == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		Iterator<String> keys=m.keySet().iterator();
//		while(keys.hasNext()){
//			String key=keys.next();
//			s += key+": "+String.valueOf(m.get(key)) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(String name, double[] message) {
//		if (message == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//	public void printToLog(String name, float[] message) {
//		if (message == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//	public void printToLog(String name, boolean[] message) {
//		if (message == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//	public void printToLog(String name, int[] message) {
//		if (message == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public void printToLog(String name, String[] message) {
//		if (message == null)
//			return;
//
//		Log log = new Log();
//		log.setStrategyID(this.StrategyID);
//		log.setPortfolioID(this.PortfolioID);
//		log.setLogDate(this.CurrentDate);
//		String s = name + "\r\n\r\n";
//		for (int i = 0; i < message.length; i++) {
//			s += String.valueOf(message[i]) + "\r\n";
//		}
//		log.setMessage(s);
//		CurrentPortfolio.addUserLog(log);
//	}
//
//	public String getStackTraceString(Throwable e) {
//
//		return com.lti.util.StringUtil.getStackTraceString(e);
//	}
//
//	public List<Security> getAssetSecurity(long assetID) {
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//		return securityManager.getSecuritiesByAsset(assetID);
//	}
//
//	public List<Security> getAssetSecurity(String name) {
//		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
//		long assetID = assetClassManager.get(name).getID();
//		return this.getAssetSecurity(assetID);
//	}
//
//	public List<Security> getSecurityByParentClass(long assetID) {
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//		return securityManager.getSecuritiesByClass(assetID);
//	}
//	
//	public List<Security> getCEF(long assetID) {
//		return securityManager.getCEF(assetID);
//	}
//
//	public List<Security> getCEF(Asset asset){
//		return securityManager.getCEF(asset.getAssetClassID());
//	}
//	
//	public List<Security> getSecurityByClass(long assetID)
//	{
//		return securityManager.getSecuritiesByAsset(assetID);
//	}
//	
//	public List<Security> getSecurityByClass(Asset asset){
//		return securityManager.getSecuritiesByAsset(asset.getAssetClassID());
//	}
//	
//	public List<Security> getMutualFundByClass(Asset asset)
//	{
//		return securityManager.getMutualFund(asset.getAssetClassID());
//	}
//	
//	public List<Security> getMutualFundByClass(long assetID)
//	{
//		return securityManager.getMutualFund(assetID);
//	}
//	
//	public AssetClass getAssetClass(String name) {
//		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
//		return assetClassManager.get(name);
//	}
//	/**
//	 * 
//	 * @param securityList
//	 * @param benchmark
//	 * @param startDate
//	 * @param endDate
//	 * @param st
//	 * @param nav
//	 * @return
//	 * added by CCD on 2010-03-02 for getting Alpha with benchMark
//	 */
//	private List<SecurityValue> getSecurityValueList(List<Security> securityList, String benchmark, Date startDate, Date endDate, SortType st,  boolean nav) {
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//		Long benchMarkID = null;
//		TimeUnit tu = TimeUnit.DAILY;
//		TimePeriod p = new TimePeriod(startDate, endDate);
//		if(securityList == null)return null;
//		if(benchmark != null){
//			Security benchFund = securityManager.getBySymbol(benchmark);
//			if(benchFund!=null)
//				benchMarkID = benchFund.getID();
//		}
//		int size = securityList.size();
//		for (int i = 0; i < size; i++) {
//				SecurityValue sv = new SecurityValue();
//				Security se = securityList.get(i);
//				if(se == null)continue;
//				Date start = se.getStartDate();
//				Date end = se.getEndDate();
//				if(start == null || startDate == null)
//					continue;
//				if(end == null || endDate == null)
//					continue;
//				if (start.after(startDate))
//					continue;
//				if(end.before(endDate))
//					continue;
//				sv.name = se.getSymbol();
//				double value = 0;
//				if (st == SortType.RETURN) {
//					try {
//						value = se.getReturn(startDate, endDate, nav);
//					} catch (Exception e) {
//						if(nav){
//							try{
//								value = se.getReturn(p);
//							}catch(Exception ee){
//								System.out.println(StringUtil.getStackTraceString(ee));
//								continue;
//							}
//						}else
//							continue;
//					}
//				}
//				else
//				{
//					/***if nav is true, judge if it has NAV before we calculate,if not ,we will set it false for the security***/
//					try{
//						boolean tmpNav=nav;
//						if(tmpNav)
//						{
//							Date tempStartDate = LTIDate.getNewTradingDate(endDate, tu, -10);
//							List<Double> tmpReturnList = baseFormulaManager.getReturns(tempStartDate, endDate, tu, se.getID(), SourceType.SECURITY_RETURN, nav);
//							if(tmpReturnList!=null && tmpReturnList.size()>0 && tmpReturnList.get(0)== 0.0)
//								tmpNav=false;
//						}
//						if (st == SortType.ALPHA)
//							value = se.getAlpha(startDate, endDate, tu, benchMarkID, tmpNav);
//						if (st == SortType.BETA)
//								value = se.getBeta(startDate, endDate, tu, tmpNav);
//						if (st == SortType.ANNULIZEDRETURN)
//							value = se.getAnnualizedReturn(startDate, endDate,tmpNav);
//						if (st == SortType.DRAWDOWN)
//							value = se.getDrawDown(startDate, endDate, tu, tmpNav);
//						if (st == SortType.SHARPE)
//							value = se.getSharpeRatio(startDate, endDate, tu, tmpNav);
//						if (st == SortType.RSQUARED)
//							value = se.getRSquared(startDate, endDate, tu, tmpNav);
//						if (st == SortType.STANDARDDIVIATION)
//							value = se.getStandardDeviation(startDate, endDate, tu, tmpNav);
//						if (st == SortType.TREYNOR)
//							value = se.getTreynorRatio(startDate, endDate, tu, tmpNav);
//						if (st == SortType.DISCOUNTRATE)
//							value = se.getAverageDiscountRate(startDate, endDate, tu);
//					}catch(Exception e){
//						continue;
//					}
//				}
//				sv.value = value;
//				svList.add(sv);
//		}
//		return svList;
//		
//	}
//	/** ************************************************************************************* */
//	/**
//	 * return security with value calculated. 
//	 * modified by CCD last modified on 2009-11-20
//	 * if nav is true,use NAV first and if NAV is null,then use price instead.
//	 */
//	private List<SecurityValue> getSecurityValueList(List<Security> securityList, Date startDate, Date endDate, SortType st,  boolean nav) {
//		return this.getSecurityValueList(securityList, null, startDate, endDate, st, nav);
//	}
//	/**
//	 * 
//	 * @param count
//	 * @param startDate
//	 * @param endDate
//	 * @param sortType
//	 * @param tu
//	 * @param bench
//	 * @param nav
//	 * @return
//	 * @throws NoPriceException
//	 * added by CCD on 2010-03-02
//	 */
//	public List<Security> getTopSecurity(int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu, String bench, boolean nav)throws NoPriceException {
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//
//		List<Security> securityList = new ArrayList<Security>();
//		securityList = securityManager.getSecurities();
//
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//
//		svList = this.getSecurityValueList(securityList, bench, startDate, endDate, sortType, nav);
//
//		if(svList == null || svList.size() == 0)return null;
//		
//		
//		Mycomparator comparator = new Mycomparator();
//		Collections.sort(svList, comparator);
//		
//		
//		securityList.clear();
//		
//		count = count<svList.size()?count:svList.size();
//		
//		for (int i = 0; i < count; i++) {
//			securityList.add(securityManager.getBySymbol(svList.get(i).name));
//		}
//
//		return securityList;
//	}
//	/**
//	 * return the Top count security order by sortType, it orders all the securities in database.
//	 * modified by CCD
//	 * **/
//	@SuppressWarnings("unchecked")
//	public List<Security> getTopSecurity(int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu, boolean nav)throws NoPriceException {
//		return this.getTopSecurity(count, startDate, endDate, sortType, tu, null, nav);
//	}
//	/**
//	 * return the Top count security order by sortType,it orders the security which's id in securityID
//	 * @author CCD
//	 * run for test.
//	 * **/
//	public List<Security> testGetTopSecurity(int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu, boolean nav)throws NoPriceException {
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//
//		List<Security> securityList = new ArrayList<Security>();
//		Security security;
//		Long[] securityID={191L,195L,196L,66L,190L,338L,4619L};
//		for(int i=0;i<7;++i)
//		{
//			security=securityManager.get(securityID[i]);
//			securityList.add(security);
//		}
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//		svList = this.getSecurityValueList(securityList, startDate, endDate, sortType, nav);
//		
//		if(svList == null || svList.size() == 0)return null;
//	
//		Mycomparator comparator = new Mycomparator();
//		Collections.sort(svList, comparator);
//		
//		securityList.clear();
//		count = count<svList.size()?count:svList.size();
//		for (int i = 0; i < count; i++)
//			securityList.add(securityManager.getBySymbol(svList.get(i).name));
//		
//		return securityList;
//		
//	}
//	/**
//	 * 
//	 * @param count
//	 * @param time
//	 * @param currentDate
//	 * @param tu
//	 * @param sortType
//	 * @param bench
//	 * @param nav
//	 * @return
//	 * @throws NoPriceException
//	 * added by CCD on 2010-03-02
//	 */
//	public List<Security> getTopSecurity(int count, int time, Date currentDate, TimeUnit tu, SortType sortType, String bench, boolean nav)throws NoPriceException {
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//
//		Date startDate = new Date();
//		Calendar ca = Calendar.getInstance();
//		ca.setTime(currentDate);
//		if (time > 0)
//			time = time * -1;
//		if (tu == TimeUnit.DAILY)
//			startDate=LTIDate.getNewNYSETradingDay(currentDate, time);
//		else if (tu == TimeUnit.WEEKLY)
//			startDate=LTIDate.getNewNYSEWeek(currentDate, time);
//		else if (tu == TimeUnit.MONTHLY)
//			startDate=LTIDate.getNewNYSEMonth(currentDate, time);
//		else if (tu == TimeUnit.YEARLY)
//			startDate=LTIDate.getNewNYSEYear(currentDate, time);
//
//		List<Security> securityList = new ArrayList<Security>();
//		securityList = securityManager.getSecurities();
//		
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//		
//		svList = this.getSecurityValueList(securityList, bench, startDate, currentDate, sortType, nav);
//
//		if(svList == null || svList.size() == 0)return null;
//		
//		
//		Mycomparator comparator = new Mycomparator();
//		Collections.sort(svList, comparator);
//		
//		securityList.clear();
//		count = count<svList.size()?count:svList.size();
//		
//		for (int i = 0; i < count; i++) {
//			securityList.add(securityManager.getBySymbol(svList.get(i).name));
//		}
//
//		return securityList;
//	}
//	@SuppressWarnings("unchecked")
//	public List<Security> getTopSecurity(int count, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav)throws NoPriceException {
//		return this.getTopSecurity(count, time, currentDate, tu, sortType, null, nav);
//	}
//	/**
//	 * 
//	 * @param names
//	 * @param bench
//	 * @param time
//	 * @param currentDate
//	 * @param tu
//	 * @param sortType
//	 * @param nav
//	 * @return
//	 * @throws NoPriceException
//	 * added by CCD on 2010-03-02
//	 */
//	public String[] getTopSecurityArray(String[] names, String bench, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException{
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//		Date startDate = new Date();
//		Calendar ca = Calendar.getInstance();
//		ca.setTime(currentDate);
//		if (time > 0)
//			time = time * -1;
//		if (tu == TimeUnit.DAILY)
//			//ca.add(Calendar.DAY_OF_YEAR, time);
//			startDate=LTIDate.getNewNYSETradingDay(currentDate, time);
//		else if (tu == TimeUnit.WEEKLY)
//			//ca.add(Calendar.WEEK_OF_YEAR, time);
//			startDate=LTIDate.getNewNYSEWeek(currentDate, time);
//		else if (tu == TimeUnit.MONTHLY)
//			//ca.add(Calendar.MONTH, time);
//			startDate=LTIDate.getNewNYSEMonth(currentDate, time);
//		else if (tu == TimeUnit.YEARLY)
//			//ca.add(Calendar.YEAR, time);
//			startDate=LTIDate.getNewNYSEYear(currentDate, time);
//		//startDate = ca.getTime();
//
//		List<Security> securityList = new ArrayList<Security>();
//		// securityList = securityManager.getSecurities();
//		for (int i = 0; i < names.length; i++)
//			securityList.add(securityManager.get(names[i]));
//
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//
//		svList = this.getSecurityValueList(securityList, bench, startDate, currentDate, sortType, nav);
//
//		if(svList == null || svList.size() == 0)return null;
//		
//		Mycomparator comparator = new Mycomparator();
//		Collections.sort(svList, comparator);
//		String[] returns = new String[securityList.size()];
//		for (int i = 0; i < svList.size(); i++) {
//			returns[i] = svList.get(i).name;
//		}
//		return returns;
//	}
//	public String[] getTopSecurityArray(String[] names, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException{
//		return this.getTopSecurityArray(names,null, time, currentDate, tu, sortType, nav);
//	}
//	/**
//	 * 
//	 * @param securityList
//	 * @param count
//	 * @param time
//	 * @param currentDate
//	 * @param tu
//	 * @param sortType
//	 * @param nav
//	 * @return
//	 * @throws NoPriceException
//	 * added by CCD on 2010-03-02
//	 */
//	public List<Security> getTopSecurity(List<Security> securityList, String benchmark, int count, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException{
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//
//		Date startDate = new Date();
//		Calendar ca = Calendar.getInstance();
//		ca.setTime(currentDate);
//		if (time > 0)
//			time = time * -1;
//		if (tu == TimeUnit.DAILY)
//			startDate=LTIDate.getNewNYSETradingDay(currentDate, time);
//		else if (tu == TimeUnit.WEEKLY)
//			startDate=LTIDate.getNewNYSEWeek(currentDate, time);
//		else if (tu == TimeUnit.MONTHLY)
//			startDate=LTIDate.getNewNYSEMonth(currentDate, time);
//		else if (tu == TimeUnit.YEARLY)
//			startDate=LTIDate.getNewNYSEYear(currentDate, time);
//
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//
//		svList = this.getSecurityValueList(securityList, startDate, currentDate, sortType, nav);
//
//		if(svList == null || svList.size() == 0)return null;
//		
//		Mycomparator comparator = new Mycomparator();
//		Collections.sort(svList, comparator);
//		
//		//securityList.clear();
//		List<Security> seList = new ArrayList<Security>();
//		
//		count = count<svList.size()?count:svList.size();
//		
//		for (int i = 0; i < count; i++) {
//			seList.add(securityManager.getBySymbol(svList.get(i).name));
//		}
//
//		return seList;
//	}
//	public List<Security> getTopSecurity(List<Security> securityList, int count, int time, Date currentDate, TimeUnit tu, SortType sortType, boolean nav) throws NoPriceException{
//		return getTopSecurity(securityList, null, count, time, currentDate, tu, sortType, nav);
//	}
//	
////	private List<SecurityValue> getTopSharpeSecurity(List<SecurityValue> svList)
////	{
////		List<SecurityValue> seList = new ArrayList<SecurityValue>();
////		
////		List<SecurityValue> positiveList = new ArrayList<SecurityValue>();
////		List<SecurityValue> nagetiveList = new ArrayList<SecurityValue>();
////		
////		for(int i=0;i<svList.size();i++)
////		{
////			SecurityValue sv = svList.get(i);
////			if(svList.get(i).value>=0)
////			{
////				positiveList.add(sv);
////			}
////			else
////			{
////				nagetiveList.add(sv);
////			}
////		}
////		
////		Mycomparator comparator = new Mycomparator();
////		Collections.sort(positiveList,comparator);
////		Collections.sort(nagetiveList, comparator);
////		
////		for(int i=0;i<positiveList.size();i++)
////		{
////			seList.add(positiveList.get(i));
////		}
////		
////		for(int i=nagetiveList.size()-1;i>=0;i--)
////		{
////			seList.add(nagetiveList.get(i));
////		}
////		
////		/*for(int i=0;i<seList.size();i++)
////			System.out.println(seList.get(i).name+"  "+seList.get(i).value);*/
////		
////		return seList;
////	}
//	
//	public int getCurrentYear(){
//		Calendar ca=Calendar.getInstance();
//		ca.setTime(CurrentDate);
//		return ca.get(Calendar.YEAR);
//	}
//	
//	
//	private List<SecurityValue> getPortfolioValueList(List<Portfolio> portfolioList, Date startDate, Date endDate, SortType st, TimeUnit tu) throws NoPriceException{
//		List<SecurityValue> svList = new ArrayList<SecurityValue>();
//
//		TimePeriod p = new TimePeriod(startDate, endDate);
//
//		if(portfolioList == null)return null;
//		
//		for (int i = 0; i < portfolioList.size(); i++) {
//			SecurityValue sv = new SecurityValue();
//			Portfolio po = portfolioList.get(i);
//
//			if(po == null)continue;
//			
//			long portfolioID = po.getID();
//			
//			Date start = portfolioManager.getEarliestDate(portfolioID);
//			
//			Date end = portfolioManager.getLatestDate(portfolioID);
//
//			if(start == null || startDate == null)
//				continue;
//			
//			if(end == null || endDate == null)
//				continue;
//			
//			if (start.after(startDate))
//				continue;
//
//			if(end.before(endDate))
//				continue;
//			
//			sv.name = po.getSymbol();
//			double value = 0;
//			if (st == SortType.RETURN) {
//				try {
//					List<PortfolioDailyData> tmpList = portfolioManager.getDailydatasByPeriod(po.getID(), startDate, endDate);
//					value = (tmpList.get(tmpList.size()-1).getAmount()-tmpList.get(0).getAmount())/tmpList.get(0).getAmount();
//					
//				} catch (Exception e) {
//					//e.printStackTrace();
//					System.out.println(StringUtil.getStackTraceString(e));
//				}
//			}
//			tu = TimeUnit.DAILY;
//			if (st == SortType.ALPHA)
//				value = po.getAlpha(startDate, endDate, tu);
//			if (st == SortType.BETA)
//				value = po.getBeta(startDate, endDate, tu);
//			if (st == SortType.ANNULIZEDRETURN)
//			{
//				try {
//					value = po.getAnnualizedReturn(startDate, endDate);
//				} catch (PortfolioException e) {
//					//e.printStackTrace();
//					System.out.println(StringUtil.getStackTraceString(e));
//				}
//			}
//			if (st == SortType.DRAWDOWN)
//				value = po.getDrawDown(startDate, endDate, tu);
//			if (st == SortType.SHARPE)
//				value = po.getSharpeRatio(startDate, endDate, tu);
//			if (st == SortType.RSQUARED)
//				value = po.getRSquared(startDate, endDate, tu);
//			if (st == SortType.STANDARDDIVIATION)
//				value = po.getStandardDeviation(startDate, endDate, tu);
//			if (st == SortType.TREYNOR)
//				value = po.getTreynorRatio(startDate, endDate, tu);
//			
//			sv.value = value;
//
//			svList.add(sv);
//		}
//
//		return svList;
//	}
//	public List<Portfolio> getTopPortfolio(List<String> poList, int count, Date startDate, Date endDate, SortType sortType, TimeUnit tu)
//	{		
//		List<Portfolio> plist = new ArrayList<Portfolio>();
//
//		List<SecurityValue> newList = new ArrayList<SecurityValue>();
//
//		List<Portfolio> poList1= new ArrayList<Portfolio>();
//		for (int i = 0; i < count; i++)
//		{
//			Portfolio tmpPortfoio = portfolioManager.get(poList.get(i)); 
//			if(tmpPortfoio!=null)poList1.add(tmpPortfoio);
//		}
//		
//		poList1 = this.removeSamePortfolio(poList1);
//		
//		try {
//			newList = this.getPortfolioValueList(poList1, startDate, endDate, sortType, tu);
//		} catch (NoPriceException e) {
//			//e.printStackTrace();
//			System.out.println(StringUtil.getStackTraceString(e));
//		}
//
//		if(newList == null || newList.size() == 0)return null;
//		
//		if(sortType != SortType.SHARPE)
//		{
//			Mycomparator comparator = new Mycomparator();
//			Collections.sort(newList, comparator);
//		}
//		
//		count = count<newList.size()?count:newList.size();
//		
//		poList1 = new ArrayList<Portfolio>();
//		for (int i = 0; i < count; i++) {
//			poList1.add(portfolioManager.get(newList.get(i).name));
//		}	
//		
//		return plist;
//	}
//	private List<Portfolio> removeSamePortfolio(List<Portfolio> poList)
//	{
//		List<Portfolio> newList = new ArrayList<Portfolio>();
//		boolean[] flag = new boolean[poList.size()];
//		for(int i=0;i<poList.size();i++)
//		{
//			Portfolio tmp1 = poList.get(i);
//			for(int j=i+1;j<poList.size();j++)
//			{
//				if(flag[j])continue;
//				Portfolio tmp2 = poList.get(i);
//				if(portfolioManager.isSamePortfolio(tmp1, tmp2))
//				{					
//					int index = this.chooseLongerPortfolio(tmp1, tmp2);
//					if(index == 2)
//						flag[j]=true;
//					else
//						flag[i]=true;
//				}
//			}
//			if(!flag[i])
//				newList.add(tmp1);
//		}
//		
//		return newList;
//	}
//	private int chooseLongerPortfolio(Portfolio p1,Portfolio p2)
//	{
//		Date sd1 = p1.getStartingDate();
//		Date ed1 = p1.getEndDate();
//		Date sd2 = p2.getStartingDate();
//		Date ed2 = p2.getEndDate();
//		int interval1 = LTIDate.calculateInterval(sd1, ed1);
//		int interval2 = LTIDate.calculateInterval(sd2, ed2);
//		if(interval1>interval2)
//			return 1;
//		else
//			return 2;
//	}
//	/** ************************************************************************************** */
//
//	public Date getEarliestAvaliablePriceDate(Security se) {
//		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
//		Date date = securityManager.getStartDate(se.getID());
//		if (date == null) {
//			date = new Date(8000, 1, 1);
//		}
//		return date;
//	}
//	
//	protected WeightEstimate ws;
//	
//	public void setRAALimit(double[] upper,double[] lower){
//		if(ws==null){
//			ws=new WeightEstimate();
//		}
//		ws.setLower(lower);
//		ws.setUpper(upper);
//	}
//	public double[] translateDoubleArray(Double[] ds){
//		if(ds==null||ds.length==0)return null;
//		double[] doubles=new double[ds.length];
//		for(int i=0;i<ds.length;i++){
//			doubles[i]=ds[i]==null?0.0:ds[i];
//		}
//		return doubles;
//	}
//
//	public double[] RAA(int interval, Date date,TimeUnit tu, String MF,String[] index,boolean allowShort) throws Exception{
//		if(ws==null){
//			ws=new WeightEstimate();
//		}
//		return translateDoubleArray( ws.RAA(interval, date, tu, MF, index,allowShort,true));
//	}
//	
//	public double[] RAA(int interval, Date date,TimeUnit tu, String MF,String[] index) throws Exception{
//		if(ws==null){
//			ws=new WeightEstimate();
//		}
//		return translateDoubleArray(ws.RAA(interval, date, tu, MF, index,false,true));
//	}
//	
//	/**************************************************************************************************/
//	private List<List<Double>> trigerList = new ArrayList<List<Double>>();
//	private List<String> ruleList = new ArrayList<String>();
//	private HashMap<String,Double> nameMap = new HashMap<String,Double>();
//	private HashMap<String,Date> dateMap = new HashMap<String,Date>(); 
//	public void confidenceCheckStart(String ruleName, double startAmount, Date startDate){
//		int index = ruleList.indexOf(ruleName);
//		if(index<0){
//			ruleList.add(ruleName);
//			List<Double> tmpList = new ArrayList<Double>();
//			trigerList.add(tmpList);
//		}
//		nameMap.put(ruleName, startAmount);
//		dateMap.put(ruleName, startDate);
//	}
//	public void confidenceCheckEnd(String ruleName, double endAmount, Date endDate){
//		int index = ruleList.indexOf(ruleName);
//		if(index<0){
//			return;
//		}
//		double preAmount = nameMap.get(ruleName);
//		Date startDate = dateMap.get(ruleName);
//		
//		int interval = LTIDate.calculateInterval(startDate, endDate);
//		double re = Math.pow(endAmount/preAmount, 1.0/interval) - 1.0;
//		List<Double> tmpList = trigerList.get(index);
//		tmpList.add(re);
//		trigerList.set(index, tmpList);
//	}
//	
//	public void end()
//	{
//		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
//		
//		if(trigerList == null || trigerList.size()==0)
//			return;
//		for(int i=trigerList.size()-1;i>=0;i--)
//		{
//			String name = ruleList.get(i);
//			List<Double> tmpList = trigerList.get(i);
//			baseFormulaManager.getConfidneceValue(PortfolioID,StrategyID, name, tmpList);
//			
//		}
//	}
//	
//	
//	public double getLastYearDividendYield(String symbol, Date currentDate) throws Exception
//	{
//		
//		Date startDate=LTIDate.getNewNYSEYear(currentDate, -1);
//		
//		Date endDate=currentDate;
//		
//		double yeild = 0;
//		
//		if(symbol.equalsIgnoreCase("^GSPC"))
//			yeild = securityManager.getAnnualGSPCYeild(startDate, endDate);
//		
//		else{
//			Long securityID=securityManager.getBySymbol(symbol).getID();
//			
//			yeild=securityManager.getAnnualSecurityYeild(securityID, startDate, endDate);
//		}
//		
//		return yeild;
//	}
//	
//	public double getLastYearPE(String symbol, Date endDate)
//	{
//		return securityManager.getAnnualPE(symbol, endDate);
//	}
//
//	
//	
//	/**
//	 * only run once after executing 
//	 */
//	public void afterExecuting(Portfolio portfolio,Date CurrentDate) throws Exception{
//		
//	}
//	/**
//	 * only run once after updating 
//	 */
//	public void afterUpdating(Portfolio portfolio,Date CurrentDate) throws Exception{
//		
//	}
//
//	public int getRunningMode() {
//		return RunningMode;
//	}
//
//	public void setRunningMode(int runningMode) {
//		RunningMode = runningMode;
//	}
//	
//	/**************** for stock strategies **********************/
//	public List<String> getStockBySector(String[]sector){
//		return securityManager.getStockBySector(sector);
//	}
//	
//	public List<String> getStockByIndustry(String[]industry){
//		return securityManager.getStockByIndustry(industry);
//	}
//	
//	public List<String> getIndustryBySector(String[]sector){
//		return securityManager.getIndustryBySector(sector);
//	}
//	public List<String> getAllStocks(){
//		return securityManager.getAllStocks();
//	}
//	
//	public String getIndustry(String symbol){
//		return securityManager.getIndustry(symbol);
//	}
//	
//	public List<Security> getSecuritiesByType(int type){
//		return securityManager.getSecuritiesByType(type);
//	}
//	
//	
//	protected com.lti.FinancialStatement.FinancialStatement financialStatement = new com.lti.FinancialStatement.FinancialStatementDatabaseImpl();
//	public void loadData(){
//		if(financialStatement==null||!(financialStatement instanceof com.lti.FinancialStatement.FinancialStatementMemoryImpl)){
//			financialStatement = new com.lti.FinancialStatement.FinancialStatementMemoryImpl();
//		}
//		
//	}
//	
//	public String buildXML(Date[] dates, Double[] values) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		sb.append("<Beta><Data>");
//		if (dates == null||values==null||dates.length!=values.length) {
//			sb.append("</Data></Beta>");
//			String resultString = sb.toString();
//			return resultString ;
//		}
//
//		java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
//		for (int i = 0; i < dates.length; i++) {
//
//			sb.append("<E d='" + df.format(dates[i]) + "' v='" + values[i] + "'/>");
//		}
//
//		sb.append("</Data></Beta>");
//		String resultString = sb.toString();
//		return resultString;
//	}
//	public String buildXML(Date[] dates, Double[][] valueses) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		sb.append("<Beta><Data>");
//		if (dates == null||valueses==null||dates.length!=valueses.length) {
//			sb.append("</Data></Beta>");
//			String resultString = sb.toString();
//			return resultString ;
//		}
//
//		java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
//		for (int i = 0; i < dates.length; i++) {
//			sb.append("<E d='");
//			sb.append(df.format(dates[i]));
//			sb.append("' v='");
//			for(int j=0;j<valueses[i].length;j++){
//				if(valueses[i][j]!=null)sb.append(FormatUtil.formatQuantity(valueses[i][j]));
//				else sb.append("null");
//				if(j!=valueses[i].length-1)sb.append(",");
//			}
//			sb.append("'/>");
//		}
//
//		sb.append("</Data></Beta>");
//		String resultString = sb.toString();
//		return resultString;
//	}
//	
//	
//	/**
//	 * return the parent assetclass of the assetclassname just below root.
//	 * @author CCD
//	 * @param assetClassName
//	 * @return
//	 */
//	public String getFirstParentClassName(String assetClassName) {
//		AssetClass ac = assetClassManager.getFirstParentClass(assetClassName);
//		if(ac != null)
//			return ac.getName();
//		else
//			return "";
//	}
//	/**
//	 * @author CCD
//	 * @param parentclassname
//	 * @param sonclassname
//	 * @return if assetClass with name parentclassname is upper or the same with 
//	 * assetClass with name sonclassname,return true, otherwise return false.
//	 */
//	public boolean isUpperOrSameClass(String parentclassname,String sonclassname)
//	{
//		return assetClassManager.isUpperOrSameClass(parentclassname, sonclassname);
//	}
//	/**
//	 * @author  CCD
//	 * @param parentclassname
//	 * @param symbol 
//	 * @return if assetClass with name parentclassname is upper or the same with 
//	 * assetClass of securtiy symbol,return true, otherwise return false.
//	 */
//	public Boolean isUpperOrSameClassOfSymbol(String parentclassname,String symbol){
//		Security security = securityManager.get(symbol);
//		if(security==null)
//			return null;
//		return assetClassManager.isUpperOrSameClass(parentclassname,assetClassManager.get(security.getClassID()).getName())?Boolean.TRUE:Boolean.FALSE;
//	}
//	/**
//	 * @author CCD
//	 * @param 
//	 * @param availableFundList
//	 * @return a map, of which the key is an available assetClass, and the value of it is a list of available fund
//	 * modified on 2010/06/30
//	 */
//	public HashMap<String,List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList,List<String> availableFundList){
//		return assetClassManager.getAvailableAssetClassSet(availableAssetClassList, availableFundList,false);
//	}
//	/**
//	 * @author CCD
//	 * @param 
//	 * @param availableFundList
//	 * @param longTermBondFlag
//	 * @return a map, of which the key is an available assetClass, and the value of it is a list of available fund
//	 * added on 2010/06/30
//	 */
//	public HashMap<String,List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList,List<String> availableFundList, boolean longTermBondFlag){
//		return assetClassManager.getAvailableAssetClassSet(availableAssetClassList, availableFundList, longTermBondFlag);
//	}
//	
//	public double[] MVOwithLinearLimit(String[] indexFunds, double RAF, int month, String SMAorEMA, double[][] constraints, Date endDate)throws Exception{
//		if(ws==null)
//			ws = new WeightEstimate();
//		return translateDoubleArray(ws.MVOwithLinearLimit(indexFunds, RAF, month, SMAorEMA, constraints, endDate));
//	}
//	public double[] doRAA(Security security, String[] factors,int interval, boolean isWLSOrOLS,Date endDate,TimeUnit tu)throws Exception{
//		if(ws == null){
//			double[] lower = new double[factors.length];
//			double[] upper = new double[factors.length];
//			for(int i=0;i<factors.length;++i){
//				lower[i]=0.0;
//				upper[i]=1.0;
//			}
//			ws = new WeightEstimate();
//			ws.setLower(lower);
//			ws.setUpper(upper);
//		}
//		if(isWLSOrOLS){
//			double[][] weights = new double[interval][interval];
//			for(int i=0;i<interval;++i)
//				weights[i][i] = Math.sqrt(i+1);
//			ws.setWeights(weights);
//		}
//		return translateDoubleArray(ws.newRAA(interval, endDate, tu, security, factors, isWLSOrOLS));
//	}
//	
//	
//	public double[] getWeightFromGuru(String[] targetFunds,String[] factorFunds,int topN,int sharpeMonth,double moneyAllocation,String WLSOrOLS,int interval,String targetType,Date curDate,TimeUnit tu){
//		List<Security> fundList = new ArrayList<Security>();
//		double[] totalResult  = null;
//		boolean isWLSOrOLS = false;
//		if(WLSOrOLS.equalsIgnoreCase("WLS"))
//			isWLSOrOLS= true;
//		if(targetType == null){
//			if(targetFunds!=null)
//				for(int i=0;i<targetFunds.length;++i){
//					Security  security = securityManager.getBySymbol(targetFunds[i]);
//					fundList.add(security);
//				}
//		}
//		else if(targetType.equalsIgnoreCase("Conservative"))
//			fundList = this.getSecurityByClass(66L);
//		else if(targetType.equalsIgnoreCase("Moderate"))
//			fundList = this.getSecurityByClass(65L);
//		else if(targetType.equalsIgnoreCase("Growth"))
//			fundList = this.getSecurityByClass(99L);
//		if(fundList!=null){
//			//calculate sharpe for compare
//			try {
//				List<Security> securityList = this.getTopSecurity(fundList, topN, sharpeMonth, curDate, TimeUnit.MONTHLY, SortType.SHARPE, false);
//				if(securityList!=null){
//					//do RAA for each fund and the get the average
//					totalResult = new double[factorFunds.length];
//					for(int i=0;i<factorFunds.length;++i)
//						totalResult[i]=0;
//					int totalCount=0;
//					double[] lower = new double[factorFunds.length];
//					double[] upper = new double[factorFunds.length];
//					for(int i=0;i<factorFunds.length;++i){
//						lower[i]=0.0;
//						upper[i]=1.0;
//					}
//					//initial for weight extimate
//					if(ws == null)
//						ws = new WeightEstimate();
//					ws.setLower(lower);
//					ws.setUpper(upper);
//					if(isWLSOrOLS){
//						double[][] weights = new double[interval][interval];
//						for(int i=0;i<interval;++i)
//							weights[i][i] = Math.sqrt(i+1);
//						ws.setWeights(weights);
//					}
//					for(int i=0;i<securityList.size();++i){
//						Security security = securityList.get(i);
//						double[] result=null;
//						try {
//							result = doRAA(security, factorFunds,interval,isWLSOrOLS,curDate,tu);
//						} catch (Exception e) {
//							continue;
//						}
//						if(result!=null){
//							++totalCount;
//							for(int j=0;j<factorFunds.length;++j){
//								totalResult[j]+=result[j];
//							}
//						}
//					}
//					if(totalCount!=0){
//						for(int j=0;j<factorFunds.length;++j){
//							totalResult[j] *= (1-moneyAllocation)/totalCount;
//						}
//					}
//				}
//			} catch (NoPriceException e) {
//				System.out.println(StringUtil.getStackTraceString(e));
//			}
//		}
//		return totalResult;
//	}
//	
//	static void main(String[] args) throws Exception
//	{
//		CompiledStrategy bs = new CompiledStrategy();
//		/*Date date = LTIDate.getDate(2007, 1, 1);
//		String[] names = {"CASH","AGG","CASH"}; 
//		try {
//			String[] names2 = bs.getTopSecurityArray(names, 1, date, TimeUnit.YEARLY, SortType.SHARPE, false);
//			System.out.println(names2[0]+" "+names2[1]+" "+names2[2]);
//		} catch (NoPriceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}*/
//		//testing the getLastYearDividendYield
////		Long[] securityID={191L,195L,196L,66L,190L,338L,4619L};
////		//System.out.println(bs.getFirstParentClassName("Moderate Allocation"));
////		System.out.println(bs.isUpperOrSameClassOfSymbol("HYBRID ASSETS", "WYASX"));
////		List<Security> securityList=new ArrayList<Security>();
////		for(int i=0;i<7;++i)
////			securityList.add(bs.securityManager.get(securityID[i]));
////		//Date startDate=LTIDate.getDate(2007,8,13);
////		int time=5;
////		Date currentDate=LTIDate.getDate(2007, 10, 10);
////		Date startDate=LTIDate.getNewNYSEMonth(currentDate, time*-1);
////		//Date endDate=LTIDate.getDate(2007, 11, 13);
////		securityList=bs.getTopSecurity(securityList, 6, time, currentDate, TimeUnit.MONTHLY,SortType.SHARPE, true);
////		//securityList=bs.testGetTopSecurity(6,startDate, currentDate, SortType.SHARPE, TimeUnit.MONTHLY, true);
////		if(securityList!=null && securityList.size()>0)
////		{
////			for(int i=0;i<securityList.size();++i)
////			{
////				System.out.println(securityList.get(i).getSymbol());
////			}
////		}
//		
//		List<String> availableAssetClassList = new ArrayList<String>();
//		List<String> availableFundList = new ArrayList<String>();
////		availableAssetClassList.add(65L);
////		avaableAssetClassList.add(99L);
////		avaableAssetClassList.add(8L);
////		avaibleAssetClassList.add(9L);
////		avaibleAssetClassList.add(17L);
//		availableFundList.add("PAALX");
//		availableFundList.add("CPMPX");
//		availableFundList.add("CTFDX");
//		availableFundList.add("HGAAX");
//		availableFundList.add("AAAZX");
//		availableFundList.add("ETO");
//		availableFundList.add("ETG");	
//		availableFundList.add("JGV");
//		availableFundList.add("ADF");	
//		availableFundList.add("ADX");
//		availableFundList.add("AGG");	
//		availableFundList.add("BEGBX");
//		HashMap<String, List<String>> availableFundMap = bs.getAvailableAssetClassSet(availableAssetClassList,availableFundList);
//		Iterator iterator =  availableFundMap.entrySet().iterator();
//		while(iterator.hasNext()){
//			Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>)iterator.next();
//			String assetClassName = entry.getKey();
//			List<String> fundList = entry.getValue();
//			System.out.println(assetClassName);
//			for(int i=0;i<fundList.size();++i){
//				System.out.println(fundList.get(i));
//			}
//		}
//		
//		//System.out.println(bs.getLastYearDividendYield("BFC", endDate));
//		
//	}

}
