package com.lti.service.bo;

import java.io.Serializable;

public class ConfidenceCheck implements Serializable{
	
	protected Long ID;

	protected Long PortfolioID;
	protected Long StrategyID;
	protected String RuleName;
	protected Long SampleSize;
	protected Double Mean;
	protected Double Variance;
	protected Double AboveMeanPossibility;

	protected Double MaxReturnUnderSampleProportion5;
	protected Double MaxReturnUnderSampleProportion10;
	protected Double MaxReturnUnderSampleProportion15;
	protected Double MaxReturnUnderSampleProportion20;
	protected Double MaxReturnUnderSampleProportion30;
	protected Double MaxReturnUnderSampleProportion40;
	protected Double MaxReturnUnderSampleProportion50;
	protected Double MaxReturnUnderSampleProportion60;
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		PortfolioID = portfolioID;
	}

	public Long getStrategyID() {
		return StrategyID;

	}

	public void setStrategyID(Long strategyID) {
		StrategyID = strategyID;

	}
	public String getRuleName() {
		return RuleName;
	}
	public void setRuleName(String ruleName) {
		RuleName = ruleName;
	}
	public Long getSampleSize() {
		return SampleSize;
	}
	public void setSampleSize(Long sampleSize) {
		SampleSize = sampleSize;
	}
	public Double getMean() {
		return Mean;
	}
	public void setMean(Double mean) {
		Mean = mean;
	}
	public Double getVariance() {
		return Variance;
	}
	public void setVariance(Double variance) {
		Variance = variance;
	}
	public Double getAboveMeanPossibility() {
		return AboveMeanPossibility;
	}
	public void setAboveMeanPossibility(Double aboveMeanPossibility) {
		AboveMeanPossibility = aboveMeanPossibility;
	}
	public Double getMaxReturnUnderSampleProportion5() {
		return MaxReturnUnderSampleProportion5;
	}
	public void setMaxReturnUnderSampleProportion5(
			Double maxReturnUnderSampleProportion5) {
		MaxReturnUnderSampleProportion5 = maxReturnUnderSampleProportion5;
	}
	public Double getMaxReturnUnderSampleProportion10() {
		return MaxReturnUnderSampleProportion10;
	}
	public void setMaxReturnUnderSampleProportion10(
			Double maxReturnUnderSampleProportion10) {
		MaxReturnUnderSampleProportion10 = maxReturnUnderSampleProportion10;
	}
	public Double getMaxReturnUnderSampleProportion15() {
		return MaxReturnUnderSampleProportion15;
	}
	public void setMaxReturnUnderSampleProportion15(
			Double maxReturnUnderSampleProportion15) {
		MaxReturnUnderSampleProportion15 = maxReturnUnderSampleProportion15;
	}
	public Double getMaxReturnUnderSampleProportion20() {
		return MaxReturnUnderSampleProportion20;
	}
	public void setMaxReturnUnderSampleProportion20(
			Double maxReturnUnderSampleProportion20) {
		MaxReturnUnderSampleProportion20 = maxReturnUnderSampleProportion20;
	}
	public Double getMaxReturnUnderSampleProportion30() {
		return MaxReturnUnderSampleProportion30;
	}
	public void setMaxReturnUnderSampleProportion30(
			Double maxReturnUnderSampleProportion30) {
		MaxReturnUnderSampleProportion30 = maxReturnUnderSampleProportion30;
	}
	public Double getMaxReturnUnderSampleProportion40() {
		return MaxReturnUnderSampleProportion40;
	}
	public void setMaxReturnUnderSampleProportion40(
			Double maxReturnUnderSampleProportion40) {
		MaxReturnUnderSampleProportion40 = maxReturnUnderSampleProportion40;
	}
	public Double getMaxReturnUnderSampleProportion50() {
		return MaxReturnUnderSampleProportion50;
	}
	public void setMaxReturnUnderSampleProportion50(
			Double maxReturnUnderSampleProportion50) {
		MaxReturnUnderSampleProportion50 = maxReturnUnderSampleProportion50;
	}
	public Double getMaxReturnUnderSampleProportion60() {
		return MaxReturnUnderSampleProportion60;
	}
	public void setMaxReturnUnderSampleProportion60(
			Double maxReturnUnderSampleProportion60) {
		MaxReturnUnderSampleProportion60 = maxReturnUnderSampleProportion60;
	}	
}
