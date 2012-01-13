package com.xjany.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.xjany.bbs.dao.GroupDAO;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.common.page.Finder;
import com.xjany.common.page.Pagination;

@Repository
public class GroupDAOImpl extends GeneriDAOImpl<AllUserGroup, Integer> implements GroupDAO {
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from AllUserGroup bean");
		f.append(" where 1=1");
		f.append(" order by bean.groupId desc");
		return find(f, pageNo, pageSize);
	}
}
