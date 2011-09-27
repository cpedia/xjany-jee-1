/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public class BasePlanScore implements Serializable{
	protected static final long serialVersionUID = 1L;
	protected java.lang.Long ID;
	protected java.lang.Long PlanID;
	protected java.lang.String PlanName;
	protected java.util.Date EndDate;
	protected java.lang.Double SAAScore = 0.0;
	protected java.lang.Double TAAScore = 0.0;
	protected java.lang.Double CoverageScore = 0.0;
	protected java.lang.Double FundQualityScore = 0.0;
	protected java.lang.Double CapabilityScore = 0.0;
	protected java.lang.Double InvestmentScore = 0.0;
	protected java.lang.Double TAAReturn=0.0;
	protected java.lang.Double SAAReturn=0.0;
	protected java.lang.Integer Status=0;
	protected java.lang.Double FundQualityValue = 0.0;
	protected java.lang.Double CoverageValue = 0.0;
	protected java.lang.Double CapabilityValue = 0.0;
	protected java.lang.Double InvestmentValue = 0.0;
	protected java.lang.Integer MajorAssetClass = 0;
	
	public java.lang.Double getTAAReturn() {
		return TAAReturn;
	}
	public void setTAAReturn(java.lang.Double tAAReturn) {
		TAAReturn = tAAReturn;
	}
	public java.lang.Double getSAAReturn() {
		return SAAReturn;
	}
	public void setSAAReturn(java.lang.Double sAAReturn) {
		SAAReturn = sAAReturn;
	}
	public java.lang.Double getInvestmentScore() {
		return InvestmentScore;
	}
	public void setInvestmentScore(java.lang.Double investmentScore) {
		this.InvestmentScore = investmentScore;
	}
	public java.lang.Double getCapabilityScore() {
		return CapabilityScore;
	}
	public void setCapabilityScore(java.lang.Double capabilityScore) {
		this.CapabilityScore = capabilityScore;
	}
	public java.lang.Double getFundQualityScore() {
		return FundQualityScore;
	}
	public void setFundQualityScore(java.lang.Double fundQualityScore) {
		this.FundQualityScore = fundQualityScore;
	}
	public java.lang.Double getCoverageScore() {
		return CoverageScore;
	}
	public void setCoverageScore(java.lang.Double coverageScore) {
		this.CoverageScore = coverageScore;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long iD) {
		ID = iD;
	}
	public java.lang.Long getPlanID() {
		return PlanID;
	}
	public void setPlanID(java.lang.Long planID) {
		PlanID = planID;
	}
	public java.lang.Double getSAAScore() {
		return SAAScore;
	}
	public void setSAAScore(java.lang.Double sAAScore) {
		SAAScore = sAAScore;
	}
	public java.lang.Double getTAAScore() {
		return TAAScore;
	}
	public void setTAAScore(java.lang.Double tAAScore) {
		TAAScore = tAAScore;
	}
	public java.util.Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}
	public java.lang.String getPlanName() {
		return PlanName;
	}
	public void setPlanName(java.lang.String planName) {
		PlanName = planName;
	}
	public java.lang.Integer getStatus() {
		return Status;
	}
	public void setStatus(java.lang.Integer status) {
		Status = status;
	}
	public java.lang.Double getFundQualityValue() {
		return FundQualityValue;
	}
	public void setFundQualityValue(java.lang.Double fundQualityValue) {
		FundQualityValue = fundQualityValue;
	}
	public java.lang.Double getCoverageValue() {
		return CoverageValue;
	}
	public void setCoverageValue(java.lang.Double coverageValue) {
		CoverageValue = coverageValue;
	}
	public java.lang.Double getCapabilityValue() {
		return CapabilityValue;
	}
	public void setCapabilityValue(java.lang.Double capabilityValue) {
		CapabilityValue = capabilityValue;
	}
	public java.lang.Double getInvestmentValue() {
		return InvestmentValue;
	}
	public void setInvestmentValue(java.lang.Double investmentValue) {
		this.InvestmentValue = investmentValue;
	}
	public java.lang.Integer getMajorAssetClass() {
		return MajorAssetClass;
	}
	public void setMajorAssetClass(java.lang.Integer majorAssetClass) {
		MajorAssetClass = majorAssetClass;
	}
}
