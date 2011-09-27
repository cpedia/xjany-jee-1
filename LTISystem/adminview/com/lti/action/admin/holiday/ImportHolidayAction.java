package com.lti.action.admin.holiday;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.type.PaginationSupport;
import com.lti.util.LTIDownLoader;
import com.lti.util.UpLoadException;
import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class ImportHolidayAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ImportHolidayAction.class);
	
	private File uploadFile;
	
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
		
		SecurityManager securityManager=(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		
		
		try {
			dl.uploadHolidayAndSetTradingDate(fileName);
		} 
		catch(Exception e)
		{
			uploadFile.delete();
			this.addActionError(e.toString());
			return Action.INPUT;
		}
		
		uploadFile.delete();
		
		
		return Action.SUCCESS;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

}
