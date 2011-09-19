package com.xjany.dao;

import java.util.List;

import com.xjany.entity.District;

public interface DistrictDAO
{
	public void addDistrict(District district);
	public List<District> listDistrict();
	public District findById(int id);
	public void updateDistrict(District district);
	public void delDistrict(District district);
	public List<District> findByProperty(String propertyName, Object value);
	public List<District> listMessage(int pageNo,int pageSize);
	public int getMaxLength();
	public int checkDistrict(String name, String province);
}
