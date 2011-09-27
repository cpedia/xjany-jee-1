package com.lti.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.ConfidenceCheck;
import com.lti.service.bo.GlobalObject;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioFollowDate;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioPerformance;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.type.Interval;
import com.lti.type.PaginationSupport;
import com.lti.type.finance.HoldingInf;
import com.lti.type.finance.PortfolioInfo;


/**
 * @author Michael Cai
 *
 */
/**
 * @author Administrator
 *
 */
public interface PortfolioManager {
	/**************************************************************/
	/*==basic method==Start                                       */
	/**************************************************************/
	/**
	 * get portfolio by id
	 * @param id
	 * @return
	 */
	Portfolio get(Long id);
	
	long getBenchmarkID(long id);
	
	Portfolio getBasicPortfolio(Long id);
	
	public List<Portfolio> getAllBasciPortfolio();
	
	/**
	 * get portfolio by name
	 * 
	 * @param portfName
	 * @return
	 */
	Portfolio get(String portfName);
	

	/**
	 * add a portfolio to database
	 * and do not make a copy for original portfolio
	 * this method is not suggested to use
	 * @param portf
	 * @return
	 */
	long save(Portfolio portf);

	/**
	 * remove a portfolio by id
	 * and it will also remove it's original portfolio if there is any
	 * @param portfID
	 * @throws Exception 
	 */
	void remove(Long portfID) throws Exception;


	/**
	 * update a portfolio
	 * it's suggested to update the portfolio
	 * update the original portfolio, please use another method
	 * @param portf
	 */
	void update(Portfolio portf);

	/**
	 * if there exist a portfolio with the given name
	 * return true
	 * @param name
	 * @return
	 */
	boolean isExisted(String name);

	/**
	 * get portfolios by Detached Criteria
	 * @param detachedCriteria
	 * @return
	 */
	List<Portfolio> getPortfolios(org.hibernate.criterion.DetachedCriteria detachedCriteria);
	

	
	/**************************************************************/
	/*==basic method==End                                         */
	/**************************************************************/


	
	/**************************************************************/
	/*==original portfolio==Start                                 */
	/**************************************************************/
	/**
	 * get a portfolio's original portfolio
	 * @param portfolioid
	 * @return
	 */
	Portfolio getOriginalPortfolio(long portfolioid);





	
	/**************************************************************/
	/*==List Method==Start                                        */
	/**************************************************************/
	/**
	 * get all portfolios except the original ones
	 * @return
	 */
	List<Portfolio> getPortfolios();


	/**
	 * get portfolios by name
	 * it did not contain the model one and original one
	 * @param portfName
	 * @return
	 */
	List<Portfolio> getPortfoliosByName(String portfName);

	
	

	/**************************************************************/
	/*==List Method==End                                          */
	/**************************************************************/	
	/**************************************************************/
	/*==Transaction==Start                                        */
	/**************************************************************/
	
	/**
	 * get transaction by id
	 * it's nonsense
	 * @param id
	 * @return
	 */
	Transaction getTransaction(long id);

	/**
	 * add a transaction
	 * @param transaction
	 * @return
	 */
	long addTransaction(Transaction transaction);

	/**
	 * clear all the transcations of the portfolio with the strategy
	 * @param portfolioID
	 * @param strategyID
	 * @param date
	 */
	void clearTransaction(long portfolioID, long strategyID, Date date);
	/**
	 * @param portfID
	 * @param transactionType
	 * @throws Exception
	 * added by CCD on 2010-01-25
	 */
	void clearTransaction(long portfID, int transactionType, Date curDate) throws Exception;
	/**
	 * clear all the transcations of the portfolio
	 * @param portfID
	 * @throws Exception 
	 */
	void clearTransaction(long portfID) throws Exception;
	/**
	 * delete the transactions with ID equals transactionID,actually here will always delete schedule transaction
	 * @param transactionID
	 * @throws Exception
	 */
	public void deleteTransaction(long transactionID) throws Exception;
	/**
	 * update the transaction
	 * @param transaction
	 */
	void updateTransaction(Transaction transaction);

