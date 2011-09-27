package com.lti.action.admin.stock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadUpdateLogAction extends ActionSupport implements Action{
	
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(DownloadUpdateLogAction.class);
	
	public InputStream inputStream;
	
	private String date;
	
	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		DownloadUpdateLogAction.log = log;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String Date) {
		this.date = Date;
	}
	
	public String execute() throws Exception{
		if(date==null||date.equals(" "))return Action.INPUT;
		String systemPath;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String sysPath = System.getenv("windir");
		if(!Configuration.isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
		String path = systemPath+this.getDate()+"FinancialUpdateLog.html";	
		File f = new File(path);
		if(f.exists()){
			inputStream=new FileInputStream(f);
		}
		else{
			String path2 = systemPath+"NoFinancialLogNotice.html";
			File f2 = new File(path2.toString());
			FileOutputStream fos = new FileOutputStream(path2,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write("The date of download log:"+sdf.format(today)+"\t"+this.getDate()+"'s Financial Statement has not started updatting!There is no update's log!\t\n");	
			osw.close();
			fos.close();
			inputStream=new FileInputStream(f2);

		}
		return Action.SUCCESS;
	}


}

