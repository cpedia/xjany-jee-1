package com.xjany.bbs.service;

import java.util.List;
import java.util.Map;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.common.page.Pagination;

public interface UserService {
	public List<AllUser> findAll();
	public AllUser findById(int id);
	public List<AllUser> findByProperty(Object propertyName, String value);
	public boolean update(AllUser entity);
	public boolean delete(AllUser entity);
	public int save(AllUser entity,BbsUserProfile bbsUserProfile);
	public boolean recycle(AllUser entity,boolean isRecycle);
	public boolean delete(int id);
	public boolean check(AllUser user);
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize);
}
