package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseThirdParty implements Serializable{
	protected java.lang.Long ThirdPartyID;
	protected java.lang.String ThirdParty;
	protected java.lang.String Parameters;
	public java.lang.String getParameters() {
		return Parameters;
	}
	public void setParameters(java.lang.String parameters) {
		Parameters = parameters;
	}
	public java.lang.Long getThirdPartyID() {
		return ThirdPartyID;
	}
	public void setThirdPartyID(java.lang.Long thirdPartyID) {
		ThirdPartyID = thirdPartyID;
	}
	public java.lang.String getThirdParty() {
		return ThirdParty;
	}
	public void setThirdParty(java.lang.String thirdParty) {
		ThirdParty = thirdParty;
	}
	
}