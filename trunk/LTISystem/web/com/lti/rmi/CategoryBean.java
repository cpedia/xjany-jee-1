package com.lti.rmi;

import java.io.Serializable;

public class CategoryBean implements Serializable{
	private int id;
	private String name;
	private boolean moderated;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isModerated() {
		return moderated;
	}
	public void setModerated(boolean moderated) {
		this.moderated = moderated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
