package com.lti.action.admin.loaddata;


import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

public class ExportEcnomicDataAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ExportEcnomicDataAction.class);
	
	private String filename;

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
		
		downloader.saveEIToDoc(filename);
		
		return Action.SUCCESS;
	}

	


	

}
