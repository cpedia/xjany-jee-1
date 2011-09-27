package com.lti.action.admin.portfolio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.util.LTIDownLoader;
import com.lti.util.LTIFactorManager;
import com.opensymphony.xwork2.ActionSupport;

public class UploadExecutionBatchPlanFileAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private File uploadFile;
	private String uploadFileFileName;

	public void validate(){
		
	}
	
	public String execute() throws Exception {
		
		String systemPath = new LTIDownLoader().systemPath;

		String fileName = systemPath + "temp"+ this.uploadFileFileName;
		
		InputStream stream = new FileInputStream(uploadFile);
		OutputStream bos = new FileOutputStream(fileName);

		int bytesRead = 0;
		
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.flush();
		bos.close();
		stream.close();
		
		String oldFileName = systemPath + "Batch_PlanID.csv";
		File oldFile = new File(oldFileName);
		File newFile = new File(fileName);
		if(oldFile != null)
			oldFile.delete();
		newFile.renameTo(oldFile);
		return Action.SUCCESS;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
}
