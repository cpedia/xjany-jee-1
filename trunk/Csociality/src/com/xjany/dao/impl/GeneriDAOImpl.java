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
	
	public void delete(Serializable id){
		Session session = sessionFactory.getCurrentSession();
		T t = (T) session.get(clazz, id);
		session.delete(t);
	}

}
