package com.lti.type.finance;

import java.io.Serializable;

import com.lti.service.bo.Security;

public class FundAttr implements Serializable{
	private static final long serialVersionUID = 5499659686658929822L;
	private String symbol;
	private int RedemptionLimit;
	private int WaitingPeriod;
	private int RoundtripLimit;
	private int InAfterDateFilter;
	private int TooVolatile;
	private Security fund;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getRedemptionLimit() {
		return RedemptionLimit;
	}
	public void setRedemptionLimit(int redemptionLimit) {
		RedemptionLimit = redemptionLimit;
	}
	public int getWaitingPeriod() {
		return WaitingPeriod;
	}
	public void setWaitingPeriod(int waitingPeriod) {
		WaitingPeriod = waitingPeriod;
	}
	public int getRoundtripLimit() {
		return RoundtripLimit;
	}
	public void setRoundtripLimit(int roundtripLimit) {
		RoundtripLimit = roundtripLimit;
	}
	public int getInAfterDateFilter() {
		return InAfterDateFilter;
	}
	public void setInAfterDateFilter(int inAfterDateFilter) {
		InAfterDateFilter = inAfterDateFilter;
	}
	public int getTooVolatile() {
		return TooVolatile;
	}
	public void setTooVolatile(int tooVolatile) {
		TooVolatile = tooVolatile;
	}
	public Security getFund() {
		return fund;
	}
	public void setFund(Security fund) {
		this.fund = fund;
	}
	
}
