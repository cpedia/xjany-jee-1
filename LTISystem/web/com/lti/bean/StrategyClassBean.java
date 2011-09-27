package com.lti.bean;

import java.util.List;

public class StrategyClassBean {
	String Name;
	
	Long ClassID;
	
	List<StrategyItem> Items;
	
	String portfolioName;

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<StrategyItem> getItems() {
		return Items;
	}

	public void setItems(List<StrategyItem> items) {
		Items = items;
	}

	public Long getClassID() {
		return ClassID;
	}

	public void setClassID(Long classID) {
		ClassID = classID;
	}
}
