package com.xjany.bbs.dao;

import java.util.List;

import com.xjany.bbs.entity.AllUser;

public interface UserDAO extends GenericDAO<AllUser,Integer>
{
	public boolean save(UserDAO  user);
	public boolean recycle(UserDAO  user);
	public AllUser findById(Integer id);
	public List<AllUser> listUsers(boolean recycle);
	public boolean update(UserDAO  user);
	public boolean delete(Integer...  id);
	public List<AllUser> findByProperty(Object propertyName, String value);

}
