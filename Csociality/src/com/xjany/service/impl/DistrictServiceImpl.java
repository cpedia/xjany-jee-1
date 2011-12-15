package com.xjany.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.dao.DistrictDAO;
import com.xjany.entity.District;
import com.xjany.service.DistrictService;

@Service
@Transactional
public class DistrictServiceImpl implements DistrictService
{

	private DistrictDAO districtDAO;
	
	@Resource
	public void setDistrictDAO(DistrictDAO districtDAO)
	{
		this.districtDAO = districtDAO;
	}

	public void addDistrict(District district)
	{
		districtDAO.save(district);
	}

	public List<District> listDistrict()
	{
		return districtDAO.findAll();
	}

	public District findById(int id)
	{
		return districtDAO.findById(id);
	}

	public void updateDistrict(District district)
	{
		districtDAO.update(district);
	}

	public void delDistrict(District district)
	{
		districtDAO.delete(district);
	}

	public List<District> findByProperty(String propertyName, Object value)
	{
		return districtDAO.findByProperty(propertyName, value);
	}

	public List<District> listMessage(int pageNo, int pageSize)
	{
		return districtDAO.listMessage(pageNo, pageSize);
	}

	public int getMaxLength()
	{
		return districtDAO.getMaxLength();
	}

	@Override
	public boolean checkDistrict(District district,
			List<District> propertyName, Object[] value) {
		// TODO Auto-generated method stub
		return false;
	}

//	public boolean checkDistrict(District district, List<District> propertyName, Object[] value)
//	{
////		return districtDAO.check(district, propertyName, value);
//	}

}
