package com.lti.action.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;



import com.lti.action.Action;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class DownloadForUpdateAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;


	private String fileName;
	
	private String name;
	
	public InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() throws Exception {
		File f = new File(StringUtil.getPath(new String[] { ContextHolder.getServletPath() + "/" + Configuration.get("CLASS_ROOT_PATH"), fileName}));
		System.out.println("fileName: " + fileName);
		inputStream = new FileInputStream(f);
		name = f.getName();
		System.out.println("name: " +name);
		return Action.SUCCESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
