package com.lti.rmi;

import java.io.Serializable;

public class ForumBean implements Serializable{
	private int id;
	private String name;
	private String description;
	private int categoryId;
	private boolean moderated;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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
