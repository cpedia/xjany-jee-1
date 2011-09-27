package com.lti.action.portfolio;

public class HoldingItemBean {
	protected java.lang.Double Share;
	protected java.lang.Boolean ReInvest = false;
	protected java.lang.String symbol;
	protected java.lang.Double Price;
	protected java.lang.Double Amount;

	public java.lang.Double getShare() {
		return Share;
	}

	public void setShare(java.lang.Double share) {
		Share = share;
	}

	public java.lang.Boolean getReInvest() {
		return ReInvest;
	}

	public void setReInvest(java.lang.Boolean reInvest) {
		ReInvest = reInvest;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public java.lang.Double getPrice() {
		return Price;
	}

	public void setPrice(java.lang.Double price) {
		Price = price;
	}

	public java.lang.Double getAmount() {
		return Amount;
	}

	public void setAmount(java.lang.Double amount) {
		Amount = amount;
	}
	
	

}