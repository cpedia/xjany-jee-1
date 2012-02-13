package com.xjany.bbs.dao;

import java.io.Serializable;
import java.util.List;

import com.xjany.common.util.XjanyMap;


/*
 * @param 一个通用的dao层接口
 * 一些的基本处理(增，删，改，查)
 */
public interface GenericDAO<T,Pk extends Serializable> {
	public List<T> findAll();
	public T findById(int id);
	public List<T> findByProperty(Object propertyName, String value);
	public T update(T entity);
	public T delete(T entity);
	public T save(T entity);
	public T recycle(T entity,boolean isRecycle);
	public void delete(int ... id);
	public T check(T entity, XjanyMap<String, String> property);
	public List findBySql(String sql);
}