	/**
	 * get transactions with detached criteria by page
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport findTransactions(org.hibernate.criterion.DetachedCriteria detachedCriteria,int pageSize, int startIndex);

	/**
	 * get transactions with detached criteria
	 * @param detachedCriteria
	 * @return
	 */
	List<Transaction> getTransactions(org.hibernate.criterion.DetachedCriteria detachedCriteria);
	/**
	 * get the portfolio's all real transactions
	 * @param portfolioID
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	List<Transaction> getTransactions(long portfolioID);
	/**
	 * get the portfolio's all transaction
	 * @param portfolioID
	 * @return
	 */
	List<Transaction> getAllTransactions(long portfolioID);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param transactionTypes
	 * @return
	 * added by CCD on 2010-01-27
	 */
	public List<Transaction> getTransactions(long portfolioID, Integer[] transactionTypes);
	/**
	 * 
	 * @param portfolioID
	 * @param securityID
	 * @param transactionTypes
	 * @return
	 */
	public List<Transaction> getTransactions(long portfolioID, long securityID, Integer[] transactionTypes);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @return
	 * added by CCD on 2010-01-27
	 */
	public List<Transaction> getNonScheduleTransactions(long portfolioID, long securityID, Date lastDate);
	/**
	 * @param portfolioID
	 * @return
	 * added by CCD on 2011-04-02
	 */
	List<Transaction> getRealAndReinvestTransactions(Long portfolioID);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @return
	 */
	public List<Transaction> getCashFlowTransaction(long portfolioID);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param transactionType
	 * @return return the portfolio's transactions with type transactionType
	 * added by CCD on 2010-01-25
	 */
	List<Transaction> getTransactions(long portfolioID, int transactionType);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param transactionType
	 * @throws Exception
	 */
	public void deleteTransactions(long portfolioID, int transactionType) throws Exception;
	/**
	 * get the portfolio's transactions from the starting date to the specific end date
	 * @param portfolioID
	 * @param endDate
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	List<Transaction> getTransactionsInPeriod(long portfolioID, Date endDate);
	
	/**
	 * get the portfolio's transactions by page
	 * @param portfolioid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	PaginationSupport getTransactions(long portfolioid, int pageSize, int startIndex);

	/**
	 * get the portfolio's transactions between the interval 
	 * @param portfolioid
	 * @param interval
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	List<Transaction> getTransactions(long portfolioid, Interval interval);
	
	/**
	 * @author cherry 2009-03-31
	 * get transactions after the special date for the email notification or else
	 * @param portfolioid
	 * @param date
	 * @return
	 */
	List<Transaction> getTransactionsAfter(long portfolioid, Date date);
	/**
	 * get transactions of the portfolio with the strategy
	 * @param portfolioid
	 * @param securityid
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	List<Transaction> getTransactions(long portfolioid, long securityid);
	/**
	 * get transactions by portfolioname and securityid
	 * @param portfolioname if not null, it will get the portfolios with name like %portfolioname%
	 * @param securityid
	 * @param userid if not null, it only get the user's portfolios and public portfolios
	 * @param startDate
	 * @param endDate
	 * @param size if it is zero, it will return all items.
	 * @return
	 */
	List<Transaction> getTransactions(String portfolioname,long securityid,long userid,Date startDate,Date endDate,int size, String orderBy);
	/**
	 * get transactions of the portfolio with the strategy by page
	 * @param portfolioid
	 * @param securityid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	PaginationSupport getTransactions(long portfolioid, long securityid, int pageSize,int startIndex);

	/**
	 * get transactions of the portfolio with the strategy by page
	 * @param portfolioid
	 * @param securityid
	 * @param interval the date scope
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	List<Transaction> getTransactions(long portfolioid, long securityid,	Interval interval);

	/**
	 * get transactions of the portfolio with the strategy by page
	 * @param portfolioid
	 * @param interval the date scope
	 * @param securityid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	PaginationSupport getTransactions(long portfolioid, Interval interval,long securityid, int pageSize, int startIndex);

	/**
	 * get the portfolio's transactions by the given date
	 * @param portfolioid
	 * @param date
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	List<Transaction> getTransactions(long portfolioid, Date date);
	/**
	 * get the portfolio's transactions with the given date by page
	 * @param portfolioid
	 * @param interval
	 * @param pageSize
	 * @param startIndex
	 * @return
	 * last modified by CCD on 2010-01-25 by adding a new Restrictions
	 */
	PaginationSupport getTransactions(long portfolioid, Interval interval, int pageSize,	int startIndex);
	/**************************************************************/
	/*==Transaction==End                                          */
	/**************************************************************/	
	/**************************************************************/
	/*==DailyData==Start                                          */
	/**************************************************************/

