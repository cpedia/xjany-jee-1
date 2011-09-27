package com.lti.service.bo.base;

import java.io.Serializable;


public abstract class BaseJforumUser implements Serializable {
	private static final long serialVersionUID = 234131L;
	private Integer id;
	private String username;
	private String email;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
