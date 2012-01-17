package com.xjany.bbs.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.GroupDAO;
import com.xjany.bbs.dao.ResourceDAO;
import com.xjany.bbs.entity.AllResLibrary;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.File;
import com.xjany.common.page.Finder;
import com.xjany.common.page.Pagination;

@Repository
public class ResourceDAOImpl extends GeneriDAOImpl<AllResLibrary, Integer> implements ResourceDAO {
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from AllResLibrary bean");
		f.append(" where 1=1");
		f.append(" order by bean.groupId desc");
		return find(f, pageNo, pageSize);
	}
	
	public AllResLibrary save(AllResLibrary allResLibrary,int parentId) {
		Session session = sessionFactory.getCurrentSession();
		allResLibrary.setParentId(parentId);
		session.save(allResLibrary);
		return allResLibrary;
	}
	
	public List<AllResLibrary> findByParentId(int parentId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery( "from AllResLibrary as bean where bean.parentId= ?");
		query.setParameter(0, parentId);
		List<AllResLibrary> list = query.list();
		return list;
	}
}