	/**
	 * add a daily data of the portfolio
	 * @param portfolioDailyData
	 * @return
	 */
	long saveDailyData(PortfolioDailyData portfolioDailyData);

	/**
	 * get the portfolio's daily data by the given date
	 * @param portfolioid
	 * @param date
	 * @return
	 */
	PortfolioDailyData getDailydata(long portfolioid, Date date);

	PortfolioDailyData getDailyDataIngoreError(long portfolioid, Date date);
	

	
	PortfolioDailyData getLatestDailyData(long portfolioid);
	/**
	 * remove the portfolio's daily data of the given date
	 * @param portfolioid
	 * @param date
	 */
	void removeDailyData(long portfolioid, Date date);

	/**
	 * clear the portfolio's all daily data
	 * @param portfID
	 * @throws Exception 
	 */
	void clearDailyData(long portfID) throws Exception;

	/**
	 * get daily datum by detached criteria
	 * @param detachedCriteria
	 * @return
	 */
	List<PortfolioDailyData> getDailydatas(org.hibernate.criterion.DetachedCriteria detachedCriteria);

	/**
	 * get daily datum with detached criteria by page 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport findDailyDatas(org.hibernate.criterion.DetachedCriteria detachedCriteria,	int pageSize, int startIndex);

	/**
	 * get the portfolio's all daily datum
	 * @param portfolioid
	 * @return
	 */
	List<PortfolioDailyData> getDailydatas(long portfolioid);
	
	List<PortfolioDailyData> getDailydatasByPeriod(long portfolioid, Date startDate, Date endDate);
	
	
	/**
	 * get daily data of end of the month(or closest to it) according to the date
	 * @author cherry 2009-2-23
	 * @param portfolioid
	 * @param date
	 * @return the PortfolioDailyData which is closest to the end of the month of the date
	 * */
	public PortfolioDailyData getSnapShotDailyData(long portfolioid, Date date);
	
	/**
	 * get the portfolio's daily data from the starting date to the specific end date
	 * @param portfolioid
	 * @param date
	 * @return
	 */
	List<PortfolioDailyData> getDailydataInPeirod(long portfolioid, Date endDate);

	/**
	 * get the portfolio's all daily datum by page
	 * @param portfolioid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getDailydatas(long portfolioid, int pageSize, int startIndex);

	/**
	 * get the portfolio's all daily datum with the interval 
	 * @param portfolioid
	 * @param interval
	 * @return
	 */
	List<PortfolioDailyData> getDailydatas(long portfolioid, Interval interval);

	/**
	 * get the portfolio's all daily datum with the interval by page
	 * @param portfolioid
	 * @param interval
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getDailydatas(long portfolioid, Interval interval, int pageSize,int startIndex);	
	/**************************************************************/
	/*==DailyData==End                                            */
	/**************************************************************/
	/**************************************************************/
	/*==Log==Start                                                */
	/**************************************************************/

	/**
	 * add a log
	 * @param log
	 * @return
	 */
	long addLog(Log log,int type);

	/**
	 * remove a log
	 * but it's nonsense, just called by other method
	 * you can call the clearLog methods
	 * @param id
	 */
	void removeLog(long id);

	/**
	 * clear the portfolio's all logs
	 * @param portfolioid
	 * @throws Exception 
	 */
	void clearLog(long portfolioid) throws Exception;

	/**
	 * clear the portfolio's logs with the specify strategy
	 * @param portfolioid
	 * @param strategyid
	 */
	void clearLog(long portfolioid, long strategyid);

	/**
	 * clear the portfolio's logs with the specify date
	 * @param portfolioid
	 * @param logDate
	 */
	void clearLog(long portfolioid, Date logDate);

