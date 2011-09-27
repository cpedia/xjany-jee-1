/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public class BasePortfolioPerformance implements Serializable{
	protected static final long serialVersionUID = 1L;
	protected java.lang.Long ID;
	protected java.lang.Long PlanID;
	protected java.lang.Long PortfolioID;
	protected java.lang.Double ARRating;
	protected java.lang.Double SortinoRating;
	protected java.lang.Double SharpeRating;
	protected java.lang.Double TreynorRating;
	protected java.lang.Double DrawdownRating;
	protected java.lang.Double WinningRating;
	
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.Long getPlanID() {
		return PlanID;
	}
	public void setPlanID(java.lang.Long planID) {
		PlanID = planID;
	}
	public java.lang.Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(java.lang.Long portfolioID) {
		PortfolioID = portfolioID;
	}
	public java.lang.Double getARRating() {
		return ARRating;
	}
	public void setARRating(java.lang.Double rating) {
		ARRating = rating;
	}
	public java.lang.Double getSortinoRating() {
		return SortinoRating;
	}
	public void setSortinoRating(java.lang.Double sortinoRating) {
		SortinoRating = sortinoRating;
	}
	public java.lang.Double getSharpeRating() {
		return SharpeRating;
	}
	public void setSharpeRating(java.lang.Double sharpeRating) {
		SharpeRating = sharpeRating;
	}
	public java.lang.Double getTreynorRating() {
		return TreynorRating;
	}
	public void setTreynorRating(java.lang.Double treynorRating) {
		TreynorRating = treynorRating;
	}
	public java.lang.Double getDrawdownRating() {
		return DrawdownRating;
	}
	public void setDrawdownRating(java.lang.Double drawdownRating) {
		DrawdownRating = drawdownRating;
	}
	public java.lang.Double getWinningRating() {
		return WinningRating;
	}
	public void setWinningRating(java.lang.Double winningRating) {
		WinningRating = winningRating;
	}
	
}
