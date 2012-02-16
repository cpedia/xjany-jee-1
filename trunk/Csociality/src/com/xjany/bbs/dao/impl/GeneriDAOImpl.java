package com.xjany.bbs.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.GenericDAO;
import com.xjany.bbs.entity.InterGeneric;
import com.xjany.common.exception.DaoException;
import com.xjany.common.page.Finder;
import com.xjany.common.page.Pagination;
import com.xjany.common.util.XjanyMap;
import com.xjany.common.util.XjanyMapEntry;

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
	
	public T delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
		return entity;
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
	

	public T save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
		return entity;
	}

	public T update(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public void delete(int ... id) throws DaoException{
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
	}
	@Transactional(readOnly = true)
	public T check(T entity, XjanyMap<String, String> property) {
		Query query = null;
			StringBuffer sql = new StringBuffer("from "+clazz.getName()+" a where 1=1 ");
//			Set<Map.Entry<String, String>> set = property.entrySet();
//	        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
//	            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
//	            sql.append(" and a."+ entry.getKey() +"='"+entry.getValue()+"'");
//	        }
			for(Iterator<XjanyMapEntry<String,String>> e = property.iterator() ; e.hasNext();)
			{
				XjanyMapEntry<String,String> e_ = e.next();
				sql.append(" and a."+ e_.getKey() +"='"+e_.getValue()+"'");
			}
			query = sessionFactory.getCurrentSession().createQuery(sql.toString());
			entity = (T)query.list().get(0);
			return entity;
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
	public T recycle(T entity,boolean isRecycle)
	{
		((InterGeneric) entity).recycle(isRecycle);
		this.update(entity);
		return entity;
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
