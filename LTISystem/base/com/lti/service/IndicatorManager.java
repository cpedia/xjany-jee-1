package com.lti.service;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.Indicator;
import com.lti.service.bo.IndicatorDailyData;
import com.lti.type.PaginationSupport;



public interface IndicatorManager {

	public long addDailyData(com.lti.service.bo.IndicatorDailyData indicatorDailyData);

	public long add(com.lti.service.bo.Indicator indicator);
	
	public void addAll(List<com.lti.service.bo.IndicatorDailyData>  indicatorDailyDataList);

	public PaginationSupport findDailyDataPage(final DetachedCriteria detachedCriteria, final int pageSize,final int startIndex) ;
	
	public PaginationSupport findPage(final DetachedCriteria detachedCriteria);
	
	public PaginationSupport findPage(final DetachedCriteria detachedCriteria, final int startIndex);
	
	public PaginationSupport findPage(final DetachedCriteria detachedCriteria, final int pageSize, final int startIndex);
	
	public com.lti.service.bo.Indicator get(long id);
	
	public com.lti.service.bo.Indicator get(String indicatorName);
	
	public java.util.List<com.lti.service.bo.IndicatorDailyData> getDailydata(long id);
	
	public java.util.List<com.lti.service.bo.IndicatorDailyData> getDailydata(long id, com.lti.type.Interval interval);

	public PaginationSupport getDailydata(long id, com.lti.type.Interval interval, final int pageSize,final int startIndex);

	public PaginationSupport getDailydata(long id, final int pageSize,final int startIndex);
	
	public com.lti.service.bo.IndicatorDailyData getDailydata(long id, java.util.Date date);

	public java.util.List<com.lti.service.bo.Indicator> getList();

	public java.util.List<com.lti.service.bo.Indicator> getList(final DetachedCriteria detachedCriteria);

	public PaginationSupport getList(final int pageSize,final int startIndex);

	public java.util.List<com.lti.service.bo.Indicator> getListByName(String indicatorName);
  
	public void remove(long indicatorID);
  
	public void update(com.lti.service.bo.Indicator indicator);
	
	public boolean isExisted(String name);
	public void clearDailyData(long indicatorID);

	public void updateDailyData(IndicatorDailyData indicatorDailyData);

	Indicator get(Long id);

	Long save(Indicator i);

	void saveOrUpdate(Indicator i);
	
	public IndicatorDailyData getFirstDailyData(Long id);
	
	public IndicatorDailyData getLastDailyData(Long id);
	
	/**
	 * get dailydata by it's id, not indicator's id
	 * @param dailydataid
	 * @return
	 */
	public IndicatorDailyData getIndicatorDailyData(long dailydataid);
	/**
	 * get dailydata by it's id, not indicator's id
	 * @param dailydataid
	 * @return
	 */
	public void removeIndicatorDailyData(long dailydataid);

	public void saveOrUpdateAll(Collection sdds);
	
}

