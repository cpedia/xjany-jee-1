package com.lti.action.admin.fundlist;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lti.action.Action;
import com.lti.action.admin.group.SaveAction;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.CSVReader;
import com.lti.util.Edgar11K;
import com.lti.util.FileOperator;
import com.opensymphony.xwork2.ActionSupport;

public class UploadAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
		
	private List<String> rList = new ArrayList<String>();
		
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
	
	public String execute(){
		 String fundContent = Edgar11K.extract11KFund(upload.getAbsolutePath(),fileName.substring(0, fileName.indexOf(".txt")),fileName.substring(0, fileName.indexOf(".txt")));
		
		if(fundContent != null && fundContent !=""){
			return Action.SUCCESS;
		}
		else{
			return Action.ERROR;
		}
	}
}
