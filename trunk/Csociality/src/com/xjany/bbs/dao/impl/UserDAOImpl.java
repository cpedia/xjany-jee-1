package com.xjany.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;
@Repository
public class UserDAOImpl extends GeneriDAOImpl<AllUser,Integer> implements UserDAO
{
	
}
