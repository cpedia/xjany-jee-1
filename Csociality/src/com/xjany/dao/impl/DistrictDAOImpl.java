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
public class DistrictDAOImpl extends GeneriDAOImpl<District, Integer> implements DistrictDAO {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<District> findByProperty(String propertyName, Object value) {
		String queryString = "from District as district where district." + propertyName + "= ?";
		return (List<District>) sessionFactory.getCurrentSession().createQuery(queryString).setParameter(0, value).list();
	}

	@SuppressWarnings("unchecked")
	public List<District> listMessage(int pageNo, int pageSize) {
		final String listDistrict = "from District district ";
		Query query = sessionFactory.getCurrentSession().createQuery(listDistrict);
		query.setFirstResult(pageNo);
		query.setMaxResults(pageSize);
		return (List<District>) query.list();
	}

	public int getMaxLength() {
		return this.findAll().size();
	}

	public boolean checkDistrict(District district) {
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("from District a where a.name=? and a.province=?");
			query.setString(0, district.getName());
			query.setString(1, district.getProvince());
			if(query.list().size() > 0)
				return true;
			else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (sessionFactory != null)
				sessionFactory.close();
		}
	}
}
