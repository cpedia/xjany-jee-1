package com.xjany.bbs.dao;

import com.xjany.bbs.entity.AllUser;
import com.xjany.common.page.Pagination;

public interface UserDAO extends GenericDAO<AllUser,Integer>
{
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize);
}
