package com.lti.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.CUSIPManager;
import com.lti.service.bo.CUSIP;

public class CUSIPManagerImpl extends DAOManagerImpl implements CUSIPManager {

	private static final long serialVersionUID = 1L;


	@Override
	public void deleteByHQL(String string) {
		super.deleteByHQL(string);
	}

	@Override
	public CUSIP get(long id) {
		return (CUSIP) getHibernateTemplate().get(CUSIP.class, id);
	}




	@Override
	public void remove(long id) {
		Object obj = getHibernateTemplate().get(CUSIP.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public long save(CUSIP c) {
		getHibernateTemplate().save(c);
		return c.getID();
	}

	@Override
	public void saveOrUpdate(CUSIP c) {
		getHibernateTemplate().saveOrUpdate(c);
	}

	@Override
	public void update(CUSIP c) {
		getHibernateTemplate().update(c);
	}
	
	@Override
	public CUSIP getByCUSIP(String cusip) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CUSIP.class); 
		detachedCriteria.add(Restrictions.eq("CUSIP", cusip));
		List<CUSIP> cs=super.findByCriteria(detachedCriteria);
		if(cs!=null&&cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public CUSIP getBySymbol(String symbol) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CUSIP.class); 
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		List<CUSIP> cs=super.findByCriteria(detachedCriteria);
		if(cs!=null&&cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public String getCUSIP(String symbol) {
		CUSIP c=getBySymbol(symbol);
		if(c!=null)return c.getCUSIP();
		else return null;
	}

	@Override
	public String getSymbol(String cusip) {
		CUSIP c=getByCUSIP(cusip);
		if(c!=null)return c.getSymbol();
		else return null;
	}

	@Override
	public List<CUSIP> getCUSIPs() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CUSIP.class); 
		List<CUSIP> cs=super.findByCriteria(detachedCriteria);
		return cs;
	}
	

}
