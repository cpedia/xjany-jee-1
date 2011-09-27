package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseSystemLog implements Serializable{

	protected java.lang.Long ID;

	protected java.util.Date date;
	
	protected java.lang.String thread;
	protected java.lang.String level;
	
	protected java.lang.String classInf;
	
	
	protected java.lang.String message;


	public java.lang.Long getID() {
		return ID;
	}


	public void setID(java.lang.Long id) {
		ID = id;
	}


	public java.util.Date getDate() {
		return date;
	}


	public void setDate(java.util.Date date) {
		this.date = date;
	}


	public java.lang.String getThread() {
		return thread;
	}


	public void setThread(java.lang.String thread) {
		this.thread = thread;
	}


	public java.lang.String getLevel() {
		return level;
	}


	public void setLevel(java.lang.String level) {
		this.level = level;
	}


	public java.lang.String getClassInf() {
		return classInf;
	}


	public void setClassInf(java.lang.String classInf) {
		this.classInf = classInf;
	}


	public java.lang.String getMessage() {
		return message;
	}


	public void setMessage(java.lang.String message) {
		this.message = message;
	}
	



}