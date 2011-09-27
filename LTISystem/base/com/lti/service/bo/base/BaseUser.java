package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseUser implements Serializable{

	protected java.lang.Long ID;
	protected java.lang.String UserName;
	protected java.lang.String Password;
	protected java.lang.Boolean Enabled;
	protected java.lang.String Authority;
	protected java.lang.String Address;
	protected java.lang.String QQ;
	protected java.lang.String Telephone;
	protected java.lang.String EMail;
	protected java.lang.String CharCode;
	protected java.lang.Integer PlanIDNum;
	protected java.lang.Integer strategyCount;
	protected java.lang.Integer portfolioCount;
	protected java.lang.Integer fundtableCount;
	protected java.lang.String firstName;
	protected java.lang.String lastName;
	protected java.lang.String addressState;
	protected java.lang.String addressCountry;
	protected java.lang.String addressCity;
	protected java.lang.String addressZip;
	protected java.lang.Long InviteCodeID;
	protected java.lang.String ThirdParty;
	
	public java.lang.String getThirdParty() {
		return ThirdParty;
	}
	public void setThirdParty(java.lang.String thirdParty) {
		ThirdParty = thirdParty;
	}
	public java.lang.Long  getInviteCodeID() {
		return InviteCodeID;
	}
	public void setInviteCodeID(java.lang.Long inviteCodeID) {
		InviteCodeID = inviteCodeID;
	}
	protected java.lang.String company;
	
	protected java.lang.String license;
	
	protected java.lang.String description;
	
	protected Date createdDate;
	
	protected String logo;
	
	
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.lang.String getCompany() {
		return company;
	}
	public void setCompany(java.lang.String company) {
		this.company = company;
	}
	public java.lang.String getLicense() {
		return license;
	}
	public void setLicense(java.lang.String license) {
		this.license = license;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.String getUserName() {
		return UserName;
	}
	public void setUserName(java.lang.String userName) {
		UserName = userName;
	}
	public java.lang.String getPassword() {
		return Password;
	}
	public void setPassword(java.lang.String password) {
		Password = password;
	}
	public java.lang.Boolean getEnabled() {
		return Enabled;
	}
	public void setEnabled(java.lang.Boolean enabled) {
		Enabled = enabled;
	}
	public java.lang.String getAuthority() {
		return Authority;
	}
	public void setAuthority(java.lang.String authority) {
		Authority = authority;
	}
	public java.lang.String getAddress() {
		return Address;
	}
	public void setAddress(java.lang.String address) {
		Address = address;
	}
	public java.lang.String getQQ() {
		return QQ;
	}
	public void setQQ(java.lang.String qq) {
		QQ = qq;
	}
	public java.lang.String getTelephone() {
		return Telephone;
	}
	public void setTelephone(java.lang.String telephone) {
		Telephone = telephone;
	}
	public java.lang.String getEMail() {
		return EMail;
	}
	public void setEMail(java.lang.String mail) {
		EMail = mail;
	}
	public java.lang.String getCharCode() {
		return CharCode;
	}
	public void setCharCode(java.lang.String charCode) {
		CharCode = charCode;
	}
	public java.lang.Integer getPlanIDNum() {
		return PlanIDNum;
	}
	public void setPlanIDNum(java.lang.Integer planIDNum) {
		PlanIDNum = planIDNum;
	}
	public java.lang.Integer getStrategyCount() {
		return strategyCount;
	}
	public void setStrategyCount(java.lang.Integer strategyCount) {
		this.strategyCount = strategyCount;
	}
	public java.lang.Integer getPortfolioCount() {
		return portfolioCount;
	}
	public void setPortfolioCount(java.lang.Integer portfolioCount) {
		this.portfolioCount = portfolioCount;
	}
	public java.lang.Integer getFundtableCount() {
		return fundtableCount;
	}
	public void setFundtableCount(java.lang.Integer fundtableCount) {
		this.fundtableCount = fundtableCount;
	}
	public java.lang.String getFirstName() {
		return firstName;
	}
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}
	public java.lang.String getLastName() {
		return lastName;
	}
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}
	public java.lang.String getAddressState() {
		return addressState;
	}
	public void setAddressState(java.lang.String addressState) {
		this.addressState = addressState;
	}
	public java.lang.String getAddressCountry() {
		return addressCountry;
	}
	public void setAddressCountry(java.lang.String addressCountry) {
		this.addressCountry = addressCountry;
	}
	public java.lang.String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(java.lang.String addressCity) {
		this.addressCity = addressCity;
	}
	public java.lang.String getAddressZip() {
		return addressZip;
	}
	public void setAddressZip(java.lang.String addressZip) {
		this.addressZip = addressZip;
	}
	
}