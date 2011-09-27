package com.lti.service.bo.base;

import java.util.Date;


public class BaseUserProfile implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String PAYMENT_STATUS_INACTIVE="inactive";
	public final static String PAYMENT_STATUS_NORMAL="normal";
	public final static String PAYMENT_STATUS_WAITING="waiting";
	public final static String PAYMENT_STATUS_EXPIRED="expired";
	public final static String PAYMENT_STATUS_LOCKED="locked";
	public final static String PAYMENT_STATUS_WAITING_FOR_CANCEL="waiting_for_cancel";
	public final static String PAYMENT_STATUS_CANCEL="cancel";
	private Long userID;
	private String payerEmail;
	private String subscrId;
	private String payerId;
	private Date paymentStartDate;
	private Date paymentEnddate;
	private String itemName;
	private String userStatus=PAYMENT_STATUS_INACTIVE;
	private String txnId;
	private Double amount;
	private String period1;
	private String period3;

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public Date getPaymentStartDate() {
		return paymentStartDate;
	}

	public void setPaymentStartDate(Date paymentStartDate) {
		this.paymentStartDate = paymentStartDate;
	}

	public Date getPaymentEnddate() {
		return paymentEnddate;
	}

	public void setPaymentEnddate(Date paymentEnddate) {
		this.paymentEnddate = paymentEnddate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getSubscrId() {
		return subscrId;
	}

	public void setSubscrId(String subscrId) {
		this.subscrId = subscrId;
	}

}