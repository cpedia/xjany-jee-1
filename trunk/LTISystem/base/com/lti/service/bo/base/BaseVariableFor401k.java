package com.lti.service.bo.base;

public abstract class BaseVariableFor401k {
	
	protected Long ID;
	private String symbol;
	private String name;
	private Long StrategyID;
	private String portfolioName;
	private Long portfolioID;
	private String assetClassName;
	private Integer redemption;
	private String description;
	private String memo;
	private Integer roundtripLimit = 60;
	private Integer waitingPeriod = 0;
	private Double quality=0.0;
	public Double getQuality() {
		return quality;
	}
	public void setQuality(Double quality) {
		this.quality = quality;
	}
	public String getSymbol() {
		return symbol;
	} 
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	} 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public Long getPortfolioID() {
		return portfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	public String getAssetClassName() {
		return assetClassName;
	}
	public void setAssetClassName(String assetClassName) {
		this.assetClassName = assetClassName;
	}
	public Integer getRedemption() {
		return redemption;
	}
	public void setRedemption(Integer redemption) {
		this.redemption = redemption;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public Long getStrategyID() {
		return StrategyID;
	}
	public void setStrategyID(Long strategyID) {
		StrategyID = strategyID;
	}
	public Integer getRoundtripLimit() {
		return roundtripLimit;
	}
	public void setRoundtripLimit(Integer roundtripLimit) {
		this.roundtripLimit = roundtripLimit;
	}
	public Integer getWaitingPeriod() {
		return waitingPeriod;
	}
	public void setWaitingPeriod(Integer waitingPeriod) {
		this.waitingPeriod = waitingPeriod;
	}

}
