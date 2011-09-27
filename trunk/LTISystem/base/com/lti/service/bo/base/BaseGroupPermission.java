/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 * 每个组用户的权限分配
 */
public class BaseGroupPermission implements Serializable{
	
	private static final long serialVersionUID = -7520307374202804562L;

	protected java.lang.Long GroupID;
	
	protected java.lang.Integer MaxPortfolioRealTimeNum;
	
	protected java.lang.Integer MaxPortfolioFollowNum;

	protected java.lang.Integer MaxPlanCreateNum;
	
	protected java.lang.Integer MaxPlanRefNum;
	
	protected java.lang.Integer MaxPlanFundTableNum;
	
	protected java.lang.Boolean FollowSAA;
	
	protected java.lang.Boolean FollowTAA;
	
	protected java.lang.Boolean FollowADV;
	
	protected java.lang.Boolean ChangeMHP;
	
	protected java.lang.Boolean CreatePlan;
	
	protected java.lang.Boolean PortfolioCompare;
	
	protected java.lang.Boolean PlanCompare;
	
	protected java.lang.Boolean PlanRollover;
	
	protected java.lang.Boolean CustomerReport;
	
	protected java.lang.Integer MaxConsumerPlanNum;

	public java.lang.Integer getMaxConsumerPlanNum() {
		return MaxConsumerPlanNum;
	}

	public void setMaxConsumerPlanNum(java.lang.Integer maxConsumerPlanNum) {
		MaxConsumerPlanNum = maxConsumerPlanNum;
	}

	public java.lang.Long getGroupID() {
		return GroupID;
	}

	public void setGroupID(java.lang.Long groupID) {
		GroupID = groupID;
	}

	public java.lang.Integer getMaxPortfolioRealTimeNum() {
		return MaxPortfolioRealTimeNum;
	}

	public void setMaxPortfolioRealTimeNum(java.lang.Integer maxPortfolioRealTimeNum) {
		MaxPortfolioRealTimeNum = maxPortfolioRealTimeNum;
	}

	public java.lang.Integer getMaxPortfolioFollowNum() {
		return MaxPortfolioFollowNum;
	}

	public void setMaxPortfolioFollowNum(java.lang.Integer maxPortfolioFollowNum) {
		MaxPortfolioFollowNum = maxPortfolioFollowNum;
	}

	public java.lang.Integer getMaxPlanCreateNum() {
		return MaxPlanCreateNum;
	}

	public void setMaxPlanCreateNum(java.lang.Integer maxPlanCreateNum) {
		MaxPlanCreateNum = maxPlanCreateNum;
	}

	public java.lang.Integer getMaxPlanRefNum() {
		return MaxPlanRefNum;
	}

	public void setMaxPlanRefNum(java.lang.Integer maxPlanRefNum) {
		MaxPlanRefNum = maxPlanRefNum;
	}

	public java.lang.Integer getMaxPlanFundTableNum() {
		return MaxPlanFundTableNum;
	}

	public void setMaxPlanFundTableNum(java.lang.Integer maxPlanFundTableNum) {
		MaxPlanFundTableNum = maxPlanFundTableNum;
	}

	public java.lang.Boolean getFollowSAA() {
		return FollowSAA;
	}

	public void setFollowSAA(java.lang.Boolean followSAA) {
		FollowSAA = followSAA;
	}

	public java.lang.Boolean getFollowTAA() {
		return FollowTAA;
	}

	public void setFollowTAA(java.lang.Boolean followTAA) {
		FollowTAA = followTAA;
	}

	public java.lang.Boolean getFollowADV() {
		return FollowADV;
	}

	public void setFollowADV(java.lang.Boolean followADV) {
		FollowADV = followADV;
	}

	public java.lang.Boolean getChangeMHP() {
		return ChangeMHP;
	}

	public void setChangeMHP(java.lang.Boolean changeMHP) {
		ChangeMHP = changeMHP;
	}

	public java.lang.Boolean getCreatePlan() {
		return CreatePlan;
	}

	public void setCreatePlan(java.lang.Boolean createPlan) {
		CreatePlan = createPlan;
	}

	public java.lang.Boolean getPortfolioCompare() {
		return PortfolioCompare;
	}

	public void setPortfolioCompare(java.lang.Boolean portfolioCompare) {
		PortfolioCompare = portfolioCompare;
	}

	public java.lang.Boolean getPlanCompare() {
		return PlanCompare;
	}

	public void setPlanCompare(java.lang.Boolean planCompare) {
		PlanCompare = planCompare;
	}

	public java.lang.Boolean getPlanRollover() {
		return PlanRollover;
	}

	public void setPlanRollover(java.lang.Boolean planRollover) {
		PlanRollover = planRollover;
	}

	public java.lang.Boolean getCustomerReport() {
		return CustomerReport;
	}

	public void setCustomerReport(java.lang.Boolean customerReport) {
		CustomerReport = customerReport;
	}
}
