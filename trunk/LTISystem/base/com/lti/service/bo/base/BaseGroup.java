package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseGroup implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.String Name;
	
	protected java.lang.String Description;
	
	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
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