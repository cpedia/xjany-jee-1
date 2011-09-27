package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseWidgetUser;

public  class WidgetUser extends BaseWidgetUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String website;
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getWebsite()
	{
		return website;
	}
	public void setWebsite(String website)
	{
		this.website = website;
	}

	
}