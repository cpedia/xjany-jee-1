package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseGroupUser implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.Long UserID;
	
	protected java.lang.Long GroupID;
	
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.Long getUserID() {
		return UserID;
	}
	public void setUserID(java.lang.Long userID) {
		UserID = userID;
	}
	public java.lang.Long getGroupID() {
		return GroupID;
	}
	public void setGroupID(java.lang.Long groupID) {
		GroupID = groupID;
	}



}