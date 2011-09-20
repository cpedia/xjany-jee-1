package com.xjany.dao;

import java.util.List;

import com.xjany.entity.District;

public interface DistrictDAO extends GenericDAO<District,Integer>
{
	public List<District> findByProperty(String propertyName, Object value);
	public List<District> listMessage(int pageNo,int pageSize);
	public int getMaxLength();
}
