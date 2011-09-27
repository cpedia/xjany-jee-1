package com.lti.action.admin.validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.lti.action.Action;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class CheckMptLogAction extends ActionSupport implements Action {

    private static final long serialVersionUID = 1L;
    private String name;
	public InputStream inputStream;	

	public String execute() throws Exception {
	
		String log = Configuration.getTempDir()+this.getName()+"CheckMpt.log";
		
		File f = new File(log);
		inputStream=new FileInputStream(f);
		return Action.SUCCESS;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	public boolean isLinux()
	{
		String str = System.getProperty("os.name").toUpperCase();
		if(str.indexOf("WINDOWS") == -1)
		{
			return true;
		}
		return false;
	}

}
