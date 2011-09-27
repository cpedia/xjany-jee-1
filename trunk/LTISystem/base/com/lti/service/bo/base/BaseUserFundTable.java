package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseUserFundTable implements Serializable{
	
	protected java.lang.Long ID;

	protected java.lang.Long UserID;
	
	protected java.lang.Long PlanID;
	
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
	public java.lang.Long getPlanID() {
		return PlanID;
	}
	public void setPlanID(java.lang.Long planID) {
		PlanID = planID;
	}

}
