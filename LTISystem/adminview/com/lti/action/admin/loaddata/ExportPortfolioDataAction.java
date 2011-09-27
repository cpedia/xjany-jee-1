/**
 * 
 */
package com.lti.action.admin.loaddata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class ExportPortfolioDataAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ExportSecurityDataAction.class);
	
	private String portfolioFilename;
	
	public String getPortfolioFilename() {
		return portfolioFilename;
	}

	public void setPortfolioFilename(String portfolioFilename) {
		this.portfolioFilename = portfolioFilename;
	}

	private Integer updateMode;
	
	public Integer getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(Integer updateMode) {
		this.updateMode = updateMode;
	}

	public InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void validate(){
		if(updateMode == null)
			updateMode = 0;
		if(this.portfolioFilename==null){
			addFieldError("filename","File name is not validate!");
			return;
		}else{
			try{
				File f=new File(portfolioFilename);
			}catch(Exception e){
				addFieldError("filename","File name is not validate!");
				return;
			}
		}
		
	}
	
	@Deprecated
	@Override
	public String execute() throws Exception {
		
		LTIDownLoader downloader = new LTIDownLoader();
		
		String systemPath;
		String sysPath = System.getenv("windir");
		if(!downloader.isLinux())
			systemPath=sysPath+"\\temp\\";
		else 
			systemPath="/var/tmp/";
		
		String file = systemPath+this.getPortfolioFilename();
		//为？
		//downloader.savePortfolioUpdateModeToFile(file, updateMode);
		
		File f = new File(file);
		inputStream=new FileInputStream(f);
		
		return Action.SUCCESS;
	}

	
}
