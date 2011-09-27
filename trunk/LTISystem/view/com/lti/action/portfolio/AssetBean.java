package com.lti.action.portfolio;

import java.util.List;
import java.util.Map;


public class AssetBean {
	protected java.lang.String Name;
	protected double TargetPercentage;
	protected String AssetClassName;
	protected String AssetStrategy;
	protected Map<String, String> AssetStrategyParameter;
	protected String AssetStrategyParameterJSON;
	protected double Amount;
	protected double Price;
	
	List<HoldingItemBean> HoldingItems;

	public java.lang.String getName() {
		return Name;
	}

	public void setName(java.lang.String name) {
		Name = name;
	}

	public double getTargetPercentage() {
		return TargetPercentage;
	}

	public void setTargetPercentage(double targetPercentage) {
		TargetPercentage = targetPercentage;
	}

	public String getAssetClassName() {
		return AssetClassName;
	}

	public void setAssetClassName(String assetClassName) {
		AssetClassName = assetClassName;
	}

	public String getAssetStrategy() {
		return AssetStrategy;
	}

	public void setAssetStrategy(String assetStrategy) {
		AssetStrategy = assetStrategy;
	}

	public List<HoldingItemBean> getHoldingItems() {
		return HoldingItems;
	}

	public void setHoldingItems(List<HoldingItemBean> holdingItems) {
		HoldingItems = holdingItems;
	}

	public Map<String, String> getAssetStrategyParameter() {
		return AssetStrategyParameter;
	}

	public void setAssetStrategyParameter(Map<String, String> assetStrategyParameter) {
		AssetStrategyParameter = assetStrategyParameter;
	}

	public String getAssetStrategyParameterJSON() {
		return AssetStrategyParameterJSON;
	}

	public void setAssetStrategyParameterJSON(String assetStrategyParameterJSON) {
		AssetStrategyParameterJSON = assetStrategyParameterJSON;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	
}
