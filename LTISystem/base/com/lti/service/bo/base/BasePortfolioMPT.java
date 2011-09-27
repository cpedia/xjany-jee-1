package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.List;

public abstract class BasePortfolioMPT implements Serializable{
	protected Long ID;
	protected Long portfolioID;
	protected Double alpha;
	protected Double beta;
	protected Double AR;
	protected Double RSquared;
	protected Double sharpeRatio;
	protected Double standardDeviation;
	protected Double treynorRatio;
	protected Double drawDown;
	protected Long classID;
	protected String name;
	protected Long userID;
	protected Boolean isModelPortfolio;
	protected Long benchmarkID;
	protected Integer year;
	protected Long strategyID;
	protected Double sortinoRatio;
	
	protected List<com.lti.type.LongString> permissions;
	
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public Long getPortfolioID() {
		return portfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	public Double getAlpha() {
		return alpha;
	}
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}
	public Double getBeta() {
		return beta;
	}
	public void setBeta(Double beta) {
		this.beta = beta;
	}
	public Double getAR() {
		return AR;
	}
	public void setAR(Double ar) {
		AR = ar;
	}
	public Double getRSquared() {
		return RSquared;
	}
	public void setRSquared(Double squared) {
		RSquared = squared;
	}
	public Double getSharpeRatio() {
		return sharpeRatio;
	}
	public void setSharpeRatio(Double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}
	public Double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	public Double getTreynorRatio() {
		return treynorRatio;
	}
	public void setTreynorRatio(Double treynorRatio) {
		this.treynorRatio = treynorRatio;
	}
	public Double getDrawDown() {
		return drawDown;
	}
	public void setDrawDown(Double drawDown) {
		this.drawDown = drawDown;
	}
	public Long getClassID() {
		return classID;
	}
	public void setClassID(Long classID) {
		this.classID = classID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Boolean getIsModelPortfolio() {
		return isModelPortfolio;
	}
	public void setIsModelPortfolio(Boolean isModelPortfolio) {
		this.isModelPortfolio = isModelPortfolio;
	}
	public Long getBenchmarkID() {
		return benchmarkID;
	}
	public void setBenchmarkID(Long benchmarkID) {
		this.benchmarkID = benchmarkID;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Long getStrategyID() {
		return strategyID;
	}
	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}
	public List<com.lti.type.LongString> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<com.lti.type.LongString> permissions) {
		this.permissions = permissions;
	}
	public BasePortfolioMPT(Long portfolioID, Double alpha, Double beta, Double ar, Double squared, Double sharpeRatio, Double standardDeviation, Double treynorRatio, Double drawDown) {
		super();
		this.portfolioID = portfolioID;
		this.alpha = alpha;
		this.beta = beta;
		AR = ar;
		RSquared = squared;
		this.sharpeRatio = sharpeRatio;
		this.standardDeviation = standardDeviation;
		this.treynorRatio = treynorRatio;
		this.drawDown = drawDown;
	}

	public BasePortfolioMPT() {
		// TODO Auto-generated constructor stub
	}
	public Double getSortinoRatio() {
		return sortinoRatio;
	}
	public void setSortinoRatio(Double sortinoRatio) {
		this.sortinoRatio = sortinoRatio;
	}

}
