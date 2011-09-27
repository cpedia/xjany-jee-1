package com.lti.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.cache.LTICache;
import com.lti.cache.YearCache;
import com.lti.service.IndicatorManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.util.LTIDate;
import com.lti.util.TimeMap;
import com.sun.tools.doclets.internal.toolkit.taglets.CodeTaglet;

@SuppressWarnings("unchecked")
public class SecurityManagerImplWithYearCache extends SecurityManagerImpl implements SecurityManager, Serializable {

	private static final long serialVersionUID = 540121870354332261L;
	
	private boolean isSandBox = false;
	
	private Date sandBoxEndDate = null;
	
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

	@Override
	public double getDividend(Long securityid, Date date) {
		try {
			return YearCache.getInstance().getDividend(securityid, date);
		} catch (NoPriceException e) {
			return 0.0;
		}
	}

	@Override
	public double getPrice(Long id, Date date) throws NoPriceException {
		return YearCache.getInstance().getClose(id, date);
	}

	@Override
	public Double getSplit(Long id, Date date) throws NoPriceException {
		return YearCache.getInstance().getSplit(id, date);
	}

	@Override
	public double getAdjPrice(Long id, Date date) throws NoPriceException {
		return YearCache.getInstance().getAdjClose(id, date);
	}

	@Override
	public double getOpenPrice(Long id, Date date) throws NoPriceException {
		return YearCache.getInstance().getOpen(id, date);
	}
	
	TimeMap<Long, Security> securityMap1 = new TimeMap<Long, Security>();
	TimeMap<String, Security> securityMap2 = new TimeMap<String, Security>();

	@Override
	public Security get(Long securityid) {

		Security s = securityMap1.get(securityid);
		if (s != null)
			return s;

		s = super.get(securityid);
		if (s != null) {
			securityMap1.put(securityid, s);
		}
		return s;
	}

	@Override
	public void saveOrUpdate(Security security) {
		securityMap1.remove(security.getID());
		securityMap2.remove(security.getSymbol());
		securityMap2.remove(security.getName());
		super.saveOrUpdate(security);
	}

	@Override
	public void update(Security security) {
		securityMap1.remove(security.getID());
		securityMap2.remove(security.getSymbol());
		securityMap2.remove(security.getName());
		super.update(security);
	}

	@Override
	public Security get(String name) {
		Security s = securityMap2.get(name);
		if (s != null)
			return s;

		s = super.get(name);
		if (s != null) {
			securityMap2.put(name, s);
		}
		return s;
	}

	@Override
	public double getAdjNAVPrice(Long id, Date date) throws NoPriceException {
		return YearCache.getInstance().getAdjNAV(id, date);
	}

