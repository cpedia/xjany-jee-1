package com.xjany.bbs.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsSubBoard;

/**
 * BbsSubBoard entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_sub_board", catalog = "cs_xjany")
public class BbsSubBoard extends AbstractBbsSubBoard implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsSubBoard() {
	}

	/** minimal constructor */
	public BbsSubBoard(String subName) {
		super(subName);
	}

	/** full constructor */
	public BbsSubBoard(BbsBoard bbsBoard, String subName, String subImg,
			String subInfo, String subMaster, Integer subTopicNum,
			Integer subReNum, String subLastUser, String subLastTopic,
			Integer cmsDel, Set<BbsTopic> bbsTopics) {
		super(bbsBoard, subName, subImg, subInfo, subMaster, subTopicNum,
				subReNum, subLastUser, subLastTopic, cmsDel, bbsTopics);
	}

}
