package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseCustomizePage implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.String Name;
	
	protected java.lang.String Code;
	
	protected java.lang.String Template;
	
	protected java.lang.String Functions;

	
	
	public java.lang.String getFunctions() {
		return Functions;
	}

	public void setFunctions(java.lang.String functions) {
		Functions = functions;
	}

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

	public java.lang.String getCode() {
		return Code;
	}

	public void setCode(java.lang.String code) {
		Code = code;
	}

	public java.lang.String getTemplate() {
		return Template;
	}

	public void setTemplate(java.lang.String template) {
		Template = template;
	}




}