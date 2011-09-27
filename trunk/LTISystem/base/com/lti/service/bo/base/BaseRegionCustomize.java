package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseRegionCustomize implements Serializable{
	protected java.lang.Long ID;
	protected java.lang.String PageName;
	protected java.lang.String RegionName;
	protected java.lang.String RegionContent;
	protected java.lang.Long GroupID;
	
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.String getPageName() {
		return PageName;
	}
	public void setPageName(java.lang.String pageName) {
		PageName = pageName;
	}
	public java.lang.String getRegionName() {
		return RegionName;
	}
	public void setRegionName(java.lang.String regionName) {
		RegionName = regionName;
	}
	public java.lang.String getRegionContent() {
		return RegionContent;
	}
	public void setRegionContent(java.lang.String regionContent) {
		RegionContent = regionContent;
	}
	public java.lang.Long getGroupID() {
		return GroupID;
	}
	public void setGroupID(java.lang.Long groupID) {
		GroupID = groupID;
	}
	
}