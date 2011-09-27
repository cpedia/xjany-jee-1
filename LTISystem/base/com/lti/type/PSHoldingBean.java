package com.lti.type;

public class PSHoldingBean {
	
	private String name;
	private String symbol;
	private Double shares;
	private Double costBasis;
	private Double mktValue;
	
	public Double getMktValue() {
		return mktValue;
	}
	public void setMktValue(Double mktValue) {
		this.mktValue = mktValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getShares() {
		return shares;
	}
	public void setShares(Double shares) {
		this.shares = shares;
	}
	public Double getCostBasis() {
		return costBasis;
	}
	public void setCostBasis(Double costBasis) {
		this.costBasis = costBasis;
	}

}
