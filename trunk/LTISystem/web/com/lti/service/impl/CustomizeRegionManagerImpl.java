package com.lti.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.io.Serializable;
import java.util.List;

import com.lti.service.CustomizeRegionManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Pages;
import com.lti.util.CustomizeUtil;


public class CustomizeRegionManagerImpl extends DAOManagerImpl implements CustomizeRegionManager, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	public long add(CustomizeRegion cr){
		if(IsPageCustomize(cr.getPageName())){	//the page has been customize, return 0
			return 0;
		}
		else
		{
			getHibernateTemplate().save(cr);
			return cr.getID();
		}
	}

	public Boolean update(CustomizeRegion cr){
		//the page is not existed
		if(IsPageCustomize(cr.getPageName())==false){
			return false;
		}
		//update the page
		else{
			// the page is existed when creating and the user try to save it again after the error notifying
			if(cr.getID().equals(new Long(0l))){
				CustomizeRegion cr_temp = cr;
				cr = this.get(cr.getPageName());
				if(cr == null)
					return false;
				CustomizeUtil.copyCustomizeRegionWithoutID(cr_temp, cr);
			}
			//execute the update procedure
			getHibernateTemplate().update(cr);
			return true;
		}
	}
	
	public CustomizeRegion get(Long ID){
		return (CustomizeRegion)getHibernateTemplate().get(CustomizeRegion.class, ID);
	}
	
	public CustomizeRegion get(String pageName){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(CustomizeRegion.class);
		detachedCriteria.add(Restrictions.eq("PageName", pageName));
		List<CustomizeRegion> crList = findByCriteria(detachedCriteria);
		if(crList != null && crList.size()!=0)
			return crList.get(0);
		else
			return null;
	}
	
	public CustomizeRegion getByPageID(Long pageID){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(CustomizeRegion.class);
		detachedCriteria.add(Restrictions.eq("PageID", pageID));
		List<CustomizeRegion> crList = findByCriteria(detachedCriteria);
		if(crList != null && crList.size()!=0)
			return crList.get(0);
		else
			return null;
	}
	
	public Boolean IsPageCustomize(String pageName){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(CustomizeRegion.class);
		detachedCriteria.add(Restrictions.eq("PageName", pageName));
		List<CustomizeRegion> crList = findByCriteria(detachedCriteria);
		if(crList != null && crList.size()!=0){
			return true;
		}
		else
			return false;
	}
	
	public List<Pages> getPages(){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Pages.class);
		detachedCriteria.addOrder(Order.asc("PageName"));
		List<Pages> pagesList = findByCriteria(detachedCriteria);
		if(pagesList == null || pagesList.size()==0)
			return null;
		return pagesList;
	}
	
	public Pages getPage(String pageName){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Pages.class);
		detachedCriteria.add(Restrictions.eq("PageName", pageName));
		List<Pages> pageList = findByCriteria(detachedCriteria);
		if(pageList != null && pageList.size()!=0){
			return pageList.get(0);
		}
		else
			return null;
	}
	
	public Pages getPage(Long ID){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Pages.class);
		detachedCriteria.add(Restrictions.eq("ID", ID));
		List<Pages> pageList = findByCriteria(detachedCriteria);
		if(pageList != null && pageList.size()!=0){
			return pageList.get(0);
		}
		else
			return null;
	}
	
	public Pages getPageBySymbol(String symbol){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Pages.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		List<Pages> pageList = findByCriteria(detachedCriteria);
		if(pageList != null && pageList.size()!=0){
			return pageList.get(0);
		}
		else
			return null;
	}
	
	public List<CustomizeRegion> getRegionCustomizes(){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(CustomizeRegion.class);
		detachedCriteria.addOrder(Order.asc("PageName"));
		List<CustomizeRegion> crList = findByCriteria(detachedCriteria);
		if(crList == null || crList.size()==0)
			return null;
		return crList;
	}

}
