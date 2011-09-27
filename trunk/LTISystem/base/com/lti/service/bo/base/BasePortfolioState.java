package com.lti.service.bo.base;

import java.io.Serializable;


public abstract class BasePortfolioState implements Serializable{

	private static final long serialVersionUID = 1L;
	protected java.lang.Long PortfolioID;
	protected java.lang.Integer State;
	protected java.util.Date UpdateTime;
	protected byte[] PersistentBytes;
	protected byte[] DelayChart;
	protected byte[] DelayPieChart;
	protected byte[] RealtimeChart;
	protected byte[] RealtimePieChart;
	public java.lang.Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(java.lang.Long portfolioID) {
		PortfolioID = portfolioID;
	}
	public java.lang.Integer getState() {
		return State;
	}
	public void setState(java.lang.Integer state) {
		State = state;
	}
	public java.util.Date getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		UpdateTime = updateTime;
	}
	public byte[] getPersistentBytes() {
		return PersistentBytes;
	}
	public void setPersistentBytes(byte[] persistentBytes) {
		PersistentBytes = persistentBytes;
	}
	public byte[] getDelayChart() {
		return DelayChart;
	}
	public void setDelayChart(byte[] delayChart) {
		DelayChart = delayChart;
	}
	public byte[] getDelayPieChart() {
		return DelayPieChart;
	}
	public void setDelayPieChart(byte[] delayPieChart) {
		DelayPieChart = delayPieChart;
	}
	public byte[] getRealtimeChart() {
		return RealtimeChart;
	}
	public void setRealtimeChart(byte[] realtimeChart) {
		RealtimeChart = realtimeChart;
	}
	public byte[] getRealtimePieChart() {
		return RealtimePieChart;
	}
	public void setRealtimePieChart(byte[] realtimePieChart) {
		RealtimePieChart = realtimePieChart;
	}

	
}