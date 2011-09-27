package com.lti.service;

import java.util.List;

import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Pages;

public interface CustomizeRegionManager {
	long add(CustomizeRegion cr);

	Boolean update(CustomizeRegion cr);
	
	CustomizeRegion get(String pageName);
	
	CustomizeRegion getByPageID(Long pageID);
	
	CustomizeRegion get(Long ID);
	
	List<Pages> getPages();
	
	Pages getPage(String pageName);
	
	Pages getPage(Long ID);
	
	Pages getPageBySymbol(String symbol);
	
	List<CustomizeRegion> getRegionCustomizes();
}
