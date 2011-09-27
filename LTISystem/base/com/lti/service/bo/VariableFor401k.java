package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseVariableFor401k;

public class VariableFor401k extends BaseVariableFor401k implements Cloneable,Serializable {
	
	private static final long serialVersionUID = 1L;
	private String startDate = "";
	private String endDate = "";
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public VariableFor401k clone() throws CloneNotSupportedException {
		return (VariableFor401k) super.clone();
	}
	
}
