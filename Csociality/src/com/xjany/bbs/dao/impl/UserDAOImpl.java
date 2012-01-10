package com.xjany.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUser;
import com.xjany.common.page.Finder;
import com.xjany.common.page.Pagination;
@Repository
public class UserDAOImpl extends GeneriDAOImpl<AllUser,Integer> implements UserDAO
{
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from AllUser bean");
		f.append(" where 1=1");
		/*if (groupId != null) {
			f.append(" and bean.group.id=:groupId");
			f.setParam("groupId", groupId);
		}
		if (disabled != null) {
			f.append(" and bean.disabled=:disabled");
			f.setParam("disabled", disabled);
		}
		if (admin != null) {
			f.append(" and bean.admin=:admin");
			f.setParam("admin", admin);
		}
		if (rank != null) {
			f.append(" and bean.rank<=:rank");
			f.setParam("rank", rank);
		}*/
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}
}
