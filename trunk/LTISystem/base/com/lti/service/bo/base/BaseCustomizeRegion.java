package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseCustomizeRegion implements Serializable{
	protected java.lang.Long ID;
	protected java.lang.String PageName;
	protected java.lang.Long PageID;
	
	protected java.lang.Long East;
	protected java.lang.Long West;
	protected java.lang.Long Center;
	protected java.lang.Long South;
	
	protected java.lang.Long EastGroupID;
	protected java.lang.Long WestGroupID;
	protected java.lang.Long SouthGroupID;
	
	protected java.lang.String EastTitle;
	protected java.lang.String WestTitle;
	protected java.lang.String SouthTitle;
	protected java.lang.String CenterTitle;
	
	protected java.lang.Double EastWidth;
	protected java.lang.Double SouthHeight;
	protected java.lang.Double WestWidth;
	protected java.lang.Double CenterWidth;
	
	public java.lang.String getPageName() {
		return PageName;
	}
	public void setPageName(java.lang.String pageName) {
		PageName = pageName;
	}
	public java.lang.Long getPageID() {
		return PageID;
	}
	public void setPageID(java.lang.Long pageID) {
		PageID = pageID;
	}
	public java.lang.Long getEast() {
		return East;
	}
	public void setEast(java.lang.Long east) {
		East = east;
	}
	public java.lang.Long getWest() {
		return West;
	}
	public void setWest(java.lang.Long west) {
		West = west;
	}
	public java.lang.Long getCenter() {
		return Center;
	}
	public void setCenter(java.lang.Long center) {
		Center = center;
	}
	public java.lang.Long getSouth() {
		return South;
	}
	public void setSouth(java.lang.Long south) {
		South = south;
	}

	public java.lang.Long getEastGroupID() {
		return EastGroupID;
	}
	public void setEastGroupID(java.lang.Long eastGroupID) {
		EastGroupID = eastGroupID;
	}
	public java.lang.Long getWestGroupID() {
		return WestGroupID;
	}
	public void setWestGroupID(java.lang.Long westGroupID) {
		WestGroupID = westGroupID;
	}
	public java.lang.Long getSouthGroupID() {
		return SouthGroupID;
	}
	public void setSouthGroupID(java.lang.Long southGroupID) {
		SouthGroupID = southGroupID;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.String getEastTitle() {
		return EastTitle;
	}
	public void setEastTitle(java.lang.String eastTitle) {
		EastTitle = eastTitle;
	}
	public java.lang.String getWestTitle() {
		return WestTitle;
	}
	public void setWestTitle(java.lang.String westTitle) {
		WestTitle = westTitle;
	}
	public java.lang.String getSouthTitle() {
		return SouthTitle;
	}
	public void setSouthTitle(java.lang.String southTitle) {
		SouthTitle = southTitle;
	}
	public java.lang.String getCenterTitle() {
		return CenterTitle;
	}
	public void setCenterTitle(java.lang.String centerTitle) {
		CenterTitle = centerTitle;
	}
	public java.lang.Double getEastWidth() {
		return EastWidth;
	}
	public void setEastWidth(java.lang.Double eastWidth) {
		EastWidth = eastWidth;
	}
	public java.lang.Double getSouthHeight() {
		return SouthHeight;
	}
	public void setSouthHeight(java.lang.Double southHeight) {
		SouthHeight = southHeight;
	}
	public java.lang.Double getWestWidth() {
		return WestWidth;
	}
	public void setWestWidth(java.lang.Double westWidth) {
		WestWidth = westWidth;
	}
	public java.lang.Double getCenterWidth() {
		return CenterWidth;
	}
	public void setCenterWidth(java.lang.Double centerWidth) {
		CenterWidth = centerWidth;
	}
}