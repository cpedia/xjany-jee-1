package com.lti.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
//import com.lti.service.bo.FundAlert;
import com.lti.service.bo.CompanyFund;
import com.lti.service.bo.SecurityMPTIncData;
import com.lti.service.bo.SecurityType;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.ShillerSP500;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VAFund;
import com.lti.type.PaginationSupport;
import com.lti.util.DailyUpdateListener;
import com.lti.service.bo.RelativeStrength;



public interface SecurityManager {
	
	/**
	 * add a security
	 * @param security
	 * @return
	 * @throws SecurityException 
	 */
	public Long add(Security security) throws SecurityException;

	/**
	 * clear the security's all daily data
	 * @param portfID
	 */
	public void clearDailyData(Long securityid);
	public List<Security> getSecuritiesLessByType(int type);
	public boolean updateAr(String symbol);
	public void updateAllAr();
	public boolean updateMpt(String symbol);
	public void updateAllMpt();
	public int getProcess();
	public int getSumProcess();
	public void setIsRun(boolean b);
	public List<SecurityDailyData> searchSecurityDailyData(String symbol, Date startDate, Date endDate);
	public List<Object[]> getQuality(String symbol);
	/**
	 * delete a security by id
	 * @param id
	 */
	public void delete(Long id);

	public void delete(String string);
	/**
	 * delete a security and its security daily data and mpt
	 * @param id
	 */
	public void deleteSecurityCascade(Long securityID);
	/**
	 * 
	 * @param securityID
	 */
	public void deleteSecurityDataCascade(Long securityID);
	public void deleteSecurityRanking(Long securityID);
	/**
	 * delete the daily data by id
	 * @param id
	 */
	public void delteDailyData(Long id);
	
	/**
	 * delete the securityMPT data
	 * @param id
	 */
	public void deleteSecurityMPT(Long id);
	public void deleteSecurityMPTAfterYear(Long securityID, int year);
	public List<SecurityDailyData> findByCriteria(DetachedCriteria detachedCriteria, int  pageSize, int startIndex);

	/**
	 * get daily datum with detached criteria by page 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport findDailyDatas(DetachedCriteria detachedCriteria,	int pageSize, int startIndex);
	/**
	 * basic method for getting securities by page
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport findSecurities(DetachedCriteria detachedCriteria,	int pageSize, int startIndex);

	/**************************************************************/
	/*==Basic Method== Start                                      */
	/**************************************************************/
	/**
	 * get a security by id
	 * @param securityid
	 * @return
	 */
	public Security get(Long securityid);


	/**
	 * get a security by name
	 * @param name
	 * @return
	 */
	public Security get(String name);
	
	public double getAdjPrice(Long id) throws SecurityException, NoPriceException;

	public double getAdjPrice(Long id, Date date) throws SecurityException, NoPriceException;
	
	
	/**
	 * get a security by symbol
	 * @param symbol
	 * @return
	 */
	public Security getBySymbol(String symbol);
	
	public List<Security> getCEF(long assetClassID);

	public List<SecurityDailyData> getCloseList(Long securityid,Date startDate);

	/**
	 * get the security's daily data by the given date (only the date, won't retrieve last valid date)
	 * @param securityid
	 * @param date
	 * @return
	 */
	public SecurityDailyData getDailydata(Long securityid, Date date,boolean flag);

	public List<SecurityDailyData> getDailyData(Long id, Object[] Dates);
	
	/**
	 * get daily data last date
	 * @param securityid
	 * @return
	 */
	public Date getDailyDataLastDate(Long securityid);

	/**
	 * get daily datum by detached criteria
	 * @param detachedCriteria
	 * @return
	 */
	public List<SecurityDailyData> getDailydatas(DetachedCriteria detachedCriteria);
	/**
	 * @author CCD
	 * @param securityID
	 * @return
	 */
	public int getDailyDataCount(Long securityID);
	/**
	 * get the security's all daily datum
	 * @param securityid
	 * @return
	 */
	public List<SecurityDailyData> getDailydatas(Long securityid,boolean flag);
	/**
	 * get the daily data of securityID order by date asc
	 * @param securityID
	 * @return
	 */
	public List<SecurityDailyData> getDailydatas(Long securityID);
	/**
	 * get the security's all daily datum by page
	 * @param securityid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getDailydatas(Long securityid, int pageSize, int startIndex);

	/**************************************************************/
	/*==Daily Data== End                                          */
	/**************************************************************/

