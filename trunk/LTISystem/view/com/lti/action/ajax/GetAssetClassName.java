package com.lti.action.ajax;


import com.lti.action.Action;
import com.lti.service.AssetClassManager;

import com.opensymphony.xwork2.ActionSupport;

public class GetAssetClassName extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private String resultString;
	
	private Long AssetClassID;
	
	private AssetClassManager assetClassManager;

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public Long getAssetClassID() {
		return AssetClassID;
	}

	public void setAssetClassID(Long assetClassID) {
		AssetClassID = assetClassID;
	}
	
	public String execute(){
		resultString="";
		try {
			if(AssetClassID!=null){
				String assetClassName;
				assetClassName = assetClassManager.get(AssetClassID).getName();
				resultString = assetClassName;
			}
			else
				resultString = "UNKNOWN";
		} catch (Exception e) {
			// TODO: handle exception
			resultString = "UNKNOWN";
		}
		return Action.SUCCESS;
	}

}
