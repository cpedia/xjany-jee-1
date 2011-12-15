package com.xjany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class File {
	private Integer id;
	private Integer upId;
	private Integer downId;
	private String name;
	private String path;
	@Id
	@GeneratedValue
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	@Column(length = 10)
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	@Column(length = 10)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column(length = 10)
	public Integer getUpId() {
		return upId;
	}
	public void setUpId(Integer upId) {
		this.upId = upId;
	}
	@Column(length = 10)
	public Integer getDownId() {
		return downId;
	}
	public void setDownId(Integer downId) {
		this.downId = downId;
	}
}
