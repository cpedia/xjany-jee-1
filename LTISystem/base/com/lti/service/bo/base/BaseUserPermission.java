/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public class BaseUserPermission implements Serializable{

	private static final long serialVersionUID = -3051707312544144510L;
	
	protected java.lang.Long UserID;
	
	protected java.lang.Integer CurPortfolioRealTimeNum = 0;
	
	protected java.lang.Integer CurPortfolioFollowNum = 0;

	protected java.lang.Integer CurPlanCreateNum = 0;
	
	protected java.lang.Integer CurPlanRefNum = 0;
	
	protected java.lang.Integer CurPlanFundTableNum = 0;
	
	protected java.lang.Integer MaxPortfolioRealTimeNum = 0;
	
	protected java.lang.Integer MaxPortfolioFollowNum = 0;

	protected java.lang.Integer MaxPlanCreateNum = 0;
	
	protected java.lang.Integer MaxPlanRefNum = 0;
	
	protected java.lang.Integer MaxPlanFundTableNum = 0;
	
	protected java.lang.Integer CurConsumerPlanNum = 0;
	
	protected java.lang.Integer MaxConsumerPlanNum = 0;

	public java.lang.Long getUserID() {
		return UserID;
	}

	public void setUserID(java.lang.Long userID) {
		UserID = userID;
	}

	public java.lang.Integer getCurPortfolioRealTimeNum() {
		return CurPortfolioRealTimeNum;
	}

	public void setCurPortfolioRealTimeNum(java.lang.Integer curPortfolioRealTimeNum) {
		CurPortfolioRealTimeNum = curPortfolioRealTimeNum;
	}

	public java.lang.Integer getCurPortfolioFollowNum() {
		return CurPortfolioFollowNum;
	}

	public void setCurPortfolioFollowNum(java.lang.Integer curPortfolioFollowNum) {
		CurPortfolioFollowNum = curPortfolioFollowNum;
	}

	public java.lang.Integer getCurPlanCreateNum() {
		return CurPlanCreateNum;
	}

	public void setCurPlanCreateNum(java.lang.Integer curPlanCreateNum) {
		CurPlanCreateNum = curPlanCreateNum;
	}

	public java.lang.Integer getCurPlanRefNum() {
		return CurPlanRefNum;
	}

	public void setCurPlanRefNum(java.lang.Integer curPlanRefNum) {
		CurPlanRefNum = curPlanRefNum;
	}

	public java.lang.Integer getCurPlanFundTableNum() {
		return CurPlanFundTableNum;
	}

	public void setCurPlanFundTableNum(java.lang.Integer curPlanFundTableNum) {
		CurPlanFundTableNum = curPlanFundTableNum;
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

	public java.lang.Integer getCurConsumerPlanNum() {
		return CurConsumerPlanNum;
	}

	public void setCurConsumerPlanNum(java.lang.Integer curConsumerPlanNum) {
		CurConsumerPlanNum = curConsumerPlanNum;
	}

	public java.lang.Integer getMaxConsumerPlanNum() {
		return MaxConsumerPlanNum;
	}

	public void setMaxConsumerPlanNum(java.lang.Integer maxConsumerPlanNum) {
		MaxConsumerPlanNum = maxConsumerPlanNum;
	}
	
}
