package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseSecurityType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Created by Tomara 2008-11-14 19:47 
	 */
	
	protected java.lang.Integer ID;
	
	protected java.lang.String Name;
	
	protected java.lang.String Description;
	
	
	public Integer getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
	}
	public java.lang.String getName() {
		return Name;
	}
	public void setName(java.lang.String name) {
		Name = name;
	}
	public java.lang.String getDescription() {
		return Description;
	}
	public void setDescription(java.lang.String description) {
		Description = description;
	}

}
