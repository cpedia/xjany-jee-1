package com.lti.action.admin.holiday;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.type.PaginationSupport;
import com.lti.util.LTIDownLoader;
import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class ExportHolidayAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ExportHolidayAction.class);
	
	private String filename;
	
	public InputStream inputStream;
	

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void validate(){
		
		if(this.filename==null){
			addFieldError("filename","File name is not validate!");
			return;
		}else{
			try{
				File f=new File(filename);
			}catch(Exception e){
				addFieldError("filename","File name is not validate!");
				return;
			}
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		LTIDownLoader downloader = new LTIDownLoader();
		
		String systemPath;
		String sysPath = System.getenv("windir");
		if(!downloader.isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
		
		String file = systemPath+this.getFilename();
		
		downloader.exportSecurityToDoc(file);
		
		
		File f = new File(file);
		inputStream=new FileInputStream(f);
		
		return Action.SUCCESS;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}	

}
