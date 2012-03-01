package com.xjany.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.xjany.bbs.dao.SubBoardDAO;
import com.xjany.bbs.entity.BbsSubBoard;
import com.xjany.common.page.Finder;
import com.xjany.common.page.Pagination;

@Repository
public class SubBoardDAOImpl extends GeneriDAOImpl<BbsSubBoard, Integer> implements SubBoardDAO {
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BbsBoard bean");
		f.append(" where 1=1");
		f.append(" order by bean.boaId desc");
		return find(f, pageNo, pageSize);
	}
}
