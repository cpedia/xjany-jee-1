package com.lti.bean;

public class PortfolioItem {

	public final static String AR_LASTONEYEAR = "AR_1YR";
	public final static String AR_LASTTHREEYEAR = "AR_3YR";
	public final static String AR_LASTFIVEYEAR = "AR_5YR";

	public final static String SHARPE_LASTONEYEAR = "Sharpe_1YR";
	public final static String SHARPE_LASTTHREEYEAR = "Sharpe_3YR";
	public final static String SHARPE_LASTFIVEYEAR = "Sharpe_5YR";

	private Long ID;
	private String showName; // short name for showing
	private String Name;
	private String lastValidDate;
	private String lastTransactionDate;
	private String state;
	private String publicState;
	private Boolean deleteChoosed;
	private Boolean delayed;

	private java.lang.String AR1;
	private java.lang.String AR3;
	private java.lang.String AR5;

	private java.lang.String beta1;
	private java.lang.String beta3;
	private java.lang.String beta5;

	private java.lang.String sharpeRatio1;
	private java.lang.String sharpeRatio3;
	private java.lang.String sharpeRatio5;

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName() {
		this.showName = this.Name;
	}

	public void setLiveShowName() {
		this.showName = this.Name + " (Live)";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public java.lang.String getAR1() {
		return AR1;
	}

	public void setAR1(java.lang.String ar1) {
		AR1 = ar1;
	}

	public java.lang.String getAR3() {
		return AR3;
	}

	public void setAR3(java.lang.String ar3) {
		AR3 = ar3;
	}

	public java.lang.String getAR5() {
		return AR5;
	}

	public void setAR5(java.lang.String ar5) {
		AR5 = ar5;
	}

	public java.lang.String getBeta1() {
		return beta1;
	}

	public void setBeta1(java.lang.String beta1) {
		this.beta1 = beta1;
	}

	public java.lang.String getBeta3() {
		return beta3;
	}

	public void setBeta3(java.lang.String beta3) {
		this.beta3 = beta3;
	}

	public java.lang.String getBeta5() {
		return beta5;
	}

	public void setBeta5(java.lang.String beta5) {
		this.beta5 = beta5;
	}

	public java.lang.String getSharpeRatio1() {
		return sharpeRatio1;
	}

	public void setSharpeRatio1(java.lang.String sharpeRatio1) {
		this.sharpeRatio1 = sharpeRatio1;
	}

	public java.lang.String getSharpeRatio3() {
		return sharpeRatio3;
	}

	public void setSharpeRatio3(java.lang.String sharpeRatio3) {
		this.sharpeRatio3 = sharpeRatio3;
	}

	public java.lang.String getSharpeRatio5() {
		return sharpeRatio5;
	}

	public void setSharpeRatio5(java.lang.String sharpeRatio5) {
		this.sharpeRatio5 = sharpeRatio5;
	}

	public String getLastValidDate() {
		return lastValidDate;
	}

	public void setLastValidDate(String lastValidDate) {
		this.lastValidDate = lastValidDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPublicState() {
		return publicState;
	}

	public void setPublicState(String publicState) {
		this.publicState = publicState;
	}

	public String getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(String lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public Boolean getDeleteChoosed() {
		return deleteChoosed;
	}

	public void setDeleteChoosed(Boolean deleteChoosed) {
		this.deleteChoosed = deleteChoosed;
	}

	public Boolean getDelayed() {
		return delayed;
	}

	public void setDelayed(Boolean delayed) {
		this.delayed = delayed;
	}

}
