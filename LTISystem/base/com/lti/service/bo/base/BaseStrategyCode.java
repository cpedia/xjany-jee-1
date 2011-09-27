package com.lti.service.bo.base;

import java.io.Serializable;

import com.lti.type.executor.CodeInf;

public abstract class BaseStrategyCode implements Serializable{
	
	private static final long serialVersionUID = 1L;

	protected Long ID;
	
	protected Long StrategyID;
	
	protected java.util.Date Date;
	
	protected CodeInf Code;

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getStrategyID() {
		return StrategyID;
	}

	public void setStrategyID(Long strategyID) {
		StrategyID = strategyID;
	}

	public java.util.Date getDate() {
		return Date;
	}

	public void setDate(java.util.Date date) {
		Date = date;
	}

	public CodeInf getCode() {
		return Code;
	}

	public void setCode(CodeInf code) {
		Code = code;
	}
	
	
	
}
