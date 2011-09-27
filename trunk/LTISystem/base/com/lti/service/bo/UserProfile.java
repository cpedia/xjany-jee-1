package com.lti.service.bo;

import java.io.Serializable;
import java.util.Date;

import com.lti.service.bo.base.BaseUserProfile;

public class UserProfile extends BaseUserProfile implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private int validtime;
	private int timeperiod;
	private int timeout;
	
	public int getTimeperiod() {
		return timeperiod;
	}

	public void setTimeperiod(int timeperiod) {
		this.timeperiod = timeperiod;
	}

	public int getValidtime() {
		return validtime;
	}

	public void setValidtime(int validtime) {
		this.validtime = validtime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
