package com.lti.action.admin.thirdparty;

import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.ThirdPartyResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class MainAction {
	
	private InputStream fis;
	private String fileName;
	private String thirdParty;
	public InputStream getFis() {
		return fis;
	}
	public void setFis(InputStream fis) {
		this.fis = fis;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String execute()throws Exception{
		fileName=thirdParty+"_resources.csv";
		StrategyManager sm=ContextHolder.getStrategyManager();
		
		List<ThirdPartyResource> trs=sm.getThirdPartyResources(thirdParty);
		if(trs!=null&&trs.size()>0){
			Writer w=new StringWriter();
			CsvListWriter clw=new CsvListWriter(w , CsvPreference.STANDARD_PREFERENCE);
			
			for(ThirdPartyResource tr:trs){
				List<String> list=new ArrayList<String>();
				list.add(tr.getThirdParty());
				list.add(tr.getResourceType()+"");
				list.add(tr.getResourceID()+"");
				clw.write(list);
			}
			clw.close();
			fis = new ByteArrayInputStream(w.toString().getBytes());
			return Action.DOWNLOAD;
		}else{
			message="no record";
			return Action.MESSAGE;
		}
		
	}
}
