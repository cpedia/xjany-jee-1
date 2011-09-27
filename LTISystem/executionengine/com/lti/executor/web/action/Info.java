package com.lti.executor.web.action;

import com.lti.executor.web.BasePage;

public class Info extends BasePage{

	private String info;
	
	@Override
	public String execute() throws Exception {
		info="Welcome to validfi.com.";
		return "info.ftl";
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	
}
