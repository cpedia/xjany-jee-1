package com.lti.service.bo.base;

import java.util.Date;

import com.lti.service.bo.User;

public class BaseUserOperation implements java.io.Serializable {

	public static final String CONDITION_FINISH="finish";
	public static final String CONDITION_NEW="new";
	public static final int TYPE_PAYPAL=0;
	
	// Fields

	private Long id;
	private Long userID;
	private Date operationDate;
	private Integer operationType;
	private String optDescription;
	private String optCondition;

	// Constructors

	
	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOperationDate() {
		return this.operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public Integer getOperationType() {
		return this.operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getOptDescription() {
		return this.optDescription;
	}

	public void setOptDescription(String optDescription) {
		this.optDescription = optDescription;
	}

	public String getOptCondition() {
		return this.optCondition;
	}

	public void setOptCondition(String optCondition) {
		this.optCondition = optCondition;
	}


	public Long getUserID() {
		return userID;
	}


	public void setUserID(Long userID) {
		this.userID = userID;
	}
	
	

}