package com.lti.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.service.BaseFormulaManager;
import com.lti.service.IndicatorManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CompanyFund;
import com.lti.service.bo.IndicatorDailyData;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.RelativeStrength;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.SecurityMPTIncData;
import com.lti.service.bo.SecurityType;
import com.lti.service.bo.ShillerSP500;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VAFund;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.LTINumber;
import com.lti.type.PaginationSupport;
import com.lti.type.TimePara;
import com.lti.type.TimeUnit;
import com.lti.util.BaseFormulaUtil;
import com.lti.util.DailyUpdateListener;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.lti.util.ZipObject;

@SuppressWarnings("unchecked")
public class SecurityManagerImpl extends DAOManagerImpl implements SecurityManager, Serializable {
	private static final long serialVersionUID = 1L;

	private List<DailyUpdateListener> dailyUpdateListenerList = new ArrayList<DailyUpdateListener>();

	public SecurityManagerImpl() {
	}

	@Override
	public Long add(Security security) throws SecurityException {

		if (this.get(security.getSymbol()) != null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.SECURITY_ALREADY_EXISTS);
			throw se;
		}

		getHibernateTemplate().save(security);

		return security.getID();
	}

	@Override
	public void clearDailyData(Long securityid) {
		// deleteByHQL("from SecurityDailyData where SecurityID=" + securityid);
		try {
			executeSQL("delete from ltisystem_securitydailydata where SecurityID=" + securityid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Long id) {
		Object obj = getHibernateTemplate().get(Security.class, id);
		getHibernateTemplate().delete(obj);
		this.clearDailyData(id);
	}

	@Override
	public void delete(String string) {
		Security s = get(string);
		if (s != null)
			delete(s.getID());
	}

	@Override
	public void deleteSecurityCascade(Long securityID) {
		this.deleteSecurityMPT(securityID);
		this.deleteSecurityRanking(securityID);
		this.delete(securityID);

	}

	@Override
	public void deleteSecurityDataCascade(Long securityID) {
		this.deleteSecurityMPT(securityID);
		this.deleteSecurityRanking(securityID);
		this.clearDailyData(securityID);
	}

	@Override
	public void deleteSecurityRanking(Long securityID) {
		try {
			executeSQL("delete from ltisystem_securityranking where SecurityID=" + securityID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteSecurityMPT(Long securityid) {
		// deleteByHQL("from SecurityDailyData where SecurityID=" + securityid);
		try {
			executeSQL("delete from ltisystem_securitympt where SecurityID=" + securityid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteSecurityMPTAfterYear(Long securityID, int year) {
		try {
			executeSQL("delete from ltisystem_securitympt where SecurityID=" + securityID + " and year >=" + year);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delteDailyData(Long id) {
		Object obj = getHibernateTemplate().get(SecurityDailyData.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public PaginationSupport findDailyDatas(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	@Override
	public PaginationSupport findSecurities(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	@Override
	public Security get(Long securityid) {
		// long t1=System.currentTimeMillis();
		Session s = null;
		try {
			if (securityid == null)
				return null;
			s = getHibernateTemplate().getSessionFactory().openSession();
			Security se = (Security) s.get(Security.class, securityid);
			return se;
		} catch (RuntimeException e) {
			System.out.println(StringUtil.getStackTraceString(e));
			throw e;
		} finally {
			if (s != null && s.isOpen())
				s.close();
			// long t2=System.currentTimeMillis();
			// System.out.println("read: "+(t2-t1));
		}
	}

	@Override
	public Security get(String name) {
		if (name == null || name.equals(""))
			return null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.or(Restrictions.eq("Name", name), Restrictions.eq("Symbol", name)));
		List<com.lti.service.bo.Security> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist.size() >= 1) {
			return bolist.get(0);
		} else {
			return null;
		}
	}

	@Override
	public double getAdjPrice(Long id) throws NoPriceException {
		return this.getAdjPrice(id, new Date());
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
		} else {
			return sdd.getAdjClose();
		}
	}

	@Override
	public List<Security> getCEF(long assetClassID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", com.lti.system.Configuration.SECURITY_TYPE_CLOSED_END_FUND));
		detachedCriteria.add(Restrictions.eq("ClassID", assetClassID));
		return this.getSecurities(detachedCriteria);
	}

	@Override
	public List<SecurityDailyData> getCloseList(Long securityid, Date startDate) {
		return this.getDailydatasFromDB(securityid, true, startDate, null);
	}

	@Override
	public SecurityDailyData getDailydata(Long id, Date date, boolean flag) {
		List<SecurityDailyData> sdds = this.getDailydatasFromDB(id, flag, date, date);
		if (sdds != null && sdds.size() == 1) {
			return sdds.get(0);
		}
		return null;
	}

	@Override
	public List<SecurityDailyData> getDailyData(Long id, Object[] dates) {
		if (id == null || dates == null || dates.length <= 1) {
			return null;
		}

		List<SecurityDailyData> newSdds = new ArrayList<SecurityDailyData>();
		int pointer = 0;
		for (int i = 0; i < dates.length; i++) {
			SecurityDailyData sdd = getDailydata(id, (Date) dates[i], true);
			if (sdd != null)
				newSdds.add(sdd);
		}
		return newSdds;
	}

	@Override
	public List<SecurityDailyData> getDailydatas(DetachedCriteria detachedCriteria) {
		return findByCriteria(detachedCriteria);
	}

	public List<SecurityDailyData> getDailydatas(Long securityID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.addOrder(Order.asc("Date"));
		return getDailydatas(detachedCriteria);
	}

	protected List<SecurityDailyData> getDailydatasFromDB(Long securityid, boolean flag, Date startDate, Date endDate) {
		List<SecurityDailyData> newSdds = null;
		try {
			Security s = get(securityid);
			if (s.getSymbol().startsWith("P_")) {
				newSdds = new ArrayList<SecurityDailyData>();
				Long portfolioid = null;
				try {
					portfolioid = Long.parseLong(s.getSymbol().substring(2));
				} catch (Exception e2) {
				}
				if (portfolioid != null) {
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(com.lti.service.bo.PortfolioDailyData.class);
					detachedCriteria.addOrder(Order.asc("Date"));
					detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
					if (startDate != null && endDate != null && LTIDate.equals(startDate, endDate)) {
						detachedCriteria.add(Restrictions.eq("Date", startDate));
					} else {
						if (startDate != null) {
							detachedCriteria.add(Restrictions.ge("Date", startDate));
						}
						if (endDate != null) {
							detachedCriteria.add(Restrictions.le("Date", endDate));
						}
					}

					List<com.lti.service.bo.PortfolioDailyData> pdds = super.findByCriteria(detachedCriteria);
					if (pdds != null && pdds.size() != 0) {
						for (int i = 0; i < pdds.size(); i++) {
							SecurityDailyData sdd = new SecurityDailyData();
							sdd.setSecurityID(s.getID());
							double price = pdds.get(i).getAmount();
							sdd.setClose(price);
							sdd.setAdjClose(price);
							sdd.setOpen(price);
							sdd.setLow(price);
							sdd.setNAV(price);
							sdd.setAdjNAV(price);
							sdd.setDate(pdds.get(i).getDate());
							newSdds.add(sdd);
						}
					}
				}

			} else {
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
				detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
				if (flag) {
					detachedCriteria.add(Restrictions.isNotNull("Close"));
					detachedCriteria.add(Restrictions.isNotNull("AdjClose"));
				}
				if (startDate != null && endDate != null && LTIDate.equals(startDate, endDate)) {
					detachedCriteria.add(Restrictions.eq("Date", startDate));
				} else {
					if (startDate != null) {
						detachedCriteria.add(Restrictions.ge("Date", startDate));
					}
					if (endDate != null) {
						detachedCriteria.add(Restrictions.le("Date", endDate));
					}
				}
				detachedCriteria.addOrder(Order.asc("Date"));
				newSdds = super.findByCriteria(detachedCriteria);
			}

		} catch (Exception e1) {
			throw new RuntimeException("Cannot get records from database.", e1);
		}
		return newSdds;
	}

	@Override
	public List<SecurityDailyData> getDailydatas(Long id, boolean flag) {
		return this.getDailydatasFromDB(id, flag, null, null);
	}

	@Override
	public int getDailyDataCount(Long securityID) {
		String sql = "select count(*) from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.securityid=" + securityID + " and s.close is not null";
		BigInteger size = BigInteger.ZERO;
		try {
			List objects = super.findBySQL(sql);
			Object obj = (Object) objects.get(0);
			size = (BigInteger) obj;
			return size.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public PaginationSupport getDailydatas(Long id, int pageSize, int startIndex) {
		Security s = get(id);
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
				detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
				detachedCriteria.addOrder(Order.asc("Date"));
				PaginationSupport ps = super.findPageByCriteria(detachedCriteria, pageSize, startIndex);
				List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
				List<com.lti.service.bo.PortfolioDailyData> pdds = (List<PortfolioDailyData>) ps.getItems();
				if (pdds != null && pdds.size() != 0) {
					for (int i = 0; i < pdds.size(); i++) {
						SecurityDailyData sdd = new SecurityDailyData();
						sdd.setSecurityID(s.getID());
						double price = pdds.get(i).getAmount();
						sdd.setClose(price);
						sdd.setAdjClose(price);
						sdd.setOpen(price);
						sdd.setLow(price);
						sdd.setNAV(price);
						sdd.setAdjNAV(price);
						sdd.setDate(pdds.get(i).getDate());
						sdds.add(sdd);
					}
				}
				ps.setItems(sdds);

				return ps;
			}
		} else {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", id));
			detachedCriteria.addOrder(Order.asc("Date"));
			return super.findPageByCriteria(detachedCriteria, pageSize, startIndex);
		}
		return null;

	}

	// @Override
	// public List<SecurityDailyData> getDailyDatas(Long id, Interval interval)
	// {
	// return this.getDailyDatas(id, interval.getStartDate(),
	// interval.getEndDate());
	// }

	public PaginationSupport getDailyDatas(Long id, Date startDate, Date endDate, int pageSize, int startIndex) {
		return this.getDailyDatas(id, new Interval(startDate, endDate), pageSize, startIndex);
	}

	public PaginationSupport getDailyDatas(Long id, Interval interval, int pageSize, int startIndex) {
		Security s = get(id);
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
				detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
				detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));
				detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
				PaginationSupport ps = super.findPageByCriteria(detachedCriteria, pageSize, startIndex);
				List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
				List<com.lti.service.bo.PortfolioDailyData> pdds = (List<PortfolioDailyData>) ps.getItems();
				if (pdds != null && pdds.size() != 0) {
					for (int i = 0; i < pdds.size(); i++) {
						SecurityDailyData sdd = new SecurityDailyData();
						sdd.setSecurityID(s.getID());
						double price = pdds.get(i).getAmount();
						sdd.setClose(price);
						sdd.setAdjClose(price);
						sdd.setOpen(price);
						sdd.setLow(price);
						sdd.setNAV(price);
						sdd.setAdjNAV(price);
						sdd.setDate(pdds.get(i).getDate());
						sdds.add(sdd);
					}
				}
				ps.setItems(sdds);

				return ps;
			}
		} else {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", id));
			detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));
			detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
			return super.findPageByCriteria(detachedCriteria, pageSize, startIndex);
		}
		return null;

	}

	@Override
	public List<SecurityDailyData> getDailyDatas(Long id, Date startDate, Date endDate) {
		return this.getDailydatasFromDB(id, true, startDate, endDate);
	}

	@Override
	public SecurityDailyData getDailydataWithDividend(Long securityid, Date date) {
		return getDailydata(securityid, date, false);
	}

	@Override
	public double getDividend(Long securityid, Date date) {
		SecurityDailyData sdd = getDailydata(securityid, date, false);
		if (sdd == null)
			return 0.0;
		if (sdd.getDividend() == null)
			return 0.0;
		return sdd.getDividend();
	}

	@Override
	public Date getNAVLastDate(Long securityid) {
		try {
			List objects = super.findBySQL("select s.date from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.close is not null and s.adjclose is not null and s.nav is not null and s.securityid=" + securityid + " order by s.date desc limit 0,1");
			Timestamp t = null;
			try {
				t = (Timestamp) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				t = (Timestamp) objects.get(0);
			}
			return t;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;

	}

	@Override
	public Date getDailyDataLastDate(Long securityid) {
		try {
			List objects = super.findBySQL("select s.date from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.close is not null and s.adjclose is not null and s.split is not null and s.securityid=" + securityid + " order by s.date desc limit 0,1");
			Timestamp t = null;
			try {
				t = (Timestamp) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				t = (Timestamp) objects.get(0);
			}
			return t;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Date getDividendLastDate(Long securityid) {
		try {
			List objects = super.findBySQL("select s.date from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where dividend is not null and s.dividend>0.0 and s.securityid=" + securityid + " order by s.date desc limit 0,1");
			Timestamp t = null;
			try {
				t = (Timestamp) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				t = (Timestamp) objects.get(0);
			}
			return t;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SecurityDailyData> getDividendList(Long securityid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
		detachedCriteria.add(Restrictions.gt("Dividend", 0.0));
		detachedCriteria.addOrder(Order.asc("Date"));
		return super.findByCriteria(detachedCriteria);
	}

	@Override
	public List<SecurityDailyData> getDividendList(Long securityID, Date dDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.add(Restrictions.gt("Dividend", 0.0));
		detachedCriteria.add(Restrictions.ge("Date", dDate));
		detachedCriteria.addOrder(Order.asc("Date"));
		return super.findByCriteria(detachedCriteria);
	}

	@Override
	public Date getEndDate(Long securityid) {
		String sql = "";
		Security s = get(securityid);
		if (s.getEndDate() != null) {
			return s.getEndDate();
		}
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				sql = "select s.date from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " s where s.portfolioid=" + portfolioid + " order by s.date desc limit 0,1";
			}
		} else {
			sql = "select s.date from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.close is not null and s.adjclose is not null  and s.securityid=" + securityid + " order by s.date desc limit 0,1";
		}

		try {
			List objects = super.findBySQL(sql);
			Timestamp t = null;
			try {
				t = (Timestamp) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				t = (Timestamp) objects.get(0);
			}
			s.setEndDate(t);
			update(s);
			return t;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
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
		} else {
			return sdd.getHigh();
		}
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

		maxPrice = Double.MIN_VALUE;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List objects = super.findBySQL("select s.adjclose from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.adjclose is not null  and s.securityid=" + id + " and s.date >='" + df.format(startDate) + "' and s.date <='" + df.format(endDate) + "' order by s.adjclose desc limit 0,1");
			try {
				maxPrice = (Double) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				maxPrice = (Double) objects.get(0);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		if (maxPrice == Double.MIN_VALUE) {
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
	public double getHighestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		double maxPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_HIGHEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		maxPrice = Double.MIN_VALUE;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List objects = super.findBySQL("select s.adjnav from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.adjnav is not null  and s.securityid=" + id + " and s.date >='" + df.format(startDate) + "' and s.date <='" + df.format(endDate) + "' order by s.adjnav desc limit 0,1");
			try {
				maxPrice = (Double) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				maxPrice = (Double) objects.get(0);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		if (maxPrice == Double.MIN_VALUE) {
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

		maxPrice = Double.MIN_VALUE;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List objects = super.findBySQL("select s.close from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.close is not null  and s.securityid=" + id + " and s.date >='" + df.format(startDate) + "' and s.date <='" + df.format(endDate) + "' order by s.close desc limit 0,1");
			try {
				maxPrice = (Double) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				maxPrice = (Double) objects.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (maxPrice == Double.MIN_VALUE) {
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
	public SecurityDailyData getLatestDailydata(Long securityid) {
		Security s = get(securityid);
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
				detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
				detachedCriteria.addOrder(Order.desc("Date"));
				List<com.lti.service.bo.PortfolioDailyData> pdds = super.findByCriteria(detachedCriteria, 1, 0);
				if (pdds != null && pdds.size() == 1) {
					SecurityDailyData sdd = new SecurityDailyData();
					sdd.setSecurityID(s.getID());
					double price = pdds.get(0).getAmount();
					sdd.setClose(price);
					sdd.setAdjClose(price);
					sdd.setOpen(price);
					sdd.setLow(price);
					sdd.setNAV(price);
					sdd.setAdjNAV(price);
					sdd.setDate(pdds.get(0).getDate());
					return sdd;
				}
			}
		} else {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
			detachedCriteria.add(Restrictions.isNotNull("Close"));
			detachedCriteria.add(Restrictions.isNotNull("AdjClose"));
			detachedCriteria.addOrder(Order.desc("Date"));
			List<SecurityDailyData> newSdds = super.findByCriteria(detachedCriteria, 1, 0);
			if (newSdds != null && newSdds.size() == 1)
				return newSdds.get(0);
		}
		return null;

	}

	@Override
	public SecurityDailyData getOldestDailydata(Long securityid) {
		Security s = get(securityid);
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
				detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
				detachedCriteria.addOrder(Order.asc("Date"));
				List<com.lti.service.bo.PortfolioDailyData> pdds = super.findByCriteria(detachedCriteria, 1, 0);
				if (pdds != null && pdds.size() == 1) {
					SecurityDailyData sdd = new SecurityDailyData();
					sdd.setSecurityID(s.getID());
					double price = pdds.get(0).getAmount();
					sdd.setClose(price);
					sdd.setAdjClose(price);
					sdd.setOpen(price);
					sdd.setLow(price);
					sdd.setNAV(price);
					sdd.setAdjNAV(price);
					sdd.setDate(pdds.get(0).getDate());
					return sdd;
				}
			}
		} else {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
			detachedCriteria.add(Restrictions.isNotNull("Close"));
			detachedCriteria.add(Restrictions.isNotNull("AdjClose"));
			detachedCriteria.addOrder(Order.asc("Date"));
			List<SecurityDailyData> newSdds = super.findByCriteria(detachedCriteria, 1, 0);
			if (newSdds != null && newSdds.size() == 1)
				return newSdds.get(0);
		}
		return null;
	}

	@Override
	public SecurityDailyData getLatestDailydata(Long securityid, Date date) {
		if (securityid == null)
			return null;
		Security s = get(securityid);
		if (s == null)
			return null;
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
				detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
				detachedCriteria.add(Restrictions.le("Date", date));
				detachedCriteria.addOrder(Order.desc("Date"));
				List<com.lti.service.bo.PortfolioDailyData> pdds = super.findByCriteria(detachedCriteria, 1, 0);
				if (pdds != null && pdds.size() == 1) {
					SecurityDailyData sdd = new SecurityDailyData();
					sdd.setSecurityID(s.getID());
					double price = pdds.get(0).getAmount();
					sdd.setClose(price);
					sdd.setAdjClose(price);
					sdd.setOpen(price);
					sdd.setLow(price);
					sdd.setNAV(price);
					sdd.setAdjNAV(price);
					sdd.setDate(pdds.get(0).getDate());
					return sdd;
				}
			}
		} else {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
			detachedCriteria.add(Restrictions.isNotNull("Close")); // 收盘价
			detachedCriteria.add(Restrictions.isNotNull("AdjClose"));// 调整后的收盘价
			detachedCriteria.add(Restrictions.le("Date", date));
			detachedCriteria.addOrder(Order.desc("Date"));
			List<SecurityDailyData> newSdds = super.findByCriteria(detachedCriteria, 1, 0);
			if (newSdds != null && newSdds.size() == 1)
				return newSdds.get(0);
		}
		return null;
	}

	@Override
	public SecurityDailyData getLatestNAVDailydata(Long id, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", id));
		detachedCriteria.add(Restrictions.isNotNull("Close"));
		detachedCriteria.add(Restrictions.isNotNull("AdjClose"));
		detachedCriteria.add(Restrictions.le("Date", date));
		detachedCriteria.add(Restrictions.gt("NAV", 0.0));
		detachedCriteria.addOrder(Order.desc("Date"));
		List<SecurityDailyData> newSdds = super.findByCriteria(detachedCriteria, 1, 0);
		if (newSdds != null && newSdds.size() == 1)
			return newSdds.get(0);
		return null;

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
	public double getLowestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException {

		double minPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		minPrice = Double.MAX_VALUE;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List objects = super.findBySQL("select s.adjclose from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.adjclose is not null  and s.securityid=" + id + " and s.date >='" + df.format(startDate) + "' and s.date <='" + df.format(endDate) + "' order by s.adjclose asc limit 0,1");
			try {
				minPrice = (Double) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				minPrice = (Double) objects.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	public double getLowestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException {
		double minPrice;

		if (id == null) {
			SecurityException se = new SecurityException();
			se.setDetail(SecurityException.NO_LOWEST_PRICE);
			se.setStartDate(startDate);
			se.setEndDate(endDate);
			throw se;
		}

		minPrice = Double.MAX_VALUE;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List objects = super.findBySQL("select s.adjnav from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.adjnav is not null  and s.securityid=" + id + " and s.date >='" + df.format(startDate) + "' and s.date <='" + df.format(endDate) + "' order by s.adjnav asc limit 0,1");
			try {
				minPrice = (Double) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				minPrice = (Double) objects.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

		minPrice = Double.MAX_VALUE;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List objects = super.findBySQL("select s.close from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.close is not null  and s.securityid=" + id + " and s.date >='" + df.format(startDate) + "' and s.date <='" + df.format(endDate) + "' order by s.close asc limit 0,1");
			try {
				minPrice = (Double) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				minPrice = (Double) objects.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public List<Security> getMutualFund(long assetClassID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", com.lti.system.Configuration.SECURITY_TYPE_MUTUAL_FUND));
		detachedCriteria.add(Restrictions.eq("ClassID", assetClassID));
		return this.getSecurities(detachedCriteria);
	}

	public List<Security> getMutualFunds() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", com.lti.system.Configuration.SECURITY_TYPE_MUTUAL_FUND));
		return this.getSecurities(detachedCriteria);
	}

	public PaginationSupport getMutualFunds(int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.lt("SecurityType", com.lti.system.Configuration.SECURITY_TYPE_MUTUAL_FUND));
		return this.findSecurities(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public List<SecurityDailyData> getNAVList(Long securityid) {

		List<SecurityDailyData> sdds = this.getDailydatas(securityid, true);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
		// detachedCriteria.add(Restrictions.isNotNull("Close"));
		// detachedCriteria.add(Restrictions.isNotNull("AdjClose"));
		detachedCriteria.add(Restrictions.ge("Date", startDate));
		detachedCriteria.add(Restrictions.gt("NAV", 0.0));
		detachedCriteria.addOrder(Order.asc("Date"));
		List<SecurityDailyData> newSdds = super.findByCriteria(detachedCriteria);
		return newSdds;
	}

	public List<SecurityDailyData> getNAVList(Long securityid, Date startDate, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
		detachedCriteria.add(Restrictions.isNotNull("Close"));
		detachedCriteria.add(Restrictions.isNotNull("AdjClose"));
		detachedCriteria.add(Restrictions.ge("Date", startDate));
		detachedCriteria.add(Restrictions.le("Date", endDate));
		detachedCriteria.add(Restrictions.gt("NAV", 0.0));
		detachedCriteria.addOrder(Order.asc("Date"));
		List<SecurityDailyData> newSdds = super.findByCriteria(detachedCriteria);
		return newSdds;

	}

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
		} else
			return sdd.getNAV();

	}

	public double getAdjNAVPrice(Long id, Date date) throws NoPriceException {
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
		} else
			return sdd.getAdjNAV();
	}

	@SuppressWarnings("deprecation")
	public double getPrice(Long id) throws NoPriceException {
		return this.getPrice(id, new Date());
	}

	// @Override
	// public double getPriceOnly(long id,Date date){
	// }
	// @Override
	// public double getSplitOnly(long id,Date date){
	//
	// }
	// @Override
	// public double getDividendOnly(long id,Date date){
	//
	// }

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
		} else
			return sdd.getClose();
	}

	public List<Security> getSecurities() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		return this.getSecurities(detachedCriteria);
	}

	public List<Security> getSecurities(DetachedCriteria detachedCriteria) {
		return findByCriteria(detachedCriteria);
	}

	public PaginationSupport getSecurities(int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		return this.findSecurities(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public PaginationSupport getPureSecurities(int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.ne("SecurityType", 6));
		return this.findSecurities(detachedCriteria, pageSize, startIndex);
	}

	public List<Security> getSecurities(String[] nameArray) {
		if (nameArray != null) {
			List<Security> list = new ArrayList<Security>();
			for (int i = 0; i < nameArray.length; i++) {
				Security s = get(nameArray[i]);
				if (s != null)
					list.add(s);
			}
			return list;
		}
		return null;
	}

	@Override
	public List<Security> getSecuritiesByAsset(Long assetID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("ClassID", assetID));
		detachedCriteria.addOrder(Order.asc("ID"));
		return this.getSecurities(detachedCriteria);
	}

	@Override
	public List<Security> getSecuritiesByAssetIDAndEndDate(Long assetID, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("ClassID", assetID));
		detachedCriteria.add(Restrictions.ne("SecurityType", Configuration.SECURITY_TYPE_STOCK));
		detachedCriteria.add(Restrictions.gt("EndDate", endDate));
		detachedCriteria.addOrder(Order.asc("ID"));
		return this.getSecurities(detachedCriteria);
	}

	public List<Security> getSecuritiesByClass(Long classid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);

		if (classid == 0)
			return this.getSecurities(detachedCriteria);
		else if (classid > 0) {
			AssetClass assetClass = (AssetClass) getHibernateTemplate().get(AssetClass.class, classid);
			if (assetClass == null)
				return null;
			List<Long> IDList = new ArrayList<Long>();
			// IDList.add(classid);
			Queue<Long> IDQueue = new LinkedList<Long>();
			IDQueue.add(classid);
			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();
				IDList.add(ID);
				List<AssetClass> childClasses = findByHQL("from AssetClass ac where ac.ParentID=" + ID);
				if (childClasses != null && childClasses.size() > 0) {
					for (int i = 0; i < childClasses.size(); i++)
						if (!IDList.contains(childClasses.get(i).getID()))
							IDQueue.add(childClasses.get(i).getID());

				}
			}
			Object[] IDs = IDList.toArray();
			detachedCriteria.add(Restrictions.in("ClassID", IDs));
			detachedCriteria.addOrder(Order.asc("ID"));
			// detachedCriteria.add(Restrictions.in("AssetClassID", IDs));
			return this.getSecurities(detachedCriteria);
		}
		return null;
	}

	@Override
	public List<Security> getFundsByClassIDAndEndDate(Long classID, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);

		if (classID == 0)
			return this.getSecurities(detachedCriteria);
		else if (classID > 0) {
			AssetClass assetClass = (AssetClass) getHibernateTemplate().get(AssetClass.class, classID);
			if (assetClass == null)
				return null;
			List<Long> IDList = new ArrayList<Long>();
			// IDList.add(classid);
			Queue<Long> IDQueue = new LinkedList<Long>();
			IDQueue.add(classID);
			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();
				IDList.add(ID);
				List<AssetClass> childClasses = findByHQL("from AssetClass ac where ac.ParentID=" + ID);
				if (childClasses != null && childClasses.size() > 0) {
					for (int i = 0; i < childClasses.size(); i++)
						if (!IDList.contains(childClasses.get(i).getID()))
							IDQueue.add(childClasses.get(i).getID());

				}
			}
			Object[] IDs = IDList.toArray();
			detachedCriteria.add(Restrictions.in("ClassID", IDs));
			detachedCriteria.add(Restrictions.ne("SecurityType", Configuration.SECURITY_TYPE_STOCK));
			detachedCriteria.add(Restrictions.gt("EndDate", endDate));
			detachedCriteria.addOrder(Order.asc("ID"));
			// detachedCriteria.add(Restrictions.in("AssetClassID", IDs));
			return this.getSecurities(detachedCriteria);
		}
		return null;
	}

	public List<Security> getFundsByClass(Long classid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);

		if (classid == 0)
			return this.getSecurities(detachedCriteria);
		else if (classid > 0) {
			AssetClass assetClass = (AssetClass) getHibernateTemplate().get(AssetClass.class, classid);
			if (assetClass == null)
				return null;
			List<Long> IDList = new ArrayList<Long>();
			// IDList.add(classid);
			Queue<Long> IDQueue = new LinkedList<Long>();
			IDQueue.add(classid);
			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();
				IDList.add(ID);
				List<AssetClass> childClasses = findByHQL("from AssetClass ac where ac.ParentID=" + ID);
				if (childClasses != null && childClasses.size() > 0) {
					for (int i = 0; i < childClasses.size(); i++)
						if (!IDList.contains(childClasses.get(i).getID()))
							IDQueue.add(childClasses.get(i).getID());

				}
			}
			Object[] IDs = IDList.toArray();
			detachedCriteria.add(Restrictions.in("ClassID", IDs));
			detachedCriteria.add(Restrictions.or(Restrictions.eq("SecurityType", 2), Restrictions.eq("SecurityType", 4)));
			// detachedCriteria.add(Restrictions.in("AssetClassID", IDs));
			return this.getSecurities(detachedCriteria);
		}
		return null;
	}

	public PaginationSupport getSecuritiesByClass(Long classid, int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		if (classid == 0)
			return this.findSecurities(detachedCriteria, pageSize, startIndex);
		else if (classid > 0) {
			AssetClass assetClass = (AssetClass) getHibernateTemplate().get(AssetClass.class, classid);
			if (assetClass == null)
				return null;
			List<Long> IDList = new ArrayList<Long>();
			IDList.add(classid);
			Queue<Long> IDQueue = new LinkedList<Long>();
			IDQueue.add(classid);
			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();
				IDList.add(ID);
				List<AssetClass> childClasses = findByHQL("from AssetClass ac where ac.ParentID=" + classid);
				if (childClasses != null && childClasses.size() > 0) {
					for (int i = 0; i < childClasses.size(); i++)
						if (!IDList.contains(childClasses.get(i).getID()))
							IDQueue.add(childClasses.get(i).getID());
				}
			}
			Object[] IDs = IDList.toArray();
			detachedCriteria.add(Restrictions.in("AssetClassID", IDs));
			return this.findSecurities(detachedCriteria, pageSize, startIndex);
		}
		return null;
	}

	@Override
	public List<Security> getSecuritiesByNameConsiderLength(String key, int considerLength) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		if (considerLength != 0 && key.length() > 5)
			detachedCriteria.add(Restrictions.ne("SecurityType", Configuration.SECURITY_TYPE_CLOSED_END_FUND));
		detachedCriteria.add(Restrictions.or(Restrictions.like("Name", "%" + key + "%"), Restrictions.like("Symbol", "%" + key + "%")));
		return this.getSecurities(detachedCriteria);
	}

	public List<Security> getSecuritiesByName(String key) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.or(Restrictions.like("Name", "%" + key + "%"), Restrictions.like("Symbol", "%" + key + "%")));
		return this.getSecurities(detachedCriteria);
	}

	public PaginationSupport getSecuritiesByName(String key, int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.or(Restrictions.like("Name", "%" + key + "%"), Restrictions.like("Symbol", "%" + key + "%")));
		return this.findSecurities(detachedCriteria, pageSize, startIndex);
	}

	public List<Security> getSecuritiesByType(int type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", type));
		return this.getSecurities(detachedCriteria);
	}

	public PaginationSupport getSecuritiesByType(int type, int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", type));
		return this.findSecurities(detachedCriteria, pageSize, startIndex);
	}

	public List<Security> getSecuritiesByTypeAndClass(Long classid, int type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", type));
		if (classid == 0)
			return this.getSecurities(detachedCriteria);
		else if (classid > 0) {
			AssetClass assetClass = (AssetClass) getHibernateTemplate().get(AssetClass.class, classid);
			if (assetClass == null)
				return null;
			List<Long> IDList = new ArrayList<Long>();
			IDList.add(classid);

			//
			Queue<Long> IDQueue = new LinkedList<Long>();
			IDQueue.add(classid);
			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();
				IDList.add(ID);
				List<AssetClass> childClasses = findByHQL("from AssetClass ac where ac.ParentID=" + classid);
				if (childClasses != null && childClasses.size() > 0) {
					for (int i = 0; i < childClasses.size(); i++)
						IDQueue.add(childClasses.get(i).getID());
				}
			}
			//
			Long[] IDs = (Long[]) IDList.toArray();
			detachedCriteria.add(Restrictions.in("AssetClassID", IDs));
			return this.getSecurities(detachedCriteria);
		}
		return null;
	}

	public PaginationSupport getSecuritiesByTypeAndClass(Long classid, int type, int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", type));
		if (classid == 0)
			return this.findSecurities(detachedCriteria, pageSize, startIndex);
		else if (classid > 0) {
			AssetClass assetClass = (AssetClass) getHibernateTemplate().get(AssetClass.class, classid);
			if (assetClass == null)
				return null;
			List<Long> IDList = new ArrayList<Long>();
			IDList.add(classid);

			//
			Queue<Long> IDQueue = new LinkedList<Long>();
			IDQueue.add(classid);
			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();
				IDList.add(ID);

				//
				List<AssetClass> childClasses = findByHQL("from AssetClass ac where ac.ParentID=" + classid);
				if (childClasses != null && childClasses.size() > 0) {
					for (int i = 0; i < childClasses.size(); i++)
						IDQueue.add(childClasses.get(i).getID());
				}
			}

			//
			Long[] IDs = (Long[]) IDList.toArray();
			detachedCriteria.add(Restrictions.in("AssetClassID", IDs));
			return this.findSecurities(detachedCriteria, pageSize, startIndex);
		}
		return null;
	}

	@Override
	public SecurityDailyData getSecurityDailyData(long id) {
		return (SecurityDailyData) getHibernateTemplate().get(SecurityDailyData.class, id);
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
		} else
			return sdd.getSplit();
	}

	@Override
	public Date getNAVStartDate(Long securityID) {
		Security s = get(securityID);
		Date NAVStartDate = s.getNAVStartDate();
		if (NAVStartDate != null)
			return NAVStartDate;
		String sql = "select s.date from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.nav is not null and s.securityid = " + securityID + " order by s.date asc limit 0,1";
		try {
			List objects = super.findBySQL(sql);
			Timestamp t = null;
			try {
				t = (Timestamp) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				t = (Timestamp) objects.get(0);
			}
			s.setNAVStartDate(t);
			this.update(s);
			return t;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Date getStartDate(Long securityid) {
		String sql = "";
		Security s = get(securityid);
		Date startDate;
		startDate = s.getStartDate();
		if (startDate != null)
			return startDate;
		if (s.getSymbol().startsWith("P_")) {
			Long portfolioid = null;
			try {
				portfolioid = Long.parseLong(s.getSymbol().substring(2));
			} catch (Exception e2) {
			}
			if (portfolioid != null) {
				sql = "select s.date from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " s where s.portfolioid=" + portfolioid + " order by s.date asc limit 0,1";
			}
		} else {
			sql = "select s.date from " + Configuration.TABLE_SECURITY_DAILY_DATA + " s where s.close is not null and s.adjclose is not null  and s.securityid=" + securityid + " order by s.date asc limit 0,1";
		}

		try {
			List objects = super.findBySQL(sql);
			Timestamp t = null;
			try {
				t = (Timestamp) ((Object[]) objects.get(0))[0];
			} catch (Exception e) {
				t = (Timestamp) objects.get(0);
			}
			s.setStartDate(t);
			this.update(s);
			return t;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
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
	public byte[] getZipDailydatas(Long securityid) {
		try {
			List<SecurityDailyData> list = null;
			try {
				list = this.getDailydatas(securityid, true);
			} catch (Exception e) {
			}
			if (list == null) {
				list = this.getDailydatasFromDB(securityid, true, null, null);
			}
			return ZipObject.ObjectToZipBytes(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void removeAll(List<SecurityDailyData> sdds) {
		if (sdds == null || sdds.size() <= 0) {
			return;
		}
		super.removeAll(sdds);
	}

	public void removeDailyData(Long securityid, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityDailyData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
		detachedCriteria.add(Restrictions.eq("Date", date));
		List<SecurityDailyData> bolist = findByCriteria(detachedCriteria);
		if (bolist == null) {
			return;
		}
		for (int i = 0; i < bolist.size(); i++) {
			getHibernateTemplate().delete(bolist.get(i));
		}
	}

	@Override
	public void removeSecurityDailyData(long id) {
		SecurityDailyData s = getSecurityDailyData(id);
		if (s != null) {
			getHibernateTemplate().delete(s);
		}
	}

	@Override
	public void saveAll(List<SecurityDailyData> sdds) {
		if (sdds == null || sdds.size() <= 0) {
			return;
		}
		super.saveAll(sdds);
	}

	public Long saveDailyData(SecurityDailyData securityDailyData) {
		getHibernateTemplate().save(securityDailyData);
		return securityDailyData.getID();
	}

	@Override
	public List<SecurityMPT> getSecurityByMPT(DetachedCriteria detachedCriteria) {
		List<SecurityMPT> MPTs = findByCriteria(detachedCriteria);
		return MPTs;
	}

	@Override
	public void saveOrUpdateAll(List<SecurityDailyData> sdds) {
		if (sdds == null || sdds.size() <= 0) {
			return;
		}
		super.saveOrUpdateAll(sdds);

	}

	public void update(Security security) {
		if (security == null || security.getID() == null) {
			return;
		}
		getHibernateTemplate().update(security);

	}

	@Override
	public void saveOrUpdate(Security security) {
		if (security == null) {
			return;
		}
		getHibernateTemplate().saveOrUpdate(security);

	}

	@Override
	public void updateMPTLastDate(Security security) throws Exception {
		if (security != null && security.getMptLastDate() != null) {
			String date = LTIDate.parseDateToString(security.getMptLastDate());
			String sql = "update ltisystem.ltisystem_security set mptlastdate = \'" + date + "\' where id = " + security.getID();
			super.executeSQL(sql);
		}
	}

	@Override
	public void updateNAVStartDate(Security security) throws Exception {
		if (security != null && security.getNAVStartDate() != null) {
			String date = LTIDate.parseDateToString(security.getNAVStartDate());
			String sql = "update ltisystem.ltisystem_security set navstartdate = \'" + date + "\' where id = " + security.getID();
			super.executeSQL(sql);
		}
	}

	@Override
	public void updateEndDateAndPriceLastDate(Security security) throws Exception {
		if (security != null && security.getEndDate() != null) {
			String date = LTIDate.parseDateToString(security.getEndDate());
			String sql = "update ltisystem.ltisystem_security set pricelastdate = \'" + date + "\' , enddate = \'" + date + "\' where id = " + security.getID();
			super.executeSQL(sql);
		}
	}

	@Override
	public void updateNewDividendDate(Security security) throws Exception {
		if (security != null) {
			String sql;
			if (security.getNewDividendDate() != null) {
				String date = LTIDate.parseDateToString(security.getNewDividendDate());
				sql = "update ltisystem.ltisystem_security set newdividenddate = \'" + date + "\' where id = " + security.getID();
			} else
				sql = "update ltisystem.ltisystem_security set newdividenddate = null where id = " + security.getID();

			super.executeSQL(sql);
		}
	}

	@Override
	public void updateNAVLastDate(Security security) throws Exception {
		if (security != null && security.getNavLastDate() != null) {
			String date = LTIDate.parseDateToString(security.getNavLastDate());
			String sql = "update ltisystem.ltisystem_security set navlastdate = \'" + date + "\' where id = " + security.getID();
			super.executeSQL(sql);
		}
	}

	@Override
	public void updateDividendLastDate(Security security) throws Exception {
		if (security != null && security.getDividendLastDate() != null) {
			String date = LTIDate.parseDateToString(security.getDividendLastDate());
			String sql = "update ltisystem.ltisystem_security set dividendlastdate = \'" + date + "\' where id = " + security.getID();
			super.executeSQL(sql);
		}

	}

	@Override
	public void updateStartDate(Security security) throws Exception {
		if (security != null && security.getStartDate() != null) {
			String date = LTIDate.parseDateToString(security.getStartDate());
			String sql = "update ltisystem.ltisystem_security set startdate = \'" + date + "\' where id = " + security.getID();
			super.executeSQL(sql);
		}
	}

	public void updateDailyData(SecurityDailyData securityDailyData) {
		getHibernateTemplate().update(securityDailyData);
	}

	@Override
	public void saveOrUpdateAllSecurityMPT(List<SecurityMPT> smpts) {
		if (smpts == null || smpts.size() <= 0) {
			return;
		}
		super.saveOrUpdateAll(smpts);
	}

	public void saveOrUpdateAllSecurityMPTIncDate(List<SecurityMPTIncData> mpts) {
		if (mpts == null || mpts.size() <= 0)
			return;
		super.saveOrUpdateAll(mpts);
	}

	public void saveOrUpdateAllSecurity(List<Security> securityList) {
		if (securityList == null || securityList.size() <= 0)
			return;
		super.saveOrUpdateAll(securityList);
	}

	public void removeAllShillerSP500() throws Exception {
		String sql = "delete from ltisystem.ltisystem_shillersp500";
		super.executeSQL(sql);
	}

	public void saveAllShillerSP500(List<ShillerSP500> list) {
		if (list == null || list.size() <= 0)
			return;
		super.saveAll(list);
	}

	/***
	 * @author CCD first run to prepare incremental data for MPT calculation
	 ****/
	public void setSecurityMPTIncrementalData() {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Long cashID = this.get(Configuration.getCashSymbol()).getID();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.ne("SecurityType", 6));
		List<Security> securityList = getSecurities(detachedCriteria);
		// long startTime = System.currentTimeMillis();
		for (int i = 0; i < securityList.size(); ++i) {
			try {
				Security security = securityList.get(i);
				if (security.getAssetClass() == null)
					continue;
				List<SecurityDailyData> securityDailyDataList = securityManager.getDailydatas(security.getID(), true);
				if (securityDailyDataList == null || securityDailyDataList.size() == 0)
					continue;
				int size = securityDailyDataList.size();
				Date startDate = LTIDate.getNewTradingDate(securityDailyDataList.get(0).getDate(), TimeUnit.DAILY, -5);
				Date endDate = LTIDate.getNewTradingDate(securityDailyDataList.get(size - 1).getDate(), TimeUnit.DAILY, 5);
				List<SecurityDailyData> benchList = securityManager.getDailyDatas(security.getAssetClass().getBenchmarkID(), startDate, endDate);
				List<SecurityDailyData> cashList = securityManager.getDailyDatas(cashID, startDate, endDate);
				initialMPTIncrementalData(security, securityDailyDataList, benchList, cashList);
				// System.out.println("Success : Security "+
				// securityList.get(i).getSymbol()+ " ID"+
				// securityList.get(i).getID());
			} catch (Exception e) {

				// System.out.println("Fail : Security "+
				// securityList.get(i).getSymbol()+ " ID"+
				// securityList.get(i).getID());
				e.printStackTrace();
				continue;
			}
		}
		// long endTime = System.currentTimeMillis();
		// System.out.println("Total time: " + (endTime - startTime));
	}

	/**
	 * @author CCD set the initial data for incremenatl calculation of MPT
	 */
	public void initialMPTIncrementalData(Security security, List<SecurityDailyData> securityList, List<SecurityDailyData> benchList, List<SecurityDailyData> cashList) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		List<SecurityMPTIncData> securityMPTIncDataList = new ArrayList<SecurityMPTIncData>();
		SecurityMPTIncData[] securityMPTIncDatas = new SecurityMPTIncData[5];
		int datasize = securityList.size();
		Date lastDate = securityList.get(datasize - 1).getDate();
		Long securityID = securityList.get(0).getSecurityID();
		Date startDate, endDate = new Date();
		Date[] yearStartDate = new Date[5];
		Date[] yearNextStartDate = new Date[5];

		yearStartDate[0] = LTIDate.getNewNYSEYear(lastDate, -5);
		yearStartDate[1] = LTIDate.getNewNYSEYear(lastDate, -3);
		yearStartDate[2] = LTIDate.getNewNYSEYear(lastDate, -1);
		yearStartDate[3] = security.getStartDate();
		yearStartDate[4] = LTIDate.getLastNYSETradingDayOfYear(yearStartDate[2]);
		/** if the security don't have 3 year or 5 five history data ***/
		for (int i = 0; i < 5; ++i) {
			if (LTIDate.before(yearStartDate[i], yearStartDate[3]))
				yearStartDate[i] = yearStartDate[3];
			// System.out.println("From "+yearStartDate[i]+" to "+lastDate);
		}
		/** year indexs for security mpt ****/
		int[] securityMPTIndex = new int[5];
		securityMPTIndex[0] = -5;
		securityMPTIndex[1] = -3;
		securityMPTIndex[2] = -1;
		securityMPTIndex[3] = 0;
		securityMPTIndex[4] = lastDate.getYear() + 1900;
		/*************************************************************** */
		/**
		 * 
		 * 0 stands for five year to lastDate 1 stands for three year to
		 * lastDate 2 stands for one year to lastDate 3 stands for startDate to
		 * lastDate 4 stands for thisyear to lastDate
		 * ****/
		double[] sigmaSB = new double[5];
		double[] sigmaS = new double[5];
		double[] sigmaSS = new double[5];
		double[] sigmaB = new double[5];
		double[] sigmaBB = new double[5];
		double[] sigmaR = new double[5];
		double[] sigmaLS = new double[5];
		double[] sigmaLSS = new double[5];
		double[] drawDownHigh = new double[5];
		double[] drawDown = new double[5];
		boolean[] isFirst = new boolean[5];
		int[] interval = new int[5];
		int[] size = new int[5];
		double preAValue = 0;
		double curAValue = 0;
		double preBValue = 0;
		double curBValue = 0;
		double preCValue = 0;
		double curCValue = 0;
		Date currentDate = securityList.get(0).getDate();
		preAValue = securityList.get(0).getAdjClose();
		preBValue = this.getBenchmarkAmount(benchList, currentDate);
		preCValue = this.getBenchmarkAmount(cashList, currentDate);
		double currentAReturn, currentBReturn, currentLogAReturn;
		double lastS, lastB, lastLS, lastR, lastSS, lastSB, lastBB, lastLSS, tempDrawDown;
		/******************************************************************************************/
		for (int i = 0; i < 5; ++i) {
			sigmaSB[i] = sigmaS[i] = sigmaSS[i] = sigmaB[i] = sigmaBB[i] = sigmaR[i] = sigmaLS[i] = sigmaLSS[i] = drawDownHigh[i] = drawDown[i] = 0.0;
			isFirst[i] = true;
			interval[i] = LTIDate.calculateIntervalIgnoreHolidDay(yearStartDate[i], lastDate);
		}
		for (int i = 1; i < datasize; ++i) {
			currentDate = securityList.get(i).getDate();

			curAValue = securityList.get(i).getAdjClose();
			curBValue = this.getBenchmarkAmount(benchList, currentDate);
			curCValue = this.getBenchmarkAmount(cashList, currentDate);

			lastS = baseFormulaManager.computeIntervalReturn(curAValue, preAValue);
			lastLS = baseFormulaManager.computeLogIntervalReturn(curAValue, preAValue);
			lastB = baseFormulaManager.computeIntervalReturn(curBValue, preBValue);
			lastR = baseFormulaManager.computeIntervalReturn(curCValue, preCValue);
			lastSB = lastS * lastB;
			lastSS = lastS * lastS;
			lastBB = lastB * lastB;
			lastLSS = lastLS * lastLS;

			for (int j = 0; j < 5; ++j) {
				if (LTIDate.after(currentDate, yearStartDate[j])) {
					sigmaSB[j] += lastSB;
					sigmaS[j] += lastS;
					sigmaSS[j] += lastSS;
					sigmaB[j] += lastB;
					sigmaBB[j] += lastBB;
					sigmaLS[j] += lastLS;
					sigmaLSS[j] += lastLSS;
					sigmaR[j] += lastR;
					if (j >= 3) {
						if (isFirst[j]) {
							isFirst[j] = false;
							size[j] = datasize - i;
							tempDrawDown = (preAValue - curAValue) / preAValue;
							drawDown[j] = tempDrawDown > drawDown[j] ? tempDrawDown : drawDown[j];
							drawDownHigh[j] = preAValue > curAValue ? preAValue : curAValue;
						} else {
							tempDrawDown = (drawDownHigh[j] - curAValue) / drawDownHigh[j];
							drawDown[j] = tempDrawDown > drawDown[j] ? tempDrawDown : drawDown[j];
							drawDownHigh[j] = drawDownHigh[j] > curAValue ? drawDownHigh[j] : curAValue;
						}
					} else {
						if (isFirst[j]) {
							isFirst[j] = false;
							size[j] = datasize - i;
						}
					}
				} else
					break;
			}

			preAValue = curAValue;
			preBValue = curBValue;
			preCValue = curCValue;
		}

		for (int i = 0; i < 5; ++i) {
			securityMPTIncDatas[i] = new SecurityMPTIncData();
			securityMPTIncDatas[i].setDataLastDate(lastDate);
			securityMPTIncDatas[i].setDataStartDate(yearStartDate[i]);
			securityMPTIncDatas[i].setSigmaB(sigmaB[i]);
			securityMPTIncDatas[i].setSigmaBB(sigmaBB[i]);
			securityMPTIncDatas[i].setSigmaS(sigmaS[i]);
			securityMPTIncDatas[i].setSigmaSS(sigmaSS[i]);
			securityMPTIncDatas[i].setSigmaSB(sigmaSB[i]);
			securityMPTIncDatas[i].setSigmaR(sigmaR[i]);
			securityMPTIncDatas[i].setSigmaLS(sigmaLS[i]);
			securityMPTIncDatas[i].setSigmaLSS(sigmaLSS[i]);
			securityMPTIncDatas[i].setIntervalDays(interval[i]);
			securityMPTIncDatas[i].setSize(size[i]);
			securityMPTIncDatas[i].setYear(securityMPTIndex[i]);
			securityMPTIncDatas[i].setDrawDownHigh(drawDownHigh[i]);
			securityMPTIncDatas[i].setDrawDown(drawDown[i]);
			securityMPTIncDatas[i].setSecurityID(securityID);

			securityMPTIncDataList.add(securityMPTIncDatas[i]);
		}

		this.saveOrUpdateAllSecurityMPTIncDate(securityMPTIncDataList);
	}

	public List<SecurityMPTIncData> getSecurityMPTIncData(Long securityID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPTIncData.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.addOrder(Order.asc("Year"));
		List<SecurityMPTIncData> securityMPTIncDataList = findByCriteria(detachedCriteria);
		return securityMPTIncDataList;
	}

	/**
	 * @author CCD addedon 2009-11-12 for junit test
	 * @return return the security MPT using Incremental calculation
	 */
	public List<SecurityMPT> getOneSecurityINCMPT(Long securityID) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		Security security = this.get(securityID);
		String symbol = security.getSymbol();
		Security benchmark = get(security.getAssetClass().getBenchmarkID());
		if (security.getAssetClass() == null)
			return null;
		Date endDate = security.getEndDate();
		List<SecurityMPTIncData> securityMPTIncDataList = getSecurityMPTIncData(securityID);
		List<SecurityMPT> securityMPTList = null;
		boolean isNewest = true;
		Long[] years = { 2009L, 0L, -1L, -3L, -5L };
		if (securityMPTIncDataList != null && securityMPTIncDataList.size() > 0) {
			Date mptLastDate = securityMPTIncDataList.get(0).getDataLastDate();
			Date today = LTIDate.getNewTradingDate(mptLastDate, TimeUnit.DAILY, 1);
			// System.out.println("******************"+security.getSymbol()+"******************");
			while (!LTIDate.after(today, endDate)) {
				isNewest = false;
				// System.out.println("******************"+today+"******************");
				securityMPTIncDataList = getSecurityMPTIncData(security.getID());
				try {
					securityMPTList = baseFormulaManager.calculateOneSecurityINCMPT(security, benchmark, securityMPTIncDataList, today);
				} catch (NoPriceException e) {
					return null;
				}
				today = LTIDate.getNewTradingDate(today, TimeUnit.DAILY, 1);
			}
			security.setMptLastDate(endDate);
			this.update(security);
		}
		if (isNewest) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
			detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
			detachedCriteria.add(Restrictions.in("Year", years));
			detachedCriteria.addOrder(Order.desc("Year"));
			securityMPTList = findByCriteria(detachedCriteria);

		}
		return securityMPTList;
	}

	/**
	 * @author CCD using Incremental calculation for MPT update
	 */
	public void updateAllSecurityMPT(Date logDate) {

		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		int total_count = 0;
		int success_count = 0;
		long cashID = this.get(Configuration.getCashSymbol()).getID();
		Date curDate = new Date();
		Configuration.writeLog("Start Update MPT", logDate, curDate, "\n************************************\n ");
		String symbol;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.ne("SecurityType", 6));
		int day = curDate.getDay();
		if (day != 6)
			detachedCriteria.add(Restrictions.ne("SecurityType", 5));

		List<Security> securityList = getSecurities(detachedCriteria);
		// securityList = securityList.subList(0, 400);
		total_count = securityList.size();
		// long startTime = System.currentTimeMillis();
		/****** actually List has just one element *****/
		for (int j = 0; j < dailyUpdateListenerList.size(); ++j) {
			if (dailyUpdateListenerList.get(j) != null)
				dailyUpdateListenerList.get(j).copy_total(total_count);
		}
		long start = System.currentTimeMillis();
		long end;
		for (int i = 0; i < total_count; ++i) {
			for (int j = 0; j < dailyUpdateListenerList.size(); ++j) {
				if (dailyUpdateListenerList.get(j) != null)
					dailyUpdateListenerList.get(j).copy_current(i + 1);
			}
			Security security = securityList.get(i);
			symbol = security.getSymbol();
			Security benchmark = get(security.getAssetClass().getBenchmarkID());
			try {
				// long t1 = System.currentTimeMillis();
				if (security.getAssetClass() == null)
					continue;
				Date endDate = security.getEndDate();
				List<SecurityMPTIncData> securityMPTIncDataList = getSecurityMPTIncData(security.getID());
				if (securityMPTIncDataList != null && securityMPTIncDataList.size() > 0) {
					Date mptLastDate = securityMPTIncDataList.get(0).getDataLastDate();
					Date today = LTIDate.getNewTradingDate(mptLastDate, TimeUnit.DAILY, 1);
					// System.out.println("******************"+security.getSymbol()+"******************");
					while (!LTIDate.after(today, endDate)) {
						// System.out.println("******************"+today+"******************");
						securityMPTIncDataList = getSecurityMPTIncData(security.getID());
						baseFormulaManager.getOneSecurityMPT_Incremental(security, benchmark, securityMPTIncDataList, today);
						today = LTIDate.getNewTradingDate(today, TimeUnit.DAILY, 1);
					}
					security.setMptLastDate(endDate);
					this.update(security);
				} else {
					// the security doesn't have incremental data,today we
					// initial the increment data for it
					List<SecurityDailyData> securityDailyDataList = securityManager.getDailydatas(security.getID(), true);
					if (securityDailyDataList == null || securityDailyDataList.size() == 0)
						continue;
					int size = securityDailyDataList.size();
					Date sDate = LTIDate.getNewTradingDate(securityDailyDataList.get(0).getDate(), TimeUnit.DAILY, -5);
					Date eDate = LTIDate.getNewTradingDate(securityDailyDataList.get(size - 1).getDate(), TimeUnit.DAILY, 5);
					List<SecurityDailyData> benchList = securityManager.getDailyDatas(security.getAssetClass().getBenchmarkID(), sDate, eDate);
					List<SecurityDailyData> cashList = securityManager.getDailyDatas(cashID, sDate, eDate);
					initialMPTIncrementalData(security, securityDailyDataList, benchList, cashList);
				}
				// long t2 = System.currentTimeMillis();
				Configuration.writeLog(symbol, logDate, curDate, " finish caculation of MPT");
				// System.out.println("Symbol: "+ symbol +" Cost Time: "+
				// (t2-t1));
				++success_count;

				// System.out.println("Success : Security "+
				// securityList.get(i).getSymbol()+ " ID"+
				// securityList.get(i).getID());
			} catch (Exception e) {
				Configuration.writeLog(symbol, logDate, curDate, " fail caculation of MPT");
				// System.out.println("Fail : Security "+
				// securityList.get(i).getSymbol()+ " ID"+
				// securityList.get(i).getID());
				// e.printStackTrace();
				continue;
			}

		}
		end = System.currentTimeMillis();
		// System.out.println("Calcuate MPT for 400 securities\nCost Time: "+
		// (end-start));
		Configuration.writeLog("ALL Security ", logDate, curDate, "\n************************************\n MPT caculation finished!\n************************************\n");
		Configuration.writeLog("ALL Security ", logDate, curDate, "\nThere are " + total_count + " securities MPT calculation!\n");
		Configuration.writeLog("ALL Security ", logDate, curDate, "\nThere are " + success_count + " securities MPT calculation success!\n");
		Configuration.writeLog("ALL Security ", logDate, curDate, "MPT cost time: " + (end - start));
		// long endTime = System.currentTimeMillis();
		// System.out.println("Total time: " + (endTime - startTime));
	}

	/**
	 * using Traditional calculation for MPT update
	 */
	public void getAllSecurityMPT(Date logDate, boolean update) {
		BaseFormulaUtil bf = new BaseFormulaUtil();
		long startTime = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		/*** initial the mptupdatelog ***/
		CsvListWriter clw = null;
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String dateStr = LTIDate.parseDateToString(new Date());
		String logFileName = systemPath + "MPTUpdateLog_" + dateStr + ".csv";
		File file = new File(logFileName);
		try {
			clw = new CsvListWriter(new FileWriter(file, true), CsvPreference.EXCEL_PREFERENCE);
			String[] header = { "Date", "CostTime", "SecurityID", "Symbol", "EndDate", "MPTLastDate", "State", "Other" };
			clw.writeHeader(header);

		} catch (IOException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		/******************************************************************/
		long cashID = this.get(Configuration.getCashSymbol()).getID();
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		int total_count = 0;
		int success_count = 0;
		int fail_count = 0;
		Date date1 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		boolean justAR = cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY;
		// boolean justAR = true;
		int day = date1.getDay();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.lt("SecurityType", 6));
		if (day != 6)
			detachedCriteria.add(Restrictions.ne("SecurityType", 5));
		detachedCriteria.addOrder(Order.desc("MptLastDate"));
		List<Security> seList = getSecurities(detachedCriteria);
		total_count = seList.size();
		/****** actually List has just one element *****/
		for (int j = 0; j < dailyUpdateListenerList.size(); ++j) {
			if (dailyUpdateListenerList.get(j) != null)
				dailyUpdateListenerList.get(j).copy_total(seList.size());
		}
		for (int i = 0; i < seList.size(); i++) {
			boolean success = true;
			for (int j = 0; j < dailyUpdateListenerList.size(); ++j) {
				if (dailyUpdateListenerList.get(j) != null)
					dailyUpdateListenerList.get(j).copy_current(i + 1);
			}
			List<String> strs = new ArrayList<String>();
			long t1 = System.currentTimeMillis();
			Security se = seList.get(i);
			String symbol = se.getSymbol();
			try {
				// if(!symbol.equalsIgnoreCase("PRJPX"))continue;

				// do not calculate the portfolio and stock
				if (se.getAssetClass() == null) {
					strs = this.getOneLineForCVS(se, "FAIL", "Asset Class Null");
					clw.write(strs);
					continue;
				}
				Date mptLastDate = se.getMptLastDate();
				if (se.getID() == 7163L)
					System.out.println("hi");
				if (justAR) {
					if (mptLastDate != null && se.getEndDate() != null) {
						if (LTIDate.equals(mptLastDate, se.getEndDate())) {
							strs = this.getOneLineForCVS(se, "SUCCESS", "Newest");
							clw.write(strs);
							continue;
						} else {
							List<SecurityMPT> updateList = bf.computeSecurityMPTForAR(se);
							if (updateList.size() > 0) {
								se.setMptLastDate(se.getEndDate());
								this.updateMPTLastDate(se);
								this.saveOrUpdateAllSecurityMPT(updateList);
							}
						}
					}
				} else {
					List<SecurityDailyData> sdds = getDailydatas(se.getID(), true);

					if (sdds == null || sdds.size() == 0)
						continue;

					Date startDate = sdds.get(0).getDate();
					startDate = LTIDate.getNewTradingDate(startDate, TimeUnit.DAILY, -5);
					Date endDate = sdds.get(sdds.size() - 1).getDate();
					if (mptLastDate != null && LTIDate.equals(mptLastDate, endDate)) {// mpt
																						// is
																						// newest
						strs = this.getOneLineForCVS(se, "SUCCESS", "Newest");
						clw.write(strs);
						continue;
					}
					endDate = LTIDate.getNewTradingDate(endDate, TimeUnit.DAILY, 5);
					List<SecurityDailyData> benchs = getDailyDatas(se.getAssetClass().getBenchmarkID(), startDate, endDate);
					List<SecurityDailyData> risks = getDailyDatas(cashID, startDate, endDate);
					this.getOneSecurityMPT(se.getID(), sdds, benchs, update);
					// save the last mpt date
					se.setMptLastDate(sdds.get(sdds.size() - 1).getDate());
					this.updateMPTLastDate(se);
				}

				Configuration.writeLog(symbol, logDate, date1, " finish caculation of MPT");
				++success_count;
			} catch (Exception e) {
				success = false;
				++fail_count;
				Configuration.writeLog(symbol, logDate, date1, " fail caculation of MPT");
				// e.printStackTrace();
			}
			long t2 = System.currentTimeMillis();
			strs.add(new Date().toString());
			strs.add(String.valueOf((t2 - t1) * 1.0 / 1000));
			strs.add(se.getID().toString());
			strs.add(se.getSymbol());
			if (se.getEndDate() != null)
				strs.add(df.format(se.getEndDate()));
			else
				strs.add("NULL");
			if (se.getMptLastDate() != null)
				strs.add(df.format(se.getMptLastDate()));
			else
				strs.add("NULL");
			if (success)
				strs.add("SUCCESS");
			else {
				strs.add("FAIL");
				strs.add("Compute Fail");
			}
			try {
				clw.write(strs);
			} catch (IOException e) {
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}
		Configuration.writeLog("ALL Security ", logDate, date1, "\n************************************\n MPT caculation finished!\n************************************\n");
		Configuration.writeLog("ALL Security ", logDate, date1, "\nThere are " + total_count + " securities MPT calculation!\n");
		Configuration.writeLog("ALL Security ", logDate, date1, "\nThere are " + success_count + " securities MPT calculation success!\n");
		long endTime = System.currentTimeMillis();
		List<String> strs = new ArrayList<String>();
		try {
			strs.add("Complete");
			strs.add("TotalCostTime");
			strs.add("TotalCount");
			strs.add("FailCount");
			clw.write(strs);
			strs.clear();
			strs.add("Yes");
			strs.add(String.valueOf((endTime - startTime) * 1.0 / 1000 / 60));
			strs.add(String.valueOf(total_count));
			strs.add(String.valueOf(fail_count));
			clw.write(strs);
			clw.close();
		} catch (IOException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		} finally {
			if (clw != null) {
				try {
					clw.close();
				} catch (IOException e) {
					System.out.println(StringUtil.getStackTraceString(e));
				}
			}
		}
	}

	private List<String> getOneLineForCVS(Security se, String state, String message) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<String> strs = new ArrayList<String>();
		strs.add(new Date().toString());
		strs.add("0");
		strs.add(se.getID().toString());
		strs.add(se.getSymbol());
		if (se.getEndDate() != null)
			strs.add(df.format(se.getEndDate()));
		else
			strs.add("NULL");
		if (se.getMptLastDate() != null)
			strs.add(df.format(se.getMptLastDate()));
		else
			strs.add("NULL");
		strs.add(state);
		strs.add(message);
		return strs;
	}

	/**
	 * @author CCD added by CCD on 2009-11-12 for junit test
	 * @return
	 */
	public void getOneSecurityMPT(Long securityID) {
		// long cashID = this.get(Configuration.getCashSymbol()).getID();
		Security se = this.get(securityID);
		String symbol = se.getSymbol();
		try {
			if (se.getAssetClass() == null)
				return;
			List<SecurityDailyData> sdds = getDailydatas(se.getID(), true);

			if (sdds != null && sdds.size() > 0) {
				Date startDate = sdds.get(0).getDate();
				startDate = LTIDate.getNewTradingDate(startDate, TimeUnit.DAILY, -5);
				Date endDate = sdds.get(sdds.size() - 1).getDate();
				endDate = LTIDate.getNewTradingDate(endDate, TimeUnit.DAILY, 5);
				List<SecurityDailyData> benchs = getDailyDatas(se.getAssetClass().getBenchmarkID(), startDate, endDate);
				// List<SecurityDailyData> risks = getDailyDatas(cashID,
				// startDate, endDate);
				getOneSecurityMPT(se.getID(), sdds, benchs, true);
				se.setMptLastDate(sdds.get(sdds.size() - 1).getDate());
				this.updateMPTLastDate(se);
			}
		} catch (Exception e) {
			System.out.println(symbol + " Fail");
		}
	}

	/*
	 * private SecurityMPT getOneYearMPT_INC(SecurityMPT smpt, List<Double>
	 * A_Amounts, List<Double> A_Returns, List<Double> B_Returns, List<Double>
	 * R_Returns,List<Double> R_Amounts, List<Date> dateList, int intervalType,
	 * Security se) { BaseFormulaManager baseFormulaManager =
	 * (BaseFormulaManager)
	 * ContextHolder.getInstance().getApplicationContext().getBean
	 * ("baseFormulaManager");
	 * 
	 * double sigmaX = 0; double sigmaB = 0; double sigmaXX = 0; double sigmaBB
	 * = 0; double sigmaXB = 0; double sigmaR = 0; double sigmaLogX = 0; double
	 * sigmaLogXX = 0;
	 * 
	 * if(this.checkCanGetOldSigma(smpt)) { sigmaX = smpt.getSigmaX(); sigmaB =
	 * smpt.getSigmaB(); sigmaXX = smpt.getSigmaXX(); sigmaBB =
	 * smpt.getSigmaBB(); sigmaXB = smpt.getSigmaXB(); sigmaR =
	 * smpt.getSigmaR(); sigmaLogX = smpt.getSigmaLogX(); sigmaLogXX =
	 * smpt.getSigmaLogXX(); }
	 * 
	 * Date lastMPTDate = se.getMptLastDate(); int endIndex =
	 * dateList.indexOf(lastMPTDate);
	 * 
	 * int startIndex = 0; if(intervalType == 0L)startIndex=0; else startIndex =
	 * baseFormulaManager.getSubListIndex(dateList, lastMPTDate, intervalType);
	 * int lastIndex = dateList.size()-1;
	 * 
	 * Date startDate = dateList.get(startIndex); Date currentDate =
	 * dateList.get(lastIndex);
	 * 
	 * if(startIndex>0)startIndex--; lastIndex--; endIndex--; //-2?
	 * 
	 * while(endIndex<=lastIndex){ endIndex++; double tmpX =
	 * A_Returns.get(endIndex); double tmpB = B_Returns.get(endIndex); double
	 * tmpR = R_Returns.get(endIndex); sigmaX+=tmpX; sigmaB+=tmpB; sigmaR+=tmpR;
	 * sigmaXX+=tmpX*tmpX; sigmaBB+=tmpB*tmpB; sigmaXB+=tmpX*tmpB; double logX =
	 * Math.log(1+tmpX); sigmaLogX+=logX; sigmaLogXX+=logX*logX;
	 * 
	 * if(intervalType != 0 && intervalType != -1){ tmpX =
	 * A_Returns.get(startIndex); tmpB = B_Returns.get(startIndex); tmpR =
	 * R_Returns.get(startIndex); sigmaX-=tmpX; sigmaB-=tmpB; sigmaR-=tmpR;
	 * sigmaXX-=tmpX*tmpX; sigmaBB-=tmpB*tmpB; sigmaXB-=tmpX*tmpB; logX =
	 * Math.log(1+tmpX); sigmaLogX-=logX; sigmaLogXX-=logX*logX; startIndex++; }
	 * }
	 * 
	 * int size = lastIndex-startIndex+1; int interval = size+1;
	 * 
	 * double beta = (sigmaXB*size-(sigmaX*sigmaB));
	 * 
	 * double lastRiskFree = R_Amounts.get(R_Amounts.size()-1); double
	 * firstRiskFree = R_Amounts.get(0); double annualizedRiskFree =
	 * baseFormulaManager.computeIntervalReturn(lastRiskFree, firstRiskFree);
	 * if(intervalType!=-1)annualizedRiskFree =
	 * Math.pow(lastRiskFree/firstRiskFree,
	 * TimePara.workingday/(interval*1.0))-1;
	 * 
	 * double averageRiskFree = sigmaR/(interval);
	 * 
	 * double averagePortfolioReturn = sigmaX/size - averageRiskFree; double
	 * averageBenchmarkReturn = sigmaB/size - averageRiskFree;
	 * 
	 * double alpha = baseFormulaManager.computeAlpha(beta,
	 * averagePortfolioReturn, averageBenchmarkReturn);
	 * 
	 * double standardDeviation =
	 * (sigmaLogXX-sigmaLogX*sigmaLogX/size)/(size-1); standardDeviation =
	 * Math.sqrt(standardDeviation); standardDeviation *=
	 * Math.pow(TimePara.workingday, 0.5);
	 * 
	 * double RSquared = 0; double co = (sigmaXB-sigmaX*sigmaB/size)/(size-1);
	 * co = co*co; double stdA = (sigmaXX-sigmaX*sigmaX/size)/(size-1); double
	 * stdB = (sigmaBB-sigmaB*sigmaB/size)/(size-1); RSquared = co/(stdA*stdB);
	 * 
	 * double currentAmount = A_Amounts.get(endIndex); double firstAmount =
	 * A_Amounts.get(startIndex); double AR =
	 * baseFormulaManager.computeIntervalReturn(currentAmount, firstAmount);
	 * 
	 * double sharpeRatio = baseFormulaManager.computeRatio(AR,
	 * annualizedRiskFree, standardDeviation);
	 * 
	 * double treynorRatio = baseFormulaManager.computeRatio(AR,
	 * annualizedRiskFree, beta);
	 * 
	 * 
	 * double drawDown =
	 * baseFormulaManager.computeDrawDown(A_Amounts.subList(startIndex,
	 * endIndex));
	 * 
	 * if (alpha < LTINumber.INF) smpt.setAlpha(alpha); if (beta <
	 * LTINumber.INF) smpt.setBeta(beta); if (AR < LTINumber.INF)
	 * smpt.setAR(AR); if (RSquared < LTINumber.INF) smpt.setRSquared(RSquared);
	 * if (standardDeviation < LTINumber.INF)
	 * smpt.setStandardDeviation(standardDeviation); if (sharpeRatio <
	 * LTINumber.INF) smpt.setSharpeRatio(sharpeRatio); if (treynorRatio <
	 * LTINumber.INF) smpt.setTreynorRatio(treynorRatio); if (drawDown <
	 * LTINumber.INF) smpt.setDrawDown(drawDown); if (AR < LTINumber.INF)
	 * smpt.setReturn(AR);
	 * 
	 * smpt.setSecurityID(se.getID()); smpt.setSecurityName(se.getName());
	 * smpt.setSymbol(se.getSymbol());
	 * smpt.setSecurityType(se.getSecurityType());
	 * 
	 * return smpt; }
	 * 
	 * private boolean checkCanGetOldSigma(SecurityMPT oldsmpt){
	 * if(oldsmpt==null || oldsmpt.getSigmaX()==null ||
	 * oldsmpt.getSigmaXX()==null || oldsmpt.getSigmaB()==null ||
	 * oldsmpt.getSigmaBB()==null || oldsmpt.getSigmaXB()==null ||
	 * oldsmpt.getSigmaR()==null) return false; else return true; }
	 * 
	 * public void getOneSecurityMPTNew(long id, List<SecurityDailyData> sdds,
	 * List<SecurityDailyData> benchs,List<SecurityDailyData> risks, boolean
	 * update) { BaseFormulaManager baseFormulaManager = (BaseFormulaManager)
	 * ContextHolder
	 * .getInstance().getApplicationContext().getBean("baseFormulaManager");
	 * 
	 * Security se = get(id);
	 * 
	 * long assetClass = se.getAssetClass().getID();
	 * 
	 * List<Date> dateList = new ArrayList<Date>();
	 * 
	 * List<Double> A_Amounts = new ArrayList<Double>(); List<Double> B_Amounts
	 * = new ArrayList<Double>(); List<Double> R_Amounts = new
	 * ArrayList<Double>();
	 * 
	 * List<Double> A_Returns = new ArrayList<Double>(); List<Double> B_Returns
	 * = new ArrayList<Double>(); List<Double> R_Returns = new
	 * ArrayList<Double>();
	 * 
	 * double preAValue = 0; double curAValue = 0; double preBValue = 0; double
	 * curBValue = 0; double preRValue = 0; double curRValue = 0;
	 * 
	 * List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();
	 * 
	 * Date yearStart = sdds.get(0).getDate(); Date yearEnd;
	 * 
	 * Date totalStartDate = yearStart;
	 * 
	 * Date totalEndDate = sdds.get(sdds.size()-1).getDate();
	 * if(se.getMptLastDate()!=null &&
	 * LTIDate.equals(totalEndDate,se.getMptLastDate()))return;
	 * 
	 * List<Double> temp_A_Amounts = new ArrayList<Double>(); List<Double>
	 * temp_B_Amounts = new ArrayList<Double>(); List<Double> temp_A_Returns =
	 * new ArrayList<Double>(); List<Double> temp_B_Returns = new
	 * ArrayList<Double>();
	 * 
	 * for (int i = 0; i < sdds.size(); i++) { Date currentDate =
	 * sdds.get(i).getDate();
	 * 
	 * dateList.add(currentDate);
	 * 
	 * curAValue = sdds.get(i).getAdjClose(); curBValue =
	 * this.getBenchmarkAmount(benchs, currentDate); curRValue =
	 * this.getBenchmarkAmount(risks, currentDate);
	 * 
	 * A_Amounts.add(curAValue); B_Amounts.add(curBValue);
	 * R_Amounts.add(curRValue);
	 * 
	 * if (i == 0) { preAValue = curAValue; preBValue = curBValue; preRValue =
	 * curRValue; } if (i > 0) { double AReturn =
	 * baseFormulaManager.computeIntervalReturn(curAValue, preAValue); double
	 * BReturn = baseFormulaManager.computeIntervalReturn(curBValue, preBValue);
	 * double RReturn = baseFormulaManager.computeIntervalReturn(curRValue,
	 * preRValue); A_Returns.add(AReturn); B_Returns.add(BReturn);
	 * R_Returns.add(RReturn); preAValue = curAValue; preBValue = curBValue;
	 * preRValue = curRValue; }
	 * 
	 * boolean isHoldingDate = LTIDate.isHoldingDate(currentDate);
	 * 
	 * 
	 * if (isHoldingDate || i == sdds.size() - 1 || ((i != sdds.size() - 1) &&
	 * (sdds.get(i).getDate().getYear() - sdds.get(i + 1).getDate().getYear() ==
	 * -1))) {
	 * 
	 * int index = baseFormulaManager.getYearEndIndex(dateList, currentDate);
	 * 
	 * Date startDate = dateList.get(index);
	 * 
	 * temp_A_Amounts = A_Amounts.subList(index, i + 1); temp_B_Amounts =
	 * B_Amounts.subList(index, i + 1);
	 * 
	 * temp_A_Returns = A_Returns.subList(index, i); temp_B_Returns =
	 * B_Returns.subList(index, i);
	 * 
	 * yearEnd = currentDate;
	 * 
	 * int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate,
	 * yearEnd);
	 * 
	 * long year = currentDate.getYear() + 1900;
	 * 
	 * SecurityMPT oldsmpt = this.getSecurityMPT(id, year); if (oldsmpt != null
	 * && i != sdds.size() - 1) { if (update) continue; }
	 * 
	 * SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns,
	 * temp_B_Returns, temp_A_Amounts, yearStart, yearEnd, interval, se);
	 * smpt.setAR(smpt.getReturn());
	 * 
	 * smpt.setYear(year);
	 * 
	 * smpt.setAssetClassID(assetClass);
	 * 
	 * smpts.add(smpt); }
	 * 
	 * //Total mpt of security if(i==sdds.size()-1){ Date startDate =
	 * dateList.get(0); int interval =
	 * LTIDate.calculateIntervalIgnoreHolidDay(startDate, currentDate);
	 * SecurityMPT oldsmpt = this.getSecurityMPT(id, 0); SecurityMPT smpt =
	 * null;
	 * 
	 * if(!this.checkCanGetOldSigma(oldsmpt)) smpt = this.getOneYearMPT(oldsmpt,
	 * A_Returns, B_Returns, A_Amounts, startDate, currentDate, interval, se);
	 * else smpt = this.getOneYearMPT_INC(oldsmpt,A_Amounts, A_Returns,
	 * B_Returns, R_Returns,R_Amounts, dateList, 0, se);
	 * 
	 * smpt.setAR(smpt.getReturn()); smpt.setYear(0L);
	 * smpt.setAssetClassID(assetClass); smpts.add(smpt); }
	 * 
	 * if (i == sdds.size() - 1) {
	 * 
	 * int days = LTIDate.calculateInterval(totalStartDate, currentDate);
	 * 
	 * if (days >= TimePara.yearday) { int index =
	 * baseFormulaManager.getSubListIndex(dateList, currentDate, 1); Date
	 * startDate = dateList.get(index); Date oneYearStartDate = startDate;
	 * temp_A_Amounts = A_Amounts.subList(index, i + 1); temp_B_Amounts =
	 * B_Amounts.subList(index, i + 1);
	 * 
	 * temp_A_Returns = A_Returns.subList(index, i); temp_B_Returns =
	 * B_Returns.subList(index, i);
	 * 
	 * SecurityMPT oldsmpt = this.getSecurityMPT(id, -1);
	 * 
	 * SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns,
	 * temp_B_Returns, temp_A_Amounts, startDate, currentDate,
	 * TimePara.workingday, se); smpt.setYear(-1l);
	 * smpt.setAssetClassID(assetClass); smpts.add(smpt); } if (days >=
	 * TimePara.yearday * 3) { int index =
	 * baseFormulaManager.getSubListIndex(dateList, currentDate, 3); Date
	 * startDate = dateList.get(index); Date oneYearStartDate = startDate;
	 * temp_A_Amounts = A_Amounts.subList(index, i + 1); temp_B_Amounts =
	 * B_Amounts.subList(index, i + 1);
	 * 
	 * temp_A_Returns = A_Returns.subList(index, i); temp_B_Returns =
	 * B_Returns.subList(index, i);
	 * 
	 * SecurityMPT oldsmpt = this.getSecurityMPT(id, -3);
	 * 
	 * SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns,
	 * temp_B_Returns, temp_A_Amounts, startDate, currentDate,
	 * TimePara.workingday * 3, se); smpt.setYear(-3l);
	 * smpt.setAssetClassID(assetClass); smpts.add(smpt);
	 * 
	 * } if (days >= TimePara.yearday * 5) { int index =
	 * baseFormulaManager.getSubListIndex(dateList, currentDate, 5); Date
	 * startDate = dateList.get(index); Date oneYearStartDate = startDate;
	 * temp_A_Amounts = A_Amounts.subList(index, i + 1); temp_B_Amounts =
	 * B_Amounts.subList(index, i + 1);
	 * 
	 * temp_A_Returns = A_Returns.subList(index, i); temp_B_Returns =
	 * B_Returns.subList(index, i);
	 * 
	 * SecurityMPT oldsmpt = this.getSecurityMPT(id, -5);
	 * 
	 * SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns,
	 * temp_B_Returns, temp_A_Amounts, startDate, currentDate,
	 * TimePara.workingday * 5, se);
	 * 
	 * smpt.setYear(-5l); smpt.setAssetClassID(assetClass); smpts.add(smpt);
	 * 
	 * } } } saveOrUpdateAllSecurityMPT(smpts);
	 * 
	 * se.setMptLastDate(totalEndDate); this.saveOrUpdate(se); }
	 */

	public void reCalculateOneYearMPT(Long securityID, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, int year) {

		// save the last mpt date
	}

	public void getOneSecurityMPT(long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, boolean update) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		Security se = get(id);
		// System.out.println("security id: "+ id +" security symbol: "+
		// se.getSymbol());
		long assetClass = se.getAssetClass().getID();

		List<Date> dateList = new ArrayList<Date>();

		List<Double> A_Amounts = new ArrayList<Double>();
		List<Double> B_Amounts = new ArrayList<Double>();

		List<Double> A_Returns = new ArrayList<Double>();
		List<Double> B_Returns = new ArrayList<Double>();

		double preAValue = 0;
		double curAValue = 0;
		double preBValue = 0;
		double curBValue = 0;

		List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();

		Date yearStart = sdds.get(0).getDate();
		Date yearEnd;

		Date totalStartDate = yearStart;

		List<Double> temp_A_Amounts = new ArrayList<Double>();
		List<Double> temp_B_Amounts = new ArrayList<Double>();
		List<Double> temp_A_Returns = new ArrayList<Double>();
		List<Double> temp_B_Returns = new ArrayList<Double>();

		/****************************************************************************
		 * List<FundAlert> fundAlertList = new ArrayList<FundAlert>();
		 * if(!update)this.deleteFundAlert(se.getID());
		 ****************************************************************************/
		for (int i = 0; i < sdds.size(); i++) {
			Date currentDate = sdds.get(i).getDate();

			dateList.add(currentDate);

			curAValue = sdds.get(i).getAdjClose();
			curBValue = this.getBenchmarkAmount(benchs, currentDate);

			A_Amounts.add(curAValue);
			B_Amounts.add(curBValue);

			if (i == 0) {
				preAValue = curAValue;
				preBValue = curBValue;
			}
			if (i > 0) {
				double AReturn = baseFormulaManager.computeIntervalReturn(curAValue, preAValue);
				double BReturn = baseFormulaManager.computeIntervalReturn(curBValue, preBValue);

				A_Returns.add(AReturn);
				B_Returns.add(BReturn);
				preAValue = curAValue;
				preBValue = curBValue;

				/*
				 * if(!update) { FundAlert fa =
				 * baseFormulaManager.getFundAlert(A_Returns,
				 * B_Returns,dateList, AReturn, BReturn,se, TimeUnit.MONTHLY, 1,
				 * currentDate);
				 * //System.out.println(currentDate+","+fa.getSTD());
				 * if(fa!=null && fa.getPointType()!=0.0)fundAlertList.add(fa);
				 * }
				 */
			}

			// if(LTIDate.isLastNYSETradingDayOfYear(currentDate) || i ==
			// sdds.size() -1 || LTIDate.isYearEnd(currentDate))

			boolean isHoldingDate = LTIDate.isHoldingDate(currentDate);

			if (isHoldingDate || i == sdds.size() - 1 || ((i != sdds.size() - 1) && (sdds.get(i).getDate().getYear() - sdds.get(i + 1).getDate().getYear() == -1))) {
				// if (i == sdds.size() - 1 || ((i != sdds.size() - 1) &&
				// (sdds.get(i).getDate().getYear() - sdds.get(i +
				// 1).getDate().getYear() == -1))) {

				/*
				 * if(update) { if(i!=sdds.size()-1) continue; }
				 */

				int index = baseFormulaManager.getYearEndIndex(dateList, currentDate);

				Date startDate = dateList.get(index);

				temp_A_Amounts = A_Amounts.subList(index, i + 1);
				temp_B_Amounts = B_Amounts.subList(index, i + 1);

				temp_A_Returns = A_Returns.subList(index, i);
				temp_B_Returns = B_Returns.subList(index, i);

				yearEnd = currentDate;

				int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, yearEnd);

				long year = currentDate.getYear() + 1900;

				SecurityMPT oldsmpt = this.getSecurityMPT(id, year);
				if (oldsmpt != null && i != sdds.size() - 1) {
					if (update)
						continue;
				}

				// SecurityMPT smpt = this.getOneYearMPT(oldsmpt,
				// temp_A_Returns, temp_B_Returns, temp_A_Amounts, yearStart,
				// yearEnd, interval, se);
				// System.out.println(startDate);
				// System.out.println(yearEnd);
				SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, yearEnd, interval, se);
				System.out.println(startDate);
				System.out.println(yearEnd);
				smpt.setAR(smpt.getReturn());

				smpt.setYear(year);

				smpt.setAssetClassID(assetClass);

				smpts.add(smpt);
			}

			// Total mpt of security
			if (i == sdds.size() - 1) {
				Date startDate = dateList.get(0);
				int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, currentDate);
				SecurityMPT oldsmpt = this.getSecurityMPT(id, 0);
				SecurityMPT smpt = this.getOneYearMPT(oldsmpt, A_Returns, B_Returns, A_Amounts, startDate, currentDate, interval, se);
				// smpt.setAR(smpt.getReturn());
				smpt.setYear(0L);
				smpt.setAssetClassID(assetClass);
				smpts.add(smpt);
			}

			/*
			 * if(update){ FundAlert oldFa =
			 * this.getFundAlertBySecurityID(se.getID(), currentDate); FundAlert
			 * newFa = baseFormulaManager.getFundAlert(A_Returns,
			 * B_Returns,dateList, AReturn, BReturn, se, TimeUnit.MONTHLY, 1,
			 * currentDate); if(newFa!=null){ if(oldFa == null)
			 * fundAlertList.add(newFa); else { oldFa.setDate(newFa.getDate());
			 * oldFa.setDR(newFa.getDR()); oldFa.setMean(newFa.getMean());
			 * oldFa.setPointType(newFa.getPointType());
			 * oldFa.setSecurityID(newFa.getSecurityID());
			 * oldFa.setSTD(newFa.getSTD()); fundAlertList.add(oldFa); } } }
			 */

			if (i == sdds.size() - 1) {

				int days = LTIDate.calculateInterval(totalStartDate, currentDate);

				if (days >= TimePara.yearday) {
					int index = baseFormulaManager.getSubListIndex(dateList, currentDate, 1);
					Date startDate = dateList.get(index);
					Date oneYearStartDate = startDate;
					temp_A_Amounts = A_Amounts.subList(index, i + 1);
					temp_B_Amounts = B_Amounts.subList(index, i + 1);

					temp_A_Returns = A_Returns.subList(index, i);
					temp_B_Returns = B_Returns.subList(index, i);
					SecurityMPT oldsmpt = this.getSecurityMPT(id, -1);
					// System.out.println("one year with size of returns: "+
					// (i-index));
					// System.out.println(startDate);
					// System.out.println(currentDate);
					SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, currentDate, TimePara.workingday, se);
					smpt.setYear(-1l);
					smpt.setAssetClassID(assetClass);
					smpts.add(smpt);
				}
				if (days >= TimePara.yearday * 3) {
					int index = baseFormulaManager.getSubListIndex(dateList, currentDate, 3);
					Date startDate = dateList.get(index);
					Date oneYearStartDate = startDate;
					temp_A_Amounts = A_Amounts.subList(index, i + 1);
					temp_B_Amounts = B_Amounts.subList(index, i + 1);

					temp_A_Returns = A_Returns.subList(index, i);
					temp_B_Returns = B_Returns.subList(index, i);

					SecurityMPT oldsmpt = this.getSecurityMPT(id, -3);
					// System.out.println("three year with size of returns: "+
					// (i-index));
					// System.out.println(startDate);
					// System.out.println(currentDate);
					SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, currentDate, TimePara.workingday * 3, se);
					smpt.setYear(-3l);
					smpt.setAssetClassID(assetClass);
					smpts.add(smpt);

				}
				if (days >= TimePara.yearday * 5) {
					int index = baseFormulaManager.getSubListIndex(dateList, currentDate, 5);
					Date startDate = dateList.get(index);
					Date oneYearStartDate = startDate;
					temp_A_Amounts = A_Amounts.subList(index, i + 1);
					temp_B_Amounts = B_Amounts.subList(index, i + 1);

					temp_A_Returns = A_Returns.subList(index, i);
					temp_B_Returns = B_Returns.subList(index, i);

					SecurityMPT oldsmpt = this.getSecurityMPT(id, -5);
					// System.out.println("five year with size of returns: "+
					// (i-index));
					// System.out.println(startDate);
					// System.out.println(currentDate);
					SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, currentDate, TimePara.workingday * 5, se);

					smpt.setYear(-5l);
					smpt.setAssetClassID(assetClass);
					smpts.add(smpt);

				}
			}
		}
		saveOrUpdateAllSecurityMPT(smpts);

		// securityManager.saveOrUpdateFundAlert(fundAlertList);
	}

	@Override
	public void calculateSecurityMPTAfterYear(Long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, List<SecurityDailyData> riskFreeList) {

		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		Security se = get(id);
		// System.out.println("security id: "+ id +" security symbol: "+
		// se.getSymbol());
		long assetClass = se.getAssetClass().getID();

		List<Date> dateList = new ArrayList<Date>();

		List<Double> A_Amounts = new ArrayList<Double>();
		List<Double> B_Amounts = new ArrayList<Double>();
		List<Double> C_Amounts = new ArrayList<Double>();
		List<Double> A_Returns = new ArrayList<Double>();
		List<Double> B_Returns = new ArrayList<Double>();
		List<Double> C_Returns = new ArrayList<Double>();

		double preAValue = 0;
		double curAValue = 0;
		double preBValue = 0;
		double curBValue = 0;
		double preCValue = 0;
		double curCValue = 0;

		List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();

		Date yearEnd;

		List<Double> temp_A_Amounts = new ArrayList<Double>();
		List<Double> temp_A_Returns = new ArrayList<Double>();
		List<Double> temp_B_Returns = new ArrayList<Double>();
		List<Double> temp_C_Returns = new ArrayList<Double>();

		for (int i = 0; i < sdds.size(); i++) {
			Date currentDate = sdds.get(i).getDate();

			dateList.add(currentDate);

			curAValue = sdds.get(i).getAdjClose();
			curBValue = this.getBenchmarkAmount(benchs, currentDate);
			curCValue = this.getBenchmarkAmount(riskFreeList, currentDate);

			A_Amounts.add(curAValue);
			B_Amounts.add(curBValue);
			C_Amounts.add(curCValue);

			if (i == 0) {
				preAValue = curAValue;
				preBValue = curBValue;
				preCValue = curCValue;
			}
			if (i > 0) {
				double AReturn = baseFormulaManager.computeIntervalReturn(curAValue, preAValue);
				double BReturn = baseFormulaManager.computeIntervalReturn(curBValue, preBValue);
				double CReturn = baseFormulaManager.computeIntervalReturn(curCValue, preCValue);
				A_Returns.add(AReturn);
				B_Returns.add(BReturn);
				C_Returns.add(CReturn);
				preAValue = curAValue;
				preBValue = curBValue;
				preCValue = curCValue;

			}

			if (i == sdds.size() - 1 || ((i != sdds.size() - 1) && (sdds.get(i).getDate().getYear() - sdds.get(i + 1).getDate().getYear() == -1))) {

				int index = baseFormulaManager.getYearEndIndex(dateList, currentDate);

				Date startDate = dateList.get(index);

				temp_A_Amounts = A_Amounts.subList(index, i + 1);
				temp_A_Returns = A_Returns.subList(index, i);
				temp_B_Returns = B_Returns.subList(index, i);
				temp_C_Returns = C_Returns.subList(index, i);

				yearEnd = currentDate;

				int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, yearEnd);
				double annualizedRiskFree = Math.pow(curCValue / C_Amounts.get(index), TimePara.workingday * 1.0 / interval) - 1;
				long year = currentDate.getYear() + 1900;

				SecurityMPT smpt = this.getOneYearMPT(null, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, yearEnd, interval, se);

				smpt.setAR(smpt.getReturn());

				smpt.setYear(year);

				smpt.setAssetClassID(assetClass);

				smpts.add(smpt);
			}
		}
		saveOrUpdateAllSecurityMPT(smpts);
	}

	/**
	 * @author CCD added by CCD on 2009-11-12 for junit test using traditional
	 *         calculation of MPT
	 */
	public List<SecurityMPT> calculateOneSecurityMPT(long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, boolean update) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		Security se = get(id);
		// System.out.println("security id: "+ id +" security symbol: "+
		// se.getSymbol());
		long assetClass = se.getAssetClass().getID();

		List<Date> dateList = new ArrayList<Date>();

		List<Double> A_Amounts = new ArrayList<Double>();
		List<Double> B_Amounts = new ArrayList<Double>();

		List<Double> A_Returns = new ArrayList<Double>();
		List<Double> B_Returns = new ArrayList<Double>();

		double preAValue = 0;
		double curAValue = 0;
		double preBValue = 0;
		double curBValue = 0;

		List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();

		Date yearStart = sdds.get(0).getDate();
		Date yearEnd;

		Date totalStartDate = yearStart;

		List<Double> temp_A_Amounts = new ArrayList<Double>();
		List<Double> temp_B_Amounts = new ArrayList<Double>();
		List<Double> temp_A_Returns = new ArrayList<Double>();
		List<Double> temp_B_Returns = new ArrayList<Double>();

		for (int i = 0; i < sdds.size(); i++) {
			Date currentDate = sdds.get(i).getDate();

			dateList.add(currentDate);

			curAValue = sdds.get(i).getAdjClose();
			curBValue = this.getBenchmarkAmount(benchs, currentDate);

			A_Amounts.add(curAValue);
			B_Amounts.add(curBValue);

			if (i == 0) {
				preAValue = curAValue;
				preBValue = curBValue;
			}
			if (i > 0) {
				double AReturn = baseFormulaManager.computeIntervalReturn(curAValue, preAValue);
				double BReturn = baseFormulaManager.computeIntervalReturn(curBValue, preBValue);

				A_Returns.add(AReturn);
				B_Returns.add(BReturn);
				preAValue = curAValue;
				preBValue = curBValue;

				boolean isHoldingDate = LTIDate.isHoldingDate(currentDate);

				if (isHoldingDate || i == sdds.size() - 1 || ((i != sdds.size() - 1) && (sdds.get(i).getDate().getYear() - sdds.get(i + 1).getDate().getYear() == -1))) {

					int index = baseFormulaManager.getYearEndIndex(dateList, currentDate);

					Date startDate = dateList.get(index);

					temp_A_Amounts = A_Amounts.subList(index, i + 1);
					temp_B_Amounts = B_Amounts.subList(index, i + 1);

					temp_A_Returns = A_Returns.subList(index, i);
					temp_B_Returns = B_Returns.subList(index, i);

					yearEnd = currentDate;

					int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, yearEnd);

					long year = currentDate.getYear() + 1900;

					SecurityMPT oldsmpt = this.getSecurityMPT(id, year);
					if (oldsmpt != null && i != sdds.size() - 1) {
						if (update)
							continue;
					}

					SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, yearEnd, interval, se);
					smpt.setAR(smpt.getReturn());

					smpt.setYear(year);

					smpt.setAssetClassID(assetClass);

					smpts.add(smpt);
				}

				// Total mpt of security
				if (i == sdds.size() - 1) {
					Date startDate = dateList.get(0);
					int interval = LTIDate.calculateIntervalIgnoreHolidDay(startDate, currentDate);
					SecurityMPT oldsmpt = this.getSecurityMPT(id, 0);
					SecurityMPT smpt = this.getOneYearMPT(oldsmpt, A_Returns, B_Returns, A_Amounts, startDate, currentDate, interval, se);
					// smpt.setAR(smpt.getReturn());
					smpt.setYear(0L);
					smpt.setAssetClassID(assetClass);
					smpts.add(smpt);
				}

				if (i == sdds.size() - 1) {

					int days = LTIDate.calculateInterval(totalStartDate, currentDate);

					if (days >= TimePara.yearday) {
						int index = baseFormulaManager.getSubListIndex(dateList, currentDate, 1);
						Date startDate = dateList.get(index);
						Date oneYearStartDate = startDate;
						temp_A_Amounts = A_Amounts.subList(index, i + 1);
						temp_B_Amounts = B_Amounts.subList(index, i + 1);

						temp_A_Returns = A_Returns.subList(index, i);
						temp_B_Returns = B_Returns.subList(index, i);
						SecurityMPT oldsmpt = this.getSecurityMPT(id, -1);
						SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, currentDate, TimePara.workingday, se);
						smpt.setYear(-1l);
						smpt.setAssetClassID(assetClass);
						smpts.add(smpt);
					}
					if (days >= TimePara.yearday * 3) {
						int index = baseFormulaManager.getSubListIndex(dateList, currentDate, 3);
						Date startDate = dateList.get(index);
						Date oneYearStartDate = startDate;
						temp_A_Amounts = A_Amounts.subList(index, i + 1);
						temp_B_Amounts = B_Amounts.subList(index, i + 1);

						temp_A_Returns = A_Returns.subList(index, i);
						temp_B_Returns = B_Returns.subList(index, i);

						SecurityMPT oldsmpt = this.getSecurityMPT(id, -3);
						// System.out.println("three year with size of returns: "+
						// (i-index));
						// System.out.println(startDate);
						// System.out.println(currentDate);
						SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, currentDate, TimePara.workingday * 3, se);
						smpt.setYear(-3l);
						smpt.setAssetClassID(assetClass);
						smpts.add(smpt);

					}
					if (days >= TimePara.yearday * 5) {
						int index = baseFormulaManager.getSubListIndex(dateList, currentDate, 5);
						Date startDate = dateList.get(index);
						Date oneYearStartDate = startDate;
						temp_A_Amounts = A_Amounts.subList(index, i + 1);
						temp_B_Amounts = B_Amounts.subList(index, i + 1);

						temp_A_Returns = A_Returns.subList(index, i);
						temp_B_Returns = B_Returns.subList(index, i);

						SecurityMPT oldsmpt = this.getSecurityMPT(id, -5);
						// System.out.println("five year with size of returns: "+
						// (i-index));
						// System.out.println(startDate);
						// System.out.println(currentDate);
						SecurityMPT smpt = this.getOneYearMPT(oldsmpt, temp_A_Returns, temp_B_Returns, temp_A_Amounts, startDate, currentDate, TimePara.workingday * 5, se);

						smpt.setYear(-5l);
						smpt.setAssetClassID(assetClass);
						smpts.add(smpt);

					}
				}
			}
		}
		return smpts;

	}

	private void updateAssetID() {
		List<SecurityMPT> pmpt = new ArrayList<SecurityMPT>();
		List<SecurityMPT> next = new ArrayList<SecurityMPT>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
		pmpt = findByCriteria(detachedCriteria);
		for (int i = 0; i < pmpt.size(); i++) {
			SecurityMPT tmp = pmpt.get(i);
			Security se = get(tmp.getSecurityID());
			long assetID = se.getAssetClass().getID();
			tmp.setAssetClassID(assetID);
			next.add(tmp);
		}
		saveOrUpdateAllSecurityMPT(next);
	}

	/*
	 * public void updateLastYearSecurityMPT(long id,List<SecurityDailyData>
	 * sdds,List<SecurityDailyData> benchs) { BaseFormulaManager
	 * baseFormulaManager = (BaseFormulaManager)
	 * ContextHolder.getInstance().getApplicationContext
	 * ().getBean("baseFormulaManager");
	 * 
	 * SecurityManager securityManager = (SecurityManager)
	 * ContextHolder.getInstance
	 * ().getApplicationContext().getBean("securityManager");
	 * 
	 * Security se = securityManager.get(id);
	 * 
	 * List<Double> A_Amounts = new ArrayList<Double>(); List<Double> B_Amounts
	 * = new ArrayList<Double>();
	 * 
	 * List<Double> A_Returns = new ArrayList<Double>(); List<Double> B_Returns
	 * = new ArrayList<Double>();
	 * 
	 * double preAValue = 0; double curAValue = 0; double preBValue = 0; double
	 * curBValue = 0;
	 * 
	 * List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();
	 * 
	 * Date yearStart = sdds.get(0).getDate(); Date yearEnd;
	 * 
	 * int lastYear = sdds.get(sdds.size()-1).getDate().getYear();
	 * 
	 * boolean last = false;
	 * 
	 * for(int i=0;i<sdds.size();i++) { Date currentDate =
	 * sdds.get(i).getDate();
	 * 
	 * if(!last) { int thisYear = currentDate.getYear();
	 * 
	 * if(thisYear!=lastYear) continue; }
	 * 
	 * curAValue = sdds.get(i).getAdjClose(); curBValue =
	 * this.getBenchmarkAmount(benchs, currentDate);
	 * 
	 * A_Amounts.add(curAValue); B_Amounts.add(curBValue);
	 * 
	 * if(!last){ preAValue = curAValue; preBValue = curBValue; last = true; }
	 * else{ double AReturn =
	 * baseFormulaManager.computeIntervalReturn(curAValue, preAValue); double
	 * BReturn = baseFormulaManager.computeIntervalReturn(curBValue, preBValue);
	 * 
	 * A_Returns.add(AReturn); B_Returns.add(BReturn); preAValue = curAValue;
	 * preBValue = curBValue; }
	 * 
	 * if(LTIDate.isLastNYSETradingDayOfYear(currentDate) || i == sdds.size() -1
	 * || LTIDate.isYearEnd(currentDate)) { yearEnd = currentDate;
	 * 
	 * int interval = LTIDate.calculateIntervalIgnoreHolidDay(yearStart,
	 * yearEnd);
	 * 
	 * SecurityMPT smpt =
	 * this.getOneYearMPT(A_Returns,B_Returns,A_Amounts,yearStart
	 * ,yearEnd,interval,se); smpts.add(smpt);
	 * 
	 * yearStart = yearEnd;
	 * 
	 * A_Amounts.clear(); B_Amounts.clear(); A_Returns.clear();
	 * B_Returns.clear(); } } securityManager.saveOrUpdateAllSecurityMPT(smpts);
	 * }
	 */

	public SecurityMPT getOneYearMPT(SecurityMPT smpt, List<Double> A_Returns, List<Double> B_Returns, List<Double> C_Returns, List<Double> A_Amounts, Date startDate, Date currentDate, int interval, Security se, double preRiskFree, double curRiskFree) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		if (smpt == null)
			smpt = new SecurityMPT();

		double beta = baseFormulaManager.computeBeta(A_Returns, B_Returns);

		double annualizedRiskFree = baseFormulaManager.computeAnnualizedReturn(curRiskFree, preRiskFree, interval);

		double averageRiskFree = baseFormulaManager.computeAverage(C_Returns);

		double averagePortfolioReturn = baseFormulaManager.computeAverage(A_Returns) - averageRiskFree;
		double averageBenchmarkReturn = baseFormulaManager.computeAverage(B_Returns) - averageRiskFree;

		double alpha = baseFormulaManager.computeAlpha(beta, averagePortfolioReturn, averageBenchmarkReturn);

		double RSquared = baseFormulaManager.computeCorrelationCoefficient(A_Returns, B_Returns);
		RSquared = RSquared * RSquared;

		double standardDeviation = baseFormulaManager.computeAnnulizedStandardDeviation(A_Returns, TimeUnit.DAILY);// .computeStandardDeviation(portfolioReturns);

		double AR = baseFormulaManager.computeAnnualizedReturn(A_Amounts, interval);

		double totalReturn = baseFormulaManager.computeTotalReturn(A_Amounts);

		double sharpeRatio = baseFormulaManager.computeRatio(AR, annualizedRiskFree, standardDeviation);

		double treynorRatio = baseFormulaManager.computeRatio(AR, annualizedRiskFree, beta);

		double drawDown = baseFormulaManager.computeDrawDown(A_Amounts);

		if (alpha < LTINumber.INF)
			smpt.setAlpha(alpha);
		if (beta < LTINumber.INF)
			smpt.setBeta(beta);
		if (AR < LTINumber.INF)
			smpt.setAR(AR);
		if (RSquared < LTINumber.INF)
			smpt.setRSquared(RSquared);
		if (standardDeviation < LTINumber.INF)
			smpt.setStandardDeviation(standardDeviation);
		if (sharpeRatio < LTINumber.INF)
			smpt.setSharpeRatio(sharpeRatio);
		if (treynorRatio < LTINumber.INF)
			smpt.setTreynorRatio(treynorRatio);
		if (drawDown < LTINumber.INF)
			smpt.setDrawDown(drawDown);
		if (totalReturn < LTINumber.INF)
			smpt.setReturn(totalReturn);

		smpt.setSecurityID(se.getID());
		smpt.setSecurityName(se.getName());
		smpt.setSymbol(se.getSymbol());
		smpt.setSecurityType(se.getSecurityType());

		return smpt;
	}

	public SecurityMPT getOneYearMPT(SecurityMPT smpt, List<Double> A_Returns, List<Double> B_Returns, List<Double> A_Amounts, Date startDate, Date currentDate, int interval, Security se) {
		BaseFormulaManager baseFormulaManager = (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");

		if (smpt == null)
			smpt = new SecurityMPT();

		double beta = baseFormulaManager.computeBeta(A_Returns, B_Returns);

		double annualizedRiskFree;
		try {
			annualizedRiskFree = baseFormulaManager.getAnnualizedRiskFree(startDate, currentDate);
		} catch (NoPriceException e) {
			annualizedRiskFree = 0;
		}

		double averageRiskFree = baseFormulaManager.getAverageRiskFree(startDate, currentDate);

		double averagePortfolioReturn = baseFormulaManager.computeAverage(A_Returns) - averageRiskFree;
		double averageBenchmarkReturn = baseFormulaManager.computeAverage(B_Returns) - averageRiskFree;

		double alpha = baseFormulaManager.computeAlpha(beta, averagePortfolioReturn, averageBenchmarkReturn);

		double RSquared = baseFormulaManager.computeCorrelationCoefficient(A_Returns, B_Returns);
		RSquared = RSquared * RSquared;

		double standardDeviation = baseFormulaManager.computeAnnulizedStandardDeviation(A_Returns, TimeUnit.DAILY);// .computeStandardDeviation(portfolioReturns);

		// double std = this.computeStandardDeviation(portfolioReturns);

		double AR = baseFormulaManager.computeAnnualizedReturn(A_Amounts, interval);

		double totalReturn = baseFormulaManager.computeTotalReturn(A_Amounts);

		double sharpeRatio = baseFormulaManager.computeRatio(AR, annualizedRiskFree, standardDeviation);

		double treynorRatio = baseFormulaManager.computeRatio(AR, annualizedRiskFree, beta);

		double drawDown = baseFormulaManager.computeDrawDown(A_Amounts);

		if (alpha < LTINumber.INF)
			smpt.setAlpha(alpha);
		if (beta < LTINumber.INF)
			smpt.setBeta(beta);
		if (AR < LTINumber.INF)
			smpt.setAR(AR);
		if (RSquared < LTINumber.INF)
			smpt.setRSquared(RSquared);
		if (standardDeviation < LTINumber.INF)
			smpt.setStandardDeviation(standardDeviation);
		if (sharpeRatio < LTINumber.INF)
			smpt.setSharpeRatio(sharpeRatio);
		if (treynorRatio < LTINumber.INF)
			smpt.setTreynorRatio(treynorRatio);
		if (drawDown < LTINumber.INF)
			smpt.setDrawDown(drawDown);
		if (totalReturn < LTINumber.INF)
			smpt.setReturn(totalReturn);

		smpt.setSecurityID(se.getID());
		smpt.setSecurityName(se.getName());
		smpt.setSymbol(se.getSymbol());
		smpt.setSecurityType(se.getSecurityType());

		return smpt;
	}

	private double getBenchmarkAmount(List<SecurityDailyData> sdds, Date date) {
		double preAmount = 0.0;

		if (sdds == null || sdds.size() == 0)
			return preAmount;

		for (int i = 0; i < sdds.size(); i++) {
			SecurityDailyData sdd = sdds.get(i);

			Date curDate = sdd.getDate();

			// if (curDate.equals(date)) {
			if (LTIDate.equals(curDate, date)) {
				if (sdd.getAdjClose() != null)
					return sdd.getAdjClose();
				else
					return preAmount;
			} else if (curDate.before(date)) {
				try {
					preAmount = sdd.getAdjClose();
				} catch (Exception e) {
				}
			}

			if (curDate.after(date)) {
				break;
			}

		}

		return preAmount;
	}

	public SecurityMPT getSecurityMPT(long securityID, long year) {
		List<SecurityMPT> pmpt = new ArrayList<SecurityMPT>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);

		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));

		detachedCriteria.add(Restrictions.eq("Year", year));

		pmpt = findByCriteria(detachedCriteria);

		if (pmpt.size() == 0)
			return null;

		return pmpt.get(0);
	}

	/**
	 * @author CCD
	 */
	public List<SecurityMPT> getSecurityMPTByYears(long securityID, Long[] years) {
		List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);

		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));

		detachedCriteria.add(Restrictions.in("Year", years));

		detachedCriteria.addOrder(Order.desc("Year"));

		smpts = findByCriteria(detachedCriteria);

		if (smpts.size() == 0)
			return null;
		return smpts;
	}

	/**
	 * @author CCD
	 * @param securityIDs
	 * @param year
	 * @return
	 */
	public List<SecurityMPT> getSecurityMPTBySecurityIDs(List<Long> securityIDs, Long year) {
		List<SecurityMPT> smpts = new ArrayList<SecurityMPT>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);

		detachedCriteria.add(Restrictions.in("SecurityID", securityIDs));

		detachedCriteria.add(Restrictions.eq("Year", year));

		detachedCriteria.add(Restrictions.isNotNull("Alpha"));

		detachedCriteria.add(Restrictions.isNotNull("AR"));

		detachedCriteria.add(Restrictions.isNotNull("TreynorRatio"));

		detachedCriteria.addOrder(Order.asc("SecurityID"));

		smpts = findByCriteria(detachedCriteria);

		if (smpts.size() == 0)
			return null;

		return smpts;
	}

	@Override
	public Date getEndDate(String securityName) {
		Security s = this.get(securityName);
		return this.getEndDate(s.getID());
	}

	@Override
	public Date getStartDate(String securityName) {
		Long securityid = this.get(securityName).getID();
		return this.getStartDate(securityid);
	}

	@Override
	public boolean canGetPrice(String securityName, Date date) {
		Security se = this.get(securityName);
		if (this.getDailydata(se.getID(), date, true) == null)
			return false;
		else
			return true;
	}

	@Override
	public boolean canGetPrice(Long securityID, Date date) {
		if (this.getDailydata(securityID, date, true) == null)
			return false;
		else
			return true;
	}

	@Override
	public SecurityType getSecurityTypeByID(int id) {
		List<SecurityType> st = new ArrayList<SecurityType>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityType.class);
		detachedCriteria.add(Restrictions.eq("ID", id));
		st = findByCriteria(detachedCriteria);
		if (st.size() == 0) {
			return null;
		}
		return st.get(0);
	}

	@Override
	public List<SecurityType> getSecurityTypes() {
		List<SecurityType> sts = new ArrayList<SecurityType>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityType.class);
		sts = findByCriteria(detachedCriteria);
		if (sts.size() == 0) {
			return null;
		} else {
			return sts;
		}
	}

	@Override
	public List<SecurityMPT> getSecurityMPTS(long securityID) {
		List<SecurityMPT> pmpt = new ArrayList<SecurityMPT>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.addOrder(Order.asc("Year"));
		pmpt = findByCriteria(detachedCriteria);
		if (pmpt.size() == 0) {
			return null;
		}
		return pmpt;
	}

	/*
	 * It will be better to get dividends from db directly if there is no cache.
	 * (non-Javadoc)
	 * 
	 * @see com.lti.service.SecurityManager#getAnnualSecurityYeild(long,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public double getAnnualSecurityYeild(long securityID, Date startDate, Date endDate) throws Exception {
		List<SecurityDailyData> dividends = null;
		Double yeild = 0d;
		dividends = getDailyDatas(securityID, startDate, endDate);
		if (dividends != null)
			for (int i = 0; i < dividends.size(); i++) {
				if (dividends.get(i).getDividend() == null || dividends.get(i).getDividend() <= 0.0)
					continue;
				BigDecimal b1 = new BigDecimal(yeild.toString());
				BigDecimal b2 = new BigDecimal(dividends.get(i).getDividend().toString());
				yeild = b2.add(b1).doubleValue();
			}
		BigDecimal c1 = new BigDecimal(yeild.toString());
		BigDecimal c2 = new BigDecimal(this.getLatestDailydata(securityID, endDate).getClose().toString());
		yeild = c1.divide(c2, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		return yeild;

	}

	public double getAnnualGSPCYeild(Date startDate, Date endDate) throws Exception {
		IndicatorManager sm = (IndicatorManager) ContextHolder.getIndicatorManager();
		List<IndicatorDailyData> dividends = null;
		Double yeild = 0d;
		Long securityID = getBySymbol("^GSPC").getID();
		Long indicatorID = sm.get("SPDIVIDEND").getID();
		dividends = sm.getDailydata(indicatorID, new Interval(startDate, endDate));
		if (dividends != null)
			for (int i = 0; i < dividends.size(); i++) {
				if (dividends.get(i).getValue() == null || dividends.get(i).getValue() <= 0.0)
					continue;
				BigDecimal b1 = new BigDecimal(yeild.toString());
				BigDecimal b2 = new BigDecimal(dividends.get(i).getValue().toString());
				yeild = b2.add(b1).doubleValue();
			}
		BigDecimal c1 = new BigDecimal(yeild.toString());
		BigDecimal c2 = new BigDecimal(this.getLatestDailydata(securityID, endDate).getClose().toString());
		yeild = c1.divide(c2, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		return yeild;
	}

	@Override
	public double getAnnualPE(String symbol, Date endDate) throws NoPriceException {
		NoPriceException npe = new NoPriceException();
		Long securityID = getBySymbol(symbol).getID();
		Date validDate = LTIDate.getRecentNYSETradingDay(endDate);
		SecurityDailyData sdd = getLatestDailydata(securityID, validDate);
		if (sdd != null && sdd.getPE() != null) {
			BigDecimal price = new BigDecimal(sdd.getClose().toString());
			BigDecimal earning = new BigDecimal(sdd.getEPS().toString());
			// remain 5 after decimal
			double pe = price.divide(earning, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			return pe;
		} else {
			npe.setSecurityID(securityID);
			npe.setSecurityName(symbol);
			npe.setDate(endDate);
			throw npe;
		}
	}

	@Override
	public Security getBySymbol(String symbol) {
		return get(symbol);
	}

	public List<Security> test() {
		Session session = null;
		try {
			String sql = "SELECT s.* FROM ltisystem.ltisystem_security s left join ltisystem_company c on s.symbol=c.symbol  where sector ='TECHNOLOGY' or sector ='BASIC MATERIALS' or sector ='CONGLOMERATES' or sector ='CONSUMER GOODS' or sector ='HEALTHCARE' or sector ='INDUSTRIAL GOODS' or sector ='SERVICES'";
			session = getHibernateTemplate().getSessionFactory().openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Security.class);
			List<Security> results = query.list();
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

		return null;
	}

	// FinancialStatementManager fsm = (FinancialStatementManager)
	// ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager")

	/* SecurityType:Stock;Valuation:52 weeks return */
	@Override
	public void getAllStockRS(Date logDate, boolean update) {
		/**** part 15: update stockRS ****/
		/*
		 * In case to choose Funds in stocks,we use the stocks in
		 * ltisystem_company to calculate the Relative Strenth
		 */
		List<String> stocks = getAllStocks();
		// String[]arr =
		// {"IBM","AIZ","AMIC","AOC","CNO","EIHI","FPIC","GTS","PICO","SFG","UNM","AMG","AMP","BAM","BEN","BX","CLMS","CNS","ACAS","ACG","ADX","AFB","AINV","AKP","ALD","APX","ARCC","ARK","ASP","AVK","AWF","AYN","BAF","BBF","BBK","BCK","BCV","BDF","BDJ","BDV","BFK","BFO","BFZ","ADF","AGD","ASG","BDT","BEP","BGR","BHY","BLU","BME","BOE","BTA","BTO","BWC","CEF","APB","APF","ASA","BHK","BIE","CEE","CH","CHN","CUBA","DGF","EEA","EFL","EMD","EMF","FAX","GCH","GF","GIM","GRR","IFN","IIF","IRL","AACC","ACF","ADS","ADVNB","AEA","AGM","AXP","BKCC","CACC","CCRT","AAV","AIB","BAP","BCS","BLX","BMO","CS","GGAL","IBN","LYG","MFG","MTU","NBG","SAN","SBP","STD","WBK","AFSI","AJG","BRO","CHSI","CISG","CRVL","DCAP","EHTH","MHLD","MMC","NFP","UAHC","WSH","AMPL","AMTD","BGCP","COWN","ETFC","FCSX","IBKR","JMP","KBW","LTS","MF","MKTX","NMR","OXPS","QCC","RODM","SCHW","BLK","BPSG","DHIL","FBR","GBL","GFIG","GHL","ITG","JEF","LAB","LM","NITE","OPY","PLCC","RJF","SF","SIEB","SWS","WDR","AAME","AEL","AGO","ANAT","AXA","CIA","DFG","FAC","FFG","GNW","IHC","IPCR","KCLI","LFC","BAC","BK","BNS","C","CM","JPM","KEY","AFN","ANH","ATAX","BRT","CMO","CRE","DFR","DRL","DX","ELC","FNM","FRE","HCM","HF","HTS","IOT","MFA","NLY","NYMT","RWT","SNFCA","UPFC","ACAP","ACE","ACGL","AFFM","AFG","AGII","AHL","AIG","ALL","AMPH","AMSF","ASI","AWH","AXS","BWINB","ADC","AKR","ALX","APSA","ARL","BPO","CBG","CHLN","CIM","CT","CXW","DFT","EJ","FOR","FXRE","GBE","GKK","ABCB","ABVA","ACBA","AMNB","ANCX","ANNB","ASFN","BAYN","BBT","BCAR","BKOR","BKSC","BNCN","BOFL","BOMK","BOVA","CAPE","CART","CBAN","CBKN","CCBG","CFFC","CFFI","CHCO","CLBH","AMFI","ASBC","BUSE","CBC","CHEV","CHFC","CITZ","CMA","CNAF","CORS","CRBC","CZWI","DEAR","FBIZ","FBMI","FFBC","FFNM","FITB","FMBI","FMER","FPFC","FRME","GABC","HBAN","IBCP","ALLB","ALNC","AROW","ASRV","ATLO","BARI","BCBP","BDGE","BERK","BHB","BMTC","BNCL","BNV","BPFH","CAC","CBNJ","CBU","CCBP","CCNE","CEBK","CIZN","CJBK","CMSB","CNBC","CNBKA","AANB","AMRB","AWBC","BBNK","BMRC","BOCH","BOH","BSRR","CACB","CASB","CATY","CBBO","CBON","CCBD","CFNB","CPF","CTBK","CVBF","CVCY","CVLL","CWBC","CWLZ","CYN","DNBF","EWBC","ACFC","APAB","AUBN","BFNB","BKBK","BTFG","BXS","CADE","CFNL","CNB","CSFL","CSNT","CTBI","CZFC","EVBS","FABK","FBMS","FBSS","FFKT","FFKY","FHN","FMFC","FNB","BANF","BOKF","CASS","CBSH","CFR","COBZ","CSHB","EBTX","EFSC","FCCO","FFIN","FFNW","FFSX","FSNM","GFED","GFG","GSBC","HTLF","IBOC","LARK","LBCP","MCBI","MFNC","MOFG","OSBK","ABBC","ABCW","ABNJ","AF","ASBI","ATBC","BANR","BBX","BCSB","BFBC","BFED","BFIN","BFSB","BHLB","BKJ","BKMU","ABK","FAF","FNF","ITIC","MBI","MTG","ORI","PMI","RAMR","RDN","STC","SUR","TGIC","AEE","ALE","AVA","CHG","CMS","CNP","DPL","ED","EXC","FPU","LNT","MGEE","MIR","NI","NU","NVE","NWE","PCG","PEG","PNM","ACPW","AEP","AES","APWR","AYE","BCON","BIP","BKH","CEG","CNL","CPN","CPL","CWCO","ENI","EOC","HNP","KEP","MGS","SBS","TGS","VE","AGL","APU","ATO","CLNE","CPK","DGAS","EGN","EPB","EQT","EWST","GAS","LG","NFG","NGG","NJR","NWN","OKE","PNY","ARTNA","AWK","AWR","BWTR","CTWS","CWT","HOO","MSEX","PNNW","SJW","SWWC","WTR","YORW","PVD","AXC","HDS","ALUS","AMV","AYA","AWSR.OB","MABAA.OB","AMTC","PEX","APRB.OB","AMEH.OB","ASCQ.OB","CIO","ASPO.OB","AXG","AUCAF.OB","BTE","BSOI.OB","BHCG.OB","BHWF.OB","BPW","CAEL.OB","CMKT.OB","CLA","CYSU.OB","CLNH.OB","CTCJ.OB","HOL","CIIC","CHNQE.OB","CNSJ.OB","GLA","CME","RFI","CLXS.OB","COBK","BUS","CWBS","BTC","CODI","CCVR.OB","CVA","CRT","CRWE.OB","DCDC.OB","DOM","DMLP","DEGH.OB","DYER.OB","EBIG.OB","NGT","EDRG.OB","ESA","ESGI.OB","EST","FCIC.OB","FCVA","FSTF","FMNG.OB","FRXT.OB","FIGI","FFI","GFN","GCNV.OB","GGHO.OB","GHQ","GQN","GHC","GISV.OB","GSL","GPH","GS","GEYO.OB","GGA","GLFO.OB","HTE","HHDG.OB","HEK","HAV","TOH","HBRF.OB","HIA","HGT","HYDQ.OB","IDI","ICGP.OB","IAN","IAQG.OB","ICH","IVOI.OB","JKAK.OB","KHA","LTTV.OB","LPTC.OB","LTHO.OB","LIA","LPHI","LTVL.OB","LCHL.OB","LIXT.OB","LRT","MNDL.OB","MMPA.OB","MARPS","MAQC.OB","MHLI.OB","MBH","TVH","MMDA.OB","MRHD.OB","MOSH.OB","MSB","MCMV.OB","SMCG","MMTRS.OB","MS","NDAQ","NVDF.OB","NNA","NBMF.OB","NEWT","NHR","NRT","NORH.OB","NSAQ.OB","NAQ","NYX","OOLN.OB","OKN","ONCE.OB","NLX","PWE","PGID.OB","PBT","PIP","PGRI.OB","PAX","PVX","RAME","RAQP.OB","RTRO.OB","RTTE.OB","RBCF.OB","SBR","SFE","STJC.OB","SJT","SFEF.OB","MEJ","FYR","SHIP","HLD","SOSV.OB","SACQ.OB","SIBE.OB","SIGN.OB","SVGP.OB","SOON.OB","SOPK.OB","DSP","HMR","SLAW.OB","SOC","STQN.OB","OOO","FGF","TNF","TCXB.OB","TELOZ","TPL","THLM.OB","OZOM.OB","TIRTZ.OB","TMI","TXIC","TRU","TAMG.OB","TTSP.OB","TTII.OB","TGY","TISG.OB","TUX","TCW","TRBD.OB","UHFI.OB","UBNK","URX","UWBK","VTG","VRY","VPFG","VIIC.OB","VYTC.OB","WAL","WSMO.OB","WASM.OB","WHLM.OB","WTU","XDRC.OB","ZAP","ZXSI.OB","TWFH.OB","TTY","EGHA.OB","BBV","BBD","BCH","BMA","CIB","BFR","CBIN","BCA","EUBK","FBP","FBAK.OB","HDB","BPOP","SHG","IRE","WF","AGU","AVD","CF","CGA","CMP","COIN","EDEN","KMGB","MON","MOS","POT","SYT","TRA","TNH","SMG","AA","ACH","CENX","KALU","APD","ASH","CE","DOW","WIRE","FCX","AEM","ANV","AU","AGT","AZK","ABX","CDY","CGR","BVN","KRY","DROOY","EGO","EGI","XRA","GRS","GFI","GRZ","GG","GSS","HMY","IAG","THM","KGN","KBX","KGC","MDW","MGH","NSU","NEM","NAK","NXG","NG","PMU","RIC","RGLD","RBY","SA","XPL","TRE","UXG","VGZ","WGW","AUY","AXAS","AEZ","APC","APA","APAGF","ATPG","AOG","BRN","BRY","BDCO","KAZ","BEXP","COG","CPE","CNQ","CRZO","CHK","SNP","XEC","CKX","CWEI","CEO","CRK","MCF","CRED","XTEX","DPTR","DNR","DVN","DBLE","EPEX","EEQ","ECA","EAC","END","EPD","EOG","EPM","XCO","FPP","FST","GSX","GPR","GEOI","GMXR","GDP","GTE","GPOR","HNR","HKN","HUSA","ISRL","LINE","LEI","MPET","MMLP","MMR","TMR","MXC","NFX","NBL","OXY","PHX","PLLL","PVA","HK","PQ","PXD","PXP","PNRG","PDO","STR","KWK","RRC","ROSE","ROYL","SWN","SM","SGY","SU","SFY","TLM","TRGL","TGA","TXCO","UPL","EGY","WMZ","XTO","AXU","ATI","ARLP","AAU","ANR","ANO","ACI","AZC","BHP","BBL","BW","CCJ","CHNR","SHZ","CNX","ETQ","CXZ","DNN","BOOM","FCL","FRG","GMO","ZINC","ICO","IVN","JRCC","MEE","MMG","MFN","NANX","NCOC","NRP","PZG","PCX","BTU","PVG","PVR","PLG","PLM","QMM","RTI","SWC","TCK","TC","TLR","TIE","URG","URZ","UEC","URRE","USEG","USU","WLT","WLB","YZC","BP","CVX","E","XOM","PZE","PTR","REP","RDS-B","TOT","AHGP","CHBT","HWD","ROY","IPI","MDM","NGD","BQI","AREX","ARD","ATLS","ATN","AHD","ATW","BBG","BPZ","BBEP","BRNC","SNG","CFW","LNG","CXG","CMZ","CXO","CEP","CLR","QBC","DEJ","DO","DNE","EC","ENP","ERF","ESV","ENT","EVEP","FXEN","GST","GGR","GMET","GLRP","GIFI","HP","HERO","HPGP","HLND","HDY","KEG","KOG","LGCY","ME","MWE","MVO","NBR","NTG","GBR","NGAS","NE","NOG","PKD","PTEN","PGH","PRC","PED","PBR","PINN","PDC","PSE","PDE","QELP","QRCP","KGS","REXX","RDC","SSN","SD","STXX","STO","TGC","TEC","RIG","TIV","UDRL","UNT","VNR","VQ","WTI","WLL","ALY","BHI","BAS","BJS","BOLT","WEL","DVR","CAM","CRR","CGV","CPX","CLB","DWSN","DRQ","EXXI","EXH","EXLP","FTI","GOK","GLBL","GLF","HAL","HLX","LUFK","NOV","NGS","NR","NOA","OII","OIS","OMNI","PDRT","PDS","RES","SLB","SII","SPN","SWSI","TESO","TTI","TGE","TRMA","WRES","WFT","WG","WH","ZN","APL","BGH","BPL","CQP","CPNO","DPM","DEP","EP","EEP","ENB","ETE","ETP","HEP","KMP","KMR","MGG","MMP","NS","NSH","OKS","PAA","RGNC","SE","SEP","SXL","NGLS","TCLP","TPP","TLP","WES","WMB","ALJ","ARSD","BPT","CLMT","XTXI","CVI","DK","EROC","EPE","FTO","HES","HOC","IMO","IOC","MUR","NBF","PCZ","PETD","SSL","SUF","SUN","SYNM","TSO","VLO","WNR","YPF","CDE","EXK","HL","IMR","MVG","MGN","SSRI","SLW","SVM","ADES","ALTI","APFC","BIOF","BAK","CBT","CYAN","CYT","FOE","FSI","FTK","GPRE","GU","FUL","IOSP","KRO","LZ","MACE","NGBF","NOEC","NEU","ODC","OMG","OMN","PEIX","PENX","PPO","KWR","SXT","SDTH","SIAL","SOA","SYMX","VRNM","GRA","WDFC","WLK","WPZ","AKS","MT","TPUT","CRS","CPSL","CLF","CMC","SID","DSUP","FRD","GSI","GNA","GGB","ROCK","GNI","SIM","HSC","HAYN","IIIN","MEA","NWPX","NUE","ZEUS","PKX","RTP","SCHN","STLD","SUTR","SYNL","TS","TX","USAP","WOR","ALB","AWC","ARJ","CCC","SQM","CDTI","GGC","HUN","ICOC","IPHS","IFF","LNDC","LDL","MTX","NLC","NL","NCX","OLN","POL","PX","ROC","SHI","TORM","VHI","ASFI","CHKE","CBE","CR","DHR","GY","GE","PPG","RTK","TXT","UTX","MMM","ARCI","GAI","HELE","HDSN","IRBT","LII","NPK","SPW","WHR","CVR","DAI","F","GM","HMC","SORL","TTM","TM","AXL","ARGN","ARM","ALV","BWA","CAAS","CLC","CVGI","CSLR","ATC","DAN","DORM","FDML","FSYS","GETI","GNTX","HAYZ","JCI","LEA","MLR","MOD","MPAA","NOBL","PLI","QTWW","SMP","SRI","STRT","SUP","TEN","TWI","TKS","TRW","WBC","WMCO","WATG","WSCI","SAM","ABV","HOOK","FMX","TAP","COKE","CCE","KOF","CCH","COT","DPS","AKO-A","HANS","JSDA","LBIX","FIZZ","PBG","PAS","REED","KO","BF-B","ROX","CEDC","STZ","DEO","WVVI","CATM","CSTR","DBD","EFOI","FEP","MLHR","HNI","HYC","KBALB","KNL","LYTS","OPMR","PTC","PBI","SCS","PAY","VIRC","XRX","MO","BTI","LO","PM","RAI","STSI","VGR","CHD","CLX","DAR","ECL","OBCI","SCL","ZEP","CBY","CZZ","HSY","IPSU","RMCF","TR","DF","LWAY","SYUT","TOF","WBD","AVID","CSCD","COBR","DTSI","MSN","HAR","HUB-B","ICOP","PHG","KOSS","NIV","PC","RWC","ROFO","SNE","TMS","UEIC","GRO","ALCO","ANDE","ADM","BG","CALM","CVGW","CQB","FDP","SEED","AFCE","HNZ","KFT","LANC","AYI","BSET","ETH","FLXS","FO","FBN","HOFT","LZB","LEG","NTZ","ZZ","SCSS","STLY","TPX","USHS","XNN","BLT","JAH","LBY","LCUT","NWL","BCPC","BRID","CGL-A","HRL","IBA","PDA","SDA","SAFM","SEB","SFD","TSN","HOGS","ABD","ACU","ATX","EBF","FC","OMX","SR","TFCO","BLL","BMS","BWY","CCK","GPK","GEF","ITP","MWV","MPAC","NTIC","OI","PKG","PTV","SEE","BEST","SLGN","TUP","UFPT","VTO","ABH","ARA","AVY","BZ","BKI","CSAR","CVO","CLW","UFS","AIP","IAX","IP","KAI","KPPC","MERC","NSHA","NP","TIS","GLT","PCH","RKT","SPP","SWM","SON","TIN","VRS","VCP","WPP","ACV","AVP","BARE","BTH","CAW","CL","RDEN","EL","IPAR","KMB","BHIP","PARL","FACE","PG","REV","TSC","SUMR","UG","CAJ","CHYR","EK","IKNX","IMAX","NRVN","XRIT","FEED","AIPC","BGS","CPB","JVA","CAG","CPO","FZN","DLM","DMND","FARM","FLO","GIS","GLDC","GMCR","GMK","JJSF","JMBA","JBSS","K","KTEC","LNCE","MLP","MKC","MJN","MGPI","PSTA","NWD","OME","OFI","PEET","PEP","RAH","FRZ","SLE","SENEA","SMBL","STKL","TSTY","HAIN","SNAK","SJM","THS","UN","UL","FOOD","TYM","FOSL","FUQI","JADE","MOV","ORNG","SMD","LVB","EAG","ACAT","HOG","MPX","PII","THO","WGO","SHLM","AEPI","ABL","AMTY","ATR","CSL","CTB","CMT","CTIB","DSWL","FHC","FORD","FFHL","GT","MBLX","MYE","QUIX","ROG","SHLO","SEH","TG","ADGF","ALDA","ELY","CYBI","ESCA","JOUT","NLS","POOL","RGR","APP","BEBE","CRI","COLM","CRWS","DLA","EBHI","EVK","FOH","GIII","GIL","HBI","JOEZ","LIZ","LULU","MFB","OXM","PERY","PVH","RL","ZQK","SPOR","SGC","TAGS","TRLG","UA","VFC","WRC","BWS","COH","CROX","DECK","HLYS","ICON","KSWS","KCP","BOOT","NKE","PXG","DFZ","RCKY","SKX","SHOO","TBAC","TLF","TBL","VLCM","WEYS","WWW","AOI","UVV","APII","FNET","GMTC","GPIC","HAS","JAKK","LF","MCZ","MAT","RCRC","RUS","FSS","FRPT","KNDI","NAV","OSK","PCAR","SPAR","STS","WNC","ASTM","ABII","ACAD","ACHN","ACOR","AEN","ADLS","ANX","AEZS","AFFY","ALNY","ALSE","ALTU","AOB","AMGN","FOLD","AMLN","ANDS","ANSV","ANIK","AGEN","ABIO","ARIA","ATHX","BCRX","BDSI","BIIB","BMRN","BNVI","BPUR","BPAX","BSTC","CADX","CBM","CSII","CXM","CVM","CELG","CEGE","CTIC","CERS","CRL","CHTP","CLDA","CBMX","CRXX","CGEN","CORT","CRTX","CGRB","CRGN","CRIS","CVTX","CYCC","CYPB","CYTK","CYTX","CYTR","DARA","DNDN","DSCO","DYAX","EBS","ENMD","ENZN","EPIX","EURX","EXAS","EXEL","FACT","GENR","GENE","GNVC","GENZ","GILD","GTCB","GTXI","HALO","HNAB","HEB","HSKA","HEPH","HGSI","IDIX","IDRA","IDMI","IG","ILMN","IMM","IMGN","INCY","INHX","INSM","ITMN","JAZZ","LJPC","LXRX","LIFE","LGND","MNKD","MATK","MRNA","MEDX","MNOV","MRX","MDVN","MBRX","MITI","MIPI","MNTA","NABI","CUR","NTII","NBIX","NLTX","NFLD","NVAX","NPSP","NXXI","OMPI","ONTY","ONXX","OPXA","OPTR","CAPS","OSIP","OSIR","OXGN","PDLI","PPHM","PSTI","PARD","PGNX","PLX","QGEN","QLTI","QCOR","REGN","RGEN","RPRX","RNN","RXII","SGMO","SVNT","SGEN","SNT","SQNM","SVA","STEM","SNSS","TRGT","TGEN","TECH","THRX","THLD","TCM","TSPT","TTHI","TRMS","TRBN","VNDA","VIAP","VICL","VPHM","XNPT","XOMA","XTLB","ZIOP","ZGEN","SSRX","ABAX","AKRX","AMAG","ABMC","APPY","AVRX","CLDX","EPCT","ICGN","IDXX","ICCC","BLUD","IMMU","INFI","ILI","ISTA","VIVO","MGRM","MYGN","NEOG","NYMX","OGXI","ORCH","ORXE","PTN","PBIO","QDEL","RGDX","ROSG","DDD","SRDX","SNTA","TPTX","TRIB","ACUR","ALKS","BVF","CBRX","DCTH","ELN","EMIS","FLML","GNBT","HSP","ISV","KV-A","MTXX","NKTR","NVD","NOVN","PPCO","PETS","QGLY","ULU","ABT","ACET","APPA","AZN","AVGN","BMY","CPEX","LLY","EVTC","GORX","GSK","IPXL","JAV","JNJ","KERX","MRK","NVS","NVGN","OSCI","PFE","RIGL","SNY","SGP","SPPI","WX","WYE","ADLR","ALXN","ALXA","AGN","ALTH","AMRN","RDEA","ARNA","ARQL","ARYX","AVNR","AVII","BIOD","CRME","CEPH","CHTT","COR","CBST","DEPO","RDY","DRRX","DUSA","DVAX","ELI","ENDP","FRX","GENT","GERN","ISPH","ISIS","KNDL","KG","LCI","MAPP","MSHL","MDCO","MBRK","NRGN","NGSX","NEXM","NTMD","NBY","NVO","OPK","OREX","PTIE","PCYC","VRUS","PXSL","POZN","PRAN","RGN","RELV","SNTS","SCLN","SEPR","SIGA","SOMX","SCMP","SUPG","TELK","TEVA","TPI","UTHR","VRX","VRTX","WCRX","YMI","AUXL","KUN","CSKI","DDSS","MTEX","MDNU","NAII","NTY","NUTR","PRGO","PBH","SLXP","WNI","TBV","USNA","CPD","CPRX","HLCS","HITK","ILE","MYL","PRX","SRLS","WPI","AET","AGP","CNC","CI","CVH","HNT","HS","HUM","MGLN","MDF","MOH","UNH","UAM","WCG","WLP","AHCI","AFAM","AMED","KAD","CHE","GTIV","LHCG","LNCR","PHC","AMSG","CYH","DYII","HMA","LPNT","MDTH","RHB","SSY","THC","UHS","ADK","AVCA","ALC","BKD","CSU","ESC","FVE","KND","NHC","ODSY","SKH","SUNH","SRZ","ENSG","ARAY","ALGN","AHPI","APT","AMAC","AMMD","HRT","ASPM","ATSI","BFRM","BSDM","CLZR","CDIC","CLSN","CHIO","CNMD","CRY","CUTR","CYBX","CYNO","DRAD","DYNT","EDAP","EW","LZR","ENDO","ETRM","ESMC","EXAC","FONR","GIVN","GB","HNSN","HTRN","HOLX","ISRG","IVC","IRIX","IVVI","KCI","LAKE","GAIT","MAKO","MASI","MDCI","MDT","MEND","MSA","MSON","BABY","NSTR","NXTM","OFIX","PMTI","PHMD","RMD","RMTI","RTIX","SHMR","SIRO","SNN","SLTM","SMTS","SNCI","SPAN","STRC","STJ","STXS","STE","SMA","SURG","ELOS","SYNO","SPNC","TGX","KOOL","TOMO","ULGX","VAR","VSCI","VNUS","VOLC","WHRT","WMGI","XCR","ZMH","ZOLL","ABMD","ASB","ACL","ATEC","ADL","ANGN","ANGO","AIS","ARTL","ATRC","ATRI","BAX","BDX","BLTI","BMTI","BSMD","BSX","BVX","CMN","CSCX","CRDC","BEAT","CASM","CMED","CPTS","COV","BCR","DXR","XRAY","DXCM","MELA","ELGX","EVVV","HAE","HTWR","HRC","HDIX","IFLO","ICUI","INO","PODD","IART","ISR","IVD","KNSY","LMAT","LMNX","MMSI","MLAB","MR","NSPH","NURO","NMTI","NCST","NUVA","TEAR","OCLS","OSUR","VITA","OSTE","PKI","PMII","PDEX","RVP","ROCM","SENO","SONO","SDIX","SYK","COO","THOR","TSON","UPI","UTMD","VASC","VSGN","VVUS","WST","XTNT","YDNT","ZILA","AIQ","ARRY","BITI","BRLI","ENZ","ERES","GHDX","GXDX","LH","MTOX","NADX","PPDI","PMD","DGX","RDNT","SPEX","INMD","LCAV","PZZ","TRCR","ALLI","ASGR","ANCI","AMS","BIOS","CNU","DVA","DCAI","GRMH","HGR","HLS","HWAY","EAR","HH","HYTM","IPCM","MDZ","MD","NBS","NHWK","VETS","PRSC","PSYS","QGP","TLCV","USPH","VRAD","BA","RTN","AIR","AIM","AVAV","ATK","ADG","ATRO","SPAB","BEAV","BZC","CGT","CVU","CW","DCO","EDAC","ESLT","ERJ","ERI","ESL","GD","GR","HWK","HEI","HXL","HSR","HON","KAMN","KRSL","LMIA","LMT","MOG-A","NOC","ORB","COL","SIF","SWHC","SPR","TASR","TDY","TOD","TDG","TGI","CX","CRH","EXP","JHX","RMX","TXI","RMIX","ATU","ADEP","AP","BLD","BNSO","BGG","BWEN","BC","CYD","CFX","CMI","CVV","DOV","DRC","ETC","FLDR","FLS","GDI","GRB","GRC","GGG","IEX","ITW","IR","IIN","ITT","JBT","KUB","MNTX","MPR","MIDD","NDSN","NVMI","PLL","PMFG","PRST","RBN","SHS","SHFL","TAYD","TFX","TNC","TWIN","WUHN","AG","ALG","ARTW","ASTE","BUCY","CAE","CAT","CNH","CMCO","DE","GENC","HIT","JOYG","LNN","MTW","NC","PTG","TEX","AAON","ACO","AMN","APOG","AWI","CUO","DW","FAST","GFF","HW","IPII","LXU","MLM","MDU","NCS","OC","PGTI","QEPC","NX","RSOL","RPM","TATTF","TECUA","USLM","USG","VAL","VMC","CBI","CAEI","FIX","EME","IESC","KSW","PEC","PWR","SPWRA","AGX","DY","ICA","FLR","FWLT","GV","GVA","GLDD","IGC","INSU","LAYN","MTZ","MTRX","MDR","MYRG","OMGI","PCR","PLPC","PCYO","SERV","STRL","ABB","ABAT","AIMC","AETI","AME","AOS","ARTX","AZZ","BEZ","BLDP","BDC","CPST","CBAK","CCIX","DAKT","ETN","HEV","ENR","ENS","XIDE","FELE","FCEL","FSIN","BGC","GTI","HRBN","HOLI","HPJ","HYGS","LFUS","MAG","MDTL","NJ","PLUG","POWL","RZ","RBC","ROK","SVT","TRCI","TNB","TIII","TLGD","ULBI","UQM","VLNC","WGOV","ZRBA","ZOLT","ASEI","B","CRDN","CCF","CFSG","CIR","EGT","EMR","NPO","HEES","MWA","NGA","OFLX","PH","PNR","ROP","HEAT","SXI","SNHY","TTES","TRS","AFP","WTS","WXCO","AERT","AMWD","DEL","JCTCF","KOP","LUK","LPX","MAS","MXM","PATK","POPE","TWP","UFPI","WY","CIX","FLOW","HDNG","KDN","KMT","NNBR","ROLL","SWK","THMD","TKR","CAV","CVCO","CHB","NOBH","PHHM","SKY","GTLS","GHM","HIHO","LDSH","MATW","MINI","MLI","PKOH","PCP","RS","X","VMI","CECE","DCI","ERII","CLWT","FTEK","MFRI","PURE","WPRT","AVTR","BZH","BHS","CTX","CHCI","DHI","GFA","HOV","KBH","LEN","MHO","MDC","MTH","NVR","OHB","PHM","RYL","SPF","TOL","XIN","AERO","EML","LECO","SCX","PFIN","SSD","SNA","BDK","TTC","AIN","CFI","DII","HWG","IFSIA","MHK","DXYN","UFI","XRM","ECOL","AWX","CWST","CLH","GNH","HCCI","PESI","RSG","SGR","SRCL","WCN","WMI","WSII","WCAA","FMCN","IPG","MWW","OMC","VCLK","VISN","XSEL","AIRT","ATSG","CHRW","EXPD","FDX","HUBG","PACR","SINO","UPS","UTIW","VEXP","AIRM","AAWW","BRS","PAC","ASR","LIMC","PHII","ANF","ARO","AEO","ANN","BKRS","BKE","CACH","CMRG","CTR","CHIC","CHRS","CHS","CBK","CTRN","PSS","DEST","DBRN","DSW","FINL","FL","GPS","GCO","GES","GYMB","HOTT","JCG","JNY","JOSB","LTD","MW","NWY","JWN","PSUN","RVI","ROST","SCVL","SSI","SMRT","SYMS","PLCE","TLB","TWB","URBN","WTSLA","ZUMZ","AAP","AZO","ORLY","PBY","PRTS","CRV","GPC","LKQX","AE","ANW","CAS","GLP","HWKN","FSTR","MIC","RVEP","FUEL","INT","BBGI","CJR","CXR","CMLS","EMMS","ETM","RC","ROIAK","RGCI","SGA","SALM","SIRI","SBSA","BLC","CBS","CETV","CTCM","EVC","FSCI","GTN","TV","HTV","TVL","NXST","NTN","PLA","RCI","SBGI","WPTE","AKNS","BECN","BXC","IBI","AAC","ABM","CIDM","AM","AMIN","APEI","ARP","APAC","ARBX","ATAC","ATHN","BNE","BRC","BR","CDZI","CBZ","CHMP","CTAS","CLCT","CTT","CPSI","SCOR","CGX","CSGP","CRD-B","CSS","CYBS","DLX","DGIT","DCP","ES","ENOC","EEFT","EXLS","FIC","FIS","FADV","VIFL","GKSR","GA","GPN","GRIF","HCSG","HPY","HMSY","HQS","IMNY","IAO","ICTG","INWK","INOC","NPLA","INSW","ICE","INET","IILG","IRM","KONG","LPS","LIME","LLNW","LPSN","LMLP","MFW","MA","MELI","MGI","MXB","LABL","UEPS","ONT","OWW","ORS","PDII","PFSW","PFWD","TUTR","PRAA","POWR","PRGX","RRD","REIS","REFR","DINE","RBA","ROL","SVVS","SDBT","SGRP","SYKE","SNX","TISI","TTGT","TTEC","TRI","TKTM","TSS","UNF","USAT","UTK","IVA","VCGH","PGV","VVI","V","VPRT","WINA","WNS","ZANE","ATV","AMZN","BIDZ","BFLY","DLIA","EBAY","GAIA","GSIC","HSNI","IACI","NSIT","LINTA","LIVE","OSTK","PCCC","MALL","RBI","STMP","SYX","VVTV","ASCMA","BSY","CVC","CABL","CMCSA","CRWN","DISCA","DISH","LMDIA","LBTYA","LCAPA","LNET","MCCC","SNI","SJR","SPDE","TWC","TIVO","VIA-B","VMED","AVSR","ENPT","GTSI","IM","INXI","NAVR","PRLS","PMRY","SCSC","TECD","WSTG","CTRP","DCU","EMS","FGXI","TUC","MNRO","NTRI","RURL","SFLY","LOV","STAN","KDE","BONT","CPWM","DDS","JCP","KSS","M","SKS","SHLD","TJX","BIG","BJ","COST","DLTR","DUCK","FDO","FRED","PSMT","TGT","TUES","WMT","NDN","NPD","CVS","MHS","OCR","PMC","RAD","WAG","ABC","BJGP","CAH","SAB","HLF","MCK","NUS","EPAX","AMCE","APOL","ATAI","CPLA","CECO","DL","CAST","CEDU","MBA","COCO","DV","GPX","LOPE","ESI","LRN","LTRE","LINC","MMS","EDU","NLCI","REVU","STRA","UTI","BBY","CONN","GME","HGG","RSH","RSC","AEY","AXE","ARW","VOXX","AVT","CELL","CRFT","HWCC","IFON","ISIG","ITRN","JACO","NUHC","ORBT","RADA","RELL","TAIT","TESS","UPG","UUU","GWW","WSO","WCC","CKXE","GET","NWSA","TWX","DIS","DIT","CORE","WILC","NAFC","SPTN","SYY","UNFI","VPS","BYI","CPHC","CHDN","DDE","LACO","MNTG","MGAM","PNK","PTEK","NCTY","UBET","CCL","CUK","FUN","LYV","OUTD","PRXI","RCL","SIX","ORCD","TIXC","WMG","WWE","ARDNA","BSI","CASY","CBD","DEG","DDRX","IMKTA","KR","PTRY","RDK","SWY","SVU","SUSS","GAP","VLGEA","WMK","WFMI","WINN","BBBY","DWRI","HVT","JEN","KIRK","PIR","WSM","BLDR","LOW","LL","HD","ARG","AIT","CFK","DXPE","HRSH","KHD","LAWS","MSM","SNEN","BMJ","NILE","DGC","SIG","TIF","ZLC","CHH","HMIN","IHG","MCS","MAR","MHGC","OEH","RLH","SNSTA","HOT","WYN","AMR","CEA","ZNH","CAL","DAL","TAM","UAUA","LCC","ACN","ADPI","BDMS","CDII","CFS","EXBD","CRAI","DTPI","DUF","ELOY","EXPO","ESRX","FCN","IT","G","GLOI","HSII","HEW","HIL","HURN","ICFI","IDSA","III","VTIV","TMNG","MHH","BKR","NOVA","RMKR","RECN","HCKT","TGIS","TRR","WW","AMCN","ALOY","CCO","CTCT","HHS","HOLL","IUSA","KOW","LAMR","MCHX","MDCA","CMKG","NCMI","SGK","SORC","VCI","AHII","CHDX","HSIC","MWIV","NYER","OMI","PDCO","PSSI","STAA","CKEC","CNK","DWA","DISK","LGF","MVL","NOOF","PAE","PTSX","RDI","RGC","RENT","RHIE","SAPX","BBI","HAST","NFLX","TWMC","AMIE","CSV","CPY","LONG","HRB","FIT","HI","JTX","LGBT","PPD","RGS","RSCR","ROAC","SCI","STNR","STEI","STON","ULTA","WOOF","WTW","WU","CRRC","JW-A","NED","PSO","PEDH","SCHL","MHP","AHC","DJCO","GCI","JRN","LEE","MEG","SSP","MNI","NYT","WPO","DM","MSO","MDP","PRM","PRVT","RUK","SPRO","ARII","BNI","CNI","CP","CSX","RAIL","GWR","GBX","GSH","KSU","NSC","PRPX","PWX","TRN","UNP","WAB","AAI","ALK","ALGT","CPA","XJT","GOL","GIA","HA","JBLU","LFL","MESA","PNCL","RJET","SKYW","LUV","RNT","AER","ACY","AYR","UHAL","CAR","FLY","CAP","DTG","ELRC","GMT","GLS","HTZ","MRLN","MGRC","MIND","RCII","RRR","R","TAL","TGH","URI","WSC","WLFC","ACTG","ABCO","AMRI","ARB","ARWR","BASI","CBLI","CBTE","CVD","GTF","DCGN","ENCO","FORR","GPRO","ICLR","LDR","XPRT","LSR","LUNA","MAXY","NRCI","NTSC","OMEX","PRXL","SNMX","SPIR","SMMX","ASCA","BYD","CNTY","NYNY","FLL","WOLF","ISLE","LVS","LGN","MPEL","MGM","MCRI","UWN","PENN","RIV","MTN","WYNN","ARKR","BNHNA","BJRI","BOBE","EAT","BWLD","BKC","CPKI","TAST","CEC","CMG","CKR","COSI","CBRL","DRI","DENN","DIN","DPZ","BAGL","DAVE","FRS","GTIM","GCFB","JAX","JACK","KONA","KKD","LNY","LUB","MSSR","MCD","CASA","MRT","NATH","CHUX","PZZA","PFCB","PZZI","RRGB","RICK","RUBO","RT","RUTH","SONC","STRZ","SNS","TXRH","CAKE","WEN","WEST","YUM","BCO","CFL","CKP","CSR","MOC","CRN","DMC","GEO","HBE","HTC","ID","LOJN","MAGS","NSSC","PNTR","PONE","TRIS","AXB","ACLI","RAMS","BHO","CPLP","DAC","DHT","DSX","DRYS","EGLE","ESEA","EXM","FREE","FRO","GNK","GMR","GLNG","TMM","HRZ","HOS","ISH","KSP","KEX","VLCCF","NM","NMM","NAT","OCNF","ONAV","OSP","OSG","PRGN","RLOG","SB","CKH","SSW","SFL","SBLK","GASS","TBSI","TK","TGP","TOO","TNK","TDW","TOPS","TRBR","TNP","ULTR","CBOU","BDL","PNRA","SBUX","THI","ACMR","BKS","BAMM","BGP","CWTR","FGP","HIST","GMTN","NRGP","NRGY","IPT","JAS","LUX","HZO","MED","MDS","ODP","PERF","PETM","SBH","BID","SPLS","SGU","SPH","TITN","TSCO","TA","UGP","WMAR","FLWS","BWL-A","DVD","GEE","ISCA","LTM","TRK","CLUB","BGFV","CAB","DKS","DOVR","DRJ","GOLF","HIBB","SPCHB","ASF","AHS","BBSI","CDI","CITP","CCRN","DHX","JOB","GVHR","HHGP","KELYA","KFRC","KFY","MAN","MPS","NCI","ASGN","PAYX","RCMT","RHI","SFN","SRT","TSTF","TBI","VSCP","VOL","JOBS","ACM","CAI","EEI","ENG","FRM","JEC","KBR","KTOS","PRIM","SAI","STN","TTEK","URS","VSR","VSEC","WYY","WLDN","BBW","ABFS","CLDN","CNW","CVTI","DDMX","XPO","FWRD","FFEX","HTLD","JBHT","KNX","LSTR","MRTN","ODFL","PTSI","PATR","QLTY","SAIA","UACL","USAK","WERN","YRCW","CENT","CTHR","EDUC","LKI","MHJ","SCHS","SMS","USTR","VCO","ACTU","ADBE","ADVS","AMSWA","ATEA","BITS","BMC","BORL","EPAY","BLSW","CA","CALL","STV","CPBY","CTFO","CVLT","CPWR","TRAK","PROJ","EPIC","FNDT","GUID","INTU","LWSN","MGIC","MSFT","TYPE","NTWK","NUAN","OBAS","ORCL","PWRD","PVSW","PTEC","PRGS","PRO","QADI","RHT","RTLX","RNOW","SLRY","SAP","SCIL","SMSI","SOAP","SOFO","SPSS","LIMS","SXCI","SY","SNCR","TLEO","TIGR","VSNT","VOXW","WZE","ACCL","DOX","AMCS","ANLY","ARST","ASUR","ADP","BLKB","BCSI","BPHX","CALD","CATT","CTDC","COGT","CTSH","CVG","CNIC","CSGS","CSPI","DTLK","DWCH","DMAN","DRCO","EFUT","EBIX","EPIQ","PLUS","FALC","FISV","FORTY","GFSI","GRVY","GVP","HMNA","HRAY","ITWO","IHS","RX","MAIL","INFA","ISSC","ISYS","IDN","ININ","JKHY","JDAS","KNXA","LGTY","LFT","MVSN","MANT","MSTR","NTCT","N","NINE","NVTL","OMTR","ONSM","PSOF","PTI","PEGA","PRFT","PKT","QDHC","QSFT","RADS","SAPE","SLP","SBN","SLH","SNIC","SFSF","SUMT","SYNA","TSYS","TIBX","TTPA","TRXI","TSRI","TTIL","UNCA","UNFY","VIT","VTAL","WIND","WIT","RNIN","WSTM","YTEC","APKT","ADCT","ADTN","ALU","ATGN","APSG","ARRS","AUDC","AWRE","BBND","CAMP","CBEY","CRNT","CIEN","CLFD","CLRO","CMRO","CTV","JCS","CMTL","GLW","DTV","DITC","TBUS","DSPG","SATS","ECTX","EFJI","ELMG","ENWV","FEIM","GILT","HLIT","HRS","HSTX","IDSY","IKAN","INFN","JDSU","KVHI","LORL","MRM","MOT","NTGR","NEXS","NMRX","OPTC","OCCF","ORCT","PIII","ANTP","PWAV","XING","QCOM","RFMI","SHOR","SWIR","SONS","SCON","SYMM","TLAB","TNS","VRAZ","CHIP","VSAT","WGNR","WSTL","XETA","ZHNE","ZOOM","EGHT","ADPT","CCUR","OMCL","SCKT","TLVT","XATA","ACFN","ARUN","ALOT","AUO","AVCT","EFII","ELX","ESCC","HAUP","ICAD","IGOI","IMMR","INFS","IN","INPH","KTCC","LTRX","LXK","LOGI","MSII","MRCY","MTSI","NICE","OIIM","OPNT","PLNR","RDCM","RSYS","RIMG","SCMM","SSYS","TISA","TACT","PANL","VIDE","WAVX","ZBRA","ALAN","BRCD","CML","DDUP","DRAM","HILL","EMC","HTCH","IMN","ISLN","LCRD","NTAP","OVRL","QTM","STX","STEC","VOLT","WDC","XRTX","PAR","AMT","BTM","CNTF","CCOI","CCI","ELNK","EQ","GEOY","GLBC","HUGH","IDT","IGLD","IHO","LVLT","MDLK","MTSL","NSR","WAVE","ORBC","PAET","PGI","RIMM","RRST","SDXC","SVR","TEO","TI-A","TSTC","TKG","TKO","TII","TSTR","TWER","VG","WPCS","XFN","SMLC","CRAY","HPQ","IGT","NZ","NYFX","RACK","SGMS","SGIC","SCLD","JAVA","TDC","AEIS","AMSC","ATCO","APH","AVZA","AVX","BELFB","BOSC","CHP","COGO","CNLG","CPII","CTS","DPW","DLB","ESIO","ENA","ESLR","EXFO","HOKU","IVAC","JST","KOPN","KYO","LDK","LPL","MXWL","MEI","NOIZ","MVIS","MOLX","NTE","NSYS","OESX","PWER","QBAK","RFIL","SIMO","SLI","SPA","SPEC","STP","TDK","TNL","TEL","TYC","VICR","VSH","WGA","WMS","MDRX","APY","CERN","ECLP","ETWC","HLTH","MDAS","MEDW","MEDQ","MRGE","MGT","QSII","STRM","ANSW","DATA","DST","DNB","FDS","GNET","IDC","WEBM","WXS","ACXM","ACS","AGYS","CBR","CSC","CTGX","DMRC","EDGW","ELRN","HX","IGTE","IFLG","MNDO","MTMC","NAVI","NCIT","NCR","NSTC","PER","RDWR","SAY","SRX","SXE","SYNT","TSCC","TEAM","TIER","UIS","VRTU","ADAM","AKAM","ABTL","BIDU","RATE","BNX","JRJC","EDGR","DIET","EXPE","GSOL","GOOG","HGRD","HSTM","HSWI","INSP","LOCM","LOOK","LEDR","MIVA","MOVE","NTES","NWMO","PCLN","QPSA","REDF","SOHU","KNOT","TSCM","TRAC","TZOO","TCX","UNTD","UDW","WWWW","WBMD","YHOO","CTCH","ESIC","GSB","IIJI","SIFY","ARBA","ARTG","ADAT","BVSN","CHINA","GIB","CTXS","CKSW","DRIV","DSCM","FFIV","GIGM","GMKT","HPOL","IBAS","IIG","INOD","INAP","ICGE","IPAS","JCOM","JCDA","KEYN","LIOX","LQDT","MLNK","EGOV","ORCC","ONVI","OTEX","OPWV","RAX","RNWK","SONE","SABA","CRM","SLTC","SNDA","SINA","SNWL","LNUX","SPRT","SYMC","ULTI","VRSN","VIGN","VHC","VOCS","WBSN","ZIXI","GNCMA","SAAS","TMX","ATVI","BBBB","CYOU","CNVR","CREL","DIVX","ERTS","GLUU","KNM","COOL","MMUS","RLRN","SKIL","TTWO","THQI","WZEN","TDSC","BBOX","CSCO","DGII","DIGA","ELON","EXTR","FNSR","JNPR","NWK","OCNW","RVBD","SILC","STAR","SMCI","SCMR","VPF","COMS","AAPL","DELL","PALM","BHE","CLS","DDIC","ESYS","ELTK","FLEX","JBL","MERX","MFLX","PKE","PTIX","PLXS","RAVN","SANM","SGMA","SMTX","TTMI","BDR","EDCI","GCOM","ITI","LVWR","PLT","PLCM","RVSN","RITT","SEAC","SNR","SYNX","TKLC","VII","AXK","ATRM","AFFX","A","AMOT","ALOG","AERG","STST","AXYS","BMI","BEC","BIO","BRKR","CALP","CAMT","CRA","CPHD","CLRT","CGNX","COHR","COMV","CUB","CYBE","DAIO","DGLY","DNEX","ELSE","ESE","FARO","FEIC","FLIR","GRMN","GIGA","GSIG","HBIO","HRLY","HURC","ICXT","IIVI","IMA","IO","IRIS","ITRI","KTII","KEI","KEQU","LLL","LB","LCRY","MEAD","MEAS","MKTY","MTD","MIL","MOCO","MTSC","NANO","NEWP","OICO","ORBK","OYOG","PRCP","PSDV","RAE","RSTI","RTEC","SMIT","SIRF","STKR","STRN","SYPR","TIK","LGL","TMO","TLX","TRNS","TRMB","VARI","WAT","WEX","WTT","ZIGO","ACTI","ASIA","CHKP","DBTK","ENTU","MFE","NENG","NOVL","FIRE","VDSI","AMD","ADI","ATML","CAVM","CY","ENTR","FORM","GSIT","IFX","IDTI","INTC","ISIL","MXIM","MLNX","NSM","ONNN","OPXT","SIMG","SPRD","STM","TXN","WEDC","ZILG","API","AMKR","ANAD","AMCC","ATHR","AVNX","AXTI","BRCM","CAMD","CSUN","CNXT","DIOD","EMKR","EXAR","FCS","HITT","ITLN","IRF","INTT","IPGP","IRSN","LDIS","LAVA","MRVL","WFR","MTLK","MCRL","MSCC","TUNE","MOSY","OVTI","OPLK","PRKR","PSEM","PLAB","PXLW","PLXT","PMCS","POWI","QLGC","QUIK","SOL","RFMD","SATC","SMI","SMTC","SWKS","SMOD","SMSC","SUPX","TSM","TO","TSEM","TXCC","TQNT","UMC","VLTR","YGE","ACTL","ACTS","AATI","AFOP","ALTR","ASTI","AUTH","BKHM","CSIQ","CEVA","CRUS","DSTI","ENER","ENTN","FSLR","SOLR","HIMX","IXYS","JASO","LSCC","LLTC","LOGC","LSI","MEMS","MCHP","MPWR","NVEC","NVDA","OTIV","RBCN","SIGM","SLAB","SOLF","TRID","TSL","UCTT","VIMC","VIRL","VUNC","XLNX","ZRAN","ASX","ATE","AEHR","ASYS","AMAT","ASMI","ASYT","ATMI","ACLS","BRKS","BTUI","CCMP","IMOS","COHU","CREE","CYMI","ENTG","EZCH","FSII","XXIA","KLAC","KLIC","LRCX","LPTH","LTXC","MTSN","MSPD","MKSI","NVLS","OSIS","SMTL","SRSL","TWLL","TGAL","TER","TSRA","TRT","UTEK","VSEA","VECO","VRGY","ISSI","LGVN","MU","MIPS","NLST","NETL","RMBS","RMTR","SNDK","SSTI","SPSN","ACIW","ALLT","ANSS","ADSK","BSQR","CDNS","CHRD","CIMT","CNQR","EVOL","ISNS","INS","MANH","MENT","MCRS","MSCS","NATI","OPTV","PMTC","PDFS","PNS","SPNS","SNPS","TYL","VMW","ALSK","T","BCE","CTL","CBB","CNSL","DECC","EONC","EQIX","FRP","FTR","HTCO","IWA","Q","SHEN","SURW","TWTC","VZ","VOCL","WWVY","WIN","ATNI","BRP","TBH","BT","CHA","CTEL","FTE","OTE","HTX","IIT","KTC","MTA","MTE","MICC","MBND","NTT","NTL","TLK","PHI","ROS","SI","SKM","TCL","TNE","NZT","TAR","TEF","TMRK","AIRV","ALVR","AMX","ANEN","ARCW","AFT","CEL","CYCL","GRRF","CHL","CHU","CLWR","DT","FTGX","FTWR","FSN","GSAT","ICOG","IDCC","IPCS","KNOL","LEAP","LTON","MXT","PCS","MBT","TNDM","NIHD","NTLS","DCM","PTNR","PCTI","PT","PRXM","PRPL","QXM","RCNI","SBAC","S","TSP","TMB","TDS","WRLS","TU","TSU","TKC","USM","USMO","UTSI","VIP","VM","VIV","VOD","WVCM","MAD","COP","BWP","GEL","MRO","MTL","MGA","CRC","BTN","FCTY","FCCY","FPBN","SRCE","TCHC","AAN","AFL","AGCO","ALEX","ARE","Y","AB","AMB","CRMT","ACC","AGNC","APO","ADY","MRF","AQQ","AXR","AHR","AIV","ABR","ABG","AHT","AEC","ASTC","AN","AVB","TBBK","TBHS","GRAN","BKYF","OZRK","BMR","BKT","BXG","BOFI","BXP","BDN","BRE","BPI","BYFC","BRKL","CACI","CHY","CALC","CAFI","CPT","COF","CSWC","CSE","CFFN","LSE","KMX","CRRB","CARV","CSH","CBL","CDR","CLFC","CFBK","GTU","CET","CV","CVBK","TRUE","CBNK","CBEH","CHNG","CB","CHT","CINF","CIT","CZNC","CTZN","CSBC","CSBK","CNA","CVLY","CSA","CLP","COLB","CMFB","CPBK","CPBC","ELP","CTBC","CTO","CPSS","CPRT","OFC","CUZ","CIK","DHY","CRFN","CRMH","DNBK","DCT","DB","DDR","DRH","DLR","DGI","DCOM","DFS","DNP","DLLR","D","DEI","DMF","LEO","DSM","DTE","DUK","DRE","DYN","DD","EGBN","EGP","EMN","EV","EOI","EVV","EVN","EVY","ETB","ECBE","EDN","EIX","EDR","EE","EMITF","ECF","ESBK","EMCI","EDE","EIG","ECPG","ENH","EGX","ESGR","ETR","EBTC","EPR","EPG","EPHC","EFX","ELS","EQY","EQR","EQS","ERIE","ESBF","ESP","ESSA","ESS","EVBN","EVR","RE","EEE","EBI","EXR","EZPW","FFH","FBCM","FRT","FII","FFCO","FCH","FFDF","FSBI","LION","FSC","FIF","FISI","FBNC","FNLC","FBSI","SUFB","FCAL","FCAP","FCFS","FCZA","FCNCA","FCLF","FCF","FCBC","FCFL","FDEF","FFBH","THFF","FFCH","FFHS","FR","FKFS","FMD","FMAR","FMR","FNSC","FNFG","FLIC","FF","FPTB","FPO","FRGB","FSFG","FSGI","FSBK","FUNC","FWV","FCFC","FE","FBC","FSR","FFIC","FMC","FNBN","FDI","FIG","FXCB","FPBI","FPL","FSP","FT","FTBK","FULT","GAN","GTY","GBCI","GLAD","GOOD","GAIN","GLBZ","GLG","GRT","GCA","GHI","GTA","GBG","GFLB","GXP","GRNB","GBH","GCBC","GRH","GLRE","GPI","GSLA","GNV","GBNK","GYRO","HQH","HQL","HABC","HALL","HBNK","HMPR","HBHC","HAFC","THG","HGIC","HNBC","HARL","HWFG","TINY","HE","HWBK","HCC","HCP","HCN","HR","HTGC","HTBK","HFWA","HBOS","HEOP","HT","HFFC","HIW","HTH","HIFS","HMG","HMNF","HBCP","HOMB","HOME","HME","HXM","HFBC","HMN","HBNC","HRZB","HPT","HST","HRP","HCBK","IBKC","IEP","IDA","IFSB","INDB","INCB","IPCC","IGD","IRC","IHT","NSUR","IBNK","INGA","TEG","INTG","IAAC","INTX","IBCA","IVZ","ISBC","IRET","IRS","IFC","ISAT","IBB","SFI","ITC","MAYS","JAXB","JXSB","JAG","JNS","JFBI","JFBC","JLI","JFC","JLL","KFED","KED","KYE","KYN","KRNY","KENT","KFFB","KRC","KIM","KFS","KRG","KFN","KCAP","LSBK","LBAI","LKFN","LPSB","LHO","LAZ","LEGC","LXP","LRY","LNC","LAD","LNBB","L","LOOP","LABC","LSBX","LSBI","LTC","MTB","MCBC","MACC","MAC","CLI","MPG","MGYR","MAIN","MAM","MSFG","MLVF","LOAN","MFC","MKL","MI","MXGL","MFLR","MBFI","MBTF","MCGC","MIG","TAXI","MPW","MBR","MBWM","MIGP","MBVT","MCY","EBSB","MERR","MTR","CASH","MV","MET","MDH","MIM","MFI","MPB","MAA","MBRG","MSL","MBHI","MSW","MMCE","MCBF","MNRK","MNRTA","MROE","MRH","MCO","RNE","ICB","MORN","MSBF","MFSF","MVC","NARA","NASB","NKSH","NHI","NATL","NPBC","NNN","NSEC","NWLI","NHP","NVSL","NAVG","NBTF","NBTB","NNI","NHS","NCBC","NEBS","NEN","NHTB","NYB","NAL","NBBC","NCT","NFSB","NEWS","NGPC","NICK","NOK","FFFD","NOVB","NBN","NECB","NSFC","NTRS","NFBK","NRIM","NRF","NWSB","NWFL","NRG","NST","NLP","NCA","NMI","NNY","NYM","OVLY","OCN","ORH","OGE","OLCB","OVBC","OLBK","ONB","OPOF","OSBC","OHI","OLP","OB","ONFC","OPEN","OPHC","OFG","ORN","ORIT","IX","ORA","ORRF","OTTR","PABK","PCBC","PCBK","PMBC","PCE","PPBI","PSBC","PACW","PBCI","PFED","PRK","PKBK","PVSA","PKY","PRE","PBHC","PCAP","PNBK","PGC","PNNT","PWOD","PEI","PAG","PNSN","PBCT","PEBO","PEBK","PCBI","PFBX","POM","PEO","PHH","PNX","PNFP","PNW","PTP","PCL","PLBC","PCC","PNC","PBIB","POR","PPS","PPL","PFBC","PFBI","PRWT","PLFE","PRS","PNBC","PFG","PVTB","PRA","PGN","PGR","PLD","PSEC","PRSP","PL","PCBS","PROV","PFS","PBNY","PBIP","PRU","PUK","PSB","PSBH","PSA","PULB","PIM","PVFC","PZN","QCCO","QCRH","RPFG","RAS","RPT","RAND","RYN","O","ENL","REG","RF","RNR","RNST","RBCAA","FRBK","REXI","RSO","RGCO","RINO","RMG","RIVR","RVSB","RLI","RPI","RCKB","ROMA","ROME","RST","RBPAA","RY","RVT","RRI","RBNF","STBA","SYBT","SAFT","SAL","SMHG","SASR","BFS","SAVB","SCG","SCBT","SBX","SBCF","SBKC","SEIC","SIGI","SRE","SNH","SVBI","SMED","SHW","SHBI","SIFI","SBNY","SGI","SVLF","SLG","SLM","SWI","SOMH","SAH","SOR","TSFG","SJI","SOCB","SCMF","SO","SSE","PCU","SFST","SMBC","SONA","SUG","SBSI","OKSB","SWX","SGB","SSS","SUAI","JOE","STFC","STBC","STT","STEL","STL","SBIB","STBK","STSA","SLT","SSFN","BEE","STRS","STU","SUBK","SMMF","SSBI","SAMB","SNBC","SUI","SLF","SHO","STI","SUPR","SPPR","SUSQ","SBBX","SIVB","SNV","TROW","TFC","TAMB","SKT","TGB","TCO","TAYC","TCB","TSH","TE","TRC","TEI","TNCC","TCBI","THRD","TFSL","TPGI","TWPG","TIBB","TICC","TDBK","TONE","TSBK","TMP","TMK","TD","TTO","TYY","TYG","TYN","TOBC","TOFC","TWGP","TOWN","TRAD","TAC","TRH","TRP","TCI","TRV","TREE","TY","TCAP","TCBK","TRST","TRMK","TKF","YSI","USB","GROW","UBS","UDR","UGI","UIL","UMBF","UMH","UMPQ","UNAM","UBSH","UNB","UNS","INDM","UBCP","UBOH","UBSI","UCBA","UCBI","UCFC","UFCS","UBFO","USBI","UTL","UTR","UNTY","UHT","UVE","UTA","UVSP","UBA","VR","VYFC","VLY","VALU","VKQ","VVC","VTR","VRTA","VRTB","VBFC","VCBI","VRTS","VIST","VNO","VSBN","WHI","WPC","WRB","WBNK","WAIN","WAC","WBCO","WFSL","WRE","WASH","WSCC","WSBF","WAYN","WBS","WRI","WFC","WSBC","WTBA","WCBO","WABC","WR","WFD","WHG","WGL","WGNB","WTM","RVR","WTNY","GIW","WL","WIBC","WOC","FUR","WTFC","WEC","WRLD","WSB","WSFS","WVFC","XEL","XL","YAVY","ZBB","ZNT","ZION","ZIPR"};
		List<Security> seList = new ArrayList<Security>();
		for (int i = 0; i < stocks.size(); i++) {
			try {
				seList.add(this.get(stocks.get(i)));
			} catch (Exception e) {
				System.out.println(stocks.get(i));
			}
		}
		Date curDate = new Date();
		Date lastTradingDate = LTIDate.getLastNYSETradingDayOfMonth(LTIDate.getLastMonthDate(curDate));// get
																										// last
																										// trading
																										// date
																										// of
																										// last
																										// month
		Long rank = 0L;
		Double return_52w = Double.NEGATIVE_INFINITY;

		seList = com.lti.service.bo.RelativeStrength.getSortedSecurity(seList, lastTradingDate);
		for (int j = 0; j < dailyUpdateListenerList.size(); ++j) {
			if (dailyUpdateListenerList.get(j) != null)
				dailyUpdateListenerList.get(j).copy_total(seList.size());
		}
		for (int i = 0; i < seList.size(); i++) {
			for (int j = 0; j < dailyUpdateListenerList.size(); ++j) {
				if (dailyUpdateListenerList.get(j) != null)
					dailyUpdateListenerList.get(j).copy_current(i + 1);
			}
			Security se = seList.get(i);
			try {
				return_52w = se.getReturn(lastTradingDate, TimeUnit.WEEKLY, -52);
			} catch (Exception e) {
			}

			if (this.getRS(se.getSymbol(), lastTradingDate) == null) {
				RelativeStrength rs = new RelativeStrength();
				rank = com.lti.service.bo.RelativeStrength.getSecurityRank(se.getSymbol(), seList);
				rs.setDate(lastTradingDate);
				rs.setSecurityID(se.getID());
				rs.setSymbol(se.getSymbol());
				rs.setSecurityType(se.getSecurityType());
				if (return_52w == Double.NEGATIVE_INFINITY)
					rs.setReturn_52w(null);
				else
					rs.setReturn_52w(return_52w);
				rs.setRank(rank);
				this.saveRS(rs);
				Configuration.writeLog(se.getSymbol(), logDate, curDate, " update Relative Strength Data!");
			}
		}
		Configuration.writeLog("ALL Stock", logDate, curDate, "\n************************************\n Relative Strength Update finished!\n************************************\n");

	}

	@Override
	public void getHistoricalRS(Date date, Date logDate, boolean update) {
		// String[]arr =
		// {"IBM","AIZ","AMIC","AOC","CNO","EIHI","FPIC","GTS","PICO","SFG","UNM","AMG","AMP","BAM","BEN","BX","CLMS","CNS","ACAS","ACG","ADX","AFB","AINV","AKP","ALD","APX","ARCC","ARK","ASP","AVK","AWF","AYN","BAF","BBF","BBK","BCK","BCV","BDF","BDJ","BDV","BFK","BFO","BFZ","ADF","AGD","ASG","BDT","BEP","BGR","BHY","BLU","BME","BOE","BTA","BTO","BWC","CEF","APB","APF","ASA","BHK","BIE","CEE","CH","CHN","CUBA","DGF","EEA","EFL","EMD","EMF","FAX","GCH","GF","GIM","GRR","IFN","IIF","IRL","AACC","ACF","ADS","ADVNB","AEA","AGM","AXP","BKCC","CACC","CCRT","AAV","AIB","BAP","BCS","BLX","BMO","CS","GGAL","IBN","LYG","MFG","MTU","NBG","SAN","SBP","STD","WBK","AFSI","AJG","BRO","CHSI","CISG","CRVL","DCAP","EHTH","MHLD","MMC","NFP","UAHC","WSH","AMPL","AMTD","BGCP","COWN","ETFC","FCSX","IBKR","JMP","KBW","LTS","MF","MKTX","NMR","OXPS","QCC","RODM","SCHW","BLK","BPSG","DHIL","FBR","GBL","GFIG","GHL","ITG","JEF","LAB","LM","NITE","OPY","PLCC","RJF","SF","SIEB","SWS","WDR","AAME","AEL","AGO","ANAT","AXA","CIA","DFG","FAC","FFG","GNW","IHC","IPCR","KCLI","LFC","BAC","BK","BNS","C","CM","JPM","KEY","AFN","ANH","ATAX","BRT","CMO","CRE","DFR","DRL","DX","ELC","FNM","FRE","HCM","HF","HTS","IOT","MFA","NLY","NYMT","RWT","SNFCA","UPFC","ACAP","ACE","ACGL","AFFM","AFG","AGII","AHL","AIG","ALL","AMPH","AMSF","ASI","AWH","AXS","BWINB","ADC","AKR","ALX","APSA","ARL","BPO","CBG","CHLN","CIM","CT","CXW","DFT","EJ","FOR","FXRE","GBE","GKK","ABCB","ABVA","ACBA","AMNB","ANCX","ANNB","ASFN","BAYN","BBT","BCAR","BKOR","BKSC","BNCN","BOFL","BOMK","BOVA","CAPE","CART","CBAN","CBKN","CCBG","CFFC","CFFI","CHCO","CLBH","AMFI","ASBC","BUSE","CBC","CHEV","CHFC","CITZ","CMA","CNAF","CORS","CRBC","CZWI","DEAR","FBIZ","FBMI","FFBC","FFNM","FITB","FMBI","FMER","FPFC","FRME","GABC","HBAN","IBCP","ALLB","ALNC","AROW","ASRV","ATLO","BARI","BCBP","BDGE","BERK","BHB","BMTC","BNCL","BNV","BPFH","CAC","CBNJ","CBU","CCBP","CCNE","CEBK","CIZN","CJBK","CMSB","CNBC","CNBKA","AANB","AMRB","AWBC","BBNK","BMRC","BOCH","BOH","BSRR","CACB","CASB","CATY","CBBO","CBON","CCBD","CFNB","CPF","CTBK","CVBF","CVCY","CVLL","CWBC","CWLZ","CYN","DNBF","EWBC","ACFC","APAB","AUBN","BFNB","BKBK","BTFG","BXS","CADE","CFNL","CNB","CSFL","CSNT","CTBI","CZFC","EVBS","FABK","FBMS","FBSS","FFKT","FFKY","FHN","FMFC","FNB","BANF","BOKF","CASS","CBSH","CFR","COBZ","CSHB","EBTX","EFSC","FCCO","FFIN","FFNW","FFSX","FSNM","GFED","GFG","GSBC","HTLF","IBOC","LARK","LBCP","MCBI","MFNC","MOFG","OSBK","ABBC","ABCW","ABNJ","AF","ASBI","ATBC","BANR","BBX","BCSB","BFBC","BFED","BFIN","BFSB","BHLB","BKJ","BKMU","ABK","FAF","FNF","ITIC","MBI","MTG","ORI","PMI","RAMR","RDN","STC","SUR","TGIC","AEE","ALE","AVA","CHG","CMS","CNP","DPL","ED","EXC","FPU","LNT","MGEE","MIR","NI","NU","NVE","NWE","PCG","PEG","PNM","ACPW","AEP","AES","APWR","AYE","BCON","BIP","BKH","CEG","CNL","CPN","CPL","CWCO","ENI","EOC","HNP","KEP","MGS","SBS","TGS","VE","AGL","APU","ATO","CLNE","CPK","DGAS","EGN","EPB","EQT","EWST","GAS","LG","NFG","NGG","NJR","NWN","OKE","PNY","ARTNA","AWK","AWR","BWTR","CTWS","CWT","HOO","MSEX","PNNW","SJW","SWWC","WTR","YORW","PVD","AXC","HDS","ALUS","AMV","AYA","AWSR.OB","MABAA.OB","AMTC","PEX","APRB.OB","AMEH.OB","ASCQ.OB","CIO","ASPO.OB","AXG","AUCAF.OB","BTE","BSOI.OB","BHCG.OB","BHWF.OB","BPW","CAEL.OB","CMKT.OB","CLA","CYSU.OB","CLNH.OB","CTCJ.OB","HOL","CIIC","CHNQE.OB","CNSJ.OB","GLA","CME","RFI","CLXS.OB","COBK","BUS","CWBS","BTC","CODI","CCVR.OB","CVA","CRT","CRWE.OB","DCDC.OB","DOM","DMLP","DEGH.OB","DYER.OB","EBIG.OB","NGT","EDRG.OB","ESA","ESGI.OB","EST","FCIC.OB","FCVA","FSTF","FMNG.OB","FRXT.OB","FIGI","FFI","GFN","GCNV.OB","GGHO.OB","GHQ","GQN","GHC","GISV.OB","GSL","GPH","GS","GEYO.OB","GGA","GLFO.OB","HTE","HHDG.OB","HEK","HAV","TOH","HBRF.OB","HIA","HGT","HYDQ.OB","IDI","ICGP.OB","IAN","IAQG.OB","ICH","IVOI.OB","JKAK.OB","KHA","LTTV.OB","LPTC.OB","LTHO.OB","LIA","LPHI","LTVL.OB","LCHL.OB","LIXT.OB","LRT","MNDL.OB","MMPA.OB","MARPS","MAQC.OB","MHLI.OB","MBH","TVH","MMDA.OB","MRHD.OB","MOSH.OB","MSB","MCMV.OB","SMCG","MMTRS.OB","MS","NDAQ","NVDF.OB","NNA","NBMF.OB","NEWT","NHR","NRT","NORH.OB","NSAQ.OB","NAQ","NYX","OOLN.OB","OKN","ONCE.OB","NLX","PWE","PGID.OB","PBT","PIP","PGRI.OB","PAX","PVX","RAME","RAQP.OB","RTRO.OB","RTTE.OB","RBCF.OB","SBR","SFE","STJC.OB","SJT","SFEF.OB","MEJ","FYR","SHIP","HLD","SOSV.OB","SACQ.OB","SIBE.OB","SIGN.OB","SVGP.OB","SOON.OB","SOPK.OB","DSP","HMR","SLAW.OB","SOC","STQN.OB","OOO","FGF","TNF","TCXB.OB","TELOZ","TPL","THLM.OB","OZOM.OB","TIRTZ.OB","TMI","TXIC","TRU","TAMG.OB","TTSP.OB","TTII.OB","TGY","TISG.OB","TUX","TCW","TRBD.OB","UHFI.OB","UBNK","URX","UWBK","VTG","VRY","VPFG","VIIC.OB","VYTC.OB","WAL","WSMO.OB","WASM.OB","WHLM.OB","WTU","XDRC.OB","ZAP","ZXSI.OB","TWFH.OB","TTY","EGHA.OB","BBV","BBD","BCH","BMA","CIB","BFR","CBIN","BCA","EUBK","FBP","FBAK.OB","HDB","BPOP","SHG","IRE","WF","AGU","AVD","CF","CGA","CMP","COIN","EDEN","KMGB","MON","MOS","POT","SYT","TRA","TNH","SMG","AA","ACH","CENX","KALU","APD","ASH","CE","DOW","WIRE","FCX","AEM","ANV","AU","AGT","AZK","ABX","CDY","CGR","BVN","KRY","DROOY","EGO","EGI","XRA","GRS","GFI","GRZ","GG","GSS","HMY","IAG","THM","KGN","KBX","KGC","MDW","MGH","NSU","NEM","NAK","NXG","NG","PMU","RIC","RGLD","RBY","SA","XPL","TRE","UXG","VGZ","WGW","AUY","AXAS","AEZ","APC","APA","APAGF","ATPG","AOG","BRN","BRY","BDCO","KAZ","BEXP","COG","CPE","CNQ","CRZO","CHK","SNP","XEC","CKX","CWEI","CEO","CRK","MCF","CRED","XTEX","DPTR","DNR","DVN","DBLE","EPEX","EEQ","ECA","EAC","END","EPD","EOG","EPM","XCO","FPP","FST","GSX","GPR","GEOI","GMXR","GDP","GTE","GPOR","HNR","HKN","HUSA","ISRL","LINE","LEI","MPET","MMLP","MMR","TMR","MXC","NFX","NBL","OXY","PHX","PLLL","PVA","HK","PQ","PXD","PXP","PNRG","PDO","STR","KWK","RRC","ROSE","ROYL","SWN","SM","SGY","SU","SFY","TLM","TRGL","TGA","TXCO","UPL","EGY","WMZ","XTO","AXU","ATI","ARLP","AAU","ANR","ANO","ACI","AZC","BHP","BBL","BW","CCJ","CHNR","SHZ","CNX","ETQ","CXZ","DNN","BOOM","FCL","FRG","GMO","ZINC","ICO","IVN","JRCC","MEE","MMG","MFN","NANX","NCOC","NRP","PZG","PCX","BTU","PVG","PVR","PLG","PLM","QMM","RTI","SWC","TCK","TC","TLR","TIE","URG","URZ","UEC","URRE","USEG","USU","WLT","WLB","YZC","BP","CVX","E","XOM","PZE","PTR","REP","RDS-B","TOT","AHGP","CHBT","HWD","ROY","IPI","MDM","NGD","BQI","AREX","ARD","ATLS","ATN","AHD","ATW","BBG","BPZ","BBEP","BRNC","SNG","CFW","LNG","CXG","CMZ","CXO","CEP","CLR","QBC","DEJ","DO","DNE","EC","ENP","ERF","ESV","ENT","EVEP","FXEN","GST","GGR","GMET","GLRP","GIFI","HP","HERO","HPGP","HLND","HDY","KEG","KOG","LGCY","ME","MWE","MVO","NBR","NTG","GBR","NGAS","NE","NOG","PKD","PTEN","PGH","PRC","PED","PBR","PINN","PDC","PSE","PDE","QELP","QRCP","KGS","REXX","RDC","SSN","SD","STXX","STO","TGC","TEC","RIG","TIV","UDRL","UNT","VNR","VQ","WTI","WLL","ALY","BHI","BAS","BJS","BOLT","WEL","DVR","CAM","CRR","CGV","CPX","CLB","DWSN","DRQ","EXXI","EXH","EXLP","FTI","GOK","GLBL","GLF","HAL","HLX","LUFK","NOV","NGS","NR","NOA","OII","OIS","OMNI","PDRT","PDS","RES","SLB","SII","SPN","SWSI","TESO","TTI","TGE","TRMA","WRES","WFT","WG","WH","ZN","APL","BGH","BPL","CQP","CPNO","DPM","DEP","EP","EEP","ENB","ETE","ETP","HEP","KMP","KMR","MGG","MMP","NS","NSH","OKS","PAA","RGNC","SE","SEP","SXL","NGLS","TCLP","TPP","TLP","WES","WMB","ALJ","ARSD","BPT","CLMT","XTXI","CVI","DK","EROC","EPE","FTO","HES","HOC","IMO","IOC","MUR","NBF","PCZ","PETD","SSL","SUF","SUN","SYNM","TSO","VLO","WNR","YPF","CDE","EXK","HL","IMR","MVG","MGN","SSRI","SLW","SVM","ADES","ALTI","APFC","BIOF","BAK","CBT","CYAN","CYT","FOE","FSI","FTK","GPRE","GU","FUL","IOSP","KRO","LZ","MACE","NGBF","NOEC","NEU","ODC","OMG","OMN","PEIX","PENX","PPO","KWR","SXT","SDTH","SIAL","SOA","SYMX","VRNM","GRA","WDFC","WLK","WPZ","AKS","MT","TPUT","CRS","CPSL","CLF","CMC","SID","DSUP","FRD","GSI","GNA","GGB","ROCK","GNI","SIM","HSC","HAYN","IIIN","MEA","NWPX","NUE","ZEUS","PKX","RTP","SCHN","STLD","SUTR","SYNL","TS","TX","USAP","WOR","ALB","AWC","ARJ","CCC","SQM","CDTI","GGC","HUN","ICOC","IPHS","IFF","LNDC","LDL","MTX","NLC","NL","NCX","OLN","POL","PX","ROC","SHI","TORM","VHI","ASFI","CHKE","CBE","CR","DHR","GY","GE","PPG","RTK","TXT","UTX","MMM","ARCI","GAI","HELE","HDSN","IRBT","LII","NPK","SPW","WHR","CVR","DAI","F","GM","HMC","SORL","TTM","TM","AXL","ARGN","ARM","ALV","BWA","CAAS","CLC","CVGI","CSLR","ATC","DAN","DORM","FDML","FSYS","GETI","GNTX","HAYZ","JCI","LEA","MLR","MOD","MPAA","NOBL","PLI","QTWW","SMP","SRI","STRT","SUP","TEN","TWI","TKS","TRW","WBC","WMCO","WATG","WSCI","SAM","ABV","HOOK","FMX","TAP","COKE","CCE","KOF","CCH","COT","DPS","AKO-A","HANS","JSDA","LBIX","FIZZ","PBG","PAS","REED","KO","BF-B","ROX","CEDC","STZ","DEO","WVVI","CATM","CSTR","DBD","EFOI","FEP","MLHR","HNI","HYC","KBALB","KNL","LYTS","OPMR","PTC","PBI","SCS","PAY","VIRC","XRX","MO","BTI","LO","PM","RAI","STSI","VGR","CHD","CLX","DAR","ECL","OBCI","SCL","ZEP","CBY","CZZ","HSY","IPSU","RMCF","TR","DF","LWAY","SYUT","TOF","WBD","AVID","CSCD","COBR","DTSI","MSN","HAR","HUB-B","ICOP","PHG","KOSS","NIV","PC","RWC","ROFO","SNE","TMS","UEIC","GRO","ALCO","ANDE","ADM","BG","CALM","CVGW","CQB","FDP","SEED","AFCE","HNZ","KFT","LANC","AYI","BSET","ETH","FLXS","FO","FBN","HOFT","LZB","LEG","NTZ","ZZ","SCSS","STLY","TPX","USHS","XNN","BLT","JAH","LBY","LCUT","NWL","BCPC","BRID","CGL-A","HRL","IBA","PDA","SDA","SAFM","SEB","SFD","TSN","HOGS","ABD","ACU","ATX","EBF","FC","OMX","SR","TFCO","BLL","BMS","BWY","CCK","GPK","GEF","ITP","MWV","MPAC","NTIC","OI","PKG","PTV","SEE","BEST","SLGN","TUP","UFPT","VTO","ABH","ARA","AVY","BZ","BKI","CSAR","CVO","CLW","UFS","AIP","IAX","IP","KAI","KPPC","MERC","NSHA","NP","TIS","GLT","PCH","RKT","SPP","SWM","SON","TIN","VRS","VCP","WPP","ACV","AVP","BARE","BTH","CAW","CL","RDEN","EL","IPAR","KMB","BHIP","PARL","FACE","PG","REV","TSC","SUMR","UG","CAJ","CHYR","EK","IKNX","IMAX","NRVN","XRIT","FEED","AIPC","BGS","CPB","JVA","CAG","CPO","FZN","DLM","DMND","FARM","FLO","GIS","GLDC","GMCR","GMK","JJSF","JMBA","JBSS","K","KTEC","LNCE","MLP","MKC","MJN","MGPI","PSTA","NWD","OME","OFI","PEET","PEP","RAH","FRZ","SLE","SENEA","SMBL","STKL","TSTY","HAIN","SNAK","SJM","THS","UN","UL","FOOD","TYM","FOSL","FUQI","JADE","MOV","ORNG","SMD","LVB","EAG","ACAT","HOG","MPX","PII","THO","WGO","SHLM","AEPI","ABL","AMTY","ATR","CSL","CTB","CMT","CTIB","DSWL","FHC","FORD","FFHL","GT","MBLX","MYE","QUIX","ROG","SHLO","SEH","TG","ADGF","ALDA","ELY","CYBI","ESCA","JOUT","NLS","POOL","RGR","APP","BEBE","CRI","COLM","CRWS","DLA","EBHI","EVK","FOH","GIII","GIL","HBI","JOEZ","LIZ","LULU","MFB","OXM","PERY","PVH","RL","ZQK","SPOR","SGC","TAGS","TRLG","UA","VFC","WRC","BWS","COH","CROX","DECK","HLYS","ICON","KSWS","KCP","BOOT","NKE","PXG","DFZ","RCKY","SKX","SHOO","TBAC","TLF","TBL","VLCM","WEYS","WWW","AOI","UVV","APII","FNET","GMTC","GPIC","HAS","JAKK","LF","MCZ","MAT","RCRC","RUS","FSS","FRPT","KNDI","NAV","OSK","PCAR","SPAR","STS","WNC","ASTM","ABII","ACAD","ACHN","ACOR","AEN","ADLS","ANX","AEZS","AFFY","ALNY","ALSE","ALTU","AOB","AMGN","FOLD","AMLN","ANDS","ANSV","ANIK","AGEN","ABIO","ARIA","ATHX","BCRX","BDSI","BIIB","BMRN","BNVI","BPUR","BPAX","BSTC","CADX","CBM","CSII","CXM","CVM","CELG","CEGE","CTIC","CERS","CRL","CHTP","CLDA","CBMX","CRXX","CGEN","CORT","CRTX","CGRB","CRGN","CRIS","CVTX","CYCC","CYPB","CYTK","CYTX","CYTR","DARA","DNDN","DSCO","DYAX","EBS","ENMD","ENZN","EPIX","EURX","EXAS","EXEL","FACT","GENR","GENE","GNVC","GENZ","GILD","GTCB","GTXI","HALO","HNAB","HEB","HSKA","HEPH","HGSI","IDIX","IDRA","IDMI","IG","ILMN","IMM","IMGN","INCY","INHX","INSM","ITMN","JAZZ","LJPC","LXRX","LIFE","LGND","MNKD","MATK","MRNA","MEDX","MNOV","MRX","MDVN","MBRX","MITI","MIPI","MNTA","NABI","CUR","NTII","NBIX","NLTX","NFLD","NVAX","NPSP","NXXI","OMPI","ONTY","ONXX","OPXA","OPTR","CAPS","OSIP","OSIR","OXGN","PDLI","PPHM","PSTI","PARD","PGNX","PLX","QGEN","QLTI","QCOR","REGN","RGEN","RPRX","RNN","RXII","SGMO","SVNT","SGEN","SNT","SQNM","SVA","STEM","SNSS","TRGT","TGEN","TECH","THRX","THLD","TCM","TSPT","TTHI","TRMS","TRBN","VNDA","VIAP","VICL","VPHM","XNPT","XOMA","XTLB","ZIOP","ZGEN","SSRX","ABAX","AKRX","AMAG","ABMC","APPY","AVRX","CLDX","EPCT","ICGN","IDXX","ICCC","BLUD","IMMU","INFI","ILI","ISTA","VIVO","MGRM","MYGN","NEOG","NYMX","OGXI","ORCH","ORXE","PTN","PBIO","QDEL","RGDX","ROSG","DDD","SRDX","SNTA","TPTX","TRIB","ACUR","ALKS","BVF","CBRX","DCTH","ELN","EMIS","FLML","GNBT","HSP","ISV","KV-A","MTXX","NKTR","NVD","NOVN","PPCO","PETS","QGLY","ULU","ABT","ACET","APPA","AZN","AVGN","BMY","CPEX","LLY","EVTC","GORX","GSK","IPXL","JAV","JNJ","KERX","MRK","NVS","NVGN","OSCI","PFE","RIGL","SNY","SGP","SPPI","WX","WYE","ADLR","ALXN","ALXA","AGN","ALTH","AMRN","RDEA","ARNA","ARQL","ARYX","AVNR","AVII","BIOD","CRME","CEPH","CHTT","COR","CBST","DEPO","RDY","DRRX","DUSA","DVAX","ELI","ENDP","FRX","GENT","GERN","ISPH","ISIS","KNDL","KG","LCI","MAPP","MSHL","MDCO","MBRK","NRGN","NGSX","NEXM","NTMD","NBY","NVO","OPK","OREX","PTIE","PCYC","VRUS","PXSL","POZN","PRAN","RGN","RELV","SNTS","SCLN","SEPR","SIGA","SOMX","SCMP","SUPG","TELK","TEVA","TPI","UTHR","VRX","VRTX","WCRX","YMI","AUXL","KUN","CSKI","DDSS","MTEX","MDNU","NAII","NTY","NUTR","PRGO","PBH","SLXP","WNI","TBV","USNA","CPD","CPRX","HLCS","HITK","ILE","MYL","PRX","SRLS","WPI","AET","AGP","CNC","CI","CVH","HNT","HS","HUM","MGLN","MDF","MOH","UNH","UAM","WCG","WLP","AHCI","AFAM","AMED","KAD","CHE","GTIV","LHCG","LNCR","PHC","AMSG","CYH","DYII","HMA","LPNT","MDTH","RHB","SSY","THC","UHS","ADK","AVCA","ALC","BKD","CSU","ESC","FVE","KND","NHC","ODSY","SKH","SUNH","SRZ","ENSG","ARAY","ALGN","AHPI","APT","AMAC","AMMD","HRT","ASPM","ATSI","BFRM","BSDM","CLZR","CDIC","CLSN","CHIO","CNMD","CRY","CUTR","CYBX","CYNO","DRAD","DYNT","EDAP","EW","LZR","ENDO","ETRM","ESMC","EXAC","FONR","GIVN","GB","HNSN","HTRN","HOLX","ISRG","IVC","IRIX","IVVI","KCI","LAKE","GAIT","MAKO","MASI","MDCI","MDT","MEND","MSA","MSON","BABY","NSTR","NXTM","OFIX","PMTI","PHMD","RMD","RMTI","RTIX","SHMR","SIRO","SNN","SLTM","SMTS","SNCI","SPAN","STRC","STJ","STXS","STE","SMA","SURG","ELOS","SYNO","SPNC","TGX","KOOL","TOMO","ULGX","VAR","VSCI","VNUS","VOLC","WHRT","WMGI","XCR","ZMH","ZOLL","ABMD","ASB","ACL","ATEC","ADL","ANGN","ANGO","AIS","ARTL","ATRC","ATRI","BAX","BDX","BLTI","BMTI","BSMD","BSX","BVX","CMN","CSCX","CRDC","BEAT","CASM","CMED","CPTS","COV","BCR","DXR","XRAY","DXCM","MELA","ELGX","EVVV","HAE","HTWR","HRC","HDIX","IFLO","ICUI","INO","PODD","IART","ISR","IVD","KNSY","LMAT","LMNX","MMSI","MLAB","MR","NSPH","NURO","NMTI","NCST","NUVA","TEAR","OCLS","OSUR","VITA","OSTE","PKI","PMII","PDEX","RVP","ROCM","SENO","SONO","SDIX","SYK","COO","THOR","TSON","UPI","UTMD","VASC","VSGN","VVUS","WST","XTNT","YDNT","ZILA","AIQ","ARRY","BITI","BRLI","ENZ","ERES","GHDX","GXDX","LH","MTOX","NADX","PPDI","PMD","DGX","RDNT","SPEX","INMD","LCAV","PZZ","TRCR","ALLI","ASGR","ANCI","AMS","BIOS","CNU","DVA","DCAI","GRMH","HGR","HLS","HWAY","EAR","HH","HYTM","IPCM","MDZ","MD","NBS","NHWK","VETS","PRSC","PSYS","QGP","TLCV","USPH","VRAD","BA","RTN","AIR","AIM","AVAV","ATK","ADG","ATRO","SPAB","BEAV","BZC","CGT","CVU","CW","DCO","EDAC","ESLT","ERJ","ERI","ESL","GD","GR","HWK","HEI","HXL","HSR","HON","KAMN","KRSL","LMIA","LMT","MOG-A","NOC","ORB","COL","SIF","SWHC","SPR","TASR","TDY","TOD","TDG","TGI","CX","CRH","EXP","JHX","RMX","TXI","RMIX","ATU","ADEP","AP","BLD","BNSO","BGG","BWEN","BC","CYD","CFX","CMI","CVV","DOV","DRC","ETC","FLDR","FLS","GDI","GRB","GRC","GGG","IEX","ITW","IR","IIN","ITT","JBT","KUB","MNTX","MPR","MIDD","NDSN","NVMI","PLL","PMFG","PRST","RBN","SHS","SHFL","TAYD","TFX","TNC","TWIN","WUHN","AG","ALG","ARTW","ASTE","BUCY","CAE","CAT","CNH","CMCO","DE","GENC","HIT","JOYG","LNN","MTW","NC","PTG","TEX","AAON","ACO","AMN","APOG","AWI","CUO","DW","FAST","GFF","HW","IPII","LXU","MLM","MDU","NCS","OC","PGTI","QEPC","NX","RSOL","RPM","TATTF","TECUA","USLM","USG","VAL","VMC","CBI","CAEI","FIX","EME","IESC","KSW","PEC","PWR","SPWRA","AGX","DY","ICA","FLR","FWLT","GV","GVA","GLDD","IGC","INSU","LAYN","MTZ","MTRX","MDR","MYRG","OMGI","PCR","PLPC","PCYO","SERV","STRL","ABB","ABAT","AIMC","AETI","AME","AOS","ARTX","AZZ","BEZ","BLDP","BDC","CPST","CBAK","CCIX","DAKT","ETN","HEV","ENR","ENS","XIDE","FELE","FCEL","FSIN","BGC","GTI","HRBN","HOLI","HPJ","HYGS","LFUS","MAG","MDTL","NJ","PLUG","POWL","RZ","RBC","ROK","SVT","TRCI","TNB","TIII","TLGD","ULBI","UQM","VLNC","WGOV","ZRBA","ZOLT","ASEI","B","CRDN","CCF","CFSG","CIR","EGT","EMR","NPO","HEES","MWA","NGA","OFLX","PH","PNR","ROP","HEAT","SXI","SNHY","TTES","TRS","AFP","WTS","WXCO","AERT","AMWD","DEL","JCTCF","KOP","LUK","LPX","MAS","MXM","PATK","POPE","TWP","UFPI","WY","CIX","FLOW","HDNG","KDN","KMT","NNBR","ROLL","SWK","THMD","TKR","CAV","CVCO","CHB","NOBH","PHHM","SKY","GTLS","GHM","HIHO","LDSH","MATW","MINI","MLI","PKOH","PCP","RS","X","VMI","CECE","DCI","ERII","CLWT","FTEK","MFRI","PURE","WPRT","AVTR","BZH","BHS","CTX","CHCI","DHI","GFA","HOV","KBH","LEN","MHO","MDC","MTH","NVR","OHB","PHM","RYL","SPF","TOL","XIN","AERO","EML","LECO","SCX","PFIN","SSD","SNA","BDK","TTC","AIN","CFI","DII","HWG","IFSIA","MHK","DXYN","UFI","XRM","ECOL","AWX","CWST","CLH","GNH","HCCI","PESI","RSG","SGR","SRCL","WCN","WMI","WSII","WCAA","FMCN","IPG","MWW","OMC","VCLK","VISN","XSEL","AIRT","ATSG","CHRW","EXPD","FDX","HUBG","PACR","SINO","UPS","UTIW","VEXP","AIRM","AAWW","BRS","PAC","ASR","LIMC","PHII","ANF","ARO","AEO","ANN","BKRS","BKE","CACH","CMRG","CTR","CHIC","CHRS","CHS","CBK","CTRN","PSS","DEST","DBRN","DSW","FINL","FL","GPS","GCO","GES","GYMB","HOTT","JCG","JNY","JOSB","LTD","MW","NWY","JWN","PSUN","RVI","ROST","SCVL","SSI","SMRT","SYMS","PLCE","TLB","TWB","URBN","WTSLA","ZUMZ","AAP","AZO","ORLY","PBY","PRTS","CRV","GPC","LKQX","AE","ANW","CAS","GLP","HWKN","FSTR","MIC","RVEP","FUEL","INT","BBGI","CJR","CXR","CMLS","EMMS","ETM","RC","ROIAK","RGCI","SGA","SALM","SIRI","SBSA","BLC","CBS","CETV","CTCM","EVC","FSCI","GTN","TV","HTV","TVL","NXST","NTN","PLA","RCI","SBGI","WPTE","AKNS","BECN","BXC","IBI","AAC","ABM","CIDM","AM","AMIN","APEI","ARP","APAC","ARBX","ATAC","ATHN","BNE","BRC","BR","CDZI","CBZ","CHMP","CTAS","CLCT","CTT","CPSI","SCOR","CGX","CSGP","CRD-B","CSS","CYBS","DLX","DGIT","DCP","ES","ENOC","EEFT","EXLS","FIC","FIS","FADV","VIFL","GKSR","GA","GPN","GRIF","HCSG","HPY","HMSY","HQS","IMNY","IAO","ICTG","INWK","INOC","NPLA","INSW","ICE","INET","IILG","IRM","KONG","LPS","LIME","LLNW","LPSN","LMLP","MFW","MA","MELI","MGI","MXB","LABL","UEPS","ONT","OWW","ORS","PDII","PFSW","PFWD","TUTR","PRAA","POWR","PRGX","RRD","REIS","REFR","DINE","RBA","ROL","SVVS","SDBT","SGRP","SYKE","SNX","TISI","TTGT","TTEC","TRI","TKTM","TSS","UNF","USAT","UTK","IVA","VCGH","PGV","VVI","V","VPRT","WINA","WNS","ZANE","ATV","AMZN","BIDZ","BFLY","DLIA","EBAY","GAIA","GSIC","HSNI","IACI","NSIT","LINTA","LIVE","OSTK","PCCC","MALL","RBI","STMP","SYX","VVTV","ASCMA","BSY","CVC","CABL","CMCSA","CRWN","DISCA","DISH","LMDIA","LBTYA","LCAPA","LNET","MCCC","SNI","SJR","SPDE","TWC","TIVO","VIA-B","VMED","AVSR","ENPT","GTSI","IM","INXI","NAVR","PRLS","PMRY","SCSC","TECD","WSTG","CTRP","DCU","EMS","FGXI","TUC","MNRO","NTRI","RURL","SFLY","LOV","STAN","KDE","BONT","CPWM","DDS","JCP","KSS","M","SKS","SHLD","TJX","BIG","BJ","COST","DLTR","DUCK","FDO","FRED","PSMT","TGT","TUES","WMT","NDN","NPD","CVS","MHS","OCR","PMC","RAD","WAG","ABC","BJGP","CAH","SAB","HLF","MCK","NUS","EPAX","AMCE","APOL","ATAI","CPLA","CECO","DL","CAST","CEDU","MBA","COCO","DV","GPX","LOPE","ESI","LRN","LTRE","LINC","MMS","EDU","NLCI","REVU","STRA","UTI","BBY","CONN","GME","HGG","RSH","RSC","AEY","AXE","ARW","VOXX","AVT","CELL","CRFT","HWCC","IFON","ISIG","ITRN","JACO","NUHC","ORBT","RADA","RELL","TAIT","TESS","UPG","UUU","GWW","WSO","WCC","CKXE","GET","NWSA","TWX","DIS","DIT","CORE","WILC","NAFC","SPTN","SYY","UNFI","VPS","BYI","CPHC","CHDN","DDE","LACO","MNTG","MGAM","PNK","PTEK","NCTY","UBET","CCL","CUK","FUN","LYV","OUTD","PRXI","RCL","SIX","ORCD","TIXC","WMG","WWE","ARDNA","BSI","CASY","CBD","DEG","DDRX","IMKTA","KR","PTRY","RDK","SWY","SVU","SUSS","GAP","VLGEA","WMK","WFMI","WINN","BBBY","DWRI","HVT","JEN","KIRK","PIR","WSM","BLDR","LOW","LL","HD","ARG","AIT","CFK","DXPE","HRSH","KHD","LAWS","MSM","SNEN","BMJ","NILE","DGC","SIG","TIF","ZLC","CHH","HMIN","IHG","MCS","MAR","MHGC","OEH","RLH","SNSTA","HOT","WYN","AMR","CEA","ZNH","CAL","DAL","TAM","UAUA","LCC","ACN","ADPI","BDMS","CDII","CFS","EXBD","CRAI","DTPI","DUF","ELOY","EXPO","ESRX","FCN","IT","G","GLOI","HSII","HEW","HIL","HURN","ICFI","IDSA","III","VTIV","TMNG","MHH","BKR","NOVA","RMKR","RECN","HCKT","TGIS","TRR","WW","AMCN","ALOY","CCO","CTCT","HHS","HOLL","IUSA","KOW","LAMR","MCHX","MDCA","CMKG","NCMI","SGK","SORC","VCI","AHII","CHDX","HSIC","MWIV","NYER","OMI","PDCO","PSSI","STAA","CKEC","CNK","DWA","DISK","LGF","MVL","NOOF","PAE","PTSX","RDI","RGC","RENT","RHIE","SAPX","BBI","HAST","NFLX","TWMC","AMIE","CSV","CPY","LONG","HRB","FIT","HI","JTX","LGBT","PPD","RGS","RSCR","ROAC","SCI","STNR","STEI","STON","ULTA","WOOF","WTW","WU","CRRC","JW-A","NED","PSO","PEDH","SCHL","MHP","AHC","DJCO","GCI","JRN","LEE","MEG","SSP","MNI","NYT","WPO","DM","MSO","MDP","PRM","PRVT","RUK","SPRO","ARII","BNI","CNI","CP","CSX","RAIL","GWR","GBX","GSH","KSU","NSC","PRPX","PWX","TRN","UNP","WAB","AAI","ALK","ALGT","CPA","XJT","GOL","GIA","HA","JBLU","LFL","MESA","PNCL","RJET","SKYW","LUV","RNT","AER","ACY","AYR","UHAL","CAR","FLY","CAP","DTG","ELRC","GMT","GLS","HTZ","MRLN","MGRC","MIND","RCII","RRR","R","TAL","TGH","URI","WSC","WLFC","ACTG","ABCO","AMRI","ARB","ARWR","BASI","CBLI","CBTE","CVD","GTF","DCGN","ENCO","FORR","GPRO","ICLR","LDR","XPRT","LSR","LUNA","MAXY","NRCI","NTSC","OMEX","PRXL","SNMX","SPIR","SMMX","ASCA","BYD","CNTY","NYNY","FLL","WOLF","ISLE","LVS","LGN","MPEL","MGM","MCRI","UWN","PENN","RIV","MTN","WYNN","ARKR","BNHNA","BJRI","BOBE","EAT","BWLD","BKC","CPKI","TAST","CEC","CMG","CKR","COSI","CBRL","DRI","DENN","DIN","DPZ","BAGL","DAVE","FRS","GTIM","GCFB","JAX","JACK","KONA","KKD","LNY","LUB","MSSR","MCD","CASA","MRT","NATH","CHUX","PZZA","PFCB","PZZI","RRGB","RICK","RUBO","RT","RUTH","SONC","STRZ","SNS","TXRH","CAKE","WEN","WEST","YUM","BCO","CFL","CKP","CSR","MOC","CRN","DMC","GEO","HBE","HTC","ID","LOJN","MAGS","NSSC","PNTR","PONE","TRIS","AXB","ACLI","RAMS","BHO","CPLP","DAC","DHT","DSX","DRYS","EGLE","ESEA","EXM","FREE","FRO","GNK","GMR","GLNG","TMM","HRZ","HOS","ISH","KSP","KEX","VLCCF","NM","NMM","NAT","OCNF","ONAV","OSP","OSG","PRGN","RLOG","SB","CKH","SSW","SFL","SBLK","GASS","TBSI","TK","TGP","TOO","TNK","TDW","TOPS","TRBR","TNP","ULTR","CBOU","BDL","PNRA","SBUX","THI","ACMR","BKS","BAMM","BGP","CWTR","FGP","HIST","GMTN","NRGP","NRGY","IPT","JAS","LUX","HZO","MED","MDS","ODP","PERF","PETM","SBH","BID","SPLS","SGU","SPH","TITN","TSCO","TA","UGP","WMAR","FLWS","BWL-A","DVD","GEE","ISCA","LTM","TRK","CLUB","BGFV","CAB","DKS","DOVR","DRJ","GOLF","HIBB","SPCHB","ASF","AHS","BBSI","CDI","CITP","CCRN","DHX","JOB","GVHR","HHGP","KELYA","KFRC","KFY","MAN","MPS","NCI","ASGN","PAYX","RCMT","RHI","SFN","SRT","TSTF","TBI","VSCP","VOL","JOBS","ACM","CAI","EEI","ENG","FRM","JEC","KBR","KTOS","PRIM","SAI","STN","TTEK","URS","VSR","VSEC","WYY","WLDN","BBW","ABFS","CLDN","CNW","CVTI","DDMX","XPO","FWRD","FFEX","HTLD","JBHT","KNX","LSTR","MRTN","ODFL","PTSI","PATR","QLTY","SAIA","UACL","USAK","WERN","YRCW","CENT","CTHR","EDUC","LKI","MHJ","SCHS","SMS","USTR","VCO","ACTU","ADBE","ADVS","AMSWA","ATEA","BITS","BMC","BORL","EPAY","BLSW","CA","CALL","STV","CPBY","CTFO","CVLT","CPWR","TRAK","PROJ","EPIC","FNDT","GUID","INTU","LWSN","MGIC","MSFT","TYPE","NTWK","NUAN","OBAS","ORCL","PWRD","PVSW","PTEC","PRGS","PRO","QADI","RHT","RTLX","RNOW","SLRY","SAP","SCIL","SMSI","SOAP","SOFO","SPSS","LIMS","SXCI","SY","SNCR","TLEO","TIGR","VSNT","VOXW","WZE","ACCL","DOX","AMCS","ANLY","ARST","ASUR","ADP","BLKB","BCSI","BPHX","CALD","CATT","CTDC","COGT","CTSH","CVG","CNIC","CSGS","CSPI","DTLK","DWCH","DMAN","DRCO","EFUT","EBIX","EPIQ","PLUS","FALC","FISV","FORTY","GFSI","GRVY","GVP","HMNA","HRAY","ITWO","IHS","RX","MAIL","INFA","ISSC","ISYS","IDN","ININ","JKHY","JDAS","KNXA","LGTY","LFT","MVSN","MANT","MSTR","NTCT","N","NINE","NVTL","OMTR","ONSM","PSOF","PTI","PEGA","PRFT","PKT","QDHC","QSFT","RADS","SAPE","SLP","SBN","SLH","SNIC","SFSF","SUMT","SYNA","TSYS","TIBX","TTPA","TRXI","TSRI","TTIL","UNCA","UNFY","VIT","VTAL","WIND","WIT","RNIN","WSTM","YTEC","APKT","ADCT","ADTN","ALU","ATGN","APSG","ARRS","AUDC","AWRE","BBND","CAMP","CBEY","CRNT","CIEN","CLFD","CLRO","CMRO","CTV","JCS","CMTL","GLW","DTV","DITC","TBUS","DSPG","SATS","ECTX","EFJI","ELMG","ENWV","FEIM","GILT","HLIT","HRS","HSTX","IDSY","IKAN","INFN","JDSU","KVHI","LORL","MRM","MOT","NTGR","NEXS","NMRX","OPTC","OCCF","ORCT","PIII","ANTP","PWAV","XING","QCOM","RFMI","SHOR","SWIR","SONS","SCON","SYMM","TLAB","TNS","VRAZ","CHIP","VSAT","WGNR","WSTL","XETA","ZHNE","ZOOM","EGHT","ADPT","CCUR","OMCL","SCKT","TLVT","XATA","ACFN","ARUN","ALOT","AUO","AVCT","EFII","ELX","ESCC","HAUP","ICAD","IGOI","IMMR","INFS","IN","INPH","KTCC","LTRX","LXK","LOGI","MSII","MRCY","MTSI","NICE","OIIM","OPNT","PLNR","RDCM","RSYS","RIMG","SCMM","SSYS","TISA","TACT","PANL","VIDE","WAVX","ZBRA","ALAN","BRCD","CML","DDUP","DRAM","HILL","EMC","HTCH","IMN","ISLN","LCRD","NTAP","OVRL","QTM","STX","STEC","VOLT","WDC","XRTX","PAR","AMT","BTM","CNTF","CCOI","CCI","ELNK","EQ","GEOY","GLBC","HUGH","IDT","IGLD","IHO","LVLT","MDLK","MTSL","NSR","WAVE","ORBC","PAET","PGI","RIMM","RRST","SDXC","SVR","TEO","TI-A","TSTC","TKG","TKO","TII","TSTR","TWER","VG","WPCS","XFN","SMLC","CRAY","HPQ","IGT","NZ","NYFX","RACK","SGMS","SGIC","SCLD","JAVA","TDC","AEIS","AMSC","ATCO","APH","AVZA","AVX","BELFB","BOSC","CHP","COGO","CNLG","CPII","CTS","DPW","DLB","ESIO","ENA","ESLR","EXFO","HOKU","IVAC","JST","KOPN","KYO","LDK","LPL","MXWL","MEI","NOIZ","MVIS","MOLX","NTE","NSYS","OESX","PWER","QBAK","RFIL","SIMO","SLI","SPA","SPEC","STP","TDK","TNL","TEL","TYC","VICR","VSH","WGA","WMS","MDRX","APY","CERN","ECLP","ETWC","HLTH","MDAS","MEDW","MEDQ","MRGE","MGT","QSII","STRM","ANSW","DATA","DST","DNB","FDS","GNET","IDC","WEBM","WXS","ACXM","ACS","AGYS","CBR","CSC","CTGX","DMRC","EDGW","ELRN","HX","IGTE","IFLG","MNDO","MTMC","NAVI","NCIT","NCR","NSTC","PER","RDWR","SAY","SRX","SXE","SYNT","TSCC","TEAM","TIER","UIS","VRTU","ADAM","AKAM","ABTL","BIDU","RATE","BNX","JRJC","EDGR","DIET","EXPE","GSOL","GOOG","HGRD","HSTM","HSWI","INSP","LOCM","LOOK","LEDR","MIVA","MOVE","NTES","NWMO","PCLN","QPSA","REDF","SOHU","KNOT","TSCM","TRAC","TZOO","TCX","UNTD","UDW","WWWW","WBMD","YHOO","CTCH","ESIC","GSB","IIJI","SIFY","ARBA","ARTG","ADAT","BVSN","CHINA","GIB","CTXS","CKSW","DRIV","DSCM","FFIV","GIGM","GMKT","HPOL","IBAS","IIG","INOD","INAP","ICGE","IPAS","JCOM","JCDA","KEYN","LIOX","LQDT","MLNK","EGOV","ORCC","ONVI","OTEX","OPWV","RAX","RNWK","SONE","SABA","CRM","SLTC","SNDA","SINA","SNWL","LNUX","SPRT","SYMC","ULTI","VRSN","VIGN","VHC","VOCS","WBSN","ZIXI","GNCMA","SAAS","TMX","ATVI","BBBB","CYOU","CNVR","CREL","DIVX","ERTS","GLUU","KNM","COOL","MMUS","RLRN","SKIL","TTWO","THQI","WZEN","TDSC","BBOX","CSCO","DGII","DIGA","ELON","EXTR","FNSR","JNPR","NWK","OCNW","RVBD","SILC","STAR","SMCI","SCMR","VPF","COMS","AAPL","DELL","PALM","BHE","CLS","DDIC","ESYS","ELTK","FLEX","JBL","MERX","MFLX","PKE","PTIX","PLXS","RAVN","SANM","SGMA","SMTX","TTMI","BDR","EDCI","GCOM","ITI","LVWR","PLT","PLCM","RVSN","RITT","SEAC","SNR","SYNX","TKLC","VII","AXK","ATRM","AFFX","A","AMOT","ALOG","AERG","STST","AXYS","BMI","BEC","BIO","BRKR","CALP","CAMT","CRA","CPHD","CLRT","CGNX","COHR","COMV","CUB","CYBE","DAIO","DGLY","DNEX","ELSE","ESE","FARO","FEIC","FLIR","GRMN","GIGA","GSIG","HBIO","HRLY","HURC","ICXT","IIVI","IMA","IO","IRIS","ITRI","KTII","KEI","KEQU","LLL","LB","LCRY","MEAD","MEAS","MKTY","MTD","MIL","MOCO","MTSC","NANO","NEWP","OICO","ORBK","OYOG","PRCP","PSDV","RAE","RSTI","RTEC","SMIT","SIRF","STKR","STRN","SYPR","TIK","LGL","TMO","TLX","TRNS","TRMB","VARI","WAT","WEX","WTT","ZIGO","ACTI","ASIA","CHKP","DBTK","ENTU","MFE","NENG","NOVL","FIRE","VDSI","AMD","ADI","ATML","CAVM","CY","ENTR","FORM","GSIT","IFX","IDTI","INTC","ISIL","MXIM","MLNX","NSM","ONNN","OPXT","SIMG","SPRD","STM","TXN","WEDC","ZILG","API","AMKR","ANAD","AMCC","ATHR","AVNX","AXTI","BRCM","CAMD","CSUN","CNXT","DIOD","EMKR","EXAR","FCS","HITT","ITLN","IRF","INTT","IPGP","IRSN","LDIS","LAVA","MRVL","WFR","MTLK","MCRL","MSCC","TUNE","MOSY","OVTI","OPLK","PRKR","PSEM","PLAB","PXLW","PLXT","PMCS","POWI","QLGC","QUIK","SOL","RFMD","SATC","SMI","SMTC","SWKS","SMOD","SMSC","SUPX","TSM","TO","TSEM","TXCC","TQNT","UMC","VLTR","YGE","ACTL","ACTS","AATI","AFOP","ALTR","ASTI","AUTH","BKHM","CSIQ","CEVA","CRUS","DSTI","ENER","ENTN","FSLR","SOLR","HIMX","IXYS","JASO","LSCC","LLTC","LOGC","LSI","MEMS","MCHP","MPWR","NVEC","NVDA","OTIV","RBCN","SIGM","SLAB","SOLF","TRID","TSL","UCTT","VIMC","VIRL","VUNC","XLNX","ZRAN","ASX","ATE","AEHR","ASYS","AMAT","ASMI","ASYT","ATMI","ACLS","BRKS","BTUI","CCMP","IMOS","COHU","CREE","CYMI","ENTG","EZCH","FSII","XXIA","KLAC","KLIC","LRCX","LPTH","LTXC","MTSN","MSPD","MKSI","NVLS","OSIS","SMTL","SRSL","TWLL","TGAL","TER","TSRA","TRT","UTEK","VSEA","VECO","VRGY","ISSI","LGVN","MU","MIPS","NLST","NETL","RMBS","RMTR","SNDK","SSTI","SPSN","ACIW","ALLT","ANSS","ADSK","BSQR","CDNS","CHRD","CIMT","CNQR","EVOL","ISNS","INS","MANH","MENT","MCRS","MSCS","NATI","OPTV","PMTC","PDFS","PNS","SPNS","SNPS","TYL","VMW","ALSK","T","BCE","CTL","CBB","CNSL","DECC","EONC","EQIX","FRP","FTR","HTCO","IWA","Q","SHEN","SURW","TWTC","VZ","VOCL","WWVY","WIN","ATNI","BRP","TBH","BT","CHA","CTEL","FTE","OTE","HTX","IIT","KTC","MTA","MTE","MICC","MBND","NTT","NTL","TLK","PHI","ROS","SI","SKM","TCL","TNE","NZT","TAR","TEF","TMRK","AIRV","ALVR","AMX","ANEN","ARCW","AFT","CEL","CYCL","GRRF","CHL","CHU","CLWR","DT","FTGX","FTWR","FSN","GSAT","ICOG","IDCC","IPCS","KNOL","LEAP","LTON","MXT","PCS","MBT","TNDM","NIHD","NTLS","DCM","PTNR","PCTI","PT","PRXM","PRPL","QXM","RCNI","SBAC","S","TSP","TMB","TDS","WRLS","TU","TSU","TKC","USM","USMO","UTSI","VIP","VM","VIV","VOD","WVCM","MAD","COP","BWP","GEL","MRO","MTL","MGA","CRC","BTN","FCTY","FCCY","FPBN","SRCE","TCHC","AAN","AFL","AGCO","ALEX","ARE","Y","AB","AMB","CRMT","ACC","AGNC","APO","ADY","MRF","AQQ","AXR","AHR","AIV","ABR","ABG","AHT","AEC","ASTC","AN","AVB","TBBK","TBHS","GRAN","BKYF","OZRK","BMR","BKT","BXG","BOFI","BXP","BDN","BRE","BPI","BYFC","BRKL","CACI","CHY","CALC","CAFI","CPT","COF","CSWC","CSE","CFFN","LSE","KMX","CRRB","CARV","CSH","CBL","CDR","CLFC","CFBK","GTU","CET","CV","CVBK","TRUE","CBNK","CBEH","CHNG","CB","CHT","CINF","CIT","CZNC","CTZN","CSBC","CSBK","CNA","CVLY","CSA","CLP","COLB","CMFB","CPBK","CPBC","ELP","CTBC","CTO","CPSS","CPRT","OFC","CUZ","CIK","DHY","CRFN","CRMH","DNBK","DCT","DB","DDR","DRH","DLR","DGI","DCOM","DFS","DNP","DLLR","D","DEI","DMF","LEO","DSM","DTE","DUK","DRE","DYN","DD","EGBN","EGP","EMN","EV","EOI","EVV","EVN","EVY","ETB","ECBE","EDN","EIX","EDR","EE","EMITF","ECF","ESBK","EMCI","EDE","EIG","ECPG","ENH","EGX","ESGR","ETR","EBTC","EPR","EPG","EPHC","EFX","ELS","EQY","EQR","EQS","ERIE","ESBF","ESP","ESSA","ESS","EVBN","EVR","RE","EEE","EBI","EXR","EZPW","FFH","FBCM","FRT","FII","FFCO","FCH","FFDF","FSBI","LION","FSC","FIF","FISI","FBNC","FNLC","FBSI","SUFB","FCAL","FCAP","FCFS","FCZA","FCNCA","FCLF","FCF","FCBC","FCFL","FDEF","FFBH","THFF","FFCH","FFHS","FR","FKFS","FMD","FMAR","FMR","FNSC","FNFG","FLIC","FF","FPTB","FPO","FRGB","FSFG","FSGI","FSBK","FUNC","FWV","FCFC","FE","FBC","FSR","FFIC","FMC","FNBN","FDI","FIG","FXCB","FPBI","FPL","FSP","FT","FTBK","FULT","GAN","GTY","GBCI","GLAD","GOOD","GAIN","GLBZ","GLG","GRT","GCA","GHI","GTA","GBG","GFLB","GXP","GRNB","GBH","GCBC","GRH","GLRE","GPI","GSLA","GNV","GBNK","GYRO","HQH","HQL","HABC","HALL","HBNK","HMPR","HBHC","HAFC","THG","HGIC","HNBC","HARL","HWFG","TINY","HE","HWBK","HCC","HCP","HCN","HR","HTGC","HTBK","HFWA","HBOS","HEOP","HT","HFFC","HIW","HTH","HIFS","HMG","HMNF","HBCP","HOMB","HOME","HME","HXM","HFBC","HMN","HBNC","HRZB","HPT","HST","HRP","HCBK","IBKC","IEP","IDA","IFSB","INDB","INCB","IPCC","IGD","IRC","IHT","NSUR","IBNK","INGA","TEG","INTG","IAAC","INTX","IBCA","IVZ","ISBC","IRET","IRS","IFC","ISAT","IBB","SFI","ITC","MAYS","JAXB","JXSB","JAG","JNS","JFBI","JFBC","JLI","JFC","JLL","KFED","KED","KYE","KYN","KRNY","KENT","KFFB","KRC","KIM","KFS","KRG","KFN","KCAP","LSBK","LBAI","LKFN","LPSB","LHO","LAZ","LEGC","LXP","LRY","LNC","LAD","LNBB","L","LOOP","LABC","LSBX","LSBI","LTC","MTB","MCBC","MACC","MAC","CLI","MPG","MGYR","MAIN","MAM","MSFG","MLVF","LOAN","MFC","MKL","MI","MXGL","MFLR","MBFI","MBTF","MCGC","MIG","TAXI","MPW","MBR","MBWM","MIGP","MBVT","MCY","EBSB","MERR","MTR","CASH","MV","MET","MDH","MIM","MFI","MPB","MAA","MBRG","MSL","MBHI","MSW","MMCE","MCBF","MNRK","MNRTA","MROE","MRH","MCO","RNE","ICB","MORN","MSBF","MFSF","MVC","NARA","NASB","NKSH","NHI","NATL","NPBC","NNN","NSEC","NWLI","NHP","NVSL","NAVG","NBTF","NBTB","NNI","NHS","NCBC","NEBS","NEN","NHTB","NYB","NAL","NBBC","NCT","NFSB","NEWS","NGPC","NICK","NOK","FFFD","NOVB","NBN","NECB","NSFC","NTRS","NFBK","NRIM","NRF","NWSB","NWFL","NRG","NST","NLP","NCA","NMI","NNY","NYM","OVLY","OCN","ORH","OGE","OLCB","OVBC","OLBK","ONB","OPOF","OSBC","OHI","OLP","OB","ONFC","OPEN","OPHC","OFG","ORN","ORIT","IX","ORA","ORRF","OTTR","PABK","PCBC","PCBK","PMBC","PCE","PPBI","PSBC","PACW","PBCI","PFED","PRK","PKBK","PVSA","PKY","PRE","PBHC","PCAP","PNBK","PGC","PNNT","PWOD","PEI","PAG","PNSN","PBCT","PEBO","PEBK","PCBI","PFBX","POM","PEO","PHH","PNX","PNFP","PNW","PTP","PCL","PLBC","PCC","PNC","PBIB","POR","PPS","PPL","PFBC","PFBI","PRWT","PLFE","PRS","PNBC","PFG","PVTB","PRA","PGN","PGR","PLD","PSEC","PRSP","PL","PCBS","PROV","PFS","PBNY","PBIP","PRU","PUK","PSB","PSBH","PSA","PULB","PIM","PVFC","PZN","QCCO","QCRH","RPFG","RAS","RPT","RAND","RYN","O","ENL","REG","RF","RNR","RNST","RBCAA","FRBK","REXI","RSO","RGCO","RINO","RMG","RIVR","RVSB","RLI","RPI","RCKB","ROMA","ROME","RST","RBPAA","RY","RVT","RRI","RBNF","STBA","SYBT","SAFT","SAL","SMHG","SASR","BFS","SAVB","SCG","SCBT","SBX","SBCF","SBKC","SEIC","SIGI","SRE","SNH","SVBI","SMED","SHW","SHBI","SIFI","SBNY","SGI","SVLF","SLG","SLM","SWI","SOMH","SAH","SOR","TSFG","SJI","SOCB","SCMF","SO","SSE","PCU","SFST","SMBC","SONA","SUG","SBSI","OKSB","SWX","SGB","SSS","SUAI","JOE","STFC","STBC","STT","STEL","STL","SBIB","STBK","STSA","SLT","SSFN","BEE","STRS","STU","SUBK","SMMF","SSBI","SAMB","SNBC","SUI","SLF","SHO","STI","SUPR","SPPR","SUSQ","SBBX","SIVB","SNV","TROW","TFC","TAMB","SKT","TGB","TCO","TAYC","TCB","TSH","TE","TRC","TEI","TNCC","TCBI","THRD","TFSL","TPGI","TWPG","TIBB","TICC","TDBK","TONE","TSBK","TMP","TMK","TD","TTO","TYY","TYG","TYN","TOBC","TOFC","TWGP","TOWN","TRAD","TAC","TRH","TRP","TCI","TRV","TREE","TY","TCAP","TCBK","TRST","TRMK","TKF","YSI","USB","GROW","UBS","UDR","UGI","UIL","UMBF","UMH","UMPQ","UNAM","UBSH","UNB","UNS","INDM","UBCP","UBOH","UBSI","UCBA","UCBI","UCFC","UFCS","UBFO","USBI","UTL","UTR","UNTY","UHT","UVE","UTA","UVSP","UBA","VR","VYFC","VLY","VALU","VKQ","VVC","VTR","VRTA","VRTB","VBFC","VCBI","VRTS","VIST","VNO","VSBN","WHI","WPC","WRB","WBNK","WAIN","WAC","WBCO","WFSL","WRE","WASH","WSCC","WSBF","WAYN","WBS","WRI","WFC","WSBC","WTBA","WCBO","WABC","WR","WFD","WHG","WGL","WGNB","WTM","RVR","WTNY","GIW","WL","WIBC","WOC","FUR","WTFC","WEC","WRLD","WSB","WSFS","WVFC","XEL","XL","YAVY","ZBB","ZNT","ZION","ZIPR"};
		// List<Security> seList = this.getSecurities(arr);
		// System.out.println(seList.size());

		List<String> stocks = getAllStocks();
		List<Security> seList = new ArrayList<Security>();
		for (int i = 0; i < stocks.size(); i++) {
			try {
				seList.add(this.get(stocks.get(i)));
			} catch (Exception e) {
				System.out.println(stocks.get(i));
			}
		}
		Date updateDate = date;
		Long rank = 0L;
		Double return_52w = Double.NEGATIVE_INFINITY;

		seList = com.lti.service.bo.RelativeStrength.getSortedSecurity(seList, updateDate);
		for (int i = 0; i < seList.size(); i++) {
			Security se = seList.get(i);
			Date startDate = this.getStartDate(se.getID());
			Date tmpDate = new Date();
			if (startDate != null && !(LTIDate.getNewDate(tmpDate, TimeUnit.WEEKLY, -52).before(startDate))) {
				try {
					return_52w = se.getReturn(updateDate, TimeUnit.WEEKLY, -52);
				} catch (Exception e) {
				}

				if (this.getRS(se.getSymbol(), updateDate) == null) {
					RelativeStrength rs = new RelativeStrength();
					rank = rs.getSecurityRank(se.getSymbol(), seList);
					rs.setDate(updateDate);
					rs.setSecurityID(se.getID());
					rs.setSymbol(se.getSymbol());
					System.out.println(rs.getSymbol());
					rs.setSecurityType(se.getSecurityType());
					if (return_52w == Double.NEGATIVE_INFINITY)
						rs.setReturn_52w(null);
					else
						rs.setReturn_52w(return_52w);
					System.out.println(rs.getReturn_52w());
					rs.setRank(rank);
					// System.out.println(rs.getRank());
					this.saveRS(rs);
					Configuration.writeLog(se.getSymbol(), logDate, updateDate, " update Relative Strength Data!");
					// System.out.println(se.getSymbol()+";"+logDate+";"+updateDate+" update Relative Strength Data!");
				}
				tmpDate = LTIDate.getNewDate(tmpDate, TimeUnit.MONTHLY, -1);
			}
		}
		Configuration.writeLog("ALL Stock", logDate, updateDate, "\n************************************\n Relative Strength Update finished!\n************************************\n");
	}

	@Override
	public void getStockRS(Security se, Date logDate, boolean update) {
		List<Security> seList = getSecuritiesByType(5);
		Date curDate = new Date();
		Date lastMonthEnd = LTIDate.getMonthEnd(LTIDate.getLastMonthDate(curDate));
		Long rank = 0L;
		Double return_52w = Double.NEGATIVE_INFINITY;

		try {
			return_52w = se.getReturn(lastMonthEnd, TimeUnit.WEEKLY, -52);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.getRS(se.getSymbol(), lastMonthEnd) == null) {
			RelativeStrength rs = new RelativeStrength();
			rank = rs.getSecurityRank(se.getSymbol(), rs.getSortedSecurity(seList, lastMonthEnd));
			rs.setDate(lastMonthEnd);
			rs.setSecurityID(se.getID());
			rs.setSymbol(se.getSymbol());
			rs.setSecurityType(se.getSecurityType());
			if (return_52w == Double.NEGATIVE_INFINITY)
				rs.setReturn_52w(null);
			else
				rs.setReturn_52w(return_52w);
			rs.setRank(rank);
			this.saveRS(rs);
			Configuration.writeLog(se.getSymbol(), logDate, curDate, " update Relative Strength Data!");
		}
	}

	@Override
	public RelativeStrength getRS(Long id) {
		return (RelativeStrength) getHibernateTemplate().get(RelativeStrength.class, id);
	}

	@Override
	public RelativeStrength getRS(String symbol, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RelativeStrength.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Date", date));
		List<RelativeStrength> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public void removeRS(Long id) {
		RelativeStrength rs = getRS(id);
		if (rs != null) {
			getHibernateTemplate().delete(rs);
		}
	}

	@Override
	public void saveRS(RelativeStrength rs) {
		getHibernateTemplate().save(rs);
	}

	@Override
	public void updateRS(RelativeStrength rs) {
		getHibernateTemplate().update(rs);
	}

	/**
	 * it should search the quoted portfolio on the holding table.
	 * 
	 * @param symbol
	 * @return
	 */
	@Deprecated
	public List<Portfolio> getQuotedPortfolios(String symbol) {
		// if(symbol == null || symbol.equals(""))
		// return null;
		// Security security = this.getBySymbol(symbol);
		// if(security == null)
		// return null;
		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(SecurityItem.class);
		// detachedCriteria.add(Restrictions.eq("SecurityID",
		// security.getID()));
		// List<SecurityItem> securityItems = findByCriteria(detachedCriteria);
		// if(securityItems == null || securityItems.size() == 0)
		// return null;
		// List<Long> assetIDs = new ArrayList<Long>();
		// for(int i = 0; i < securityItems.size(); i++){
		// SecurityItem si = securityItems.get(i);
		// Long assetID = si.getAssetID();
		// if(assetIDs.contains(assetID) == false){
		// assetIDs.add(assetID);
		// }
		// }
		// DetachedCriteria detachedCriteria2 =
		// DetachedCriteria.forClass(Asset.class);
		// detachedCriteria2.add(Restrictions.in("ID", assetIDs));
		// List<Asset> assets = findByCriteria(detachedCriteria2);
		// if(assets == null || assets.size() == 0)
		// return null;
		// List<Long> portfolioIDs = new ArrayList<Long>();
		// for(int i = 0; i < assets.size(); i++){
		// Asset asset = assets.get(i);
		// Long portfolioID = asset.getPortfolioID();
		// if(portfolioIDs.contains(portfolioID) == false)
		// portfolioIDs.add(portfolioID);
		// }
		// DetachedCriteria detachedCriteria3 =
		// DetachedCriteria.forClass(Portfolio.class);
		// detachedCriteria3.add(Restrictions.in("ID", portfolioIDs));
		// List<Portfolio> portfolios = findByCriteria(detachedCriteria3);
		// if(portfolios == null || portfolios.size() == 0)
		// return null;
		// return portfolios;
		return null;
	}

	/***************************
	 * For Fund
	 * Alert************************************************************
	 * ************ public List<FundAlert> getFundAlertBySecurityID(long
	 * securityID){ List<FundAlert> fundAlertList = new ArrayList<FundAlert>();
	 * DetachedCriteria detachedCriteria =
	 * DetachedCriteria.forClass(FundAlert.class);
	 * detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
	 * fundAlertList = findByCriteria(detachedCriteria); return fundAlertList; }
	 * 
	 * public FundAlert getFundAlertBySecurityID(long securityID, Date date) {
	 * List<FundAlert> fundAlertList = new ArrayList<FundAlert>();
	 * DetachedCriteria detachedCriteria =
	 * DetachedCriteria.forClass(FundAlert.class);
	 * detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
	 * detachedCriteria.add(Restrictions.eq("Date", date)); fundAlertList =
	 * findByCriteria(detachedCriteria); if(fundAlertList != null &&
	 * fundAlertList.size()>0)return fundAlertList.get(0); else return null; }
	 * public List<FundAlert> getAllFundAlert() { List<FundAlert> fundAlertList
	 * = new ArrayList<FundAlert>(); DetachedCriteria detachedCriteria =
	 * DetachedCriteria.forClass(FundAlert.class); fundAlertList =
	 * findByCriteria(detachedCriteria); return fundAlertList; } public void
	 * deleteFundAlert(long securityID) {
	 * deleteByHQL("from FundAlert where SecurityID=" + securityID); } /
	 *******************************************************************************************************************/

	@Override
	public List<Strategy> getQuotedStrategiesByOrder(String symbol, int size, int order, int year) {
		return this.getQuotedStrategiesByOrder(symbol, null, size, order, year);
	}

	@Override
	public List<Strategy> getQuotedStrategies(String symbol, int size) {
		return this.getQuotedStrategies(symbol, null, size);
	}

	@Deprecated
	@Override
	public List<Strategy> getQuotedStrategies(String symbol, Long userID, int size) {
		// List<Portfolio> portfolios = getQuotedPortfolios(symbol);
		// if(portfolios == null)
		// return null;
		// List<Long> strategyIDs = new ArrayList<Long>();
		// int count = 0;
		// for(int i = 0; i < portfolios.size(); i++){
		// Portfolio p = portfolios.get(i);
		// if(p.getIsModelPortfolio() != null && p.getIsModelPortfolio() == true
		// && p.getMainStrategyID() != null){
		// Long strategyID = p.getMainStrategyID();
		// if(strategyIDs.contains(strategyID) == false){
		// strategyIDs.add(strategyID);
		// count++;
		// }
		// if(size != 0 && count >= size)
		// break;
		// }
		// }
		// DetachedCriteria detachedCriteria4 =
		// DetachedCriteria.forClass(Strategy.class);
		// detachedCriteria4.add(Restrictions.in("ID", strategyIDs));
		// if(userID!=null&&!userID.equals(Configuration.SUPER_USER_ID)){
		// detachedCriteria4.add(Restrictions.eq("UserID", userID));
		// }
		// List<Strategy> strategies = findByCriteria(detachedCriteria4);
		// return strategies;
		return null;
	}

	@Deprecated
	@Override
	public List<Strategy> getQuotedStrategiesByOrder(String symbol, Long userid, int size, int order, int year) {
		// List<Portfolio> portfolios = getQuotedPortfolios(symbol);
		// if(portfolios == null)
		// return null;
		// List<Long> strategyIDs = new ArrayList<Long>();
		// for(int i = 0; i < portfolios.size(); i++){
		// Portfolio p = portfolios.get(i);
		// if(p.getIsModelPortfolio() != null && p.getIsModelPortfolio() == true
		// && p.getMainStrategyID() != null){
		// Long strategyID = p.getMainStrategyID();
		// if(strategyIDs.contains(strategyID) == false){
		// strategyIDs.add(strategyID);
		// }
		// }
		// }
		// if(strategyIDs==null||strategyIDs.size()==0)return null;
		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(PortfolioMPT.class);
		// detachedCriteria.add(Restrictions.in("strategyID", strategyIDs));
		// detachedCriteria.add(Restrictions.eq("year", year));
		//
		//
		// switch (order) {
		// case PortfolioMPT.SORT_BY_ALPHA:
		// detachedCriteria.addOrder(Order.desc("alpha"));
		// break;
		// case PortfolioMPT.SORT_BY_AR:
		// detachedCriteria.addOrder(Order.desc("AR"));
		// break;
		// case PortfolioMPT.SORT_BY_BETA:
		// detachedCriteria.addOrder(Order.desc("beta"));
		// break;
		// case PortfolioMPT.SORT_BY_DRAWDOWN:
		// detachedCriteria.addOrder(Order.desc("drawDown"));
		// break;
		// case PortfolioMPT.SORT_BY_RSQUARED:
		// detachedCriteria.addOrder(Order.desc("RSquared"));
		// break;
		// case PortfolioMPT.SORT_BY_SHARPERATIO:
		// detachedCriteria.addOrder(Order.desc("sharpeRatio"));
		// break;
		// case PortfolioMPT.SORT_BY_STANDARDDEVIATION:
		// detachedCriteria.addOrder(Order.desc("standardDeviation"));
		// break;
		// case PortfolioMPT.SORT_BY_TREYNORRATIO:
		// detachedCriteria.addOrder(Order.desc("treynorRatio"));
		// break;
		// default:
		// break;
		// }
		// List<PortfolioMPT> mpts = findByCriteria(detachedCriteria);
		// //List<Long> sortedStrategyIDs = new ArrayList<Long>();
		// List<Strategy> strategies = new ArrayList<Strategy>();
		// StrategyManager strategyManager = ContextHolder.getStrategyManager();
		// List<Long> sortedStrategyIDs = new ArrayList<Long>();
		// int count = 0;
		// if (mpts != null) {
		// for (int i = 0; i < mpts.size(); i++) {
		// PortfolioMPT pm = mpts.get(i);
		// Long strategyID = pm.getStrategyID();
		// if (sortedStrategyIDs.contains(strategyID) == false) {
		// Strategy strategy = strategyManager.get(strategyID);
		// if
		// (userid==null||userid.equals(Configuration.SUPER_USER_ID)||userid.equals(strategy.getUserID()))
		// {
		// strategies.add(strategy);
		// sortedStrategyIDs.add(strategyID);
		// strategyIDs.remove(strategyID);
		// count++;
		// }
		// }
		// if(size > 0 && count >= size)
		// break;
		// }
		// }
		//
		// if ((count < size || size == 0) && (strategyIDs != null &&
		// strategyIDs.size() != 0)) {
		// DetachedCriteria detachedCriteria2 =
		// DetachedCriteria.forClass(Strategy.class);
		// detachedCriteria2.add(Restrictions.in("ID", strategyIDs));
		// if(userid!=null&&!userid.equals(Configuration.SUPER_USER_ID)){
		// detachedCriteria2.add(Restrictions.eq("UserID", userid));
		// }
		// List<Strategy> others =
		// strategyManager.getStrategies(detachedCriteria2);
		// if(size == 0){
		// strategies.addAll(others);
		// }
		// else
		// {
		// for(int i = 0; i < size - count && i < others.size(); i++){
		// strategies.add(others.get(i));
		// }
		// }
		// }
		// return strategies;
		return null;
	}

	@Override
	public Double getRSGrade(String symbol, int Type, Date date) {
		/*
		 * List<Security>seList = getSecuritiesByType(5); Date tmpDate =
		 * LTIDate.getLastNYSETradingDayOfMonth(date); Date lastPriceDate =
		 * this.get(symbol).getPriceLastDate(); if(date.after(lastPriceDate)){
		 * tmpDate = LTIDate.getMonthEnd(LTIDate.getLastMonthDate(date)); }
		 * DetachedCriteria detachedCriteria =
		 * DetachedCriteria.forClass(RelativeStrength.class);
		 * detachedCriteria.add(Restrictions.eq("Date", tmpDate));
		 * List<RelativeStrength> bolist =
		 * super.findByCriteria(detachedCriteria); if (bolist.size()>= 1){
		 * RelativeStrength rs = getRS(symbol, tmpDate); Long rank =
		 * rs.getRank(); //System.out.println(bolist.size()); double grade =
		 * 100-100*rank/bolist.size(); return grade; } else return 0.0;
		 */
		int year = LTIDate.getYear(date);
		int month = date.getMonth() + 1;
		String sql = "SELECT rank FROM `ltisystem_relativestrength`where year(date)=" + year + " and month(date)=" + month + " and symbol='" + symbol + "';";
		String sql1 = "SELECT count(symbol) FROM `ltisystem_relativestrength`where year(date)=" + year + " and month(date)=" + month + ";";
		BigInteger size = BigInteger.ZERO;

		try {
			List list1 = super.findBySQL(sql1);
			Object obj = (Object) list1.get(0);
			size = (BigInteger) obj;
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		try {
			List list = super.findBySQL(sql);
			Object obj = (Object) list.get(0);
			BigInteger v = (BigInteger) obj;
			Double grade = 100.0 - 100.0 * v.doubleValue() / size.doubleValue();
			return grade;
		} catch (Exception e) {
			return null;
		}
	}

	// Get the Open price of one security
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
		} else
			return sdd.getOpen();
	}

	/*********************************************************************************/
	/* Get Stocks By Sector Or Industry */
	/*********************************************************************************/

	@Override
	public List<String> getStockByIndustry(String industry) {
		String sql = "SELECT distinct s.symbol FROM ltisystem.ltisystem_security s left join ltisystem_company c on s.symbol=c.symbol  where industry ='" + industry + "'";
		List<String> stocks = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String symbol = (String) objs;
					stocks.add(symbol);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return stocks;
	}

	@Override
	public List<String> getStockBySector(String sector) {
		String sql = "SELECT distinct s.symbol FROM ltisystem.ltisystem_security s left join ltisystem_company c on s.symbol=c.symbol  where sector ='" + sector + "'";
		List<String> stocks = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String symbol = (String) objs;
					stocks.add(symbol);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return stocks;
	}

	@Override
	public List<String> getStockByIndustry(String[] industries) {
		String sql = "SELECT distinct s.symbol FROM ltisystem.ltisystem_security s left join ltisystem_company c on s.symbol=c.symbol  where industry ='" + industries[0] + "'";
		for (int i = 1; i < industries.length; i++)
			sql += " or industry ='" + industries[i] + "'";
		List<String> stocks = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String symbol = (String) objs;
					stocks.add(symbol);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return stocks;
	}

	@Override
	public List<String> getStockBySector(String[] sectors) {
		String sql = "SELECT distinct s.symbol FROM ltisystem.ltisystem_security s left join ltisystem_company c on s.symbol=c.symbol  where sector ='" + sectors[0] + "'";
		for (int i = 1; i < sectors.length; i++)
			sql += " or sector ='" + sectors[i] + "'";
		List<String> stocks = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String symbol = (String) objs;
					stocks.add(symbol);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return stocks;
	}

	@Override
	public List<String> getIndustryBySector(String[] sector) {
		String sql = "SELECT distinct industry FROM ltisystem_company l where sector='" + sector[0] + "'";
		for (int i = 1; i < sector.length; i++)
			sql += " or sector ='" + sector[i] + "'";
		List<String> industries = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String industry = (String) objs;
					industries.add(industry);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return industries;
	}

	@Override
	public List<String> getIndustryBySector(String sector) {
		String sql = "SELECT distinct industry FROM ltisystem_company l where sector='" + sector + "'";
		List<String> industries = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String industry = (String) objs;
					industries.add(industry);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return industries;
	}

	@Override
	public String getIndustry(String symbol) {
		String sql = "SELECT industry FROM `ltisystem`.`ltisystem_company`where symbol='" + symbol + "'";
		String industry;
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				Object obj = (Object) list.get(0);
				industry = (String) obj;
				return industry;
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	/***  function: get all the stocks  ***/
	public List<String> getAllStocks() {
		String sql = "SELECT symbol FROM `ltisystem_company`where sector!=''";
		List<String> stocks = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String stock = (String) objs;
					stocks.add(stock);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return stocks;
	}

	public void addListener(DailyUpdateListener dailyUpdateListener) {
		dailyUpdateListenerList.add(dailyUpdateListener);
	}

	public void removeListener(DailyUpdateListener dailyUpdateListener) {
		dailyUpdateListenerList.remove(dailyUpdateListener);
	}

	/**
	 * @author CCD update the security daily data and set the security
	 *         pricelastdate to be latest
	 * @param sdds
	 *            the securityDailyDataList need to be saved
	 */
	public void updateSecurityEndDate(List<SecurityDailyData> sdds) {
		int i;
		SecurityDailyData securityDailyData = null;
		Security se;
		if (sdds != null && sdds.size() > 0) {
			for (i = 0; i < sdds.size(); ++i) {
				securityDailyData = sdds.get(i);
				se = this.get(securityDailyData.getSecurityID());
				if (se.getEndDate() == null || securityDailyData.getDate().after(se.getEndDate())) {
					se.setEndDate(securityDailyData.getDate());
					se.setPriceLastDate(securityDailyData.getDate());
					this.update(se);
				}
			}
		}
	}

	public void updateSecurityNAVLastDate(List<SecurityDailyData> sdds) {
		int i;
		SecurityDailyData securityDailyData = null;
		Security se;
		if (sdds != null && sdds.size() > 0) {
			for (i = 0; i < sdds.size(); ++i) {
				securityDailyData = sdds.get(i);
				se = this.get(securityDailyData.getSecurityID());
				if (se.getNavLastDate() == null || securityDailyData.getDate().after(se.getNavLastDate())) {
					se.setNavLastDate(securityDailyData.getDate());
					this.update(se);
				}
			}
		}
	}

	/**
	 * @author CCD
	 * @param date
	 *            the enddate of ten year real earnings data
	 * @return return the average real earnings in ten years
	 */
	public Double getShillerSP500CAPE10(Date date) throws NoPriceException {
		Date enddate = LTIDate.getFirstDateOfMonth(date);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ShillerSP500.class);
		detachedCriteria.add(Restrictions.eq("SPDate", enddate));
		List<ShillerSP500> shillerSP500List = findByCriteria(detachedCriteria);
		if (shillerSP500List != null && shillerSP500List.size() > 0)
			return shillerSP500List.get(0).getAvgPriceEarnings();
		NoPriceException npe = new NoPriceException();
		npe.setSecurityName("ShillerSP500");
		npe.setDate(enddate);
		throw npe;
	}

	public ShillerSP500 getShillerSP500Data(Date date) throws NoPriceException {
		Date enddate = LTIDate.getFirstDateOfMonth(date);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ShillerSP500.class);
		detachedCriteria.add(Restrictions.eq("SPDate", enddate));
		List<ShillerSP500> shillerSP500List = findByCriteria(detachedCriteria);
		if (shillerSP500List != null && shillerSP500List.size() > 0)
			return shillerSP500List.get(0);
		NoPriceException npe = new NoPriceException();
		npe.setSecurityName("ShillerSP500");
		npe.setDate(enddate);
		throw npe;
	}

	public void saveAllCompanyFund(List<CompanyFund> companyFundList) {
		this.saveAll(companyFundList);
	}

	public static void main(String[] args) {
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		sm.getQuality("agg".toUpperCase());
		// Date d1=LTIDate.getDate(2007, 9, 1);
//		sm.getAllSecurityMPT(new Date(), true);
		// Date date = LTIDate.getDate(2009, 8, 1);
		// ShillerSP500 shillerSP500 = sm.getShillerSP500Data(date);
		// System.out.println(shillerSP500.getAvgPriceEarnings());
		// System.out.println(shillerSP500.getAvgPricePeakEarnings());
		// System.out.println(shillerSP500.getAvgEarnings10());
		// System.out.println(shillerSP500.getRealEarnings());
		// System.out.println(shillerSP500.getRealPrice());
		// System.out.println(shillerSP500.getPeakEarnings10());
		// System.out.println(shillerSP500.getPriceEarningsRatio());
		// System.out.println(shillerSP500.getPricePeakEarnings10());
		// sm.setSecurityMPTIncrementalData();

		// Date d1=LTIDate.getDate(2009, 4, 1);
		// Date d2=LTIDate.getDate(2009, 9, 10);
		// for(int i=0;i<LTIDate.calculateInterval(d1, d2);i++){
		// Date d = LTIDate.getNewDate(d1, TimeUnit.DAILY, i);
		// if(LTIDate.isLastNYSETradingDayOfMonth(d)){
		// sm.getHistoricalRS(d, d2, true);
		// }
		// }
		// sm.getAllSecurityMPT(new Date(), true);
		// sm.calculateFundQuality();
		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(Security.class);
		// List<Security> securityList;
		// sm.getOneSecurityMPT(4878L);
		// sm.getAllSecurityMPT(new Date(), true);
		// detachedCriteria.add(Restrictions.or(Restrictions.eq("SecurityType",
		// 1), Restrictions.eq("SecurityType", 4)));
		// securityList = sm.getSecurities(detachedCriteria);
		// System.out.println(securityList.size());
		// detachedCriteria.add(Restrictions.eq("SecurityType", 1));
		// securityList = sm.getSecurities(detachedCriteria);
		// System.out.println(securityList.size());
		// detachedCriteria.add(Restrictions.eq("SecurityType", 4));
		// securityList = sm.getSecurities(detachedCriteria);
		// System.out.println(securityList.size());
		// for(int i=0;i<securityList.size();++i)
		// System.out.println(i+" "+ securityList.get(i).getSymbol());

		// Date logDate=new Date();
		// sm.getAllStockRS(logDate, true);
		//
		// List<String>stocks = sm.getAllStocks();
		// for(int i=0;i<stocks.size();i++){
		// System.out.println(stocks.get(i));
		// }
	}

	@Override
	public CompanyFund getCompanyFundByTicker(String ticker) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyFund.class);
		detachedCriteria.add(Restrictions.eq("Ticker", ticker));
		List<CompanyFund> companyFundList = this.findByCriteria(detachedCriteria);
		if (companyFundList != null && companyFundList.size() > 0)
			return companyFundList.get(0);
		return null;
	}

	@Override
	public List<CompanyFund> getCompanyFundListByCompany(String company) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyFund.class);
		detachedCriteria.add(Restrictions.eq("Company", company));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public List<CompanyFund> getCompanyFundListByCompanyAndCategory(String company, String category) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyFund.class);
		detachedCriteria.add(Restrictions.eq("Company", company));
		detachedCriteria.add(Restrictions.eq("Category", category));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void saveOrUpdateAllCompanyFund(List<CompanyFund> companyFundList) {
		this.saveOrUpdateAll(companyFundList);
	}

	@Override
	public boolean hasFundsForCompany(String company) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyFund.class);
		detachedCriteria.add(Restrictions.eq("Company", company));
		List<CompanyFund> companyFundList = this.findByCriteria(detachedCriteria, 1, 0);
		if (companyFundList != null && companyFundList.size() > 0)
			return true;
		return false;
	}

	@Override
	public void saveCompanyFund(CompanyFund companyFund) {
		this.getHibernateTemplate().save(companyFund);
	}

	@Override
	public List<String> getAssetNameListByCompanyName(String company) {
		String companyCompare = "";
		if (!company.equals("All"))
			companyCompare = " company = \"" + company + "\" AND";
		String sql = "SELECT distinct category FROM " + Configuration.TABLE_COMPANYFUND + " WHERE " + companyCompare + " category is not null AND category !=\"\" order by category asc";
		List<String> assetNameList = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String category = (String) objs;
					assetNameList.add(category);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return assetNameList;
	}

	@Override
	public List<String> getCompanyNameList() {
		String sql = "SELECT distinct company FROM " + Configuration.TABLE_COMPANYFUND + " order by company asc";
		List<String> companyList = new ArrayList<String>();
		try {
			List list = this.findBySQL(sql);
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object objs = (Object) list.get(i);
					String company = (String) objs;
					companyList.add(company);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return companyList;
	}

	@Override
	public List<CompanyFund> getCompanyFundListByCompanyAndCategorys(String company, String category1, String category2, String category3) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyFund.class);
		if (!company.equals("All"))
			detachedCriteria.add(Restrictions.eq("Company", company));
		if (!category1.equals("All")) {
			SimpleExpression restriction1 = null;
			SimpleExpression restriction2 = null;
			SimpleExpression restriction3 = null;
			SimpleExpression currentRestriction = null;
			LogicalExpression restriction = null;
			if (!category1.equals(""))
				restriction1 = Restrictions.eq("Category", category1);
			if (!category2.equals(""))
				restriction2 = Restrictions.eq("Category", category2);
			if (!category3.equals(""))
				restriction3 = Restrictions.eq("Category", category3);
			if (restriction1 != null)
				currentRestriction = restriction1;
			if (restriction2 != null) {
				if (currentRestriction == null)
					currentRestriction = restriction2;
				else
					restriction = Restrictions.or(currentRestriction, restriction2);
			}
			if (restriction3 != null) {
				if (currentRestriction == null)
					currentRestriction = restriction3;
				else {
					if (restriction != null)
						restriction = Restrictions.or(restriction, restriction3);
					else
						restriction = Restrictions.or(currentRestriction, restriction3);
				}
			}
			if (restriction != null)
				detachedCriteria.add(restriction);
			else if (currentRestriction != null)
				detachedCriteria.add(currentRestriction);
		}
		detachedCriteria.addOrder(Order.asc("Category"));
		detachedCriteria.addOrder(Order.asc("MSName"));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void addAllVAFunds(List<VAFund> vaFundList) {
		this.saveAll(vaFundList);
	}

	@Override
	public void addVAFund(VAFund vaFund) {
		this.getHibernateTemplate().save(vaFund);
	}

	@Override
	public VAFund getVAFundByBarronName(String barronName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VAFund.class);
		detachedCriteria.add(Restrictions.eq("BarronName", barronName));
		List<VAFund> vaFundList = this.findByCriteria(detachedCriteria, 1, 0);
		if (vaFundList != null && vaFundList.size() > 0)
			return vaFundList.get(0);
		return null;
	}

	@Override
	public VAFund getVAFundByTicker(String ticker) {
		return null;
	}

	@Override
	public List<CompanyFund> getCompanyFundList() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyFund.class);
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public List<VAFund> getVAFundList() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VAFund.class);
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void saveOrUpdateVAFunds(List<VAFund> vaFundList) {
		this.saveOrUpdateAll(vaFundList);
	}

	@Override
	public List<Security> getSecuritiesBeforeEndDate(Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.lt("EndDate", endDate));
		return this.getSecurities(detachedCriteria);
	}

	@Override
	public List<Security> getSecuritiesByEndDate(Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("EndDate", endDate));
		return this.getSecurities(detachedCriteria);
	}
	public List<Security> getSecuritiesLessByType(int type)
	{
		Session session = null;
		final String hql = "FROM Security a where a.SecurityType < :type";
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createQuery(hql);
			query.setParameter("type", type);
			List<Security> result = query.list();
			return result;
		} catch (Exception e) {
			throw new RuntimeException("sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}
	public boolean updateAr(String symbol) {
		boolean b = false;
		BaseFormulaUtil baseFormulaUtil = new BaseFormulaUtil();
		try {
			Security security = this.getBySymbol(symbol);
			List<SecurityMPT> list = baseFormulaUtil.computeSecurityMPTForAR(security);
			this.saveOrUpdateAllSecurityMPT(list);
			b = true;
		} catch (Exception e) {
			b = false;
		}
		return b;
	}

	private static int process;
	private static int sumProcess;

	public int getProcess() {
		return process;
	}

	public int getSumProcess() {
		return sumProcess;
	}

	
	public void updateAllAr() {
		BaseFormulaUtil baseFormulaUtil = new BaseFormulaUtil();
		List<Security> securityes = this.getSecuritiesLessByType(6);
		sumProcess = securityes.size();
		for (int i = 0; i < securityes.size(); i++) {
			if(!isRun) 
			{
				isRun = true;
				break;
			}
			try {
				List<SecurityMPT> list = baseFormulaUtil.computeSecurityMPTForAR(securityes.get(i));
				this.saveOrUpdateAllSecurityMPT(list);
			} catch (Exception e) {
				//e.printStackTrace();
			}
			process = i;
		}
	}
	
	public static boolean isRun = true;
	
	public void setIsRun(boolean b){
		isRun = b;
	}

	public boolean updateMpt(String symbol) {
		boolean b = false;
		Security security = this.getBySymbol(symbol.trim().toUpperCase());
		try {
			this.getOneSecurityMPT(security.getID());
			b = true;
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

	public void updateAllMpt() {
		List<Security> securityes = this.getSecuritiesLessByType(6);
		sumProcess = securityes.size();
		for (int i = 0; i < securityes.size(); i++) {
			if(!isRun) 
			{
				isRun = true;
				break;
			}
			try {
				this.getOneSecurityMPT(securityes.get(i).getID());
			} catch (Exception e) {
				e.printStackTrace();
			}
			process = i;
		}
	}
	public List<SecurityDailyData> searchSecurityDailyData(String symbol, Date startDate, Date endDate)
	{
		Session session = null;
		List<SecurityDailyData> list = new ArrayList<SecurityDailyData>();
		try {
			session = this.getHibernateTemplate().getSessionFactory().openSession();
			final String sql = "FROM SecurityDailyData as a where a.SecurityID = (select b.id from Security as b where b.Symbol = :symbol) and a.Date in(:startDate,:endDate) order by a.Date desc)";
			Query query = session.createQuery(sql);
			query.setParameter("symbol", symbol);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			list = query.list();
			System.out.println(list.size());
			for(SecurityDailyData date : list)
			{
				System.out.println(date.getDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(session != null)
			{
				session.flush();
				session.clear();
				session.close();
			}
		}
		
		return list;
	}
	public List<Object[]> getQuality(String symbol)
	{
		Session session = null;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			session = this.getHibernateTemplate().getSessionFactory().openSession();
			final String hql = "select a.Quality,a.Symbol FROM Security a where a.ClassID = (select b.ClassID from Security b where b.Symbol = :symbol) and a.SecurityType = (select c.SecurityType from Security c where c.Symbol = :symbol2) order by a.Quality desc";
//			final String hql = "select a.Quality,a.Symbol FROM Security a ,select b.ClassID,b.SecurityType from Security b where b.Symbol = :symbol c where a.ClassID = c.ClassID and a.SecurityType = c.SecurityType order by a.Quality desc";
			Query query = session.createQuery(hql);
			query.setFirstResult(0);
			query.setMaxResults(5);
			query.setParameter("symbol", symbol);
			query.setParameter("symbol2", symbol);
			list = query.list();
			for(Object[] date : list)
			{
				System.out.println(date[1] + ":" +date[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if(session != null)
			{
				session.flush();
				session.clear();
				session.close();
			}
		}
		return list;
	}
	
}
