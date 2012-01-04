package com.xjany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class District
{
	private Integer id;
	private String name;
	private String province;
	private int remove;
	
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
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	@Column(length = 4)
	public int getRemove() {
		return remove;
	}
	public void setRemove(int remove) {
		this.remove = remove;
	}
}
