package com.lti.service;

import java.util.Date;
import java.util.List;

import com.lti.service.bo.HoldingItem;

/**
 * @author cherry 2009-2-14
 * */
public interface PortfolioHoldingManager {
	
	/**
	 * Get a portfolio holding item
	 * @author cherry 2009-2-14
	 * @param ID
	 * @return a PortfolioHolding item relative to the ID
	 * */
	
	/**
	 * Get a portfolio holding item according to the execution date
	 * @author cherry 2009-2-14
	 * @param portfoloID
	 * @param date
	 * @return get the PortfolioHolding relative to the portfolio on that specific date
	 * */
	/**
	 * Get a portfolio holding item according to the execution date
	 * @author CCD 2009-2-14
	 * @param portfoloID
	 * @param date
	 * @return get the Portfolio Holding Item relative to the portfolio on that specific date
	 * */
	List<HoldingItem> getHoldingItems(Long portfolioID, Date date);
	/**
	 * Get a portfolio holding item according to the last valid date
	 * @param portfolioID
	 * @param lastValidDate
	 * @return get a portfolio holding item according to the last valid date
	 */
	
	/**
	 * Get all portfolio holdings related to the portfolio
	 * @author cherry 2009-2-17
	 * @param portfolioID
	 * @return get the portfolio holdings related to the portfolio
	 * */
	
	/**
	 * Get latest portfolio holdings related to the portfolio
	 * @author cherry 2009-2-19
	 * @param portfolioID
	 * @return get the portfolio holding item which is latest to now
	 * */
	
	/**
	 * get all portfolio holdings related to all portfolios
	 * @author cherry 2009-2-20
	 * @return all portfolio holdings related to all portfolios
	 * */
	
	/**
	 * Add  portfolios holding item
	 * @author cherry 2009-2-14
	 * @param portfolioHolding
	 * @return if it fails, it will throw an exception
	 * */
	
	/**
	 * Add portfolio holding items to the database
	 * @author cherry 2009-2-14
	 * @param portfolioHoldings
	 * @return if it fails, it will throw an exception
	 * */
	
	/**
	 * Delete a portfolio holding item
	 * @author cherry 2009-2-14
	 * @param ID
	 * @return if it fails, it will throw an exception
	 * */
	void delete(Long ID) throws Exception;
	
	/**
	 * Delete all the portfolio holding items relative the portfolio
	 * @author cherry 2009-2-14
	 * @param portfolioID
	 * @return if it fails, it will throw an exception
	 * */
	void deletePortfolioHolding(Long portfolioID) throws Exception;
	
	/**
	 * Update a portfolio holding item
	 * @author cherry
	 * @param portfolioHolding
	 * @return if it fails, it will throw an exception
	 * */
	
	public List findByHQL(String hql);

	public List findBySQL(String string)throws Exception;
	
	/*************************the below APIs are added for holding items*******************************/
	/**
	 * Get a holding item
	 * @author CCD 2010-03-09
	 * @param ID
	 * @return a Holding item relative to the ID
	 * */
	HoldingItem getHoldItem(Long ID);
	/**
	 * Get a holding item
	 * @param portfolioID
	 * @param securityID
	 * @param date
	 * @return
	 */
	HoldingItem getOneHoldItemBefore(Long portfolioID, Long securityID, Date date);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param date
	 * @return
	 */
	List<HoldingItem> getLatestHoldingItems(Long portfolioID, Date date);
	/**
	 * Get a portfolio's holding items before the specific date by portfolioID
	 * @author CCD 2010-03-09
	 * @param portfoloID
	 * @param date
	 * @return get the Portfolio's Holding items relative to the portfolio on that specific date
	 * */
	List<HoldingItem> getLatestHoldingItemsBefore(Long portfolioID, Date date);
	/**
	 * Get a portfolio's latest holding item before the specific date by securityID
	 * @author CCD 2010-03-09
	 * @param portfoloID
	 * @param securityID
	 * @param date
	 * @return
	 * */
	HoldingItem getLatestHoldingItemsBeforeBySecurityID(Long portfolioID, Long securityID, Date date);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param securityID
	 * @param date
	 * @return
	 */
	List<HoldingItem> getAllHoldItemAfter(Long portfolioID, Long securityID, Date date);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param date
	 * @return
	 */
	List<Date> getHoldingDateAfter(Long portfolioID, Date date);
	/**
	 * @author CCD
	 * @param portfolioID
	 * @param securityID
	 * @param date
	 * @return
	 */
	HoldingItem getLatestHoldItem(Long portfolioID, Long securityID, Date date);
	/**
	 * Get a portfolio's holding items after the specific date
	 * @author CCD 2010-03-09
	 * @param portfoloID
	 * @param date
	 * @return
	 * */
	List<HoldingItem> getAllHoldingItemsAfter(Long portfolioID, Date date);
	
	/**
	 * Add holding item
	 * @author CCD 2010-03-09
	 * @param holdingItem
	 * @return
	 * */
	void addHoldingItem(HoldingItem holdingItem) throws Exception;
	
	/**
	 * Add holding items to the database
	 * @author CCD 2010-03-09
	 * @param holdingItemList
	 * @return
	 * */
	public void addAllHoldingItems(List<HoldingItem> holdingItemList) throws Exception;
	
	/**
	 * Delete a holding item
	 * @author CCD 2010-03-09
	 * @param ID
	 * @return
	 * */
	void deleteHoldingItem(Long ID) throws Exception;
	
	/**
	 * Delete all the holding items relative to the portfolio
	 * @author CCD 2010-03-09
	 * @param portfolioID
	 * @return
	 * */
	void deleteAllHoldingItems(Long portfolioID) throws Exception;
	
	/**
	 * Update a holding item
	 * @author CCD 2010-03-09
	 * @param holdingItem
	 * @return
	 * */
	void updateHoldingItem(HoldingItem holdingItem) throws Exception;
	/**
	 * get all the holding item after date for portfolioid and securityid
	 * @param portfolioID
	 * @param securityID
	 * @param date
	 * @return
	 */
	List<HoldingItem> getHoldingItemsAfter(Long portfolioID, Long securityID, Date date);
	/**
	 * get the special holding item on date for portfolioid and securityid
	 * @param portfolioID
	 * @param securityID
	 * @param date
	 * @return
	 */
	HoldingItem getHoldingItem(Long portfolioID, Long securityID, Date date);
	
}
