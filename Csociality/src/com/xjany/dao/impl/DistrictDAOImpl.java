package com.xjany.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.dao.DistrictDAO;
import com.xjany.entity.District;

@Repository
public class DistrictDAOImpl implements DistrictDAO
{

	private SessionFactory sessionFactory;
	private District district;
	
	public District getDistrict()
	{
		return district;
	}
	public void setDistrict(District district)
	{
		this.district = district;
	}
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public void addDistrict(District district)
	{
		sessionFactory.getCurrentSession().persist(district);
	}
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<District> listDistrict()
	{
		final String listDistrict = "from District";
		return (List<District>)sessionFactory.getCurrentSession().createQuery(listDistrict).list();
	}

	public District findById(int id)
	{
		return (District)sessionFactory.getCurrentSession().get(District.class, id);
	}

	public void updateDistrict(District district)
	{
		sessionFactory.getCurrentSession().merge(district);
	}

	public void delDistrict(District district)
	{
		sessionFactory.getCurrentSession().delete(district);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<District> findByProperty(String propertyName, Object value)
	{
		String queryString = "from District as district where district."
			+ propertyName + "= ?";
		return (List<District>)sessionFactory.getCurrentSession().createQuery(queryString).setParameter(0, value).list();
	}

	@SuppressWarnings("unchecked")
	public List<District> listMessage(int pageNo, int pageSize)
	{
		final String listDistrict = "from District district ";
		Query query = sessionFactory.getCurrentSession().createQuery(listDistrict);
		query.setFirstResult(pageNo);
		query.setMaxResults(pageSize);
		return (List<District>)query.list();
	}

	public int getMaxLength()
	{
		return listDistrict().size();
	}
	
	public int checkDistrict(String name, String province)
	{
		Query query = sessionFactory.getCurrentSession().createQuery("from District district where district.name=? and district.province=?");
		query.setString(0, name);
		query.setString(1, province);
		return query.list().size();
	}
}
