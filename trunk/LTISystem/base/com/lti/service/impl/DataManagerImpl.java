package com.lti.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.service.AssetClassManager;
import com.lti.service.IndicatorManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.IndicatorDailyData;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Security;
import com.lti.service.bo.ShillerSP500;
import com.lti.util.*;
import com.lti.service.DataManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.SourceType;
import com.lti.type.TimeUnit;

public class DataManagerImpl implements DataManager, Serializable
{

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(DataManager.class);

	private SecurityManager securityManager;

	private AssetClassManager assetClassManager;

	private IndicatorManager indicatorManager;

	private PortfolioManager portfolioManager;

	private int bFlagUpdateAddDaily;

	private List<DailyUpdateListener> dailyUpdateListenerList = new ArrayList<DailyUpdateListener>();

	public void setSecurityManager(SecurityManager securityManager)
	{
		this.securityManager = securityManager;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager)
	{
		this.assetClassManager = assetClassManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager)
	{
		this.portfolioManager = portfolioManager;
	}

	public void setIndicatorManager(IndicatorManager indicatorManager)
	{
		this.indicatorManager = indicatorManager;
	}

	public void UpdateData(String file) throws Exception
	{

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		//
		int bFlagUpdateAdd = 0;
		//
		int bFlagUpdateAddDialy = 0;

		long id = 0;
		int Count = 0;
		String line = "";

		//
		line = br.readLine();
		while (line != null)
		{
			Count++;
			line = line.trim();
			String[] s = line.split(",");
			bFlagUpdateAdd = 0;

			if (Count == 2)
			{
				try
				{
					Security security;
					security = securityManager.get(s[DataHeader.NAME]);
					if (security == null)
					{
						security = new Security();
						security.setName(s[DataHeader.NAME]);
						bFlagUpdateAdd = 1;
					} else
						bFlagUpdateAdd = 0;

					security.setSymbol(s[DataHeader.SYMBOL]);
					security.setSecurityType(Configuration.getSecurityType(s[DataHeader.SECURITY_TYPE]));
					AssetClass ac = assetClassManager.get(s[DataHeader.ASSET_CLASS]);

					long classid;
					if (ac == null)
					{
						classid = 0;
					} else
						classid = ac.getID();

					ac = assetClassManager.getChildClass(s[DataHeader.ASSET_TYPE], classid);
					if (ac == null)
					{
						classid = 0;
					} else
						classid = ac.getID();

					ac = assetClassManager.getChildClass(s[DataHeader.SUBCLASS], classid);
					if (ac == null)
					{
						classid = 0;
					} else
						classid = ac.getID();

					security.setClassID(classid);
					if (s[DataHeader.DIVERSIFIED].equalsIgnoreCase("0"))
						security.setDiversified(false);
					else
						security.setDiversified(true);

					if (bFlagUpdateAdd == 1)
						securityManager.add(security);
					else
						securityManager.update(security);

					id = security.getID();
				} catch (Exception e)
				{
					// e.printStackTrace();
					System.out.println(StringUtil.getStackTraceString(e));
					throw new Exception("ERROR DATA FORMAT");
				}

			}

			if (Count > 4)
			{
				bFlagUpdateAddDialy = 0;

				// Date,Open,High,Low,Close,Volume,Adj Close
				// 2007-9-4,99.98,101,99.09,99.87,565500,99.87
				try
				{
					com.lti.service.bo.SecurityDailyData securityDailyData;
					String str = s[DataBody.DATE];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					// Date dt = sf.parse(str);
					Date dt = (Date) sf.parseObject(str);
					// judge the same dailydata
					securityDailyData = securityManager.getDailydataWithDividend(id, dt);
					if (securityDailyData == null)
					{// ||!securityDailyData.getDate().equals(dt)){
						securityDailyData = new SecurityDailyData();
						bFlagUpdateAddDialy = 1;
					} else
						bFlagUpdateAddDialy = 0;

					securityDailyData.setDate(dt);
					securityDailyData.setOpen(Double.parseDouble(s[DataBody.OPEN]));
					securityDailyData.setHigh(Double.parseDouble(s[DataBody.HIGH]));
					securityDailyData.setLow(Double.parseDouble(s[DataBody.LOW]));
					securityDailyData.setClose(Double.parseDouble(s[DataBody.CLOSE]));
					securityDailyData.setVolume(Long.parseLong(s[DataBody.VOLUNE]));
					securityDailyData.setAdjClose(Double.parseDouble(s[DataBody.ADJCLOSE]));
					securityDailyData.setDividend(Double.parseDouble(s[DataBody.DIVIDEND]));
					securityDailyData.setSplit(Double.parseDouble(s[DataBody.SPLIT]));
					securityDailyData.setSecurityID(id);

					/*
					 * if(bFlagUpdateAddDialy==1) ((SecurityManager)
					 * securityManager).saveDailyData(securityDailyData); else
					 * securityManager.updateDailyData(securityDailyData);
					 */

					sdds.add(securityDailyData);

				} catch (Exception e)
				{
					e.printStackTrace();
					log.warn("Skip Line:" + line);
					line = br.readLine();
					continue;
				}
			}
			line = br.readLine();
		}// end while
		securityManager.saveOrUpdateAll(sdds);
	}// end process file

	/***************************************************************************/
	private List<List<Date>> dividendDateList;
	private HashMap<String, Integer> securityMap;
	private int totalSecurity;
	private boolean firstOne = true;

	public void setAllForFlush()
	{
		dividendDateList = new ArrayList<List<Date>>();
		securityMap = new HashMap<String, Integer>();
		totalSecurity = 0;
	}

	/**
	 * check if the securitydailydata on date exists, if so, return it,
	 * otherwise, return null
	 * 
	 * @author CCD
	 * @param dataList
	 * @param date
	 * @return
	 */
	private SecurityDailyData binarySearch(List<SecurityDailyData> dataList, Date date)
	{
		// dataList is asc by date;
		if (dataList != null)
		{
			int start = 0;
			int last = dataList.size() - 1;
			while (start <= last)
			{
				int mid = (start + last) / 2;
				SecurityDailyData sdd = dataList.get(mid);
				if (LTIDate.equals(sdd.getDate(), date))
					return sdd;
				else if (LTIDate.before(sdd.getDate(), date))
					start = mid + 1;
				else
					last = mid - 1;
			}
		}
		return null;
	}

