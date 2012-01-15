package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.common.page.Pagination;


public interface GroupService {
	public List findGroupBySql();
	public List<AllUserGroup> findById(int id );
	public Pagination getPage(int pageNo, int pageSize) ;
}