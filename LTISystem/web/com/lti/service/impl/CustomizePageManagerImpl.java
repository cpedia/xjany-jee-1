package com.lti.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.CustomizePageManager;
import com.lti.service.bo.CustomizePage;
import com.lti.type.PaginationSupport;

public class CustomizePageManagerImpl extends DAOManagerImpl implements CustomizePageManager, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public long add(CustomizePage cp) {
		// TODO Auto-generated method stub
		if(this.get(cp.getName()) != null){
			return -1l;
		}
		getHibernateTemplate().save(cp);
		return cp.getID();
	}

	@Override
	public PaginationSupport getCustomizePages(DetachedCriteria detachedCriteria,
			int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public CustomizePage get(long id) {
		// TODO Auto-generated method stub
		return (CustomizePage) getHibernateTemplate().get(CustomizePage.class, id);
	}

	@Override
	public CustomizePage get(String cpname) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomizePage.class); 
		detachedCriteria.add(Restrictions.eq("Name", cpname));
		List<com.lti.service.bo.CustomizePage> bolist=findByCriteria(detachedCriteria);
		if(bolist.size()>=1)return bolist.get(0);
		else return null;
	}

	@Override
	public List<CustomizePage> getCustomizePages() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomizePage.class);
		List<CustomizePage> bolist= findByCriteria(detachedCriteria);
		return bolist;
	}
	@Override
	public List<CustomizePage> getCustomizePages(DetachedCriteria detachedCriteria) {
		// TODO Auto-generated method stub
		return findByCriteria(detachedCriteria);
	}
	@Override
	public PaginationSupport getCustomizePages(int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomizePage.class);
		PaginationSupport ps=findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}

	@Override
	public void remove(long id) {
		// TODO Auto-generated method stub
		Object obj = getHibernateTemplate().get(CustomizePage.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public void update(CustomizePage cp) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(cp);
	}
}
