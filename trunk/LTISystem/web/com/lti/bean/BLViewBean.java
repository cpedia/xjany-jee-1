package com.lti.bean;

import java.util.List;

import com.lti.type.BLItem;

public class BLViewBean {
	
	private List<BLItem> view;
	
	private Double targetValue;
	
	private Double myViewOnWeight;

	private String Name;
	
	private Integer viewID;


	public Double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Double targetValue) {
		this.targetValue = targetValue;
	}

	public Double getMyViewOnWeight() {
		return myViewOnWeight;
	}

	public void setMyViewOnWeight(Double myViewOnWeight) {
		this.myViewOnWeight = myViewOnWeight;
	}

	public List<BLItem> getView() {
		return view;
	}

	public void setView(List<BLItem> view) {
		this.view = view;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getViewID() {
		return viewID;
	}

	public void setViewID(Integer viewID) {
		this.viewID = viewID;
	}	

}
