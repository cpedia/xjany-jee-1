package com.lti.action.admin.factors;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.lti.action.Action;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

public class DownloadFactorsFileAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private String type;
	
	public InputStream inputStream;
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() throws Exception {
		
		String systemPath = new LTIDownLoader().systemPath;
		
		String filename;
		if(type.equalsIgnoreCase("Style"))filename="style-factors.csv";
		else filename="RAA-factors.csv";
		String log = systemPath+filename;
		
		File f = new File(log);
		inputStream=new FileInputStream(f);
		return Action.SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
