package com.xjany.bbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.opensymphony.xwork2.validator.annotations.CustomValidator;

@Entity
public class Student {
	@Id
	@GeneratedValue
	private int stuId;
	private String stuName;

	public int getStuId() {
		return stuId;
	}

	public void setStuId(int stuId) {
		this.stuId = stuId;
	}
	@Column(length = 10)
	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
}