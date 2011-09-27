package com.lti.service;

import java.util.*;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.Holiday;
import com.lti.service.bo.TradingDate;
import com.lti.type.PaginationSupport;

public interface HolidayManager {

	public void update(com.lti.service.bo.Holiday holiday);
	
	public com.lti.service.bo.Holiday get(Date date);

	
	public List<com.lti.service.bo.Holiday> getHolidays(); 

    public PaginationSupport findPage(final DetachedCriteria detachedCriteria, final int pageSize,   
            final int startIndex);
    
    /**
     * get the date list form the startdate to today
     * there is no holiday in the date list,not sun,and sat too
     * it's very important when running the strategies
     * 
     * @param startDate
     * @return
     */

	Date getQuarterEnd(int year,int q);

	Date getMonthEnd(int year,int month);

	Date getYearEnd(int year);

	boolean isHoliday(Date date);

	void remove(long id);

	Holiday get(Long id);

	Long save(Holiday h);

	void saveOrUpdate(Holiday h);

	List<TradingDate> getTradingDates();
	Long saveTradingDate(TradingDate h);

	public List<Date> findByCriteria(DetachedCriteria detachedCriteria);
	public List<Date> getTradingDateList();
	public List<Date> getTradingDateList(Date pdate, Date stopDate);

	long[] getTradingDateArray();
	
	public void getTradingDateFromHoliday(Date startDate, Date endDate);

	String getTimeZoneID();
}
