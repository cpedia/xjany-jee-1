package com.lti.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.cache.LTICache;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

public class SecurityManagerImplTest extends TestCase {

	private SecurityManagerImpl sm1;
	private SecurityManagerImplWithCache sm2;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private boolean isDebug = false;

	public SecurityManagerImplTest() {
		// TODO Auto-generated constructor stub
		sm1 = new SecurityManagerImpl();
		sm2 = new SecurityManagerImplWithCache();
		sm1.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
		sm2.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));

	}

	public static void main(String[] args) {
		SecurityManagerImplWithCache sm2= new SecurityManagerImplWithCache();
		sm2.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
		List<Security> ss=sm2.getSecurities();	
		long t1=System.currentTimeMillis();
		for(int i=0;i<2*250;i++){
			try {
				long t3=System.currentTimeMillis();
				double d=sm2.getAdjPrice(ss.get(i%250).getID(),LTIDate.getDate(2008,12,31));
				long t4=System.currentTimeMillis();
				System.out.println("["+i%250+"]"+(t4-t3)+"\t\t"+d);
			} catch (NoPriceException e) {
				e.printStackTrace();
			}
		}
		try {
			LTICache.getSecurityDailyDataCache().removeAll();
			//System.gc();
			//System.out.println(Runtime.getRuntime().freeMemory());
			Thread.sleep(6*60*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<2*250;i++){
			try {
				long t3=System.currentTimeMillis();
				sm2.getAdjPrice(ss.get(i%250).getID(),LTIDate.getDate(2008,12,31));
				long t4=System.currentTimeMillis();
				System.out.println("["+i%250+"]"+(t4-t3));
			} catch (NoPriceException e) {
				e.printStackTrace();
			}
		}
		long t2=System.currentTimeMillis();
		System.out.println("time:"+(t2-t1));
	}

	private Date[] dates = { LTIDate.getDate(1990, 2, 3), LTIDate.getDate(1990, 12, 3), LTIDate.getDate(1994, 3, 4), LTIDate.getDate(1998, 4, 1), LTIDate.getDate(1999, 2, 3), LTIDate.getDate(1999, 12, 3), LTIDate.getDate(2000, 5, 4), LTIDate.getDate(2004, 2, 3), LTIDate.getDate(2008, 2, 4), LTIDate.getDate(2008, 2, 5) };

	public void testGetAdjPriceLong() {
		for (long i = 1; i < 10; i++) {
			try {
				// assertEquals(sm1.getAdjPrice(i), sm2.getAdjPrice(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void testGetAdjPriceLongDate() {
		for (long i = 1; i < 10; i++) {
			if (isDebug)
				System.out.println("Start Date:+" + df.format(sm1.getStartDate(i)));
			if (isDebug)
				System.out.println("End Date:" + df.format(sm1.getEndDate(i)));
			for (int j = 0; j < dates.length; j++) {
				double d1, d2;
				try {
					d1 = sm1.getAdjPrice(i, dates[j]);
					d2 = sm2.getAdjPrice(i, dates[j]);
				} catch (NoPriceException e) {
					// TODO Auto-generated catch block
					if (isDebug)
						System.out.println("skip[" + i + "][" + df.format(dates[j]) + "]");
					continue;
				}
				assertEquals(d1, d2);
			}
		}
	}

	public void testGetCloseList() {
		for (long i = 1; i < 10; i++) {
			for (int j = 0; j < dates.length; j++) {
				List<SecurityDailyData> sdds1 = sm1.getCloseList(i, dates[j]);
				List<SecurityDailyData> sdds2 = sm2.getCloseList(i, dates[j]);
				if (sdds1 == null && sdds2 == null)
					continue;
				if (sdds1 != null && sdds2 != null) {
					assertEquals(sdds1.size(), sdds2.size());
					if (sdds1.size() != 0) {
						assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
						assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
						assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
						assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

					}
				} else {
					assertFalse(true);
				}
			}
		}
	}

	public void testGetDailydata() {
		for (long i = 1; i < 10; i++) {
			for (int j = 0; j < dates.length; j++) {
				SecurityDailyData sdd1 = sm1.getDailydata(i, dates[j], true);
				SecurityDailyData sdd2 = sm2.getDailydata(i, dates[j], true);
				if (sdd1 == null && sdd2 == null)
					continue;
				if (sdd1 != null && sdd2 != null) {
					assertEquals(sdd1.getDate(), sdd2.getDate());
					assertEquals(sdd1.getClose(), sdd2.getClose());
				} else {
					assertFalse(true);
				}

				SecurityDailyData sdd3 = sm1.getDailydata(i, dates[j], false);
				SecurityDailyData sdd4 = sm2.getDailydata(i, dates[j], false);
				if (sdd3 == null && sdd4 == null)
					continue;
				if (sdd3 != null && sdd4 != null) {
					assertEquals(sdd3.getDate(), sdd4.getDate());
					assertEquals(sdd3.getClose(), sdd4.getClose());
				} else {
					assertFalse(true);
				}

			}
		}
	}

	public void testGetDailyData() {
		/*
		 * for (long i = 1; i < 10; i++) {
		 * 
		 * List<SecurityDailyData> sdds1 = sm1.getDailyData(i, dates); List<SecurityDailyData>
		 * sdds2 = sm2.getDailyData(i, dates); if (sdds1 == null && sdds2 ==
		 * null) continue; if (sdds1 != null && sdds2 != null) {
		 * assertEquals(sdds1.size(), sdds2.size()); if (sdds1.size() != 0) {
		 * assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
		 * assertEquals(sdds1.get(sdds1.size() - 1).getDate(),
		 * sdds2.get(sdds1.size() - 1).getDate());
		 * assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
		 * assertEquals(sdds1.get(sdds1.size() - 1).getClose(),
		 * sdds2.get(sdds1.size() - 1).getClose()); } } else {
		 * assertFalse(true); } }
		 */
		assertFalse(true);
	}

	public void testGetDailydatasLongBoolean() {
		for (long i = 1; i < 10; i++) {
			List<SecurityDailyData> sdds1 = sm1.getDailydatas(i, true);
			List<SecurityDailyData> sdds2 = sm2.getDailydatas(i, true);
			if (sdds1 == null && sdds2 == null)
				continue;
			if ((sdds1 != null ) && (sdds2 != null )){
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
		for (long i = 1; i < 10; i++) {
			List<SecurityDailyData> sdds1 = sm1.getDailydatas(i, false);
			List<SecurityDailyData> sdds2 = sm2.getDailydatas(i, false);
			if (sdds1 == null && sdds2 == null)
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetDailydatasLongInterval() {
		for (long i = 1; i < 10; i++) {
			List<SecurityDailyData> sdds1 = sm1.getDailyDatas(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
			List<SecurityDailyData> sdds2 = sm2.getDailyDatas(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
			if ((sdds1 == null || sdds1.size() == 0) && (sdds2 == null || sdds2.size() == 0))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetDailyDatas() {
		for (long i = 1; i < 10; i++) {
			List<SecurityDailyData> sdds1 = sm1.getDailyDatas(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
			List<SecurityDailyData> sdds2 = sm2.getDailyDatas(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
			if ((sdds1 == null || sdds1.size() == 0) && (sdds2 == null || sdds2.size() == 0))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetDailydataWithDividend() {
		for (long i = 1; i < 10; i++) {
			for (int j = 0; j < dates.length; j++) {
				SecurityDailyData sdd1 = sm1.getDailydataWithDividend(i, dates[j]);
				SecurityDailyData sdd2 = sm2.getDailydataWithDividend(i, dates[j]);
				if (sdd1 == null && sdd2 == null)
					continue;
				if (sdd1 != null && sdd2 != null) {
					assertEquals(sdd1.getDate(), sdd2.getDate());
					assertEquals(sdd1.getClose(), sdd2.getClose());
				} else {
					assertFalse(true);
				}
			}
		}
	}

	public void testGetDividend() {
		for (long i = 40; i < 50; i++) {
			for (int j = 0; j < dates.length; j++) {
				Double sdd1 = sm1.getDividend(i, dates[j]);
				Double sdd2 = sm2.getDividend(i, dates[j]);
				if (sdd1 == null && sdd2 == null)
					continue;
				if (sdd1 != null && sdd2 != null) {
					assertEquals(sdd1, sdd2);
				} else {
					assertFalse(true);
				}
			}
		}
	}

	public void testGetDividendList() {
		for (long i = 1; i < 10; i++) {
			List<SecurityDailyData> sdds1 = sm1.getDividendList(i);
			List<SecurityDailyData> sdds2 = sm2.getDividendList(i);
			if ((sdds1 == null || sdds1.size() == 0) && (sdds2 == null || sdds2.size() == 0))
				continue;
			if ((sdds1 != null ) && (sdds2 != null )) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} 
		}
	}

	public void testGetEndDateLong() {
		for (long i = 40; i < 50; i++) {
			Date sdd1 = sm1.getEndDate(i);
			Date sdd2 = sm2.getEndDate(i);
			if (sdd1 == null && sdd2 == null)
				continue;
			if (sdd1 != null && sdd2 != null) {
				assertEquals(sdd1, sdd2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetHighestAdjPriceLongDateDate() {
		for (long i = 1; i < 10; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getHighestAdjPrice(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
				sdds2 = sm2.getHighestAdjPrice(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
			} catch (SecurityException e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[(int) i % 3]) + "][" + df.format(dates[(int) i % 3 + 4]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetHighestNAVPrice() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getHighestNAVPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
				sdds2 = sm2.getHighestNAVPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
			} catch (SecurityException e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetHighestPriceLongDateDate() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getHighestPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
				sdds2 = sm2.getHighestPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
			} catch (SecurityException e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetLatestDailydataLong() {
		for (long i = 1; i < 10; i++) {
			SecurityDailyData sdd1 = sm1.getLatestDailydata(i);
			SecurityDailyData sdd2 = sm2.getLatestDailydata(i);
			if (sdd1 == null && sdd2 == null)
				continue;
			if (sdd1 != null && sdd2 != null) {
				assertEquals(sdd1.getDate(), sdd2.getDate());
				assertEquals(sdd1.getClose(), sdd2.getClose());
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetLatestDailydataLongDate() {
		for (long i = 1; i < 10; i++) {
			SecurityDailyData sdd1 = sm1.getLatestDailydata(i,LTIDate.getDate(2007, 1, 1));
			SecurityDailyData sdd2 = sm2.getLatestDailydata(i,LTIDate.getDate(2007, 1, 1));
			if (sdd1 == null && sdd2 == null)
				continue;
			if (sdd1 != null && sdd2 != null) {
				assertEquals(sdd1.getDate(), sdd2.getDate());
				assertEquals(sdd1.getClose(), sdd2.getClose());
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetLatestNAVDailydata() {
		for (long i = 190; i < 200; i++) {
			SecurityDailyData sdd1 = sm1.getLatestNAVDailydata(i,LTIDate.getDate(2007, 1, 1));
			SecurityDailyData sdd2 = sm2.getLatestNAVDailydata(i,LTIDate.getDate(2007, 1, 1));
			if (sdd1 == null && sdd2 == null)
				continue;
			if (sdd1 != null && sdd2 != null) {
				assertEquals(sdd1.getDate(), sdd2.getDate());
				assertEquals(sdd1.getClose(), sdd2.getClose());
			} else {
				assertFalse(true);
			}
		}
	}



	public void testGetLowestAdjPriceLongDateDate() {
		for (long i = 190; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getLowestAdjPrice(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
				sdds2 = sm2.getLowestAdjPrice(i, dates[(int) i % 3], dates[(int) i % 3 + 4]);
			} catch (SecurityException e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[(int) i % 3]) + "][" + df.format(dates[(int) i % 3 + 4]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetLowestNAVPrice() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getLowestNAVPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
				sdds2 = sm2.getLowestNAVPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
			} catch (SecurityException e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetLowestPriceLongDateDate() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getLowestPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
				sdds2 = sm2.getLowestPrice(i, dates[dates.length - 3], dates[dates.length - 2]);
			} catch (SecurityException e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetNAVListLong() {
		for (long i = 189; i < 200; i++) {
			List<SecurityDailyData> sdds1 = sm1.getNAVList(i);
			List<SecurityDailyData> sdds2 = sm2.getNAVList(i);
			if ((sdds1 == null || sdds1.size() == 0) && (sdds2 == null || sdds2.size() == 0))
				continue;
			if ((sdds1 != null ) && (sdds2 != null )) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetNAVListLongDate() {
		for (long i = 189; i < 200; i++) {
			List<SecurityDailyData> sdds1 = sm1.getNAVList(i,LTIDate.getDate(2008, 12, 4));
			List<SecurityDailyData> sdds2 = sm2.getNAVList(i,LTIDate.getDate(2008, 12, 4));
			if ((sdds1 == null || sdds1.size() == 0) && (sdds2 == null || sdds2.size() == 0))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetNAVListLongDateDate() {
		for (long i = 189; i < 200; i++) {
			List<SecurityDailyData> sdds1 = sm1.getNAVList(i,LTIDate.getDate(2008, 1, 4),LTIDate.getDate(2008, 12, 4));
			List<SecurityDailyData> sdds2 = sm2.getNAVList(i,LTIDate.getDate(2008, 1, 4),LTIDate.getDate(2008, 12, 4));
			if ((sdds1 == null || sdds1.size() == 0) && (sdds2 == null || sdds2.size() == 0))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1.size(), sdds2.size());
				if (sdds1.size() != 0) {
					assertEquals(sdds1.get(0).getDate(), sdds2.get(0).getDate());
					assertEquals(sdds1.get(sdds1.size() - 1).getDate(), sdds2.get(sdds1.size() - 1).getDate());
					assertEquals(sdds1.get(0).getClose(), sdds2.get(0).getClose());
					assertEquals(sdds1.get(sdds1.size() - 1).getClose(), sdds2.get(sdds1.size() - 1).getClose());

				}
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetNAVPrice() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getNAVPrice(i, LTIDate.getDate(2008, 12, 4));
				sdds2 = sm2.getNAVPrice(i, LTIDate.getDate(2008, 12, 4));
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetPriceLong() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds2 = sm2.getPrice(i);
				sdds1 = sm1.getPrice(i);
				
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetPriceLongDate() {
		for (long i = 189; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds1 = sm1.getPrice(i, LTIDate.getDate(2008, 12, 4));
				sdds2 = sm2.getPrice(i, LTIDate.getDate(2008, 12, 4));
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}


	public void testGetSplit() {
		for (long i = 193; i < 200; i++) {
			Double sdds1 = null;
			Double sdds2 = null;
			try {
				sdds2 = sm2.getSplit(i, LTIDate.getDate(2000, 10, 20));
				sdds1 = sm1.getSplit(i, LTIDate.getDate(2000, 10, 20));
				
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("skip.[" + i + "][" + df.format(dates[dates.length - 3]) + "][" + df.format(dates[dates.length - 2]) + "]");
			}
			if ((sdds1 == null) && (sdds2 == null))
				continue;
			if (sdds1 != null && sdds2 != null) {
				assertEquals(sdds1, sdds2);
			} else {
				assertFalse(true);
			}
		}
	}

	public void testGetStartDateLong() {
		for (long i = 40; i < 50; i++) {
			Date sdd1 = sm1.getStartDate(i);
			Date sdd2 = sm2.getStartDate(i);
			if (sdd1 == null && sdd2 == null)
				continue;
			if (sdd1 != null && sdd2 != null) {
				assertEquals(sdd1, sdd2);
			} else {
				assertFalse(true);
			}
		}
	}


	public void testGetBySymbol() {
		Security s=sm1.getBySymbol("SPY");
		assertTrue(s.getSymbol().equals("SPY"));
	}

}
