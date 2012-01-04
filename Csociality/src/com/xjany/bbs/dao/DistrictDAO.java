package com.xjany.bbs.dao;

import java.io.Serializable;
import java.util.List;

import com.xjany.bbs.entity.District;

public interface DistrictDAO extends GenericDAO<District,Integer>
{
	public List<District> findByProperty(String propertyName, Object value);
	public List<District> listMessage(int pageNo,int pageSize);
	public int getMaxLength();
	public boolean recycle(Serializable... id);
}
