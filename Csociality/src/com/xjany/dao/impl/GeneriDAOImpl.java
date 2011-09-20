package com.xjany.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.dao.GenericDAO;

/*
 * @param 一个通用的dao层
 */

@Transactional
public class GeneriDAOImpl<T,Pk extends Serializable> implements GenericDAO<T, Pk>{
	@Resource(name = "sessionFactory")
	protected SessionFactory sessionFactory;

	private Class<T> clazz;
	public GeneriDAOImpl() {
		clazz = (Class<T>)((ParameterizedType)getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	public List<T> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from " + clazz.getName());
		List list = query.list();
		return list;
	}

	public T findById(Serializable id) {
		Session session = sessionFactory.getCurrentSession();
		T t = (T) session.get(clazz, id);
		return t;
	}
	

	public void save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
	}

	public void update(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}
	
	public boolean delete(Serializable[] id){
		try {
			Session session = sessionFactory.getCurrentSession();
			T t = (T) session.get(clazz, id[0]);
			session.delete(t);
			if(id.length -1 > 0)
			{
				for (int i = 0; i < id.length; i++)
				{
					t = (T) session.get(clazz, id[i+1]);
					session.delete(t);
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
	
	
	public boolean check(T entity, List<T> propertyName, String[] value) {
		Query query = null;
		try {
			StringBuffer sql = new StringBuffer("from "+clazz.getName()+" a where a."+ propertyName.get(0) +"="+ value[0]);
			for(int i = 0; i < propertyName.size(); i++)
			{
				sql.append(" and a."+ propertyName.get(i+1) +"="+value[i+1]);
			}
				query = sessionFactory.getCurrentSession().createQuery(sql.toString());
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
	public List<T> findByProperty(Object propertyName, String value) {
		String queryString = "from "+clazz.getName()+" as a where a." + propertyName + "= ?";
		return (List<T>) sessionFactory.getCurrentSession().createQuery(queryString).setParameter(0, value).list();
	}


}
