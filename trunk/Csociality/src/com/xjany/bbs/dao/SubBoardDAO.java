package com.xjany.bbs.dao;

import com.xjany.bbs.entity.BbsSubBoard;
import com.xjany.common.page.Pagination;

public interface SubBoardDAO extends GenericDAO<BbsSubBoard, Integer> {
	public Pagination getPage(int pageNo, int pageSize);
}
