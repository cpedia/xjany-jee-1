package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseJforumPostText implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Integer ID;
	private java.lang.String PostText;
	private java.lang.String PostSubject;
	
	
	public java.lang.Integer getID() {
		return ID;
	}
	public void setID(java.lang.Integer iD) {
		ID = iD;
	}
	public java.lang.String getPostText() {
		return PostText;
	}
	public void setPostText(java.lang.String postText) {
		PostText = postText;
	}
	public java.lang.String getPostSubject() {
		return PostSubject;
	}
	public void setPostSubject(java.lang.String postSubject) {
		PostSubject = postSubject;
	}

}
