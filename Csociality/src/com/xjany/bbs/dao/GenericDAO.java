package com.xjany.bbs.dao;

import java.io.Serializable;
import java.util.List;


/*
 * @param 一个通用的dao层接口
 * 一些的基本处理(增，删，改，查)
 */
public interface GenericDAO<T,Pk extends Serializable> {
	public List<T> findAll();
	public T findById(int id);
	public List<T> findByProperty(Object propertyName, String value);
	public boolean update(T entity);
	public boolean delete(T entity);
	public int save(T entity);
	public boolean recycle(T entity,boolean isRecycle);
	public boolean delete(int ... id);
	public boolean check(T entity, List<T> propertyName, String... value);
	public List findBySql(String sql);
}