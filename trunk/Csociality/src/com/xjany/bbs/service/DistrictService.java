package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.District;

public interface DistrictService
{
	public void addDistrict(District district);
	public List<District> listDistrict();
	public District findById(int id);
	public void updateDistrict(District district);
	public void delDistrict(District district);
	public List<District> findByProperty(String propertyName, Object value);
	public List<District> listMessage(int pageNo,int pageSize);
	public int getMaxLength();
	public boolean checkDistrict(District district, List<District> propertyName, Object[] value);
}
