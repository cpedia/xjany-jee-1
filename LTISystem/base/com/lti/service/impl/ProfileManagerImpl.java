package com.lti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.lti.service.bo.Profile;
import com.lti.system.ContextHolder;

import com.lti.type.PaginationSupport;

public class ProfileManagerImpl extends DAOManagerImpl implements com.lti.service.ProfileManager, Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	public Profile get(
			java.lang.Long portfolioID,
			java.lang.Long userID,
			java.lang.Long planID
	){
		List<Profile> pps=super.findByHQL("from Profile pp where "+
				"pp.PortfolioID="+portfolioID+" and "+
				"pp.UserID="+userID+" and "+
				"pp.PlanID="+planID
			
		);
		if(pps!=null&&pps.size()>0)return pps.get(0);
		else return null;
	}
	
	public void save(Profile profile) {
		getHibernateTemplate().save(profile);
	}
	
	public void delete(
			java.lang.Long portfolioID,
			java.lang.Long userID,
			java.lang.Long planID
	){
		Profile pp=this.get(
				portfolioID,
				userID,
				planID
		);
		if(pp!=null)getHibernateTemplate().delete(pp);
	}
	public List<Profile> getProfilesByPortfolioID(java.lang.Long portfolioID){
		List<Profile> pps=super.findByHQL("from Profile pp where pp.PortfolioID="+portfolioID);
		return pps;
	}
	
	public Profile getProfileByPortfolioID(java.lang.Long portfolioID){
		List<Profile> pps = getProfilesByPortfolioID(portfolioID);
		if(pps != null && pps.size() > 0)
			return pps.get(0);
		return null;
	}
	
	public List<Profile> getProfilesByUserID(java.lang.Long userID){
		List<Profile> pps=super.findByHQL("from Profile pp where pp.UserID="+userID);
		return pps;
	}
	public List<Profile> getProfilesByPlanID(java.lang.Long planID){
		List<Profile> pps=super.findByHQL("from Profile pp where pp.PlanID="+planID);
		return pps;
	}

	
	@Override
	public void saveOrUpdate(Profile profile) {
		getHibernateTemplate().saveOrUpdate(profile);
	}

	
	@Override
	public void update(Profile profile) {
		getHibernateTemplate().update(profile);
		
	}

	
	
	

	

	
	@Override
	public List<Profile> getProfiles() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Profile.class); 
		List<Profile> objs= findByCriteria(detachedCriteria);
		return objs;
	}
	
	
	@Override
	public PaginationSupport getProfiles(int pageSize, int startIndex) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Profile.class); 
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}
	@Override
	public PaginationSupport getProfiles(DetachedCriteria detachedCriteria,	int pageSize, int startIndex){
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}
	public List<Profile> getProfiles(DetachedCriteria detachedCriteria){
		return findByCriteria(detachedCriteria);
	}
}
