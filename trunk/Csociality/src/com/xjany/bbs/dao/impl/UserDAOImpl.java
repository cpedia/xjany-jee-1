package com.xjany.bbs.dao.impl;

import java.util.List;

import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;

public class UserDAOImpl extends GeneriDAOImpl<AllUser,Integer> implements UserDAO
{

	@Override
	public boolean save(UserDAO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean recycle(UserDAO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AllUser findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AllUser> listUsers(boolean recycle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserDAO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Integer... id) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
