package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseProfile implements Serializable{

	protected static final long serialVersionUID = 1L;
	protected java.lang.Long PortfolioID=0l;
	protected java.lang.Long UserID;
	protected java.lang.Long PlanID=0l;
	protected java.lang.String PortfolioName="";
	protected java.lang.String UserName="";
	protected java.lang.String PlanName="";
	protected java.lang.Double RiskNumber=20.0;
	protected int YearsToRetire=40;
	protected java.lang.String Attitude="";
	protected java.lang.Boolean IsGenerated=false;
	protected java.util.Date UpdateTime=new Date();
	
	
	public java.lang.Boolean getIsGenerated() {
		return IsGenerated;
	}

	public void setIsGenerated(java.lang.Boolean isGenerated) {
		IsGenerated = isGenerated;
	}

	public java.lang.Long getPortfolioID() {
		return PortfolioID;
	}

	public void setPortfolioID(java.lang.Long portfolioID) {
		this.PortfolioID = portfolioID;
	}
	public java.lang.Long getUserID() {
		return UserID;
	}

	public void setUserID(java.lang.Long userID) {
		this.UserID = userID;
	}
	public java.lang.Long getPlanID() {
		return PlanID;
	}

	public void setPlanID(java.lang.Long planID) {
		this.PlanID = planID;
	}
	public java.lang.String getPortfolioName() {
		return PortfolioName;
	}

	public void setPortfolioName(java.lang.String portfolioName) {
		this.PortfolioName = portfolioName;
	}
	public java.lang.String getUserName() {
		return UserName;
	}

	public void setUserName(java.lang.String userName) {
		this.UserName = userName;
	}
	public java.lang.String getPlanName() {
		return PlanName;
	}

	public void setPlanName(java.lang.String planName) {
		this.PlanName = planName;
	}
	public java.lang.Double getRiskNumber() {
		return RiskNumber;
	}

	public void setRiskNumber(java.lang.Double riskNumber) {
		this.RiskNumber = riskNumber;
	}
	public int getYearsToRetire() {
		return YearsToRetire;
	}

	public void setYearsToRetire(int yearsToRetire) {
		this.YearsToRetire = yearsToRetire;
	}
	public java.lang.String getAttitude() {
		return Attitude;
	}

	public void setAttitude(java.lang.String attitude) {
		this.Attitude = attitude;
	}

	public java.util.Date getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.UpdateTime = updateTime;
	}
}