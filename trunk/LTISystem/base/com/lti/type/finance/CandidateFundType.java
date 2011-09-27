package com.lti.type.finance;

import java.io.Serializable;

import com.lti.service.bo.Security;

public class CandidateFundType implements Serializable{
	private static final long serialVersionUID = -8324097237682377495L;
	private String Symbol;
	private Security Security;
	private Security Benchmark;
	private int RedemptionLimit=0;
	private int WaitingPeriod=0;
	private int RoundtripLimit=13;
	private boolean InAfterDateFilter=false;
	private boolean TooVolatile=false;
	private boolean TooSmallVolume=false;
	private double score=Double.MIN_VALUE;
	
	private int index;
	
	private int Record=0;
	private int NoOfRoundtrip=0;
	private int Append;
	
	private CandidateFundType source=null;
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	private com.lti.service.bo.AssetClass AssetClass;
	
	private boolean ShortHistory=false;
	
	private boolean Chosen=false;
	
	public boolean isChosen() {
		return Chosen;
	}
	public void setChosen(boolean chosen) {
		Chosen = chosen;
	}
	public com.lti.service.bo.AssetClass getAssetClass() {
		return AssetClass;
	}
	public void setAssetClass(com.lti.service.bo.AssetClass assetClass) {
		AssetClass = assetClass;
	}
	public String getSymbol() {
		return Symbol;
	}
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}
	public Security getSecurity() {
		return Security;
	}
	public void setSecurity(Security security) {
		Security = security;
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
	public boolean isInAfterDateFilter() {
		return InAfterDateFilter;
	}
	public void setInAfterDateFilter(boolean inAfterDateFilter) {
		InAfterDateFilter = inAfterDateFilter;
	}
	public boolean isTooVolatile() {
		return TooVolatile;
	}
	public void setTooVolatile(boolean tooVolatile) {
		TooVolatile = tooVolatile;
	}
	public boolean isTooSmallVolume() {
		return TooSmallVolume;
	}
	public void setTooSmallVolume(boolean tooSmallVolume) {
		TooSmallVolume = tooSmallVolume;
	}
	public boolean isShortHistory() {
		return ShortHistory;
	}
	public void setShortHistory(boolean shortHistory) {
		ShortHistory = shortHistory;
	}
	public Security getBenchmark() {
		return Benchmark;
	}
	public void setBenchmark(Security benchmark) {
		Benchmark = benchmark;
	}
	public CandidateFundType getSource() {
		return source;
	}
	public void setSource(CandidateFundType source) {
		this.source = source;
	}
	public int getRecord() {
		return Record;
	}
	public void setRecord(int record) {
		Record = record;
	}
	public int getNoOfRoundtrip() {
		return NoOfRoundtrip;
	}
	public void setNoOfRoundtrip(int noOfRoundtrip) {
		NoOfRoundtrip = noOfRoundtrip;
	}
	public int getAppend() {
		return Append;
	}
	public void setAppend(int append) {
		Append = append;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
