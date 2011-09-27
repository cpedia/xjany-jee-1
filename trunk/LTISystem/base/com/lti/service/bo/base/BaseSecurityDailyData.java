package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Hashtable;

public abstract class BaseSecurityDailyData implements Serializable{

	protected java.lang.Long ID;

	// fields

	protected java.util.Date Date;
	
	
	protected java.lang.Double Split;
	protected java.lang.Double Dividend;

	/**
	 * 
	 */
	protected java.lang.Double EPS;

	protected java.lang.Double MarketCap;

	protected java.lang.Double PE;

	protected java.lang.Double Close;

	protected java.lang.Double Open;

	protected java.lang.Double High;

	protected java.lang.Double Low;

	protected java.lang.Double AdjClose;

	protected java.lang.Long Volume;

	protected java.lang.Double ReturnDividend;

	protected java.lang.Long SecurityID;

	protected java.lang.Double TurnoverRate;
	
	protected java.lang.Double NAV;
	
	protected java.lang.Double AdjNAV;
	//method
	
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
	public java.lang.Double getEPS() {
		return EPS;
	}
	public void setEPS(java.lang.Double eps) {
		EPS = eps;
	}
	public java.lang.Double getMarketCap() {
		return MarketCap;
	}
	public void setMarketCap(java.lang.Double marketCap) {
		MarketCap = marketCap;
	}
	public java.lang.Double getPE() {
		return PE;
	}
	public void setPE(java.lang.Double pe) {
		PE = pe;
	}

	public java.lang.Double getClose() {
		return Close;
	}
	public void setClose(java.lang.Double close) {
		Close = close;
	}
	public java.lang.Double getOpen() {
		return Open;
	}
	public void setOpen(java.lang.Double open) {
		Open = open;
	}
	public java.lang.Double getHigh() {
		return High;
	}
	public void setHigh(java.lang.Double high) {
		High = high;
	}
	public java.lang.Double getLow() {
		return Low;
	}
	public void setLow(java.lang.Double low) {
		Low = low;
	}

	public java.lang.Long getVolume() {
		return Volume;
	}
	public void setVolume(java.lang.Long volume) {
		Volume = volume;
	}
	public java.lang.Double getReturnDividend() {
		return ReturnDividend;
	}
	public void setReturnDividend(java.lang.Double returnDividend) {
		ReturnDividend = returnDividend;
	}
	public java.lang.Long getSecurityID() {
		return SecurityID;
	}
	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}
	public java.lang.Double getTurnoverRate() {
		return TurnoverRate;
	}
	public void setTurnoverRate(java.lang.Double turnoverRate) {
		TurnoverRate = turnoverRate;
	}
	public java.lang.Double getSplit() {
		return Split;
	}
	public void setSplit(java.lang.Double split) {
		Split = split;
	}
	public java.lang.Double getDividend() {
		return Dividend;
	}
	public void setDividend(java.lang.Double divident) {
		Dividend = divident;
	}
	public java.lang.Double getNAV() {
		return NAV;
	}
	public void setNAV(java.lang.Double nav) {
		NAV = nav;
	}
	public java.lang.Double getAdjNAV() {
		return AdjNAV;
	}
	public void setAdjNAV(java.lang.Double adjnav) {
		AdjNAV = adjnav;
	}
	public java.lang.Double getAdjClose() {
		return AdjClose;
	}
	public void setAdjClose(java.lang.Double adjClose) {
		AdjClose = adjClose;
	}
}