package com.lti.service.bo.base;

import java.util.Date;

public class BaseUserTransaction implements java.io.Serializable {

	private static final long serialVersionUID = 34341L;
	private Long ID;
	private Long userID;
	private String lastName;
	private String firstName;
	private String addressName;
	private String addressCountryCode;
	private String addressState;
	private String addressCountry;
	private String addressCity;
	private String addressZip;
	private String addressStreet;
	private Double amountPerCycle;
	private Double amount;
	private String business;
	private String code;
	private String charset;
	private String subscrId;
	private Double shipping;
	private String receiverId;
	private String receiverEmail;
	private String residenceCountry;
	private String reasonCode;
	private String recurringPaymentId;
	private Double paymentGross;
	private Date paymentDate;
	private String paymentStatus;
	private String paymentType;
	private Double paymentFee;
	private String paymentCycle;
	private String periodType;
	private String payerEmail;
	private String payerId;
	private String payerStatus;
	private String protectionEligibility;
	private String parentTxnId;
	private String productName;
	private String itemName;
	private Double initialPaymentAmount;
	private Double mcFee;
	private String mcCurrency;
	private Double mcGross;
	private String notifyVersion;
	private Date nextPaymentDate;
	private String transactionSubject;
	private String testIpn;
	private String txnType;
	private String txnId;
	private Date timeCreated;
	private String verifySign;
	private Double outstandingBalance;
	private String period1;
	private String period3;

	public BaseUserTransaction() {

	}

	public BaseUserTransaction(Long userID, Date timeCreated,
			Double paymentGross) {
		this.userID = userID;
		this.timeCreated = timeCreated;
		this.paymentGross = paymentGross;
	}

	public String getPeriod1() {
		return period1;
	}

	public void setPeriod1(String period1) {
		this.period1 = period1;
	}

	public String getPeriod3() {
		return period3;
	}

	public void setPeriod3(String period3) {
		this.period3 = period3;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getAddressCountryCode() {
		return addressCountryCode;
	}

	public void setAddressCountryCode(String addressCountryCode) {
		this.addressCountryCode = addressCountryCode;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressZip() {
		return addressZip;
	}

	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public Double getAmountPerCycle() {
		return amountPerCycle;
	}

	public void setAmountPerCycle(Double amountPerCycle) {
		this.amountPerCycle = amountPerCycle;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSubscrId() {
		return subscrId;
	}

	public void setSubscrId(String subscrId) {
		this.subscrId = subscrId;
	}

	public Double getShipping() {
		return shipping;
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getResidenceCountry() {
		return residenceCountry;
	}

	public void setResidenceCountry(String residenceCountry) {
		this.residenceCountry = residenceCountry;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getRecurringPaymentId() {
		return recurringPaymentId;
	}

	public void setRecurringPaymentId(String recurringPaymentId) {
		this.recurringPaymentId = recurringPaymentId;
	}

	public Double getPaymentGross() {
		return paymentGross;
	}

	public void setPaymentGross(Double paymentGross) {
		this.paymentGross = paymentGross;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getPaymentFee() {
		return paymentFee;
	}

	public void setPaymentFee(Double paymentFee) {
		this.paymentFee = paymentFee;
	}

	public String getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public String getPayerStatus() {
		return payerStatus;
	}

	public void setPayerStatus(String payerStatus) {
		this.payerStatus = payerStatus;
	}

	public String getProtectionEligibility() {
		return protectionEligibility;
	}

	public void setProtectionEligibility(String protectionEligibility) {
		this.protectionEligibility = protectionEligibility;
	}

	public String getParentTxnId() {
		return parentTxnId;
	}

	public void setParentTxnId(String parentTxnId) {
		this.parentTxnId = parentTxnId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getInitialPaymentAmount() {
		return initialPaymentAmount;
	}

	public void setInitialPaymentAmount(Double initialPaymentAmount) {
		this.initialPaymentAmount = initialPaymentAmount;
	}

	public Double getMcFee() {
		return mcFee;
	}

	public void setMcFee(Double mcFee) {
		this.mcFee = mcFee;
	}

	public String getMcCurrency() {
		return mcCurrency;
	}

	public void setMcCurrency(String mcCurrency) {
		this.mcCurrency = mcCurrency;
	}

	public Double getMcGross() {
		return mcGross;
	}

	public void setMcGross(Double mcGross) {
		this.mcGross = mcGross;
	}

	public String getNotifyVersion() {
		return notifyVersion;
	}

	public void setNotifyVersion(String notifyVersion) {
		this.notifyVersion = notifyVersion;
	}

	public Date getNextPaymentDate() {
		return nextPaymentDate;
	}

	public void setNextPaymentDate(Date nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public String getTransactionSubject() {
		return transactionSubject;
	}

	public void setTransactionSubject(String transactionSubject) {
		this.transactionSubject = transactionSubject;
	}

	public String getTestIpn() {
		return testIpn;
	}

	public void setTestIpn(String testIpn) {
		this.testIpn = testIpn;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getVerifySign() {
		return verifySign;
	}

	public void setVerifySign(String verifySign) {
		this.verifySign = verifySign;
	}

	public Double getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(Double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

}