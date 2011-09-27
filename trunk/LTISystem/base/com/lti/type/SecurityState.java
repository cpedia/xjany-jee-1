/**
 * 
 */
package com.lti.type;

import java.util.Date;

/**
 * @author CCD
 *
 */
public class SecurityState {
	private String symbol;
	private String endDate;
	private String updateTime;
	
	public SecurityState(){
		
	}
	public SecurityState(String symbol, String endDate, String updateTime) {
		this.symbol = symbol;
		this.endDate = endDate;
		this.updateTime = updateTime;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
