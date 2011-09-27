package com.lti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.Exception.SecurityException;
import com.lti.cache.LTICache;
import com.lti.service.SecurityManager;
import com.lti.service.bo.SecurityDailyData;
import com.lti.util.LTIDate;

@SuppressWarnings("unchecked")
public class SecurityManagerImplWithCache extends SecurityManagerImpl implements SecurityManager, Serializable {
	
	private static final long serialVersionUID = 1L;

	protected LTICache dailyDataCache;
	
	public SecurityManagerImplWithCache(){
		dailyDataCache=LTICache.getSecurityDailyDataCache();
	}
	
	public void setDailyDataCache(LTICache cache){
		dailyDataCache=cache;
	}
	
	@Override
	public List<SecurityDailyData> getCloseList(Long securityid, Date startDate) {
		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getCloseList(securityid, startDate);
		}
		
		if (sdds != null && sdds.size() > 0) {
			int start = locateStart(sdds, startDate);
			if (start == -1)
				return null;
			return sdds.subList(start, sdds.size());
		}
		return null;
	}
	@Override
	public SecurityDailyData getDailydata(Long id, Date date, boolean flag) {

		List<SecurityDailyData> sdds = getDailydatasWithCache(id, flag);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getDailydata(id, date, flag);
		}
		
		int p = locatePosition(sdds, date);
		if (p != -1) {
			return sdds.get(p);
		}
		return null;
	}
	@Override
	public List<SecurityDailyData> getDailyData(Long id, Object[] dates) {
		if (id == null || dates == null || dates.length <= 1) {
			return null;
		}

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(id, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
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
	}


	
	public List<SecurityDailyData> getDailydatasWithCache(Long id, boolean flag) {
		if (id == null) {
			return null;
		}
		List<SecurityDailyData> sdds = (List<SecurityDailyData>) dailyDataCache.get(id);
		if (sdds == null && !dailyDataCache.isBusy()) {
			sdds = super.getDailydatasFromDB(id, flag,null,null);
			if (sdds != null) {
				dailyDataCache.put(id, sdds);
			} else {
				return null;
			}
		} 
		return sdds;
	}
	@Override
	public List<SecurityDailyData> getDailydatas(Long id, boolean flag) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(id, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getDailydatas(id, flag);
		}
		
		return sdds;
	}


	@Override
	public List<SecurityDailyData> getDailyDatas(Long id, Date startDate, Date endDate) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(id, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getDailyDatas(id, startDate, endDate);
		}
		
		int start = locateStart(sdds, startDate);
		int end = locateEnd(sdds, endDate);
		if (start != -1 && end != -1 && end >= start) {
			return sdds.subList(start, end + 1);
		}
		return null;
	}


	public List<SecurityDailyData> getDividendList(Long securityid) {

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
	}


	@Override
	public double getHighestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException {

		double maxPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

		if(dailydataList==null&&dailyDataCache.isBusy()){
			return super.getHighestAdjPrice(id, startDate, endDate);
		}
		
		maxPrice = -1;

		if (dailydataList != null) {
			for (int i = 0; i < dailydataList.size(); i++) {

				if (dailydataList.get(i).getAdjClose() != null && dailydataList.get(i).getAdjClose() > maxPrice) {

					maxPrice = dailydataList.get(i).getAdjClose();

				}
			}
		}

		if (maxPrice == -1) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			throw se;
		}
		return maxPrice;
	}


	@Override
	public double getHighestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		// TODO Auto-generated method stub
		double maxPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

		if(dailydataList==null&&dailyDataCache.isBusy()){
			return super.getHighestNAVPrice(id, startDate, endDate);
		}
		
		maxPrice = -1;

		if (dailydataList != null) {
			for (int i = 0; i < dailydataList.size(); i++) {

				if (dailydataList.get(i).getAdjNAV() != null && dailydataList.get(i).getAdjNAV() > maxPrice) {

					maxPrice = dailydataList.get(i).getAdjNAV();

				}
			}
		}

		if (maxPrice == -1) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			throw se;
		}
		return maxPrice;
	}

	@Override
	public double getHighestPrice(Long id, Date startDate, Date endDate) throws SecurityException {

		double maxPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

		if(dailydataList==null&&dailyDataCache.isBusy()){
			return super.getHighestPrice(id, startDate, endDate);
		}
		
		maxPrice = -1;

		if (dailydataList != null) {
			for (int i = 0; i < dailydataList.size(); i++) {

				if (dailydataList.get(i).getClose() != null && dailydataList.get(i).getClose() > maxPrice) {

					maxPrice = dailydataList.get(i).getClose();

				}
			}
		}

		if (maxPrice == -1) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			throw se;
		}
		return maxPrice;
	}


	@Override
	public SecurityDailyData getLatestDailydata(Long securityid) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(securityid, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getLatestDailydata(securityid);
		}
		
		if (sdds != null && sdds.size() > 0)
			return sdds.get(sdds.size() - 1);
		else
			return null;
	}

	@Override
	public SecurityDailyData getLatestDailydata(Long id, Date date) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(id, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getLatestDailydata(id,date);
		}
		
		int p = locateEnd(sdds, date);
		if (p != -1) {
			return sdds.get(p);
		}
		return null;

	}

	public SecurityDailyData getLatestNAVDailydata(Long id, Date date) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(id, true);
		
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getLatestNAVDailydata(id,date);
		}
		
		int p = locateEnd(sdds, date);
		if (p != -1) {
			SecurityDailyData sdd = sdds.get(p);
			while (sdd.getNAV() == null || sdd.getNAV() <= 0.0) {
				p--;
				if (p < 0)
					return null;
				sdd = sdds.get(p);
			}
			return sdd;
		}
		return null;

	}



	@Override
	public double getLowestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException {

		double minPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

		if(dailydataList==null&&dailyDataCache.isBusy()){
			return super.getLowestAdjPrice(id, startDate, endDate);
		}
		
		minPrice = Double.MAX_VALUE;

		if (dailydataList != null) {
			for (int i = 0; i < dailydataList.size(); i++) {

				if (dailydataList.get(i).getAdjClose() != null && dailydataList.get(i).getAdjClose() < minPrice) {

					minPrice = dailydataList.get(i).getAdjClose();

				}
			}
		}

		if (minPrice == Double.MAX_VALUE) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			throw se;
		}
		return minPrice;
	}


	@Override
	public double getLowestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		// TODO Auto-generated method stub
		double minPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

		if(dailydataList==null&&dailyDataCache.isBusy()){
			return super.getLowestNAVPrice(id, startDate, endDate);
		}
		
		minPrice = Double.MAX_VALUE;

		if (dailydataList != null) {
			for (int i = 0; i < dailydataList.size(); i++) {

				if (dailydataList.get(i).getAdjNAV() != null && dailydataList.get(i).getAdjNAV() < minPrice) {

					minPrice = dailydataList.get(i).getAdjNAV();

				}
			}
		}

		if (minPrice == Double.MAX_VALUE) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			throw se;
		}
		return minPrice;
	}

	@Override
	public double getLowestPrice(Long id, Date startDate, Date endDate) throws SecurityException {

		double minPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

		if(dailydataList==null&&dailyDataCache.isBusy()){
			return super.getLowestPrice(id, startDate, endDate);
		}
		
		minPrice = Double.MAX_VALUE;

		if (dailydataList != null) {
			for (int i = 0; i < dailydataList.size(); i++) {

				if (dailydataList.get(i).getClose() != null && dailydataList.get(i).getClose() < minPrice) {

					minPrice = dailydataList.get(i).getClose();

				}
			}
		}

		if (minPrice == Double.MAX_VALUE) {
			SecurityException se = new SecurityException();
			se.setSecurityID(id);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			throw se;
		}
		return minPrice;
	}


	@Override
	public List<SecurityDailyData> getNAVList(Long securityid) {

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		if(sdds==null&&dailyDataCache.isBusy()){
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

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getNAVList(securityid,startDate);
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

		List<SecurityDailyData> sdds = this.getDailydatasWithCache(securityid, true);
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getNAVList(securityid,startDate,endDate);
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


	@Override
	public Date getStartDate(Long securityid) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(securityid, true);
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getStartDate(securityid);
		}
		if (sdds != null && sdds.size() > 0)
			return sdds.get(0).getDate();
		else
			return null;
	}

	@Override
	public Date getEndDate(Long securityid) {
		List<SecurityDailyData> sdds = getDailydatasWithCache(securityid, true);
		if(sdds==null&&dailyDataCache.isBusy()){
			return super.getEndDate(securityid);
		}
		if (sdds != null && sdds.size() > 0)
			return sdds.get(sdds.size()-1).getDate();
		else
			return null;
	}

	// 1 2 3 5 6 7 8 9
	// locate 4 will return 3
	public int locateEnd(List<SecurityDailyData> sdds, Date date) {
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
	public int locatePosition(List<SecurityDailyData> sdds, Date date) {
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
	public int locateStart(List<SecurityDailyData> sdds, Date date) {
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

	public LTICache getDailyDataCache() {
		return dailyDataCache;
	}


}
