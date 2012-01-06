package com.xjany.bbs.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.GenericDAO;
import com.xjany.bbs.entity.InterGeneric;

/**
 * @param 一个通用的dao层
 */

@Transactional
public class GeneriDAOImpl<T,Pk extends Serializable> implements GenericDAO<T, Pk>{
	@Resource(name = "sessionFactory")
	protected SessionFactory sessionFactory;

	private Class<T> clazz;
	@SuppressWarnings("unchecked")
	public GeneriDAOImpl() {
		clazz = (Class<T>)((ParameterizedType)getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public boolean delete(T entity) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.delete(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(sessionFactory != null)
				sessionFactory.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from " + clazz.getName());
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public T findById(Serializable id) {
		Session session = sessionFactory.getCurrentSession();
		T t = (T) session.get(clazz, id);
		return t;
	}
	

	public boolean save(T entity) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(sessionFactory != null)
				sessionFactory.close();
		}
	}

	public boolean update(T entity) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(sessionFactory != null)
				sessionFactory.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean delete(Serializable... id){
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
	
	/**
	 * 根据属性查找对象
	 * @param propertyName 属性名称
	 * @param value 属性对应的值
	 * @return 对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByProperty(Object propertyName, String value) {
		String queryString = "from "+clazz.getName()+" as a where a." + propertyName + "= ?";
		return (List<T>) sessionFactory.getCurrentSession().createQuery(queryString).setParameter(0, value).list();
	}
	
	/**
	 * 回收站
	 * @param isRecycle 是否放到回收站
	 * @return 设置是否成功
	 */
	public boolean recycle(T entity,boolean isRecycle)
	{
		try{
			((InterGeneric) entity).recycle(isRecycle);
			this.update(entity);
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
