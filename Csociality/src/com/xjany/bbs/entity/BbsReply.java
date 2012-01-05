package com.xjany.bbs.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsReply;

/**
 * BbsReply entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_reply", catalog = "cs_xjany")
public class BbsReply extends AbstractBbsReply implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsReply() {
	}

	/** minimal constructor */
	public BbsReply(BbsTopic bbsTopic, String repUser, String repContent,
			Timestamp repTime, String repIp) {
		super(bbsTopic, repUser, repContent, repTime, repIp);
	}

	/** full constructor */
	public BbsReply(BbsTopic bbsTopic, String repUser, String repContent,
			Timestamp repTime, String repIp, Integer cmsDel) {
		super(bbsTopic, repUser, repContent, repTime, repIp, cmsDel);
	}

}
