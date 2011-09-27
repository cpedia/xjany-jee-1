package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseWidgetUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected java.lang.Long ID;

	public java.lang.Long getID()
	{
		return ID;
	}

	public void setID(java.lang.Long iD)
	{
		ID = iD;
	}
	
}