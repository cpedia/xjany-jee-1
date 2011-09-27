/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public class BaseSecurityMPTIncData implements Serializable{
	
	protected java.lang.Long ID;
	
	protected java.lang.Long SecurityID;
	
	protected java.lang.Integer Year;
	
	protected java.lang.Double SigmaS;
	
	protected java.lang.Double SigmaSS;
	
	protected java.lang.Double SigmaB;
	
	protected java.lang.Double SigmaBB;
	
	protected java.lang.Double SigmaSB;
	
	protected java.lang.Double SigmaR;
	
	protected java.lang.Double SigmaLS;
	
	protected java.lang.Double SigmaLSS;
	
	protected java.lang.Double DrawDownHigh;
	
	protected java.util.Date DataLastDate;
	
	protected java.util.Date DataStartDate;
	
	protected java.lang.Integer Size;
	
	protected java.lang.Integer IntervalDays;
	
	protected java.lang.Double DrawDown;

	public java.lang.Long getSecurityID() {
		return SecurityID;
	}

	public void setSecurityID(java.lang.Long securityID) {
		SecurityID = securityID;
	}

	public java.lang.Integer getYear() {
		return Year;
	}

	public void setYear(java.lang.Integer year) {
		Year = year;
	}

	public java.lang.Double getSigmaS() {
		return SigmaS;
	}

	public void setSigmaS(java.lang.Double sigmaS) {
		SigmaS = sigmaS;
	}

	public java.lang.Double getSigmaSS() {
		return SigmaSS;
	}

	public void setSigmaSS(java.lang.Double sigmaSS) {
		SigmaSS = sigmaSS;
	}

	public java.lang.Double getSigmaB() {
		return SigmaB;
	}

	public void setSigmaB(java.lang.Double sigmaB) {
		SigmaB = sigmaB;
	}

	public java.lang.Double getSigmaBB() {
		return SigmaBB;
	}

	public void setSigmaBB(java.lang.Double sigmaBB) {
		SigmaBB = sigmaBB;
	}

	public java.lang.Double getSigmaSB() {
		return SigmaSB;
	}

	public void setSigmaSB(java.lang.Double sigmaSB) {
		SigmaSB = sigmaSB;
	}

	public java.lang.Double getSigmaR() {
		return SigmaR;
	}

	public void setSigmaR(java.lang.Double sigmaR) {
		SigmaR = sigmaR;
	}

	public java.lang.Double getSigmaLS() {
		return SigmaLS;
	}

	public void setSigmaLS(java.lang.Double sigmaLS) {
		SigmaLS = sigmaLS;
	}

	public java.lang.Double getSigmaLSS() {
		return SigmaLSS;
	}

	public void setSigmaLSS(java.lang.Double sigmaLSS) {
		SigmaLSS = sigmaLSS;
	}

	public java.lang.Double getDrawDownHigh() {
		return DrawDownHigh;
	}

	public void setDrawDownHigh(java.lang.Double drawDownHigh) {
		DrawDownHigh = drawDownHigh;
	}

	public java.util.Date getDataLastDate() {
		return DataLastDate;
	}

	public void setDataLastDate(java.util.Date dataLastDate) {
		DataLastDate = dataLastDate;
	}

	public java.lang.Integer getSize() {
		return Size;
	}

	public void setSize(java.lang.Integer size) {
		Size = size;
	}

	public java.lang.Integer getIntervalDays() {
		return IntervalDays;
	}

	public void setIntervalDays(java.lang.Integer intervalDays) {
		IntervalDays = intervalDays;
	}

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Double getDrawDown() {
		return DrawDown;
	}

	public void setDrawDown(java.lang.Double drawDown) {
		DrawDown = drawDown;
	}

	public java.util.Date getDataStartDate() {
		return DataStartDate;
	}

	public void setDataStartDate(java.util.Date dataStartDate) {
		DataStartDate = dataStartDate;
	}
}
