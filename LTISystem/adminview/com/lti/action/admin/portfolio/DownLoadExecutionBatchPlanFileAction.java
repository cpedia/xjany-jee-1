/**
 * 
 */
package com.lti.action.admin.portfolio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.lti.action.Action;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class DownLoadExecutionBatchPlanFileAction extends ActionSupport implements Action {
	
	private static final long serialVersionUID = 1L;
	
	public InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public String fileName;
	
	public String execute() throws Exception {
		
		String systemPath = new LTIDownLoader().systemPath;
		
		fileName = "Batch_PlanID.csv";
		String filePath = systemPath + fileName;
		
		File f = new File(filePath);
		inputStream=new FileInputStream(f);
		return Action.SUCCESS;
	}
}
