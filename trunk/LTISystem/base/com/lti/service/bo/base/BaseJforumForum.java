package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseJforumForum implements Serializable{

	private java.lang.Integer ID;
	private java.lang.Integer CategoriesId;
	private java.lang.String ForumName;
	private java.lang.String ForumDesc;
	private java.lang.Integer ForumOrder;
	private java.lang.Integer ForumTopics;
	private java.lang.Integer ForumLastPostId;
	private java.lang.Integer Moderated;
	
	public java.lang.Integer getID() {
		return ID;
	}
	public void setID(java.lang.Integer iD) {
		ID = iD;
	}
	public java.lang.Integer getCategoriesId() {
		return CategoriesId;
	}
	public void setCategoriesId(java.lang.Integer categoriesId) {
		CategoriesId = categoriesId;
	}
	public java.lang.String getForumDesc() {
		return ForumDesc;
	}
	public void setForumDesc(java.lang.String forumDesc) {
		ForumDesc = forumDesc;
	}
	public java.lang.Integer getForumOrder() {
		return ForumOrder;
	}
	public void setForumOrder(java.lang.Integer forumOrder) {
		ForumOrder = forumOrder;
	}
	public java.lang.Integer getForumTopics() {
		return ForumTopics;
	}
	public void setForumTopics(java.lang.Integer forumTopics) {
		ForumTopics = forumTopics;
	}
	public java.lang.Integer getForumLastPostId() {
		return ForumLastPostId;
	}
	public void setForumLastPostId(java.lang.Integer forumLastPostId) {
		ForumLastPostId = forumLastPostId;
	}
	public java.lang.Integer getModerated() {
		return Moderated;
	}
	public void setModerated(java.lang.Integer moderated) {
		Moderated = moderated;
	}
	public String getForumName() {
		return ForumName;
	}
	public void setForumName(java.lang.String forumName) {
		ForumName = forumName;
	}
	
}
