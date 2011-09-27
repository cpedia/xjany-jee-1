package com.lti.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import com.lti.service.WidgetUserManager;
import com.lti.service.bo.WidgetUser;

public class WidgetUserManagerImpl extends DAOManagerImpl implements WidgetUserManager, Serializable{
	
	
	private static final long serialVersionUID = 1L;

	public void remove(long id){
		Object obj = getHibernateTemplate().get(com.lti.service.bo.WidgetUser.class, id);
		getHibernateTemplate().delete(obj);
	}

	public void save(WidgetUser widgetUser)
	{
		getHibernateTemplate().save(widgetUser);
	}

	
	public void saveOrUpdate(WidgetUser widgetUser) {
		getHibernateTemplate().saveOrUpdate(widgetUser);
	}

	public void update(WidgetUser widgetUser)
	{
		getHibernateTemplate().merge(widgetUser);
	}

	public List<WidgetUser> getWidgetUserList()
	{
		final String listWidgetUser = "from WidgetUser widgetUser ";
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(listWidgetUser);
		return query.list();
	}

	public WidgetUser getWidgetUser(long id)
	{
		return (WidgetUser)getHibernateTemplate().get(WidgetUser.class, id);
	}

	public Integer checkWidgetUser(String email)
	{
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from WidgetUser widgetUser where widgetUser.email=?");
		query.setParameter(0, email);
		return query.list().size();
	}
}
	