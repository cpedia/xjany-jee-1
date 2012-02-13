package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.common.page.Pagination;

public interface UserService {
	public List<AllUser> findAll();
	public AllUser findById(int id);
	public List<AllUser> findByProperty(Object propertyName, String value);
	public AllUser update(AllUser entity);
	public AllUser delete(AllUser entity);
	public AllUser save(AllUser entity,BbsUserProfile bbsUserProfile,AllUserGroup allUserGroup);
	public AllUser recycle(AllUser entity,boolean isRecycle);
	public AllUser delete(int id);
	public AllUser check(AllUser user);
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize);
}
