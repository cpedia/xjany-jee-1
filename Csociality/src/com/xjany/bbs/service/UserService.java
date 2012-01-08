package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllUser;

public interface UserService {
	public List<AllUser> findAll();
	public AllUser findById(int id);
	public List<AllUser> findByProperty(Object propertyName, String value);
	public boolean update(AllUser entity);
	public boolean delete(AllUser entity);
	public boolean save(AllUser entity);
	public boolean recycle(AllUser entity,boolean isRecycle);
	public boolean delete(int... id);
	public boolean check(AllUser entity, List<AllUser> propertyName, String... value);
}
