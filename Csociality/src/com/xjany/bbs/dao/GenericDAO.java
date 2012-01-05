package com.xjany.bbs.dao;

import java.io.Serializable;
import java.util.List;


/*
 * @param 一个通用的dao层接口
 * 一些的基本处理(增，删，改，查)
 */
public interface GenericDAO<T,Pk extends Serializable> {
	public List<T> findAll();
	public T findById(Pk id);
	public List<T> findByProperty(Object propertyName, String value);
	public void update(T entity);
	public void delete(T entity);
	public void save(T entity);
	public boolean recycle(T entity,boolean isRecycle);
	public boolean delete(Serializable... id);
	public boolean check(T entity, List<T> propertyName, String[] value);
}