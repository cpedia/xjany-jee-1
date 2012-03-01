package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.BoardDAO;
import com.xjany.bbs.dao.GroupDAO;
import com.xjany.bbs.dao.SubBoardDAO;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsBoard;
import com.xjany.bbs.service.BoardService;
import com.xjany.bbs.service.GroupService;
import com.xjany.common.exception.DaoException;
import com.xjany.common.page.Pagination;
@Service
@Transactional
public class BoardServiceImpl implements BoardService {
	
	private BoardDAO boardDAO;
	private SubBoardDAO subBoardDAO;

	public BoardDAO getBoardDAO() {
		return boardDAO;
	}
	@Resource
	public void setBoardDAO(BoardDAO boardDAO) {
		this.boardDAO = boardDAO;
	}
	
	public SubBoardDAO getSubBoardDAO() {
		return subBoardDAO;
	}
	
	@Resource
	public void setSubBoardDAO(SubBoardDAO subBoardDAO) {
		this.subBoardDAO = subBoardDAO;
	}
	@Override
	public BbsBoard findById(int id) {
		return boardDAO.findById(id);
	}
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination pagination = boardDAO.getPage(pageNo, pageSize);
		return pagination;
	}
	@Override
	public BbsBoard save(BbsBoard bbsBoard) {
		return boardDAO.save(bbsBoard);
	}
	@Override
	public List<BbsBoard> findAll() {
		return boardDAO.findAll();
	}
	@Override
	public BbsBoard deleteById(int id){
		boardDAO.delete(id);
		return null;
	}
	@Override
	public BbsBoard update(BbsBoard bbsBoard) {
		boardDAO.update(bbsBoard);
		return null;
	}

}
