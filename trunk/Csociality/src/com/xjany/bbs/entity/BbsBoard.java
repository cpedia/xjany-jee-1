package com.xjany.bbs.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsBoard;

/**
 * BbsBoard entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_board", catalog = "cs_xjany")
public class BbsBoard extends AbstractBbsBoard implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsBoard() {
	}

	/** minimal constructor */
	public BbsBoard(String boaName) {
		super(boaName);
	}

	/** full constructor */
	public BbsBoard(String boaName, String boaMaster, Integer cmsDel,
			Set<BbsSubBoard> bbsSubBoards) {
		super(boaName, boaMaster, cmsDel, bbsSubBoards);
	}
	
	@Override
	public void recycle(boolean isRecycle) {
		// TODO Auto-generated method stub
		if(isRecycle)
			this.setCmsDel(1);
		else 
			this.setCmsDel(0);
	}
}