	/**
	 * clear the logs
	 * @param portfolioid the given portfolio's id
	 * @param strategyid the given strategy's id
	 * @param logDate the give date
	 */
	void clearLog(long portfolioid, long strategyid, Date logDate);

	/**
	 * get logs with detached criteria by page
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport findLogs(org.hibernate.criterion.DetachedCriteria detachedCriteria, int pageSize,	int startIndex);

	/**
	 * get logs with detached criteria
	 * @param detachedCriteria
	 * @return
	 */
	List<Log> getLogs(org.hibernate.criterion.DetachedCriteria detachedCriteria);

	/**
	 * get portfolio's logs with the specify strategy
	 * @param portfolioID
	 * @param strategyID
	 * @return
	 */
	List<Log> getLogs(long portfolioID, long strategyID);

	/**
	 * get portfolio's logs from the starting date to the specific date
	 * @param portfolioID
	 * @param endDate
	 * @return
	 */
	List<Log> getLogsInPeriod(long portfolioID, Date endDate);
	
	/**
	 * get portfolio's logs with specific strategy from the starting date to the specific date
	 * @param portfolioID
	 * @param endDate
	 * @param strategyID
	 * @return
	 */
	List<Log> getLogsInPeriod(long portfolioID, long strategyID, Date endDate);
	
	/**
	 * get portfolio's logs with the specify strategy by page
	 * @param portfolioID
	 * @param strategyID
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getLogs(long portfolioID, long strategyID, int pageSize,	int startIndex);

	/**
	 * get portfolio's logs of the given date
	 * @param portfolioID
	 * @param logDate
	 * @return
	 */
	List<Log> getLogs(long portfolioID, Date logDate);
	List<Log> getTailLogs(long portfolioID, int count);

	/**
	 * get portfolio's logs with the specify strategy at the given date 
	 * @param portfolioID
	 * @param logDate
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getLogs(long portfolioID, Date logDate, int pageSize,int startIndex);

	List<Log> getLogs(long portfolioID);

	PaginationSupport getLogs(long portfolioID, int pageSize, int startIndex);

	long addUserLog(Log log);

	long addSystemLog(Log log);

	Log getLog(long portfolioID, long strategyID, Date logDate, int type);

	Log getUserLog(long portfolioID, long strategyID, Date logDate);

	Log getSystemLog(long portfolioID, long strategyID, Date logDate);

	
	
	


	void updatePortfolioState(PortfolioState ps);

	PortfolioState getPortfolioState(Long id);

	void clearLogBeforeDate(Long id, Date pdate);

	void clearTransactionBeforeDate(Long id, Date pdate);

	PaginationSupport getSystemLogs(long portfolioID, int pageSize,
			int startIndex);

	PaginationSupport getStrategyLogs(long portfolioID, int pageSize,
			int startIndex);
	

	
	Date getEarliestDate(long portfolioid);
	Date getLatestDate(long portfolioid);

	void saveDailyDatas(List<PortfolioDailyData> pdds) throws Exception;
	
	
	List<Portfolio> getTopModelPortfolio(Object[] classid, int year, int sort, int size);

	List<Portfolio> getTopModelPortfolio(long strategyid, int year, int sort, int size);
	
	
	public void clearPortfolioMPT(long generatedPortfolioID) throws Exception;
	
	

	

	void saveLogs(List<Log> logs);

	void saveTransactions(List<Transaction> transactions);

	
	List<PortfolioMPT> getEveryYearsMPT(long portfolioID);



	List<PortfolioMPT> getPortfolioMPTsByYearArray(long portfolioID, List<Integer> years);
	
	PortfolioMPT getPortfolioMPT(long portfolioID, int year);
	
	List<PortfolioMPT> getPortfolioMPTsByYear(List<Long> portfolioIDs, int year);
	
	public Date getTransactionEarliestDate(long portfolioid);
	
	public Date getTransactionLatestDate(long portfolioid);
	public Date getTransactionLatestDateByDate(long portfolioid,Date currentDate);
	public Transaction getLatestTransaction(long portfolioid);
	public Transaction getLatestTransaction(long portfolioid,Date CurrentDate);
	/**
	 * get the latest transaction date before the specific date
	 * @param portfolioID
	 * @param date
	 * @return
	 */
	public Date getTransactionLatestDate(long portfolioID, Date date);
	
