package com.lti.action.admin.assetclass;

import java.io.File;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class UpdateBenchmarkAction extends ActionSupport implements Action {
	
	private File upload;
	private String uploadFileName;
	
	public String execute(){
		return Action.SUCCESS;
	}
	
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
}
