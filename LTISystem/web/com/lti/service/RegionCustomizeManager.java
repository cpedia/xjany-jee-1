package com.lti.service;

import java.util.List;

import com.lti.service.bo.RegionCustomize;
import com.lti.type.PaginationSupport;

public interface RegionCustomizeManager{
	
	long add(RegionCustomize rc);
	
	RegionCustomize get(long ID);
	
	RegionCustomize get(String PageName,String RegionName,long GruopID);
	
	List<RegionCustomize> getRegionCustomizes(Integer pageSize,Integer startIndex);
	
	PaginationSupport getRegionCustomize(Integer pageSize,Integer startIndex);
	
	int update(RegionCustomize rc);
	
	void remove(long ID);
	
}