	/**
	 * 获得这个portfolio的最后一条真实transaction的日期
	 * @param portfolioID
	 * @param date
	 * @return
	 */
	Date getRealTransactionLatestDate(long portfolioID, Date date);
	
	List<Transaction> getCashFlowTransaction(Long id,Date startDate, Date endDate);
	
	public void saveOrUpdateAll(Collection pdds);

	public void saveAll(Collection pdds);
	
	List<PortfolioMPT> findByCriteria(org.hibernate.criterion.DetachedCriteria detachedCriteria);

	void deleteByHQL(String string);

	List findByHQL(String string);
	
	public void saveMPTs(List<PortfolioMPT> pmpts);
	
	public void saveConfidenceCheck(ConfidenceCheck cc);
	public List<ConfidenceCheck> checkConfidence(Long portfolioID,Long strategyID,boolean strategy);

	void updateSymbol(Portfolio portf);

	public List<Portfolio> getCurrentPortfolios();

	public List<PortfolioState> getPortfolioStates();


	List<PortfolioMPT> getPortfolioMPTs(int year, int sort);

	List findBySQL(String string)throws Exception;
	


	/**
	 * get transactions from database
	 * @param portfolioid
	 * @param date if there is not transaction at date 'date', then return all transactions at date 'date - 1'
	 * @return
	 */
	public List<Transaction> getLatestTransactions(long portfolioid, Date date);

	
	/**
	 * get Portfolios By GroupID and RoleID from Portfolio and GroupRole tables
	 * @param groupid
	 * @param roleid
	 * @return
	 */
    List<Portfolio> getPortfoliosByGroup(long groupid,long roleid);
	/**
	 * get Portfolios By GroupID from Portfolio and GroupRole tables
	 * @param groupid
	 * @return
	 */
	List<Portfolio> getPortfoliosByGroup(long groupid);


	/**
	 * get Portfolios By GroupIDs and RoleIDs from Portfolio and GroupRole tables
	 * @param groupids
	 * @param roleids
	 * @return
	 */
	List<Portfolio> getPortfoliosByGroupRoles(Long[] groupids, Long[] roleids);

	List<Long> createStarPortfolio(String assetclassname, int ranking, int interval, Date enddate);
	/**
	 * @author CCD 2010-03-09
	 * @param portfolioID
	 * @param updateMode
	 */
	public void updatePortfolioMode(Long portfolioID, int updateMode);
	/**
	 * @author CCD 2010-03-09
	 * @param securityID
	 * @param dividend
	 * @param dDate
	 */
	@Deprecated
	public boolean doOneDividendAdjustment(List<Portfolio> portfolioList, Long securityID, List<SecurityDailyData> priceList, double dividend, double price, Date dDate);
	/**
	 * @author CCD 2010-03-09
	 */
	public void checkDividendAdjustment();
	/**
	 * @author CCD
	 * @param portfolioID
	 * @return
	 */
	PortfolioFollowDate getPortfolioFollowDate(Long portfolioID);
	/**
	 * @author CCD
	 * @return
	 */
	List<PortfolioFollowDate> getPortfolioFollowDates();
	/**
	 * @author CCD
	 * @param pfd
	 */
	void saveOrUpdatePortfolioFollowDate(PortfolioFollowDate pfd);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param curDate
	 */
	void changeTransaction(Long portfolioID, Date curDate);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @return
	 */
	public List<Date> getPortfolioFollowDatesByID(Long portfolioID);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @return
	 */
	public String checkPlanName(String portfolioIDs);
	public String returnPortfolio(String portfolioIDs);
	public String returnPortfolioStatistics(String portfolioIDs);

	String getPortfolioLastFollowDate(Long portfolioID);


	List<Transaction> getTransactionsAfter(long portfolioid, int transactiontype, Date date);
	
	List<Double> getPortfolioDailyAmount(long portfolioID);
	
	Double getPortfolioAmountByDate(long portfolioID,String date);

	PortfolioInf getPortfolioInf(long portfolioid,long portfolioHoldingOriginal);
	
	void savePortfolioInf(PortfolioInf portfolioInf);
	
	List<PortfolioInf> getPortfolioInfs(long portfolioHoldingType);

