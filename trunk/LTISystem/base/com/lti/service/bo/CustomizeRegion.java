package com.lti.service.bo;
import java.io.Serializable;

import com.lti.service.bo.base.BaseCustomizeRegion;

public class CustomizeRegion extends BaseCustomizeRegion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String eastRegionName;
	
	private java.lang.String westRegionName;
	
	private java.lang.String southRegionName;
	
	private java.lang.String centerRegionName;
	
	private java.lang.String eastWidthStr;
	
	private java.lang.String westWidthStr;
	
	private java.lang.String southHeightStr;
	
	private java.lang.String centerWidthStr;
	
	public java.lang.String getEastRegionName() {
		return eastRegionName;
	}
	public void setEastRegionName(java.lang.String eastRegionName) {
		this.eastRegionName = eastRegionName;
	}
	public java.lang.String getWestRegionName() {
		return westRegionName;
	}
	public void setWestRegionName(java.lang.String westRegionName) {
		this.westRegionName = westRegionName;
	}
	public java.lang.String getSouthRegionName() {
		return southRegionName;
	}
	public void setSouthRegionName(java.lang.String southRegionName) {
		this.southRegionName = southRegionName;
	}
	public java.lang.String getCenterRegionName() {
		return centerRegionName;
	}
	public void setCenterRegionName(java.lang.String centerRegionName) {
		this.centerRegionName = centerRegionName;
	}
	public java.lang.String getEastWidthStr() {
		return eastWidthStr;
	}
	public void setEastWidthStr(java.lang.String eastWidthStr) {
		this.eastWidthStr = eastWidthStr;
	}
	public java.lang.String getWestWidthStr() {
		return westWidthStr;
	}
	public void setWestWidthStr(java.lang.String westWidthStr) {
		this.westWidthStr = westWidthStr;
	}
	public java.lang.String getSouthHeightStr() {
		return southHeightStr;
	}
	public void setSouthHeightStr(java.lang.String southHeightStr) {
		this.southHeightStr = southHeightStr;
	}
	public java.lang.String getCenterWidthStr() {
		return centerWidthStr;
	}
	public void setCenterWidthStr(java.lang.String centerWidthStr) {
		this.centerWidthStr = centerWidthStr;
	}
	
}