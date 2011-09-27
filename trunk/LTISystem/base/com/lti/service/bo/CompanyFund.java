package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseCompanyFund;


public  class CompanyFund extends BaseCompanyFund implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return Ticker+"\n" + MSName + "\n" + MSLink + "\n" + Category;
	}
}