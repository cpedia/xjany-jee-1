package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.service.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService{
	UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}
	@Resource
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
			String... value) {
		return userDAO.check(entity, propertyName, com.xjany.common.util.MyMD5Util.MD5(value[0]));
	}

	public boolean update(AllUser entity) {
		return userDAO.update(entity);
	}

	public boolean delete(AllUser entity) {
		return userDAO.delete(entity);
	}

	public boolean save(AllUser entity) {
		entity.setUserPsw(com.xjany.common.util.MyMD5Util.MD5(entity.getUserPsw()));
		return userDAO.save(entity);
	}
}
