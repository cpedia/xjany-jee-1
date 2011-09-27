package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseProfile;


public class Profile extends BaseProfile implements Serializable{
	private static final long serialVersionUID = 1L;

	private Boolean isEMailAlert=false;

	public Boolean getIsEMailAlert() {
		return isEMailAlert;
	}

	public void setIsEMailAlert(Boolean isEMailAlert) {
		this.isEMailAlert = isEMailAlert;
	}
}
