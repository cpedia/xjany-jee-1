package com.lti.service.bo.base;

import java.io.Serializable;


public abstract class BasePortfolioDailyData implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.Double Amount;

	protected java.util.Date Date;
	protected java.lang.Long PortfolioID;
	protected java.lang.Double Alpha;
	protected java.lang.Double Alpha1;
	protected java.lang.Double Alpha3;
	protected java.lang.Double Alpha5;
	protected java.lang.Double Beta;
	protected java.lang.Double Beta1;
	protected java.lang.Double Beta5;
	protected java.lang.Double Beta3;
	protected java.lang.Double AR;
	protected java.lang.Double AR1;
	protected java.lang.Double AR3;
	protected java.lang.Double AR5;	
	protected java.lang.Double RSquared;
	protected java.lang.Double RSquared1;
	protected java.lang.Double RSquared3;
	protected java.lang.Double RSquared5;
	protected java.lang.Double SharpeRatio;
	protected java.lang.Double SharpeRatio1;
	protected java.lang.Double SharpeRatio3;
	protected java.lang.Double SharpeRatio5;
	protected java.lang.Double StandardDeviation;
	protected java.lang.Double StandardDeviation1;
	protected java.lang.Double StandardDeviation3;
	protected java.lang.Double StandardDeviation5;
	protected java.lang.Double TreynorRatio;
	protected java.lang.Double TreynorRatio1;
	protected java.lang.Double TreynorRatio3;
	protected java.lang.Double TreynorRatio5;
	protected java.lang.Double DrawDown;
	protected java.lang.Double DrawDown1;
	protected java.lang.Double DrawDown3;
	protected java.lang.Double DrawDown5;
	
	protected java.lang.Double SortinoRatio;
	protected java.lang.Double SortinoRatio1;
	protected java.lang.Double SortinoRatio3;
	protected java.lang.Double SortinoRatio5;
	
	public java.lang.Double getRSquared() {
		return RSquared;
	}
	public void setRSquared(java.lang.Double squared) {
		RSquared = squared;
	}
	public java.lang.Double getRSquared1() {
		return RSquared1;
	}
	public void setRSquared1(java.lang.Double squared1) {
		RSquared1 = squared1;
	}	
	public java.lang.Double getRSquared3() {
		return RSquared3;
	}
	public void setRSquared3(java.lang.Double squared3) {
		RSquared3 = squared3;
	}
	public java.lang.Double getRSquared5() {
		return RSquared5;
	}
	public void setRSquared5(java.lang.Double squared5) {
		RSquared5 = squared5;
	}
	public java.lang.Double getSharpeRatio() {
		return SharpeRatio;
	}
	public void setSharpeRatio(java.lang.Double sharpeRatio) {
		SharpeRatio = sharpeRatio;
	}
	public java.lang.Double getSharpeRatio1() {
		return SharpeRatio1;
	}
	public void setSharpeRatio1(java.lang.Double sharpeRatio1) {
		SharpeRatio1 = sharpeRatio1;
	}
	public java.lang.Double getSharpeRatio3() {
		return SharpeRatio3;
	}
	public void setSharpeRatio3(java.lang.Double sharpeRatio3) {
		SharpeRatio3 = sharpeRatio3;
	}
	public java.lang.Double getSharpeRatio5() {
		return SharpeRatio5;
	}
	public void setSharpeRatio5(java.lang.Double sharpeRatio5) {
		SharpeRatio5 = sharpeRatio5;
	}
	public java.lang.Double getStandardDeviation() {
		return StandardDeviation;
	}
	public void setStandardDeviation(java.lang.Double standardDeviation) {
		StandardDeviation = standardDeviation;
	}
	public java.lang.Double getStandardDeviation1() {
		return StandardDeviation1;
	}
	public void setStandardDeviation1(java.lang.Double standardDeviation1) {
		StandardDeviation1 = standardDeviation1;
	}
	public java.lang.Double getStandardDeviation3() {
		return StandardDeviation3;
	}
	public void setStandardDeviation3(java.lang.Double standardDeviation3) {
		StandardDeviation3 = standardDeviation3;
	}
	public java.lang.Double getStandardDeviation5() {
		return StandardDeviation5;
	}
	public void setStandardDeviation5(java.lang.Double standardDeviation5) {
		StandardDeviation5 = standardDeviation5;
	}
	public java.lang.Double getTreynorRatio() {
		return TreynorRatio;
	}
	public void setTreynorRatio(java.lang.Double treynorRatio) {
		TreynorRatio = treynorRatio;
	}
	public java.lang.Double getTreynorRatio1() {
		return TreynorRatio1;
	}
	public void setTreynorRatio1(java.lang.Double treynorRatio1) {
		TreynorRatio1 = treynorRatio1;
	}
	public java.lang.Double getTreynorRatio3() {
		return TreynorRatio3;
	}
	public void setTreynorRatio3(java.lang.Double treynorRatio3) {
		TreynorRatio3 = treynorRatio3;
	}
	public java.lang.Double getTreynorRatio5() {
		return TreynorRatio5;
	}
	public void setTreynorRatio5(java.lang.Double treynorRatio5) {
		TreynorRatio5 = treynorRatio5;
	}
	public java.lang.Double getDrawDown() {
		return DrawDown;
	}
	public void setDrawDown(java.lang.Double drawDown) {
		DrawDown = drawDown;
	}
	public java.lang.Double getDrawDown1() {
		return DrawDown1;
	}
	public void setDrawDown1(java.lang.Double drawDown1) {
		DrawDown1 = drawDown1;
	}
	public java.lang.Double getDrawDown3() {
		return DrawDown3;
	}
	public void setDrawDown3(java.lang.Double drawDown3) {
		DrawDown3 = drawDown3;
	}
	public java.lang.Double getDrawDown5() {
		return DrawDown5;
	}
	public void setDrawDown5(java.lang.Double drawDown5) {
		DrawDown5 = drawDown5;
	}
	public java.lang.Double getAlpha() {
		return Alpha;
	}
	public void setAlpha(java.lang.Double alpha) {
		Alpha = alpha;
	}
	public java.lang.Double getAlpha1() {
		return Alpha1;
	}
	public void setAlpha1(java.lang.Double alpha1) {
		Alpha1 = alpha1;
	}
	public java.lang.Double getAlpha3() {
		return Alpha3;
	}
	public void setAlpha3(java.lang.Double alpha3) {
		Alpha3 = alpha3;
	}
	public java.lang.Double getAlpha5() {
		return Alpha5;
	}
	public void setAlpha5(java.lang.Double alpha5) {
		Alpha5 = alpha5;
	}
	public java.lang.Double getBeta() {
		return Beta;
	}
	public void setBeta(java.lang.Double beta) {
		Beta = beta;
	}
	public java.lang.Double getBeta1() {
		return Beta1;
	}
	public void setBeta1(java.lang.Double beta1) {
		Beta1 = beta1;
	}
	public java.lang.Double getBeta5() {
		return Beta5;
	}
	public void setBeta5(java.lang.Double beta5) {
		Beta5 = beta5;
	}
	public java.lang.Double getBeta3() {
		return Beta3;
	}
	public void setBeta3(java.lang.Double beta3) {
		Beta3 = beta3;
	}
	public java.lang.Double getAR() {
		return AR;
	}
	public void setAR(java.lang.Double ar) {
		AR = ar;
	}
	public java.lang.Double getAR1() {
		return AR1;
	}
	public void setAR1(java.lang.Double ar1) {
		AR1 = ar1;
	}
	public java.lang.Double getAR3() {
		return AR3;
	}
	public void setAR3(java.lang.Double ar3) {
		AR3 = ar3;
	}
	public java.lang.Double getAR5() {
		return AR5;
	}
	public void setAR5(java.lang.Double ar5) {
		AR5 = ar5;
	}
	public java.lang.Long getPortfolioID() {
		return PortfolioID;
	}
	public void setPortfolioID(java.lang.Long portfolioID) {
		PortfolioID = portfolioID;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}
	public java.lang.Double getAmount() {
		return Amount;
	}
	public void setAmount(java.lang.Double amount) {
		Amount = amount;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
	}
	public java.lang.Double getSortinoRatio() {
		return SortinoRatio;
	}
	public void setSortinoRatio(java.lang.Double SortinoRatio) {
		this.SortinoRatio = SortinoRatio;
	}
	public java.lang.Double getSortinoRatio1() {
		return SortinoRatio1;
	}
	public void setSortinoRatio1(java.lang.Double SortinoRatio1) {
		this.SortinoRatio1 = SortinoRatio1;
	}
	public java.lang.Double getSortinoRatio3() {
		return SortinoRatio3;
	}
	public void setSortinoRatio3(java.lang.Double SortinoRatio3) {
		this.SortinoRatio3 = SortinoRatio3;
	}
	public java.lang.Double getSortinoRatio5() {
		return SortinoRatio5;
	}
	public void setSortinoRatio5(java.lang.Double SortinoRatio5) {
		this.SortinoRatio5 = SortinoRatio5;
	}
}