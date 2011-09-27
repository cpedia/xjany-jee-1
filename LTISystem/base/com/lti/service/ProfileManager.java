package com.lti.service;

import java.util.List;

import com.lti.service.bo.Profile;
import com.lti.type.PaginationSupport;

import org.hibernate.criterion.DetachedCriteria;

public interface ProfileManager {

	public Profile get(
			java.lang.Long portfolioID,
			java.lang.Long userID,
			java.lang.Long planID
	);
	
	public void save(Profile profile);
	
	public void delete(
			java.lang.Long portfolioID,
			java.lang.Long userID,
			java.lang.Long planID
	);
	public List<Profile> getProfilesByPortfolioID(java.lang.Long portfolioID);
	public Profile getProfileByPortfolioID(java.lang.Long portfolioID);
	public List<Profile> getProfilesByUserID(java.lang.Long userID);
	public List<Profile> getProfilesByPlanID(java.lang.Long planID);

	public void saveOrUpdate(Profile profile);

	public void update(Profile profile);
	

	
	
	
	public List<Profile> getProfiles();
	public PaginationSupport getProfiles(int pageSize, int startIndex);
	
	public void deleteByHQL(String string);
	
	public List<Profile> getProfiles(DetachedCriteria detachedCriteria);
	public PaginationSupport getProfiles(DetachedCriteria detachedCriteria,	int pageSize, int startIndex);

}
