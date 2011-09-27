package com.lti.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.admin.group.SaveAction;
import com.lti.system.ContextHolder;
import com.lti.util.Edgar11K;
import com.opensymphony.xwork2.ActionSupport;

public class commonaction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
		
	private String message;
		
	private File upload;

	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

		public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	private boolean tinymce=false;

	private String url;
	
	public boolean isTinymce() {
		return tinymce;
	}

	public void setTinymce(boolean tinymce) {
		this.tinymce = tinymce;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	private boolean error;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String upload(){
		try {
			if(fileName.lastIndexOf('\\')!=-1){
				fileName=fileName.substring(fileName.lastIndexOf('\\')+1);
			}
			
			fileName=fileName.toLowerCase();
			if(fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith(".bmp")||fileName.endsWith(".gif")){
				String _file=System.currentTimeMillis()+fileName;
				FileUtils.copyFile(upload, new File(ContextHolder.getServletPath()+"/UserFiles/"+_file));
				upload.delete();
				url="UserFiles/"+_file;
			}
		} catch (Exception e) {
			url=e.getMessage();
			e.printStackTrace();
			error=true;
		}
		if(url==null){
			url="Only can upload jpeg,png,bmp,gif.";
			error=true;
		}
		return Action.SUCCESS;
	}
	
}
