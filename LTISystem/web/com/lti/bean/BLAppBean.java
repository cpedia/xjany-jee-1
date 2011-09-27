package com.lti.bean;

import java.util.List;

import com.lti.type.BLItem;


public class BLAppBean {
	List<BLItem> InvestList;
	
	Double RiskAversion;
	
	Integer Backward;
	
	Integer Forward;
	
	String RiskFreeSymbol;



	public Double getRiskAversion() {
		return RiskAversion;
	}

	public void setRiskAversion(Double riskAversion) {
		RiskAversion = riskAversion;
	}

	public Integer getBackward() {
		return Backward;
	}

	public void setBackward(Integer backward) {
		Backward = backward;
	}

	public Integer getForward() {
		return Forward;
	}

	public void setForward(Integer forward) {
		Forward = forward;
	}

	public String getRiskFreeSymbol() {
		return RiskFreeSymbol;
	}

	public void setRiskFreeSymbol(String riskFreeSymbol) {
		RiskFreeSymbol = riskFreeSymbol;
	}

	public List<BLItem> getInvestList() {
		return InvestList;
	}

	public void setInvestList(List<BLItem> investList) {
		InvestList = investList;
	}
}
