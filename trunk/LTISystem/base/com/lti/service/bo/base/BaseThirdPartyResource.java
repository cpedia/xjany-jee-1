package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseThirdPartyResource implements Serializable{
	
	protected java.lang.Long ThirdPartyResourceID;
	
	protected java.lang.String ThirdParty;
	
	protected java.lang.Long ResourceID;
	
	protected java.lang.Integer ResourceType;

	public java.lang.Long getThirdPartyResourceID() {
		return ThirdPartyResourceID;
	}

	public void setThirdPartyResourceID(java.lang.Long thirdPartyResourceID) {
		ThirdPartyResourceID = thirdPartyResourceID;
	}

	public java.lang.String getThirdParty() {
		return ThirdParty;
	}

	public void setThirdParty(java.lang.String thirdParty) {
		ThirdParty = thirdParty;
	}

	public java.lang.Long getResourceID() {
		return ResourceID;
	}

	public void setResourceID(java.lang.Long resourceID) {
		ResourceID = resourceID;
	}

	public java.lang.Integer getResourceType() {
		return ResourceType;
	}

	public void setResourceType(java.lang.Integer resourceType) {
		ResourceType = resourceType;
	}
	

}
