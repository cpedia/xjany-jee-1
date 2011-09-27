package com.lti.service.bo;

import java.io.Serializable;
import java.util.Date;

import com.lti.service.bo.base.BaseUserTransaction;

public class UserTransaction extends BaseUserTransaction implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private String userName;

	public UserTransaction() {

	}

	public UserTransaction(Long userID, Date timeCreated, Double paymentGross) {
		super(userID, timeCreated, paymentGross);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
