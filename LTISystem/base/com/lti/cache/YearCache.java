package com.lti.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


import com.lti.Exception.Security.NoPriceException;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class YearCache {
	public final static int Time_Out = 3600 * 1000;
	public final static int Size = 100000;
	private boolean isDebug = false;
	private boolean isSandBox = false;
	private Date sandBoxEndDate;
	DateMap dateMap = null;

	Map<Long, YearSerial> caches = new HashMap<Long, YearSerial>();

	private Queue<Long> keyQueue = new LinkedList<Long>();

	private static YearCache instance = null;

	public static synchronized YearCache getInstance() {
		if (instance == null) {
			instance = new YearCache();
		}
		return instance;
	}

	private YearCache() {
		dateMap = DateMap.getInstance();
		securityManager = ContextHolder.getSecurityManager();
	}

	private com.lti.service.SecurityManager securityManager;

	public YearSerial getYearSerial(int year, long securityid) {
		YearSerial ys = caches.get(getKey(year, securityid));
		if (ys != null && System.currentTimeMillis() - ys.timeStamp > Time_Out) {
			clearCache();
			ys = null;
		}

		if (ys == null) {
			ys = loadCache(year, securityid);
		}
		return ys;
	}

	public double getSplit(long securityid, Date pdate) throws NoPriceException {
		return getValue(securityid, pdate, Type_Split);
	}

	public double getClose(long securityid, Date pdate) throws NoPriceException {
		return getValue(securityid, pdate, Type_Close);
	}
	
	public double getOpen(long securityid, Date pdate) throws NoPriceException {
		return getValue(securityid, pdate, Type_Open);
	}

	public double getDividend(long securityid, Date pdate) throws NoPriceException {
		return getValue(securityid, pdate, Type_Dividend);
	}

	public double getAdjClose(long securityid, Date pdate) throws NoPriceException {
		return getValue(securityid, pdate, Type_AdjClose);
	}
	
	public double getAdjNAV(long securityid, Date pdate) throws NoPriceException {
		return getValue(securityid, pdate, Type_AdjNAV);
	}

	public final static int Type_Split = 0;
	public final static int Type_Dividend = 1;
	public final static int Type_Close = 2;
	public final static int Type_AdjClose = 3;
	public final static int Type_AdjNAV = 4;
	public final static int Type_Open = 5;

	public double getValue(long securityid, Date pdate, int type) throws NoPriceException {
		int number = dateMap.getNumber(pdate);
		int year = number / 1000;
		YearSerial ys = getYearSerial(year, securityid);
		
		if (ys == null) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(securityid);
			se.setDate(pdate);
			Security s = securityManager.get(securityid);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		double result = Double.MIN_VALUE;
		if (type == Type_Close) {
			result = ys.getClose(number % 1000);
		}else if (type == Type_Open){
			result = ys.getOpen(number % 1000);
		}else if (type == Type_Split) {
			result = ys.getSplit(number % 1000);
		} else if (type == Type_Dividend) {
			result = ys.getDividend(number % 1000);
		} else if (type == Type_AdjClose) {
			result = ys.getAdjClose(number % 1000);
		} else if (type == Type_AdjNAV) {
			result = ys.getAdjNAV(number % 1000);
		}
		
		if (result == Double.MIN_VALUE) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(securityid);
			se.setDate(pdate);
			Security s = securityManager.get(securityid);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}
		return result;
	}

	private long getKey(int year, long securityid) {
		return year * 100000 + securityid;
	}

	/**
	 * 在这里先判断是不是最后一年的数据并且处于sandbox模式，如果是的话就将未来的虚拟数据一并加入到cache中去
	 * @param year
	 * @param securityid
	 * @return
	 */
	private YearSerial loadCache(int year, long securityid) {
		synchronized (this) {
			if (isDebug)
				System.out.println("Load: "+securityid+"["+caches.size()+"]");
			Calendar ca = Calendar.getInstance();
			ca.setTime(new Date());
			int curYear = ca.get(Calendar.YEAR);
			boolean lastYear = (year == curYear);
			
			long key = getKey(year, securityid);
			if (caches.get(key) != null)
				return caches.get(key);

			String sql = null;
			Security s = securityManager.get(securityid);
			boolean isPortfolio=false;
			if (s == null)
				return null;
			if (s.getSymbol().startsWith("P_")) {
				Long portfolioid;
				try {
					portfolioid = Long.parseLong(s.getSymbol().substring(2));
				} catch (Exception e) {
					return null;
				}
				sql = "SELECT date,amount,1,0,amount,amount,amount FROM ltisystem_portfoliodailydata l where portfolioid=" + portfolioid + " and date >= '" + year + "-01-01' and date <='" + year + "-12-31'";
				isPortfolio=true;
			} else {
				sql = "SELECT date,close,split,dividend,adjclose,adjnav,open FROM ltisystem_securitydailydata l where securityid=" + securityid + " and date >= '" + year + "-01-01' and date <='" + year + "-12-31' and close is not null and adjclose is not null";
			}

			try {
				List<Object[]> records = securityManager.findBySQL(sql);
				if (records != null && records.size() > 0) {
					int count = 0;
					YearSerial ys = new YearSerial(year, securityid);
					for (int i = 0; i < records.size(); i++) {
						int number = dateMap.getNumber((Date) records.get(i)[0]);
						count = number % 1000;
						if(number/1000!=year){
							count=1;
							//System.out.println((Date) records.get(i)[0]+" "+number);
						}
						Double close = (Double) records.get(i)[1];
						Double split =null;
						Double dividend =null;
						if(isPortfolio){
							split = 1.0;
							dividend = 0.0;
						}else{
							split = (Double) records.get(i)[2];
							dividend = (Double) records.get(i)[3];
						}
						Double adjclose = (Double) records.get(i)[4];
						Double adjnav = (Double) records.get(i)[5];
						Double open = (Double) records.get(i)[6];
						ys.closes[count] = close == null ? 0.0 : close;
						ys.splits[count] = split == null ? 1 : split;
						ys.dividends[count] = dividend == null ? 0.0 : dividend;
						ys.adjcloses[count] = adjclose == null ? 0.0 : adjclose;
						ys.adjnavs[count] = adjnav == null ? Double.MIN_VALUE : adjnav;
						ys.opens[count] = open == null ? 0.0 : open;
					}
					if(lastYear && isSandBox){//如果是最后一年并且是sandbox,加数据
						double lastOpen = ys.opens[count];
						double lastClose = ys.closes[count];
						double lastAdjnav = ys.adjnavs[count];
						double lastAdjclose = ys.adjcloses[count];
						Date futureStartDate = LTIDate.getNewNYSETradingDay((Date)records.get(records.size() - 1)[0], 1);
						List<Date> futureDates = LTIDate.getTradingDates(futureStartDate, sandBoxEndDate);
						if(futureDates != null && futureDates.size() > 0){
							for(int i=0;i<futureDates.size();++i){
								int number = dateMap.getNumber(futureDates.get(i));
								count = number % 1000;
								if(number/1000!=year){
									count=1;
								}
								ys.closes[count] = lastClose;
								ys.splits[count] = 1.0;
								ys.dividends[count] = 0.0;
								ys.adjcloses[count] = lastAdjclose;
								ys.adjnavs[count] = lastAdjnav;
								ys.opens[count] = lastOpen;
							}
							
						}
					}
					ys.symbol = s.getSymbol();
					ys.securityid=securityid;
					ys.year=year;
					fixSpacing(ys,s);
					caches.put(key, ys);
					if (isDebug)
						System.out.println("Put: "+securityid+"["+caches.size()+"]");
					keyQueue.add(key);
					while (caches.size() > Size) {
						caches.remove(keyQueue.poll());
					}
					return ys;
				}
			} catch (Exception e) {
				System.out.println(StringUtil.getStackTraceString(e)+"["+caches.size()+"]");
			}
			return null;
		}
	}
	
	private void fixSpacing(YearSerial ys,Security s){
		Date startDate=securityManager.getStartDate(s.getID());
		Date endDate=securityManager.getEndDate(s.getID());
		
		Calendar ca=Calendar.getInstance();
		ca.setTime(startDate);
		int startYear=ca.get(Calendar.YEAR);
		if(startYear<ys.year){
			String sql = null; 
			if(ys.closes[1]==Double.MIN_VALUE){
				if (s.getSymbol().startsWith("P_")) {
					Long portfolioid;
					try {
						portfolioid = Long.parseLong(s.getSymbol().substring(2));
					} catch (Exception e) {
						throw new RuntimeException("Portfolio ID is not valid when parsing securitySymbol",e);
					}
					sql = "SELECT date,amount,1,0,amount,amount,amount FROM ltisystem_portfoliodailydata l where portfolioid=" + portfolioid + " and date <= '" + ys.year + "-01-01' order by date desc limit 0,1";
				} else {
					sql = "SELECT date,close,split,dividend,adjclose,adjnav,open FROM ltisystem_securitydailydata l where securityid=" + s.getID() + " and date <= '" + ys.year + "-01-01' and close is not null and adjclose is not null order by date desc limit 0,1";
				}
				try{
					List<Object[]> records = securityManager.findBySQL(sql);
					if (records != null && records.size() > 0) {
						Double close = (Double) records.get(0)[1];
						Double split = (Double) records.get(0)[2];
						Double dividend = (Double) records.get(0)[3];
						Double adjclose = (Double) records.get(0)[4];
						Double adjnav = (Double) records.get(0)[5];
						Double open = (Double) records.get(0)[6];
						ys.closes[1] = close == null ? 0.0 : close;
						ys.splits[1] = split == null ? 1 : split;
						ys.dividends[1] = dividend == null ? 0.0 : dividend;
						ys.adjcloses[1] = adjclose == null ? 0.0 : adjclose;
						ys.adjnavs[1] = adjnav == null ? 0.0 : adjnav;
						ys.opens[1] = open == null ? 0.0 : open;
					}
				}catch(Exception e){}
			}//end if(ys.closes[1]==Double.MIN_VALUE)
		}//end if(startYear<ys.year)

		int number = dateMap.getNumber(endDate);
		int upper=number%1000;
		int endYear = number / 1000;
		
		int len=ys.closes.length;
		if(endYear==ys.year){
			len=upper+1;
		}
		//the if-else-if
		boolean first=false;
		for(int i=0;i<len;i++){
			if(ys.closes[i]!=Double.MIN_VALUE){
				first=true;
			}else{
				if(first){
					ys.closes[i] = ys.closes[i-1];
					ys.splits[i] = ys.splits[i-1];
					ys.dividends[i] = ys.dividends[i-1];
					ys.adjcloses[i] = ys.adjcloses[i-1];
					ys.adjnavs[i] = ys.adjnavs[i-1];
					ys.opens[i] = ys.opens[i-1];
				}
			}
		}
	}

	public void clearCache() {
		synchronized (this) {
			while (!keyQueue.isEmpty()) {
				Long key = keyQueue.peek();
				YearSerial ys = caches.get(key);
				if (System.currentTimeMillis() - ys.timeStamp > Time_Out) {
					caches.remove(keyQueue.poll());
					if (isDebug)
						System.out.println("Remove: " + key+"["+caches.size()+"]");
				} else
					break;
			}
			while (keyQueue.size() > Size) {
				caches.remove(keyQueue.poll());
			}
		}
	}
	
	public void removeAll(){
		synchronized (this) {
			if (isDebug)
				System.out.println("Remove: All"+"["+caches.size()+"]" );
			while (!keyQueue.isEmpty()) {
				caches.remove(keyQueue.poll());
				
			}
		}
	}
	
	
	public static Map<Long, double[][]> maps = new HashMap<Long, double[][]>();
	
	public static void init()throws Exception{
		long securityid = 942l;
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		String sql = null;
		Security s = securityManager.get(securityid);
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid;
			portfolioid = Long.parseLong(s.getSymbol().substring(2));
			sql = "SELECT date,amount FROM ltisystem_portfoliodailydata l where portfolioid=" + portfolioid + " order by amount desc";
		} else {
			sql = "SELECT date,close FROM ltisystem_securitydailydata l where securityid=" + securityid + " and close is not null order by close desc";
		}
		Map<Long, double[][]> maps = new HashMap<Long, double[][]>();
		long t1=System.currentTimeMillis();
		List<Object[]> records = securityManager.findBySQL(sql);
		if (records != null && records.size() > 0) {
			
			arrays = new double[records.size()][2];
			for (int i = 0; i < records.size(); i++) {
				arrays[i][0] = ((Date) records.get(i)[0]).getTime();
				arrays[i][1] = (Double) records.get(i)[1];
			}
			maps.put(securityid, arrays);
		}
		long t2=System.currentTimeMillis();
		System.out.println("Times: "+(t2-t1));
	}
	public static double[][] arrays;
	public static double getHigh(Date start,Date end)throws Exception{
		//double[][] arrays=maps.get(942l);
		long s=start.getTime();
		long e=end.getTime();
		for(int i=0;i<arrays.length;i++){
			if(arrays[i][0]>=s&&arrays[i][0]<=e){
				return arrays[i][1];
			}
		}
		throw new NoPriceException();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(Math.pow(32,0.2));

//		init();
//		Date[][] dateArrays=new Date[2][2];
//		dateArrays[0][0]=LTIDate.getDate(2007, 01, 02);
//		dateArrays[0][1]=LTIDate.getDate(2008, 01, 01);
//		dateArrays[1][0]=LTIDate.getDate(2004, 01, 02);
//		dateArrays[1][1]=LTIDate.getDate(2006, 01, 01);
//		for(int i=0;i<2;i++){
//			try{
//				long t1=System.currentTimeMillis();
//				System.out.println(getHigh(dateArrays[i][0],dateArrays[i][1]));
//				long t2=System.currentTimeMillis();
//				System.out.println("Times: "+(t2-t1));
//				
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		
		// Warm up all classes/methods we will use
//		runGC();
//		usedMemory();
//		long heap1 = 0;
//		
//		SecurityManager sm=ContextHolder.getSecurityManager();
//		List<Security> secs=sm.getSecurities();
//		for(int i=0;i<secs.size();i++){
//			Date startDate=sm.getStartDate(secs.get(i).getID());
//			if(startDate==null)continue;
//			Calendar ca=Calendar.getInstance();
//			ca.setTime(startDate);
//			while(startDate.before(new Date())){
//				try {
//					secs.get(i).getAdjClose(startDate);
//					
//				} catch (Exception e) {
//				}
//				ca.add(Calendar.YEAR, 1);
//				startDate=ca.getTime();
//			}
//			System.out.println("size:"+YearCache.getInstance().caches.size());
//			System.out.println("used Memory:"+usedMemory());
//			System.out.println("totaled Memory:"+s_runtime.totalMemory());
//			runGC();
//			long heap2 = usedMemory(); // Take an after heap snapshot:
//			System.out.println("Average:"+(heap2-heap1)/YearCache.getInstance().caches.size());
//		}

	}
	private static long usedMemory() {
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}

	private static final Runtime s_runtime = Runtime.getRuntime();
	
	private static void runGC() throws Exception {
		// It helps to call Runtime.gc()
		// using several method calls:
		for (int r = 0; r < 4; ++r)
			_runGC();
	}

	private static void _runGC() throws Exception {
		long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) {
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.currentThread().yield();

			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}

	public boolean isSandBox() {
		return isSandBox;
	}

	public void setSandBox(boolean isSandBox) {
		this.isSandBox = isSandBox;
	}

	public Date getSandBoxEndDate() {
		return sandBoxEndDate;
	}

	public void setSandBoxEndDate(Date sandBoxEndDate) {
		this.sandBoxEndDate = sandBoxEndDate;
	}
}
