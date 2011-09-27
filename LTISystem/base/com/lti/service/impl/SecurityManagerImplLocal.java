package com.lti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;



import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.cache.LTICache;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CompanyFund;
import com.lti.service.bo.RelativeStrength;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.SecurityMPTIncData;
import com.lti.service.bo.SecurityType;
import com.lti.service.bo.ShillerSP500;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VAFund;
import com.lti.type.PaginationSupport;
import com.lti.type.SortType;
import com.lti.util.DailyUpdateListener;
import com.lti.util.LTIDate;
import com.lti.util.ZipObject;

public class SecurityManagerImplLocal  implements SecurityManager, Serializable {

	private static final long serialVersionUID = 1L;
	LTICache securityDailyDataCache=new LTICache();
	LTICache securityCache=new LTICache();
	

	private SecurityManager securityManagerService;

	public void setSecurityManagerService(SecurityManager securityManagerService) {
		this.securityManagerService = securityManagerService;
	}
	
	

	public List<SecurityDailyData> getDailydatas(Long id) {
		if (id == null) {
			return null;
		}

		List<SecurityDailyData> newSdds = (List<SecurityDailyData>) securityDailyDataCache.get(id);

		if (newSdds == null) {
			try {
				byte[] bytes=securityManagerService.getZipDailydatas(id);
				newSdds = (List<SecurityDailyData>) ZipObject.ZipBytesToObject(bytes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (newSdds != null) {
				securityDailyDataCache.put(id,newSdds);
			}
		}

		if (newSdds != null) {
			return newSdds;
		}

		return null;
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
			if (LTIDate.before(date, sdds.get(0).getDate())){
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
	@Override
	public List<SecurityDailyData> getDailyData(Long id, Object[] dates) {

		if (id == null || dates == null || dates.length <= 1) {
			return null;
		}

		List<SecurityDailyData> sdds = this.getDailydatas(id);

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

	@Override
	public List<SecurityDailyData> getDailyDatas(Long id, Date startDate, Date endDate) {
		List<SecurityDailyData> sdds=getDailydatas(id);
		int start=locateStart(sdds, startDate);
		int end=locateEnd(sdds, endDate);
		if(start!=-1&&end!=-1&&end>=start){
			return sdds.subList(start, end+1);
		}
		return null;
	}
	public List<SecurityDailyData> getDailydatasByPeriod(Long securityid, Date startDate, Date endDate) {
		return getDailyDatas(securityid, startDate, endDate);
	}


	public SecurityDailyData getDailydata(Long id, Date date) {

		List<SecurityDailyData> sdds=getDailydatas(id);
		int p=locatePosition(sdds, date);
		if(p!=-1){
			return sdds.get(p);
		}
		return null;
	}



	@Override
	public SecurityDailyData getLatestDailydata(Long id, Date date) {
		List<SecurityDailyData> sdds=getDailydatas(id);
		int p=locateEnd(sdds, date);
		if(p!=-1){
			return sdds.get(p);
		}
		return null;

	}
	@Override
	public SecurityDailyData getLatestNAVDailydata(Long id, Date date) {
		List<SecurityDailyData> sdds=getDailydatas(id);
		int p=locateEnd(sdds, date);
		if(p!=-1){
			SecurityDailyData sdd= sdds.get(p);
			while(sdd.getNAV()==null||sdd.getNAV()<=0.0){
				p--;
				if(p<0)return null;
				sdd= sdds.get(p);
			}
		}
		return null;

	}


	/** *********************************************************** */
	/* ==fields== End */
	/** *********************************************************** */
	/** *********************************************************** */
	/* ==Set Method for Spring== Start */
	/** *********************************************************** */



	/** *********************************************************** */
	/* ==Set Method for Spring== End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==Basic Method== Start */
	/** *********************************************************** */
	@Override
	public Security get(Long id) {
		if (id == null) {
			return null;
		}

		Security s = (Security) securityCache.get("Security"+id);

		if (s == null) {
			s = securityManagerService.get(id);
			if (s != null) {
				securityCache.put("Security"+id,s);
				securityCache.put("Security"+s.getSymbol(),s);
			}
		}

		if (s != null) {
			return s;
		}

		return null;
	}
	@Override
	public Security get(String name) {

		if (name == null) {
			return null;
		}

		Security s = (Security) securityCache.get("Security"+name);

		if (s == null) {
			s = securityManagerService.get(name);
			if (s != null) {
				securityCache.put("Security"+s.getID(),s);
				securityCache.put("Security"+s.getSymbol(),s);
			}
		}

		if (s != null) {
			return s;
		}

		return null;

	}

	

	/** *********************************************************** */
	/* ==Basic Method== End */
	/** *********************************************************** */
	/** *********************************************************** */
	/* ==Extend Method== Start */
	/** *********************************************************** */
	@Override
	public double getHighestPrice(String name, Date startDate, Date endDate) throws SecurityException {

		Security security = this.get(name);

		if (security == null) {
			SecurityException se = new SecurityException();
			se.setSecurityName(name);
			se.setDetail(SecurityException.NULL_SECURITY);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		return this.getHighestPrice(security.getID(), startDate, endDate);
	}
	@Override
	public double getLowestPrice(String name, Date startDate, Date endDate) throws SecurityException {

		Security security = this.get(name);

		if (security == null) {
			SecurityException se = new SecurityException();
			se.setSecurityName(name);
			se.setDetail(SecurityException.NULL_SECURITY);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		return this.getLowestPrice(security.getID(), startDate, endDate);
	}

	@Override
	public double getHighestAdjPrice(String name, Date startDate, Date endDate) throws SecurityException {

		Security security = this.get(name);

		if (security == null) {
			SecurityException se = new SecurityException();
			se.setSecurityName(name);
			se.setDetail(SecurityException.NULL_SECURITY);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		return this.getHighestAdjPrice(security.getID(), startDate, endDate);
	}

	@Override
	public double getLowestAdjPrice(String name, Date startDate, Date endDate) throws SecurityException {

		Security security = this.get(name);

		if (security == null) {
			SecurityException se = new SecurityException();
			se.setSecurityName(name);
			se.setDetail(SecurityException.NULL_SECURITY);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		return this.getLowestAdjPrice(security.getID(), startDate, endDate);
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

	@SuppressWarnings("deprecation")
	public double getPrice(Long id) throws NoPriceException {

		return this.getPrice(id, new Date());
	}

	@Override
	public Date getStartDate(Long securityid) {
		List<SecurityDailyData> sdds=getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0)return sdds.get(0).getDate();
		else return null;
	}

	@Override
	public Date getEndDate(Long securityid) {
		List<SecurityDailyData> sdds=getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0)return sdds.get(sdds.size()-1).getDate();
		else return null;
	}

	@Override
	public double getPrice(Long id, Date date) throws NoPriceException {

		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getClose();
	}

	@Override
	public double getHigh(Long id, Date date) throws NoPriceException {

		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getHigh();
	}

	@Override
	public double getLow(Long id, Date date) throws NoPriceException {

		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getLow();
	}

	@Override
	public Double getSplit(Long id, Date date) throws NoPriceException {

		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getSplit();
	}

	@Override
	public double getAdjPrice(Long id, Date date) throws NoPriceException {

		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getAdjClose();
	}
	@Override
	public double getNAVPrice(Long id, Date date) throws NoPriceException {

		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {

			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;

		}

		else
			return sdd.getAdjNAV();

	}

	

	/** *********************************************************** */
	/* ==Extend Method== End */
	/** *********************************************************** */
	/** *********************************************************** */
	/* ==List Method== Start */
	/** *********************************************************** */
	@Override
	public List<Security> getSecuritiesByClass(Long classid) {

		return securityManagerService.getSecuritiesByClass(classid);
	}

	@Override
	public List<Security> getSecurities(String[] nameArray) {
		return securityManagerService.getSecurities(nameArray);
	}
	@Override
	public List<Security> getSecurities() {

		return securityManagerService.getSecurities();
	}
	@Override
	public List<Security> getSecuritiesByType(int type) {

		return securityManagerService.getSecuritiesByType(type);
	}


	@Override
	public List<Security> getCEF(long assetClassID) {

		return securityManagerService.getCEF(assetClassID);
	}
	@Override
	public List<Security> getMutualFund(long assetClassID) {
		return securityManagerService.getMutualFund(assetClassID);
	}
	@Override
	public List<Security> getSecuritiesByTypeAndClass(Long classid, int type) {

		return securityManagerService.getSecuritiesByTypeAndClass(classid,type);
	}

	@Override
	public List<Security> getMutualFunds() {

		return securityManagerService.getMutualFunds();
	}



	/** *********************************************************** */
	/* ==List Method== End */
	/** *********************************************************** */
	/** *********************************************************** */
	/* ==Daily Data== Start */
	/** *********************************************************** */

	


	/** *********************************************************** */
	/* ==Daily Data== End */
	/** *********************************************************** */
	@Override
	public List<SecurityDailyData> getDividendList(Long securityid) {

		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			List<SecurityDailyData> ds=new ArrayList<SecurityDailyData>();
			for (int i = 0; i < sdds.size(); i++) {
				if(sdds.get(i).getDividend()!=null&&sdds.get(i).getDividend()>0.0)ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;
	}
	@Override
	public List<SecurityDailyData> getCloseList(Long securityid, Date startDate) {

		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			int start=locateStart(sdds, startDate);
			if(start==-1)return null;
			return sdds.subList(start, sdds.size());
		}
		return null;
	}
	@Override
	public List<SecurityDailyData> getNAVList(Long securityid, Date startDate) {

		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			List<SecurityDailyData> ds=new ArrayList<SecurityDailyData>();
			int start=locateStart(sdds, startDate);
			if(start==-1)return null;
			for (int i = start; i < sdds.size(); i++) {
				if(sdds.get(i).getNAV()!=null&&sdds.get(i).getNAV()>0.0)ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;
	}
	@Override
	public List<SecurityDailyData> getNAVList(Long securityid, Date startDate, Date endDate) {

		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			List<SecurityDailyData> ds=new ArrayList<SecurityDailyData>();
			int start=locateStart(sdds, startDate);
			int end=locateStart(sdds, endDate);
			if(start==-1||end==-1)return null;
			for (int i = start; i <= end; i++) {
				if(sdds.get(i).getNAV()!=null&&sdds.get(i).getNAV()>0.0)ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;

	}

	@Override
	public List<SecurityDailyData> getNAVList(Long securityid) {

		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			List<SecurityDailyData> ds=new ArrayList<SecurityDailyData>();
			for (int i = 0; i < sdds.size(); i++) {
				if(sdds.get(i).getNAV()!=null&&sdds.get(i).getNAV()>0.0)ds.add(sdds.get(i));
			}
			return ds;
		}
		return null;
	}

	@Override
	public Date getDividendLastDate(Long securityid) {
		
		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			for (int i = sdds.size()-1; i >=0; i--) {
				if(sdds.get(i).getDividend()!=null&&sdds.get(i).getDividend()>0.0)return sdds.get(i).getDate();
			}
		}
		return null;
	}
	@Override
	public Date getDailyDataLastDate(Long securityid) {
		
		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			for (int i = sdds.size()-1; i >=0; i--) {
				SecurityDailyData sdd=sdds.get(i);
				if(sdd.getAdjClose()>0.0&&sdd.getSplit()!=null&&sdd.getSplit()>0.0)return sdds.get(i).getDate();
			}
		}
		return null;

	}
	@Override
	public Date getNAVLastDate(Long securityid) {
		
		List<SecurityDailyData> sdds=this.getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0){
			for (int i = sdds.size()-1; i >=0; i--) {
				SecurityDailyData sdd=sdds.get(i);
				if(sdd.getAdjClose()>0.0&&sdd.getNAV()!=null&&sdd.getNAV()>0.0)return sdds.get(i).getDate();
			}
		}
		return null;

	}

	@Override
	public List<Security> getSecuritiesByAsset(Long assetID) {

		return securityManagerService.getSecuritiesByAsset(assetID);
	}

	@Override
	public double getDividend(Long securityid, Date date) {
		
		SecurityDailyData sdd = getDailydata(securityid, date);
		if (sdd == null)
			return 0.0;
		if (sdd.getDividend() == null)
			return 0.0;
		return sdd.getDividend();
	}

	@Override
	public double getAdjPrice(Long id) throws NoPriceException {
		
		return this.getAdjPrice(id, new Date());
	}


	@Override
	public Date getValidStartDate(String[] ses) {
		if (ses == null || ses.length == 0)
			return null;

		Date validDate = com.lti.util.LTIDate.getDate(1970, 1, 1);
		try {

			validDate = com.lti.util.LTIDate.clearHMSM(validDate);
			for (int i = 0; i < ses.length; i++) {
				long id = get(ses[i]).getID();
				Date startDate = this.getStartDate(id);
				if (startDate == null)
					return null;
				else if (com.lti.util.LTIDate.after(startDate, validDate)) {
					validDate = startDate;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return validDate;
	}

	@Override
	public Date getValidEndDate(String[] ses) {
		if (ses == null || ses.length == 0)
			return null;

		Date validDate = new Date();
		try {

			validDate = com.lti.util.LTIDate.clearHMSM(validDate);
			for (int i = 0; i < ses.length; i++) {
				long id = get(ses[i]).getID();
				Date endDate = this.getEndDate(id);
				if (endDate == null)
					return null;
				else if (com.lti.util.LTIDate.before(endDate, validDate)) {
					validDate = endDate;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return validDate;
	}

	@Override
	public double getHighestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		
		double maxPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

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
	public double getLowestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		
		double minPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		List<SecurityDailyData> dailydataList = getDailyDatas(id, startDate, endDate);

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
	public SecurityDailyData getLatestDailydata(Long securityid) {
		
		List<SecurityDailyData> sdds=getDailydatas(securityid);
		if(sdds!=null&&sdds.size()>0)return sdds.get(sdds.size()-1);
		else return null;
	}

	@Override
	public byte[] getZipDailydatas(Long id) {
		
		return null;
	}

	@Override
	public Long add(Security security) throws SecurityException {
		
		
		return null;
	}

	@Override
	public void clearDailyData(Long securityid) {
		
		
	}


	@Override
	public void delete(Long id) {
		
		
	}

	@Override
	public void delete(String string) {
		
		
	}

	@Override
	public void delteDailyData(Long id) {
		
		
	}

	@Override
	public List<SecurityDailyData> findByCriteria(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public List findByHQL(String string) {
		
		return null;
	}

	@Override
	public PaginationSupport findDailyDatas(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public PaginationSupport findSecurities(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		
		return null;
	}



	@Override
	public Security getBySymbol(String symbol) {
		
		return securityManagerService.getBySymbol(symbol);
	}

	@Override
	public SecurityDailyData getDailydata(Long securityid, Date date, boolean flag) {
		
		return securityManagerService.getDailydata(securityid, date, flag);
	}

	@Override
	public SecurityDailyData getDailydataWithDividend(Long securityid, Date date) {
		
		return null;
	}

	@Override
	public List<SecurityDailyData> getDailydatas(DetachedCriteria detachedCriteria) {
		
		return null;
	}

	@Override
	public List<SecurityDailyData> getDailydatas(Long securityid, boolean flag) {
		
		return null;
	}

	@Override
	public PaginationSupport getDailydatas(Long securityid, int pageSize, int startIndex) {
		
		return null;
	}


	@Override
	public PaginationSupport getMutualFunds(int pageSize, int startIndex) {
		
		return null;
	}

	//there may be some problems here.
	@Override
	public void getOneSecurityMPT(long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, boolean update) {
		
		securityManagerService.getOneSecurityMPT(id, sdds, benchs, update);
	}

	@Override
	public SecurityMPT getOneYearMPT(SecurityMPT smpt, List<Double> A_Returns, List<Double> B_Returns, List<Double> A_Amounts, Date startDate, Date currentDate, int interval, Security se) {
		
		return securityManagerService.getOneYearMPT(smpt, A_Returns, B_Returns, A_Amounts, startDate, currentDate, interval, se);
	}

	@Override
	public List<Security> getSecurities(DetachedCriteria detachedCriteria) {
		
		return null;
	}

	@Override
	public PaginationSupport getSecurities(int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public PaginationSupport getSecuritiesByClass(Long classid, int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public List<Security> getSecuritiesByName(String key) {
		
		 return securityManagerService.getSecuritiesByName(key);
	}

	@Override
	public PaginationSupport getSecuritiesByName(String key, int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public PaginationSupport getSecuritiesByType(int type, int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public PaginationSupport getSecuritiesByTypeAndClass(Long classid, int type, int pageSize, int startIndex) {
		
		return null;
	}

	@Override
	public List<SecurityMPT> getSecurityByMPT(DetachedCriteria detachedCriteria) {
		
		 return securityManagerService.getSecurityByMPT(detachedCriteria);
	}

	@Override
	public SecurityDailyData getSecurityDailyData(long securitydailydataid) {
		
		 return securityManagerService.getSecurityDailyData(securitydailydataid);
	}

	@Override
	public SecurityMPT getSecurityMPT(long securityID, long year) {
		
		 return securityManagerService.getSecurityMPT(securityID,year);
	}


	@Override
	public void removeAll(List<SecurityDailyData> sdds) {
		
		
	}

	@Override
	public void removeDailyData(Long securityid, Date date) {
		
		
	}

	@Override
	public void removeSecurityDailyData(long id) {
		
		
	}

	@Override
	public void saveAll(List<SecurityDailyData> sdds1) {
		
		
	}

	@Override
	public Long saveDailyData(SecurityDailyData securityDailyData) {
		
		return null;
	}

	@Override
	public void saveOrUpdateAll(List<SecurityDailyData> sdds) {
		
		
	}

	@Override
	public void saveOrUpdateAllSecurityMPT(List<SecurityMPT> smpts) {
		
		
	}

	@Override
	public void update(Security security) {
		
		
	}

	@Override
	public void updateDailyData(SecurityDailyData securityDailyData) {
		
		
	}

	@Override
	public boolean canGetPrice(String securityName, Date date) {
		
		return securityManagerService.canGetPrice(securityName, date);
	}

	@Override
	public boolean canGetPrice(Long securityID, Date date) {
		
		return securityManagerService.canGetPrice(securityID, date);
	}

	@Override
	public Date getEndDate(String securityName) {
		
		return securityManagerService.getEndDate(securityName);
	}

	@Override
	public Date getStartDate(String securityName) {
		
		return securityManagerService.getStartDate(securityName);
	}


	@Override
	public SecurityType getSecurityTypeByID(int id) {
		
		return securityManagerService.getSecurityTypeByID(id);
	}

	@Override
	public List<SecurityType> getSecurityTypes() {
		
		return securityManagerService.getSecurityTypes();
	}

	@Override
	public List<SecurityMPT> getSecurityMPTS(long securityID) {
		
		return securityManagerService.getSecurityMPTS(securityID);
	}

	@Override
	public double getAnnualPE(String symbol, Date endDate) throws NoPriceException {
		
		return securityManagerService.getAnnualPE(symbol, endDate);
	}

	@Override
	public double getAnnualSecurityYeild(long securityID, Date startDate, Date endDate) throws Exception {
		
		return securityManagerService.getAnnualSecurityYeild(securityID, startDate, endDate);
	}

	@Override
	public void saveOrUpdate(Security arg0) {
		
	}

	@Override
	public void getAllSecurityMPT(Date arg0, boolean arg1) {
		
		
	}

	@Override
	public void addListener(DailyUpdateListener dailyUpdateListener) {
		
	}

	@Override
	public List<SecurityMPT> calculateOneSecurityMPT(long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, boolean update) {
		return null;
	}

	@Override
	public void deleteSecurityCascade(Long securityID) {
		
	}

	@Override
	public void deleteSecurityMPT(Long id) {
		
	}

	@Override
	public void deleteSecurityRanking(Long securityID) {
		
	}

	@Override
	public List findBySQL(String sql) throws Exception {
		return securityManagerService.findBySQL(sql);
	}

	@Override
	public double getAdjNAVPrice(Long id, Date date) throws SecurityException, NoPriceException {
		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getAdjNAV();
	}

	@Override
	public void getAllStockRS(Date logDate, boolean update) {
		
	}

	@Override
	public List<String> getAllStocks() {
		return securityManagerService.getAllStocks();
	}

	@Override
	public double getAnnualGSPCYeild(Date startDate, Date endDate) throws Exception {
		return securityManagerService.getAnnualGSPCYeild( startDate, endDate);
	}

	@Override
	public PaginationSupport getDailyDatas(Long securityid, Date startDate, Date endDate, int pageSize, int startIndex) {
		return null;
	}

	@Override
	public List<Security> getFundsByClass(Long singleAssetClassID) {
		return securityManagerService.getFundsByClass(singleAssetClassID);
	}

	@Override
	public void getHistoricalRS(Date date, Date logDate, boolean update) {
		
	}

	@Override
	public String getIndustry(String symbol) {
		return securityManagerService.getIndustry(symbol);
	}

	@Override
	public List<String> getIndustryBySector(String[] sector) {
		return securityManagerService.getIndustryBySector(sector);
	}

	@Override
	public List<String> getIndustryBySector(String sector) {
		return securityManagerService.getIndustryBySector(sector);
	}

	@Override
	public List<SecurityMPT> getOneSecurityINCMPT(Long securityID) {
		return securityManagerService.getOneSecurityINCMPT(securityID);
	}

	@Override
	public void getOneSecurityMPT(Long securityID) {
		securityManagerService.getOneSecurityMPT(securityID);
	}

	@Override
	public double getOpenPrice(Long id, Date date) throws NoPriceException {
		SecurityDailyData sdd = getLatestDailydata(id, date);

		Date endDate = this.getEndDate(id);

		if (sdd == null || com.lti.util.LTIDate.after(date, endDate)) {
			NoPriceException se = new NoPriceException();
			se.setSecurityID(id);
			se.setDate(date);
			Security s = get(id);
			if (s != null) {
				se.setSecurityName(s.getSymbol());
			}
			throw se;
		}

		else
			return sdd.getOpen();
	}

	@Override
	public PaginationSupport getPureSecurities(int pageSize, int startIndex) {
		return null;
	}

	@Override
	public List<Strategy> getQuotedStrategies(String symbol, int size) {
		return null;
	}

	@Override
	public List<Strategy> getQuotedStrategies(String symbol, Long userID, int size) {
		return null;
	}

	@Override
	public List<Strategy> getQuotedStrategiesByOrder(String symbol, int size, int order, int year) {
		return null;
	}

	@Override
	public List<Strategy> getQuotedStrategiesByOrder(String symbol, Long userid, int size, int order, int year) {
		return null;
	}

	@Override
	public RelativeStrength getRS(Long id) {
		return securityManagerService.getRS(id);
	}

	@Override
	public RelativeStrength getRS(String symbol, Date date) {
		return securityManagerService.getRS(symbol, date);
	}

	@Override
	public Double getRSGrade(String symbol, int Type, Date date) {
		return securityManagerService.getRSGrade(symbol, Type, date);
	}

	@Override
	public List<SecurityMPTIncData> getSecurityMPTIncData(Long securityID) {
		return securityManagerService.getSecurityMPTIncData(securityID);
	}

	@Override
	public Double getShillerSP500CAPE10(Date date) throws NoPriceException {
		return securityManagerService.getShillerSP500CAPE10(date);
	}

	@Override
	public ShillerSP500 getShillerSP500Data(Date date) throws NoPriceException {
		return securityManagerService.getShillerSP500Data(date);
	}

	@Override
	public List<String> getStockByIndustry(String industry) {
		return securityManagerService.getStockByIndustry(industry);
	}

	@Override
	public List<String> getStockByIndustry(String[] industries) {
		return securityManagerService.getStockByIndustry(industries);
	}

	@Override
	public List<String> getStockBySector(String sector) {
		return securityManagerService.getStockBySector(sector);
	}

	@Override
	public List<String> getStockBySector(String[] sectors) {
		return securityManagerService.getStockBySector(sectors);
	}

	@Override
	public void getStockRS(Security se, Date logDate, boolean update) {
		
	}

	@Override
	public void initialMPTIncrementalData(Security security, List<SecurityDailyData> securityList, List<SecurityDailyData> benchList, List<SecurityDailyData> cashList) {
		
	}

	@Override
	public void removeAllShillerSP500() throws Exception {
		
	}

	@Override
	public void removeListener(DailyUpdateListener dailyUpdateListener) {
	}

	@Override
	public void removeRS(Long id) {
	}

	@Override
	public void saveAllShillerSP500(List<ShillerSP500> list) {
	}

	@Override
	public void saveOrUpdateAllSecurity(List<Security> securityList) {
	}

	@Override
	public void saveOrUpdateAllSecurityMPTIncDate(List<SecurityMPTIncData> mpts) {
	}

	@Override
	public void saveRS(RelativeStrength rs) {
	}

	@Override
	public void setSecurityMPTIncrementalData() {
	}

	@Override
	public void updateAllSecurityMPT(Date logDate) {
	}

	@Override
	public void updateRS(RelativeStrength rs) {
	}

	@Override
	public void updateSecurityEndDate(List<SecurityDailyData> sdds) {
		securityManagerService.updateSecurityEndDate(sdds);
	}



	@Override
	public void deleteSecurityDataCascade(Long securityID) {
		securityManagerService.deleteSecurityDataCascade(securityID);
	}



	@Override
	public List<SecurityDailyData> getDividendList(Long securityID, Date date) {
		return securityManagerService.getDividendList(securityID, date);
	}



	@Override
	public void updateDividendLastDate(Security security) throws Exception {
		securityManagerService.updateDividendLastDate(security);
	}



	@Override
	public void updateEndDateAndPriceLastDate(Security security) throws Exception {
		securityManagerService.updateEndDateAndPriceLastDate(security);
	}



	@Override
	public void updateMPTLastDate(Security security) throws Exception {
		securityManagerService.updateMPTLastDate(security);
	}



	@Override
	public void updateNAVLastDate(Security security) throws Exception {
		securityManagerService.updateNAVLastDate(security);
	}



	@Override
	public void updateNewDividendDate(Security security) throws Exception {
		securityManagerService.updateNewDividendDate(security);
	}



	@Override
	public void updateStartDate(Security security) throws Exception {
		securityManagerService.updateStartDate(security);
	}



	
	@Override
	public List<SecurityMPT> getSecurityMPTBySecurityIDs(List<Long> securityIDs, Long year) {
		return securityManagerService.getSecurityMPTBySecurityIDs(securityIDs, year);
	}



	@Override
	public List<SecurityMPT> getSecurityMPTByYears(long securityID, Long[] years) {
		return securityManagerService.getSecurityMPTByYears(securityID, years);
	}



	@Override
	public int getDailyDataCount(Long securityID) {
		return securityManagerService.getDailyDataCount(securityID);
	}



	@Override
	public SecurityDailyData getOldestDailydata(Long securityid) {
		return securityManagerService.getOldestDailydata(securityid);
	}



	@Override
	public List<Security> getFundsByClassIDAndEndDate(Long classID, Date endDate) {
		return securityManagerService.getFundsByClassIDAndEndDate(classID, endDate);
	}



	@Override
	public List<Security> getSecuritiesByAssetIDAndEndDate(Long assetID, Date endDate) {
		return securityManagerService.getSecuritiesByAssetIDAndEndDate(assetID, endDate);
	}



	@Override
	public Date getNAVStartDate(Long securityID) {
		return securityManagerService.getNAVStartDate(securityID);
	}



	@Override
	public void updateNAVStartDate(Security security) throws Exception {
		securityManagerService.updateNAVStartDate(security);
	}



	@Override
	public List<Security> getSecuritiesByNameConsiderLength(String key, int considerLength) {
		return securityManagerService.getSecuritiesByNameConsiderLength(key, considerLength);
	}



	@Override
	public void saveAllCompanyFund(List<CompanyFund> companyFundList) {
		securityManagerService.saveAllCompanyFund(companyFundList);
	}



	@Override
	public CompanyFund getCompanyFundByTicker(String ticker) {
		return securityManagerService.getCompanyFundByTicker(ticker);
	}



	@Override
	public List<CompanyFund> getCompanyFundListByCompany(String company) {
		return securityManagerService.getCompanyFundListByCompany(company);
	}



	@Override
	public List<CompanyFund> getCompanyFundListByCompanyAndCategory(String company, String category) {
		return securityManagerService.getCompanyFundListByCompanyAndCategory(company, category);
	}



	@Override
	public void saveOrUpdateAllCompanyFund(List<CompanyFund> companyFundList) {
		securityManagerService.saveOrUpdateAllCompanyFund(companyFundList);
	}



	@Override
	public boolean hasFundsForCompany(String company) {
		return securityManagerService.hasFundsForCompany(company);
	}



	@Override
	public void saveCompanyFund(CompanyFund companyFund) {
		securityManagerService.saveCompanyFund(companyFund);
	}



	@Override
	public List<String> getAssetNameListByCompanyName(String companyName) {
		return securityManagerService.getAssetNameListByCompanyName(companyName);
	}



	@Override
	public List<CompanyFund> getCompanyFundListByCompanyAndCategorys(String company, String category1, String category2, String category3) {
		return securityManagerService.getCompanyFundListByCompanyAndCategorys(company, category1, category2, category3);
	}



	@Override
	public List<String> getCompanyNameList() {
		return securityManagerService.getCompanyNameList();
	}



	@Override
	public void addAllVAFunds(List<VAFund> vaFundList) {
		securityManagerService.addAllVAFunds(vaFundList);
	}



	@Override
	public void addVAFund(VAFund vaFund) {
		securityManagerService.addVAFund(vaFund);
	}



	@Override
	public VAFund getVAFundByBarronName(String barronName) {
		return securityManagerService.getVAFundByBarronName(barronName);
	}



	@Override
	public VAFund getVAFundByTicker(String ticker) {
		return securityManagerService.getVAFundByTicker(ticker);
	}



	@Override
	public List<CompanyFund> getCompanyFundList() {
		return securityManagerService.getCompanyFundList();
	}



	@Override
	public List<VAFund> getVAFundList() {
		return securityManagerService.getVAFundList();
	}



	@Override
	public void saveOrUpdateVAFunds(List<VAFund> vaFundList) {
		securityManagerService.saveOrUpdateVAFunds(vaFundList);
	}



	@Override
	public List<Security> getSecuritiesBeforeEndDate(Date endDate) {
		return securityManagerService.getSecuritiesBeforeEndDate(endDate);
	}



	@Override
	public List<Security> getSecuritiesByEndDate(Date endDate) {
		return securityManagerService.getSecuritiesByEndDate(endDate);
	}



	@Override
	public void updateSecurityNAVLastDate(List<SecurityDailyData> sdds) {
		securityManagerService.updateSecurityNAVLastDate(sdds);
	}



	@Override
	public void calculateSecurityMPTAfterYear(Long id,
			List<SecurityDailyData> sdds, List<SecurityDailyData> benchs,
			List<SecurityDailyData> riskFreeList) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deleteSecurityMPTAfterYear(Long securityID, int year) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public int getProcess()
	{
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getSumProcess()
	{
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public boolean updateAr(String symbol)
	{
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void updateAllAr()
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean updateMpt(String symbol)
	{
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void updateAllMpt()
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public List<SecurityDailyData> searchSecurityDailyData(String symbol, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Security> getSecuritiesLessByType(int type) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setIsRun(boolean b) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List<Object[]> getQuality(String symbol) {
		// TODO Auto-generated method stub
		return null;
	}




}
