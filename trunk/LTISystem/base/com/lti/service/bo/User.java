package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseUser;

public class User extends BaseUser implements Serializable ,Cloneable{

	private static final long serialVersionUID = 1L;
	
	public User clone(){
		try {
			return (User) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Cannot clone this user"+super.getUserName()+".",e);
		}
	}

	
}