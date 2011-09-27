/**
 * 
 */
package com.lti.action.admin.portfolio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class DownLoadUnSendPortfolioIDFileAction extends ActionSupport implements Action {
	
	private static final long serialVersionUID = 1L;
	
	public InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public String fileName;
	
	public String endDate;
	
	public String execute() throws Exception {
		UserManager userManager = ContextHolder.getUserManager();
		Date eDate = LTIDate.parseStringToDate(endDate);
		List<Long> portfolioIDList = userManager.getUnSendEmailPortfolioIDList(eDate);
		String systemPath = new LTIDownLoader().systemPath;
		
		fileName = "UnSendPortfolioID.csv";
		String filePath = systemPath + fileName;
		BufferedWriter buff = new BufferedWriter(new FileWriter(filePath));
		for (Long portfolioID : portfolioIDList)
			buff.write(portfolioID.toString()+"\n");
		buff.flush();
		buff.close();
		File f = new File(filePath);
		inputStream=new FileInputStream(f);
		return Action.SUCCESS;
	}
}
