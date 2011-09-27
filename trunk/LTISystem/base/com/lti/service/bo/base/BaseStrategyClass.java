package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseStrategyClass implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.String Name;

	protected java.lang.Long ParentID;

	protected java.lang.Long CategoryID;
	
	protected String OverView;
	
	protected String StrategyTableUp;
	
	protected String StrategyTableDown;
	
	protected String LatestCommons;
	
	public java.lang.Long getCategoryID() {
		return CategoryID;
	}
	public void setCategoryID(java.lang.Long categoryID) {
		CategoryID = categoryID;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.String getName() {
		return Name;
	}
	public void setName(java.lang.String name) {
		Name = name;
	}
	public java.lang.Long getParentID() {
		return ParentID;
	}
	public void setParentID(java.lang.Long parentID) {
		ParentID = parentID;
	}
	public String getOverView() {
		return OverView;
	}
	public void setOverView(String overView) {
		OverView = overView;
	}
	public String getStrategyTableUp() {
		return StrategyTableUp;
	}
	public void setStrategyTableUp(String strategyTableUp) {
		StrategyTableUp = strategyTableUp;
	}
	public String getStrategyTableDown() {
		return StrategyTableDown;
	}
	public void setStrategyTableDown(String strategyTableDown) {
		StrategyTableDown = strategyTableDown;
	}
	public String getLatestCommons() {
		return LatestCommons;
	}
	public void setLatestCommons(String latestCommons) {
		LatestCommons = latestCommons;
	}
}