package com.lti.service.bo;

import java.io.Serializable;
import java.util.List;

import com.lti.service.bo.base.BaseMutualFundDailyBeta;

public class MutualFundDailyBeta extends BaseMutualFundDailyBeta implements Serializable {
	
	private List<String> betaList;
	
	private String RSquareString;

	public List<String> getBetaList() {
		return betaList;
	}

	public void setBetaList(List<String> betaList) {
		this.betaList = betaList;
	}

	public String getRSquareString() {
		return RSquareString;
	}

	public void setRSquareString(String squareString) {
		RSquareString = squareString;
	}

}
