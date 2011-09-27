package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseLog;

public class Log extends BaseLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private String logDateStr;

	public String getLogDateStr() {
		return logDateStr;
	}

	public void setLogDateStr(String logDateStr) {
		this.logDateStr = logDateStr;
	}

}