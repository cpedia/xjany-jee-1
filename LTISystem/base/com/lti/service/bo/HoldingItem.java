/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseHoldingItem;

/**
 * @author CCD
 *
 */
public class HoldingItem extends BaseHoldingItem implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private double close;
	
	private double open;
	
	private double dividend;
	
	/**
	 * 指明其在plan fund table所代表的fund
	 */
	private String description;
	
	private String portflolioSymbol;
	
	private String portfolioName;
	



	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getPortflolioSymbol() {
		return portflolioSymbol;
	}

	public void setPortflolioSymbol(String portflolioSymbol) {
		this.portflolioSymbol = portflolioSymbol;
	}

	public String getDescription() {
		return description;
	}
	
	private String fundDescription;

	public String getFundDescription() {
		return fundDescription;
	}

	public void setFundDescription(String fundDescription) {
		this.fundDescription = fundDescription;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HoldingItem clone(){
		HoldingItem o = null;
		try {
			o = (HoldingItem) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return o;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getOpen() {
		return open;
	}

	public double getDividend() {
		return dividend;
	}

	public void setDividend(double dividend) {
		this.dividend = dividend;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	private String CUSIP;

	public String getCUSIP() {
		return CUSIP;
	}

	public void setCUSIP(String cUSIP) {
		CUSIP = cUSIP;
	}
	
	
}
