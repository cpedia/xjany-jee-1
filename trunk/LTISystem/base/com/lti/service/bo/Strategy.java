package com.lti.service.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lti.service.bo.base.BaseStrategy;


public  class Strategy extends BaseStrategy  implements Serializable,Cloneable{

	public Strategy(Long id, String name) {
		super(id, name);
	}

	private static final long serialVersionUID = 1L;

	
	public Strategy(){
		super();
	}

	public Strategy(Long id, String name, Long userID) {
		super(id, name, userID);
	}
	public Strategy(Long id, String name, Long userID,Integer planType,Integer status,Date cr) {
		super(id, name, userID);
		this.CreatedDate=cr;
		this.PlanType=planType;
		this.Status=status;
		
	}
	public Strategy clone(){
		try {
			return (Strategy) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	private List<VariableFor401k> VariablesFor401k;


	public List<VariableFor401k> getVariablesFor401k() {
		return VariablesFor401k;
	}

	public void setVariablesFor401k(List<VariableFor401k> variablesFor401k) {
		VariablesFor401k = variablesFor401k;
	}
	

	

}