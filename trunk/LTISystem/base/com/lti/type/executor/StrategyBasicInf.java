package com.lti.type.executor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StrategyBasicInf implements Serializable{
	
	private static final long serialVersionUID = 1698730446866496984L;
	
	private long ID=0;
	
	private String Name="STATIC";
	
	private Map<String, String> Parameter=new HashMap<String, String>();
	
	private String AssetName;
	
	public String getAssetName() {
		return AssetName;
	}
	public void setAssetName(String assetName) {
		AssetName = assetName;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Map<String, String> getParameter() {
		return Parameter;
	}
	public void setParameter(Map<String, String> parameter) {
		Parameter = parameter;
	}
}
