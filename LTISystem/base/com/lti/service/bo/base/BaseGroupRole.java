package com.lti.service.bo.base;

public class BaseGroupRole {
	private java.lang.Long ID;
	private java.lang.Long GroupID;
	private java.lang.Long RoleID;
	private java.lang.Long ResourceID;
	private java.lang.Integer ResourceType;
	private java.lang.String ResourceName;
	private java.lang.String ResourceString;
	
	
	public java.lang.String getResourceName() {
		return ResourceName;
	}
	public void setResourceName(java.lang.String resourceName) {
		ResourceName = resourceName;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.Long getGroupID() {
		return GroupID;
	}
	public void setGroupID(java.lang.Long groupID) {
		GroupID = groupID;
	}
	public java.lang.Long getRoleID() {
		return RoleID;
	}
	public void setRoleID(java.lang.Long roleID) {
		RoleID = roleID;
	}
	public java.lang.Long getResourceID() {
		return ResourceID;
	}
	public void setResourceID(java.lang.Long resourceID) {
		ResourceID = resourceID;
	}
	public java.lang.String getResourceString() {
		return ResourceString;
	}
	public void setResourceString(java.lang.String resourceString) {
		ResourceString = resourceString;
	}
	public java.lang.Integer getResourceType() {
		return ResourceType;
	}
	public void setResourceType(java.lang.Integer resourceType) {
		ResourceType = resourceType;
	}

}
