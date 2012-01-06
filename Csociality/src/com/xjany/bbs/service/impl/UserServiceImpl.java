package com.xjany.bbs.service.impl;

import java.util.List;

import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.service.UserService;

public class UserServiceImpl implements UserService{
	UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public List<AllUser> findAll() {
		return userDAO.findAll();
	}

	public AllUser findById(int id) {
		return userDAO.findById(id);
	}

	public List<AllUser> findByProperty(Object propertyName, String value) {
		return userDAO.findByProperty(propertyName, value);
	}

	public boolean recycle(AllUser entity, boolean isRecycle) {
		return userDAO.recycle(entity, isRecycle);
	}

	public boolean delete(int... id) {
		return userDAO.delete(id);
	}

	public boolean check(AllUser entity, List<AllUser> propertyName,
			String[] value) {
		return userDAO.check(entity, propertyName, value);
	}

	public boolean update(AllUser entity) {
		return userDAO.update(entity);
	}

	public boolean delete(AllUser entity) {
		return userDAO.delete(entity);
	}

	public boolean save(AllUser entity) {
		return userDAO.save(entity);
	}
	
	
}
