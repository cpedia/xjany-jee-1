package com.xjany.dao;

import java.io.Serializable;
import java.util.List;


/*
 * @param 一个通用的dao层接口
 * 一些的基本处理(增，删，改，查)
 */
public interface GenericDAO<T,Pk extends Serializable> {
	public List<T> findAll();
	public T findById(Pk id);
	public List<T> findByProperty(Object propertyName, Object value);
	public void update(T entity);
	public void delete(T entity);
	public void save(T entity);
	public void delete(Serializable id);
	public boolean check(T entity, Object propertyName, Object value);
}