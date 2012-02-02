package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.common.page.Pagination;


public interface GroupService {
	public List<?> findGroupBySql();
	public AllUserGroup findById(int id);
	public AllUserGroup deleteById(int id);
	public List<AllUserGroup> findAll();
	public AllUserGroup save(AllUserGroup allUserGroup);
	public Pagination getPage(int pageNo, int pageSize) ;
}