	List<Portfolio> getModelPortfolios(long id);
	
	void updatePortfolioInf(PortfolioInf portfolioInf);

	long updatePortfolioMPT(PortfolioMPT mpt);

	HoldingInf getHolding(long portfolioID, long portfolioHoldingOriginal);
	
	List<CachePortfolioItem> getCachePortfolioItems(long portfolioid);
	CachePortfolioItem getCachePortfolioItem(long groupid,long roleid,long portfolioid);

	/**
	 * 此API的portfolio只包含五个基本属性
	 * @param mainstrategyid　如果不为-１则查询相应strategy下的model portfolio
	 * @param type 如不为-１，则查询所有type的portfolio
	 * @return
	 */
	List<Portfolio> getSimplePortfolios(long mainstrategyid, long type);

	/**
	 * 查询portfolio
	 * @param modelportfolioid　如果不为-１则查询相应strategy下的model portfolio
	 * @param type 如不为-１，则查询所有type的portfolio
	 * @return
	 */
	List<Portfolio> getPortfolios(long mainstrategyid, long type);

	void updateGlobalObject(GlobalObject go);

	GlobalObject getGlobalObject(String key);

	HoldingInf getOriginalHolding(long id);
	
	List<HoldingRecord> getHoldingRecords(Long portfolioID);

	void setHoldingRecord(Long securityID, Date dividendDate);

	PaginationSupport getPortfolios(DetachedCriteria detachedCriteria, int pageSize, int startIndex);
	boolean hasHoldingRecord(Long portfolioID);
	
	int getDailyDataCount(Long portfolioID);
	
	List<PortfolioDailyData> getDuplicateDailyDatas(Long portfolioID, Date date);
	
	void deletePortfolioPerformance()throws Exception;
	
	void deletePortfolioPerformanceByPlanID(long planID) throws Exception;
	
	void saveAllPortfolioPerformance(List<PortfolioPerformance> ppList);
	
	void savePortfolioInformation(PortfolioInfo portfolioInfo);

	List<Transaction> getRealTransactions(long portfolioID, long securityID, Date startDate);
	
	void deleteDailyData(Long ID) throws Exception;
	
	void deleteDuplicateDailyDataByPortfolioIDAndID(Long portfolioID, Long ID) throws Exception;

	PortfolioDailyData getSecondeDuplicateStartDailyData(Portfolio p);

	PortfolioDailyData getLatestNewerThanFirstVersionDailyData(PortfolioDailyData pdd);

	PortfolioDailyData getFirstVersionLatestDailyDataBySecondStartDailyData(PortfolioDailyData pdd);

	void deleteDuplicateDailyDataAfterIDBeforeDate(Long portfolioID, Long ID, Date date) throws Exception;

	void removeDailyDatas(List<PortfolioDailyData> pdds);

	void recoverPortfolio(long portfolioID, Date endDate) throws Exception;
	
	Double getDividendAmountByPortfolioIDAndYear(Long portfolioID, int year);
	
	PortfolioDailyData getLastPortfolioDailyDataByPortfolioIDAndYear(Long portfolioID, int year);

	List<Transaction> getTransactionsByPortfolioIDAndTypeAndDate(long portfolioID, int transactionType, Date date);
	/**
	 * 返回用户的所有portfolio
	 * @param userID
	 * @return
	 */
	List<Portfolio> getPortfolioListByUserID(Long userID);

	List<Portfolio> getPortfoliosByUser(long userid, long type);

	void changeUserPortfolioToExpired(Long userID, Date expiredTime);

	void changeUserPlanToExpired(Long userID, Date expiredTime);

	void activateUserPlan(Long userID, List<Long> planIDList);

	void changeUserFundTableToExpired(Long userID, Date expiredTime);

	void clearUserPermission(Long userID);

	void changeUserResourceWhenCanclePaypal(Long userID, Date expiredTime);

	void activateFollowPortfolio(Long userID, List<Portfolio> portfolioList);
	/**
	 * 清除超过时限的expired portfolio记录，如果是owner，则同时清除portfolio
	 */
	void clearExpiredPortfolios(int month);

	void changeUserResourceBeforeDeletePlan(Strategy plan);
	
}

