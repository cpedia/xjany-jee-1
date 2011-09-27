package com.lti.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.service.IndicatorManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.IndicatorDailyData;
import com.lti.type.Interval;
import com.lti.type.PaginationSupport;

public class IndicatorManagerImpl extends DAOManagerImpl implements IndicatorManager , Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void remove(long id){
		Object obj = getHibernateTemplate().get(Indicator.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public Indicator get(Long id) {
		return (Indicator) getHibernateTemplate().get(Indicator.class, id);
	}

	
	@Override
	public Long save(Indicator i) {
		getHibernateTemplate().save(i);
		return i.getID();
	}

	
	@Override
	public void saveOrUpdate(Indicator i) {
		getHibernateTemplate().saveOrUpdate(i);
	}

	
	@Override
	public void update(Indicator i) {
		getHibernateTemplate().update(i);
	}
	
	
	@Override
	public Indicator get(String name) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Indicator.class); 
		
		detachedCriteria.add(Restrictions.eq("Symbol", name));
		
		List<Indicator> bolist= findByCriteria(detachedCriteria,1,0);
		
		if(bolist.size()>=1)return bolist.get(0);
		
		else return null;
	}
	
	
	
	
	public long add(Indicator indicator) {
		//judge whether the indicator does already exist(add by chaos)
		if(this.get(indicator.getSymbol()) != null){
			return -1l;
		}
		
		getHibernateTemplate().save(indicator);
		
		return indicator.getID();
	}
	
	public void addAll(List<com.lti.service.bo.IndicatorDailyData>  indicatorDailyDataList)
	{
		getHibernateTemplate().saveOrUpdateAll(indicatorDailyDataList);
	}

	public long addDailyData(IndicatorDailyData indicatorDailyData) {

		getHibernateTemplate().save(indicatorDailyData);
		
		return indicatorDailyData.getID();
	}

	public void clearDailyData(long indicatorID) {
		
		deleteByHQL("from IndicatorDailyData idd where idd.IndicatorID="+indicatorID);
		
	}

	public PaginationSupport findDailyDataPage(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		
		PaginationSupport ps=findPageByCriteria(detachedCriteria, pageSize, startIndex);
		
		return ps;
	}

	public PaginationSupport findPage(DetachedCriteria detachedCriteria) {
		return findPage(detachedCriteria, PaginationSupport.PAGESIZE, 0);
	}

	public PaginationSupport findPage(DetachedCriteria detachedCriteria,int startIndex) {
		// TODO Auto-generated method stub
		return findPage(detachedCriteria, PaginationSupport.PAGESIZE, startIndex);
	}

	public PaginationSupport findPage(DetachedCriteria detachedCriteria,int pageSize, int startIndex) {
		
		PaginationSupport ps=findPageByCriteria(detachedCriteria, pageSize, startIndex);
		
		return ps;
		
	}

	public Indicator get(long id) {
		
		Indicator entity = (Indicator) getHibernateTemplate().get(Indicator.class, id);
		
		return entity;
	}
	


	public List<IndicatorDailyData> getDailydata(long id) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		List<com.lti.service.bo.IndicatorDailyData> bolist = findByCriteria(detachedCriteria);
		
		return bolist;
	}

	public List<IndicatorDailyData> getDailydata(long id, Interval interval) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));
		
		detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
		
		detachedCriteria.addOrder(Order.asc("Date"));
		
		List<com.lti.service.bo.IndicatorDailyData> bolist = findByCriteria(detachedCriteria);
		
		return bolist;
	}

	public PaginationSupport getDailydata(long id, Interval interval,int pageSize, int startIndex) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));
		
		detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
		
		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		
		return ps;
	}

	public PaginationSupport getDailydata(long id, int pageSize, int startIndex) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	//this method is low efficiency
	public IndicatorDailyData getDailydata(long id, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class); 
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		detachedCriteria.add(Restrictions.le("Date", date));
		
		detachedCriteria.addOrder(Order.desc("Date"));
		
		List<IndicatorDailyData> bolist=findByCriteria(detachedCriteria);
		
		if(bolist.size()>=1)return bolist.get(0);
		
		else  {
			
			detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
			
			detachedCriteria.add(Restrictions.eq("IndicatorID", id));
			
			detachedCriteria.add(Restrictions.gt("Date", date));
			
			detachedCriteria.addOrder(Order.asc("Date"));
			
			bolist=findByCriteria(detachedCriteria);
			
			if(bolist.size()>=1)return bolist.get(0);
			
		}
		
		return null;
	}

	public List<Indicator> getList() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Indicator.class);
		
		List<com.lti.service.bo.Indicator> bolist = findByCriteria(detachedCriteria);
		
		return bolist;
	}

	public List<Indicator> getList(DetachedCriteria detachedCriteria) {
		
		List<com.lti.service.bo.Indicator> bolist = findByCriteria(detachedCriteria);
		
		return bolist;
	}

	public PaginationSupport getList(int pageSize, int startIndex) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Indicator.class);
		
		PaginationSupport ps = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		
		return ps;
	}

	public List<Indicator> getListByName(String indicatorName) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Indicator.class);
		
		detachedCriteria.add(Restrictions.eq("Symbol", indicatorName));
		
		List<com.lti.service.bo.Indicator> bolist = findByCriteria(detachedCriteria);
		
		return bolist;
	}

	public boolean isExisted(String name) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Indicator.class);
		
		detachedCriteria.add(Restrictions.eq("Symbol", name));
		
		List<com.lti.service.bo.Indicator> bolist = findByCriteria(detachedCriteria);
		
		if(bolist.size()>=1)return true;
		
		else return false;
	}


	public void updateDailyData(IndicatorDailyData indicatorDailyData)
	{
		getHibernateTemplate().update(indicatorDailyData);
	}
	
	public IndicatorDailyData getFirstDailyData(Long id){
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		detachedCriteria.addOrder(Order.asc("Date"));
		
		detachedCriteria.add(Restrictions.isNotNull("Value"));
		
		List<com.lti.service.bo.IndicatorDailyData> bolist = findByCriteria(detachedCriteria);
		
		if(bolist.size()>=1)return bolist.get(0);
		
		else return null;
	}

	public IndicatorDailyData getLastDailyData(Long id){
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndicatorDailyData.class);
		
		detachedCriteria.add(Restrictions.eq("IndicatorID", id));
		
		detachedCriteria.addOrder(Order.desc("Date"));
		
		detachedCriteria.add(Restrictions.isNotNull("Value"));
		
		List<com.lti.service.bo.IndicatorDailyData> bolist = findByCriteria(detachedCriteria);
		
		if(bolist.size()>=1)return bolist.get(0);
		
		else return null;
	}


	@Override
	public IndicatorDailyData getIndicatorDailyData(long dailydataid) {
		// TODO Auto-generated method stub
		return (IndicatorDailyData) getHibernateTemplate().get(IndicatorDailyData.class, dailydataid);
	}


	@Override
	public void removeIndicatorDailyData(long dailydataid) {
		// TODO Auto-generated method stub
		IndicatorDailyData i=getIndicatorDailyData(dailydataid);
		if(i!=null)getHibernateTemplate().delete(i);
	}

	
	
}
