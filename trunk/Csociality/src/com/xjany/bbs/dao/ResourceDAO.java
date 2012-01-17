package com.xjany.bbs.dao;

import java.util.List;

import com.xjany.bbs.entity.AllResLibrary;
import com.xjany.common.page.Pagination;

public interface ResourceDAO extends GenericDAO<AllResLibrary, Integer> {
	public Pagination getPage(int pageNo, int pageSize);
	public AllResLibrary save(AllResLibrary allResLibrary,int parentId);
	public List<AllResLibrary> findByParentId(int parentId);
}
