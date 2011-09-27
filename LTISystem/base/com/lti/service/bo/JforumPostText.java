package com.lti.service.bo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.lti.service.bo.base.BaseJforumPostText;

public class JforumPostText extends BaseJforumPostText implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int postReplies;
	
	private Timestamp topicPostTime;
	
	private int topicId;

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getPostReplies() {
		return postReplies;
	}

	public void setPostReplies(int postReplies) {
		this.postReplies = postReplies;
	}

	public Timestamp getTopicPostTime() {
		return topicPostTime;
	}

	public void setTopicPostTime(Timestamp topicPostTime) {
		this.topicPostTime = topicPostTime;
	}

}
