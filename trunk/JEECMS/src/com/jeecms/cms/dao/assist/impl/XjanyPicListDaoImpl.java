package com.jeecms.cms.dao.assist.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.cms.dao.assist.XjanyPicListDao;
import com.jeecms.cms.entity.assist.XjanyPicList;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class XjanyPicListDaoImpl extends
		HibernateBaseDao<XjanyPicList, Integer> implements XjanyPicListDao {
	@SuppressWarnings("unchecked")
	public List<XjanyPicList> getList() {
		Finder f = Finder.create("from XjanyPicList bean where 1=1");
		return find(f);
	}

	public int countByCtgId(Integer ctgId) {
		String hql = "select count(*) from XjanyPicList bean where bean.id=:ctgId";
		return ((Number) getSession().createQuery(hql).setParameter("ctgId",
				ctgId).iterate().next()).intValue();
	}

	public XjanyPicList findById(Integer id) {
		XjanyPicList entity = get(id);
		return entity;
	}

	public XjanyPicList save(XjanyPicList bean) {
		getSession().save(bean);
		return bean;
	}

	public XjanyPicList deleteById(Integer id) {
		XjanyPicList entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<XjanyPicList> getEntityClass() {
		return XjanyPicList.class;
	}

}