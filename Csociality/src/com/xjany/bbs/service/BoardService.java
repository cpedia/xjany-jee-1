package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsBoard;
import com.xjany.common.page.Pagination;


public interface BoardService {
	public BbsBoard findById(int id);
	public BbsBoard update(BbsBoard bbsBoard);
	public BbsBoard deleteById(int id);
	public List<BbsBoard> findAll();
	public BbsBoard save(BbsBoard bbsBoard);
	public Pagination getPage(int pageNo, int pageSize) ;
}
