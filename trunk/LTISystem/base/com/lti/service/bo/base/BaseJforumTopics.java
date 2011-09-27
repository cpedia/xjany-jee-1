package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseJforumTopics implements Serializable {
	private java.lang.Integer ID;
	private java.lang.Integer ForumId;
	private java.lang.String TopicTitle;
	private java.lang.Integer UserId;
	private java.sql.Timestamp TopicTime;
	private java.lang.Integer TopicViews;
	private java.lang.Integer TopicReplies;
	private java.lang.Integer TopicStatus;
	private java.lang.Integer TopicVoteId;
	private java.lang.Integer TopicType;
	private java.lang.Integer TopicFirstPostId;
	private java.lang.Integer TopicLastPostId;
	private java.lang.Integer TopicMovedId;
	private java.lang.Integer Moderated;
	private java.lang.String PlanId;
	private boolean NeedModerate;
	
	public boolean isNeedModerate() {
		return NeedModerate;
	}
	public void setNeedModerate(boolean needModerate) {
		NeedModerate = needModerate;
	}
	public java.sql.Timestamp getTopicTime() {
		return TopicTime;
	}
	public void setTopicTime(java.sql.Timestamp topicTime) {
		TopicTime = topicTime;
	}
	public java.lang.Integer getID() {
		return ID;
	}
	public void setID(java.lang.Integer iD) {
		ID = iD;
	}
	public java.lang.Integer getForumId() {
		return ForumId;
	}
	public void setForumId(java.lang.Integer forumId) {
		ForumId = forumId;
	}
	public java.lang.String getTopicTitle() {
		return TopicTitle;
	}
	public void setTopicTitle(java.lang.String topicTitle) {
		TopicTitle = topicTitle;
	}
	public java.lang.Integer getUserId() {
		return UserId;
	}
	public void setUserId(java.lang.Integer userId) {
		UserId = userId;
	}

	public java.lang.Integer getTopicViews() {
		return TopicViews;
	}
	public void setTopicViews(java.lang.Integer topicViews) {
		TopicViews = topicViews;
	}
	public java.lang.Integer getTopicReplies() {
		return TopicReplies;
	}
	public void setTopicReplies(java.lang.Integer topicReplies) {
		TopicReplies = topicReplies;
	}
	public java.lang.Integer getTopicStatus() {
		return TopicStatus;
	}
	public void setTopicStatus(java.lang.Integer topicStatus) {
		TopicStatus = topicStatus;
	}
	public java.lang.Integer getTopicVoteId() {
		return TopicVoteId;
	}
	public void setTopicVoteId(java.lang.Integer topicVoteId) {
		TopicVoteId = topicVoteId;
	}
	public java.lang.Integer getTopicType() {
		return TopicType;
	}
	public void setTopicType(java.lang.Integer topicType) {
		TopicType = topicType;
	}
	public java.lang.Integer getTopicFirstPostId() {
		return TopicFirstPostId;
	}
	public void setTopicFirstPostId(java.lang.Integer topicFirstPostId) {
		TopicFirstPostId = topicFirstPostId;
	}
	public java.lang.Integer getTopicLastPostId() {
		return TopicLastPostId;
	}
	public void setTopicLastPostId(java.lang.Integer topicLastPostId) {
		TopicLastPostId = topicLastPostId;
	}
	public java.lang.Integer getTopicMovedId() {
		return TopicMovedId;
	}
	public void setTopicMovedId(java.lang.Integer topicMovedId) {
		TopicMovedId = topicMovedId;
	}
	public java.lang.Integer getModerated() {
		return Moderated;
	}
	public void setModerated(java.lang.Integer moderated) {
		Moderated = moderated;
	}
	public java.lang.String getPlanId() {
		return PlanId;
	}
	public void setPlanId(java.lang.String planId) {
		PlanId = planId;
	}

}
