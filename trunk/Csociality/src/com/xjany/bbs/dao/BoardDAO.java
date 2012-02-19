package com.xjany.bbs.dao;

import com.xjany.bbs.entity.BbsBoard;
import com.xjany.common.page.Pagination;

public interface BoardDAO extends GenericDAO<BbsBoard, Integer> {
	public Pagination getPage(int pageNo, int pageSize);
}