	public List<SecurityDailyData> getDailyDatas(Long id, Date startDate, Date endDate);
	public PaginationSupport getDailyDatas(Long securityid, Date startDate, Date endDate,int pageSize, int startIndex);

	/**
	 * get the security's daily data by the given date when dividend is not null
	 * ????????????????????????????????? please check!
	 * @param securityid
	 * @param date
	 * @return
	 */
	public SecurityDailyData getDailydataWithDividend(Long securityid, Date date);
	public double getDividend(Long securityid, Date date);
	/**
	 * get dividend last date
	 * @param securityid
	 * @return
	 */
	public Date getDividendLastDate(Long securityid);
	/**
	 * get dividend list
	 * @param securityid
	 * @return list
	 */
	public List<SecurityDailyData> getDividendList(Long securityid);
	/**
	 * get dividend list after dDate
	 * @author CCD
	 */
	public List<SecurityDailyData> getDividendList(Long securityID, Date dDate);
	public Date getEndDate(Long securityid);
	public Date getEndDate(String securityName);
	
	public double getHigh(Long id, Date date) throws NoPriceException;
	
	public double getHighestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException;

	public double getHighestAdjPrice(String name, Date startDate, Date endDate) throws SecurityException;

	public double getHighestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException;

	public double getHighestPrice(Long id, Date startDate, Date endDate) throws SecurityException;

	/**************************************************************/
	/*==Basic Method== End                                        */
	/**************************************************************/
	/**************************************************************/
	/*==Extend Method== Start                                     */
	/**************************************************************/
	/**
	 * get the highest price from the start date to the end date
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SecurityException 
	 * @throws NoPriceException 
	 */
	public double getHighestPrice(String name, Date startDate, Date endDate) throws SecurityException, NoPriceException;
	/**
	 * get the oldest daily data
	 * @param securityid
	 * @return
	 */
	SecurityDailyData getOldestDailydata(Long securityid);
	SecurityDailyData getLatestDailydata(Long securityid);
	
	/**
	 * get latest daily data
	 * @param securityid
	 * @param date
	 * @return
	 */
	public SecurityDailyData getLatestDailydata(Long securityid, Date date);
	
	/**
	 * get latest daily data with nav
	 * @param securityid
	 * @param date
	 * @return
	 */
	public SecurityDailyData getLatestNAVDailydata(Long securityid, Date date);

	public double getLow(Long id, Date date) throws NoPriceException;

	public double getLowestAdjPrice(Long id, Date startDate, Date endDate) throws SecurityException;

	public double getLowestAdjPrice(String name, Date startDate, Date endDate) throws SecurityException;
	
	/*
	 * add by chaos
	 */
	
	public double getLowestNAVPrice(Long id, Date startDate, Date endDate) throws SecurityException;
	
	public double getLowestPrice(Long id, Date startDate, Date endDate) throws SecurityException;
	
	/**
	 * get the lowest price from the start date to the end date
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SecurityException 
	 * @throws NoPriceException 
	 */
	public double getLowestPrice(String name, Date startDate, Date endDate) throws SecurityException, NoPriceException;
	
	public List<Security> getMutualFund(long assetClassID);

	/**
	 * get all mutual funds
	 * @return
	 */
	public List<Security> getMutualFunds();
	
	/**
	 * get mutual funds by page
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getMutualFunds(int pageSize, int startIndex);


	
	public Date getNAVLastDate(Long securityid);
	
	/**
	 * get nav list
	 * @param securityid
	 * @return list
	 */
	public List<SecurityDailyData> getNAVList(Long securityid);
	
	public List<SecurityDailyData> getNAVList(Long securityid,Date startDate);

	public List<SecurityDailyData> getNAVList(Long securityid, Date startDate, Date endDate);

	public double getNAVPrice(Long id, Date date) throws SecurityException, NoPriceException;
	
	public double getAdjNAVPrice(Long id, Date date) throws SecurityException, NoPriceException;
	/**
	 * get the security's closed price , today
	 * @param id
	 * @return
	 * @throws SecurityException 
	 * @throws NoPriceException 
	 */
	public double getPrice(Long id) throws NoPriceException;

