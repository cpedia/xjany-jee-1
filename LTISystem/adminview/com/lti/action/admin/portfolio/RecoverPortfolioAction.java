/**
 * 
 */
package com.lti.action.admin.portfolio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.lti.service.PortfolioManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author ccd
 *
 */
public class RecoverPortfolioAction extends ActionSupport implements Action {
	private File uploadFile;
	private String endDateStr;
	private PortfolioManager portfolioManager;
	private String resultString;
	@Override
	public String execute(){
		String message = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader(uploadFile));
			Date endDate = LTIDate.parseStringToDate(endDateStr);
			String line = br.readLine();
			
			while(line != null){
				try{
					String strID = line.split(",")[0];
					line = br.readLine();
					portfolioManager.recoverPortfolio(Long.parseLong(strID), endDate);
				}catch(Exception e){
					message += StringUtil.getStackTraceString(e);
				}
			}
		}catch(Exception e){
			resultString = "open file fail";
			return Action.ERROR;
		}
		resultString = "successful";
		resultString += message;
		return Action.SUCCESS;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}
	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
}
