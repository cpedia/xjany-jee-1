package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseUserMarket implements Serializable{
	
	private String userFirstname;
	private String userLastname;
	private String userTelephone;
	private String userAddress;
	private String userCompany;
	private String userEmail;
	private String charcode;
	private String addressState;
	private String addressCountry;
	private String addressCity;
	private String groupKey1;
	private String groupKey2;
	private String groupKey3;
	private String groupKey4;
	private String groupKey5;
	protected Boolean isSend = true;
	private String subject;
	
	
	// Property accessors

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUserFirstname() {
		return this.userFirstname;
	}

	public void setUserFirstname(String userFirstname) {
		this.userFirstname = userFirstname;
	}

	public String getUserLastname() {
		return this.userLastname;
	}

	public void setUserLastname(String userLastname) {
		this.userLastname = userLastname;
	}

	public String getUserTelephone() {
		return this.userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

	public String getUserAddress() {
		return this.userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserCompany() {
		return this.userCompany;
	}

	public void setUserCompany(String userCompany) {
		this.userCompany = userCompany;
	}

	public String getCharcode() {
		return this.charcode;
	}

	public void setCharcode(String charcode) {
		this.charcode = charcode;
	}

	public String getAddressState() {
		return this.addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressCountry() {
		return this.addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getAddressCity() {
		return this.addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getGroupKey1() {
		return this.groupKey1;
	}

	public void setGroupKey1(String groupKey1) {
		this.groupKey1 = groupKey1;
	}

	public String getGroupKey2() {
		return this.groupKey2;
	}

	public void setGroupKey2(String groupKey2) {
		this.groupKey2 = groupKey2;
	}

	public String getGroupKey3() {
		return this.groupKey3;
	}

	public void setGroupKey3(String groupKey3) {
		this.groupKey3 = groupKey3;
	}

	public String getGroupKey4() {
		return this.groupKey4;
	}

	public void setGroupKey4(String groupKey4) {
		this.groupKey4 = groupKey4;
	}

	public String getGroupKey5() {
		return this.groupKey5;
	}

	public void setGroupKey5(String groupKey5) {
		this.groupKey5 = groupKey5;
	}

	public Boolean getIsSend() {
		return isSend;
	}

	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