	/**
	 * get the security's closed price of the given date 
	 * @param id
	 * @param date
	 * @return
	 * @throws NoPriceException 
	 */
	public double getPrice(Long id, Date date) throws NoPriceException;

	/**
	 * get all securities
	 * @return
	 */
	public List<Security> getSecurities();

	/**
	 * basic method for get securities
	 * @param detachedCriteria
	 * @return
	 */
	public List<Security> getSecurities(DetachedCriteria detachedCriteria);

	/**
	 * get all securities by page
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getSecurities(int pageSize, int startIndex);
	/**
	 * get all securities before enddate
	 * @param endDate
	 * @return
	 */
	public List<Security> getSecuritiesBeforeEndDate(Date endDate);
	/**
	 * get all securities on enddate
	 * @param endDate
	 * @return
	 */
	public List<Security> getSecuritiesByEndDate(Date endDate);
	
	/**
	 * get securities without portfolios by page
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getPureSecurities(int pageSize, int startIndex);

	/**
	 * get securities with the specify names
	 * @param nameArray
	 * @return
	 */
	public List<Security> getSecurities(String[] nameArray);
	
	/**
	 * get securities by asset class
	 * @param name
	 * @return
	 */
	public List<Security> getSecuritiesByAsset(Long assetID);
	
	/**
	 * 根据assetclassID返回enddate在endDate之前的fund(不包括stock)
	 * @param assetID
	 * @param endDate
	 * @return
	 */
	List<Security> getSecuritiesByAssetIDAndEndDate(Long assetID, Date endDate);

	/**************************************************************/
	/*==Extend Method== End                                       */
	/**************************************************************/	
	/**************************************************************/
	/*==List Method== Start                                       */
	/**************************************************************/
	/**
	 * get securities with class
	 * @param classid
	 * @return
	 */
	public List<Security> getSecuritiesByClass(Long classid);
	/**
	 * 根据assetclassID及其子类型返回enddate在endDate之前的fund(不包括stock)
	 * @param classID
	 * @param endDate
	 * @return
	 */
	List<Security> getFundsByClassIDAndEndDate(Long classID, Date endDate);

	/**
	 * get securities with class by page
	 * @param classid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getSecuritiesByClass(Long classid, int pageSize,	int startIndex);

	/**
	 * query securities with key word
	 * @param key
	 * @return
	 */
	public List<Security> getSecuritiesByName(String key);

	/**
	 * query securities with key word by page
	 * @param key
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getSecuritiesByName(String key, int pageSize,	int startIndex);

	/**
	 * get securities with security type
	 * @param type
	 * @return
	 */
	public List<Security> getSecuritiesByType(int type);

	/**
	 * get securities with security type by page
	 * @param type
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getSecuritiesByType(int type, int pageSize, int startIndex);
	
	/**
	 * get securities with security type and asset class
	 * @param classid
	 * @param type
	 * @return
	 */
	public List<Security> getSecuritiesByTypeAndClass(Long classid, int type);
	
	/**
	 * get securities with security type and asset class by page
	 * @param classid
	 * @param type
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public PaginationSupport getSecuritiesByTypeAndClass(Long classid, int type,int pageSize, int startIndex);

	/**
	 * get SecurityDailyData by its id, not security's id
	 * @param id
	 * @return
	 */
	public SecurityDailyData getSecurityDailyData(long securitydailydataid);

	public Double getSplit(Long id, Date date) throws NoPriceException;

	public Date getStartDate(Long securityid);
	public Date getStartDate(String securityName);

	public Date getValidEndDate(String[] ses);

	public Date getValidStartDate(String[] ses);

	/**
	 * get the security's all daily datum
	 * @param securityid
	 * @return
	 */
	public byte[] getZipDailydatas(Long securityid);


	public void removeAll(List<SecurityDailyData> sdds);
	
	/**
	 * remove the security's daily data of the given date
	 * @param securityid
	 * @param date
	 */
	public void removeDailyData(Long securityid, Date date);
	
	/**
	 * remove SecurityDailyData by its id, not security's id
	 * @param id
	 * @return
	 */
	public void removeSecurityDailyData(long id);
	public void saveAll(List<SecurityDailyData> sdds1);

