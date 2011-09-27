package com.lti.action.admin.loaddata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import com.lti.action.Action;
import com.lti.util.StringUtil;
import com.lti.util.SystemEnvironment;

public class DownloadLogAction {

	private static final long serialVersionUID = 1L;
	
	public InputStream inputStream;
	
	public String logType;
	
	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() throws Exception {
		//File f=new File(StringUtil.getPath(new String[]{SystemEnvironment.getSysEnv("CATALINA_HOME"),name}));
		
		String systemPath;
		String sysPath = System.getenv("windir");
		if(!isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
				
		String log = systemPath+this.getName()+"downloadlog.html";
		
		if(logType.equalsIgnoreCase("EOD"))
			log = systemPath+this.getName()+"EoDlog.html";
		
		File f = new File(log);
		inputStream=new FileInputStream(f);
		return Action.SUCCESS;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
