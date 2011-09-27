package com.lti.service.bo.base;

import java.io.Serializable;

import com.lti.type.finance.HoldingInf;

public abstract class BasePortfolioInf implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected Long ID;
	
	protected Long PortfolioID;
	
	protected Long Type;
	
	protected HoldingInf Holding;
	
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public Long getType() {
		return Type;
	}
	public void setType(Long type) {
		Type = type;
	}
	public Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		PortfolioID = portfolioID;
	}
	public HoldingInf getHolding() {
		return Holding;
	}
	public void setHolding(HoldingInf holding) {
		Holding = holding;
	}
	
	
}
