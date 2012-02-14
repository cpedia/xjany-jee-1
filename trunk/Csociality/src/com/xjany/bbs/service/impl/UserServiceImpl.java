package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.BbsUserProfileDAO;
import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.bbs.service.UserService;
import com.xjany.common.MD5Util;
import com.xjany.common.page.Pagination;
import com.xjany.common.util.MD5UtilImpl;
import com.xjany.common.util.XjanyMap;
import com.xjany.common.util.XjanyMapImpl;
import com.xjany.common.web.session.SessionProvider;

import static com.xjany.common.frame.XjanyConstants.SESSIONNAME;
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

	public AllUser recycle(AllUser entity, boolean isRecycle) {
		return userDAO.recycle(entity, isRecycle);
	}

	public AllUser delete(int id) {
		AllUser allUser = userDAO.findById(id);
		bbsUserProfileDAO.delete(allUser.getBbsUserProfile()); //删除allUser之前先删除bbsUserProfile
		return userDAO.delete(allUser);
	}

	public AllUser check(AllUser user,SessionProvider session,HttpServletRequest request, HttpServletResponse response) {
		XjanyMap<String, String> property = new XjanyMapImpl<String, String>();
		if(!"".equals(user.getUserEmail()) && user.getUserEmail() != null)
		{
			property.put("userEmail", user.getUserEmail());
		} if(!"".equals(user.getUserName()) && user.getUserName() != null)
		{
			property.put("userName", user.getUserName());
			if(!"".equals(user.getUserPsw()) && user.getUserPsw() != null)
				property.put("userPsw", md5.encryption(user.getUserPsw(),user.getUserName()));
		} 
		AllUser allUser = userDAO.check(user, property);
		if(allUser!=null){
			session.setAttribute(request, response, SESSIONNAME, allUser.getUserId());
		}
		return userDAO.check(user, property);
	}

	public AllUser update(AllUser entity) {
		return userDAO.update(entity);
	}

	public AllUser delete(AllUser entity) {
		return userDAO.delete(entity);
	}

	public AllUser save(AllUser entity,BbsUserProfile bbsUserProfile,AllUserGroup allUserGroup) {
		entity.setUserPsw(md5.encryption(entity.getUserPsw(),entity.getUserName()));
		BbsUserProfile bup = bbsUserProfileDAO.save(bbsUserProfile);
		entity.setBbsUserProfile(bup);
		if(allUserGroup!=null)
			entity.setAllUserGroup(allUserGroup);
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
