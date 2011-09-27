package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseAllocationTemplate implements Serializable{
	private static final long serialVersionUID = 3205551994844734486L;
	
	protected Long ID;
	protected String TemplateName;
	protected String AssetClassName;
	protected String AssetClassWeight;
	protected String Description;
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getTemplateName() {
		return TemplateName;
	}
	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}
	public String getAssetClassName() {
		return AssetClassName;
	}
	public void setAssetClassName(String assetClassName) {
		AssetClassName = assetClassName;
	}
	public String getAssetClassWeight() {
		return AssetClassWeight;
	}
	public void setAssetClassWeight(String assetClassWeight) {
		AssetClassWeight = assetClassWeight;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
}
