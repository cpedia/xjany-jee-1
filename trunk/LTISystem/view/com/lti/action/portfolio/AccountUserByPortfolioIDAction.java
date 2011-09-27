package com.lti.action.portfolio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class AccountUserByPortfolioIDAction extends ActionSupport implements Action {
private static final long serialVersionUID = 1L;
	
	public InputStream inputStream;
	private String portfolioID;
	private String portfolioName;
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String execute() {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		getUserByPortfolioID();
		String dateStr = LTIDate.parseDateToString(new Date());
		String systemPath;
		Long longPortfolioID = Long.parseLong(this.getPortfolioID().replace(",", ""));
		portfolioName = portfolioManager.get(longPortfolioID).getName()+".csv";
		String sysPath = System.getenv("windir");
		if(!isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
		
		String fllowPortfolios = systemPath+"FllowPortfolios_"+longPortfolioID.toString()+"_"+dateStr+ ".csv";		
		File f = new File(fllowPortfolios);
		try {
			inputStream=new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	public boolean isLinux()
	{
		String str = System.getProperty("os.name").toUpperCase();
		if(str.indexOf("WINDOWS") == -1)
		{
			return true;
		}
		return false;
	}
	public void getUserByPortfolioID(){
		CsvListWriter clw = null;
		String systemPath;
		Long longPortfolioID = Long.parseLong(this.getPortfolioID().replace(",", ""));
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if(str.indexOf("WINDOWS") == -1)
			systemPath="/var/tmp/";
		else
			systemPath=sysPath+"\\temp\\";
		String dateStr = LTIDate.parseDateToString(new Date());
		String logFileName = systemPath+ "FllowPortfolios_"+longPortfolioID.toString()+"_"+dateStr+ ".csv";
		File file = new File(logFileName);
		try {
			clw = new CsvListWriter(new FileWriter(file,false), CsvPreference.EXCEL_PREFERENCE);
			String[] header = {"user_id","account_name","email"};
			clw.writeHeader(header);
		} catch (IOException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		List<UserResource> listur = new ArrayList<UserResource>();
		UserManager userManager = ContextHolder.getUserManager();
		List<String> strs = null;
		listur = userManager.getUserResourceByResourceIDAndResourceType(longPortfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		for(int i = 0;i<listur.size();i++){
			strs = new ArrayList<String>();
			strs.add(Long.toString(listur.get(i).getUserID()));
			strs.add(userManager.get(listur.get(i).getUserID()).getUserName());
			if(userManager.get(listur.get(i).getUserID()).getEMail()==null){
				strs.add("NULL");
			}else{
			strs.add(userManager.get(listur.get(i).getUserID()).getEMail());
			}
			try {
				clw.write(strs);
			} catch (IOException e) {
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}
		strs = new ArrayList<String>();
		strs.add("totalNum");
		strs.add(Integer.toString(listur.size()));
		try {
			clw.write(strs);
			clw.close();
		} catch (IOException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
	}
}
