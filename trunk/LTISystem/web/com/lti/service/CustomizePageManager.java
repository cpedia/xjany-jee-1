package com.lti.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.CustomizePage;
import com.lti.type.PaginationSupport;

public interface CustomizePageManager {

	void update(CustomizePage cp);

	void remove(long id);

	PaginationSupport getCustomizePages(int pageSize, int startIndex);

	List<CustomizePage> getCustomizePages(DetachedCriteria detachedCriteria);

	List<CustomizePage> getCustomizePages();

	CustomizePage get(String cpname);

	CustomizePage get(long id);

	PaginationSupport getCustomizePages(DetachedCriteria detachedCriteria, int pageSize, int startIndex);

	long add(CustomizePage cp);

}
