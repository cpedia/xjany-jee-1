package com.lti.action.admin.loaddata;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

public class ImportEcnomicDatasAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ImportEcnomicDatasAction.class);
	
	private String filepath;

	public void validate(){
		
		if(this.filepath==null){
			addFieldError("filepath","File path file is not validate!");
			return;
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		LTIDownLoader dl = new LTIDownLoader();
		
		dl.loadEcomToDB(this.filepath);
		
		return Action.SUCCESS;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	


	

}
