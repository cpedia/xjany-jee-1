package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseTransaction;
import com.lti.type.finance.ITransaction;
import com.lti.util.StringUtil;


public  class Transaction extends BaseTransaction implements Serializable, Cloneable, ITransaction {


	private static final long serialVersionUID = 1L;


	private java.lang.String strategyName;
	private java.lang.String securityName;
	private java.lang.String DateStr;
	
	private java.lang.String portfolioName;
	
	private java.lang.String description401k;
	private java.lang.String portfolioSymbol;
	
	private Double price;
	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public java.lang.String getPortfolioSymbol() {
		return portfolioSymbol;
	}

	public void setPortfolioSymbol(java.lang.String portfolioSymbol) {
		this.portfolioSymbol = portfolioSymbol;
	}

	public java.lang.String getStrategyName() {
		return strategyName;
	}
	
	public void setStrategyName(java.lang.String strategyName) {
		this.strategyName = strategyName;
	}
	
	public java.lang.String getSecurityName() {
		return securityName;
	}
	
	public void setSecurityName(java.lang.String securityName) {
		this.securityName = securityName;
	}

	public java.lang.String getDateStr() {
		return DateStr;
	}

	public void setDateStr(java.lang.String dateStr) {
		DateStr = dateStr;
	}

	public java.lang.String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(java.lang.String portfolioName) {
		this.portfolioName = portfolioName;
	}
	
	public Transaction clone(){
		try {
			return (Transaction)super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(StringUtil.getStackTraceString(e));
			//e.printStackTrace();
		}
		return null;
	}

	public java.lang.String getDescription401k() {
		return description401k;
	}

	public void setDescription401k(java.lang.String description401k) {
		this.description401k = description401k;
	}


}