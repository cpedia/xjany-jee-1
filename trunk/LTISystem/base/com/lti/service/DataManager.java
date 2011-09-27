package com.lti.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.lti.service.bo.SecurityDailyData;
import com.lti.util.DailyUpdateListener;

public interface DataManager {
	/**
	 * @param filename
	 * @throws Exception
	 */
	public void UpdateData(String filename) throws Exception;



	public void updateEcom(String name,String filename)throws Exception;
	
	public void updateEcom2(String name,String filename)throws Exception;

	/**
	 * update security dailydata
	 * @param filename
	 * @param id
	 * @throws Exception
	 */
	public void UpdateDailyData( long id, String filename, boolean flush, Date logDate) throws Exception;

	/**
	 * update security dailydata
	 * @param filename
	 * @param name
	 * @throws Exception
	 */
	public void UpdateDailyData(String filename, String name, boolean flush, Date logDate) throws Exception;
	/**
	 * update adjust close
	 * @param symbol
	 * @param startDate
	 * @return
	 * @throws Exception 
	 */
	boolean updateAdjustCloseFromStartDate(String symbol, Date startDate, List<SecurityDailyData> riskFreeList) throws Exception;
	/**
	 * update adjust NAV
	 * @param symbol
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	boolean updateAdjustNAVFromStartDate(String symbol, Date startDate) throws Exception;
	public void UpdateDailyDataAndAdjust(String filename, String name,boolean flash,Date lastDate,boolean fromStart,Date logDate) throws Exception;

	/**
	 * update security dividends
	 * @param filename
	 * @param name
	 * @throws Exception
	 */
	public Date UpdateDividends(String filename, String name, boolean flush) throws Exception;



	public void UpdateNAV(String currentFile, String securityName) throws Exception;
	
	public void UpdateNAV2(String currentFile, String securityName) throws Exception;



	public void UpdateAdjNAV(String securityName) throws Exception;
	
	public void UpdateAdjClose(String securityName, Date date, SecurityDailyData sd1, SecurityDailyData sd2) throws Exception;
	
	public void UpdateAdjNAV(String securityName, Date date) throws Exception;
	
	/**
	 * update S&P 500 earning and PE according to the .xls file
	 * @param fileName
	 * @throws ParseException
	 */ 
	public void UpdateSP500EaringAndPE(String fileName,Date logDate) throws ParseException;
	/**
	 * update Shiller S&P 500 earning and PE according to the .xls file
	 * @param fileName
	 * @throws ParseException
	 */ 
	public void UpdateShillerSP500Data(String fileName,Date logDate) throws ParseException;
	
	public void setAllForFlush();
	
	/*update the S&P 500 dividend according to the excel file*/
	/*change the S&P 500 dividend from a security attribute to an indicator*/
	//public void UpdateSP500Dividend(String fileName,Date logDate) throws ParseException;
	
	public void UpdateSP500Dividend(String path)throws ParseException;
	
	public void addListener(DailyUpdateListener dailyUpdateListener);
	
	public void removeListener(DailyUpdateListener dailyUpdateListener);
	
	public int addNewSecurityForSpecial(String fileName);
	
	public int deleteDuplicateSecurity();
}
