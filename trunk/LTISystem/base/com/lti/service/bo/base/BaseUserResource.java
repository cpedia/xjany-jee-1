package com.lti.service.bo.base;

import java.util.Date;

import com.lti.service.bo.User;


public class BaseUserResource implements java.io.Serializable {

	// Fields

	private long id;
	
	private Long userID;
	private Integer resourceType;
	private long resourceId;
	private String resourceName;
	private int resourceCount = 1;
	private Date updateTime;
	private Date expiredTime;
	private Long relationID = null;

	// Property accessors

	public Long getRelationID() {
		return relationID;
	}

	public void setRelationID(Long relationID) {
		this.relationID = relationID;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Integer getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	public long getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(int resourceCount) {
		this.resourceCount = resourceCount;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

}