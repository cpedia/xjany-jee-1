package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseFundAlert implements Serializable{

	protected java.lang.Long ID;
	protected java.util.Date Date;
	protected java.lang.Long SecurityID;
	protected java.lang.Double PointType;
	protected java.lang.Double DR;
	protected java.lang.Double Mean;
	protected java.lang.Double STD;
	
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
	}
	public java.lang.Long getSecurityID() {
		return SecurityID;
	}
	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}
	public java.lang.Double getPointType() {
		return PointType;
	}
	public void setPointType(java.lang.Double pointType) {
		PointType = pointType;
	}
	public java.lang.Double getDR() {
		return DR;
	}
	public void setDR(java.lang.Double dr) {
		DR = dr;
	}
	public java.lang.Double getMean() {
		return Mean;
	}
	public void setMean(java.lang.Double mean) {
		Mean = mean;
	}
	public java.lang.Double getSTD() {
		return STD;
	}
	public void setSTD(java.lang.Double std) {
		STD = std;
	}
	
}
