package com.xjany.dao.impl;

import java.io.Serializable;
import java.util.List;


import org.hibernate.Query;
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
	public boolean recycle(Serializable... id)
	{
		try{
			District d = this.findById(id);
			//d.setName(1);
			this.update(d);
			if(id.length -1 > 0)
			{
				for (int i = 0; i < id.length; i++)
				{
					d = new District();
					d = this.findById(id[i+1]);
					//d.setName(1);
					this.update(d);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(sessionFactory != null)
			sessionFactory.close();
		}
	}
}
