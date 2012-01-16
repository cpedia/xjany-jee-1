package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.BbsUserProfileDAO;
import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.bbs.service.UserService;
import com.xjany.common.MD5Util;
import com.xjany.common.page.Pagination;
import com.xjany.common.util.MD5UtilImpl;
import com.xjany.common.util.XjanyMap;
import com.xjany.common.util.XjanyMapImpl;
@Service
@Transactional
public class UserServiceImpl implements UserService{
	private UserDAO userDAO;
	private MD5Util md5 = new MD5UtilImpl();
	public UserDAO getUserDAO() {
		return userDAO;
	}
	@Resource
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	BbsUserProfileDAO bbsUserProfileDAO;
	
	public BbsUserProfileDAO getBbsUserProfileDAO() {
		return bbsUserProfileDAO;
	}
	@Resource
	public void setBbsUserProfileDAO(BbsUserProfileDAO bbsUserProfileDAO) {
		this.bbsUserProfileDAO = bbsUserProfileDAO;
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

	public boolean delete(int id) {
		AllUser allUser = userDAO.findById(id);
		bbsUserProfileDAO.delete(allUser.getBbsUserProfile()); //删除allUser之前先删除bbsUserProfile
		return userDAO.delete(allUser);
	}

	public boolean check(AllUser user) {
		XjanyMap<String, String> property = new XjanyMapImpl<String, String>();
		if(!"".equals(user.getUserEmail()) && user.getUserEmail() != null)
		{
			property.put("userEmail", user.getUserEmail());
		} else if(!"".equals(user.getUserName()) && user.getUserName() != null)
		{
			property.put("userName", user.getUserName());
			if(!"".equals(user.getUserPsw()) && user.getUserPsw() != null)
				property.put("userPsw", md5.encryption(user.getUserPsw(),user.getUserName()));
		} 
		return userDAO.check(user, property);
	}

	public boolean update(AllUser entity) {
		return userDAO.update(entity);
	}

	public boolean delete(AllUser entity) {
		return userDAO.delete(entity);
	}

	public int save(AllUser entity,BbsUserProfile bbsUserProfile) {
		entity.setUserPsw(md5.encryption(entity.getUserPsw(),entity.getUserName()));
		int d = bbsUserProfileDAO.save(bbsUserProfile);
		BbsUserProfile bup = bbsUserProfileDAO.findById(d);
		entity.setBbsUserProfile(bup);
		return userDAO.save(entity);
	}
	
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize){
		Pagination page = userDAO.getPage(username, email, siteId, groupId,
				disabled, admin, rank, pageNo, pageSize);
		return page;
	}
}
