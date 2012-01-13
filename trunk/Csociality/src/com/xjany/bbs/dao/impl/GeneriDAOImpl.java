package com.xjany.bbs.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.GenericDAO;
import com.xjany.bbs.entity.InterGeneric;
import com.xjany.common.page.Finder;
import com.xjany.common.page.Pagination;

/**
 * @param 一个通用的dao层
 */

@Transactional
public class GeneriDAOImpl<T,Pk extends Serializable> implements GenericDAO<T, Pk>
{
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
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from " + clazz.getName());
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public T findById(int id) {
		Session session = sessionFactory.getCurrentSession();
		T t = (T) session.get(clazz, id);
		return t;
	}
	

	public int save(T entity) {
		try {
			Session session = sessionFactory.getCurrentSession();
			int d = (Integer) session.save(entity);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
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
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean delete(int ... id){
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
		}
	}
	@Transactional(readOnly = true)
	public boolean check(T entity, Map<String, String> property) {
		Query query = null;
		try {

			StringBuffer sql = new StringBuffer("from "+clazz.getName()+" a where 1=1 ");
			Set<Map.Entry<String, String>> set = property.entrySet();
	        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
	            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
	            sql.append(" and a."+ entry.getKey() +"="+entry.getValue());
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
	@Transactional(readOnly = true)
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
		}
	}
	
	/**
	 * 通过Finder获得分页数据
	 * 
	 * @param finder
	 *            Finder对象
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Pagination find(Finder finder, int pageNo, int pageSize) {
		int totalCount = countQueryResult(finder);
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());
			return p;
		}
		Query query = sessionFactory.getCurrentSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		List list = query.list();
		p.setList(list);
		return p;
	}
	
	/**
	 * 获得Finder的记录总数
	 * 
	 * @param finder
	 * @return
	 */
	protected int countQueryResult(Finder finder) {
		Query query = sessionFactory.getCurrentSession().createQuery(finder.getRowCountHql());
		finder.setParamsToQuery(query);
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		return ((Number) query.iterate().next()).intValue();
	}
	
	/**
	 * 通过sql查找
	 * @param sql
	 */
	public List findBySql(String sql){
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}

}
