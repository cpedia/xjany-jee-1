package com.xjany.bbs.dao;

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.common.page.Pagination;

public interface GroupDAO extends GenericDAO<AllUserGroup, Integer> {
	public Pagination getPage(int pageNo, int pageSize);
}
