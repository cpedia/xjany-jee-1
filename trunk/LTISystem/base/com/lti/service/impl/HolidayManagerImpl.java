package com.lti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import com.lti.util.LTIDate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.service.HolidayManager;
import com.lti.service.bo.Holiday;
import com.lti.service.bo.TradingDate;
import com.lti.system.ContextHolder;
import com.lti.type.PaginationSupport;

public class HolidayManagerImpl extends DAOManagerImpl implements HolidayManager , Serializable {

	private static final long serialVersionUID = 1L;
	


	@Override
	public void remove(long id){
		Object obj = getHibernateTemplate().get(com.lti.service.bo.Holiday.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public Holiday get(Long id) {
		return (Holiday) getHibernateTemplate().get(Holiday.class, id);
	}

	
	@Override
	public Long save(Holiday h) {
		getHibernateTemplate().save(h);
		return h.getID();
	}

	
	@Override
	public void saveOrUpdate(Holiday h) {
		getHibernateTemplate().saveOrUpdate(h);
	}

	
	@Override
	public void update(Holiday h) {
		getHibernateTemplate().update(h);
	}
	

	public PaginationSupport findPage(DetachedCriteria detachedCriteria,int pageSize, int startIndex) {

		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
		
	}

	public Holiday get(Date date) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Holiday.class);
		
		detachedCriteria.add(Restrictions.eq("Date", date));
		
		List<Holiday> bolist= findByCriteria(detachedCriteria);
		
		if(bolist.size()>=1)return bolist.get(0);
		
		else return null;
	}


	public boolean isHoliday(Date date){
		
		if(this.get(date)!=null)return true;
		
		else return false;
		
	}
	
	public List<Holiday> getHolidays() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Holiday.class); 
		detachedCriteria.addOrder(Order.asc("Date"));
		List<Holiday> bolist=findByCriteria(detachedCriteria);
		
		return bolist;
		
	}

	public List<TradingDate> getTradingDates() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TradingDate.class); 
		
		List<TradingDate> bolist=findByCriteria(detachedCriteria);
		
		return bolist;
		
	}


	public Date getYearEnd(int year){
		
		return this.getMonthEnd(year, 12);
		
	}
	
	public Date getMonthEnd(int year,int month){
		Calendar ca = Calendar.getInstance();
		
		
		
		if(month>=12)
			ca.set(year+1, 0, 1,0,0,0);
		else
			ca.set(year, month,1,0,0,0);
		
		Date bDate=ca.getTime();
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TradingDate.class);
		
		detachedCriteria.add(Restrictions.lt("TradingDate", bDate));
		
		detachedCriteria.addOrder(Order.desc("TradingDate"));
		
		List<TradingDate> tds=findByCriteria(detachedCriteria,1,0);
		
		if(tds!=null&&tds.size()>=1)return tds.get(0).getTradingDate();
		else return null;
	}
	public Date getQuarterEnd(int year,int q){
		int month=3;
		switch(q){
			case 1:
				month=3;
				break;
			case 2:
				month=6;
				break;
			case 3:
				month=9;
				break;
			default:
				month=12;
				break;
		}
		
		return this.getMonthEnd(year, month);
	}

	@Override
	public Long saveTradingDate(TradingDate h) {
		// TODO Auto-generated method stub
		 getHibernateTemplate().save(h);
		 return h.getID();
	}

	@Override
	public List<Date> getTradingDateList() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TradingDate.class);
		
		detachedCriteria.addOrder(Order.desc("TradingDate"));
		
		detachedCriteria.setProjection(Projections.property("TradingDate"));
		
		List<Date> tds=findByCriteria(detachedCriteria);
		
		if(tds==null)return null;
		
		return tds;
	}

	@Override
	public List<Date> getTradingDateList(Date pdate, Date stopDate) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TradingDate.class);
		
		if(pdate!=null)detachedCriteria.add(Restrictions.ge("TradingDate", pdate));
		
		if(stopDate!=null)detachedCriteria.add(Restrictions.le("TradingDate", stopDate));
		
		detachedCriteria.addOrder(Order.asc("TradingDate"));
		
		detachedCriteria.setProjection(Projections.property("TradingDate"));
		
		List<Date> tds=findByCriteria(detachedCriteria);
		
		return tds;
	}

	@Override
	public long[] getTradingDateArray(){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TradingDate.class);
		
		detachedCriteria.addOrder(Order.desc("TradingDate"));
		
		detachedCriteria.setProjection(Projections.property("TradingDate"));
		
		List<Date> tds=findByCriteria(detachedCriteria);
		
		if(tds==null)return null;
		
		long[] dates=new long[tds.size()];
		for(int i=0;i<dates.length;i++){
			dates[i]=tds.get(i).getTime();
		}
		return dates;
	}
	
	public void getTradingDateFromHoliday(Date startDate, Date endDate){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Holiday.class); 
		detachedCriteria.add(Restrictions.and(Restrictions.ge("Date",startDate ), Restrictions.le("Date", endDate)));
		List<Holiday> holidayList=findByCriteria(detachedCriteria);
		List<TradingDate> tradingDateList = new ArrayList<TradingDate>();
		HashMap<Date,Boolean> holidayMap = new HashMap<Date,Boolean>();
		for(int i=0;i<holidayList.size();++i)
			holidayMap.put(holidayList.get(i).getDate(), Boolean.TRUE);
		Date curDate = new Date();
		curDate = startDate;
		while(!LTIDate.after(curDate, endDate)){
			if(LTIDate.isWeekDay(curDate)&&holidayMap.get(curDate)==null)
			{
				TradingDate tradingDate = new TradingDate();
				tradingDate.setTradingDate(curDate);
				tradingDateList.add(tradingDate);
			}
			curDate = LTIDate.add(curDate, 1);
		}
		getHibernateTemplate().saveOrUpdateAll(tradingDateList);
	}
	
	@Override
	public String getTimeZoneID(){
		return TimeZone.getDefault().getID();
	}
	
	public static void main(String[] cmds){
		HolidayManager holidayManager = (HolidayManager)ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
		Date startDate = LTIDate.getDate(2010, 1, 1);
		Date endDate = LTIDate.getDate(2011, 12, 31);
		holidayManager.getTradingDateFromHoliday(startDate, endDate);
	}
}
