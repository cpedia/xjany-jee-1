package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseJforumTopics;

public class JforumTopics extends BaseJforumTopics implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String postText;

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}
	
}