	@Override
	public double getHighestPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		return getExtremePrice(id, startDate, endDate, YearCache.Type_Close, true);
	}

	@Override
	public double getLowestPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		return getExtremePrice(id, startDate, endDate, YearCache.Type_Close, false);
	}

	@Override
	public double getHighestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		return getExtremePrice(id, startDate, endDate, YearCache.Type_AdjClose, true);
	}

	@Override
	public double getLowestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		return getExtremePrice(id, startDate, endDate, YearCache.Type_AdjClose, false);
	}

	private double getExtremePrice(long id, Date startDate, Date endDate, int type, boolean highest) throws SecurityException {
		try {
			long start = startDate.getTime();
			Date startPriceDate = this.getStartDate(id);
			if (startPriceDate.getTime() > start) {
				start = startPriceDate.getTime();
			}

			long end = endDate.getTime();
			Date endPriceDate = this.getEndDate(id);
			if (endPriceDate.getTime() < end) {
				end = endPriceDate.getTime();
			}
			double price = Double.MAX_VALUE;
			if (highest)
				price = Double.MIN_VALUE;
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(start);
			ca.set(Calendar.HOUR_OF_DAY, 0);

			if (type == YearCache.Type_AdjClose) {
				if (highest) {
					while (ca.getTimeInMillis() < end) {
						double d = 0.0;
						d = this.getAdjPrice(id, ca.getTime());
						if (d > price)
							price = d;
						ca.add(Calendar.DAY_OF_YEAR, 1);
						ca.set(Calendar.HOUR_OF_DAY, 0);
					}
				} else {
					while (ca.getTimeInMillis() < end) {
						double d = 0.0;
						d = this.getAdjPrice(id, ca.getTime());
						if (d < price)
							price = d;
						ca.add(Calendar.DAY_OF_YEAR, 1);
						ca.set(Calendar.HOUR_OF_DAY, 0);
					}
				}
			} else if (type == YearCache.Type_Close) {
				if (highest) {
					while (ca.getTimeInMillis() < end) {
						double d = 0.0;
						d = this.getPrice(id, ca.getTime());
						if (d > price)
							price = d;
						ca.add(Calendar.DAY_OF_YEAR, 1);
						ca.set(Calendar.HOUR_OF_DAY, 0);
					}
				} else {
					while (ca.getTimeInMillis() < end) {
						double d = 0.0;
						d = this.getPrice(id, ca.getTime());
						if (d < price)
							price = d;
						ca.add(Calendar.DAY_OF_YEAR, 1);
						ca.set(Calendar.HOUR_OF_DAY, 0);
					}
				}
			}

			if (price == Double.MAX_VALUE || price == Double.MIN_VALUE) {
				throw new SecurityException();
			}
			return price;
		} catch (Exception e) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			throw se;
		}
	}

	public static void main(String[] args) {
		SecurityManager sm = ContextHolder.getSecurityManager();
		Date[][] dateArrays = new Date[4][2];
		dateArrays[0][0] = LTIDate.getDate(2007, 01, 02);
		dateArrays[0][1] = LTIDate.getDate(2008, 01, 01);
		dateArrays[1][0] = LTIDate.getDate(2004, 01, 02);
		dateArrays[1][1] = LTIDate.getDate(2006, 01, 01);
		dateArrays[2][0] = LTIDate.getDate(2000, 01, 02);
		dateArrays[2][1] = LTIDate.getDate(2009, 01, 01);
		dateArrays[3][0] = LTIDate.getDate(1990, 01, 02);
		dateArrays[3][1] = LTIDate.getDate(2009, 01, 01);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "SELECT min(close) FROM ltisystem_securitydailydata l where securityid=942 and date >='{1}' and date <='{2}';";

		for (int i = 0; i < 20; i++) {
			try {
				long t1 = System.currentTimeMillis();
				System.out.println(sm.getLowestPrice(942l, dateArrays[i % 4][0], dateArrays[i % 4][1]));
				System.out.println(sql.replace("{1}", df.format(dateArrays[i % 4][0])).replace("{2}", df.format(dateArrays[i % 4][1])));
				long t2 = System.currentTimeMillis();
				System.out.println("Times: " + (t2 - t1));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc) Please notice that this API just return
	 * AdjClose,Close,Dividend,Split,NAV
	 * 
	 * @see com.lti.service.impl.SecurityManagerImpl#getDailyDatas(java.lang.Long,
	 *      java.util.Date, java.util.Date)
	 */
	// @Override
	// public List<SecurityDailyData> getDailyDatas(Long id, Date startDate,
	// Date endDate) {
	// // TODO Auto-generated method stub
	// List<SecurityDailyData> sdds=new ArrayList<SecurityDailyData>();
	// List<Date> dates=LTIDate.getTradingDates(startDate, endDate);
	// for(int i=0;i<dates.size();i++){
	//			
	// }
	// if(sdds.size()!=0)return sdds;
	// else return null;
	// }
	public List<SecurityDailyData> getDailyDatasByYearCache(Long id, Date startDate, Date endDate) {
		Date _startDate = this.getStartDate(id);
		Date _endDate = this.getEndDate(id);
		if (_startDate.before(startDate)) {
			_startDate = startDate;
		}
		if (_endDate.after(endDate)) {
			_endDate = endDate;
		}

		List<Date> tradingDates = tradingDates = LTIDate.getTradingDates(_startDate, _endDate);
		List<SecurityDailyData> list = new ArrayList<SecurityDailyData>(tradingDates.size());
		try {
			for (int i = 0; i < tradingDates.size(); i++) {
				Date date = tradingDates.get(i);
				SecurityDailyData sdd = new SecurityDailyData();
				sdd.setDate(date);
				sdd.setAdjClose(this.getAdjPrice(id, date));
				sdd.setNAV(this.getNAVPrice(id, date));
				sdd.setSplit(this.getSplit(id, date));
				sdd.setDividend(this.getDividend(id, date));
				sdd.setClose(this.getPrice(id, date));
				list.add(sdd);
			}
		} catch (Exception e) {
			throw new RuntimeException("Get Daily Datas error.", e);
		}
		return list;
	}

	// ==================================================================

	private LTICache dailyDataCache;

	public boolean useOldCache() {
		Integer count = counts.get();
		if (count == null) {
			dailyDataCache.getSecurityDailyDataCache().removeAll();
			count = 0;
		}

		if (count > 20) {
			dailyDataCache = dailyDataCache.getSecurityDailyDataCache();
			// System.out.println("use old cache.");
			return true;
		}

		count++;
		counts.set(count);

		return false;
	}

	public static ThreadLocal<Integer> counts = new ThreadLocal<Integer>();

	@Override
	public List<SecurityDailyData> getCloseList(Long securityid, Date startDate) {
		if (useOldCache()) {
			List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);

			if (sdds == null && dailyDataCache.isBusy()) {
				return super.getCloseList(securityid, startDate);
			}

			if (sdds != null && sdds.size() > 0) {
				int start = locateStart(sdds, startDate);
				if (start == -1)
					return null;
				return sdds.subList(start, sdds.size());
			}
			return null;
		} else {
			return super.getCloseList(securityid, startDate);
		}

	}

	@Override
	public List<SecurityDailyData> getDailyData(Long id, Object[] dates) {
		if (useOldCache()) {
			if (id == null || dates == null || dates.length <= 1) {
				return null;
			}

			List<SecurityDailyData> sdds = this.getDailydatasWithCache(id, true);

			if (sdds == null && dailyDataCache.isBusy()) {
				return super.getDailyData(id, dates);
			}

			if (sdds != null && sdds.size() > 0) {

				if (LTIDate.before((Date) dates[dates.length - 1], sdds.get(0).getDate())) {
					return null;
				}
				if (LTIDate.after((Date) dates[0], sdds.get(sdds.size() - 1).getDate())) {
					return null;
				}
				List<SecurityDailyData> newSdds = new ArrayList<SecurityDailyData>();
				int pointer = 0;
				for (int i = 0; i < sdds.size(); i++) {
					if (pointer == dates.length)
						break;
					SecurityDailyData sdd = sdds.get(i);
					if (LTIDate.equals(sdd.getDate(), (Date) dates[pointer])) {
						newSdds.add(sdd);
						pointer++;
						continue;
					}
					if (LTIDate.after(sdd.getDate(), (Date) dates[pointer])) {
						newSdds.add(sdds.get(i - 1));
						pointer++;
						continue;
					}
				}
				return newSdds;

			}

			return null;
		} else {
			return super.getDailyData(id, dates);
		}

	}

	public List<SecurityDailyData> getDailydatasWithCache(Long id, boolean flag) {
		if (id == null) {
			return null;
		}
		List<SecurityDailyData> sdds = (List<SecurityDailyData>) dailyDataCache.get(id);
		if (sdds == null && (!dailyDataCache.isBusy() || isSandBox)) {
			sdds = super.getDailydatasFromDB(id, flag, null, null);
			if (sdds != null) {//在这里截助，如果是sandbox, 则必须将未来的一些虚拟数据导入
				if(isSandBox){
					SecurityDailyData lastSdd = sdds.get(sdds.size() - 1);
					Date futureStartDate = LTIDate.getNewNYSETradingDay(lastSdd.getDate(), 1);
					List<Date> futureDates = LTIDate.getTradingDates(futureStartDate, sandBoxEndDate);
					if(futureDates != null && futureDates.size() > 0){
						for(Date date: futureDates){
							SecurityDailyData sdd = new SecurityDailyData();
							sdd.setDate(date);
							sdd.setSecurityID(lastSdd.getSecurityID());
							
							sdd.setAdjClose(lastSdd.getAdjClose());
							sdd.setAdjNAV(lastSdd.getAdjNAV());
							sdd.setClose(lastSdd.getClose());
							sdd.setOpen(lastSdd.getOpen());
							sdd.setDividend(0.0);
							sdd.setSplit(1.0);
							sdds.add(sdd);
						}
					}
				}
				dailyDataCache.put(id, sdds);
			} else {
				return null;
			}
		}
		return sdds;
	}

	@Override
	public List<SecurityDailyData> getDailyDatas(Long id, Date startDate, Date endDate) {
		if (useOldCache()) {
			List<SecurityDailyData> sdds = getDailydatasWithCache(id, true);

			if (sdds == null && dailyDataCache.isBusy()) {
				return super.getDailyDatas(id, startDate, endDate);
			}

			int start = locateStart(sdds, startDate);
			int end = locateEnd(sdds, endDate);
			if (start != -1 && end != -1 && end >= start) {
				return sdds.subList(start, end + 1);
			}
			return null;
		} else {
			return super.getDailyDatas(id, startDate, endDate);
		}
	}

	
	
	@Override
	public SecurityDailyData getLatestDailydata(Long id, Date date) {
		if(useOldCache()){
			List<SecurityDailyData> sdds = getDailydatasWithCache(id, true);
			
			if(sdds==null&&dailyDataCache.isBusy()){
				return super.getLatestDailydata(id,date);
			}
			
			int p = locateEnd(sdds, date);
			if (p != -1) {
				return sdds.get(p);
			}
			return null;
		}else{
			return super.getLatestDailydata(id, date);
		}
		
	}

	public List<SecurityDailyData> getDividendList(Long securityid) {
		if (useOldCache()) {
			List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, false);
			if (sdds != null && sdds.size() > 0) {
				List<SecurityDailyData> ds = new ArrayList<SecurityDailyData>();
				for (int i = 0; i < sdds.size(); i++) {
					if (sdds.get(i).getDividend() != null && sdds.get(i).getDividend() > 0.0)
						ds.add(sdds.get(i));
				}
				return ds;
			}
			return null;
		} else {
			return super.getDividendList(securityid);
		}

	}

	@Override
	public List<SecurityDailyData> getNAVList(Long securityid) {

		if (!useOldCache())
			return super.getNAVList(securityid);

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		if (sdds == null && dailyDataCache.isBusy()) {
			return super.getNAVList(securityid);
		}

		if (sdds != null && sdds.size() > 0) {
			List<SecurityDailyData> ds = new ArrayList<SecurityDailyData>();
			for (int i = 0; i < sdds.size(); i++) {
				if (sdds.get(i).getNAV() != null && sdds.get(i).getNAV() > 0.0)
					ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;
	}

	public List<SecurityDailyData> getNAVList(Long securityid, Date startDate) {

		if (!useOldCache())
			return super.getNAVList(securityid, startDate);

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		if (sdds == null && dailyDataCache.isBusy()) {
			return super.getNAVList(securityid, startDate);
		}
		if (sdds != null && sdds.size() > 0) {
			List<SecurityDailyData> ds = new ArrayList<SecurityDailyData>();
			int start = locateStart(sdds, startDate);
			if (start == -1)
				return null;
			for (int i = start; i < sdds.size(); i++) {
				if (sdds.get(i).getNAV() != null && sdds.get(i).getNAV() > 0.0)
					ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;
	}

	public List<SecurityDailyData> getNAVList(Long securityid, Date startDate, Date endDate) {

		if (!useOldCache())
			return super.getNAVList(securityid, startDate, endDate);

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		if (sdds == null && dailyDataCache.isBusy()) {
			return super.getNAVList(securityid, startDate, endDate);
		}
		if (sdds != null && sdds.size() > 0) {
			List<SecurityDailyData> ds = new ArrayList<SecurityDailyData>();
			int start = locateStart(sdds, startDate);
			int end = locateStart(sdds, endDate);
			if (start == -1 || end == -1)
				return null;
			for (int i = start; i <= end; i++) {
				if (sdds.get(i).getNAV() != null && sdds.get(i).getNAV() > 0.0)
					ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;

	}

	// 1 2 3 5 6 7 8 9
	// locate 4 will return 3
	private int locateEnd(List<SecurityDailyData> sdds, Date date) {
		if (sdds != null && sdds.size() != 0) {
			int start = 0;
			int end = sdds.size() - 1;

			if (LTIDate.before(date, sdds.get(0).getDate())) {
				return -1;
			} else if (LTIDate.equals(date, sdds.get(0).getDate())) {
				return 0;
			} else if (LTIDate.after(date, sdds.get(end).getDate()) || LTIDate.equals(date, sdds.get(end).getDate())) {
				return end;
			}

			while (end >= start) {
				int m = (start + end) / 2;
				SecurityDailyData sdd = sdds.get(m);
				if (LTIDate.equals(date, sdd.getDate())) {
					return m;
				} else {
					if (start == end) {
						if (LTIDate.equals(sdd.getDate(), date)) {
							return start;
						} else {
							while (LTIDate.after(sdds.get(start).getDate(), date)) {
								start--;
							}
							return start;
						}
					}
					if (sdd.getDate().before(date)) {
						start = m + 1;
					} else {
						end = m - 1;
					}
				}
			}// end while

			while (LTIDate.after(sdds.get(start).getDate(), date)) {
				start--;
			}
			return start;
		}
		return -1;
	}

	// 1 2 3 5 6 7 8 9
	// locate 4 will return -1
	private int locatePosition(List<SecurityDailyData> sdds, Date date) {
		if (sdds == null || sdds.size() == 0 || date == null) {
			return -1;
		}

		if (sdds != null) {
			if (LTIDate.before(date, sdds.get(0).getDate())) {
				return -1;
			}
			int start = 0;
			int end = sdds.size() - 1;
			while (end >= start) {
				int m = (start + end) / 2;
				SecurityDailyData sdd = sdds.get(m);
				if (LTIDate.equals(date, sdd.getDate())) {
					return m;
				} else {
					if (sdd.getDate().before(date)) {
						start = m + 1;
					} else {
						end = m - 1;
					}
				}
			}// end while
		}

		return -1;
	}

	// 1 2 3 5 6 7 8 9
	// locate 4 will return 5
	private int locateStart(List<SecurityDailyData> sdds, Date date) {
		if (sdds != null && sdds.size() != 0) {
			int start = 0;
			int end = sdds.size() - 1;
			if (LTIDate.after(date, sdds.get(end).getDate())) {
				return -1;
			} else if (LTIDate.equals(date, sdds.get(end).getDate())) {
				return end;
			} else if (LTIDate.before(date, sdds.get(0).getDate()) || LTIDate.equals(date, sdds.get(0).getDate())) {
				return 0;
			}

			while (end >= start) {
				int m = (start + end) / 2;
				SecurityDailyData sdd = sdds.get(m);
				if (LTIDate.equals(date, sdd.getDate())) {
					return m;
				} else {
					if (start == end) {
						if (LTIDate.equals(sdd.getDate(), date)) {
							return start;
						} else {
							while (LTIDate.before(sdds.get(start).getDate(), date)) {
								start++;
							}
							return start;
						}
					}
					if (sdd.getDate().before(date)) {
						start = m + 1;
					} else {
						end = m - 1;
					}
				}
			}// end while

			while (LTIDate.before(sdds.get(start).getDate(), date)) {
				start++;
			}
			return start;
		}
		return -1;
	}

//	public double getAnnualGSPCYeild(Date startDate, Date endDate) throws Exception {
//
//		IndicatorManager sm = (IndicatorManager) ContextHolder.getIndicatorManager();
//		Double yeild = 0d;
//		Long securityID = getBySymbol("^GSPC").getID();
//		Long indicatorID = sm.get("SPDIVIDEND").getID();
//		List<Date> tradingDates = LTIDate.getTradingDates(startDate, endDate);
//		if (tradingDates != null) {
//			for (int i = 0; i < tradingDates.size(); i++) {
//				BigDecimal b1 = new BigDecimal(yeild.toString());
//				BigDecimal b2 = new BigDecimal(this.getDividend(indicatorID, tradingDates.get(i)));
//				yeild = b2.add(b1).doubleValue();
//			}
//		}
//
//		BigDecimal c1 = new BigDecimal(yeild.toString());
//		BigDecimal c2 = new BigDecimal(String.valueOf(this.getPrice(securityID, endDate)));
//		yeild = c1.divide(c2, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
//		return yeild;
//
//	}
}
