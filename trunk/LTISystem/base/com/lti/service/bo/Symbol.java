package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseSymbol;

public class Symbol extends BaseSymbol implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int topicReplies;

	private String postText;
	
	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public int getTopicReplies() {
		return topicReplies;
	}

	public void setTopicReplies(int topicReplies) {
		this.topicReplies = topicReplies;
	}
	
}