	/**
	 * @author CCD
	 * @param symbol
	 * @param filename
	 * @param flash
	 * @param flush
	 *            modified by CCD on 2010-03-02
	 */
	public void UpdateDailyData(String symbol, String filename, boolean flush, Date logDate) throws Exception
	{
		Security se = securityManager.getBySymbol(symbol);
		if (se == null)
			return;
		long ID = se.getID();
		// List<SecurityDailyData> existsDailyDataList =
		// securityManager.getDailydatas(ID);
		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		int linenum = 0;
		Date firstDay = null;
		// Date priceLastDate = null;
		line = br.readLine();
		Date endDate = se.getEndDate();
		while (line != null)
		{
			if (line.length() == 0)
				continue;
			line = line.trim();
			String[] s = line.split(",");
			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;
				DataBody.DATE = -1;
				DataBody.ADJCLOSE = -1;
				DataBody.CLOSE = -1;
				DataBody.DIVIDEND = -1;
				DataBody.HIGH = -1;
				DataBody.LOW = -1;
				DataBody.OPEN = -1;
				DataBody.SPLIT = -1;
				DataBody.VOLUNE = -1;
				DataBody.NAV = -1;
				DataBody.ADJNAV = -1;
				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("Date"))
						DataBody.DATE = i;
					else if (s[i].equalsIgnoreCase("Open"))
						DataBody.OPEN = i;
					else if (s[i].equalsIgnoreCase("High"))
						DataBody.HIGH = i;
					else if (s[i].equalsIgnoreCase("Low"))
						DataBody.LOW = i;
					else if (s[i].equalsIgnoreCase("Close"))
						DataBody.CLOSE = i;
					else if (s[i].equalsIgnoreCase("Volume"))
						DataBody.VOLUNE = i;
					else if (s[i].equalsIgnoreCase("Adj Close") || s[i].equalsIgnoreCase("AdjClose"))
						DataBody.ADJCLOSE = i;
					else if (s[i].equalsIgnoreCase("Dividends") || s[i].equalsIgnoreCase("Dividend"))
						DataBody.DIVIDEND = i;
					else if (s[i].equalsIgnoreCase("Split"))
						DataBody.SPLIT = i;
					else if (s[i].equalsIgnoreCase("NAV"))
						DataBody.NAV = i;
					else if (s[i].equalsIgnoreCase("Adj NAV") || s[i].equalsIgnoreCase("AdjNAV"))
						DataBody.ADJNAV = i;
				}
			} else
			{
				try
				{
					SecurityDailyData securityDailyData = null;
					String str = s[DataBody.DATE];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date dt = (Date) sf.parseObject(str);
					// if(priceLastDate == null)
					// {
					// priceLastDate = dt;
					// se.setPriceLastDate(priceLastDate);
					// se.setEndDate(priceLastDate);
					// securityManager.update(se);
					// }
					// if(flush){
					// Integer index= this.securityMap.get(symbol);
					// if(index!=null && index >= 0){
					// if(dividendDateList.get(index).indexOf(dt)>=0)
					// securityDailyData =
					// this.binarySearch(existsDailyDataList, dt);
					// }
					// else
					// securityDailyData = null;
					// }else
					// securityDailyData =
					// this.binarySearch(existsDailyDataList, dt);
					if (!flush)
						securityDailyData = securityManager.getDailydata(ID, dt, false);
					else
					{
						Integer index = this.securityMap.get(symbol);
						if (index != null && index >= 0)
							if (dividendDateList.get(index).indexOf(dt) >= 0)
								securityDailyData = securityManager.getDailydata(ID, dt, false);
					}

					if (securityDailyData == null)
						securityDailyData = new SecurityDailyData();

					securityDailyData.setDate(dt);
					if (endDate == null || LTIDate.after(dt, endDate))
						endDate = dt;
					if (firstDay == null)
						firstDay = dt;

					if (DataBody.OPEN != -1)
						securityDailyData.setOpen(Double.parseDouble(s[DataBody.OPEN]));
					else
						securityDailyData.setOpen(0.0);

					if (DataBody.HIGH != -1)
						securityDailyData.setHigh(Double.parseDouble(s[DataBody.HIGH]));
					else
						securityDailyData.setHigh(0.0);

					if (DataBody.LOW != -1)
						securityDailyData.setLow(Double.parseDouble(s[DataBody.LOW]));
					else
						securityDailyData.setLow(0.0);

					if (DataBody.CLOSE != -1)
						securityDailyData.setClose(Double.parseDouble(s[DataBody.CLOSE]));
					else
						securityDailyData.setClose(0.0);

					if (DataBody.VOLUNE != -1)
						securityDailyData.setVolume(Long.parseLong(s[DataBody.VOLUNE]));
					else
						securityDailyData.setVolume(0l);

					if (DataBody.ADJCLOSE != -1)
						securityDailyData.setAdjClose(Double.parseDouble(s[DataBody.ADJCLOSE]));

					if (DataBody.DIVIDEND != -1)
						securityDailyData.setDividend(Double.parseDouble(s[DataBody.DIVIDEND]));

					if (DataBody.SPLIT != -1)
						securityDailyData.setSplit(Double.parseDouble(s[DataBody.SPLIT]));

					if (DataBody.NAV != -1 && s[DataBody.NAV] != null && s[DataBody.NAV].length() != 0)
						securityDailyData.setNAV(Double.parseDouble(s[DataBody.NAV]));

					if (DataBody.ADJNAV != -1 && s[DataBody.ADJNAV] != null && s[DataBody.ADJNAV].length() != 0)
						securityDailyData.setAdjNAV(Double.parseDouble(s[DataBody.ADJNAV]));

					if (securityDailyData.getDividend() == null)
						securityDailyData.setDividend(0.0);

					securityDailyData.setSecurityID(ID);
					sdds.add(securityDailyData);
					linenum++;

				} catch (Exception e)
				{
					e.printStackTrace();
					log.warn("Skip Line:" + line);
					throw e;
				}
			}
			line = br.readLine();
		}// end while
		Date date = new Date();
		if (linenum == 0)
			Configuration.writeLog(symbol, logDate, date, symbol + " has no update data at all");
		else
			Configuration.writeLog(symbol, logDate, date, symbol + "'s last date from download file is " + firstDay);
		securityManager.saveOrUpdateAll(sdds);
		if (se.getEndDate() == null || LTIDate.after(endDate, se.getEndDate()))
		{
			se.setPriceLastDate(endDate);
			securityManager.updateEndDateAndPriceLastDate(se);
		}
	}

	public void UpdateDailyData(long id, String filename, boolean flush, Date logDate) throws Exception
	{
		Security security = securityManager.get(id);
		if (security == null)
			return;
		this.UpdateDailyData(security.getName(), filename, flush, logDate);
	}

	/**
	 * modified by ccd at 2009-08-29 update the security daily data with
	 * seucrity's name is name and update the pricelastdate of it
	 **/
	public void UpdateDailyDataAndAdjust(String symbol, String filename, boolean flush, Date lastDate, boolean fromStart, Date logDate) throws Exception
	{

		Security se = securityManager.getBySymbol(symbol);
		if (se == null)
			return;
		long ID = se.getID();
		List<SecurityDailyData> existsDailyDataList = null;
		if (flush)
		{
			existsDailyDataList = securityManager.getDailydatas(ID);
		}
		// List<SecurityDailyData> existsDailyDataList =
		// securityManager.getDailydatas(ID);
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);

		Date endDate = se.getEndDate();

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		/****** For Adjust ************************************************************/
		Date date2 = LTIDate.getDate(1900, 0, 0);
		Date lastDailyDataDate = lastDate;// securityManager.getDividendLastDate(id);//.getDailyDataLastDate(id);
		/*** don't go through the security daily data table */
		if (lastDailyDataDate == null)
			lastDailyDataDate = date2;
		lastDailyDataDate = LTIDate.getNewTradingDate(lastDailyDataDate, TimeUnit.DAILY, -1);
		SecurityDailyData staticDailyData = null;
		if (flush)
			staticDailyData = existsDailyDataList.get(existsDailyDataList.size() - 1);
		else
			staticDailyData = securityManager.getLatestDailydata(ID, lastDailyDataDate); // 小于最新收盘价日期且收盘价不为空的DailyData
		SecurityDailyData staticDailyData2 = new SecurityDailyData();
		CopyUtil.Translate(staticDailyData, staticDailyData2);// ???
		/****************************************************************************/

		String line = "";

		int linenum = 0;
		Date firstDay = null;

		line = br.readLine();

		// Date priceLastDate = null;

		while (line != null)
		{

			if (line.length() == 0)
				continue;
			line = line.trim();
			String[] s = line.split(",");

			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;
				DataBody.DATE = -1;
				DataBody.ADJCLOSE = -1;
				DataBody.CLOSE = -1;
				// DataBody.DIVIDEND = -1;
				DataBody.HIGH = -1;
				DataBody.LOW = -1;
				DataBody.OPEN = -1;
				DataBody.SPLIT = -1;
				DataBody.VOLUNE = -1;
				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("Date"))
						DataBody.DATE = i;
					else if (s[i].equalsIgnoreCase("Open"))
						DataBody.OPEN = i;
					else if (s[i].equalsIgnoreCase("High"))
						DataBody.HIGH = i;
					else if (s[i].equalsIgnoreCase("Low"))
						DataBody.LOW = i;
					else if (s[i].equalsIgnoreCase("Close"))
						DataBody.CLOSE = i;
					else if (s[i].equalsIgnoreCase("Volume"))
						DataBody.VOLUNE = i;
					else if (s[i].equalsIgnoreCase("Adj Close"))
						DataBody.ADJCLOSE = i;
					else if (s[i].equalsIgnoreCase("Split"))
						DataBody.SPLIT = i;
				}
			} else
			{
				try
				{

					SecurityDailyData securityDailyData = null;
					String str = s[DataBody.DATE];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date dt = (Date) sf.parseObject(str);

					// if(priceLastDate == null)
					// {
					// priceLastDate = dt;
					// se.setPriceLastDate(priceLastDate);
					// se.setEndDate(priceLastDate);
					// securityManager.update(se);
					// }

					// if(!flush)
					// securityDailyData =
					// this.binarySearch(existsDailyDataList, dt);
					// else
					// {
					// int index= this.securityMap.get(symbol);
					// if(dividendDateList.get(index).indexOf(dt)>=0)
					// securityDailyData =
					// this.binarySearch(existsDailyDataList, dt);
					// }

					if (!flush)
						securityDailyData = securityManager.getDailydata(se.getID(), dt, false);
					else
					{
						int index = this.securityMap.get(symbol);
						if (dividendDateList.get(index).indexOf(dt) >= 0)
							securityDailyData = this.binarySearch(existsDailyDataList, dt);
						// int index= this.securityMap.get(symbol);
						// if(dividendDateList.get(index).indexOf(dt)>=0)
						// securityDailyData =
						// securityManager.getDailydataWithDividend(se.getID(),
						// dt);//.getDailydata(se.getID(), dt,false);
					}
					if (securityDailyData == null)
						securityDailyData = new SecurityDailyData();

					securityDailyData.setDate(dt);
					securityDailyData.setSecurityID(ID);

					if (endDate == null || LTIDate.after(dt, endDate))
						endDate = dt;

					if (firstDay == null)
						firstDay = dt;

					if (DataBody.OPEN != -1)
						securityDailyData.setOpen(Double.parseDouble(s[DataBody.OPEN]));
					else
						securityDailyData.setOpen(0.0);

					if (DataBody.HIGH != -1)
						securityDailyData.setHigh(Double.parseDouble(s[DataBody.HIGH]));
					else
						securityDailyData.setHigh(0.0);

					if (DataBody.LOW != -1)
						securityDailyData.setLow(Double.parseDouble(s[DataBody.LOW]));
					else
						securityDailyData.setLow(0.0);

					if (DataBody.CLOSE != -1)
						securityDailyData.setClose(Double.parseDouble(s[DataBody.CLOSE]));
					else
						securityDailyData.setClose(0.0);

					if (DataBody.VOLUNE != -1)
						securityDailyData.setVolume(Long.parseLong(s[DataBody.VOLUNE]));
					else
						securityDailyData.setVolume(0l);

					if (DataBody.ADJCLOSE != -1)
						securityDailyData.setAdjClose(Double.parseDouble(s[DataBody.ADJCLOSE]));
					if (securityDailyData.getDividend() == null)
						securityDailyData.setDividend(0.0);

					if (securityDailyData.getAdjNAV() == null)
						securityDailyData.setAdjNAV(null);

					securityDailyData.setSplit(1.0); // 拆股
					sdds.add(securityDailyData);
					linenum++;
					if (linenum % 1000 == 0)
						System.out.println(linenum);

				} catch (Exception e)
				{
					e.printStackTrace();
					log.warn("Skip Line:" + line);
					line = br.readLine();
					continue;
				}
			}
			line = br.readLine();

		}// end while

		Date date = new Date();

		/********************************************************/
		List<SecurityDailyData> newsdds = this.UpdateAdjClose(sdds, staticDailyData2, fromStart);
		if (linenum == 0)
			Configuration.writeLog(symbol, logDate, date, symbol + " has no update data at all");
		else
			Configuration.writeLog(symbol, logDate, date, symbol + "'s last date from download file is " + firstDay);
		securityManager.saveOrUpdateAll(newsdds);

		if (se.getEndDate() == null || LTIDate.after(endDate, se.getEndDate()))
		{
			se.setEndDate(endDate);
			securityManager.updateEndDateAndPriceLastDate(se);
		}
	}

	public Date UpdateDividends(String filename, String symbol, boolean flush) throws Exception
	{
		Security se = securityManager.getBySymbol(symbol);
		if (se == null)
			return null;

		List<Date> listDividentDate = new ArrayList<Date>();
		long ID = se.getID();
		// List<SecurityDailyData> existsDailyDataList =
		// securityManager.getDailydatas(ID);
		if (flush && this.firstOne)
		{
			this.setAllForFlush();
			this.firstOne = false;
		}
		Date lastDate = null;

		List<Date> newdateList = new ArrayList<Date>();

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		FileReader fr = new FileReader(filename);

		BufferedReader br = new BufferedReader(fr);

		String line = "";

		Date oldDividendLastDate = se.getDividendLastDate();
		Date dividendLastDate = null;
		Date dividendFirstDate = null;

		line = br.readLine();

		while (line != null)
		{

			if (line.length() == 0)
				continue;

			line = line.trim();
			String[] s = line.split(",");

			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;
				DataBody.DATE = -1;
				DataBody.DIVIDEND = -1;
				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("Date"))
						DataBody.DATE = i;
					else if (s[i].equalsIgnoreCase("Dividends"))
						DataBody.DIVIDEND = i;
				}
			}

			else
			{
				try
				{
					com.lti.service.bo.SecurityDailyData securityDailyData;
					String str = s[DataBody.DATE];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date dt = (Date) sf.parseObject(str);
					listDividentDate.add(dt);
					lastDate = dt;

					if (dividendLastDate == null)
					{
						dividendLastDate = dt;
						if (se.getDividendLastDate() == null || LTIDate.before(se.getDividendLastDate(), dt))
						{
							se.setDividendLastDate(dividendLastDate);
							securityManager.updateDividendLastDate(se);
							portfolioManager.setHoldingRecord(ID, dividendLastDate);
						}
					}
					// if(!flush){
					// securityDailyData =
					// this.binarySearch(existsDailyDataList, dt);
					// if(securityDailyData==null)
					// securityDailyData=new SecurityDailyData();
					// }
					// else{
					// //set a new one
					// securityDailyData=new SecurityDailyData();
					// newdateList.add(dt);
					// }
					if (!flush)
					{
						securityDailyData = securityManager.getDailydata(ID, dt, false);
						if (securityDailyData == null)
							securityDailyData = new SecurityDailyData();
					} else
					{
						securityDailyData = new SecurityDailyData();
						newdateList.add(dt);
					}

					if (dividendFirstDate == null || LTIDate.before(dt, dividendFirstDate))
						dividendFirstDate = dt;

					securityDailyData.setDate(dt);
					securityDailyData.setSecurityID(ID);

					if (DataBody.DIVIDEND != -1)
						securityDailyData.setDividend(Double.parseDouble(s[DataBody.DIVIDEND]));
					else
						securityDailyData.setDividend(0.0);

					sdds.add(securityDailyData);

					// System.out.println("success:"+line);

				} catch (Exception e)
				{
					e.printStackTrace();
					line = br.readLine();
					continue;
				}
			}
			line = br.readLine();

		}// end while

		if (flush)
		{
			securityMap.put(symbol, this.totalSecurity);
			this.totalSecurity++;
			this.dividendDateList.add(newdateList);
		}

		securityManager.saveOrUpdateAll(sdds);
		if ((se.getNewDividendDate() == null && oldDividendLastDate != null && LTIDate.after(dividendFirstDate, oldDividendLastDate)) || oldDividendLastDate == null)
		{
			se.setNewDividendDate(dividendFirstDate);
			securityManager.updateNewDividendDate(se);
		}
		if (listDividentDate.size() > 1)
		{
			for (int i = 0; i < listDividentDate.size(); i++)
			{
				for (int j = 0; j < listDividentDate.size() - 1; j++)
				{
					// if(LTIDate.equals(listDividentDate.get(j),
					// (Date)lastDivident)){
					// listDividentDate.remove(j);
					// continue;
					// }
					/*
					 * 对日期处理，从小到大排序
					 */
					if (LTIDate.after(listDividentDate.get(j), listDividentDate.get(j + 1)))
					{
						Date compareDate = listDividentDate.get(j);
						listDividentDate.set(j, listDividentDate.get(j + 1));
						listDividentDate.set(j + 1, compareDate);
					}
				}
			}
			lastDate = listDividentDate.get(1);// 获取所下载的dividend中的最小日期
		}
		return lastDate;
	}

	public void UpdateNAV2(String filename, String symbol) throws Exception
	{
		Security se = securityManager.getBySymbol(symbol);
		if (se == null)
			return;
		long ID = se.getID();
		// List<SecurityDailyData> existsDailyDataList =
		// securityManager.getDailydatas(ID);
		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);

		String line = "";

		int linenum = 0;

		line = br.readLine();

		Date navLastDate = null;

		while (line != null)
		{
			if (line.length() == 0)
				continue;
			line = line.trim();
			String[] s = line.split(",");

			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;
				DataBody.DATE = -1;
				DataBody.CLOSE = -1;
				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("Date"))
						DataBody.DATE = i;
					else if (s[i].equalsIgnoreCase("Close"))
						DataBody.CLOSE = i;
				}
			} else
			{
				try
				{
					SecurityDailyData securityDailyData;
					String str = s[DataBody.DATE];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date dt = (Date) sf.parseObject(str);

					if (navLastDate == null)
					{
						navLastDate = dt;
						se.setNavLastDate(navLastDate);
						securityManager.updateNAVLastDate(se);
					}

					securityDailyData = securityManager.getDailydataWithDividend(ID, dt);
					if (securityDailyData == null)// ||!securityDailyData.getDate().equals(dt)){
						securityDailyData = new SecurityDailyData();

					// securityDailyData =
					// this.binarySearch(existsDailyDataList, dt);
					// if(securityDailyData==null)
					// securityDailyData=new SecurityDailyData();

					securityDailyData.setDate(dt);
					securityDailyData.setSecurityID(ID);

					if (DataBody.CLOSE != -1)
						securityDailyData.setNAV(Double.parseDouble(s[DataBody.CLOSE]));
					else
						securityDailyData.setNAV(0.0);

					if (securityDailyData.getDividend() == null)
						securityDailyData.setDividend(0.0);

					if (securityDailyData.getAdjNAV() == null)
						securityDailyData.setAdjNAV(null);

					sdds.add(securityDailyData);

					linenum++;
					if (linenum % 1000 == 0)
						System.out.println(linenum);

				} catch (Exception e)
				{
					e.printStackTrace();
					log.warn("Skip Line:" + line);
					line = br.readLine();
					continue;
				}
			}

			line = br.readLine();

		}// end while

		securityManager.saveOrUpdateAll(sdds);

	}

	public void UpdateNAV(String filename, String name) throws Exception
	{

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		FileReader fr = new FileReader(filename);

		BufferedReader br = new BufferedReader(fr);

		Security se = securityManager.getBySymbol(name);

		long id = se.getID();

		String line = "";

		//
		int bFlagUpdateAddDialy = 0;

		Date navLastDate = null;

		line = br.readLine();
		while (line != null)
		{

			int DataBody_DATE = 0;
			int DataBody_NAV = 1;

			if (line.length() == 0)
				continue;

			line = line.trim();
			String[] s = line.split(",");

			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;

				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("Date"))
					{
						DataBody_DATE = i;
					} else if (s[i].equalsIgnoreCase("NAV"))
					{
						DataBody_NAV = i;
					}
				}
			}

			else
			{
				try
				{
					com.lti.service.bo.SecurityDailyData securityDailyData;
					String str = s[DataBody_DATE];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					// Date dt = sf.parse(str);
					Date dt = (Date) sf.parseObject(str);

					if (navLastDate == null)
					{
						navLastDate = dt;
						se.setNavLastDate(navLastDate);
						securityManager.updateNAVLastDate(se);
					}

					securityDailyData = securityManager.getLatestDailydata(id, dt);
					if (securityDailyData == null)
					{// ||!securityDailyData.getDate().equals(dt)){
						securityDailyData = new SecurityDailyData();
						bFlagUpdateAddDialy = 1;
					}

					else
						bFlagUpdateAddDialy = 0;

					securityDailyData.setDate(dt);
					if (DataBody.NAV != -1)
					{
						securityDailyData.setNAV(Double.parseDouble(s[DataBody_NAV]));
					} else
					{
						securityDailyData.setNAV(0.0);
					}
					securityDailyData.setSecurityID(id);

					sdds.add(securityDailyData);

					// System.out.println("success:"+line);

				} catch (Exception e)
				{
					e.printStackTrace();
					line = br.readLine();
					continue;
				}
			}
			line = br.readLine();

		}// end while
		securityManager.saveOrUpdateAll(sdds);
	}

	public void updateEcom(String filename, String name) throws Exception
	{

		List<IndicatorDailyData> sdds = new ArrayList<IndicatorDailyData>();

		FileReader fr = new FileReader(filename);

		BufferedReader br = new BufferedReader(fr);

		Indicator indicator = indicatorManager.get(name);

		long id = indicator.getID();

		// int Count = 0;
		String line = "";

		// int bFlagUpdateAdd=0;

		int bFlagUpdateAddDialy = 0;

		int DataBodyDate = 0, DataBodyValue = 1;

		line = br.readLine();
		while (line != null)
		{

			if (line.length() == 0)
				continue;

			line = line.trim();
			String[] s = line.split(",");

			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;
				DataBodyDate = -1;
				DataBodyValue = -1;
				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("DATE"))
					{
						DataBodyDate = i;
					} else if (s[i].equalsIgnoreCase("VALUE"))
					{
						DataBodyValue = i;
					}
				}
			}

			else
			{
				try
				{
					com.lti.service.bo.IndicatorDailyData indicatorDailyData;
					String str = s[DataBodyDate];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date dt = (Date) sf.parseObject(str);
					indicatorDailyData = indicatorManager.getDailydata(id, dt);
					if (indicatorDailyData == null || !indicatorDailyData.getDate().equals(dt))
					{
						indicatorDailyData = new IndicatorDailyData();
						bFlagUpdateAddDialy = 1;
					}

					else
						bFlagUpdateAddDialy = 0;

					if (DataBodyValue != -1)
					{
						indicatorDailyData.setValue(Double.parseDouble(s[DataBodyValue]));
					} else
					{
						indicatorDailyData.setValue(0.0);
					}
					indicatorDailyData.setIndicatorID(id);
					indicatorDailyData.setDate(dt);

					sdds.add(indicatorDailyData);

					// log.info("success:"+line);

				} catch (Exception e)
				{
					e.printStackTrace();
					log.warn("skip line:" + line);
					line = br.readLine();
					continue;
				}
			}
			line = br.readLine();

		}
		indicatorManager.saveOrUpdateAll(sdds);
	}

	public void updateEcom2(String filename, String name) throws Exception
	{
		List<IndicatorDailyData> inList = new ArrayList<IndicatorDailyData>();

		FileReader fr = new FileReader(filename);

		BufferedReader br = new BufferedReader(fr);

		Indicator indicator = indicatorManager.get(name);

		if (indicator == null)
			return;

		long id = indicator.getID();

		IndicatorDailyData lastData = indicatorManager.getLastDailyData(id);
		Date lastDate = null;
		if (lastData != null)
			lastDate = lastData.getDate();

		String line = "";

		int DataBodyDate = 0, DataBodyValue = 1;

		line = br.readLine();
		while (line != null)
		{

			if (line.length() == 0)
				continue;

			System.out.println(line);

			line = line.trim();
			String[] s = line.split(",");

			if (line.charAt(0) < '0' || line.charAt(0) > '9')
			{
				int i = -1;
				DataBodyDate = -1;
				DataBodyValue = -1;
				for (i = 0; i < s.length; i++)
				{
					if (s[i].equalsIgnoreCase("DATE"))
					{
						DataBodyDate = i;
					} else if (s[i].equalsIgnoreCase("VALUE"))
					{
						DataBodyValue = i;
					}
				}
			}

			else
			{
				try
				{
					com.lti.service.bo.IndicatorDailyData indicatorDailyData = null;
					String str = s[DataBodyDate];
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date dt = (Date) sf.parseObject(str);

					if (lastDate != null && !dt.after(lastDate))
					{
						line = br.readLine();
						continue;
					}

					// indicatorDailyData =
					// indicatorManager.getLatestDailydata(id, dt);
					if (indicatorDailyData == null || !indicatorDailyData.getDate().equals(dt))
					{
						indicatorDailyData = new IndicatorDailyData();
						if (DataBodyValue != -1)
						{
							indicatorDailyData.setValue(Double.parseDouble(s[DataBodyValue]));
						} else
						{
							indicatorDailyData.setValue(0.0);
						}
						indicatorDailyData.setIndicatorID(id);
						indicatorDailyData.setDate(dt);
						// indicatorManager.addDailyData(indicatorDailyData);
						inList.add(indicatorDailyData);
					}

					else
					{
						line = br.readLine();
						continue;
					}

				} catch (Exception e)
				{
					e.printStackTrace();
					log.warn("skip line:" + line);
					line = br.readLine();
					continue;
				}
			}
			line = br.readLine();

		}

		indicatorManager.saveOrUpdateAll(inList);

	}

	/*
	 * just for template usage author : chaos need to be modify
	 */
	public void UpdateAdjNAV(String securityName)
	{

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		Security se = securityManager.getBySymbol(securityName);

		List<SecurityDailyData> dividendList = new ArrayList<SecurityDailyData>();
		List<SecurityDailyData> NAVList = new ArrayList<SecurityDailyData>();
		List<Date> dateList = new ArrayList<Date>();
		HashMap<Date, Double> navMap = new HashMap<Date, Double>();
		HashMap<Date, Double> dividendMap = new HashMap<Date, Double>();
		HashMap<Date, Double> rateMap = new HashMap<Date, Double>();

		double rate = 1;
		dividendList = securityManager.getDividendList(se.getID());
		NAVList = securityManager.getNAVList(se.getID());

		if (dividendList.size() == 0 || NAVList.size() == 0)
		{
			System.out.println("?????????WRONG " + securityName);
			log.warn("WRONG; " + securityName);
			return;
		}

		for (int i = 0; i < dividendList.size(); i++)
		{
			SecurityDailyData tmp = dividendList.get(i);
			double dividend = tmp.getDividend();
			Date date = tmp.getDate();

			double lastNAV = this.getYesterDayNAV(date, NAVList);
			if (lastNAV != 0.0)
			{
				// rate = rate * (1.0 - dividend/lastNAV);
				dateList.add(date);
				navMap.put(date, lastNAV);
				dividendMap.put(date, dividend);
			}
		}

		if (dateList.size() == 0)
		{
			System.out.println("!!!!!!!!!!!!!WRONG " + securityName);
			log.warn("WRONG; " + securityName);
			return;
		}

		for (int i = dateList.size() - 1; i >= 0; i--)
		{
			Date tmpDate = dateList.get(i);
			double nav = (Double) navMap.get(tmpDate);
			double dividend = (Double) dividendMap.get(tmpDate);
			rate = rate * (1.0 - dividend / nav);
			rateMap.put(tmpDate, rate);
		}

		Date minDate = dateList.get(0);
		Date maxDate = dateList.get(dateList.size() - 1);
		for (int i = 0; i < NAVList.size(); i++)
		{
			SecurityDailyData tmp = NAVList.get(i);
			tmp.setAdjNAV(0.0);
			Date date = tmp.getDate();
			if (!date.after(maxDate))
			{
				for (int j = 0; j < dateList.size(); j++)
				{
					if (j == 0)
					{
						if (date.before(dateList.get(0)))
						{
							Date dateMark = dateList.get(0);
							double rateMark = (Double) rateMap.get(dateMark);
							double adjnav = tmp.getNAV() * rateMark;
							tmp.setAdjNAV(adjnav);
							break;
						}
					} else if (date.before(dateList.get(j)) && date.after(dateList.get(j - 1)))
					{
						Date dateMark = dateList.get(j);
						double rateMark = (Double) rateMap.get(dateMark);
						double adjnav = tmp.getNAV() * rateMark;
						tmp.setAdjNAV(adjnav);
						break;
					}
				}
			}
			// securityManager.updateDailyData(tmp);
			sdds.add(tmp);
		}
		securityManager.saveOrUpdateAll(sdds);

	}

	public double getYesterDayNAV(Date date, List<SecurityDailyData> list)
	{
		double value = 0;
		if (LTIDate.equals(date, list.get(0).getDate()))
		{
			return list.get(0).getNAV();
		}
		for (int i = 1; i < list.size(); i++)
		{
			Date afterdate = list.get(i).getDate();
			Date beforedate = list.get(i - 1).getDate();
			if (afterdate.after(date) && !beforedate.after(date))
			{
				value = list.get(i - 1).getNAV();
				break;
			}
		}
		return value;
	}

	public final class HotItemComparator implements Comparator
	{
		/*
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1)
		{
			Date obj1 = (Date) arg0;
			Date obj2 = (Date) arg1;
			if (obj2.after(obj1))
				return 1;
			else
				return 0;
		}
	}

	public final class HotItemComparator2 implements Comparator
	{
		/*
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1)
		{
			Date obj1 = (Date) arg0;
			Date obj2 = (Date) arg1;
			if (obj2.before(obj1))
				return 1;
			else
				return 0;
		}
	}

	@Override
	public void UpdateAdjClose(String securityName, Date date, SecurityDailyData sd1, SecurityDailyData sd2) throws Exception
	{

		Security se = securityManager.getBySymbol(securityName);

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		sdds = securityManager.getCloseList(se.getID(), date);

		if (sdds == null || sdds.size() == 0)
			return;

		SecurityDailyData pre = new SecurityDailyData();

		SecurityDailyData changePre = new SecurityDailyData();

		CopyUtil.Translate(sdds.get(0), pre);

		CopyUtil.Translate(sd2, changePre);

		if (changePre == null || changePre.getDate() == null)
		{
			CopyUtil.Translate(pre, changePre);
			changePre.setAdjClose(changePre.getClose());
		}

		if (changePre.getAdjClose() == null && changePre.getClose() == null)
			return;

		if (changePre.getAdjClose() == null || changePre.getAdjClose() == 0)
		{
			changePre.setAdjClose(changePre.getClose());
		}

		if (changePre != null)
			securityManager.updateDailyData(changePre);

		List<SecurityDailyData> newsdds = new ArrayList<SecurityDailyData>();

		for (int i = 1; i < sdds.size(); i++)
		{

			SecurityDailyData tmp = new SecurityDailyData();

			tmp = sdds.get(i);

			if (tmp == null || pre == null || tmp.getAdjClose() == null || tmp.getClose() == null || pre.getAdjClose() == null || tmp.getClose() == null)
				continue;

			/*
			 * double w1 = 1; if(tmp.getClose()!=0)w1 =
			 * tmp.getAdjClose()/tmp.getClose(); double w0 = 1;
			 * if(pre.getClose()!=0)w0 = pre.getAdjClose()/pre.getClose();
			 * 
			 * double tmpDividend = 1; if(pre.getClose()!=0) tmpDividend = 1-
			 * tmp.getDividend()/pre.getClose();
			 * 
			 * double split = 1; if(w0!=0){ split = w1 * tmpDividend / w0; }
			 * 
			 * if(Math.abs(split-1.0)<=0.05) { split = 1.0; } else
			 * if(Math.abs(split-2.0)<=0.05) { split = 2.0; } else
			 * if(Math.abs(split-3.0)<=0.05) { split = 3.0; } else
			 * if(Math.abs(split-4.0)<=0.05) { split = 4.0; }
			 * 
			 * double todayDividend = 1;
			 * if(pre.getClose()-tmp.getDividend()!=0)todayDividend = 1+
			 * tmp.getDividend() /(pre.getClose()-tmp.getDividend());
			 * 
			 * double totalDividend = todayDividend * changePre.getAdjClose();
			 * if(changePre.getClose()!=0)totalDividend /= changePre.getClose();
			 * 
			 * 
			 * double adjPrice = totalDividend * split * tmp.getClose();
			 */

			/****************************************************************************************/
			double w1 = 1;
			if (tmp.getClose() != 0)
				w1 = tmp.getAdjClose() / tmp.getClose();
			double w0 = 1;
			if (pre.getClose() != 0)
				w0 = pre.getAdjClose() / pre.getClose();

			double tmpDividend = 1;
			if (pre.getClose() != 0)
				tmpDividend = 1 - tmp.getDividend() / pre.getClose();

			double split = 1;
			if (w0 != 0)
			{
				split = w1 * tmpDividend / w0;
			}

			DecimalFormat df = new DecimalFormat("#.00");
			String tt = df.format(split);
			split = Double.parseDouble(tt);

			double todayDividend = 1;
			if (pre.getClose() - tmp.getDividend() != 0)
				todayDividend = 1 + tmp.getDividend() / (pre.getClose() - tmp.getDividend());

			double totalDividend = todayDividend * changePre.getAdjClose();
			if (changePre.getClose() != 0)
				totalDividend /= changePre.getClose();

			double adjPrice = totalDividend * split * tmp.getClose();
			/****************************************************************************************/

			CopyUtil.Translate(tmp, pre);

			tmp.setAdjClose(adjPrice);

			tmp.setSplit(split);

			newsdds.add(tmp);
			if (i % 1000 == 0)
				System.out.println(i);

			CopyUtil.Translate(tmp, changePre);

		}

		if (sd2 != null && sd2.getAdjClose() != null)
			sd1.setAdjClose(sd2.getAdjClose());
		if (sd1 != null)
		{
			newsdds.add(sd1);
		}
		securityManager.saveOrUpdateAll(newsdds);

	}

	@Override
	public boolean updateAdjustCloseFromStartDate(String symbol, Date startDate, List<SecurityDailyData> riskFreeList) throws Exception
	{
		Security security = securityManager.getBySymbol(symbol);

		if (security != null && security.getEndDate() != null)
		{
			int startYear = startDate.getYear() + 1900;
			Long securityID = security.getID();
			List<SecurityDailyData> securityDailyDataList = securityManager.getDailyDatas(securityID, startDate, security.getEndDate());

			if (securityDailyDataList != null && securityDailyDataList.size() > 0)
			{

				SecurityDailyData pre = new SecurityDailyData();

				SecurityDailyData changePre = new SecurityDailyData();

				CopyUtil.Translate(securityDailyDataList.get(0), pre);

				CopyUtil.Translate(securityDailyDataList.get(0), changePre);

				if (changePre.getAdjClose() == null && changePre.getClose() == null)
					return false;

				if (changePre.getAdjClose() == null || changePre.getAdjClose() == 0)
				{
					changePre.setAdjClose(changePre.getClose());
				}

				List<SecurityDailyData> newsdds = new ArrayList<SecurityDailyData>();

				for (int i = 1; i < securityDailyDataList.size(); i++)
				{

					SecurityDailyData tmp = securityDailyDataList.get(i);

					if (tmp == null || pre == null || tmp.getAdjClose() == null || tmp.getClose() == null || pre.getAdjClose() == null || tmp.getClose() == null)
						continue;

					double split = tmp.getSplit();
					// double w1 = 1;
					// if(tmp.getClose()!=0)w1 =
					// tmp.getAdjClose()/tmp.getClose();
					// double w0 = 1;
					// if(pre.getClose()!=0)w0 =
					// pre.getAdjClose()/pre.getClose();
					//
					// double tmpDividend = 1;
					// if(pre.getClose()!=0) tmpDividend = 1-
					// tmp.getDividend()/pre.getClose();
					//
					// double split = 1;
					// if(w0!=0){
					// split = w1 * tmpDividend / w0;
					// }
					//
					// DecimalFormat df = new DecimalFormat("#.00");
					// String tt = df.format(split);
					// split = Double.parseDouble(tt);

					// double todayDividend = 1;
					//
					//
					// if(changePre.getClose()!= 0)todayDividend =
					// (tmp.getClose() + tmp.getDividend()) /
					// changePre.getClose();
					//
					// double totalDividend = todayDividend *
					// changePre.getAdjClose();
					// if(changePre.getClose()!=0)totalDividend /=
					// changePre.getClose();

					double percentage = (tmp.getClose() * split + tmp.getDividend()) / changePre.getClose();

					double adjPrice = percentage * changePre.getAdjClose();

					CopyUtil.Translate(tmp, pre);

					tmp.setAdjClose(adjPrice);

					tmp.setSplit(split);

					newsdds.add(tmp);

					CopyUtil.Translate(tmp, changePre);

				}
				securityManager.saveOrUpdateAll(newsdds);
				securityManager.deleteSecurityMPTAfterYear(securityID, startYear);

				Date sDate = newsdds.get(0).getDate();
				sDate = LTIDate.getNewTradingDate(sDate, TimeUnit.DAILY, -5);
				Date eDate = newsdds.get(newsdds.size() - 1).getDate();
				eDate = LTIDate.getNewTradingDate(eDate, TimeUnit.DAILY, 5);
				List<SecurityDailyData> benchs = securityManager.getDailyDatas(security.getAssetClass().getBenchmarkID(), sDate, eDate);
				securityManager.calculateSecurityMPTAfterYear(securityID, newsdds, benchs, riskFreeList);
				// save the last mpt date
				security.setMptLastDate(newsdds.get(newsdds.size() - 1).getDate());
				securityManager.updateMPTLastDate(security);
				return true;
			}
		}
		return false;
	}

	public boolean updateAdjustNAVFromStartDate(String symbol, Date startDate) throws Exception
	{
		Security security = securityManager.getBySymbol(symbol);

		if (security != null && security.getNavLastDate() != null)
		{
			Long securityID = security.getID();
			List<SecurityDailyData> securityDailyDataList = securityManager.getDailyDatas(securityID, startDate, security.getNavLastDate());

			if (securityDailyDataList != null && securityDailyDataList.size() > 0)
			{

				SecurityDailyData pre = new SecurityDailyData();

				SecurityDailyData changePre = new SecurityDailyData();

				CopyUtil.Translate(securityDailyDataList.get(0), pre);

				CopyUtil.Translate(securityDailyDataList.get(0), changePre);

				if (changePre.getAdjNAV() == null && changePre.getNAV() == null)
					return false;

				if (changePre.getAdjNAV() == null || changePre.getAdjNAV() == 0)
				{
					changePre.setAdjNAV(changePre.getNAV());
				}

				List<SecurityDailyData> newsdds = new ArrayList<SecurityDailyData>();

				for (int i = 1; i < securityDailyDataList.size(); i++)
				{

					SecurityDailyData tmp = securityDailyDataList.get(i);

					if (tmp == null || pre == null || tmp.getNAV() == null || pre.getAdjNAV() == null || pre.getNAV() == null)
						continue;

					double split = 1.0;
					if (tmp.getSplit() != null && tmp.getSplit() != 0.0)
						split = tmp.getSplit();

					double percentage = (tmp.getNAV() * split + tmp.getDividend()) / changePre.getNAV();

					double adjNAV = percentage * changePre.getAdjNAV();

					tmp.setAdjNAV(adjNAV);

					tmp.setSplit(split);

					CopyUtil.Translate(tmp, pre);

					newsdds.add(tmp);

					CopyUtil.Translate(tmp, changePre);

				}
				securityManager.saveOrUpdateAll(newsdds);
				return true;
			}
		}
		return false;
	}

	public List<SecurityDailyData> UpdateAdjClose(List<SecurityDailyData> newList, SecurityDailyData oldLastData, boolean fromStart) throws Exception
	{

		if (newList == null || newList.size() == 0)
			return newList;

		int startIndex = 1;
		if (fromStart)
			startIndex = 0;

		List<SecurityDailyData> tmpList = new ArrayList<SecurityDailyData>();
		for (int i = newList.size() - 1; i >= 0; i--)
		{
			tmpList.add(newList.get(i));
		}
		newList = tmpList;

		/*
		 * pre 前一个从csv读出的 changePre 已经修改的 tmp 当前的从csv读出的
		 */

		SecurityDailyData pre = new SecurityDailyData(); // 下载的文件里拿出来

		SecurityDailyData changePre = new SecurityDailyData(); // 数据库里拿出来

		CopyUtil.Translate(newList.get(0), pre);

		CopyUtil.Translate(oldLastData, changePre);

		if (changePre == null || changePre.getDate() == null)
		{
			CopyUtil.Translate(pre, changePre);// changePre为空的话就把下载文件里的值值到changePre中
			changePre.setAdjClose(changePre.getClose());// 把调整后的收盘价设置为下载文件中的收盘价
		}

		if (changePre.getAdjClose() == null && changePre.getClose() == null)
			return newList;

		if (changePre.getAdjClose() == null || changePre.getAdjClose() == 0)
		{
			changePre.setAdjClose(changePre.getClose());
		}

		// if(changePre!=null &&)securityManager.updateDailyData(changePre);

		List<SecurityDailyData> newsdds = new ArrayList<SecurityDailyData>();

		// newsdds.add(changePre);
		for (int i = startIndex; i < newList.size(); i++)
		{

			SecurityDailyData tmp = new SecurityDailyData();

			tmp = newList.get(i);

			if (tmp == null || pre == null || tmp.getAdjClose() == null || tmp.getClose() == null || pre.getAdjClose() == null || tmp.getClose() == null)
				continue;

			double w1 = 1;
			if (tmp.getClose() != 0)
				w1 = tmp.getAdjClose() / tmp.getClose();
			double w0 = 1;
			if (pre.getClose() != 0)
				w0 = pre.getAdjClose() / pre.getClose();

			double tmpDividend = 1;
			if (pre.getClose() != 0)
				tmpDividend = 1 - tmp.getDividend() / pre.getClose();

			double split = 1;
			if (w0 != 0)
			{
				split = w1 * tmpDividend / w0;
			}

			DecimalFormat df = new DecimalFormat("#.00");
			String tt = df.format(split);
			split = Double.parseDouble(tt);

			/*
			 * if(split<1.35)split = 1.0; else if(split<1.75)split = 1.5; else
			 * if(split<2.35)split = 2.0; else if(split<2.75)split = 2.5; else
			 * if(split<3.35)split = 3.0; else if(split<3.75)split = 3.5; else
			 * if(split<4.35)split = 4.0; else{ DecimalFormat df = new
			 * DecimalFormat("#.0"); String tt = df.format(split); split =
			 * Double.parseDouble(tt); }
			 * 
			 * /*if(Math.abs(split-1.0)<=0.1) { split = 1.0; } else
			 * if(Math.abs(split-2.0)<=0.1) { split = 2.0; } else
			 * if(Math.abs(split-3.0)<=0.1) { split = 3.0; } else
			 * if(Math.abs(split-4.0)<=0.1) { split = 4.0; }
			 */

			double percentage = (tmp.getClose() * split + tmp.getDividend()) / changePre.getClose();

			// double todayDividend = 1;
			// if(pre.getClose()-tmp.getDividend()!=0)todayDividend = 1+
			// tmp.getDividend() /(pre.getClose()-tmp.getDividend());
			//
			// double totalDividend = todayDividend * changePre.getAdjClose();
			// if(changePre.getClose()!=0)totalDividend /= changePre.getClose();
			//
			//
			// double adjPrice = totalDividend * split * tmp.getClose();

			double adjPrice = percentage * changePre.getAdjClose();

			CopyUtil.Translate(tmp, pre);

			/*
			 * DecimalFormat df = new DecimalFormat("#.00"); String tt =
			 * df.format(adjPrice); adjPrice = Double.parseDouble(tt);
			 */

			tmp.setAdjClose(adjPrice);

			tmp.setSplit(split);

			newsdds.add(tmp);

			CopyUtil.Translate(tmp, changePre);

		}

		/*
		 * if(sd2!=null &&
		 * sd2.getAdjClose()!=null)sd1.setAdjClose(sd2.getAdjClose());
		 * if(sd1!=null){ newsdds.add(sd1); }
		 */
		return newsdds;
	}

	public void UpdateAdjNAV(String securityName, Date date) throws Exception
	{

		Security security = securityManager.getBySymbol(securityName);

		if (security != null && security.getNavLastDate() != null)
		{

			List<SecurityDailyData> securityDailyDataList = securityManager.getNAVList(security.getID(), date);

			if (securityDailyDataList != null && securityDailyDataList.size() > 0)
			{

				SecurityDailyData pre = new SecurityDailyData();

				SecurityDailyData changePre = new SecurityDailyData();

				CopyUtil.Translate(securityDailyDataList.get(0), pre);

				CopyUtil.Translate(securityDailyDataList.get(0), changePre);

				if (changePre.getAdjNAV() == null && changePre.getNAV() == null)
					return;

				if (changePre.getAdjNAV() == null || changePre.getAdjNAV() == 0)
				{
					changePre.setAdjNAV(changePre.getNAV());
				}

				List<SecurityDailyData> newsdds = new ArrayList<SecurityDailyData>();

				for (int i = 1; i < securityDailyDataList.size(); i++)
				{

					SecurityDailyData tmp = securityDailyDataList.get(i);

					if (tmp == null || pre == null || tmp.getNAV() == null || pre.getAdjNAV() == null || pre.getNAV() == null)
						continue;

					double split = 1.0;
					
					if (tmp.getSplit() != null && tmp.getSplit() != 0.0)
						split = tmp.getSplit();

					double percentage = (tmp.getNAV() * split + tmp.getDividend()) / changePre.getNAV();

					double adjNAV = percentage * changePre.getAdjNAV();

					tmp.setAdjNAV(adjNAV);

					tmp.setSplit(split);

					CopyUtil.Translate(tmp, pre);

					newsdds.add(tmp);

					CopyUtil.Translate(tmp, changePre);

				}
				securityManager.saveOrUpdateAll(newsdds);
			}
		}
		//
		// Security se = securityManager.getBySymbol(securityName);
		//
		// List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
		//
		// sdds = securityManager.getNAVList(se.getID(),date);
		//
		// if(sdds == null || sdds.size() == 0)return;
		//
		// boolean flag = false;
		//
		// for(int i=0;i<sdds.size();i++)
		// {
		// SecurityDailyData tmp = sdds.get(i);
		//
		// double dividend = 1;
		//
		// double adjNAV = 0;
		//
		// if(tmp.getAdjClose() != null && tmp.getClose() != null &&
		// tmp.getClose() != 0)
		// {
		// dividend = tmp.getAdjClose() / tmp.getClose();
		//
		// adjNAV = dividend * tmp.getNAV();
		//
		// flag = true;
		// }
		//
		// else if(i == 0)
		// {
		// adjNAV = tmp.getNAV();
		// }
		//
		// else
		// {
		// if(flag){
		// adjNAV = sdds.get(i-1).getAdjNAV() * tmp.getNAV()/
		// sdds.get(i-1).getNAV();
		// tmp.setAdjClose(sdds.get(i-1).getAdjClose());
		// tmp.setClose(sdds.get(i-1).getClose());
		// }
		// else adjNAV = tmp.getNAV();
		// }
		//
		// tmp.setAdjNAV(adjNAV);
		// }
		// securityManager.saveOrUpdateAll(sdds);
		/*
		 * SecurityDailyData pre = new SecurityDailyData();
		 * 
		 * SecurityDailyData changePre = new SecurityDailyData();
		 * 
		 * CopyUtil.Translate(sdds.get(0),pre);
		 * 
		 * CopyUtil.Translate(sd, changePre);
		 * 
		 * 
		 * 
		 * if(changePre.getAdjNAV() == null && changePre.getNAV() ==
		 * null)return;
		 * 
		 * if(changePre.getAdjNAV() == null || changePre.getAdjNAV() == 0){
		 * changePre.setAdjNAV(changePre.getNAV()); }
		 * 
		 * if(changePre!=null)securityManager.updateDailyData(changePre);
		 * 
		 * for(int i=1;i<sdds.size();i++){
		 * 
		 * SecurityDailyData tmp = new SecurityDailyData();
		 * 
		 * tmp = sdds.get(i);
		 * 
		 * if(tmp == null || pre == null || tmp.getAdjNAV() == null ||
		 * tmp.getNAV() == null || pre.getAdjNAV() == null || tmp.getNAV() ==
		 * null) continue;
		 * 
		 * double w1 = tmp.getAdjNAV()/tmp.getNAV(); double w0 =
		 * pre.getAdjNAV()/pre.getNAV();
		 * 
		 * double tempDividend = 0;
		 * 
		 * if(tmp.getDividend() != null)tempDividend = tmp.getDividend();
		 * 
		 * double tmpDividend = 1 - tempDividend/pre.getNAV();
		 * 
		 * double split = 1;
		 * 
		 * if(w0!=0){ split = w1 * tmpDividend / w0; if(Math.abs(split - 1) <
		 * 0.1)split = 1; } double todayDividend = 1 + tempDividend
		 * /tmp.getNAV();
		 * 
		 * double totalDividend = todayDividend * changePre.getAdjNAV() /
		 * changePre.getNAV();
		 * 
		 * 
		 * double adjPrice = totalDividend * split * tmp.getNAV();
		 * 
		 * CopyUtil.Translate( tmp,pre);
		 * 
		 * tmp.setAdjNAV(adjPrice);
		 * 
		 * securityManager.updateDailyData(tmp);
		 * 
		 * CopyUtil.Translate(tmp,changePre );
		 * 
		 * }
		 */
	}

	/**
	 * @author CCD
	 * @param fileName
	 *            the absolute name of the .xls
	 * @param logDate
	 *            update the shiller S&P500 data in the database added on
	 *            2009-10-12 last modified on 2009-12-07
	 */
	public void UpdateShillerSP500Data(String fileName, Date logDate)
	{
		try
		{
			SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
			Configuration.writeLog("Shiller S&P 500 Earning and PE", logDate, new Date(), "Start to update S&P 500 Earning and PE");
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// Choosing the work sheet "stock Data".
			HSSFSheet sheet = wb.getSheet("Data");
			boolean start = false;
			Configuration.writeLog("Shiller S&P 500 Earning and PE", logDate, new Date(), "Start to read excel file...");
			List<ShillerSP500> shillerSP500List = new ArrayList<ShillerSP500>();
			ShillerSP500 shillerSP500;
			int month = 0;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
			Date spDate;
			Double pe, realEarnings, earnings = 0.0, peakRealEarnings = 0.0;
			int size = 0;
			int peSize = 0;
			int ppeSize = 0;
			double totalEarnings10, totalRealEarnings10, avgEarnings10, totalPriceEarnings, avgPriceEarnings, peakEarnings, pricePeakEarnings, totalPricePeakEarnings, avgPricePeakEarnings;
			totalEarnings10 = 0;
			totalRealEarnings10 = 0;
			totalPriceEarnings = 0;
			peakEarnings = 0;
			totalPricePeakEarnings = 0;

			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet.rowIterator(); rit.hasNext();)
			{
				HSSFRow row = rit.next();
				if (!start)
				{
					if (row.getCell(0) != null && row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING && row.getCell(0).toString().trim().equals("Date"))
					{
						start = true;
					}
				} else
				{
					if (row.getCell(0) != null)
					{
						shillerSP500 = new ShillerSP500();
						spDate = dateFormat.parse(row.getCell(0).toString());
						if (month == 8 && spDate.getMonth() == 0)
							spDate.setMonth(9);
						month = spDate.getMonth();
						shillerSP500.setSPDate(spDate);
						if (row.getCell(1) != null)
							shillerSP500.setComp(row.getCell(1).getNumericCellValue());
						if (row.getCell(2) != null)
						{
							try
							{
								shillerSP500.setDividend(row.getCell(2).getNumericCellValue());
							} catch (Exception e)
							{
								shillerSP500.setDividend(0.0);
							}
						} else
							shillerSP500.setDividend(0.0);
						if (row.getCell(3) != null)
						{
							try
							{
								earnings = row.getCell(3).getNumericCellValue();
								shillerSP500.setEarnings(earnings);
							} catch (Exception e)
							{
								shillerSP500.setEarnings(0.0);
							}
						} else
							shillerSP500.setEarnings(0.0);
						if (row.getCell(4) != null)
							shillerSP500.setCPI(row.getCell(4).getNumericCellValue());
						if (row.getCell(5) != null)
							shillerSP500.setDateFraction(row.getCell(5).getNumericCellValue());
						if (row.getCell(6) != null)
						{
							try
							{
								shillerSP500.setLongInterestRate(row.getCell(6).getNumericCellValue());
							} catch (Exception e)
							{
								shillerSP500.setLongInterestRate(0.0);
							}
						} else
							shillerSP500.setLongInterestRate(0.0);

						if (row.getCell(7) != null)
							shillerSP500.setRealPrice(row.getCell(7).getNumericCellValue());
						if (row.getCell(8) != null)
							try
							{
								shillerSP500.setRealDividend(row.getCell(8).getNumericCellValue());
							} catch (Exception e)
							{
							}
						if (row.getCell(9) != null)
						{
							try
							{
								realEarnings = row.getCell(9).getNumericCellValue();
								shillerSP500.setRealEarnings(realEarnings);
								if (size < 120)
								{
									// all the get Earnings we use normal
									// earnings
									// avgPeakPriceEarings we use real earnings
									totalEarnings10 += earnings;
									totalRealEarnings10 += realEarnings;
									if (earnings > peakEarnings)
										peakEarnings = earnings;
									if (realEarnings > peakRealEarnings)
										peakRealEarnings = realEarnings;
								} else
								{
									if (size > 1668)
										System.out.println(size);
									++ppeSize;
									shillerSP500.setPeakEarnings10(peakEarnings);
									shillerSP500.setPeakRealEarnings10(peakRealEarnings);
									shillerSP500.setAvgEarnings10(totalEarnings10 / 120);
									shillerSP500.setAvgRealEarnings10(totalRealEarnings10 / 120);
									avgPricePeakEarnings = shillerSP500.getRealPrice() / peakRealEarnings;
									shillerSP500.setPricePeakEarnings10(avgPricePeakEarnings);
									totalPricePeakEarnings += avgPricePeakEarnings;
									shillerSP500.setAvgPricePeakEarnings(totalPricePeakEarnings / ppeSize);
									// adjust 10 year total earnings and real
									// earnings by subing the first and adding
									// the last
									totalEarnings10 += earnings - shillerSP500List.get(size - 120).getEarnings();
									totalRealEarnings10 += realEarnings - shillerSP500List.get(size - 120).getRealEarnings();
									// adjust peak earnings and peak real
									// earnings
									peakEarnings = earnings;
									peakRealEarnings = realEarnings;
									for (int j = size - 1, k = 0; k < 119; ++k, --j)
									{
										ShillerSP500 temp = shillerSP500List.get(j);
										if (temp.getEarnings() > peakEarnings)
											peakEarnings = temp.getEarnings();
										if (temp.getRealEarnings() > peakRealEarnings)
											peakRealEarnings = temp.getRealEarnings();
									}
								}
							} catch (Exception e)
							{

							}

						}
						if (row.getCell(10) != null)
						{
							try
							{
								pe = row.getCell(10).getNumericCellValue();
								shillerSP500.setPriceEarningsRatio(pe);
								++peSize;
								totalPriceEarnings += pe;
								shillerSP500.setAvgPriceEarnings(totalPriceEarnings / peSize);
							} catch (Exception e)
							{
								/** pe is NA */
								System.out.println("null");
							}
						}
						shillerSP500.setCPIRatio(shillerSP500.getRealPrice() / shillerSP500.getComp());
						shillerSP500List.add(shillerSP500);
						++size;
					} else
						break;
				}
			}
			if (shillerSP500List.size() > 0)
			{
				securityManager.removeAllShillerSP500();
				securityManager.saveAllShillerSP500(shillerSP500List);
			}
			// System.out.println(shillerSP500List.size());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void UpdateSP500EaringAndPE(String fileName, Date logDate) throws ParseException
	{
		try
		{
			Configuration.writeLog("S&P 500 Earning and PE", logDate, new Date(), "Start to update S&P 500 Earning and PE");
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			// Choosing the work sheet "ESTIMATES&PEs".
			HSSFSheet sheet = wb.getSheet("ESTIMATES&PEs");

			int oe = 0; // The column index of EPS in the document.

			boolean hr = false; // whether reading the data header currently.
			boolean er = false; // whether reading the estimate earning
								// currently.
			boolean ar = false; // whether reading the actual earning currently.

			// SPQuaterEarning list to store all the quarter earnings in the
			// work sheet.
			List<SPQuarterValue> qes = new ArrayList<SPQuarterValue>();
			Configuration.writeLog("S&P 500 Earning and PE", logDate, new Date(), "Start to read excel file...");
			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet.rowIterator(); rit.hasNext();)
			{
				HSSFRow row = rit.next();

				if (row.getCell(0) != null && row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING && row.getCell(0).toString().trim().equals("QUARTER"))
				{
					hr = true;
				}
				if (hr)
				{
					// The first cell content "ESTIMATES" means the start of
					// estimates earning.
					/*
					 * Cell values of ESTIMATES and ACTUAL have changed in the
					 * SPData file downloaded from SP site. Sep 15 2009
					 */
					if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING && row.getCell(0).toString().equals("ESTIMATES"))
					{
						er = true;
						continue;
					}
					// currently reading estimate earning and meet the first
					// line of blank means the
					// ending of estimate earning.
					else if (er && row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_BLANK)
					{
						er = false;
						continue;
					}
					// The first cell content "ACTUALS" means the following
					// lines are actual earnings.
					else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING && row.getCell(0).toString().contains("ACTUAL"))
					{
						ar = true;
						continue;
					}
					// The first cell's format is not a date means the ending of
					// Actual Earning.
					// The format may be mm/dd/yyyy or dd-第几月-yyyy
					else if (ar && !row.getCell(0).toString().matches("\\d{1,2}/\\d{1,2}/\\d{4}.*|\\d{1,2}-.{1,3}-\\d{4}.*"))
					{
						ar = false;
						continue;
					} else if (!er && !ar)
						// Looking for the EPS index in the work sheet.
						// The column index of the first occurrence of "PER SHR"
						for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>) row.cellIterator(); cit.hasNext();)
						{
							HSSFCell cell = cit.next();
							if (oe == 0 && cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cell.getRichStringCellValue().getString().equals("PER SHR"))
							{
								oe = cell.getColumnIndex();
								break;
							}
						}
					// Reading estimate earnings.
					else if (er)
					{
						SPQuarterValue qe = new SPQuarterValue();
						// If the date is presented in the format of
						// "MM/dd/yyyy"
						if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING)
						{
							SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
							Date qeDate = dateFormat.parse(row.getCell(0).toString());
							qe.date = qeDate;
						}
						// If the date is presented as the interval days to
						// 1900-01-00
						else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							// excel takes 1900-01-00 as the start day, and java
							// takes 1970-01-01;
							// besides, excel consider 1900 to be leap year,
							// so we need to subtract 25567(days between 1900
							// and 1970)+2 days from the days we get from excel.
							long time = ((long) row.getCell(0).getNumericCellValue() - 25569) * 24 * 60 * 60 * 1000;
							Date qeDate = new Date(time);
							qe.date = qeDate;
						}
						// Get PES
						if (row.getCell(oe).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							qe.value = row.getCell(oe).getNumericCellValue();
						}
						qes.add(qe);
					}
					// Reading the actual earnings.
					else if (ar)
					{
						SPQuarterValue qe = new SPQuarterValue();
						if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING)
						{
							SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
							Date qeDate = dateFormat.parse(row.getCell(0).toString());
							qe.date = qeDate;
						} else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							// excel takes 1900-01-00 as the start day, and java
							// takes 1970-01-01;
							// besides, excel consider 1900 to be leap year,
							// so we need to subtract 25567(days between 1900
							// and 1970)+2 days from the days we get from excel.
							long time = ((long) row.getCell(0).getNumericCellValue() - 25569) * 24 * 60 * 60 * 1000;
							Date qeDate = new Date(time);
							qe.date = qeDate;
						}
						if (row.getCell(oe).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							qe.value = row.getCell(oe).getNumericCellValue();
						}
						qes.add(qe);
					}
				}
			}
			Configuration.writeLog("S&P 500 Earning and PE", logDate, new Date(), "Finish read from the excel file.");
			List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
			Configuration.writeLog("S&P 500 Earning and PE", logDate, new Date(), "Starting update database...");
			/****** actually List has just one element *****/
			for (int j = 0; j < dailyUpdateListenerList.size(); ++j)
			{
				if (dailyUpdateListenerList.get(j) != null)
					dailyUpdateListenerList.get(j).copy_total(qes.size());
			}
			for (int i = 0; i < qes.size(); i++)
			{
				for (int j = 0; j < dailyUpdateListenerList.size(); ++j)
				{
					if (dailyUpdateListenerList.get(j) != null)
						dailyUpdateListenerList.get(j).copy_current(i + 1);
				}
				Date startDate, endDate;
				double annualEarning; // store the sum of last 4 quarters'
										// earning to the start date.

				// Not the earliest date in the work sheet.
				if (i != qes.size() - 1)
				{
					startDate = qes.get(i + 1).date;

					// One day forward the next quarter.
					endDate = LTIDate.getYesterday(qes.get(i).date);

					// Not the 3th earliest date in the work sheet.
					if (i < qes.size() - 4)
						annualEarning = qes.get(i + 1).value + qes.get(i + 2).value + qes.get(i + 3).value + qes.get(i + 4).value;
					else
						annualEarning = 0d;

					// Get security daily data from the start date to the end
					// date
					sdds = securityManager.getDailyDatas(securityManager.getBySymbol("^GSPC").getID(), startDate, endDate);

					if (sdds != null && sdds.size() != 0)
					{
						System.out.println(sdds.size());
						for (SecurityDailyData e : sdds)
						{
							// Set EPS to annualEarning
							System.out.println(e.getDate());
							e.setEPS(annualEarning);

							// Set PE
							if (annualEarning != 0)
								e.setPE(e.getClose().doubleValue() / annualEarning);
							else
								e.setPE(null);
						}
						securityManager.saveOrUpdateAll(sdds);
						Configuration.writeLog("S&P 500 Earning and PE", logDate, new Date(), startDate + " to " + endDate + " updated.");
					}

				}
			}
			Configuration.writeLog("S&P 500 Earning and PE", logDate, new Date(), "Finish update database.");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private class SPQuarterValue
	{
		public Date date;
		public double value;

		public String toString()
		{
			return date.toLocaleString() + " " + value;
		}
	}

	/**************
	 * Change the SP500 Dividend from a security attribute to an
	 * indicator**********
	 * 
	 * @Override public void UpdateSP500Dividend(String fileName, Date
	 *           logDate)throws ParseException { try{
	 *           Configuration.writeLog("S&P 500 Dividends",logDate, new Date(),
	 *           "Start to update S&P 500 Dividends"); POIFSFileSystem fs=new
	 *           POIFSFileSystem(new FileInputStream(fileName)); HSSFWorkbook
	 *           wb=new HSSFWorkbook(fs); //Choosing the work sheet "DIVIDENDS".
	 *           HSSFSheet sheet= wb.getSheet("DIVIDENDS");
	 * 
	 *           int flg = 0;//mark the column index of dividends; boolean dh =
	 *           false;//mark the row of data header starts; boolean dt =
	 *           false;//mark the row of data body starts;
	 *           List<SPQuarterValue>qvs = new ArrayList<SPQuarterValue>();
	 * 
	 *           Configuration.writeLog("S&P 500 Dividends",logDate, new Date(),
	 *           "Start to read excel file..."); for(Iterator<HSSFRow> rit =
	 *           (Iterator<HSSFRow>)sheet.rowIterator(); rit.hasNext();){
	 *           HSSFRow row = rit.next();
	 * 
	 *           if(row.getCell(0)!=null&&row.getCell(0).getCellType()==HSSFCell
	 *           .CELL_TYPE_STRING&&row.getCell(0).toString().trim().equals(
	 *           "QUARTER")){ dh = true; } if(dh){ if(!dt){ //find the column of
	 *           dividend for (Iterator<HSSFCell> cit =
	 *           (Iterator<HSSFCell>)row.cellIterator(); cit.hasNext(); ){
	 *           HSSFCell cell = cit.next();
	 *           if(flg==0&&cell.getCellType()==HSSFCell
	 *           .CELL_TYPE_STRING&&cell.getRichStringCellValue
	 *           ().getString().equals("PER SHR")){ flg = cell.getColumnIndex();
	 *           dt = true; break; } } } //read the date and dividend data else{
	 *           SPQuarterValue qv=new SPQuarterValue(); //format the date
	 *           if(row
	 *           .getCell(0)!=null&&row.getCell(0).getCellType()==HSSFCell.
	 *           CELL_TYPE_STRING) { SimpleDateFormat dateFormat=new
	 *           SimpleDateFormat ("MM/dd/yyyy"); Date
	 *           qvDate=dateFormat.parse(row.getCell(0).toString());
	 *           qv.date=qvDate; } else
	 *           if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
	 *           //excel takes 1900-01-00 as the start day, and java takes
	 *           1970-01-01; //besides, excel consider 1900 to be leap year,
	 *           //so we need to subtract 25567(days between 1900 and 1970)+2
	 *           days from the days we get from excel. long
	 *           time=((long)row.getCell
	 *           (0).getNumericCellValue()-25569)*24*60*60*1000; Date qvDate=new
	 *           Date(time); qv.date=qvDate; } //get dividend
	 *           if(row.getCell(flg)
	 *           !=null&&row.getCell(flg).getCellType()==HSSFCell
	 *           .CELL_TYPE_NUMERIC) {
	 *           qv.value=row.getCell(flg).getNumericCellValue(); } qvs.add(qv);
	 *           } } }
	 * 
	 *           Configuration.writeLog("S&P 500 Dividends",logDate, new Date(),
	 *           "Finish reading the excel file.");
	 *           Configuration.writeLog("S&P 500 Dividends",logDate, new Date(),
	 *           "Start updating database..."); List<SecurityDailyData> sdds=new
	 *           ArrayList<SecurityDailyData>(); for(int i=0;i<qvs.size();i++){
	 *           Date startDate ,endDate; double quarterDividend; //store the
	 *           sum of last 4 quarters' dividend from the start date. //Not the
	 *           earliest date in the work sheet. if(i!=qvs.size()-1) {
	 *           startDate=qvs.get(i+1).date;
	 *           endDate=LTIDate.getYesterday(qvs.get(i).date);//get the date
	 *           before next quarter. quarterDividend = qvs.get(i+1).value;
	 * 
	 *           //Get security daily data from the start date to the end date
	 *           sdds=securityManager.getDailyDatas(securityManager.getBySymbol(
	 *           "^GSPC").getID(), startDate, endDate);
	 *           if(sdds!=null&&sdds.size()!=0) { for(SecurityDailyData e:sdds)
	 *           { e.setDividend(quarterDividend); }
	 *           securityManager.saveOrUpdateAll(sdds);
	 *           Configuration.writeLog("S&P 500 Dividends",logDate, new Date(),
	 *           startDate+" to "+endDate+" updated."); }
	 * 
	 *           } } Configuration.writeLog("S&P 500 Dividends",logDate, new
	 *           Date(), "Finish updateing database.");
	 * 
	 *           }catch (IOException e) { e.printStackTrace(); }
	 * 
	 *           }
	 ****************************************/

	@Override
	public void UpdateSP500Dividend(String path) throws ParseException
	{
		/* select the date and put/call ratios from the csv file */
		List<String> tmp = new ArrayList();
		String filePath = Configuration.getTempDir() + "SP500 Dividend.csv";
		try
		{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// Choosing the work sheet "DIVIDENDS".
			HSSFSheet sheet = wb.getSheet("DIVIDENDS");

			int flg = 0;// mark the column index of dividends;
			boolean dh = false;// mark the row of data header starts;
			boolean dt = false;// mark the row of data body starts;
			List<SPQuarterValue> qvs = new ArrayList<SPQuarterValue>();

			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet.rowIterator(); rit.hasNext();)
			{
				HSSFRow row = rit.next();

				if (row.getCell(0) != null && row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING && row.getCell(0).toString().trim().equals("QUARTER"))
				{
					dh = true;
				}
				if (dh)
				{
					if (!dt)
					{
						// find the column of dividend
						for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>) row.cellIterator(); cit.hasNext();)
						{
							HSSFCell cell = cit.next();
							if (flg == 0 && cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cell.getRichStringCellValue().getString().equals("PER SHR"))
							{
								flg = cell.getColumnIndex();
								dt = true;
								break;
							}
						}
					}
					// read the date and dividend
					else
					{
						SPQuarterValue qv = new SPQuarterValue();
						String d = "0";
						String v = "0";
						// format the date
						if (row.getCell(0) != null && row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING)
						{
							String[] date = row.getCell(0).toString().split("/");
							d = date[2] + "-" + date[0] + "-" + date[1];
							System.out.println(d);
						} else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							// excel takes 1900-01-00 as the start day, and java
							// takes 1970-01-01;
							// besides, excel consider 1900 to be leap year,
							// so we need to subtract 25567(days between 1900
							// and 1970)+2 days from the days we get from excel.
							long time = ((long) row.getCell(0).getNumericCellValue() - 25569) * 24 * 60 * 60 * 1000;
							Date qvDate = new Date(time);
							SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
							d = f.format(qvDate);
						}
						// get dividend
						if (row.getCell(flg) != null && row.getCell(flg).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							qv.value = row.getCell(flg).getNumericCellValue();
							v = Double.toString(qv.value);
						}
						tmp.add(d.trim() + "," + v.trim() + "," + "\n");
					}
				}
			}

			// write the date and value columns into a new file
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos, "utf-8");
			bw = new BufferedWriter(osw);
			bw.write("DATE,VALUE\r\n");
			for (int i = 0; i < tmp.size(); i++)
			{
				bw.write(tmp.get(i));
			}
			bw.flush();
			bw.close();
			osw.close();
			fos.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// import the dividend data into database
		try
		{
			this.updateEcom2(filePath, "SPDIVIDEND");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addListener(DailyUpdateListener dailyUpdateListener)
	{
		dailyUpdateListenerList.add(dailyUpdateListener);
	}

	public void removeListener(DailyUpdateListener dailyUpdateListener)
	{
		dailyUpdateListenerList.remove(dailyUpdateListener);
	}

	/**
	 * @author CCD
	 * @param fileName
	 *            the file constains all the new security add new securities to
	 *            database for special file "msall_20K_funds_10_05_2009"
	 *            modified by CCD last on 2010-01-19
	 */
	public int addNewSecurityForSpecial(String fileName)
	{
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		int success_count = 0;
		int fail_count = 0;
		try
		{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// Choosing the work sheet "stock Data".
			HSSFSheet sheet = wb.getSheet("msall_20K_funds_10_05_2009");
			boolean start = false;
			List<Security> securityList = new ArrayList<Security>();
			String category;
			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet.rowIterator(); rit.hasNext();)
			{
				HSSFRow row = rit.next();
				if (!start)
				{
					if (row.getCell(0) != null && row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING && row.getCell(0).toString().trim().equals("Symbol"))
						start = true;
				} else
				{
					Security security = new Security();
					String symbol = row.getCell(0).toString();
					Security oldSecurity = securityManager.getBySymbol(symbol);
					if (oldSecurity != null)// the security has existed in our
											// database
						continue;
					security.setSymbol(symbol);// symbol
					security.setName(row.getCell(1).toString()); // description
					category = row.getCell(2).toString();
					AssetClass assetClass = assetClassManager.get(category);
					if (assetClass == null)
					{
						String assetClassName = assetClassManager.getAssetClassNameForMutualFundAndETF(security.getSymbol());
						assetClass = assetClassManager.get(assetClassName);
						if (assetClass == null)
						{
							assetClass = new AssetClass();
							assetClass.setName(assetClassName);
							assetClass.setParentID(0L);
							assetClassManager.save(assetClass);
							assetClass = assetClassManager.get(assetClassName);
						}
						if (assetClass != null)
							security.setClassID(assetClass.getID());
					}
					security.setDiversified(false);
					security.setSecurityType(4);
					security.setManual(0);
					securityList.add(security);
					System.out.println(security.getSymbol());
					success_count++;
				}
			}
			securityManager.saveOrUpdateAllSecurity(securityList);
			// System.out.println("Success: "+ success_count);
			return success_count;
		} catch (FileNotFoundException e)
		{
			// System.out.println("File not found");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @author CCD delete the duplicate security, delete the one with the larger
	 *         ID modified by CCD last on 2010-01-19
	 */
	public int deleteDuplicateSecurity()
	{
		Set<String> securitySet = new HashSet<String>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.or(Restrictions.eq("SecurityType", 4), Restrictions.eq("SecurityType", 0)));
		detachedCriteria.addOrder(Order.asc("ID"));
		List<Security> securityList = securityManager.getSecurities(detachedCriteria);
		int deleteNum = 0;
		for (int i = 0; i < securityList.size(); ++i)
		{
			Security security = securityList.get(i);
			String symbol = security.getSymbol();
			// System.out.println(security.getID()+ " " + symbol);
			Long securityID = security.getID();
			if (securitySet.contains(symbol))
			{
				System.out.println(security.getID() + " " + symbol);
				securityManager.deleteSecurityCascade(securityID);
				deleteNum++;
			} else
				securitySet.add(symbol);
		}
		return deleteNum;
	}

	public static void main(String[] args) throws ParseException
	{

		DataManager dm = (DataManager) ContextHolder.getInstance().getApplicationContext().getBean("dataManager");
		String sysPath = System.getenv("windir");
		String systemPath = sysPath + "\\temp\\us\\";
		String file = systemPath + "newfunds.xls";
		System.out.println(file);
		// dm.deleteDuplicateSecurity();
		dm.addNewSecurityForSpecial(file);

		// Date d = new Date();
		// dm.UpdateSP500Dividend("C:\\WINDOWS\\Temp\\SP500EPSEST.XLS");
		// dm.UpdateSP500EaringAndPE("C:\\WINDOWS\\Temp\\SP500EPSEST.XLS", d);
		// dmi.setSecurityManager((SecurityManager)
		// ContextHolder.getInstance().getApplicationContext().getBean("securityManager"));
		// dmi.UpdateAdjNAV("S&P 500 INDEX")

	}

}
