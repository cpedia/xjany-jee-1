package com.lti.service.impl;


import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.service.RegionCustomizeManager;
import com.lti.service.bo.CustomizePage;
import com.lti.service.bo.RegionCustomize;
import com.lti.type.PaginationSupport;

public class RegionCustomizeManagerImpl extends DAOManagerImpl implements RegionCustomizeManager, Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Override
	public long add(RegionCustomize rc) {
		String PageName,RegionName;
		long GroupID;
		PageName=rc.getPageName();
		RegionName=rc.getRegionName();
		GroupID=rc.getGroupID();
		if(this.get(PageName,RegionName,GroupID)!=null)
			return -1;
		else
		{
			getHibernateTemplate().save(rc);
			return rc.getID();
		}
	}

	@Override
	public void remove(long ID) {
		Object obj=getHibernateTemplate().get(RegionCustomize.class, ID);
		getHibernateTemplate().delete(obj);
		
	}

	@Override
	public RegionCustomize get(long ID) {
		return (RegionCustomize)getHibernateTemplate().get(RegionCustomize.class, ID);
	}

	@Override
	public RegionCustomize get(String PageName, String RegionName, long GroupID) {
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(RegionCustomize.class);
		detachedCriteria.add(Restrictions.eq("PageName",PageName));
		detachedCriteria.add(Restrictions.eq("RegionName", RegionName));
		detachedCriteria.add(Restrictions.eq("GroupID", GroupID));
		List<RegionCustomize> rc=findByCriteria(detachedCriteria,1,0);
		if(rc.size()>=1)
			return rc.get(0);
		else
			return null;
	}

	public List<RegionCustomize> getRegionCustomizes(Integer pageSize,Integer startIndex)
	{
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(RegionCustomize.class);
		detachedCriteria.addOrder(Order.asc("PageName"));
		List<RegionCustomize> rcs=findByCriteria(detachedCriteria);
		return rcs;

	}
	
	
	public PaginationSupport getRegionCustomize(Integer pageSize, Integer startIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegionCustomize.class);
		PaginationSupport ps=findPageByCriteria(detachedCriteria, pageSize, startIndex);

		return ps;
	}
	
	@Override
	public int update(RegionCustomize rc) {
		String PageName,RegionName;
		long GroupID;
		PageName=rc.getPageName();
		RegionName=rc.getRegionName();
		GroupID=rc.getGroupID();
		RegionCustomize temp=this.get(PageName,RegionName,GroupID);
		if(temp!=null&&!temp.getID().equals(rc.getID()))
			return -1;
		else
		{
			getHibernateTemplate().update(rc);
			return 1;
		}		
	}

	
}