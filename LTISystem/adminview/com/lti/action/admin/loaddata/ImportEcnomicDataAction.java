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

public class ImportEcnomicDataAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ImportEcnomicDataAction.class);
	
	private File uploadFile;

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public void validate(){
		
		if(this.uploadFile==null){
			addFieldError("uploadFile","Upload file is not validate!");
			return;
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		LTIDownLoader dl = new LTIDownLoader();
		String filePath = dl.systemPath;
		

		InputStream stream = new FileInputStream(uploadFile);

		OutputStream bos = new FileOutputStream(filePath + uploadFile.getName());

		int bytesRead = 0;
		
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		stream.close();

		
		String fileName = filePath + uploadFile.getName();
		
		dl.batchLoadEcon(fileName);
		
		return Action.SUCCESS;
	}

	


	

}
