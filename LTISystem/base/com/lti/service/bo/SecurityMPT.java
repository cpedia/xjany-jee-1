package com.lti.service.bo;

import java.io.Serializable;
import java.util.ArrayList;

import com.lti.service.bo.base.BaseSecurityMPT;

public class SecurityMPT extends BaseSecurityMPT implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.Double discountRate;
	
	private java.lang.String assetClassName;
	
	private java.lang.String securityTypeName;
	
	private java.util.List<String> MPTStatistics;
	
	private java.util.Map<String, String> extraAttrs;
	
	private java.util.List<String> extras;
	
	public SecurityMPT(){
		super();
		MPTStatistics = new ArrayList<String>();
		extras = new ArrayList<String>();
	}

	public SecurityMPT(Long securityID, String securityName, String symbol,
			Integer securityType, Long assetClassID, Long year) {
		super(securityID, securityName, symbol, securityType, assetClassID, year);
		// TODO Auto-generated constructor stub
	}



	public java.lang.Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(java.lang.Double discountRate) {
		this.discountRate = discountRate;
	}

	public java.util.Map<String, String> getExtraAttrs() {
		return extraAttrs;
	}

	public void setExtraAttrs(java.util.Map<String, String> extraAttrs) {
		this.extraAttrs = extraAttrs;
	}

	public java.lang.String getAssetClassName() {
		return assetClassName;
	}

	public void setAssetClassName(java.lang.String assetClassName) {
		this.assetClassName = assetClassName;
	}

	public java.lang.String getSecurityTypeName() {
		return securityTypeName;
	}

	public void setSecurityTypeName(java.lang.String securityTypeName) {
		this.securityTypeName = securityTypeName;
	}

	public java.util.List<String> getMPTStatistics() {
		return MPTStatistics;
	}

	public void setMPTStatistics(java.util.List<String> statistics) {
		MPTStatistics = statistics;
	}

	public java.util.List<String> getExtras() {
		return extras;
	}

	public void setExtras(java.util.List<String> extras) {
		this.extras = extras;
	}
	
	public String toString()
	{
		return  SecurityID + " " + Symbol + " " + Year + "\nAlpha: " + Alpha +" Beta: " + Beta +
				" AR: " + AR + " StandardDeviation: " + StandardDeviation + "\nSharpeRatio: " + SharpeRatio +
				" TreynorRatio: " + TreynorRatio + " DrawDown: " + DrawDown + " Return: " + Return;
	}
}
