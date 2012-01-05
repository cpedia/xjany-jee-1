package com.xjany.bbs.entity;

import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsTopic;

/**
 * BbsTopic entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_topic", catalog = "cs_xjany")
public class BbsTopic extends AbstractBbsTopic implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsTopic() {
	}

	/** minimal constructor */
	public BbsTopic(BbsSubBoard bbsSubBoard, String topicTitle,
			String topicContent, Timestamp topicTime, Timestamp topicLastTime) {
		super(bbsSubBoard, topicTitle, topicContent, topicTime, topicLastTime);
	}

	/** full constructor */
	public BbsTopic(BbsSubBoard bbsSubBoard, String topicTitle,
			String topicContent, String topicUser, String topicIp,
			Timestamp topicTime, Integer topicHits, Integer topicReply,
			Integer isNews, Integer topicElite, Integer topicTop,
			String topicLastUser, Timestamp topicLastTime, Integer cmsDel,
			Set<BbsReply> bbsReplies) {
		super(bbsSubBoard, topicTitle, topicContent, topicUser, topicIp,
				topicTime, topicHits, topicReply, isNews, topicElite, topicTop,
				topicLastUser, topicLastTime, cmsDel, bbsReplies);
	}

}
