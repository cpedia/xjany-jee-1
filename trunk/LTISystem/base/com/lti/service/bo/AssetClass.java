package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseAssetClass;

public  class AssetClass extends BaseAssetClass implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fullName;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	

}