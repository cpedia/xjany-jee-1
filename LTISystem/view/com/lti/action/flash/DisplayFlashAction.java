package com.lti.action.flash;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

public class DisplayFlashAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	
	private String chartName;
	
	private String lineNameArray;
	
	private String address;
	
	private String port;
	
	private String currentMode;
	
	private String dir;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		address = ServletActionContext.getRequest().getServerName();
		port = String.valueOf(ServletActionContext.getRequest().getServerPort());
		//System.out.print(address + "  " + port);
	}
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return Action.SUCCESS;
	}



	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getChartName() {
		return chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public String getLineNameArray() {
		return lineNameArray;
	}

	public void setLineNameArray(String lineNameArray) {
		this.lineNameArray = lineNameArray;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
	}

	
}