	/**
	 * add a daily data of the security
	 * @param securityDailyData
	 * @return
	 */
	public Long saveDailyData(SecurityDailyData securityDailyData);

	public void saveOrUpdateAll(List<SecurityDailyData> sdds);
	
	public void saveOrUpdateAllSecurityMPT(List<SecurityMPT> smpts);
	
	public void saveOrUpdateAllSecurityMPTIncDate(List<SecurityMPTIncData> mpts);
	
	public void saveOrUpdateAllSecurity(List<Security> securityList);
	/**
	 * update a security
	 * @param security
	 */
	public void update(Security security);

	/**************************************************************/
	/*==List Method== End                                         */
	/**************************************************************/
	/**************************************************************/
	/*==Daily Data== Start                                        */
	/**************************************************************/
	public void updateDailyData(SecurityDailyData securityDailyData);
	
	public List<SecurityMPT> getSecurityByMPT(DetachedCriteria detachedCriteria);

	public List findByHQL(String string);
	
	public List findBySQL(String sql)throws Exception;
	
	public void getAllSecurityMPT(Date logDate,boolean update);
	
	public void getOneSecurityMPT(long id,List<SecurityDailyData> sdds,List<SecurityDailyData> benchs,boolean update);
	
	//public void updateLastYearSecurityMPT(long id,List<SecurityDailyData> sdds,List<SecurityDailyData> benchs);
	
	public SecurityMPT getOneYearMPT(SecurityMPT smpt,List<Double> A_Returns,List<Double> B_Returns,List<Double> A_Amounts,Date startDate,Date currentDate,int interval,Security se);
	
	public SecurityMPT getSecurityMPT(long securityID, long year);
	
	public List<SecurityMPT> getSecurityMPTByYears(long securityID, Long[] years);
	
	public List<SecurityMPT> getSecurityMPTBySecurityIDs(List<Long> securityIDs, Long year);
	
	public List<SecurityType> getSecurityTypes();
	
	public SecurityType getSecurityTypeByID(int id);

	public List<SecurityMPT> getSecurityMPTS(long securityID);
	
	public double getAnnualSecurityYeild(long securityID, Date startDate, Date endDate) throws Exception;
	
	//Only for GSPC (get from indicator)
	public double getAnnualGSPCYeild(Date startDate, Date endDate) throws Exception;
	
	public double getAnnualPE(String symbol, Date endDate) throws NoPriceException;
	
	public boolean canGetPrice(String securityName,Date date);
	public boolean canGetPrice(Long securityID,Date date);
	
	/***************************For Fund Alert************************************************************************
	public List<FundAlert> getFundAlertBySecurityID(long securityID);
	public FundAlert getFundAlertBySecurityID(long securityID, Date date);
	public List<FundAlert> getAllFundAlert();
	public void saveOrUpdateFundAlert(List<FundAlert> fundAlertList);
	public void deleteFundAlert(long securityID);
	*/
	void saveOrUpdate(Security security);
	
	/*Relative Strength*/
	void removeRS(Long id);

	public RelativeStrength getRS(Long id);
	
	public RelativeStrength getRS(String symbol,Date date);
	
	void updateRS(RelativeStrength rs);
	
	void saveRS(RelativeStrength rs);
	
	public void getAllStockRS(Date logDate,boolean update);
	
	
	/**
	 * @author cherry 2009-5-14
	 * get the strategies quoting the specific security
	 * @param symbol
	 * @param size 0 for no limits
	 * @return
	 */
	public List<Strategy> getQuotedStrategies(String symbol, int size);
	
	/**
	 * @author cherry 2009-5-14
	 * get the top strategies quoting the specific security
	 * @param symbol
	 * @param size
	 * @param order
	 * @param year
	 * @return
	 */
	public List<Strategy> getQuotedStrategiesByOrder(String symbol, int size, int order, int year);
	
	public void getStockRS(Security se,Date logDate,boolean update);
	
	public Double getRSGrade(String symbol, int Type, Date date);
	
	public void getHistoricalRS(Date date,Date logDate,boolean update);
	
	public double getOpenPrice(Long id, Date date) throws NoPriceException;
	
	public List<String> getStockBySector(String sector);
	
	public List<String> getStockByIndustry(String industry);

	public List<String> getStockBySector(String[] sectors);
	
	public List<String> getStockByIndustry(String[] industries);
	
