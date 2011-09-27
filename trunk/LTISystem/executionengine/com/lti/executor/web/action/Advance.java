package com.lti.executor.web.action;

import com.lti.executor.web.BasePage;

/**
 * for uploading csv file
 * @author Michael Chua
 *
 */
public class Advance extends BasePage {

	
	
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private String ip;
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String execute() throws Exception {
		ip=request.getServerName();
		return "advance.ftl";
	}

}
