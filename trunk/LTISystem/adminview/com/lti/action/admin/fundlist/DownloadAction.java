package com.lti.action.admin.fundlist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class DownloadAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(DownloadAction.class);
	
	private String planName;

	private InputStream fis;
	
	public final static String rootPath = Configuration.get11KDir();
	
	public String execute() throws Exception{
		
		
		File root = new File(rootPath,"DataFile");
		
		if(!root.exists()){
			root.mkdirs();
		}
		File planFile = new File(root,planName);
		
		if(!planFile.exists()){
			planFile.createNewFile();
		}
		fis  =   new  FileInputStream(planFile);

		return Action.INPUT;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public InputStream getFis() {
		return fis;
	}

	public void setFis(InputStream fis) {
		this.fis = fis;
	}
	
	

}