	public List<String> getIndustryBySector(String[]sector);
	
	public List<String> getIndustryBySector(String sector);
	
	public String getIndustry(String symbol);


	public List<Security> getFundsByClass(Long singleAssetClassID);

	
	public List<String> getAllStocks();
	
	public void addListener(DailyUpdateListener dailyUpdateListener);
	
	public void removeListener(DailyUpdateListener dailyUpdateListener);

	/**
	 * public Strategies, strategy role: role_strategy_read
	 * @param symbol
	 * @param userid
	 * @param size
	 * @param order
	 * @param year
	 * @return
	 */
	List<Strategy> getQuotedStrategiesByOrder(String symbol, Long userid, int size, int order, int year);
    /**
     * public Strategies, strategy role: role_strategy_read
     * @param symbol
     * @param userID
     * @param size
     * @return
     */
	List<Strategy> getQuotedStrategies(String symbol, Long userID, int size);
	
	public void updateSecurityEndDate(List<SecurityDailyData> sdds);
	
	public void updateSecurityNAVLastDate(List<SecurityDailyData> sdds);
	
	public void setSecurityMPTIncrementalData();
	
	public void initialMPTIncrementalData(Security security, List<SecurityDailyData> securityList, List<SecurityDailyData> benchList, List<SecurityDailyData> cashList);
	
	public List<SecurityMPTIncData> getSecurityMPTIncData(Long securityID);
	
	public void updateAllSecurityMPT(Date logDate);
	
	public void removeAllShillerSP500() throws Exception ;
		
	
	public void saveAllShillerSP500(List<ShillerSP500> list);
	
	public Double getShillerSP500CAPE10(Date date) throws NoPriceException;
	
	public ShillerSP500 getShillerSP500Data (Date date) throws NoPriceException;
	
	public void getOneSecurityMPT(Long securityID);
	
	public List<SecurityMPT> getOneSecurityINCMPT(Long securityID);
	
	public List<SecurityMPT> calculateOneSecurityMPT(long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs,boolean update);

	public void updateMPTLastDate(Security security) throws Exception;
	
	public void updateNAVLastDate(Security security) throws Exception;

	public void updateDividendLastDate(Security security) throws Exception;
	
	public void updateEndDateAndPriceLastDate(Security security) throws Exception;
	
	public void updateNewDividendDate(Security security) throws Exception;
	
	public void updateStartDate(Security security) throws Exception;

	void updateNAVStartDate(Security security) throws Exception;

	Date getNAVStartDate(Long securityID);

	List<Security> getSecuritiesByNameConsiderLength(String key, int considerLength);
	
	void saveAllCompanyFund(List<CompanyFund> companyFundList);

	List<CompanyFund> getCompanyFundListByCompany(String company);
	
	void saveOrUpdateAllCompanyFund(List<CompanyFund> companyFundList);
	
	CompanyFund getCompanyFundByTicker(String ticker);
	
	List<CompanyFund> getCompanyFundListByCompanyAndCategory(String company, String category);
	
	boolean hasFundsForCompany(String company);
	
	void saveCompanyFund(CompanyFund companyFund);
	
	List<CompanyFund> getCompanyFundList();
	
	/**
	 * 返回company列表
	 * @return
	 */
	List<String> getCompanyNameList();
	/**
	 * 返回company所对应的所有category的列表
	 * @param companyName
	 * @return
	 */
	List<String> getAssetNameListByCompanyName(String companyName);
	/**
	 * 返回相应的company和category1的列表
	 * @param company
	 * @param category1
	 * @param category2
	 * @param category3
	 * @return
	 */
	List<CompanyFund> getCompanyFundListByCompanyAndCategorys(String company, String category1, String category2, String category3);
	
	VAFund getVAFundByTicker(String ticker);
	
	VAFund getVAFundByBarronName(String barronName);
	
	void addVAFund(VAFund vaFund);
	
	void addAllVAFunds(List<VAFund> vaFundList);
	
	void saveOrUpdateVAFunds(List<VAFund> vaFundList);
	
	List<VAFund> getVAFundList();
	//只计算某些特定年份的数据
	void calculateSecurityMPTAfterYear(Long id, List<SecurityDailyData> sdds, List<SecurityDailyData> benchs, List<SecurityDailyData> riskFreeList);
}
