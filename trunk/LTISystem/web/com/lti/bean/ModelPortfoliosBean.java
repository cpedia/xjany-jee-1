package com.lti.bean;

import java.util.List;

import com.lti.service.bo.CachePortfolioItem;

public class ModelPortfoliosBean {
	private Long strategyID;
	private String strategyName;
	private List<CachePortfolioItem> modelPortfolios;
	public Long getStrategyID() {
		return strategyID;
	}
	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public List<CachePortfolioItem> getModelPortfolios() {
		return modelPortfolios;
	}
	public void setModelPortfolios(List<CachePortfolioItem> modelPortfolios) {
		this.modelPortfolios = modelPortfolios;
	}
	
}
