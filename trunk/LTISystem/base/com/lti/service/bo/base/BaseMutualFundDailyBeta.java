package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseMutualFundDailyBeta implements Serializable{

	protected java.lang.Long ID;

	protected java.util.Date Date;
	
	protected java.lang.Double RSquare;
	
	protected java.lang.Double[] Betas;

	protected java.lang.Long MutualFundID;

	
	public java.lang.Long getMutualFundID() {
		return MutualFundID;
	}

	public void setMutualFundID(java.lang.Long mutualFundID) {
		MutualFundID = mutualFundID;
	}

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


	public java.lang.Double getRSquare() {
		return RSquare;
	}

	public void setRSquare(java.lang.Double square) {
		RSquare = square;
	}

	public java.lang.Double[] getBetas() {
		return Betas;
	}

	public void setBetas(java.lang.Double[] betas) {
		Betas = betas;
	}
	


}